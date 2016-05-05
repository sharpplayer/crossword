package biz.computerkraft.crossword.gui.input;

import java.awt.Component;

/**
 * Interface to indicate a cell editor control has multiple inputs.
 * 
 * @author Raymond Francis
 *
 */
public interface ComplexCellEditor {

	/**
	 * 
	 * Gets first active component in editor.
	 * 
	 * @return first component in editor
	 */
	Component getFirstComponent();

	/**
	 * 
	 * Gets last active component in editor.
	 * 
	 * @return last component in editor
	 */
	Component getLastComponent();

}
