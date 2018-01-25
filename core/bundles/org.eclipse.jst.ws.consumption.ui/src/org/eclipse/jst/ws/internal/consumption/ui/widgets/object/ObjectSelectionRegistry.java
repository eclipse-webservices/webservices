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
 * 20060830   155114 pmoogk@ca.ibm.com - Peter Moogk, Updated patch for this defect.
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import java.util.Hashtable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.IObjectSelectionLaunchable;
import org.eclipse.wst.command.internal.env.core.data.Transformer;

public class ObjectSelectionRegistry
{
  private static ObjectSelectionRegistry instance;
  private IConfigurationElement[]        elements;
  private Hashtable                      cachedSelectionWidgets;
  
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
  
  /**
   * 
   * @param id if the selection widget object.
   * @return returns the transformer class if has been specified.  Otherwise
   * null is returned.
   */
  public Transformer getTransformer( String id )
  {
    TableEntry  entry  = getTableEntry( id );
    Transformer result = null;
    
    if( entry != null )
    {
      try
      {
        Object object = entry.element.createExecutableExtension("transformer");
        
        if( object instanceof Transformer )
        {
          result = (Transformer)object;
        }
      }
      catch( CoreException exc ){}     
    }
    
    return result;
  }
  
  /**
   * 
   * @param id if the selection widget object.
   * @return returns the value of the external modify attribute if
   * it has been specified.  Otherwise false is returned.
   */
  public boolean getExternalModify( String id )
  {
    TableEntry entry  = getTableEntry( id );
    boolean    result = false;
    
    if( entry != null )
    {
      String modifyString = entry.element.getAttribute("external_modify"); 
      
      result = new Boolean( modifyString ).booleanValue();
    }
    
    return result;
  }
  
  /**
   * Gets the IObjectSelectionWidget based on the selectionId.  If one is
   * not found null is returned.
   * 
   * @param selectionId the ID of the selection widget.
   * @return returns the registered IObjectSelectionWidget object.
   */
  public Object getSelectionWidget( String selectionId )
  {
    TableEntry entry = getTableEntry( selectionId );
    
    return entry == null ? null : entry.objectSelection;
  }
  
  public void cleanup()
  {
    instance = null;
    elements = null;
    cachedSelectionWidgets = null;
  }
  
  private TableEntry getTableEntry( String id )
  {
    if( id == null ) return null;
    
    TableEntry entry = (TableEntry)cachedSelectionWidgets.get( id );
    
    if( entry == null )
    {
      for( int index = 0; index < elements.length; index++ )
      {
        IConfigurationElement element = elements[index];
        String                elemId  = element.getAttribute( "id" );
      
        if( id.equals( elemId ) )
        {
          try
          {
            Object object = element.createExecutableExtension("class");
            
            if( object instanceof IObjectSelectionWidget || object instanceof IObjectSelectionLaunchable )
            {
              entry = new TableEntry();
              entry.element = element;
              entry.objectSelection = object;
              cachedSelectionWidgets.put( id, entry );
              break;
            }
          }
          catch( CoreException exc ){}
        }
      }
    }
    
    return entry;
  }
  
  private class TableEntry
  {
    public IConfigurationElement  element;
    public Object                 objectSelection;
  }
}
