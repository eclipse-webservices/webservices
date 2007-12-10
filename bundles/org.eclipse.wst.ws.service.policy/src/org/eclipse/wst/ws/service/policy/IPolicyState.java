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
package org.eclipse.wst.ws.service.policy;

import org.eclipse.wst.ws.service.policy.listeners.IPolicyStateChangeListener;

/**
 * 
 * This interface is used by service policy extenders to save the state of
 * there policies.  Note: extenders do not need to worry about if these changes
 * are committed or discarded.  This is handled by the framework.  For example
 * if an extender makes a number of calls to the putValue method and at some
 * point ServicePolicyPlatform.discard() is called, the framework will handle
 * the discarding of the changes made by the putValue method calls.
 * 
 * Note: this data will eventually be stored in Eclipse with the following key:
 * 
 *     servicePolicyId + "." + key
 *     
 *     where servicePolicyId is the unique id for this service policy and the key
 *     is the key specified in the putValue method.
 * 
 */
public interface IPolicyState
{
  public final static String DefaultValueKey = "default.value.key";
  
  /**
   * 
   * @return returns true if the state of this policy is mutable.
   */
  public boolean isMutable();
  
  /**
   * 
   * @param mutable sets whether this policy is mutable or not.  Note:
   * if the service policy for this IPolicyState object is predefined calling
   * this method will have no effect.
   */
  public void setMutable( boolean mutable );
  
  /**
   * This method sets some state for a service policy.
   * 
   * @param key a key for this policy state.
   * @param value the value of the policy state.
   */
  public void putValue( String key, String value );
  
  /**
   * This method gets some state for a service policy.  If there
   * is no persisted value for this key the default value will be
   * returned.  If there is no default value an empty string is returned.
   * 
   * @param key a key for this service policy.
   * @return returns the value for the key specified.
   */
  public String getValue( String key );
  
  /**
   * This method sets the default for a particular key.  
   * 
   * @param key the key
   * @param defaultValue the default value.
   */
  public void putDefaultValue( String key, String defaultValue );
  
  /**
   * Adds a policy state change listener to this service policy.
   * 
   * @param listener the listener
   * @param notifyOnlyOnCommit indicates if this listener should be
   * called for all state changes or only when the state data is
   * committed. 
   */
  public void addPolicyStateChangeListener( IPolicyStateChangeListener listener, boolean notifyOnlyOnCommit );
  
  /**
   * Removes a policy state change listener from this service policy.
   * @param listener
   */
  public void removePolicyStateChangeListener( IPolicyStateChangeListener listener );
}
