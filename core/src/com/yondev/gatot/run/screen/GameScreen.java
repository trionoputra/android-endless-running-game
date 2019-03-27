package com.yondev.gatot.run.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.yondev.gatot.run.GatotRun;
import com.yondev.gatot.run.handler.InputHandler;
import com.yondev.gatot.run.world.GameWorld;
import com.yondev.gatot.run.world.GameWorldRenderer;

public class GameScreen  extends GatotScreen implements Screen{

	private int width;
	private int height;
	
	private TextureAtlas atlas;
	
	private GameWorld world;
	private GameWorldRenderer renderer;
	
	private float runTime;
	public GameScreen(GatotRun game)
	{
		super(game);
		
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
	
		atlas = game.assets.get("data/gatot.pack", TextureAtlas.class);
		for(AtlasRegion ar : atlas.getRegions()) {
	         ar.flip(false, true);
	    }
		
		world = new GameWorld(game,atlas,width,height);
		renderer = new GameWorldRenderer(world,width,height);
		world.setRenderer(renderer);
		
		// Gdx.input.setInputProcessor(new GestureDetector(new MyGestureListener(world)));
		Gdx.input.setInputProcessor(new InputHandler(world));
		// Gdx.input.setInputProcessor();
		// new InputHandler(world)
	}
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		runTime += delta;
		world.update(delta);
		renderer.render(delta, runTime);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	public enum GameState {
		READY, RUNNING, GAMEOVER
	}

}
