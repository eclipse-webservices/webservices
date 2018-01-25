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

package org.eclipse.wst.ws.internal.wsrt;


public class SimpleContext implements IContext {

	boolean develop;
	boolean assemble;
	boolean deploy;
	boolean install;
	boolean run;
	boolean client;
	boolean test;
	boolean publish;
	
	WebServiceScenario scenario;
	
	boolean overwriteFiles;
	boolean createFolders;
	boolean checkOutFiles;
	
	
	
	
	public SimpleContext()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public SimpleContext(boolean develop, boolean assemble, boolean deploy, boolean install, boolean run, boolean client, boolean test, boolean publish, WebServiceScenario scenario, boolean overwriteFiles, boolean folders, boolean checkOutFiles) {
		super();		
		this.develop = develop;
		this.assemble = assemble;
		this.deploy = deploy;
		this.install = install;
		this.run = run;
		this.client = client;
		this.test = test;
		this.publish = publish;
		this.scenario = scenario;
		this.overwriteFiles = overwriteFiles;
		this.createFolders = folders;
		this.checkOutFiles = checkOutFiles;
	}
	
	public boolean getAssemble() {
		return assemble;
	}
	public void setAssemble(boolean assemble) {
		this.assemble = assemble;
	}
	public boolean getCheckOutFiles() {
		return checkOutFiles;
	}
	public void setCheckOutFiles(boolean checkOutFiles) {
		this.checkOutFiles = checkOutFiles;
	}
	public boolean getClient() {
		return client;
	}
	public void setClient(boolean client) {
		this.client = client;
	}
	public boolean getCreateFolders() {
		return createFolders;
	}
	public void setCreateFolders(boolean createFolders) {
		this.createFolders = createFolders;
	}
	public boolean getDeploy() {
		return deploy;
	}
	public void setDeploy(boolean deploy) {
		this.deploy = deploy;
	}
	public boolean getDevelop() {
		return develop;
	}
	public void setDevelop(boolean develop) {
		this.develop = develop;
	}
	public boolean getInstall() {
		return install;
	}
	public void setInstall(boolean install) {
		this.install = install;
	}
	public boolean getOverWriteFiles() {
		return overwriteFiles;
	}
	public void setOverWriteFiles(boolean overwriteFiles) {
		this.overwriteFiles = overwriteFiles;
	}
	public boolean getPublish() {
		return publish;
	}
	public void setPublish(boolean publish) {
		this.publish = publish;
	}
	public boolean getRun() {
		return run;
	}
	public void setRun(boolean run) {
		this.run = run;
	}
	public WebServiceScenario getScenario() {
		return scenario;
	}
	public void setScenario(WebServiceScenario scenario) {
		this.scenario = scenario;
	}
	public boolean getTest() {
		return test;
	}
	public void setTest(boolean test) {
		this.test = test;
	}
}
