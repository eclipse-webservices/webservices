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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.j2ee.applicationclient.internal.creation.ApplicationClientNatureRuntime;
import org.eclipse.jst.j2ee.client.ApplicationClient;
import org.eclipse.jst.j2ee.ejb.EJBJar;
import org.eclipse.jst.j2ee.internal.ejb.project.EJBNatureRuntime;
import org.eclipse.jst.j2ee.internal.webservice.WebServiceNavigatorGroupType;
import org.eclipse.jst.j2ee.internal.webservice.helper.WebServicesManager;
import org.eclipse.jst.j2ee.web.componentcore.util.WebArtifactEdit;
import org.eclipse.jst.j2ee.webapplication.WebApp;
import org.eclipse.jst.j2ee.webservice.wsclient.Handler;
import org.eclipse.jst.j2ee.webservice.wsclient.ServiceRef;
import org.eclipse.jst.j2ee.webservice.wsclient.WebServicesResource;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.HandlerTableItem;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.ComponentHandle;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;

/**
 * ClientHandlersWidgetDefaultingCommand
 *
 * Initialize and load the handlers data
 */
public class ClientHandlersWidgetDefaultingCommand extends AbstractHandlersWidgetDefaultingCmd {

  private HandlerTableItem[] handlers_;

  private MessageUtils msgUtils_;

  private boolean isGenSkeletonEnabled_;

  private IPath sourceOutputLocation_;

  private Hashtable wsRefsToHandlersTable_;

  private Hashtable refNameToServiceRefObj_;

//  private WebServiceEditModel wsEditModel_;

  private WebServicesManager webServicesManager_;

  private IProject project_;
  
  private String componentName_;
  
  private WebServicesResource wsClientRes_;

  private String serviceRefName_ = null;
  
  private Collection wsServiceRefs_;
  
  public Status execute(Environment env) {
    String pluginId = "org.eclipse.jst.ws.consumption.ui";
    MessageUtils msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
    Status status = new SimpleStatus("");

    webServicesManager_ = new WebServicesManager();

    IStructuredSelection selection = getInitialSelection();
    if (selection == null) {
      status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_TASK_EXCEPTED"), Status.ERROR, null);
      env.getStatusHandler().reportError(status);
      return status;
    }

    status = processHandlers();
    return status;

  }

  public Status processHandlers() {
    try {
      Vector handlers = new Vector();
      wsRefsToHandlersTable_ = new Hashtable();
      refNameToServiceRefObj_ = new Hashtable();

//      wsEditModel_ = getWebServiceEditModel();

      wsServiceRefs_ = getWSServiceRefsFromSelection();

      if (wsServiceRefs_ != null) {
        Iterator wsRefsIter = wsServiceRefs_.iterator();
        for (int i = 0; i < wsServiceRefs_.size(); i++) {

          ServiceRef wsServiceRef = (ServiceRef) wsRefsIter.next();

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

        handlers_ = (HandlerTableItem[]) handlers.toArray(new HandlerTableItem[0]);
      }
    }
    catch (Exception e) {
      return new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_TASK_EXCEPTED"), Status.ERROR, e);
    }
    return new SimpleStatus("");
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

  /**
   * @return Returns the wsEditModel.
   */
//  public WebServiceEditModel getWsEditModel() {
//    return wsEditModel_;
//  }

  public String getServiceRefName() {
    return this.serviceRefName_;
  }

  public WebServicesResource getWsClientResource() {
    return wsClientRes_;
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

      if (project_==null){
        project_ = getProject();
      }
      if (project_==null){
        return null;
      }
      
      // get module name
      componentName_ = getComponentName();
      
      ComponentHandle ch = ComponentHandle.create(project_, componentName_);
       
      List clientWSResourceList = webServicesManager_.get13ServiceRefs(ch);
      if (!clientWSResourceList.isEmpty())
        wsClientRes_ = (WebServicesResource)clientWSResourceList.get(0);

      if (J2EEUtils.isWebComponent(project_, componentName_)) {
        WebArtifactEdit webEdit = null;
        try {
          IVirtualComponent vc = ComponentCore.createComponent(project_, componentName_);          
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
      else if (J2EEUtils.isAppClientComponent(project_, componentName_)){
        ApplicationClientNatureRuntime rt = ApplicationClientNatureRuntime.getRuntime(project_);
        if (rt!=null) {
          ApplicationClient appClient = rt.getApplicationClient();
          if (appClient != null){
            return webServicesManager_.getServiceRefs(appClient);
          }
        }
      }
      else if (J2EEUtils.isEJBComponent(project_, componentName_)){
        EJBNatureRuntime rt = EJBNatureRuntime.getRuntime(project_);
        if(rt!=null){
          EJBJar ejbJar = rt.getEJBJar();
          if (ejbJar !=null){
            return webServicesManager_.getServiceRefs(ejbJar);
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