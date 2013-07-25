package com.hammers.lingo;

public class Ball {
	private int row;
	private int column;
	
	public Ball()
	{
		setRow(0);
		setColumn(0);
	}
	
	public Ball(int x, int y)
	{
		setRow(x);
		setColumn(y);
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
}
