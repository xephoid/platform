package com.ionmarkgames.platform.engine;

import java.util.HashMap;
import java.util.Map;

import com.ionmarkgames.platform.control.behavior.FollowerBehavior;
import com.ionmarkgames.platform.control.behavior.IPlatformInteractionBoolean;
import com.ionmarkgames.platform.control.behavior.PlatformObjectInteractionEnum;
import com.ionmarkgames.platform.model.PlatformState;
import com.ionmarkgames.platform.model.PlatformTerrain;
import com.ionmarkgames.platform.model.PlatformTerrainCell;
import com.ionmarkgames.platform.model.gfx.PlatformObject;
import com.ionmarkgames.platform.util.ObjectCreatorUtil;

public class TestTerrrainLoader implements IPlatformTerrainLoader {
	
	private int playerId;

	@Override
	public PlatformTerrain getTerrain(PlatformState gamestate) {
		PlatformTerrain terrain = new PlatformTerrain(128, 32, 32, 32); 
		
		for(int i = 0; i < 31; i++) {
			PlatformObject block = ObjectCreatorUtil.createBlock(i * 31, 32, false);
			gamestate.getObjects().add(block);
			if (i > 0 && i < 20) {
				gamestate.getObjects().add(ObjectCreatorUtil.createBlock(0, i * 31, false));
				gamestate.getObjects().add(ObjectCreatorUtil.createBlock(27 * 31, i * 31, false));
			}
			if (i < 20) {
				gamestate.getObjects().add(ObjectCreatorUtil.createBlock(i * 31, 7 * 32,false));
			}
			if (i < 8) {
				gamestate.getObjects().add(ObjectCreatorUtil.createBlock(i * 31, 8 * 32,false));
				gamestate.getObjects().add(ObjectCreatorUtil.createBlock(i * 31, 9 * 32,false));
				gamestate.getObjects().add(ObjectCreatorUtil.createBlock(i * 31, 10 * 32,false));
				gamestate.getObjects().add(ObjectCreatorUtil.createBlock(i * 31, 11 * 32,false));
			}
			if (i < 7) {
				gamestate.getObjects().add(ObjectCreatorUtil.createBlock(i * 31, 12 * 32,false));
				gamestate.getObjects().add(ObjectCreatorUtil.createBlock(i * 31, 13 * 32,false));
				gamestate.getObjects().add(ObjectCreatorUtil.createBlock(i * 31, 14 * 32,false));
			}
		}
		
		gamestate.getObjects().add(ObjectCreatorUtil.createBlock(95, 64, false));
		
		gamestate.getObjects().add(ObjectCreatorUtil.createBlock(190, 64, false));
		gamestate.getObjects().add(ObjectCreatorUtil.createBlock(190, 95, false));
		
		gamestate.getObjects().add(ObjectCreatorUtil.createBlock(280, 64, false));
		gamestate.getObjects().add(ObjectCreatorUtil.createBlock(280, 95, false));
		gamestate.getObjects().add(ObjectCreatorUtil.createBlock(280, 120, false));
		
		PlatformObject bottomBlock = ObjectCreatorUtil.createBlock(512, 128, true);
		gamestate.getObjects().add(ObjectCreatorUtil.createBlock(512, 64, false));
		gamestate.getObjects().add(ObjectCreatorUtil.createBlock(512, 96, false));
		gamestate.getObjects().add(bottomBlock);
		
		gamestate.getObjects().add(ObjectCreatorUtil.createSwitch(512, 8 * 32, 32, 32, bottomBlock));
		
		gamestate.getObjects().add(ObjectCreatorUtil.createBlock(768 - 64, 97, false));
		gamestate.getObjects().add(ObjectCreatorUtil.createBlock(768 - 96, 4 * 32, false));
		gamestate.getObjects().add(ObjectCreatorUtil.createBlock(768 - 128, 5 * 32, false));
		gamestate.getObjects().add(ObjectCreatorUtil.createBlock(768 - 160, 6 * 32, false));
		
		gamestate.getObjects().add(ObjectCreatorUtil.createBlock(280, 8 * 32, false));
		gamestate.getObjects().add(ObjectCreatorUtil.createBlock(280, 9 * 32, false));
		gamestate.getObjects().add(ObjectCreatorUtil.createBlock(280, 10 * 32, false));
		gamestate.getObjects().add(ObjectCreatorUtil.createBlock(280, 11 * 32, false));
		
		gamestate.getObjects().add(ObjectCreatorUtil.createBlock(248, 8 * 32, false));
		gamestate.getObjects().add(ObjectCreatorUtil.createBlock(248, 9 * 32, false));
		gamestate.getObjects().add(ObjectCreatorUtil.createBlock(248, 10 * 32, false));
		gamestate.getObjects().add(ObjectCreatorUtil.createBlock(248, 11 * 32, false));
		
		gamestate.getObjects().add(ObjectCreatorUtil.createBlock(216, 12 * 32, false));
		gamestate.getObjects().add(ObjectCreatorUtil.createBlock(216, 13 * 32, false));
		gamestate.getObjects().add(ObjectCreatorUtil.createBlock(216, 14 * 32, false));
		
		FollowerBehavior follower = new FollowerBehavior();
		
		playerId = gamestate.getPlayer().getId();
		
		Map<PlatformObjectInteractionEnum, IPlatformInteractionBoolean> followerProfile = new HashMap<PlatformObjectInteractionEnum, IPlatformInteractionBoolean>();
		followerProfile.put(PlatformObjectInteractionEnum.DISPLAY, IPlatformInteractionBoolean.TRUE);
		followerProfile.put(PlatformObjectInteractionEnum.RIDE, IPlatformInteractionBoolean.TRUE);
		followerProfile.put(PlatformObjectInteractionEnum.USE, IPlatformInteractionBoolean.TRUE);
		followerProfile.put(PlatformObjectInteractionEnum.STOP, new IPlatformInteractionBoolean() {
			@Override
			public boolean is(PlatformObject po) {
				return (po.getId() != playerId);
			}
		});
		
		PlatformObject helper1 = ObjectCreatorUtil.createWhitey(384, 64, follower);
		helper1.setProfile(followerProfile);
		gamestate.getObjects().add(helper1);
		
		PlatformObject helper2 = ObjectCreatorUtil.createYellow(576, 64, follower);
		helper2.setProfile(followerProfile);
		gamestate.getObjects().add(helper2);
		
		gamestate.getObjects().add(ObjectCreatorUtil.createCoin(104, 96));
		gamestate.getObjects().add(ObjectCreatorUtil.createCoin(200, 128));
		gamestate.getObjects().add(ObjectCreatorUtil.createCoin(296, 160));
		gamestate.getObjects().add(ObjectCreatorUtil.createCoin(420, 160));
		
		gamestate.getObjects().add(ObjectCreatorUtil.createFlag(95, 15 * 32));
		
		// add all objects to cells
		for(PlatformObject po : gamestate.getObjects()) {
			po.setCells(terrain.getCells(po));
			for (PlatformTerrainCell cell : po.getCells()) {
				cell.add(po);
			}
		}
		
		return terrain;
	}

}
