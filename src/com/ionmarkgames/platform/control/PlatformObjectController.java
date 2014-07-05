package com.ionmarkgames.platform.control;

import com.ionmarkgames.platform.control.behavior.IBehavior;
import com.ionmarkgames.platform.control.behavior.IPlatformInteractionBoolean;
import com.ionmarkgames.platform.control.behavior.PlatformObjectInteractionEnum;
import com.ionmarkgames.platform.exception.PlatformException;
import com.ionmarkgames.platform.model.PlatformState;
import com.ionmarkgames.platform.model.PlatformTerrainCell;
import com.ionmarkgames.platform.model.gfx.PlatformDirectionEnum;
import com.ionmarkgames.platform.model.gfx.PlatformObject;
import com.ionmarkgames.platform.model.gfx.PlatformObjectStateEnum;
import com.ionmarkgames.platform.sound.SoundManager;

public class PlatformObjectController {

	public void behave(PlatformState pstate, PlatformObject po) {
		IBehavior behavior = po.getBehavior();
		if (behavior != null) {
			behavior.act(this, pstate, po);
		} else {
			throw new PlatformException("Object without Behavior! " + po);
		}
	}
	
	public void jump(PlatformState pstate, PlatformObject po) {
		if (!po.isFalling() && !po.isJumping()) {
			po.setJumping(true);
			po.setJumpVelocity(po.getMaxJump());
			PlatformObject platform = po.getStandingOn();
			if (platform != null) {
				platform.getRiders().remove(po);
				po.setStandingOn(null);
			}
			SoundManager.playJumpSound();
		}
	}
	
	public void fall(PlatformState pstate, PlatformObject po) {
		po.setJumping(false);
		po.setFalling(true);
		if (po.getStandingOn() != null) {
			po.getStandingOn().getRiders().remove(po);
			po.setStandingOn(null);
		}
	}
	
