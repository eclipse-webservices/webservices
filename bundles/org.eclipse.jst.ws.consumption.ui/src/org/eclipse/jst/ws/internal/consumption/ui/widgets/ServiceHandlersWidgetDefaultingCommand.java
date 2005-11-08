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

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
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

  private HandlerTableItem[] handlers_;
  private WebServiceDescription wsDescription_;
  private WsddResource wsddResource_;
  private WebServicesManager webServicesManager_;
  private IProject project_;
  private Hashtable wsDescToHandlers_;
  private Hashtable serviceDescNameToDescObj_;
  private String descriptionName_ = null;

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {    
    IEnvironment env = getEnvironment();
    IStatus status = Status.OK_STATUS;

    webServicesManager_ = new WebServicesManager();
    IStructuredSelection selection = getInitialSelection();
    if (selection == null) {
      status = StatusUtils.errorStatus( ConsumptionUIMessages.MSG_ERROR_TASK_EXCEPTED );
      env.getStatusHandler().reportError(status);
      return status;
    }

    status = processHandlers(env);
    return status;

  }

  public IStatus processHandlers(IEnvironment env) {
    try {
      Vector handlers = new Vector();
      wsDescToHandlers_ = new Hashtable();
      serviceDescNameToDescObj_ = new Hashtable();
      
//      WebServiceEditModel wsed = getWebServiceEditModel();
  //    if (wsed == null)
        wsddResource_ = getWsddResourceFromSelection();
  //    else
  //      wsddResource_ = wsed.getWebServicesXmlResource();

      if (wsddResource_ == null) {
        IStatus status = StatusUtils.errorStatus( ConsumptionUIMessages.MSG_ERROR_TASK_EXCEPTED );
        env.getStatusHandler().reportError(status);
        return status;
      }

      WebServices webServices = wsddResource_.getWebServices();
      if (webServices != null) {
        List wsDescriptions = wsddResource_.getWebServices().getWebServiceDescriptions();
        for (int i = 0; i < wsDescriptions.size(); i++) {

          wsDescription_ = (WebServiceDescription) wsDescriptions.get(i);
          
          List wsPortComponents = wsDescription_.getPortComponents();
          for (int j = 0; j < wsPortComponents.size(); j++) {
            PortComponent wsPort = (PortComponent) wsPortComponents.get(j);
            String portName = wsPort.getPortComponentName();
            List wsHandlers = wsPort.getHandlers();
            HandlerTableItem[] handlerItems = new HandlerTableItem[wsHandlers.size()];            
            for (int k = 0; k < wsHandlers.size(); k++) {

              Handler wsHandler = (Handler) wsHandlers.get(k);

              HandlerTableItem handlerItem = new HandlerTableItem();
              handlerItem.setHandler(wsHandler);
              handlerItem.setHandlerName(wsHandler.getHandlerName());
              handlerItem.setHandlerClassName(wsHandler.getHandlerClass());
              handlerItem.setPort(wsPort);
              handlerItem.setPortName(portName);
              handlerItem.setWsDescRef(wsDescription_);
              handlerItems[k] = handlerItem;
              
              handlers.add(handlerItem);
            }
            String wsDescName = wsDescription_.getWebServiceDescriptionName();
            wsDescToHandlers_.put(wsDescName, handlerItems);
            serviceDescNameToDescObj_.put(wsDescName, wsDescription_);
            
          }

        }

        handlers_ = (HandlerTableItem[]) handlers.toArray(new HandlerTableItem[0]);
      }
    }
    catch (Exception e) 
    {
      e.printStackTrace();
      return StatusUtils.errorStatus( ConsumptionUIMessages.MSG_ERROR_TASK_EXCEPTED, e);
    }
    return Status.OK_STATUS;
  }

  public HandlerTableItem[] getHandlers() {
    return this.handlers_;
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

  public WsddResource getWsddResource() {
    return wsddResource_;
  }

  private WsddResource getWsddResourceFromSelection() {
    IStructuredSelection initSel = getInitialSelection();
    if (initSel != null && initSel.size() == 1) {
      Object obj = initSel.getFirstElement();
      if (obj instanceof ServiceImpl) {
        // Service object
        Service service = (Service) obj;
        descriptionName_ = service.getQName().getLocalPart();
        project_ = ProjectUtilities.getProject(service);
        return webServicesManager_.getWsddResource(service);
      }
      else if (obj instanceof WSDLResourceImpl) {
        // WSDL resource
        WSDLResourceImpl res = (WSDLResourceImpl) obj;
        project_ = ProjectUtilities.getProject(res);
        List wsdlResources = webServicesManager_.getWSDLServices(res);
        return webServicesManager_.getWsddResource((Service) wsdlResources.get(0));
      }
      else if (obj instanceof WebServiceNavigatorGroupType) {
        WebServiceNavigatorGroupType wsngt = (WebServiceNavigatorGroupType) obj;
        Service service = (Service)wsngt.getWsdlService();
        descriptionName_ = service.getQName().getLocalPart();        
        project_ = ProjectUtilities.getProject(service);
        return webServicesManager_.getWsddResource(service);
      }
    }
    
    return null;

  }

  /**
   * @deprecated
   */
  public WebServiceDescription getWsDescription() {
    return wsDescription_;
  }
  
  public String getDescriptionName(){
    return descriptionName_;
  }
  
  public Hashtable getWsRefsToHandlers(){
    return wsDescToHandlers_;
  }
  
  public Hashtable getServiceDescNameToDescObj(){
    return this.serviceDescNameToDescObj_;
  }
  
  
}