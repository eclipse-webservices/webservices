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
package org.eclipse.wst.wsdl.ui.internal.asd.util;

import java.util.Comparator;

import org.eclipse.wst.wsdl.ui.internal.asd.facade.IBinding;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IEndPoint;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IInterface;

public class EndPointComparator implements Comparator {
	public int compare(Object o1, Object o2) {
		if (o1 instanceof IEndPoint && o2 instanceof IEndPoint) {
			IBinding binding1 = ((IEndPoint) o1).getBinding();
			IBinding binding2 = ((IEndPoint) o2).getBinding();

			if (binding1 != null && binding2 != null) {
				IInterface interface1 = binding1.getInterface();
				IInterface interface2 = binding2.getInterface();

				if (interface1 != null && interface2 != null) {
					String name1 = interface1.getName();
					String name2 = interface2.getName();
					return name1.compareTo(name2);
				}

				return doNullComparison(interface1, interface2);
			}
			
			return doNullComparison(binding1, binding2);
		}

		return doNullComparison(o1, o2);
	}
	
	private int doNullComparison(Object o1, Object o2) {
		if (o1 != null && o2 == null) {
			return -1;
		}
		else if (o1 == null && o2 != null) {
			return 1;
		}
		
		return 0;
	}
}
