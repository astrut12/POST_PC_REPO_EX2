package il.ac.huji.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoDBHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String TODO_TABLE_NAME = "todo";
	private static final String TODO_TABLE_CREATE = 
			"CREATE TABLE " + TODO_TABLE_NAME + 
				" ( " + 
					"_id " + "integer primary key autoincrement, " +
					"title " + "TEXT, " +
					"due " + "LONG" +
				");";
	
	public TodoDBHelper(Context context) {
		super(context, "todo_db", null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TODO_TABLE_CREATE);	
	}
	
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
