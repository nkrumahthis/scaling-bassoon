import scala.util.{Try, Success, Failure}
import java.io.{BufferedWriter, FileWriter, File}

class Database(val dbFilename: String):
    def insert(record: String): Try[Unit] = ???
    def selectAll(): Try[Seq[String]] = ???
    def delete(indexToDelete: Int): Try[Int] = ???

    /**
      * write to the file
      *
      * @param lines
      * @param append
      * @return
      */
    private def writeToFile(lines: Seq[String], append: Boolean): Try[Unit] =
        var bw: BufferedWriter = null
        try 
            bw = BufferedWriter(FileWriter(File(dbFilename), append))
            for line <- lines do bw.write(s"$line\n")
            Success(())
        catch
            case e: Throwable => Failure(e)
        finally
            if bw != null then bw.close

@main
def ToDoList =
    val db = Database("./ToDoList.dat")