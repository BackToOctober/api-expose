package vn.com.vtcc.apiExpose.app

import java.util.Properties

import org.apache.log4j.LogManager
import org.apache.spark.sql.SparkSession
import org.json.JSONObject
import vn.com.vtcc.apiExpose.dataSource.mysql.{MysqlConnectorFactory, SparkThriftConnectorFactory}
import vn.com.vtcc.apiExpose.entity.JobRequest
import vn.com.vtcc.apiExpose.utils.FileUtils
import vn.com.vtcc.apiExpose.utils.transform.QueryParsing

import scala.collection.mutable.ArrayBuffer

object ScheduleJobSparkApplication {

    val logger = LogManager.getLogger(ScheduleJobSparkApplication.getClass)

    var outputHdfsFolder : String = _
    var metadataFolder : String = _
    var mysqlFactory : MysqlConnectorFactory = null

    var thresholdRetry : Int = 1

    def lockJobRequest(): Boolean = {
        true
    }

    def listJobRequest(): ArrayBuffer[JobRequest] = {
        val query = "select * from job_request"
        val conn = mysqlFactory.createConnect()
        val statement = conn.createStatement
        val results = statement.executeQuery(query)
        var list = new ArrayBuffer[JobRequest]()
        while (results.next()){
            val id = results.getString("id")
            val query = results.getString("query")
            val job_state = results.getString("job_state")
            val updated_time = results.getLong("updated_time")
            val created_time = results.getLong("created_time")
            val retry = results.getInt("retry")
            if (job_state.equals(JobState.WAITING) || (job_state.equals(JobState.FAIL) && retry < thresholdRetry)) {
                val jobRequest = new JobRequest(id, query, job_state, retry, updated_time, created_time)
                list.+=(jobRequest)
            }
        }
        conn.close()
        list
    }

    def run(props: Properties): Unit = {
        mysqlFactory = new MysqlConnectorFactory(props)
        thresholdRetry = props.getProperty("job.retry").toInt
        metadataFolder = props.getProperty("config.metadata")
        outputHdfsFolder = props.getProperty("hdfs.output.result")
        val jobs = listJobRequest()
        for (job <- jobs) {
            try {
                runSpark(job)
            } catch {
                case e: Exception => e.printStackTrace()
            }
        }
    }

    def runSpark(job: JobRequest): Unit = {
        val sql = parseToSql(job.getQuery)
        val jobId = job.getId
        try {
            updateJobState(jobId, JobState.RUNNING, job.getRetry)
            val spark = SparkSession.builder().getOrCreate()
            val df = spark.sql(sql)
            val path = outputHdfsFolder + "/" + jobId
            df.write.option("header", "true").option("delimiter", ",").csv(path)
            updateJobState(jobId, JobState.FILE_SAVE, job.getRetry + 1)
        } catch {
            case e: Exception => {
                updateJobState(jobId, JobState.FAIL, job.getRetry + 1)
            }
        }
    }

    def updateJobState(id: String, state: String, retry: Int): Unit = {
        val query = "update job_request set job_state = {{state}} and retry = {{retry}} and updated_time = {{updated_time}} where id = {{id}}"
        val conn = mysqlFactory.createConnect()
        val statement = conn.createStatement
        val results = statement.executeQuery(query.replace("{{state}}", state)
                    .replace("{{retry}}", retry.toString)
                    .replace("{{updated_time}}", System.currentTimeMillis().toString)
                    .replace("{{id}}", id))
    }

    def parseToSql(query: String): String= {
        val json = new JSONObject(query)
        val t1 = QueryParsing.parse(json.getJSONObject("query").toString, metadataFolder)
        logger.info("--> query: " + t1)
        t1
    }

    def main(args: Array[String]): Unit = {
        val path = args(0)
        val props = FileUtils.readPropertiesFile(path)
        run(props)
    }
}
