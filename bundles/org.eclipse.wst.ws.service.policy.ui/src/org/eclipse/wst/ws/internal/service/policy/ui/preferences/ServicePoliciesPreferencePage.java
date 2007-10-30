/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20071025          ericdp@ca.ibm.com - Eric Peters
 *******************************************************************************/
package org.eclipse.wst.ws.internal.service.policy.ui.preferences;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.wst.ws.service.policy.IServicePolicy;
import org.eclipse.wst.ws.service.policy.ServicePolicyPlatform;
import org.eclipse.wst.ws.service.policy.ui.ServicePolicyActivatorUI;
import org.osgi.framework.Bundle;

public class ServicePoliciesPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage, SelectionListener

{
	Composite parentComposite;
	IWorkbenchHelpSystem helpSystem;
	Composite masterComposite;
	Composite detailsComposite;
	private Tree masterPrefTree;
	private Tree detailsPrefTree;
	private Text text_DetailsPanel_description;
	private Text text_DetailsPanel_dependencies;
	private Label label_DetailsPanel_description;
	private Label label_detailsPanel_dependancies;
	private static final Image folderImage = ServicePolicyActivatorUI
			.getImageDescriptor("icons/full/obj16/fldr_obj.gif").createImage(); //$NON-NLS-1$
	private static final Image leafImage = ServicePolicyActivatorUI
			.getImageDescriptor("icons/full/obj16/file_obj.gif").createImage(); //$NON-NLS-1$

