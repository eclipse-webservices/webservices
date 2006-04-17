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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.common.core.search.SearchEngine;
import org.eclipse.wst.common.core.search.SearchMatch;
import org.eclipse.wst.common.core.search.pattern.QualifiedName;
import org.eclipse.wst.common.core.search.pattern.SearchPattern;
import org.eclipse.wst.common.core.search.scope.SearchScope;
import org.eclipse.wst.common.core.search.util.CollectingSearchRequestor;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.xml.core.internal.search.XMLComponentDeclarationPattern;

public class WSDLComponentFinder {
	QualifiedName metaName;
    public WSDLComponentFinder(QualifiedName metaName) {
    	this.metaName = metaName;
    }
    
    
    protected void findComponents(SearchEngine searchEngine, List list, SearchScope searchScope)
    {
      try {
          CollectingSearchRequestor requestor = new CollectingSearchRequestor();
          
          XMLComponentDeclarationPattern pattern = new XMLComponentDeclarationPattern(new QualifiedName("*", "*"), metaName, SearchPattern.R_PATTERN_MATCH); //$NON-NLS-1$ //$NON-NLS-2$
          
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
          
          // TODO (cs) I'm betting we'll need to provide some hook here to allow extenders to configure
          // the search options.  Currently we just pass in null.
          searchEngine.search(pattern, requestor, searchScope, null, new NullProgressMonitor());
          
          for (Iterator i = requestor.getResults().iterator(); i.hasNext(); )
          {
            SearchMatch match = (SearchMatch)i.next();
            ComponentSpecification spec = new ComponentSpecification();
            spec.setMetaName(metaName);
            spec.setFile(match.getFile());
            
            Object o = match.map.get("name"); //$NON-NLS-1$
            if (o != null && o instanceof QualifiedName)
            {  
              QualifiedName qualifiedName = (QualifiedName)o;
              if (qualifiedName.getLocalName() != null)
              {
                spec.setName(qualifiedName.getLocalName());
                spec.setQualifier(qualifiedName.getNamespace());
                list.add(spec);
              }  
            }  
          }  
      } catch (CoreException e) {
        e.printStackTrace();
          //status.add(e.getStatus());
      }      
    }
    
    public List getWorkbenchResourceComponents(SearchScope scope) {

        List list = new ArrayList();
        SearchEngine searchEngine = new SearchEngine();
        findComponents(searchEngine, list, scope);    
        return list;       
    }
}
