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
package org.eclipse.jst.ws.internal.ui.wse;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

public interface WSExplorerType
{
  public static final String LAUNCH_UNIQUE_SESSION = "launchUniqueSession";
  
  // WSDL Page preload constants
  public static final String WSDL_URL = "wsdl";
  public static final String WEB_SERVICE_ENDPOINT = "webServiceEndpoint";
  public static final String SERVICE_QNAME_STRING = "serviceQNameString";
  public static final String BINDING_NAME_STRING = "bindingNameString";
  
  // UDDI Page preload constants
  public static final String INQUIRY_URL = "inquiry";
  public static final String PUBLISH_URL = "publish";
  public static final String SERVICE_NAME = "serviceName";
  public static final String SERVICE_KEY = "serviceKey";
  public static final String CATEGORIES_DIRECTORY = "categoriesDirectory";

  public String getContextName();
  public String getParentPluginID();
  public String getWARLocation();
  public String getWebAppLocation();
  public String getWelcomeURL();
  public String getLaunchOptionRegistryURL();
  public String getBaseURL();
  public String getMetadataDirectory();

  public IStatus launch(IWorkbench wb, IStructuredSelection sel, LaunchOption[] options,boolean forLaunchOutsideIDE);
}