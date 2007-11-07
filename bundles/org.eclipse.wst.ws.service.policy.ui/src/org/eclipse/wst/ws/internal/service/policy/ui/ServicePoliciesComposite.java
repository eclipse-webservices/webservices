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
 * 20071106       ericdp@ca.ibm.com - Eric Peters
 *******************************************************************************/
package org.eclipse.wst.ws.internal.service.policy.ui;

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
 * 20071107          ericdp@ca.ibm.com - Eric Peters
 *******************************************************************************/
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.util.NLS;
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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.wst.ws.service.policy.IDescriptor;
import org.eclipse.wst.ws.service.policy.IPolicyEnumerationList;
import org.eclipse.wst.ws.service.policy.IPolicyRelationship;
import org.eclipse.wst.ws.service.policy.IPolicyState;
import org.eclipse.wst.ws.service.policy.IPolicyStateEnum;
import org.eclipse.wst.ws.service.policy.IServicePolicy;
import org.eclipse.wst.ws.service.policy.IStateEnumerationItem;
import org.eclipse.wst.ws.service.policy.ServicePolicyPlatform;
import org.eclipse.wst.ws.service.policy.ui.IPolicyOperation;
import org.eclipse.wst.ws.service.policy.ui.ServicePolicyActivatorUI;
import org.eclipse.wst.ws.service.policy.ui.ServicePolicyPlatformUI;
import org.eclipse.wst.ws.service.policy.ui.IPolicyOperation.OperationKind;
import org.eclipse.wst.ws.service.policy.ui.utils.IConManager;
import org.osgi.framework.Bundle;
import org.eclipse.core.resources.IProject;

