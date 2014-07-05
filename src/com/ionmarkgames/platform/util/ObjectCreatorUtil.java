package com.ionmarkgames.platform.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ionmarkgames.platform.control.PlatformObjectController;
import com.ionmarkgames.platform.control.behavior.AbstractBehavior;
import com.ionmarkgames.platform.control.behavior.IBehavior;
import com.ionmarkgames.platform.control.behavior.IPlatformInteractionBoolean;
import com.ionmarkgames.platform.control.behavior.NullBehavior;
import com.ionmarkgames.platform.control.behavior.PlatformObjectInteractionEnum;
import com.ionmarkgames.platform.control.behavior.WinLevelBehavior;
import com.ionmarkgames.platform.model.PlatformState;
import com.ionmarkgames.platform.model.gfx.PlatformDirectionEnum;
import com.ionmarkgames.platform.model.gfx.PlatformObject;
import com.ionmarkgames.platform.model.gfx.PlatformObjectStateEnum;
import com.ionmarkgames.platform.sound.SoundManager;

@SuppressWarnings("serial")
public class ObjectCreatorUtil {

	private static IBehavior wallBehavoir = new NullBehavior();
	
	private static IBehavior movingWallBehavior = new AbstractBehavior() {
		@Override
		public void act(PlatformObjectController controller, PlatformState state,
				PlatformObject po) {
			if (po.isMoving()) {
				if (po.getY() > 96) {
					controller.teleport(state, po, po.getX(), po.getY() - 1);
				}
			}
		}
	};
	
	private static IBehavior switchBehavior = new AbstractBehavior() {
		
		@Override
		public void handleUse(PlatformObjectController controller,
				PlatformState state, PlatformObject src, PlatformObject dest) {
			if (PlatformDirectionEnum.DOWN.equals(dest.getDirection())) {
				dest.setDirection(PlatformDirectionEnum.UP);
				if (dest.getTarget() != null) {
					dest.getTarget().setMoving(true);
					dest.getTarget().setFrame(1);
					dest.setInteracted(true);
					SoundManager.playTreasureChestSound();
				}
			}
		}

		@Override
		public void act(PlatformObjectController controller,
				PlatformState state, PlatformObject po) {
			// no-op
		}
	};
	
	private static IBehavior coinBehavior = new AbstractBehavior() {
		
		@Override
		public void handleCollect(PlatformObjectController controller, PlatformState state, PlatformObject src, PlatformObject dest) {
			dest.setVisible(false);
			dest.setDead(true);
			SoundManager.playCoinSound();
		}
		
		@Override
		public void act(PlatformObjectController controller, PlatformState state,
				PlatformObject po) {
			po.setFrame((po.getFrame() + 1) % po.getFrameCount());
		}
	};
	
	private static Map<PlatformDirectionEnum, List<TextureRegion>> wallMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>(); 
	private static Map<PlatformDirectionEnum, List<TextureRegion>> blockMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
	private static Map<PlatformDirectionEnum, List<TextureRegion>> switchMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
	private static Map<PlatformDirectionEnum, List<TextureRegion>> coinMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
	private static Map<PlatformDirectionEnum, List<TextureRegion>> flagMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
	private static Map<PlatformObjectInteractionEnum, IPlatformInteractionBoolean> wallProfile = new HashMap<PlatformObjectInteractionEnum, IPlatformInteractionBoolean>(){{
		put(PlatformObjectInteractionEnum.STOP, new IPlatformInteractionBoolean() {
			@Override
			public boolean is(PlatformObject po) {
				return !po.isStatic();
			}
		});
		put(PlatformObjectInteractionEnum.RIDE, IPlatformInteractionBoolean.TRUE);
	}};
	
	private static Map<PlatformObjectInteractionEnum, IPlatformInteractionBoolean> usableProfile = new HashMap<PlatformObjectInteractionEnum, IPlatformInteractionBoolean>(){{
		put(PlatformObjectInteractionEnum.DISPLAY, IPlatformInteractionBoolean.TRUE);
		put(PlatformObjectInteractionEnum.USE, IPlatformInteractionBoolean.TRUE);
	}};
	
