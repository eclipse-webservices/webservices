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

import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.command.internal.env.ui.common.LabelsAndIds;

public class AntExtensionCreation {

	private Hashtable antExtentions_;
	private static AntExtensionCreation antExtensionC_;
	private Hashtable runtimeStructs_ = null;
	
	
	public static AntExtensionCreation getInstance(){
		if(antExtensionC_ == null){
			antExtensionC_ = new AntExtensionCreation();
		}
		return antExtensionC_;
	}
	
	public void createExtensions()
	{
		antExtentions_ = new Hashtable();
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		//Load  by reading all extensions to org.eclipse.wst.command.env.ui.antfiles
		IConfigurationElement[] antExts = reg.getConfigurationElementsFor("org.eclipse.wst.command.env.ui", "antfiles");
    
		for(int idx=0; idx<antExts.length; idx++) 
		{
			IConfigurationElement elem = antExts[idx];
			if (elem.getName().equals("antfiles"))
			{
				AntExtension ae = new AntExtension(elem);
				antExtentions_.put(elem.getAttribute("id"), ae);
			}        
		}
	}
	
	public void createRuntimes(){
		Enumeration antExtsEnum = antExtentions_.elements();
		runtimeStructs_ = new Hashtable();
		
		while(antExtsEnum.hasMoreElements()){
			AntExtension antExt = (AntExtension)antExtsEnum.nextElement();
			RuntimeStruct rs = (RuntimeStruct)runtimeStructs_.get(antExt.getRuntimeID());
			if(rs == null){
				rs = new RuntimeStruct(antExt.getRuntimeID()); 
				rs.setRuntimeLabel(antExt.getRuntimeLabel());
				rs.setScenario(antExt.getID(), antExt.getScenarioLabel());
				runtimeStructs_.put(rs.getRuntimeID(), rs);
			}
			else{
				rs.setScenario(antExt.getID(), antExt.getScenarioLabel());
			}
		}
	}
		
	public LabelsAndIds getRuntimeLabelsAndIds(){
		if(antExtentions_ == null) createExtensions();
		if(runtimeStructs_ == null)createRuntimes();
		LabelsAndIds runtime = new LabelsAndIds(); 
		Enumeration runtimeStructs = runtimeStructs_.elements();
		while(runtimeStructs.hasMoreElements()){
			RuntimeStruct rs = (RuntimeStruct)runtimeStructs.nextElement();	
			runtime.add(rs.getRuntimeID(),rs.getRuntimeLabel());
		}
		return runtime;
	}
	
	public LabelsAndIds getScenarioLabelsByRuntime(String runtimeId){
		if(antExtentions_ == null) createExtensions();
		if(runtimeStructs_ == null) createRuntimes();
		RuntimeStruct rs = (RuntimeStruct)runtimeStructs_.get(runtimeId);	
		return rs.getScenarioLabels();
	}
	
		
	public AntExtension getAnrExtByScenario(String selection){
		if(antExtentions_ == null) return null;
		return (AntExtension)antExtentions_.get(selection);	
	}

	
}
