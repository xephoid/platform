package com.ionmarkgames.platform.engine;

import com.badlogic.gdx.ApplicationListener;
import com.ionmarkgames.platform.control.MainController;

public class PlatformApp implements ApplicationListener {

	private MainController main = new MainController();
	
	@Override
	public void create() {
		main.init();
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		main.loop();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

}
