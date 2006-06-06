/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.wizard;

import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceImpl;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.wst.ws.internal.wsrt.WebServiceScenario;


public class TypeSelectionFilter2
{
  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  public String[] getWebServiceTypeByInitialSelection(IStructuredSelection initialSelection, ArrayList webServiceTypeList)
  {
        
    ArrayList supportedTypes = new ArrayList();
    if (initialSelection != null && initialSelection.size() == 1)
    {
      
      // Check for null initial selection
      Object initialObject = initialSelection.getFirstElement();
      if (initialObject == null)
      {
        return null;
      }
      // Match up resource type metadata with initial selection
      Iterator iter = webServiceTypeList.iterator();
      while (iter.hasNext())
      {
        String wst = (String)iter.next();
        String scenario = wst.substring(0,wst.indexOf("/"));
        String implId = wst.substring(wst.indexOf("/")+1);        
        //IWebServiceType wst = (IWebServiceType)iter.next();
        //String typeId = (String)wst.getId();
        if (scenario.equals(String.valueOf(WebServiceScenario.BOTTOMUP)) && !supportedTypes.contains(wst))
        {

          
          WebServiceImpl wsimpl = WebServiceRuntimeExtensionUtils2.getWebServiceImplById(implId);
          String[] resourceTypes = wsimpl.getResourceTypeMetadata();
          for (int i=0; i<resourceTypes.length; i++)
          {
            if (resourceTypes[i]!=null)
            {              
              if ((initialObject.getClass().getName()).endsWith(resourceTypes[i]))
              {
                String name = null;
                IResource resource = null;

                try
                {                  
                  resource = ResourceUtils.getResourceFromSelection(initialObject);
                }
                catch( CoreException exc )
                {
                  resource = null;
                }

                if (resource instanceof IContainer)
                {
                  name = initialObject.getClass().getName();
                }
                else if( resource != null )
                {
                  name = resource.getFullPath().toString();
                }
                else
                {
                  name = initialObject.getClass().getName();
                }
                String[] extensions = wsimpl.getExtensionMetadata();
                if (acceptsName(name, extensions))
                {
                  supportedTypes.add(wst);
                }
              }
              
            }
          }
        }
      }
      
      //If no wsImpls have accepted this selection, check if this is WSDL. If it is,
      //add the top-down Java service type to the list.
      if (supportedTypes.isEmpty())
      {
        String[] resourceTypes = {"File", "IResource", "String", "ServiceImpl", "ServiceRefImpl", "WSDLResourceImpl"};
        for (int i=0; i<resourceTypes.length; i++)
        {
          if (resourceTypes[i]!=null)
          {              
            if ((initialObject.getClass().getName()).endsWith(resourceTypes[i]))
            {
              String name = null;
              IResource resource = null;

              try
              {                  
                resource = ResourceUtils.getResourceFromSelection(initialObject);
              }
              catch( CoreException exc )
              {
                resource = null;
              }

              if (resource instanceof IContainer)
              {
                name = initialObject.getClass().getName();
              }
              else if( resource != null )
              {
                name = resource.getFullPath().toString();
              }
              else if (initialObject instanceof String)
              { //the initialObject could be a path name that is a string
                //bug 105045
            	name = (String)initialObject;
              }
              else
              {
                name = initialObject.getClass().getName();
              }    
              
              String[] extensions = {".wsdl", ".wsil", ".html", ".ServiceImpl", ".ServiceRefImpl", ".WSDLResourceImpl"};
              if (acceptsName(name, extensions))
              {
                StringBuffer entrybuff = new StringBuffer();
                entrybuff.append(String.valueOf(WebServiceScenario.TOPDOWN));
                entrybuff.append("/");
                entrybuff.append("org.eclipse.jst.ws.wsImpl.java");
                String entry = entrybuff.toString();                
                supportedTypes.add(entry);
              }
            }
            
          }
        }      
      }
      
    }    
  

    
    if (supportedTypes.isEmpty())
    {
      //It's not a registered implementation and its not WSDL so return null;
      return null;
    }
    else
    {
      return (String[])supportedTypes.toArray(new String[0]);
    }
  }
  //
  // Checks if the given name is acceptable based upon its extension
  //
  private boolean acceptsName ( String name, String[] extensions)
  {
    //Return true if "all" extensions are supported.

    if (extensions[0]!=null)
    {
      if (extensions[0].equals("all"))
      {
        return true;
      }
    }

    for (int i=0; i<extensions.length; i++)
    {
      if (extensions[i]!=null)
      {
        if (name.endsWith(extensions[i]))
          return true;
      }
    }
    return false;
  }


}
