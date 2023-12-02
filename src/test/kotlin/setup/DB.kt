package setup

import java.sql.Connection
import java.sql.DriverManager

object DB {
    private val dbName = createDb(TestDbSettings)
    private val url = "jdbc:postgresql://${TestDbSettings.dbHost}/$dbName"

    val connection: Connection = DriverManager
        .getConnection(url, TestDbSettings.dbUser, TestDbSettings.dbPassword)
}