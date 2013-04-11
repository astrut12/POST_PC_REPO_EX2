package il.ac.huji.todolist;

import java.util.Date;

public class ListItem implements ITodoItem{
	public String title;
	public Date dueDate;
	
	public ListItem(String title, Date date) {
		this.title = title;
		
		this.dueDate = date;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public Date getDueDate() {
		return dueDate;
	}	
	
	
}
