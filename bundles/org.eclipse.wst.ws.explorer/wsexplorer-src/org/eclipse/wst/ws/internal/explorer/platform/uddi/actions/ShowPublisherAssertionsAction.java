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

import org.uddi4j.UDDIException;
import org.uddi4j.transport.TransportException;
import org.uddi4j.util.*;
import org.uddi4j.response.*;
import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.client.UDDIProxy;

import java.util.*;
import java.net.*;

public class ShowPublisherAssertionsAction extends CommonPublisherAssertionsAction
{

  protected String subQueryKey_;
  protected boolean isSubQueryGet_;
  protected boolean subQueryInitiated_;
  protected String newSubQuery_;
  protected String newSubQueryItem_;

  public ShowPublisherAssertionsAction(Controller controller)
  {
    super(controller);

    subQueryKey_ = null;
    isSubQueryGet_ = false;
    subQueryInitiated_ = false;
  }

  protected final boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    if (registryNode_ == null)
      return false;

    // ids from uddiObjects_table
    String[] selectedBusIds = parser.getParameterValues(UDDIActionInputs.PUBLISHER_ASSERTIONS_SELECTED_BUS_ID);
    if (selectedBusIds != null && selectedBusIds.length > 0)
      propertyTable_.put(UDDIActionInputs.PUBLISHER_ASSERTIONS_SELECTED_BUS_ID, selectedBusIds);

    newSubQuery_ = parser.getParameter(UDDIActionInputs.NEW_SUBQUERY_INITIATED);
    String isSubQueryGetString = parser.getParameter(UDDIActionInputs.SUBQUERY_GET);
    newSubQueryItem_ = parser.getParameter(UDDIActionInputs.NEW_SUBQUERY_QUERY_ITEM);
    subQueryKey_ = parser.getParameter(UDDIActionInputs.SUBQUERY_KEY);

    // Validate the data.
    controller_.getUDDIPerspective();
    FormTool formTool = getSelectedFormTool();
    FormToolPropertiesInterface formToolPI = ((MultipleFormToolPropertiesInterface)formTool).getFormToolProperties(subQueryKey_);
    formToolPI.clearErrors();

    if (newSubQuery_ != null && newSubQuery_.length() > 0)
      subQueryInitiated_ = true;
    else
      subQueryInitiated_ = false;

    if (isSubQueryGetString != null)
      isSubQueryGet_ = Boolean.valueOf(isSubQueryGetString).booleanValue();

    // if not yet logged in, validate the parameters needed to log in
    // NOTE: This action does NOT require the user to be logged in.
    if (!isLoggedIn_) {
      String publishURL = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
      String userID = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
      String password = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD);

