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

import javax.swing.JPanel;

import biz.computerkraft.crossword.grid.Cell;
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
	private final List<Cell> indirectSelections = new ArrayList<>();

	/** Cell update listener. */
	private final CellUpdateListener listener;

	/**
	 * Constructor.
	 * 
	 * @param newListener
	 *            new cell update listener
	 */
	public CrosswordPanel(final CellUpdateListener newListener) {
		CrosswordMouseAdapter mouseAdapter = new CrosswordMouseAdapter(this);
		addMouseListener(mouseAdapter);
		listener = newListener;
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
	 * double)
	 */
	@Override
	public final void selectCellAt(final double x, final double y) {
		Point2D point = new Point2D.Double(x, y);
		for (CellRenderer renderer : renderers) {
			Shape shape = renderer.getCellShape();
			if (shape.contains(point)) {
				Rectangle2D bounds = shape.getBounds2D();
				Point2D selectionOffset = new Point2D.Double((x - bounds.getX()) / bounds.getWidth(),
						(y - bounds.getY()) / bounds.getHeight());
				listener.selectCell(renderer.getCell(), selectionOffset);
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
		} else if (indirectSelections.contains(cell)) {
			return Selection.INDIRECT;
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
	 * Sets the indirect selection.
	 * 
	 * @param selection
	 *            cell to show as selected
	 */
	public final void setIndirectSelection(final List<Cell> selection) {
		indirectSelections.clear();
		indirectSelections.addAll(selection);
		repaint();
	}
}
