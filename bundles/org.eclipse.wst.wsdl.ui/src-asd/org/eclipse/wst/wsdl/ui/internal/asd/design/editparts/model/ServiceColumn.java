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
package org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IEndPoint;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IService;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;
import org.eclipse.wst.wsdl.ui.internal.asd.util.EndPointComparator;

public class ServiceColumn extends AbstractModelCollection {
	public ServiceColumn(IDescription description) {
		super(description, "ServiceColumn"); //$NON-NLS-1$
	}
	
	public ITreeElement[] getChildren() {
		List services = ((IDescription)model).getServices();
		Collections.sort(services, new ServiceComparator());
		Object array[] = services.toArray();
		
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
	
	private class ServiceComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			if (o1 instanceof IService && o2 instanceof IService) {
				IEndPoint endPoint1 = getFirstAssociatedEndPoint((IService) o1);
				IEndPoint endPoint2 = getFirstAssociatedEndPoint((IService) o2);
				
				EndPointComparator comparator = new EndPointComparator();
				return comparator.compare(endPoint1, endPoint2);
			}
			
			return 0;
		}
		
		private IEndPoint getFirstAssociatedEndPoint(IService service) {
			Iterator endPoints= service.getEndPoints().iterator();
			
			while (endPoints.hasNext()) {
				IEndPoint tempEndPoint = (IEndPoint) endPoints.next();
				if (tempEndPoint.getBinding() != null) {
					return tempEndPoint;
				}
			}
			
			return null;
		}
  }
}
