/*******************************************************************************
 * Copyright (c) 2007 WSO2 Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070516   183147 sandakith@wso2.com - Lahiru Sandakith Fix for the persisting DBCS paths
 *******************************************************************************/ 
package org.eclipse.jst.ws.axis2.core.context;

import org.eclipse.jst.ws.axis2.core.plugin.WebServiceAxis2CorePlugin;
import org.eclipse.wst.command.internal.env.context.PersistentContext;

public class PersistentAxis2EmitterContext extends PersistentContext implements
		Axis2EmitterContext {

	private static PersistentAxis2EmitterContext context_ = null;

	public static PersistentAxis2EmitterContext getInstance() 
	{
		if (context_ == null) {
			context_ = new PersistentAxis2EmitterContext();
			context_.load();
		}
		return context_;
	}
	
	public void load(){
		setDefault(PREFERENCE_AXIS2_RUNTIME_LOCATION, Axis2EmitterDefaults.getAxis2RuntimeLocation());
	}
	
	private PersistentAxis2EmitterContext() 	{
		super(WebServiceAxis2CorePlugin.getInstance());
	}

	public String getAxis2RuntimeLocation() {
		return getValueAsString(PREFERENCE_AXIS2_RUNTIME_LOCATION);
	}

	public void setAxis2RuntimeLocation(String runtimeLocation) {
		setValue(PREFERENCE_AXIS2_RUNTIME_LOCATION, runtimeLocation);
	}

}
