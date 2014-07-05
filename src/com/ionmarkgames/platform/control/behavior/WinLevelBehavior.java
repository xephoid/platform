package com.ionmarkgames.platform.control.behavior;

import com.ionmarkgames.platform.control.PlatformObjectController;
import com.ionmarkgames.platform.model.PlatformState;
import com.ionmarkgames.platform.model.gfx.PlatformObject;
import com.ionmarkgames.platform.sound.SoundManager;

public class WinLevelBehavior extends AbstractBehavior {

	@Override
	public void act(PlatformObjectController controller, PlatformState state,
			PlatformObject po) {
		// no-op
	}

	@Override
	public void handleCollect(PlatformObjectController controller,
			PlatformState state, PlatformObject src, PlatformObject dest) {
		float x = src.getX();
		float y = src.getY();
		
		if (src.getX() > dest.getX()) {
			x = src.getX() - .5f;
		} else if (src.getX() > dest.getX()) {
			x = src.getX() + 1;
		}
		
		if (src.getY() > dest.getY()) {
			y = src.getY() - .5f;
		} else if (src.getY() > dest.getY()) {
			y = src.getY() + 1;
		}
		
		//controller.teleport(state, src, x, y);
		src.setX(x);
		src.setY(y);
		
		if (Math.abs(src.getX() - dest.getX()) < 1 && Math.abs(src.getY() - dest.getY()) < 1) {
			state.setLevelWin(true);
			state.getPlayer().setBehavior(new NullBehavior());
			SoundManager.stopLevelMusic();
			SoundManager.playFailSound();
		}
	}

}