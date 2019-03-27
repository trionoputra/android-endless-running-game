package com.yondev.gatot.run.entity;

import com.badlogic.gdx.math.Vector2;

public class Scrollable {
	
	protected Vector2 position;
	protected Vector2 velocity;
	protected Vector2 acceleration;
	protected float width;
	protected float height;
	protected boolean isScrolledLeft;
	protected float speed;
	protected boolean isStop;
	public float incrementSpeed;
	protected float timerSpeed;
	public float currentSpeed;
	
	
	public Scrollable(float x, float y, float width, float height, float scrollSpeed) {
		this.speed = scrollSpeed;
		position = new Vector2(x, y);
		velocity = new Vector2();
		acceleration = new Vector2();
		this.width = width;
		this.height = height;
		isScrolledLeft = false;
		isStop = true;
		incrementSpeed = 0;
		currentSpeed = Math.abs(scrollSpeed);
	}

	public void update(float delta) {
		if (isStop)
			return;
		
		if (position.x + width < 0) {
			isScrolledLeft = true;
		}
		
		timerSpeed += delta;
		if (incrementSpeed >= 0)
		{
			incrementSpeed = 0;
			timerSpeed = 0;
		}
		
		if (incrementSpeed <= -40 )
			incrementSpeed = -40;
			
		currentSpeed = Math.abs(speed + incrementSpeed);
		velocity.x = -currentSpeed;
		position.x += velocity.x;
		incrementSpeed += 1;
		
	}
	public void reset(float newX) {
		position.x = newX;
		isScrolledLeft = false;
	}
	
	public void stop() {
		isStop = true;
	}
	
	public void start() {
		isStop = false;
	}
	
	
	public void setSpeed(float s) {
			this.setSpeed(s,false);
	}
	
	public void setSpeed(float s,boolean reset) {
		if(!reset)
			incrementSpeed += s;
		else
		{
			incrementSpeed = 0;
			speed = s;
		}
	}
	
	public boolean isScrolledLeft() {
		return isScrolledLeft;
	}

	public float getTailX() {
		return position.x + width;
	}

	public float getX() {
		return position.x;
	}
	
	public void setX(float x) {
		this.position.x = x;
	}
	
	public void setY(float y) {
		this.position.y = y;
	}
	
	public float getY() {
		return position.y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
	

}
