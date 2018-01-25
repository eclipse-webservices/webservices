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
 * 20070419 182864   gilberta@ca.ibm.com - Gilbert Andrews
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import java.util.Vector;

import org.uddi4j.UDDIException;
import org.uddi4j.response.DispositionReport;
import org.uddi4j.response.Result;

public class UDDIExceptionHandler {

	public static boolean requiresReset(UDDIException e){
		
    	DispositionReport dp = e.getDispositionReport();
    	Vector rv = dp.getResultVector();
    	for(int i = 0;i < rv.size();i++){
    		Result result = (Result)rv.get(i);
    		if(result.getErrno().equals("10140"))
    			 return true;
    	}
    	return false;
	}
}
