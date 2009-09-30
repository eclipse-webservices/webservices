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
package org.eclipse.jst.ws.jaxws.dom.runtime.validation.messages;

import org.eclipse.osgi.util.NLS;

public class ValidationMessages extends NLS
{
	private static final String BUNDLE_NAME = "org.eclipse.jst.ws.jaxws.dom.runtime.validation.messages.ValidationMessages"; //$NON-NLS-1$

	public static String BottomUpProviderGenerator_FailedtoAnnotate;

	public static String BottomUpProviderGenerator_FailedToCollectMethods;

	public static String BottomUpProviderGenerator_MethodNotSupported;

	public static String BottomUpProviderGenerator_ProjectNotAccesible;

	public static String Ejb3TopDownProviderGenerator_InconsistentMetadata;

	public static String Ejb3TopDownProviderGenerator_InternalErrorInterface;

	public static String Ejb3TopDownProviderGenerator_InternalErrorOperation;

	public static String Ejb3TopDownProviderGenerator_InvalidInterfaceMapping;

	public static String Ejb3TopDownProviderGenerator_MappingsEmpty;

	public static String ProxyWorkaround_UnableToDeleteProxy;

	public static String ProxyWorkaround_UnableToRenameProxy;
	
	public static String Wsdl2JavaGenerator_ErrorDuringGeneration;

	public static String Wsdl2JavaGenerator_IncorrectURL;

	public static String Wsdl2JavaGenerator_PathInappropriate;

	public static String Wsdl2JavaGenerator_WSDLDownloadException;

	/** Message for not existing SEI */
	public static String WsValidation_SeiDoesNotExists;

	/** Message for non existing WSDL referenced by wsdlLocation */
	public static String WsValidation_WsdlDoesNotExists;
	
	/** On SEI portName, serviceName and endpointInterface should not be used */
	public static String SeiValidation_RedundantAttributes;
	
	/** Message for already existing method name in SEI */
	public static String WebMethodValidation_NameExists;

	/** Message for excluded methods inside SEI */
	public static String WebMethodValidation_MethodShouldNotBeExcluded;	
	
	/** Excluded method should contain only exclude attribute */
	public static String WebMethodValidation_ExcludedHasOtherAttributes;
	
	/** Message for used 'name' attribute while SEI is referenced */
	public static String WsValidation_NameShouldNotBeUsedWithSei;
	
	/** Message for duplicated web parameter name */
	public static String WebParamValidation_NameExists;
	
	/** Attribute 'name' is not needed due to current SOAPBinding configuration */
	public static String WebParamValidation_NameRedundant;
	
	/** Attribute 'name' is required */
	public static String WebParamValidation_NameRequired;
	
	public static String WsValidation_WsAnnotationOnNonSessionBean_Error;
	

	static
	{
		NLS.initializeMessages(BUNDLE_NAME, ValidationMessages.class);
	}
}
