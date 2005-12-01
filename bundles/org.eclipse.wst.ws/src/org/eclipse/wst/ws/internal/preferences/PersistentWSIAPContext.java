/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.preferences;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;



public class PersistentWSIAPContext extends PersistentWSIContext
{
	private static final String NON_WSI_AP_COMPLIANCE = "nonWSIAPCompliance";

public PersistentWSIAPContext () 
{
	super();
	non_wsi_compliance = NON_WSI_AP_COMPLIANCE;
	name = new QualifiedName(WSPlugin.ID , non_wsi_compliance);
	wsiWarning_ = "WSI_AP_WARNING";
	wsiError_ = "WSI_AP_ERROR";
}

public String getPersistentWSICompliance ()
{
	String property = getValueAsString(non_wsi_compliance);
	// default to Warning if no init has been done from ini file
	if (property == null || property.equals("")) {
		setValue( non_wsi_compliance, WARN_NON_WSI);
		return WARN_NON_WSI;
	}
	else
		return property;
}
}
