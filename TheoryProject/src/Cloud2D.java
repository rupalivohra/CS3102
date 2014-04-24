import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JFrame;


public class Cloud2D {
//	private static JFrame frame;
	public static void generate() {
//		Frame gc = new Frame();
		JFrame frame = new JFrame("Title here");
		frame.setSize(1650, 1080);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//		Rectangle bounds = frame.getBounds();
		frame.setLocation(0, 0);
//		System.out.println(bounds);
//		f.setDefaultLookAndFeelDecorated(true);
		frame.setVisible(true);
		Graphics g = frame.getGraphics();
		String s = "test";
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font[] fonts = ge.getAllFonts();
//		for (int i = 0; i < fonts.length; i++) {
//			System.out.println(i + ": " + fonts[i]);
//		}
		Font font = new Font("RupaliHandwrite", Font.PLAIN, 100);
//		Font f = new Font(fonts[377]);
		g.setFont(font);
		g.drawString(s, 500, 500);
	}
}
