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
 * 20071030   196997 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.wst.ws.service.internal.policy;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.wst.ws.service.policy.IDescriptor;
import org.eclipse.wst.ws.service.policy.IPolicyEnumerationList;
import org.eclipse.wst.ws.service.policy.IPolicyRelationship;
import org.eclipse.wst.ws.service.policy.IServicePolicy;
import org.eclipse.wst.ws.service.policy.IStateEnumerationItem;
import org.eclipse.wst.ws.service.policy.PolicyEnumerationListImpl;
import org.eclipse.wst.ws.service.policy.PolicyRelationshipImpl;
import org.eclipse.wst.ws.service.policy.ServicePolicyActivator;
import org.osgi.service.prefs.BackingStoreException;

public class LocalUtils
{
  private static final String BASE_KEY      = "org.eclipse.wst."; //$NON-NLS-1$
  private static final String LOCAL_IDS_KEY = "localIds"; //$NON-NLS-1$
  private static final String PARENT_KEY    = "parentId"; //$NON-NLS-1$
  private static final String MUTABLE_KEY   = "mutable"; //$NON-NLS-1$
  private static final String DESCR_KEY     = "description"; //$NON-NLS-1$
  private static final String LONGNAME_KEY  = "longname"; //$NON-NLS-1$
  private static final String SHORTNAME_KEY = "shortname"; //$NON-NLS-1$
  private static final String ICONPATH_KEY  = "iconpath"; //$NON-NLS-1$
  private static final String ICONBUND_KEY  = "iconbundleid"; //$NON-NLS-1$
  private static final String HELPID_KEY    = "contexthelpid"; //$NON-NLS-1$
  private static final String ENUMLIST_KEY  = "enumlistid"; //$NON-NLS-1$
  private static final String DEFENUM_KEY   = "defaultenumid"; //$NON-NLS-1$
  private static final String REL_KEY       = "rel"; //$NON-NLS-1$
  private static final String TARGET_SUFFIX = ".target"; //$NON-NLS-1$
  private static final String SOURCE_SUFFIX = ".source"; //$NON-NLS-1$
  
  public static List<String> getLocalPolicyIds()
  {
    List<String>        result      = new Vector<String>();
    IEclipsePreferences preferences = getPreferences();
    String              key         = BASE_KEY + LOCAL_IDS_KEY;
    String              idsValue    = preferences.get( key , "" ); //$NON-NLS-1$
    
    if( !idsValue.equals( "" ) ) //$NON-NLS-1$
    {
      result = createStringList( idsValue );
    }
    
    return result;
  }
  
  public static void saveLocalIds( List<String> localIds )
  { 
    IEclipsePreferences preferences = getPreferences();
    String              key         = BASE_KEY + LOCAL_IDS_KEY;
    
    if( localIds.size() > 0 )
    {
      preferences.put( key, createSpaceDelimitedString( localIds ) );
    }
  }
  
  public static void removeAllLocalPolicies()
  {
    try
    {
      IEclipsePreferences preferences = getPreferences();
      String[]            keys        = preferences.keys();
      
      for( String key : keys )
      {
        if( key.startsWith( BASE_KEY ) )
        {
          preferences.remove( key );
        }
      }
    }
    catch( BackingStoreException exc )
    {
      ServicePolicyActivator.logError( "Error loading service policy local preferences.", exc); //$NON-NLS-1$
    }
  }
  
  public static void saveLocalPolicy( ServicePolicyImpl policy )
  {
    IEclipsePreferences preferences = getPreferences();
    String              policyId    = policy.getId();
    IDescriptor         descriptor  = policy.getDescriptor();
    IServicePolicy      parent      = policy.getParentPolicy();
    
    save( preferences, policyId, PARENT_KEY,  parent == null ? null : parent.getId() );
    save( preferences, policyId, MUTABLE_KEY, "" + policy.getPolicyState().isMutable() ); //$NON-NLS-1$
    save( preferences, policyId, ENUMLIST_KEY, policy.getEnumListId() );
    save( preferences, policyId, DEFENUM_KEY, policy.getDefaultEnumId() );
    save( preferences, policyId, DESCR_KEY, descriptor.getDescription() );
    save( preferences, policyId, SHORTNAME_KEY, descriptor.getShortName() );
    save( preferences, policyId, LONGNAME_KEY, descriptor.getLongName() );
    save( preferences, policyId, ICONPATH_KEY, descriptor.getIconPath() );
    save( preferences, policyId, ICONBUND_KEY, descriptor.getIconBundleId() );
    save( preferences, policyId, HELPID_KEY, descriptor.getContextHelpId() );
    saveRelationships( preferences, policyId, policy.getRelationships() );
  }
  
