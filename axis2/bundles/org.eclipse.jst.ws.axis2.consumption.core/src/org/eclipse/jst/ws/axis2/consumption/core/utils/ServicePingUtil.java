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
 * 20080116    sandakith@wso2.com - Lahiru Sandakith, Introduced for the Fix 209411
 *                                                    WSDL retrieval issue
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.consumption.core.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.eclipse.jst.ws.axis2.consumption.core.messages.Axis2ConsumptionUIMessages;

/**
 * Utility class for the service ping
 */
public class ServicePingUtil {
	/**
	 * WSDL Retrieve Timeout
	 */
	int timeTORetrieveWSDL = 10000 ; // Default is 10 sec 
	
	/**
	 * Utility method to connect to a particular URL
	 * @param url
	 */
	public void connectToURL(String urlString) throws Exception{
		URL url = new URL(urlString);
        long start = System.currentTimeMillis();
        while (true) {
            try {
                URLConnection urlCon;
                urlCon = url.openConnection();
                // TODO handle the HTTPS scenario as well. 
                if (urlCon instanceof HttpURLConnection) {
                	HttpURLConnection httpCon = (HttpURLConnection)urlCon;
                    httpCon.setDoOutput(true);
                    httpCon.setDoInput(true);
                    httpCon.setUseCaches(false);
                    httpCon.setRequestMethod("GET");
                    HttpURLConnection.setFollowRedirects(true);  
                    httpCon.setReadTimeout(timeTORetrieveWSDL);
                    httpCon.connect();
                    Thread.sleep(1000);
                    httpCon.disconnect();
				} else {
					// Do nothing 
					// Assume that this is in the local file system
				}
                break;
            } catch (IOException e) {
                if(System.currentTimeMillis() - start >= timeTORetrieveWSDL){
                    throw new IOException(Axis2ConsumptionUIMessages.ERROR_WSDL_PING_TIME_OUT);
                }
                Thread.sleep(1000);
            }
        }
	}

	/**
	 * Set the WSDL read timeout 
	 * @param timeTORetrieveWSDL 
	 */
	public void setTimeTORetrieveWSDL(int timeTORetrieveWSDL) {
		this.timeTORetrieveWSDL = timeTORetrieveWSDL;
	}

}
