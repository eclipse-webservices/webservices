/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20090302   242462 ericdp@ca.ibm.com - Eric D. Peters, Save Web services wizard settings
 *******************************************************************************/
package org.eclipse.jst.ws.internal.ui.common;

public abstract interface WidgetWithHistory {
	public abstract void storeWidgetHistory(String storeKey);

	public abstract void restoreWidgetHistory(String restoreKey);

}
