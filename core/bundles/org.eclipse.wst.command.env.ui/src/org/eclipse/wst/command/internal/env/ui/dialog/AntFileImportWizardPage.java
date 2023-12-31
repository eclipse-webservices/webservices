/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
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
 * 20080221   146023 gilberta@ca.ibm.com - Gilbert Andrews 
 * 20080512   222094 makandre@ca.ibm.com - Andrew Mak, Error handling when Ant template cannot be found
 * 20080618   237671 makandre@ca.ibm.com - Andrew Mak, Label on Ant files wizard is truncated
 * 20080716   238059 makandre@ca.ibm.com - Andrew Mak, New ant files wizard not working
 * 20081023   251911 ericdp@ca.ibm.com   - Eric D. Peters, NPE in .log when choosing ant files to import
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.ui.dialog;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardResourceImportPage;
import org.eclipse.wst.command.internal.env.ui.EnvironmentUIMessages;
import org.eclipse.wst.command.internal.env.ui.common.LabelsAndIds;

public class AntFileImportWizardPage extends WizardResourceImportPage{
	  
	private String pluginId_ = "org.eclipse.wst.command.internal.env.ui";
	
	/* CONTEXT_ID SRPF0001 for server preference on the server runtime preference page */
	private String INFOPOP_SRPF_SERVER_PREF = pluginId_ + ".SRPF0001";
	private Combo scenario_;

	/* CONTEXT_ID SRPF0002 for runtime preference on the server runtime preference page */
	private String INFOPOP_SRPF_RUNTIME_PREF = pluginId_ + ".SRPF0002";
	private Combo runtime_;	
	
	private AntExtensionCreation antExtC_;
	private LabelsAndIds labelsAndIdsScenario_;
	private LabelsAndIds labelsAndIdsRuntime_;
//	private int runtimeIdx_;
	
	public AntFileImportWizardPage(String pageName,
	            IStructuredSelection selection) {
	        super("WSAntFilesPage1", selection);//$NON-NLS-1$
	        setTitle(pageName);	        
	        setDescription(EnvironmentUIMessages.WIZARD_PAGE_DESC_ANT);	        
	    }
	
		
	public Combo createCombo( Composite parent, String labelName, String tooltip, String infopop, int style )
	  {    
	    tooltip = tooltip == null ? labelName : tooltip;
	    
	    if( labelName != null )
	    {
	      Label label = new Label( parent, SWT.NONE);
	      label.setText( labelName );
	      label.setToolTipText( tooltip );
	    }
	    
	    Combo combo = new Combo( parent, style );
	    GridData griddata = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
	     
	    combo.setLayoutData( griddata );
	    combo.setToolTipText( tooltip );
	    
	    if( infopop != null ) PlatformUI.getWorkbench().getHelpSystem().setHelp( combo, pluginId_ + "." + infopop );
	    
	    return combo;      
	  }
	
	protected ITreeContentProvider getFileProvider() {
		return null;
	}
	
	public IPath getPath()
	{
		return getContainerFullPath();
	}
	
	protected void createSourceGroup(Composite parent) {
		Composite page = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		page.setLayout(gl);
		GridData gd = new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL);
		page.setLayoutData(gd);
	  
		runtime_ = createCombo(page,EnvironmentUIMessages.LABEL_RUNTIMES_LIST,
			  EnvironmentUIMessages.TOOLTIP_PPAD_COMBO_RUNTIME,
									 INFOPOP_SRPF_RUNTIME_PREF,
									 SWT.SINGLE|SWT.BORDER|SWT.READ_ONLY);
	
		runtime_.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {}
			
			public void widgetSelected(SelectionEvent e) {
				handleRuntimeSelected();					
			}
		});
	
		scenario_ = createCombo(page,EnvironmentUIMessages.LABEL_SERVERS_LIST,
				  EnvironmentUIMessages.TOOLTIP_PPAD_COMBO_SCENARIO,
										INFOPOP_SRPF_SERVER_PREF,
										SWT.SINGLE|SWT.BORDER|SWT.READ_ONLY);
		
		iniatialize();
	}
	
	protected boolean validateSourceGroup() {
		return runtime_.getItemCount() > 0;
	}

	protected ITreeContentProvider getFolderProvider() {
		 return null;
	}
	
	protected void createOptionsGroup(Composite parent) {
        //empty - don't want an options group 
    }
	
	public void handleEvent(Event event)
	{
		if (scenario_ == event.widget)
			handleScenarioSelected();
		else if (runtime_ == event.widget)
			handleRuntimeSelected();
		else
			super.handleEvent(event);
			
	}
	
	public void handleRuntimeSelected(){
		labelsAndIdsScenario_ = antExtC_.getScenarioLabelsByRuntime(labelsAndIdsRuntime_.getId(runtime_.getSelectionIndex()));
		setScenarioLabels(labelsAndIdsScenario_.getLabels());
	}
	
	public void handleScenarioSelected(){
		
	}
	
	private void iniatialize(){
		antExtC_ = AntExtensionCreation.getInstance();
		labelsAndIdsRuntime_ = antExtC_.getRuntimeLabelsAndIds();
		if (labelsAndIdsRuntime_.getLabels().length > 0)
			setRuntimeLabels(labelsAndIdsRuntime_.getLabels());
	}
	
	public String getScenario(){
		return labelsAndIdsScenario_.getId(scenario_.getSelectionIndex());
	}
	
	private void setSelection(Combo combo, String s)
	{
		String[] items = combo.getItems();
		for (int i=0; i<items.length; i++)
		{
			if (items[i].equals(s))
			{
				combo.select(i);
				return;
			}
		}
	}
	
	private void setRuntimeLabels(String[] runtimeLabels)
	{
		runtime_.setItems(runtimeLabels);
	    //until the preferences are set up
		setSelection(runtime_,runtimeLabels[0]);
//		runtimeIdx_ = 0;
		labelsAndIdsScenario_ = antExtC_.getScenarioLabelsByRuntime(labelsAndIdsRuntime_.getId(0));
		setScenarioLabels(labelsAndIdsScenario_.getLabels());
	}
	
	private void setScenarioLabels(String[] scenarioLabels)
	{
		scenario_.setItems(scenarioLabels);
	    //until the preferences are set up
		setSelection(scenario_,scenarioLabels[0]);
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.WizardDataTransferPage#displayErrorDialog(java.lang.String)
	 */
	protected void displayErrorDialog(String message) {
		super.displayErrorDialog(message);
	}
}



