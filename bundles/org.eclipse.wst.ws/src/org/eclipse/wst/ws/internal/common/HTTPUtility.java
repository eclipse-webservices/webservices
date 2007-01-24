/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070124   167487 gilberta@ca.ibm.com - Gilbert Andrews
 *******************************************************************************/
package org.eclipse.wst.ws.internal.common;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HTTPUtility {

	public String handleRedirect(String urlString){
		URLConnection conn = null;
		String urlRedirect = urlString;
		int time = 0;
		while(urlRedirect!= null){
			if (time == 6) return urlRedirect;
			try{
				URL url = new URL(urlRedirect);
				conn = url.openConnection();
			}catch(Exception exc){
				return urlRedirect;
			}
			if (conn instanceof HttpURLConnection)
				{
				HttpURLConnection http = (HttpURLConnection) conn;
				http.setInstanceFollowRedirects(false);
	         
				try{
					int code = http.getResponseCode();
					if (code >= 300 && code <= 307 && code != 306 &&
	        			 code != HttpURLConnection.HTTP_NOT_MODIFIED)
					{
						String urlRedirect2 = changeSlash(http.getHeaderField("Location"));
	           	 		if (urlRedirect2 == null || urlRedirect.equals(urlRedirect2)) return urlRedirect;
	          	 		else urlRedirect = urlRedirect2;
					}else return urlRedirect;
				
				
				}catch(IOException exc){
					return urlRedirect; 
				}
			}
			else return urlRedirect;
			time++;
		}
		return urlString;
	}

	public String changeSlash(String url){
		String temp = "";
		try{
			temp = url.replace('\\', '/');
		}catch(Exception exc){
			return url;
		}
		return temp;
	}
}
