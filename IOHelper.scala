import scala.util.Try
import scala.io.StdIn

object IOHelper:
    def promptUser(): Try[Unit] = Try {
        println("\n(Commands: a \"task\", d 1, u 1, h, q, v)")
        print("Yo: ")
    }

    def readInput(): Try[String] = Try {
        StdIn.readLine()
    }

    def showHelp(): Try[Unit] = Try {
        val text = """
        |   Possible commands
        |   ------------------
        |   a <task>        - add a to-do item
        |   h               - show this help text
        |   d [task number] - delete a task by its number
        |   v               - view the list of tasks
        |   q               - quit       
        """.stripMargin.trim
        println(text)
    }
end IOHelper