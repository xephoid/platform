package com.ionmarkgames.platform.engine;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class PlatformMainClass {

	public static void main(String[] args) {
		new LwjglApplication(new PlatformApp(), "Test", 800, 600, false);
	}

}
