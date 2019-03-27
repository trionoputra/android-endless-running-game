package com.yondev.gatot.run.entity;


public class scrollObject extends Scrollable {


	public scrollObject(float x, float y, float width, float height,float scrollSpeed) {
		super(x, y, width, height, scrollSpeed);
		// TODO Auto-generated constructor stub
	}
	
	public void onRestart(float x, float scrollSpeed) {
		position.x = x;
		velocity.x = scrollSpeed;
	}

}
