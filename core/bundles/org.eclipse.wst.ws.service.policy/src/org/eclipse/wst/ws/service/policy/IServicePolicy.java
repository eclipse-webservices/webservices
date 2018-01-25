/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
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
 * 20080516   232603 pmoogk@ca.ibm.com - Peter Moogk, Clean up java doc
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.ws.service.policy.listeners.IPolicyChildChangeListener;
import org.eclipse.wst.ws.service.policy.listeners.IStatusChangeListener;

/**
 * 
 * This interface encapsulates the idea of a service policy
 *
 */
public interface IServicePolicy
{ 
  /**
   * 
   * Returns if this service policy is defined by an extension or not.
   * 
   * @return returns true if this Service policy is defined in plugin.xml 
   * meta data.  Otherwise, false is returned.
   */
  public boolean isPredefined();
  
  /**
   * 
   * Returns the unique ID for this service policy.
   * 
   * @return returns the unique ID for this service policy.
   */
  public String getId();
  
  /**
   * 
   * Returns the descriptor for this service policy.
   * 
   * @return returns the descriptor for this service policy.
   */
  public IDescriptor getDescriptor();
  
  /**
   * 
   * Returns a list of relationships to other IServicePolicy objects.
   *   
   * @return returns a list of relationships to other IServicePolicy objects.  
   *  
   */
  public List<IPolicyRelationship> getRelationships();

  /**
   * Sets the relationships for this service policy.
   * 
   * @param relationships
   */
  public void setRelationships( List<IPolicyRelationship> relationships );
  
  /**
   * Restores the policy to its original state.  All locally added
   * child policies will be removed.  The state of each policy will
   * be set to is default value.   Note: the changes made calling
   * this method can be undone if the platform performs a discardChanges
   * operation.  Alternatively, if the plaform performs a commitChanges
   * operation these changes will be saved.
   */
  public void restoreDefaults();
  
  /**
   * Restores the policy to its original state for a particular project.  
   * The state of each policy will be set to is default value.   
   * Note: the changes made calling
   * this method can be undone if the platform performs a discardChanges
   * operation.  Alternatively, if the plaform performs a commitChanges
   * operation these changes will be saved.
   */
  public void restoreDefaults( IProject project );
  
  /**
   * 
   * Returns the policy state for this service policy.
   * 
   * @return returns the policy state for this service policy.
   */
  public IPolicyState getPolicyState();
  
  /**
   * 
   * Returns the IPolicyState for a particular project.
   * 
   * @param project the project.
   * @return The IPolicyState for a particular project.
   */
  public IPolicyState getPolicyState( IProject project );
  
  /**
   * 
   * Returns the policy state enumeration object.  Note: this is just
   * a wrapper around the policy state object.
   * 
   * @return returns the state enumeration object for this service
   * policy.  If this policy is not associated with an enumeration
   * null will be returned.
   */
  public IPolicyStateEnum getPolicyStateEnum();
  
  /**
   * 
   * Returns the policy state enumeration object for a particular project.  
   * Note: this is just a wrapper around the policy state object.
   * 
   * @param project the project
   * @return returns the state enumeration for a particular project.  If
   * no state enumeration is associated with the service policy
   * null will be returned.
   */
  public IPolicyStateEnum getPolicyStateEnum( IProject project );
  
  /**
   * 
   * Returns the parent service policy object.
   * 
   * @return returns the parent policy for this service policy.  If this
   * service policy has no parent then null is returned.
   */
  public IServicePolicy getParentPolicy();
  
  /**
   * 
   * @return returns the child service policies for this service policy.  Changes
   * made to the returned list do not change the underlying number of children
   * for this service policy.
   */
  public List<IServicePolicy> getChildren();
  
  /**
   * Removes a child service policy from this service policy.  If the policy specified
   * is null or is not a child of this service policy this method will have no effect.
   * Also, if this service policy is predefined calling this method will have
   * no effect.
   * @param policy
   */
  public void removeChild( IServicePolicy policy );
  
  /**
   * Adds a child listener to this service policy.
   * 
   * @param listener the listener
   */
  public void addPolicyChildChangeListener( IPolicyChildChangeListener listener );
  
  /**
   * Removes a child listener from this service policy.
   * @param listener
   */
  public void removePolicyChildChangeListener( IPolicyChildChangeListener listener );
  
  /**
   * Adds a status change listener to this service policy. When the status changes
   * this listener will be notified.
   *  
   * @param listener
   */
  public void addStatusChangeListener( IStatusChangeListener listener );
  
  /**
   * Removes a status change listener from this service policy.
   * 
   * @param listener
   */
  public void removeStatusChangeListener( IStatusChangeListener listener );
  
  /**
   * 
   * Returns the status for this service policy.
   * 
   * @return returns the status for this service policy.
   */
  public IStatus getStatus();
  
  /**
   * Sets the status for this service policy.
   * 
   * @param status
   */
  public void setStatus( IStatus status );
}
