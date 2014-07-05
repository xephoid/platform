package com.ionmarkgames.platform.control.behavior;

import com.ionmarkgames.platform.control.PlatformObjectController;
import com.ionmarkgames.platform.exception.PlatformException;
import com.ionmarkgames.platform.model.PlatformState;
import com.ionmarkgames.platform.model.gfx.PlatformObject;

public abstract class AbstractBehavior implements IBehavior {

	@Override
	public void interact(PlatformObjectInteractionEnum type,
			PlatformObjectController controller, PlatformState state,
			PlatformObject src, PlatformObject dest) {
		
		switch(type) {
			case BUMP:
				handleBump(controller, state, src, dest);
				break;
			case STOP:
				handleStop(controller, state, src, dest);
				break;
			case RIDE:
				handleRide(controller, state, src, dest);
				break;
			case USE:
				handleUse(controller, state, src, dest);
				break;
			case COLLECT:
				handleCollect(controller, state, src, dest);
				break;
			case ATTACK:
				handleAttack(controller, state, src, dest);
				break;
			case DISPLAY:
				handleDisplay(controller, state, src, dest);
				break;
			default:
				throw new PlatformException("Unhandled interaction type " +type+ "!");
		}
	}
	
	protected void handleBump(PlatformObjectController controller, PlatformState state,
			PlatformObject src, PlatformObject dest) {}
	
	protected void handleStop(PlatformObjectController controller, PlatformState state,
			PlatformObject src, PlatformObject dest) {}

	protected void handleRide(PlatformObjectController controller, PlatformState state,
			PlatformObject src, PlatformObject dest) {}
	
	protected void handleUse(PlatformObjectController controller, PlatformState state,
			PlatformObject src, PlatformObject dest) {}
	
	protected void handleCollect(PlatformObjectController controller, PlatformState state,
			PlatformObject src, PlatformObject dest) {}
	
	protected void handleAttack(PlatformObjectController controller, PlatformState state,
			PlatformObject src, PlatformObject dest) {}
	
	protected void handleDisplay(PlatformObjectController controller, PlatformState state,
			PlatformObject src, PlatformObject dest) {}
	
}
