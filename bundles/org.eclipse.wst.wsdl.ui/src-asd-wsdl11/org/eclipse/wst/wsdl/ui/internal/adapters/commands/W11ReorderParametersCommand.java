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
package org.eclipse.wst.wsdl.ui.internal.adapters.commands;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11ParameterForPart;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;

public class W11ReorderParametersCommand extends Command {
    protected IParameter leftSibling;
    protected IParameter rightSibling;
    protected IParameter movingParameter;
    
    public W11ReorderParametersCommand(IParameter leftSibling, IParameter rightSibling, IParameter movingParameter) {
        super("");  // TODO: Need to add String here...
        this.leftSibling = leftSibling;
        this.rightSibling = rightSibling;
        this.movingParameter = movingParameter;
    }
    
    public void execute() {
		Part leftSibElement = null;
		Part rightSibElement = null;
		Part movingChild = null;
		
		if (leftSibling instanceof W11ParameterForPart) {
			leftSibElement = (Part) ((W11ParameterForPart) leftSibling).getTarget(); 
		}
		if (rightSibling instanceof W11ParameterForPart) {
			rightSibElement = (Part) ((W11ParameterForPart) rightSibling).getTarget();
		}
		if (movingParameter instanceof W11ParameterForPart) {
			movingChild = (Part) ((W11ParameterForPart) movingParameter).getTarget();
		}
		
		if (movingChild.equals(leftSibElement) || movingChild.equals(rightSibElement)) {
			return;
		}
    	
    	if (movingChild != null) {    		
    		Message message = (Message) movingChild.eContainer();
			List parts = message.getEParts();

			parts.remove(movingChild);

			int leftIndex = -1, rightIndex = -1;
			if (leftSibElement != null) {
				leftIndex = parts.indexOf(leftSibElement);
			}
			if (rightSibElement != null) {
				rightIndex = parts.indexOf(rightSibElement);
			}

			if (leftIndex == -1) {
				// Add moving child to the front
				parts.add(0, movingChild);  				
			}
			else if (rightIndex == -1) {
				// Add moving child to the end
				parts.add(movingChild);
			}
			else {
				// Add moving child after the occurence of the left sibling
				parts.add(leftIndex + 1, movingChild);
			}
    	}
    }
}