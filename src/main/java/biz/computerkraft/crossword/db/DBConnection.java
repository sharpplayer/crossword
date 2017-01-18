package biz.computerkraft.crossword.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import biz.computerkraft.crossword.grid.Clue;

/**
 * 
 * Manages database connection.
 * 
 * @author Raymond Francis
 *
 */
public class DBConnection {

	/** Clue column. */
	private static final int COLUMN_CLUE = 3;

	/** Clue id column. */
	private static final int COLUMN_CLUE_ID = 1;

	/** Word column. */
	private static final int COLUMN_WORD = 2;

	/** Word id column. */
	private static final int COLUMN_WORD_ID = 1;

	/** Verse id column. */
	private static final int COLUMN_VERSE_ID = 1;

	/** Book id column. */
	private static final int COLUMN_BOOK = 2;

	/** Chapter column. */
	private static final int COLUMN_CHAPTER = 3;

	/** Verse column. */
	private static final int COLUMN_VERSE = 4;

	/** Book id column. */
	private static final int COLUMN_BOOK_ID = 1;

	/** Book name column. */
	private static final int COLUMN_BOOK_NAME = 2;

	/** Book short name column. */
	private static final int COLUMN_BOOK_SHORTNAME = 2;

	/** Text column. */
	private static final int COLUMN_TEXT = 1;

	/** Live connection. */
	private static Connection connection = null;

	/** Short book names. */
	private static Map<Integer, String> shortNames = null;

	/** Long book names. */
	private static Map<Integer, String> fullNames = null;

	/**
	 * Gets a connection to database.
	 * 
	 * @return connection to database
	 * @throws SQLException
	 *             on MySQL error
	 */
	private static Connection getConnection() throws SQLException {
		if (connection == null) {
			connection = DriverManager.getConnection("jdbc:mysql:///bible", "root", "weasel");
		}
		return connection;
	}

	/**
	 * 
	 * Gets a list of words that match filter. Spaces are wildcards.
	 * 
	 * @param filter
	 *            word filter
	 * @param sortLetter
	 *            letter in word to sort by
	 * @return possible word matches from database
	 */
	public final List<Word> getWords(final String filter, final int sortLetter) {

		List<Word> returnList = new ArrayList<>();

		String sql = "SELECT *, substring(Word, " + sortLetter + ") a FROM tblword WHERE Word LIKE '"
				+ filter.replace(" ", "_") + "' ORDER BY a, Word";
		try {
			ResultSet result = executeQuery(sql);
			while (result.next()) {
				returnList.add(new Word(result.getInt(COLUMN_WORD_ID), result.getString(COLUMN_WORD)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnList;

	}

	/**
	 * 
	 * @param word
	 *            word to clue hunt
	 * @return list of save clues for word
	 */
	public final List<Clue> getClues(final String word) {
		List<Clue> returnList = new ArrayList<>();

		String sql = "SELECT * FROM tblclue WHERE Word = '" + word + "' ORDER BY Word, Clue";
		try {
			ResultSet result = executeQuery(sql);
			while (result.next()) {
				returnList.add(new Clue(result.getInt(COLUMN_CLUE_ID), result.getString(COLUMN_CLUE)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnList;

	}

	/**
	 * 
	 * Executes my sql query.
	 * 
	 * @param sql
	 *            sql to execute
	 * @return recordset from query
	 * @throws SQLException
	 *             any sql errors
	 */
	private static ResultSet executeQuery(final String sql) throws SQLException {
		Statement query = getConnection().createStatement();
		if (sql.startsWith("SELECT")) {
			return query.executeQuery(sql);
		} else if (sql.startsWith("INSERT")) {
			query.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			return query.getGeneratedKeys();
		} else {
			query.executeUpdate(sql);
			return null;
		}
	}

	/**
	 * 
	 * Saves a clue to the database.
	 * 
	 * @param word
	 *            word to associate clue with
	 * @param clue
	 *            clue to save
	 */
	public final void saveClue(final String word, final Clue clue) {
		try {
			if (clue.getClueId() == 0) {
				ResultSet insert = executeQuery(
						"INSERT INTO tblclue(Word, Clue) VALUES('" + word + "', '" + clue.getClueText() + "')");
				if (insert.next()) {
					clue.setClueId(insert.getInt(COLUMN_CLUE_ID));
				}
			} else {
				executeQuery("UPDATE tblclue SET Clue='" + clue.getClueText() + "' WHERE ClueId=" + clue.getClueId());
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 * Gets verses containing given word.
	 * 
	 * @param word
	 *            word to search for.
	 * @return list of words
	 */
	public final List<Verse> getVersesWithWord(final Word word) {
		List<Verse> list = new ArrayList<>();
		String sql = "SELECT * FROM tblverse WHERE VerseId IN (Select VerseId FROM tbltext WHERE WordId="
				+ word.getIdentifier() + ")";
		try {
			ResultSet result = executeQuery(sql);
			while (result.next()) {
				list.add(new Verse(result.getInt(COLUMN_VERSE_ID), result.getInt(COLUMN_BOOK),
						result.getInt(COLUMN_CHAPTER), result.getInt(COLUMN_VERSE)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 
	 * Gets long book names.
	 * 
	 * @return map of long booknames.
	 */
	public static final Map<Integer, String> getFullBookNames() {
		getBooks();
		return fullNames;
	}

	/**
	 * 
	 * Gets short book names.
	 * 
	 * @return map of short booknames.
	 */
	public static final Map<Integer, String> getShortBookNames() {
		getBooks();
		return shortNames;

	}

	/**
	 * Gets the book names.
	 */
	private static void getBooks() {
		if (fullNames == null) {
			fullNames = new HashMap<>();
			shortNames = new HashMap<>();
			String sql = "SELECT * FROM tblbook";
			try {
				ResultSet result = executeQuery(sql);
				while (result.next()) {
					fullNames.put(result.getInt(COLUMN_BOOK_ID), result.getString(COLUMN_BOOK_NAME));
					shortNames.put(result.getInt(COLUMN_BOOK_ID), result.getString(COLUMN_BOOK_SHORTNAME));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Gets verse text.
	 * 
	 * @param verseId
	 *            verse to get text for
	 * 
	 * @return text of given verse
	 */
	public final String getVerse(final int verseId) {
		String sql = "SELECT Word FROM tbltext t LEFT JOIN tblword w ON t.WordId = w.WordId WHERE VerseId = " + verseId
				+ " ORDER BY WordNumber";
		String text = "";
		String conjunction = "";
		try {
			ResultSet result = executeQuery(sql);
			while (result.next()) {
				text = text + conjunction + result.getString(COLUMN_TEXT);
				conjunction = " ";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;

	}
}
