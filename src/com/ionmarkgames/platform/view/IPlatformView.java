package com.ionmarkgames.platform.view;

import com.badlogic.gdx.graphics.Camera;
import com.ionmarkgames.platform.model.PlatformState;

public interface IPlatformView {

	public void init(PlatformState gamestate);
	public void render(PlatformState gamestate);
	
	public void setCamera(Camera camera);
}
