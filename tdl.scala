class Database(val dbFilename: String):
    // methods go here
end Database

@main
def ToDoList =
    val db = Database("./ToDoList.dat")