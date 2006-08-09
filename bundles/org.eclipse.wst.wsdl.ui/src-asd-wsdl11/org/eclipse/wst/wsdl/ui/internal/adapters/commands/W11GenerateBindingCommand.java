/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.adapters.commands;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.wizards.BindingWizard;

public class W11GenerateBindingCommand extends W11TopLevelElementCommand {
	protected Binding binding;
	
	public W11GenerateBindingCommand(Binding binding) {
        super(Messages.getString("_UI_GENERATE_BINDING_CONTENT"), binding.getEnclosingDefinition());
		this.binding = binding;
	}
	
	public void execute() {
		try {
			beginRecording(definition.getElement());

			BindingWizard wizard = new BindingWizard(binding.getEnclosingDefinition(), binding, BindingWizard.KIND_REGENERATE_BINDING);
			wizard.setBindingName(ComponentReferenceUtil.getName(binding));
			wizard.setPortTypeName(ComponentReferenceUtil.getPortTypeReference(binding));
			WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
			wizardDialog.create();
			if (wizardDialog.open() == Window.OK) {
				formatChild(binding.getElement());
			}
		}
		finally {
			endRecording(definition.getElement());
		}
	}
}