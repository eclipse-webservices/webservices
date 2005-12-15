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
import org.eclipse.wst.ws.internal.WstWSPluginMessages;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;



public class PersistentWSISSBPContext extends PersistentWSIContext
{
	private static final String NON_WSI_SSBP_COMPLIANCE = "nonWSISSBPCompliance";

public PersistentWSISSBPContext () 
{
	super();
	non_wsi_compliance = NON_WSI_SSBP_COMPLIANCE;
	name = new QualifiedName(WSPlugin.ID , non_wsi_compliance);
	wsiWarning_ = WstWSPluginMessages.WSI_SSBP_WARNING;
	wsiError_ = WstWSPluginMessages.WSI_SSBP_ERROR;
}

}
