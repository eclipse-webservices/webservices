/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
import org.eclipse.wst.ws.internal.explorer.platform.util.*;

import org.uddi4j.*;
import org.uddi4j.util.*;
import org.uddi4j.response.*;
import org.uddi4j.client.UDDIProxy;
import org.uddi4j.datatype.assertion.*;
import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.datatype.tmodel.TModel;
import org.uddi4j.transport.TransportException;

import java.util.*;
import java.net.*;

public class AddPublisherAssertionsAction extends CommonPublisherAssertionsAction
{
  public AddPublisherAssertionsAction(Controller controller)
  {
    super(controller);
  }

  protected final boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    if (registryNode_ == null)
      return false;

    boolean inputsValid = true;
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    FormTool formTool = getSelectedFormTool();

    // ids from uddiObjects_table.jsp
    String[] selectedBusIds = parser.getParameterValues(UDDIActionInputs.PUBLISHER_ASSERTIONS_SELECTED_BUS_ID);
    if (selectedBusIds == null || selectedBusIds.length == 0) {
      // Synchronize client view and server model (uddiObject_table.jsp)
      synchronizeUDDIObjectTable();
      inputsValid = false;
    }
    else {
      propertyTable_.put(UDDIActionInputs.PUBLISHER_ASSERTIONS_SELECTED_BUS_ID, selectedBusIds);
      // Synchronize client view and server model (uddiObject_table.jsp)
      synchronizeUDDIObjectTable();
    }

    String[] assertionTypes = parser.getParameterValues(UDDIActionInputs.PUBLISHER_ASSERTIONS_TYPE);
    if (assertionTypes != null && assertionTypes.length > 0)
      propertyTable_.put(UDDIActionInputs.PUBLISHER_ASSERTIONS_TYPE, assertionTypes);
    else
      inputsValid = false;

    String[] directions = parser.getParameterValues(UDDIActionInputs.PUBLISHER_ASSERTIONS_DIRECTION);
    if (directions != null && directions.length > 0)
      propertyTable_.put(UDDIActionInputs.PUBLISHER_ASSERTIONS_DIRECTION, directions);
    else
      inputsValid = false;

    // if not yet logged in, validate the parameters needed to log in
    if (!isLoggedIn_) {
      String publishURL = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
      String userID = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
      String password = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD);

