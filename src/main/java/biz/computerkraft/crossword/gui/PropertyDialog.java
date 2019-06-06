package biz.computerkraft.crossword.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.table.TableCellEditor;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import biz.computerkraft.crossword.grid.crossword.enumeration.Encoding;
import biz.computerkraft.crossword.grid.crossword.enumeration.Symmetry;

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

    /** Logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyDialog.class);

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
        puzzles.setRenderer((list, value, index, isSelected, cellHasFocus) -> new JLabel(value.getName()));
        puzzles.addActionListener(e -> {
            Puzzle properties = (Puzzle) puzzles.getSelectedItem();
            model.setProperties(properties);
        });

        // Row 2
        JPanel panel = new JPanel();
        JTable table = new JHighDPITable(model) {
            /** Serial id. */
            private static final long serialVersionUID = 1L;

            /*
             * (non-Javadoc)
             * 
             * @see javax.swing.JTable#getCellEditor(int, int)
             */
            @Override
            public TableCellEditor getCellEditor(final int row, final int column) {
                Class<?> cellClass = model.getValueAt(row, column)
                        .getClass();
                if (cellClass.isAssignableFrom(Symmetry.class)) {
                    return new DefaultCellEditor(new JComboBox<>(Symmetry.values()));
                } else if (cellClass.isAssignableFrom(Encoding.class)) {
                    return new DefaultCellEditor(new JComboBox<>(Encoding.values()));
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
        panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        container.add(panel);
        JButton button = new JButton("Open...");
        panel.add(button);
        button.addActionListener(e -> {
            if (openFile(PropertyDialog.this, masterFrame)) {
                setVisible(false);
            }
        });

        button = new JButton("OK");
        panel.add(button);
        button.addActionListener(e -> {
            setVisible(false);
            try {
                Puzzle properties = (Puzzle) puzzles.getSelectedItem()
                        .getClass()
                        .getDeclaredConstructor()
                        .newInstance();
                properties.setProperties(model.getProperties());
                masterFrame.activatePuzzle(properties, PropertyDialog.this, null);
            } catch (Exception e1) {
                LOGGER.error("Failed to create puzzle object", e1);
            }
        });
        button = new JButton("Cancel");
        button.addActionListener(e -> {
            setVisible(false);
            if (!masterFrame.isVisible()) {
                System.exit(0);
            }
        });
        panel.add(button);

        addWindowListener(new WindowAdapter() {
            /*
             * (non-Javadoc)
             * 
             * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event. WindowEvent)
             */
            @Override
            public void windowClosing(final WindowEvent e) {
                if (!masterFrame.isVisible()) {
                    System.exit(0);
                }
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
            puzzles.addItem(puzzle.getDeclaredConstructor()
                    .newInstance());
        } catch (Exception e) {
            LOGGER.error("Failed to register puzzle", e);
        }
    }

    /**
     * 
     * Opens a puzzle file.
     * 
     * @param parent
     *            parent control to own dialog
     * @param frame
     *            frame to activate puzzle in
     * @return true if opened file
     */
    public final boolean openFile(final Component parent, final GridDialog frame) {
        JFileChooser chooser = new JFileChooser();
        Puzzle puzzle = null;
        if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            List<Class<?>> puzzleClass = new ArrayList<>();
            puzzleClass.add(Symmetry.class);
            for (int index = 0; index < puzzles.getItemCount(); index++) {
                Puzzle puzzleItem = puzzles.getItemAt(index);
                puzzleClass.add(puzzleItem.getClass());
            }

            try {
                JAXBContext context = JAXBContext.newInstance(puzzleClass.toArray(new Class<?>[0]));
                Unmarshaller unmarshaller = context.createUnmarshaller();
                unmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
                puzzle = (Puzzle) unmarshaller.unmarshal(chooser.getSelectedFile());
                puzzle.postLoadTidyup();
                frame.activatePuzzle(puzzle, this, chooser.getSelectedFile());
                return true;
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
