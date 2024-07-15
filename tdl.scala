import scala.util.Try
class Database(val dbFilename: String):
    def insert(record: String): Try[Unit] = ???
    def selectAll(): Try[Seq[String]] = ???
    def delete(indexToDelete: Int): Try[Int] = ???
end Database

@main
def ToDoList =
    val db = Database("./ToDoList.dat")