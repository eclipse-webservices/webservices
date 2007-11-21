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
package org.eclipse.wst.ws.service.internal.policy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    this.policyState                = new PolicyStateImpl( this, null );
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
    
    //Restore children.
    children = new Vector<IServicePolicy>( committedChildren );
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
        platform.removePolicy( this );
        fireChildChangeEvent( policy, false );
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
      projectPolicyState = new PolicyStateImpl( this, project );
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
      newEnum = new EnumerationStateImpl( enumListId, defaultEnumId, policyState );
    }
    
    return newEnum;
  }
  
  public IPolicyStateEnum getPolicyStateEnum( IProject project )
  {
    EnumerationStateImpl newEnum = null;
    
    if( enumListId != null )
    {
      newEnum = new EnumerationStateImpl( enumListId, defaultEnumId, getPolicyState( project ) );
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
    for( IServicePolicy child : children )
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
    projectPolicyStates.get(project).restoreDefaults();
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
  
  private void fireChildChangeEvent( IServicePolicy policy, boolean isAdd )
  {
    for( IPolicyChildChangeListener listener : childChangeListeners )
    {
      listener.childChange( policy, isAdd );     
    }
  }
}
