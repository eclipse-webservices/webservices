/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.tempuri.AreaService;

public class AreaServiceProxy implements org.tempuri.AreaService.AreaService_PortType {
  private String _endpoint = null;
  private org.tempuri.AreaService.AreaService_PortType areaService_PortType = null;
  
  public AreaServiceProxy() {
    _initAreaServiceProxy();
  }
  
  private void _initAreaServiceProxy() {
    try {
      areaService_PortType = (new org.tempuri.AreaService.AreaService_ServiceLocator()).getAreaServiceSOAP();
      if (areaService_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)areaService_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)areaService_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (areaService_PortType != null)
      ((javax.xml.rpc.Stub)areaService_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.tempuri.AreaService.AreaService_PortType getAreaService_PortType() {
    if (areaService_PortType == null)
      _initAreaServiceProxy();
    return areaService_PortType;
  }
  
  public float calculateRectArea(org.tempuri.AreaService.Dimensions parameters) throws java.rmi.RemoteException{
    if (areaService_PortType == null)
      _initAreaServiceProxy();
    return areaService_PortType.calculateRectArea(parameters);
  }
  
  
}