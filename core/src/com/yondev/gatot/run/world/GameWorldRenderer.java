package com.yondev.gatot.run.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.yondev.gatot.run.entity.GameObject;
import com.yondev.gatot.run.entity.Obstacle;
import com.yondev.gatot.run.entity.Player;
import com.yondev.gatot.run.entity.scrollObject;
import com.yondev.gatot.run.handler.ScrollHandler;
import com.yondev.gatot.run.world.GameWorld.GameState;

public class GameWorldRenderer {
	
	private GameWorld world;
	private OrthographicCamera cam;
	private SpriteBatch batcher;
	
	private int width;
	private int height;
	private float scaleFactor = 0;
	private TextureAtlas atlas;
	
	//background
	private TextureRegion bgImage;
	
	// scrollobject
	private ScrollHandler scroller;
	private scrollObject groundBack,groundFront,groundMid;
	private scrollObject cloud1,cloud2,cloud3,cloud4;
	
	//player
	private Player player;
	private Animation playerRun,playerSprint,playerSlideDown;
	private TextureRegion playerIdle,playerJump;
	
	//star
	private Animation star;
	
	//obtacles
	private  Obstacle obtacle1,obtacle2,obtacle3;
	private Animation bird;
	
	// HUD
	private TextureRegion play;
	private BitmapFont gameOverText;
	private float gameoverwidth;
	private BitmapFont scoreText;
	private BitmapFont hiscoreText;
	private float scorewidth;
	
	private BitmapFont jumpText;
	private BitmapFont boxText;
	private float jumpTextwidth;
	
	private Texture txtWrapper;
	
	
	ShapeRenderer shapeRenderer;
	
	private int lastscore = 0;
	private int score = 0;
	
	private float timer = 0;
	private boolean isFirstime = true;
	
	private float timerStar1 = 0;
	private float timerStar2 = 0;
	private float timerStar3 = 0;
	
	public float freezTime = 0;
	
	private float birdPos[];
	private Sound point;
	
	private float elapsed;
	private static final float FADE_TIME = 0.5f;
	private float blinktime = 0;
	private boolean playblink = false;
	public GameWorldRenderer(GameWorld world,int gameWidth, int gameHeight) {
		this.world = world;
		this.width = gameWidth;
		this.height = gameHeight;
		
		this.cam = new OrthographicCamera();
		this.batcher = new SpriteBatch();
		this.cam.setToOrtho(true, this.width, this.height);
		
		this.atlas = this.world.getAtlas();
		scaleFactor = this.world.getScaleFactor();
		
		lastscore = world.prefs.getInteger("hiscore", 0);
		if(lastscore != 0)
			isFirstime = false;
		
		initAnimation();
		initBackground();
		initGameObjects();
		initScrollerObject();
		
		shapeRenderer = new ShapeRenderer();
		
		float playerHeight = ( this.atlas.findRegion("player/1").getRegionHeight() * scaleFactor);
		float burungHeight = ( this.atlas.findRegion("burung/2").getRegionHeight() * scaleFactor);
		birdPos = new float[] {world.getGround().y - playerHeight - 6.6f*scaleFactor,
							  world.getGround().y - burungHeight + 6.6f*scaleFactor,
							  world.getGround().y - playerHeight - burungHeight + 13.3f*scaleFactor,
							  world.getGround().y - playerHeight - 6.6f*scaleFactor,
							  world.getGround().y - burungHeight + 6.6f*scaleFactor,
							  world.getGround().y - playerHeight - burungHeight + 13.3f*scaleFactor,
							  world.getGround().y - playerHeight - burungHeight + 13.3f*scaleFactor};
		
	
		point = Gdx.audio.newSound(Gdx.files.internal("data/sound/point.mp3"));
		
	}
	
	public void renderObject(GameObject obj)
	{
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.rect(obj.bounds.x, obj.bounds.y, obj.bounds.width,  obj.bounds.height);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.end();
	}
	
	public void renderObject(scrollObject obj)
	{
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.rect(obj.getX(), obj.getY(), obj.getWidth(),  obj.getHeight());
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.end();
	}
	
