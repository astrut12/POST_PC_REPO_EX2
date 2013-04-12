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
import com.parse.ParseUser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoDAL {
	
	private Context context;
	private SQLiteDatabase todo_db;
	private SQLiteOpenHelper todo_db_helper;
	
	private static String TABLE_NAME;
	private static String TITLE_COLUMN;
	private static String DUE_COLUMN;
	private static String DB_NAME;
	
	public TodoDAL(Context context) {
		this.context = context;
		todo_db_helper = new TodoDBHelper(context);
		todo_db = todo_db_helper.getWritableDatabase();
		TABLE_NAME = context.getString(R.string.SQL_todo_table);
		TITLE_COLUMN = context.getString(R.string.SQL_title_column);
		DUE_COLUMN = context.getString(R.string.SQL_due_column);
		DB_NAME = context.getString(R.string.SQL_todo_db);
		
		Parse.initialize(context, context.getString(R.string.parseApplication), context.getString(R.string.clientKey));
		ParseUser.enableAutomaticUser();
	}
	
	public void deleteDB() {
		context.deleteDatabase(DB_NAME);
//		clearParse();
	}
	
	private void clearParse() {
		ParseQuery allItemsQuery = new ParseQuery(TABLE_NAME);
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
		ParseObject newTodoItemParse = new ParseObject(TABLE_NAME);
		
		newTodoItem.put(TITLE_COLUMN, title);
		newTodoItemParse.put(TITLE_COLUMN, title);
		
		if (dueDate == null) {
			newTodoItem.putNull(DUE_COLUMN);
			newTodoItemParse.put(DUE_COLUMN, JSONObject.NULL);
		}
		else
		{
			newTodoItem.put(DUE_COLUMN, dueDate.getTime());
			newTodoItemParse.put(DUE_COLUMN, dueDate.getTime());
		}
		
		newTodoItemParse.saveInBackground();
		addedItemId = todo_db.insert(TABLE_NAME, null, newTodoItem);
		
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
		ParseQuery todoItemToUpdateParseQuery = new ParseQuery(TABLE_NAME);
		todoItemToUpdateParseQuery.whereEqualTo(TITLE_COLUMN, title);
		
		// Execute the query and update found PARSE Item accordingly.
		todoItemToUpdateParseQuery.findInBackground(new FindCallback() {
			
			@Override
			public void done(List<ParseObject> foundItems, ParseException e) {
				if (e == null && foundItems != null && foundItems.size() > 0) {
					ParseObject todoItemToUpdateParse = foundItems.get(0);
					
					if (dueDateToUpdate == null)
						todoItemToUpdateParse.put(DUE_COLUMN, JSONObject.NULL);
					else
						todoItemToUpdateParse.put(DUE_COLUMN, dueDateToUpdate.getTime());
					
					todoItemToUpdateParse.saveInBackground();
				}
			}
		});
		
		// Update SQLite.
		if (dueDateToUpdate == null)
			todoItemToUpdate.putNull(DUE_COLUMN);
		else
			todoItemToUpdate.put(DUE_COLUMN, dueDateToUpdate.getTime());
		
		numChangedRows = todo_db.update(TABLE_NAME, todoItemToUpdate, "title=?", new String[] {title});
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
		ParseQuery todoItemToDeleteParseQuery = new ParseQuery(TABLE_NAME);
		todoItemToDeleteParseQuery.whereEqualTo(TITLE_COLUMN, title);
		
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
		numDeletedRows = todo_db.delete(TABLE_NAME, "title=?", new String[] {title});
		
		return numDeletedRows > 0;
	}
	
	//TODO: synced with SQLite or PARSE
	public List<ITodoItem> all() {
		List<ITodoItem> res = new ArrayList<ITodoItem>();		
		Cursor query = queryAllItems();
		int titleInd = query.getColumnIndex(TITLE_COLUMN);
		int dueInd = query.getColumnIndex(DUE_COLUMN);
		
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
		String[] columns = {"_id", TITLE_COLUMN, DUE_COLUMN};
		return todo_db.query(TABLE_NAME, columns, null, null, null, null, null);
	}
}
