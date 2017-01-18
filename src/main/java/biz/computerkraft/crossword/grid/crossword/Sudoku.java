package biz.computerkraft.crossword.grid.crossword;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import biz.computerkraft.crossword.db.Word;
import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.grid.Clue;
import biz.computerkraft.crossword.gui.CellRenderer;
import biz.computerkraft.crossword.gui.ClueItem;
import biz.computerkraft.crossword.gui.cluemodel.WordsearchClueModel;
import biz.computerkraft.crossword.gui.renderer.SudokuCellRenderer;

/**
 * 
 * Clued sudoku variant.
 * 
 * @author Raymond Francis
 *
 */
@XmlRootElement(name = "sudoku")
public class Sudoku extends MultiDirectionGrid {

	/** Search clue category. */
	private static final String CATEGORY_CLUES = "Clues";

	/** Invalid cell text. */
	private static final String TEXT_INVALID_CELL = "*";

	/** Default cell group width. */
	private static final int DEFAULT_CELL_GROUP_WIDTH = 3;

	/** Default cell group height. */
	private static final int DEFAULT_CELL_GROUP_HEIGHT = 3;

	/** Property name for cell group height. */
	private static final String PROPERTY_CELL_GROUP_WIDTH = "Cell Group Width";

	/** Property name for cell group height. */
	private static final String PROPERTY_CELL_GROUP_HEIGHT = "Cell Group Height";

	/** Markers options. */
	private static final String MARKERS = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZa";

	/** Cell group width. */
	private int cellGroupWidth = DEFAULT_CELL_GROUP_WIDTH;

	/** Cell group width. */
	private int cellGroupHeight = DEFAULT_CELL_GROUP_HEIGHT;

	/** Sudoku rows. */
	private List<List<Cell>> rows = new ArrayList<>();

	/** Sudoku columns. */
	private List<List<Cell>> colums = new ArrayList<>();

