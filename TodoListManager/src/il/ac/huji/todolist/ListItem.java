package il.ac.huji.todolist;

import java.util.Date;

public class ListItem {
	public String title;
	public Date dueDate;
	
	public ListItem(String title, Date date) {
		this.title = title;
		
		this.dueDate = date;
	}	
}
