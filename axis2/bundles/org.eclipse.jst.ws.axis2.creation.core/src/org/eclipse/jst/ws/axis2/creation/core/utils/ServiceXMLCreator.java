/*******************************************************************************
 * Copyright (c) 2007 WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070110   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 * 20070620   193441 sandakith@wso2.com - Lahiru Sandakith, fix for the discription not attach to service
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.creation.core.utils;

import java.util.ArrayList;

public class ServiceXMLCreator {
    private String serviceName;
    private String serviceClass;
    private ArrayList operations;

    public ServiceXMLCreator(String serviceName, String serviceClass, ArrayList operations) {
        this.serviceName = serviceName;
        this.serviceClass = serviceClass;
        this.operations = operations;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public ArrayList getOperations() {
        return operations;
    }

    public String toString() {
        String serviceXML = "<service name=\"" + serviceName + "\" >\n" +
        "\t<Description>\n" +
        "\t\tPlease Type your service description here\n" +
        "\t</Description>\n" +
        "\t<messageReceivers>\n" +
        "\t\t<messageReceiver mep=\"http://www.w3.org/2004/08/wsdl/in-only\" " +
        "class=\"org.apache.axis2.rpc.receivers.RPCInOnlyMessageReceiver\" />\n" +
        "\t\t<messageReceiver  mep=\"http://www.w3.org/2004/08/wsdl/in-out\"  " +
        "class=\"org.apache.axis2.rpc.receivers.RPCMessageReceiver\"/>\n" +
        "\t</messageReceivers>\n" + 
        "\t<parameter name=\"ServiceClass\" locked=\"false\">" + serviceClass + "</parameter>\n" ; 
        serviceXML = serviceXML + "</service>\n";
        return serviceXML;
    }

}