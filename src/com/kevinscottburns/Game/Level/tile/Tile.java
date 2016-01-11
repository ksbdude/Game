package com.kevinscottburns.Game.Level.tile;

import com.kevinscottburns.Game.Graphics.Screen;
import com.kevinscottburns.Game.Graphics.Sprite;

public class Tile {
	public int x, y;
	public Sprite sprite;
	
	public static Tile grass = new GrassTile(Sprite.grass);
	public static Tile voidTile = new VoidTile(Sprite.voidSprite);

	public Tile(Sprite sprite) {
		this.sprite = sprite;
		System.out.println("Made Tile");
	}

	public void render(int x, int y, Screen screen) {
	}
	
	public boolean solid(){
		return false;
	}
}
