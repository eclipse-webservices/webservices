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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.ws.service.policy.Descriptor;
import org.eclipse.wst.ws.service.policy.IStateEnumerationItem;
import org.eclipse.wst.ws.service.policy.ServicePolicyActivator;
import org.eclipse.wst.ws.service.policy.listeners.IPolicyPlatformLoadListener;
import org.eclipse.wst.ws.service.policy.utils.RegistryUtils;

public class ServicePolicyRegistry
{
  private final String SERVICE_POLICY_ID = ServicePolicyActivator.PLUGIN_ID + ".servicepolicy";
  
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
      String elementName = element.getName().toLowerCase();
      
      if( elementName.equals( "servicepolicy") )
      {
        loadServicePolicy( element, loadListeners, policyMap, enumMap, enumItemMap );
      }
      else
      {
        error( "Undefined service policy element, " + elementName + " found." );
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
      String childName = child.getName().toLowerCase();
      
      if( childName.equals( "loadlistener" ) )
      {
        loadLoadListener( child, loadListeners );
      }
      else if( childName.equals( "policy" ) )
      {
        loadPolicy( child, policyMap );
      }
      else if( childName.equals( "enumeration" ) )
      {
        loadEnumeration( child, enumMap, enumItemMap );
      }
      else
      {
        error( "Undefined service policy element, " + childName + " found." );        
      }
    }
  }
  
  private void loadEnumeration( IConfigurationElement                    element,
                                Map<String, List<IStateEnumerationItem>> enumMap,
                                Map<String, StateEnumerationItemImpl>    enumItemMap )
  {
    String                      enumId        = RegistryUtils.getAttribute( element, "id" );
    String                      defaultEnumId = RegistryUtils.getAttribute( element, "default" );
    IConfigurationElement[]     childElements = element.getChildren();
    List<IStateEnumerationItem> enumList      = new Vector<IStateEnumerationItem>();
    
    if( enumId == null )
    {
      error( "\"Id\" attribute missing from Serivce Policy Enumeration element." );
      return;
    }
    
    for( IConfigurationElement child : childElements )
    {
      String childName = child.getName().toLowerCase();
      
      if( childName.equals( "item" ) )
      {
        loadItem( child, enumList, enumItemMap );
      }
      else
      {
        error( "Undefined service policy enumeration element, " + childName + " found." );        
      } 
    }
    
    if( defaultEnumId != null )
    {
      StateEnumerationItemImpl defaultEnum = enumItemMap.get( defaultEnumId );
      
      if( defaultEnum == null )
      {
        error( "Default enum attribute for enumeration " + enumId + " not found." );
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
    String itemId    = RegistryUtils.getAttribute( element, "id" );
    String shortName = RegistryUtils.getAttribute( element, "shortname" );
    String longName  = RegistryUtils.getAttribute( element, "longname" );
    
    if( itemId == null )
    {
      error( "\"Id\" attribute missing from Serivce Policy Enumeration item element." );
    }
    
    if( shortName == null && longName == null )
    {
      error( "shortName or longName attribute missing from Service Policy Enumeration item element." );
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
      Object listener = element.createExecutableExtension( "class" );
      
      if( listener instanceof IPolicyPlatformLoadListener )
      {
        loadListeners.add( (IPolicyPlatformLoadListener)listener );
      }
      else
      {
        error( "Load listener, " + element.getAttribute( "class" ) + " does not implement IPolicyPlatformLoadListener" );
      }
    }
    catch( CoreException exc )
    {
      ServicePolicyActivator.logError( "Error loading service policy loadlistener.", exc );
    }
  }
  
  private void loadPolicy( IConfigurationElement policy, Map<String, ServicePolicyImpl> policyMap )
  {
    IConfigurationElement[]      policyElements = policy.getChildren();
    String                       parentId       = RegistryUtils.getAttribute( policy, "parentpolicyid" );
    String                       id             = RegistryUtils.getAttribute( policy, "id" );
    String                       enumListId     = RegistryUtils.getAttribute( policy, "enumlistid" );
    String                       defaultEnumId  = RegistryUtils.getAttribute( policy, "defaultenumid" );
    Descriptor                   descriptor     = null;
    List<UnresolvedRelationship> relationships  = new Vector<UnresolvedRelationship>();
    
    try
    {
      for( IConfigurationElement policyElement : policyElements )
      {
        String name = policyElement.getName().toLowerCase();
      
        if( name.equals( "descriptor") )
        {
          descriptor = RegistryUtils.loadDescriptor( policyElement );
        }
        else if( name.equals( "relationship" ) )
        {
          loadRelationship( policyElement, relationships );
        }
        else
        {
          error( "Undefined service policy element, " + name + " found." );                  
        }
      }
      
      ServicePolicyImpl newPolicy = new ServicePolicyImpl( true, id, platform );
      
      newPolicy.setUnresolvedParent( parentId );
      newPolicy.setUnresolvedRelationships( relationships );
      newPolicy.setDescriptor( descriptor );
      newPolicy.setEnumListId( enumListId );
      newPolicy.setDefaultEnumId( defaultEnumId );
      policyMap.put( id, newPolicy );
    }
    catch( IllegalArgumentException exc )
    {
      error( "An error was found loading a service policy." );
    }
  }
    
  private void loadRelationship( IConfigurationElement relationshipElement, List<UnresolvedRelationship> relationships )
  {
    String enumValueAttr = RegistryUtils.getAttribute( relationshipElement, "enumlist" );
    
    if( enumValueAttr == null )
    {
      error( "Service policy relationship missing attribute \"enumList\"." );
      throw new IllegalArgumentException();
    }
    
    List<String>                       sourceEnumList         = Arrays.asList( enumValueAttr.split( " " ) );
    IConfigurationElement[]            children               = relationshipElement.getChildren();
    List<UnresolvedPolicyRelationship> targetRelationshipList = new Vector<UnresolvedPolicyRelationship>();
    
    for( IConfigurationElement child : children )
    {
      String name = child.getName().toLowerCase();
      
      if( name.equals( "policy" ) )
      {
        loadPolicyRelationship( child, targetRelationshipList );
      }
      else
      {
        error( "Undefined service policy element, " + name + " found." );                  
      }
    }
    
    UnresolvedRelationship relationship = new UnresolvedRelationship( sourceEnumList, targetRelationshipList );
    
    relationships.add( relationship );
  }
  
  private void loadPolicyRelationship( IConfigurationElement policyElement,
                                       List<UnresolvedPolicyRelationship> policyRelationshipList )
  {
    String itemListAttr = RegistryUtils.getAttribute( policyElement, "itemlist" );
    String policyId      = RegistryUtils.getAttribute( policyElement, "id" );
    
    if( itemListAttr == null )
    {
      error( "Service policy relationship missing attribute \"enumList\"." );
      throw new IllegalArgumentException();
    }
    
    if( policyId == null )
    {
      error( "Service policy relationship missing attribute \"id\"." );
      throw new IllegalArgumentException();
    }
    
    List<String>                 targetEnumList     = Arrays.asList( itemListAttr.split( " " ) );
    UnresolvedPolicyRelationship policyRelationship = new UnresolvedPolicyRelationship( policyId, targetEnumList );
    
    policyRelationshipList.add( policyRelationship );
  }
  
  private void error( String message )
  {
    ServicePolicyActivator.logError( message, null );
  }
}
