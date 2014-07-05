package com.ionmarkgames.platform.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.ionmarkgames.platform.control.behavior.AbstractBehavior;
import com.ionmarkgames.platform.engine.IPlatformTerrainLoader;
import com.ionmarkgames.platform.engine.ImageTerrainLoader;
import com.ionmarkgames.platform.model.PlatformState;
import com.ionmarkgames.platform.model.PlatformTerrain;
import com.ionmarkgames.platform.model.gfx.PlatformDirectionEnum;
import com.ionmarkgames.platform.model.gfx.PlatformObject;
import com.ionmarkgames.platform.sound.SoundManager;
import com.ionmarkgames.platform.util.ObjectCreatorUtil;
import com.ionmarkgames.platform.view.BasicView;
import com.ionmarkgames.platform.view.IPlatformView;

public class MainController {
	private PlatformMode mode = PlatformMode.INTRO;
	private PlayController playControl = new PlayController();
	private PlatformObjectController poc = new PlatformObjectController();
	private PlatformState gamestate;
	
	public void init() {
		gamestate = new PlatformState();
		
		gamestate.setPlayer(ObjectCreatorUtil.createBoxy(50, 96, new AbstractBehavior() {
			
			@Override
			public void act(PlatformObjectController controller, PlatformState state,
					PlatformObject po) {
				if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
					controller.changeDirection(state, po, PlatformDirectionEnum.RIGHT);
					po.setMoving(true);
				} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
					controller.changeDirection(state, po, PlatformDirectionEnum.LEFT);
					po.setMoving(true);
				} else {
					po.setMoving(false);
				}
				
				if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
					controller.jump(state, po);
				}
				
				if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
					controller.interact(state, po);
				}
				
				controller.move(state, po);
			}
		}));
		
		IPlatformTerrainLoader loader = new ImageTerrainLoader();
		
		gamestate.setLevelId(1);
		PlatformTerrain terrain = loader.getTerrain(gamestate);
		
		gamestate.setTerrain(terrain);
		
		IPlatformView view = new BasicView();
		view.init(gamestate);
		
		Camera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.translate(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
		view.setCamera(camera);
		playControl.setView(view);
		SoundManager.loadSounds();
	}
	
	public void loop() {
		switch(mode) {
			case INTRO:
				mode = PlatformMode.PLAYING;
				break;
			case INSTRUCTIONS:
				break;
			case PLAYING:
				playControl.mainLoop(poc, gamestate);
				break;
			case PAUSE:
				break;
			case NEXT_LEVEL:
				SoundManager.playLevelMusic();
				break;
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			System.exit(0);
		}
	}
}
