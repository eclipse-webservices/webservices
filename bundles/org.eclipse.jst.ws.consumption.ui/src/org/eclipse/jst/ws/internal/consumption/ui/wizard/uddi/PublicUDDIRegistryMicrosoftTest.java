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

import org.eclipse.wst.ws.parser.PluginMessages;

public class PublicUDDIRegistryMicrosoftTest implements PublicUDDIRegistryType
{

    // Copyright
    public static final String copyright = "(c) Copyright IBM Corporation 2002.";

    public PublicUDDIRegistryMicrosoftTest() {
    }

    public String getName() {
        return PluginMessages.PUBLICUDDIREGISTRYTYPE_NAME_MICROSOFT_TEST;
    }

    public String getInquiryURL() {
        return "http://test.uddi.microsoft.com/inquire";
    }

    public String getPublishURL() {
        return "https://test.uddi.microsoft.com/publish";
    }

    public String getRegistrationURL() {
        return "http://test.uddi.microsoft.com/register.aspx";
    }

}