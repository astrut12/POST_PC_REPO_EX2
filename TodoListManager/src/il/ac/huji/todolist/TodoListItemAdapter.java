package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public final class TodoListItemAdapter extends SimpleCursorAdapter {

	// Color list from which to alternate.
	private static final int LIST_ITEM_LAYOUT = R.layout.list_item_row;
	
	public TodoListItemAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, LIST_ITEM_LAYOUT, c, from, to, flags);
	}
	
//	public TodoListItemAdapter(Context context) {
//		super(context, LIST_ITEM_LAYOUT);
//	}
//
//	public TodoListItemAdapter(Context context, ListItem[] objects) {
//		super(context, LIST_ITEM_LAYOUT, objects);
//	}
//
//	public TodoListItemAdapter(Context context, List<ListItem> objects) {
//		super(context, LIST_ITEM_LAYOUT, objects);
//	}

	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Inflate the item View.
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View resultView = inflater.inflate(LIST_ITEM_LAYOUT, null);
		
		// Retrieve text views.
		TextView txtTodoTitle = (TextView)resultView.findViewById(R.id.txtTodoTitle);
		TextView txtTodoDueDate = (TextView)resultView.findViewById(R.id.txtTodoDueDate);
		
		// Set text views values.
		txtTodoTitle.setText(this.getItem(position).title);
		Date dueDate = this.getItem(position).dueDate;
		txtTodoDueDate.setText(parseDueDate(dueDate));
		
		// Paint in red if due date passed.
		if (dueDate != null && dueDate.before(new Date())) {
			txtTodoTitle.setTextColor(Color.RED);
			txtTodoDueDate.setTextColor(Color.RED);
		}
		
		return resultView;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// Inflate the item View.
		LayoutInflater inflater = LayoutInflater.from(context);
		View resultView = inflater.inflate(LIST_ITEM_LAYOUT, null);
		
		// Retrieve text views.
		TextView txtTodoTitle = (TextView)resultView.findViewById(R.id.txtTodoTitle);
		TextView txtTodoDueDate = (TextView)resultView.findViewById(R.id.txtTodoDueDate);
		
		// Set text views values.
		txtTodoTitle.setText(this.getItem(position).title);
		Date dueDate = this.getItem(position).dueDate;
		txtTodoDueDate.setText(parseDueDate(dueDate));
		
		return super.newView(context, cursor, parent);
	}

	// Returns formated date or a default string if dueDate is null
	private String parseDueDate(Date dueDate) {
		SimpleDateFormat df = new SimpleDateFormat(getContext().getString(R.string.date_format), Locale.US);
		return dueDate == null ? getContext().getString(R.string.no_due_date) : df.format(dueDate);
	}
}