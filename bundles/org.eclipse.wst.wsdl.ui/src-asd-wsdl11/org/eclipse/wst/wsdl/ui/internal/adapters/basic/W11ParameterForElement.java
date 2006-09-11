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
import java.util.List;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.actions.W11SetExistingTypeAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.actions.W11SetNewTypeAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11DeleteParameterCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11RenameCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11SetTypeCommand;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDDeleteAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.BaseSelectionAction;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessageReference;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;
import org.eclipse.wst.xsd.ui.internal.adt.editor.ProductCustomizationProvider;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDTypeDefinition;


public class W11ParameterForElement extends WSDLBaseAdapter implements IParameter
{
  protected XSDElementDeclaration getXSDElementDeclaration()
  {
    return ((XSDElementDeclaration) target).getResolvedElementDeclaration();
  }

  public String getName()
  {
    return getXSDElementDeclaration().getName();
  }

  public String getComponentName()
  {
	String compName = "";// + Messages.getString("_UI_LABEL_NO_TYPE_SPECIFIED") + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    XSDTypeDefinition anonymousType = getXSDElementDeclaration().getAnonymousTypeDefinition();
    XSDTypeDefinition td = getXSDElementDeclaration().getTypeDefinition();
    
    if (anonymousType != null) {
        compName = "**anonymous**"; 
    }

    return td.getName() != null ? td.getName() : compName;
  }
  
  public String getPreview() {
	  return ""; //$NON-NLS-1$
  }

  public String getComponentNameQualifier()
  {
    XSDTypeDefinition td = getXSDElementDeclaration().getTypeDefinition();
    return td != null ? td.getTargetNamespace() : null;
   }
  
  public String[] getActions(Object object) {
	  List actions = new ArrayList();
	  //list.add(ASDAddOperationAction.ID);
      //list.add(ASDAddInputAction.ID);
      //list.add(ASDAddOutputAction.ID);
      //list.add(ASDAddFaultAction.ID);
      actions.add(BaseSelectionAction.SUBMENU_START_ID + Messages._UI_ACTION_SET_TYPE); //$NON-NLS-1$
      actions.add(W11SetNewTypeAction.ID);
      actions.add(W11SetExistingTypeAction.ID);
      actions.add(BaseSelectionAction.SUBMENU_END_ID);
     
      actions.add(ASDDeleteAction.ID);	  
      String[] result = new String[actions.size()];
      actions.toArray(result);
	  return result;
  }

  public Command getDeleteCommand()
  {
    return new W11DeleteParameterCommand(this);
  }

  public Command getSetTypeCommand(String actionId) {
	  return new W11SetTypeCommand(this.getTarget(), actionId);
  }
  
  public Command getSetNameCommand(String newName) {    
    return new W11RenameCommand(this, newName) {
      public void execute() {
  		try {
			beginRecording(getXSDElementDeclaration().getElement());
			getXSDElementDeclaration().setName(newName);
  		}
  		finally {
  			endRecording(getXSDElementDeclaration().getElement());
  		}
      }
    };  
  }
  
  public Object getOwner()
  {
	  return (IMessageReference) owner;
  }
  
	public Image getImage() {
		return null;
	}
	
	public String getText() {
		return getParameterString() + " - element"; 
	}
	
	  private String getParameterString() {
		  String string = "parameter";

		  if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null &&
				  PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() != null &&
				  PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() != null) {
			  Object object = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getAdapter(ProductCustomizationProvider.class);
			  if (object instanceof ProductCustomizationProvider) {
				  ProductCustomizationProvider productCustomizationProvider = (ProductCustomizationProvider)object;
				  String newString = productCustomizationProvider.getProductString("_UI_LABEL_PARAMETER");
				  if (newString != null) {
					  string = newString;
				  }
			  }
		  }

		  return string;
	  }
	
	public ITreeElement[] getChildren() {
		return new ITreeElement[0];
	}

	public boolean hasChildren() {
		return false;
	}

	public ITreeElement getParent() {
		return null;
	}
}
