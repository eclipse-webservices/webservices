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

package org.eclipse.wst.ws.internal.provisional.wsrt;

public interface IContext {
	
	public boolean getAssemble();
	
	public boolean getCheckOutFiles();
	
	public boolean getClient();
	
	public boolean getCreateFolders();
	
	public boolean getDeploy();
	
	public boolean getDevelop();
	
	public boolean getInstall();
	
	public boolean getOverWriteFiles();
	
	public boolean getPublish();
	
	public boolean getRun();
	
	public WebServiceScenario getScenario();
	
	public boolean getTest();

}
