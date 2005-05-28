/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.wsrt;

import org.eclipse.wst.ws.internal.provisional.wsrt.IContext;
import org.eclipse.wst.ws.internal.provisional.wsrt.WebServiceScenario;

/**
 * @author rsinha
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Context implements IContext {	
	private WebServiceScenario scenario;
	private boolean develop;
	private boolean assemble;
	private boolean deploy;
	private boolean install;
	private boolean run;
	private boolean client;
	private boolean test;
	private boolean publish;
	private boolean overWriteFiles;
	private boolean createFolders;
	private boolean checkOutFiles;
	
	public boolean getAssemble() {
		return assemble;
	}
	
	public boolean getCheckOutFiles() {
		return checkOutFiles;
	}
	
	public boolean getClient() {
		return client;
	}
	
	public boolean getCreateFolders() {
		return createFolders;
	}
	
	public boolean getDeploy() {
		return deploy;
	}
	
	public boolean getDevelop() {
		return develop;
	}
	
	public boolean getInstall() {
		return install;
	}
	
	public boolean getOverWriteFiles() {
		return overWriteFiles;
	}
	
	public boolean getPublish() {
		return publish;
	}
	
	public boolean getRun() {
		return run;
	}
	
	public WebServiceScenario getScenario() {
		return scenario;
	}
	
	public boolean getTest() {
		return test;
	}

	public void setAssemble(boolean assemble) {
		this.assemble = assemble;
	}
	

	public void setCheckOutFiles(boolean checkOutFiles) {
		this.checkOutFiles = checkOutFiles;
	}
	

	public void setClient(boolean client) {
		this.client = client;
	}
	

	public void setCreateFolders(boolean createFolders) {
		this.createFolders = createFolders;
	}
	

	public void setDeploy(boolean deploy) {
		this.deploy = deploy;
	}
	

	public void setDevelop(boolean develop) {
		this.develop = develop;
	}
	

	public void setInstall(boolean install) {
		this.install = install;
	}
	

	public void setOverWriteFiles(boolean overWriteFiles) {
		this.overWriteFiles = overWriteFiles;
	}
	

	public void setPublish(boolean publish) {
		this.publish = publish;
	}
	

	public void setRun(boolean run) {
		this.run = run;
	}
	

	public void setScenario(WebServiceScenario scenario) {
		this.scenario = scenario;
	}
	

	public void setTest(boolean test) {
		this.test = test;
	}		
	
}
