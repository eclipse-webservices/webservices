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
 * 20071024   196997 pmoogk@ca.ibm.com - Peter Moogk, Initial coding of service policy
 *******************************************************************************/
package org.eclipse.wst.ws.internal.service.policy.ui;

import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.wst.ws.service.policy.Descriptor;
import org.eclipse.wst.ws.service.policy.IServicePolicy;
import org.eclipse.wst.ws.service.policy.ui.IEnableOperation;
import org.eclipse.wst.ws.service.policy.ui.ILaunchOperation;
import org.eclipse.wst.ws.service.policy.ui.IPolicyOperation;
import org.eclipse.wst.ws.service.policy.ui.ServicePolicyActivatorUI;
import org.eclipse.wst.ws.service.policy.utils.RegistryUtils;

public class PolicyOperationImpl implements IPolicyOperation
{
  private String                id;
  private Descriptor            descriptor;
  private OperationKind         operationKind;
  private String                policyIdPattern;
  private boolean               multiSelect = false;
  private ILaunchOperation      launchOperationObject;
  private IEnableOperation      enableOperationObject;
  private String                enumId;
  private IConfigurationElement enabledElement;
  private IConfigurationElement complexElement;
  
  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public void setSelection( boolean isIcon )
  {
    if( isIcon )
    {
      operationKind = OperationKind.iconSelection;
    }
    else
    {
      operationKind = OperationKind.selection;     
    }
  }
  
  public Descriptor getDescriptor()
  {
    return descriptor;
  }

  public OperationKind getOperationKind()
  {
    return operationKind;
  }

  public String getEnumerationId()
  {
    return enumId;
  }

  public void setEnumerationId( String enumId )
  {
    this.enumId   = enumId;  
    operationKind = OperationKind.enumeration;
  }
  
  public String getPolicyIdPattern()
  {
    return policyIdPattern;
  }

  public boolean isEnabled( List<IServicePolicy> selectedPolicies )
  {
    boolean result = true;
    
    // If we don't allow multi select and multiple policies have been selected
    // then this operation is not enabled.
    if( !multiSelect && selectedPolicies.size() > 1 )
    {
      result = false;
    }
    
    if( result && enabledElement != null )
    {
      // We have an extension for this enablement code so we will call this
      // code.
      if( enableOperationObject == null )
      {
        try
        {
          String enabledClassName = RegistryUtils.getAttributeName( enabledElement, "enabledclass" );
          enableOperationObject = (IEnableOperation)enabledElement.createExecutableExtension( enabledClassName );
        }
        catch( Exception exc )
        {
          ServicePolicyActivatorUI.logError( "Error loading service policy ui \"enabled\" class.", exc );          
        }
        
        if( enableOperationObject != null )
        {
          result = enableOperationObject.isEnabled( selectedPolicies );
        }
      }
    }
    
    return result;
  }

  public void launchOperation( List<IServicePolicy> selectedPolicies )
  {
    if( launchOperationObject == null )
    {
      try
      {
        String launchClassName = RegistryUtils.getAttributeName( complexElement, "launchclass" );
        launchOperationObject = (ILaunchOperation)complexElement.createExecutableExtension( launchClassName );
      }
      catch( Exception exc )
      {
        ServicePolicyActivatorUI.logError( "Error loading service policy ui launch class.", exc );
      }
    }
    
    if( launchOperationObject != null )
    {
      launchOperationObject.launch( selectedPolicies );
    }
  }

  public boolean isMultiSelect()
  {
    return multiSelect;
  }

  public void setMultiSelect(boolean multiSelect)
  {
    this.multiSelect = multiSelect;
  }

  public void setDescriptor(Descriptor descriptor)
  {
    this.descriptor = descriptor;
  }

  public void setPolicyIdPattern(String policyIdPattern)
  {
    this.policyIdPattern = policyIdPattern;
  }

  public void setEnabledElement(IConfigurationElement enabledElement)
  {
    this.enabledElement = enabledElement;
  }

  public void setComplexElement(IConfigurationElement complexElement)
  {
    this.complexElement = complexElement;
    operationKind = OperationKind.complex;
  }
}
