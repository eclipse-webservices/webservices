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
package org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.INamedObject;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;

public class InterfaceColumn extends AbstractModelCollection {
	public InterfaceColumn(IDescription description) {
		super(description, "InterfaceColumn"); //$NON-NLS-1$
	}
	
	public ITreeElement[] getChildren() {
		List interfaces = ((IDescription)model).getInterfaces();		
		Comparator compare = new NamedObjectComparator();
		Collections.sort(interfaces, compare);
		Object array[] = interfaces.toArray();
		
		ITreeElement treeElement[] = new ITreeElement[array.length];
		for (int index = 0; index < array.length; index++) {
			treeElement[index] = (ITreeElement) array[index];
		}
		
		return treeElement;
	}

	public boolean hasChildren() {
		ITreeElement treeElement[] = getChildren();
		if (treeElement.length > 0) {
			return true;
		}
		
		return false;
	}
	
	public String getText() {
		return "definition"; //$NON-NLS-1$
	}
	
	private class NamedObjectComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			if (o1 instanceof INamedObject && o2 instanceof INamedObject) {
				String name1 = ((INamedObject) o1).getName();
				String name2 = ((INamedObject) o2).getName();
				return name1.compareTo(name2);
			}
			
			return -1;
		}
	}
}
