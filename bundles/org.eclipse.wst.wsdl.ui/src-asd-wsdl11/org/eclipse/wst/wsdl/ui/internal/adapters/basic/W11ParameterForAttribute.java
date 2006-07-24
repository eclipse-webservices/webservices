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
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.actions.W11SetExistingTypeAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.actions.W11SetNewTypeAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11DeleteParameterCommand;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDDeleteAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.BaseSelectionAction;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessageReference;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDAttributeUse;
import org.eclipse.xsd.XSDTypeDefinition;

//import com.ibm.ccl.soa.sdo.wsdl.ui.internal.commands.SDOW11DeleteCommand;

public class W11ParameterForAttribute extends WSDLBaseAdapter implements IParameter {
	
	protected XSDAttributeUse getXSDAttributeUse() {
		return (XSDAttributeUse) target;
	}

	protected XSDAttributeDeclaration getXSDAtttributeDeclaration() {
		XSDAttributeUse attrUse = getXSDAttributeUse();
		return attrUse.getAttributeDeclaration();		
	}
	
	public Object getOwner() {
		return (IMessageReference) owner;
	}

	public String getName() {
		return getXSDAtttributeDeclaration().getName();
	}
	
	public String getComponentName() {
		XSDAttributeDeclaration attrDecl = getXSDAtttributeDeclaration();
		
		if (attrDecl.getType() != null) {
			return attrDecl.getType().getName();
		}
		
		return "error: could not get type name"; //$NON-NLS-1$
	}
	
	public String getPreview() {
		return ""; //$NON-NLS-1$
	}

	public String getComponentNameQualifier() {
        XSDTypeDefinition td = getXSDAtttributeDeclaration().getTypeDefinition();
        return td != null ? td.getTargetNamespace() : null;
	}
	
	  public String[] getActions(Object object) {
          List actions = new ArrayList();
          //list.add(ASDAddOperationAction.ID);
          //list.add(ASDAddInputAction.ID);
          //list.add(ASDAddOutputAction.ID);
          //list.add(ASDAddFaultAction.ID);
          actions.add(BaseSelectionAction.SUBMENU_START_ID + Messages.getString("_UI_ACTION_SET_TYPE")); //$NON-NLS-1$
          actions.add(W11SetNewTypeAction.ID);
          actions.add(W11SetExistingTypeAction.ID);
          actions.add(BaseSelectionAction.SUBMENU_END_ID);

          actions.add(ASDDeleteAction.ID);    
          String[] result = new String[actions.size()];
          actions.toArray(result);
          return result;
	}

	public Command getDeleteCommand() {
        return new W11DeleteParameterCommand(this);
	}
	
	public Command getSetTypeCommand(String id) {
		return null;
	}

	public Image getImage() {
		return null;
	}
	
	public String getText() {
		return "parameter - attribute";
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
