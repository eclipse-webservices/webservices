/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20071024   196997 pmoogk@ca.ibm.com - Peter Moogk
 * 20080428   227501 pmoogk@ca.ibm.com - Peter Moogk, Fixed toLowerCase Locale problem.
 *******************************************************************************/
package org.eclipse.wst.ws.service.internal.policy;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.ExpressionConverter;
import org.eclipse.core.expressions.ExpressionTagNames;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.ws.service.policy.IStateEnumerationItem;
import org.eclipse.wst.ws.service.policy.ServicePolicyActivator;
import org.eclipse.wst.ws.service.policy.listeners.IPolicyPlatformLoadListener;
import org.eclipse.wst.ws.service.policy.utils.RegistryUtils;

public class ServicePolicyRegistry
{
  private final String SERVICE_POLICY_ID = ServicePolicyActivator.PLUGIN_ID + ".servicepolicy"; //$NON-NLS-1$
  
  private ServicePolicyPlatformImpl platform;
  
  public ServicePolicyRegistry( ServicePolicyPlatformImpl platform )
  {
    this.platform = platform;
  }
  
  public void load( List<IPolicyPlatformLoadListener>        loadListeners,
                    Map<String, ServicePolicyImpl>           policyMap,
                    Map<String, List<IStateEnumerationItem>> enumMap,
                    Map<String, StateEnumerationItemImpl>    enumItemMap )
  {
    IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor(SERVICE_POLICY_ID);
    
    for( IConfigurationElement element : elements )
    {
      String elementName = element.getName().toLowerCase( Locale.ENGLISH );
      
      if( elementName.equals( "servicepolicy") ) //$NON-NLS-1$
      {
        loadServicePolicy( element, loadListeners, policyMap, enumMap, enumItemMap );
      }
      else
      {
        error( "Undefined service policy element, " + elementName + " found." ); //$NON-NLS-1$ //$NON-NLS-2$
      }
    }
    
    // Resolve all policy services.
    for( String policyId : policyMap.keySet() )
    {
      ServicePolicyImpl policy = policyMap.get( policyId );
      
      policy.resolve();
    }
  }
   
  private void loadServicePolicy( IConfigurationElement                    element,
                                  List<IPolicyPlatformLoadListener>        loadListeners,
                                  Map<String, ServicePolicyImpl>           policyMap,
                                  Map<String, List<IStateEnumerationItem>> enumMap,
                                  Map<String, StateEnumerationItemImpl>    enumItemMap ) 
  {
    IConfigurationElement[] childElements = element.getChildren();
    
    for( IConfigurationElement child : childElements )
    {
      String childName = child.getName().toLowerCase( Locale.ENGLISH );
      
      if( childName.equals( "loadlistener" ) ) //$NON-NLS-1$
      {
        loadLoadListener( child, loadListeners );
      }
      else if( childName.equals( "policy" ) ) //$NON-NLS-1$
      {
        loadPolicy( child, policyMap );
      }
      else if( childName.equals( "enumeration" ) ) //$NON-NLS-1$
      {
        loadEnumeration( child, enumMap, enumItemMap );
      }
      else if( childName.equals( ExpressionTagNames.ENABLEMENT ) ) 
      {
        loadEnablement( child );
      }
      else
      {
        error( "Undefined service policy element, " + childName + " found." );         //$NON-NLS-1$ //$NON-NLS-2$
      }
    }
  }
  
  private void loadEnablement( IConfigurationElement element )
  {
    try
    {
      Expression expression = ExpressionConverter.getDefault().perform( element );
      
      platform.addEnabledExpression( expression );
    }
    catch( CoreException exc )
    {
      error( "Error loading enablement expression: " + exc.getMessage() ); //$NON-NLS-1$
    }
  }
  
