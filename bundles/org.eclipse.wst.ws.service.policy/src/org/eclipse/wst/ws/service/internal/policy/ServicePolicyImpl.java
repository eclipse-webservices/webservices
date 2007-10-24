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
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.ws.service.policy.Descriptor;
import org.eclipse.wst.ws.service.policy.IPolicyEnumerationList;
import org.eclipse.wst.ws.service.policy.IPolicyRelationship;
import org.eclipse.wst.ws.service.policy.IPolicyState;
import org.eclipse.wst.ws.service.policy.IPolicyStateEnum;
import org.eclipse.wst.ws.service.policy.IServicePolicy;
import org.eclipse.wst.ws.service.policy.IStateEnumerationItem;
import org.eclipse.wst.ws.service.policy.PolicyEnumerationListImpl;
import org.eclipse.wst.ws.service.policy.PolicyRelationshipImpl;
import org.eclipse.wst.ws.service.policy.ServicePolicyActivator;
import org.eclipse.wst.ws.service.policy.listeners.IPolicyChildChangeListener;
import org.eclipse.wst.ws.service.policy.listeners.IStatusChangeListener;

public class ServicePolicyImpl implements IServicePolicy
{
  private boolean                          predefined;
  private String                           id;
  private ServicePolicyImpl                parent;
  private Descriptor                       descriptor;
  private List<IServicePolicy>             children;
  private PolicyStateImpl                  policyState;
  private List<IPolicyRelationship>        relationshipList;
  private List<IPolicyChildChangeListener> childChangeListeners;
  private ServicePolicyPlatformImpl        platform;
  private String                           enumListId;
  private String                           defaultEnumId;
  
  private String                           unresolvedParent;
  private List<UnresolvedRelationship>     unresolvedRelationshipList;
  
  public ServicePolicyImpl( boolean isPredefined, String id, ServicePolicyPlatformImpl platform )
  {
    this.predefined                 = isPredefined;  
    this.id                         = id;
    this.children                   = new Vector<IServicePolicy>();
    this.relationshipList           = new Vector<IPolicyRelationship>();
    this.unresolvedRelationshipList = new Vector<UnresolvedRelationship>();
    this.policyState                = new PolicyStateImpl();
    this.platform                   = platform;    
  }
  
  public boolean getPredefined()
  {
    return predefined;
  }

  public String getId()
  {
    return id;  
  }
  
  public void setParent( ServicePolicyImpl parent )
  {
    this.parent = parent;
    
    if( parent != null )
    {
      parent.addChild( this, false );
    }
  }
  
  public List<IServicePolicy> getChildren()
  {
    return children;
  }
  
  public void addChild( ServicePolicyImpl child, boolean fireEvent )
  {
    children.add( child );
      
    if( fireEvent )
    {
      fireChildChangeEvent( child, true );
    }
  }
  
  public void removeChild(IServicePolicy policy)
  {
    if( !predefined )
    {
      boolean removed = children.remove( policy );
      
      if( removed )
      {
        fireChildChangeEvent( policy, false );
      }
    }
  }

  public Descriptor getDescriptor()
  {
    return descriptor;
  }
  
  public void setDescriptor( Descriptor descriptor )
  {
    this.descriptor = descriptor;
  }

  public IServicePolicy getParentPolicy()
  {
    return parent;
  }

  public IPolicyState getPolicyState()
  {
    return policyState;
  }
  
  public IPolicyState getPolicyState( IProject project )
  {
    // TODO
    return null;
  }
  
  public IPolicyStateEnum getPolicyStateEnum()
  {
    return new EnumerationStateImpl( enumListId, defaultEnumId, policyState );
  }
  
  public IPolicyStateEnum getPolicyStateEnum( IProject project )
  {
    //TODO
    return null;
  }

  public void setPolicyState( PolicyStateImpl policyState )
  {
    this.policyState = policyState;
  }
  
  public List<IPolicyRelationship> getRelationships()
  {
    return relationshipList;
  }
  
  public void setRelationships(List<IPolicyRelationship> relationships)
  {
    if( !predefined )
    {
      this.relationshipList = relationships;
    }
  }

  public void setEnumListId(String enumListId)
  {
    this.enumListId = enumListId;
  }

  public void setDefaultEnumId(String defaultEnumValue)
  {
    this.defaultEnumId = defaultEnumValue;
  }

  public void setUnresolvedParent( String parentId )
  {
    unresolvedParent = parentId;  
  }
    
  public void setUnresolvedRelationships( List<UnresolvedRelationship> relationships )
  {
    unresolvedRelationshipList = relationships;
  }
  
  public void resolve()
  {
    setParent( platform.getServicePolicy( unresolvedParent ) );
        
    for( UnresolvedRelationship relationship : unresolvedRelationshipList )
    {
      List<String>                       sourceEnumIdList = relationship.getSourceEnumerationList(); 
      List<UnresolvedPolicyRelationship> targetEnumIdList = relationship.getTargetEnumerationList();
      List<IStateEnumerationItem>        sourceEnumList   = getResolvedEnumList( sourceEnumIdList );
      IPolicyEnumerationList             sourcePolicyList = new PolicyEnumerationListImpl( sourceEnumList, this );
      List<IPolicyEnumerationList>       targetPolicyList = new Vector<IPolicyEnumerationList>();
      
      for( UnresolvedPolicyRelationship targetEnum : targetEnumIdList )
      {
        IServicePolicy              targetPolicy         = platform.getServicePolicy( targetEnum.getPolicyId() );
        List<IStateEnumerationItem> targetList           = getResolvedEnumList( targetEnum.getEnumList());
        IPolicyEnumerationList      targetEnumPolicyList = new PolicyEnumerationListImpl( targetList, targetPolicy );
        
        targetPolicyList.add( targetEnumPolicyList );
        
        if( targetPolicy == null )
        {
          ServicePolicyActivator.logError( "Policy id, " + targetEnum.getPolicyId() + " not found.", null );                              
        }
      }
      
      IPolicyRelationship policyRelationship = new PolicyRelationshipImpl( sourcePolicyList, targetPolicyList );
      
      relationshipList.add( policyRelationship );
    }
        
    unresolvedParent = null;
    unresolvedRelationshipList = null;
  }
  
  private List<IStateEnumerationItem> getResolvedEnumList( List<String> enumIdList )
  {
    List<IStateEnumerationItem> enumList = new Vector<IStateEnumerationItem>();
    
    for( String enumId : enumIdList )
    {
      enumList.add( platform.getStateItemEnumeration( enumId ) );    
    }
    
    return enumList;
  }
  
  public void addPolicyChildChangeListener(IPolicyChildChangeListener listener)
  {
    childChangeListeners.add( listener );
  }
  
  public void removePolicyChildChangeListener(IPolicyChildChangeListener listener)
  {
    childChangeListeners.remove( listener );
  }
  
  public void addStatusChangeListener( IStatusChangeListener listener )
  {
    //TODO
  }
  
  public void removeStatusChangeListener( IStatusChangeListener listener )
  {
    //TODO
  }
  
  public IStatus getStatus()
  {
    return null;
    //TODO
  }
  
  public void setStatus( IStatus status )
  {
    //TODO
  }

  private void fireChildChangeEvent( IServicePolicy policy, boolean isAdd )
  {
    for( IPolicyChildChangeListener listener : childChangeListeners )
    {
      listener.childChange( policy, isAdd );     
    }
  }
}