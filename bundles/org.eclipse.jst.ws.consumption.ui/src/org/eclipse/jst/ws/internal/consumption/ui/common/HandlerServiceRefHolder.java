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
 * 20060404 134913  sengpl@ca.ibm.com - Seng Phung-Lu
 * -------- -------- -----------------------------------------------------------
 */
package org.eclipse.jst.ws.internal.consumption.ui.common;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.j2ee.webservice.wsclient.Handler;
import org.eclipse.jst.j2ee.webservice.wsclient.ServiceRef;
import org.eclipse.jst.j2ee.webservice.wsclient.Webservice_clientFactory;
import org.eclipse.jst.j2ee.webservice.wsclient.internal.impl.Webservice_clientFactoryImpl;
import org.eclipse.jst.ws.internal.common.JavaMOFUtils;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.HandlerTableItem;


public class HandlerServiceRefHolder {

  private String serviceRefName;
  private List handlerList;
  private ServiceRef serviceRef;
  private IPath sourceOutputPath;
  
  public IPath getSourceOutputPath() {
    return sourceOutputPath;
  }
  
  public void setSourceOutputPath(IPath sourceOutputPath) {
    this.sourceOutputPath = sourceOutputPath;
  }  
  
  public IProject getProject() {
    if (serviceRef!=null)
      return ProjectUtilities.getProject(serviceRef);
    else
      return null;
  }

  public List getHandlerList() {
    return handlerList;
  }
  
  public void setHandlerList(List handlerList) {
    this.handlerList = handlerList;
  }
  
  public ServiceRef getServiceRef() {
    return serviceRef;
  }
  
  public void setServiceRef(ServiceRef serviceRef) {
    this.serviceRef = serviceRef;
  }
  
  public String getServiceRefName() {
    return serviceRefName;
  }
  
  public void setServiceRefName(String serviceRefName) {
    this.serviceRefName = serviceRefName;
  }
  
  public void addHandlerToServiceRef(){
    if (handlerList!=null && !handlerList.isEmpty()){
      
      for (int x=0;x<handlerList.size();x++){
        HandlerTableItem hti = (HandlerTableItem)handlerList.get(x);
        if (serviceRef!=null){
          List existingHandlers = serviceRef.getHandlers();
          boolean alreadyExists = false;
          for (int i=0;i<existingHandlers.size();i++){
            Handler handler = (Handler)existingHandlers.get(i);
            if (handler.getHandlerClass().equals(hti.getHandlerClassName())){
              alreadyExists = true;
            }
          }
          
          if (!alreadyExists) {
            
            Webservice_clientFactory wsClientFactory = new Webservice_clientFactoryImpl();
            Handler newHandler = wsClientFactory.createHandler();
            newHandler.setHandlerName(hti.getHandlerName());
            try {
              IProject project = ProjectUtilities.getProject(serviceRef);
              if (project!=null) {
                JavaClass javaClass = JavaMOFUtils.getJavaClass(hti.getHandlerClassName(), project);
                if (javaClass != null) {
                  newHandler.setHandlerClass(javaClass);
                }    
              }
            }
            catch(Exception e){
              e.printStackTrace();
            }
          }
          
        }
      
      }
    }
  }
  
}
