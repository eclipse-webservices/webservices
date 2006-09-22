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
package org.eclipse.wst.wsdl.ui.internal.edit;

import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.common.core.search.scope.SearchScope;
import org.eclipse.wst.common.ui.internal.search.dialogs.IComponentList;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.search.IWSDLSearchConstants;

public class WSDLBindingSearchListProvider extends WSDLBaseSearchListProvider {
	public WSDLBindingSearchListProvider(Definition definition) {
		this.definition = definition;
	}
	
	public void populateComponentList(IComponentList list, SearchScope scope, IProgressMonitor pm) {
		// Grab explictly defined components
		createWSDLComponentObjects(list, definition.getEBindings(), IWSDLSearchConstants.BINDING_META_NAME);

		// Files excluded if we search with the search engine later
		HashMap exclusions = new HashMap();	
		exclusions.put(definition.getDocumentBaseURI(), Boolean.TRUE);		
		
		// Grab directly imported components and update the exclusions 'list'
		getImportedComponents(list, IWSDLSearchConstants.BINDING_META_NAME, exclusions);

		searchOutsideCurrentResource(list, scope, IWSDLSearchConstants.BINDING_META_NAME, exclusions);
	}

	protected List getSearchingComponents(Definition importDefinition) {
		return importDefinition.getEBindings();
	}
}
