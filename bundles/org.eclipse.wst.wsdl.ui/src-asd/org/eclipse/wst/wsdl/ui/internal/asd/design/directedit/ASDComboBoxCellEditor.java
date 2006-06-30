/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd.design.directedit;

import java.text.MessageFormat;

import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.xsd.ui.internal.adt.edit.ComponentReferenceEditManager;
import org.eclipse.wst.xsd.ui.internal.adt.edit.IComponentDialog;

/*
 * This is a copy of ComboBoxCellEditor.
 * We need to apply and deactivate the combo on a single click (not on a double click like
 * the ComboBoxCellEditor).
 */
public class ASDComboBoxCellEditor extends CellEditor {

    /**
     * The list of items to present in the combo box.
     */
    private String[] items;

    /**
     * The zero-based index of the selected item.
     */
    int selection;

    /**
     * The custom combo box control.
     */
    ASDCCombo comboBox;
    
    /**
     * Used to determine if the value should be applied to the cell.
     */
    private boolean continueApply;    
    private Object selectedValue;
    private ComponentReferenceEditManager componentReferenceEditManager;
    private int textIndent = 5;

    /**
     * Default ComboBoxCellEditor style
     */
    private static final int defaultStyle = SWT.NONE;

    /**
     * Creates a new cell editor with a combo containing the given 
     * list of choices and parented under the given control. The cell
     * editor value is the zero-based index of the selected item.
     * Initially, the cell editor has no cell validator and
     * the first item in the list is selected. 
     *
     * @param parent the parent control
     * @param items the list of strings for the combo box
     */
    public ASDComboBoxCellEditor(Composite parent, String[] items, ComponentReferenceEditManager editManager) {
        super(parent, defaultStyle);
        setItems(items);
        componentReferenceEditManager = editManager;
    }

    /**
     * Returns the list of choices for the combo box
     *
     * @return the list of choices for the combo box
     */
    public String[] getItems() {
        return this.items;
    }

    /**
     * Sets the list of choices for the combo box
     *
     * @param items the list of choices for the combo box
     */
    public void setItems(String[] items) {
        Assert.isNotNull(items);
        this.items = items;
        populateComboBoxItems();
    }
    
    public void setTextIndent(int indent) {
    	textIndent = indent;
    	comboBox.setTextIndent(indent);
    }

