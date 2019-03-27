package com.yondev.gatot.run.screen;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.yondev.gatot.run.GatotRun;
import com.yondev.gatot.run.helper.SpriteAccessor;

public class SplashScreen extends GatotScreen  implements Screen {
	
	private Texture logoTexture1;
	private TextureRegion logo1;
	private Texture logoTexture2;
	private TextureRegion logo2;
	private TweenManager manager;
	private SpriteBatch batcher;
	private Sprite sprite1;
	private Sprite sprite2;
	public SplashScreen(GatotRun game) {
		super(game);
		logoTexture1 = new Texture(Gdx.files.internal("data/logoyondev.png"));
		logo1 = new TextureRegion(logoTexture1, 0, 0, 585, 150);
		logoTexture2 = new Texture(Gdx.files.internal("data/gamewae.png"));
		logo2 = new TextureRegion(logoTexture2, 0, 0, 314, 55);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		manager.update(delta);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batcher.begin();
		sprite1.draw(batcher);
		sprite2.draw(batcher);
		batcher.end();
		
		if(game.assets != null)
		{
			if(game.assets.update()){
				game.setScreen(new GameScreen(game));
			}
		}
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		sprite1 = new Sprite(logo1);
		sprite1.setColor(1, 1, 1, 0);
		
		sprite2 = new Sprite(logo2);
		sprite2.setColor(1, 1, 1, 0);
		
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		float desiredWidth = width * .7f;
		
		float scale1 = desiredWidth / sprite1.getWidth();
		float scale2 = desiredWidth / sprite2.getWidth();

		sprite1.setSize(sprite1.getWidth() * scale1, sprite1.getHeight() * scale1);
		sprite1.setPosition((width / 2) - (sprite1.getWidth() / 2), (height / 2)- (sprite1.getHeight() / 2));
		
		sprite2.setSize(sprite2.getWidth() * scale2, sprite2.getHeight() * scale2);
		sprite2.setPosition((width / 2) - (sprite2.getWidth() / 2), (height / 2)- (sprite2.getHeight() / 2));
		
		setupTween();
		
		batcher = new SpriteBatch();
	}
	
	private void setupTween() {
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		manager = new TweenManager();

		TweenCallback cb = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				game.assets = new AssetManager();
				game.assets.load("data/gatot.pack",TextureAtlas.class);
				
			}
		};
		
		Timeline.createSequence()
		.push(Tween.to(sprite1, SpriteAccessor.ALPHA, 1f).target(1)
				.ease(TweenEquations.easeInOutQuad).repeatYoyo(1, 2f))
		.push(Tween.to(sprite2, SpriteAccessor.ALPHA, 1f).target(1)
				.ease(TweenEquations.easeInOutQuad).repeatYoyo(1, 2f)
				.setCallback(cb).setCallbackTriggers(TweenCallback.COMPLETE)
				).start(manager);
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

}
