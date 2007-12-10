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
import org.eclipse.wst.ws.service.policy.IDescriptor;
import org.eclipse.wst.ws.service.policy.IServicePolicy;
import org.eclipse.wst.ws.service.policy.IStateEnumerationItem;
import org.eclipse.wst.ws.service.policy.ServicePolicyPlatform;
import org.eclipse.wst.ws.service.policy.ui.IEnableOperation;
import org.eclipse.wst.ws.service.policy.ui.ILaunchOperation;
import org.eclipse.wst.ws.service.policy.ui.ServicePolicyActivatorUI;
import org.eclipse.wst.ws.service.policy.ui.IPolicyOperation.OperationKind;
import org.eclipse.wst.ws.service.policy.utils.RegistryUtils;

public class BaseOperationImpl 
{
  private String                id;
  private IDescriptor           descriptor;
  private OperationKind         operationKind;
  private String                policyIdPattern;
  private boolean               multiSelect = false;
  private ILaunchOperation      launchOperationObject;
  private IEnableOperation      enableOperationObject;
  private String                enumId;
  private IConfigurationElement enabledElement;
  private IConfigurationElement complexElement;
  private boolean               workspaceOnly;
  private String                defaultItem;  
  private boolean               useDefaultData;
  
  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }
  public boolean isWorkspaceOnly()
  {
    return workspaceOnly;
  }

  public void setWorkspaceOnly( boolean workspaceOnly )
  {
    this.workspaceOnly = workspaceOnly;  
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
  
  public IDescriptor getDescriptor()
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
          String enabledClassName = RegistryUtils.getAttributeName( enabledElement, "enabledclass" ); //$NON-NLS-1$
          enableOperationObject = (IEnableOperation)enabledElement.createExecutableExtension( enabledClassName );
        }
        catch( Exception exc )
        {
          ServicePolicyActivatorUI.logError( "Error loading service policy ui \"enabled\" class.", exc );           //$NON-NLS-1$
        }
        
        if( enableOperationObject != null )
        {
          result = enableOperationObject.isEnabled( selectedPolicies );
        }
      }
    }
    
    return result;
  }

  public void launchOperation( IServicePolicy thisPolicy, List<IServicePolicy> selectedPolicies )
  {
    if( launchOperationObject == null )
    {
      try
      {
        String launchClassName = RegistryUtils.getAttributeName( complexElement, "launchclass" ); //$NON-NLS-1$
        launchOperationObject = (ILaunchOperation)complexElement.createExecutableExtension( launchClassName );
      }
      catch( Exception exc )
      {
        ServicePolicyActivatorUI.logError( "Error loading service policy ui launch class.", exc ); //$NON-NLS-1$
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

  public void setDescriptor(IDescriptor descriptor)
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

  public void setDefaultItem( String defaultItem )
  {
    this.defaultItem = defaultItem;  
  }
  
  public String getDefaultItem()
  {
    if( defaultItem == null )
    {
      ServicePolicyPlatform       platform = ServicePolicyPlatform.getInstance();
      List<IStateEnumerationItem> enumList = platform.getStateEnumeration( enumId );
      
      if( enumList != null )
      {
        defaultItem = enumList.get(0).getId();
      
        // No default item has been specified so we will search the enumeration for one.
        // If none was specified the first item in the enumeration is used.
        for( IStateEnumerationItem item : enumList )
        {
          if( item.isDefault() )
          {
            defaultItem = item.getId();
            break;
          }
        }
      }
    }
      
    return defaultItem;
  }

  public boolean isUseDefaultData()
  {
    return useDefaultData;
  }

  public void setUseDefaultData( boolean useDefaultData )
  {
    this.useDefaultData = useDefaultData;
  }  
  
}