  private void loadEnumeration( IConfigurationElement                    element,
                                Map<String, List<IStateEnumerationItem>> enumMap,
                                Map<String, StateEnumerationItemImpl>    enumItemMap )
  {
    String                      enumId        = RegistryUtils.getAttribute( element, "id" ); //$NON-NLS-1$
    String                      defaultEnumId = RegistryUtils.getAttribute( element, "default" ); //$NON-NLS-1$
    IConfigurationElement[]     childElements = element.getChildren();
    List<IStateEnumerationItem> enumList      = new Vector<IStateEnumerationItem>();
    
    if( enumId == null )
    {
      error( "\"Id\" attribute missing from Serivce Policy Enumeration element." ); //$NON-NLS-1$
      return;
    }
    
    for( IConfigurationElement child : childElements )
    {
      String childName = child.getName().toLowerCase( Locale.ENGLISH );
      
      if( childName.equals( "item" ) ) //$NON-NLS-1$
      {
        loadItem( child, enumList, enumItemMap );
      }
      else
      {
        error( "Undefined service policy enumeration element, " + childName + " found." );         //$NON-NLS-1$ //$NON-NLS-2$
      } 
    }
    
    if( defaultEnumId != null )
    {
      StateEnumerationItemImpl defaultEnum = enumItemMap.get( defaultEnumId );
      
      if( defaultEnum == null )
      {
        error( "Default enum attribute for enumeration " + enumId + " not found." ); //$NON-NLS-1$ //$NON-NLS-2$
      }
      else
      {
        defaultEnum.setDefault( true );
      }
    }
    
    enumMap.put( enumId, enumList );
  }
  
  private void loadItem( IConfigurationElement                 element,
                         List<IStateEnumerationItem>           enumList,
                         Map<String, StateEnumerationItemImpl> enumItemMap)
  {
    String itemId    = RegistryUtils.getAttribute( element, "id" ); //$NON-NLS-1$
    String shortName = RegistryUtils.getAttribute( element, "shortname" ); //$NON-NLS-1$
    String longName  = RegistryUtils.getAttribute( element, "longname" ); //$NON-NLS-1$
    
    if( itemId == null )
    {
      error( "\"Id\" attribute missing from Serivce Policy Enumeration item element." ); //$NON-NLS-1$
    }
    
    if( shortName == null && longName == null )
    {
      error( "shortName or longName attribute missing from Service Policy Enumeration item element." ); //$NON-NLS-1$
    }
    else if( shortName == null )
    {
      shortName = longName;
    }
    else if( longName == null )
    {
      longName = shortName;
    }
      
    StateEnumerationItemImpl item = new StateEnumerationItemImpl();
    
    item.setId( itemId );
    item.setShortName( shortName );
    item.setLongName( longName );
    enumList.add( item );
    enumItemMap.put( itemId, item );
  }
  
