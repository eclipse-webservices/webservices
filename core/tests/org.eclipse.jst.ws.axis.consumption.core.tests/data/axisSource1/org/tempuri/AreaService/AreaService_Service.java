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
/**
 * AreaService_Service.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.tempuri.AreaService;

public interface AreaService_Service extends javax.xml.rpc.Service {
    public java.lang.String getAreaServiceSOAPAddress();

    public org.tempuri.AreaService.AreaService_PortType getAreaServiceSOAP() throws javax.xml.rpc.ServiceException;

    public org.tempuri.AreaService.AreaService_PortType getAreaServiceSOAP(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