	private static Map<PlatformObjectInteractionEnum, IPlatformInteractionBoolean> collectableProfile = new HashMap<PlatformObjectInteractionEnum, IPlatformInteractionBoolean>(){{
		put(PlatformObjectInteractionEnum.COLLECT, IPlatformInteractionBoolean.TRUE);
	}};
	
	public static PlatformObject createWall(float x, float y, int width, int height) {
		PlatformObject wall = new PlatformObject();
		wall.setX(x);
		wall.setY(y);
		wall.setWidth(width);
		wall.setHeight(height);
		if (wallMap.get(PlatformDirectionEnum.NONE) == null) {
			List<TextureRegion> wallList = new ArrayList<TextureRegion>();
			wallList.add(new TextureRegion(new Texture(Gdx.files.internal("images/StoneBlockAtlas.png"))));
			wallMap.put(PlatformDirectionEnum.NONE, wallList);
		}
		wall.getAssets().put(PlatformObjectStateEnum.STAND, wallMap);
		wall.setProfile(wallProfile);
		wall.setStatic(true);
		
		wall.setBehavior(wallBehavoir);
		
		return wall;
	}
	
	public static PlatformObject createBlock(float x, float y, boolean controlled) {
		PlatformObject wall = new PlatformObject();
		wall.setX(x);
		wall.setY(y);
		wall.setWidth(32);
		wall.setHeight(32);
		if (blockMap.get(PlatformDirectionEnum.NONE) == null) {
			List<TextureRegion> wallList = new ArrayList<TextureRegion>();
			wallList.add(new TextureRegion(new Texture(Gdx.files.internal("images/wall-box.gif"))));
			blockMap.put(PlatformDirectionEnum.NONE, wallList);
			List<TextureRegion> upList = new ArrayList<TextureRegion>();
			upList.add(new TextureRegion(new Texture(Gdx.files.internal("images/wall-box_control.gif"))));
			upList.add(new TextureRegion(new Texture(Gdx.files.internal("images/wall-box_control_on.gif"))));
			blockMap.put(PlatformDirectionEnum.DOWN, upList);
		}
		wall.getAssets().put(PlatformObjectStateEnum.STAND, blockMap);
		wall.setProfile(wallProfile);
		wall.setStatic(true);
		
		if (controlled) {
			wall.setDirection(PlatformDirectionEnum.DOWN);
			wall.setBehavior(movingWallBehavior);
		} else {
			wall.setBehavior(wallBehavoir);
		}
		
		return wall;
	}
	
	public static PlatformObject createSwitch(float x, float y, int width, int height, PlatformObject target) {
		PlatformObject swich = new PlatformObject();
		swich.setX(x);
		swich.setY(y);
		swich.setWidth(width);
		swich.setHeight(height);
		if (!switchMap.containsKey(PlatformDirectionEnum.UP)) {
			List<TextureRegion> offList = new ArrayList<TextureRegion>();
			List<TextureRegion> onList = new ArrayList<TextureRegion>();
			offList.add(new TextureRegion(new Texture(Gdx.files.internal("images/switch-off.gif"))));
			onList.add(new TextureRegion(new Texture(Gdx.files.internal("images/switch-on.gif"))));
			switchMap.put(PlatformDirectionEnum.DOWN, offList);
			switchMap.put(PlatformDirectionEnum.UP, onList);
		}
		swich.getAssets().put(PlatformObjectStateEnum.STAND, switchMap);
		swich.setProfile(usableProfile);
		swich.setDirection(PlatformDirectionEnum.DOWN);
		swich.setTarget(target);
		
		swich.setBehavior(switchBehavior);
		return swich;
	}
	
