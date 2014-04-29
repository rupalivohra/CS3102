import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

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
		Random rand = new Random();
		int firstX = 0;
		for (Node n : words) {
			// firstX = firstX + n.getFontSize() * n.getWord().length();
			Font font = new Font("Verdana", Font.PLAIN, n.getFontSize());
			g.setFont(font);
			// g.drawString(n.getWord(), firstX, 50 + n.getFontSize());
			g.drawString(n.getWord(), rand.nextInt(1600), rand.nextInt(900));
			// todo: placement on pane (adjusting the 250, 250 above based on0
			// relatedness)
			// consider: x and y are related. y and z are related. x and z are
			// not related.
		}
	}
}