/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.utils.internal.text;

import org.eclipse.osgi.util.NLS;

public class JaxWsUtilMessages extends NLS
{
	private static final String BUNDLE_NAME = "org.eclipse.jst.ws.jaxws.utils.internal.text.JaxWsUtilMessages"; //$NON-NLS-1$
	
	
	public static String CannotPerformEditMsg;


	public static String CompUnitMissingMsg;


	public static String InvalidTreeStateMsg;


	public static String AnnotationCannotBeStoredMsg;


	public static String AnnotationUtils_ParamShouldNotBeNullMsg;

//
//	public static String EngineDataProvider_UnableToDeterminePortOfDeployServerMsg;
//	
//	public static String EngineDataProvider_UnableToDetermineNameOfDeployServerMsg;
//
//	public static String EngineDataProvider_UnableToDetermineSapSystemOfDeployServerMsg;
//
//
	/** message constant */
	public static String WsdlNamesValidator_EmptyXmlName;
	
	/** message constant */
	public static String WsdlNamesValidator_InvalidNCName;
	
	/** message constant */
	public static String WsdlNamesValidator_EmptyXmlName2;
	
	/** message constant */
	public static String WsdlNamesValidator_InvalidNCName2;	
	
	public static String EditResourcesManager_FILE_CONTENTS_CHANGE_FAILED_MSG;
	
	public static String ProjectManagementUtils_ProjectHasNoMetaInfFolderMsg;
	
	static
	{
		NLS.initializeMessages(BUNDLE_NAME, JaxWsUtilMessages.class);
	}
}
