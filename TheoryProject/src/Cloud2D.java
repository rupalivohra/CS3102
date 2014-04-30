import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JFrame;

public class Cloud2D {
	public void generate(ArrayList<Node> words, String title) {
		JFrame frame = new JFrame(title);
		int frameSize = 0;
		frameSize = words.size() * 10;
		int x = 0;
		int y = 0;
		if (frameSize <= 1600) {
			x = frameSize;
		} else {
			x = 1600;
		}
		if (frameSize <= 900) {
			y = frameSize;
		} else {
			y = 900;
		}
		frame.setSize(x, y);
		frame.setLocation(0, 0);
		frame.setVisible(true);
		frame.setOpacity(Color.OPAQUE);
		frame.setBackground(Color.gray);
		Rectangle bounds = frame.getBounds();
		Graphics g = frame.getGraphics();
		addText(g, words, bounds, frameSize);
	}

	public void addText(Graphics g, ArrayList<Node> words, Rectangle bounds,
			int frameSize) {
		Collections.shuffle(words);
		int x = 30;
		int y = 65;
		for (int h = 0; h < words.size(); ++h) {
			if (x < frameSize - 150) {
				Font font = new Font("Verdana", Font.PLAIN, words.get(h)
						.getFontSize());
				g.setFont(font);
				g.setColor(words.get(h).getFontColor());
				g.drawString(words.get(h).getWord(), x, y);
//				System.out.print(words.get(h).getWord() + ", ");
				x += g.getFontMetrics().stringWidth(words.get(h).getWord() + 1);
			} else {
				x = 30;
				y += g.getFontMetrics().getHeight() + 5;
			}
		}
	}
}