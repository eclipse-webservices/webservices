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


/**
* This is the interface for objects that represent a kind of
* Web Service artifact. The primary purpose of a WebServiceType
* object is to manufacture the wizard pages that support the type.
*/
public class WebServiceTypeImpl implements IWebServiceType
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  public IConfigurationElement element_;
  public String typeId_;
  public String typeLabel_;
  public String[] extensionMetadata_;
  public String[] resourceTypeMetadata_;
  public Boolean canFinish_;

  public WebServiceTypeImpl(IConfigurationElement elem)
  {
    super();
    this.element_ = elem; 
  }
  /**
  * Returns the id of this Web Service type.
  * @return id of this Web Service type.
  */
  public String getId ()
  {
    if (typeId_==null)
      typeId_ = element_.getAttribute("id");
    return typeId_;
  }
  /**
  * Returns a short, locale specific name of this Web Service type.
  * @return A short, locale specific name of this Web Service type.
  */
  public String getLabel ()
  {
    if (typeLabel_ == null)
      typeLabel_ = element_.getAttribute("label");
    return typeLabel_;    
  }

  /**
  * Returns a locale specific description of this Web Service type's extension metadata.
  * @return A locale specific description of this Web Service type's extension metadata.
  */
  public String[] getExtensionMetadata ()
  {
    if (extensionMetadata_ ==null)
    {
      StringTokenizer st = new StringTokenizer(element_.getAttribute("extensionMetadata"));
      extensionMetadata_ = new String[st.countTokens()];
      int i=0;
      while (st.hasMoreElements())
      {
        String exten = (String)st.nextToken();
        if (exten!=null)
        {
          extensionMetadata_[i]=exten;
        }
        i++;
      }    
    }
    return this.extensionMetadata_;
  }    

  /**
  * Returns a locale specific description of this Web Service type's resource metadata.
  * @return A locale specific description of this Web Service type's resource metadata.
  */
  public String[] getResourceTypeMetadata ()
  {
    if (resourceTypeMetadata_ == null)
    {
      StringTokenizer st = new StringTokenizer(element_.getAttribute("resourceTypeMetadata"));    
      resourceTypeMetadata_ = new String[st.countTokens()];
      int i=0;
      while (st.hasMoreElements())
      {
        String resourceType = (String)st.nextToken();
        if (resourceType!=null)
        {
          resourceTypeMetadata_[i]=resourceType;
        }
        i++;
      }
    }
    return resourceTypeMetadata_;
  }

  /**
  * Returns true if the plugin.xml contains an attribute of "canFinish" with a value of true.  Otherwise, 
  * false is returned.  Note: if the "canFinish" attribute is not specified true is returned by default.
  * @return Returns whether or not the user can finish on the pages before the web service type pages.
  */
  public boolean getCanFinish()
  {
    if( canFinish_ == null )
    {      
      String canFinishString = element_.getAttribute("canFinish");

      canFinish_ = canFinishString == null ? new Boolean( true ) : Boolean.valueOf( canFinishString );
    }

    return canFinish_.booleanValue();
  }
  
  /**
   * Returns the configuration element associated with
   * this type.
   * 
   * @return org.eclipse.core.runtime.IConfigurationElement
   */
  public IConfigurationElement getConfigurationElement()
  {
    return this.element_; 
  }

  public String getObjectSelectionWidget()
  {
    return element_.getAttribute("objectSelectionWidget");
  }
}
