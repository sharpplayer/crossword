package biz.computerkraft.crossword.grid.crossword;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlRootElement;

import biz.computerkraft.crossword.db.Word;
import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.grid.Clue;
import biz.computerkraft.crossword.gui.CellRenderer;
import biz.computerkraft.crossword.gui.ClueItem;
import biz.computerkraft.crossword.gui.cluemodel.WordsearchClueModel;
import biz.computerkraft.crossword.gui.renderer.WordsearchCellRenderer;

/**
 * 
 * Classic wordsearch puzzle.
 * 
 * @author Raymond Francis
 *
 */
@XmlRootElement(name = "wordsearch")
public class Wordsearch extends MultiDirectionGrid {

	/** Search clue category. */
	private static final String CATEGORY_SEARCH = "Search";

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.grid.Grid#getClues()
	 */
	@Override
	public final List<ClueItem> getClues() {
		List<ClueItem> clues = new ArrayList<>();
		for (Cell cell : getCells()) {
			for (Entry<Integer, Clue> clueEntry : cell.getClues().entrySet()) {
				clues.add(new ClueItem(clueEntry.getValue(), CATEGORY_SEARCH, cell, clueEntry.getKey(),
						clueEntry.getValue().getClueText()));
			}
		}
		return clues;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.grid.crossword.MultiDirectionGrid#
	 * setMultiDirectionGridProperties(java.util.Map)
	 */
	@Override
	public final void setMultiDirectionGridProperties(final Map<String, Object> properties) {
		setClueModels();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.Puzzle#getRendererClass()
	 */
	@Override
	public final CellRenderer getNewCellRenderer() {
		return new WordsearchCellRenderer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.Puzzle#getIndirectSelection(biz.
	 * computerkraft.crossword.grid.Cell, java.awt.geom.Point2D)
	 */
	@Override
	public final List<Cell> getIndirectSelection(final Cell cell, final Point2D offset) {
		return getFreeIndirectSelection(cell, offset);
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
		addCluedWordContent(cells, word);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.grid.Grid#setMarkers()
	 */
	@Override
	protected final void setMarkers() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.grid.Grid#setClueModels()
	 */
	@Override
	protected final void setClueModels() {
		addClueModel(new WordsearchClueModel(CATEGORY_SEARCH));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.grid.crossword.MultiDirectionGrid#
	 * multiDirectionGridPostLoadTidyup()
	 */
	@Override
	public final void multiDirectionGridPostLoadTidyup() {
		updateClues();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.grid.crossword.RectangleGrid#
	 * getRectangleGridProperties(java.util.Map)
	 */
	@Override
	protected final Map<String, Object> getRectangleGridProperties(final Map<String, Object> currentProperties) {
		return currentProperties;
	}

}
