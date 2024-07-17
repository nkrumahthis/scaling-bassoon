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
end IOHelper