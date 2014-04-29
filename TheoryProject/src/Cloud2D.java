import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;

public class Cloud2D {
	public void generate(ArrayList<Node> words) {
		JFrame frame = new JFrame("Title here");
		frame.setSize(1600, 900);
		frame.setLocation(0, 0);
		frame.setVisible(true);
		Graphics g = frame.getGraphics();
		addText(g, words);
	}

	public void addText(Graphics g, ArrayList<Node> words) {
		Collections.shuffle(words);
		int j = 30;
		int i = 30;
		for (int h = 0; h < words.size(); ++h) {
			if (j < 1570) {
				Font font = new Font("Verdana", Font.PLAIN, words.get(h)
						.getFontSize());
				// g.setFont(font);
				g.setFont(font);
				g.setColor(words.get(h).getFontColor());
				g.drawString(words.get(h).getWord(), j, i);
				j += g.getFontMetrics().stringWidth(words.get(h).getWord() + 1);
			} else {
				j = 30;
				i += g.getFontMetrics().getHeight();
			}
		}
	}
}