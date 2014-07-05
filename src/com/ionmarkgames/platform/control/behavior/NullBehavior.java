package com.ionmarkgames.platform.control.behavior;

import com.ionmarkgames.platform.control.PlatformObjectController;
import com.ionmarkgames.platform.model.PlatformState;
import com.ionmarkgames.platform.model.gfx.PlatformObject;

public class NullBehavior extends AbstractBehavior {

	@Override
	public void act(PlatformObjectController controller, PlatformState state,
			PlatformObject po) {
		// no-op
	}

}