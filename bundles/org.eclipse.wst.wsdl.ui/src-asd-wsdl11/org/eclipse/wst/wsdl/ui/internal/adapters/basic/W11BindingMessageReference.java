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

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IBindingMessageReference;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessageReference;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;


public class W11BindingMessageReference extends WSDLBaseAdapter implements IBindingMessageReference, ITreeElement
{
  public ITreeElement[] getChildren()
  {
    return ITreeElement.EMPTY_LIST;
  }
  
  public boolean hasChildren() {
	  return false;
  }
  
  public ITreeElement getParent() {
	  return null;
  }

  public Image getImage()
  {   
    String imageName = null;
    if (target instanceof BindingInput)
    {
      imageName = "icons/input_obj.gif"; //$NON-NLS-1$
    }
    else if (target instanceof BindingOutput)
    {
      imageName = "icons/output_obj.gif";       //$NON-NLS-1$
    } 
    else //if (target instanceof BindingFault)
    {
      imageName = "icons/fault_obj.gif"; //$NON-NLS-1$
    }  
    return WSDLEditorPlugin.getInstance().getImage(imageName);
  }
  
  public String getName()
  {
	  String name = ""; //$NON-NLS-1$
	  if (target instanceof BindingInput)
	  {
		  name = "input";
	  }
	  else if (target instanceof BindingOutput)
	  {
		  name = "output";
	  } 
	  else //if (target instanceof BindingFault)
	  {
		  name = "fault";
	  }  

	  return name;
  }

  public String getText()
  {
    return getName();
  }

  public IMessageReference getMessageReference()
  {
    EObject resultNotifier = null;
    if (target instanceof BindingInput)
    {
      resultNotifier = ComponentReferenceUtil.computeInput((BindingInput)target);
    }
    else if (target instanceof BindingOutput)
    {
      resultNotifier = ComponentReferenceUtil.computeOutput((BindingOutput)target);
    }          
    else if (target instanceof BindingFault)
    {
      resultNotifier = ComponentReferenceUtil.computeFault((BindingFault)target);
    }               
    return resultNotifier != null ? (IMessageReference)createAdapter(resultNotifier) : null;
  }
  
  public List getExtensiblityObjects()
  {
    return Collections.EMPTY_LIST;
  }  
}
