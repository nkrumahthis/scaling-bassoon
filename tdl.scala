import scala.util.{Try, Success, Failure}
import java.io.{BufferedWriter, FileWriter, File}
import scala.io.{Source, BufferedSource}
import IOHelper.*

class Database(val dbFilename: String):
    def insert(record: String): Try[Unit] =
        writeToFile(List(record), true)

    def selectAll(): Try[Seq[String]] = 
        var bufferedSource: BufferedSource = null
        try 
            bufferedSource = Source.fromFile(dbFilename)
            val lines = for line <- bufferedSource.getLines yield line
            Success(lines.toList)
        catch
            case t: Throwable => Failure(t)
        finally
            if bufferedSource != null then bufferedSource.close

    def delete(indexToDelete: Int): Try[Int] = 
        for 
            rows <- selectAll()
            newRows <- removeElementByIndex(rows, indexToDelete)
            numRowsDeleted = rows.size - newRows.size
            _ <- writeToFile(newRows, false)
        yield
            numRowsDeleted


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

    private def removeElementByIndex(rows: Seq[String], indexToDelete: Int): Try[Seq[String]] =
        try
            val result = rows.zipWithIndex.filter{case(_, idx) => idx != indexToDelete}.map(_._1)
            Success(result)
        catch
            case e: Throwable => Failure(e)

def mainLoop(): Try[Unit] = for{
    _ <- promptUser()
    } yield ()

@main
def ToDoList =
    val datafile = "./ToDoList.dat"
    val db = Database(datafile)
    // val procssor = InputProcessor(db)