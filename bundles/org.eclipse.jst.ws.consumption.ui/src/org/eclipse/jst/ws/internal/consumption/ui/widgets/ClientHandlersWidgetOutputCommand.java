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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jst.j2ee.applicationclient.componentcore.util.AppClientArtifactEdit;
import org.eclipse.jst.j2ee.ejb.componentcore.util.EJBArtifactEdit;
import org.eclipse.jst.j2ee.web.componentcore.util.WebArtifactEdit;
import org.eclipse.jst.j2ee.webservice.wsclient.Handler;
import org.eclipse.jst.j2ee.webservice.wsclient.ServiceRef;
import org.eclipse.jst.j2ee.webservice.wsclient.Webservice_clientFactory;
import org.eclipse.jst.j2ee.webservice.wsclient.internal.impl.Webservice_clientFactoryImpl;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.JavaMOFUtils;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.HandlerTableItem;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.componentcore.ArtifactEdit;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.common.internal.emf.utilities.EtoolsCopyUtility;

/*
 * Provide a way to externalize the edited fields and create new handlers
 *  
 */
public class ClientHandlersWidgetOutputCommand extends AbstractDataModelOperation 
{

  private Hashtable oldWSServiceRefsToHandlersTable_;

  private Hashtable newWSServiceRefsToHandlersTable_;

  private Hashtable handlersTable_;

  private IProject project_;

  private Collection wsServiceRefs_;

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    IStatus status = Status.OK_STATUS;

    try {

      oldWSServiceRefsToHandlersTable_ = new Hashtable();
      newWSServiceRefsToHandlersTable_ = new Hashtable();

      if (wsServiceRefs_ != null) {
        Iterator wsRefsIter = wsServiceRefs_.iterator();
        for (int i = 0; i < wsServiceRefs_.size(); i++) {

          ServiceRef wsServiceRef = (ServiceRef) wsRefsIter.next();

          List wsHandlers = wsServiceRef.getHandlers();
          oldWSServiceRefsToHandlersTable_.put(wsServiceRef, wsHandlers);
          newWSServiceRefsToHandlersTable_.put(wsServiceRef, new ArrayList());
        }

        Enumeration e = handlersTable_.keys();
        while (e.hasMoreElements()) {

          String serviceRefName = (String) e.nextElement();
          List handlerTableItems_ = (List) handlersTable_.get(serviceRefName);
          if (handlerTableItems_ != null) {
            // form Handler tables
            for (int i = 0; i < handlerTableItems_.size(); i++) {

              HandlerTableItem hti = (HandlerTableItem) handlerTableItems_.get(i);
              Object wsModelRef = hti.getWsDescRef();
              if (wsModelRef != null && wsModelRef instanceof ServiceRef) {
                ServiceRef wsRef = (ServiceRef) wsModelRef;

                Object handler = hti.getHandler();
                if (handler != null && handler instanceof Handler) {
                  // clone it
                  Handler clonedHandler = (Handler) EtoolsCopyUtility.createCopy((Handler) handler);
                  ((List) newWSServiceRefsToHandlersTable_.get(wsRef)).add(clonedHandler);
                }
                else {
                  // create it
                  Webservice_clientFactory wsClientFactory = new Webservice_clientFactoryImpl();
                  Handler newHandler = wsClientFactory.createHandler();
                  newHandler.setHandlerName(hti.getHandlerName());

                  JavaClass javaClass = JavaMOFUtils.getJavaClass(hti.getHandlerClassName(), project_);
                  if (javaClass != null) {
                    newHandler.setHandlerClass(javaClass);
                  }

                  ((List) newWSServiceRefsToHandlersTable_.get(wsRef)).add(newHandler);
                }

              }

            }
          }
        }

        // add handlers to ports
        addHandlersToServiceRefs();
        // save the artifact edit model
        saveEditModel();

      }
    }
    catch (Exception e) 
    {
      return StatusUtils.errorStatus(ConsumptionUIMessages.MSG_ERROR_TASK_EXCEPTED, e);
    }

    return status;
  }

  private void saveEditModel() {
      ArtifactEdit artifactEdit = null;
      try {
          if (J2EEUtils.isWebComponent(project_)) {
                artifactEdit = WebArtifactEdit.getWebArtifactEditForWrite(project_);
          }
          else if (J2EEUtils.isEJBComponent(project_)){
                artifactEdit = EJBArtifactEdit.getEJBArtifactEditForWrite(project_);
            }
          else if (J2EEUtils.isAppClientComponent(project_)){
                artifactEdit = AppClientArtifactEdit.getAppClientArtifactEditForWrite(project_);
          }
      }
      finally {
          if (artifactEdit!=null) {
              artifactEdit.save(null);
              artifactEdit.dispose();
          }
      }
  }
  
  private void addHandlersToServiceRefs() {
    try {
      Enumeration refsToHandlers = newWSServiceRefsToHandlersTable_.keys();
      while (refsToHandlers.hasMoreElements()) {
        ServiceRef serviceRef = (ServiceRef) refsToHandlers.nextElement();
        if (serviceRef != null) {
          List handlers = (List) newWSServiceRefsToHandlersTable_.get(serviceRef);
          List modelHandlers = (List) oldWSServiceRefsToHandlersTable_.get(serviceRef);
          modelHandlers.clear();
          //wsServiceRefs_.addAll(handlers);
          modelHandlers.addAll(handlers);
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The new handlerTableItems to set.
   * @param handlerTableItems
   */
  public void setHandlersTable(Hashtable handlersTable) {
    this.handlersTable_ = handlersTable;
  }

  public void setClientProject(IProject project) {
    this.project_ = project;
  }

  public void setWsServiceRefs(Collection wsRefs) {
    this.wsServiceRefs_ = wsRefs;
  }

}