/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060404 134913   sengpl@ca.ibm.com - Seng Phung-Lu       
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jem.util.emf.workbench.WorkbenchResourceHelperBase;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.j2ee.applicationclient.componentcore.util.AppClientArtifactEdit;
import org.eclipse.jst.j2ee.client.ApplicationClient;
import org.eclipse.jst.j2ee.client.ApplicationClientResource;
import org.eclipse.jst.j2ee.ejb.EJBJar;
import org.eclipse.jst.j2ee.ejb.EJBResource;
import org.eclipse.jst.j2ee.ejb.internal.modulecore.util.EJBArtifactEditUtilities;
import org.eclipse.jst.j2ee.internal.webservice.WebServiceNavigatorGroupType;
import org.eclipse.jst.j2ee.internal.webservice.helper.WebServicesManager;
import org.eclipse.jst.j2ee.web.componentcore.util.WebArtifactEdit;
import org.eclipse.jst.j2ee.webapplication.WebApp;
import org.eclipse.jst.j2ee.webapplication.WebAppResource;
import org.eclipse.jst.j2ee.webservice.wsclient.Handler;
import org.eclipse.jst.j2ee.webservice.wsclient.ServiceRef;
import org.eclipse.jst.j2ee.webservice.wsclient.WebServicesResource;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.common.HandlerServiceRefHolder;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.HandlerTableItem;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.environment.IEnvironment;

/**
 * ClientHandlersWidgetDefaultingCommand
 *
 * Initialize and load the handlers data
 */
public class ClientHandlersWidgetDefaultingCommand extends AbstractHandlersWidgetDefaultingCmd {

  private WebServicesManager webServicesManager_;

  private IProject project_;
  
  private String serviceRefName_ = null;
  
  private List wsServiceRefs_;
  
  private HandlerServiceRefHolder[] handlerServiceRefHolder_;
  private String errorStatusMsg_ = null;
  private boolean isMultipleSelection_;
  
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {    
    IEnvironment env = getEnvironment();
    IStatus status = Status.OK_STATUS;

    webServicesManager_ = WebServicesManager.getInstance();

    IStructuredSelection selection = getInitialSelection();
    if (selection == null) {
      status = StatusUtils.errorStatus( ConsumptionUIMessages.MSG_ERROR_TASK_EXCEPTED );
      env.getStatusHandler().reportError(status);
      return status;
    }
    else if (selection.size()>1){
      status = processMultipleHandlers();
      return status;
    }

    status = processHandlers();
    return status;

  }

  /**
   * For processing handlers
   * @return
   */
  public IStatus processHandlers() {
    try {

      Collection selectedServiceRefs = getWSServiceRefsFromSelection(); // get initial selection values
      if (selectedServiceRefs==null || selectedServiceRefs.isEmpty()){
        //report no Web service client is available
      return StatusUtils.errorStatus(ConsumptionUIMessages.MSG_ERROR_WEB_SERVICE_CLIENTS_NOT_FOUND);
      }
      wsServiceRefs_ = webServicesManager_.getAllWorkspaceServiceRefs(); 

      if (wsServiceRefs_ != null) {
        int numberOfServiceRefs = wsServiceRefs_.size();
        handlerServiceRefHolder_ = new HandlerServiceRefHolder[numberOfServiceRefs];
        
        for (int i = 0; i < numberOfServiceRefs; i++) {
          ServiceRef wsServiceRef = (ServiceRef) wsServiceRefs_.get(i);
          Vector handlers = new Vector();
          List wsHandlers = wsServiceRef.getHandlers();
      
          for (int k = 0; k < wsHandlers.size(); k++) {

            Handler wsHandler = (Handler) wsHandlers.get(k);

            HandlerTableItem handlerItem = new HandlerTableItem();
            handlerItem.setHandler(wsHandler);
            handlerItem.setHandlerName(wsHandler.getHandlerName());
            handlerItem.setHandlerClassName(wsHandler.getHandlerClass().getQualifiedName());
            handlerItem.setWsDescRef(wsServiceRef);

            handlers.add(handlerItem);
          }
          String wsServiceRefName = wsServiceRef.getServiceRefName();
          handlerServiceRefHolder_[i] = new HandlerServiceRefHolder();
          handlerServiceRefHolder_[i].setHandlerList(handlers);
          handlerServiceRefHolder_[i].setServiceRef(wsServiceRef);
          handlerServiceRefHolder_[i].setServiceRefName(wsServiceRefName);
          
        }

      }
      
    }
    catch (Exception e) {
      return StatusUtils.errorStatus( ConsumptionUIMessages.MSG_ERROR_TASK_EXCEPTED, e);
    }
    return Status.OK_STATUS;
  }

  public IStatus processMultipleHandlers(){
      ServiceRef[] serviceRefs = getServiceRefs();
      if (errorStatusMsg_ != null){
          return StatusUtils.errorStatus(errorStatusMsg_);
      }
      isMultipleSelection_ = true;
      
      handlerServiceRefHolder_ = new HandlerServiceRefHolder[serviceRefs.length];
      Vector handlers = new Vector();
      for (int i=0;i<serviceRefs.length;i++){
        
        String refName = serviceRefs[i].getServiceRefName();
        handlerServiceRefHolder_[i] = new HandlerServiceRefHolder();
        handlerServiceRefHolder_[i].setHandlerList(handlers);
        handlerServiceRefHolder_[i].setServiceRefName(refName);
        handlerServiceRefHolder_[i].setServiceRef(serviceRefs[i]);
      }
      return Status.OK_STATUS;      
  }
  
