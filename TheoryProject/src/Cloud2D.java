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
		frame.setSize(frameSize, frameSize);
		frame.setLocation(0, 0);
		frame.setVisible(true);
		frame.setOpacity(Color.OPAQUE);
		frame.setBackground(Color.gray);
		Rectangle bounds = frame.getBounds();
		Graphics g = frame.getGraphics();
		addText(g, words, bounds, frameSize);
	}

	public void addText(Graphics g, ArrayList<Node> words, Rectangle bounds,
			int frame) {
		Collections.shuffle(words);
		int x = 30;
		int y = 65;
		for (int h = 0; h < words.size(); ++h) {
			if (x < frame - frame / 10) {
				Font font = new Font("Verdana", Font.PLAIN, words.get(h)
						.getFontSize());
				g.setFont(font);
				g.setColor(words.get(h).getFontColor());
				g.drawString(words.get(h).getWord(), x, y);
				System.out.println(words.get(h).getWord() + ", ");
				x += g.getFontMetrics().stringWidth(words.get(h).getWord() + 1);
			} else {
				x = 30;
				y += g.getFontMetrics().getHeight() + 5;
			}
		}
	}
}