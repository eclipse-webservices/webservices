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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.j2ee.webservice.wsdd.Handler;
import org.eclipse.jst.j2ee.webservice.wsdd.PortComponent;
import org.eclipse.jst.j2ee.webservice.wsdd.WebServiceDescription;
import org.eclipse.jst.j2ee.webservice.wsdd.WsddFactory;
import org.eclipse.jst.j2ee.webservice.wsdd.WsddResource;
import org.eclipse.jst.j2ee.webservice.wsdd.internal.impl.WsddFactoryImpl;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.HandlerTableItem;
import org.eclipse.wst.command.internal.provisional.env.core.AbstractDataModelOperation;
import org.eclipse.wst.common.internal.emf.utilities.EtoolsCopyUtility;

/*
 * Provide a way to externalize the edited fields and create new handlers
 *  
 */
public class ServiceHandlersWidgetOutputCommand extends AbstractDataModelOperation
{

  private WsddResource wsddResource_;
  private Hashtable wsDescToHandlers_;
  private Hashtable serviceDescNameToDescObj_;

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    IStatus status = Status.OK_STATUS;
    try {
      Enumeration e = wsDescToHandlers_.keys();
      while (e.hasMoreElements()) {

        Hashtable wsPortsTable = new Hashtable();
        Hashtable wsPortToHandlerTable = new Hashtable();

        String serviceDescName = (String) e.nextElement();
        // load PortName -> PortComponent table
        WebServiceDescription wsDescription_ = (WebServiceDescription) serviceDescNameToDescObj_.get(serviceDescName);
        if (wsDescription_ != null) {

          List wsPorts = wsDescription_.getPortComponents();
          for (int k = 0; k < wsPorts.size(); k++) {
            PortComponent port = (PortComponent) wsPorts.get(k);
            wsPortsTable.put(port.getPortComponentName(), port);
            wsPortToHandlerTable.put(port.getPortComponentName(), new ArrayList());
          }
        }

        List handlerTableItems = (List) wsDescToHandlers_.get(serviceDescName);
        if (handlerTableItems != null) {

          WsddFactory wsddFactory = new WsddFactoryImpl();

          //form port components tables first
          for (int i = 0; i < handlerTableItems.size(); i++) {

            HandlerTableItem hti = (HandlerTableItem) handlerTableItems.get(i);
            PortComponent wsPort = (PortComponent) hti.getPort();
            if (wsPort == null) {
              //find it
              if (wsPortsTable.get(hti.getPortName()) != null)
                wsPort = (PortComponent) wsPortsTable.get(hti.getPortName());
              else {
                // create it; should not get into here
                wsPort = wsddFactory.createPortComponent();
                wsPort.setPortComponentName(hti.getPortName());
                wsPortsTable.put(wsPort.getPortComponentName(), wsPort);
              }
            }
            wsPortToHandlerTable.put(wsPort.getPortComponentName(), new ArrayList());
          }

          // form Handler and ports table
          for (int i = 0; i < handlerTableItems.size(); i++) {

            HandlerTableItem hti = (HandlerTableItem) handlerTableItems.get(i);
            String portName = hti.getPortName();

            PortComponent port = (PortComponent) wsPortsTable.get(portName);
            if (port == null) return status;

            List existingHandlers = port.getHandlers();

            Object handler = hti.getHandler();
            if (!existingHandlers.contains(handler)) {
              // create it
              Handler newHandler = wsddFactory.createHandler();
              newHandler.setHandlerName(hti.getHandlerName());
              newHandler.setHandlerClass(hti.getHandlerClassName());
              ((List) wsPortToHandlerTable.get(portName)).add(newHandler);

            }
            else if (handler instanceof Handler) {
              // clone it
              EtoolsCopyUtility copyUtil = new EtoolsCopyUtility();
              copyUtil.setCopyAdapters(true);
              Handler clonedHandler = (Handler) copyUtil.copy((Handler) handler);
              ((List) wsPortToHandlerTable.get(portName)).add(clonedHandler);
            }
          }

        }

        // remove existing handlers from ports
        removeExistingHandlers(wsPortsTable, wsPortToHandlerTable);

        // add ports to wsDescription
        addPortsToDescriptions(wsPortsTable, wsDescription_);

        // add handlers to ports
        addHandlersToPorts(wsPortsTable, wsPortToHandlerTable);
      }
      if (wsddResource_ != null) {
        wsddResource_.save(new HashMap());
      }

    }
    catch (Exception e) {
      //status = new
      e.printStackTrace();
    }
    return status;
  }

  private void removeExistingHandlers(Hashtable portsTable, Hashtable portsToHandlersTable) {
    try {
      Enumeration ports = portsToHandlersTable.keys();
      while (ports.hasMoreElements()) {
        String portName = (String) ports.nextElement();
        Object port = portsTable.get(portName);
        if (port != null && port instanceof PortComponent) {
          List handlers = ((PortComponent) port).getHandlers();
          handlers.clear();
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void addPortsToDescriptions(Hashtable portsTable, WebServiceDescription wsDescription_) {
    try {
      Enumeration ports = portsTable.keys();
      while (ports.hasMoreElements()) {
        String portName = (String) ports.nextElement();
        // newly edited port
        Object port = portsTable.get(portName);
        
        // existing port
        List emfPorts = wsDescription_.getPortComponents();
        if (!emfPorts.contains(port) && port != null && port instanceof PortComponent) {
          emfPorts.add((PortComponent) port);
        }
      }
    }

    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void addHandlersToPorts(Hashtable portsTable, Hashtable portsToHandlersTable) {
    try {
      Enumeration ports = portsToHandlersTable.keys();
      while (ports.hasMoreElements()) {
        String portName = (String) ports.nextElement();
        Object port = portsTable.get(portName);
        if (port != null && port instanceof PortComponent) {
          List handlers = (List) portsToHandlersTable.get(portName);

          List modelHandlers = ((PortComponent) port).getHandlers();
          modelHandlers.addAll(handlers);
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @param handlerTableItems
   *          The handlerTableItems to set.
   */
  public void setHandlersList(List handlerTableItems) {
  }

  public void setWsDescToHandlers(Hashtable wsDescToHandlers) {
    this.wsDescToHandlers_ = wsDescToHandlers;
  }

//  public void setWsDescription(WebServiceDescription wsDesc) {
//    this.wsDescription_ = wsDesc;
//  }

  public void setWsddResource(WsddResource wsddRes) {
    this.wsddResource_ = wsddRes;
  }

  public void setServiceDescNameToDescObj(Hashtable serviceDescNameToDescObj) {
    this.serviceDescNameToDescObj_ = serviceDescNameToDescObj;
  }
}