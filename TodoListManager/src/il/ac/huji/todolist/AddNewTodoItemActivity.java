package il.ac.huji.todolist;

import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddNewTodoItemActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_todo_item);
	}
	
	public void onNewTaskConfirm(View view) {
		// Retrieve task and due date entered by user. 
		EditText edtNewItem = (EditText)findViewById(R.id.edtNewItem);
		DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);
		
		String title = edtNewItem.getText().toString();
		Date dueDate = new Date(datePicker.getCalendarView().getDate());
		
		// Set return parameters and return to TodoListManager. 
		Intent data = new Intent(this, TodoListManagerActivity.class);
		data.putExtra(getString(R.string.extra_key_title), title);
		data.putExtra(getString(R.string.extra_key_due_date_title), dueDate);
		
		setResult(RESULT_OK, data);
		finish();
	}
}
