package vn.com.vtcc.apiExpose.app

import java.util.Properties

import vn.com.vtcc.apiExpose.app.SparkJobRequestProcessingApplication.{hiveFactory, isRunning, listJobRequest, mysqlFactory, run, runSpark}
import vn.com.vtcc.apiExpose.dataSource.mysql.{MysqlConnectorFactory, SparkThriftConnectorFactory}
import vn.com.vtcc.apiExpose.entity.JobRequest
import vn.com.vtcc.apiExpose.utils.FileUtils

import scala.collection.mutable.ArrayBuffer

object ScheduleJobSparkApplication {

    var OUTPUT_FOLDER = "output/result"
    var mysqlFactory : MysqlConnectorFactory = null
    var hiveFactory : SparkThriftConnectorFactory = null
    @volatile var isRunning = false

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
            if (job_state.equals(JobState.WAITING)) {
                val jobRequest = new JobRequest(id, query, job_state, updated_time, created_time)
                list.+=(jobRequest)
            }
        }
        conn.close()
        list
    }

    def run(props: Properties): Unit = {
        isRunning = true
        mysqlFactory = new MysqlConnectorFactory(props)
        //TODO: create folder if not exists

        while (isRunning) {
            val jobs = listJobRequest()
            for (job <- jobs) {
                runSpark(job)
            }
            Thread.sleep(60000)
        }
    }

    def runSpark(job: JobRequest): Unit = {
        val tup = parseToSql(job.getQuery)
        val sql = tup._1
        val fields = tup._2
        val jobId = job.getId
        val conn = hiveFactory.createConnect()
        val statement = conn.createStatement
        val results = statement.executeQuery(sql)
        while (results.next()) {
            //TODO: write csv file
        }
        updateJobState(job, "")
    }

    def updateJobState(job: JobRequest, state: String): Unit = {

    }

    def csvWriter(): Unit = {

    }

    def parseToSql(query: String): (String, String) = {
        ("", "")
    }

    def close(): Unit = {
        isRunning = false
    }

    def main(args: Array[String]): Unit = {
        val path = args(0)
        val props = FileUtils.readPropertiesFile(path)
        run(props)
    }
}
