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
import java.util.Collection;
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
	  String name = null;
	  if (target instanceof BindingInput)
	  {
		  name = ((BindingInput) target).getName();
	  }
	  else if (target instanceof BindingOutput)
	  {
		  name = ((BindingOutput) target).getName();
	  } 
	  else if (target instanceof BindingFault)
	  {
		  name = ((BindingFault) target).getName();
	  }
	  
	  if (name == null) {
		  name = ""; //$NON-NLS-1$
	  }

	  return name;
  }

  public String getText()
  {
	  String text = "";
	  if (target instanceof BindingInput)
	  {
		  text = "binding input";
	  }
	  else if (target instanceof BindingOutput)
	  {
		  text = "binding output";
	  } 
	  else if (target instanceof BindingFault)
	  {
		  text = "binding fault";
	  }
	  
	  return text;
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
  
  public String[] getActions(Object object) {    
    Collection actionIDs = new ArrayList();
//    if (isReadOnly()) {
//      actionIDs.add(OpenInNewEditor.ID);
//    }
    return (String [])actionIDs.toArray(new String[0]);
  }
}
