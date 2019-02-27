/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.properties.sections;

import org.eclipse.jface.viewers.IFilter;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessageReference;

public class W11MessageReferenceSectionFilter implements IFilter {
	// rmah: we should consider other ways of 'extending' this capability
	// rather than simply checking a variable....
	public static boolean showW11MessageReferenceSection = true;
	
	public boolean select(Object toTest) {
		if (toTest instanceof IMessageReference && showW11MessageReferenceSection) {
			return true;
		}
		
		return false;
	}
}