/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20071106 196997   ericdp@ca.ibm.com - Eric Peters
 * 20071120 209858 ericdp@ca.ibm.com - Eric Peters, Enhancing service policy framework and UI 
 * 20071212   209858 ericdp@ca.ibm.com - Eric Peters, Enhancing service policy framework and UI
 * 20080211   218520 pmoogk@ca.ibm.com - Peter Moogk
 * 20080324   222095 pmoogk@ca.ibm.com - Peter Moogk, UI now listens for state changes.
 * 20080324   223634 ericdp@ca.ibm.com - Eric D. Peters, Service Policies preference tree should be 3 level tree
 * 20080506   219005 ericdp@ca.ibm.com - Eric D. Peters, Service policy preference page not restoring properly
 *******************************************************************************/
package org.eclipse.wst.ws.internal.service.policy.ui;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.JFaceResources;
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
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.wst.ws.service.policy.IDescriptor;
import org.eclipse.wst.ws.service.policy.IPolicyEnumerationList;
import org.eclipse.wst.ws.service.policy.IPolicyRelationship;
import org.eclipse.wst.ws.service.policy.IPolicyState;
import org.eclipse.wst.ws.service.policy.IPolicyStateEnum;
import org.eclipse.wst.ws.service.policy.IServicePolicy;
import org.eclipse.wst.ws.service.policy.IStateEnumerationItem;
import org.eclipse.wst.ws.service.policy.ServicePolicyPlatform;
import org.eclipse.wst.ws.service.policy.listeners.IPolicyChildChangeListener;
import org.eclipse.wst.ws.service.policy.listeners.IPolicyStateChangeListener;
import org.eclipse.wst.ws.service.policy.ui.IPolicyOperation;
import org.eclipse.wst.ws.service.policy.ui.ServicePolicyActivatorUI;
import org.eclipse.wst.ws.service.policy.ui.ServicePolicyPlatformUI;
import org.eclipse.wst.ws.service.policy.ui.IPolicyOperation.OperationKind;
import org.eclipse.wst.ws.service.policy.ui.utils.ServiceUtils;
import org.osgi.framework.Bundle;

