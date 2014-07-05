package com.ionmarkgames.platform.control.behavior;

import com.ionmarkgames.platform.model.gfx.PlatformObject;

public interface IPlatformInteractionBoolean {

	public boolean is(PlatformObject po);
	
	public IPlatformInteractionBoolean TRUE = new IPlatformInteractionBoolean() {
		@Override
		public boolean is(PlatformObject po) {
			return true;
		}
	};
	
	public IPlatformInteractionBoolean FALSE = new IPlatformInteractionBoolean() {
		@Override
		public boolean is(PlatformObject po) {
			return false;
		}
	};
	
	public class NotPlayerBoolean implements IPlatformInteractionBoolean {
		private PlatformObject player;
		
		public NotPlayerBoolean(PlatformObject player) {
			this.player = player;
		}
		
		@Override
		public boolean is(PlatformObject po) {
			return po.getId() != player.getId();
		}
	}
}