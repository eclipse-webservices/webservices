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
package org.eclipse.wst.wsdl.ui.internal.soap.customizations;

import java.util.Collections;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.binding.soap.SOAPBody;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;

public class SOAPSelectPartsDialog extends Dialog
{  
  Definition definition;
  SOAPBody body;
  CheckboxTreeViewer checkList;
  Part[] selectedParts = {};
  
  public SOAPSelectPartsDialog(Shell parentShell, Definition definition, SOAPBody body)
  {
    super(parentShell);  
    this.definition = definition;
    this.body = body;
  }

  protected Control createDialogArea(Composite parent)
  {
    Composite composite = (Composite)super.createDialogArea(parent);
    checkList = new CheckboxTreeViewer(composite, SWT.NONE);
    GridData gridData = new GridData(GridData.FILL_BOTH);
    gridData.heightHint = 100;
    gridData.widthHint = 200;
    checkList.getControl().setLayoutData(gridData);
    checkList.setContentProvider(new InternalTreeContentProvider());
    checkList.setLabelProvider(new InternalLabelProvider());
    checkList.setInput(""); //$NON-NLS-1$
    
    List list = body.getParts();
    checkList.setCheckedElements(list.toArray());     
    return composite;
  }
  
  protected void okPressed()
  {
    Object[] checked = checkList.getCheckedElements();
    selectedParts = new Part[checked.length];
    for (int i = 0; i < checked.length; i++)
    {  
      selectedParts[i] = (Part)checked[i];
    } 
    super.okPressed();    
  }
  
 
  class InternalLabelProvider extends LabelProvider
  {
    public String getText(Object element)
    {
      Part part = (Part)element;
      return part.getName();
    }
    
    public Image getImage(Object element)
    {
      return WSDLEditorPlugin.getInstance().getImage("icons/part_obj.gif"); //$NON-NLS-1$
    }
  }
  
  class InternalTreeContentProvider implements ITreeContentProvider
  {  
    public Object[] getElements(Object inputElement)
    {    
      EObject container = body.eContainer();
      MessageReference messageReference = null;
      if (container instanceof BindingInput)
      {
        messageReference = ComponentReferenceUtil.computeInput((BindingInput)container); 
      } 
      else if (container instanceof BindingOutput)
      {
        messageReference = ComponentReferenceUtil.computeOutput((BindingOutput)container);         
      }  
      if (messageReference != null && messageReference.getEMessage() != null)
      {
        return messageReference.getEMessage().getEParts().toArray();
      }  
      return Collections.EMPTY_LIST.toArray();
    }

    public Object[] getChildren(Object parentElement)
    {
      return Collections.EMPTY_LIST.toArray();
    }

    public Object getParent(Object element)
    {
      return null;
    }

    public boolean hasChildren(Object element)
    {
      return false;
    }

    public void dispose()
    {      
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
    }
  }

  public Part[] getSelectedParts()
  {
    return selectedParts;
  }
}
