package com.yondev.gatot.run.helper;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class FontAccessor implements TweenAccessor<BitmapFont> {

	public static final int ALPHA = 1;

	public int getValues(BitmapFont target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case ALPHA:
			returnValues[0] = target.getColor().a;
			
			return 1;
		default:
			return 0;
		}
	}

	public void setValues(BitmapFont target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case ALPHA:
			target.setColor(1, 1, 1, newValues[0]);
			break;
		}
	}

}