	/** Sudoku blocks. */
	private List<List<Cell>> blocks = new ArrayList<>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.grid.crossword.RectangleGrid#
	 * getRectangleGridProperties(java.util.Map)
	 */
	@Override
	protected final Map<String, Object> getRectangleGridProperties(final Map<String, Object> properties) {
		if (!properties.containsKey(PROPERTY_CELL_GROUP_WIDTH)) {
			properties.remove(RectangleGrid.PROPERTY_WIDTH);
			properties.remove(RectangleGrid.PROPERTY_HEIGHT);
			properties.put(PROPERTY_CELL_GROUP_WIDTH, cellGroupWidth);
			properties.put(PROPERTY_CELL_GROUP_HEIGHT, cellGroupHeight);
		}
		return properties;
	}

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
				clues.add(new ClueItem(clueEntry.getValue(), CATEGORY_CLUES, cell, clueEntry.getKey(),
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
		cellGroupWidth = (int) properties.get(PROPERTY_CELL_GROUP_HEIGHT);
		cellGroupHeight = (int) properties.get(PROPERTY_CELL_GROUP_WIDTH);
		properties.put(PROPERTY_WIDTH, cellGroupWidth * cellGroupHeight);
		properties.put(PROPERTY_HEIGHT, cellGroupWidth * cellGroupHeight);
		setMultiDirectionGridProperties(properties);
		Map<String, Cell> grid = getCellMap();
		for (int x = 1; x < cellGroupHeight; x++) {
			for (int y = 0; y < cellGroupWidth * cellGroupHeight; y++) {
				String name = (x * cellGroupWidth - 1) + ":" + y;
				grid.get(name).setBlock(DIRECTION_E, true);
			}
		}
		for (int x = 0; x < cellGroupHeight * cellGroupHeight; x++) {
			for (int y = 1; y < cellGroupWidth; y++) {
				String name = x + ":" + (y * cellGroupHeight - 1);
				grid.get(name).setBlock(DIRECTION_S, true);
			}
		}

		String markers = getMarkers();
		for (Cell cell : getCells()) {
			cell.setMarker(markers);
		}
		setCellGroups();
		setSudokuCellGroups();
		setClueModels();
	}

	/**
	 * Gets the cell group width.
	 * 
	 * @return the cellGroupWidth
	 */
	@XmlElement(name = "cellGroupWidth")
	public final int getCellGroupWidth() {
		return cellGroupWidth;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.grid.crossword.MultiDirectionGrid#
	 * multiDirectionGridPostLoadTidyup()
	 */
	@Override
	public final void multiDirectionGridPostLoadTidyup() {
		setSudokuCellGroups();
		setMarkers();
		updateClues();
	}

	/**
	 * Sets the cell group width.
	 * 
	 * @param newCellGroupWidth
	 *            the cell group width
	 */
	public final void setCellGroupWidth(final int newCellGroupWidth) {
		cellGroupWidth = newCellGroupWidth;
	}

	/**
	 * Gets cell group height.
	 * 
	 * @return the cell group height
	 */
	@XmlElement(name = "cellGroupHeight")
	public final int getCellGroupHeight() {
		return cellGroupHeight;
	}

	/**
	 * 
	 * Sets cell group height.
	 * 
	 * @param newCellGroupHeight
	 *            the new cell group height
	 */
	public final void setCellGroupHeight(final int newCellGroupHeight) {
		cellGroupHeight = newCellGroupHeight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.Puzzle#getNewCellRenderer()
	 */
	@Override
	public final CellRenderer getNewCellRenderer() {
		return new SudokuCellRenderer(cellGroupWidth, cellGroupHeight, TEXT_INVALID_CELL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.Puzzle#getIndirectSelection(biz.
	 * computerkraft.crossword.grid.Cell, java.awt.geom.Point2D)
	 */
	@Override
	public final List<Cell> getIndirectSelection(final Cell cell, final Point2D offset) {
		return getRestrictedIndirectSelection(cell, offset, true, true);
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
		setMarkers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.grid.Grid#setMarkers()
	 */
	@Override
	protected final void setMarkers() {
		String markers = getMarkers();
		for (Cell cell : getCells()) {
			if (cell.getContents().isEmpty() || !cell.getTransientContents().isEmpty()) {
				cell.setContents("");
				cell.setMarker(markers);
			} else {
				cell.setMarker("");
			}
			cell.setTransientContents("");
		}

		boolean repeat = true;
		while (repeat) {
			repeat = false;
			for (List<Cell> cellBlock : rows) {
				repeat |= updateCellBlock(cellBlock);
			}
			for (List<Cell> cellBlock : colums) {
				repeat |= updateCellBlock(cellBlock);
			}
			for (List<Cell> cellBlock : blocks) {
				repeat |= updateCellBlock(cellBlock);
			}
		}

		for (Cell cell : getCells()) {
			if (cell.isEmpty() && cell.getMarker().trim().length() == 0) {
				cell.setTransientContents(TEXT_INVALID_CELL);
			}
		}

	}

	/**
	 * Stores cells in rows columns and groups.
	 */
	private void setSudokuCellGroups() {
		Cell start = getCells().iterator().next();
		for (int list = 0; list < cellGroupHeight * cellGroupWidth; list++) {
			blocks.add(new ArrayList<>());
		}
		int row = 0;
		do {
			List<Cell> rowOfCells = getLineOfCells(start, DIRECTION_E);
			int x = 0;
			for (Cell cell : rowOfCells) {
				blocks.get((row / cellGroupHeight) * cellGroupWidth + x / cellGroupWidth).add(cell);
				x++;
			}
			rows.add(rowOfCells);
			Optional<Cell> optionalStart = start.getAdjacent(DIRECTION_S);
			if (optionalStart.isPresent()) {
				start = optionalStart.get();
				row++;
			} else {
				start = null;
			}
		} while (start != null);

		start = getCells().iterator().next();
		Cell columnStart = start;
		do {
			List<Cell> columnOfCells = getLineOfCells(columnStart, DIRECTION_S);
			colums.add(columnOfCells);

			Optional<Cell> optionalStart = columnStart.getAdjacent(DIRECTION_E);
			if (optionalStart.isPresent()) {
				columnStart = optionalStart.get();
			} else {
				columnStart = null;
			}
		} while (columnStart != null);
	}

	/**
	 * Evaluates options in a cell block.
	 * 
	 * @param block
	 *            row, column or block to analyse.
	 * @return true if a change in cell occurred
	 */
	private boolean updateCellBlock(final List<Cell> block) {
		String used = "";
		String markers = getMarkers();
		for (Cell cell : block) {
			if (!cell.getContents().isEmpty()) {
				used += cell.getContents();
			}
		}
		boolean repeat = true;
		boolean change = false;
		String count = "";
		while (repeat) {
			while (repeat) {
				count = "";
				repeat = false;
				String regeXused = "[" + used + "]";
				for (Cell cell : block) {
					if (cell.isEmpty()) {
						if (!used.isEmpty()) {
							cell.setMarker(cell.getMarker().replaceAll(regeXused, " "));
							if (cell.getMarker().trim().length() == 1) {
								cell.setTransientContents(cell.getMarker().trim());
								cell.setMarker("");
								used += cell.getTransientContents();
								regeXused = "[" + used + "]";
								count = count.replaceAll(cell.getTransientContents(), " ");
								repeat = true;
								change = true;
							} else {
								count += cell.getMarker();
							}
						} else {
							count += cell.getMarker();
						}
					}
				}
			}
			repeat = false;
			for (int index = 0; index < markers.length(); index++) {
				String marker = markers.substring(index, index + 1);
				if (countMatches(count, marker) == 1) {
					for (Cell cell : block) {
						if (cell.getMarker().contains(marker)) {
							cell.setTransientContents(marker);
							cell.setMarker("");
							used += cell.getTransientContents();
							repeat = true;
							change = true;
						}
					}
				}
			}

			Map<String, Integer> groups = new HashMap<>();
			for (Cell cell : block) {
				String marker = cell.getMarker();
				if (!marker.isEmpty()) {
					if (groups.containsKey(marker)) {
						groups.put(marker, groups.get(marker) + 1);
					} else {
						groups.put(marker, 1);
					}
				}
			}

			for (Entry<String, Integer> entry : groups.entrySet()) {
				String compactMarker = entry.getKey().replaceAll(" ", "");
				if (compactMarker.length() == entry.getValue()) {
					String regeXused = "[" + compactMarker + "]";
					for (Cell cell : block) {
						if (!cell.getMarker().equals(entry.getKey())) {
							cell.setMarker(cell.getMarker().replaceAll(regeXused, " "));
							if (cell.getMarker().trim().length() == 1) {
								cell.setTransientContents(cell.getMarker().trim());
								count += cell.getMarker();
								cell.setMarker("");
								used += cell.getTransientContents();
								repeat = true;
								change = true;
							}
						}
					}
				}
			}

		}

		return change;

	}

	/**
	 * Gets a line of cells in a given direction, ignoring word breaks.
	 * 
	 * @param start
	 *            start cell
	 * @param direction
	 *            direction from start cell
	 * @return line of cells
	 */
	private List<Cell> getLineOfCells(final Cell start, final int direction) {
		Optional<Cell> optionalStart;
		List<Cell> listOfCells = new ArrayList<>();
		Cell rowStart = start;
		do {
			listOfCells.add(rowStart);
			optionalStart = rowStart.getAdjacent(direction);
			if (optionalStart.isPresent()) {
				rowStart = optionalStart.get();
			} else {
				rowStart = null;
			}
		} while (rowStart != null);

		return listOfCells;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.grid.crossword.RectangleGrid#
	 * rectangleGridAddCellContent(biz.computerkraft.crossword.grid.Cell,
	 * java.lang.String)
	 */
	@Override
	public final void rectangleGridAddCellContent(final Cell cell, final String content) {
		String markers = cell.getMarker();
		if (markers.contains(content.toUpperCase())) {
			addCellContent(cell, content.toUpperCase());
			setMarkers();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.grid.crossword.RectangleGrid#
	 * rectangleGridClearCellContent(biz.computerkraft.crossword.grid.Cell)
	 */
	@Override
	public final void rectangleGridClearCellContent(final Cell cell) {
		clearCellContent(cell, true);
		setMarkers();
	}

	/**
	 * 
	 * Gets the markers string.
	 * 
	 * @return possible markers.
	 */
	private String getMarkers() {
		return MARKERS.substring(0, cellGroupHeight * cellGroupWidth);
	}

	/**
	 * 
	 * Counts occurences of string in another.
	 * 
	 * @param haystack
	 *            string to search
	 * @param substring
	 *            string to search for
	 * @return number of occurrences
	 */
	private int countMatches(final String haystack, final String substring) {
		int count = 0;
		int index = 0;
		while ((index = haystack.indexOf(substring, index)) != -1) {
			count++;
			index += substring.length();
		}
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.grid.Grid#setClueModels()
	 */
	@Override
	protected final void setClueModels() {
		addClueModel(new WordsearchClueModel(CATEGORY_CLUES));
	}
}
