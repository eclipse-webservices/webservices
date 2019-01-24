/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
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

package org.eclipse.jst.ws.internal.axis.creation.ui.wsrt;

import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;

public class AxisWebServiceInfo {
	
	private JavaWSDLParameter javaWSDLParameter;

	public JavaWSDLParameter getJavaWSDLParameter() {
		return javaWSDLParameter;
	}

	public void setJavaWSDLParameter(JavaWSDLParameter javaWSDLParameter) {
		this.javaWSDLParameter = javaWSDLParameter;
	}

}
