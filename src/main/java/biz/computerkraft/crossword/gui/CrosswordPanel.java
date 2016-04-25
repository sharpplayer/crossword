package biz.computerkraft.crossword.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.SystemColor;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/**
 * 
 * Crossword panel.
 * 
 * @author Raymond Francis
 *
 */
public class CrosswordPanel extends JPanel {

	/** Serial id. */
	private static final long serialVersionUID = 6617629272499072181L;

	/** List of cell renderers. */
	private List<CellRenderer> renderers = new ArrayList<>();

	/** Preferred size with no zoom. */
	private Dimension preferredSize;

	/** Zoom percentage. */
	private double zoomPc = 1;

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
			renderer.renderCell(graphics2d, size.getWidth(), size.getHeight());
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
}
