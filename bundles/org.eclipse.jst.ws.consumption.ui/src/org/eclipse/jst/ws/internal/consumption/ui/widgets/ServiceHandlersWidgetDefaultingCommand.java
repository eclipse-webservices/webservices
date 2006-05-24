/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060404   134913 sengpl@ca.ibm.com - Seng Phung-Lu   
 * 20060517   142027 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20060518   142554 sengpl@ca.ibm.com - Seng Phung-Lu
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jem.util.emf.workbench.WorkbenchResourceHelperBase;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.j2ee.internal.webservice.WebServiceNavigatorGroupType;
import org.eclipse.jst.j2ee.internal.webservice.helper.WebServicesManager;
import org.eclipse.jst.j2ee.webservice.wsdd.Handler;
import org.eclipse.jst.j2ee.webservice.wsdd.PortComponent;
import org.eclipse.jst.j2ee.webservice.wsdd.WebServiceDescription;
import org.eclipse.jst.j2ee.webservice.wsdd.WebServices;
import org.eclipse.jst.j2ee.webservice.wsdd.WsddResource;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.common.HandlerDescriptionHolder;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.HandlerTableItem;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.internal.impl.ServiceImpl;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;

/**
 * ServiceHandlersWidgetDefaultingCommand
 * 
 * Initialize and load the handlers data
 */
public class ServiceHandlersWidgetDefaultingCommand extends AbstractHandlersWidgetDefaultingCmd {

  private WsddResource[] wsddResource_ = null;
  private IProject project_;
  private String descriptionName_ = null;
  private String errorStatusMsg_ = null;
  private boolean isMultipleSelection_;
  private HandlerDescriptionHolder[] handlerDescriptionHolder_;

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {    
    IEnvironment env = getEnvironment();
    IStatus status = Status.OK_STATUS;
    
    IStructuredSelection selection = getInitialSelection();
    if (selection == null) {
      status = StatusUtils.errorStatus( ConsumptionUIMessages.MSG_ERROR_TASK_EXCEPTED );
      env.getStatusHandler().reportError(status);
      return status;
    }
    else if (selection.size()>1){
    	status = processMultipleHandlers(env);
    	return status;
    }

    status = processHandlers(env);
    return status;

  }

  public IStatus processHandlers(IEnvironment env) {
    try {
      WebServicesManager webServicesManager = new WebServicesManager();
      List allWSDLServices = webServicesManager.getAllWSDLServices();
      int servicesSize = allWSDLServices.size();

      wsddResource_= new WsddResource[servicesSize];
      wsddResource_[0] = getWsddResourceFromSelection();
      if (wsddResource_[0] == null) {
          return StatusUtils.errorStatus(ConsumptionUIMessages.MSG_ERROR_WSDD_NOT_FOUND);
      }
     
      // check for another wsddResource of another J2EE level
      int x = 0;
      while (x<servicesSize) {
        Service service = (Service )allWSDLServices.get(x);
        WsddResource wsddRes = webServicesManager.getWsddResource(service);
        boolean isUniqueWsdd = true;
        for (int w=0;w<wsddResource_.length;w++){
        	if (wsddRes.equals(wsddResource_[w])){
        		isUniqueWsdd = false;
        		break;
        	}
        }
        
        if (isUniqueWsdd) {
          wsddResource_[x] = wsddRes;
        }
        x++;
      }

      // determine total # for descriptions
      handlerDescriptionHolder_ = new HandlerDescriptionHolder[getNumberofServices(wsddResource_)];
      
      int descCounter = 0;
      for (int y=0;y<wsddResource_.length;y++) {
        if (wsddResource_[y] !=null) {
	        WebServices webServices = wsddResource_[y].getWebServices();
	        if (webServices != null) {
	          List wsDescriptions = webServices.getWebServiceDescriptions();
	          for (int i = 0; i < wsDescriptions.size() ; i++) {
	 
	            WebServiceDescription wsDescription = (WebServiceDescription) wsDescriptions.get(i);
	            Vector handlers = new Vector();  
	            List wsPortComponents = wsDescription.getPortComponents();
	            for (int j = 0; j < wsPortComponents.size(); j++) {
	              PortComponent wsPort = (PortComponent) wsPortComponents.get(j);
	              String portName = wsPort.getPortComponentName();
	              List wsHandlers = wsPort.getHandlers();
	     
	              for (int k = 0; k < wsHandlers.size(); k++) {
	  
	                Handler wsHandler = (Handler) wsHandlers.get(k);
	  
	                HandlerTableItem handlerItem = new HandlerTableItem();
	                handlerItem.setHandler(wsHandler);
	                handlerItem.setHandlerName(wsHandler.getHandlerName());
	                handlerItem.setHandlerClassName(wsHandler.getHandlerClass());
	                handlerItem.setPort(wsPort);
	                handlerItem.setPortName(portName);
	                handlerItem.setWsDescRef(wsDescription);
	                
	                handlers.add(handlerItem);
	              }
	            }
	            String wsDescName = wsDescription.getWebServiceDescriptionName();
	            handlerDescriptionHolder_[descCounter] = new HandlerDescriptionHolder();
	            handlerDescriptionHolder_[descCounter].setHandlerList(handlers);
	            handlerDescriptionHolder_[descCounter].setDescriptionObject(wsDescription);
	            handlerDescriptionHolder_[descCounter].setDescriptionName(wsDescName);
	            descCounter++;
	          }
	        }
	      }
      }

      if (handlerDescriptionHolder_ == null){
          //report no Web service is available
          return StatusUtils.errorStatus(ConsumptionUIMessages.MSG_ERROR_WEB_SERVICES_NOT_FOUND);        
      }      
    }
    catch (Exception e) {
      e.printStackTrace();
      return StatusUtils.errorStatus( ConsumptionUIMessages.MSG_ERROR_TASK_EXCEPTED, e);
    }
    return Status.OK_STATUS;
  }

