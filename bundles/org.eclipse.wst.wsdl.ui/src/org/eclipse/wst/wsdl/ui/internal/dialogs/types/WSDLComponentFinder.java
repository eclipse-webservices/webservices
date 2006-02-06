/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.dialogs.types;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.common.core.search.SearchEngine;
import org.eclipse.wst.common.core.search.SearchMatch;
//import org.eclipse.wst.common.core.search.SearchParticipant;
//import org.eclipse.wst.common.core.search.SearchPlugin;
import org.eclipse.wst.common.core.search.pattern.QualifiedName;
import org.eclipse.wst.common.core.search.pattern.SearchPattern;
import org.eclipse.wst.common.core.search.scope.ProjectSearchScope;
import org.eclipse.wst.common.core.search.scope.SearchScope;
import org.eclipse.wst.common.core.search.scope.WorkspaceSearchScope;
import org.eclipse.wst.common.core.search.util.CollectingSearchRequestor;
//import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.wst.xml.core.internal.search.XMLComponentDeclarationPattern;
import org.eclipse.wst.xsd.ui.internal.dialogs.types.xml.XMLComponentFinder;
import org.eclipse.wst.xsd.ui.internal.dialogs.types.xml.XMLComponentSpecification;

public class WSDLComponentFinder extends XMLComponentFinder {
	QualifiedName metaName;
    public WSDLComponentFinder(QualifiedName metaName) {
    	this.metaName = metaName;
    }
    
    
    protected void findComponents(SearchEngine searchEngine, List list, int scope)
    {      
      SearchScope searchScope = new WorkspaceSearchScope();
      if (scope == ENCLOSING_PROJECT_SCOPE)
      {
        searchScope = new ProjectSearchScope(currentIFile.getProject().getLocation());
      }       
      try {
          CollectingSearchRequestor requestor = new CollectingSearchRequestor();
          
          XMLComponentDeclarationPattern pattern = new XMLComponentDeclarationPattern(new QualifiedName("*", "*"), metaName, SearchPattern.R_PATTERN_MATCH);
          
          // TODO (cs) rethink the commented out code below.  Is there some reason for narrowing to list of search particpants?
          // It seems the only benefit may be to filter our xsd component declarations that aren't defined
          // in stand alone wsdl documents.  Perhaps it's best to do this with an additional property arg on the
          // declaration pattern or perhaps simply doing some 'post' filtering.
          //
          //String participantId = metaName.getNamespace().equals(WSDLConstants.WSDL_NAMESPACE_URI) ?
          //    "org.eclipse.wst.wsdl.search.WSDLSearchParticipant" :
          //    "org.eclipse.wst.xsd.search.XSDSearchParticipant";        		  
          //SearchParticipant particpant = SearchPlugin.getDefault().getSearchParticipant(participantId);         
          //SearchParticipant[] participants = { particpant };     
          
          searchEngine.search(pattern, requestor, searchScope, new NullProgressMonitor());
          
          for (Iterator i = requestor.getResults().iterator(); i.hasNext(); )
          {
            SearchMatch match = (SearchMatch)i.next();
            XMLComponentSpecification spec = new XMLComponentSpecification(metaName);
            spec.setFileLocation(match.getFile().getLocation().toString());
            Object o = match.map.get("name");
            if (o != null && o instanceof QualifiedName)
            {  
              QualifiedName qualifiedName = (QualifiedName)o;
              if (qualifiedName.getLocalName() != null)
              {  
                spec.addAttributeInfo("name", qualifiedName.getLocalName());
                spec.setTargetNamespace(qualifiedName.getNamespace());
                list.add(spec);
              }  
            }  
          }  
      } catch (CoreException e) {
        e.printStackTrace();
          //status.add(e.getStatus());
      }      
    }
    
    public List getWorkbenchResourceComponents(int scope) {

        List list = new ArrayList();
        SearchEngine searchEngine = new SearchEngine();
        findComponents(searchEngine, list, scope);    
        return list;       
    }
}
