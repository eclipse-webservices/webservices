/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.wizard;

import java.util.StringTokenizer;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;


/**
* This is the interface for objects that represent a kind of
* Web Service artifact. 
*/
public class WebServiceRuntime implements IWebServiceRuntime
{
  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  private IConfigurationElement element_;
  public String runtimeId_;
  public String runtimeLabel_;
  public String runtimeDescription_;
  public String[] j2eeVersions;
  
  
  public WebServiceRuntime(IConfigurationElement elem)
  {
    super();
    this.element_ = elem; 
  }
  
  /**
  * Returns the id of this Web Service runtime.
  * @return id of this Web Service runtime.
  */
  public String getId ()
  {
    if (runtimeId_ == null)
      runtimeId_ = element_.getAttribute("id");
    return runtimeId_;
  }
  
  
  /**
  * Returns a locale specific description of this Web Service runtime.
  * @return A locale specific description of this Web Service runtime.
  */
  public String getDescription ()
  {
    if (runtimeDescription_ == null)
      runtimeDescription_ = element_.getAttribute("description");
    return runtimeDescription_;
  }

  /**
  * Returns a short, locale specific Label of this Web Service runtime.
  * @return A short, locale specific Label of this Web Service runtime.
  */
  public String getLabel ()
  {
    if (runtimeLabel_==null)
      runtimeLabel_ = WebServiceConsumptionUIPlugin.getMessage((String)element_.getAttribute("label"));    
    return runtimeLabel_;
  }

  /**
   *  Returns a True if this runtime has been set as default
   *  @return a boolean
   */
  public boolean getIsDefault()
  {
    return Boolean.valueOf(element_.getAttribute("isDefault")).booleanValue();
  }

  /**
   * Returns a String, J2EE version
   * @return
   */
  public String[] getJ2EEVersions()
  {
    if (j2eeVersions == null)
    {
      String attr = element_.getAttribute("j2eeversion");
      if (attr != null)
      {
        StringTokenizer st = new StringTokenizer(attr, " ");
        int size = st.countTokens();
        j2eeVersions = new String[size];
        for (int i = 0; i < j2eeVersions.length; i++)
          j2eeVersions[i] = st.nextToken();
      }
    }
    return j2eeVersions;
  }

  /**
   * Returns the configuration element associated with
   * this runtime.
   * 
   * @return org.eclipse.core.runtime.IConfigurationElement
   */
  public IConfigurationElement getConfigurationElement()
  {
    return element_;
  }

}


