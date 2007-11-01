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
package org.eclipse.wst.ws.internal.service.policy.ui;

import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.ws.service.policy.IDescriptor;
import org.eclipse.wst.ws.service.policy.ui.ServicePolicyActivatorUI;
import org.eclipse.wst.ws.service.policy.utils.RegistryUtils;

public class ServicePolicyRegistryUI
{
  private final String SERVICE_POLICY_ID = ServicePolicyActivatorUI.PLUGIN_ID + ".servicepolicyui"; //$NON-NLS-1$
  
  public ServicePolicyRegistryUI()
  {
  }
  
  public void load( Map<String, PolicyOperationImpl> operationMap )
  {
    IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor(SERVICE_POLICY_ID);
    
    for( IConfigurationElement element : elements )
    {
      String elementName = element.getName().toLowerCase();
      
      if( elementName.equals( "servicepolicyui") ) //$NON-NLS-1$
      {
        IConfigurationElement[] children = element.getChildren();
        
        for( IConfigurationElement child : children )
        {
          String name = child.getName().toLowerCase();
          
          if( name.equals( "operation" ) ) //$NON-NLS-1$
          {
            loadServicePolicyui( child, operationMap );
          }
          else
          {
            error( "Undefined service policy ui element, " + name + " found." ); //$NON-NLS-1$ //$NON-NLS-2$            
          }
        }
      }
      else
      {
        error( "Undefined service policy ui element, " + elementName + " found." ); //$NON-NLS-1$ //$NON-NLS-2$
      }
    } 
  }
   
  private void loadServicePolicyui( IConfigurationElement element,
                                    Map<String, PolicyOperationImpl> operationMap )
  {
    String                  id             = RegistryUtils.getAttribute( element, "id" ); //$NON-NLS-1$
    String                  policyPattern  = RegistryUtils.getAttribute( element, "policypattern" ); //$NON-NLS-1$
    String                  workspaceOnly  = RegistryUtils.getAttribute( element, "workspaceonly" ); //$NON-NLS-1$
    IConfigurationElement[] children       = element.getChildren();
    IDescriptor             descriptor     = null;
    String                  enumId         = null;
    boolean                 selection      = false;
    boolean                 icon           = false;
    boolean                 multiSelect    = false;
    IConfigurationElement   enabledElement = null;
    IConfigurationElement   launchElement  = null;
    boolean                 error          = false;
    
    for( IConfigurationElement child : children )
    {
      String name = child.getName().toLowerCase();
      
      if( name.equals( "descriptor" ) ) //$NON-NLS-1$
      {
        descriptor = RegistryUtils.loadDescriptor( child );
      }
      else if( name.equals( "enumeration" ) ) //$NON-NLS-1$
      {
        enumId = RegistryUtils.getAttribute( child, "id" ); //$NON-NLS-1$
        
        if( enumId == null )
        {
          error( "Service policy ui enumeration element is missing id attribute." ); //$NON-NLS-1$
          error = true;
        }
      }
      else if( name.equals( "selection" ) ) //$NON-NLS-1$
      {
        String iconValue = RegistryUtils.getAttribute( child, "icon" ); //$NON-NLS-1$
        
        selection = true;
        
        if( iconValue != null && iconValue.equalsIgnoreCase( "true" ) ) //$NON-NLS-1$
        {
          icon = true;
        }
      }
      else if( name.equals( "complex" ) ) //$NON-NLS-1$
      {
        String launchClass = RegistryUtils.getAttribute( child, "launchclass" ); //$NON-NLS-1$
        
        if( launchClass == null )
        {
          error( "Service policy ui enumeration element is missing id attribute." );                     //$NON-NLS-1$
          error = true;
        }
        
        launchElement = child;
      }
      else if( name.equals( "enabled" ) ) //$NON-NLS-1$
      {
        String multiSelectValue = RegistryUtils.getAttribute( child, "multiselect" ).toLowerCase(); //$NON-NLS-1$
       
        if( multiSelectValue != null && multiSelectValue.equals( "true" )) //$NON-NLS-1$
        {
          multiSelect = true;
        }
       
        String enabledClass = RegistryUtils.getAttribute( child, "enabledclass" ); //$NON-NLS-1$
        
        if( enabledClass != null )
        {
          enabledElement = child;
        }
      }
      else 
      {
        error( "Undefined service policy ui element, " + name + " found." ); //$NON-NLS-1$ //$NON-NLS-2$
        error = true;
      }
    }
    
    if( descriptor == null )
    {
      error( "Service policy operation element must have a descriptor element defined." );   //$NON-NLS-1$
      error = true;
    }
    
    if( id == null )
    {
      error( "Id attribute not specified in service policy ui operation element." ); //$NON-NLS-1$
      error = true;
    }
    
    if( policyPattern == null )
    {
      error( "Id attribute not specified in service policy ui operation element." ); //$NON-NLS-1$
      error = true;     
    }
    
    if( ( enumId == null && !selection && launchElement == null)  ||
        ( enumId != null && (selection || launchElement != null)) ||
        ( selection && (enumId != null || launchElement != null)) ||
        ( launchElement != null && (selection && enumId != null)))
    {
      error( "Service policy operation element must have either an enumeration element or a selection element or a complex element" ); //$NON-NLS-1$
      error = true;
    }
    
    if( ! error )
    {
      PolicyOperationImpl operation          = new PolicyOperationImpl();
      boolean             workspaceOnlyValue = workspaceOnly != null && workspaceOnly.equals( "true" );
      
      operation.setId( id );
      operation.setPolicyIdPattern( policyPattern );
      operation.setDescriptor( descriptor );
      operation.setMultiSelect( multiSelect );
      operation.setEnumerationId( enumId );
      operation.setWorkspaceOnly( workspaceOnlyValue );
      
      if( selection )
      {
        operation.setSelection( icon );
      }
      
      if( launchElement != null )
      {
        operation.setComplexElement( launchElement );
      }
      
      if( enabledElement != null )
      {
        operation.setEnabledElement( enabledElement );
      }
      
      operationMap.put( id, operation );
    }
  }
  
  private void error( String message )
  {
    ServicePolicyActivatorUI.logError( message, null );
  }
}
