package com.ionmarkgames.platform.util;

public class ClockUtil {

	private long startTime = 0;

	public void start() {
		startTime = System.currentTimeMillis();
	}

	public float getTime() {
		return (float)(System.currentTimeMillis() - startTime) * 0.001f;
	}

	public boolean isTimeYet(float time) {
		return (getTime() > time);
	}
	
}