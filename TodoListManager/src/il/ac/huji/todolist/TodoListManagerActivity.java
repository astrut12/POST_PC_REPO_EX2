package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class TodoListManagerActivity extends Activity {
	
	private ArrayAdapter <ListItem> lstTodoItemsAdapter;
	private ArrayList<ListItem> dataTodoItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Initialize main layout.
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		
		// Create an adapter for lstTodoItems.
		dataTodoItems = new ArrayList<ListItem>();
		lstTodoItemsAdapter = new TodoListItemAdapter(this, dataTodoItems);
		ListView lstTodoItems = (ListView)findViewById(R.id.lstTodoItems);
		(lstTodoItems).setAdapter(lstTodoItemsAdapter);
		
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
		
		//TODO: identify the right view.
		TextView txtTodoTitle = (TextView)v.findViewById(R.id.txtTodoTitle);
		if (txtTodoTitle == null)
			return;
		
	    getMenuInflater().inflate(R.menu.todo_list_context, menu);
	    menu.setHeaderTitle(txtTodoTitle.getText());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menuItemAdd:
				onAddSelected();
				break;
		}
		return true;
	}
	
	
	//TODO: review
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo itemInfo = (AdapterContextMenuInfo)item.getMenuInfo();
		
		switch (item.getItemId()) {
			case R.id.menuItemDelete:
				if (itemInfo == null)
					return false;
				onDeleteSelected(itemInfo.position);
				break;
		}
		return true;
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
	      
					lstTodoItemsAdapter.add(new ListItem(newItemTitle, newItemDueDate));
				}
		}
	}

	
	private void onDeleteSelected(int positionToDelete) {
		// Return on invalid position
		if (positionToDelete == ListView.INVALID_POSITION)		
			return;
		
		// Remove the selected item from underlying list and update adapter.
		dataTodoItems.remove(positionToDelete);
		lstTodoItemsAdapter.notifyDataSetChanged();
		
	}
}
