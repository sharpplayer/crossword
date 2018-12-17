package biz.computerkraft.crossword.grid.crossword;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import biz.computerkraft.crossword.db.Word;
import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.gui.ClueItem;
import biz.computerkraft.crossword.pdf.BasicPdf;

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
	 * @see biz.computerkraft.crossword.grid.crossword.AbstractCrossword#
	 * setAbstractCrosswordProperties(java.util.Map)
	 */
	@Override
	protected final void setAbstractCrosswordProperties(final Map<String, Object> properties) {
		updateClues();
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
	 * @see biz.computerkraft.crossword.grid.crossword.AbstractCrossword#
	 * abstractCrosswordPostLoadTidyup()
	 */
	@Override
	public final void abstractCrosswordPostLoadTidyup() {
		updateClues();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.grid.crossword.RectangleGrid#
	 * rectangleGridAddWordContent(java.util.List,
	 * biz.computerkraft.crossword.db.Word)
	 */
	@Override
	public final void rectangleGridAddWordContent(final List<Cell> cells, final Word word) {
		updateClues();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.grid.crossword.AbstractCrossword#
	 * getAbstractCrosswordProperties(java.util.Map)
	 */
	@Override
	protected final Map<String, Object> getAbstractCrosswordProperties(final Map<String, Object> properties) {
		return properties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.Puzzle#exportPdf(java.io.File)
	 */
	@Override
	public final void exportPdf(final File file) {
		try {
			BasicPdf pdf = new BasicPdf(file, getName());
			for (Cell cell : getCells()) {
				pdf.renderRectangleCell(getCellWidth(), getCellHeight(), cell, "", true);
			}
			pdf.export();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
