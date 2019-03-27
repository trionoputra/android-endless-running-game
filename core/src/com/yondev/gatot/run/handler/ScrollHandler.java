package com.yondev.gatot.run.handler;

import java.util.Collections;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.yondev.gatot.run.entity.Obstacle;
import com.yondev.gatot.run.entity.Player;
import com.yondev.gatot.run.entity.scrollObject;
import com.yondev.gatot.run.world.GameWorld;

public class ScrollHandler {
	
	//ground
	private scrollObject groundBack,groundmid,groundFront;
	
	//cloud
	private scrollObject cloud1,cloud2,cloud3,cloud4;

	//obtacles
	private Obstacle obstacle1,obstacle2,obstacle3;
	
	
	public static float  SCROLL_SPEED_MAX = -25f;
	public static float  SCROLL_SPEED_INIT = -13f;
	public static float SCROLL_SPEED = -13f;
	
	public int width;
	public int height;
	public float distance = 0;
	public float speed = 0;
	
	private GameWorld world;
	private TextureAtlas atlas;
	

	private float scaleFactor;
	
	private Player player;
	private float groundY;
	
	private float[] distanceObstacle;
	public ScrollHandler(GameWorld world) {
		this.world = world;
		atlas = this.world.getAtlas();
		scaleFactor = this.world.getScaleFactor();
		
		
		SCROLL_SPEED_MAX = -16.6f * scaleFactor;
		SCROLL_SPEED_INIT = -8.6f * scaleFactor;
		SCROLL_SPEED = -8.6f * scaleFactor;
		
		this.height = this.world.height;
		this.width = this.world.width;
		
		player = this.world.getPlayer();
		
		
		groundY =  this.world.getGround().y;
		
		groundFront  = new scrollObject(0,groundY , (int)(atlas.findRegion("tanah").getRegionWidth() * scaleFactor), (int)(atlas.findRegion("tanah").getRegionHeight() * scaleFactor),SCROLL_SPEED);
		groundmid = new scrollObject(groundFront.getTailX() - 5, groundY, (int)(atlas.findRegion("tanah").getRegionWidth() * scaleFactor), (int)(atlas.findRegion("tanah").getRegionHeight() * scaleFactor),SCROLL_SPEED);
		groundBack = new scrollObject(groundmid.getTailX() - 5, groundY, (int)(atlas.findRegion("tanah").getRegionWidth() * scaleFactor), (int)(atlas.findRegion("tanah").getRegionHeight() * scaleFactor),SCROLL_SPEED);
		
		
		distanceObstacle = new float[]{width,width/2,width - 100,width,width*1.2f,width*1.4f,width*1.5f,width*2f};
		
		initObstables();
		initParalaxObjet();
	}
	
	public void initObstables()
	{
		obstacle1 = new Obstacle(this.width * 2, groundY - (10*scaleFactor) - (int)(atlas.findRegion("gunungan/1").getRegionHeight() * scaleFactor), (int)(atlas.findRegion("gunungan/1").getRegionWidth() * scaleFactor  * 1.5f), (int)(atlas.findRegion("gunungan/1").getRegionHeight() * scaleFactor  * 1.2f) , SCROLL_SPEED);
		obstacle2 = new Obstacle(obstacle1.getTailX() + this.width, groundY  - (10*scaleFactor) -  (int)(atlas.findRegion("gunungan/2").getRegionHeight() * scaleFactor),(int)(atlas.findRegion("gunungan/2").getRegionWidth() * scaleFactor * 1.5f), (int)(atlas.findRegion("gunungan/2").getRegionHeight() * scaleFactor  * 1.2f), SCROLL_SPEED);
		obstacle3 = new Obstacle(obstacle2.getTailX() + this.width, groundY - (10*scaleFactor) -  (int)(atlas.findRegion("gunungan/3").getRegionHeight() * scaleFactor),(int)(atlas.findRegion("gunungan/3").getRegionWidth() * scaleFactor * 1.5f), (int)(atlas.findRegion("gunungan/3").getRegionHeight() * scaleFactor * 1.2f), SCROLL_SPEED);
	}
	
