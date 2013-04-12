package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public final class TodoListItemAdapter extends SimpleCursorAdapter {

	private static final int LIST_ITEM_LAYOUT = R.layout.list_item_row;
	
	private Context context;
	
	public TodoListItemAdapter(Context context, Cursor c, String[] fromColumns, int[] toViews) {
		super(context, LIST_ITEM_LAYOUT, c, fromColumns, toViews, 0);
		this.context = context;
	}	
	
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return super.getView(position, null, parent);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		int dueDateColumn = cursor.getColumnIndex(context.getString(R.string.SQL_due_column));
		int titleColumn = cursor.getColumnIndex(context.getString(R.string.SQL_title_column));
		
		Date dueDate = cursor.isNull(dueDateColumn) ? null : new Date(cursor.getLong(dueDateColumn));
		String title = cursor.getString(titleColumn);
		
		TextView txtTodoTitle = (TextView)view.findViewById(R.id.txtTodoTitle);
		TextView txtTodoDueDate = (TextView)view.findViewById(R.id.txtTodoDueDate);
		
		txtTodoDueDate.setText(parseDueDate(dueDate));
		txtTodoTitle.setText(title);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View resultView = inflater.inflate(LIST_ITEM_LAYOUT, parent, false);//ZXc
		
		// Retrieve text views.
		TextView txtTodoTitle = (TextView)resultView.findViewById(R.id.txtTodoTitle);
		TextView txtTodoDueDate = (TextView)resultView.findViewById(R.id.txtTodoDueDate);
		
		// Retrieve column indices.
		int dueDateColumn = cursor.getColumnIndex(context.getString(R.string.SQL_due_column));
		
		// Set text views values.
		Date dueDate = cursor.isNull(dueDateColumn) ? null : new Date(cursor.getLong(dueDateColumn));
		
		// Paint in red if due date passed.
		if (dueDate != null && dueDate.before(new Date())) {
			txtTodoTitle.setTextColor(Color.RED);
			txtTodoDueDate.setTextColor(Color.RED);
		}
		else {
			txtTodoTitle.setTextColor(Color.BLACK);
			txtTodoDueDate.setTextColor(Color.BLACK);
		}
		
		return resultView;
	}

	// Returns formated date or a default string if dueDate is null
	private String parseDueDate(Date dueDate) {
		SimpleDateFormat df = new SimpleDateFormat(context.getString(R.string.date_format), Locale.US);
		return dueDate == null ? context.getString(R.string.no_due_date) : df.format(dueDate);
	}
}