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

import org.eclipse.wst.command.internal.env.ui.common.LabelsAndIds;

public class RuntimeStruct {

	private String runtimeID_;
	private String runtimeLabel_;
	private LabelsAndIds scenarios_;
	
	public RuntimeStruct(String runtimeID){
		runtimeID_ = runtimeID;
	}

	public String getRuntimeID(){
		return runtimeID_;
	}

	public void setRuntimeLabel(String label){
		runtimeLabel_ = label;
	}
	
	public String getRuntimeLabel(){
		return runtimeLabel_;
	}
	
	public void setScenario(String id, String label){
		if(scenarios_ == null) scenarios_ = new LabelsAndIds(); 
		if(id == null || label == null) return;
		scenarios_.add(id,label);
	}
	
	public LabelsAndIds getScenarioLabels(){
		return scenarios_;
	}
	
}
