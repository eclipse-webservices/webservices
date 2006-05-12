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
package org.eclipse.wst.wsdl.ui.internal.adapters.actions;

import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Import;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.BaseSelectionAction;

public class W11OpenImportAction extends BaseSelectionAction {
	public static String ID = "ASDOpenImportAction";  //$NON-NLS-1$
	
	public W11OpenImportAction(IWorkbenchPart part)	{
		super(part);
		setId(ID);
		setText(Messages.getString("_UI_ACTION_OPEN_IMPORT")); //$NON-NLS-1$
	}
	
	public void run() {
		if (getSelectedObjects().size() > 0 && getSelectedObjects().get(0) instanceof W11Import) {
			Import theImport = getWSDLImport((W11Import) getSelectedObjects().get(0));
			
			Definition definition = theImport.getEnclosingDefinition();
			org.eclipse.wst.wsdl.ui.internal.util.OpenOnSelectionHelper helper = new org.eclipse.wst.wsdl.ui.internal.util.OpenOnSelectionHelper(definition);
		    helper.openEditor((org.eclipse.emf.ecore.EObject) theImport);
		}
	}
	
	  protected boolean calculateEnabled() {
		  boolean enabled = super.calculateEnabled();
		  if (enabled) {
			  if (getSelectedObjects().size() > 0 && getSelectedObjects().get(0) instanceof W11Import) {
					Import theImport = getWSDLImport((W11Import) getSelectedObjects().get(0));
					
					if (theImport != null) {
						String location = theImport.getLocationURI();
					
						if (location == null || location.trim().equals(""))
							enabled = false;
					}
			  }
		  }
		  
		  return enabled;
	  }
	  
	  private Import getWSDLImport(W11Import w11Import) {
		  return (Import) ((W11Import) w11Import).getTarget();
	  }
}
