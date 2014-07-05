package com.ionmarkgames.platform.control.behavior;

import com.ionmarkgames.platform.control.PlatformObjectController;
import com.ionmarkgames.platform.model.PlatformState;
import com.ionmarkgames.platform.model.gfx.PlatformObject;

public interface IBehavior {

	public void act(PlatformObjectController controller, PlatformState state, PlatformObject po);
	public void interact(PlatformObjectInteractionEnum type, PlatformObjectController controller, PlatformState state, PlatformObject src, PlatformObject dest);
	
}