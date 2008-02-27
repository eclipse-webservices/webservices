/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20080221 146023 gilberta@ca.ibm.com - Gilbert Andrews 
 *******************************************************************************/
/**
 */
package org.eclipse.wst.command.internal.env.ui.dialog;

import org.eclipse.core.runtime.IConfigurationElement;

public class AntExtension {
	
	private String label_;
	private String runtime_;
	private String scenario_;
	private String id_;
	private String runtimeID_;
	private String plugin_;
	private String path_;
	private String wsgenpath_;
	private IConfigurationElement element_;
	
	public AntExtension(IConfigurationElement element)
	{
		element_ = element;
	}
	
	public String getLabel()
	{
	    if (label_==null)
	    {
	    	label_ = element_.getAttribute("label");
	    }
	    return label_;
	}   

	public String getRuntimeLabel()
	{
	    if (runtime_==null)
	    {
	    	runtime_ = element_.getAttribute("runtimelabel");
	    }
	    return runtime_;
	}
	
	public String getScenarioLabel()
	{
	    if (scenario_==null)
	    {
	    	scenario_ = element_.getAttribute("scenariolabel");
	    }
	    return scenario_;
	}
	
	public String getID()
	{
	    if (id_==null)
	    {
	    	id_ = element_.getAttribute("id");
	    }
	    return id_;
	}
	
	public String getRuntimeID()
	{
	    if (runtimeID_==null)
	    {
	    	runtimeID_ = element_.getAttribute("runtimeid");
	    }
	    return runtimeID_;
	}
	
	public String getPlugin()
	{
	    if (plugin_==null)
	    {
	    	plugin_ = element_.getAttribute("pluginlocation");
	    }
	    return plugin_;
	}
	
	public String getPath()
	{
	    if (path_==null)
	    {
	    	path_ = element_.getAttribute("path");
	    }
	    return path_;
	}

	public String getWSGenPath()
	{
	    if (wsgenpath_==null)
	    {
	    	wsgenpath_ = element_.getAttribute("wsgenpath");
	    }
	    return wsgenpath_;
	}

	
}
