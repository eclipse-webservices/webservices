/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.datamodel.wsdlmodel;

import org.eclipse.jst.ws.internal.datamodel.Model;

public class StubbedWSDLModel
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";


  protected Model fModel;
  public StubbedWSDLModel()
  {
    WSDLElement wsdlElement = new WSDLElement("WSDLFiles");
    fModel = wsdlElement.getModel();   
  }

  public Model getWSDLModel()
  {
    return fModel;
  }

  public void buildModel()
  {
    DefinitionElement def1 = new DefinitionElement((WSDLElement)fModel.getRootElement(),"StockQuote"); 
    ServiceElement ser1 = new ServiceElement(def1,"getQuote");
    ServiceElement ser2 = new ServiceElement(def1,"setQuote");
    PortElement port1 = new PortElement(ser1,"port1");
    PortElement port2 = new PortElement(ser1,"port2");
    PortElement port3 = new PortElement(ser2,"port1");
    PortElement port4 = new PortElement(ser2,"port2");
    BindingElement bind1 = new BindingElement(port1,"bind1");
    BindingElement bind2 = new BindingElement(port2,"bind1");
    BindingElement bind3 = new BindingElement(port3,"bind1");
    BindingElement bind4 = new BindingElement(port4,"bind1");
    OperationElement op1 = new OperationElement(bind1,"op1");
    OperationElement op2 = new OperationElement(bind2,"op1");
    OperationElement op3 = new OperationElement(bind3,"op1");
    OperationElement op4 = new OperationElement(bind4,"op1");
    MessageElement me1 = new MessageElement(op1,"me1");
    MessageElement me2 = new MessageElement(op1,"me2");
    PartElement pe1  = new PartElement(me1,"pe1");
    PartElement pe2  = new PartElement(me2,"pe2");

    DefinitionElement def2 = new DefinitionElement((WSDLElement)fModel.getRootElement(),"TempConversion"); 
    ServiceElement ser3 = new ServiceElement(def2,"getTemp");
    ServiceElement ser4 = new ServiceElement(def2,"setTemp");
    PortElement port5 = new PortElement(ser3,"port1");
    PortElement port6 = new PortElement(ser4,"port1");
    BindingElement bind5 = new BindingElement(port5,"bind1");
    BindingElement bind6 = new BindingElement(port6,"bind1");
    OperationElement op5 = new OperationElement(bind5,"op1");
    OperationElement op6 = new OperationElement(bind6,"op1");

  }
}

