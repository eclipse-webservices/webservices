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

import org.eclipse.wst.wsdl.ui.internal.asd.facade.IBinding;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IInterface;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.INamedObject;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;

public class BindingColumn extends AbstractModelCollection {
	  public BindingColumn(IDescription description) {
	    super(description, "BindingColumn"); //$NON-NLS-1$
	  }
	  
	  public ITreeElement[] getChildren() {
			List bindings = ((IDescription)model).getBindings();
			Collections.sort(bindings, new BindingComparator());
			Object array[] = bindings.toArray();
			
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
	  
	  private class BindingComparator implements Comparator {
			public int compare(Object o1, Object o2) {
				if (o1 instanceof IBinding && o2 instanceof IBinding) {
					IInterface interface1 = ((IBinding) o1).getInterface();
					IInterface interface2 = ((IBinding) o2).getInterface();
					
					if (interface1 != null && interface2 != null) {
						String name1 = ((INamedObject) interface1).getName();
						String name2 = ((INamedObject) interface2).getName();
						return name1.compareTo(name2);
					}
					else if (interface1 != null && interface2 == null) {
						return -1;
					}
					else if (interface1 == null && interface2 != null) {
						return 1;
					}
				}
				
				return 0;
			}
	  }
}
