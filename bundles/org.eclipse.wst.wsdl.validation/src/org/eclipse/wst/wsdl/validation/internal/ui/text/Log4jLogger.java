/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.validation.internal.ui.text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.wst.wsdl.validation.internal.logging.ILogger;

/**
 * A custom WSDL validator logger that passes logging events to log4j.
 */
public class Log4jLogger implements ILogger 
{
  protected Object logger = null;
  protected Method error = null;
  protected Method warn = null;
  protected Method info = null;
  
  public Log4jLogger()
  {
	try
    {
	  Class loggerClass = getClass().getClassLoader().loadClass("org.apache.log4j.Logger");
	  Class categoryClass = getClass().getClassLoader().loadClass("org.apache.log4j.Category");
	  Method getLogger = loggerClass.getDeclaredMethod("getLogger" , new Class[]{Class.class});
	  logger = getLogger.invoke(loggerClass, new Object[]{WSDLValidate.class});
	  error = categoryClass.getDeclaredMethod("error" , new Class[]{Object.class, Throwable.class});
	  warn = categoryClass.getDeclaredMethod("warn" , new Class[]{Object.class, Throwable.class});
	  info = categoryClass.getDeclaredMethod("info" , new Class[]{Object.class, Throwable.class});
	}
	catch(ClassNotFoundException e)
	{
	  System.err.println("Unable to create Log4j Logger. Ensure Log4J is on the classpath."); 
	}
	catch(NoSuchMethodException e)
	{
		
	}
	catch(IllegalAccessException e)
	{
		
	}
	catch(InvocationTargetException e)
	{
		
	}
  }
  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.validation.internal.logging.ILogger#log(java.lang.String, int, java.lang.Throwable)
   */
  public void log(String message, int severity, Throwable throwable) 
  {
	if(logger != null)
	{
	  try
	  {
	    if(severity == ILogger.SEV_ERROR)
	    {
		  error.invoke(logger, new Object[]{message, throwable});
	    }
	    else if(severity == ILogger.SEV_WARNING)
	    {
		  warn.invoke(logger, new Object[]{message, throwable});
	    }
	    else if(severity == ILogger.SEV_INFO)
	    {
		  info.invoke(logger, new Object[]{message, throwable});
	    }
	  }
	  catch(InvocationTargetException e)
	  {
		  
	  }
	  catch(IllegalAccessException e)
	  {
		  
	  }
	}
	// Logger logger = new Logger();

  }

}