	public static PlatformObject createJim(float x, float y, int width, int height, IBehavior behavior) {
		PlatformObject player = new PlatformObject();
		player.setX(x);
		player.setY(y);
		player.setWidth(width);
		player.setHeight(height);
		player.setBehavior(behavior);
		
		player.setSpeed(5);
		player.setDirection(PlatformDirectionEnum.RIGHT);
		
		List<TextureRegion> rightStandList = new ArrayList<TextureRegion>();
		List<TextureRegion> leftStandList = new ArrayList<TextureRegion>();
		rightStandList.add(new TextureRegion(new Texture(Gdx.files.internal("images/Jim_breathe001.gif"))));
		leftStandList.add(new TextureRegion(new Texture(Gdx.files.internal("images/Jim_breathe001.gif")), 64, 0, -64, 64));
		Map<PlatformDirectionEnum, List<TextureRegion>> standMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
		standMap.put(PlatformDirectionEnum.LEFT, leftStandList);
		standMap.put(PlatformDirectionEnum.RIGHT, rightStandList);
		
		List<TextureRegion> rightWalkList = new ArrayList<TextureRegion>();
		List<TextureRegion> leftWalkList = new ArrayList<TextureRegion>();
		rightWalkList.add(new TextureRegion(new Texture(Gdx.files.internal("images/Jim_run_east001.gif"))));
		leftWalkList.add(new TextureRegion(new Texture(Gdx.files.internal("images/Jim_run_east001.gif")), 64, 0, -64, 64));
		Map<PlatformDirectionEnum, List<TextureRegion>> walkMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
		walkMap.put(PlatformDirectionEnum.LEFT, leftWalkList);
		walkMap.put(PlatformDirectionEnum.RIGHT, rightWalkList);
		
		List<TextureRegion> rightJumpList = new ArrayList<TextureRegion>();
		List<TextureRegion> leftJumpList = new ArrayList<TextureRegion>();
		rightJumpList.add(new TextureRegion(new Texture(Gdx.files.internal("images/Jim_jump004.gif"))));
		leftJumpList.add(new TextureRegion(new Texture(Gdx.files.internal("images/Jim_jump004.gif")), 64, 0, -64, 64));
		Map<PlatformDirectionEnum, List<TextureRegion>> jumpMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
		jumpMap.put(PlatformDirectionEnum.LEFT, leftJumpList);
		jumpMap.put(PlatformDirectionEnum.RIGHT, rightJumpList);
		
		List<TextureRegion> rightLandList = new ArrayList<TextureRegion>();
		List<TextureRegion> leftLandList = new ArrayList<TextureRegion>();
		rightLandList.add(new TextureRegion(new Texture(Gdx.files.internal("images/Jim_fall001.gif"))));
		leftLandList.add(new TextureRegion(new Texture(Gdx.files.internal("images/Jim_fall001.gif")), 64, 0, -64, 64));
		Map<PlatformDirectionEnum, List<TextureRegion>> landMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
		landMap.put(PlatformDirectionEnum.LEFT, leftLandList);
		landMap.put(PlatformDirectionEnum.RIGHT, rightLandList);
		
		player.getAssets().put(PlatformObjectStateEnum.STAND, standMap);
		player.getAssets().put(PlatformObjectStateEnum.WALK, walkMap);
		player.getAssets().put(PlatformObjectStateEnum.JUMP, jumpMap);
		player.getAssets().put(PlatformObjectStateEnum.FALL, landMap);
		
		return player;
	}
	
