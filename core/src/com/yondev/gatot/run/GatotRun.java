package com.yondev.gatot.run;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.FPSLogger;
import com.yondev.gatot.run.screen.SplashScreen;

public class GatotRun extends Game {
	boolean firstTimeCreate = true;
	FPSLogger fps;
	public AssetManager assets;
	
	private AdsController adsController;
	public GatotRun(AdsController adsController){
	    this.adsController = adsController;
	}
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		setScreen(new SplashScreen(this));
		fps = new FPSLogger();
	}
	
	@Override
	public void render() {
		super.render();
		fps.log();
	
	}

	@Override
	public void dispose () {
		super.dispose();
	}

	public AdsController getAdsController() {
		return adsController;
	}
	
	
}
