package hw2;

import java.awt.*;
import java.io.IOException;

/**
 * Class Offset stores different offsets from the vertical middle line for each
 * displayed word.
 */
class Offset {
	// id: index of the character to be centered in the word
	public int id;
	// Total amount of offset for the left edge of the displayed word
	public int total;
	// Offset for the left edge of the centered character
	public int centeredChar;
}

/**
 * Class Bound stores coordinates and metrics for each line in the frame
 */
class Bound {
	// y-coord of the top of the word
	public int yDrawnStrTop;
	// y-coord of the bottom of the word
	public int yDrawnStrBottom;
	public int width;
	public int fontHeight;
}

/**
 * Main class of the Speed Reader program that uses the WordGenerator class to
 * read a text file, and after displaying it, reports the number of words and
 * number of sentences it processed
 * 
 * @author Enrique Rueda & Khoa Ho
 * @since 9/1/2017
 */
public class SpeedReader {

	/**
	 * Finds the centered character of each word, or Optimal Recognition Position
	 * (ORP), based on its length
	 * 
	 * @param word
	 *            word to be displayed
	 * @return index of the character to be centered
	 */
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

	/**
	 * Calculates the horizontal offset of each displayed word based on its length
	 * 
	 * @param word
	 *            word to be displayed
	 * @param m
	 *            FontMetrics object associated with the currently used font
	 * @return Offset object
	 */
	public static Offset findHorizOffset(String word, FontMetrics m) {
		Offset o = new Offset();
		// Initial total offset is half the word width (true if word has only 1 letter)
		o.total = m.stringWidth(word) / 2;
		o.id = findCenteredCharId(word);

		// If word has more than 1 letter, then calculate the actual offset
		if (o.id > 0) {
			int preCenteredChar = m.stringWidth(word.substring(0, o.id));
			o.centeredChar = m.stringWidth(word.substring(o.id, o.id + 1)) / 2;
			o.total = preCenteredChar + o.centeredChar;
		}
		return o;
	}

	/**
	 * Draw a frame, consisting of 2 horizontal lines at the top and bottom of the
	 * displayed area and 2 vertical short lines focusing the centered character of
	 * each word
	 * 
	 * @param g
	 *            Graphics object
	 * @param width
	 *            width of the drawing panel
	 * @param height
	 *            height of the drawing panel
	 * @param fontHeight
	 *            font height of the currently used font
	 * @return the Bound object
	 */
	public static Bound drawFrame(Graphics g, int width, int height, int fontHeight) {
		Bound b = new Bound();
		int xCenter = width / 2;
		int yCenter = height / 2;
		int yUpperHorizLine = (int) (yCenter - fontHeight * 0.6);
		int yLowerHorizLine = (int) (yCenter + fontHeight * 0.6);
		b.yDrawnStrTop = (int) (yCenter - fontHeight * 0.5);
		b.yDrawnStrBottom = (int) (yCenter + fontHeight * 0.5);
		b.width = width;
		b.fontHeight = fontHeight;

		// Dynamically change the width the frame based on the font height
		((Graphics2D) g).setStroke(new BasicStroke(Math.max(fontHeight / 20, 1)));
		g.drawLine(0, yUpperHorizLine, width, yUpperHorizLine);
		g.drawLine(0, yLowerHorizLine, width, yLowerHorizLine);
		g.drawLine(xCenter, yUpperHorizLine, xCenter, b.yDrawnStrTop);
		g.drawLine(xCenter, yLowerHorizLine, xCenter, b.yDrawnStrBottom);
		return b;
	}

	/**
	 * Draw a solid white rectangle on the word-displaying area after each display
	 * to prepare for the next one
	 * 
	 * @param g
	 *            Graphics object
	 * @param b
	 *            Bound object
	 */
	public static void drawBlankRect(Graphics g, Bound b) {
		g.setColor(Color.WHITE);
		g.fillRect(0, b.yDrawnStrTop, b.width, b.fontHeight);
		g.setColor(Color.BLACK);
	}

	/**
	 * Implements the Speed Reader function that reads a text filename, the
	 * dimensions of a drawing panel, the font size, and the word-per-minute and
	 * generates a drawing panel that displays words using a style called Rapid
	 * Serial Visual Presentation (RSVP). The total word count and sentence count
	 * are printed to the console after displaying all words.
	 * 
	 * @param filename
	 *            name of the input text file
	 * @param width
	 *            width of the drawing panel
	 * @param height
	 *            height of the drawing panel
	 * @param fontSize
	 *            font size to be used for word display
	 */
	public static void speedReader(String filename, int width, int height, int fontSize, int wpm)
			throws IOException, InterruptedException {
		DrawingPanel panel = new DrawingPanel(width, height);
		Graphics g = panel.getGraphics();
		Font f = new Font("Courier", Font.BOLD, fontSize);
		FontMetrics m = g.getFontMetrics(f);
		WordGenerator text = new WordGenerator("test.txt");
		int fontHeight = m.getHeight();
		int yCoord = (height + fontHeight / 2) / 2;
		int xCenter = width / 2;

		g.setFont(f);
		Bound b = drawFrame(g, width, height, fontHeight);

		while (text.hasNext()) {
			String nextWord = text.next();
			Offset o = findHorizOffset(nextWord, m);

			g.drawString(nextWord, xCenter - o.total, yCoord);
			g.setColor(Color.RED);
			g.drawString(nextWord.substring(o.id, o.id + 1), xCenter - o.centeredChar, yCoord);
			Thread.sleep(60000 / wpm);
			drawBlankRect(g, b);
		}
		System.out.println("Total words read: " + text.getWordCount());
		System.out.println("Total sentences read: " + text.getSentCount());
	}

	/**
	 * Converts all numeric command-line arguments from Strings to integers
	 * 
	 * @param args
	 *            array of all command-line arguments
	 * @param integer
	 *            array of all numeric command-line arguments
	 */
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
