package biz.computerkraft.crossword.gui.input;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTable;

/**
 * 
 * Table cell tab action modifier.
 * 
 * @author Raymond Francis
 *
 */
public class TableCellTabAction extends AbstractAction {

    /** Serial id. */
    private static final long serialVersionUID = -4464455599285900995L;

    /** Original table action. */
    private transient Action originalAction;

    /** Column number of clues. */
    private int column;

    /** Tab forward action. */
    private boolean forward;

    /** key of action. */
    private String key;

    /** Editor component. */
    private transient ComplexCellEditor component;

    /**
     * 
     * Constructor for editor.
     * 
     * @param table
     *            table to apply action on
     * @param newColumn
     *            column to apply action on
     * @param newForward
     *            true if tab forward action
     * @param newComponent
     *            component containing complex editor
     */
    public TableCellTabAction(final JTable table, final int newColumn, final boolean newForward,
            final ComplexCellEditor newComponent) {
        forward = newForward;
        key = "selectNextColumnCell";
        if (!forward) {
            key = "selectPreviousColumnCell";
        }
        originalAction = table.getActionMap()
                .get(key);
        column = newColumn;
        component = newComponent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public final void actionPerformed(final ActionEvent e) {
        JTable table = (JTable) e.getSource();
        if (table.getSelectedColumn() == column && table.isEditing()) {
            Component limitComponent;
            if (forward) {
                limitComponent = component.getLastComponent();
            } else {
                limitComponent = component.getFirstComponent();
            }
            if (KeyboardFocusManager.getCurrentKeyboardFocusManager()
                    .getFocusOwner()
                    .equals(limitComponent)) {
                originalAction.actionPerformed(e);
            } else {
                KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
                if (forward) {
                    manager.focusNextComponent();
                } else {
                    manager.focusPreviousComponent();
                }
            }
        } else {
            originalAction.actionPerformed(e);
        }
    }

    /**
     * 
     * Gets the action key.
     * 
     * @return action key
     */
    public final String getKey() {
        return key;
    }

}
