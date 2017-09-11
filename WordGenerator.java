package hw2;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;

/**
 * Class Generator reads in text from a text file and logs statistics about the
 * text that is read
 * 
 * @author Enrique Rueda & Khoa Ho
 * @since 9/1/2017
 */
public class WordGenerator {
	private Scanner text;
	private int wordCount;
	private int sentCount;

	/**
	 * Constructor for the WordGenerator class
	 * 
	 * @param filename
	 *            name of the input text file
	 * @throws IOException
	 */
	public WordGenerator(String filename) throws IOException {
		this.text = new Scanner(new File(filename));
		this.wordCount = 0;
	}

	/**
	 * Checks whether a WordGenerator object has words left to read
	 * 
	 * @return true if it has any word left, false otherwise
	 */
	public boolean hasNext() {
		return this.text.hasNext();
	}

	/**
	 * Returns the next word and increments the word count and sentence count (when
	 * applicable)
	 * 
	 * @return the next word in the file
	 */
	public String next() {
		// Regular expression for a word ended with ".", "?", or "!"
		String puncRegex = "\\w+[.!?]";
		String nextWord = this.text.next();
		this.wordCount++;
		// If there is a match, then the sentence has ended.
		if (nextWord.matches(puncRegex)) {
			this.sentCount++;
		}
		return nextWord;
	}

	/**
	 * Gets the total number of words processed
	 * 
	 * @return the word count
	 */
	public int getWordCount() {
		return this.wordCount;
	}

	/**
	 * Gets the total number of sentences processed
	 * 
	 * @return the sentence count
	 */
	public int getSentCount() {
		return this.sentCount;
	}
}