	public static PlatformObject createSasquatch(float x, float y, int width, int height, IBehavior behavior) {
		PlatformObject player = new PlatformObject();
		player.setX(x);
		player.setY(y);
		player.setWidth(width);
		player.setHeight(height);
		player.setBehavior(behavior);
		
		player.setSpeed(3);
		player.setDirection(PlatformDirectionEnum.RIGHT);
		
		List<TextureRegion> rightStandList = new ArrayList<TextureRegion>();
		List<TextureRegion> leftStandList = new ArrayList<TextureRegion>();
		rightStandList.add(new TextureRegion(new Texture(Gdx.files.internal("images/Sasquatch_stand.gif"))));
		leftStandList.add(new TextureRegion(new Texture(Gdx.files.internal("images/Sasquatch_stand.gif")), 64, 0, -64, 64));
		Map<PlatformDirectionEnum, List<TextureRegion>> standMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
		standMap.put(PlatformDirectionEnum.LEFT, leftStandList);
		standMap.put(PlatformDirectionEnum.RIGHT, rightStandList);
		
		List<TextureRegion> rightWalkList = new ArrayList<TextureRegion>();
		List<TextureRegion> leftWalkList = new ArrayList<TextureRegion>();
		rightWalkList.add(new TextureRegion(new Texture(Gdx.files.internal("images/Sasquatch_walk.gif"))));
		leftWalkList.add(new TextureRegion(new Texture(Gdx.files.internal("images/Sasquatch_walk.gif")), 64, 0, -64, 64));
		Map<PlatformDirectionEnum, List<TextureRegion>> walkMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
		walkMap.put(PlatformDirectionEnum.LEFT, leftWalkList);
		walkMap.put(PlatformDirectionEnum.RIGHT, rightWalkList);
		
		List<TextureRegion> rightJumpList = new ArrayList<TextureRegion>();
		List<TextureRegion> leftJumpList = new ArrayList<TextureRegion>();
		rightJumpList.add(new TextureRegion(new Texture(Gdx.files.internal("images/Sasquatch_jump.gif"))));
		leftJumpList.add(new TextureRegion(new Texture(Gdx.files.internal("images/Sasquatch_jump.gif")), 64, 0, -64, 64));
		Map<PlatformDirectionEnum, List<TextureRegion>> jumpMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
		jumpMap.put(PlatformDirectionEnum.LEFT, leftJumpList);
		jumpMap.put(PlatformDirectionEnum.RIGHT, rightJumpList);
		
		List<TextureRegion> rightLandList = new ArrayList<TextureRegion>();
		List<TextureRegion> leftLandList = new ArrayList<TextureRegion>();
		rightLandList.add(new TextureRegion(new Texture(Gdx.files.internal("images/Sasquatch_jump.gif"))));
		leftLandList.add(new TextureRegion(new Texture(Gdx.files.internal("images/Sasquatch_jump.gif")), 64, 0, -64, 64));
		Map<PlatformDirectionEnum, List<TextureRegion>> landMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
		landMap.put(PlatformDirectionEnum.LEFT, leftLandList);
		landMap.put(PlatformDirectionEnum.RIGHT, rightLandList);
		
		player.getAssets().put(PlatformObjectStateEnum.STAND, standMap);
		player.getAssets().put(PlatformObjectStateEnum.WALK, walkMap);
		player.getAssets().put(PlatformObjectStateEnum.JUMP, jumpMap);
		player.getAssets().put(PlatformObjectStateEnum.FALL, landMap);
		
		return player;
	}
	
	public static PlatformObject createPotemkin(float x, float y, IBehavior behavior) {
		PlatformObject player = new PlatformObject();
		player.setX(x);
		player.setY(y);
		player.setWidth(64);
		player.setHeight(64);
		player.setBehavior(behavior);
		
		player.setSpeed(3);
		player.setDirection(PlatformDirectionEnum.RIGHT);
		
		List<TextureRegion> rightStandList = new ArrayList<TextureRegion>();
		List<TextureRegion> leftStandList = new ArrayList<TextureRegion>();
		rightStandList.add(new TextureRegion(new Texture(Gdx.files.internal("images/Potemkin_stand.gif"))));
		leftStandList.add(new TextureRegion(new Texture(Gdx.files.internal("images/Potemkin_stand.gif")), 64, 0, -64, 64));
		Map<PlatformDirectionEnum, List<TextureRegion>> standMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
		standMap.put(PlatformDirectionEnum.LEFT, leftStandList);
		standMap.put(PlatformDirectionEnum.RIGHT, rightStandList);
		
		List<TextureRegion> rightWalkList = new ArrayList<TextureRegion>();
		List<TextureRegion> leftWalkList = new ArrayList<TextureRegion>();
		rightWalkList.add(new TextureRegion(new Texture(Gdx.files.internal("images/Potemkin_walk.gif"))));
		leftWalkList.add(new TextureRegion(new Texture(Gdx.files.internal("images/Potemkin_walk.gif")), 64, 0, -64, 64));
		Map<PlatformDirectionEnum, List<TextureRegion>> walkMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
		walkMap.put(PlatformDirectionEnum.LEFT, leftWalkList);
		walkMap.put(PlatformDirectionEnum.RIGHT, rightWalkList);
		
		List<TextureRegion> rightJumpList = new ArrayList<TextureRegion>();
		List<TextureRegion> leftJumpList = new ArrayList<TextureRegion>();
		rightJumpList.add(new TextureRegion(new Texture(Gdx.files.internal("images/Potemkin_jump.gif"))));
		leftJumpList.add(new TextureRegion(new Texture(Gdx.files.internal("images/Potemkin_jump.gif")), 64, 0, -64, 64));
		Map<PlatformDirectionEnum, List<TextureRegion>> jumpMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
		jumpMap.put(PlatformDirectionEnum.LEFT, leftJumpList);
		jumpMap.put(PlatformDirectionEnum.RIGHT, rightJumpList);
		
		List<TextureRegion> rightLandList = new ArrayList<TextureRegion>();
		List<TextureRegion> leftLandList = new ArrayList<TextureRegion>();
		rightLandList.add(new TextureRegion(new Texture(Gdx.files.internal("images/Potemkin_jump.gif"))));
		leftLandList.add(new TextureRegion(new Texture(Gdx.files.internal("images/Potemkin_jump.gif")), 64, 0, -64, 64));
		Map<PlatformDirectionEnum, List<TextureRegion>> landMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
		landMap.put(PlatformDirectionEnum.LEFT, leftLandList);
		landMap.put(PlatformDirectionEnum.RIGHT, rightLandList);
		
		player.getAssets().put(PlatformObjectStateEnum.STAND, standMap);
		player.getAssets().put(PlatformObjectStateEnum.WALK, walkMap);
		player.getAssets().put(PlatformObjectStateEnum.JUMP, jumpMap);
		player.getAssets().put(PlatformObjectStateEnum.FALL, landMap);
		
		return player;
	}
	
