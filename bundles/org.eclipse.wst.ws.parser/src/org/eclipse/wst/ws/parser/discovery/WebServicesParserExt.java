/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.wst.ws.parser.discovery;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.Security;

import org.eclipse.wst.ws.parser.wsil.WebServicesParser;

import com.ibm.jsse.JSSEProvider;

public class WebServicesParserExt extends WebServicesParser {
    // Initialize the SSL provider subsystem.
    static {
        Security.addProvider(new JSSEProvider());
        System.setProperty("java.protocol.handler.pkgs", "com.ibm.net.ssl.internal.www.protocol");
    }

    public WebServicesParserExt() {
        super();
    }

    public WebServicesParserExt(String uri) {
        super(uri);
    }

    protected URL createURL(String url) throws MalformedURLException {
        return NetUtils.createURL(url);
    }
}