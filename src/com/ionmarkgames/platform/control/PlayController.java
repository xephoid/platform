package com.ionmarkgames.platform.control;

import com.ionmarkgames.platform.model.PlatformState;
import com.ionmarkgames.platform.model.PlatformTerrainCell;
import com.ionmarkgames.platform.model.gfx.PlatformObject;
import com.ionmarkgames.platform.util.ClockUtil;
import com.ionmarkgames.platform.view.IPlatformView;

public class PlayController {

	private IPlatformView view;
	
	private ClockUtil clock = new ClockUtil();
	
	public void mainLoop(PlatformObjectController controller, PlatformState state) {
		if (clock.isTimeYet(.01f)) {
			view.render(state);
			controller.behave(state, state.getPlayer());
			for (PlatformObject po : state.getObjects()) {
				if (!po.isDead()) {
					controller.behave(state, po);
				} else {
					for (PlatformTerrainCell cell : po.getCells()) {
						cell.remove(po);
					}
				}
			}
			clock.start();
		}
	}

	public void setView(IPlatformView view) {
		this.view = view;
	}

	public IPlatformView getView() {
		return view;
	}
}