public class ServicePoliciesComposite extends Composite implements
		SelectionListener {

	private ScrolledComposite operationsScrolledComposite;
	private Composite operationsComposite;
	private IWorkbenchHelpSystem helpSystem;
	private Composite masterComposite;
	private Composite detailsComposite;
	private Tree masterPrefTree;
	private Tree detailsPrefTree;
	private Text text_DetailsPanel_description;
	private Text text_DetailsPanel_dependencies;
	private Label label_DetailsPanel_description;
	private Label label_detailsPanel_dependancies;
	private Hashtable<String, IStatus> allErrors;
	private IStatus error;
	private static final IConManager iconManager = new IConManager();
	private IProject project = null;
	private SelectionListener listener;

	public ServicePoliciesComposite(Composite parent, IProject project,
			SelectionListener listener) {
		super(parent, SWT.NONE);
		this.project = project;
		this.listener = listener;
		allErrors = new Hashtable<String, IStatus>();
		helpSystem = PlatformUI.getWorkbench().getHelpSystem();

		GridLayout parentLayout = new GridLayout();
		parentLayout.numColumns = 2;
		parentLayout.horizontalSpacing = 0;
		this.setLayout(parentLayout);
		this.setLayoutData(new GridData(GridData.FILL_BOTH));

		masterComposite = new Composite(this, SWT.NONE);
		GridLayout masterLayout = new GridLayout();
		masterLayout.numColumns = 1;
		masterLayout.horizontalSpacing = 0;
		masterComposite.setLayout(masterLayout);
		masterComposite.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		detailsComposite = new Composite(this, SWT.NONE);
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
		// detailsPrefTreeGD.heightHint = 200;
		// detailsPrefTreeGD.widthHint = 400;
		detailsPrefTree.setLayoutData(detailsPrefTreeGD);
		detailsPrefTree.setVisible(false);

		ServicePolicyPlatform platform = ServicePolicyPlatform.getInstance();
		List<IServicePolicy> policyList = platform.getRootServicePolicies(null);

		for (IServicePolicy policy : policyList) {
			addPolicy(policy, masterPrefTree, true);
		}

		operationsScrolledComposite = new ScrolledComposite(detailsComposite,
				SWT.H_SCROLL | SWT.V_SCROLL);
		operationsScrolledComposite.setExpandHorizontal(true);
		operationsScrolledComposite.setExpandVertical(true);
		operationsComposite = new Composite(operationsScrolledComposite,
				SWT.NONE);
		operationsScrolledComposite.setContent(operationsComposite);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		operationsComposite.setLayout(layout);
		operationsScrolledComposite.setMinSize(operationsComposite.computeSize(
				400, 100));

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
	}

	private void setImage(TreeItem ti, IServicePolicy sp, String[] iconOverlays) {
		if (iconOverlays == null)
			iconOverlays = new String[0];
		Image i = (sp.getChildren().size() == 0) ? iconManager.getIconOverlay(
				iconManager.getLeafBaseUrl(), iconOverlays) : iconManager
				.getIconOverlay(iconManager.getFolderBaseUrl(), iconOverlays);
		if (sp.getDescriptor() != null) {
			String iconPathBundleID = null;
			String iconPath = null;
			iconPathBundleID = sp.getDescriptor().getIconBundleId();
			iconPath = sp.getDescriptor().getIconPath();
			if (iconPathBundleID != null && iconPath != null
					&& iconPathBundleID.length() > 0 && iconPath.length() > 0) {
				Bundle b = Platform.getBundle(iconPathBundleID);
				i = iconManager.getIconOverlay(FileLocator.find(b,
						new Path(iconPath), null).toString(), iconOverlays);
			}
		}
		ti.setImage(i);
		// image has changed, notify change listeners so tree gets updated
		ti.notifyListeners(SWT.Modify, new Event());
	}

	private void addPolicy(IServicePolicy sp, Widget parent,
			boolean addLevel2Only) {
		if (addLevel2Only && sp.getParentPolicy() != null
				&& sp.getParentPolicy().getParentPolicy() != null)
			// don't add tertiary and higher branches, these are added on demand
			// to a different tree
			return;
		TreeItem ti;
		if (parent instanceof TreeItem)
			ti = new TreeItem((TreeItem) parent, SWT.NONE);
		else
			ti = new TreeItem((Tree) parent, SWT.NONE);
		ti.setText(sp.getDescriptor().getLongName());
		ti.setData(sp);

		setImage(ti, sp, getIconOverlayStrings(sp, false));
		List<IServicePolicy> childrenPolicyList = sp.getChildren();

		for (IServicePolicy policy : childrenPolicyList) {
			addPolicy(policy, ti, addLevel2Only);
		}

	}

	private String[] getIconOverlayStrings(IServicePolicy sp, boolean invalid) {
		String[] overLays = new String[3];
		IPolicyState polState = (project == null) ? sp.getPolicyState() : sp
				.getPolicyState(project);
		if (!polState.isMutable())
			overLays[0] = iconManager.lock;
		if ((sp.getStatus() != null && !sp.getStatus().isOK()) || invalid)
			overLays[1] = iconManager.invalid;
		return overLays;
	}

	/**
	 * Does anything necessary because the default button has been pressed.
	 */
	public void performDefaults() {
		initializeDefaults();
		// fire selection event to tree that is associated with operation UI so
		// operation UI gets updated
		if (detailsPrefTree.isVisible())
			detailsPrefTree.notifyListeners(SWT.Selection, new Event());
		else
			masterPrefTree.notifyListeners(SWT.Selection, new Event());
		TreeItem selected;
		if (detailsPrefTree.isVisible())
			selected = detailsPrefTree.getSelection()[0];
		else
			selected = masterPrefTree.getSelection()[0];
		IServicePolicy focusSP = (IServicePolicy) selected.getData();
		error = validateAllPolicies(focusSP);

	}

	/**
	 * Initializes states of the controls using default values in the preference
	 * store.
	 */
	private void initializeDefaults() {
		if (project == null)
			ServicePolicyPlatform.getInstance().restoreDefaults();
		else
			ServicePolicyPlatform.getInstance().restoreDefaults(project);

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
				helpSystem.setHelp(this, CSH_ID);
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
							addPolicy(policy, detailsPrefTree, false);
						}
						detailsPrefTree.setVisible(true);
					} else
						detailsPrefTree.setVisible(false);
				}
				addActionButtons(sp);

			}

		} else {
			// an action control fired a change event
			Control actionControl = (Control) e.getSource();
			updatePreference(actionControl);
			IServicePolicy changedSP = (IServicePolicy) ((Object[]) (actionControl
					.getData()))[1];
			error = validateAllPolicies(changedSP);
			listener.widgetSelected(e);

		}

	}
	/**
	 * Validates all Service Policies in the composite
	 * @param IServicePolicy the focus policy, i.e. one that changed state
	 * @return IStatus null if no Service policies in error, otherwise a Service Policy error (the 
	 * focucPolicy error if it is in error. Postcondition- the Service Policy tree icons
	 * reflect the state of all Service Policies, and the error property is updated.
	 */
	private IStatus validateAllPolicies(IServicePolicy focusPolicy) {
		// re-validate tree
		TreeItem[] ti = masterPrefTree.getItems();
		for (int i = 0; i < ti.length; i++) {
			validatePolicy(ti[i]);
		}
		// define error message
		IStatus error = allErrors.get(focusPolicy);
		if (error == null)
			if (!allErrors.isEmpty())
				error = allErrors.get(allErrors.keys().nextElement());

		return error;
	}
	/**
	 * Validates the Service Policies for the TreeItem and all it's children.
	 * @param TreeItem the tree item to validate
	 */
	private void validatePolicy(TreeItem ti) {
		IServicePolicy sp = (IServicePolicy) ti.getData();
		//assume policy state is valid
		allErrors.remove(sp.getId());
		propogateValidPolicyState(ti, sp);
		String operationLongName;
		String operationSelectionLongName;
		String dependantPolicyShortName;
		List<IPolicyRelationship> relationShipList = sp.getRelationships();
		List<IPolicyOperation> spOperationsUsingEnumList;
		String currentValueID = null;
		if (sp.getPolicyStateEnum() != null) {
			currentValueID = (project == null) ? sp.getPolicyStateEnum()
					.getCurrentItem().getId() : sp.getPolicyStateEnum(project)
					.getCurrentItem().getId();
			spOperationsUsingEnumList = getOperationsUsingSameEnum(sp);
			for (IPolicyOperation operationItem : spOperationsUsingEnumList) {
				operationLongName = operationItem.getDescriptor().getLongName();
				for (IPolicyRelationship relationShipItem : relationShipList) {
					// policies associated with the relationship item
					List<IPolicyEnumerationList> relatedPolicies = relationShipItem
							.getRelatedPolicies();
					List<IStateEnumerationItem> spStateEnumerationList = relationShipItem
							.getPolicyEnumerationList().getEnumerationList();
					for (IStateEnumerationItem stateEnumerationItem : spStateEnumerationList) {
						if (!stateEnumerationItem.getId()
								.equals(currentValueID))
							continue;
						operationSelectionLongName = stateEnumerationItem
								.getLongName();
						for (IPolicyEnumerationList relatedPolicyEnumerationItem : relatedPolicies) {
							dependantPolicyShortName = relatedPolicyEnumerationItem
									.getPolicy().getDescriptor().getShortName();
							List<IPolicyOperation> relatedSPOperationsUsingEnumList = getOperationsUsingSameEnum(relatedPolicyEnumerationItem
									.getPolicy());
							for (IPolicyOperation relatedSPOperationsUsingEnumItem : relatedSPOperationsUsingEnumList) {
								List<IStateEnumerationItem> relatedSPStateEnumerationList = relatedPolicyEnumerationItem
										.getEnumerationList();
								List<String> validShortNames = new Vector<String>();
								for (int i = 0; i < relatedSPStateEnumerationList
										.size(); i++) {
									validShortNames
											.add(relatedSPStateEnumerationList
													.get(i).getShortName());

								}
								String currentItemShortName = (project == null) ? relatedPolicyEnumerationItem
										.getPolicy().getPolicyStateEnum()
										.getCurrentItem().getShortName()
										: relatedPolicyEnumerationItem
												.getPolicy()
												.getPolicyStateEnum(project)
												.getCurrentItem()
												.getShortName();
								if (!validShortNames
										.contains(currentItemShortName)) {
									//policy state is invalid
									IStatus error = createDependencyError(sp,
											operationLongName,
											operationSelectionLongName,
											dependantPolicyShortName,
											relatedSPOperationsUsingEnumItem,
											relatedSPStateEnumerationList);
									allErrors.put(sp.getId(), error);
									propogateInvalidPolicyState(ti, sp);

								} 

							}

						}

					}
				}
			}
		}
		TreeItem[] children = getChildren(ti);
		if (children != null && children.length != 0)
			for (int i = 0; i < children.length; i++) {
				validatePolicy(children[i]);

			}

	}

	private void propogateValidPolicyState(TreeItem ti, IServicePolicy sp) {
		setImage(ti, sp, getIconOverlayStrings(sp,
				false));
		TreeItem parent = ti.getParentItem();
		while (parent != null
				&& !allErrors
						.containsKey(((IServicePolicy) parent
								.getData()).getId())) {
			setImage(parent,
					(IServicePolicy) parent
							.getData(),
					getIconOverlayStrings(
							(IServicePolicy) parent
									.getData(),
							false));
			allErrors
					.remove(((IServicePolicy) parent
							.getData()).getId());
			parent = parent.getParentItem();
		}
	}

	private void propogateInvalidPolicyState(TreeItem ti, IServicePolicy sp) {
		setImage(ti, sp, getIconOverlayStrings(sp,
				true));
		TreeItem parent = ti.getParentItem();
		while (parent != null) {
			setImage(parent,
					(IServicePolicy) parent
							.getData(),
					getIconOverlayStrings(sp, true));
			parent = parent.getParentItem();
		}
	}

	private IStatus createDependencyError(IServicePolicy sp,
			String operationLongName, String operationSelectionLongName,
			String dependantPolicyShortName,
			IPolicyOperation relatedSPOperationsUsingEnumItem,
			List<IStateEnumerationItem> relatedSPStateEnumerationList) {
		String dependantOperationShortName;
		String dependantOperationSelectionShortNameList;
		dependantOperationShortName = relatedSPOperationsUsingEnumItem
				.getDescriptor().getShortName();
		dependantOperationSelectionShortNameList = new String();
		for (int i = 0; i < relatedSPStateEnumerationList
				.size(); i++) {
			IStateEnumerationItem item = relatedSPStateEnumerationList
					.get(i);
			if (i != 0)
				dependantOperationSelectionShortNameList += " | ";
			dependantOperationSelectionShortNameList += item
					.getShortName();

		}
		String[] args = {
				sp.getDescriptor().getLongName(),
				operationLongName,
				operationSelectionLongName,
				dependantPolicyShortName,
				dependantOperationShortName,
				"["
						+ dependantOperationSelectionShortNameList
						+ "]" };
		IStatus error = new Status(
				Status.ERROR,
				ServicePolicyActivatorUI.PLUGIN_ID,
				NLS
						.bind(
								WstSPUIPluginMessages.SERVICEPOLICIES_DEPENDENCY_ERROR,
								args));
		return error;
	}

	private TreeItem[] getChildren(TreeItem parent) {
		TreeItem[] children = parent.getItems();
		if (parent.getItems() == null
				&& ((IServicePolicy) parent.getData()).getChildren().size() != 0) {
			children = getChildrenInDetailsTree(detailsPrefTree.getItems(),
					((IServicePolicy) parent.getData()).getId());
		}
		return children;
	}

	private TreeItem[] getChildrenInDetailsTree(TreeItem[] parentNodes,
			String parentID) {
		List<TreeItem> children = null;
		TreeItem[] toReturn = null;
		if (parentNodes != null && parentNodes.length != 0) {
			for (int i = 0; i < parentNodes.length; i++) {
				if (((IServicePolicy) parentNodes[i].getData()).getId().equals(
						parentID)) {
					children.add(parentNodes[i]);
				}
			}
			if (children.size() > 0) {
				toReturn = children.toArray(toReturn);
			} else {
				for (int i = 0; i < parentNodes.length; i++) {
					getChildrenInDetailsTree(parentNodes[i].getItems(),
							parentID);
				}
			}

		}
		return toReturn;

	}

	private String getDependanciesText(IServicePolicy sp) {
		// the relationships defined for the policy
		List<IPolicyRelationship> relationShipList = sp.getRelationships();
		if (relationShipList == null || relationShipList.size() == 0)
			return WstSPUIPluginMessages.SERVICEPOLICIES_DEPENDENCIES_NONE;
		String toReturn = new String();

		String operationLongName;
		String operationSelectionLongName;
		String dependantPolicyShortName;
		String dependantOperationShortName;
		String dependantOperationSelectionShortNameList;
		// the operations using the same enumeration as the policy
		List<IPolicyOperation> spOperationsUsingEnumList = getOperationsUsingSameEnum(sp);
		for (IPolicyOperation operationItem : spOperationsUsingEnumList) {
			operationLongName = operationItem.getDescriptor().getLongName();
			for (IPolicyRelationship relationShipItem : relationShipList) {
				// policies associated with the relationship item
				List<IPolicyEnumerationList> relatedPolicies = relationShipItem
						.getRelatedPolicies();
				List<IStateEnumerationItem> spStateEnumerationList = relationShipItem
						.getPolicyEnumerationList().getEnumerationList();
				for (IStateEnumerationItem stateEnumerationItem : spStateEnumerationList) {
					operationSelectionLongName = stateEnumerationItem
							.getLongName();
					for (IPolicyEnumerationList relatedPolicyEnumerationItem : relatedPolicies) {
						dependantPolicyShortName = relatedPolicyEnumerationItem
								.getPolicy().getDescriptor().getShortName();
						List<IPolicyOperation> relatedSPOperationsUsingEnumList = getOperationsUsingSameEnum(relatedPolicyEnumerationItem
								.getPolicy());
						for (IPolicyOperation relatedSPOperationsUsingEnumItem : relatedSPOperationsUsingEnumList) {
							List<IStateEnumerationItem> relatedSPStateEnumerationList = relatedPolicyEnumerationItem
									.getEnumerationList();
							dependantOperationShortName = relatedSPOperationsUsingEnumItem
									.getDescriptor().getShortName();
							dependantOperationSelectionShortNameList = new String();
							for (int i = 0; i < relatedSPStateEnumerationList
									.size(); i++) {
								IStateEnumerationItem item = relatedSPStateEnumerationList
										.get(i);
								if (i != 0)
									dependantOperationSelectionShortNameList += " | ";
								dependantOperationSelectionShortNameList += item
										.getShortName();

							}
							String[] args = {
									operationLongName,
									operationSelectionLongName,
									dependantPolicyShortName,
									dependantOperationShortName,
									"["
											+ dependantOperationSelectionShortNameList
											+ "]" };
							toReturn += NLS
									.bind(
											WstSPUIPluginMessages.SERVICEPOLICIES_DEPENDENCIES,
											args)
									+ "\r\n";

						}

					}

				}
			}
		}
		return toReturn;
	}

	private List<IPolicyOperation> getOperationsUsingSameEnum(IServicePolicy sp) {
		List<IPolicyOperation> toReturn = null;
		toReturn = new Vector<IPolicyOperation>();
		ServicePolicyPlatformUI platform = ServicePolicyPlatformUI
				.getInstance();
		List<IPolicyOperation> operationList = platform.getAllOperations();

		for (IPolicyOperation policyOperation : operationList) {
			if (Pattern.matches(policyOperation.getPolicyIdPattern(), sp
					.getId())) {
				IPolicyStateEnum polEnum = (project == null) ? sp
						.getPolicyStateEnum() : sp.getPolicyStateEnum(project);
				if ((polEnum.getEnumId().equals(
						"org.eclipse.wst.service.policy.booleanEnum") && (policyOperation
						.getOperationKind().equals(OperationKind.selection) || policyOperation
						.getOperationKind().equals(OperationKind.iconSelection)))
						|| (policyOperation.getOperationKind().equals(
								OperationKind.enumeration) && policyOperation
								.getEnumerationId().equals(polEnum.getEnumId()))) {

					toReturn.add(policyOperation);
				}
			}

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

	}

	public boolean okToLeave() {
		return allErrors.size() == 0;

	}

	private void updateSelectionOperationPreference(Combo actionControl,
			IPolicyOperation po, IServicePolicy sp) {
		String selectedValue = actionControl.getText();
		List<IStateEnumerationItem> enumItemList = ServicePolicyPlatform
				.getInstance().getStateEnumeration(po.getEnumerationId());
		for (IStateEnumerationItem enumItem : enumItemList) {
			if (enumItem.getLongName().equals(selectedValue)) {
				IPolicyStateEnum polEnum = (project == null) ? sp
						.getPolicyStateEnum() : sp.getPolicyStateEnum(project);
				polEnum.setCurrentItem(enumItem.getId());
				break;
			}
		}
	}

	private void updateComplexOperationPreference(Button actionControl,
			IPolicyOperation po, IServicePolicy sp) {
	}

	public void dispose() {
		super.dispose();
	}

	private void addActionButtons(IServicePolicy sp) {
		// remove existing action controls
		Control[] toRemove = operationsComposite.getChildren();
		for (int i = 0; i < toRemove.length; i++) {
			toRemove[i].dispose();
		}
		ServicePolicyPlatformUI platform = ServicePolicyPlatformUI
				.getInstance();
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
		// just removed and added some controls so force composite
		// to re-layout it's controls
		operationsScrolledComposite.setMinSize(operationsComposite.computeSize(
				SWT.DEFAULT, SWT.DEFAULT));
		operationsComposite.layout();
	}

	private void addSelectionOperationUI(IPolicyOperation po, IServicePolicy sp) {
		IDescriptor d = po.getDescriptor();
		Control selectionControl;
		if (po.getOperationKind() == IPolicyOperation.OperationKind.enumeration) {
			Label l = new Label(operationsComposite, SWT.NONE);
			l.setText(d.getLongName() + ":");
			Combo cb = new Combo(operationsComposite, SWT.DROP_DOWN
					| SWT.READ_ONLY);
			selectionControl = cb;
			cb.addSelectionListener(this);
			List<IStateEnumerationItem> enumItemList = ServicePolicyPlatform
					.getInstance().getStateEnumeration(po.getEnumerationId());
			for (IStateEnumerationItem enumItem : enumItemList) {
				cb.add(enumItem.getLongName());
			}
			cb.setText(getEnumOperationSelection(sp));
		} else {
			// a selection or icon
			Button checkBox = new Button(operationsComposite, SWT.CHECK);
			selectionControl = checkBox;
			GridData checkBoxGD = new GridData();
			checkBoxGD.horizontalSpan = 2;
			checkBox.setLayoutData(checkBoxGD);
			checkBox.setText(d.getLongName());
			boolean selected = (getSelectionOperationSelection(sp));
			checkBox.setSelection(selected);

		}
		selectionControl.setData(new Object[] { po, sp });

	}

	private boolean getSelectionOperationSelection(IServicePolicy sp) {
		IPolicyStateEnum polEnum = (project == null) ? sp.getPolicyStateEnum()
				: sp.getPolicyStateEnum(project);
		return (polEnum.getCurrentItem().getId().equals("org.eclipse.wst.true")) ? true
				: false;
	}

	private String getEnumOperationSelection(IServicePolicy sp) {
		IPolicyStateEnum polEnum = (project == null) ? sp.getPolicyStateEnum()
				: sp.getPolicyStateEnum(project);
		return polEnum.getCurrentItem().getLongName();
	}

	private void addComplexOperationUI(IPolicyOperation po, IServicePolicy sp) {
		IDescriptor d = po.getDescriptor();
		Button pushButton = new Button(operationsComposite, SWT.PUSH);
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

	public IStatus getError() {
		return error;
	}

}
