package hw2;

import java.awt.*;
import java.io.IOException;

class Offset {
	public int id;
	public int total;
	public int centeredChar;
}

public class SpeedReader {

	public static int findCenteredCharId(String word) {
		int wordLen = word.length();
		int id;

		if (wordLen <= 1) {
			id = 0;
		} else if (wordLen >= 2 && wordLen <= 5) {
			id = 1;
		} else if (wordLen >= 6 && wordLen <= 9) {
			id = 2;
		} else if (wordLen >= 10 && wordLen <= 13) {
			id = 3;
		} else {
			id = 4;
		}
		return id;
	}

	public static Offset findHorizOffset(String word, FontMetrics m) {
		Offset o = new Offset();
		o.total = word.length() / 2;
		o.id = findCenteredCharId(word);

		if (o.id > 0) {
			int preCenteredChar = m.stringWidth(word.substring(0, o.id));
			o.centeredChar = m.stringWidth(word.substring(o.id, o.id + 1)) / 2;
			o.total = preCenteredChar + o.centeredChar;
		}
		return o;
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
			Offset o = findHorizOffset(nextWord, m);
			g.drawString(nextWord, width - o.total, yCoord);
			g.setColor(Color.RED);
			g.drawString(nextWord.substring(o.id, o.id + 1), width - o.centeredChar, yCoord);
			g.setColor(Color.BLACK);
			Thread.sleep(60000 / wpm);
			panel.clear();
		}
		System.out.println("Total words read: " + text.getWordCount());
		System.out.println("Total sentences read: " + text.getSentCount());
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
