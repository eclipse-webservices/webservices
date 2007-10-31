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
import java.util.Vector;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.wst.ws.service.policy.IDescriptor;
import org.eclipse.wst.ws.service.policy.IPolicyEnumerationList;
import org.eclipse.wst.ws.service.policy.IPolicyRelationship;
import org.eclipse.wst.ws.service.policy.IServicePolicy;
import org.eclipse.wst.ws.service.policy.IStateEnumerationItem;
import org.eclipse.wst.ws.service.policy.ServicePolicyPlatform;
import org.eclipse.wst.ws.service.policy.ui.IPolicyOperation;
import org.eclipse.wst.ws.service.policy.ui.ServicePolicyActivatorUI;
import org.eclipse.wst.ws.service.policy.ui.ServicePolicyPlatformUI;
import org.osgi.framework.Bundle;

public class ServicePoliciesPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage, SelectionListener

{
	private Composite parentComposite;
	private ScrolledComposite sc2;
	private Composite c2;
	private IWorkbenchHelpSystem helpSystem;
	private Composite masterComposite;
	private Composite detailsComposite;
	private Tree masterPrefTree;
	private Tree detailsPrefTree;
	private Text text_DetailsPanel_description;
	private Text text_DetailsPanel_dependencies;
	private Label label_DetailsPanel_description;
	private Label label_detailsPanel_dependancies;
	private List<IStatus> errorMessages;
	private static final Image folderImage = ServicePolicyActivatorUI
			.getImageDescriptor("icons/full/obj16/fldr_obj.gif").createImage();
	private static final Image leafImage = ServicePolicyActivatorUI
			.getImageDescriptor("icons/full/obj16/file_obj.gif").createImage();

	/**
	 * Creates preference page controls on demand.
	 *   @param parentComposite  the parent for the preference page
	 */
	protected Control createContents(Composite superparent) {
		errorMessages = new Vector<IStatus>();
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
	 	
	    sc2 = new ScrolledComposite(detailsComposite, SWT.H_SCROLL
				| SWT.V_SCROLL);
		sc2.setExpandHorizontal(true);
		sc2.setExpandVertical(true);
		c2 = new Composite(sc2, SWT.NONE);
		sc2.setContent(c2);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		c2.setLayout(layout);
		sc2.setMinSize(c2.computeSize(400, 100));
		
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
		//fire selection event to tree that is associated with operation UI so operation UI gets updated
		if (detailsPrefTree.isVisible())
			detailsPrefTree.notifyListeners(SWT.Selection, new Event());
		else
			masterPrefTree.notifyListeners(SWT.Selection, new Event());
			
		
	}

	/**
	 * Do anything necessary because the OK button has been pressed.
	 *  @return whether it is okay to close the preference page
	 */
	public boolean performOk() {
		storeValues();
		return true;
	}
	/**
	 * Do anything necessary because the Cancel button has been pressed.
	 *  @return whether it is okay to close the preference page
	 */
	public boolean performCancel() {
		ServicePolicyPlatform.getInstance().discardChanges();
		return true;
	}
	
