package vn.com.vtcc.apiExpose.app

import java.util.Properties

import org.apache.hadoop.fs.Path
import vn.com.vtcc.apiExpose.dataSource.mysql.MysqlConnectorFactory
import vn.com.vtcc.apiExpose.entity.JobRequest
import vn.com.vtcc.apiExpose.utils.{FileUtils, HdfsUtils}

import scala.collection.mutable.ArrayBuffer

object ScheduleHdfsPullDataApplication {

    var mysqlFactory : MysqlConnectorFactory = _
    var outputHdfsFolder : String = _
    var outputLocalFolder : String = _

    def listJobFileSave(): ArrayBuffer[JobRequest] = {
        val query = "select * from job_request where job_state = 'file_save'"
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
            val jobRequest = new JobRequest(id, query, job_state, retry, updated_time, created_time)
            list.+=(jobRequest)
        }
        conn.close()
        list
    }

    def run(props: Properties): Unit = {
        mysqlFactory = new MysqlConnectorFactory(props)
        outputHdfsFolder = props.getProperty("hdfs.output.result")
        outputLocalFolder = props.getProperty("local.output.result")
        val coreSitePath = props.getProperty("hdfs.core_site")
        val hdfsSitePath = props.getProperty("hdfs.hdfs_site")

        println("hdfs_site_path: " + hdfsSitePath)
        println("core_site_path: " +coreSitePath)

        val jobs = listJobFileSave()
        for (job <- jobs) {
            pullDataFromHdfs(job, coreSitePath, hdfsSitePath)
        }
    }

    def pullDataFromHdfs(job: JobRequest, coreSitePath: String, hdfsSitePath: String): Unit = {
        try {
            val fs = HdfsUtils.builder().setCoreSite(coreSitePath).setHdfsSite(hdfsSitePath).init()
            val hdfsPath = outputHdfsFolder + "/" + job.getId
            val localPath = outputLocalFolder + "/" + job.getId
            if (HdfsUtils.exists(hdfsPath, fs)) {
                fs.copyToLocalFile(new Path(hdfsPath), new Path(localPath))
            }
            updateJobState(job.getId, JobState.SUCCESS, job.getRetry)
            fs.close()
        } catch {
            case e: Exception => {
                e.printStackTrace()
            }
        }
    }

    def updateJobState(id: String, state: String, retry: Int): Unit = {
        var query = "update job_request set job_state = '{{state}}', retry = {{retry}}, updated_time = {{updated_time}} where id = '{{id}}'"
        query = query.replace("{{state}}", state)
            .replace("{{retry}}", retry.toString)
            .replace("{{updated_time}}", System.currentTimeMillis().toString)
            .replace("{{id}}", id)
        val conn = mysqlFactory.createConnect()
        val statement = conn.createStatement
        val results = statement.executeUpdate(query)
    }

    def main(args: Array[String]): Unit = {
        val path = args(0)
        val props = FileUtils.readPropertiesFile(path)
        run(props)
    }
}
