import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import setup.DB

class DbTest1 {

    @BeforeEach
    fun setup() {
        val con = DB.connection
        val truncateStatement = con.createStatement()
        truncateStatement.execute("TRUNCATE TABLE test RESTART IDENTITY")
        truncateStatement.close()
    }

    @Test
    fun test1() {
        val con = DB.connection

        val insertStatement = con.prepareStatement("INSERT INTO test (number) VALUES (?), (?)")
        insertStatement.setInt(1, 123)
        insertStatement.setInt(2, 456)
        insertStatement.executeUpdate()

        // Query the table to count the number of rows
        val queryStatement = con.prepareStatement("SELECT COUNT(*) AS rowcount FROM test")
        val resultSet = queryStatement.executeQuery()

        if (resultSet.next()) {
            val rowCount = resultSet.getInt("rowcount")
            assertEquals(2, rowCount, "There should be exactly 2 rows in the database")
        }

        // Close resources
        resultSet.close()
        queryStatement.close()
        insertStatement.close()
    }
}
