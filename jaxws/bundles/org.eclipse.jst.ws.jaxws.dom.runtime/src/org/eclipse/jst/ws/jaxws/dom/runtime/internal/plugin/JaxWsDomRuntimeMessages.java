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
package org.eclipse.jst.ws.jaxws.dom.runtime.internal.plugin;

import org.eclipse.osgi.util.NLS;

public class JaxWsDomRuntimeMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.jst.ws.jaxws.dom.runtime.internal.plugin.messages"; //$NON-NLS-1$
	
	public static String AbstractClassNotImplementedException_Error_Message;
	public static String ConstructorNotExposableException_Error_Message;
	public static String EndpointTypeValidator_ERR_MSG_BINARYCLASS;
	public static String EndpointTypeValidator_ERR_MSG_NOTCLASS;
	public static String EndpointTypeValidator_ERR_MSG_ABSTRACT;
	public static String EndpointTypeValidator_ERR_MSG_CONSTRUCTOR;
	public static String EndpointTypeValidator_ERR_MSG_NOTPUBLIC;
	public static String EndpointTypeValidator_ERR_MSG_FINAL;
	public static String EndpointTypeValidator_ERR_MSG_HASFINALYZE;
	public static String HasInadmisableInnerTypesException_Error_Message;
	public static String InheritanceAndImplementationExecption_Error_Message;
	public static String InterfacesNotSupportedException_Error_Message;
	public static String MethodNotPublicException_Error_Message;
	public static String MultipleImplementationException_Error_Message;
	public static String RemoteObjectException_Error_Message;
	public static String WsDomStartupParticipant_Startup_Job_Message;
	public static String WorkspaceCUFinder_LOADING_CANCELED;
	public static String JAXWS_DOM_LOADING;
	
	public static String SeiImplementationValidator_METHOD_NOT_FOUND;
	public static String SeiImplementationValidator_METHOD_HASDIFFERENT_EXCEPTIONS;
	public static String SeiImplementationValidator_METHOD_NOT_PUBLIC;
	
	public static String SeiValidator_NOT_COMPILATIONUNIT_MSG;
	public static String SeiValidator_NOT_INTERFACE_MSG;
	public static String SeiValidator_NOT_OUTER_MSG;
	public static String SeiValidator_NOT_PUBLIC_MSG;
	public static String TypeNotFoundException_Error_Message;
	public static String TargetNsValidUriConstraint_URI_SYNTAX_ERROR;
	
	public static String DomValidationConstraintDescriptor_WS_DOM_VALIDATION_CONSTRAINT;

	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, JaxWsDomRuntimeMessages.class);
	}

	private JaxWsDomRuntimeMessages() {
	}
}
