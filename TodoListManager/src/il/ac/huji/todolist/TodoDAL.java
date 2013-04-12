package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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
		
		Parse.initialize(context, "5ifLjXNAB2jNuwnTiyTFI5q02ZQ0v1HAswsLyfLd", "DXnVpuWZktMm5sgyxaGQwzvynIb43FABGJFhAlz7");
	}
	
	public void deleteDB() {
		context.deleteDatabase("todo_db");
//		clearParse();
	}
	
	private void clearParse() {
		ParseQuery allItemsQuery = new ParseQuery("todo");
		allItemsQuery.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> allItems, ParseException e) {
				if (e == null)
					for (ParseObject item : allItems)
						item.deleteInBackground();
			}
			
		});
	}

	public boolean insert(ITodoItem todoItem) {
		String title = todoItem.getTitle();
		Date dueDate = todoItem.getDueDate();
		long addedItemId = -1;
		
		if (title == null)
			return false;
		
		ContentValues newTodoItem = new ContentValues();
		ParseObject newTodoItemParse = new ParseObject("todo");
		
		newTodoItem.put("title", title);
		newTodoItemParse.put("title", title);
		
		if (dueDate == null) {
			newTodoItem.putNull("due");
			newTodoItemParse.put("due", JSONObject.NULL);
		}
		else
		{
			newTodoItem.put("due", dueDate.getTime());
			newTodoItemParse.put("due", dueDate.getTime());
		}
		
		newTodoItemParse.saveInBackground();
		addedItemId = todo_db.insert("todo", null, newTodoItem);
		
		return addedItemId > -1;
	}
	
	public boolean update(ITodoItem todoItem) {
		final String title = todoItem.getTitle();
		final Date dueDateToUpdate = todoItem.getDueDate();
		int numChangedRows = 0;
		
		// Exit on invalid item title.
		if (title == null)
			return false;
		
		// Create a ContentValue with the new fields for the SQLite update.
		ContentValues todoItemToUpdate = new ContentValues();
		
		// Create a PARSE query to find the Item to update.
		ParseQuery todoItemToUpdateParseQuery = new ParseQuery("todo");
		todoItemToUpdateParseQuery.whereEqualTo("title", title);
		
		// Execute the query and update found PARSE Item accordingly.
		todoItemToUpdateParseQuery.findInBackground(new FindCallback() {
			
			@Override
			public void done(List<ParseObject> foundItems, ParseException e) {
				if (e == null && foundItems != null && foundItems.size() > 0) {
					ParseObject todoItemToUpdateParse = foundItems.get(0);
					
					if (dueDateToUpdate == null)
						todoItemToUpdateParse.put("due", JSONObject.NULL);
					else
						todoItemToUpdateParse.put("due", dueDateToUpdate.getTime());
					
					todoItemToUpdateParse.saveInBackground();
				}
			}
		});
		
		// Update SQLite.
		if (dueDateToUpdate == null)
			todoItemToUpdate.putNull("due");
		else
			todoItemToUpdate.put("due", dueDateToUpdate.getTime());
		
		numChangedRows = todo_db.update("todo", todoItemToUpdate, "title=?", new String[] {title});
		return numChangedRows > 0;
	}
	
	//TODO: check what is the required behavior
	public boolean delete(ITodoItem todoItem) {
		String title = todoItem.getTitle();
		int numDeletedRows = 0;
		
		// Exit on invalid item title.
		if (title == null)
			return false;
		// Create a PARSE query to find the Item to delete.
		ParseQuery todoItemToDeleteParseQuery = new ParseQuery("todo");
		todoItemToDeleteParseQuery.whereEqualTo("title", title);
		
		// Execute the query and delete found PARSE Item accordingly.
		todoItemToDeleteParseQuery.findInBackground(new FindCallback() {
			
			@Override
			public void done(List<ParseObject> foundItems, ParseException e) {
				if (e == null && foundItems != null && foundItems.size() > 0) {
					ParseObject todoItemToDeleteParse = foundItems.get(0);
					todoItemToDeleteParse.deleteInBackground();
				}
			}
		});
		
		// Delete Item from SQLite.
		numDeletedRows = todo_db.delete("todo", "title=?", new String[] {title});
		
		return numDeletedRows > 0;
	}
	
	//TODO: synced with SQLite or PARSE
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
