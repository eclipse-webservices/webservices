/*******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
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

package org.eclipse.jst.ws.internal.consumption.ui.wizard.uddi;

import org.eclipse.wst.ws.parser.PluginMessages;

public class PublicUDDIRegistrySAP implements PublicUDDIRegistryType
{

    // Copyright
    public static final String copyright = "(c) Copyright IBM Corporation 2002.";

    public PublicUDDIRegistrySAP() {
    }

    public String getName() {
        return PluginMessages.PUBLICUDDIREGISTRYTYPE_NAME_SAP;
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
