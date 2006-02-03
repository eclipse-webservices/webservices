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
import java.util.List;

import org.eclipse.wst.common.core.search.SearchParticipant;
import org.eclipse.wst.common.core.search.SearchPlugin;
import org.eclipse.wst.common.core.search.pattern.SearchPattern;
import org.eclipse.wst.xml.core.internal.search.ComponentSearchContributor;
import org.eclipse.wst.xml.core.internal.search.XMLComponentDeclarationPattern;
import org.eclipse.wst.xml.core.internal.search.XMLComponentReferencePattern;
import org.eclipse.wst.xml.core.internal.search.XMLComponentSearchPattern;
import org.eclipse.wst.xml.core.internal.search.XMLSearchParticipant;
import org.eclipse.wst.xml.core.internal.search.XMLSearchPattern;
import org.eclipse.wst.xsd.ui.internal.search.IXSDSearchConstants;

public class WSDLSearchParticipant extends XMLSearchParticipant {
	
	private static String ID = "org.eclipse.wst.wsdl.search.WSDLSearchParticipant";
	private static String XSD_PARTICIPANNT_ID = "org.eclipse.wst.xsd.search.XSDSearchParticipant";


	
	public  boolean initialize(SearchPattern pattern, String[] contentTypes){
		
		super.initialize(pattern, contentTypes);
		id = ID;
		if(pattern instanceof XMLComponentSearchPattern ){
			XMLComponentSearchPattern componentPattern = (XMLComponentSearchPattern)pattern;
			String namespace = componentPattern.getMetaName().getNamespace();
			if(IWSDLSearchConstants.WSDL_NAMESPACE.equals(namespace) ||
					IXSDSearchConstants.XMLSCHEMA_NAMESPACE.equals(namespace)){
				return true;
			}
		}
		return false;
	}


	public ComponentSearchContributor getSearchContributor() {

		return new WSDLSearchContributor();
	}
	
  public void beginSearching(SearchPattern pattern) {
		
		super.beginSearching(pattern);
		List patterns = new ArrayList();
		if(pattern instanceof XMLComponentDeclarationPattern){
			
			XMLComponentDeclarationPattern componentPattern = (XMLComponentDeclarationPattern)pattern;
			if(IWSDLSearchConstants.WSDL_NAMESPACE.equals(componentPattern.getMetaName().getNamespace())){
				XMLSearchPattern childPattern = getSearchContributor().getDeclarationPattern(componentPattern.getMetaName());
				if(childPattern != null){
					childPattern.setSearchName(componentPattern.getName().getLocalName());
					childPattern.setSearchNamespace(componentPattern.getName().getNamespace());
	     			patterns.add(childPattern);
				}
			}
			else if(IXSDSearchConstants.XMLSCHEMA_NAMESPACE.equals(componentPattern.getMetaName().getNamespace())){
				SearchParticipant xsdParticipant = SearchPlugin.getDefault().getSearchParticipant(XSD_PARTICIPANNT_ID);
				if(xsdParticipant instanceof XMLSearchParticipant){
					ComponentSearchContributor xsdContributor = ((XMLSearchParticipant)xsdParticipant).getSearchContributor();
					if(xsdContributor != null){
						XMLSearchPattern childPattern = xsdContributor.getDeclarationPattern(componentPattern.getMetaName());
						if(childPattern != null){
							childPattern.setSearchName(componentPattern.getName().getLocalName());
							childPattern.setSearchNamespace(componentPattern.getName().getNamespace());
			     			patterns.add(childPattern);
						}
					}
				}
			}
			componentPattern.setChildren((XMLSearchPattern[]) patterns.toArray(new XMLSearchPattern[patterns.size()]));
			
		}
		else if(pattern instanceof XMLComponentReferencePattern){
			XMLComponentReferencePattern componentPattern = (XMLComponentReferencePattern)pattern;
			XMLSearchPattern[] childPatterns = getSearchContributor().getReferencesPatterns(componentPattern.getMetaName());
			for (int i = 0; i < childPatterns.length; i++) {
				XMLSearchPattern childPattern = childPatterns[i];
				childPattern.setSearchName(componentPattern.getName().getLocalName());
				childPattern.setSearchNamespace(componentPattern.getName().getNamespace());	
				patterns.add(childPattern);
			}
			SearchParticipant xsdParticipant = SearchPlugin.getDefault().getSearchParticipant(XSD_PARTICIPANNT_ID);
			if(xsdParticipant instanceof XMLSearchParticipant){
				ComponentSearchContributor xsdContributor = ((XMLSearchParticipant)xsdParticipant).getSearchContributor();
				if(xsdContributor != null){
					childPatterns = xsdContributor.getReferencesPatterns(componentPattern.getMetaName());
					for (int i = 0; i < childPatterns.length; i++) {
						XMLSearchPattern childPattern = childPatterns[i];
						childPattern.setSearchName(componentPattern.getName().getLocalName());
						childPattern.setSearchNamespace(componentPattern.getName().getNamespace());		
						patterns.add(childPattern);
					}
				}
			}
			componentPattern.setChildren((XMLSearchPattern[]) patterns.toArray(new XMLSearchPattern[patterns.size()]));
			
		}
		
	}

  
  
	
	
	

}
