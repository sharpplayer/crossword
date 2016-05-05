package biz.computerkraft.crossword.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

	/** Live connection. */
	private Connection connection = null;

	/**
	 * Gets a connection to database.
	 * 
	 * @return connection to database
	 * @throws SQLException
	 *             on MySQL error
	 */
	private Connection getConnection() throws SQLException {
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
				returnList.add(new Word(result.getInt(1), result.getString(2)));
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
				returnList.add(new Clue(result.getInt(1), result.getString(COLUMN_CLUE)));
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
	private ResultSet executeQuery(final String sql) throws SQLException {
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
}
