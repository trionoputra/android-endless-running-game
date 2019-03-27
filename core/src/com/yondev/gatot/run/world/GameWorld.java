package com.yondev.gatot.run.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.yondev.gatot.run.GatotRun;
import com.yondev.gatot.run.entity.Player;
import com.yondev.gatot.run.handler.ScrollHandler;

public class GameWorld {
	
	private GameWorldRenderer renderer;
	
	public int width;
	public int height;
	
	private float scaleFactor;
	private TextureAtlas atlas;
	
	private ScrollHandler scroller;
	private Player player;
	private Rectangle ground;
	
	public GameState currentState = GameState.READY;
	public enum GameState {
		READY, RUNNING, GAMEOVER
	}
	
	private Sound dead;
	public Preferences prefs;
	private GatotRun game;
	int gameOverCount = 0;
	public GameWorld(GatotRun game,TextureAtlas atlas,int width,int height) 
	{
		this.game = game;
		this.atlas = atlas;
		this.width = width;
		this.height = height;
		scaleFactor = height/480f; //(float)(this.atlas.findRegion("player/1").getRegionWidth() / this.width);
		//scaleFactor = 2.5f; 
		
		prefs = Gdx.app.getPreferences("GatotData");
		dead = Gdx.audio.newSound(Gdx.files.internal("data/sound/dead.mp3"));
		
		float groundHeight = this.atlas.findRegion("tanah").getRegionHeight() *scaleFactor;
		ground = new Rectangle(0, height - groundHeight, width, groundHeight - 13.3f*scaleFactor);
		player = new Player(this,66.6f*scaleFactor, ground.y - ( this.atlas.findRegion("player/1").getRegionHeight() * scaleFactor ) + (15 * scaleFactor )  , ( this.atlas.findRegion("player/1").getRegionWidth() * scaleFactor),( this.atlas.findRegion("player/1").getRegionHeight() * scaleFactor));
		
		scroller = new ScrollHandler(this);
	}
	
	public void setRenderer(GameWorldRenderer renderer) {
		this.renderer = renderer;
	}
	
	public GameWorldRenderer getRenderer() {
		return this.renderer;
	}
	
	public void update(float delta)
	{
		scroller.update(delta);
		player.update(delta);
	}
	
	public TextureAtlas getAtlas()
	{
		return this.atlas;
	}
	
	public float getScaleFactor()
	{
		return this.scaleFactor;
	}
	
	public ScrollHandler getScroller() {
		return scroller;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Rectangle getGround() {
		return ground;
	}
	
	public void start() {
		this.game.getAdsController().hideBannerAd();
		currentState = GameState.RUNNING;
		player.state = Player.IDLE;
	}
	
	public void start(boolean force) {
		this.game.getAdsController().hideBannerAd();
		currentState = GameState.RUNNING;
		player.state = Player.RUN;
	}
	
	public void stop() {
		if(currentState != GameState.GAMEOVER)
		{
			gameOverCount++;
			if(gameOverCount % 3 == 0)
				this.game.getAdsController().showInterstitialAd();
			else
				this.game.getAdsController().showBannerAd();
		}
			
		
		currentState = GameState.GAMEOVER;
		
		if(player.state != Player.DEAD)
			dead.play();
		
		player.state = Player.DEAD;
		
		scroller.stop();
	}
}
