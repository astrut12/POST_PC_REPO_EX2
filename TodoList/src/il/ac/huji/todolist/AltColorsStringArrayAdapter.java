package il.ac.huji.todolist;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public final class AltColorsStringArrayAdapter extends ArrayAdapter<String> {
	
	// Color list from which to alternate.
	private static final int[] COLORS = {Color.RED, Color.BLUE};
	private static final int CONVERT_LIST_ITEM = android.R.layout.simple_list_item_1;
	
	public AltColorsStringArrayAdapter(Context context) {
		super(context, CONVERT_LIST_ITEM);
	}

	public AltColorsStringArrayAdapter(Context context, String[] objects) {
		super(context, CONVERT_LIST_ITEM, objects);
	}

	public AltColorsStringArrayAdapter(Context context, List<String> objects) {
		super(context, CONVERT_LIST_ITEM, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView originalView = (TextView)super.getView(position, convertView, parent);
		
		// Set the appropriate alternating color from COLORS.
		originalView.setTextColor(COLORS[position % COLORS.length]);

		return originalView;
	}
}
