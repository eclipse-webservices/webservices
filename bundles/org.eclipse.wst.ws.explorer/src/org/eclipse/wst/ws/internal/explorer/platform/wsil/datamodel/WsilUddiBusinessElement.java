/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel;

import org.apache.wsil.Link;
import org.apache.wsil.extension.ExtensionElement;
import org.apache.wsil.extension.uddi.BusinessDescription;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.explorer.platform.util.Validator;
import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.util.DiscoveryURL;

/**
* The data model element that represents 
* a UDDI service in a WSIL document
*/
public class WsilUddiBusinessElement extends WsilLinkElement
{
    private BusinessEntity busEntity_;

    public WsilUddiBusinessElement(String name, Model model, Link link) {
        super(name, model, link);
        busEntity_ = null;
    }

    public void setServiceProvider(BusinessEntity be) {
      busEntity_ = be;
    }

    public BusinessEntity getServiceProvider() {
        return busEntity_;
    }

    public String getName() {
        return (busEntity_  == null) ? null : busEntity_.getDefaultNameString();
    }

    public String getDescription() {
        return (busEntity_ == null) ? null : busEntity_.getDefaultDescriptionString();
    }

    public String getUDDILinkInquiryAPI() {
        BusinessDescription bd = getValidUDDIBusinessDescription();
        return (bd == null) ? null : bd.getLocation();
    }

    public String getUDDILinkBusinessKey() {
        BusinessDescription bd = getValidUDDIBusinessDescription();
        return (bd == null) ? null : bd.getBusinessKey().getText();
    }

    public String getUDDILinkDiscoveryURL() {
        BusinessDescription bd = getValidUDDIBusinessDescription();
        if (bd == null)
            return null;
        else {
            DiscoveryURL discoveryURL = bd.getDiscoveryURL();
            return (discoveryURL == null) ? null : discoveryURL.getText();
        }
    }

    public boolean validateUDDILink() {
        BusinessDescription bd = getValidUDDIBusinessDescription();
        return (bd != null);
    }

    private BusinessDescription getValidUDDIBusinessDescription() {
        ExtensionElement extElement = link_.getExtensionElement();
        // The extension element of a UDDI link
        // must be a BusinessDescription element
        if (!(extElement instanceof BusinessDescription))
            return null;
        // A valid BusinessDescription must have an inquiry API
        String inquiryAPI = ((BusinessDescription)extElement).getLocation();
        if (!Validator.validateURL(inquiryAPI))
            return null;
        // A valid BusinessDescription must have a business key
        String businessKey = ((BusinessDescription)extElement).getBusinessKey().getText();
        if (businessKey == null || businessKey.length() <= 0)
            return null;
        return (BusinessDescription)extElement;
    }

    public String toString() {
        return getName();
    }
}
