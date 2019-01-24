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

public class PublicUDDIRegistryNTTComm implements PublicUDDIRegistryType
{

    // Copyright
    public static final String copyright = "(c) Copyright IBM Corporation 2002.";

    public PublicUDDIRegistryNTTComm() {
    }

    public String getName() {
        return PluginMessages.PUBLICUDDIREGISTRYTYPE_NAME_NTTCOMM;
    }

    public String getInquiryURL() {
        return "http://www.uddi.ne.jp/ubr/inquiryapi";
    }

    public String getPublishURL() {
        return "https://www.uddi.ne.jp/ubr/publishapi";
    }

    public String getRegistrationURL() {
        return "http://www.ntt.com/uddi/";
    }

}
