/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060825   155114 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import java.util.Hashtable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

public class ObjectSelectionRegistry
{
  private static ObjectSelectionRegistry instance;
  private static IConfigurationElement[] elements;
  private static Hashtable               cachedSelectionWidgets;
  
  private ObjectSelectionRegistry()
  {
    elements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.eclipse.jst.ws.consumption.ui", "objectSelectionWidget");
    cachedSelectionWidgets = new Hashtable();    
  }
  
  public static ObjectSelectionRegistry getInstance()
  {
    if (instance == null)
    {
      instance = new ObjectSelectionRegistry();
    }
    
    return instance;
  }

  public IConfigurationElement[] getConfigurationElements()
  {
    return elements;
  }
  
  /**
   * Gets the IObjectSelectionWidget based on the selectionId.  If one is
   * not found null is returned.
   * 
   * @param selectionId the ID of the selection widget.
   * @return returns the registered IObjectSelectionWidget object.
   */
  public IObjectSelectionWidget getSelectionWidget( String selectionId )
  {
    IObjectSelectionWidget result = null;
    
    if( selectionId == null ) return null;
    
    result = (IObjectSelectionWidget)cachedSelectionWidgets.get( selectionId );
    
    if( result == null )
    {
      for( int index = 0; index < elements.length; index++ )
      {
        IConfigurationElement element = elements[index];
        String                elemId  = element.getAttribute( "id" );
      
        if( selectionId.equals( elemId ) )
        {
          try
          {
            Object object = element.createExecutableExtension("class");
            
            if( object instanceof IObjectSelectionWidget )
            {
              result = (IObjectSelectionWidget)object;
              cachedSelectionWidgets.put( selectionId, result );
              break;
            }
          }
          catch( CoreException exc ){}
        }
      }
    }
    
    return result;
  }
  
  public void cleanup()
  {
    instance = null;
    elements = null;
    cachedSelectionWidgets = null;
  }
}
