/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.preferences;

import org.eclipse.wst.command.internal.env.context.PersistentContext;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;



public class PersistentWaitForWSDLValidationContext extends PersistentContext
{

	private String wait_for_wsdl_validation = "WAIT_FOR_WSDL_VALIDATION";

public PersistentWaitForWSDLValidationContext () 
{
	super(  WSPlugin.getInstance());
	
}

public void load() 
{
	setDefault(wait_for_wsdl_validation, true);
}

// to be used only by the preference page
public void setWaitForWSDLValidation ( boolean value)
{
 		setValue( wait_for_wsdl_validation, value);
}

public boolean getPersistentWaitForWSDLValidation ()
{
	return getValueAsBoolean(wait_for_wsdl_validation);

}

public boolean getDefault()
{
	return getDefaultBoolean(wait_for_wsdl_validation);
}




}
