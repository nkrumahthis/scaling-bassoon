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
    catch case t: Throwable => Failure(t)
    finally if bufferedSource != null then bufferedSource.close

  def delete(indexToDelete: Int): Try[Int] =
    for
      rows <- selectAll()
      newRows <- removeElementByIndex(rows, indexToDelete)
      numRowsDeleted = rows.size - newRows.size
      _ <- writeToFile(newRows, false)
    yield numRowsDeleted

  /** write to the file
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
    catch case e: Throwable => Failure(e)
    finally if bw != null then bw.close

  private def removeElementByIndex(
      rows: Seq[String],
      indexToDelete: Int
  ): Try[Seq[String]] =
    try
      val result = rows.zipWithIndex
        .filter { case (_, idx) => idx != indexToDelete }
        .map(_._1)
      Success(result)
    catch case e: Throwable => Failure(e)

class InputProcessor(db: Database):
  def handleView(): Try[Unit] = Try {
    // attempt to read lal the tasks from the database.
    // this function returns a list of strings, wrapped inside
    // a Try:
    val res: Try[Seq[String]] = db.selectAll()

    // handle the Success and Failure cases:
    res match
      case Success(tasks) =>
        // in the Success case we get a list of tasks (strings),
        // so print those out in a nice format:
        for 
          (task, count) <- tasks.zip(Stream from 1)
        do
          println(s"${count}. $task")
      case Failure(exception) => 
        System.err.println(exception)

  }

  def handleUserInput(input: String): Try[Unit] = input match
    case "q" =>
      Try(System.exit(0))
    case "h" =>
      IOHelper.showHelp()
    case "v" | "l" =>
      handleView()
    case add if add.startsWith("a ") =>
      ???
    case del if del.startsWith("d ") =>
      ???
    case _ =>
      ???
  end handleUserInput

end InputProcessor

@main
def ToDoList =
  val datafile = "./ToDoList.dat"
  val db = Database(datafile)
  val ip = InputProcessor(db)

  def mainLoop(): Try[Unit] = for {
    _ <- promptUser()
    input <- readInput()
    _ <- {
      ip.handleUserInput(input)
      mainLoop()
    }
  } yield ()

  // this starts the application running.
  mainLoop()

end ToDoList