	/**
	 * Creates preference page controls on demand.
	 *   @param parentComposite  the parent for the preference page
	 */
	protected Control createContents(Composite superparent) {

		helpSystem = PlatformUI.getWorkbench().getHelpSystem();

		parentComposite = new Composite(superparent, SWT.NONE);
		GridLayout parentLayout = new GridLayout();
		parentLayout.numColumns = 2;
		parentLayout.horizontalSpacing = 0;
		parentComposite.setLayout(parentLayout);
		parentComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		masterComposite = new Composite(parentComposite, SWT.NONE);
		GridLayout masterLayout = new GridLayout();
		masterLayout.numColumns = 1;
		masterLayout.horizontalSpacing = 0;
		masterComposite.setLayout(masterLayout);
		masterComposite.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		detailsComposite = new Composite(parentComposite, SWT.NONE);
		GridLayout detailsLayout = new GridLayout();
		detailsLayout.numColumns = 1;
		detailsLayout.horizontalSpacing = 0;
		detailsComposite.setLayout(detailsLayout);
		detailsComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		masterPrefTree = new Tree(masterComposite, SWT.BORDER);
		masterPrefTree.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		masterPrefTree.addSelectionListener(this);
		detailsPrefTree = new Tree(detailsComposite, SWT.BORDER);
		detailsPrefTree.addSelectionListener(this);
		GridData detailsPrefTreeGD = new GridData(
				GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		detailsPrefTreeGD.heightHint = 200;
		detailsPrefTreeGD.widthHint = 400;
		detailsPrefTree.setLayoutData(detailsPrefTreeGD);
		detailsPrefTree.setVisible(false);

		ServicePolicyPlatform platform = ServicePolicyPlatform.getInstance();
		List<IServicePolicy> policyList = platform.getRootServicePolicies(null);

		for (IServicePolicy policy : policyList) {

			try {
				addPolicy(policy, masterPrefTree, true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		label_DetailsPanel_description = new Label(detailsComposite, SWT.NONE);

		label_DetailsPanel_description
				.setText(WstSPUIPluginMessages.LABEL_SERVICEPOLICIES_DESCRIPTION);
		label_DetailsPanel_description
				.setToolTipText(WstSPUIPluginMessages.TOOLTIP_PSP_DESCRIPTION);

		text_DetailsPanel_description = new Text(detailsComposite, SWT.WRAP
				| SWT.BORDER | SWT.V_SCROLL | SWT.READ_ONLY);
		GridData detailsGD = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL);
		detailsGD.heightHint = 50;
		detailsGD.widthHint = 400;
		text_DetailsPanel_description.setLayoutData(detailsGD);

		label_detailsPanel_dependancies = new Label(detailsComposite, SWT.NONE);

		label_detailsPanel_dependancies
				.setText(WstSPUIPluginMessages.LABEL_SERVICEPOLICIES_DEPENDENCIES);
		label_detailsPanel_dependancies
				.setToolTipText(WstSPUIPluginMessages.TOOLTIP_PSP_DESCRIPTION);

		text_DetailsPanel_dependencies = new Text(detailsComposite, SWT.WRAP
				| SWT.BORDER | SWT.V_SCROLL | SWT.READ_ONLY);
		GridData dependenciesGD = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL);
		dependenciesGD.heightHint = 100;
		dependenciesGD.widthHint = 400;
		text_DetailsPanel_dependencies.setLayoutData(dependenciesGD);
		initializeValues();
		org.eclipse.jface.dialogs.Dialog.applyDialogFont(superparent);

		return parentComposite;
	}

	private Image getImage(IServicePolicy sp) throws IOException {
		Image i = (sp.getChildren().size() == 0) ? leafImage : folderImage;

		if (sp.getDescriptor() != null) {
			String iconPathBundleID = null;
			String iconPath = null;
			iconPathBundleID = sp.getDescriptor().getIconBundleId();
			iconPath = sp.getDescriptor().getIconPath();
			if (iconPathBundleID != null && iconPath != null
					&& iconPathBundleID.length() > 0 && iconPath.length() > 0) {
				Bundle b = Platform.getBundle(iconPathBundleID);
				i = new Image(masterPrefTree.getDisplay(), Platform.resolve(
						b.getEntry(iconPath)).getPath());
			}
		}
		return i;
	}

	private void addPolicy(IServicePolicy sp, Widget parent,
			boolean addLevel2Only) throws IOException {
		if (addLevel2Only && sp.getParentPolicy() != null
				&& sp.getParentPolicy().getParentPolicy() != null)
			//don't add tertiary and higher branches, these are added on demand to a different tree
			return;
		TreeItem ti;
		if (parent instanceof TreeItem)
			ti = new TreeItem((TreeItem) parent, SWT.NONE);
		else
			ti = new TreeItem((Tree) parent, SWT.NONE);
		ti.setText(sp.getDescriptor().getLongName());
		ti.setData(sp);

		ti.setImage(getImage(sp));
		List<IServicePolicy> childrenPolicyList = sp.getChildren();

		for (IServicePolicy policy : childrenPolicyList) {
			addPolicy(policy, ti, addLevel2Only);
		}

	}

	/**
	 * Does anything necessary because the default button has been pressed.
	 */
	protected void performDefaults() {
		super.performDefaults();
		initializeDefaults();
	}

	/**
	 * Do anything necessary because the OK button has been pressed.
	 *  @return whether it is okay to close the preference page
	 */
	public boolean performOk() {
		storeValues();
		return true;
	}

	protected void performApply() {
		performOk();
	}

	/**
	 * @see IWorkbenchPreferencePage
	 */
	public void init(IWorkbench workbench) {
	}

	/**
	 * Initializes states of the controls using default values
	 * in the preference store.
	 */
	private void initializeDefaults() {

	}

	/**
	 * Initializes states of the controls from the preference store.
	 */
	private void initializeValues() {

	}

	/**
	 * Stores the values of the controls back to the preference store.
	 */
	private void storeValues() {
	}

	public void widgetSelected(SelectionEvent e) {
		if (e.getSource() == masterPrefTree || e.getSource() == detailsPrefTree) {
			TreeItem[] selectedItems;
			if (e.getSource() == masterPrefTree)
				selectedItems = masterPrefTree.getSelection();
			else
				selectedItems = detailsPrefTree.getSelection();
			if (selectedItems.length == 1) {
				// update details and dependencies
				IServicePolicy sp = (IServicePolicy) selectedItems[0].getData();
				// set the CSH

				String CSH_ID = (sp.getDescriptor() == null) ? null : sp
						.getDescriptor().getContextHelpId();
				IServicePolicy parentSP = sp;
				while ((CSH_ID == null || CSH_ID.length() == 0)) {
					parentSP = parentSP.getParentPolicy();
					if (parentSP == null)
						break;
					CSH_ID = (parentSP.getDescriptor() == null) ? null
							: parentSP.getDescriptor().getContextHelpId();
				}
				helpSystem.setHelp(parentComposite, CSH_ID);
				String desc = (sp.getDescriptor() == null || sp.getDescriptor()
						.getDescription() == null) ? "" : sp.getDescriptor() //$NON-NLS-1$
						.getDescription();
				text_DetailsPanel_description.setText(desc);
				String dep = (sp.getRelationships() == null) ? "" : sp //$NON-NLS-1$
						.getRelationships().toString();
				text_DetailsPanel_dependencies.setText(dep);
				if (e.getSource() == masterPrefTree) {
					// if selected node in master tree is 2nd level & has
					// children, populate details tree
					if (sp.getParentPolicy() != null
							&& sp.getChildren().size() > 0) {
						List<IServicePolicy> childpolicyList = sp.getChildren();
						detailsPrefTree.removeAll();
						for (IServicePolicy policy : childpolicyList) {
							try {
								addPolicy(policy, detailsPrefTree, false);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						}
						detailsPrefTree.setVisible(true);
					} else
						detailsPrefTree.setVisible(false);
				}

			}

		}

	}

	public void widgetDefaultSelected(SelectionEvent e) {

	}

	public void selectionChanged() {

	}

}
