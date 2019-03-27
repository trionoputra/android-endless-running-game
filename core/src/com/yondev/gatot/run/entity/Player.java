package com.yondev.gatot.run.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.yondev.gatot.run.world.GameWorld;

public class Player extends DynamicGameObject{
	
	public static final int IDLE = 0;
	public static final int RUN = 1;
	public static final int JUMP = 2;
	public static final int DEAD = 3;
	public static final int SPRINT = 4;
	
	static float JUMP_VELOCITY_INIT = 1200;
	static float GRAVITY_INIT = 4200.0f;
	
	static float JUMP_VELOCITY = 1200;
	static float GRAVITY = 4200.0f;
	
	static float JUMP_VELOCITY_MAX = 1400;
	static float GRAVITY_MAX = 5500.0f;
	
	public float stateTime = 0;
	public int state = IDLE;
	
	private int rotation = 0;
	private GameWorld world;
	
	private boolean isSlideDown = false;
	
	private Sound jump;
	public Player(GameWorld world, float x, float y, float width, float height) {
		super(world, x, y, width, height);
		this.world = world;

		bounds.width = width/3;
		bounds.x =  x + bounds.width/2 + 10;
		
		
		JUMP_VELOCITY_INIT = 800*this.world.getScaleFactor();
		JUMP_VELOCITY = JUMP_VELOCITY_INIT;
		
		GRAVITY_INIT = 2800*this.world.getScaleFactor();
		GRAVITY = GRAVITY_INIT;
		
		JUMP_VELOCITY_MAX = 933.3f*this.world.getScaleFactor();
		GRAVITY_MAX = 3666.6f*this.world.getScaleFactor();
		
		setMaxJump();
		
		jump = Gdx.audio.newSound(Gdx.files.internal("data/sound/jump.mp3"));
	}

	public void update(float delta) {
		
		acceleration.y = GRAVITY;
		acceleration.scl(delta);
		
		velocity.add(acceleration.x, acceleration.y);
		velocity.scl(delta);
		
		if (bounds.overlaps(this.world.getGround()))
		{
			if ((bounds.y + height) - this.world.getGround().y   >=  15f * world.getScaleFactor())
			{
				bounds.y = this.world.getGround().y - (height + 0.1f)  +  (15 * world.getScaleFactor() );
				position.y = bounds.y;
				if(state == JUMP)
				{
					state = RUN;
					stateTime = 0;
				}
			}
		}
		
		if (state == JUMP)
		{
			bounds.y += velocity.y;
			position.y = bounds.y;	
		}
		
		velocity.scl(1.0f / delta);
		stateTime += delta;
	}

	public float getX() {
		return position.x;
	}

	public float getY() {
		return position.y;
	}

	public float getWidth() {
		return width;
	}
	
	public void setWidth(float w) {
		this.width = w;
	}
	
	public void setHeight(float h) {
		this.height = h;
		bounds.height = h;
		bounds.y = this.getY();
	}
	
	public void setBoundY(float y) {
		bounds.y = y;
	}
	
	public void setBoundX(float x) {
		bounds.x = x;
	}

	public float getHeight() {
		return height;
	}

	public Rectangle getBounds() {
		return bounds;
	}
	
	public void jump()
	{
		if (state != JUMP)
		{
			jump.play();
			velocity.y = -JUMP_VELOCITY;
			state = JUMP;
			stateTime = 0;
		}
	}
	
	public void setRotation(int rotate)
	{
		this.rotation = rotate;
	}
	
	public void setMaxJump()
	{
		JUMP_VELOCITY = JUMP_VELOCITY_MAX;
		GRAVITY = GRAVITY_MAX;
	}
	
	public void setResetJump()
	{
		JUMP_VELOCITY = JUMP_VELOCITY_INIT;
		GRAVITY = GRAVITY_INIT;
	}
	
	public int getRotation()
	{
		return this.rotation;
	}
	
	public boolean isSlideDown() {
		return isSlideDown;
	}

	public void setSlideDown(boolean isSlideDown) {
		this.isSlideDown = isSlideDown;
	}

	
}
