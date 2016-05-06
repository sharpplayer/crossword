package biz.computerkraft.crossword.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.gui.input.CrosswordCellAction;
import biz.computerkraft.crossword.gui.input.CrosswordKeyListener;
import biz.computerkraft.crossword.gui.input.CrosswordMouseAdapter;
import biz.computerkraft.crossword.gui.input.InputListener;

/**
 * 
 * Crossword panel.
 * 
 * @author Raymond Francis
 *
 */
public class CrosswordPanel extends JPanel implements InputListener {

	/** Serial id. */
	private static final long serialVersionUID = 6617629272499072181L;

	/** List of cell renderers. */
	private final List<CellRenderer> renderers = new ArrayList<>();

	/** Preferred size with no zoom. */
	private Dimension preferredSize;

	/** Zoom percentage. */
	private double zoomPc = 1;

	/** Direct selections. */
	private final List<Cell> directSelections = new ArrayList<>();

	/** Indirect selections. */
	private final List<List<Cell>> indirectSelections = new ArrayList<>();

	/** Cell selection listener. */
	private final CellSelectListener listener;

	/** Cell update listener. */
	private final CellUpdateListener updateListener;

	/** Popup menu. */
	private final JPopupMenu popup = new JPopupMenu();

	/**
	 * Constructor.
	 * 
	 * @param newListener
	 *            new cell seelction listener
	 * @param newUpdateListener
	 *            listener to update cells
	 */
	public CrosswordPanel(final CellSelectListener newListener, final CellUpdateListener newUpdateListener) {
		setFocusable(true);
		addMouseListener(new CrosswordMouseAdapter(this));
		addKeyListener(new CrosswordKeyListener(this));
		listener = newListener;
		updateListener = newUpdateListener;
	}

	/**
	 * 
	 * Sets screen dimensions of puzzle.
	 * 
	 * @param newPreferredSize
	 *            size required to represent puzzle
	 */
	public final void reset(final Dimension newPreferredSize) {
		preferredSize = newPreferredSize;
		renderers.clear();
		directSelections.clear();
		indirectSelections.clear();
		repaint();
	}

