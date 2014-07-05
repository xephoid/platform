package com.ionmarkgames.platform.engine;

import com.ionmarkgames.platform.model.PlatformState;
import com.ionmarkgames.platform.model.PlatformTerrain;

public interface IPlatformTerrainLoader {

	public PlatformTerrain getTerrain(PlatformState state);
	
}