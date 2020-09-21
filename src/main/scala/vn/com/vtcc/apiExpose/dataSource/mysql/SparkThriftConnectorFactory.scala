package vn.com.vtcc.apiExpose.dataSource.mysql

import java.sql.{Connection, DriverManager}
import java.util.Properties

class SparkThriftConnectorFactory(props: Properties) {
    var host : String = props.getProperty("hive.host")
    var port : Int = props.getProperty("hive.port").toInt
    var dbName : String = props.getProperty("hive.db_name")

    def createConnect(): Connection = {
        val connectionUrl = "jdbc:hive2://" + host + ":" + port + "/" + dbName
        var connection: Connection = null
        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver")
            connection = DriverManager.getConnection(connectionUrl, "", "")
        } catch {
            case e: Exception => e.printStackTrace()
        }
        connection
    }
}
