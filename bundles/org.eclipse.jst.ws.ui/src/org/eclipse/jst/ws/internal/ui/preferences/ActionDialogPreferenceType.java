/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.ui.preferences;

public class ActionDialogPreferenceType {

    private String id_;
	public void setId (String id) {
		id_ = id;
	}
	public String getId() {
    	return id_;
	}

	private String name_;
	public void setName(String name) {
		name_ = name;
	}
	public String getName() {
    	return name_;
	}

	private String infopop_;
    public void setInfopop (String infopop) {
    	infopop_ = infopop;
    	}
    public String getInfopop () {
    	return infopop_;
    	}

	private String tooltip_;
    public void setTooltip (String tooltip) {
    	tooltip_= tooltip;
    	}
    public String getTooltip () {
    	return tooltip_;
    	}

 }
