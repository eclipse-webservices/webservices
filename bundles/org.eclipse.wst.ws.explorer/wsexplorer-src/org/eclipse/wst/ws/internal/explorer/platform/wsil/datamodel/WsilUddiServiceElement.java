/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel;

import org.eclipse.wst.ws.internal.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.util.Uddi4jHelper;
import org.eclipse.wst.ws.internal.explorer.platform.util.Validator;

import org.apache.wsil.*;
import org.apache.wsil.extension.*;
import org.apache.wsil.extension.uddi.*;

import org.uddi4j.util.DiscoveryURL;
import org.uddi4j.datatype.service.BusinessService;

/**
* The data model element that represents 
* a UDDI service in a WSIL document
*/
public class WsilUddiServiceElement extends WsilServiceElement
{
    private BusinessService busService_;

    public WsilUddiServiceElement(String name, Model model, Service service) {
        super(name, model, service);
        busService_ = null;
    }

    public void setServiceDefinition(BusinessService busService) {
        busService_ = busService;
    }

    public BusinessService getServiceDefinition() {
        return busService_;
    }

    public String getName() {
        return (busService_ == null) ? null : busService_.getDefaultNameString();
    }

    public String getDescription() {
        return (busService_ == null) ? null : busService_.getDefaultDescriptionString();
    }

    public String getUDDIServiceInquiryAPI() {
        ServiceDescription sd = getValidUDDIServiceDescription();
        return (sd == null) ? null : sd.getLocation();
    }

    public String getUDDIServiceKey() {
        ServiceDescription sd = getValidUDDIServiceDescription();
        return (sd == null) ? null : sd.getServiceKey().getText();
    }

    public String getUDDIServiceDiscoveryURL() {
        ServiceDescription sd = getValidUDDIServiceDescription();
        if (sd == null)
            return null;
        else {
            DiscoveryURL discoveryURL = sd.getDiscoveryURL();
            return (discoveryURL == null) ? null : discoveryURL.getText();
        }
    }

    private ServiceDescription getValidUDDIServiceDescription() {
        Description[] descList = service_.getDescriptions();
        for (int i = 0; i < descList.length; i++) {
            ExtensionElement extElement = descList[i].getExtensionElement();
            // The extension element of a UDDI service description
            // must be a ServiceDescription element
            if (!(extElement instanceof ServiceDescription))
                continue;
            // A valid ServiceDescription must have an inquiry API
            if (!Validator.validateURL(((ServiceDescription)extElement).getLocation()))
                continue;
            // A valid ServiceDescription must have a service key
            String serviceKey = ((ServiceDescription)extElement).getServiceKey().getText();
            if (serviceKey == null || serviceKey.length() <= 0)
                continue;
            return (ServiceDescription)extElement;
        }
        return null;
    }

    public boolean validateUDDIService() {
        ServiceDescription sd = getValidUDDIServiceDescription();
        return (sd != null);
    }

    public String getWsdlUrl() {
        return (new Uddi4jHelper()).getWSDL(getServiceDefinition(), null);
    }

    public String toString() {
        return getName();
    }
}
