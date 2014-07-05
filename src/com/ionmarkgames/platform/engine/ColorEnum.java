package com.ionmarkgames.platform.engine;

import java.util.EnumSet;

public enum ColorEnum {
	BLACK(0, 0, 0),
	WHITE(1, 1, 1),
	RED(1, 0, 0),
	GREEN(0, 1, 0),
	BLUE(0, 0, 1),
	CYAN(0, 1, 1),
	YELLOW(1, 1, 0),
	PURPLE(1, 0, 1),
	;
	
	private float r,g,b;
	
	ColorEnum(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public float getDistance(int rgb) {
		float r = (float)(0xFF & (rgb >> 16)) / 255.0f;
		float g = (float)(0xFF & (rgb >> 8)) / 255.0f;
		float b = (float)(0xFF & (rgb)) / 255.0f;		
		return (float)Math.sqrt( (r - this.r) * (r - this.r) + (g - this.g) * (g - this.g) + (b - this.b) * (b - this.b) );
	}
	
	public static ColorEnum closest(int rgb) {
		float closest = Float.MAX_VALUE;
		float distance = 0;
		ColorEnum match = ColorEnum.WHITE;
		
		for(ColorEnum c : EnumSet.allOf(ColorEnum.class)) {
			distance = c.getDistance(rgb);
			if (distance < closest) {
				closest = distance;
				match = c;
			}
		}
		return match;
	}
}