	public static PlatformObject createBoxy(float x, float y, IBehavior behavior) {
		PlatformObject player = new PlatformObject();
		player.setX(x);
		player.setY(y);
		player.setWidth(32);
		player.setHeight(32);
		player.setBehavior(behavior);
		
		player.setSpeed(3);
		player.setDirection(PlatformDirectionEnum.RIGHT);
		
		List<TextureRegion> rightStandList = new ArrayList<TextureRegion>();
		List<TextureRegion> leftStandList = new ArrayList<TextureRegion>();
		
		TextureRegion boxyRight = new TextureRegion(new Texture(Gdx.files.internal("images/boxy.gif")));
		TextureRegion boxyLeft = new TextureRegion(new Texture(Gdx.files.internal("images/boxy.gif")), 32, 0, -32, 32);
		
		
		rightStandList.add(boxyRight);
		leftStandList.add(boxyLeft);
		Map<PlatformDirectionEnum, List<TextureRegion>> standMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
		standMap.put(PlatformDirectionEnum.LEFT, leftStandList);
		standMap.put(PlatformDirectionEnum.RIGHT, rightStandList);
		
		List<TextureRegion> rightWalkList = new ArrayList<TextureRegion>();
		List<TextureRegion> leftWalkList = new ArrayList<TextureRegion>();
		rightWalkList.add(boxyRight);
		leftWalkList.add(boxyLeft);
		Map<PlatformDirectionEnum, List<TextureRegion>> walkMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
		walkMap.put(PlatformDirectionEnum.LEFT, leftWalkList);
		walkMap.put(PlatformDirectionEnum.RIGHT, rightWalkList);
		
		List<TextureRegion> rightJumpList = new ArrayList<TextureRegion>();
		List<TextureRegion> leftJumpList = new ArrayList<TextureRegion>();
		rightJumpList.add(boxyRight);
		leftJumpList.add(boxyLeft);
		Map<PlatformDirectionEnum, List<TextureRegion>> jumpMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
		jumpMap.put(PlatformDirectionEnum.LEFT, leftJumpList);
		jumpMap.put(PlatformDirectionEnum.RIGHT, rightJumpList);
		
		List<TextureRegion> rightLandList = new ArrayList<TextureRegion>();
		List<TextureRegion> leftLandList = new ArrayList<TextureRegion>();
		rightLandList.add(boxyRight);
		leftLandList.add(boxyLeft);
		Map<PlatformDirectionEnum, List<TextureRegion>> landMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
		landMap.put(PlatformDirectionEnum.LEFT, leftLandList);
		landMap.put(PlatformDirectionEnum.RIGHT, rightLandList);
		
		player.getAssets().put(PlatformObjectStateEnum.STAND, standMap);
		player.getAssets().put(PlatformObjectStateEnum.WALK, walkMap);
		player.getAssets().put(PlatformObjectStateEnum.JUMP, jumpMap);
		player.getAssets().put(PlatformObjectStateEnum.FALL, landMap);
		
		return player;
	}
	
