package hw2;

import java.awt.*;
import java.io.IOException;

public class SpeedReader {
	public static int findHorizOffset(String word, FontMetrics m) {
		int wordLen = word.length();
		int offset = wordLen / 2;
		int id;
		if (wordLen <= 2) {
			id = 0;
		} else if (wordLen >= 3 && wordLen <= 5) {
			id = 1;
		} else if (wordLen >= 6 && wordLen <= 9) {
			id = 2;
		} else if (wordLen >= 10 && wordLen <= 13) {
			id = 3;
		} else {
			id = 4;
		}
		if (id > 0) {
			int preCenteredCharOffset = m.stringWidth(word.substring(0, id));
			int centeredCharOffset = m.stringWidth(word.substring(id, id + 1)) / 2;
			offset = preCenteredCharOffset + centeredCharOffset;
		}
		return offset;
	}

	public static void speedReader(String filename, int width, int height, int fontSize, int wpm)
			throws IOException, InterruptedException {
		DrawingPanel panel = new DrawingPanel(width, height);
		Graphics g = panel.getGraphics();
		Font f = new Font("Courier", Font.BOLD, fontSize);
		FontMetrics m = g.getFontMetrics(f);
		int fontHeight = m.getHeight();
		int yCoord = (height + fontHeight / 2) / 2;
		WordGenerator text = new WordGenerator("test.txt");
		g.setFont(f);
		width /= 2;
		while (text.hasNext()) {
			String nextWord = text.next();
			int offset = findHorizOffset(nextWord, m);
			g.drawString(nextWord, width - offset, yCoord);
			Thread.sleep(60000 / wpm);
			panel.clear();
		}
	}

	public static int[] strArgsToIntArgs(String[] args) {
		int[] intArgs = new int[4];
		for (int i = 1; i < args.length; i++) {
			intArgs[i - 1] = Integer.parseInt(args[i]);
		}
		return intArgs;
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		int[] intArgs = strArgsToIntArgs(args);
		speedReader(args[1], intArgs[0], intArgs[1], intArgs[2], intArgs[3]);
	}
}
