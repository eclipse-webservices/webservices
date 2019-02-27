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
 * 20071024   196997 pmoogk@ca.ibm.com - Peter Moogk, Initial coding.
 * 20080516   232603 pmoogk@ca.ibm.com - Peter Moogk, Clean up java doc
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy.ui;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.wst.ws.service.policy.IDescriptor;
import org.eclipse.wst.ws.service.policy.IServicePolicy;

/**
 * 
 * This interface represents a Service policy operation that has been defined
 * in the plugin.xml meta data.
 *
 */
public interface IPolicyOperation
{
  /**
   * 
   * Indicates the kind of Service policy operation that this object represents.
   * <ul>
   * <li>enumeration: indicates that a list of enumerated values is associated.</li>
   * <li>selection: indicates that this operation is associated with some
   * boolean operation</li>
   * <li> iconSelection: this operation is similar to the selection operation
   * except that it visual state is rendered via an icon.</li>
   * <li>complex: indicates that some complex operation is required to render
   * and persist some policy data.  This operation will be rendered via a UI
   * button.  When this button is selected the launch class associated with this
   * operation is called.</li>   
   * <ul/> 
   */
  public enum OperationKind { enumeration, selection, iconSelection, complex };
 
  /**
   * 
   * Returns the unique ID associated with this service policy operation.
   * 
   * @return returns the unique ID associated with this service policy operation.
   */
  public String getId();
  
  /**
   * 
   * Returns the descriptor for this service policy operation.
   * 
   * @return returns the descriptor for this service policy operation.
   */
  public IDescriptor getDescriptor();
  
  /**
   * 
   * Returns whether this is a workspace only operation or not.
   * 
   * @return returns true if this service policy operation should only appear
   * in the workspace service policy preference page.
   */
  public boolean isWorkspaceOnly();
  
  /**
   * 
   * Returns the kind of service policy operation.
   * 
   * @return returns the kind of service policy operation.
   */
  public OperationKind getOperationKind();
  
  /**
   * 
   * Returns the enum ID for this operation.
   * 
   * @return returns the enumeration ID if this service policy operation is
   * an enumeration.
   */
  public String getEnumerationId();
  
  /**
   * 
   * Returns the default enumeration item for this operation.
   * 
   * @return returns if this is an enumeration it returns the default item for this
   * enumeration.
   */
  public String getDefaultItem();
  
  /**
   * Launches the service policy operation if this is a complex operation.
   * 
   * @param selectedPolicies the selected service policies.
   */
  public void launchOperation( List<IServicePolicy> selectedPolicies );
  
  /**
   * 
   * Returns whether this operation should be enabled or not.
   * 
   * @param selectedPolicies the selected service policies.
   * 
   * @return returns whether this operation should be enabled or not.
   */
  public boolean isEnabled( List<IServicePolicy> selectedPolicies );
  
  /**
   * 
   * Returns the id pattern for this operation.
   * 
   * @return returns the regular expression pattern associated with this operation.
   * All service policy ID that match this regular expression will be associated
   * with this operation.
   */
  public String getPolicyIdPattern();
  
  /**
   * 
   * Returns the service policy for this operation.
   * 
   * @return returns the service policy for this operation.
   */
  public IServicePolicy getServicePolicy();
  
  /**
   * 
   * Returns the current enumeration item for this operation.
   * 
   * @param project the project if this is project property page item.
   * For the preference page context null should be specified.
   * @return returns the current enumeration item for this operation.
   */
  public String getStateItem( IProject project );
  
  /**
   * Sets the current enumeration item for this operation.
   * 
   * @param project the project if this is project property page item.
   * For the preference page context null should be specified.
   * @param stateItem the item.
   */
  public void setStateItem( IProject project, String stateItem );
  
  /**
   * 
   * Returns whether this operation uses default state or not.
   * 
   * @return returns whether this operation uses default state or not.
   */
  public boolean isUseDefaultData(); 
}