  public static ServicePolicyImpl loadLocalPolicy( String policyId, ServicePolicyPlatformImpl platform )
  {
    IEclipsePreferences preferences = getPreferences();
    ServicePolicyImpl   policy      = new ServicePolicyImpl( false, policyId, platform );
    DescriptorImpl      descriptor  = (DescriptorImpl)policy.getDescriptor();
    String              parentId    = preferences.get( createKey( policyId, PARENT_KEY), null );
    
    if( parentId != null )
    {
      IServicePolicy parentPolicy = platform.getServicePolicy( parentId );
      
      policy.setParent( (ServicePolicyImpl)parentPolicy );
    }
    
    policy.getPolicyState().setMutable( preferences.getBoolean( createKey( policyId, MUTABLE_KEY), true ) );
    policy.setEnumListId( preferences.get( createKey( policyId, ENUMLIST_KEY), null ) );
    policy.setDefaultEnumId( preferences.get( createKey( policyId, DEFENUM_KEY), null ) );
    descriptor.setDescription( preferences.get( createKey( policyId, DESCR_KEY), null ) );
    descriptor.setShortName( preferences.get( createKey( policyId, SHORTNAME_KEY), null ) );
    descriptor.setLongName( preferences.get( createKey( policyId, LONGNAME_KEY), null ) );
    descriptor.setIconPath( preferences.get( createKey( policyId, ICONPATH_KEY), null ) );
    descriptor.setIconBundleId( preferences.get( createKey( policyId, ICONBUND_KEY), null ) );
    descriptor.setContextHelpId( preferences.get( createKey( policyId, HELPID_KEY), null ) );
    policy.setRelationships( loadRelationships( preferences, policyId, platform ) );
    
    return policy;
  }
  
  private static IEclipsePreferences getPreferences()
  {
    return new InstanceScope().getNode( ServicePolicyActivator.PLUGIN_ID );    
  }
  
  private static List<String> createStringList( String spaceDelimitedString )
  {
    String[] splitValues = spaceDelimitedString.split( " " ); //$NON-NLS-1$
    
    return Arrays.asList( splitValues );
  }
  
  private static String createSpaceDelimitedString( List<String> stringList )
  {
    StringBuffer buffer = new StringBuffer();
    
    for( int index = 0; index < stringList.size(); index++ )
    {
      String id = stringList.get( index );
      
      buffer.append( id );
      
      if( index < (stringList.size() - 1) ) buffer.append( ' ' );
    }

    return buffer.toString();
  }
  
  private static void saveRelationships( IEclipsePreferences preferences, String policyId, List<IPolicyRelationship> relationships )
  {
    for( int index = 0; index < relationships.size(); index++ )
    {
      IPolicyRelationship          relationship   = relationships.get( index );
      IPolicyEnumerationList       policyList     = relationship.getPolicyEnumerationList();
      List<IPolicyEnumerationList> targetPolicies = relationship.getRelatedPolicies();
      
      savePolicyRelationship( index+1, preferences, policyId, policyList, SOURCE_SUFFIX );
      
      for( int targetIndex = 0; targetIndex < targetPolicies.size(); targetIndex++ )
      {
        IPolicyEnumerationList targetPolicy = targetPolicies.get( targetIndex );
        String                 targetSuffix = TARGET_SUFFIX + (targetIndex+1);
        
        savePolicyRelationship( index+1, preferences, policyId, targetPolicy, targetSuffix );
      }
    }
  }
  
