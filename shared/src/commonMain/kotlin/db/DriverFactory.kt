import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import com.tarokanizer.Database
import com.tarokanizer.RoundEntity

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

val listOfIntsAdapter = object : ColumnAdapter<List<Int>, String> {
    override fun decode(value: String) =
        if (value.isEmpty()) {
            listOf()
        } else {
            value.split(",").map { it.toInt() }
        }
    override fun encode(value: List<Int>): String {
        return value.joinToString(separator = ",")
    }
}

fun createDatabase(driverFactory: DriverFactory): Database {
    val driver = driverFactory.createDriver()
    return Database(
        driver = driver,
        RoundEntityAdapter = RoundEntity.Adapter(
            pointsAdapter = listOfIntsAdapter,
        )
    )
}
