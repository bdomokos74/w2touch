package org.bds.touch.model;

public class Post {
	private int id;
	private int chatId;
	private Direction direction;
	private String text;
	
	public enum Direction{
		IN(0), OUT(1);
		
		private int id;
		Direction(int id) {
			this.id = id;
		}
		public int getId() {
			return id;
		}
		public static Direction getDirection(int i) {
			if(i==0)
				return IN;
			else if (i == 1)
				return OUT;
			else
				throw new IllegalArgumentException("Invalid value for Direction");
		}
	};
	
	
	public Post(int postId, int chatId, Direction direction, String text) {
		id = postId;
		this.chatId = chatId;
		this.direction = direction;
		this.text = text;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setChatId(int chatId) {
		this.chatId = chatId;
	}

	public int getChatId() {
		return chatId;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
	
	@Override
	public String toString() {
		return "Post["+id+","+chatId+","+direction+"]";
	}
}
