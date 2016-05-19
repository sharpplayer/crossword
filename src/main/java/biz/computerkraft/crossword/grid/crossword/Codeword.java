package biz.computerkraft.crossword.grid.crossword;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import biz.computerkraft.crossword.db.Word;
import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.grid.Clue;
import biz.computerkraft.crossword.grid.crossword.enumeration.Encoding;
import biz.computerkraft.crossword.gui.ClueItem;
import biz.computerkraft.crossword.gui.cluemodel.CodewordClueModel;

/**
 * 
 * Basic crossword puzzle.
 * 
 * @author Raymond Francis
 *
 */
@XmlRootElement(name = "codeword")
public class Codeword extends AbstractFillCrossword {

	/** Codeword clue letter order. */
	private static final String PROPERTY_ENCODING = "Encoding";

	/** Codeword clue letter order. */
	private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/** Codeword clue category. */
	private static final String CATEGORY_CODE = "Code";

	/** How to encode. */
	private Encoding encoding = Encoding.RANDOM;

	/** Random seed. */
	private long seed = System.nanoTime();

	/** Codeword letter order. */
	private String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/** Code list to present to clue model. */
	private List<ClueItem> clueItems = new ArrayList<>();

	/**
	 * 
	 * Get letters code.
	 * 
	 * @return letters code
	 */
	@XmlElement(name = "letters")
	public final String getLetters() {
		return letters;
	}

	/**
	 * 
	 * Sets the letter code.
	 * 
	 * @param newLetters
	 *            new letter code
	 */
	public final void setLetters(final String newLetters) {
		letters = newLetters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.grid.crossword.AbstractCrossword#setMarkers()
	 */
	@Override
	protected final void setMarkers() {

		String lettersToUse = LETTERS;
		String oldLetters = letters;
		if (encoding == Encoding.GRID) {
			letters = "";
			for (Cell cell : getCells()) {
				if (!cell.getContents().isEmpty() && lettersToUse.contains(cell.getContents())) {
					letters += cell.getContents();
					lettersToUse = lettersToUse.replace(cell.getContents(), "");
				}
			}
			letters += lettersToUse;
		}

		for (Cell cell : getCells()) {
			if (!cell.isEmpty()) {
				int index = 1 + letters.indexOf(cell.getDisplayContents());
				cell.setMarker(Integer.toString(index));
			} else {
				cell.setMarker("");
			}
		}

		if (!oldLetters.equals(letters)) {
			updateClues();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.grid.crossword.AbstractCrossword#
	 * setClueModels()
	 */
	@Override
	protected final void setClueModels() {
		addClueModel(new CodewordClueModel(CATEGORY_CODE));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.grid.Grid#getClues()
	 */
	@Override
	public final List<ClueItem> getClues() {
		for (int letter = 0; letter < LETTERS.length(); letter++) {
			String trueLetter = LETTERS.substring(letter, letter + 1);
			clueItems.add(new ClueItem(new Clue(letters.indexOf(trueLetter) + 1, trueLetter), CATEGORY_CODE, null,
					DIRECTION_E, trueLetter));
		}

		String letterCells = LETTERS;
		for (ClueItem item : clueItems) {
			item.setStartCell(null);
		}
		for (Cell cell : getCells()) {
			if (!cell.getDisplayContents().isEmpty() && letterCells.contains(cell.getDisplayContents())) {
				clueItems.get(LETTERS.indexOf(cell.getDisplayContents())).setStartCell(cell);
				letterCells.replace(cell.getDisplayContents(), "");
				if (letterCells.length() == 0) {
					break;
				}
			}
		}
		return clueItems;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.Puzzle#addCellContent(biz.computerkraft.
	 * crossword.grid.Cell, java.lang.String)
	 */
	@Override
	public final void addCellContent(final Cell cell, final String content) {
		baseAddCellContent(cell, content);
		setMarkers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.Puzzle#clearCellContent(biz.computerkraft
	 * .crossword.grid.Cell)
	 */
	@Override
	public final void clearCellContent(final Cell cell) {
		baseClearCellContent(cell);
		setMarkers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.Puzzle#postLoadTidyup()
	 */
	@Override
	public final void postLoadTidyup() {
		abstractCrosswordPostLoadTidyup();
		setMarkers();
		updateClues();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.Puzzle#setProperties(java.util.Map)
	 */
	@Override
	public final void setProperties(final Map<String, Object> properties) {
		setAbstractCrosswordProperties(properties);
		encoding = (Encoding) properties.get(PROPERTY_ENCODING);
		if (encoding == Encoding.NEWSEED) {
			encoding = Encoding.RANDOM;
			seed = System.nanoTime();
		}

		String lettersToUse = LETTERS;
		if (encoding == Encoding.RANDOM) {
			Random random = new Random(seed);
			letters = "";
			while (lettersToUse.length() > 0) {
				int index = random.nextInt(lettersToUse.length());
				String letter = lettersToUse.substring(index, index + 1);
				letters += letter;
				lettersToUse = lettersToUse.replace(letter, "");
			}
		}
		updateClues();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.Puzzle#getProperties()
	 */
	@XmlTransient
	@Override
	public final Map<String, Object> getProperties() {
		Map<String, Object> properties = getAbstractCrosswordProperties();
		properties.put(PROPERTY_ENCODING, encoding);
		return properties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.Puzzle#addWordContent(java.util.List,
	 * biz.computerkraft.crossword.db.Word)
	 */
	@Override
	public final void addWordContent(final List<Cell> cells, final Word word) {
		baseAddWordContent(cells, word);
		setMarkers();
	}

	/**
	 * 
	 * How to encode letters.
	 * 
	 * @return the encoding
	 */
	public final Encoding getEncoding() {
		return encoding;
	}

	/**
	 * 
	 * Sets the encoding.
	 * 
	 * @param newEncoding
	 *            the encoding to set
	 */
	public final void setEncoding(final Encoding newEncoding) {
		this.encoding = newEncoding;
	}

}
