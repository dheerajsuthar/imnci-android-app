package org.dheeraj.imnci;

class Updates {
	private int created_at;
	private String ID;
	private int priority;
	private String title;
	private String message;

	Updates() {
		created_at = 0;
		ID = "";
		priority = 0;
		title = "";
		message = "";
	}

	Updates(int created_at, String ID, int priority, String title,
			String message) {
		this.created_at = created_at;
		this.ID = ID;
		this.priority = priority;
		this.title = title;
		this.message = message;
	}

	public int getCreated_at() {
		return created_at;
	}

	public void setCreated_at(int created_at) {
		this.created_at = created_at;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}