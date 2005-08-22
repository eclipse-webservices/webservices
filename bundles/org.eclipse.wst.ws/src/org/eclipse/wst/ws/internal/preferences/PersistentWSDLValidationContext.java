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



public class PersistentWSDLValidationContext extends PersistentContext
{
	public static final String VALIDATE_NO_WSDL = "0";
	public static final String VALIDATE_REMOTE_WSDL = "1";
	public static final String VALIDATE_ALL_WSDL = "2";

	private String wsdl_validation = "WSDL_VALIDATION";

public PersistentWSDLValidationContext () 
{
	super(  WSPlugin.getInstance());
	
}

public void load() 
{
	setDefault(wsdl_validation, VALIDATE_NO_WSDL);
}

// to be used only by the preference page
public void updateWSDLValidation ( String value)
{
 		setValue( wsdl_validation, value);
}

public String getPersistentWSDLValidation ()
{
	String property = getValueAsString(wsdl_validation);
//	 default to Ignore if no init has been done from ini file
	if (property == null || property.equals("")) {
		setValue( wsdl_validation, VALIDATE_NO_WSDL);
		return VALIDATE_NO_WSDL;
	}
	else
		return property;
}

public String getDefault()
{
	return getDefaultString(wsdl_validation);
}

public boolean ValidateNoWSDL()
{
	 return VALIDATE_NO_WSDL.equals(getPersistentWSDLValidation());
}

public boolean WarnNonWSICompliances()
{
	 return VALIDATE_REMOTE_WSDL.equals(getPersistentWSDLValidation());
}

public boolean IgnoreNonWSICompliances()
{
	 return VALIDATE_ALL_WSDL.equals(getPersistentWSDLValidation());
}


}
