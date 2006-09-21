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
import java.util.Iterator;
import java.util.List;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.actions.OpenInNewEditor;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.actions.W11AddPartAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.actions.W11SetExistingElementAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.actions.W11SetExistingTypeAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.actions.W11SetNewElementAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.actions.W11SetNewTypeAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11DeleteCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11SetElementCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11SetTypeCommand;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddFaultAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddOperationAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDDeleteAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.BaseSelectionAction;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessageReference;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IOperation;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;
import org.eclipse.wst.wsdl.ui.internal.visitor.WSDLVisitorForParameters;
import org.eclipse.xsd.XSDAttributeUse;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;


public class W11ParameterForPart extends WSDLBaseAdapter implements IParameter
{
  protected Part getPart()
  {
    return (Part) target;
  }

  public String getName()
  {
    return getPart().getName();
  }

  public String getComponentName()
  {
    if (getPart().getElementDeclaration() != null)
    {  
      return getPart().getElementDeclaration().getName();
    }
    else if (getPart().getTypeDefinition() != null)
    {
      return getPart().getTypeDefinition().getName();
    }
    else
    {
      return getNoNameSpecifiedString();
    }  
  }
  
  private String getNoNameSpecifiedString() {
	  return "";
  }

  public String getComponentNameQualifier()
  {
    if (getPart().getElementDeclaration() != null)
    {  
      return getPart().getElementDeclaration().getTargetNamespace();
    }
    else if (getPart().getTypeDefinition() != null)
    {
      return getPart().getTypeDefinition().getTargetNamespace();
    }
    else
    {  
      return "(no namespace specified)";
    }  
  }
  
  public String getPreview() {
	  String preview = ""; //$NON-NLS-1$
	  
//      parameters = new ArrayList();
//      otherThingsToListenTo = new ArrayList();
      WSDLVisitorForParameters visitorForParameters = new WSDLVisitorForParameters();
      visitorForParameters.visitMessage((Message) getPart().eContainer());
      
      Iterator it = visitorForParameters.concreteComponents.iterator();
      while (it.hasNext()) {
    	  String stringItem = null;
    	  Object item = it.next();
    	  if (item instanceof XSDElementDeclaration) {
    		  XSDElementDeclaration xsdElement = (XSDElementDeclaration) item;
    		  xsdElement = xsdElement.getResolvedElementDeclaration();
    		  if (xsdElement.getTypeDefinition() != null) {
    			  stringItem = xsdElement.getTypeDefinition().getName();
    		  }
     	  }
    	  else if (item instanceof XSDAttributeUse) {
    		  stringItem = ((XSDAttributeUse) item).getAttributeDeclaration().getName();
    	  }
    	  
    	  else if (item instanceof Part) {
    		  if (((Part) item).getTypeDefinition() != null) {
    			  stringItem = ((Part) item).getTypeDefinition().getName();
    		  }
    	  }
    	  
    	  if (stringItem != null) {
    		  preview = preview + stringItem + ", "; //$NON-NLS-1$
    	  }
      }
      
      if (preview.length() -2 > 0) {
    	  preview = preview.substring(0, preview.length() - 2);
      }
      
      return "(" + preview + ")"; //$NON-NLS-1$ //$NON-NLS-2$
  }
  
  public String[] getActions(Object object) {
	  if (object instanceof MultiPageEditorPart) {
		  IOperation operation = ((IMessageReference) getOwner()).getOwnerOperation();

		  List actions = new ArrayList();
		  actions.add(W11AddPartAction.ID);
		  actions.add(ASDAddOperationAction.ID);
		  actions.addAll(((W11Operation) operation).getValidInputOutpuActions());
		  actions.add(ASDAddFaultAction.ID);
		  
		  actions.add(BaseSelectionAction.SUBMENU_START_ID + Messages._UI_ACTION_SET_TYPE); //$NON-NLS-1$
		  actions.add(W11SetNewTypeAction.ID);
		  actions.add(W11SetExistingTypeAction.ID);
		  actions.add(BaseSelectionAction.SUBMENU_END_ID);

		  actions.add(BaseSelectionAction.SUBMENU_START_ID + Messages._UI_ACTION_SET_ELEMENT); //$NON-NLS-1$
		  actions.add(W11SetNewElementAction.ID);
		  actions.add(W11SetExistingElementAction.ID);
		  actions.add(BaseSelectionAction.SUBMENU_END_ID);		  
		  
		  actions.add(ASDDeleteAction.ID);
      
      if (isReadOnly()) {
        actions.add(OpenInNewEditor.ID);
      }
		  
		  String[] actionIDs = new String[actions.size()];
		  for (int index = 0; index < actions.size(); index++) {
			  actionIDs[index] = (String) actions.get(index);
		  }
	  
		  return actionIDs;
	  }
	  if (object instanceof ContentOutline) {
		  String[] actionIDs = new String[2];
		  actionIDs[0] = W11AddPartAction.ID;
		  actionIDs[1] = ASDDeleteAction.ID;
		  
		  return actionIDs;
	  }
	  
	  return new String[0];
  }

  public Command getDeleteCommand()
  {
	  return new W11DeleteCommand(this);
  }

  public Object getOwner()
  {
	  return owner;
  }
  
	public Image getImage() {
		return WSDLEditorPlugin.getInstance().getImage("icons/part_obj.gif"); //$NON-NLS-1$
	}
    
    public Image getSecondaryImage() {
      
      if (getPart().getElementDeclaration() != null)
      {  
        return WSDLEditorPlugin.getInstance().getImage("icons/element_obj.gif");
      }
      else if (getPart().getTypeDefinition() != null)
      {
        if (getPart().getTypeDefinition() instanceof XSDComplexTypeDefinition)
        {  
          return WSDLEditorPlugin.getInstance().getImage("icons/complextype_obj.gif");
        }
        else
        {
          return WSDLEditorPlugin.getInstance().getImage("icons/simpletype_obj.gif");
        }  
      }
      else
      {  
        return WSDLEditorPlugin.getInstance().getImage("icons/part_obj.gif"); //$NON-NLS-1$
      }  
    }    
	
	public String getText() {
		return "part";
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
	
	public Command getSetTypeCommand(String actionId) {
		return new W11SetTypeCommand((Part) this.getTarget(), actionId);
	}
	
	public Command getSetElementCommand(String actionId) {
		return new W11SetElementCommand((Part) this.getTarget(), actionId);
	}
	
	// TODO: We should move the isType() method to the IParameter Interface........
	public boolean isType() {
		Part part = (Part) target;
		if (part.getTypeDefinition() != null) {
			return true;
		}
		
		return false;
	}
}
