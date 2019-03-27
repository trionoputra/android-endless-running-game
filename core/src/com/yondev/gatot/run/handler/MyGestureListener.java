package com.yondev.gatot.run.handler;

import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.yondev.gatot.run.entity.Player;
import com.yondev.gatot.run.world.GameWorld;

public class MyGestureListener implements GestureListener{
	
	private GameWorld world;
	private Player player;
	private ScrollHandler scroll;
	private boolean firstTouch = false;
	public MyGestureListener(GameWorld world)
	{
		this.world = world;
		this.player = this.world.getPlayer();
		this.scroll = this.world.getScroller();
	}
	
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {

        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
    	
    	if (!firstTouch)
		{
			
			firstTouch = true;
			world.start();
			this.scroll.start();

			player.jump();
		}
    	else
    	{
    		if ((world.width/2 >  x ) )
				player.jump();
			else
			{
				player.setSlideDown(true);
			}
    	}
		
		
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {

        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {

        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {

        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {

        return false;
    }

    @Override
    public boolean zoom (float originalDistance, float currentDistance){

       return false;
    }

    @Override
    public boolean pinch (Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer){

       return false;
    }
    
}