	/**
	 * 
	 * Adds a cell renderer.
	 * 
	 * @param renderer
	 *            cell renderer
	 */
	public final void addRenderer(final CellRenderer renderer) {
		renderers.add(renderer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected final void paintComponent(final Graphics g) {
		super.paintComponent(g);
		Graphics2D graphics2d = (Graphics2D) g;
		graphics2d.setColor(getBackground());
		graphics2d.fillRect(0, 0, getWidth(), getHeight());
		Dimension size = getPreferredSize();
		graphics2d.setColor(Color.WHITE);
		graphics2d.fillRect(0, 0, (int) size.getWidth(), (int) size.getHeight());
		for (CellRenderer renderer : renderers) {
			renderer.renderCell(graphics2d, size.getWidth(), size.getHeight(), getCellSelection(renderer.getCell()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	@Override
	public final Dimension getPreferredSize() {
		Dimension dimension = new Dimension();
		if (preferredSize != null) {
			dimension.setSize(preferredSize.getWidth() * zoomPc, preferredSize.getHeight() * zoomPc);
		}
		return dimension;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.input.InputListener#selectCellAt(double,
	 * double, boolean)
	 */
	@Override
	public final void selectCellAt(final double x, final double y, final boolean withPopup) {
		requestFocusInWindow();
		Point2D point = new Point2D.Double(x, y);
		for (CellRenderer renderer : renderers) {
			Shape shape = renderer.getCellShape();
			if (shape.contains(point)) {
				Rectangle2D bounds = shape.getBounds2D();
				Point2D selectionOffset = new Point2D.Double((x - bounds.getX()) / bounds.getWidth(),
						(y - bounds.getY()) / bounds.getHeight());
				listener.selectCell(renderer.getCell(), selectionOffset);
				if (withPopup) {
					popup.removeAll();
					List<String> actions = new ArrayList<>();
					updateListener.populateCellMenu(renderer.getCell(), actions);
					for (String action : actions) {
						JMenuItem item = new JMenuItem(action);
						item.setAction(new CrosswordCellAction(renderer.getCell(), action, updateListener, this));
						popup.add(item);
					}
					popup.show(this, (int) x, (int) y);
				}
				break;
			}
		}
	}

	/**
	 * 
	 * Gets selection mode of cell.
	 * 
	 * @param cell
	 *            cell to test for selection.
	 * @return selection mode of cell
	 */
	private Selection getCellSelection(final Cell cell) {
		if (directSelections.contains(cell)) {
			return Selection.DIRECT;
		} else {
			for (List<Cell> indirectSelection : indirectSelections) {
				if (indirectSelection.contains(cell)) {
					return Selection.INDIRECT;
				}
			}
		}
		return Selection.NONE;

	}

	/**
	 * 
	 * Sets the direct selection to given cell.
	 * 
	 * @param selection
	 *            cell to show as selected
	 */
	public final void setDirectSelection(final Cell selection) {
		directSelections.clear();
		directSelections.add(selection);
		repaint();
	}

	/**
	 * 
	 * Sets the direct selection one on from current cell.
	 * 
	 */
	private void setDirectSelectionNext() {
		List<Cell> newDirectSelection = new ArrayList<>();
		for (Cell cell : directSelections) {
			boolean select = false;
			for (List<Cell> cells : indirectSelections) {
				if (cells.contains(cell)) {
					for (Cell selectCell : cells) {
						if (select) {
							newDirectSelection.add(selectCell);
							break;
						} else if (selectCell.equals(cell)) {
							select = true;
						}

					}
				}
			}
		}
		directSelections.clear();
		directSelections.addAll(newDirectSelection);
		repaint();
	}

	/**
	 * 
	 * Sets the direct selection one back from current cell.
	 * 
	 */
	private void setDirectSelectionPrevious() {
		List<Cell> newDirectSelection = new ArrayList<>();
		for (Cell cell : directSelections) {
			for (List<Cell> cells : indirectSelections) {
				if (cells.contains(cell)) {
					Cell select = cell;
					for (Cell selectCell : cells) {
						if (selectCell.equals(cell)) {
							break;
						}
						select = selectCell;
					}
					newDirectSelection.add(select);
				}
			}
		}
		directSelections.clear();
		directSelections.addAll(newDirectSelection);
		repaint();
	}

	/**
	 *
	 * Gets the first indirect selection.
	 * 
	 * @return indirect selection
	 */
	public final List<Cell> getFirstIndirectSelection() {
		if (indirectSelections.size() > 0) {
			return indirectSelections.get(0);
		} else {
			return new ArrayList<>();
		}
	}

	/**
	 * 
	 * Sets the indirect selection.
	 * 
	 * @param selection
	 *            cell to show as selected
	 */
	public final void setIndirectSelection(final List<Cell> selection) {
		indirectSelections.clear();
		indirectSelections.add(selection);
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.input.InputListener#addCellContent(java.
	 * lang.String)
	 */
	@Override
	public final void addCellContent(final String content) {
		for (Cell cell : directSelections) {
			updateListener.addCellContent(cell, content);
		}
		setDirectSelectionNext();
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.input.InputListener#deleteCellContent()
	 */
	@Override
	public final void deleteCellContent() {
		for (Cell cell : directSelections) {
			updateListener.clearCellContent(cell);
		}
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.input.InputListener#backspaceCellContent(
	 * )
	 */
	@Override
	public final void backspaceCellContent() {
		setDirectSelectionPrevious();
		for (Cell cell : directSelections) {
			updateListener.clearCellContent(cell);
		}
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.input.InputListener#moveLeft()
	 */
	@Override
	public final void moveLeft() {
		List<Cell> currentSelection = new ArrayList<>();
		currentSelection.addAll(directSelections);
		// indirectSelections.clear();
		for (Cell cell : currentSelection) {
			listener.selectCellLeft(cell);
		}
		repaint();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.input.InputListener#moveUp()
	 */
	@Override
	public final void moveUp() {
		List<Cell> currentSelection = new ArrayList<>();
		currentSelection.addAll(directSelections);
		indirectSelections.clear();
		for (Cell cell : currentSelection) {
			listener.selectCellUp(cell);
		}
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.input.InputListener#moveRight()
	 */
	@Override
	public final void moveRight() {
		List<Cell> currentSelection = new ArrayList<>();
		currentSelection.addAll(directSelections);
		indirectSelections.clear();
		for (Cell cell : currentSelection) {
			listener.selectCellRight(cell);
		}
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.input.InputListener#moveDown()
	 */
	@Override
	public final void moveDown() {
		List<Cell> currentSelection = new ArrayList<>();
		currentSelection.addAll(directSelections);
		indirectSelections.clear();
		for (Cell cell : currentSelection) {
			listener.selectCellDown(cell);
		}
		repaint();
	}
}
