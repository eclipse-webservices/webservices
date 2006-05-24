/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060404 134913  sengpl@ca.ibm.com - Seng Phung-Lu
 * 20060518 142554 sengpl@ca.ibm.com  - Seng Phung-Lu
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.common;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.j2ee.webservice.wsdd.Handler;
import org.eclipse.jst.j2ee.webservice.wsdd.PortComponent;
import org.eclipse.jst.j2ee.webservice.wsdd.WebServiceDescription;
import org.eclipse.jst.j2ee.webservice.wsdd.WsddFactory;
import org.eclipse.jst.j2ee.webservice.wsdd.internal.impl.WsddFactoryImpl;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.HandlerTableItem;


public class HandlerDescriptionHolder {
  
  private String descriptionName;
  private List handlerList;
  private WebServiceDescription descriptionObject;
  private IPath sourceOutputPath;
  
    
  public IPath getSourceOutputPath() {
    return sourceOutputPath;
  }
  
  public void setSourceOutputPath(IPath sourceOutputPath) {
    this.sourceOutputPath = sourceOutputPath;
  }

  public IProject getProject(){
    if (descriptionObject!=null)
      return ProjectUtilities.getProject(descriptionObject);
    else
      return null;
  }
  
  /*
   * get the WebServiceDescription object 
   */
  public WebServiceDescription getDescriptionObject() {
    return descriptionObject;
  }
  
  /**
   * set WebServiceDescription object
   * @param descriptionObject
   */
  public void setDescriptionObject(WebServiceDescription descriptionObject) {
    this.descriptionObject = descriptionObject;
  }

  /**
   * get Description name
   * @return
   */
  public String getDescriptionName() {
    return descriptionName;
  }
  
  /**
   * set Description name
   * @param descriptionName
   */
  public void setDescriptionName(String descriptionName) {
    this.descriptionName = descriptionName;
  }
  
  /**
   * get List of HandlerTableItem associated with this service description
   * @return
   */
  public List getHandlerList() {
    return handlerList;
  }

  /**
   * set List of HandlerTableItem associated with this service description
   * @param handlerList
   */
  public void setHandlerList(List handlerList) {
    this.handlerList = handlerList;
  }
  
  public void addHandlerToAllPorts(){
    
    if (handlerList!=null && !handlerList.isEmpty()){
      
      for (int z=0;z<handlerList.size();z++){
        HandlerTableItem hti = (HandlerTableItem)handlerList.get(z);  
        if (descriptionObject!=null) {
          List portComponents = descriptionObject.getPortComponents();
          for (int x=0;x<portComponents.size();x++){
            PortComponent wsPort = (PortComponent) portComponents.get(x);
            List wsHandlers = wsPort.getHandlers();
            if (wsHandlers!=null){
              boolean alreadyExists = false;
              for (int y=0;y<wsHandlers.size();y++){
                Handler handler = (Handler)wsHandlers.get(y);
                if (handler.getHandlerClass().equals(hti.getHandlerClassName())){
                  alreadyExists = true;
                }
              }
          
              if (!alreadyExists) {
                //create it and add to the list
                WsddFactory wsddFactory = new WsddFactoryImpl();
                Handler newHandler = wsddFactory.createHandler();
                newHandler.setHandlerName(hti.getHandlerName());
                newHandler.setHandlerClass(hti.getHandlerClassName());
                wsHandlers.add(newHandler);
              }
            }
          }
        }
      }
    }
    
  }
  
  
}
