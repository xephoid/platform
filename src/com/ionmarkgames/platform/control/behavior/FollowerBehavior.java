package com.ionmarkgames.platform.control.behavior;

import com.ionmarkgames.platform.control.PlatformObjectController;
import com.ionmarkgames.platform.model.PlatformState;
import com.ionmarkgames.platform.model.gfx.PlatformDirectionEnum;
import com.ionmarkgames.platform.model.gfx.PlatformObject;
import com.ionmarkgames.platform.sound.SoundManager;

public class FollowerBehavior extends AbstractBehavior {

	@Override
	public void act(PlatformObjectController controller, PlatformState state,
			PlatformObject po) {
		PlatformObject target = po.getTarget();
		if (target != null && po.getRiders().isEmpty()) {
			if (po.getCounter() == 0 && !target.isFalling() && !target.isJumping() && target.getY() > po.getY()) {
				controller.jump(state, po);
				po.setCounter((int) (Math.random() * 50));
			}
			if (!controller.overlap(po.getX(), po.getX() + po.getWidth(), target.getX(), target.getX() + target.getWidth())) {
				if (target.getX() > po.getX()) {
					controller.changeDirection(state, po, PlatformDirectionEnum.RIGHT);
					po.setMoving(true);
				} else if (target.getX() < po.getX()) {
					controller.changeDirection(state, po, PlatformDirectionEnum.LEFT);
					po.setMoving(true);
				}
			} else {
				po.setMoving(false);
			}
		} else {
			po.setMoving(false);
		}
		
		if (po.getCounter() > 0) {
			po.setCounter(po.getCounter() - 1);
		}
		controller.move(state, po);
	}

	@Override
	public void handleUse(PlatformObjectController controller,
			PlatformState state, PlatformObject src, PlatformObject dest) {
		if (dest.getTarget() != src) {
			dest.setTarget(src);
			dest.setInteracted(true);
			SoundManager.playPowerUpSound();
		}
	}
}