	public void initParalaxObjet()
	{
		cloud1 = new scrollObject(width,height/3, (int)(atlas.findRegion("awan").getRegionWidth() * scaleFactor), (int)(atlas.findRegion("awan").getRegionHeight() * scaleFactor), SCROLL_SPEED + 12);
		cloud2 = new scrollObject(cloud1.getX() + width/2,30,cloud1.getWidth(),cloud1.getHeight(), SCROLL_SPEED + 12);
		cloud3 = new scrollObject(cloud2.getX() + width,height/4,cloud1.getWidth(),cloud1.getHeight(), SCROLL_SPEED + 12);
		cloud4 = new scrollObject(cloud3.getX() + width,40,cloud1.getWidth(),cloud1.getHeight(), SCROLL_SPEED + 12);
	}
	
	public void update(float delta) {
		
		groundFront.update(delta);
		groundmid.update(delta);
		groundBack.update(delta);
		
		obstacle1.update(delta);
		obstacle2.update(delta);
		obstacle3.update(delta);
		
		
		cloud1.update(delta);
		cloud2.update(delta);
		cloud3.update(delta);
		cloud4.update(delta);
		
		if (player.state != Player.JUMP && player.state != Player.DEAD )
		{
			if (groundFront.incrementSpeed < -10)
			{
				if (player.state != Player.SPRINT)
					player.stateTime = 0;
				player.state = Player.SPRINT;
				
			}
			else
			{
				if (player.state != Player.RUN)
					player.stateTime = 0;
				
				if(player.state != Player.IDLE)
					player.state = Player.RUN;
				
			}
		}
		
		
		
		if (groundFront.isScrolledLeft()) {
			
			groundFront.reset(groundBack.getTailX() - 5);

		} else if (groundmid.isScrolledLeft()) {
			groundmid.reset(groundFront.getTailX() - 5);

		}
		else if (groundBack.isScrolledLeft()) {
			groundBack.reset(groundmid.getTailX()  - 5);
		}
		
		
		if (cloud1.isScrolledLeft()) {
			
			cloud1.reset(cloud4.getX() + 200);

		} else if (cloud2.isScrolledLeft()) {
			cloud2.reset(cloud1.getX() + (width/2 - 200));

		}
		else if (cloud3.isScrolledLeft()) {
			cloud3.reset(cloud2.getX() + (width - 200));
		}
		else if (cloud4.isScrolledLeft()) {
			cloud4.reset(cloud3.getX() + (width - 400));
		}
		
		distance += (groundFront.currentSpeed);
		speed = groundFront.currentSpeed;
		
		cekCollision();
		
		spawnObstacles();
		
		speedLevel(delta);
	}

	private void speedLevel(float delta)
	{
		if(world.getRenderer().getScore() >= 50)
		{
			SCROLL_SPEED -= delta * 0.2;
			if(SCROLL_SPEED <= SCROLL_SPEED_MAX )
				SCROLL_SPEED = SCROLL_SPEED_MAX;
			
			
			this.setSpeed(SCROLL_SPEED,true);
			Gdx.app.debug("SPEEF", SCROLL_SPEED + "");
		}
	}
	