	public void render(float delta, float runTime) {
		
		timer += delta;
		if( timer >= 0.25f)
		{	
			if(world.currentState == GameWorld.GameState.RUNNING)
			{
				score = score + 1;
				if(score % 100 == 0)
				{
					playblink = true;
					point.play();
					
				}
					
			}
			else if(world.currentState == GameWorld.GameState.GAMEOVER)
			{
				
				if(score > lastscore)
					lastscore = score;
				
				world.prefs.putInteger("hiscore", lastscore);
				world.prefs.flush();
				isFirstime = false; 
			}
				
			
			timer=0f; 
		}
			
		if(world.currentState == GameWorld.GameState.GAMEOVER)
			freezTime += delta;
		
		timerStar1 += delta;
		timerStar2 += delta;
		timerStar3 += delta;
		
		if(playblink)
		{
			blinktime += delta;
			if(blinktime <= 1f)
			{
				elapsed += delta;
				scoreText.setColor(scoreText.getColor().r, scoreText.getColor().r, scoreText.getColor().b, Interpolation.fade.apply((elapsed / FADE_TIME) % 1f));;
			}
			else
			{
				playblink = false;
				blinktime = 0;
				scoreText.setColor(scoreText.getColor().r, scoreText.getColor().r, scoreText.getColor().b, 1);
				
			}
			
		}
		
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batcher.setProjectionMatrix(cam.combined);
		
		drawBackground();
		
		batcher.enableBlending();
		batcher.begin();
		
		drawScrollerObject();
		drawPlayer(delta, runTime);
		drawUIScreen();
		drawStar();
		drawObstacles();
		batcher.end();
		
		/*
		shapeRenderer.setProjectionMatrix(cam.combined);
		
		renderObject(player);
		renderObject(groundFront);
		renderObject(groundMid);
		renderObject(groundBack);
		renderObject(obtacle1);
		renderObject(obtacle2);
		renderObject(obtacle3);
		*/
	
	}
	public void resetScore()
	{
		
		score = 0;
		freezTime = 0;
	}
	
	private void initBackground()
	{
		//gress = atlas.findRegion("rumput");
		//sky = atlas.findRegion("background");
		bgImage = atlas.findRegion("bg");
		play = atlas.findRegion("play");
		
		gameOverText = new BitmapFont(Gdx.files.internal("data/vt232.fnt"),true);
		gameOverText.getData().setScale(0.47f * scaleFactor);
		GlyphLayout layout = new GlyphLayout();
		layout.setText(gameOverText,"G A M E   O V E R");
		gameoverwidth = layout.width;
		
		scoreText = new BitmapFont(Gdx.files.internal("data/vt232.fnt"),true);
		hiscoreText = new BitmapFont(Gdx.files.internal("data/vt232.fnt"),true);
		gameOverText.getData().setScale(0.47f * scaleFactor);
		hiscoreText.getData().setScale(0.2f * scaleFactor);
		scoreText.getData().setScale(0.2f * scaleFactor);
		
		
		hiscoreText.setColor(Color.BLACK);
		scoreText.setColor(Color.BLACK);
		gameOverText.setColor(Color.BLACK);
		
		
		jumpText = new BitmapFont(Gdx.files.internal("data/vt232.fnt"),true);
		boxText = new BitmapFont(Gdx.files.internal("data/vt232.fnt"),true);
		
		jumpText.setColor(Color.WHITE);
		boxText.setColor(Color.WHITE);
		
		jumpText.getData().setScale(0.3f * scaleFactor);
		boxText.getData().setScale(0.3f * scaleFactor);
		
		
		
		//txtWrapper = new Rectangle(0, 0, height - 6.6f * scaleFactor, height - 6.6f * scaleFactor);
		
		 Pixmap pixmap = new Pixmap( (int)(width/2 - (6.6f * scaleFactor)*2),(int)(height - (6.6f * scaleFactor)*2  - world.getGround().height), Pixmap.Format.RGBA8888);
		 
		 pixmap.setColor(0,0,0,0.7f);
	     pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
	     txtWrapper = new Texture(pixmap);
		
		GlyphLayout layout1 = new GlyphLayout();
		layout1.setText(hiscoreText,"0000000");
		scorewidth = layout1.width;
		
		GlyphLayout layout2 = new GlyphLayout();
		layout2.setText(jumpText,"Tap here to jump");
		jumpTextwidth = layout2.width;
		
	}
	
