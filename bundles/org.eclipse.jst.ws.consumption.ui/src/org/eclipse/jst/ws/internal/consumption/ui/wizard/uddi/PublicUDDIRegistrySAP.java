/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.wizard.uddi;

import org.eclipse.wst.ws.WSPlugin;

public class PublicUDDIRegistrySAP implements PublicUDDIRegistryType
{

    // Copyright
    public static final String copyright = "(c) Copyright IBM Corporation 2002.";

    public PublicUDDIRegistrySAP() {
    }

    public String getName() {
        return WSPlugin.getResourceString("%PUBLICUDDIREGISTRYTYPE_NAME_SAP");
    }

    public String getInquiryURL() {
        return "http://uddi.sap.com/UDDI/api/inquiry/";
    }

    public String getPublishURL() {
        return "https://uddi.sap.com/UDDI/api/publish/";
    }

    public String getRegistrationURL() {
        return "http://uddi.sap.com/";
    }

}
