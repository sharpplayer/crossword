package biz.computerkraft.crossword.gui;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

/**
 * 
 * Model to display puzzle properties.
 * 
 * @author Raymond Francis
 *
 */
public class PropertiesModel extends AbstractTableModel {

    /** Serial id. */
    private static final long serialVersionUID = -7950556697546366686L;

    /** Actual properties to handle. */
    private transient List<Entry<String, Object>> propertyMap = null;

    /**
     * 
     * Sets the properties to edit.
     * 
     * @param properties
     *            new properties to display.
     */
    public final void setProperties(final Puzzle properties) {
        Set<Entry<String, Object>> entrySet = properties.getProperties()
                .entrySet();
        propertyMap = new ArrayList<>(entrySet);
        Collections.sort(propertyMap, (o1, o2) -> o1.getKey()
                .compareTo(o2.getKey()));
        fireTableDataChanged();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public final int getRowCount() {
        if (propertyMap == null) {
            return 0;
        } else {
            return propertyMap.size();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public final int getColumnCount() {
        return 2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public final Object getValueAt(final int rowIndex, final int columnIndex) {
        if (propertyMap == null) {
            return null;
        } else {
            if (columnIndex == 1) {
                return propertyMap.get(rowIndex)
                        .getValue();
            } else {
                return propertyMap.get(rowIndex)
                        .getKey();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    @Override
    public final Class<?> getColumnClass(final int columnIndex) {
        if (columnIndex == 1) {
            return Integer.class;
        } else {
            return String.class;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public final String getColumnName(final int column) {
        if (column == 1) {
            return "Value";
        } else {
            return "Name";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    @Override
    public final boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return (columnIndex == 1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
     */
    @Override
    public final void setValueAt(final Object value, final int rowIndex, final int columnIndex) {
        String key = propertyMap.get(rowIndex)
                .getKey();
        Entry<String, Object> newEntry = new AbstractMap.SimpleEntry<>(key, value);
        propertyMap.remove(rowIndex);
        propertyMap.add(rowIndex, newEntry);
    }

    /**
     * 
     * Gets a new property map.
     * 
     * @return new properties map
     */
    public final Map<String, Object> getProperties() {
        Map<String, Object> newProperties = new HashMap<>();
        for (Entry<String, Object> entry : propertyMap) {
            newProperties.put(entry.getKey(), entry.getValue());
        }
        return newProperties;
    }
}