  /**
   * For processing multiple service selection; only prepare table of service description names
   * description objects will be processed later by the output command 
   * @param env
   * @return
   */
  private IStatus processMultipleHandlers(IEnvironment env){

	  Service[] services = getSelectedServices();
      if (errorStatusMsg_ != null){
    	  return StatusUtils.errorStatus(errorStatusMsg_);
      }
      isMultipleSelection_ = true;

      handlerDescriptionHolder_ = new HandlerDescriptionHolder[services.length];
      Vector handlers = new Vector();
      for (int i=0;i<services.length;i++){
        
        String descName = services[i].getQName().getLocalPart();
        WebServiceDescription wsDescription = getServiceDescription(services[i], descName);
        handlerDescriptionHolder_[i] = new HandlerDescriptionHolder();
        handlerDescriptionHolder_[i].setHandlerList(handlers);
        handlerDescriptionHolder_[i].setDescriptionName(descName);
        handlerDescriptionHolder_[i].setDescriptionObject(wsDescription);
      }
      return Status.OK_STATUS;
  }
  
  /**
   * Matches the serviceName with the ServiceDescriptionName to get the WebServiceDescription
   * @param service
   * @param serviceName
   * @return
   */
  private WebServiceDescription getServiceDescription(Service service, String serviceName){
    WebServicesManager webServicesManager = new WebServicesManager();
    WsddResource wsddResource = webServicesManager.getWsddResource(service);
    if (wsddResource!=null) {
      WebServices services = wsddResource.getWebServices();
      if (services!=null) {
        List descriptions  = services.getWebServiceDescriptions();
        for (int i=0;i<descriptions.size();i++){
          WebServiceDescription wsd = (WebServiceDescription)descriptions.get(i);
          if (wsd!=null && wsd.getWebServiceDescriptionName().equals(serviceName)){
            return wsd;
          }
        }
      }
    }
    return null;
  }
  
  public boolean getIsMultipleSelection(){
	  return this.isMultipleSelection_;
  }
  
  /**
   * @return Returns the isGenSkeletonEnabled_.
   */
  public boolean getGenSkeletonEnabled() {
    if (isMultipleSelection_)
      return false;
    return true;
  }

