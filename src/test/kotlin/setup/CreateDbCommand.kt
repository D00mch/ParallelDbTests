package setup

import DbSettings
import java.sql.DriverManager

private val pid = ProcessHandle.current().pid()
private val newDbName = "${TestDbSettings.dbName}$pid"

/**
 * Creates a DB copy from [DbSettings.dbName] template
 *
 * @return DB name that was created
 */
fun createDb(settings: DbSettings = TestDbSettings): String = with(settings) {
    println("Creating DB with PID: $pid")

    val dbUrl = "jdbc:postgresql://$dbHost/postgres"
    DriverManager.getConnection(dbUrl, dbUser, dbPassword).use { conn ->

        // make the function idempotent
        val exists = conn
            .prepareStatement("SELECT FROM pg_database WHERE datname = '${newDbName}';")
            .executeQuery()
            .next()

        if (exists) {
            println("DB $newDbName already exists")
            return newDbName
        }

        // template db cannot be accessed by other connections
        conn.prepareStatement(
            """
            SELECT pg_terminate_backend(pg_stat_activity.pid)
            FROM pg_stat_activity
            WHERE pg_stat_activity.datname = '${dbName}'
            AND pid <> pg_backend_pid();
            """.trimIndent()
        ).execute()

        conn.prepareStatement(
            "CREATE DATABASE $newDbName WITH TEMPLATE $dbName OWNER ${dbUser};"
        ).execute()
        return newDbName
    }
}