	protected void performApply() {
		storeValues();
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
		ServicePolicyPlatform.getInstance().restoreDefaults();
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
		ServicePolicyPlatform.getInstance().commitChanges();
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
						.getDescription() == null) ? "" : sp.getDescriptor()
						.getDescription();
				text_DetailsPanel_description.setText(desc);
				text_DetailsPanel_dependencies.setText(getDependanciesText(sp));
				
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
				addActionButtons(sp);

			}

		} else {
			//an action control fired a change event
			Control actionControl = (Control) e.getSource();
			updatePreference(actionControl);
		}

	}

	private String getDependanciesText(IServicePolicy sp) {

		List<IPolicyRelationship> relationShipList = sp.getRelationships();
		if (relationShipList == null || relationShipList.size() == 0)
			return WstSPUIPluginMessages.SERVICEPOLICIES_DEPENDENCIES_NONE;
		String toReturn = new String();
		for (IPolicyRelationship relationShipItem : relationShipList) {
			String operationLongName = "";
			String operationSelectionLongName = "";
			String dependantPolicyShortName = "";
			String dependantOperationShortName = "";
			String dependantOperationSelectionShortNameList = "";
			IPolicyEnumerationList pel = relationShipItem
					.getPolicyEnumerationList();

			List<IPolicyEnumerationList> rpel = relationShipItem
					.getRelatedPolicies();
			for (IPolicyEnumerationList relationshipPolicyItem : rpel) {
				dependantPolicyShortName = relationshipPolicyItem.getPolicy()
						.getDescriptor().getShortName();
				List<IStateEnumerationItem> stateEnumerationList = relationshipPolicyItem
						.getEnumerationList();
				for (IStateEnumerationItem stateEnumerationItem : stateEnumerationList) {
					if (dependantOperationSelectionShortNameList.length() != 0) 
						dependantOperationSelectionShortNameList += " | ";
					dependantOperationSelectionShortNameList += stateEnumerationItem
							.getShortName();

				}
			}

			String[] args = { operationLongName, operationSelectionLongName,
					dependantPolicyShortName, dependantOperationShortName,
					"[" + dependantOperationSelectionShortNameList + "]" };
//			toReturn += NLS.bind(
//					WstSPUIPluginMessages.SERVICEPOLICIES_DEPENDENCIES, args)
//					+ "\r\n";
		}

		return toReturn;
	}
	private void updatePreference(Control actionControl) {

		IPolicyOperation po = (IPolicyOperation) ((Object[]) (actionControl
				.getData()))[0];
		IServicePolicy sp = (IServicePolicy) ((Object[]) (actionControl
				.getData()))[1];
		if (actionControl instanceof Button) {
			updateComplexOperationPreference((Button) actionControl, po, sp);
		} else
			updateSelectionOperationPreference((Combo) actionControl, po, sp);
		performValidation(sp);
	}

	private void performValidation(IServicePolicy sp) {
		
		List<IPolicyRelationship> relationShipList = sp.getRelationships(); 
		for (IPolicyRelationship relationShipItem : relationShipList) {
			IPolicyEnumerationList pel = relationShipItem.getPolicyEnumerationList();
			List<IPolicyEnumerationList> rpel =  relationShipItem.getRelatedPolicies();
			
		}

	}
		

	private void updateSelectionOperationPreference(Combo actionControl,
			IPolicyOperation po, IServicePolicy sp) {
			String selectedValue = actionControl.getText();
			List<IStateEnumerationItem> enumItemList = ServicePolicyPlatform.getInstance().getStateEnumeration(po.getEnumerationId());
			for (IStateEnumerationItem enumItem : enumItemList) {
				if (enumItem.getLongName().equals(selectedValue)) {
				sp.getPolicyStateEnum().setCurrentItem(enumItem.getId());
				sp.getPolicyStateEnum().getCurrentItem();
				break;
				}
			}
	}


	private void updateComplexOperationPreference(Button actionControl,
			IPolicyOperation po, IServicePolicy sp) {
	} 
		


	private void addActionButtons(IServicePolicy sp) {
		//remove existing action controls
		Control[] toRemove = c2.getChildren();
		for (int i =0; i < toRemove.length ; i++)
		{
			toRemove[i].dispose();
		}
		ServicePolicyPlatformUI platform = ServicePolicyPlatformUI.getInstance();
		List<IPolicyOperation> operationList = platform.getAllOperations();

		for (IPolicyOperation policyOperation : operationList) {
			if (Pattern.matches(policyOperation.getPolicyIdPattern(), sp
					.getId())) {
				if (policyOperation.getOperationKind() == IPolicyOperation.OperationKind.complex) {
					addComplexOperationUI(policyOperation, sp);
				} else 
					addSelectionOperationUI(policyOperation, sp);
				
			}

		}
		//just removed and added some controls so force composite
		//to re-layout it's controls
        sc2.setMinSize(c2.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        c2.layout();
	}

	private void addSelectionOperationUI(IPolicyOperation po, IServicePolicy sp) {
		IDescriptor d = po.getDescriptor();
		Control selectionControl;
		if (po.getOperationKind() == IPolicyOperation.OperationKind.enumeration) {
			Label l = new Label(c2, SWT.NONE);
			l.setText(d.getLongName());
			Combo cb = new Combo(c2, SWT.DROP_DOWN | SWT.READ_ONLY);
			selectionControl = cb;
			cb.addSelectionListener(this);
			List<IStateEnumerationItem> enumItemList = ServicePolicyPlatform.getInstance().getStateEnumeration(po.getEnumerationId());
			for (IStateEnumerationItem enumItem : enumItemList) {
				cb.add(enumItem.getLongName());
			}
			cb.setText(getEnumOperationSelection(sp));
		}
		else {
			//a selection or icon 
			Button checkBox = new Button(c2, SWT.CHECK);
			selectionControl = checkBox;
			GridData checkBoxGD = new GridData();
			checkBoxGD.horizontalSpan = 2;
			checkBox.setLayoutData(checkBoxGD);
			checkBox.setText(d.getLongName());
			boolean selected = (getSelectionOperationSelection(sp));
			checkBox.setSelection(selected);

		}
		selectionControl.setData(new Object[] {po, sp});

	}

	private boolean getSelectionOperationSelection(IServicePolicy sp) {
		return (sp.getPolicyStateEnum().getCurrentItem().getId().equals("org.eclipse.wst.true")) ? true : false;
	}
	private String getEnumOperationSelection (IServicePolicy sp) {
		return sp.getPolicyStateEnum().getCurrentItem().getLongName();
	}

	private void addComplexOperationUI(IPolicyOperation po, IServicePolicy sp) {
		IDescriptor d = po.getDescriptor();
		Button pushButton = new Button(c2, SWT.PUSH);
		GridData pushButtonGD = new GridData();
		pushButtonGD.horizontalSpan = 2;
		pushButton.setText(d.getLongName());

	}

	public void widgetDefaultSelected(SelectionEvent e) {

	}

	public Tree getMasterPrefTree() {
		return masterPrefTree;
	}

	public void setMasterPrefTree(Tree masterPrefTree) {
		this.masterPrefTree = masterPrefTree;
	}

	public Tree getDetailsPrefTree() {
		return detailsPrefTree;
	}

	public void setDetailsPrefTree(Tree detailsPrefTree) {
		this.detailsPrefTree = detailsPrefTree;
	}

}
