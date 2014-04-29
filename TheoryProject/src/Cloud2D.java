import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;

public class Cloud2D {
	// private static JFrame frame;
	public static void generate(ArrayList<Node> words) {
		// Frame gc = new Frame();
		JFrame frame = new JFrame("Title here");
		frame.setSize(1600, 900);
		// frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		Rectangle bounds = frame.getBounds();
		frame.setLocation(0, 0);
		// System.out.println(bounds);
		// f.setDefaultLookAndFeelDecorated(true);
		frame.setVisible(true);
		Graphics g = frame.getGraphics();
		String s = "test";
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		Font[] fonts = ge.getAllFonts();
		for (int i = 0; i < fonts.length; i++) {
			// /System.out.println(i + ": " + fonts[i]);
		}
		Font font = new Font("Comfortaa", Font.PLAIN, 70);
		// Font f = new Font(fonts[377]);
		g.setFont(font);
		// g.drawString(s, 500, 500);
		addText(g, words);
	}

	public static void addText(Graphics g, ArrayList<Node> words) {
		Collections.shuffle(words);
		FontMetrics fm = g.getFontMetrics();
		int j = 30;
		int i = 30;
		for (int h = 0; h < words.size(); ++h) {
			if (j < 1500) {
				Font font = new Font("Verdana", Font.PLAIN, words.get(h)
						.getFontSize());
				g.setFont(font);
				g.drawString(words.get(h).getWord(), j, i);
				j += g.getFontMetrics().stringWidth(words.get(h).getWord() + 1);
			} else {
				j = 30;
				i += g.getFontMetrics().getHeight();
			}
		}
	}
}