package biz.computerkraft.crossword.grid.crossword;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.gui.ClueItem;

/**
 * 
 * Basic crossword puzzle.
 * 
 * @author Raymond Francis
 *
 */
@XmlRootElement(name = "crossword")
public class Crossword extends AbstractFillCrossword {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.grid.crossword.AbstractCrossword#setMarkers()
	 */
	@Override
	protected final void setMarkers() {
		setAcrossDownMarkers();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.grid.crossword.AbstractCrossword#
	 * setClueModels()
	 */
	@Override
	protected final void setClueModels() {
		setAcrossDownClueModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.grid.Grid#getClues()
	 */
	@Override
	public final List<ClueItem> getClues() {
		return getAcrossDownClues();
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
	}

}
