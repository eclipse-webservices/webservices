/*******************************************************************************
 * Copyright (c) 2007 WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070222  168766 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  facet to the framework for 168766
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.facet.model;

public class FacetModel {
	
	private static String webFacetConinerDir;

	public static String getWebFacetConinerDir() {
		return webFacetConinerDir;
	}

	public static void setWebFacetConinerDir(String inputWebFacetConinerDir) {
		webFacetConinerDir = inputWebFacetConinerDir;
	}

}
