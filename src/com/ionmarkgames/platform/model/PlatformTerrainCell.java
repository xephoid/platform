package com.ionmarkgames.platform.model;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ionmarkgames.platform.control.behavior.IPlatformInteractionBoolean;
import com.ionmarkgames.platform.control.behavior.PlatformObjectInteractionEnum;
import com.ionmarkgames.platform.model.gfx.PlatformObject;

public class PlatformTerrainCell {

	private int x;
	private int y;
	private Map<PlatformObjectInteractionEnum, List<PlatformObject>> objects = new HashMap<PlatformObjectInteractionEnum, List<PlatformObject>>();
	
	public PlatformTerrainCell(int x, int y) {
		this.x = x;
		this.y = y;
		for(PlatformObjectInteractionEnum type : EnumSet.allOf(PlatformObjectInteractionEnum.class)) {
			objects.put(type, new ArrayList<PlatformObject>());
		}
	}
	
	public void clear() {
		for(PlatformObjectInteractionEnum type : EnumSet.allOf(PlatformObjectInteractionEnum.class)) {
			objects.get(type).clear();
		}
	}
	
	public void add(PlatformObject po) {
		for(PlatformObjectInteractionEnum type : EnumSet.allOf(PlatformObjectInteractionEnum.class)) {
			IPlatformInteractionBoolean bool = po.getProfile().get(type);
			if (bool != null) {
				objects.get(type).add(po);
			}
		}
	}
	
	public void remove(PlatformObject po) {
		for(PlatformObjectInteractionEnum type : EnumSet.allOf(PlatformObjectInteractionEnum.class)) {
			objects.get(type).remove(po);
		}
	}
	
	public List<PlatformObject> get(PlatformObjectInteractionEnum type) {
		return objects.get(type);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}