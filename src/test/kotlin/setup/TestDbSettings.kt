package setup

import DbSettings

object TestDbSettings : DbSettings {
    override val dbHost = "localhost:5432"
    override val dbName = "parallel_tests"
    override val dbUser = "parallel_tests"
    override val dbPassword = "parallel_tests"
}