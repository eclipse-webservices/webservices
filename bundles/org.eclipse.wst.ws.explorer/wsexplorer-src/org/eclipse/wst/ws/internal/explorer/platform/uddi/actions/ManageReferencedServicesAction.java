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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.ListElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormToolPropertiesInterface;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Perspective;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.BusinessElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.MultipleFormToolPropertiesInterface;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.RegistryNode;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.util.Validator;
import org.uddi4j.UDDIException;
import org.uddi4j.client.UDDIProxy;
import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.datatype.service.BusinessService;
import org.uddi4j.datatype.service.BusinessServices;
import org.uddi4j.transport.TransportException;

public class ManageReferencedServicesAction extends UDDIPropertiesFormAction
{
  protected String subQueryKey_;
  protected boolean isSubQueryGet_;
  protected String newSubQuery_;
  protected String newSubQueryItem_;
  protected boolean subQueryInitiated_;
  protected RegistryNode regNode_;

  public ManageReferencedServicesAction(Controller controller)
  {
    super(controller);
    subQueryKey_ = null;
    isSubQueryGet_ = false;
    subQueryInitiated_ = false;
    regNode_ = getRegistryNode();
  }

  protected final boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    newSubQuery_ = parser.getParameter(UDDIActionInputs.NEW_SUBQUERY_INITIATED);
    String isSubQueryGetString = parser.getParameter(UDDIActionInputs.SUBQUERY_GET);
    newSubQueryItem_ = parser.getParameter(UDDIActionInputs.NEW_SUBQUERY_QUERY_ITEM);
    subQueryKey_ = parser.getParameter(UDDIActionInputs.SUBQUERY_KEY);
    String[] referencedServicesCheckboxes = parser.getParameterValues(UDDIActionInputs.REFERENCED_SERVICE_SELECT_STATE);
    String[] serviceNodeIds = parser.getParameterValues(UDDIActionInputs.NODEID_SERVICE);
    String operation = parser.getParameter(UDDIActionInputs.MANAGE_REFERENCED_SERVICES_OPERATION);
    String publishURL = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
    String userId = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
    String password = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD);

    // Validate the data.
    boolean inputsValid = true;
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();

    FormTool formTool = getSelectedFormTool();
    FormToolPropertiesInterface formToolPI = ((MultipleFormToolPropertiesInterface)formTool).getFormToolProperties(subQueryKey_);
    formToolPI.clearErrors();

    if (newSubQuery_ != null && newSubQuery_.length() > 0)
      subQueryInitiated_ = true;
    else
      subQueryInitiated_ = false;

    if (isSubQueryGetString != null)
      isSubQueryGet_ = Boolean.valueOf(isSubQueryGetString).booleanValue();

    if (operation != null)
      propertyTable_.put(UDDIActionInputs.MANAGE_REFERENCED_SERVICES_OPERATION,operation);
    else
      removeProperty(UDDIActionInputs.MANAGE_REFERENCED_SERVICES_OPERATION);

    if (referencedServicesCheckboxes != null)
      propertyTable_.put(UDDIActionInputs.REFERENCED_SERVICE_SELECT_STATE,referencedServicesCheckboxes);
    else
      removeProperty(UDDIActionInputs.REFERENCED_SERVICE_SELECT_STATE);

    if (serviceNodeIds != null)
    {
      Vector services = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_SERVICES);
      if (services == null)
        services = new Vector();
      Hashtable resultHash = new Hashtable();
      for (int i=0;i<serviceNodeIds.length;i++)
        resultHash.put(serviceNodeIds[i],Boolean.TRUE);
      for (int i=0;i<services.size();i++)
      {
        ListElement listElement = (ListElement)services.elementAt(i);
        if (resultHash.get(String.valueOf(listElement.getTargetNodeId())) == null)
        {
          services.removeElementAt(i);
          i--;
        }
      }
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_SERVICES,services);
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_SERVICES);

    RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
    if (!regElement.isLoggedIn())
    {
      if (publishURL != null)
      {
        propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL,publishURL);
        formTool.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL,publishURL);
      }

      if (!subQueryInitiated_ && !Validator.validateString(publishURL))
      {
        inputsValid = false;
        formTool.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
        messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_PUBLISH_URL"));
      }

      if (userId != null)
      {
        propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID,userId);
        formTool.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID,userId);
      }

      if (!subQueryInitiated_ && !Validator.validateString(userId))
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

    for (int i=0;i<removedProperties_.size();i++)
      formToolPI.removeProperty(removedProperties_.elementAt(i));
    formToolPI.updatePropertyTable(propertyTable_);

    return inputsValid;
  }

  public boolean run()
  {
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    String operationString = (String)propertyTable_.get(UDDIActionInputs.MANAGE_REFERENCED_SERVICES_OPERATION);
    int operation = Integer.parseInt(operationString);
    RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
    BusinessElement busElement = (BusinessElement)(getSelectedNavigatorNode().getTreeElement());
    FormTool formTool = getSelectedFormTool();
    FormToolPropertiesInterface formToolPI = ((MultipleFormToolPropertiesInterface)formTool).getFormToolProperties(subQueryKey_);
    Vector referencedServices = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_REFERENCED_SERVICES);
    boolean operationResult = true;
    try
    {
      String publishURL = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
      String userId = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
      String password = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD);
      UDDIProxy proxy = regElement.getProxy();
      BusinessEntity currentSP = busElement.getBusinessEntity();
      BusinessEntity sp = (BusinessEntity)proxy.get_businessDetail(currentSP.getBusinessKey()).getBusinessEntityVector().get(0);
      
      if (!regElement.isLoggedIn())
        regElement.performLogin(publishURL,userId,password);
        
      Hashtable servicesHash = new Hashtable();
      Vector backupBusServiceVector = new Vector();
      switch (operation)
      {
        case UDDIActionInputs.MANAGE_REFERENCED_SERVICES_OPERATION_ADD:
          Vector services = (Vector)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_SERVICES);          
          for (int i=0;i<services.size();i++)
          {
            ListElement serviceListElement = (ListElement)services.elementAt(i);
            BusinessService bs = (BusinessService)serviceListElement.getObject();
            servicesHash.put(bs.getServiceKey(),bs);
          }
          
          // Save the current list of BusinessServices for rollback operations.
          backupAndUpdateBusinessServices(backupBusServiceVector,sp,servicesHash,true);
                    
          try
          {
            Vector beVector = new Vector();
            beVector.add(sp);
            sp = (BusinessEntity)proxy.save_business(regElement.getAuthInfoString(), beVector).getBusinessEntityVector().get(0);
            for (int i=0;i<services.size();i++)
            {
              ListElement serviceElement = (ListElement)services.elementAt(i);
              BusinessService bs = (BusinessService)serviceElement.getObject();
              String bsName = bs.getDefaultNameString();
              messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_SERVICE_REFERENCED",bsName));
              services.removeElementAt(i);
              i--;
            }
          }
          catch (TransportException e)
          {
            // Roll back and restore the old list of services.
            rollbackBusinessServices(sp,backupBusServiceVector,services,uddiPerspective,messageQueue,"MSG_ERROR_SERVICE_NOT_REFERENCED");
            handleUnexpectedException(uddiPerspective,messageQueue,"TransportException",e);
            operationResult = false;
          }
          catch (UDDIException e)
          {
            // Roll back and restore the old list of services.
            rollbackBusinessServices(sp,backupBusServiceVector,services,uddiPerspective,messageQueue,"MSG_ERROR_SERVICE_NOT_REFERENCED");
            messageQueue.addMessage(uddiPerspective.getController().getMessage("MSG_ERROR_UNEXPECTED"));
            messageQueue.addMessage("UDDIException");
            messageQueue.addMessage(e.toString());
            operationResult = false;
          }
          formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_SERVICES,services);
          break;
        case UDDIActionInputs.MANAGE_REFERENCED_SERVICES_OPERATION_REMOVE:
          String[] referencedServicesCheckboxes = (String[])propertyTable_.get(UDDIActionInputs.REFERENCED_SERVICE_SELECT_STATE);
          Vector selectedReferencedServiceVector = new Vector();
          for (int i=0;i<referencedServicesCheckboxes.length;i++)
          {
            boolean isChecked = Boolean.valueOf(referencedServicesCheckboxes[i]).booleanValue();
            if (isChecked)
            {
              ListElement referencedServiceListElement = (ListElement)referencedServices.elementAt(i);
              BusinessService bs = (BusinessService)referencedServiceListElement.getObject();
              servicesHash.put(bs.getServiceKey(),bs);
              selectedReferencedServiceVector.addElement(bs);
            }
          }          
          
          // Save the current list of business services for rollback operations.
          backupAndUpdateBusinessServices(backupBusServiceVector,sp,servicesHash,false);
          
          try
          {
            Vector beVector = new Vector();
            beVector.add(sp);
            sp = (BusinessEntity)proxy.save_business(regElement.getAuthInfoString(), beVector).getBusinessEntityVector().get(0);
            for (int i=0;i<selectedReferencedServiceVector.size();i++)
            {
              BusinessService bs = (BusinessService)selectedReferencedServiceVector.elementAt(i);
              String bsName = bs.getDefaultNameString();
              messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_SERVICE_REFERENCE_REMOVED",bsName));
            }
          }
          catch (TransportException e)
          {
            // Roll back and restore the old list of services.
            rollbackBusinessServices(sp,backupBusServiceVector,selectedReferencedServiceVector,uddiPerspective,messageQueue,"MSG_ERROR_REFERENCE_NOT_REMOVED");
            handleUnexpectedException(uddiPerspective,messageQueue,"TransportException",e);
            operationResult = false;
          }
          catch (UDDIException e)
          {
            // Roll back and restore the old list of services.
            rollbackBusinessServices(sp,backupBusServiceVector,selectedReferencedServiceVector,uddiPerspective,messageQueue,"MSG_ERROR_REFERENCE_NOT_REMOVED");
            messageQueue.addMessage(uddiPerspective.getController().getMessage("MSG_ERROR_UNEXPECTED"));
            messageQueue.addMessage("UDDIException");
            messageQueue.addMessage(e.toString());
            operationResult = false;
          }
      }
      // Ensure that the referenced services are refreshed when the form reloads.
      formToolPI.removeProperty(UDDIActionInputs.QUERY_INPUT_REFERENCED_SERVICES);
      busElement.setBusinessEntity(sp);
      return operationResult;
    }
    catch (TransportException e)
    {
      handleUnexpectedException(uddiPerspective,messageQueue,"TransportException",e);
      operationResult = false;
    }
    catch (UDDIException e)
    {
      messageQueue.addMessage(uddiPerspective.getController().getMessage("MSG_ERROR_UNEXPECTED"));
      messageQueue.addMessage("UDDIException");
      messageQueue.addMessage(e.toString());
      operationResult = false;
    }
    catch (MalformedURLException e)
    {
      handleUnexpectedException(uddiPerspective,messageQueue,"MalformedURLException",e);
      operationResult = false;
    }
    return operationResult;
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
  
  // Backup the business services and remove those in serviceHash from the current list. Return the updated list of business services
  private final void backupAndUpdateBusinessServices(Vector backupBusServiceVector,BusinessEntity be,Hashtable servicesHash,boolean isAdd)
  {
    // Save the current list of business services for rollback operations.
    BusinessServices currentBusServices = be.getBusinessServices();
    if (currentBusServices == null)
      currentBusServices = new BusinessServices();
    Vector currentBusServiceVector = currentBusServices.getBusinessServiceVector();
    for (int i=0;i<currentBusServiceVector.size();i++)
    {
      BusinessService currentBusService = (BusinessService)currentBusServiceVector.elementAt(i);
      backupBusServiceVector.addElement(currentBusService);
      // If the current business service is a member of the list to be removed/added, remove it now.
      if (servicesHash.get(currentBusService.getServiceKey()) != null)
      {
        currentBusServiceVector.removeElementAt(i);
        i--;
      }
    }
    
    if (isAdd)
    {
      Enumeration e = servicesHash.elements();
      while (e.hasMoreElements())
        currentBusServiceVector.addElement((BusinessService)e.nextElement());
    }
    be.setBusinessServices(currentBusServices);
  }
  
  private final void rollbackBusinessServices(BusinessEntity be,Vector backupBusServiceVector,Vector operatedServices,Perspective perspective,MessageQueue messageQueue,String errorMessageKey)
  {
    // Roll back and restore the old list of services.
    be.getBusinessServices().setBusinessServiceVector(backupBusServiceVector);
    for (int i=0;i<operatedServices.size();i++)
    {
      ListElement serviceListElement = (ListElement)operatedServices.elementAt(i);
      BusinessService bs = (BusinessService)serviceListElement.getObject();
      String bsName = bs.getDefaultNameString();
      messageQueue.addMessage(perspective.getMessage(errorMessageKey,bsName));
    }    
  }
}
