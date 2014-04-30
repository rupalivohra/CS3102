import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;

public class Cloud2D {
	public void generate(ArrayList<Node> words, String title) {
		JFrame frame = new JFrame(title);
		frame.setForeground(Color.gray);
		frame.setSize(1600, 900);
		frame.setLocation(0, 0);
//		frame.setOpacity(Color.OPAQUE);
		frame.setVisible(true);
		Graphics g = frame.getGraphics();
		addText(g, words);
	}

	public void addText(Graphics g, ArrayList<Node> words) {
		Collections.shuffle(words);
		int x = 30;
		int y = 60;
		for (int h = 0; h < words.size(); ++h) {
			if (x < 1570) {
				Font font = new Font("Verdana", Font.PLAIN, words.get(h)
						.getFontSize());
				// g.setFont(font);
				g.setFont(font);
//				System.out.println(words.get(h).getFontColor());
				g.setColor(words.get(h).getFontColor());
//				g.setPaintMode();
				g.drawString(words.get(h).getWord(), x, y);
				x += g.getFontMetrics().stringWidth(words.get(h).getWord() + 1);
			} else {
				x = 30;
				y += g.getFontMetrics().getHeight();
			}
		}
	}
}