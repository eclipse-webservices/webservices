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

public interface AxisEmitterContext {
	/**
	 * This constant string is used to lookup the all_wanted general preference
	 * from the plugins local preferences store.
	 */
	public static final String PREFERENCE_ALL_WANTED = "allWanted";

	/**
	 * This constant string is used to lookup the helper_wanted general
	 * preference from the plugins local preferences store.
	 */
	public static final String PREFERENCE_HELPER_WANTED = "helperWanted";

	/**
	 * This constant string is used to lookup the wrap_arrays general preference
	 * from the plugins local preferences store.
	 */
	public static final String PREFERENCE_WRAP_ARRAYS = "wrapArrays";

	/**
	 * This constant string is used to lookup the deploy_scope general
	 * preference from the plugins local preferences store.
	 */
	public static final String PREFERENCE_DEPLOY_SCOPE = "deployScope";

	/**
	 * This constant string is used to lookup the time_out general preference
	 * from the plugins local preferences store.
	 */
	public static final String PREFERENCE_TIME_OUT = "timeOut";

	/*
	 * Ensure the order is the same as it in deployScopeTypes.setItems(...) for
	 * class
	 * org.eclipse.jst.ws.internal.axis.consumption.ui.preferences.AxisEmitterPreferencePage.
	 */
	public static final int DEPLOY_SCOPE_TYPE_APPLICATION = 0;

	public static final int DEPLOY_SCOPE_TYPE_REQUEST = 1;

	public static final int DEPLOY_SCOPE_TYPE_SESSTION = 2;

	/**
	 * This constant string is used to lookup the use_inherited_methods general
	 * preference from the plugins local preferences store.
	 */
	public static final String PREFERENCE_USE_INHERITED_METHODS = "useInheritedMethods";
	
	/**
	 * This constant string is used to lookup the "validate against JAXRPC"
	 * preference from the plugins local preferences store.
	 */
	public static final String PREFERENCE_VALIDATE_AGAINST_JAXRPC = "validateAgainstJAXRPC";

	/**
	 * 
	 * @param enable
	 *            set whether generating code for all elements is enabled.
	 */
	public void setAllWantedEnabled(boolean enable);

	/**
	 * 
	 * @return returns whether generating code for all elements is enabled.
	 */
	public boolean isAllWantedEnabled();

	/**
	 * 
	 * @param enable
	 *            set whether emitting separate Helper classes for meta data is
	 *            enabled.
	 */
	public void setHelperWantedEnabled(boolean enable);

	/**
	 * 
	 * @param returns
	 *            whether emitting separate Helper classes for meta data is
	 *            enabled.
	 */
	public boolean isHelperWantedEnabled();

	/**
	 * 
	 * @param enable
	 *            set whether wrapping arrays is enabled.
	 */

	public void setWrapArraysEnabled(boolean enable);

	/**
	 * 
	 * @param returns
	 *            whether wrapping arrays is enabled.
	 */

	public boolean isWrapArraysEnabled();

	/**
	 * 
	 * @param enable
	 *            set whether using inherited methods is enabled.
	 */
	public void setUseInheritedMethodsEnabled(boolean enable);

	/**
	 * 
	 * @param returns
	 *            whether using inherited methods is enabled.
	 */

	public boolean isUseInheritedMethodsEnabled();

	/**
	 * 
	 * @param enable
	 *            set whether JAX-RPC analysis of the service class is enabled.
	 */
	public void setValidateAgainstJAXRPCEnabled(boolean enable);

	/**
	 * 
	 * @param returns
	 *            whether JAX-RPC analysis of the service class is enabled.
	 */

	public boolean isValidateAgainstJAXRPCEnabled();

	/**
	 * 
	 * @param selection
	 *            set the deploy scope type.
	 */

	public void selectDeployScopeType(int selection);

	/**
	 * 
	 * @param returns
	 *            the deploy scope type.
	 */

	public int getDeployScopeType();

	/**
	 * 
	 * @param seconds
	 *            set the time out.
	 */

	public void setTimeOut(int seconds);

	/**
	 * 
	 * @param returns
	 *            the time out.
	 */

	public int getTimeOut();

	/**
	 * 
	 * @return returns a copy of this AxisEmitterContext.
	 */
}
