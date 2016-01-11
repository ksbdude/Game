package com.kevinscottburns.Game.Level.tile;

import com.kevinscottburns.Game.Graphics.Screen;
import com.kevinscottburns.Game.Graphics.Sprite;

public class GrassTile extends Tile {

	public GrassTile(Sprite sprite) {
		super(sprite);
	}

	public void render(int x, int y, Screen screen) {
		screen.renderTile(x << 4, y << 4, this);
	}
}