  /**
   * @return Returns the sourceOutputLocation_.
   */
  public IPath[] getSourceOutputLocation() {
    IPath[] locations = null;
    IProject project = getProject();

    if (project != null) {
      locations = ResourceUtils.getAllJavaSourceLocations(project);
    }
    else {
      project = project_;
      if (project != null) {
        locations = ResourceUtils.getAllJavaSourceLocations(project);
      }
    }
    return locations;
  }

  /**
   * Gets the WsddResource
   * @return
   */
  public WsddResource[] getWsddResource() {
    return wsddResource_;
  }

  private WsddResource getWsddResourceFromSelection() {
    WebServicesManager webServicesManager = new WebServicesManager();

    IStructuredSelection initSel = getInitialSelection();
    if (initSel != null && initSel.size() == 1) {
      Object obj = initSel.getFirstElement();
      if (obj instanceof ServiceImpl) {
        // Service object
        Service service = (Service) obj;
        descriptionName_ = service.getQName().getLocalPart();
        project_ = ProjectUtilities.getProject(service);
        return webServicesManager.getWsddResource(service);
      }
      else if (obj instanceof WSDLResourceImpl) {
        // WSDL resource
        WSDLResourceImpl res = (WSDLResourceImpl) obj;
        project_ = ProjectUtilities.getProject(res);
        List wsdlResources = webServicesManager.getWSDLServices(res);
        return webServicesManager.getWsddResource((Service) wsdlResources.get(0));
      }
      else if (obj instanceof WebServiceNavigatorGroupType) {
        WebServiceNavigatorGroupType wsngt = (WebServiceNavigatorGroupType) obj;
        Service service = (Service)wsngt.getWsdlService();
        descriptionName_ = service.getQName().getLocalPart();        
        project_ = ProjectUtilities.getProject(service);
        return webServicesManager.getWsddResource(service);
      }
      else if (obj instanceof IFile){
    	  // webservices.xml file
    	  Resource res = WorkbenchResourceHelperBase.getResource((IFile)obj, true);
    	  WsddResource wsddRes = (WsddResource)res;
          WebServices webServices = wsddRes.getWebServices();
          if (webServices != null) {
            List wsDescriptions = webServices.getWebServiceDescriptions();
            if (wsDescriptions!=null) {
              WebServiceDescription wsd = (WebServiceDescription)wsDescriptions.get(0);
              descriptionName_ = wsd.getWebServiceDescriptionName();
            }
          }
    	  project_ = ProjectUtilities.getProject(res);
    	  return wsddRes;
      }
    }
    
    return null;

  }
  
  /**
   * Get total number of ServiceDescriptions given 14 and 13 services
   * @param wsddRes
   * @return
   */
  private int getNumberofServices(WsddResource[] wsddRes){
    int num= 0;
    for (int i=0;i<wsddRes.length;i++){
    	if (wsddRes[i]!=null) {
    		WebServices ws = wsddRes[i].getWebServices();
    		if (ws!=null){
    			num += ws.getWebServiceDescriptions().size();
    		}
    	}
    }
    return num;
    
  }

  /**
   * For multiple selection of Services
   * @return
   */
  private Service[] getSelectedServices(){
	  WebServicesManager wsManager = new WebServicesManager();
	  IStructuredSelection initSel = getInitialSelection();
  	  Service[] services = new Service[initSel.size()];
  	  wsddResource_ = new WsddResource[initSel.size()];
  	  Iterator iter = initSel.iterator();
  	  for (int i=0;i<initSel.size();i++) {
  		  Object obj = iter.next();
  		  if (obj instanceof Service){
  			  services[i] = (Service)obj;
  			  wsddResource_[i] = wsManager.getWsddResource(services[i]);
  		  }
  		  else{
  			  errorStatusMsg_ = ConsumptionUIMessages.MSG_ERROR_INVALID_MULTIPLE_SERVICE_SELECT;
  			  return null;
  		  }
  	  }
  	  return services;
  }
  

  
  /**
   * Get Description Name for single Web services selected
   * null if mulitple services are selected
   * @return
   */
  public String getDescriptionName(){
    return descriptionName_;
  }
  
  /**
   * An array of HandlerDescriptionHolders
   * @return
   */
  public HandlerDescriptionHolder[] getHandlerDescriptionHolders(){
    return handlerDescriptionHolder_;
  }
  
  
}