public class ServicePoliciesComposite extends Composite implements
		SelectionListener, IPolicyChildChangeListener  {

	private ScrolledComposite operationsScrolledComposite;
	//a scrollable composite containing operations available for the selected policies
	private Composite operationsComposite;
	private IWorkbenchHelpSystem helpSystem;
	private Composite masterComposite;
	private Composite detailsComposite;
	private Tree masterPolicyTree;
	//tertiary and higher level policy nodes in a separate tree
	private Tree detailsPolicyTree;
	private Text text_DetailsPanel_description;
	private Text text_DetailsPanel_dependencies;
	private Label label_DetailsPanel_description;
	private Label label_detailsPanel_dependancies;
	private Hashtable<String, IStatus> allErrors;
	private IStatus error;
	private boolean bComplexOpCompleted = true;
	private List<ChildChangeEvent> listChildChangeEvents;
	private IConManager iconManager = new IConManager();
	private IProject project = null;
	private SelectionListener listener;
	private ExpandableComposite excomposite;
	private ServicePolicyPlatform platform = ServicePolicyPlatform
			.getInstance();
	private ServicePolicyPlatformUI platformUI = ServicePolicyPlatformUI
			.getInstance();
	private IPolicyStateChangeListener stateChangeListener  = new StateChangeListener();
	private boolean                    stateChangeEnabled   = true;
	private HashSet<IServicePolicy>    stateChangePolicySet = new HashSet<IServicePolicy>();
	private List<IServicePolicy>       lastSelectedSp       = null;

	/**
	 * Creates an expandable composite
	 * @param parent
	 * @param nColumns
	 * @return
	 */
	private ExpandableComposite createExpandableComposite(Composite parent,
			int nColumns) {
		ExpandableComposite excomposite = new ExpandableComposite(parent,
				SWT.NONE, ExpandableComposite.TWISTIE
						| ExpandableComposite.CLIENT_INDENT);
		excomposite.setExpanded(false);
		excomposite.setVisible(false);
		excomposite.setFont(JFaceResources.getFontRegistry().getBold(
				JFaceResources.DIALOG_FONT));
		excomposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
				true, false, nColumns, 1));
		excomposite.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				expandedStateChanged((ExpandableComposite) e.getSource());
			}
		});
		makeScrollableCompositeAware(excomposite);
		return excomposite;
	}
	
	/**
	 * @author ericdp
	 * Helper class that encapsulates data required when an action control changes state
	 *
	 */
	private class ActionControlData {
		private List<IServicePolicy> spList;
		private List<IPolicyOperation> poList;
//		private IPolicyOperation po;

		public ActionControlData(List<IServicePolicy> spList, List<IPolicyOperation> poList) {
			this.spList = spList;
			this.poList = poList;
		}
		public List<IServicePolicy> getSpList() {
			return spList;
		}

		public void setSpList(List<IServicePolicy> spList) {
			this.spList = spList;
		}
//
//		public IPolicyOperation getPo() {
//			return po;
//		}
//
//		public void setPo(IPolicyOperation po) {
//			this.po = po;
//		}

		public List<IPolicyOperation> getPoList() {
			return poList;
		}

		public void setPoList(List<IPolicyOperation> poList) {
			this.poList = poList;
		}
	}

	/**
	 * Makes the scrollable composite aware of this control, so expand/collapse
	 * of the scrollable composite will move this control down/up accordingly
	 * @param control the control to make the scrollable composite aware of
	 */
	private void makeScrollableCompositeAware(Control control) {
		ScrolledPageContent parentScrolledComposite = getParentScrolledComposite(control);
		if (parentScrolledComposite != null) {
			parentScrolledComposite.adaptChild(control);
		}
	}

	private ScrolledPageContent getParentScrolledComposite(Control control) {
		Control parent = control.getParent();
		while (!(parent instanceof ScrolledPageContent) && parent != null) {
			parent = parent.getParent();
		}
		if (parent instanceof ScrolledPageContent) {
			return (ScrolledPageContent) parent;
		}
		return null;
	}

	private final void expandedStateChanged(ExpandableComposite expandable) {
		ScrolledPageContent parentScrolledComposite = getParentScrolledComposite(expandable);
		if (parentScrolledComposite != null) {
			parentScrolledComposite.reflow(true);
		}
	}

	/**
	 * @param parent
	 * @return a scrollable composite containing an expandable content
	 *
	 */
	private Composite createDetailsScrollPageContent(Composite parent) {

		int nColumns = 1;

		final ScrolledPageContent pageContent = new ScrolledPageContent(parent);

		GridLayout pageContLayout = new GridLayout();
		pageContLayout.numColumns = nColumns;
		pageContLayout.marginHeight = 0;
		pageContLayout.marginWidth = 0;

		Composite composite = pageContent.getBody();
		composite.setLayout(pageContLayout);

		excomposite = createExpandableComposite(composite, nColumns);

		Composite inner = new Composite(excomposite, SWT.NONE);
		inner.setFont(composite.getFont());
		inner.setLayout(new GridLayout(nColumns, false));
		excomposite.setClient(inner);
		//details policy tree for tertiary and higher level service policies
		detailsPolicyTree = new Tree(inner, SWT.BORDER | SWT.MULTI);
		detailsPolicyTree.addSelectionListener(this);
		GridData detailsPrefTreeGD = new GridData(
				GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		detailsPrefTreeGD.heightHint = 50;
		detailsPolicyTree.setLayoutData(detailsPrefTreeGD);
		detailsPolicyTree.setToolTipText(WstSPUIPluginMessages.TOOLTIP_PSP_DETAILSTREE);
		//make the scrollable composite aware of the tree so expand/collapse works properly
		makeScrollableCompositeAware(detailsPolicyTree);
		
		createPolicyOperationsComposite(composite);

		label_DetailsPanel_description = new Label(composite, SWT.NONE);

		label_DetailsPanel_description
				.setText(WstSPUIPluginMessages.LABEL_SERVICEPOLICIES_DESCRIPTION);
		makeScrollableCompositeAware(label_DetailsPanel_description);
		text_DetailsPanel_description = new Text(composite, SWT.WRAP
				| SWT.BORDER | SWT.V_SCROLL | SWT.READ_ONLY);
		GridData detailsGD = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL);
		detailsGD.heightHint = 50;
		detailsGD.widthHint = 400;
		text_DetailsPanel_description.setLayoutData(detailsGD);
		text_DetailsPanel_description.setToolTipText(WstSPUIPluginMessages.TOOLTIP_PSP_DESCRIPTION);

		makeScrollableCompositeAware(text_DetailsPanel_description);
		label_detailsPanel_dependancies = new Label(composite, SWT.NONE);
		makeScrollableCompositeAware(label_detailsPanel_dependancies);
		label_detailsPanel_dependancies
				.setText(WstSPUIPluginMessages.LABEL_SERVICEPOLICIES_DEPENDENCIES);
		text_DetailsPanel_dependencies = new Text(composite, SWT.WRAP
				| SWT.BORDER | SWT.V_SCROLL | SWT.READ_ONLY);
		text_DetailsPanel_dependencies.setToolTipText(WstSPUIPluginMessages.TOOLTIP_PSP_DEPENDENCIES);
		makeScrollableCompositeAware(text_DetailsPanel_dependencies);
		GridData dependenciesGD = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL);
		dependenciesGD.heightHint = 100;
		dependenciesGD.widthHint = 400;
		text_DetailsPanel_dependencies.setLayoutData(dependenciesGD);

		return pageContent;
	}

	/**
	 * Creates the scrollable composite that will contain widgets associated with a policy operation
	 * @param parent the parent composite
	 */
	private void createPolicyOperationsComposite(Composite parent) {
		operationsScrolledComposite = new ScrolledComposite(parent,
				SWT.H_SCROLL | SWT.V_SCROLL);
		operationsScrolledComposite.setExpandHorizontal(true);
		operationsScrolledComposite.setExpandVertical(true);
		GridData operationsScrolledCompositeGD = new GridData(
				GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		operationsScrolledCompositeGD.heightHint =60;
		operationsScrolledComposite.setLayoutData(operationsScrolledCompositeGD);
		operationsComposite = new Composite(operationsScrolledComposite,
				SWT.NONE);
		operationsScrolledComposite.setContent(operationsComposite);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		operationsComposite.setLayout(layout);
//		GridData operationsCompositeGD = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
//		operationsCompositeGD.heightHint = 1000;
//		operationsCompositeGD.widthHint = 1000;
//		operationsComposite.setLayoutData(operationsCompositeGD);
		operationsScrolledComposite.setMinSize(operationsComposite.computeSize(
				400, 100));
		makeScrollableCompositeAware(operationsScrolledComposite);
		makeScrollableCompositeAware(operationsComposite);
	}

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
		masterPolicyTree = new Tree(masterComposite, SWT.BORDER | SWT.MULTI);
		masterPolicyTree.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		masterPolicyTree.setToolTipText(WstSPUIPluginMessages.TOOLTIP_PSP_TREE);

		Composite othersComposite = createDetailsScrollPageContent(detailsComposite);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
				true);
		othersComposite.setLayoutData(gridData);

		masterPolicyTree.addSelectionListener(this);

		List<IServicePolicy> policyList = platform.getRootServicePolicies(null);
		
		policyList = ServiceUtils.sortList( policyList );
		
		for (IServicePolicy policy : policyList) {
			addPolicy(policy, masterPolicyTree, true);
		}
		TreeItem[] treeItems = masterPolicyTree.getItems();
		if (treeItems.length > 0) {
			//select the first item in the tree & fire event so UI is updated
			masterPolicyTree.setSelection(treeItems[0]);
			masterPolicyTree.notifyListeners(SWT.Selection, new Event());
		}
	}

	/**
	 * Sets the image on ti, if associated service policy defines an icon
	 * it will will be used, otherwise default Folder/Leaf icons will be used
	 * @param ti the TreeItem to set the image for
	 * @param sp the service policy associated with ti
	 * @param iconOverlays description of which overlays should be applied 
	 */
	private void setImage(TreeItem ti, IServicePolicy sp, String[] iconOverlays) {
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
	
	private boolean is4thLevelOrHigherPolicy (IServicePolicy sp) {
		return sp.getParentPolicy() != null
		&& sp.getParentPolicy().getParentPolicy() != null && sp.getParentPolicy().getParentPolicy().getParentPolicy() != null;
		
	}
	
	private boolean is3rdLevelPolInMasterTreeWithChildren (List<IServicePolicy> spListInMasterTree) {
		return spListInMasterTree.get(0).getParentPolicy() != null && spListInMasterTree.get(0).getParentPolicy().getParentPolicy() != null
		&& spListInMasterTree.get(0).getChildren().size() > 0 ;
	}
	/**
	 * Add service policy sp and children of sp to the parent
	 * @param sp the service policy to add
	 * @param parent either a Tree (masterPolicyTree or DetailsPolicyTree), or a TreeItem
	 * to add service policy children to
	 * @param addUpToLevel3Only false if should add 4th and higher level
	 * child policies
	 */
	private void addPolicy(IServicePolicy sp, Widget parent,
			boolean addUpToLevel3Only) {
		sp.addPolicyChildChangeListener(this);
		addStateListener( sp );
		if (addUpToLevel3Only && is4thLevelOrHigherPolicy(sp) )
			// don't add 4th level and higher branches, these are added on demand
			// to a different tree
			return;
		TreeItem ti;
		if (parent instanceof TreeItem)
			ti = new TreeItem((TreeItem) parent, SWT.NONE);
		else
			ti = new TreeItem((Tree) parent, SWT.NONE);
		ti.setText(sp.getDescriptor().getLongName());
		ti.setData(sp);

		setImage(ti, sp, getIconOverlayInfo(sp, false));
		List<IServicePolicy> childrenPolicyList = sp.getChildren();
		
		childrenPolicyList = ServiceUtils.sortList( childrenPolicyList );
		
		for (IServicePolicy policy : childrenPolicyList) {
			addPolicy(policy, ti, addUpToLevel3Only);
		}

	}

	/**
	 * Returns icon overlay information
	 * @param sp the service policy that should have some overlay images associated with it
	 * @param invalid true if the service policy is valid
	 * @return String[] containing information about what overlays to apply to the icon that
	 * is associated with sp
	 */
	private String[] getIconOverlayInfo(IServicePolicy sp, boolean invalid) {
		String[] overLays = new String[4];
				
		IPolicyState polState = (project == null) ? sp.getPolicyState() : sp
				.getPolicyState(project);
		IPolicyStateEnum polEnum = (project == null) ? sp.getPolicyStateEnum()
				: sp.getPolicyStateEnum(project);
		if (!polState.isMutable())
			overLays[0] = iconManager.lock;
		if ((sp.getStatus() != null && sp.getStatus().getSeverity() == IStatus.ERROR) || invalid)
			overLays[1] = iconManager.invalid;
		if ((sp.getStatus() != null && sp.getStatus().getSeverity() == IStatus.WARNING))
			overLays[2] = iconManager.warning;
		if (polEnum != null) {
			if (polEnum.getEnumId().equals(
					"org.eclipse.wst.service.policy.booleanEnum")) { //$NON-NLS-1$
				if (policyHasIconSelectionOperationSelected(sp))
					overLays[3] = iconManager.favorite;
			}
				
		}
		return overLays;
	}

	/**
	 * @param sp
	 */
	private boolean policyHasIconSelectionOperationSelected(IServicePolicy sp) {
		boolean iconSelectionOperationSelected = false;
		List<IPolicyOperation> operationList = platformUI.getOperations(sp, project == null);
		for (IPolicyOperation operationItem : operationList) {
			if (operationItem.getOperationKind().equals(OperationKind.iconSelection)) {
				String stateID = operationItem.getStateItem(project);
				if (stateID.equals("org.eclipse.wst.true"))  //$NON-NLS-1$
					return true;
			}
			
		}
		return iconSelectionOperationSelected;
	}

	/**
	 * Does anything necessary because the default button has been pressed.
	 */
	public void performDefaults() {
		initializeDefaults();
		List<IServicePolicy> policyList = platform.getRootServicePolicies(null);
		
		policyList = ServiceUtils.sortList( policyList );
		masterPolicyTree.removeAll();
		
		for (IServicePolicy policy : policyList) {
			addPolicy(policy, masterPolicyTree, true);
		}
		TreeItem[] treeItems = masterPolicyTree.getItems();
		if (treeItems.length > 0) {
			//select the first item in the tree & fire event so UI is updated
			masterPolicyTree.setSelection(treeItems[0]);
			masterPolicyTree.notifyListeners(SWT.Selection, new Event());
			validateAllPolicies((IServicePolicy)treeItems[0].getData());
		}

	}
	private class ChildChangeEvent {
		public ChildChangeEvent(List<IServicePolicy> changedChildren, List<Boolean> added ) {
			this.changedChildren = changedChildren;
			this.added = added;
			
		}
		private List<IServicePolicy> changedChildren;
		private List<Boolean> added;
		public List<IServicePolicy> getChangedChildren() {
			return changedChildren;
		}
		public void setChangedChildren(List<IServicePolicy> changedChildren) {
			this.changedChildren = changedChildren;
		}
		public List<Boolean> getAdded() {
			return added;
		}
		public void setAdded(List<Boolean> added) {
			this.added = added;
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.service.policy.listeners.IPolicyChildChangeListener#childChange(List<org.eclipse.wst.ws.service.policy.IServicePolicy>, List<java.lang.Boolean>)
	 */
	public void childChange( List<IServicePolicy> child, List<Boolean> added ) {
		//ignore events unless we are in a complex operation
		if (!bComplexOpCompleted)
			//cache the event until complex operation completed
			listChildChangeEvents.add(new ChildChangeEvent(child, added));
			
	}
	
	
	/**
	 * @param child
	 * @param added
	 */
	private void processChildChangeEvent(ChildChangeEvent cce,
			Hashtable<String, IServicePolicy> htTopLevelAddtions) {
		List<IServicePolicy> changedServicePolicyList = cce.getChangedChildren();
		List<Boolean> changedServicePolicyAddedList = cce.getAdded();
		for (int i = 0; i < changedServicePolicyList.size(); i++) {
			IServicePolicy nextChangedServicePolicy = changedServicePolicyList.get(i);
			boolean added = changedServicePolicyAddedList.get(i);
			if (!added) {
				//delete node
				processChildChangeEventRemoveServicePolicy(nextChangedServicePolicy);
			} else {
				//an addition, only process top level additions as this is a recursive operation (adds children too)
				if (htTopLevelAddtions.containsKey(nextChangedServicePolicy.getId())) {
					processChildChangeEventAddServicePolicy(nextChangedServicePolicy);
				}
			}
		}
	}

	
	/**
	 * @param nextChangedServicePolicy
	 */
	private void processChildChangeEventRemoveServicePolicy(IServicePolicy nextChangedServicePolicy) {
		boolean is4thLevelOrHigher = is4thLevelOrHigherPolicy(nextChangedServicePolicy);
		
		removeStateListener( nextChangedServicePolicy );
		
		if (is4thLevelOrHigher) {
			if (detailsPolicyTree.isVisible()) {
				TreeItem[] children = detailsPolicyTree.getItems();
				TreeItem found = null;
				for (int i=0; i < children.length; i++) {
					 found = findPolicy(nextChangedServicePolicy, children[i]);	
					 if (found != null)
						 break;
				}
				if (found != null && !found.isDisposed()) {
					processChildChangeEventDeleteTreeItem(found);
				}
			}
		} else {
			TreeItem[] children = masterPolicyTree.getItems();
			TreeItem found = null;
			for (int i=0; i < children.length; i++) {
				 found = findPolicy(nextChangedServicePolicy, children[i]);	
				 if (found != null)
					 break;
			}
			if (found != null && !found.isDisposed()) {
				processChildChangeEventDeleteTreeItem(found);
				
			}

			
			
		}
		
	}

	/**
	 * Delete the item, and choose set a new selection as appropriate from one of the policy trees 
	 * @param deleteItem
	 */
	private void processChildChangeEventDeleteTreeItem(TreeItem deleteItem) {
		Tree parentTree = deleteItem.getParent();
		Tree selectionTree = parentTree;
		TreeItem parentTreeItem = deleteItem.getParentItem();
		//index of found in the tree
		int idx = (parentTreeItem == null) ? parentTree.indexOf(deleteItem) : parentTreeItem.indexOf(deleteItem);
		//is index the last index in the tree
		boolean isLastIndex = (parentTreeItem == null) ? parentTree.indexOf(deleteItem) == parentTree.getItemCount() -1 : parentTreeItem.indexOf(deleteItem) == parentTreeItem.getItemCount() -1;
		//number of siblings
		int siblingsCount = (parentTreeItem == null) ? parentTree.getItemCount() -1 : parentTreeItem.getItemCount() -1;
		boolean hasParent = (parentTreeItem == null) ? parentTree.getItemCount() == 0 : parentTreeItem.getItemCount() == 0;
		deleteItem.dispose();
		parentTree.redraw();
		TreeItem selectItem = null;
		if (siblingsCount == 0) {
			if (hasParent) {
			//no siblings, select parent
			if  (parentTreeItem == null) 
				selectItem = parentTree.getItem(0);
			else 
				selectItem = parentTreeItem;
			} else {
				if (parentTree == detailsPolicyTree)
					//select something from master tree
					if (masterPolicyTree.getItems().length > 0) {
						selectItem = masterPolicyTree.getItem(0);
						selectionTree = masterPolicyTree;
					}
				
			}
			
		} else {
			if  (parentTreeItem == null) {
				if (isLastIndex)
					selectItem = parentTree.getItem(idx -1);
				else 
					selectItem = parentTree.getItem(idx);
			} else {
				if (isLastIndex)
					selectItem = parentTreeItem.getItem(idx -1);
				else 
					selectItem = parentTreeItem.getItem(idx);

			}
		}
		if (selectItem != null) {
			selectionTree.setSelection(selectItem);
			selectionTree.notifyListeners(SWT.Selection, new Event());
		}
	}

	/**
	 * @param policyToAdd
	 */
	private void processChildChangeEventAddServicePolicy(IServicePolicy policyToAdd) {
		boolean is4thLevelOrHigher = is4thLevelOrHigherPolicy(policyToAdd);
		IServicePolicy parent = policyToAdd.getParentPolicy();
		if (is4thLevelOrHigher) {
			if (detailsPolicyTree.isVisible()) {
				TreeItem[] children = detailsPolicyTree.getItems();
				TreeItem found = null;
				for (int i=0; i < children.length; i++) {
					 found = findPolicy(parent, children[i]);	
					 if (found != null)
						 break;
				}
				if (found != null && !found.isDisposed()) {
					addPolicy(policyToAdd, found, false);
				}
			}
		} else {
				//less than 4th level policy
				TreeItem[] children = masterPolicyTree.getItems();
				TreeItem found = null;
				for (int i=0; i < children.length; i++) {
					 found = findPolicy(parent, children[i]);	
					 if (found != null)
						 break;
				}
				if (found != null && !found.isDisposed()) {
					addPolicy(policyToAdd, found, false);
					
				}
		}
	}

	/**
	 * Find the policy within the tree ti
	 * @param policyToFind
	 */
	private TreeItem findPolicy(IServicePolicy policyToFind, TreeItem ti) {
		TreeItem toReturn = null;
		if (ti == null || !(ti.getData() instanceof IServicePolicy))
			return toReturn;
		IServicePolicy sp = (IServicePolicy)ti.getData();
		if (sp.getId().equals(policyToFind.getId()))
			return ti;
		else {
			TreeItem[] tiChildren = ti.getItems();
			for (int i =0; i < tiChildren.length; i++) {
				toReturn = findPolicy(policyToFind, tiChildren[i]);
				if (toReturn != null)
					break;
			}
		}
		return toReturn;
	}
	/**
	 * Initializes states of the controls using default values in the preference
	 * store.
	 */
	private void initializeDefaults() {
		if (project == null)
			platform.restoreDefaults();
		else
			platform.restoreDefaults(project);

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource() == masterPolicyTree || e.getSource() == detailsPolicyTree) {
			TreeItem[] selectedItems;
			if (e.getSource() == masterPolicyTree)
				selectedItems = masterPolicyTree.getSelection();
			else
				selectedItems = detailsPolicyTree.getSelection();
			//possible that an event is fired and no nodes selected
			if (!(selectedItems.length > 0))
				return;
			List<IServicePolicy> sp = new Vector<IServicePolicy>();
			for (int i = 0; i < selectedItems.length; i++) {
				sp.add((IServicePolicy) selectedItems[i].getData());
			}
			//update the context sensitive help
			updateCSH(sp);
			updateInfoPanels(sp);

			if (e.getSource() == masterPolicyTree) {
				// if selected node in master tree is 3rd level & has
				// children, populate details tree
				if (is3rdLevelPolInMasterTreeWithChildren(sp)) {
					populateDetailsPolicyTree(sp);
				} else {
					//if expandable composite was visible, collapse and set invisible
					if (excomposite.getVisible()) {
						excomposite.setVisible(false);
						excomposite.setExpanded(false);
						expandedStateChanged(excomposite);
					}
				}
			}
			addActionButtons(sp);
			lastSelectedSp = sp;

		} else {
			// an action control fired a change event
			Control actionControl = (Control) e.getSource();
			updatePolicy(actionControl);
			
			if( lastSelectedSp != null )
			{
			  addActionButtons(lastSelectedSp);  
			}
			
			//policy updates might delete nodes in the tree, so make sure action control not disposed
			IServicePolicy changedSP = null;
			if (!actionControl.isDisposed())
				changedSP = ((ActionControlData) actionControl.getData())
					.getSpList().get(0);
			error = validateAllPolicies(changedSP);
			//inform listeners that a control has changed, as composite may be in error now
			listener.widgetSelected(e);

		}

	}

	/**
	 * Populate the details policy tree with sp and their children
	 * @param sp a list of top level details tree service policies
	 */
	private void populateDetailsPolicyTree(List<IServicePolicy> sp) {
		List<IServicePolicy> childpolicyList = sp.get(0)
				.getChildren();
		detailsPolicyTree.removeAll();
		for (IServicePolicy policy : childpolicyList) {
			addPolicy(policy, detailsPolicyTree, false);
		}
		// update details preference tree
		TreeItem[] treeItems = detailsPolicyTree.getItems();
		for (int i = 0; i < treeItems.length; i++) {
			updateValidStates(treeItems[i], true);
		}

		excomposite.setVisible(true);
		excomposite.setText(sp.get(0).getDescriptor().getLongName()
				+ " (" + WstSPUIPluginMessages.TEXT_DETAILS + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		// text has changed, so need to force layout so updated
		excomposite.layout();
	}

	/**
	 * Updates the details and dependencies panels for the first service
	 * policy in spList
	 * @param spList a list of selected service policies
	 */
	private void updateInfoPanels(List<IServicePolicy> spList) {
		String desc = (spList.get(0) == null
				|| spList.get(0).getDescriptor() == null || spList.get(0)
				.getDescriptor().getDescription() == null) ? "" : spList.get(0) //$NON-NLS-1$
				.getDescriptor().getDescription();
		text_DetailsPanel_description.setText(desc);
		text_DetailsPanel_dependencies.setText(getDependanciesText(spList
				.get(0)));
	}

	/**
	 * Updates the context sensitive help for the composite with 
	 * that defined for the first policy in spList (or a parent of 
	 * first policy in spList policy does not define any CSH)
	 * @param spList the list of selected service policies
	 */
	private void updateCSH(List<IServicePolicy> spList) {
		String CSH_ID = (spList.get(0) == null || spList.get(0).getDescriptor() == null) ? null
				: spList.get(0).getDescriptor().getContextHelpId();
		IServicePolicy parentSP = spList.get(0);
		while ((CSH_ID == null || CSH_ID.length() == 0)) {
			parentSP = parentSP.getParentPolicy();
			if (parentSP == null)
				break;
			CSH_ID = (parentSP.getDescriptor() == null) ? null : parentSP
					.getDescriptor().getContextHelpId();
		}
		helpSystem.setHelp(this, CSH_ID);
	}

	/**
	 * Validates all Service Policies in the model
	 * 
	 * @param IServicePolicy
	 *            the focus policy, i.e. one that changed state
	 * @return IStatus null if no Service policies in error, otherwise a Service
	 *         Policy error (the focucPolicy error if it is in error).
	 *         Postcondition- the error property is updated.
	 */
	private IStatus validateAllPolicies(IServicePolicy focusPolicy) {
		// remove all errors as going to re-validate
		allErrors.clear();
		// re-validate all policies in model
		List<IServicePolicy> servicePolicyList = platform
				.getRootServicePolicies(null);
		for (IServicePolicy servicePolicyItem : servicePolicyList) {
			validatePolicy(servicePolicyItem);
		}
		// define error message
		IStatus error = (focusPolicy == null) ? null : allErrors
				.get(focusPolicy.getId());
		if (error == null)
			if (!allErrors.isEmpty())
				error = allErrors.get(allErrors.keys().nextElement());

		// update master policy tree
		TreeItem[] treeItems = masterPolicyTree.getItems();
		for (int i = 0; i < treeItems.length; i++) {
			updateValidStates(treeItems[i], false);
		}
		if (detailsPolicyTree.isVisible()) {
			// update details preference tree
			treeItems = detailsPolicyTree.getItems();
			for (int i = 0; i < treeItems.length; i++) {
				updateValidStates(treeItems[i], false);
			}

		}
		return error;
	}

	/**
	 * Updates the policy trees to reflect valid or invalid states, invalid states are 
	 * propagated up through the tree, and up to master tree if updateDetailsOnly is false
	 * @param ti the TreeItem to update, updates ti and all it's children
	 * @param updateDetailsOnly if false then propagate invalid states into
	 * master tree 
	 */
	private void updateValidStates(TreeItem ti, boolean updateDetailsOnly) {
		// assume tree item is valid for now
		IServicePolicy sp = (IServicePolicy) ti.getData();
		setValidPolicyState(ti);

		if (allErrors.containsKey(sp.getId()))
			setInvalidPolicyState(ti, updateDetailsOnly);
		TreeItem[] treeItems = ti.getItems();
		if (ti.getItems().length == 0) {
			if (sp.getChildren().size() != 0 && policyChildrenInvalid(sp))
				setInvalidPolicyState(ti, updateDetailsOnly);
		} else {
			for (int i = 0; i < treeItems.length; i++) {

				updateValidStates(treeItems[i], updateDetailsOnly);
			}
		}

	}

	/**
	 * Checks whether the sp has invalid children
	 * @param sp the service policy whose children may be invalid
	 * @return true if sp has an invalid child
	 */
	private boolean policyChildrenInvalid(IServicePolicy sp) {
		boolean toReturn = false;
		List<IServicePolicy> servicePolicyList = sp.getChildren();
		for (IServicePolicy servicePolicyItem : servicePolicyList) {
			if (allErrors.containsKey(servicePolicyItem.getId()))
				return true;
			else
				toReturn = policyChildrenInvalid(servicePolicyItem);
		}
		return toReturn;
	}

	/**
	 * Validates the Service Policies for the sp and all it's children, creating 
	 * error status objects for any unsatisfied relationships
	 * 
	 * @param sp
	 *            the service policy to validate
	 */
	private void validatePolicy(IServicePolicy sp) {
		String operationLongName;
		String operationSelectionLongName;
		String dependantPolicyShortName;
		List<IPolicyRelationship> relationShipList = sp.getRelationships();
		String currentValueID = null;
		if (relationShipList != null && relationShipList.size() > 0) {
		for (IPolicyRelationship relationShipItem : relationShipList) {
			//operations for the policy
			List<IPolicyOperation> policyOperationsList = platformUI
					.getOperations(relationShipItem.getPolicyEnumerationList()
							.getPolicy(), project == null);
			//default operation name
			operationLongName = ""; //$NON-NLS-1$
			for (IPolicyOperation policyOperationItem : policyOperationsList) {
				if (policyOperationItem.isUseDefaultData()) {
					operationLongName = policyOperationItem.getDescriptor()
							.getLongName();
					currentValueID = policyOperationItem.getStateItem(project);
					break;
				}
			}
			// a list of states
			List<IStateEnumerationItem> spStateEnumerationList = relationShipItem
					.getPolicyEnumerationList().getEnumerationList();
			// a list of related policies and their acceptable states to satisfy a stateEnumerationItem in spStateEnumerationList
			List<IPolicyEnumerationList> relatedPolicies = relationShipItem
					.getRelatedPolicies();
			for (IStateEnumerationItem stateEnumerationItem : spStateEnumerationList) {
				if (stateEnumerationItem.getId() != currentValueID)
					continue;
				//long name of the related policy operation
				operationSelectionLongName = stateEnumerationItem.getLongName();
				for (IPolicyEnumerationList relatedPolicyEnumerationItem : relatedPolicies) {
					dependantPolicyShortName = relatedPolicyEnumerationItem
							.getPolicy().getDescriptor().getShortName();
					//the list of related sp states that satisfy the stateEnumerationItem
					List<IStateEnumerationItem> relatedSPStateEnumerationList = relatedPolicyEnumerationItem
							.getEnumerationList();
					//the list of valid ids for the related service policy operation
					List<String> validIds = new Vector<String>();
					for (int i = 0; i < relatedSPStateEnumerationList
							.size(); i++) {
						validIds
								.add(relatedSPStateEnumerationList
										.get(i).getId());

					}
					//get the current value of the related service policy default operation
					String currentItemID = ""; //$NON-NLS-1$
					List<IPolicyOperation> relatedServicePolicyPolicyOperationsList = platformUI.getOperations(relatedPolicyEnumerationItem.getPolicy(), project ==null);
					IPolicyOperation relatedPolicyOperationItem = null;
					for (IPolicyOperation relatedServicePolicyPolicyOperationItem : relatedServicePolicyPolicyOperationsList) {
						if (relatedServicePolicyPolicyOperationItem.isUseDefaultData()) {
							currentItemID = relatedServicePolicyPolicyOperationItem.getStateItem(project);
							relatedPolicyOperationItem = relatedServicePolicyPolicyOperationItem;
							break;
						}
					}
					
					if (!validIds
							.contains(currentItemID)) {
						// policy state is invalid
						IStatus error = createUnsatisfiedRelationshipError(sp,
								operationLongName,
								operationSelectionLongName,
								dependantPolicyShortName,
								relatedPolicyOperationItem,
								relatedSPStateEnumerationList);
						allErrors.put(sp.getId(), error);

					}


				}

			}

		}
		}

		List<IServicePolicy> servicePolicyChildrenList = sp.getChildren();
		for (IServicePolicy servicePolicyChildrenItem : servicePolicyChildrenList) {
			validatePolicy(servicePolicyChildrenItem);

		}

	}

	/**
	 * Sets the icon for the ti denoting it as valid
	 * @param ti a valid tree item
	 */
	private void setValidPolicyState(TreeItem ti) {
		IServicePolicy sp = (IServicePolicy) ti.getData();
		setImage(ti, sp, getIconOverlayInfo(sp, false));
	}

	/**
	 * Sets the icon for ti denoting it as invalid, as well as ti's parent tree items
	 * @param ti an invalid tree item
	 * @param updateDetailsOnly true indicates do not update parent tree items only in master tree
	 */
	private void setInvalidPolicyState(TreeItem ti, boolean updateDetailsOnly) {
		IServicePolicy SP = (IServicePolicy) ti.getData();
		setImage(ti, SP, getIconOverlayInfo(SP, true));
		TreeItem parent;
		if (ti.getParent() == masterPolicyTree || updateDetailsOnly)
			parent = ti.getParentItem();
		else
			parent = (ti.getParentItem() == null && SP.getParentPolicy() != null) ? getParentInMasterTree(SP
					.getId())
					: ti.getParentItem();
		while (parent != null) {
			setImage(parent, (IServicePolicy) parent.getData(),
					getIconOverlayInfo((IServicePolicy) parent.getData(),
							true));
			if (updateDetailsOnly)
				parent = parent.getParentItem();
			else {
				parent = (parent.getParentItem() == null && ((IServicePolicy) parent
						.getData()).getParentPolicy() != null) ? getParentInMasterTree(((IServicePolicy) parent
						.getData()).getParentPolicy().getId())
						: parent.getParentItem();
			}

		}
	}

	/**
	 * Creates an error status object for the sp. The error message contains information about 
	 * an unsatisfied relationship
	 * @param sp
	 * @param operationLongName
	 * @param operationSelectionLongName
	 * @param dependantPolicyShortName
	 * @param relatedOperation
	 * @param relatedOperationAcceptableValues
	 * @return an Error status
	 */
	private IStatus createUnsatisfiedRelationshipError(IServicePolicy sp,
			String operationLongName, String operationSelectionLongName,
			String dependantPolicyShortName,
			IPolicyOperation relatedOperation,
			List<IStateEnumerationItem> relatedOperationAcceptableValues) {
		String dependantOperationShortName;
		String dependantOperationSelectionShortNameList;
		dependantOperationShortName = relatedOperation
				.getDescriptor().getShortName();
		dependantOperationSelectionShortNameList = new String();
		for (int i = 0; i < relatedOperationAcceptableValues.size(); i++) {
			IStateEnumerationItem item = relatedOperationAcceptableValues.get(i);
			if (i != 0)
				dependantOperationSelectionShortNameList += " | "; //$NON-NLS-1$
			dependantOperationSelectionShortNameList += item.getShortName();

		}
		String[] args = { sp.getDescriptor().getLongName(), operationLongName,
				operationSelectionLongName, dependantPolicyShortName,
				dependantOperationShortName,
				dependantOperationSelectionShortNameList};
		IStatus error = new Status(Status.ERROR,
				ServicePolicyActivatorUI.PLUGIN_ID, NLS.bind(
						WstSPUIPluginMessages.SERVICEPOLICIES_DEPENDENCY_ERROR,
						args));
		return error;
	}

	/**
	 * @param parentID the id to find in the master tree
	 * @return a TreeItem with the id in the master tree
	 */
	private TreeItem getParentInMasterTree(String parentID) {
		TreeItem parent = null;
		TreeItem[] rootNodes = masterPolicyTree.getItems();
		for (int i = 0; i < rootNodes.length; i++) {
			parent = findChildNode(rootNodes[i], parentID);
			if (parent != null)
				break;
		}
		return parent;

	}

	/**
	 * Returns a TreeItem associated with a child policy of ti
	 * @param ti
	 * @param id the policy id of a child node of ti
	 * @return null if there is no child policy matching id
	 */
	private TreeItem findChildNode(TreeItem ti, String id) {
		TreeItem toReturn = null;
		if (((IServicePolicy) ti.getData()).getId().equals(id)) {
			toReturn = ti;
		} else {
			TreeItem[] childItems = ti.getItems();
			for (int i = 0; i < childItems.length; i++) {
				toReturn = findChildNode(childItems[i], id);
			}

		}
		return toReturn;

	}

	/**
	 * Returns dependancy information about sp, that is information about
	 * it's relationships (if any) with other service policies
	 * @param sp the service policy to get dependency info about
	 * @return
	 */
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

		for (IPolicyRelationship relationShipItem : relationShipList) {
			List<IPolicyOperation> policyOperationsList = platformUI
					.getOperations(relationShipItem.getPolicyEnumerationList()
							.getPolicy(), project == null);
			operationLongName = ""; //$NON-NLS-1$
			for (IPolicyOperation policyOperationItem : policyOperationsList) {
				if (policyOperationItem.isUseDefaultData()) {
					operationLongName = policyOperationItem.getDescriptor()
							.getLongName();
				break;
				}
			}
			// policies associated with the relationship item
			List<IPolicyEnumerationList> relatedPolicies = relationShipItem
					.getRelatedPolicies();
			List<IStateEnumerationItem> spStateEnumerationList = relationShipItem
					.getPolicyEnumerationList().getEnumerationList();
			for (IStateEnumerationItem stateEnumerationItem : spStateEnumerationList) {
				operationSelectionLongName = stateEnumerationItem.getLongName();
				for (IPolicyEnumerationList relatedPolicyEnumerationItem : relatedPolicies) {
					dependantPolicyShortName = relatedPolicyEnumerationItem
							.getPolicy().getDescriptor().getShortName();
					List<IStateEnumerationItem> relatedSPStateEnumerationList = relatedPolicyEnumerationItem
							.getEnumerationList();
					List<IPolicyOperation> relatedPolicyOperationsList = platformUI
							.getOperations(relatedPolicyEnumerationItem
									.getPolicy(), (project == null));
					dependantOperationShortName = ""; //$NON-NLS-1$
					for (IPolicyOperation relatedPolicyOperationItem : relatedPolicyOperationsList) {
						if (relatedPolicyOperationItem.isUseDefaultData())
							dependantOperationShortName = relatedPolicyOperationItem
									.getDescriptor().getShortName();
						break;
					}

					dependantOperationSelectionShortNameList = new String();
					for (int i = 0; i < relatedSPStateEnumerationList.size(); i++) {
						IStateEnumerationItem item = relatedSPStateEnumerationList
								.get(i);
						if (i != 0)
							dependantOperationSelectionShortNameList += " | "; //$NON-NLS-1$
						dependantOperationSelectionShortNameList += item
								.getShortName();

					}
					String[] args = { operationLongName,
							operationSelectionLongName,
							dependantPolicyShortName,
							dependantOperationShortName,
							dependantOperationSelectionShortNameList };
					toReturn += NLS.bind(
							WstSPUIPluginMessages.SERVICEPOLICIES_DEPENDENCIES,
							args)
							+ "\r\n"; //$NON-NLS-1$

				}

			}

		}
		return toReturn;
	}


	/**
	 * Saves to the preference store the widget data (widget data contains all 
	 * service policy operations the control applies to)
	 * @param actionControl a widget who's data needs to be saved to the preference store
	 * 
	 */
	private void updatePolicy(Control actionControl) {

		List<IPolicyOperation> policyOperationList = ((ActionControlData) actionControl.getData()).getPoList();
		IPolicyOperation po0 = policyOperationList.get(0);
		
		List<IServicePolicy> servicePolicyList = ((ActionControlData) actionControl
				.getData()).getSpList();
		if (actionControl instanceof Button
				&& po0.getOperationKind().equals(OperationKind.complex))
			updateComplexOperationPreference(po0, servicePolicyList);
		else
			for (IPolicyOperation policyOperationItem : policyOperationList) {

				updateSelectionOperationPreference(actionControl, policyOperationItem);
			}

	}

	public boolean okToLeave() {
		return allErrors.size() == 0;

	}

	/**
	 * Saves value of enumeration, selection, and iconselection type operations
	 * to the preference store
	 * @param actionControl the action control containing the value to set for the preference
	 * @param po 
	 * @param sp
	 */
	private void updateSelectionOperationPreference(Control actionControl, IPolicyOperation po) {
		String selectedValue;
		
		// We are updating the state of a service policy.  We don't want this
		// update to trigger our state change listener.
		enableStateListener( false );
		
		if (actionControl instanceof Combo) {
			selectedValue = ((Combo) actionControl).getText();
			List<IStateEnumerationItem> enumItemList = ServicePolicyPlatform
					.getInstance().getStateEnumeration(po.getEnumerationId());
			for (IStateEnumerationItem enumItem : enumItemList) {
				if (enumItem.getLongName().equals(selectedValue)) {
					po.setStateItem(project, enumItem.getId());
					break;
				}
			}

		} else {
			if (((Button) actionControl).getSelection()) {
				po.setStateItem(project, "org.eclipse.wst.true"); //$NON-NLS-1$
			} else
				po.setStateItem(project, "org.eclipse.wst.false"); //$NON-NLS-1$

		}

		// Reenable our state change listener.
		enableStateListener( true );
	}

	
	private void addStateListener( IServicePolicy policy )
	{
	  if( stateChangePolicySet.contains( policy ) ) return;
	  
	  if( project == null )
	  {
	    policy.getPolicyState().addPolicyStateChangeListener( stateChangeListener, false );
	  }
	  else
	  {
	    policy.getPolicyState( project ).addPolicyStateChangeListener( stateChangeListener, false );
	  }
	  
	  stateChangePolicySet.add( policy );
	}
	
	private void removeStateListener( IServicePolicy policy )
	{
    if( stateChangePolicySet.contains( policy ) ) return;
    
    if( project == null )
    {
	    policy.getPolicyState().removePolicyStateChangeListener( stateChangeListener );
    }
    else
    {
      policy.getPolicyState( project ).removePolicyStateChangeListener( stateChangeListener );      
    }
    
	  stateChangePolicySet.remove( policy );
	}
	
	private void enableStateListener( boolean enabled )
	{
	  stateChangeEnabled = enabled;
	}
	
	/**
	 * Launches the complex operation for the list of service policies
	 * @param po a "complex" policy operation 
	 * @param sps a list of service policies to apply the operation to
	 */
	private void  updateComplexOperationPreference(IPolicyOperation po,
			List<IServicePolicy> sps) {
		//complex operations can result in added and deleted nodes, cache them until operation completed
		bComplexOpCompleted = false;
		listChildChangeEvents = new Vector<ChildChangeEvent>();
		try {
			po.launchOperation(sps);
		} catch (RuntimeException e) {
			//just want to make sure finally clause is executed so can update UI of additions/deletions
		} finally {
			bComplexOpCompleted =true;
			Hashtable<String, IServicePolicy> topLevelAdds = getHighestLevelChildren(listChildChangeEvents, true);
			for (ChildChangeEvent childChangeEventItem : listChildChangeEvents) {
				processChildChangeEvent(childChangeEventItem, topLevelAdds);
			}
			
		}
	}
	private Hashtable<String, IServicePolicy> getHighestLevelChildren (List<ChildChangeEvent> htChildChangeEvents, boolean forAddOnly) {
		Hashtable<String, IServicePolicy> toReturn = new Hashtable<String, IServicePolicy>();
		Iterator<ChildChangeEvent> elements = htChildChangeEvents.iterator();
		ChildChangeEvent next;
		Hashtable<String, IServicePolicy> allSps = new Hashtable<String, IServicePolicy>();
		while (elements.hasNext()) {
			next = elements.next();
			List<IServicePolicy> changedChildrenList = next.getChangedChildren();
			List<Boolean> addedList = next.getAdded();
			for (int i = 0; i < changedChildrenList.size(); i++) {
				if (forAddOnly && addedList.get(i).booleanValue() == true)
					allSps.put(changedChildrenList.get(i).getId(), changedChildrenList.get(i));
			}
		}
		Enumeration<IServicePolicy> servicePolicyElements = allSps.elements();
		IServicePolicy nextServicePolicyElement;
		while (servicePolicyElements.hasMoreElements()) {
			nextServicePolicyElement = servicePolicyElements.nextElement();
			if (nextServicePolicyElement.getParentPolicy() == null || !allSps.containsKey(nextServicePolicyElement.getParentPolicy().getId())) {
				toReturn.put(nextServicePolicyElement.getId(), nextServicePolicyElement);
			}
		}
		return toReturn;
	}
	public void dispose() {
		super.dispose();
		iconManager.dispose();
		
		for( IServicePolicy policy : stateChangePolicySet )
		{
		  IPolicyState state = project == null ? policy.getPolicyState() : policy.getPolicyState( project );
		  
		  state.removePolicyStateChangeListener( stateChangeListener );
		}
	}

	/**
	 * Create UI widgets for operations applicable to the list of service policies sp
	 * @param spList the list of service policies to create UI widgets, the UI widgets
	 * will be used to define operations/preferences to apply to the service policies in the list
	 */
	private void addActionButtons(List<IServicePolicy> spList) {
	  
		// remove existing action controls
		Control[] toRemove = operationsComposite.getChildren();
		for (int i = 0; i < toRemove.length; i++) {
			toRemove[i].dispose();
		}
		List<List<IPolicyOperation>> policyOperationListList = platformUI.getOperationsList(spList, (project == null));

		for (List<IPolicyOperation> policyOperationList : policyOperationListList) {
			if (policyOperationList.size() > 0) {
			if (policyOperationList.get(0).getOperationKind() == IPolicyOperation.OperationKind.complex) {
				addComplexOperationUI(policyOperationList, spList);
			} else
				addSelectionOperationUI(policyOperationList, spList);
			}

		}
		// just removed and added some controls so force composite
		// to re-layout it's controls
		operationsScrolledComposite.setMinSize(operationsComposite.computeSize(
				SWT.DEFAULT, SWT.DEFAULT));
		operationsComposite.layout();
	}
	/**
	 * Creates UI widgets for policy operations of enumeration, selection, and 
	 * iconselection types
	 * @param po the policy operation to create a UI widget for
	 * @param sp the service policies this operation will apply to
	 */
	private void addSelectionOperationUI(List<IPolicyOperation> pos, List<IServicePolicy> sps) {
		//use the first operation instance in the list for data
		IPolicyOperation po0 = pos.get(0);
		//IServicePolicy sp0 = pos.get(0).getServicePolicy();
		IDescriptor d = po0.getDescriptor();
		Control selectionControl;
		if (po0.getOperationKind() == IPolicyOperation.OperationKind.enumeration) {
			Label l = new Label(operationsComposite, SWT.NONE);
			l.setText(d.getLongName() + ":"); //$NON-NLS-1$
			Combo cb = new Combo(operationsComposite, SWT.DROP_DOWN
					| SWT.READ_ONLY);
			selectionControl = cb;
			cb.addSelectionListener(this);
			List<IStateEnumerationItem> enumItemList = ServicePolicyPlatform
					.getInstance().getStateEnumeration(po0.getEnumerationId());
			for (IStateEnumerationItem enumItem : enumItemList) {
				cb.add(enumItem.getLongName());
			}
			l.setEnabled(po0.isEnabled(sps));
			cb.setEnabled(po0.isEnabled(sps));
			if (cb.isEnabled())
				cb.setText(getEnumerationOperationCurrentSelection(po0));
		} else {
			// a selection or icon
			Button checkBox = new Button(operationsComposite, SWT.CHECK);
			selectionControl = checkBox;
			checkBox.addSelectionListener(this);
			GridData checkBoxGD = new GridData();
			checkBoxGD.horizontalSpan = 2;
			checkBox.setLayoutData(checkBoxGD);
			checkBox.setText(d.getLongName());
			checkBox.setEnabled(po0.isEnabled(sps));
			if (checkBox.isEnabled())
				checkBox
						.setSelection(getSelectionOperationCurrentSelection(po0));

		}

		selectionControl.setData(new ActionControlData(sps, pos));

	}
	/**
	 * @param sp a service policy with a selection or iconselection operation defined
	 * @return true if the operation is currently selected
	 */
	private boolean getSelectionOperationCurrentSelection(IPolicyOperation po) {
		return po.getStateItem(project).equals("org.eclipse.wst.true") ? true : false; //$NON-NLS-1$

	}

	/**
	 * @param sp a service policy with an enumeration operation defined
	 * @return the currently selected enumeration item
	 */
	private String getEnumerationOperationCurrentSelection(IPolicyOperation po) {
		String currentSelection = ""; //$NON-NLS-1$
		List<IStateEnumerationItem> enumItemList = ServicePolicyPlatform
		.getInstance().getStateEnumeration(po.getEnumerationId());
		for (IStateEnumerationItem enumItem : enumItemList) {
			if (enumItem.getId().equals(po.getStateItem(project))) {
				currentSelection = enumItem.getLongName();
				break;
			}
		}
		return currentSelection;
	}

	/**
	 * Creates UI widgets for policy operations of complex type
	 * @param po the policy operation to create a UI widget for
	 * @param sps the service policies this operation will apply to
	 */
	private void addComplexOperationUI(List<IPolicyOperation> pos,
			List<IServicePolicy> sps) {
		IPolicyOperation po0 = pos.get(0);
		IDescriptor d = po0.getDescriptor();
		Button pushButton = new Button(operationsComposite, SWT.PUSH);
		GridData pushButtonGD = new GridData();
		pushButtonGD.horizontalSpan = 2;
		pushButton.setLayoutData(pushButtonGD);
		pushButton.setText(d.getLongName());
		pushButton.addSelectionListener(this);
		pushButton.setData(new ActionControlData(sps, pos));
		pushButton.setEnabled(po0.isEnabled(sps));
  
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */  
	public void widgetDefaultSelected(SelectionEvent e) {

	}

	/**
	 * @return the error that should be shown, if there are multiple validation errors,
	 * return the validation error for a service policy that just changes state (often the
	 * selected service policy)
	 */
	public IStatus getError() {
		return error;
	}
	
	private class StateChangeListener implements IPolicyStateChangeListener
	{
    public void policyStateChange(IServicePolicy policy, String key, String oldValue, String newValue)
    {
      // We are not enabled for state change events so we will ignore this event.
      if( !stateChangeEnabled ) return;
      
      error = validateAllPolicies( policy );
      
      //inform listeners that a control has changed, as composite may be in error now
      listener.widgetSelected( null );
    }	  
	}
}
