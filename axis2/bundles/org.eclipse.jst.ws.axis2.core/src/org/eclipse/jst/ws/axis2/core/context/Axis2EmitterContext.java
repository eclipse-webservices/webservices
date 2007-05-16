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

public interface Axis2EmitterContext {
	
	/**
	 * This constant string is used to lookup the runtime location general
	 * preference from the plugins local preferences store.
	 */
	public static final String PREFERENCE_AXIS2_RUNTIME_LOCATION = "axis2RuntimeLocation";
	
	/**
	 * @param selection set the axis2 runtime location.
	 */
	public void setAxis2RuntimeLocation(String runtimeLocation);
	
	/**
	 * @param returns the axis2 runtime location.
	 */
	public String getAxis2RuntimeLocation();

}
