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

public interface IDescriptor
{
  /**
   * 
   * @return returns the short name for the descriptor
   */
  public String getShortName();
  
  /**
   * Sets the short name for the descriptor.
   * @param name
   */
  public void setShortName(String name);
  
  /**
   * 
   * @return returns the long name for the descriptor.
   */
  public String getLongName();

  /**
   * Sets the long name for the descriptor.
   * @param longName
   */
  public void setLongName(String longName);

  /**
   * 
   * @return returns the description for this descriptor.
   */
  public String getDescription();
  
  /**
   * Sets the description for this descriptor.
   * @param description
   */
  public void setDescription(String description);
  
  /**
   * 
   * @return returns the icon path for this descriptor.  This path
   * is relative to the plugin specified by the icon bundle id.  This
   * method may return null if there is no icon for this descriptor.
   */
  public String getIconPath();
  
  /**
   * Sets the icon patch for this descriptor.
   * 
   * @param iconPath
   */
  public void setIconPath(String iconPath);
  
  /**
   * 
   * @return returns the bundle ID of a plugin that contains an icon.  This
   * method may return null if there is no icon associated with this descriptor.
   */
  public String getIconBundleId();
  
  /**
   * Sets the icon bundle id for this descriptor.
   * 
   * @param iconBundleId
   */
  public void setIconBundleId(String iconBundleId);
  
  /**
   * 
   * @return returns the context help id for this descriptor.  This method
   * may return null if no context has been specified.
   */
  public String getContextHelpId();

  /**
   * Sets the context help id for this descriptor.
   * 
   * @param contextHelpId
   */
  public void setContextHelpId(String contextHelpId);
}
