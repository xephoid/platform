package com.ionmarkgames.platform.model;

import java.util.ArrayList;
import java.util.List;

import com.ionmarkgames.platform.exception.PlatformException;
import com.ionmarkgames.platform.model.gfx.PlatformObject;

public class PlatformTerrain {

	private int width;
	private int height;
	private int cellWidth;
	private int cellHeight;
	
	private List<PlatformTerrainCell> terrainCells = new ArrayList<PlatformTerrainCell>();
	
	public PlatformTerrain(int width, int height, int cellWidth, int cellHeight) {
		this.width = width;
		this.height = height;
		this.cellHeight = cellHeight;
		this.cellWidth = cellWidth;
		
		for(int i = 0; i < width * height; i++) {
			terrainCells.add(i, new PlatformTerrainCell(i % width, i / width));
		}
	}
	
	public PlatformTerrainCell[] getCells(PlatformObject po) {
		int x1 = Math.max(0, (int)(po.getX() - po.getWidth()));
		int y1 = Math.min(cellHeight * height, (int)(po.getY() + (po.getHeight() * 2)));
		int x2 = Math.min(cellWidth * width, (int) (po.getX() + (po.getWidth() * 2)));
		int y2 = Math.max(0, (int)(po.getY() - po.getHeight()));
		
		int topLeft = getIndex(x1, y1);
		int topRight = getIndex(x2, y1);
		int bottomLeft = getIndex(x1, y2);
		//System.out.println("(" +po.getX()+ ", " +(po.getY())+ ") = " + bottomLeft + "\t");
		
		//int bottomRight = getIndex((int)(po.getX() + po.getWidth()), (int) po.getY());
		
		int columns = Math.max(topRight - topLeft, 0) + 1;
		int rows = Math.max(((topLeft / getWidth()) - (bottomLeft / getWidth())), 0) + 1;
		
		PlatformTerrainCell[] result = new PlatformTerrainCell[(columns * rows)];
		int index = 0;
		if (result.length > 0) {
			for (int i = rows - 1; i >= 0; i--) {
				for (int j = 0; j < columns; j++) {
					result[index++] = terrainCells.get(topLeft - (i * width) + j);
				}
			}
		}
		
		return result;
	}
	
	public PlatformTerrainCell getCell(float x, float y) {
		return terrainCells.get(getIndex((int)x, (int)y));
	}
	
	private int getIndex(int x, int y) {
		x = (x / getCellWidth());
		y = (y / getCellHeight());
		
		if (x < 0 || y < 0 || x > getWidth() || y > getHeight()) {
			throw new PlatformException("Cell index out of bounds (" + x + ", " + y + ").");
		}
		return x + (y * getWidth());
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getCellWidth() {
		return cellWidth;
	}

	public int getCellHeight() {
		return cellHeight;
	}
}