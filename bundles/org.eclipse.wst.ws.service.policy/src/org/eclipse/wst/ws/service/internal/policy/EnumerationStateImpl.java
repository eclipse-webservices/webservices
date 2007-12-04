/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20071024   196997 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.wst.ws.service.internal.policy;

import java.util.List;

import org.eclipse.wst.ws.service.policy.IPolicyState;
import org.eclipse.wst.ws.service.policy.IPolicyStateEnum;
import org.eclipse.wst.ws.service.policy.IStateEnumerationItem;
import org.eclipse.wst.ws.service.policy.ServicePolicyPlatform;

public class EnumerationStateImpl implements IPolicyStateEnum
{
  private final String VALUE = IPolicyState.DefaultValueKey;  //$NON-NLS-1$
  
  private String                      enumId;
  private List<IStateEnumerationItem> enumList;
  private IPolicyState                state;
  private String                      defaultId;
  
  public EnumerationStateImpl( String enumId, String defaultId, IPolicyState state )
  {
    ServicePolicyPlatform platform = ServicePolicyPlatform.getInstance();
    
    this.enumId    = enumId;
    this.enumList  = platform.getStateEnumeration( enumId );
    this.state     = state;
    this.defaultId = enumList.get(0).getId();
   
    if( defaultId != null )
    {
      this.defaultId = defaultId;
      state.putDefaultValue( VALUE, defaultId ); 
    }
    else
    {
      for( IStateEnumerationItem enumItem : enumList )
      {
        if( enumItem.isDefault() )
        {
          this.defaultId = enumItem.getId();
          state.putDefaultValue( VALUE,  this.defaultId );
          break;
        }
      }
    }
  }

  public String getEnumId()
  {
    return enumId;
  }

  public String getDefaultId()
  {
    return defaultId;
  }
  
  public void setCurrentItem( String itemId )
  {
    for( IStateEnumerationItem enumItem : enumList )
    {
      String enumItemId = enumItem.getId();
      
      if( enumItemId.equals( itemId ) )
      {
        state.putValue( VALUE, enumItemId ); 
        break;
      }
    }
  }
  
  public IStateEnumerationItem getCurrentItem()
  {
    String                currentEnum = state.getValue( VALUE );
    IStateEnumerationItem currentItem = null;
    
    for( IStateEnumerationItem enumItem : enumList )
    {
      if( enumItem.getId().equals( currentEnum ) )
      {
        currentItem = enumItem;
        break;
      }
    }
    
    return currentItem;
  }
}
