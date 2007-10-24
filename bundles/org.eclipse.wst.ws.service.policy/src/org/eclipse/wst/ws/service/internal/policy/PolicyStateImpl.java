/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.service.internal.policy;

import java.util.List;

import org.eclipse.wst.ws.service.policy.IPolicyState;
import org.eclipse.wst.ws.service.policy.listeners.IPolicyStateChangeListener;

public class PolicyStateImpl implements IPolicyState
{
  private List<IPolicyStateChangeListener> stateChangeListeners;

  public String getValue(String key)
  {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean isMutable()
  {
    // TODO Auto-generated method stub
    return false;
  }

  public void putDefaultValue(String key, String defaultValue)
  {
    // TODO Auto-generated method stub
    
  }

  public void putValue(String key, String value)
  {
    // TODO Auto-generated method stub
    
  }

  public void setMutable(boolean mutable)
  {
    // TODO Auto-generated method stub
    
  }  
  
  public void addPolicyStateChangeListener(IPolicyStateChangeListener listener)
  {
    stateChangeListeners.add( listener );
  }
  
  public void removePolicyStateChangeListener(IPolicyStateChangeListener listener)
  {
    stateChangeListeners.remove( listener );
  }
}
