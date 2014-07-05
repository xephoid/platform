package com.ionmarkgames.platform.engine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.ionmarkgames.platform.control.behavior.FollowerBehavior;
import com.ionmarkgames.platform.control.behavior.IPlatformInteractionBoolean;
import com.ionmarkgames.platform.control.behavior.PlatformObjectInteractionEnum;
import com.ionmarkgames.platform.model.PlatformState;
import com.ionmarkgames.platform.model.PlatformTerrain;
import com.ionmarkgames.platform.model.PlatformTerrainCell;
import com.ionmarkgames.platform.model.gfx.PlatformObject;
import com.ionmarkgames.platform.util.ObjectCreatorUtil;

public class ImageTerrainLoader implements IPlatformTerrainLoader {

	private static final String LEVEL_PREFIX = "level";
	private static final String LEVEL_SUFFIX = ".png";
	
	@Override
	public PlatformTerrain getTerrain(PlatformState state) {
		File file = new File("images/" + LEVEL_PREFIX + state.getLevelId() + LEVEL_SUFFIX);
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			System.err.println("Could not read file: " + file);
			e.printStackTrace();
			System.exit(1);
		}

		return generateFromImage(state, image);
	}
	
	private PlatformTerrain generateFromImage(PlatformState state, BufferedImage image) {
		PlatformTerrain terrain = new PlatformTerrain(image.getWidth(), image.getHeight(), 32, 32);
		FollowerBehavior follower = new FollowerBehavior();
		Map<PlatformObjectInteractionEnum, IPlatformInteractionBoolean> followerProfile = new HashMap<PlatformObjectInteractionEnum, IPlatformInteractionBoolean>();
		followerProfile.put(PlatformObjectInteractionEnum.DISPLAY, IPlatformInteractionBoolean.TRUE);
		followerProfile.put(PlatformObjectInteractionEnum.RIDE, IPlatformInteractionBoolean.TRUE);
		followerProfile.put(PlatformObjectInteractionEnum.USE, IPlatformInteractionBoolean.TRUE);
		followerProfile.put(PlatformObjectInteractionEnum.STOP, new IPlatformInteractionBoolean.NotPlayerBoolean(state.getPlayer()));
		
		for ( int x = 0; x < image.getWidth(); ++x ) {
        	for ( int y = 0; y < image.getHeight(); ++y ) {
        		ColorEnum color = ColorEnum.closest(image.getRGB(x, image.getHeight()-1 - y));
        		switch (color) {
        			case WHITE:
        				state.getObjects().add(ObjectCreatorUtil.createBlock(x * terrain.getCellWidth(), y * terrain.getCellHeight(), false));
        				break;
        			case YELLOW:
        				state.getObjects().add(ObjectCreatorUtil.createCoin((x * terrain.getCellWidth()) + 8, y * terrain.getCellHeight()));
        				break;
        			case BLUE:
        				state.getPlayer().setX(x * terrain.getCellWidth());
        				state.getPlayer().setY(y * terrain.getCellHeight());
        				break;
        			case RED:
        				state.getObjects().add(ObjectCreatorUtil.createFlag(x * terrain.getCellWidth(), y * terrain.getCellHeight()));
        				break;
        			case GREEN:
        				PlatformObject helper = ObjectCreatorUtil.createWhitey(x * terrain.getCellWidth(), y * terrain.getCellHeight(), follower);
        				helper.setProfile(followerProfile);
        				state.getObjects().add(helper);
        				break;
        			case BLACK:
        			default:
        				// no-op
        				break;
        		}
        	}
		}
		
		// add all objects to cells
		for(PlatformObject po : state.getObjects()) {
			po.setCells(terrain.getCells(po));
			for (PlatformTerrainCell cell : po.getCells()) {
				cell.add(po);
			}
		}
		
		return terrain;
	}

}
