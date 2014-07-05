package com.ionmarkgames.platform.model;

import java.util.HashSet;
import java.util.Set;

import com.ionmarkgames.platform.model.gfx.PlatformObject;

public class PlatformState {

	private PlatformObject player;
	private PlatformTerrain terrain = null;
	private Set<PlatformObject> objects = new HashSet<PlatformObject>();
	private int levelId = 0;
	private boolean levelWin = false;

	public void setTerrain(PlatformTerrain terrain) {
		this.terrain = terrain;
	}

	public PlatformTerrain getTerrain() {
		return terrain;
	}

	public void setObjects(Set<PlatformObject> objects) {
		this.objects = objects;
	}

	public Set<PlatformObject> getObjects() {
		return objects;
	}

	public void setPlayer(PlatformObject player) {
		this.player = player;
	}

	public PlatformObject getPlayer() {
		return player;
	}

	public void setLevelWin(boolean levelWin) {
		this.levelWin = levelWin;
	}

	public boolean isLevelWin() {
		return levelWin;
	}

	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}

	public int getLevelId() {
		return levelId;
	}
}