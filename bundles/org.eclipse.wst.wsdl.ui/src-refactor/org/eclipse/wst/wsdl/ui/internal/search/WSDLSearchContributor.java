/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - Initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.ui.internal.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.wst.common.core.search.pattern.SearchPattern;
import org.eclipse.wst.xml.core.internal.search.ComponentSearchContributor;
import org.eclipse.wst.xml.core.internal.search.XMLSearchPattern;
import org.eclipse.wst.xsd.ui.internal.search.IXSDSearchConstants;

public class WSDLSearchContributor extends ComponentSearchContributor {


	protected void initializeDeclarations() {

		declarations = new HashMap();
		String ns = IWSDLSearchConstants.WSDL_NAMESPACE;

		SearchPattern pattern = new XMLSearchPattern(ns, "message", "name");
		declarations.put(IWSDLSearchConstants.MESSAGE_META_NAME, pattern);

		pattern = new XMLSearchPattern( ns, "portType", "name");
		declarations.put(IWSDLSearchConstants.PORT_TYPE_META_NAME, pattern);

		pattern = new XMLSearchPattern(ns, "binding", "name");
		declarations.put(IWSDLSearchConstants.BINDING_META_NAME, pattern);

	}

	protected void  initializeReferences() {

		references = new HashMap();
		String ns = IWSDLSearchConstants.WSDL_NAMESPACE;

		List patterns = new ArrayList();
		patterns.add(new XMLSearchPattern( ns, "part", "element"));
		references.put(IXSDSearchConstants.ELEMENT_META_NAME, patterns);

		patterns = new ArrayList();
		patterns.add(new XMLSearchPattern(ns, "part", "type"));
		references.put(IXSDSearchConstants.COMPLEX_TYPE_META_NAME, patterns);

		patterns = new ArrayList();
		patterns.add(new XMLSearchPattern(ns, "part", "type"));
		references.put(IXSDSearchConstants.SIMPLE_TYPE_META_NAME, patterns);

		patterns = new ArrayList();
		patterns.add(new XMLSearchPattern(ns, "input", "message"));
		patterns.add(new XMLSearchPattern(ns, "output", "message"));
		patterns.add(new XMLSearchPattern(ns, "fault", "message"));
		references.put(IWSDLSearchConstants.MESSAGE_META_NAME, patterns);

		patterns = new ArrayList();
		patterns.add(new XMLSearchPattern(ns, "binding", "type"));
		references.put(IWSDLSearchConstants.PORT_TYPE_META_NAME, patterns);

		patterns = new ArrayList();
		patterns.add(new XMLSearchPattern( ns, "port", "binding"));
		references.put(IWSDLSearchConstants.BINDING_META_NAME, patterns);
	}
	
	protected void initializeSupportedNamespaces() {
		namespaces = new String[]{ IXSDSearchConstants.XMLSCHEMA_NAMESPACE, IWSDLSearchConstants.WSDL_NAMESPACE};
	}

}
