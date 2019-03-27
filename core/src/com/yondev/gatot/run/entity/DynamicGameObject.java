package com.yondev.gatot.run.entity;

import com.badlogic.gdx.math.Vector2;
import com.yondev.gatot.run.world.GameWorld;

public class DynamicGameObject extends GameObject {
	public final Vector2 velocity;
	public final Vector2 acceleration;
	public DynamicGameObject (GameWorld world,float x, float y, float width, float height) {
		super(x, y, width, height);
		velocity = new Vector2();
		acceleration = new Vector2();
	}
}
