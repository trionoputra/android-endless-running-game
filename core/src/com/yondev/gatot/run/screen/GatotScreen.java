package com.yondev.gatot.run.screen;

import com.badlogic.gdx.Screen;
import com.yondev.gatot.run.GatotRun;

public abstract class GatotScreen implements Screen{
	GatotRun game;
	public GatotScreen (GatotRun game) {
		this.game = game;
	}
}
