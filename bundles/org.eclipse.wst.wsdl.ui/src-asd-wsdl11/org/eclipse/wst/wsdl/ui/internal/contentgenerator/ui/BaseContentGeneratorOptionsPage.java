/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.contentgenerator.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;
import org.eclipse.wst.ws.internal.preferences.PersistentWSIContext;
import org.eclipse.wst.wsdl.internal.generator.BaseGenerator;
import org.eclipse.wst.wsdl.ui.internal.wizards.ContentGeneratorOptionsPage;

public abstract class BaseContentGeneratorOptionsPage implements ContentGeneratorOptionsPage, SelectionListener, IMessageProvider {
	protected Composite control;
	protected BaseGenerator generator;
	protected WizardPage wizardPage;
	protected IFile targetFile;
	
	public abstract void setOptionsOnGenerator();
	
	public Composite getControl() {
		return control;
	}

	public void init(BaseGenerator baseGenerator) {
		generator = baseGenerator;
	}

	public boolean isOverwriteApplicable() {
		return true;
	}

	public void setWizardPage(WizardPage wizardPage) {
		this.wizardPage = wizardPage;
	}

	public void widgetSelected(SelectionEvent event) {
		setOptionsOnGenerator();
	}

	public void widgetDefaultSelected(SelectionEvent event) {
	}
	
	public void setTargetIFile(IFile targetFile) {
		this.targetFile = targetFile;
	}
	
    public String getMessage() {
    	return "";
    }

    public int getMessageType() {
    	return IMessageProvider.NONE;
    }
    
	public String getWSIPreferences() {
		IProject targetProject = targetFile.getProject();
		PersistentWSIContext WSISSBcontext = WSPlugin.getInstance().getWSISSBPContext();

		if (WSISSBcontext.projectStopNonWSICompliances(targetProject)) {
			return (PersistentWSIContext.STOP_NON_WSI);
		} 
		else if (WSISSBcontext.projectWarnNonWSICompliances(targetProject)) {
			return (PersistentWSIContext.WARN_NON_WSI);
		}
		else {
			return (PersistentWSIContext.IGNORE_NON_WSI);
		}
	}
}