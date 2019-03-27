package com.yondev.gatot.run.handler;

import com.badlogic.gdx.InputProcessor;
import com.yondev.gatot.run.entity.Player;
import com.yondev.gatot.run.world.GameWorld;

public class InputHandler implements InputProcessor{

	private GameWorld world;
	private Player player;
	private ScrollHandler scroll;
	private boolean firstTouch = false;
	
	public InputHandler(GameWorld world)
	{
		this.world = world;
		this.player = this.world.getPlayer();
		this.scroll = this.world.getScroller();
		
	}
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override 
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		if (!firstTouch)
		{
			
			firstTouch = true;
			world.start();
			this.scroll.start();
			
			player.jump();
		}
    	else
    	{
    		if(player.state == Player.DEAD)
    		{
    			if (world.getRenderer().freezTime >= 1)
    			{
    				scroll.initObstables();
        			world.getRenderer().initObstacle();
        			world.start(true);
        			scroll.start();
        			player.jump();
        			world.getRenderer().resetScore();
        			
    			}
    		}
    		else
    		{
    			if ((world.width/2 >  screenX ) )
        			player.setSlideDown(true);
    			else
    				player.jump();
    		}
    	}
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		player.setSlideDown(false);
		
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
	
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
