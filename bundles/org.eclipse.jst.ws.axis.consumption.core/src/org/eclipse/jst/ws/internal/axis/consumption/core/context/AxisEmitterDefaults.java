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

public class AxisEmitterDefaults
{
 private static final boolean PREFERENCE_ALL_WANTED_DEFAULT = false;
 private static final boolean PREFERENCE_HELPER_WANTED_DEFAULT = false;
 private static final boolean PREFERENCE_WRAP_ARRAYS_DEFAULT = false;
 private static final boolean PREFERENCE_USE_INHERITED_METHODS_DEFAULT = false;
 private static final boolean PREFERENCE_VALIDATE_AGAINST_JAXRPC = true;
 private static final int DEPLOY_SCOPE_DEFAULT = AxisEmitterContext.DEPLOY_SCOPE_TYPE_REQUEST;
 private static final int TIME_OUT_DEFAULT = 45;
 
 /**
  * 
  * @return returns the default setting for generating code for all elements.
  */
 public static boolean getAllWantedDefault ()
 {
 	return PREFERENCE_ALL_WANTED_DEFAULT;
 }
 
 /**
  * 
  * @return returns the default setting for emitting seperate helpser class for metadata.
  */
 public static boolean getHelperWantedDefault ()
 { 
	return PREFERENCE_HELPER_WANTED_DEFAULT;
 }

 /**
  * 
  * @return returns the default seting for wrapping arrays.
  */
 public static boolean getWrapArraysDefault()
 {
	return PREFERENCE_WRAP_ARRAYS_DEFAULT;
 }
 
 /**
  * 
  * @return returns the default setting for using inherited methods.
  */
 public static boolean getUseInheritedMethodsDefault()
 {
	return PREFERENCE_USE_INHERITED_METHODS_DEFAULT;
 }
 
 /**
  * 
  * @return returns the default setting for JAX-RPC validation of the service class.
  * Note: This is not a true Axis emitter preference. This preference controls whether
  * the Axis Web service bottom-up scenario analyzes the service class for compliance
  * to JAX-RPC some time before calling the emitters. 
  */
 public static boolean getValidateAgainstJAXRPC()
 {
	return PREFERENCE_VALIDATE_AGAINST_JAXRPC;
 }
 
 /**
  * 
  * @return returns the default setting for deploy scope type.
  */
 public static int getDeployScopeDefault()
 {
	 return DEPLOY_SCOPE_DEFAULT;
 }
 
 /**
  * 
  * @return returns the default setting for time out.
  */
 public static int getTimeOutDefault()
 {
	 return TIME_OUT_DEFAULT;
 }
}
