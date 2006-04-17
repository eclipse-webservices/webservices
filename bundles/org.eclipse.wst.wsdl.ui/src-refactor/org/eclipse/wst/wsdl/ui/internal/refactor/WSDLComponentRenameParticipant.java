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
package org.eclipse.wst.wsdl.ui.internal.refactor;

import java.util.List;

import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.search.IWSDLSearchConstants;
import org.eclipse.wst.xsd.ui.internal.refactor.rename.ComponentRenameArguments;
import org.eclipse.wst.xsd.ui.internal.refactor.rename.XMLComponentRenameParticipant;
import org.eclipse.xsd.XSDNamedComponent;

/**
 * This participant takes case of renaming matches that are XSD components
 */
public class WSDLComponentRenameParticipant extends XMLComponentRenameParticipant {

	protected boolean initialize(Object element) {

		if(element instanceof WSDLElement || element instanceof XSDNamedComponent){
			if(getArguments() instanceof ComponentRenameArguments){
				matches = (List)((ComponentRenameArguments)getArguments()).getMatches().get(IWSDLSearchConstants.WSDL_NAMESPACE);
			}
			if(matches != null){
				return true;
			}
		}
		return false;
	}

	public String getName() {
		
		return "WSDL component rename participant"; //$NON-NLS-1$
	}
}
