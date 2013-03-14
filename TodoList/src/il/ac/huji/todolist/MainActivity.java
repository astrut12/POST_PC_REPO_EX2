package il.ac.huji.todolist;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	private ArrayAdapter <String> lstTodoItemsAdapter;
	private ArrayList<String> dataTodoItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Initialize main layout.
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Create an adapter for lstTodoItems.
//		lstTodoItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1); TODO remove this
		dataTodoItems = new ArrayList<String>();
		lstTodoItemsAdapter = new AltColorsStringArrayAdapter(this, dataTodoItems);
		((ListView)findViewById(R.id.lstTodoItems)).setAdapter(lstTodoItemsAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menuItemAdd:
				onAddSelected();
				break;
			case R.id.menuItemDelete:
				onDeleteSelected();
				break;
		}
		return true;
	}

	private void onAddSelected() {
		// Retrieve text from edtNewItem. 
		EditText edtNewItem = (EditText)findViewById(R.id.edtNewItem);
		String newItemStr = edtNewItem.getText().toString();
		
		// Return on empty string.
		if(newItemStr == null || newItemStr.length() == 0)
			return;
		
		// Add to lstTodoItemsAdapter the text in edtNewItem.
		lstTodoItemsAdapter.add(newItemStr);
		edtNewItem.setText(""); //TODO check if required to clear
	}
	
	private void onDeleteSelected() {
		// Retrieve checked list item inner String object. 
		ListView lstTodoItems = (ListView)findViewById(R.id.lstTodoItems);
		int selectedItemIndex = lstTodoItems.getSelectedItemPosition();
		
		// Return on invalid position
		if (!lstTodoItems.hasFocus() || selectedItemIndex == ListView.INVALID_POSITION)
			return;
		
		// Remove the selected item from underlying list and update adapter.
		dataTodoItems.remove(selectedItemIndex);
		lstTodoItemsAdapter.notifyDataSetChanged();
		
	}

}
