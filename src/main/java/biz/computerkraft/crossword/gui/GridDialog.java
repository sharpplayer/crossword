package biz.computerkraft.crossword.gui;

import java.awt.Container;
import java.awt.Font;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SpringLayout;

import biz.computerkraft.crossword.db.DBConnection;
import biz.computerkraft.crossword.db.Word;

/**
 * 
 * Main puzzle renderer.
 * 
 * @author Raymond Francis
 *
 */
public class GridDialog extends JDialog {

	/** Serial id. */
	private static final long serialVersionUID = -1887552377378707619L;

	/** Properties dialog. */
	private PropertyDialog propertyDialog;

	/** Database connection. */
	private DBConnection connection = new DBConnection();

	/** Panel for crossword. */
	private JPanel crosswordGrid = new JPanel();

	/** Word list model. */
	private WordListModel wordlListModel = new WordListModel();

	public GridDialog() {
		super((JDialog) null, "Crossword");
		Container pane = getContentPane();
		pane.setLayout(new SpringLayout());
		pane.add(crosswordGrid);

		JPanel wordGrid = new JPanel(new SpringLayout());
		pane.add(wordGrid);
		JList<Word> wordList = new JList<>(wordlListModel);
		wordGrid.add(new JScrollPane(wordList));
		wordList.setFont(new Font("Courier New", Font.BOLD, 40));
		wordlListModel.setWordList(connection.getWords("A   ", 1));
		SpringUtilities.makeCompactGrid(wordGrid, 1, 1, 10, 10, 10, 10);
	}

	public void activatePuzzle(PuzzleProperties puzzle, PropertyDialog properties) {
		propertyDialog = properties;
		SpringUtilities.makeCompactGrid(getContentPane(), 1, 2, 10, 10, 10, 10);
		pack();
		setModal(true);
		setVisible(true);
	}
}
