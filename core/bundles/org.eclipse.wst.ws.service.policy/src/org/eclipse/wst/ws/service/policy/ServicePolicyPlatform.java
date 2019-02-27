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
 * 20080325   222095 pmoogk@ca.ibm.com - Peter Moogk
 * 20080516   232603 pmoogk@ca.ibm.com - Peter Moogk, Clean up java doc
 * 20080625   238482 pmoogk@ca.ibm.com - Peter Moogk, Adding thread safety to the service platform api.
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy;

import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.wst.ws.service.internal.policy.ServicePolicyPlatformImpl;
import org.eclipse.wst.ws.service.policy.listeners.IPolicyChildChangeListener;
import org.eclipse.wst.ws.service.policy.listeners.IPolicyPlatformProjectLoadListener;

/**
 * 
 * This class is used to access and add service policies to the platform.
 *
 */
public class ServicePolicyPlatform
{
  private static ServicePolicyPlatform instancePlatform = null;
  private ServicePolicyPlatformImpl    platformImpl;
  
  private ServicePolicyPlatform()
  {
  }
  
  /**
   * 
   * Returns a singleton instance of this service policy platform.
   * 
   * @return returns a singleton instance of this service policy platform.
   */
  public static synchronized ServicePolicyPlatform getInstance()
  {
    if( instancePlatform == null )
    {
      instancePlatform              = new ServicePolicyPlatform();     
      instancePlatform.platformImpl = new ServicePolicyPlatformImpl( instancePlatform );
      instancePlatform.platformImpl.load();
      instancePlatform.platformImpl.callLoadListeners();
    }
    
    return instancePlatform;
  }
  
  /**
   * Indicates if the property page for a project should be displayed in a popup
   * menu or not.
   * 
   * @param object
   * @return returns true if this platform is enabled for specified object.  In most
   * cases the specified object will be an IProject.  This method is usually called
   * to determine if a the service policy entry should appear for a particular project.
   */
  public boolean isEnabled( Object object )
  {
    return platformImpl.isEnabled( object );  
  }
  
  /**
   * Calling this method will commit all the state changes that were made to all
   * the service policies.  The method only applies to state changes at the workspace
   * level.
   */
  public synchronized void commitChanges()
  {
    platformImpl.commitChanges( true );
  }
  
  /**
   * Calling this method will discard all the state changes that were made to all
   * the service policies.  The method only applies to state changes at the workspace
   * level.
   */
  public synchronized void discardChanges()
  {
    platformImpl.discardChanges(); 
  }
  
  /**
   * Calling this method will commit all the state changes that were made to all
   * the service policies for a particular project.  The method only applies to 
   * state changes at the project level.
   */
  public synchronized void commitChanges( IProject project )
  {
    platformImpl.commitChanges( project );
  }
  
  /**
   * Calling this method will discard all the state changes that were made to all
   * the service policies for a particular project.  The method only applies to 
   * state changes at the project level.
   */
  public synchronized void discardChanges( IProject project )
  { 
    platformImpl.discardChanges( project ); 
  }
  
  /**
   * 
   * Returns all service policy IDs that are defined.
   * 
   * @return returns all service policy IDs that are defined.
   */
  public Set<String> getAllPolicyIds()
  {
    return platformImpl.getAllPolicyIds();
  }
  
  /**
   * 
   * Returns the list of root policies for the platform given a filter.
   * 
   * @param filter a filter a the service policies.  This method may be null if
   * no filter is required.
   * @return returns all root level service policies.  If a filter is specified
   * some service policies may be removed from the returned list.  A root
   * level service policy is defined to be a service policy with no parent.
   * 
   */
  public List<IServicePolicy> getRootServicePolicies( IFilter filter )
  {
    return platformImpl.getRootServicePolicies( filter );
  }
  
  /**
   * 
   * Returns a service policy given it's unique ID.
   * 
   * @param id
   * @return returns a service policy given it's unique ID.
   */
  public IServicePolicy getServicePolicy( String id )
  {
    return platformImpl.getServicePolicy( id );  
  }
  
  /**
   * 
   * Returns if the particular project has been enabled or not on it's
   * service policy property page.
   * 
   * @param project
   * @return returns true if the specified project has been enabled for
   * project specific policy preferences.
   */
  public boolean isProjectPreferencesEnabled( IProject project )
  {
    return platformImpl.isProjectPreferencesEnabled( project );
  }
  
