package com.ionmarkgames.platform.model.gfx;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ionmarkgames.platform.control.behavior.IBehavior;
import com.ionmarkgames.platform.control.behavior.IPlatformInteractionBoolean;
import com.ionmarkgames.platform.control.behavior.PlatformObjectInteractionEnum;
import com.ionmarkgames.platform.exception.PlatformException;
import com.ionmarkgames.platform.model.PlatformTerrainCell;

@SuppressWarnings("serial")
public class PlatformObject {
	
	private static int count = 0;
	private static Map<PlatformObjectInteractionEnum, IPlatformInteractionBoolean> BASIC_PROFILE = new HashMap<PlatformObjectInteractionEnum, IPlatformInteractionBoolean>(){{
	}};
	
	public static final float VELOCITY_INCREMENT = 1;
	
	private int id = 0;
	private float x = 0;
	private float y = 0;
	private int width = 0;
	private int height = 0;
	private int frame = 0;
	private boolean isStatic = false;
	private IBehavior behavior = null;
	private boolean isDead = false;
	private boolean isJumping = false;
	private boolean isFalling = true;
	private boolean isMoving = false;
	private boolean isVisible = true;
	private boolean showMessage = false;
	private boolean interacted = false;
	private int layer = 0;
	private PlatformDirectionEnum direction = PlatformDirectionEnum.NONE;
	private PlatformObjectStateEnum state = PlatformObjectStateEnum.STAND;
	private float speed = 0;
	private float velocity = 0;
	private float jumpVelocity = 0;
	private float maxJump = 13;
	private int damage = 0;
	private int counter = 0;
	private PlatformTerrainCell[] cells = new PlatformTerrainCell[0];
	private PlatformObject standingOn = null;
	private Set<PlatformObject> riders = new HashSet<PlatformObject>();
	private PlatformObject target = null;
	
	private Map<PlatformObjectStateEnum, Map<PlatformDirectionEnum, List<TextureRegion>>> assetMap = new HashMap<PlatformObjectStateEnum, Map<PlatformDirectionEnum, List<TextureRegion>>>();
	private Map<PlatformObjectInteractionEnum, IPlatformInteractionBoolean> profile = BASIC_PROFILE;
	
	public PlatformObject() {
		this.setId(count++);
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof PlatformObject) {
			PlatformObject go = (PlatformObject) other;
			return go.id == this.id;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public String toString() {
		return "PO " +id+ " (" + x + ", " + y + ") " + state + " | " + direction + " | " + frame;
	}
	
	public TextureRegion getTexture() {
		Map<PlatformDirectionEnum, List<TextureRegion>> stateMap = assetMap.get(state);
		if (stateMap != null) {
			List<TextureRegion> frameList = stateMap.get(direction);
			if (frameList != null) {
				return frameList.get(frame);
			}
		}
		throw new PlatformException("No such texture for " + this);
	}
	
	public int getFrameCount() {
		Map<PlatformDirectionEnum, List<TextureRegion>> stateMap = assetMap.get(state);
		if (stateMap != null) {
			List<TextureRegion> frameList = stateMap.get(direction);
			if (frameList != null) {
				return frameList.size();
			}
		}
		return 0;
	}
	
	public Map<PlatformObjectStateEnum, Map<PlatformDirectionEnum, List<TextureRegion>>> getAssets() {
		return assetMap;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getX() {
		return x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getY() {
		return y;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}

	public void setFrame(int frame) {
		this.frame = frame;
	}

	public int getFrame() {
		return frame;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public void setBehavior(IBehavior behavior) {
		this.behavior = behavior;
	}

	public IBehavior getBehavior() {
		return behavior;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setJumping(boolean isJumping) {
		this.isJumping = isJumping;
	}

	public boolean isJumping() {
		return isJumping;
	}

	public void setFalling(boolean isFalling) {
		this.isFalling = isFalling;
	}

	public boolean isFalling() {
		return isFalling;
	}

	public void setDirection(PlatformDirectionEnum direction) {
		this.direction = direction;
	}

	public PlatformDirectionEnum getDirection() {
		return direction;
	}

	public void setState(PlatformObjectStateEnum state) {
		this.state = state;
	}

	public PlatformObjectStateEnum getState() {
		return state;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getSpeed() {
		return speed;
	}

	public void setJumpVelocity(float jumpVelocity) {
		this.jumpVelocity = jumpVelocity;
	}

	public float getJumpVelocity() {
		return jumpVelocity;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getDamage() {
		return damage;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public int getCounter() {
		return counter;
	}

	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	public float getVelocity() {
		return velocity;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int getLayer() {
		return layer;
	}

	public void setCells(PlatformTerrainCell[] cells) {
		this.cells = cells;
	}

	public PlatformTerrainCell[] getCells() {
		return cells;
	}

	public void setStandingOn(PlatformObject standingOn) {
		this.standingOn = standingOn;
	}

	public PlatformObject getStandingOn() {
		return standingOn;
	}

	public void setRiders(Set<PlatformObject> riders) {
		this.riders = riders;
	}

	public Set<PlatformObject> getRiders() {
		return riders;
	}

	public void setTarget(PlatformObject target) {
		this.target = target;
	}

	public PlatformObject getTarget() {
		return target;
	}

	public void setShowMessage(boolean showMessage) {
		this.showMessage = showMessage;
	}

	public boolean isShowMessage() {
		return showMessage;
	}

	public void setMaxJump(float maxJump) {
		this.maxJump = maxJump;
	}

	public float getMaxJump() {
		return maxJump;
	}

	public void setInteracted(boolean interacted) {
		this.interacted = interacted;
	}

	public boolean isInteracted() {
		return interacted;
	}

	public void setProfile(Map<PlatformObjectInteractionEnum, IPlatformInteractionBoolean> profile) {
		this.profile = profile;
	}

	public Map<PlatformObjectInteractionEnum, IPlatformInteractionBoolean> getProfile() {
		return profile;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public boolean isVisible() {
		return isVisible;
	}

}