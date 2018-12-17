package biz.computerkraft.crossword.pdf;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.grid.crossword.AbstractCrossword;

/**
 * 
 * Creates a crossword PDF.
 * 
 * @author Raymond Francis
 *
 */
public class BasicPdf {

	/** Default cell size. */
	private static final int DEFAULT_CELL_SIZE = 60;

	/** Title font size. */
	private static final float TITLE_FONT_SIZE = 26;

	/** Title X. */
	private static final float TITLE_X = 0;

	/** Title Y. */
	private static final float TITLE_Y = 10;

	/** Marker font size. */
	private static final float MARKER_FONT_SIZE = 12;

	/** Cell marker margin. */
	private static final float MARKER_MARGIN = 5;

	/** Marker font size. */
	private static final float CONTENT_FONT_SIZE = 12;

	/** Cell content margin. */
	private static final float CONTENT_MARGIN = 15;

	/** Document. */
	private PDDocument document = new PDDocument();

	/** Content of PDF. */
	private PDPageContentStream content;

	/** File to save to. */
	private File file;

	/** Page dimensions. */
	private PDRectangle pageDimensions;

	/**
	 * 
	 * Creates a PDF document.
	 * 
	 * @param newFile
	 *            file to save to
	 * @param title
	 *            title of crossword
	 * @throws IOException
	 *             exception thrown creating PDF
	 */
	public BasicPdf(final File newFile, final String title) throws IOException {
		file = newFile;
		PDPage page = new PDPage(PDRectangle.A4);
		document.addPage(page);
		content = new PDPageContentStream(document, page, AppendMode.OVERWRITE, true, true);
		pageDimensions = page.getTrimBox();
		PDRectangle x = page.getMediaBox();
		writeText(title, PDType1Font.HELVETICA, TITLE_FONT_SIZE, TITLE_X, TITLE_Y);
	}

	/**
	 * Writes PDF text at a given location.
	 * 
	 * @param text
	 *            text
	 * @param font
	 *            font
	 * @param size
	 *            size
	 * @param x
	 *            x
	 * @param y
	 *            y
	 * @throws IOException
	 *             thrown on error
	 */
	private void writeText(final String text, final PDFont font, final float size, final float x, final float y)
			throws IOException {
		content.beginText();
		content.setFont(font, size);
		content.newLineAtOffset(getX(x), getY(y));
		content.showText(text);
		content.endText();

	}

	private float getX(float x) {
		return pageDimensions.getLowerLeftX() + x;
	}

	private float getY(float y) {
		return pageDimensions.getUpperRightY() - y;
	}

	/**
	 * 
	 * Renders a cell in the PDF.
	 * 
	 * @param width
	 *            number of cells width of grid
	 * @param height
	 *            number of cells height of grid
	 * @param cell
	 *            cell to render to PDF
	 * @param cellContent
	 *            content to render
	 * @param fill
	 *            should it be black filled
	 * @throws IOException
	 *             thrown of PDF generation failure
	 */
	public final void renderRectangleCell(final int width, final int height, final Cell cell, final String cellContent,
			final boolean fill) throws IOException {
		Rectangle cellRectangle = cell.getRectangleGridShape(width * DEFAULT_CELL_SIZE, height * DEFAULT_CELL_SIZE, 0,
				false);
		int newFill = cell.getFill(fill);
		boolean fullFill = newFill == (AbstractCrossword.DIRECTION_E | AbstractCrossword.DIRECTION_N
				| AbstractCrossword.DIRECTION_W | AbstractCrossword.DIRECTION_S);

		content.setStrokingColor(Color.BLACK);
		if (fullFill) {
			content.setNonStrokingColor(Color.BLACK);
		} else {
			content.setNonStrokingColor(Color.WHITE);
		}
		content.addRect((float) cellRectangle.getMinX(), (float) cellRectangle.getMinY(),
				(float) cellRectangle.getWidth(), (float) cellRectangle.getHeight());
		if (fullFill) {
			content.fill();
		}
		content.beginText();
		content.setFont(PDType1Font.HELVETICA, MARKER_FONT_SIZE);
		content.newLineAtOffset((float) cellRectangle.getX() + MARKER_MARGIN,
				(float) cellRectangle.getY() + MARKER_MARGIN);
		content.showText(cell.getMarker());
		content.endText();

		if (!cellContent.isEmpty()) {
			content.beginText();
			content.setFont(PDType1Font.HELVETICA_BOLD, CONTENT_FONT_SIZE);
			content.newLineAtOffset((float) cellRectangle.getX() + CONTENT_MARGIN,
					(float) cellRectangle.getY() + CONTENT_MARGIN);
			content.showText(cellContent);
			content.endText();
		}
	}

	/**
	 * 
	 * Exports pdf to file.
	 * 
	 * @throws IOException
	 *             thrown on write exception.
	 */
	public final void export() throws IOException {
		content.close();
		document.save(file);
		document.close();
	}
}
