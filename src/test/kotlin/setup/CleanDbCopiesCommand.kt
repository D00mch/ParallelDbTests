package setup

import java.sql.DriverManager

fun main() = with(TestDbSettings) {
    val dbUrl = "jdbc:postgresql://$dbHost/postgres"
    DriverManager.getConnection(dbUrl, dbUser, dbPassword).use { conn ->
        try {
            conn.prepareStatement(
                """
                SELECT pg_terminate_backend(pg_stat_activity.pid)
                FROM pg_stat_activity WHERE pg_stat_activity.datname = '$dbName'
            """.trimIndent()
            ).execute()
        } catch (e: java.lang.Exception) {
            println("Can't close running connections")
        }

        val stmt = conn.createStatement()
        val commandName = "command"
        val rs = stmt.executeQuery(
            """
            SELECT 'DROP DATABASE IF EXISTS ' || quote_ident(datname) || ';' as $commandName
            FROM pg_database 
            WHERE datname ~ '^$dbName[0-9]+${'$'}';
        """.trimIndent()
        )

        while (rs.next()) {
            val command = rs.getString(commandName)
            try {
                conn.createStatement().use { statement ->
                    statement.execute(command)
                    println("Executed: $command")
                }
            } catch (e: Exception) {
                println("Error executing command: $command. Error: ${e.message}")
            }
        }
        rs.close()
        stmt.close()
    }
}