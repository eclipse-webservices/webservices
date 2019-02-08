/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel;

import java.util.Vector;
import org.apache.wsil.Description;
import org.apache.wsil.Service;
import org.apache.wsil.extension.ExtensionElement;
import org.apache.wsil.extension.wsdl.ImplementedBinding;
import org.apache.wsil.extension.wsdl.Reference;
import org.apache.wsil.extension.wsdl.ReferencedService;
import org.eclipse.wst.ws.internal.datamodel.Model;

/**
* The data model element that represents 
* a WSDL service in a WSIL document
*/
public class WsilWsdlServiceElement extends WsilServiceElement
{
    public WsilWsdlServiceElement(String name, Model model, Service service) {
        super(name, model, service);
    }

    public String getWSDLServiceURL() {
        Description desc = getValidWSDLDescription();
        return (desc == null) ? null : makeAbsolute(desc.getLocation());
    }

    public Vector getWSDLBinding() {
        Vector v = new Vector();
        Description[] descList = service_.getDescriptions();
        for (int i = 0; i < descList.length; i++) {
            ExtensionElement extElement = descList[i].getExtensionElement();
            if (extElement != null && extElement instanceof Reference) {
                ReferencedService refService = ((Reference)extElement).getReferencedService();
                if (refService != null) {
                    v.add(refService.getReferencedServiceName());
                }
                ImplementedBinding[] implBindingList = ((Reference)extElement).getImplementedBindings();
                for (int j = 0; j < implBindingList.length; j++) {
                    v.add(implBindingList[j].getBindingName());
                }
            }
        }
        return v;
    }
    
    private Description getValidWSDLDescription() {
        Description[] descList = service_.getDescriptions();
        for (int i = 0; i < descList.length; i++) {
            // skip this description if the locatioin attribute is a malformed URL
            String location = descList[i].getLocation();
            if (location == null || location.length() <= 0)
                continue;
            // skip this description if the extension element indicates
            // that the description's location contains only the wsdl binding
            ExtensionElement extElement = descList[i].getExtensionElement();
            if (extElement != null && extElement instanceof Reference) {
                Boolean endpointPresent = ((Reference)extElement).getEndpointPresent();
                if (endpointPresent != null && endpointPresent.booleanValue() == false)
                    continue;
            }
            // a valid description is found
            return descList[i];
        }
        // no valid description is found for this service
        return null;
    }

    public boolean validateWSDLService() {
        Description desc = getValidWSDLDescription();
        return (desc != null);
    }

    public String toString() {
        return getWSDLServiceURL();
    }
}
