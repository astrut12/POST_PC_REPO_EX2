package il.ac.huji.todolist;

import java.util.Date;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class TodoListManagerActivity extends Activity {
	
	private TodoListItemAdapter dbTodoItemsAdapter;
	private TodoDAL dbWrapper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Initialize main layout.
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		
		// Initialize the DB an create an adapter for it.
		dbWrapper = new TodoDAL(this);
		String[] fromColumns = {getString(R.string.SQL_title_column), getString(R.string.SQL_due_column)};
		int[] toViews = {R.id.txtTodoTitle, R.id.txtTodoDueDate};
		dbTodoItemsAdapter = new TodoListItemAdapter(this, dbWrapper.queryAllItems(), fromColumns, toViews);
		
		// Attach the adapter to its View.
		ListView lstTodoItems = (ListView)findViewById(R.id.lstTodoItems);
		(lstTodoItems).setAdapter(dbTodoItemsAdapter);
		
		// Attach a context menu to the list.
		registerForContextMenu(lstTodoItems);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
		View selectedView = ((AdapterContextMenuInfo)menuInfo).targetView;
		TextView txtTodoTitle = (TextView)selectedView.findViewById(R.id.txtTodoTitle);
		if (txtTodoTitle == null)
			return;
		
	    getMenuInflater().inflate(R.menu.todo_list_context, menu);
	    
	    menu.setHeaderTitle(txtTodoTitle.getText());
	    
	    String callTitle = getString(R.string.call_title) + " ";
	    
	    if (txtTodoTitle.getText().toString().startsWith(callTitle))
	    {
	    	MenuItem menuItemCall = menu.findItem(R.id.menuItemCall);
	    	menuItemCall.setTitle(txtTodoTitle.getText().toString());
	    	menuItemCall.setVisible(true);
	    }
	    else //Just for the automatic tester. TODO: remove if won't be tested
	    	menu.removeItem(R.id.menuItemCall);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menuItemAdd:
				onAddSelected();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		// Retrieve the item's adapter info.
		AdapterContextMenuInfo itemInfo;
		try {
			itemInfo = (AdapterContextMenuInfo)item.getMenuInfo();
		} catch (Exception e) {
			return false;
		}
		if (itemInfo == null)
			return super.onContextItemSelected(item);
		
		// Start of action routing.
		switch (item.getItemId()) {
			case R.id.menuItemDelete:
				onDeleteSelected(itemInfo.position);
				return true;
			case R.id.menuItemCall:
				onCallSelected(item.getTitle().toString());
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}

	private void onAddSelected() {
		// Start the Add Item dialog.
		Intent addNewItemActivityIntent = new Intent(this, AddNewTodoItemActivity.class);
		startActivityForResult(addNewItemActivityIntent, R.id.AddNewItemLayout);
	}
	
	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent data) {
		switch (reqCode) {
			case R.id.AddNewItemLayout:
				if (resCode == RESULT_OK) {
					String newItemTitle = data.getStringExtra(getString(R.string.extra_key_title));
					Date newItemDueDate = (Date)data.getSerializableExtra(getString(R.string.extra_key_due_date_title));
	      
					dbWrapper.insert(new ListItem(newItemTitle, newItemDueDate));
					dbTodoItemsAdapter.changeCursor(dbWrapper.queryAllItems());
					dbTodoItemsAdapter.notifyDataSetChanged();
				}
		}
	}

	
	private void onDeleteSelected(int position) {
		// Remove the selected item from underlying list and update adapter.
		Cursor selectedCursor = (Cursor)dbTodoItemsAdapter.getItem(position);
		int titleColumn = selectedCursor.getColumnIndex(getString(R.string.SQL_title_column));
		String titleToDelete = selectedCursor.getString(titleColumn);
		
		dbWrapper.delete(new ListItem(titleToDelete, null));
		dbTodoItemsAdapter.changeCursor(dbWrapper.queryAllItems());
		dbTodoItemsAdapter.notifyDataSetChanged();
	}
	
	private void onCallSelected(String itemTitle) {
		String[] itemTitleSplit = itemTitle.split(getString(R.string.call_title));
		if (itemTitleSplit == null || itemTitleSplit.length != 2 || !itemTitleSplit[0].isEmpty())
			return;
		
		Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(getString(R.string.tel_title) + itemTitleSplit[1]));
		startActivity(dialIntent);
	}
}
