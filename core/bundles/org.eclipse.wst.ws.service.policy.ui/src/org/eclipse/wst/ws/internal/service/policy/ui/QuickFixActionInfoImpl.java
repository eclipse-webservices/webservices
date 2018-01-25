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
 * 20071120   196997 pmoogk@ca.ibm.com - Peter Moogk, Initial coding.
 *******************************************************************************/
package org.eclipse.wst.ws.internal.service.policy.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.wst.ws.service.policy.IDescriptor;
import org.eclipse.wst.ws.service.policy.ui.IQuickFixAction;
import org.eclipse.wst.ws.service.policy.ui.IQuickFixActionInfo;
import org.eclipse.wst.ws.service.policy.ui.ServicePolicyActivatorUI;

public class QuickFixActionInfoImpl implements IQuickFixActionInfo
{
  private IConfigurationElement element;
  private IDescriptor           descriptor;
  
  public QuickFixActionInfoImpl( IConfigurationElement element, IDescriptor descriptor )
  {
    this.element    = element;
    this.descriptor = descriptor;
  }
  
  public IQuickFixAction getAction()
  {
    IQuickFixAction result = null;
    
    try
    {
      Object action = element.createExecutableExtension( "class" ); //$NON-NLS-1$
      
      if( action instanceof IQuickFixAction )
      {
        result = (IQuickFixAction)action;
      }
    }
    catch( CoreException exc )
    {
      ServicePolicyActivatorUI.logError( "Error loading quick fix action.", exc ); //$NON-NLS-1$
    }
    
    return result;
  }

  public IDescriptor getDescriptor()
  {
    return descriptor;
  }
}
