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
package org.eclipse.wst.wsdl.ui.internal.adapters.basic;

import java.util.Collections;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;

public class W11TypesCategoryAdapter extends W11CategoryAdapter {
	public W11TypesCategoryAdapter(IDescription description, String label, Image image, int groupType) {
		super(description, label,image, Collections.EMPTY_LIST, groupType);
	}

	public ITreeElement[] getChildren() {
		List types = ((W11Description) getOwnerDescription()).getTypes();
		return (ITreeElement[]) types.toArray(new ITreeElement[0]);
	}
}
