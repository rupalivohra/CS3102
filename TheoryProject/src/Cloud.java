//import java.awt.Font;
//import java.awt.Shape;
//import java.awt.font.FontRenderContext;
//import java.awt.font.GlyphVector;
//import java.awt.geom.AffineTransform;
//import java.text.Bidi;
//import java.util.TreeMap;
//
//import javax.swing.JFrame;
//
//public class Cloud extends JFrame {
//	public static TreeMap<Integer, String> cl;
//	private static final long serialVersionUID = 1L;
//
//	public void getCloud(TreeMap<Integer, String> thing) {
//		cl = thing;
//	}
//
//	private static final FontRenderContext FRC = new FontRenderContext(null,
//			true, true);
//
//	public Shape make(final Font font, final double weight,
//			final String word, final double orientation) {
//		final Font sizedFont = font.deriveFont((float) weight);
//		final char[] chars = word.toCharArray();
//		final int direction = Bidi.requiresBidi(chars, 0, chars.length) ? Font.LAYOUT_RIGHT_TO_LEFT
//				: Font.LAYOUT_LEFT_TO_RIGHT;
//		final GlyphVector gv = sizedFont.layoutGlyphVector(FRC, chars, 0,
//				chars.length, direction);
//		Shape result = gv.getOutline();
//		if (orientation != 0.0) {
//			result = AffineTransform.getRotateInstance(orientation)
//					.createTransformedShape(result);
//		}
//		return result;
//	}
//}

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Cloud extends JFrame {
	public static ArrayList<Node> cl;
	private static final long serialVersionUID = 1L;

	public Cloud() {
		setTitle("Custom Component Test");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void getCloud(ArrayList<Node> thing) {
		cl = thing;
	}

	public void display() {
		add(new CustomComponent(cl));
		pack();
		setMinimumSize(getSize());// enforces minimum size of frame & component
		setVisible(true);
	}

	public void generate() {
		Cloud main = new Cloud();
		main.display();
	}
}

class CustomComponent extends JComponent {
	private static final long serialVersionUID = 1L;
	private static ArrayList<Node> create;

	public CustomComponent(ArrayList<Node> cl) {
		create = cl;
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(500, 500);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(1000, 800);
	}

	@Override
	public void paintComponent(Graphics g) {
		for (Node n : create) {
			g.setFont(new Font("TimesRoman", Font.PLAIN, n.getFreq()));
			g.drawString(n.getWord(), n.getFreq() * n.getFreq(), n.getFreq()
					* n.getFreq());
		}
		super.paintComponent(g);
		g.setColor(Color.red);
	}
}