	private void drawBackground() {
	//	batcher.begin();
	//	batcher.disableBlending();
	//	batcher.draw(gress,0,(int)(this.height - (gress.getRegionHeight()*scaleFactor)),(int)(gress.getRegionWidth()*scaleFactor),(int)(gress.getRegionHeight()*scaleFactor));
	//	batcher.draw(sky,0,(int)(this.height - (gress.getRegionHeight()*scaleFactor) - (sky.getRegionHeight()*scaleFactor)),(int)(sky.getRegionWidth()*scaleFactor),(int)(sky.getRegionHeight()*scaleFactor));
	//	batcher.end();
		batcher.begin();
		batcher.draw(bgImage,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batcher.end();
	}
	
	
	private void  initScrollerObject()
	{
		scroller = world.getScroller();
		
		groundBack = scroller.getgroundBack();
		groundMid  = scroller.getgroundMid();
		groundFront = scroller.getgroundFront();
		
		cloud1 = scroller.getCloud1();
		cloud2  = scroller.getCloud2();
		cloud3 = scroller.getCloud3();
		cloud4 = scroller.getCloud4();
		
		initObstacle();
	}
	
	public void initObstacle()
	{
		obtacle1 = scroller.getObstacle1();
		obtacle2 = scroller.getObstacle2();
		obtacle3 = scroller.getObstacle3();
	}
	
	private void drawScrollerObject()
	{
	//	batcher.draw(atlas.findRegion("pohon"), treeFront.getX(), treeFront.getY(),treeFront.getWidth(),treeFront.getHeight());
	//	batcher.draw(atlas.findRegion("pohon"), treeBack.getX(), treeBack.getY(),treeBack.getWidth(),treeBack.getHeight());
		
		batcher.draw(atlas.findRegion("tanah"), groundFront.getX(), groundFront.getY(),groundFront.getWidth(),groundFront.getHeight());
		batcher.draw(atlas.findRegion("tanah"), groundMid.getX(), groundMid.getY(),groundMid.getWidth(),groundMid.getHeight());
		batcher.draw(atlas.findRegion("tanah"), groundBack.getX(), groundBack.getY(),groundBack.getWidth(),groundBack.getHeight());
	
		batcher.draw(atlas.findRegion("awan"), cloud1.getX(), cloud1.getY(),cloud1.getWidth(),cloud1.getHeight());
		batcher.draw(atlas.findRegion("awan"), cloud2.getX(), cloud2.getY(),cloud2.getWidth(),cloud2.getHeight());
		batcher.draw(atlas.findRegion("awan"), cloud3.getX(), cloud3.getY(),cloud3.getWidth(),cloud3.getHeight());
		batcher.draw(atlas.findRegion("awan"), cloud4.getX(), cloud4.getY(),cloud4.getWidth(),cloud4.getHeight());
		
	}
	
	private void initGameObjects() {
		player = world.getPlayer();
	}
	
	private void initAnimation()
	{
		Array<TextureRegion> run = new Array<TextureRegion>();
	//	run.add(atlas.findRegion("player/1"));
		run.add(atlas.findRegion("player/2"));
		run.add(atlas.findRegion("player/3"));
		
		playerSprint = new Animation(0.05f, run, Animation.PlayMode.LOOP);
		playerRun = new Animation(0.10f, run, Animation.PlayMode.LOOP);
		
		playerIdle  = atlas.findRegion("player/1");
		playerJump  = atlas.findRegion("player/4");
		
		Array<TextureRegion> down = new Array<TextureRegion>();
		down.add(atlas.findRegion("player/5"));
		down.add(atlas.findRegion("player/6"));
		playerSlideDown = new Animation(0.10f, down, Animation.PlayMode.LOOP);
		
		Array<TextureRegion> aBird = new Array<TextureRegion>();
		aBird.add(atlas.findRegion("burung/1"));
		aBird.add(atlas.findRegion("burung/2"));
		bird = new Animation(0.25f, aBird, Animation.PlayMode.LOOP);
		
		Array<TextureRegion> bintang = new Array<TextureRegion>();
		bintang.add(atlas.findRegion("bintang/1"));
		bintang.add(atlas.findRegion("bintang/2"));
		bintang.add(atlas.findRegion("bintang/3"));
		
		star = new Animation(0.40f, bintang, Animation.PlayMode.REVERSED);
		
	}
	
	private void drawPlayer(float delta,float runTime)
	{
		int state = player.state;
		float playerTime = player.stateTime;
		
		if (state == Player.IDLE)
		{
			player.setWidth(playerIdle.getRegionWidth()* scaleFactor);
			player.setHeight(playerIdle.getRegionHeight() * scaleFactor);
			player.setBoundX((66.6f*scaleFactor) + player.bounds.width/2 + 20);
			batcher.draw(playerIdle,player.getX(),player.getY(),player.getWidth(),player.getHeight());
			
		}
		else if (state == Player.RUN && !player.isSlideDown())
		{
			player.setWidth(playerRun.getKeyFrame(playerTime,false).getRegionWidth() * scaleFactor);
			player.setHeight(playerRun.getKeyFrame(playerTime,false).getRegionHeight()* scaleFactor);
			player.setBoundX((66.6f*scaleFactor) + player.bounds.width/2 + 20);
			batcher.draw(playerRun.getKeyFrame(playerTime,true),player.getX(),player.getY(),player.getWidth(),player.getHeight());
			
		}
		else if (state == Player.SPRINT && !player.isSlideDown())
		{
		
			player.setWidth(playerSprint.getKeyFrame(playerTime,false).getRegionWidth()* scaleFactor);
			player.setHeight(playerSprint.getKeyFrame(playerTime,false).getRegionHeight()* scaleFactor);
			player.setBoundX((66.6f*scaleFactor) + player.bounds.width/2 + 20);
			batcher.draw(playerSprint.getKeyFrame(playerTime,true),player.getX(),player.getY(),player.getWidth(),player.getHeight());
			
		}
		else if(state == Player.JUMP || state == Player.DEAD)
		{
			player.setWidth(playerJump.getRegionWidth()* scaleFactor);
			player.setHeight(playerJump.getRegionHeight() * scaleFactor);
			player.setBoundX((66.6f*scaleFactor) + player.bounds.width/2 + 20);
			batcher.draw(playerJump,player.getX(),player.getY(),player.getWidth(),player.getHeight());
		}
		else if((state == Player.RUN || state == Player.SPRINT) && player.isSlideDown())
		{
			player.setWidth(playerSlideDown.getKeyFrame(playerTime,false).getRegionWidth()* scaleFactor);
			player.setHeight(playerSlideDown.getKeyFrame(playerTime,false).getRegionHeight()* scaleFactor);
			player.setBoundY(world.getGround().y -  player.getHeight() + (15 * scaleFactor ));
			player.setBoundX(player.getX() +  player.getWidth() - player.bounds.width - 25);
			batcher.draw(playerSlideDown.getKeyFrame(playerTime,true),player.getX(),world.getGround().y -  player.getHeight() + (15 * scaleFactor ),player.getWidth(),player.getHeight());
	
		}
		
		
	}

	private void drawStar()
	{
		if( timerStar1 >= 10f)
		{
			if(player.state != Player.IDLE)
				batcher.draw(star.getKeyFrame(player.stateTime,true),100,100,star.getKeyFrame(player.stateTime,false).getRegionWidth()* scaleFactor,star.getKeyFrame(player.stateTime,false).getRegionHeight()* scaleFactor);			
			
			if(timerStar1 >= 15f)
				timerStar1 = 0;
		}
		
		
		if( timerStar2 >= 15f)
		{
			if(player.state != Player.IDLE)
				batcher.draw(star.getKeyFrame(player.stateTime,true),width/2 - 200,height/3 - 100 ,star.getKeyFrame(player.stateTime,false).getRegionWidth()* scaleFactor,star.getKeyFrame(player.stateTime,false).getRegionHeight()* scaleFactor);
			
			if(timerStar2 >= 20f)
				timerStar2 = 0;
		}
		
		if( timerStar3 >= 30f)
		{
			if(player.state != Player.IDLE)
				batcher.draw(star.getKeyFrame(player.stateTime,true),width - 100,50,star.getKeyFrame(player.stateTime,false).getRegionWidth()* scaleFactor,star.getKeyFrame(player.stateTime,false).getRegionHeight()* scaleFactor);
			
			if(timerStar3 >= 50f)
				timerStar3 = 0;
		}
		
	}
	
	private void drawUIScreen()
	{
		if(world.currentState ==  GameState.GAMEOVER)
		{
			batcher.draw(atlas.findRegion("play"), world.width/2 - (atlas.findRegion("play").getRegionWidth()*scaleFactor)/2, world.height/2  +  20,atlas.findRegion("play").getRegionWidth()*scaleFactor,atlas.findRegion("play").getRegionHeight()*scaleFactor);
			gameOverText.draw(batcher, "G A M E   O V E R", world.width/2 - (gameoverwidth)/2,world.height/2 - (73.3f * scaleFactor));
		}
	 
		scoreText.draw(batcher, String.format("%05d", score), world.width - (scorewidth - 10),6.6f * scaleFactor);
		
		if(lastscore != 0 && !isFirstime)
			hiscoreText.draw(batcher,"HI " +  String.format("%05d", lastscore),world.width - scorewidth*2 - 23.3f * scaleFactor,6.6f * scaleFactor);
	
		if (player.state == Player.IDLE)
		{
			if(isFirstime)
			{
				
				batcher.draw(txtWrapper, 6.6f * scaleFactor, 6.6f * scaleFactor, txtWrapper.getWidth(), txtWrapper.getHeight());
				batcher.draw(txtWrapper, width  - txtWrapper.getWidth() - 6.6f * scaleFactor, 6.6f * scaleFactor, txtWrapper.getWidth(), txtWrapper.getHeight());
				
				jumpText.draw(batcher, "Tap here to jump", world.width - world.width/4 - (jumpTextwidth)/2,world.height/2 - 10);
				boxText.draw(batcher, "Tap here to bow", world.width/4 - (jumpTextwidth)/2,world.height/2 - 10);
				
			}
		}
		
	}
	
	private void drawObstacles()
	{
		int rand = obtacle2.getType();
		
		if(rand != 6)
		{
			obtacle1.setWidth((int)(atlas.findRegion("gunungan/" + rand).getRegionWidth() * scaleFactor  * 1.2f));
			obtacle1.setHeight((int)(atlas.findRegion("gunungan/" + rand).getRegionHeight() * scaleFactor * 1.2f));
			obtacle1.setY(world.getGround().y  + (15*scaleFactor) -   (int)(atlas.findRegion("gunungan/" + rand).getRegionHeight() * scaleFactor  * 1.2f));
			batcher.draw(atlas.findRegion("gunungan/" + rand), obtacle1.getX(), obtacle1.getY(),obtacle1.getWidth(),obtacle1.getHeight());
		}
		else
		{
			obtacle1.setWidth(bird.getKeyFrame(player.stateTime,true).getRegionWidth() * scaleFactor * 0.8f );
			obtacle1.setHeight(bird.getKeyFrame(player.stateTime,true).getRegionHeight() * scaleFactor * 0.8f );
			obtacle1.setY(birdPos[obtacle1.getPostype()]);
			batcher.draw(bird.getKeyFrame(player.stateTime,true), obtacle1.getX(), obtacle1.getY(),obtacle1.getWidth(),obtacle1.getHeight());
		}
		
		
		rand = obtacle2.getType();
		if(rand != 6)
		{
			obtacle2.setWidth((int)(atlas.findRegion("gunungan/" + rand).getRegionWidth() * scaleFactor  * 1.2f));
			obtacle2.setHeight((int)(atlas.findRegion("gunungan/" + rand).getRegionHeight() * scaleFactor * 1.2f));
			obtacle2.setY(world.getGround().y + (15*scaleFactor) - (int)(atlas.findRegion("gunungan/" + rand).getRegionHeight() * scaleFactor  * 1.2f));
			batcher.draw(atlas.findRegion("gunungan/" + rand), obtacle2.getX(), obtacle2.getY(),obtacle2.getWidth(),obtacle2.getHeight());
		}
		else
		{
			obtacle2.setWidth(bird.getKeyFrame(player.stateTime,true).getRegionWidth() * scaleFactor * 0.8f );
			obtacle2.setHeight(bird.getKeyFrame(player.stateTime,true).getRegionHeight() * scaleFactor  * 0.8f);
			obtacle2.setY(birdPos[obtacle2.getPostype()]);
			batcher.draw(bird.getKeyFrame(player.stateTime,true), obtacle2.getX(), obtacle2.getY(),obtacle2.getWidth(),obtacle2.getHeight());
		}
		
		
		rand =   obtacle3.getType();
		if(rand != 6)
		{
			obtacle3.setWidth((int)(atlas.findRegion("gunungan/" + rand).getRegionWidth() * scaleFactor  * 1.2f));
			obtacle3.setHeight((int)(atlas.findRegion("gunungan/" + rand).getRegionHeight() * scaleFactor * 1.2f));
			obtacle3.setY(world.getGround().y + (15*scaleFactor) - (int)(atlas.findRegion("gunungan/" + rand).getRegionHeight() * scaleFactor  * 1.2f));
			batcher.draw(atlas.findRegion("gunungan/" + rand), obtacle3.getX(), obtacle3.getY(),obtacle3.getWidth(),obtacle3.getHeight());
		}
		else
		{
			obtacle3.setWidth(bird.getKeyFrame(player.stateTime,true).getRegionWidth() * scaleFactor * 0.8f );
			obtacle3.setHeight(bird.getKeyFrame(player.stateTime,true).getRegionHeight() * scaleFactor * 0.8f );
			obtacle3.setY(birdPos[obtacle3.getPostype()]);
			batcher.draw(bird.getKeyFrame(player.stateTime,true), obtacle3.getX(), obtacle3.getY(),obtacle3.getWidth(),obtacle3.getHeight());
		}
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
