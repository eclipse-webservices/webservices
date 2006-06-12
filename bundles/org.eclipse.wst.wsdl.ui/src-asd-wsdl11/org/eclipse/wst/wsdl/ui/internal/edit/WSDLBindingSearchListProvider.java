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

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.common.core.search.scope.SearchScope;
import org.eclipse.wst.common.ui.internal.search.dialogs.IComponentList;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.ui.internal.search.IWSDLSearchConstants;

public class WSDLBindingSearchListProvider extends WSDLBaseSearchListProvider {
	private Definition definition;
	
	public WSDLBindingSearchListProvider(Definition definition) {
		this.definition = definition;
	}
	
	public void populateComponentList(IComponentList list, SearchScope scope, IProgressMonitor pm) {
		// Grab explictly defined components
		createWSDLComponentObjects(list, definition.getEBindings(), IWSDLSearchConstants.BINDING_META_NAME);
		
		// Grab directly imported components
		Iterator importsIt = getWSDLFileImports(definition.getEImports()).iterator();
		while (importsIt.hasNext()) {
			Import importItem = (Import) importsIt.next();
			Definition importDefinition = importItem.getEDefinition();
			if (importDefinition != null)
			{		
		  	  List importedComponents = importDefinition.getEBindings();			
			  createWSDLComponentObjects(list, importedComponents, IWSDLSearchConstants.BINDING_META_NAME);
			}  
		}
		
		if (scope != null) {
			WSDLComponentFinder finder = new WSDLComponentFinder(IWSDLSearchConstants.BINDING_META_NAME);
			Iterator it = finder.getWorkbenchResourceComponents(scope).iterator();
			while (it.hasNext()) {
				list.add(it.next());
			}
		}
	}
}
