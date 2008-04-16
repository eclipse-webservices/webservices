/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20080415   227152 makandre@ca.ibm.com - Andrew Mak, Need a way to specify a backup Web service runtime
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.preferences;

import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.wst.command.internal.env.context.PersistentContext;


/**
 * 
 */
public class PersistentServerRuntimeContext extends PersistentContext {
	
	private String PREFERENCE_SERVER = "PREFERENCE_SERVER";
	private String PREFERENCE_RUNTIME = "PREFERENCE_RUNTIME";
	private String FALLBACK_RUNTIME = "FALLBACK_RUNTIME";  // Web service runtime to fallback to if preferred runtime is not suitable
	//private String PREFERENCE_J2EE_VERSION = "PREFERENCE_J2EE_VERSION";
	
	private String SERVER_FACTORY_ID_DEFAULT = "org.eclipse.jst.server.tomcat.50";
	private String RUNTIME_ID_DEFAULT = "org.eclipse.jst.ws.axis.creation.axisWebServiceRT";
	
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
				
	}
	
	public String getDefaultServerFactoryId()
	{
		return getDefaultString(PREFERENCE_SERVER);
	}

	public String getDefaultRuntimeId()
	{
		return getDefaultString(PREFERENCE_RUNTIME);
	}

	public String getFallbackRuntimeId()
	{
		return getDefaultString(FALLBACK_RUNTIME);
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
	
	
	public void setServerFactoryId(String id)
	{
		setValue(PREFERENCE_SERVER,id);
	}
	
	public void setRuntimeId(String id)
	{
		setValue(PREFERENCE_RUNTIME, id);
	}
}