	public void move(PlatformState pstate, PlatformObject po) {
		float destX = po.getX();
		float destY = po.getY();
		
		if (po.isMoving()) {
			po.setState(PlatformObjectStateEnum.WALK);
			switch(po.getDirection()) {
				case LEFT:
					if (po.getVelocity() > -po.getSpeed()) {
						po.setVelocity(po.getVelocity() - PlatformObject.VELOCITY_INCREMENT);
					}
					break;
				case RIGHT:
					if (po.getVelocity() < po.getSpeed()) {
						po.setVelocity(po.getVelocity() + PlatformObject.VELOCITY_INCREMENT);
					}
					break;
				default:
					break;
			}
		} else {
			if (po.getVelocity() > 0) {
				po.setVelocity(po.getVelocity() - PlatformObject.VELOCITY_INCREMENT);
			} else if (po.getVelocity() < 0) {
				po.setVelocity(po.getVelocity() + PlatformObject.VELOCITY_INCREMENT);
			} else {
				po.setState(PlatformObjectStateEnum.STAND);
			}
		}
		
		for(PlatformTerrainCell cell : po.getCells()) {
			for(PlatformObject wall : cell.get(PlatformObjectInteractionEnum.STOP)) {
				if (po == wall || !checkProperty(PlatformObjectInteractionEnum.STOP, po, wall)) continue;
				
				if (overlap(po.getY() + 1, po.getY() + po.getHeight() - 1, wall.getY(), wall.getY() + wall.getHeight())) {
					if ((po.getVelocity() > 0
							&& po.getX() + po.getWidth() <= wall.getX()
							&& po.getX() + po.getWidth() + po.getVelocity() > wall.getX())) {
						float difference = wall.getX() - (po.getX() + po.getWidth()); 
						po.setVelocity(difference);
						if (difference == 0) {
							wall.getBehavior().interact(PlatformObjectInteractionEnum.STOP, this, pstate, po, wall);
						}
					} else if (po.getVelocity() < 0
							&& po.getX() >= wall.getX() + wall.getWidth()
							&& po.getX() + po.getVelocity() < wall.getX() + wall.getWidth()) {
						float difference = (wall.getX() + wall.getWidth()) - po.getX(); 
						po.setVelocity(difference);
						if (difference == 0) {
							wall.getBehavior().interact(PlatformObjectInteractionEnum.STOP, this, pstate, po, wall);
						}
					}
				}
			}
		}
		
		destX = po.getX() + po.getVelocity();
		
		if (po.isFalling()) {
			if (po.getJumpVelocity() > -po.getMaxJump()) {
				po.setJumpVelocity(po.getJumpVelocity() - PlatformObject.VELOCITY_INCREMENT);
			}
			for(PlatformTerrainCell cell : po.getCells()) {
				for (PlatformObject platform : cell.get(PlatformObjectInteractionEnum.RIDE)) {
					if (po == platform || !checkProperty(PlatformObjectInteractionEnum.RIDE, po, platform)) continue;
					float platformTop = platform.getY() + platform.getHeight();
					
					if (overlap(po.getX() + 1, po.getX() + po.getWidth() - 1, platform.getX(), platform.getX() + platform.getWidth())
						&& po.getY() >= platformTop
						&& po.getY() + po.getJumpVelocity() < platformTop) {
						po.setJumpVelocity(0);
						po.setY(platformTop);
						po.setFalling(false);
						platform.getBehavior().interact(PlatformObjectInteractionEnum.RIDE, this, pstate, po, platform);
						platform.getRiders().add(po);
						po.setStandingOn(platform);
					}
				}
			}
			if (po.getJumpVelocity() != 0) {
				po.setState(PlatformObjectStateEnum.FALL);
			}
			destY = po.getY() + po.getJumpVelocity();
		} else {
			if (!po.isJumping()) {
				PlatformObject platform = po.getStandingOn();
				if (platform == null || 
					(!overlap(po.getX() + 1, po.getX() + po.getWidth() - 1, platform.getX(), platform.getX() + platform.getWidth()))) {
					fall(pstate, po);
				}
			} else if (po.isJumping()) {
				po.setJumpVelocity(po.getJumpVelocity() - PlatformObject.VELOCITY_INCREMENT);
				po.setState(PlatformObjectStateEnum.JUMP);
				for(PlatformTerrainCell cell : po.getCells()) {
					for (PlatformObject wall : cell.get(PlatformObjectInteractionEnum.STOP)) {
						if (po == wall || !checkProperty(PlatformObjectInteractionEnum.STOP, po, wall)) continue;
						if (overlap(po.getX() + 1, po.getX() + po.getWidth() - 1, wall.getX(), wall.getX() + wall.getWidth())
							&& po.getY() + po.getHeight() < wall.getY()
							&& po.getY() + po.getHeight() + po.getJumpVelocity() > wall.getY()) {
							po.setJumpVelocity(wall.getY() - (po.getY() + po.getHeight()));
							wall.getBehavior().interact(PlatformObjectInteractionEnum.STOP, this, pstate, po, wall);
							po.setJumpVelocity(0);
							fall(pstate, po);
						}
					}
				}
				
				destY = po.getY() + po.getJumpVelocity();
				if (po.getJumpVelocity() < 1) {
					fall(pstate, po);
				}
			}
		}
		
		teleport(pstate, po, destX, destY);
	}
	
	public void teleport(PlatformState pstate, PlatformObject po, float x, float y) {
		float diffX = x - po.getX();
		float diffY = y - po.getY();
		
		for(PlatformTerrainCell cell : po.getCells()) {
			for(PlatformObject wall : cell.get(PlatformObjectInteractionEnum.STOP)) {
				if (po == wall || !checkProperty(PlatformObjectInteractionEnum.STOP, po, wall)) continue;
				if (overlap(x + 1, x + po.getWidth() - 1, wall.getX(), wall.getX() + wall.getWidth())
						&& overlap(y + 1, y + po.getHeight() - 1, wall.getY(), wall.getY() + wall.getHeight())) {
					if (overlap(x, x + po.getWidth(), wall.getX(), wall.getX() + wall.getWidth())
							&& overlap(po.getY(), po.getY() + po.getHeight(), wall.getY(), wall.getY() + wall.getHeight())) {
						x = po.getX()  + (diffX > 0 ? -1 : 1);
					}
					if (overlap(po.getX(), po.getX() + po.getWidth(), wall.getX(), wall.getX() + wall.getWidth())
							&& overlap(y + 1, y + po.getHeight() - 1, wall.getY(), wall.getY() + wall.getHeight())) {
						y = po.getY() + (diffY > 0 ? -1 : 1);
					}
				}
			}
		}
		
		removeFromCells(po);
		po.setX(x);
		po.setY(y);
		po.setCells(pstate.getTerrain().getCells(po));
		addToCells(pstate, po);
		
		for(PlatformObject rider : po.getRiders()) {
			teleport(pstate, rider, rider.getX() + diffX, rider.getY() + diffY);
		}
	}
	
