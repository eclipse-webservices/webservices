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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.common.ResourceUtils;


public class TypeSelectionFilter
{
  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  private IStructuredSelection initSelection_;

  public String[] getWebServiceTypeByInitialSelection(IStructuredSelection initialSelection, HashMap webServiceTypes)
  {
        
    HashSet supportedTypes = new HashSet();
    if (initialSelection != null && initialSelection.size() == 1)
    {
      
      // Check for null initial selection
      Object initialObject = initialSelection.getFirstElement();
      if (initialObject == null)
      {
        return null;
      }
      // Match up resource type metadata with initial selection
      Iterator iter = webServiceTypes.values().iterator();
      while (iter.hasNext())
      {
        IWebServiceType wst = (IWebServiceType)iter.next();
        String typeId = (String)wst.getId();
        if (!supportedTypes.contains(typeId))
        {
          String[] resourceTypes = wst.getResourceTypeMetadata();
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
                String[] extensions = wst.getExtensionMetadata();
                if (acceptsName(name, extensions))
                {
                  supportedTypes.add(typeId);
                }
              }
              
            }
          }
        }
      }
      
    }    
  
    if (supportedTypes.isEmpty())
      return null;
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
