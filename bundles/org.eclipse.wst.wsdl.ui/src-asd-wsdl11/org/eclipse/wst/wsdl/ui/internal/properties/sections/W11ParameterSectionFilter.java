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
package org.eclipse.wst.wsdl.ui.internal.properties.sections;

import org.eclipse.jface.viewers.IFilter;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;

public class W11ParameterSectionFilter implements IFilter {
	// rmah: we should consider other ways of 'extending' this capability
	// rather than simply checking a variable....
	public static boolean showW11ParameterSection = true;
	
	public boolean select(Object toTest) {
		if (toTest instanceof IParameter && showW11ParameterSection) {
			return true;
		}
		
		return false;
	}
}