package il.ac.huji.todolist;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public final class TodoListItemAdapter extends ArrayAdapter<ListItem> {

	// Color list from which to alternate.
	private static final int CONVERT_LIST_ITEM = R.layout.list_item_row;

	public TodoListItemAdapter(Context context) {
		super(context, CONVERT_LIST_ITEM);
	}

	public TodoListItemAdapter(Context context, ListItem[] objects) {
		super(context, CONVERT_LIST_ITEM, objects);
	}

	public TodoListItemAdapter(Context context, List<ListItem> objects) {
		super(context, CONVERT_LIST_ITEM, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView txtTodoTitle = (TextView)convertView.findViewById(R.id.txtTodoTitle);
		TextView txtTodoDueDate = (TextView)convertView.findViewById(R.id.txtTodoDueDate);
		
		Date dueDate = this.getItem(position).dueDate;
		
		txtTodoTitle.setText(this.getItem(position).title);
		txtTodoDueDate.setText(dueDate.toString());
		
		if (dueDate.after(new Date())) {
			txtTodoTitle.setTextColor(Color.RED);
			txtTodoDueDate.setTextColor(Color.RED);
		}
		
		return convertView;
	}
}