package vn.com.vtcc.apiExpose.app

import java.util.Properties

import org.apache.log4j.{LogManager, Logger}
import org.apache.spark.sql.{Row, SparkSession}
import org.json.{JSONArray, JSONObject}
import vn.com.vtcc.apiExpose.utils.FileUtils

import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object ScheduleSchemaTableUpdateApplication {

    val logger: Logger = LogManager.getLogger(ScheduleSchemaTableUpdateApplication.getClass)

    var nameSpace : List[String] = _
    var database : String = "vtcc_dw"
    var folder : String = "config/metadata"

    def run(props: Properties): Unit = {
        nameSpace = props.getProperty("data_lake.name_space").split(",").toList

        logger.info(" >> name space = " + nameSpace)

        val spark = SparkSession.builder().getOrCreate()
        val tmpTables = spark.sql(s"show tables from $database").collectAsList()
        spark.sql(s"show tables from $database").show()

        val tables = new ListBuffer[String]()

        for (r <- tmpTables) {
            if (filterFunc(r)) {
                tables.+=(r.getString(1))
            }
        }

        logger.info(" >> table:" + tables)
        logger.info("-----------")

        for (table <- tables) {
            logger.info(table)
            val schema = spark.sql(s"describe table ${database}.${table}").collectAsList()
            spark.sql(s"describe table ${database}.${table}").show()

            val json = new JSONObject()
            json.put("article", table)
            val arr = new JSONArray()
            json.put("metadata", arr)
            for (r <- schema) {
                val sub = new JSONObject()
                sub.put("field", r.getString(0))
                sub.put("type", r.getString(1))
                arr.put(sub)
            }

            // 1. check file exist
            val filePath = folder + "/" + table +".json"
            if (FileUtils.exist(filePath)) {
                // 2. compare
                val json2 = FileUtils.readJsonFile(filePath)
                if (!compareMetadata(json, json2)) {
                    FileUtils.deleteFile(filePath)
                    // 3. write file to local
                    writeToLocal(json, filePath)
                }
            } else {
                writeToLocal(json, filePath)
            }
        }
    }

    def filterFunc(r: Row): Boolean = {
        for (n <- nameSpace) {
            if (r.getString(1).startsWith(n)) {
                return true
            }
        }
        false
    }

    def writeToLocal(json: JSONObject, filePath: String): Unit = {
        FileUtils.writeFile(json.toString(4), filePath)
    }

    def compareMetadata(json1: JSONObject, json2: JSONObject): Boolean = {
        if (json1.getString("article").equals(json2.getString("article"))) {
            val metadata1 = json1.getJSONArray("metadata")
            val metadata2 = json2.getJSONArray("metadata")
            val hm1 = new mutable.HashMap[String, String]()
            val hm2 = new mutable.HashMap[String, String]()
            for (mObj <- metadata1) {
                val s = mObj.asInstanceOf[JSONObject]
                hm1.+=(s.getString("field") -> s.getString("type"))
            }
            for (mObj <- metadata2) {
                val s = mObj.asInstanceOf[JSONObject]
                hm2.+=(s.getString("field") -> s.getString("type"))
            }
            return hm1.equals(hm2)
        }
        false
    }

    def main(args: Array[String]): Unit = {
        val path = args(0)
        val props = FileUtils.readPropertiesFile(path)
        run(props)
    }
}
