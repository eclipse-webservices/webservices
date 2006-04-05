/*******************************************************************************
 * Copyright (c) 2004,2006 IBM Corporation and others.
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
import org.eclipse.jst.ws.internal.consumption.ui.common.HandlerDescriptionHelper;
import org.eclipse.jst.ws.internal.consumption.ui.common.HandlerDescriptionHolder;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.HandlerTableItem;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.common.internal.emf.utilities.EtoolsCopyUtility;

/*
 * Provide a way to externalize the edited fields and create new handlers
 *  
 */
public class ServiceHandlersWidgetOutputCommand extends AbstractDataModelOperation
{

  private WsddResource[] wsddResource_;
  private boolean isMultipleSelection_;
  private HandlerDescriptionHolder[] handlerDescriptionHolder_;

  /**
   * For each HandlerDescriptionHolder, the user selected Handlers are
   * sync'd up with the internal model
   * For multiple services, handlers are added (not removed) to each port.
   * 
   */
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    IStatus status = Status.OK_STATUS;
    try {
      if (isMultipleSelection_){
        for (int i=0; i<handlerDescriptionHolder_.length;i++){
           
          handlerDescriptionHolder_[i].addHandlerToAllPorts();
        }
      }
      else {
        // add handler(s) for single service selected
        for (int h=0;h<handlerDescriptionHolder_.length;h++){
  
          Hashtable wsPortsTable = new Hashtable();
          Hashtable wsPortToHandlerTable = new Hashtable();
  
          String serviceDescName = handlerDescriptionHolder_[h].getDescriptionName();
          // load PortName -> PortComponent table
          HandlerDescriptionHolder hdh = HandlerDescriptionHelper.getForDescriptionName(handlerDescriptionHolder_, serviceDescName);
          if (hdh!=null) {
            
            WebServiceDescription wsDescription_ =  hdh.getDescriptionObject();          
            if (wsDescription_ != null) {
              List wsPorts = wsDescription_.getPortComponents();
              for (int k = 0; k < wsPorts.size(); k++) {
                PortComponent port = (PortComponent) wsPorts.get(k);
                wsPortsTable.put(port.getPortComponentName(), port);
                wsPortToHandlerTable.put(port.getPortComponentName(), new ArrayList());
              }
            }

            List handlerTableItems = hdh.getHandlerList();          
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
        }
      }
      
      // save
      if (wsddResource_ != null) {
        for (int i=0;i<wsddResource_.length;i++) {
          wsddResource_[i].save(new HashMap());
        }
      }
  
      }
    catch (Exception e) {
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
   * HandlerDescriptionHolder
   * @param hdhs
   */
  public void setHandlerDescriptionHolders(HandlerDescriptionHolder[] hdhs){
    this.handlerDescriptionHolder_ = hdhs;
  }
  
  public void setIsMultipleSelection(boolean isMulitpleSelection) {
    this.isMultipleSelection_ = isMulitpleSelection;
  }


  public void setWsddResource(WsddResource[] wsddRes) {
    this.wsddResource_ = wsddRes;
  }

}