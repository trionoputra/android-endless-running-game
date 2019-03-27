package com.yondev.gatot.run.helper;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteAccessor implements TweenAccessor<Sprite> {

	public static final int ALPHA = 1;
	public static final int SCALE = 2;
	public static final int POSXY = 3;

	public int getValues(Sprite target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case ALPHA:
			returnValues[0] = target.getColor().a;
			return 1;
		case SCALE:
			returnValues[0] = target.getScaleX();
			returnValues[1] = target.getScaleY();                 
			 return 2;
		case POSXY:
			returnValues[0] = target.getX();                 
			returnValues[1] = target.getY();
			 return 3;
		default:
			return 0;
		}
	}

	public void setValues(Sprite target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case ALPHA:
			target.setColor(1, 1, 1, newValues[0]);
			break;
		case SCALE:
			target.setScale(newValues[0]);
			break;
		case POSXY:
			target.setPosition(newValues[0], newValues[1]);
			break;
		}
	}

}
