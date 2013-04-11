package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoDAL {
	
	private Context context;
	private SQLiteDatabase todo_db;
	private SQLiteOpenHelper todo_db_helper;
	
	public TodoDAL(Context context) {
		this.context = context;
		todo_db_helper = new TodoDBHelper(context);
		todo_db = todo_db_helper.getWritableDatabase();
	}
	
	public void deleteDB() {
		context.deleteDatabase("todo_db");
	}
	
	public boolean insert(ITodoItem todoItem) {
		String title = todoItem.getTitle();
		Date dueDate = todoItem.getDueDate();
		long addedItemId = -1;
		
		if (title == null)
			return false;
		
		ContentValues newTodoItem = new ContentValues();
		newTodoItem.put("title", title);
		if (dueDate == null)
			newTodoItem.putNull("due");
		else
			newTodoItem.put("due", dueDate.getTime());
		
		addedItemId = todo_db.insert("todo", null, newTodoItem);
		
		return addedItemId > -1;
	}
	
	public boolean update(ITodoItem todoItem) {
		String title = todoItem.getTitle();
		Date dueDate = todoItem.getDueDate();
		int numChangedRows = 0;
		
		if (title == null)
			return false;
		
		ContentValues todoItemToUpdate = new ContentValues();
		if (dueDate == null)
			todoItemToUpdate.putNull("due");
		else
			todoItemToUpdate.put("due", dueDate.getTime());
		
		numChangedRows = todo_db.update("todo", todoItemToUpdate, "title=?", new String[] {title});
		
		return numChangedRows > 0;
	}
	
	//TODO: check what is the required behavior
	public boolean delete(ITodoItem todoItem) {
		String title = todoItem.getTitle();
		int numDeletedRows = 0;
		
		if (title == null)
			return false;
		
		numDeletedRows = todo_db.delete("todo", "title=?", new String[] {title});
		
		return numDeletedRows > 0;
	}
	
	public List<ITodoItem> all() {
		List<ITodoItem> res = new ArrayList<ITodoItem>();		
		Cursor query = queryAllItems();
		int titleInd = query.getColumnIndex("title");
		int dueInd = query.getColumnIndex("due");
		
		//TODO: check if to return an empty list or null
		if (!query.moveToFirst())
			return res;
		
		do {
			String title = query.getString(titleInd);
			Date due = null;
			if (!query.isNull(dueInd))
				due = new Date(query.getLong(dueInd));
			res.add(new ListItem(title, due));
		} while (query.moveToNext());
		
		return res;
	}
	
	public Cursor queryAllItems() {
		String[] columns = {"title", "due"};
		return todo_db.query("todo", columns, null, null, null, null, null);
	}
}
