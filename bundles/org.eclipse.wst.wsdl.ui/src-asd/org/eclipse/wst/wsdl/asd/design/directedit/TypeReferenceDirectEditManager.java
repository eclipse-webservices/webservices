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
package org.eclipse.wst.wsdl.asd.design.directedit;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Label;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.wsdl.asd.editor.ASDEditorPlugin;
import org.eclipse.wst.wsdl.asd.editor.ASDMultiPageEditor;
import org.eclipse.wst.wsdl.asd.facade.IParameter;
import org.eclipse.wst.xsd.adt.edit.ComponentReferenceEditManager;
import org.eclipse.wst.xsd.editor.XSDTypeReferenceEditManager;

public class TypeReferenceDirectEditManager extends ComboBoxCellEditorManager
{
  protected AbstractGraphicalEditPart editPart;                                                
  protected IParameter setObject;
  
  public TypeReferenceDirectEditManager(IParameter parameter, AbstractGraphicalEditPart source, Label label)
  {
    super(source, label);
    editPart = source;
    setObject = parameter;
  }
  
  protected CellEditor createCellEditorOn(Composite composite)
  {
	  return super.createCellEditorOn(composite);
  }

  protected List computeComboContent()
  {
	    List list = new ArrayList();
	    ComponentReferenceEditManager editManager = getComponentReferenceEditManager();
	    if (editManager != null)
	    {
	       list.add("Browse...");
	       list.add("New...");
	       ComponentSpecification[] quickPicks = editManager.getQuickPicks();
	       if (quickPicks != null)
	       {
	         for (int i=0; i < quickPicks.length; i++)
	         {
	           ComponentSpecification componentSpecification = quickPicks[i];
	           list.add(componentSpecification.getName());
	         }  
	       }
	       ComponentSpecification[] history = editManager.getHistory();
	       if (history != null)
	       {
	         for (int i=0; i < history.length; i++)
	         {
	           ComponentSpecification componentSpecification = history[i];
	           list.add(componentSpecification.getName());
	         }  
	       }
	    } 
	    return list; 
  }

  public void performModify(Object value)
  {
	    ComponentReferenceEditManager editManager = getComponentReferenceEditManager();
	    if (editManager == null)
	    {
	      return;
	    }
	    
	    // our crude combo box can only work with 'String' objects
	    // if we get a String back we need to do some clever mapping to get the ComponentSpecification 
	    //    
	    if (value instanceof String)
	    {
	      value = getComponentSpecificationForValue((String)value);     
	    }  
	    // we assume the selected value is always of the form of a ComponentSpecification
	    // 
	    if (value instanceof ComponentSpecification)      
	    {
			editManager.modifyComponentReference(getEditPart().getModel(), (ComponentSpecification) value);
	    }
  } 

  protected List computeSortedList(List list)
  {
//    return TypesHelper.sortList(list);
	  return list;
  }
  
  protected CellEditor createCellEditor(Composite composite, String[] stringArray)
  {
    ASDComboBoxCellEditor cellEditor = new ASDComboBoxCellEditor(composite, stringArray, getComponentReferenceEditManager());
    //((ADTComboBoxCellEditor) cellEditor).setObjectToModify(setObject);
    return cellEditor;
  }

	public ComponentReferenceEditManager getComponentReferenceEditManager() {
		ASDMultiPageEditor editor = (ASDMultiPageEditor) ASDEditorPlugin.getActiveEditor();
		return (ComponentReferenceEditManager) editor.getAdapter(XSDTypeReferenceEditManager.class);
	}
	
	// TODO: rmah: This code should live in a common place..... This code is also used in other UI scenarios when
	// a similar combo box is used.  For example in the properties...(ParameterSection)  Also used in the XSDEditor...
	protected ComponentSpecification getComponentSpecificationForValue(String value)
	{
		ComponentReferenceEditManager editManager = getComponentReferenceEditManager();
		if (editManager != null)
		{  
			ComponentSpecification[] quickPicks = editManager.getQuickPicks();
			if (quickPicks != null)
			{
				for (int i=0; i < quickPicks.length; i++)
				{
					ComponentSpecification componentSpecification = quickPicks[i];
					if (value.equals(componentSpecification.getName()))
					{
						return componentSpecification;
					}                
				}  
			}
			ComponentSpecification[] history = editManager.getHistory();
			if (history != null)
			{
				for (int i=0; i < history.length; i++)
				{
					ComponentSpecification componentSpecification = history[i];
					if (value.equals(componentSpecification.getName()))
					{  
						return componentSpecification;
					}
				}  
			}
		}
		return null;
	}
}
