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

import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.wst.command.env.context.PersistentContext;


/**
 * 
 */
public class PersistentServerRuntimeContext extends PersistentContext {
	
	private String PREFERENCE_SERVER = "PREFERENCE_SERVER";
	private String PREFERENCE_RUNTIME = "PREFERENCE_RUNTIME";
	private String PREFERENCE_J2EE_VERSION = "PREFERENCE_J2EE_VERSION";
	
	private String SERVER_FACTORY_ID_DEFAULT = "org.eclipse.jst.server.tomcat.40";
	private String RUNTIME_ID_DEFAULT = "org.eclipse.jst.ws.runtime.axis11";
	private String J2EE_VERSION_DEFAULT = "13";
	
	public PersistentServerRuntimeContext()
	{
		super(WebServiceConsumptionUIPlugin.getInstance());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.context.Context#load()
	 */
	public void load() {
		//Defaults will be set via the .ini customization. They are hard coded to default values in the
		//absence of a .ini file.
		String serverDefault = getDefaultString(PREFERENCE_SERVER);
		if (serverDefault==null || serverDefault.length()==0)
		{
		  setDefault(PREFERENCE_SERVER, SERVER_FACTORY_ID_DEFAULT);
		}

		String runtimeDefault = getDefaultString(PREFERENCE_RUNTIME);
		if (runtimeDefault==null || runtimeDefault.length()==0)
		{
		  setDefault(PREFERENCE_RUNTIME, RUNTIME_ID_DEFAULT);
		}

		String j2eeDefault = getDefaultString(PREFERENCE_J2EE_VERSION);
		if (j2eeDefault==null || j2eeDefault.length()==0)
		{
		  setDefault(PREFERENCE_J2EE_VERSION, J2EE_VERSION_DEFAULT);
		}

				
	}
	
	public String getDefaultServerFactoryId()
	{
		return getDefaultString(PREFERENCE_SERVER);
	}

	public String getDefaultRuntimeId()
	{
		return getDefaultString(PREFERENCE_RUNTIME);
	}

	public String getDefaultJ2EEVersion()
	{
		return getDefaultString(PREFERENCE_J2EE_VERSION);
	}

	public String getServerFactoryId()
	{
		String value = getValueAsString(PREFERENCE_SERVER); 
		/*
		if (value==null || value.length()==0)
		{
			value = SERVER_FACTORY_ID_DEFAULT; 
			setServerFactoryId(value);
		}
		*/
		return value;
	}
	
	public String getRuntimeId()
	{
		String value = getValueAsString(PREFERENCE_RUNTIME);
		/*
		if (value==null || value.length()==0)
		{
			value = RUNTIME_ID_DEFAULT; 
			setRuntimeId(value);
		}
		*/
		return value;		
	}
	
	public String getJ2EEVersion()
	{
		String value = getValueAsString(PREFERENCE_J2EE_VERSION);
		/*
		if (value==null || value.length()==0)
		{
			value = J2EE_VERSION_DEFAULT; 
			setJ2EEVersion(value);
		}
		*/
		return value;		
	}
	
	public void setServerFactoryId(String id)
	{
		setValue(PREFERENCE_SERVER,id);
	}
	
	public void setRuntimeId(String id)
	{
		setValue(PREFERENCE_RUNTIME, id);
	}
	
	public void setJ2EEVersion(String version)
	{
		setValue(PREFERENCE_J2EE_VERSION, version);
	}
}