	private void spawnObstacles()
	{
		
		Random rn = new Random();
		int rand =  rn.nextInt(distanceObstacle.length - 1) ;
		
		int score = world.getRenderer().getScore();
		int ran =  1;
		if (obstacle1.isScrolledLeft()) {
			if(score <= 50)
			{
				obstacle1.setType(1);
			}
			else if(score > 50 && score <= 100)
			{
				ran =  rn.nextInt(3) + 1 ;
				obstacle1.setType(ran);
			}
			else if(score > 100)
			{
				ran =  rn.nextInt(6) + 1 ;
				obstacle1.setType(ran);
				if(ran == 6)
				{
					ran =  rn.nextInt(6) + 1 ;
					obstacle1.setPostype(ran);
				}
			}
			
			obstacle1.reset(obstacle3.getX() + distanceObstacle[rand]);

		} else if (obstacle2.isScrolledLeft()) {
			if(score <= 50)
			{
				obstacle2.setType(1);
			}
			else if(score > 50 && score <= 100)
			{
				ran =  rn.nextInt(3) + 1 ;
				obstacle2.setType(ran);
			}
			else if(score > 100)
			{
				ran =  rn.nextInt(6) + 1 ;
				obstacle2.setType(ran);
				if(ran == 6)
				{
					ran =  rn.nextInt(6) + 1 ;
					obstacle2.setPostype(ran);
				}
			}
			obstacle2.reset(obstacle1.getX() +  distanceObstacle[rand]);
		}
		
		else if (obstacle3.isScrolledLeft()) {
			if(score <= 50)
			{
				obstacle3.setType(1);
			}
			else if(score > 50 && score <= 100)
			{
				ran =  rn.nextInt(3) + 1 ;
				obstacle3.setType(ran);
			}
			else if(score > 100)
			{
				ran =  rn.nextInt(6) + 1 ;
				obstacle3.setType(ran);
				if(ran == 6)
				{
					ran =  rn.nextInt(6) + 1 ;
					obstacle3.setPostype(ran);
				}
			}
			
			obstacle3.reset(obstacle2.getX() +  distanceObstacle[rand]);
		}
	}
	
	private void cekCollision()
	{
		if(player.bounds.overlaps(obstacle1.getBounds()) || player.bounds.overlaps(obstacle2.getBounds()) || player.bounds.overlaps(obstacle3.getBounds()))
		{
			world.stop();
		}
	}
	
	public void setSpeed(float speed,boolean reset)
	{
		groundFront.setSpeed(speed,reset);
		groundBack.setSpeed(speed,reset);
		groundmid.setSpeed(speed,reset);
		
		
		obstacle1.setSpeed(speed,reset);
		obstacle2.setSpeed(speed,reset);
		obstacle3.setSpeed(speed,reset);
		
		cloud1.setSpeed(speed + 12,reset);
		cloud2.setSpeed(speed + 12,reset);
		cloud3.setSpeed(speed + 12,reset);
		cloud4.setSpeed(speed + 12,reset);
		
		if(speed <= -15)
		{
			player.setMaxJump();
		}
		
	}
	
	public void stop(){
		groundFront.stop();
		groundBack.stop();
		groundmid.stop();
		
		obstacle1.stop();
		obstacle2.stop();
		obstacle3.stop();
		
		cloud1.stop();
		cloud2.stop();
		cloud3.stop();
		cloud4.stop();
		
	}
	
	public void start() {	
		
		SCROLL_SPEED = SCROLL_SPEED_INIT;
		this.setSpeed(SCROLL_SPEED,true);
		player.setResetJump();
		groundFront.start();
		groundBack.start();
		groundmid.start();
		
		obstacle1.start();
		obstacle2.start();
		obstacle3.start();
		
		cloud1.start();
		cloud2.start();
		cloud3.start();
		cloud4.start();
	}

	
	public scrollObject getgroundFront() {
		return groundFront;
	}
	
	public scrollObject getgroundBack() {
		return groundBack;
	}
	
	public scrollObject getgroundMid() {
		return groundmid;
	}
	
	public Obstacle getObstacle1() {
		return obstacle1;
	}
	
	public Obstacle getObstacle2() {
		return obstacle2;
	}
	
	public Obstacle getObstacle3() {
		return obstacle3;
	}
	public scrollObject getCloud1() {
		return cloud1;
	}

	public scrollObject getCloud2() {
		return cloud2;
	}

	public scrollObject getCloud3() {
		return cloud3;
	}
	public scrollObject getCloud4() {
		return cloud4;
	}

}