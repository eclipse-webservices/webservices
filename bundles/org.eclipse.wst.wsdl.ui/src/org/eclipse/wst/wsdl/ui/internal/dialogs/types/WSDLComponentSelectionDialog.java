/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.dialogs.types;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.xsd.ui.internal.dialogs.types.common.IComponentSelectionProvider;
import org.eclipse.wst.xsd.ui.internal.dialogs.types.xml.XMLComponentSelectionDialog;

public class WSDLComponentSelectionDialog extends XMLComponentSelectionDialog {
	
    public WSDLComponentSelectionDialog(Shell shell, String dialogTitle, IComponentSelectionProvider provider) {
        super(shell, dialogTitle, provider);
    }  
}
