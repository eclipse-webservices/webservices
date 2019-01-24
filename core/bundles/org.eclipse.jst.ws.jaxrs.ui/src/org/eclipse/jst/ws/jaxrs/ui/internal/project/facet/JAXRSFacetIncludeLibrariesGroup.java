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
 * 20100310   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 * 20100512   312640 kchong@ca.ibm.com - JAX-RS facet - Extraneous spacing needs to be removed.
 * 
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.ui.internal.project.facet;

import org.eclipse.jst.ws.jaxrs.ui.internal.JAXRSUIPlugin;
import org.eclipse.jst.ws.jaxrs.ui.internal.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

public class JAXRSFacetIncludeLibrariesGroup extends Composite {

	private Button btnDeployJars;
	private Button btnSharedLibrary;
	Button copyOnPublishCheckBox;
	private Composite includeLibRadiosComposite;

	public JAXRSFacetIncludeLibrariesGroup(Composite parent, int style) {
		super(parent, style);
		
		// Set layout of this composite so its children will expand to fill available space.
        GridLayout gridLayout = new GridLayout(1, true);
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        gridLayout.verticalSpacing = 0;
        gridLayout.horizontalSpacing = 0;
        setLayout(gridLayout);
        setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // Children should have 'this' composite as their parent
	    copyOnPublishCheckBox = new Button(this, SWT.CHECK);
	    copyOnPublishCheckBox.setText(Messages.JAXRSLibraryConfigControl_IncludeGroupLabel);
	    includeLibRadiosComposite = new Composite(this, SWT.NONE);
	    GridLayout gridlayout = new GridLayout();
	    gridlayout.numColumns = 1;
	    gridlayout.marginTop = 0;
	    gridlayout.marginBottom = 0;
	    gridlayout.marginRight = 0;
	    gridlayout.marginLeft = 10;

	    includeLibRadiosComposite.setLayout(gridlayout);
	    GridData griddata = new GridData(GridData.FILL_HORIZONTAL);
	    includeLibRadiosComposite.setLayoutData(griddata);

	    btnDeployJars = createRadioButton(includeLibRadiosComposite, Messages.JAXRSLibraryConfigControl_DeployButtonLabel, Messages.JAXRSLibraryConfigControl_DeployJAR, null);

	    btnSharedLibrary = createRadioButton(includeLibRadiosComposite, Messages.JAXRSLibraryConfigControl_SharedLibButtonLabel, Messages.JAXRSLibraryConfigControl_TooltipIncludeAsSharedLib, null);

	    copyOnPublishCheckBox.addSelectionListener(new SelectionAdapter()
	    {
	      public void widgetSelected(final SelectionEvent event)
	      {
	        boolean selection = copyOnPublishCheckBox.getSelection();
	        btnDeployJars.setEnabled(selection);
	        btnSharedLibrary.setEnabled(selection);

	      }
	    });
	    // Need to initialize this properly
	    btnDeployJars.setSelection(true);

	}

	public Button getBtnDeployJars() {
		return btnDeployJars;
	}

	public Button getBtnSharedLibrary() {
		return btnSharedLibrary;
	}

	public Button getCopyOnPublishCheckBox() {
		return copyOnPublishCheckBox;
	}

	public Composite getIncludeLibRadiosComposite() {
		return includeLibRadiosComposite;
	}

	private Button createButton(int kind, Composite parent, String labelName, String tooltip, String infopop)
	  {
	    Button button = new Button(parent, kind);
	
	    tooltip = tooltip == null ? labelName : tooltip;
	    button.setText(labelName);
	    button.setToolTipText(tooltip);
	
	    if (infopop != null)
	      PlatformUI.getWorkbench().getHelpSystem().setHelp(button, JAXRSUIPlugin.PLUGIN_ID + "." + infopop);
	
	    return button;
	  }

	private Button createCheckbox(Composite parent, String labelName, String tooltip, String infopop)
	  {
	    return createButton(SWT.CHECK, parent, labelName, tooltip, infopop);
	  }

	private Button createRadioButton(Composite parent, String labelName, String tooltip, String infopop)
	  {
	    return createButton(SWT.RADIO, parent, labelName, tooltip, infopop);
	  }



}
