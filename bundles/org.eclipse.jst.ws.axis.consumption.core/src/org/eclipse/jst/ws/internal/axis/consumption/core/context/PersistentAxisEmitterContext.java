/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 * yyyymmdd   bug     Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060329   127016 andyzhai@ca.ibm.com - Andy Zhai
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.consumption.core.context;

import org.eclipse.jst.ws.internal.axis.consumption.core.plugin.WebServiceAxisConsumptionCorePlugin;
import org.eclipse.wst.command.internal.env.context.PersistentContext;

public class PersistentAxisEmitterContext extends PersistentContext implements	AxisEmitterContext
{
	private static PersistentAxisEmitterContext context_ = null;

	public static PersistentAxisEmitterContext getInstance() 
	{
		if (context_ == null) {
			context_ = new PersistentAxisEmitterContext();
			context_.load();
		}

		return context_;
	}

	private PersistentAxisEmitterContext() 
	{
		super(WebServiceAxisConsumptionCorePlugin.getInstance());
	}

	public void load() 
	{
		setDefault(PREFERENCE_ALL_WANTED, AxisEmitterDefaults.getAllWantedDefault());
		setDefault(PREFERENCE_HELPER_WANTED, AxisEmitterDefaults.getHelperWantedDefault());
		setDefault(PREFERENCE_WRAP_ARRAYS, AxisEmitterDefaults.getWrapArraysDefault());
		setDefault(PREFERENCE_USE_INHERITED_METHODS, AxisEmitterDefaults.getUseInheritedMethodsDefault());
		setDefault(PREFERENCE_DEPLOY_SCOPE, AxisEmitterDefaults.getDeployScopeDefault());
		setDefault(PREFERENCE_TIME_OUT, AxisEmitterDefaults.getTimeOutDefault());
	}

	public void setAllWantedEnabled(boolean enable) 
	{
		setValue(PREFERENCE_ALL_WANTED, enable);
	}

	public boolean isAllWantedEnabled() 
	{
		return getValueAsBoolean(PREFERENCE_ALL_WANTED);
	}

	public void setHelperWantedEnabled(boolean enable) 
	{
		setValue(PREFERENCE_HELPER_WANTED, enable);
	}

	public boolean isHelperWantedEnabled() 
	{
		return getValueAsBoolean(PREFERENCE_HELPER_WANTED);
	}

	public void setWrapArraysEnabled(boolean enable) 
	{
		setValue(PREFERENCE_WRAP_ARRAYS, enable);
	}

	public boolean isWrapArraysEnabled() 
	{
		return getValueAsBoolean(PREFERENCE_WRAP_ARRAYS);
	}

	public void setUseInheritedMethodsEnabled(boolean enable) 
	{
		setValue(PREFERENCE_USE_INHERITED_METHODS, enable);
	}

	public boolean isUseInheritedMethodsEnabled() 
	{
		return getValueAsBoolean(PREFERENCE_USE_INHERITED_METHODS);
	}

	public void selectDeployScopeType(int selection) {
		setValue(PREFERENCE_DEPLOY_SCOPE, selection);
	}

	public int getDeployScopeType() {
		return getValueAsInt(PREFERENCE_DEPLOY_SCOPE);
	}

	public void setTimeOut(int seconds) {
		setValue(PREFERENCE_TIME_OUT, seconds);
	}

	public int getTimeOut() {
		return getValueAsInt(PREFERENCE_TIME_OUT);
	}
}
