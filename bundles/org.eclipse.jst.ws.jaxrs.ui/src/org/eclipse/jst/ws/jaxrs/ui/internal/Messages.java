/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20091021   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 *******************************************************************************/package org.eclipse.jst.ws.jaxrs.ui.internal;

import org.eclipse.osgi.util.NLS;

/**
 * String resource handler.
 * 
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.jst.ws.jaxrs.ui.internal.messages"; //$NON-NLS-1$

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	public static String JAXRSFacetInstallPage_JAXRSLibraryLabel0;
	public static String JAXRSFacetInstallPage_title;
	public static String JAXRSFacetInstallPage_description;
	public static String JAXRSFacetInstallPage_JAXRSImplLabel;
	public static String JAXRSFacetInstallPage_JAXRSServletLabel;
	public static String JAXRSFacetInstallPage_Add2;
	public static String JAXRSFacetInstallPage_JAXRSServletNameLabel;
	public static String JAXRSFacetInstallPage_JAXRSServletClassNameLabel;
	public static String JAXRSFacetInstallPage_JAXRSURLMappingLabel;
	public static String JAXRSFacetInstallPage_PatternDialogTitle;
	public static String JAXRSFacetInstallPage_PatternDialogDesc;
	public static String JAXRSFacetInstallPage_Remove;
	public static String JAXRSFacetInstallPage_PatternEmptyMsg;
	public static String JAXRSFacetInstallPage_PatternSpecifiedMsg;
	public static String JAXRSFacetInstallPage_ErrorNoWebAppDataModel;
	public static String JAXRSLibrariesPreferencePage_DEFAULT_IMPL_DESC;
	public static String JAXRSLibrariesPreferencePage_DefinedJAXRSLibraries;
	public static String JAXRSLibrariesPreferencePage_MISSING_DESC;
	public static String JAXRSLibrariesPreferencePage_New;
	public static String JAXRSLibrariesPreferencePage_Edit;
	public static String JAXRSLibrariesPreferencePage_Remove;
	public static String JAXRSLibrariesPreferencePage_CannotRemovePluginProvidedTitle;
	public static String JAXRSLibrariesPreferencePage_CannotRemovePluginProvidedMessage;
	public static String JAXRSLibrariesPreferencePage_MakeDefault;
	public static String JAXRSLibrariesPreferencePage_Description;
	public static String JAXRSLibrariesPreferencePage_CannotModifyPluginProvidedTitle;
	public static String JAXRSLibrariesPreferencePage_CannotModifyPluginProvidedMessage;
	public static String JAXRSLibraryConfigControl_Library;
	public static String JAXRSLibraryConfigControl_IncludeGroupLabel;
	public static String JAXRSLibraryConfigControl_DeployButtonLabel;
	public static String JAXRSLibraryConfigControl_DeployJAR;
	public static String JAXRSLibraryConfigControl_SharedLibButtonLabel;
	public static String JAXRSLibraryConfigControl_TooltipIncludeAsSharedLib;
	public static String JAXRSLibraryConfigControl_ImplementationLibrary;
	public static String JAXRSLibraryConfigControl_NewImplButtonTooltip;
	public static String JAXRSLibraryConfigControl_NewImplementationLibrary;
	public static String JAXRSLibraryConfigControl_NullProject;
	public static String JAXRSLibraryContainerWizardPage_PageName;
	public static String JAXRSLibraryContainerWizardPage_Title;
	public static String JAXRSLibraryContainerWizardPage_Description;
	public static String JAXRSLibraryContainerWizardPage_WarningNoJAXRSFacet;
	public static String JAXRSLibraryContainerWizardPage_JAXRSLibraries;
	public static String JAXRSLibraryContainerWizardPage_Add;
	public static String JAXRSLibraryContainerWizardPage_Edit;
	public static String JAXRSLibraryContainerWizardPage_ImplAlreadyPresent;
	public static String JAXRSLibraryContainerWizardPage_EditLibrary_DescriptionText;
	public static String JAXRSLibraryWizard_DESCRIPTION;
	public static String JAXRSLibraryWizard_CreateImplementation;
	public static String JAXRSLibraryWizard_CreateJAXRSLibrary;
	public static String JAXRSLibraryWizard_EditJAXRSLibrary;
	public static String JAXRSLibraryWizard_JAXRSLibrary;
	public static String JAXRSLibraryWizard_LibraryName;
	public static String JAXRSLibraryWizard_LibraryJars;
	public static String JAXRSLibraryWizard_DeployJars;
	public static String JAXRSLibraryWizard_Add;
	public static String JAXRSLibraryWizard_Remove;
	public static String JAXRSLibraryWizard_ExtJarFileDialogTitle;
	public static String JAXRSLibraryWizard_ValidateNoJars;
	public static String JAXRSLibraryWizard_ValidateNoLibraryName;
	public static String JAXRSLibraryWizard_ValidateExistingLibraryName;
}