      if (publishURL != null)
      {
        propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL,publishURL);
        formTool.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL,publishURL);
      }

      if (userID != null)
      {
        propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID,userID);
        formTool.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID,userID);
      }

      if (password != null)
      {
        propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD,password);
        formTool.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD,password);
      }
    }

    return true;
  }

  public final boolean isSubQueryInitiated()
  {
    return subQueryInitiated_;
  }

  public final boolean isSubQueryGet()
  {
    return isSubQueryGet_;
  }

  public final String getSubQueryKey()
  {
    return subQueryKey_;
  }

  public final String getNewSubQuery()
  {
    return newSubQuery_;
  }

  public final String getNewSubQueryItem()
  {
    return newSubQueryItem_;
  }

  public boolean run()
  {
    // Synchronize client view and server model (uddiObject_table.jsp)
    synchronizeUDDIObjectTable();

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
        if (Validator.validateURL(publishURL) &&
            Validator.validateString(userID) &&
            password != null) {
          registryElement.performLogin(publishURL, userID, password);
          isLoggedIn_ = registryElement.isLoggedIn();
        }
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

    // show all assertion iff is logged in and business is owned
    // else show only completed ones
    BusinessElement busElement = (BusinessElement)selectedElement;
    if (isLoggedIn_ && isBusinessOwned(busElement))
      return showPublisherAssertionsForOwnedBus(busElement);
    else
      return showPublisherAssertionsForNonOwnedBus(busElement);
  }

  private boolean showPublisherAssertionsForOwnedBus(BusinessElement busElement) {
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();

    // try to get a list of all publisher assertion based on the current registry's authToken
    ListManager listManager = new ListManager();
    FormTool formTool = getSelectedFormTool();
    FormToolPropertiesInterface formToolPI = ((MultipleFormToolPropertiesInterface)formTool).getFormToolProperties(subQueryKey_);
    formToolPI.removeProperty(UDDIActionInputs.QUERY_INPUT_EXISTING_PUBLISHER_ASSERTIONS);
    Vector queryInputVector = new Vector();
    RegistryElement registryElement = (RegistryElement)registryNode_.getTreeElement();
    registryElement.getUserId();
    registryElement.getCred();

    // show all publisher assertions
    UDDIProxy proxy = ((RegistryElement)registryNode_.getTreeElement()).getProxy();
    AssertionStatusReport assertionReport;
    try {
      assertionReport = proxy.get_assertionStatusReport(registryElement.getAuthInfoString(), (CompletionStatus)null);
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
    Vector assertionVector = assertionReport.getAssertionStatusItemVector();
    Vector otherBusinessKeys = new Vector();
    Vector pubAssertionElements = new Vector();

    for (int i = 0; i < assertionVector.size(); i++) {
      AssertionStatusItem assertionItem = (AssertionStatusItem)assertionVector.elementAt(i);
      // determine the from/to business keys
      String fromKey = assertionItem.getFromKey().getText();
      String toKey = assertionItem.getToKey().getText();
      String owningBusinessKey = busElement.getBusinessEntity().getBusinessKey();
      // ignore this assertion if neither the from key nor the to key equals to this business's key
      if (!owningBusinessKey.equals(fromKey) && !owningBusinessKey.equals(toKey))
        continue;
      String otherBusinessKey = ((owningBusinessKey.equals(fromKey)) ? toKey : fromKey);
      // retrieve the information of the "other business"
      otherBusinessKeys.add(otherBusinessKey);

      // retrieve the status of the publisher assertion
      String status = assertionItem.getCompletionStatus().getText();
      KeyedReference keyedRef = assertionItem.getKeyedReference();
      PublisherAssertionElement pubAssertionElement = new PublisherAssertionElement(
                                                                                    fromKey,
                                                                                    toKey,
                                                                                    owningBusinessKey,
                                                                                    null,
                                                                                    -1,
                                                                                    status,
                                                                                    keyedRef);
      pubAssertionElements.add(pubAssertionElement);
    }
    if (otherBusinessKeys.size() > 0) {
      Vector busEntities;
      try {
        busEntities = proxy.get_businessDetail(otherBusinessKeys).getBusinessEntityVector();
      }
      catch (Exception ex) {
        busEntities = new Vector();
        if (otherBusinessKeys.size() > 1) {
          for (int j = 0; j < otherBusinessKeys.size(); j++) {
            try {
              busEntities.add(proxy.get_businessDetail((String)otherBusinessKeys.get(j)).getBusinessEntityVector().get(0));
            }
            catch (Exception exception) {
              pubAssertionElements.remove(j);
            }
          }
        }
        else
          pubAssertionElements.remove(0);
      }
      for (int j = 0; j < busEntities.size(); j++) {
        BusinessEntity be = (BusinessEntity)busEntities.get(j);
        PublisherAssertionElement pubAssertionElement = (PublisherAssertionElement)pubAssertionElements.get(j);
        ListElement le = new ListElement(be);
        queryInputVector.add(le);
        int subQueryItemId = queryInputVector.indexOf(le);
        pubAssertionElement.setServiceProvider(le);
        pubAssertionElement.setSubQueryItemId(subQueryItemId);
        listManager.add(new ListElement(pubAssertionElement));
      }
    }
    busElement.setPublisherAssertions(listManager);
    formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_EXISTING_PUBLISHER_ASSERTIONS, queryInputVector);
    messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_PUBLISHER_ASSERTIONS_REFRESHED"));
    return true;
  }

  private boolean showPublisherAssertionsForNonOwnedBus(BusinessElement busElement) {
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();

    // try to get a list of publisher assertions based on the business key
    ListManager listManager = new ListManager();
    FormTool formTool = getSelectedFormTool();
    FormToolPropertiesInterface formToolPI = ((MultipleFormToolPropertiesInterface)formTool).getFormToolProperties(subQueryKey_);
    formToolPI.removeProperty(UDDIActionInputs.QUERY_INPUT_EXISTING_PUBLISHER_ASSERTIONS);
    Vector queryInputVector = new Vector();
    UDDIProxy proxy = ((RegistryElement)registryNode_.getTreeElement()).getProxy();
    RelatedBusinessesList relatedBusList;
    try {
      relatedBusList = proxy.find_relatedBusinesses(busElement.getBusinessEntity().getBusinessKey(),
                                                                                                         (KeyedReference)null,
                                                                                                         (FindQualifiers)null);
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
    Vector relatedBusVector = relatedBusList.getRelatedBusinessInfos().getRelatedBusinessInfoVector();
    Vector otherBusinessKeys = new Vector();
    Vector pubAssertionElements = new Vector();
    for (int i  = 0; i < relatedBusVector.size(); i++) {
      RelatedBusinessInfo relatedBusInfo = (RelatedBusinessInfo)relatedBusVector.elementAt(i);
      // determine the from/to business keys
      String fromKey;
      String toKey;
      String owningBusinessKey;
      if (relatedBusInfo.getDefaultSharedRelationships().getDirection().equals(SharedRelationships.DIRECTION_FROMKEY)) {
        fromKey = busElement.getBusinessEntity().getBusinessKey();
        toKey = relatedBusInfo.getBusinessKey();
        owningBusinessKey = fromKey;
        otherBusinessKeys.add(toKey);
      }
      else {
        fromKey = relatedBusInfo.getBusinessKey();
        toKey = busElement.getBusinessEntity().getBusinessKey();
        owningBusinessKey = toKey;
        otherBusinessKeys.add(fromKey);
      }

      Vector keyedRefVector = relatedBusInfo.getDefaultSharedRelationships().getKeyedReferenceVector();
      PublisherAssertionElement[] pubAssertionElementArray = new PublisherAssertionElement[keyedRefVector.size()];
      for (int k = 0; k < keyedRefVector.size(); k++) {
        // retrieve the status of the publisher assertion
        String status = CompletionStatus.COMPLETE;
        KeyedReference keyedRef = (KeyedReference)keyedRefVector.elementAt(k);
        PublisherAssertionElement pubAssertionElement = new PublisherAssertionElement(
                                                                                      fromKey,
                                                                                      toKey,
                                                                                      owningBusinessKey,
                                                                                      null,
                                                                                      -1,
                                                                                      status,
                                                                                      keyedRef);
        pubAssertionElementArray[k] = pubAssertionElement;
      }
      pubAssertionElements.add(pubAssertionElementArray);
    }
    if (otherBusinessKeys.size() > 0) {
      Vector busEntities;
      try {
        busEntities = proxy.get_businessDetail(otherBusinessKeys).getBusinessEntityVector();
      }
      catch (Exception ex) {
        busEntities = new Vector();
        if (otherBusinessKeys.size() > 1) {
          for (int j = 0; j < otherBusinessKeys.size(); j++) {
            try {
              busEntities.add(proxy.get_businessDetail((String)otherBusinessKeys.get(j)).getBusinessEntityVector().get(0));
            }
            catch (Exception exception) {
              pubAssertionElements.remove(j);
            }
          }
        }
        else
          pubAssertionElements.remove(0);
      }
      for (int j  = 0; j < busEntities.size(); j++) {
        BusinessEntity be = (BusinessEntity)busEntities.get(j);
        ListElement le = new ListElement(be);
        queryInputVector.add(le);
        int subQueryItemId = queryInputVector.indexOf(le);
        PublisherAssertionElement[] pubAssertionElementArray = (PublisherAssertionElement[])pubAssertionElements.get(j);
        for (int k = 0; k < pubAssertionElementArray.length; k++) {
          pubAssertionElementArray[k].setServiceProvider(le);
          pubAssertionElementArray[k].setSubQueryItemId(subQueryItemId);
          listManager.add(new ListElement(pubAssertionElementArray[k]));
        }
      }
    }
    busElement.setPublisherAssertions(listManager);
    formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_EXISTING_PUBLISHER_ASSERTIONS, queryInputVector);
    messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_PUBLISHER_ASSERTIONS_REFRESHED"));
    return true;
  }
}
