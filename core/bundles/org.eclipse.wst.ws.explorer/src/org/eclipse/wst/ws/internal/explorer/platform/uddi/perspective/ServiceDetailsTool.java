/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective;

import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.actions.ProxyLoadPageAction;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.ServiceElement;
import org.uddi4j.datatype.service.BusinessService;
import org.uddi4j.util.CategoryBag;

public class ServiceDetailsTool extends DetailsTool
{
  public ServiceDetailsTool(ToolManager toolManager,String alt)
  {
    super(toolManager,alt,ProxyLoadPageAction.getActionLink("uddi/forms/ServiceDetailsForm.jsp"));
  }

  public final void initDefaultProperties()
  {
    clearPropertyTable();
    toolManager_.getNode().getNodeManager().getController();
    ServiceElement serviceElement = (ServiceElement)(toolManager_.getNode().getTreeElement());
    BusinessService busService = serviceElement.getBusinessService();

    setProperty(UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_KEY,busService.getServiceKey());
    setProperty(UDDIActionInputs.QUERY_INPUT_UUID_BUSINESS_KEY,busService.getBusinessKey());

    Vector nameVector = new Vector();
    copyIndexVector(busService.getNameVector(),nameVector);
    setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_NAMES,nameVector);

    Vector descriptionVector = new Vector();
    copyIndexVector(busService.getDescriptionVector(),descriptionVector);
    setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_DESCRIPTIONS,descriptionVector);

    CategoryBag catBag = busService.getCategoryBag();
    if (catBag != null)
    {
      Vector catVector = new Vector();
      copyIndexVector(catBag.getKeyedReferenceVector(),catVector);
      setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_CATEGORIES,catVector);
    }
  }
  
  public void addAuthenticationProperties(RegistryElement regElement)
  {
    String publishURL = regElement.getPublishURL();
    String userId = regElement.getUserId();
    String password = regElement.getCred();

    if (publishURL == null)
      publishURL = "";
    if (userId == null)
      userId = "";
    if (password == null)
      password = "";

    setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL,publishURL);
    setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID,userId);
    setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD,password);
  }
}