	public static PlatformObject createWhitey(float x, float y, IBehavior behavior) {
		PlatformObject player = new PlatformObject();
		player.setX(x);
		player.setY(y);
		player.setWidth(32);
		player.setHeight(32);
		player.setBehavior(behavior);
		
		player.setSpeed(3);
		player.setDirection(PlatformDirectionEnum.LEFT);
		
		List<TextureRegion> rightStandList = new ArrayList<TextureRegion>();
		List<TextureRegion> leftStandList = new ArrayList<TextureRegion>();
		
		TextureRegion boxyRight = new TextureRegion(new Texture(Gdx.files.internal("images/white-box.gif")));
		TextureRegion boxyLeft = new TextureRegion(new Texture(Gdx.files.internal("images/white-box.gif")), 32, 0, -32, 32);
		
		
		rightStandList.add(boxyRight);
		leftStandList.add(boxyLeft);
		Map<PlatformDirectionEnum, List<TextureRegion>> standMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
		standMap.put(PlatformDirectionEnum.LEFT, leftStandList);
		standMap.put(PlatformDirectionEnum.RIGHT, rightStandList);
		
		List<TextureRegion> rightWalkList = new ArrayList<TextureRegion>();
		List<TextureRegion> leftWalkList = new ArrayList<TextureRegion>();
		rightWalkList.add(boxyRight);
		leftWalkList.add(boxyLeft);
		Map<PlatformDirectionEnum, List<TextureRegion>> walkMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
		walkMap.put(PlatformDirectionEnum.LEFT, leftWalkList);
		walkMap.put(PlatformDirectionEnum.RIGHT, rightWalkList);
		
		List<TextureRegion> rightJumpList = new ArrayList<TextureRegion>();
		List<TextureRegion> leftJumpList = new ArrayList<TextureRegion>();
		rightJumpList.add(boxyRight);
		leftJumpList.add(boxyLeft);
		Map<PlatformDirectionEnum, List<TextureRegion>> jumpMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
		jumpMap.put(PlatformDirectionEnum.LEFT, leftJumpList);
		jumpMap.put(PlatformDirectionEnum.RIGHT, rightJumpList);
		
		List<TextureRegion> rightLandList = new ArrayList<TextureRegion>();
		List<TextureRegion> leftLandList = new ArrayList<TextureRegion>();
		rightLandList.add(boxyRight);
		leftLandList.add(boxyLeft);
		Map<PlatformDirectionEnum, List<TextureRegion>> landMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
		landMap.put(PlatformDirectionEnum.LEFT, leftLandList);
		landMap.put(PlatformDirectionEnum.RIGHT, rightLandList);
		
		player.getAssets().put(PlatformObjectStateEnum.STAND, standMap);
		player.getAssets().put(PlatformObjectStateEnum.WALK, walkMap);
		player.getAssets().put(PlatformObjectStateEnum.JUMP, jumpMap);
		player.getAssets().put(PlatformObjectStateEnum.FALL, landMap);
		
		return player;
	}
	
