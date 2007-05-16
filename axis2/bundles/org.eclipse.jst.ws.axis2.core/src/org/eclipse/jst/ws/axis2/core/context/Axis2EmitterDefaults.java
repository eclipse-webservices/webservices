/*******************************************************************************
 * Copyright (c) 2007 WSO2 Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070516   183147 sandakith@wso2.com - Lahiru Sandakith Fix for the persisting DBCS paths
 *******************************************************************************/ 
package org.eclipse.jst.ws.axis2.core.context;

public class Axis2EmitterDefaults {
	
	public static final String PREFERENCE_AXIS2_RUNTIME_LOCATION_DEFAULT = "";

	 /**
	  * @return returns the default setting for runtime location.
	  */
	 public static String getAxis2RuntimeLocation() {
		 return PREFERENCE_AXIS2_RUNTIME_LOCATION_DEFAULT;
	 }
	
}
