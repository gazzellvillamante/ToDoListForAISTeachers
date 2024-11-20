package com.assignment.todolistforaisteachers

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.android.gms.tasks.Task

class DatabaseHelper(val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Declaration of constant values of table name and columns
    companion object {
        private const val DATABASE_NAME = "ToDoList.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USERS = "users"
        private const val COLUMN_ID = "user_id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_CONFIRMPASSWORD = "confirmpassword"
        private const val TABLE_TASKS = "tasks"
        private const val COLUMN_TASKID = "task_id"
        private const val COLUMN_TASKUSERID = "user_id"
        private const val COLUMN_TASKNAME = "taskname"
        private const val COLUMN_TASKDESC = "taskdescription"
        private const val COLUMN_IS_COMPLETED =
            "is_completed"  // Assuming you want this column for completion status
    }

    private var loggedUser= getLoggedInUserId()

    // Function for users and tasks table creation
    override fun onCreate(db: SQLiteDatabase?) {
        // Query for users table creation
        val createUsersTableQuery = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
                $COLUMN_USERNAME TEXT, 
                $COLUMN_EMAIL TEXT, 
                $COLUMN_PASSWORD TEXT, 
                $COLUMN_CONFIRMPASSWORD TEXT
            )
        """

        // Query for tasks table creation (including is_completed column)
        val createTasksTableQuery = """
            CREATE TABLE $TABLE_TASKS (
                $COLUMN_TASKID INTEGER PRIMARY KEY AUTOINCREMENT, 
                $COLUMN_TASKNAME TEXT, 
                $COLUMN_TASKDESC TEXT,
                $COLUMN_IS_COMPLETED INTEGER DEFAULT 0,
                $COLUMN_TASKUSERID INTEGER,
                FOREIGN KEY($COLUMN_TASKUSERID) REFERENCES $TABLE_USERS($COLUMN_ID)
            )
        """

        // Executes query for table creation
        db?.execSQL(createUsersTableQuery)
        db?.execSQL(createTasksTableQuery)
    }

    @SuppressLint("Range")
    fun setLoginUser(email: String, password: String) {
        val db = writableDatabase
        val query = "SELECT $COLUMN_ID FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))

        if (cursor.moveToFirst()) {
            val userId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))

            // Store the user ID in SharedPreferences to persist across sessions
            val sharedPreferences = context.applicationContext.getSharedPreferences("user_prefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putInt("logged_in_user_id", userId)
            editor.apply()

        }
        cursor.close()
    }

    fun getLoggedInUserId(): Int {
        val sharedPreferences = context.applicationContext.getSharedPreferences("user_prefs", MODE_PRIVATE)
        return sharedPreferences.getInt("logged_in_user_id", -1) // -1 means no user is logged in
    }



    // Function to add users in the database
    fun addUser(username: String, email: String, password: String, confirmpassword: String): Long {

        // Holds the user details to be inserted into the database
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_CONFIRMPASSWORD, confirmpassword)
        }

        // Writable instance of the database for writing data
        val db = writableDatabase

        return db.insert(TABLE_USERS, null, values)
    }

    // Function to check email and password matches
    fun checkEmailPassword(email: String, password: String): Boolean {
        val db = readableDatabase
        val query = "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val queryArgs = arrayOf(email, password)
        val cursor = db.query(TABLE_USERS, null, query, queryArgs, null, null, null, null)

        val userExists = cursor.count > 0

        cursor.close()

        return userExists

    }

    // Function to check if a user already exists in the database
    fun checkUserExists(username: String, email: String): Boolean {
        val db = readableDatabase

        // Query to check if a user with the given username exists
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_EMAIL = ?"
        val cursor = db.rawQuery(query, arrayOf(username, email))

        // If cursor has any results, that means the user exists
        val userExists = cursor.count > 0

        // Close the cursor to avoid memory leaks
        cursor.close()

        return userExists
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Drop the tables if they exist
        val dropUsersTableQuery = "DROP TABLE IF EXISTS $TABLE_USERS"
        db?.execSQL(dropUsersTableQuery)

        val dropTasksTableQuery = "DROP TABLE IF EXISTS $TABLE_TASKS"
        db?.execSQL(dropTasksTableQuery)

//        val addEmailColumn = "ALTER TABLE $TABLE_USERS ADD COLUMN $COLUMN_EMAIL TEXT"
//        if(oldVersion < newVersion){
//            db?.execSQL(addEmailColumn)
//        }

        // Recreate Tables
        onCreate(db)
    }

    // Function to add task to the database
    fun addTask(taskItem: TaskItem) {
        val values = ContentValues().apply {
            put(COLUMN_TASKNAME, taskItem.name)
            put(COLUMN_TASKDESC, taskItem.desc)
            put(
                COLUMN_IS_COMPLETED,
                if (taskItem.isCompleted) 1 else 0
            )  // Assuming isCompleted is a boolean
            put(COLUMN_TASKUSERID, loggedUser)
        }

        val db = writableDatabase
        db.execSQL("PRAGMA foreign_keys = ON;")

        db.insert(TABLE_TASKS, null, values)
        db.close()
    }

    // Function to show all tasks from the database
    fun showTask(): MutableList<TaskItem> {
        val taskList = mutableListOf<TaskItem>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_TASKS WHERE $COLUMN_TASKUSERID = $loggedUser"
        val cursor = db.rawQuery(query, null)

        // Iterating through all rows to retrieve data
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASKID))
            val taskName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASKNAME))
            val taskDesc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASKDESC))
            val isCompleted =
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_COMPLETED)) == 1  // Convert to boolean
            val taskUserId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASKUSERID))
            // Create TaskItem instance
            val task = TaskItem(id, taskName, taskDesc, isCompleted, taskUserId)

            taskList.add(task)
        }

        cursor.close()

        db.close()

        return taskList
    }

    fun searchTask(searchString: String): MutableList<TaskItem> {
        val taskList = mutableListOf<TaskItem>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_TASKS WHERE $COLUMN_TASKNAME LIKE ?"
        val cursor = db.rawQuery(query, arrayOf("%$searchString%"))

        while(cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASKID))
            val taskName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASKNAME))
            val taskDesc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASKDESC))
            val isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_COMPLETED)) == 1
            val taskUserId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASKUSERID))

            val task = TaskItem(id, taskName, taskDesc, isCompleted, taskUserId)
            taskList.add(task)
        }

        cursor.close()
        db.close()

        return taskList

    }

    // Function to delete a task from the database
    fun deleteTask(taskId: Int): Int {
        val db = writableDatabase
        val whereClause = "$COLUMN_TASKID = ?"
        val whereArgs = arrayOf(taskId.toString())

        val rowsAffected = db.delete(TABLE_TASKS, whereClause, whereArgs)

        db.close()

        return rowsAffected
    }

    // Function to edit a task from the database
    fun editTask(taskItem: TaskItem) {
        val db = writableDatabase

        // Holds the values that will be updated in the database
        val values = ContentValues().apply {
            put(COLUMN_TASKNAME, taskItem.name)
            put(COLUMN_TASKDESC, taskItem.desc)
        }

        // Determines which row to update through task id
        val whereClause = "$COLUMN_TASKID = ?"
        val whereArgs = arrayOf(taskItem.id.toString())

        db.update(TABLE_TASKS, values, whereClause, whereArgs)

        db.close()

    }

    // Function to mark a task as completed or not completed in the database
    fun markAsCompleted(taskId: Int, isCompleted: Boolean) {
        val db = writableDatabase

        // Create ContentValues to hold the updated status
        val values = ContentValues().apply {
            put(COLUMN_IS_COMPLETED, if (isCompleted) 1 else 0)
        }

        // Determines which row to mark as complete through task id
        val whereClause = "$COLUMN_TASKID = ?"
        val whereArgs = arrayOf(taskId.toString())

        // Perform the update
        db.update(TABLE_TASKS, values, whereClause, whereArgs)

        db.close()
    }


}
