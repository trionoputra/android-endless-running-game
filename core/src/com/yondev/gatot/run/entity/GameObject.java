package com.yondev.gatot.run.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GameObject {
	public final Vector2 position;
	public final Rectangle bounds;
	public float width;
	public float height;

	public GameObject (float x, float y, float width, float height) {
		this.position = new Vector2(x, y);
		this.bounds = new Rectangle(x,y, width, height);
		this.width = width;
		this.height = height;
	}
}
