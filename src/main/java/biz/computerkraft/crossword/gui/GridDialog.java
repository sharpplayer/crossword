package biz.computerkraft.crossword.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import biz.computerkraft.crossword.db.DBConnection;
import biz.computerkraft.crossword.db.Word;
import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.grid.Symmetry;
import biz.computerkraft.crossword.gui.input.TableMouseAdapter;
import biz.computerkraft.crossword.gui.input.WordListKeyListener;
import biz.computerkraft.crossword.gui.input.WordListMouseAdapter;

/**
 * 
 * Main puzzle renderer.
 * 
 * @author Raymond Francis
 *
 */
public class GridDialog extends JFrame implements CellUpdateListener, CellSelectListener {

	/** Serial id. */
	private static final long serialVersionUID = -1887552377378707619L;

	/** Margin around controls. */
	private static final int MARGIN = 10;

	/** Default cell size on screen. */
	private static final int DEFAULT_CELL_SIZE = 40;

	/** Clue section width. */
	private static final int WIDTH_CLUE = 1000;

	/** Default scrollpane border. */
	private static final int SCROLLPANE_BORDER = 3;

	/** Cell centre. */
	private static final double CELL_CENTRE = 0.5;

	/** Properties dialog. */
	private PropertyDialog propertyDialog;

	/** Database connection. */
	private DBConnection connection = new DBConnection();

	/** Panel for crossword. */
	private CrosswordPanel crosswordGrid = new CrosswordPanel(this, this);

	/** Word list model. */
	private WordListModel wordlListModel = new WordListModel(connection);

	/** Crossword viewer. */
	private JScrollPane crosswordViewer;

	/** Puzzle. */
	private Puzzle puzzle;

	/** Last offset for getting direction of indirect selections. */
	private Point2D lastOffset = new Point2D.Double(CELL_CENTRE, 0.0);

	/** Tab control. */
	private JTabbedPane wordTabs = new JTabbedPane();

	/** Clue models. */
	private List<ClueModel> clueModels = new ArrayList<>();

	/** Dirty flag. */
	private boolean dirty = false;

	/** Save file. */
	private File saveFile = null;

