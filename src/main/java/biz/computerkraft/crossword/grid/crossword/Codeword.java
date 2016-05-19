package biz.computerkraft.crossword.grid.crossword;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.grid.Clue;
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
	private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/** Codeword clue category. */
	private static final String CATEGORY_CODE = "Code";

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
		for (Cell cell : getCells()) {
			if (!cell.getContents().isEmpty()) {
				int index = 1 + letters.indexOf(cell.getContents());
				cell.setMarker(Integer.toString(index));
			} else {
				cell.setMarker("");
			}
		}
		updateClues();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.grid.crossword.AbstractCrossword#
	 * setClueModels()
	 */
	@Override
	protected final void setClueModels() {
		for (int letter = 0; letter < LETTERS.length(); letter++) {
			String trueLetter = LETTERS.substring(letter, letter + 1);
			clueItems.add(new ClueItem(new Clue(letters.indexOf(trueLetter) + 1, trueLetter), CATEGORY_CODE, null,
					DIRECTION_E, trueLetter));
		}
		addClueModel(new CodewordClueModel(CATEGORY_CODE));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.grid.Grid#getClues()
	 */
	@Override
	public final List<ClueItem> getClues() {
		String letterCells = LETTERS;
		for (ClueItem item : clueItems) {
			item.setStartCell(null);
		}
		for (Cell cell : getCells()) {
			if (!cell.getContents().isEmpty() && letterCells.contains(cell.getContents())) {
				clueItems.get(LETTERS.indexOf(cell.getContents())).setStartCell(cell);
				letterCells.replace(cell.getContents(), "");
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
	}

}
