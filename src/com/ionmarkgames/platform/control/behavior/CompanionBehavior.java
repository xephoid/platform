package com.ionmarkgames.platform.control.behavior;

import java.util.Map;

import com.ionmarkgames.platform.control.PlatformObjectController;
import com.ionmarkgames.platform.model.PlatformState;
import com.ionmarkgames.platform.model.gfx.PlatformObject;

public class CompanionBehavior extends AbstractBehavior {

	private int cooldown = 0;
	
	@Override
	public void act(PlatformObjectController controller, PlatformState state,
			PlatformObject po) {
		if (cooldown > 0) {
			cooldown--;
		}
		po.setMoving(false);
		controller.move(state, po);
	}

	@Override
	public void handleUse(PlatformObjectController controller,
			PlatformState state, PlatformObject src, PlatformObject dest) {
		if (cooldown == 0) {
			Map<PlatformObjectInteractionEnum, IPlatformInteractionBoolean> tmp = dest.getProfile();
		
			dest.setBehavior(src.getBehavior());
			dest.setProfile(src.getProfile());
		
			src.setBehavior(this);
			src.setProfile(tmp);
			cooldown = 30;
		}
	}
}