  /**
   * Sets whether a project specific service policy preferences is enabled or not.
   * 
   * @param project
   * @param value
   */
  public synchronized void setProjectPreferencesEnabled( IProject project, boolean value )
  {
    platformImpl.setProjectPreferencesEnabled( project, value ); 
  }
  
  /**
   * Restores the workspace level defaults.  Note: the state changes made by 
   * calling this method need to be committed or discarded by the platform.
   */
  public synchronized void restoreDefaults()
  {
    platformImpl.restoreDefaults();
  }
  
  /**
   * 
   * Restores the project level defaults.  Note: the state changes made by 
   * calling this method need to be committed or discarded by the platform.
   * 
   * @param project
   */
  public synchronized void restoreDefaults( IProject project )
  {
    platformImpl.restoreDefaults( project );
  }
  
  /**
   * This method creates an IServicePolicy object.
   * 
   * @param parent the parent policy for this policy.  If this policy has no 
   * parent null may be specified.
   * @param id This is a unique id for this service policy.  If the id specified
   * is not unique trailing numerical digits in the id will be stripped off.  
   * Numerical digits will then be added to end of the id to make it unique.
   * If the id is empty or null the framework will assign a unique id.
   * @param enumListId If this policy's state is defined by an enumeration
   * the enumeration id should be specified here.  Otherwise null should be
   * specified.
   * @param defaultEnumId If this policy's state is defined by an enumeration
   * this parameter specifies the default value.  This value may be null 
   * if not using an enumeration or if the default value of the enumeration
   * should be used.
   * @return returns a service policy object.
   */
  public synchronized IServicePolicy createServicePolicy( IServicePolicy parent, String id, String enumListId, String defaultEnumId )
  {
    return platformImpl.createServicePolicy( parent, id, enumListId, defaultEnumId );
  }
  
  /**
   * Removes a service policy from the service policy platform.
   * 
   * @param policy the service policy
   */
  public synchronized void removeServicePolicy( IServicePolicy policy )
  {
    platformImpl.removePlatformPolicy( policy );
  }
  
  /**
   * This method allows calls to listener to any child change that is
   * made in the service policy platform regardless of where it is 
   * located in the tree of service policies.
   * 
   * @param listener
   * @param onCommit indicates whether this listener should be invoked when
   * the the platform changes are committed.
   */
  public synchronized void addChildChangeListener( IPolicyChildChangeListener listener, boolean onCommit )
  {
    platformImpl.addChildChangeListener( listener, onCommit );  
  }
  
  /**
   * Indicates whether child change listeners should be queued or not.  If
   * they are queued then child change events will be queue up until this
   * method is called with a false parameter.  When this happens the child
   * change event listeners are noticed of add the events that have occurred.
   * 
   * @param queue
   */
  public synchronized void queueChildChangeListeners( boolean queue )
  {
    platformImpl.queueChildChangeListeners( queue );
  }
  
  /**
   * Removes a child change listener from the service policy platform.
   * 
   * @param listener
   * @param onCommit indicates whether this change listener should be removed
   * from the onCommit list.
   */
  public synchronized void removeChildChangeListener( IPolicyChildChangeListener listener, boolean onCommit )
  {
    platformImpl.removeChildChangeListener( listener, onCommit );   
  }
  
  /**
   * Add a project platform listener.  When a particular project is referenced
   * by in the service policy platform this listener will be called the first
   * time the project is loaded into the system.
   * 
   * @param listener the listener
   */
  public synchronized void addProjectLoadListener( IPolicyPlatformProjectLoadListener listener )
  {
    platformImpl.addProjectLoadListener( listener );
  }
  
  /**
   * Removes a project platform listener.
   * 
   * @param listener the listener
   */
  public synchronized void removeProjectLoadListener( IPolicyPlatformProjectLoadListener listener )
  {
    platformImpl.removeProjectLoadListener( listener );    
  }
  
  /**
   * 
   * @param enumId
   * @return returns a list of state enumeration items given the unique
   * enumeration ID for this enumeration.
   */
  public List<IStateEnumerationItem> getStateEnumeration( String enumId )
  {
    return platformImpl.getStateEnumeration( enumId ); 
  }
  
  /**
   * 
   * @param stateItemId
   * @return returns a state enumeration item given this ID for this enumeration
   * item.
   */
  public IStateEnumerationItem getStateItemEnumeration( String stateItemId )
  {
    return platformImpl.getStateItemEnumeration( stateItemId );
  }
}
