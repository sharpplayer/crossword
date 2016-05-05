package biz.computerkraft.crossword.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import biz.computerkraft.crossword.grid.Clue;
import biz.computerkraft.crossword.gui.input.ComplexCellEditor;

/**
 * 
 * A complex clue editor for tables.
 * 
 * @author Raymond Francis
 *
 */
public class ClueEditor extends JPanel implements ComplexCellEditor {

	/** Serial id. */
	private static final long serialVersionUID = 7758585277362353111L;

	/** Number of controls. */
	private static final int CONTROL_COUNT = 3;

	/** Combo box of clues. */
	private JComboBox<Clue> clueList = new JComboBox<>();

	/** Button clicked for creating a new clue in the database. */
	private JButton newButton;

	/** Button clicked for overwriting an exisiting clue in the database. */
	private JButton saveButton;

	/**
	 * Constructor for the clue editor.
	 */
	public ClueEditor() {
		setLayout(new SpringLayout());
		setRequestFocusEnabled(true);
		setFocusable(true);
		clueList.setEditable(true);
		add(clueList);
		newButton = new JButton("N");
		newButton.addActionListener(new ActionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent e) {
				System.out.println("New:" + clueList.getSelectedItem());
			}
		});

		add(newButton);
		saveButton = new JButton("S");
		saveButton.addActionListener(new ActionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent e) {
				System.out.println("Save" + clueList.getSelectedItem());
			}
		});
		add(saveButton);
		clueList.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.KeyAdapter#keyReleased(java.awt.event.KeyEvent)
			 */
			@Override
			public void keyReleased(final KeyEvent e) {
				getSelectedItem(false);
			}
		});
		clueList.addActionListener(new ActionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent e) {
				getSelectedItem(false);
			}
		});
		SpringUtilities.makeCompactGrid(this, 1, CONTROL_COUNT, 0, 0, 0, 0);
	}

	/**
	 * 
	 * Gets currently selected clue.
	 * 
	 * @param updateClue
	 *            update clue with text specified
	 * @return best selected clue from list
	 */
	public final Clue getSelectedItem(final boolean updateClue) {
		Clue clue = null;
		Object text = clueList.getEditor().getItem();
		if (text instanceof String) {
			String textString = text.toString();
			ComboBoxModel<Clue> model = clueList.getModel();
			for (int index = 0; index < model.getSize(); index++) {
				Clue possibleClue = model.getElementAt(index);
				if (possibleClue.getClueText().equalsIgnoreCase(textString)) {
					clue = possibleClue;
					break;
				}
			}

			if (clue == null) {
				Object selection = clueList.getSelectedItem();
				if (selection instanceof Clue) {
					clue = (Clue) selection;
					if (clue.getClueText().equals(textString)) {
						saveButton.setEnabled(false);
						newButton.setEnabled(false);
					} else {
						saveButton.setEnabled(clue.getClueId() != 0);
						newButton.setEnabled(true);
						if (updateClue) {
							clue.setClueId(0);
							clue.setClueText(textString);
						}
					}
				} else {
					clue = new Clue(0, textString);
					saveButton.setEnabled(true);
					newButton.setEnabled(false);
				}
			} else {
				saveButton.setEnabled(false);
				newButton.setEnabled(clue.getClueId() == 0);
			}
		} else {
			clue = (Clue) text;
			saveButton.setEnabled(false);
			newButton.setEnabled(clue.getClueId() == 0);
		}

		return clue;

	}

	/**
	 * 
	 * Activates clue selection.
	 * 
	 * @param clues
	 *            list of clues to show in combo box
	 * @param clue
	 *            currently selected clue
	 */
	public final void setClue(final List<Clue> clues, final Clue clue) {
		clueList.setModel(new DefaultComboBoxModel<>(clues.toArray(new Clue[0])));
		clueList.setSelectedItem(clue);
		newButton.setEnabled(false);
		saveButton.setEnabled(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.input.ComplexCellEditor#getFirstComponent
	 * ()
	 */
	@Override
	public final Component getFirstComponent() {
		return clueList.getEditor().getEditorComponent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.input.ComplexCellEditor#getLastComponent(
	 * )
	 */
	@Override
	public final Component getLastComponent() {
		if (saveButton.isEnabled()) {
			return saveButton;
		}
		if (newButton.isEnabled()) {
			return newButton;
		}
		return getFirstComponent();
	}
}
