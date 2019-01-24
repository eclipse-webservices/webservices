/*******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
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
package org.eclipse.jst.ws.internal.consumption.ui.helper;

public class WebServiceEvent {

	public static final int REFRESH = 1;
	public static final int REMOVE = 0;
	
	private int eventType = 0;
	
	public WebServiceEvent(int anEventType) {
		super();
		eventType = anEventType;
	}
	
	public int getEventType() {
		return eventType;
	}

}
