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

package org.eclipse.jst.ws.internal.consumption.ui.preferences;

import java.util.ArrayList;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.WebServiceServerRuntimeTypeRegistry;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.selection.SelectionListChoices;


/**
 *
 */
public class ServerRuntimePreferencePage extends PreferencePage implements IWorkbenchPreferencePage, Listener 
{
	private String pluginId_= "org.eclipse.jst.ws.consumption.ui";
	private MessageUtils msgUtils_;
	
	/* CONTEXT_ID SRPF0001 for server preference on the server runtime preference page */
	private String INFOPOP_SRPF_SERVER_PREF = pluginId_ + ".SRPF0001";
	private Combo server_;

	/* CONTEXT_ID SRPF0002 for runtime preference on the server runtime preference page */
	private String INFOPOP_SRPF_RUNTIME_PREF = pluginId_ + ".SRPF0002";
	private Combo runtime_;	

	/* CONTEXT_ID SRPF0003 for J2EE version preference on the server runtime preference page */
	private String INFOPOP_SRPF_J2EE_PREF = pluginId_ + ".SRPF0003";
	private Combo j2eeVersion_;
	
	SelectionListChoices serverToRuntimeToJ2EE_;
	
	public void init(IWorkbench workbench)   { }	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) 
	{
	  
	  msgUtils_ = new MessageUtils(pluginId_ + ".plugin", this);
	  UIUtils uiUtils = new UIUtils(msgUtils_, pluginId_);
	  
	  Composite page = new Composite(parent, SWT.NONE);
	  GridLayout gl = new GridLayout();
	  gl.numColumns = 2;
	  page.setLayout(gl);
	  GridData gd = new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL);
	  page.setLayoutData(gd);
	  
	  server_ = uiUtils.createCombo(page,"LABEL_SERVERS_LIST",
	  		                        "TOOLTIP_SRPF_COMBO_SERVER",
									INFOPOP_SRPF_SERVER_PREF,
									SWT.SINGLE|SWT.BORDER|SWT.READ_ONLY);
	  
	  runtime_ = uiUtils.createCombo(page,"LABEL_RUNTIMES_LIST",
	  		                         "TOOLTIP_SRPF_COMBO_RUNTIME",
									 INFOPOP_SRPF_RUNTIME_PREF,
									 SWT.SINGLE|SWT.BORDER|SWT.READ_ONLY);
	  
	  j2eeVersion_ = uiUtils.createCombo(page,"LABEL_J2EE_VERSION",
                                         "TOOLTIP_SRPF_COMBO_J2EE",
			                             INFOPOP_SRPF_J2EE_PREF,
			                             SWT.SINGLE|SWT.BORDER|SWT.READ_ONLY);	  

	  initializeValues();
	  startListening();
	  return page;
	}

	private void initializeValues()
	{
		PersistentServerRuntimeContext context = WebServiceConsumptionUIPlugin.getInstance().getServerRuntimeContext();
		serverToRuntimeToJ2EE_ = WebServiceServerRuntimeTypeRegistry.getInstance().getServerToRuntimeToJ2EE();


		setServerItems(serverToRuntimeToJ2EE_.getList().getList());
		String defaultServerText = context.getServerFactoryId();
		serverToRuntimeToJ2EE_.getList().setSelectionValue(defaultServerText);
		setServerSelection(defaultServerText);
		
		setRuntimeItems(serverToRuntimeToJ2EE_.getChoice().getList().getList());
		String defaultRuntimeText = context.getRuntimeId();
		serverToRuntimeToJ2EE_.getChoice().getList().setSelectionValue(defaultRuntimeText);
		setRuntimeSelection(defaultRuntimeText);
		
		setJ2EEItems(serverToRuntimeToJ2EE_.getChoice().getChoice().getList().getList());
		String defaultJ2EEVersion = context.getJ2EEVersion();
		serverToRuntimeToJ2EE_.getChoice().getChoice().getList().setSelectionValue(defaultJ2EEVersion);
		setJ2EESelection(defaultJ2EEVersion);
		
	}
	
	public void handleEvent(Event event)
	{
		if (server_ == event.widget)
		{
			handleServerSelected();
		}
		else if (runtime_ == event.widget)
		{
			handleRuntimeSelected();
		}
		else if (j2eeVersion_ == event.widget)
		{			
			handleJ2EESelected();
		}			
	}

	private void handleServerSelected()
	{
		stopListening();
		
		String selectedText = getServerSelection();
		serverToRuntimeToJ2EE_.getList().setSelectionValue(selectedText);
		
		//Update the runtime -------------------------------
		setRuntimeItems(serverToRuntimeToJ2EE_.getChoice().getList().getList());
		if (serverToRuntimeToJ2EE_.getChoice().getList().getList().length > 0)
		  serverToRuntimeToJ2EE_.getChoice().getList().setIndex(0);
		
		String newRuntimeText = serverToRuntimeToJ2EE_.getChoice().getList().getSelection();
		if (newRuntimeText!=null && newRuntimeText.length()>0)
			setRuntimeSelection(newRuntimeText);
		
		//Update the j2ee version -------------------------------
		if (newRuntimeText!=null && newRuntimeText.length()>0)
		{
		  setJ2EEItems(serverToRuntimeToJ2EE_.getChoice().getChoice().getList().getList());
		  if (serverToRuntimeToJ2EE_.getChoice().getChoice().getList().getList().length > 0)
			  serverToRuntimeToJ2EE_.getChoice().getChoice().getList().setIndex(0);
			
			String newJ2EEText = serverToRuntimeToJ2EE_.getChoice().getChoice().getList().getSelection();
			if (newJ2EEText!=null && newJ2EEText.length()>0)
				setJ2EESelection(newJ2EEText);
		}
		else
		{
		  setJ2EEItems(new String[0]);
		}
		
		startListening();
	}
	
	private void handleRuntimeSelected()
	{
		stopListening();
		
		String selectedText = getRuntimeSelection();
		serverToRuntimeToJ2EE_.getChoice().getList().setSelectionValue(selectedText);
		
		//Update the j2ee version -------------------------------
		setJ2EEItems(serverToRuntimeToJ2EE_.getChoice().getChoice().getList().getList());
		if (serverToRuntimeToJ2EE_.getChoice().getChoice().getList().getList().length > 0)
		  serverToRuntimeToJ2EE_.getChoice().getChoice().getList().setIndex(0);
		
		String newJ2EEText = serverToRuntimeToJ2EE_.getChoice().getChoice().getList().getSelection();
		if (newJ2EEText!=null && newJ2EEText.length()>0)
			setJ2EESelection(newJ2EEText);
				
		startListening();		
	}
	
	private void handleJ2EESelected()
	{
		stopListening();
		
		String selectedText = getJ2EESelection();
		serverToRuntimeToJ2EE_.getChoice().getChoice().getList().setSelectionValue(selectedText);
		
		startListening();
	}
    /**
	 * Does anything necessary because the default button has been pressed.
	*/
	protected void performDefaults()
	{
	  super.performDefaults();
	  setToDefaults();
	}
	
	private void setToDefaults()
	{
		PersistentServerRuntimeContext context = WebServiceConsumptionUIPlugin.getInstance().getServerRuntimeContext();

		String defaultServerText = context.getDefaultServerFactoryId();
		serverToRuntimeToJ2EE_.getList().setSelectionValue(defaultServerText);
		setServerSelection(defaultServerText);
		
		setRuntimeItems(serverToRuntimeToJ2EE_.getChoice().getList().getList());
		String defaultRuntimeText = context.getDefaultRuntimeId();
		serverToRuntimeToJ2EE_.getChoice().getList().setSelectionValue(defaultRuntimeText);
		setRuntimeSelection(defaultRuntimeText);
		
		setJ2EEItems(serverToRuntimeToJ2EE_.getChoice().getChoice().getList().getList());
		String defaultJ2EEVersion = context.getDefaultJ2EEVersion();
		serverToRuntimeToJ2EE_.getChoice().getChoice().getList().setSelectionValue(defaultJ2EEVersion);
		setJ2EESelection(defaultJ2EEVersion);		
	}

	/**
	 * Do anything necessary because the OK button has been pressed.
	 *  @return whether it is okay to close the preference page
	 */
	public boolean performOk()
	{
	  storeValues();
	  return true;
	}

	private void storeValues()
	{
      PersistentServerRuntimeContext context = WebServiceConsumptionUIPlugin.getInstance().getServerRuntimeContext();
      context.setServerFactoryId(getServerSelection());
      context.setRuntimeId(getRuntimeSelection());
      context.setJ2EEVersion(getJ2EESelection());
	}
	
	protected void performApply()
	{
	  performOk();
	}
	
  	
	private void startListening()
	{
	  server_.addListener(SWT.Selection,this);
	  runtime_.addListener(SWT.Selection,this);
	  j2eeVersion_.addListener(SWT.Selection,this);
	}

	private void stopListening()
	{
	  server_.removeListener(SWT.Selection, this);
	  runtime_.removeListener(SWT.Selection, this);
	  j2eeVersion_.removeListener(SWT.Selection, this);
	}

	private void setServerItems(String[] factoryIds)
	{
	  
	  if (factoryIds != null)
	  {
		//String[] serverLabels = new String[factoryIds.length];
	    ArrayList serverLabelsList = new ArrayList();
		for (int i=0;i<factoryIds.length;i++)
		{
			String thisFactoryId = factoryIds[i];
			String thisServerLabel = WebServiceServerRuntimeTypeRegistry.getInstance().getServerLabel(factoryIds[i]);
			if (thisServerLabel!=null && thisServerLabel.length()>0)
			{
			  serverLabelsList.add(thisServerLabel);
			}
			
		}
		String[] serverLabels = convertToStringArray(serverLabelsList.toArray()); 
		server_.setItems(serverLabels);
	  }
		
	}
	
	private void setRuntimeItems(String[] ids)
	{
	  if (ids != null)
	  {
		String[] runtimeLabels = new String[ids.length];
		for (int i=0;i<ids.length;i++)
		{
		  runtimeLabels[i] = WebServiceServerRuntimeTypeRegistry.getInstance().getRuntimeLabel(ids[i]);
		}
		runtime_.setItems(runtimeLabels);
	  }
		
	}
	
    private void setJ2EEItems(String[] versions)
    {
      if (versions != null)
      {
        String[] j2eeLabels = new String[versions.length];
        for (int i = 0; i < versions.length; i++)
        {
          String label = J2EEUtils.getLabelFromJ2EEVersion(versions[i]);
          if (label != null && label.length()>0)
            j2eeLabels[i] = label;
          else
            j2eeLabels[i] = msgUtils_.getMessage("LABEL_NA");
        }
        j2eeVersion_.setItems(j2eeLabels);
        
        //Disable the combo if it has no items.
        if (j2eeLabels.length > 0)
          j2eeVersion_.setEnabled(true);
        else
          j2eeVersion_.setEnabled(false);
      }
        
    }
	
	private String getServerSelection()
	{
	  String serverLabel = server_.getText();
	  return WebServiceServerRuntimeTypeRegistry.getInstance().getServerFactoryId(serverLabel);		
	}
	
	private String getRuntimeSelection()
	{
      String runtimeLabel = runtime_.getText();
	  return WebServiceServerRuntimeTypeRegistry.getInstance().getRuntimeId(runtimeLabel);				
	}
	
	private String getJ2EESelection()
	{
	  String j2eeLabel = j2eeVersion_.getText();
	  return J2EEUtils.getJ2EEVersionFromLabel(j2eeLabel);		
	}
	
	private void setServerSelection(String factoryId)
	{
		String label = WebServiceServerRuntimeTypeRegistry.getInstance().getServerLabel(factoryId);
		setSelection(server_,label);
	}
	
	private void setRuntimeSelection(String id)
	{
		String label = WebServiceServerRuntimeTypeRegistry.getInstance().getRuntimeLabel(id);
		setSelection(runtime_,label);		
	}
	
	private void setJ2EESelection(String version)
	{
	  String label = J2EEUtils.getLabelFromJ2EEVersion(version);
      setSelection(j2eeVersion_, label);
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
	
	private String[] convertToStringArray(Object[] a)
	{
	  if (a==null) return new String[0];
	  	
	  int length = a.length;
	  String[] sa = new String[length];
	  for (int i=0; i<length; i++)
	  {
	  	Object obj = a[i];
	  	if (obj instanceof String)
	  	{
	      sa[i] = (String)obj;
	  	}
	  }
	  return sa;
	}	
}