	public static PlatformObject createYellow(float x, float y, IBehavior behavior) {
		PlatformObject player = new PlatformObject();
		player.setX(x);
		player.setY(y);
		player.setWidth(32);
		player.setHeight(32);
		player.setBehavior(behavior);
		
		player.setSpeed(3);
		player.setDirection(PlatformDirectionEnum.LEFT);
		
		List<TextureRegion> rightStandList = new ArrayList<TextureRegion>();
		List<TextureRegion> leftStandList = new ArrayList<TextureRegion>();
		
		TextureRegion boxyRight = new TextureRegion(new Texture(Gdx.files.internal("images/yellow-box.gif")));
		TextureRegion boxyLeft = new TextureRegion(new Texture(Gdx.files.internal("images/yellow-box.gif")), 32, 0, -32, 32);
		
		
		rightStandList.add(boxyRight);
		leftStandList.add(boxyLeft);
		Map<PlatformDirectionEnum, List<TextureRegion>> standMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
		standMap.put(PlatformDirectionEnum.LEFT, leftStandList);
		standMap.put(PlatformDirectionEnum.RIGHT, rightStandList);
		
		List<TextureRegion> rightWalkList = new ArrayList<TextureRegion>();
		List<TextureRegion> leftWalkList = new ArrayList<TextureRegion>();
		rightWalkList.add(boxyRight);
		leftWalkList.add(boxyLeft);
		Map<PlatformDirectionEnum, List<TextureRegion>> walkMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
		walkMap.put(PlatformDirectionEnum.LEFT, leftWalkList);
		walkMap.put(PlatformDirectionEnum.RIGHT, rightWalkList);
		
		List<TextureRegion> rightJumpList = new ArrayList<TextureRegion>();
		List<TextureRegion> leftJumpList = new ArrayList<TextureRegion>();
		rightJumpList.add(boxyRight);
		leftJumpList.add(boxyLeft);
		Map<PlatformDirectionEnum, List<TextureRegion>> jumpMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
		jumpMap.put(PlatformDirectionEnum.LEFT, leftJumpList);
		jumpMap.put(PlatformDirectionEnum.RIGHT, rightJumpList);
		
		List<TextureRegion> rightLandList = new ArrayList<TextureRegion>();
		List<TextureRegion> leftLandList = new ArrayList<TextureRegion>();
		rightLandList.add(boxyRight);
		leftLandList.add(boxyLeft);
		Map<PlatformDirectionEnum, List<TextureRegion>> landMap = new HashMap<PlatformDirectionEnum, List<TextureRegion>>();
		landMap.put(PlatformDirectionEnum.LEFT, leftLandList);
		landMap.put(PlatformDirectionEnum.RIGHT, rightLandList);
		
		player.getAssets().put(PlatformObjectStateEnum.STAND, standMap);
		player.getAssets().put(PlatformObjectStateEnum.WALK, walkMap);
		player.getAssets().put(PlatformObjectStateEnum.JUMP, jumpMap);
		player.getAssets().put(PlatformObjectStateEnum.FALL, landMap);
		
		return player;
	}
	
	public static PlatformObject createCoin(float x, float y) {
		PlatformObject coin = new PlatformObject();
		coin.setX(x);
		coin.setY(y);
		coin.setWidth(16);
		coin.setHeight(32);
		if (coinMap.get(PlatformDirectionEnum.NONE) == null) {
			List<TextureRegion> coinList = new ArrayList<TextureRegion>();
			coinList.add(new TextureRegion(new Texture(Gdx.files.internal("images/coin_gold_01.png")), 0, 0, 16, 32));
			coinList.add(new TextureRegion(new Texture(Gdx.files.internal("images/coin_gold_02.png")), 0, 0, 16, 32));
			coinList.add(new TextureRegion(new Texture(Gdx.files.internal("images/coin_gold_03.png")), 0, 0, 16, 32));
			coinList.add(new TextureRegion(new Texture(Gdx.files.internal("images/coin_gold_04.png")), 0, 0, 16, 32));
			coinList.add(new TextureRegion(new Texture(Gdx.files.internal("images/coin_gold_05.png")), 0, 0, 16, 32));
			coinList.add(new TextureRegion(new Texture(Gdx.files.internal("images/coin_gold_06.png")), 0, 0, 16, 32));
			coinMap.put(PlatformDirectionEnum.NONE, coinList);
		}
		coin.getAssets().put(PlatformObjectStateEnum.STAND, coinMap);
		coin.setProfile(collectableProfile);
		
		coin.setBehavior(coinBehavior);
		
		return coin;
	}
	
	public static PlatformObject createFlag(float x, float y) {
		PlatformObject flag = new PlatformObject();
		flag.setX(x);
		flag.setY(y);
		flag.setWidth(32);
		flag.setHeight(32);
		if (flagMap.get(PlatformDirectionEnum.NONE) == null) {
			List<TextureRegion> flagList = new ArrayList<TextureRegion>();
			flagList.add(new TextureRegion(new Texture(Gdx.files.internal("images/flag.gif"))));
			flagMap.put(PlatformDirectionEnum.NONE, flagList);
		}
		flag.getAssets().put(PlatformObjectStateEnum.STAND, flagMap);
		flag.setProfile(collectableProfile);
		
		flag.setBehavior(new WinLevelBehavior());
		
		return flag;
	}
}
