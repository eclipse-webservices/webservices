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
 * 20091013   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.ui.internal.project.facet;

import org.eclipse.jst.ws.jaxrs.core.internal.project.facet.IJAXRSFacetInstallDataModelProperties;
import org.eclipse.jst.ws.jaxrs.ui.internal.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelProvider;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.internal.datamodel.ui.DataModelWizardPage;
import org.eclipse.wst.common.project.facet.ui.IFacetWizardPage;
import org.eclipse.wst.common.project.facet.ui.IWizardContext;

/**
 * JAXRS Facet installation wizard page.
 * 
 */
@SuppressWarnings("restriction")
public class JAXRSFacetInstallPage extends DataModelWizardPage implements
		IJAXRSFacetInstallDataModelProperties, IFacetWizardPage {

	public JAXRSFacetInstallPage() {
		super(DataModelFactory.createDataModel(new AbstractDataModelProvider() {
		}), "jaxrs.facet.install.page"); //$NON-NLS-1$
		setTitle(Messages.JAXRSFacetInstallPage_title);
		setDescription(Messages.JAXRSFacetInstallPage_description);

	}

	@Override
	protected Composite createTopLevelComposite(Composite arg0) {
		// TODO Auto-generated method stub
		return new Composite(arg0, SWT.NONE);
	}

	@Override
	protected String[] getValidationPropertyNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setConfig(Object arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setWizardContext(IWizardContext arg0) {
		// TODO Auto-generated method stub
		
	}

	public void transferStateToConfig() {
		// TODO Auto-generated method stub
		
	}

}
