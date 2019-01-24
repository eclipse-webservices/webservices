/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.wsrt;

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
