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
package org.eclipse.wst.wsdl.ui.internal.asd;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObject;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;

public class ASDLabelProvider extends LabelProvider {
	/**
	 * 
	 */
	public ASDLabelProvider() {
		super();
	}
	
	/**
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object object) {
		if (object == null || object.equals(StructuredSelection.EMPTY)) {
			return null;
		}
		Image result = null;           
		if (object instanceof StructuredSelection) {
			Object selected = ((StructuredSelection)object).getFirstElement();
			
			if (selected instanceof ITreeElement) {
				result = ((ITreeElement) selected).getImage();
			}
		}
		
		return result;
	}
	
	/**
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object object) {
		if (object == null || object.equals(StructuredSelection.EMPTY)) {
			return "No items selected";//$NON-NLS-1$
		}
		String result = null;
		Object selected = null;
		if (object instanceof StructuredSelection) {
			selected = ((StructuredSelection) object).getFirstElement();
			
			if (selected instanceof ITreeElement) {
				result = ((ITreeElement) selected).getText();
			}
			
			if (selected instanceof IASDObject && ((IASDObject) selected).isReadOnly()) {
				result  = result + " (" + Messages.getString("_UI_LABEL_READ_ONLY") + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}
		
		return result;
	}
}
