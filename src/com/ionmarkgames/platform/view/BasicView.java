package com.ionmarkgames.platform.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ionmarkgames.platform.model.PlatformState;
import com.ionmarkgames.platform.model.PlatformTerrainCell;
import com.ionmarkgames.platform.model.gfx.PlatformDirectionEnum;
import com.ionmarkgames.platform.model.gfx.PlatformObject;

public class BasicView implements IPlatformView {

	private SpriteBatch drawer;
	private BitmapFont font;
	private Texture background;
	private Camera camera;
	private float camXOffset = 0;
	private float camYOffset = 0;
	
	@Override
	public void init(PlatformState gamestate) {
		drawer = new SpriteBatch();
		font = new BitmapFont();
		background = new Texture(Gdx.files.internal("images/sky2.png"));
	}
	
	@Override
	public void render(PlatformState gamestate) {
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		PlatformObject player = gamestate.getPlayer();
		
		if (camXOffset < (gamestate.getTerrain().getWidth() * gamestate.getTerrain().getCellWidth()) - (Gdx.graphics.getWidth() * 2)
				&& PlatformDirectionEnum.RIGHT.equals(player.getDirection())
				&& player.getX() - camXOffset > Gdx.graphics.getWidth() / 2) {
			camera.translate(player.getSpeed(), 0, 0);
			camXOffset += player.getSpeed();
		} else if (camXOffset > 0
				&& PlatformDirectionEnum.LEFT.equals(player.getDirection()) 
				&& player.getX() - camXOffset < Gdx.graphics.getWidth() / 2) {
			camera.translate(-player.getSpeed(), 0, 0);
			camXOffset -= player.getSpeed();
		}
		
		if (camYOffset > 0
				&& player.isFalling()
				&& player.getY() - camYOffset < Gdx.graphics.getHeight() / 2) {
			camera.translate(0, -3, 0);
			camYOffset -= 3;
		} else if (camYOffset < (gamestate.getTerrain().getHeight() * gamestate.getTerrain().getCellHeight()) - (Gdx.graphics.getHeight() * 1)
				&& player.isJumping()
				&& player.getY() - camYOffset > Gdx.graphics.getHeight() / 2) {
			camera.translate(0, 3, 0);
			camYOffset += 3;
		} 
		
		camera.update();
        camera.apply(Gdx.gl10);
        drawer.setProjectionMatrix(camera.combined);
		
		drawer.begin();
		drawer.draw(background, 0, 0);
		for (PlatformObject po : gamestate.getObjects()) {
			if (po.isVisible()) {
				drawer.draw(po.getTexture(), po.getX(), po.getY());
				if (po.isShowMessage()) {
					font.setColor(.5f, .5f, .5f, 1);
					font.draw(drawer, "Press SPACE", po.getX(), po.getY() + po.getHeight() + 20);
					po.setShowMessage(false);
				}
				font.setColor(1, 0, 0, 1);
				//font.draw(drawer, "M", po.getX(), po.getY() + 20);
				if (!po.isStatic()) {
					font.setColor(0, 0, 0, 1);
				} else {
					font.setColor(0, 0, 1, 1);
				}
				//for (PlatformTerrainCell cell : po.getCells()) {
					//font.draw(drawer, "W", cell.getX() * gamestate.getTerrain().getCellWidth(), cell.getY() * gamestate.getTerrain().getCellHeight());
				//}
				
				// TODO: call handle display if needed
			}
		}
		drawer.draw(gamestate.getPlayer().getTexture(), gamestate.getPlayer().getX(), gamestate.getPlayer().getY());
		//font.setColor(1, 0, 0, 1);
        //font.draw(drawer, "M", gamestate.getPlayer().getX(), gamestate.getPlayer().getY() + gamestate.getPlayer().getHeight());
        for (PlatformTerrainCell cell : gamestate.getPlayer().getCells()) {
        	font.setColor(1, 0, 0, 1);
        	//font.draw(drawer, "W", cell.getX() * gamestate.getTerrain().getCellWidth(), cell.getY() * gamestate.getTerrain().getCellHeight());
        }
        
        if (gamestate.isLevelWin()) {
        	font.setColor(1, 0, 0, 1);
        	font.draw(drawer, "YOU WIN!", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        }
        
		drawer.end();
	}

	@Override
	public void setCamera(Camera camera) {
		this.camera = camera;
	}

}