package com.yondev.gatot.run.entity;

import com.badlogic.gdx.math.Rectangle;

public class Obstacle  extends scrollObject{
	public float animTime = 0;
	public float runTime = 0;
	public boolean isCollition = false;
	public boolean hasGone = false;
	private Rectangle bounds;
	private int type = 1;
	private int postype = 1; 
	public Obstacle(float x, float y, int width, int height, float scrollSpeed) {
		super(x, y, width, height, scrollSpeed);
		// TODO Auto-generated constructor stub
		bounds = new Rectangle(x, y, width, height);
	}
	
	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		super.update(delta);
		bounds.x = position.x;
		bounds.y = position.y;
		runTime += delta;
	}
	
	public Rectangle getBounds()
	{
		return bounds;
	}
	
	
	public void setBoundY(float y) {
		bounds.y = y;
	}
	
	public void setBoundX(float x) {
		bounds.x = x;
	}
	
	public void setWidth(float w) {
		this.width = w;
		bounds.width = w;
	}
	
	public void setHeight(float h) {
		this.height = h;
		bounds.height = h;
		bounds.y = this.getY();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPostype() {
		return postype;
	}

	public void setPostype(int postype) {
		this.postype = postype;
	}
	
	
	
}
