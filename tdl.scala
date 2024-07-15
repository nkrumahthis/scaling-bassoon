import scala.util.{Try, Success, Failure}
import java.io.{BufferedWriter, FileWriter, File}

class Database(val dbFilename: String):
    def insert(record: String): Try[Unit] = ???
    def selectAll(): Try[Seq[String]] = ???
    def delete(indexToDelete: Int): Try[Int] = ???

    // notice the dbFilename comes in through a side door
    /**
      * write to the file
      *
      * @param lines
      * @param append
      * @return
      */
    private def writeToFile(lines: Seq[String], append: Boolean): Try[Unit] =
        // I create this variable before the 'try' block because I need to
        // access it in the 'finally' block:
        var bw: BufferedWriter = null
        try 
            // this is basically standard Java code that you use to
            // create a BufferedWriter:
            bw = BufferedWriter(FileWriter(File(dbFilename), append))
            // here I loop over String in 'lines', and write each
            // String (one at a time) to the file. each String is
            // followed by a newline character:
            for line <- lines do bw.write(s"$line\n")
            // if the previous code does not throw an exception, flow
            // comes here, and we will return this Success value:
            Success(())
        catch
            // if the previous code DOES throw an excception, control
            // comes here and we will return a Failure:
            case e: Throwable => Failure(e)
        finally
            // the 'finally' block always executes, so this is where
            // we release/close resource end Database
            if bw != null then bw.close

@main
def ToDoList =
    val db = Database("./ToDoList.dat")