/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20100413   307552 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS and Java EE 6 setup is incorrect
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryproviderconfig;


public interface JAXRSLibraryProvider {
	String getLibraryProviderID();
	void setLibraryProviderID(String value);
	boolean getShowUpdateDDCheckBox();
	void setShowUpdateDDCheckBox(boolean show);
	boolean getUpdateDDCheckBoxSelected();
	void setUpdateDDCheckBoxSelected(boolean show);
	String getServletClassName();
	void setServletClassName(String sevletClassName);
} 