  private static List<IPolicyRelationship> loadRelationships( IEclipsePreferences preferences, String policyId, ServicePolicyPlatformImpl platform )
  {
    List<IPolicyRelationship> relationships = new Vector<IPolicyRelationship>();
    int                       relCount      = 1;
    IPolicyEnumerationList    sourceList    = loadPolicyRelationship( relCount, preferences, platform, policyId, SOURCE_SUFFIX );
    
    while( sourceList != null )
    {
      int                          targetCount  = 1;
      String                       targetSuffix = TARGET_SUFFIX + targetCount;
      IPolicyEnumerationList       targetList   = loadPolicyRelationship( relCount, preferences, platform, policyId, targetSuffix );
      List<IPolicyEnumerationList> targets      = new Vector<IPolicyEnumerationList>();
      
      while( targetList != null )
      {
        targets.add( targetList );
        targetCount++;
        targetSuffix = TARGET_SUFFIX + targetCount;
        targetList = loadPolicyRelationship( relCount, preferences, platform, policyId, targetSuffix );
      }
      
      relationships.add( new PolicyRelationshipImpl( sourceList, targets ) );
      relCount++;
      sourceList = loadPolicyRelationship( relCount, preferences, platform, policyId, SOURCE_SUFFIX );
    }
    
    return relationships;
  }
  
  private static void savePolicyRelationship( int                    relCount, 
                                              IEclipsePreferences    preferences, 
                                              String                 policyId,
                                              IPolicyEnumerationList enumList,
                                              String                 keySuffix )
  {
    String key   = createKey( policyId, REL_KEY + relCount + keySuffix );
    String value = createRelationshipValue( enumList );
    
    preferences.put( key, value );
  }
  
  private static IPolicyEnumerationList loadPolicyRelationship( int                       relCount,
                                                                IEclipsePreferences       preferences,
                                                                ServicePolicyPlatformImpl platform,
                                                                String                    policyId,
                                                                String                    keySuffix )
  {
    String                 key      = createKey( policyId, REL_KEY + relCount + keySuffix );
    String                 value    = preferences.get( key, "" ); //$NON-NLS-1$
    IPolicyEnumerationList enumList = null;
    
    if( !value.equals( "" ) ) //$NON-NLS-1$
    {
      String[]                    splitValue     = value.split( " " ); //$NON-NLS-1$
      String                      targetPolicyId = splitValue[0];
      IServicePolicy              targetPolicy   = platform.getServicePolicy( targetPolicyId );
      List<IStateEnumerationItem> items          = new Vector<IStateEnumerationItem>();
      
      for( int index = 1; index < splitValue.length; index++ )
      {
        IStateEnumerationItem item = platform.getStateItemEnumeration( splitValue[index] );
        
        items.add( item );
      }
      
      enumList = new PolicyEnumerationListImpl( items, targetPolicy );
    }
    
    return enumList;
  }
  
  /**
   * This method will create a space delimited string of enumerations id.
   * The first id in the list will be the policy id for this relationship.
   * Subsequent ids will be enumeration item ids.
   * 
   * @param enumList
   * @return
   */
  private static String createRelationshipValue( IPolicyEnumerationList enumList )
  {
    List<String>                stringList = new Vector<String>();
    List<IStateEnumerationItem> itemList   = enumList.getEnumerationList();
    
    stringList.add( enumList.getPolicy().getId() );
    
    for( IStateEnumerationItem item : itemList )
    {
      stringList.add( item.getId() );  
    }
    
    return createSpaceDelimitedString( stringList );
  }
  
  private static void save( IEclipsePreferences preferences, String policyId, String key, String value )
  {
    if( value != null && !value.equals( "" ) ) //$NON-NLS-1$
    {
      preferences.put( createKey(policyId,key), value );      
    }
  }
  
  private static String createKey( String id, String key )
  {
    StringBuffer buffer = new StringBuffer(50);
    
    buffer.append( BASE_KEY );
    buffer.append( id );
    buffer.append( '.' );
    buffer.append( key );
    
    return buffer.toString();
  }
}
