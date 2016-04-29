package biz.computerkraft.crossword.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.SpringLayout;
import javax.swing.table.TableCellEditor;

import biz.computerkraft.crossword.grid.Symmetry;

/**
 * 
 * Renders a form to allow modifications of puzzle properties.
 * 
 * @author Raymond Francis
 *
 */
public class PropertyDialog extends JDialog {

	/** Serial Id for dialog. */
	private static final long serialVersionUID = -545827596683951250L;

	/** Pixel margin. */
	private static final int MARGIN = 10;

	/** Number of rows. */
	private static final int ROWS = 3;

	/** Number of columns. */
	private static final int COLUMNS = 2;

	/** Puzzle combo box. */
	private JComboBox<Puzzle> puzzles = new JComboBox<>();

	/** Puzzle data model. */
	private PropertiesModel model = new PropertiesModel();

	/**
	 * Show a puzzle property dialog.
	 * 
	 * @param masterFrame
	 *            frame to own dialog
	 */
	public PropertyDialog(final GridDialog masterFrame) {
		super(masterFrame);
		Container container = getContentPane();
		container.setLayout(new SpringLayout());

		// Row 1
		container.add(new JLabel("Type"));
		container.add(puzzles);
		puzzles.setRenderer(new ListCellRenderer<Puzzle>() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * javax.swing.ListCellRenderer#getListCellRendererComponent(javax.
			 * swing.JList, java.lang.Object, int, boolean, boolean)
			 */
			@Override
			public Component getListCellRendererComponent(final JList<? extends Puzzle> list,
					final Puzzle value, final int index, final boolean isSelected,
					final boolean cellHasFocus) {
				return new JLabel(value.getName());
			}
		});
		puzzles.addActionListener(new ActionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent e) {
				Puzzle properties = (Puzzle) puzzles.getSelectedItem();
				model.setProperties(properties);
			}
		});

		// Row 2
		JPanel panel = new JPanel();
		JTable table = new JTable(model) {
			/** Serial id. */
			private static final long serialVersionUID = 1L;

			/*
			 * (non-Javadoc)
			 * 
			 * @see javax.swing.JTable#getCellEditor(int, int)
			 */
			@Override
			public TableCellEditor getCellEditor(final int row, final int column) {
				Class<?> cellClass = model.getValueAt(row, column).getClass();
				if (cellClass.isAssignableFrom(Symmetry.class)) {
					return new DefaultCellEditor(new JComboBox<>(Symmetry.values()));
				} else {
					return super.getCellEditor(row, column);
				}
			}
		};
		panel.add(new JLabel("Properties"), BorderLayout.NORTH);
		container.add(panel);
		JScrollPane scrollPane = new JScrollPane(table);
		container.add(scrollPane);

		// Row 3
		container.add(new JLabel());
		panel = new JPanel();
		container.add(panel);
		JButton button = new JButton("OK");
		panel.add(button);
		button.addActionListener(new ActionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent e) {
				setVisible(false);
				try {
					Puzzle properties = (Puzzle) puzzles.getSelectedItem().getClass().newInstance();
					properties.setProperties(model.getProperties());
					masterFrame.activatePuzzle(properties, PropertyDialog.this);
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}
			}
		});
		button = new JButton("Cancel");
		button.addActionListener(new ActionListener() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent e) {
				setVisible(false);
			}
		});

		SpringUtilities.makeCompactGrid(container, ROWS, COLUMNS, MARGIN, MARGIN, MARGIN, MARGIN);
		pack();
		setModal(true);
	}

	/**
	 * 
	 * Register a puzzle with the puzzle property dialog.
	 * 
	 * @param puzzle
	 *            class of puzzle to be selectable
	 */
	public final void registerPuzzle(final Class<? extends Puzzle> puzzle) {
		try {
			puzzles.addItem(puzzle.newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
