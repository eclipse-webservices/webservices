/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20100303   291954 kchong@ca.ibm.com - Keith Chong, JAX-RS: Implement JAX-RS Facet
 * 20100428   310905 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS facet fails to install due to NPE or runtime exception due to duplicate cp entries
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.ui.internal.project.facet;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jst.ws.jaxrs.core.internal.project.facet.IJAXRSFacetInstallDataModelProperties;
import org.eclipse.jst.ws.jaxrs.ui.internal.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

public class ServletInformationGroup extends Composite {

	private Group servletInfo;
	private Label lblJAXRSServletName;
	public Text txtJAXRSServletName;
	private Label lblJAXRSServletClassName;
	public Text txtJAXRSServletClassName;
	private Label lblJAXRSServletURLPatterns;
	public List lstJAXRSServletURLPatterns;
	public Button btnAddPattern;
	public Button btnRemovePattern;

	private IDataModel optionalModel;
	
	private IDialogSettings dialogSettings;
	private IDataModel webAppDataModel;
	private String sEARProject = null;
	private String sWEBProject = null;
	private String sTargetRuntime = null;
	private boolean bAddToEAR = false;
	private static final String SETTINGS_SERVLET = "servletName"; //$NON-NLS-1$
	private static final String SETTINGS_SERVLET_CLASSNAME = "servletClassname"; //$NON-NLS-1$
	private static final String SETTINGS_URL_MAPPINGS = "urlMappings"; //$NON-NLS-1$
	private static final String SETTINGS_URL_PATTERN = "pattern"; //$NON-NLS-1$
//	private Composite composite = null;
	
	public ServletInformationGroup(Composite parent, int style) {
		super(parent, style);

        GridLayout gridLayout = new GridLayout(1, true);
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        gridLayout.verticalSpacing = 0;
        gridLayout.horizontalSpacing = 0;
        setLayout(gridLayout);
        setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        servletInfo = new Group(this, SWT.NONE);
		GridData servletGD = new GridData(GridData.FILL_HORIZONTAL);
		servletInfo.setLayoutData(servletGD);
		servletInfo.setLayout(new GridLayout(3, false));
		servletInfo.setText(Messages.JAXRSFacetInstallPage_JAXRSServletLabel);

		lblJAXRSServletName = new Label(servletInfo, SWT.NONE);
		lblJAXRSServletName
				.setText(Messages.JAXRSFacetInstallPage_JAXRSServletNameLabel);
		lblJAXRSServletName.setLayoutData(new GridData(GridData.BEGINNING));

		txtJAXRSServletName = new Text(servletInfo, SWT.BORDER);
		GridData gd2 = new GridData(GridData.FILL_HORIZONTAL);
		gd2.horizontalSpan = 2;
		txtJAXRSServletName.setLayoutData(gd2);

		lblJAXRSServletClassName = new Label(servletInfo, SWT.NONE);
		lblJAXRSServletClassName
				.setText(Messages.JAXRSFacetInstallPage_JAXRSServletClassNameLabel);
		lblJAXRSServletClassName
				.setLayoutData(new GridData(GridData.BEGINNING));

		txtJAXRSServletClassName = new Text(servletInfo, SWT.BORDER);
		GridData gd2c = new GridData(GridData.FILL_HORIZONTAL);
		gd2c.horizontalSpan = 2;
		txtJAXRSServletClassName.setLayoutData(gd2c);

		lblJAXRSServletURLPatterns = new Label(servletInfo, SWT.NULL);
		lblJAXRSServletURLPatterns
				.setText(Messages.JAXRSFacetInstallPage_JAXRSURLMappingLabel);
		lblJAXRSServletURLPatterns.setLayoutData(new GridData(
				GridData.BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING));
		lstJAXRSServletURLPatterns = new List(servletInfo, SWT.BORDER);
		GridData gd3 = new GridData(GridData.FILL_BOTH);
//		gd3.heightHint = convertHeightInCharsToPixels(5);
		lstJAXRSServletURLPatterns.setLayoutData(gd3);
		lstJAXRSServletURLPatterns.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				btnRemovePattern.setEnabled(lstJAXRSServletURLPatterns
						.getSelectionCount() > 0);
			}
		});

		Composite btnComposite = new Composite(servletInfo, SWT.NONE);
		GridLayout gl = new GridLayout(1, false);
		gl.marginLeft = 0;
		btnComposite.setLayout(gl);
		btnComposite.setLayoutData(new GridData(GridData.END
				| GridData.VERTICAL_ALIGN_FILL));

		btnAddPattern = new Button(btnComposite, SWT.NONE);
		btnAddPattern.setText(Messages.JAXRSFacetInstallPage_Add2);
		btnAddPattern.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_BEGINNING));
		btnAddPattern.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				InputDialog dialog = new InputDialog(getShell(),
						Messages.JAXRSFacetInstallPage_PatternDialogTitle,
						Messages.JAXRSFacetInstallPage_PatternDialogDesc, null,
						new IInputValidator() {

							public String isValid(String newText) {
								return isValidPattern(newText);
							}

						});
				dialog.open();
				if (dialog.getReturnCode() == Window.OK) {
					addItemToList(dialog.getValue(), true);
				}
			}
		});

		btnRemovePattern = new Button(btnComposite, SWT.NONE);
		btnRemovePattern.setText(Messages.JAXRSFacetInstallPage_Remove);
		btnRemovePattern.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_BEGINNING));
		btnRemovePattern.setEnabled(false);
		btnRemovePattern.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				removeItemFromList(lstJAXRSServletURLPatterns.getSelection());
				btnRemovePattern.setEnabled(false);
			}
		});

	}

	
	
	
	private String isValidPattern(String value) {
		if (value == null || value.trim().equals("")) //$NON-NLS-1$
			return Messages.JAXRSFacetInstallPage_PatternEmptyMsg;
		if (lstJAXRSServletURLPatterns.indexOf(value) >= 0)
			return Messages.JAXRSFacetInstallPage_PatternSpecifiedMsg;

		return null;
	}

	private void addItemToList(String pattern, boolean selectMe) {
		lstJAXRSServletURLPatterns.add(pattern == null ? "" : pattern); //$NON-NLS-1$
		if (pattern == null && selectMe)
			lstJAXRSServletURLPatterns.setSelection(lstJAXRSServletURLPatterns
					.getItemCount() - 1);
		// When 119321 is fixed - remove code below
		updateModelForURLPattern();
	}
	
	private void updateModelForURLPattern() {
		if (optionalModel != null)
		  optionalModel.setProperty(
				IJAXRSFacetInstallDataModelProperties.SERVLET_URL_PATTERNS,
				lstJAXRSServletURLPatterns.getItems());
	}
	
	private void removeItemFromList(String[] selection) {
		for (int i = 0; i < selection.length; i++) {
			String sel = selection[i];
			lstJAXRSServletURLPatterns.remove(sel);
		}
		// When 119321 is fixed - remove code below
		updateModelForURLPattern();
	}

	
	public void setDataModel(IDataModel model)
	{
	   this.optionalModel = model;
	}




	public void setFieldsEnabled(boolean selection) {
		btnAddPattern.setEnabled(selection);
		btnRemovePattern.setEnabled(selection);
		txtJAXRSServletClassName.setEnabled(selection);
		txtJAXRSServletName.setEnabled(selection);
		lblJAXRSServletClassName.setEnabled(selection);
		lblJAXRSServletName.setEnabled(selection);
		lblJAXRSServletURLPatterns.setEnabled(selection);
		lstJAXRSServletURLPatterns.setEnabled(selection);
		
	}
}
