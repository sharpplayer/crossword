package biz.computerkraft.crossword.gui.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.grid.crossword.AbstractCrossword;
import biz.computerkraft.crossword.gui.CellRenderer;
import biz.computerkraft.crossword.gui.Selection;

/**
 * 
 * Abstract cell renderer.
 * 
 * @author Raymond Francis
 *
 */
public abstract class AbstractCellRenderer implements CellRenderer {

    /** Cell to render. */
    private Cell cell;

    /** Shape rendered. */
    private Shape cellShape;

    /** Default font size. */
    private static final int DEFAULT_FONT_SIZE = 40;

    /** Default selected filled cell border. */
    private static final int CELL_BORDER = 6;

    /** Default selected filled cell border. */
    private static final int BAR_BORDER = 4;

    /** Font for crossword. */
    protected static final Font DEFAULT_FONT = new Font("Arial", Font.BOLD, DEFAULT_FONT_SIZE);

    /*
     * (non-Javadoc)
     * 
     * @see biz.computerkraft.crossword.gui.CellRenderer#setCell(biz.computerkraft. crossword.grid.Cell)
     */
    @Override
    public final void setCell(final Cell cellToRender) {
        cell = cellToRender;
    }

    /*
     * (non-Javadoc)
     * 
     * @see biz.computerkraft.crossword.gui.CellRenderer#getCellShape()
     */
    @Override
    public final Shape getCellShape() {
        return cellShape;
    }

    /*
     * (non-Javadoc)
     * 
     * @see biz.computerkraft.crossword.gui.CellRenderer#getCell()
     */
    @Override
    public final Cell getCell() {
        return cell;
    }

    /**
     * 
     * Base render of cell.
     * 
     * @param graphics
     *            graphics context
     * @param width
     *            width of grid
     * @param height
     *            height of grid
     * @param selection
     *            method of selection
     * @param fill
     *            fill sides
     */
    protected final void baseRenderCell(final Graphics2D graphics, final double width, final double height,
            final Selection selection, final int fill) {

        cellShape = cell.getRectangleGridShape(width, height, 0, false);

        boolean fullFill = renderBackground(graphics, selection, fill);

        graphics.fill(cellShape);

        Shape borderShape = renderBorder(graphics, width, height, selection, fullFill);

        graphics.draw(borderShape);

        renderForeground(graphics, width, height, fill, fullFill);

        String contents = cell.getDisplayContents();
        if (!contents.isEmpty()) {
            Rectangle2D bounds = cellShape.getBounds2D();
            graphics.setColor(Color.BLACK);
            graphics.setFont(DEFAULT_FONT);
            FontMetrics metrics = graphics.getFontMetrics(DEFAULT_FONT);
            double fontX = bounds.getX() + (bounds.getWidth() - metrics.stringWidth(contents)) / 2;
            double fontY = bounds.getY() + ((bounds.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
            graphics.drawString(contents, (int) fontX, (int) fontY);
        }

    }

    private void renderForeground(final Graphics2D graphics, final double width, final double height, final int fill,
            boolean fullFill) {
        if (!fullFill) {
            Rectangle cellRect = cell.getRectangleGridShape(width, height, BAR_BORDER / 2.0, true);

            graphics.setStroke(new BasicStroke(BAR_BORDER / 2));

            if ((fill & AbstractCrossword.DIRECTION_E) != 0) {
                graphics.drawLine((int) cellRect.getMaxX(), (int) cellRect.getMinY(), (int) cellRect.getMaxX(),
                        (int) cellRect.getMaxY());
            }
            if ((fill & AbstractCrossword.DIRECTION_W) != 0) {
                graphics.drawLine((int) cellRect.getMinX(), (int) cellRect.getMinY(), (int) cellRect.getMinX(),
                        (int) cellRect.getMaxY());
            }
            if ((fill & AbstractCrossword.DIRECTION_N) != 0) {
                graphics.drawLine((int) cellRect.getMinX(), (int) cellRect.getMaxY(), (int) cellRect.getMaxX(),
                        (int) cellRect.getMaxY());
            }
            if ((fill & AbstractCrossword.DIRECTION_S) != 0) {
                graphics.drawLine((int) cellRect.getMinX(), (int) cellRect.getMinY(), (int) cellRect.getMaxX(),
                        (int) cellRect.getMinY());
            }
        }
    }

    private boolean renderBackground(final Graphics2D graphics, final Selection selection, final int fill) {
        boolean fullFill = fill == (AbstractCrossword.DIRECTION_E | AbstractCrossword.DIRECTION_N
                | AbstractCrossword.DIRECTION_W | AbstractCrossword.DIRECTION_S);
        boolean transientContent = !cell.getTransientContents()
                .isEmpty();
        if (fullFill) {
            if (transientContent) {
                graphics.setColor(Color.DARK_GRAY);
            } else {
                graphics.setColor(Color.BLACK);
            }
        } else if (selection == Selection.NONE) {
            if (transientContent) {
                graphics.setColor(Color.LIGHT_GRAY);
            } else {
                graphics.setColor(Color.WHITE);
            }
        } else if (selection == Selection.DIRECT) {
            if (transientContent) {
                graphics.setColor(Color.ORANGE.darker());
            } else {
                graphics.setColor(Color.ORANGE);
            }
        } else if (selection == Selection.INDIRECT) {
            if (transientContent) {
                graphics.setColor(Color.YELLOW.darker());
            } else {
                graphics.setColor(Color.YELLOW);
            }
        } else if (selection == Selection.ERROR) {
            graphics.setColor(Color.RED);
        }
        return fullFill;
    }

    private Shape renderBorder(final Graphics2D graphics, final double width, final double height,
            final Selection selection, boolean fullFill) {
        Shape borderShape = cellShape;
        if (!fullFill) {
            graphics.setStroke(new BasicStroke(1));
            graphics.setColor(Color.BLACK);
        } else {
            borderShape = cell.getRectangleGridShape(width, height, CELL_BORDER / 2.0, false);
            graphics.setStroke(new BasicStroke(CELL_BORDER));
            if (selection == Selection.NONE) {
                graphics.setColor(Color.BLACK);
            } else if (selection == Selection.DIRECT) {
                graphics.setColor(Color.ORANGE);
            } else if (selection == Selection.INDIRECT) {
                graphics.setColor(Color.YELLOW);
            }
        }
        return borderShape;
    }

}