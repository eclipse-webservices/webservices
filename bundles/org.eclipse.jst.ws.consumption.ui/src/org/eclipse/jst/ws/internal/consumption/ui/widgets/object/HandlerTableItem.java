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

package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import org.eclipse.jst.j2ee.webservice.wsdd.PortComponent;



public class HandlerTableItem {
  
  private String handlerClassName;
  private String handlerName;
  private String portName;
  private int index;
  
  private PortComponent port;
  private Object handler;
  private Object wsDescRef;

  /**
   * @return Returns the handlerClassName.
   */
  public String getHandlerClassName() {
    return handlerClassName;
  }
  /**
   * @param handlerClassName The handlerClassName to set.
   */
  public void setHandlerClassName(String handlerClassName) {
    this.handlerClassName = handlerClassName;
  }
  /**
   * @return Returns the handlerName.
   */
  public String getHandlerName() {
    return handlerName;
  }
  /**
   * @param handlerName The handlerName to set.
   */
  public void setHandlerName(String handlerName) {
    this.handlerName = handlerName;
  }
  /**
   * @return Returns the port.
   */
  public PortComponent getPort() {
    return port;
  }
  /**
   * @param port The port to set.
   */
  public void setPort(PortComponent port) {
    this.port = port;
  }
  /**
   * @return Returns the handler.
   */
  public Object getHandler() {
    return handler;
  }
  /**
   * @param handler The handler to set.
   */
  public void setHandler(Object handler) {
    this.handler = handler;
  }
  /**
   * @return Returns the portName.
   */
  public String getPortName() {
    return portName;
  }
  /**
   * @param portName The portName to set.
   */
  public void setPortName(String portName) {
    this.portName = portName;
  }

  /**
   * @return Returns the index.
   */
  public int getIndex() {
    return index;
  }
  /**
   * @param index The index to set.
   */
  public void setIndex(int index) {
    this.index = index;
  }
  /**
   * @return Returns the wsDescRef.
   */
  public Object getWsDescRef() {
    return wsDescRef;
  }
  /**
   * @param wsDescRef The wsDescRef to set.
   */
  public void setWsDescRef(Object wsDescRef) {
    this.wsDescRef = wsDescRef;
  }
}
