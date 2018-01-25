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
package org.eclipse.wst.wsdl.ui.internal.asd.design;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.BaseSelectionAction;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessageReference;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IOperation;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;
import org.eclipse.wst.xsd.ui.internal.adt.design.IKeyboardDrag;
import org.eclipse.wst.xsd.ui.internal.adt.design.editpolicies.KeyBoardAccessibilityEditPolicy;

public class KeyboardDragImpl extends BaseSelectionAction implements IKeyboardDrag {

	public KeyboardDragImpl() {
		super(null);
	}
	
	public void performKeyboardDrag(GraphicalEditPart movingElement, int direction) {

		KeyBoardAccessibilityEditPolicy policy = (KeyBoardAccessibilityEditPolicy)movingElement.getEditPolicy(KeyBoardAccessibilityEditPolicy.KEY);
		
		EditPart rightElement = policy.getRelativeEditPart(movingElement, direction);
		policy = (KeyBoardAccessibilityEditPolicy)rightElement.getEditPolicy(KeyBoardAccessibilityEditPolicy.KEY);
		EditPart leftElement = (policy != null)? policy.getRelativeEditPart(rightElement, direction): null;
		  
		Object leftSibElement = (leftElement != null)? leftElement.getModel() : null;
		Object rightSibElement = (rightElement != null)? rightElement.getModel() : null;
		if (direction == PositionConstants.SOUTH) {
			leftSibElement = rightSibElement;
			rightSibElement = leftSibElement;
		} else if (direction != PositionConstants.NORTH) return;
		
		Object source = movingElement.getModel();

		if (source instanceof IMessageReference) {
  			
			if(!(leftSibElement instanceof IMessageReference)) leftSibElement = null;
			if(!(rightSibElement instanceof IMessageReference)) rightSibElement = null;
			
			Object messageRefOwner = ((IMessageReference) source).getOwnerOperation();
  			if (messageRefOwner instanceof IOperation) {
  				IMessageReference leftSib = (IMessageReference) leftSibElement;
  				IMessageReference rightSib = (IMessageReference) rightSibElement;
  				IMessageReference movingSib = (IMessageReference) source;
  				Command command = ((IOperation) messageRefOwner).getReorderMessageReferencesCommand(leftSib, rightSib, movingSib);
  				command.execute();
  				performSelection(source);
  			}
  		}
  		else if (source instanceof IParameter) {
  			
  			if(!(leftSibElement instanceof IParameter)) leftSibElement = null;
			if(!(rightSibElement instanceof IParameter)) rightSibElement = null;
			
  			Object paramOwner = ((IParameter) source).getOwner();
  			if (paramOwner instanceof IMessageReference) {
  				IParameter leftSib = (IParameter) leftSibElement;
  				IParameter rightSib = (IParameter) rightSibElement;
  				IParameter movingSib = (IParameter) source;
  				Command command = ((IMessageReference) paramOwner).getReorderParametersCommand(leftSib, rightSib, movingSib);
  				command.execute();
  				performSelection(source);
  			}
  		}
	}
}
