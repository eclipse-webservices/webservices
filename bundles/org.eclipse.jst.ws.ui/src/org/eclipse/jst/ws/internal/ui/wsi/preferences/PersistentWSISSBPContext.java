/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.ui.wsi.preferences;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jst.ws.internal.ui.plugin.WebServiceUIPlugin;



public class PersistentWSISSBPContext extends PersistentWSIContext
{
	private static final String NON_WSI_SSBP_COMPLIANCE = "nonWSISSBPCompliance";

public PersistentWSISSBPContext () 
{
	super();
	non_wsi_compliance = NON_WSI_SSBP_COMPLIANCE;
	name = new QualifiedName(WebServiceUIPlugin.ID , non_wsi_compliance);
	wsiWarning_ = "WSI_SSBP_WARNING";
	wsiError_ = "WSI_SSBP_ERROR";
}

}
