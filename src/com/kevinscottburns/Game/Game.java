package com.kevinscottburns.Game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.*;
import java.awt.image.*;

import javax.swing.JFrame;

import com.kevinscottburns.Game.Graphics.Screen;
import com.kevinscottburns.Game.Level.*;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	public static int width = 300;
	public static int height = width / 16 * 10;
	public static int scale = 3;

	private Thread thread;
	private JFrame frame;
	private Keyboard key;
	private boolean running = false;
	private Screen screen;
	private Level level;
	int y = 0, x = 0;

	public static String title = "Game";

	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	public Game() {
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);
		screen = new Screen(width, height);
		frame = new JFrame();
		key = new Keyboard();
		level = new RandomLevel(64, 64);
		addKeyListener(key);
	}

	public synchronized void start() {
		System.out.println("Starting");
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}

	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		requestFocus();
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				System.out.println("Update Run Loop");
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(updates + " ups, " + frames + " fps");
				frame.setTitle(title + " | " + updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
			System.out.println("Finished Run Loop");
		}
	}

	public void update() {
		System.out.println("Update...");
		key.update();
		if (key.up) {
			y--;
		}
		if (key.down) {
			y++;
		}
		if (key.left) {
			x--;
		}
		if (key.right) {
			x++;
		}
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		screen.clear();
		System.out.println("Cleared");
		level.render(x, y, screen);
		System.out.println("rendered screen");
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}

		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
		System.out.println("Done render");
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.frame.setResizable(false);
		game.frame.setTitle(title);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);

		game.start();
	}
}
