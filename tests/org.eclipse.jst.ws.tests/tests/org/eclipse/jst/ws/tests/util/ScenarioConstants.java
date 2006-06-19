/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.tests.util;

public class ScenarioConstants {
	// Bottom up Java or EJB to Web Service wizard ID.
	public static final String WIZARDID_BOTTOM_UP = "org.eclipse.jst.ws.creation.ui.wizard.serverwizard";
	
	// Top down WSDL to Skeleton Web Service wizard ID.
	public static final String WIZARDID_TOP_DOWN = "org.eclipse.jst.ws.creation.ui.wizard.serverwizard";
	
	// Web Service client wizard ID.
	public static final String WIZARDID_CLIENT = "org.eclipse.jst.ws.internal.consumption.ui.wizard.client.clientwizard";	
	
	// Object class for:
	// 1) Java source (bottom-up)
	// 2) WSDL or WSIL (top-down or client).
	public static final String OBJECT_CLASS_ID_IFILE = "org.eclipse.core.resources.IFile";
	
	// Object class for Java compilation units (i.e. Java classes selected in the Java perspective).
	public static final String OBJECT_CLASS_ID_COMPILATIONUNIT = "org.eclipse.jdt.internal.core.CompilationUnit";
	
	// Object class for a service selected in the J2EE perspective (top-down or client).
	public static final String OBJECT_CLASS_ID_SERVICEIMPL = "org.eclipse.wst.wsdl.Service";
	
	// Object class for a WSDLResourceImpl (top-down or client).
	public static final String OBJECT_CLASS_ID_WSDLSERVICEIMPL = "org.eclipse.wst.wsdl.internal.util.WSDLResourceImpl";
	
	// Object class for a ServiceRef (top-down or client).
	public static final String OBJECT_CLASS_ID_SERVICEREF = "org.eclipse.jst.j2ee.webservice.wsclient.ServiceRef";
	
	// Object class for a ServiceImplBean (bottom-up).
	public static final String OBJECT_CLASS_ID_SERVICEIMPLBEAN = "org.eclipse.jst.j2ee.webservice.wsdd.ServiceImplBean";
	
	// Object class for a BeanLink (bottom-up).
	public static final String OBJECT_CLASS_ID_BEANLINK = "org.eclipse.jst.j2ee.webservice.wsdd.BeanLink";
}