  /**
   * @return Returns the isGenSkeletonEnabled_.
   */
  public boolean getGenSkeletonEnabled() {
    if (isMultipleSelection_)
      return false;    
    return true;
  }


  public String getServiceRefName() {
    return this.serviceRefName_;
  }

  public IProject getClientProject() {
    return project_;
  }
  
  private ServiceRef[] getServiceRefs(){
      IStructuredSelection initSel = getInitialSelection();
      ServiceRef[] serviceRefs = new ServiceRef[initSel.size()];
      Iterator iter = initSel.iterator();
      for (int i=0;i<initSel.size();i++) {
          Object obj = iter.next();
          if (obj instanceof ServiceRef){
              serviceRefs[i] = (ServiceRef)obj;
          }
          else{
              errorStatusMsg_ = ConsumptionUIMessages.MSG_ERROR_INVALID_MULTIPLE_SERVICE_SELECT;
              return null;
          }
      }
      return serviceRefs;
}

  private Collection getWSServiceRefsFromSelection() {

    IStructuredSelection initSel = getInitialSelection();
    if (initSel != null && initSel.size() == 1) {
      Object obj = initSel.getFirstElement();
      ServiceRef serviceRef = null;
      if (obj instanceof ServiceRef) {
        // Client ServiceRef
        serviceRef = (ServiceRef) obj;
        serviceRefName_ = serviceRef.getServiceRefName();
        project_ = ProjectUtilities.getProject(serviceRef);
      }
      else if (obj instanceof WebServiceNavigatorGroupType) {
        WebServiceNavigatorGroupType wsngt = (WebServiceNavigatorGroupType) obj;
        serviceRef = wsngt.getServiceRef();
        serviceRefName_ = serviceRef.getServiceRefName();
        project_ = ProjectUtilities.getProject(serviceRef);
      }
      else if (obj instanceof IFile){
          Resource res = WorkbenchResourceHelperBase.getResource((IFile)obj, true);
          Collection serviceRefs = null;
          if (res instanceof WebServicesResource) {
              // webservicesclient.xml for J2EE 1.3
              WebServicesResource wsRes = (WebServicesResource)res;
              serviceRefs = wsRes.getWebServicesClient().getServiceRefs();
              if (!serviceRefs.isEmpty()) {
                ServiceRef ref = (ServiceRef)((List)serviceRefs).get(0);
                serviceRefName_ = ref.getServiceRefName();
                project_ = ProjectUtilities.getProject(ref);
                return serviceRefs;
              }
              return null;
          }
          else {
              if(res instanceof WebAppResource){
                  // web.xml for J2EE 1.4
                  WebAppResource webAppRes = (WebAppResource)res;
                  serviceRefs = webAppRes.getWebApp().getServiceRefs();
              }
              else if (res instanceof EJBResource){
                  EJBResource ejbRes = (EJBResource)res;
                  serviceRefs = webServicesManager_.getServiceRefs(ejbRes.getEJBJar());
              }
              else if (res instanceof ApplicationClientResource){
                  ApplicationClientResource appClientRes = (ApplicationClientResource)res;
                  serviceRefs = webServicesManager_.getServiceRefs(appClientRes.getApplicationClient());//appClientRes.getApplicationClient().getServiceRefs();
              }
              if (serviceRefs!=null && serviceRefs.size()>0) {
                  ServiceRef ref = (ServiceRef)((List)serviceRefs).get(0);
                  serviceRefName_ = ref.getServiceRefName();
                  project_ = ProjectUtilities.getProject(ref); 
              }
              return serviceRefs;              
          }
      }

      // This section is for obtaining all the serviceRefs from the project, given that the initial selection
      // was from the J2EE view (ServiceRef or WebServiceNavigatorGroupType), it will select the right serviceRef
      if (project_==null){
        project_ = getProject();
      }
      if (project_==null){
        return null;
      }     
       
      if (J2EEUtils.isWebComponent(project_)) {
        WebArtifactEdit webEdit = null;
        try {
          IVirtualComponent vc = ComponentCore.createComponent(project_);          
          webEdit = WebArtifactEdit.getWebArtifactEditForRead(vc);
          if (webEdit != null)
          {
            WebApp webApp = (WebApp) webEdit.getDeploymentDescriptorRoot();
            if (webApp != null) {
              return webServicesManager_.getServiceRefs(webApp);
            }
          }
        }
        finally{
          if(webEdit!=null)
            webEdit.dispose();
        }
      }
      else if (J2EEUtils.isEJBComponent(project_)){

    	IVirtualComponent vc = ComponentCore.createComponent(project_);
    	EJBJar ejbJar = EJBArtifactEditUtilities.getEJBJar(vc);
        if (ejbJar!=null) {
            return webServicesManager_.getServiceRefs(ejbJar);
        }
      }
      else if (J2EEUtils.isAppClientComponent(project_)){
    	  IVirtualComponent vc = ComponentCore.createComponent(project_);
    	  AppClientArtifactEdit appEdit = null;
          try {
              appEdit = AppClientArtifactEdit.getAppClientArtifactEditForRead(vc);
          if (appEdit!=null){
              ApplicationClient appClient = appEdit.getApplicationClient();
              if (appClient !=null){
                  return webServicesManager_.getServiceRefs(appClient);
              }
          }
          }
          finally{
              if(appEdit!=null){
                  appEdit.dispose();
              }
          }
      }

    
    }
    return null;
  }

  public Collection getWsServiceRefs(){
    return this.wsServiceRefs_;
  }

  public boolean getIsMultipleSelection(){
      return this.isMultipleSelection_;
  }
  
  public HandlerServiceRefHolder[] getHandlerServiceRefHolder(){
    return this.handlerServiceRefHolder_;
  }
}