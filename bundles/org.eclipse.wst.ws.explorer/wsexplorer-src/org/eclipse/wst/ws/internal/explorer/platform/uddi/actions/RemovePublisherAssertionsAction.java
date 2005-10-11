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

import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.ListElement;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.ListManager;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.BusinessElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.PublisherAssertionElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.util.Validator;
import org.uddi4j.UDDIException;
import org.uddi4j.client.UDDIProxy;
import org.uddi4j.datatype.assertion.PublisherAssertion;
import org.uddi4j.response.CompletionStatus;
import org.uddi4j.response.DispositionReport;
import org.uddi4j.response.Result;
import org.uddi4j.transport.TransportException;

public class RemovePublisherAssertionsAction extends CommonPublisherAssertionsAction
{
  public RemovePublisherAssertionsAction(Controller controller)
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

    // ids from uddiObjects_table
    String[] selectedBusIds = parser.getParameterValues(UDDIActionInputs.PUBLISHER_ASSERTIONS_SELECTED_BUS_ID);
    if (selectedBusIds != null && selectedBusIds.length > 0)
      propertyTable_.put(UDDIActionInputs.PUBLISHER_ASSERTIONS_SELECTED_BUS_ID, selectedBusIds);
    // Synchronize client view and server model (uddiObject_table.jsp)
    synchronizeUDDIObjectTable();

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
  
      if (!Validator.validateURL(publishURL))
      {
        inputsValid = false;
        formTool.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
        messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_PUBLISH_URL"));
      }

      if (userID!= null)
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

    // ids from publisher assertion table
    String[] pubAssertionsViewIds = parser.getParameterValues(UDDIActionInputs.PUBLISHER_ASSERTIONS_VIEWID);
    if (pubAssertionsViewIds == null || pubAssertionsViewIds.length == 0)
      inputsValid = false;
    else
      propertyTable_.put(UDDIActionInputs.PUBLISHER_ASSERTIONS_VIEWID, pubAssertionsViewIds);

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

    if (!(selectedElement instanceof BusinessElement))
      return false;

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
    registryElement.getUserId();
    registryElement.getCred();
    BusinessElement busElement = (BusinessElement)selectedElement;
    ListManager pubAssertions = busElement.getPublisherAssertions();

    // determine whether this is a owned business
    // if not, the AddPublisherAssertionsAction cannot be performed
    if (!isBusinessOwned(busElement)) {
        messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_NOT_OWNED_BUSINESS"));
        return false;
    }

    try {
      String[] pubAssertionViewIds = (String[])propertyTable_.get(UDDIActionInputs.PUBLISHER_ASSERTIONS_VIEWID);
      Vector pubAssertionVector = new Vector();
      boolean refreshRequired = false;

      for (int i = 0; i < pubAssertionViewIds.length; i++) {
        int viewID = Integer.parseInt(pubAssertionViewIds[i]);
        ListElement listElement = pubAssertions.getElementWithViewId(viewID);
        PublisherAssertionElement pubAssertionElement = (PublisherAssertionElement)listElement.getObject();
        PublisherAssertion pubAssertion = pubAssertionElement.getPublisherAssertion();
        pubAssertionVector.add(pubAssertion);
        if (pubAssertionElement.getStatus().equals(CompletionStatus.COMPLETE))
          refreshRequired = true;
      }

      DispositionReport report = proxy.delete_publisherAssertions(registryElement.getAuthInfoString(), pubAssertionVector);
      //report status
	  Vector reportErrInfo = report.getResultVector();
      for (int i = 0; i < reportErrInfo.size(); i++) {
        messageQueue.addMessage(((Result) reportErrInfo.get(i)).getErrInfo().getText());
      }

      // refresh is required, refresh the whole publisher assertion table
      if (refreshRequired) {
        ShowPublisherAssertionsAction showPAAction = new ShowPublisherAssertionsAction(controller_);
        Hashtable propertyTable = showPAAction.getPropertyTable();
        Object ids = propertyTable_.get(UDDIActionInputs.PUBLISHER_ASSERTIONS_SELECTED_BUS_ID);
        if (ids != null)
          propertyTable.put(UDDIActionInputs.PUBLISHER_ASSERTIONS_SELECTED_BUS_ID, ids);
        showPAAction.run();
      }
      // refresh is not required, delete the selected publisher assertion elements
      else {
        for (int k = 0; k < pubAssertionViewIds.length; k++) {
          pubAssertions.removeElementWithViewId(Integer.parseInt(pubAssertionViewIds[k]));
        }
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
    catch (Exception e) {
      messageQueue.addMessage(controller_.getMessage("MSG_ERROR_UNEXPECTED"));
      messageQueue.addMessage("Exception");
      messageQueue.addMessage(e.getMessage());
      return false;
    }
  }
}
