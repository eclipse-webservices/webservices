/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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

package org.eclipse.wst.ws.internal.explorer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Properties;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

/**
 * @author cbrealey@ca.ibm.com
 * 
 * This <code>Command</code>, when executed, launches the
 * Web Services Explorer.
 */
public class WSExplorerLauncherCommand extends AbstractDataModelOperation {
	private boolean forceLaunchOutsideIDE;

	private LaunchOption[] launchOptions;

	public WSExplorerLauncherCommand() {
	}

	public void writeCategoryInfo(String inquiryURL, String categoriesDirectory) {
		try {
			Properties p = new Properties();
			p.setProperty(LaunchOptions.CATEGORIES_DIRECTORY,
					categoriesDirectory);
			StringBuffer propertiesFileName = new StringBuffer();
			propertiesFileName.append(WSExplorer.getInstance()
					.getMetadataDirectory());
			File metadataDirectoryFile = new File(propertiesFileName.toString());
			if (!metadataDirectoryFile.exists()) {
				metadataDirectoryFile.mkdirs();
			}
			propertiesFileName.append(URLEncoder.encode(inquiryURL,"UTF-8")).append(
					".properties");
			FileOutputStream fout = new FileOutputStream(propertiesFileName
					.toString());
			p.store(fout, null);
			fout.close();
		} catch (IOException e) {
		}
	}

	public IStatus execute() {
		return WSExplorer.getInstance().launch(null, null, launchOptions,
				forceLaunchOutsideIDE);
	}

	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
  {
    return execute();
	}

	/**
	 * @param forceLaunchOutsideIDE
	 *            The forceLaunchOutsideIDE to set.
	 */
	public void setForceLaunchOutsideIDE(boolean forceLaunchOutsideIDE) {
		this.forceLaunchOutsideIDE = forceLaunchOutsideIDE;
	}

	/**
	 * @param launchOptions
	 *            The launchOptions to set.
	 */
	public void setLaunchOptions(LaunchOption[] launchOptions) {
		this.launchOptions = launchOptions;
	}
}