    /* (non-Javadoc)
     * Method declared on CellEditor.
     */
    protected Control createControl(Composite parent) {
        comboBox = new ASDCCombo(parent, getStyle());
        comboBox.setFont(parent.getFont());
        comboBox.setTextIndent(textIndent);
        
        comboBox.addKeyListener(new KeyAdapter() {
            // hook key pressed - see PR 14201  
            public void keyPressed(KeyEvent e) {
                keyReleaseOccured(e);
            }
        });

        comboBox.addSelectionListener(new SelectionAdapter() {
            public void widgetDefaultSelected(SelectionEvent event) {
                applyEditorValueAndDeactivate();
            }

            public void widgetSelected(SelectionEvent event) {
            	Object newValue = null;
            	continueApply = true;
            	selection = comboBox.getSelectionIndex();
            	String stringSelection = items[selection];
            	
            	if (stringSelection.equals(Messages.getString("_UI_BUTTON_BROWSE"))) { //$NON-NLS-1$
            		newValue = invokeDialog(componentReferenceEditManager.getBrowseDialog());
            	}
            	else if (stringSelection.equals(Messages.getString("_UI_BUTTON_NEW"))) { //$NON-NLS-1$
            		newValue = invokeDialog(componentReferenceEditManager.getNewDialog());
            	}

            	if (continueApply) {
            		if (newValue == null) {
            			int index = comboBox.getSelectionIndex();              
            			if (index != -1) {
            				selectedValue = comboBox.getItem(index);
            			}
            		}
            		else {
            			selectedValue = newValue;
            		}

            		applyEditorValueAndDeactivate();
            	}
            }
        });

        comboBox.addTraverseListener(new TraverseListener() {
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_ESCAPE
                        || e.detail == SWT.TRAVERSE_RETURN) {
                    e.doit = false;
                }
            }
        });

        comboBox.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                ASDComboBoxCellEditor.this.focusLost();
            }
        });
        return comboBox;
    }
    
    private Object invokeDialog(IComponentDialog dialog) {
  	  Object newValue = null;
  	  
  	  if (dialog == null) {
  		  return null;
  	  }
  	  
  	  if (dialog.createAndOpen() == Window.OK) {
  		  newValue = dialog.getSelectedComponent();
  	  }
  	  else {
  		  continueApply = false;
  	  }

  	  return newValue;
    }  

    /**
     * The <code>ComboBoxCellEditor</code> implementation of
     * this <code>CellEditor</code> framework method returns
     * the zero-based index of the current selection.
     *
     * @return the zero-based index of the current selection wrapped
     *  as an <code>Integer</code>
     */
    protected Object doGetValue() {
        return new Integer(selection);
    }

    /* (non-Javadoc)
     * Method declared on CellEditor.
     */
    protected void doSetFocus() {
        comboBox.setFocus();
//        String comboText = comboBox.getText();
//        comboBox.setText("         " + comboText);
    }

    /**
     * The <code>ComboBoxCellEditor</code> implementation of
     * this <code>CellEditor</code> framework method sets the 
     * minimum width of the cell.  The minimum width is 10 characters
     * if <code>comboBox</code> is not <code>null</code> or <code>disposed</code>
     * eles it is 60 pixels to make sure the arrow button and some text is visible.
     * The list of CCombo will be wide enough to show its longest item.
     */
    public LayoutData getLayoutData() {
        LayoutData layoutData = super.getLayoutData();
        if ((comboBox == null) || comboBox.isDisposed())
            layoutData.minimumWidth = 60;
        else {
            // make the comboBox 10 characters wide
            GC gc = new GC(comboBox);
            layoutData.minimumWidth = (gc.getFontMetrics()
                    .getAverageCharWidth() * 10) + 10;
            gc.dispose();
        }
        return layoutData;
    }

    /**
     * The <code>ComboBoxCellEditor</code> implementation of
     * this <code>CellEditor</code> framework method
     * accepts a zero-based index of a selection.
     *
     * @param value the zero-based index of the selection wrapped
     *   as an <code>Integer</code>
     */
    protected void doSetValue(Object value) {
        Assert.isTrue(comboBox != null && (value instanceof Integer));
        selection = ((Integer) value).intValue();
        comboBox.select(selection);
    }

    /**
     * Updates the list of choices for the combo box for the current control.
     */
    private void populateComboBoxItems() {
        if (comboBox != null && items != null) {
            comboBox.removeAll();
            for (int i = 0; i < items.length; i++)
                comboBox.add(items[i], i);

            setValueValid(true);
            selection = 0;
        }
    }

    /**
     * Applies the currently selected value and deactiavates the cell editor
     */
    void applyEditorValueAndDeactivate() {
        //	must set the selection before getting value
        selection = comboBox.getSelectionIndex();
        Object newValue = doGetValue();
        markDirty();
        boolean isValid = isCorrect(newValue);
        setValueValid(isValid);
        if (!isValid) {
            // try to insert the current value into the error message.
            setErrorMessage(MessageFormat.format(getErrorMessage(),
                    new Object[] { items[selection] }));
        }
        fireApplyEditorValue();
        deactivate();
    }

    /*
     *  (non-Javadoc)
     * @see org.eclipse.jface.viewers.CellEditor#focusLost()
     */
    protected void focusLost() {
        if (isActivated()) {
            applyEditorValueAndDeactivate();
        }
    }

    /*
     *  (non-Javadoc)
     * @see org.eclipse.jface.viewers.CellEditor#keyReleaseOccured(org.eclipse.swt.events.KeyEvent)
     */
    protected void keyReleaseOccured(KeyEvent keyEvent) {
        if (keyEvent.character == '\u001b') { // Escape character
            fireCancelEditor();
        } else if (keyEvent.character == '\t') { // tab key
            applyEditorValueAndDeactivate();
        }
    }
    
    public Object getSelectedValue() {
    	return selectedValue;
    }
}
