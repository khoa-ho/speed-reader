package hw2;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class WordGenerator {
	private Scanner text;
	private int wordCount;
	private int sentCount;

	public WordGenerator(String filename) throws IOException {
		this.text = new Scanner(new File(filename));
		this.wordCount = 0;
	}

	public boolean hasNext() {
		return this.text.hasNext();
	}

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

	public int getWordCount() {
		return this.wordCount;
	}

	public int getSentCount() {
		return this.sentCount;
	}
}
