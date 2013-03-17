package il.ac.huji.todolist;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public final class TodoListItemAdapter extends ArrayAdapter<ListItem> {

	// Color list from which to alternate.
	private static final int LIST_ITEM_LAYOUT = R.layout.list_item_row;

	public TodoListItemAdapter(Context context) {
		super(context, LIST_ITEM_LAYOUT);
	}

	public TodoListItemAdapter(Context context, ListItem[] objects) {
		super(context, LIST_ITEM_LAYOUT, objects);
	}

	public TodoListItemAdapter(Context context, List<ListItem> objects) {
		super(context, LIST_ITEM_LAYOUT, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Inflate the item View.
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View resultView = inflater.inflate(LIST_ITEM_LAYOUT, null);
		
		// Retrieve text views.
		TextView txtTodoTitle = (TextView)resultView.findViewById(R.id.txtTodoTitle);
		TextView txtTodoDueDate = (TextView)resultView.findViewById(R.id.txtTodoDueDate);
		
		// Set text views values.
		Date dueDate = this.getItem(position).dueDate;
		
		txtTodoTitle.setText(this.getItem(position).title);
		String DateStr = dueDate == null ? getContext().getString(R.string.no_due_date) : dueDate.toString();
		txtTodoDueDate.setText(DateStr);
		
		// Paint in red if due date passed.
		if (dueDate != null && dueDate.before(new Date())) {
			txtTodoTitle.setTextColor(Color.RED);
			txtTodoDueDate.setTextColor(Color.RED);
		}
		
		return resultView;
	}
}