      if (publishURL != null)
      {
        propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL,publishURL);
        formTool.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL,publishURL);
      }
  
      if (!Validator.validateString(publishURL))
      {
        inputsValid = false;
        formTool.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
        messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_PUBLISH_URL"));
      }

      if (userID != null)
      {
        propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID,userID);
        formTool.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID,userID);
      }
  
      if (!Validator.validateString(userID))
      {
        inputsValid = false;
        formTool.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
        messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_USERID"));
      }

      if (password != null)
      {
        propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD,password);
        formTool.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD,password);
      }
    }

    return inputsValid;
  }

  public boolean run()
  {
    if (registryNode_ == null)
      return false;

    RegistryElement registryElement = (RegistryElement)registryNode_.getTreeElement();
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    NodeManager navigatorManager = uddiPerspective.getNavigatorManager();
    Node selectedNode = navigatorManager.getSelectedNode();
    TreeElement selectedElement = selectedNode.getTreeElement();

    // if not yet logged in, log in first
    if (!isLoggedIn_) {
      String publishURL = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
      String userID = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
      String password = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD);

      try {
        registryElement.performLogin(publishURL, userID, password);
      }
      catch (TransportException te) {
        messageQueue.addMessage(controller_.getMessage("MSG_ERROR_UNEXPECTED"));
        messageQueue.addMessage("TransportException");
        messageQueue.addMessage(te.getMessage());
        return false;
      }
      catch (UDDIException uddie) {
        messageQueue.addMessage(controller_.getMessage("MSG_ERROR_UNEXPECTED"));
        messageQueue.addMessage("UDDIException");
        messageQueue.addMessage(uddie.toString());
        return false;
      }
      catch (MalformedURLException me) {
        messageQueue.addMessage(controller_.getMessage("MSG_ERROR_UNEXPECTED"));
        messageQueue.addMessage("MalformedURLException");
        messageQueue.addMessage(me.getMessage());
        return false;
      }
    }

    UDDIProxy proxy = registryElement.getProxy();
    String userID = registryElement.getUserId();
    String cred = registryElement.getCred();
    BusinessElement busElement = (BusinessElement)selectedElement;

    // determine whether this is a owned business
    // if not, the AddPublisherAssertionsAction cannot be performed
    if (!isBusinessOwned(busElement)) {
        messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_NOT_OWNED_BUSINESS"));
        return false;
    }

    try {
      String[] assertionTypes = (String[])propertyTable_.get(UDDIActionInputs.PUBLISHER_ASSERTIONS_TYPE);
      String[] directions = (String[])propertyTable_.get(UDDIActionInputs.PUBLISHER_ASSERTIONS_DIRECTION);
      String thisBusinessKey = busElement.getBusinessEntity().getBusinessKey();

      // get all selected businesses
      FormTool formTool = (FormTool)(selectedNode.getCurrentToolManager().getSelectedTool());
      String subQueryKey = (String)formTool.getProperty(UDDIActionInputs.SUBQUERY_KEY);
      FormToolPropertiesInterface formToolPI = ((MultipleFormToolPropertiesInterface)formTool).getFormToolProperties(subQueryKey);
      Vector selectedBusinesses = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADD_PUBLISHER_ASSERTIONS);
      Enumeration e = selectedBusinesses.elements();
      if (!e.hasMoreElements())
        return true;

      // create a vector of publisher assertions based on all the selected businesses
      Vector pubAssertionVector = new Vector();
      int i = 0;
      while (e.hasMoreElements()) {
        ListElement le = (ListElement)e.nextElement();
        BusinessEntity sp = (BusinessEntity)le.getObject();
        KeyedReference keyedRef = new KeyedReference(assertionTypes[i], assertionTypes[i], TModel.RELATIONSHIPS_TMODEL_KEY);
        if (Integer.parseInt(directions[i]) == UDDIActionInputs.DIRECTION_TO)
          pubAssertionVector.add(new PublisherAssertion(sp.getBusinessKey(), thisBusinessKey, keyedRef));
        else
          pubAssertionVector.add(new PublisherAssertion(thisBusinessKey, sp.getBusinessKey(), keyedRef));
        i++;
      }

      // proxy.add_publisherAssertions(...)
      DispositionReport report = proxy.add_publisherAssertions(registryElement.getAuthInfoString(), pubAssertionVector);

      // refresh publisher assertions iff proxy reports success
      if (report.success()) {
        selectedBusinesses.removeAllElements();
        ShowPublisherAssertionsAction showPAAction = new ShowPublisherAssertionsAction(controller_);
        Hashtable propertyTable = showPAAction.getPropertyTable();
        Object ids = propertyTable_.get(UDDIActionInputs.PUBLISHER_ASSERTIONS_SELECTED_BUS_ID);
        if (ids != null)
          propertyTable.put(UDDIActionInputs.PUBLISHER_ASSERTIONS_SELECTED_BUS_ID, ids);
        showPAAction.run();
      }

      return report.success();
    }
    catch (UDDIException uddie) {
      messageQueue.addMessage(controller_.getMessage("MSG_ERROR_UNEXPECTED"));
      messageQueue.addMessage("UDDIException");
      DispositionReport report = uddie.getDispositionReport();
	  Vector reportErrInfo = report.getResultVector();
      for (int i = 0; i < reportErrInfo.size(); i++) {
        messageQueue.addMessage(((Result) reportErrInfo.get(i)).getErrInfo().getText());
      }
      return false;
    }
    catch(Exception e) {
      messageQueue.addMessage(controller_.getMessage("MSG_ERROR_UNEXPECTED"));
      messageQueue.addMessage("Exception");
      messageQueue.addMessage(e.getMessage());
      return false;
    }
  }
}
