/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import org.eclipse.wst.ws.internal.explorer.platform.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*;

import org.uddi4j.client.UDDIProxy;
import org.uddi4j.response.BusinessInfo;
import org.uddi4j.response.RegisteredInfo;

import java.util.Vector;
import java.util.Iterator;
import java.util.Enumeration;

public abstract class CommonPublisherAssertionsAction extends UDDIPropertiesFormAction
{

  protected RegistryNode registryNode_;
  protected boolean isLoggedIn_;

  public CommonPublisherAssertionsAction(Controller controller)
  {
    super(controller);

    registryNode_ = getRegistryNode();
    if (registryNode_ != null)
      isLoggedIn_ = ((RegistryElement)registryNode_.getTreeElement()).isLoggedIn();
  }

  protected void synchronizeUDDIObjectTable() {
    String[] selectedBusIds = (String[])propertyTable_.get(UDDIActionInputs.PUBLISHER_ASSERTIONS_SELECTED_BUS_ID);
    Node selectedNode = controller_.getUDDIPerspective().getNavigatorManager().getSelectedNode();
    FormTool formTool = (FormTool)(selectedNode.getCurrentToolManager().getSelectedTool());
    String subQueryKey = (String)formTool.getProperty(UDDIActionInputs.SUBQUERY_KEY);
    FormToolPropertiesInterface formToolPI = ((MultipleFormToolPropertiesInterface)formTool).getFormToolProperties(subQueryKey);
    Vector businessVector = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADD_PUBLISHER_ASSERTIONS);
    ListManager businessCopy = (ListManager)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADD_PUBLISHER_ASSERTIONS_COPY);
    if (selectedBusIds == null || selectedBusIds.length <= 0) {
      businessVector.removeAllElements();
      businessCopy.clear();
    }
    Enumeration e = businessCopy.getListElements();
    while (e.hasMoreElements()) {
      boolean isIdFound = false;
      ListElement le = (ListElement)e.nextElement();
      int listElementViewId = le.getViewId();
      for (int i = 0; i < selectedBusIds.length; i++) {
        if (listElementViewId == Integer.parseInt(selectedBusIds[i])) {
          isIdFound = true;
          break;
        }
      }
      if (!isIdFound)
        businessVector.remove(le);
    }
  }

  protected boolean isBusinessOwned(BusinessElement busElement) {
    boolean isBusinessOwned = false;
    String isBusinessOwnedString = busElement.getPropertyAsString(UDDIModelConstants.IS_BUSINESS_OWNED);
    if (isBusinessOwnedString != null) {
      isBusinessOwned = Boolean.valueOf(isBusinessOwnedString).booleanValue();
    }
    else if (isLoggedIn_) {
      try {
        RegistryElement regElement = (RegistryElement)registryNode_.getTreeElement();
        UDDIProxy proxy = regElement.getProxy();
        RegisteredInfo ri = proxy.get_registeredInfo(regElement.getAuthInfoString());
        Vector beVector = ri.getBusinessInfos().getBusinessInfoVector();
        for (Iterator it = beVector.iterator(); it.hasNext();)
        { 
          BusinessInfo bi = (BusinessInfo)it.next();
          if (bi.getBusinessKey().equals(busElement.getBusinessEntity().getBusinessKey()))
          {  
            isBusinessOwned = true;
            break;
          }
        }
        busElement.setPropertyAsString(UDDIModelConstants.IS_BUSINESS_OWNED, String.valueOf(isBusinessOwned));
      }
      catch (Exception e) {}
    }
    return isBusinessOwned;
  }
}