  private void loadLoadListener( IConfigurationElement element,
                                 List<IPolicyPlatformLoadListener> loadListeners )
  {
    try
    {
      Object listener = element.createExecutableExtension( "class" ); //$NON-NLS-1$
      
      if( listener instanceof IPolicyPlatformLoadListener )
      {
        loadListeners.add( (IPolicyPlatformLoadListener)listener );
      }
      else
      {
        error( "Load listener, " + element.getAttribute( "class" ) + " does not implement IPolicyPlatformLoadListener" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      }
    }
    catch( CoreException exc )
    {
      ServicePolicyActivator.logError( "Error loading service policy loadlistener.", exc ); //$NON-NLS-1$
    }
  }
  
  private void loadPolicy( IConfigurationElement policy, Map<String, ServicePolicyImpl> policyMap )
  {
    IConfigurationElement[]      policyElements = policy.getChildren();
    String                       parentId       = RegistryUtils.getAttribute( policy, "parentpolicyid" ); //$NON-NLS-1$
    String                       id             = RegistryUtils.getAttribute( policy, "id" ); //$NON-NLS-1$
    String                       enumListId     = RegistryUtils.getAttribute( policy, "enumlistid" ); //$NON-NLS-1$
    String                       defaultEnumId  = RegistryUtils.getAttribute( policy, "defaultenumid" ); //$NON-NLS-1$
    String                       mutableValue   = RegistryUtils.getAttribute( policy, "mutable" ); //$NON-NLS-1$
    boolean                      mutable        = mutableValue != null && mutableValue.equalsIgnoreCase( "true" );          //$NON-NLS-1$
    DescriptorImpl               descriptor     = null;
    List<UnresolvedRelationship> relationships  = new Vector<UnresolvedRelationship>();
    List<String[]>               stateKeyValues = new Vector<String[]>();
    
    // If the mutable attribute was not specified and the enumListId attribute
    // attribute was specified then this policy should be mutable, since
    // it doesn't make sense to associate state with a policy that is not
    // changeable.
    if( mutableValue == null && enumListId != null )
    {
      mutable = true;  
    }
    
    try
    {
      for( IConfigurationElement policyElement : policyElements )
      {
        String name = policyElement.getName().toLowerCase( Locale.ENGLISH );
      
        if( name.equals( "descriptor") ) //$NON-NLS-1$
        {
          descriptor = RegistryUtils.loadDescriptor( policyElement );
        }
        else if( name.equals( "relationship" ) ) //$NON-NLS-1$
        {
          loadRelationship( policyElement, relationships );
        }
        else if( name.equals( "state" ) ) //$NON-NLS-1$
        {
          String key   = RegistryUtils.getAttribute( policyElement, "key" ); //$NON-NLS-1$
          String value = RegistryUtils.getAttribute( policyElement, "value" ); //$NON-NLS-1$
          
          if( key == null )
          {
            error( "Service policy state missing attribute \"key\"." ); //$NON-NLS-1$           
          }
          
          if( value == null )
          {
            error( "Service policy state missing attribute \"value\"." ); //$NON-NLS-1$           
          }
          
          if( key != null && value != null )
          {
            stateKeyValues.add( new String[]{ key, value } );
          }
        }
        else
        {
          error( "Undefined service policy element, " + name + " found." );                   //$NON-NLS-1$ //$NON-NLS-2$
        }
      }
      
      ServicePolicyImpl newPolicy = new ServicePolicyImpl( true, id, platform );
      
      newPolicy.setUnresolvedParent( parentId );
      newPolicy.setUnresolvedRelationships( relationships );
      newPolicy.setDescriptor( descriptor );
      newPolicy.setEnumListId( enumListId );
      newPolicy.setDefaultEnumId( defaultEnumId );
      policyMap.put( id, newPolicy );
      
      PolicyStateImpl policyState = (PolicyStateImpl)newPolicy.getPolicyState();
      
      // Temporarily allow the state to be updated for static state provided
      // by the extender.
      policyState.internalSetMutable( true );
      
      for( String[] keyValue : stateKeyValues )
      {
        policyState.putValue( keyValue[0], keyValue[1] );
      }
      
      policyState.internalSetMutable( mutable );
    }
    catch( IllegalArgumentException exc )
    {
      error( "An error was found loading a service policy." ); //$NON-NLS-1$
    }
  }
    
  private void loadRelationship( IConfigurationElement relationshipElement, List<UnresolvedRelationship> relationships )
  {
    String enumValueAttr = RegistryUtils.getAttribute( relationshipElement, "enumlist" ); //$NON-NLS-1$
    
    if( enumValueAttr == null )
    {
      error( "Service policy relationship missing attribute \"enumList\"." ); //$NON-NLS-1$
      throw new IllegalArgumentException();
    }
    
    List<String>                       sourceEnumList         = Arrays.asList( enumValueAttr.split( " " ) ); //$NON-NLS-1$
    IConfigurationElement[]            children               = relationshipElement.getChildren();
    List<UnresolvedPolicyRelationship> targetRelationshipList = new Vector<UnresolvedPolicyRelationship>();
    
    for( IConfigurationElement child : children )
    {
      String name = child.getName().toLowerCase( Locale.ENGLISH );
      
      if( name.equals( "targetpolicy" ) ) //$NON-NLS-1$
      {
        loadPolicyRelationship( child, targetRelationshipList );
      }
      else
      {
        error( "Undefined service policy element, " + name + " found." );                   //$NON-NLS-1$ //$NON-NLS-2$
      }
    }
    
    UnresolvedRelationship relationship = new UnresolvedRelationship( sourceEnumList, targetRelationshipList );
    
    relationships.add( relationship );
  }
  
  private void loadPolicyRelationship( IConfigurationElement policyElement,
                                       List<UnresolvedPolicyRelationship> policyRelationshipList )
  {
    String itemListAttr = RegistryUtils.getAttribute( policyElement, "itemlist" ); //$NON-NLS-1$
    String policyId      = RegistryUtils.getAttribute( policyElement, "id" ); //$NON-NLS-1$
    
    if( itemListAttr == null )
    {
      error( "Service policy relationship missing attribute \"enumList\"." ); //$NON-NLS-1$
      throw new IllegalArgumentException();
    }
    
    if( policyId == null )
    {
      error( "Service policy relationship missing attribute \"id\"." ); //$NON-NLS-1$
      throw new IllegalArgumentException();
    }
    
    List<String>                 targetEnumList     = Arrays.asList( itemListAttr.split( " " ) ); //$NON-NLS-1$
    UnresolvedPolicyRelationship policyRelationship = new UnresolvedPolicyRelationship( policyId, targetEnumList );
    
    policyRelationshipList.add( policyRelationship );
  }
  
  private void error( String message )
  {
    ServicePolicyActivator.logError( message, null );
  }
}
