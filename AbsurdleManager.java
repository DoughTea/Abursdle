import java.util.*;

public class AbsurdleManager {
	private Set<String> possibleWords;
	private int wordLength;
	
	// Given a dictionary of words and the target word length, initializes a new game of Absurdle.
	// The set of words contains all words from the dictionary of the given length, eliminating
	// any duplicates. Throws an IllegalArgumentException if the given length is less than 1
	public AbsurdleManager(Collection<String> dictionary, int length) {
		if (length < 1) {
			throw new IllegalArgumentException();
		}
		possibleWords = new TreeSet<String>();
		wordLength = length;
		Iterator<String> itr = dictionary.iterator();
		while (itr.hasNext()) {
			String word = itr.next();
			if (word.length() == length) {
				possibleWords.add(word);
			}
		}
	}
	
	// The comment for this method is provided. Do not modify this comment:
	// Params:
	//  String word -- the secret word trying to be guessed. Assumes word is made up of only
	//                 lower case letters and is the same length as guess.
	//  String guess -- the guess for the word. Assumes guess is made up of only
	//                  lower case letters and is the same length as word.
	// Exceptions:
	//   none
	// Returns:
	//   returns a string, made up of gray, yellow, or green squares, representing a
	//   standard wordle clue for the provided guess made against the provided secret word.
	public static String patternFor(String word, String guess) {
		if (word.length() < 1) {
			throw new IllegalArgumentException();
		}
		String result = "";
		Map<Character, Integer> counts = new TreeMap<Character, Integer>();
		String[] pattern = new String[word.length()];
		for (int i = 0; i < word.length(); i++) {
			char character = word.charAt(i);
			if (!counts.containsKey(character)) {
				counts.put(character, 1);
			} else {
				counts.put(character, counts.get(character) + 1);
			}
		}
		
		for (int i = 0; i < guess.length(); i++) {
			if (guess.charAt(i) == word.charAt(i)) {
				pattern[i] = "🟩";
				counts.put(guess.charAt(i), counts.get(guess.charAt(i)) - 1);
			}
		}
		for (int i = 0; i < guess.length(); i++) {
			if (counts.containsKey(guess.charAt(i)) && counts.get(guess.charAt(i)) > 0 && pattern[i] == null) {
				pattern[i] = "🟨";
				counts.put(guess.charAt(i), counts.get(guess.charAt(i)) - 1);
			}
		}
		for (String ob : pattern) {
			if (ob == null) {
				result = result + "⬜";
			} else {
				result = result + ob;
			}
		}
		
		return result;
	}
	
	// Gives access to the current set of words considered by the manager
	public Set<String> words() {
		return possibleWords;
	}
	
	// The client calls this method to record a guess. Using the given guess, this method
	// determines the next set of words under consideration and returns the pattern for the
	// guess. Throws an IllegalStateException if the set of words is empty, and throws an
	// IllegalArgumentException if the guess does not have the correct length.
	public String record(String guess) {
		if (possibleWords.size() == 0) {
			throw new IllegalStateException();
		} else if (guess.length() != wordLength) {
			throw new IllegalArgumentException();
		}
		Map<String, Set<String>> possiblePatterns = new TreeMap<String, Set<String>>();
		String result = "";
		int largestCount = 0;
		
		Iterator<String> itr = possibleWords.iterator();
		while (itr.hasNext()) {
			String possibleWord = itr.next();
			String pattern = patternFor(possibleWord, guess);
			if (!possiblePatterns.containsKey(pattern)) {
				possiblePatterns.put(pattern, new TreeSet<String>());
			}
			possiblePatterns.get(pattern).add(possibleWord);
		}
		int counts = 0;
		for (String patternLength : possiblePatterns.keySet()) {
			counts = possiblePatterns.get(patternLength).size();
			if (counts > largestCount) {
				largestCount = counts;
				result = patternLength;
			}
		}
		possibleWords = possiblePatterns.get(result);
		return result;
	}
}