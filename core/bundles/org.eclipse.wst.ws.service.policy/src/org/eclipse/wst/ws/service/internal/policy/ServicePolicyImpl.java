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
 * 20080214   218996 pmoogk@ca.ibm.com - Peter Moogk, Concurrent exception fix
 * 20080625   238482 pmoogk@ca.ibm.com - Peter Moogk, Adding thread safety to the service platform api.
 *******************************************************************************/
package org.eclipse.wst.ws.service.internal.policy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.ws.service.policy.IDescriptor;
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
  private DescriptorImpl                   descriptor;
  private List<IServicePolicy>             committedChildren;
  private List<IServicePolicy>             children;
  private PolicyStateImpl                  policyState;
  private Map<IProject, PolicyStateImpl>   projectPolicyStates;
  private List<IPolicyRelationship>        relationshipList;
  private List<IPolicyChildChangeListener> childChangeListeners;
  private ServicePolicyPlatformImpl        platform;
  private String                           enumListId;
  private String                           defaultEnumId;
  private List<IStatusChangeListener>      statusChangeListeners; 
  private IStatus                          status;
  
  private String                           unresolvedParent;
  private List<UnresolvedRelationship>     unresolvedRelationshipList;
  
  public ServicePolicyImpl( boolean isPredefined, String id, ServicePolicyPlatformImpl platform )
  {
    this.predefined                 = isPredefined;  
    this.id                         = id;
    this.children                   = new Vector<IServicePolicy>();
    this.relationshipList           = new Vector<IPolicyRelationship>();
    this.unresolvedRelationshipList = new Vector<UnresolvedRelationship>();
    this.policyState                = new PolicyStateImpl( platform.getApiPlatform(), this, null );
    this.platform                   = platform;  
    this.childChangeListeners       = new Vector<IPolicyChildChangeListener>();
    this.projectPolicyStates        = new HashMap<IProject, PolicyStateImpl>();
    this.status                     = Status.OK_STATUS;
  }
  
  public boolean isPredefined()
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
  
  public void commitChanges()
  {
    policyState.commitChanges();
    
    // Make a copy of the children in the committedChildren object.
    committedChildren = new Vector<IServicePolicy>( children );
  }
  
  public void discardChanges()
  {
    policyState.discardChanges();
        
    fireChildChangesDuetoDiscard();
    
    //Restore children.
    children = new Vector<IServicePolicy>( committedChildren );
  }
  
  public List<IServicePolicy> getChildren()
  {
    return children;
  }
  
  private void addChild( ServicePolicyImpl child, boolean fireEvent )
  {
    children.add( child );
      
    if( fireEvent )
    {
      fireChildChangeEvent( child, true );
    }
  }
  
  public void removeChild(IServicePolicy policyToRemove )
  {
    if( !policyToRemove.isPredefined() )
    {
      // Remove all the children of this policy first.
      List<IServicePolicy> childPolicies = new Vector<IServicePolicy>( policyToRemove .getChildren() );
      
      for( IServicePolicy childPolicy : childPolicies )
      {
        policyToRemove.removeChild( childPolicy );
      }
      
      boolean removed = children.remove( policyToRemove  );
      
      if( removed )
      {
        platform.removePolicy( policyToRemove );
        fireChildChangeEvent( policyToRemove , false );
      }
    }
  }

  public IDescriptor getDescriptor()
  {
    if( descriptor == null )
    {
      descriptor = new DescriptorImpl();  
    }
    
    return descriptor;
  }
  
  public void setDescriptor( DescriptorImpl descriptor )
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
    PolicyStateImpl projectPolicyState = projectPolicyStates.get( project );
    
    if( projectPolicyState == null )
    {
      projectPolicyState = new PolicyStateImpl( platform.getApiPlatform(), this, project );
      projectPolicyState.internalSetMutable( policyState.isMutable() );
      projectPolicyStates.put( project, projectPolicyState );
    }
    
    return projectPolicyState;
  }
  
  public IPolicyStateEnum getPolicyStateEnum()
  {
    EnumerationStateImpl newEnum = null;
    
    if( enumListId != null )
    {
      newEnum = new EnumerationStateImpl( platform.getApiPlatform(), enumListId, defaultEnumId, policyState );
    }
    
    return newEnum;
  }
  
  public IPolicyStateEnum getPolicyStateEnum( IProject project )
  {
    EnumerationStateImpl newEnum = null;
    
    if( enumListId != null )
    {
      newEnum = new EnumerationStateImpl( platform.getApiPlatform(), enumListId, defaultEnumId, getPolicyState( project ) );
    }
    
    return newEnum;
  }

  public void setPolicyState( PolicyStateImpl policyState )
  {
    this.policyState = policyState;
  }
  
  public void restoreDefaults()
  {
    // Remove all local children from the tree
    // Java will not let you remove an item from a collection
    // that is being iterated over, so we need to create a temporary
    // copy of the children collection to iterator over.
    Vector<IServicePolicy> tempChildren = new Vector<IServicePolicy>( children );
    
    for( IServicePolicy child : tempChildren )
    {
      if( !child.isPredefined() )
      {
        removeChild( child );
      }
    }
    
    policyState.restoreDefaults();
  }
  
  public void restoreDefaults( IProject project )
  {
    PolicyStateImpl stateImpl = projectPolicyStates.get( project );
    
    if( stateImpl != null )
    {
      stateImpl.restoreDefaults();
    }
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

  public String getEnumListId()
  {
    return enumListId;  
  }
  
  public void setEnumListId(String enumListId)
  {
    this.enumListId = enumListId;
  }

  public String getDefaultEnumId()
  {
    return defaultEnumId;  
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
          ServicePolicyActivator.logError( "Policy id, " + targetEnum.getPolicyId() + " not found.", null );                               //$NON-NLS-1$ //$NON-NLS-2$
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
    if( statusChangeListeners == null )
    {
      statusChangeListeners = new Vector<IStatusChangeListener>();    
    }
    
    statusChangeListeners.add( listener );
  }
  
  public void removeStatusChangeListener( IStatusChangeListener listener )
  {
    if( statusChangeListeners != null )
    {
      statusChangeListeners.remove( listener );
    }
  }
  
  public IStatus getStatus()
  {
    return status;
  }
  
  public void setStatus( IStatus status )
  {
    IStatus oldStatus = this.status;
    this.status = status;
    fireStatusChangeEvent( oldStatus, status );
  }
  
  private void fireStatusChangeEvent( IStatus oldStatus, IStatus newStatus )
  {
    if( statusChangeListeners != null )
    {
      for( IStatusChangeListener listener : statusChangeListeners )
      {
        listener.statusChange( this, oldStatus, newStatus);
      }
    }
  }
  
  private void fireChildChangesDuetoDiscard()
  {
    Set<IServicePolicy> childSet = new HashSet<IServicePolicy>( children );
    Set<IServicePolicy> committedChildSet = new HashSet<IServicePolicy>( committedChildren );
    
    for( IServicePolicy child : childSet )
    {
      if( committedChildSet.contains( child ) )
      {
        committedChildSet.remove( child );
      }
      else
      {
        // A child was added and is now being deleted by the discard.
        fireChildChangeEvent( child, false );
        platform.fireChildChangeEvent( child, false );
      }
    }
    
    // Any children left in the committed set must have been deleted and are now
    // being added back due to the discard.
    for( IServicePolicy child : committedChildSet )
    {
      fireChildChangeEvent( child, true );
      platform.fireChildChangeEvent( child, true );
    }
  }
  
  private void fireChildChangeEvent( IServicePolicy policy, boolean isAdd )
  {
    for( IPolicyChildChangeListener listener : childChangeListeners )
    {
      List<IServicePolicy> policyList = new Vector<IServicePolicy>(1);
      List<Boolean>        addedList  = new Vector<Boolean>(1);
      
      policyList.add( policy );
      addedList.add( isAdd );
      listener.childChange( policyList, addedList );     
    }
  }
}
