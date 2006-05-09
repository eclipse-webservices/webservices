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
package org.eclipse.wst.wsdl.ui.internal.adapters.basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IBindingOperation;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IOperation;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;

public class W11BindingOperation extends WSDLBaseAdapter implements IBindingOperation, ITreeElement
{
  
  public IOperation getOperation()
  {
    BindingOperation bindingOperation = (BindingOperation)target;
    Operation operation = ComponentReferenceUtil.computeOperation(bindingOperation);    
    return operation != null ? (IOperation)createAdapter(operation) : null;
  }
  
  public List getBindingMessages()
  {
    List list = new ArrayList();
    BindingOperation bindingOperation = (BindingOperation)target;
    if (bindingOperation.getEBindingInput() != null)
    {  
      list.add(bindingOperation.getEBindingInput());
    }
    if (bindingOperation.getEBindingOutput() != null)
    {  
      list.add(bindingOperation.getEBindingOutput());
    }     
    list.addAll(bindingOperation.getEBindingFaults());
    List result = new ArrayList();
    populateAdapterList(list, result);
    return result;
  }
  
  public String getName()
  {
    BindingOperation bindingOperation = (BindingOperation)target;
    Operation operation = bindingOperation.getEOperation();
    return operation != null ? operation.getName() : null;
  }

  public List getExtensiblityObjects()
  {
    return Collections.EMPTY_LIST;
  }

  public ITreeElement[] getChildren()
  {
    List list = getBindingMessages();
    ITreeElement[] result = new ITreeElement[list.size()];
    list.toArray(result);
    return result;
  }
  
  public boolean hasChildren() {
	  if (getBindingMessages().size() > 0) {
		  return true;
	  }
	  
	  return false;
  }
  
  public ITreeElement getParent() {
	  return null;
  }

  public Image getImage()
  {
    String imageName = "icons/operationbinding_obj.gif"; //$NON-NLS-1$
    return WSDLEditorPlugin.getInstance().getImage(imageName);
  }

  public String getText()
  {
    return getName();
  }
}
