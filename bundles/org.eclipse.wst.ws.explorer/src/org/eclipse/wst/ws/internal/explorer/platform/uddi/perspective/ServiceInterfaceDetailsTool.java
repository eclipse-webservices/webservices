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
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.ListElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.ServiceInterfaceElement;
import org.uddi4j.datatype.tmodel.TModel;
import org.uddi4j.util.CategoryBag;
import org.uddi4j.util.IdentifierBag;

public class ServiceInterfaceDetailsTool extends DetailsTool
{
  public ServiceInterfaceDetailsTool(ToolManager toolManager,String alt)
  {
    super(toolManager,alt,ProxyLoadPageAction.getActionLink("uddi/forms/ServiceInterfaceDetailsForm.jsp"));
  }

  public final void initDefaultProperties()
  {
    clearPropertyTable();
    toolManager_.getNode().getNodeManager().getController();
    ServiceInterfaceElement siElement = (ServiceInterfaceElement)(toolManager_.getNode().getTreeElement());
    TModel tModel = siElement.getTModel();

    setProperty(UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_INTERFACE_KEY,tModel.getTModelKey());
    ListElement nameListElement = new ListElement(tModel.getNameString());
    nameListElement.setTargetViewToolInfo(-1,-1,0);
    setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_NAME,nameListElement);

    Vector descriptionVector = new Vector();
    copyIndexVector(tModel.getDescriptionVector(),descriptionVector);
    setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_DESCRIPTIONS,descriptionVector);

    IdentifierBag idBag = tModel.getIdentifierBag();
    if (idBag != null)
    {
      Vector idVector = new Vector();
      copyIndexVector(idBag.getKeyedReferenceVector(),idVector);
      setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_IDENTIFIERS,idVector);
    }

    CategoryBag catBag = tModel.getCategoryBag();
    if (catBag != null)
    {
      Vector catVector = new Vector();
      copyIndexVector(catBag.getKeyedReferenceVector(),catVector);
      setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_CATEGORIES,catVector);
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
