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
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import java.util.Collection;
import java.util.Hashtable;
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
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
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

  private HandlerTableItem[] handlers_;

  private Hashtable wsRefsToHandlersTable_;

  private Hashtable refNameToServiceRefObj_;

  private WebServicesManager webServicesManager_;

  private IProject project_;
  
  private String serviceRefName_ = null;
  
  private Collection wsServiceRefs_;
  
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

    status = processHandlers();
    return status;

  }

  public IStatus processHandlers() {
    try {
      Vector handlers = new Vector();
      wsRefsToHandlersTable_ = new Hashtable();
      refNameToServiceRefObj_ = new Hashtable();

      wsServiceRefs_ = getWSServiceRefsFromSelection();

      if (wsServiceRefs_ != null && wsServiceRefs_.size()>0) {
        Iterator wsRefsIter = wsServiceRefs_.iterator();
        for (int i = 0; i < wsServiceRefs_.size(); i++) {

          ServiceRef wsServiceRef = (ServiceRef) wsRefsIter.next();
          if (serviceRefName_== null || wsServiceRef.getServiceRefName().equalsIgnoreCase(serviceRefName_)) {
              List wsHandlers = wsServiceRef.getHandlers();
              HandlerTableItem[] handlerItems = new HandlerTableItem[wsHandlers.size()];
              for (int k = 0; k < wsHandlers.size(); k++) {
    
                Handler wsHandler = (Handler) wsHandlers.get(k);
    
                HandlerTableItem handlerItem = new HandlerTableItem();
                handlerItem.setHandler(wsHandler);
                handlerItem.setHandlerName(wsHandler.getHandlerName());
                handlerItem.setHandlerClassName(wsHandler.getHandlerClass().getQualifiedName());
                handlerItem.setWsDescRef(wsServiceRef);
                handlerItems[k] = handlerItem;
                handlers.add(handlerItem);
              }
              String wsServiceRefName = wsServiceRef.getServiceRefName();
              wsRefsToHandlersTable_.put(wsServiceRefName, handlerItems);
              refNameToServiceRefObj_.put(wsServiceRefName, wsServiceRef);
          }
        }

        handlers_ = (HandlerTableItem[]) handlers.toArray(new HandlerTableItem[0]);
      }
      else if (wsServiceRefs_==null || wsServiceRefs_.isEmpty()){
            //report no Web service client is available
          return StatusUtils.errorStatus(ConsumptionUIMessages.MSG_ERROR_WEB_SERVICE_CLIENTS_NOT_FOUND);
        }      
    }
    catch (Exception e) {
      return StatusUtils.errorStatus( ConsumptionUIMessages.MSG_ERROR_TASK_EXCEPTED, e);
    }
    return Status.OK_STATUS;
  }

  public HandlerTableItem[] getAllHandlers() {
    return this.handlers_;
  }

  public Hashtable getHandlers() {
    return wsRefsToHandlersTable_;
  }

  /**
   * @return Returns the isGenSkeletonEnabled_.
   */
  public boolean getGenSkeletonEnabled() {
    return true;
  }

  /**
   * @return Returns the sourceOutputLocation_.
   */
  public IPath[] getSourceOutputLocation() {
    IPath[] locations = null;
    if (project_==null)
     project_ = getProject();
    if (project_ != null) {
      locations = ResourceUtils.getAllJavaSourceLocations(project_);
    }

    return locations;
  }

  public String getServiceRefName() {
    return this.serviceRefName_;
  }

  public IProject getClientProject() {
    return project_;
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
              ServiceRef ref = (ServiceRef)((List)serviceRefs).get(0);
              serviceRefName_ = ref.getServiceRefName();
              project_ = ProjectUtilities.getProject(ref);
              return serviceRefs;
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

  public Hashtable getRefNameToServiceRef() {
    return this.refNameToServiceRefObj_;
  }
  
  public Collection getWsServiceRefs(){
    return this.wsServiceRefs_;
  }

}