	/**
	 * Constructor.
	 */
	public GridDialog() {
		super("Crossword");
		JMenuBar menu = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		menu.add(fileMenu);

		JMenuItem newMenu = new JMenuItem("New...");
		fileMenu.add(newMenu);
		newMenu.addActionListener(new ActionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent e) {
				if (dirtyCheck()) {
					propertyDialog.setVisible(true);
				}
			}
		});

		JMenuItem open = new JMenuItem("Open...");
		fileMenu.add(open);
		open.addActionListener(new ActionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent e) {
				if (dirtyCheck()) {
					propertyDialog.openFile(GridDialog.this, GridDialog.this);
				}
			}
		});

		JMenuItem save = new JMenuItem("Save");
		fileMenu.add(save);
		save.addActionListener(new ActionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent e) {
				saveFile();
			}
		});

		fileMenu.addSeparator();

		JMenuItem properties = new JMenuItem("Properties...");
		fileMenu.add(properties);
		properties.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				propertyDialog.setVisible(true);
			}
		});

		setJMenuBar(menu);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.
			 * WindowEvent)
			 */
			@Override
			public void windowClosing(final WindowEvent e) {
				if (dirtyCheck()) {
					System.exit(0);
				}
			}
		});

		Container pane = getContentPane();
		pane.setLayout(new SpringLayout());
		crosswordViewer = new JScrollPane(crosswordGrid);
		pane.add(crosswordViewer);

		pane.add(wordTabs);
		JList<Word> wordList = new JList<>(wordlListModel);
		wordList.addMouseListener(new WordListMouseAdapter(this));
		wordList.addKeyListener(new WordListKeyListener(this));
		wordList.setFont(new Font("Courier New", Font.BOLD, wordList.getFont().getSize()));
		wordTabs.addTab("Words", new JScrollPane(wordList));

	}

	/**
	 * Activates puzzle.
	 * 
	 * @param newPuzzle
	 *            puzzle to set up
	 * @param properties
	 *            puzzle properties
	 * @param file
	 *            file loaded from
	 */
	public final void activatePuzzle(final Puzzle newPuzzle, final PropertyDialog properties, final File file) {
		propertyDialog = properties;
		puzzle = newPuzzle;
		saveFile = file;
		dirty = false;
		int height = puzzle.getCellHeight() * DEFAULT_CELL_SIZE;
		crosswordGrid.reset(new Dimension(puzzle.getCellWidth() * DEFAULT_CELL_SIZE, height));
		crosswordViewer.setPreferredSize(new Dimension(puzzle.getCellWidth() * DEFAULT_CELL_SIZE + SCROLLPANE_BORDER,
				puzzle.getCellHeight() * DEFAULT_CELL_SIZE + SCROLLPANE_BORDER));

		for (Cell cell : puzzle.getCells()) {
			CellRenderer renderer = puzzle.getNewCellRenderer();
			renderer.setCell(cell);
			crosswordGrid.addRenderer(renderer);
		}

		for (ClueModel model : clueModels) {
			wordTabs.remove(wordTabs.indexOfComponent(model.getVisualComponent(connection, this)));
		}
		clueModels.clear();

		List<ClueModel> clues = puzzle.getClueModels();

		for (ClueModel model : clues) {
			Component component = model.getVisualComponent(connection, this);
			component.setPreferredSize(new Dimension(WIDTH_CLUE, height));
			if (component instanceof JScrollPane) {
				Component view = ((JScrollPane) component).getViewport().getView();
				if (view instanceof JTable) {
					((JTable) view).addMouseListener(new TableMouseAdapter(this));
				}
			}
			wordTabs.addTab(model.getCategory(), component);
		}
		clueModels.addAll(clues);

		SpringUtilities.makeCompactGrid(getContentPane(), 1, 2, MARGIN, MARGIN, MARGIN, MARGIN);
		pack();
		setVisible(true);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.CellUpdateListener#selectCell(biz.
	 * computerkraft.crossword.grid.Cell, java.awt.geom.Point2D)
	 */
	@Override
	public final void selectCell(final Cell cell, final Point2D offset) {
		lastOffset = offset;
		crosswordGrid.setDirectSelection(cell);
		List<Cell> indirectSelection = puzzle.getIndirectSelection(cell, offset);
		crosswordGrid.setIndirectSelection(indirectSelection);
		updateWordList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.CellUpdateListener#addCellContent(biz.
	 * computerkraft.crossword.grid.Cell, java.lang.String)
	 */
	@Override
	public final void addCellContent(final Cell cell, final String content) {
		puzzle.addCellContent(cell, content);
		dirty = true;
		updateWordList();
	}

	/**
	 * Updates word list.
	 */
	private void updateWordList() {
		String word = "";
		for (Cell indirectCell : crosswordGrid.getFirstIndirectSelection()) {
			if (indirectCell.isEmpty()) {
				word += " ";
			} else {
				word += indirectCell.getDisplayContents();
			}
		}
		wordlListModel.setWordList(word);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.CellUpdateListener#clearCellContent(biz.
	 * computerkraft.crossword.grid.Cell)
	 */
	@Override
	public final void clearCellContent(final Cell cell) {
		puzzle.clearCellContent(cell);
		dirty = true;
		updateWordList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.CellUpdateListener#selectCellLeft(biz.
	 * computerkraft.crossword.grid.Cell)
	 */
	@Override
	public final void selectCellLeft(final Cell cell) {
		Cell newCell = puzzle.getCellLeft(cell);
		selectCell(newCell, lastOffset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.CellUpdateListener#selectCellUp(biz.
	 * computerkraft.crossword.grid.Cell)
	 */
	@Override
	public final void selectCellUp(final Cell cell) {
		Cell newCell = puzzle.getCellUp(cell);
		selectCell(newCell, lastOffset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.CellUpdateListener#selectCellRight(biz.
	 * computerkraft.crossword.grid.Cell)
	 */
	@Override
	public final void selectCellRight(final Cell cell) {
		Cell newCell = puzzle.getCellRight(cell);
		selectCell(newCell, lastOffset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.CellUpdateListener#selectCellDown(biz.
	 * computerkraft.crossword.grid.Cell)
	 */
	@Override
	public final void selectCellDown(final Cell cell) {
		Cell newCell = puzzle.getCellDown(cell);
		selectCell(newCell, lastOffset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.CellUpdateListener#cellMenuAction(biz.
	 * computerkraft.crossword.grid.Cell, java.lang.String)
	 */
	@Override
	public final void cellMenuAction(final Cell cell, final String action) {
		dirty |= puzzle.cellMenuAction(cell, action);
		selectCell(cell, lastOffset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.CellUpdateListener#populateCellMenu(biz.
	 * computerkraft.crossword.grid.Cell, java.util.List)
	 */
	@Override
	public final void populateCellMenu(final Cell cell, final List<String> actions) {
		puzzle.populateCellMenu(cell, actions);
	}

	/**
	 * 
	 * Checks for dirty crossword.
	 * 
	 * @return true if proceed with action.
	 */
	private boolean dirtyCheck() {
		if (dirty) {
			int confirm = JOptionPane.showConfirmDialog(this, "Do you wish to save changes?");
			if (confirm == JOptionPane.YES_OPTION) {
				return saveFile();
			} else if (confirm == JOptionPane.CANCEL_OPTION) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Function to save crossword.
	 * 
	 * @return true if successful
	 */
	private boolean saveFile() {
		try {
			JFileChooser chooser = new JFileChooser();
			if (saveFile == null && chooser.showSaveDialog(GridDialog.this) == JFileChooser.APPROVE_OPTION) {
				saveFile = chooser.getSelectedFile();
			}
			if (saveFile != null) {
				JAXBContext context = JAXBContext.newInstance(puzzle.getClass(), Symmetry.class);
				Marshaller marshaller = context.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

				marshaller.marshal(puzzle, saveFile);
				dirty = false;
				return true;
			}
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.CellUpdateListener#setWord(biz.
	 * computerkraft.crossword.db.Word)
	 */
	@Override
	public final void setWord(final Word word) {
		puzzle.addWordContent(crosswordGrid.getFirstIndirectSelection(), word);
		updateWordList();
		crosswordGrid.repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.CellUpdateListener#increaseSortLetter()
	 */
	@Override
	public final void increaseSortLetter() {
		wordlListModel.increaseSortLetter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.CellUpdateListener#decreaseSortLetter()
	 */
	@Override
	public final void decreaseSortLetter() {
		wordlListModel.decreaseSortLetter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.CellUpdateListener#selectCell(biz.
	 * computerkraft.crossword.grid.Cell, int)
	 */
	@Override
	public final void selectCell(final Cell cell, final int direction) {
		lastOffset = puzzle.getPointFromDirection(direction);
		selectCell(cell, lastOffset);

	}
}
