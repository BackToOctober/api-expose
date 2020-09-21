package vn.com.vtcc.apiExpose.dataSource.mysql

import java.sql.{Connection, DriverManager}
import java.util.Properties

class MysqlConnectorFactory(props: Properties) {

    var host : String = props.getProperty("mysql.host")
    var port : Int = props.getProperty("mysql.port").toInt
    var user : String = props.getProperty("mysql.user")
    var password : String = props.getProperty("mysql.password")
    var dbName : String = props.getProperty("mysql.db_name")

    def createConnect(): Connection = {
        val connectionUrl = "jdbc:mysql://" + host + ":" + port + "/" + dbName
        var connection: Connection = null
        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            connection = DriverManager.getConnection(connectionUrl, user, password)
        } catch {
            case e: Exception => e.printStackTrace()
        }
        connection
    }
}