	public void changeState(PlatformState pstate, PlatformObject po, PlatformObjectStateEnum state) {
		po.setState(state);
	}
	
	public void changeDirection(PlatformState pstate, PlatformObject po, PlatformDirectionEnum direction) {
		po.setDirection(direction);
	}
	
	public void incrementFrame(PlatformState pstate, PlatformObject po, int amount) {
		po.setFrame(po.getFrame() + amount);
	}
	
	public void die(PlatformState pstate, PlatformObject po) {
		po.setDead(true);
	}
	
	public void interact(PlatformState pstate, PlatformObject po) {
		for(PlatformTerrainCell cell : po.getCells()) {
			for (PlatformObject thing : cell.get(PlatformObjectInteractionEnum.USE)) {
				if (overlap(po.getX(), po.getX() + po.getWidth(), thing.getX(), thing.getX() + thing.getWidth())
					&& overlap(po.getY(), po.getY() + po.getHeight(), thing.getY(), thing.getY() + thing.getHeight())) {
					thing.getBehavior().interact(PlatformObjectInteractionEnum.USE, this, pstate, po, thing);
				}
			}
		}
	}
	
	private void addToCells(PlatformState pstate, PlatformObject po) {
		for(PlatformTerrainCell cell : po.getCells()) {
			cell.add(po);
			for(PlatformObject collidable : cell.get(PlatformObjectInteractionEnum.COLLECT)) {
				if (po == collidable || !checkProperty(PlatformObjectInteractionEnum.COLLECT, po, collidable)) continue;
				if (overlap(po.getX(), po.getX() + po.getWidth(), collidable.getX(), collidable.getX() + collidable.getWidth())
					&& overlap(po.getY(), po.getY() + po.getHeight(), collidable.getY(), collidable.getY() + collidable.getHeight())) {
					collidable.getBehavior().interact(PlatformObjectInteractionEnum.COLLECT, this, pstate, po, collidable);
				}
			}
			
			// TODO: get rid of this...
			if (po.getBehavior() == pstate.getPlayer().getBehavior()) {
				for(PlatformObject messeger : cell.get(PlatformObjectInteractionEnum.USE)) {
					if (overlap(po.getX(), po.getX() + po.getWidth(), messeger.getX(), messeger.getX() + messeger.getWidth())
							&& overlap(po.getY(), po.getY() + po.getHeight(), messeger.getY(), messeger.getY() + messeger.getHeight())
							&& !messeger.isInteracted()) {
						messeger.setShowMessage(true);
					}
				}
			}
		}
	}
	
	public boolean checkProperty(PlatformObjectInteractionEnum prop, PlatformObject src, PlatformObject dest) {
		IPlatformInteractionBoolean bool = dest.getProfile().get(prop);
		return bool != null && bool.is(src);
	}
	
	private void removeFromCells(PlatformObject po) {
		for(PlatformTerrainCell cell : po.getCells()) {
			cell.remove(po);
		}
	}
	
	public boolean overlap(PlatformObject a, PlatformObject b) {
		return overlap(a.getX() + 1, a.getX() + a.getWidth() - 1, b.getX(), b.getX() + b.getWidth())
			&& overlap(a.getY() + 1, a.getY() + a.getHeight() - 1, b.getY(), b.getY() + b.getHeight());
	}
	
	public boolean overlap(float amin, float amax, float bmin, float bmax) {
		return (amin >= bmin && amin <= bmax)
			|| (amax >= bmin && amax <= bmax)
			|| (bmin >= amin && bmin <= amax)
			|| (bmax >= amin && bmax <= amax);
	}
}