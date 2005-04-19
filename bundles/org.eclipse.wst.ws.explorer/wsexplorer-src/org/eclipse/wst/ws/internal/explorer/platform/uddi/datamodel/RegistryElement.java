/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel;

import org.eclipse.wst.ws.internal.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*;
import org.eclipse.wst.ws.internal.parser.discovery.*;
import org.uddi4j.UDDIException;
import org.uddi4j.client.UDDIProxy;
import org.uddi4j.response.AuthToken;
import org.uddi4j.transport.TransportException;
import org.uddi4j.datatype.service.*;
import org.uddi4j.datatype.*;
import java.util.*;
import java.net.*;

public class RegistryElement extends AbstractUDDIElement
{
  private final long AUTH_INFO_TIMEOUT = 3000000;
  private UDDIProxy proxy_;
  private long authTokenTimestamp_;
  private AuthToken authToken_;
  private String id_;
  private String password_;
  private String inquiryURL_;
  private String publishURL_;
  private String cachedPublishURL_;
  private String registrationURL_;
  private Hashtable userDefinedCategories_;
  private boolean checkForUserDefinedCategories_;
  private String categoriesDirectory_;

  public RegistryElement(UDDIProxy proxy, String inquiryURL, String name, Model model)
  {
    super(name, model);
    proxy_ = proxy;
    inquiryURL_ = inquiryURL;
    publishURL_ = null;
    authTokenTimestamp_ = -1;
    authToken_ = null;
    id_ = null;
    password_ = null;
    cachedPublishURL_ = null;
    registrationURL_ = null;
    userDefinedCategories_ = null;
    checkForUserDefinedCategories_ = false;
    categoriesDirectory_ = null;
  }

  public final UDDIProxy getProxy()
  {
    return proxy_;
  }

  public final String getInquiryURL()
  {
    return inquiryURL_;
  }

  public final QueryParentElement getQueryParentElement()
  {
    return (QueryParentElement) (getElements(UDDIModelConstants.REL_QUERIES_PARENT).nextElement());
  }

  public final PublishedItemsElement getPublishedBusinessesElement()
  {
    return (PublishedItemsElement) (getElements(UDDIModelConstants.REL_PUBLISHED_BUSINESSES_PARENT).nextElement());
  }

  public final PublishedItemsElement getPublishedServicesElement()
  {
    return (PublishedItemsElement) (getElements(UDDIModelConstants.REL_PUBLISHED_SERVICES_PARENT).nextElement());
  }

  public final PublishedItemsElement getPublishedServiceInterfacesElement()
  {
    return (PublishedItemsElement) (getElements(UDDIModelConstants.REL_PUBLISHED_SERVICE_INTERFACES_PARENT).nextElement());
  }

  public final void performLogin(String publishURL, String userId, String password) throws TransportException, UDDIException, MalformedURLException
  {
    publishURL_ = publishURL;
    proxy_.setPublishURL(NetUtils.createURL(publishURL));
    authToken_ = proxy_.get_authToken(userId, password);
    authTokenTimestamp_ = System.currentTimeMillis();
    id_ = userId;
    password_ = password;
  }

  public final String getAuthInfoString()
  {
    if (!isLoggedIn())
      return null;
    if ((System.currentTimeMillis() - authTokenTimestamp_) > AUTH_INFO_TIMEOUT)
    {
      try
      {
        authToken_ = proxy_.get_authToken(id_, password_);
        authTokenTimestamp_ = System.currentTimeMillis();
      }
      catch (Throwable t)
      {
      }
    }
    return authToken_.getAuthInfoString();
  }

  public final boolean isLoggedIn()
  {
    return authToken_ != null;
  }

  public final void setCachedPublishURL(String publishURL)
  {
    cachedPublishURL_ = publishURL;
  }

  public final String getPublishURL()
  {
    if (isLoggedIn() && publishURL_ != null)
      return publishURL_;
    return cachedPublishURL_;
  }

  public final void setRegistrationURL(String registrationURL)
  {
    registrationURL_ = registrationURL;
  }

  public final String getRegistrationURL()
  {
    return registrationURL_;
  }

  public final String getUserId()
  {
    return id_;
  }

  public final void setUserId(String userId)
  {
    id_ = userId;
  }

  public final String getCred()
  {
    return password_;
  }

  public final void setCred(String cred)
  {
    password_ = cred;
  }

  public final void setUserDefinedCategories(Hashtable userDefinedCategories)
  {
    userDefinedCategories_ = userDefinedCategories;
  }

  public final Enumeration getUserDefinedCategories()
  {
    if (userDefinedCategories_ != null)
      return userDefinedCategories_.elements();
    else
      return null;
  }

  public final CategoryModel getUserDefinedCategory(String tModelKey)
  {
    return (CategoryModel) userDefinedCategories_.get(tModelKey);
  }

  // Special handler to be invoked before sending a message to the registry.
  public final void handlePreInvocation(BusinessService busService)
  {
    // For XMethods, business services must contain a description of the form:
    // IMPLEMENTATION: ibmws
    if (getInquiryURL().equals("http://uddi.xmethods.net/inquire"))
    {
      Vector descriptionVector = busService.getDescriptionVector();
      if (descriptionVector == null)
        descriptionVector = new Vector();
      boolean containsImplementationDescription = false;
      for (int i = 0; i < descriptionVector.size(); i++)
      {
        Description description = (Description) descriptionVector.elementAt(i);
        if (description.getText().startsWith("IMPLEMENTATION: "))
        {
          containsImplementationDescription = true;
          break;
        }
      }
      if (!containsImplementationDescription)
      {
        descriptionVector.addElement(new Description("IMPLEMENTATION: ibmws"));
        busService.setDescriptionVector(descriptionVector);
      }
    }
  }

  public final void setCheckForUserDefinedCategories(boolean checkForUserDefinedCategories)
  {
    checkForUserDefinedCategories_ = checkForUserDefinedCategories;
  }

  public final boolean getCheckForUserDefinedCategories()
  {
    return checkForUserDefinedCategories_;
  }

  public final void setCategoriesDirectory(String directory)
  {
    categoriesDirectory_ = directory;
  }

  public final String getCategoriesDirectory()
  {
    return categoriesDirectory_;
  }
}