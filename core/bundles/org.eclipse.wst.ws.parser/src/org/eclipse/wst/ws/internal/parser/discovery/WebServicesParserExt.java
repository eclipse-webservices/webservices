/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.parser.discovery;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;

public class WebServicesParserExt extends WebServicesParser {

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
