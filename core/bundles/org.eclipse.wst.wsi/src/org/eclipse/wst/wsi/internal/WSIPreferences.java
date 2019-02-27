/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal;

/**
 * WS-I test tools property.
 * 
 * @author David Lauzon, IBM
 *
 */

public class WSIPreferences
{
  protected String complianceLevel = "";
  protected String tadfile = "";
  
  /**
   *  Constructor.
   */
  public WSIPreferences()
  {
	complianceLevel = WSITestToolsProperties.WARN_NON_WSI;
	tadfile = WSITestToolsProperties.DEFAULT_ASSERTION_FILE;
  }
  
  /**
   * Returns the WS-I compliance level.
   * @return the WS-I compliance level.
   * @see #setComplianceLevel
   */
  public String getComplianceLevel()
  {
    return complianceLevel;
  }

  /**
   * Set the WS-I compliance level.
   * @param compliance the WS-I compliance level.
   * @see #getComplianceLevel
   */
  public void setComplianceLevel(String compliance)
  {
    this.complianceLevel = compliance;
  }

  /**
   * Returns the location of the Basic Profile Test Assertions file.
   * @return the location of the Basic Profile Test Assertions file.
   * @see #setTADFile
   */
  public String getTADFile()
  {
    return tadfile;
  }

  /**
   * Set the location of the Basic Profile Test Assertions file.
   * @param file the location of the Basic Profile Test Assertions file.
   * @see #getTADFile
   */
  public void setTADFile(String file)
  {
    this.tadfile = file;
  }
}

