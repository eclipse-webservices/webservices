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

import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11ParameterForAttribute;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11ParameterForElement;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11ParameterForPart;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;

public class W11ReorderParametersCommand extends W11TopLevelElementCommand {
    protected IParameter leftSibling;
    protected IParameter rightSibling;
    protected IParameter movingParameter;
    
    public W11ReorderParametersCommand(IParameter leftSibling, IParameter rightSibling, IParameter movingParameter) {
        super(Messages._UI_ACTION_REORDER_PART, null);
        this.leftSibling = leftSibling;
        this.rightSibling = rightSibling;
        this.movingParameter = movingParameter;
    }
    
    public void execute() {
    	if (leftSibling instanceof W11ParameterForPart ||
    		rightSibling instanceof W11ParameterForPart ||
    		movingParameter instanceof W11ParameterForPart) {
    		executeForPart();
    	}
    	else if (leftSibling instanceof W11ParameterForElement ||
        		 rightSibling instanceof W11ParameterForElement ||
        		 movingParameter instanceof W11ParameterForElement) {
    		executeForElement();
        }
    	else if (leftSibling instanceof W11ParameterForAttribute ||
       		     rightSibling instanceof W11ParameterForAttribute ||
    		     movingParameter instanceof W11ParameterForAttribute) {
//    		executeForElement();
    	}
    }
    
    private void executeForElement() {
    	XSDElementDeclaration leftSibElement = null;
    	XSDElementDeclaration rightSibElement = null;
    	XSDElementDeclaration movingChild = null;
    	
		XSDParticle movingParticle = null;
		XSDParticle leftParticle = null;
		XSDParticle rightParticle = null;

		if (leftSibling instanceof W11ParameterForElement) {
			leftSibElement = (XSDElementDeclaration) ((W11ParameterForElement) leftSibling).getTarget();
			leftParticle = (XSDParticle) leftSibElement.eContainer();
		}
		if (rightSibling instanceof W11ParameterForElement) {
			rightSibElement = (XSDElementDeclaration) ((W11ParameterForElement) rightSibling).getTarget();
			rightParticle = (XSDParticle) rightSibElement.eContainer();
		}
		if (movingParameter instanceof W11ParameterForElement) {
			movingChild = (XSDElementDeclaration) ((W11ParameterForElement) movingParameter).getTarget();
			movingParticle = (XSDParticle) movingChild.eContainer();
		}
		
		if (movingChild.equals(leftSibElement) || movingChild.equals(rightSibElement)) {
			return;
		}
    	
    	if (movingChild != null) {
    		try {
    			beginRecording(movingParticle.getElement());

    			XSDModelGroup container = (XSDModelGroup) movingParticle.getContainer();
    			List particles = container.getContents();
    			
    			particles.remove(movingParticle);

    			int leftIndex = -1, rightIndex = -1;
    			if (leftParticle != null) {
    				leftIndex = particles.indexOf(leftParticle);
    			}
    			if (rightParticle!= null) {
    				rightIndex = particles.indexOf(rightParticle);
    			}

    			if (leftIndex == -1) {
    				// Add moving child to the front
    				particles.add(0, movingParticle);  				
    			}
    			else if (rightIndex == -1) {
    				// Add moving child to the end
    				particles.add(movingParticle);
    			}
    			else {
    				// Add moving child after the occurence of the left sibling
    				particles.add(leftIndex + 1, movingParticle);
    			}
    		}
    		finally {
    			endRecording(movingParticle.getElement());
    		}
    	}
    }
    
    private void executeForPart() {
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
    		try {
    			beginRecording(movingChild.getElement());
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
    		finally {
    			endRecording(movingChild.getElement());
    		}
    	}
    }
}