package poormansgrep.BonusProject;

public class Search {
	/**
	 * USED BY singleSearch AND multiSearch Searches the String for a keyword.
	 * Returns an array with one String per line. If no match was found in that
	 * line it is empty (null).
	 * 
	 * @param keyword
	 *            the keyword to search for in the text
	 * @param text
	 *            the text which will be searched
	 * @param i
	 *            true = case sensitive | false = case insensitive
	 * @return lines of the text. empty Strings did not contain the keyword
	 */
	private static String[] search(String keyword, String text, boolean i) {

		String lines[] = text.split("\\r?\\n"); // split text
		int index = 0; // index of the line

		if (!i) {
			for (String s : lines) {
				if (!s.toLowerCase().contains(keyword.toLowerCase()))
					lines[index] = null;
				index++;
			}
		} else {
			for (String s : lines) {
				if (!s.contains(keyword))
					lines[index] = null;
				index++;
			}
		}
		return lines;
	}

	/**
	 * Uses the search method to search the String. Removes the empty parts of
	 * the Array and merges them to one String which gets returned.
	 * 
	 * @param keyword
	 *            the keyword to search for in the text
	 * @param text
	 *            the text which will be searched
	 * @param i
	 *            true = case sensitive | false = case insensitive
	 * @return String with every line that contains the keyword.
	 */
	protected static String singleSearch(String keyword, String text, boolean i) {
		String returnString = "";

		for (String s : search(keyword, text, i)) {
			if (s != null)
				returnString += s + "\n";
		}
		return returnString;
	}

	/**
	 * Uses the search method to search multiple Strings. Removes the empty
	 * parts of the Arrays and merges them to one String which gets returned.
	 * Matching lines are marked to identify in which document they were found
	 * if l is false. If l is true only the name of the document gets returned.
	 * 
	 * @param keyword
	 *            the keyword to search for in the text
	 * @param text
	 *            the text which will be searched
	 * @param i
	 *            true = case sensitive | false = case insensitive
	 * @param l
	 *            true = return only the document name | false return line +
	 *            document name
	 * @return String with every line that contains the keyword.
	 */
	protected static String multiSearch(String keyword, String[] text, boolean i, boolean l, String[] name) {
		String returnString = "";
		
		if (!l) {
			for (int j = 0; j < text.length; j++) {
				for (String s : search(keyword, text[j], i)) {
					if (s != null)
						returnString += name[j] + ": " + s + "\n"; 
				}
			}
		} else {
			for (int j = 0; j < text.length; j++) {
				for (String s : search(keyword, text[j], i)) {
					if (s != null) {
						returnString += name[j]  + "\n";
						break;
					}
				}
			}
		}
		return returnString;
	}
}
