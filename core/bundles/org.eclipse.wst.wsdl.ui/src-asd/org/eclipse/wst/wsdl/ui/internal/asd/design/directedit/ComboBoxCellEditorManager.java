/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd.design.directedit;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public abstract class ComboBoxCellEditorManager extends DirectEditManager
{
  protected Label label;

  public ComboBoxCellEditorManager(GraphicalEditPart source, Label label)
  {
	  super(source, ComboBoxCellEditor.class, new InternalCellEditorLocator(label));
    this.label = label;                        
  }

  protected void initCellEditor() 
  {                                             
  	String initialLabelText = label.getText();   

    ASDCCombo combo = (ASDCCombo)getCellEditor().getControl();
   	combo.setFont(label.getFont());
    combo.setForeground(label.getForegroundColor());
    combo.setBackground(label.getBackgroundColor());
    combo.setVisibleItemCount(20);
    combo.setEditable(false);

  ICellEditorListener cellEditorListener = new ICellEditorListener()
  {
    public void cancelEditor()
    {
    }
    public void applyEditorValue()
    {
    }
    public void editorValueChanged(boolean old, boolean newState)
    {
    }
  };
  getCellEditor().addListener(cellEditorListener);

    String[] item = combo.getItems();
    for (int i = 0; i < item.length; i++)
    {
      if (item[i].equals(initialLabelText))
      {
	      getCellEditor().setValue(new Integer(i));
        break;
      }
    } 	
  }	 
         
  // hack... for some reason the ComboBoxCellEditor does't fire an editorValueChanged to set the dirty flag
  // unless we overide this method to return true, the manager is not notified of changes made in the cell editor
  protected boolean isDirty()
  {
	  return true;
  }

  protected CellEditor createCellEditorOn(Composite composite)
  { 
    boolean isLabelTextInList = false;                                       
    List list = computeComboContent();
    for (Iterator i = list.iterator(); i.hasNext(); )
    {
      String string = (String)i.next();
      if (string.equals(label.getText()))
      {                               
         isLabelTextInList = true;
         break;
      }
    } 
         
    if (!isLabelTextInList)
    {
      list.add(label.getText());
    }
                                               
    List sortedList = computeSortedList(list);
    String[] stringArray = new String[sortedList.size()];
    for (int i = 0; i < stringArray.length; i++)
    {
      stringArray[i] = (String)sortedList.get(i);
    }
    return createCellEditor(composite, stringArray);
  }
  
  protected CellEditor createCellEditor(Composite composite, String[] stringArray)
  {
    return new ComboBoxCellEditor(composite, stringArray);    
  }

  protected List computeSortedList(List list)
  {
    return list;
  }

  protected abstract List computeComboContent(); 

  protected abstract void performModify(Object value);

  public static class InternalCellEditorLocator implements CellEditorLocator
  {
    protected Label label;

    public InternalCellEditorLocator(Label label)
    {
      this.label = label;
    }                   

    public void relocate(CellEditor celleditor) 
    {
        ASDCCombo combo = (ASDCCombo)celleditor.getControl();  
	    Rectangle labelParentBounds = label.getParent().getBounds().getCopy();
	    label.translateToAbsolute(labelParentBounds);
	    
	    int x = labelParentBounds.x;
	    int y = labelParentBounds.y;
	    int widthK = labelParentBounds.width;
	    int height = labelParentBounds.height;
	    combo.setBounds(x, y + 1, widthK, height - 2);
    }
  } 
   
  public void performEdit(CellEditor cellEditor)
  {
	  ASDComboBoxCellEditor comboCellEditor = (ASDComboBoxCellEditor) cellEditor;
	  ASDCCombo combo = (ASDCCombo)getCellEditor().getControl();
	  int index = combo.getSelectionIndex();              
	  if (index != -1)
	  {
		  Object value = combo.getItem(index);
		  if (comboCellEditor.getSelectedValue() != null) {
			  value = comboCellEditor.getSelectedValue();
		  }
		  
		  performModify(value);
	  }
	  else
	  {
		  String typedValue = combo.getText();	
		  if (combo.indexOf(typedValue) != -1)
		  {	      
			  performModify(typedValue);
		  }      	
		  else
		  {
			  String closeMatch = getCloseMatch(typedValue, combo.getItems());
			  if (closeMatch != null)
			  {
				  performModify(closeMatch);      	
			  }
			  else
			  {      	
				  Display.getCurrent().beep();
			  }
		  }
	  }    	                                                
  }
  
  protected String getCloseMatch(String value, String[] items)
  {
    int matchIndex = -1;

    for (int i = 0; i < items.length; i++)
    {
    	String item = items[i];
    	String a = getLocalName(value);
    	String b = getLocalName(item);
		  if (a.equalsIgnoreCase(b))
		  { 
			  matchIndex = i;
			  break;				
		  }	    	     
    }  
    return matchIndex != -1 ? items[matchIndex] : null;
  }
  
  protected String getLocalName(String string)
  {
		int index = string.indexOf(":"); //$NON-NLS-1$
	  return (index != -1) ? string.substring(index + 1) : string;  
  }
}
