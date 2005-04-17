/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.widgets.bean;

import java.util.Hashtable;

import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;


public class DefaultsForConfig extends SimpleCommand
{
  public JavaWSDLParameter getJavaParameter()
  {
    JavaWSDLParameter parameter = new JavaWSDLParameter();
    Hashtable         methods   = new Hashtable();
    
    methods.put( "method1", new Boolean( true ) );
    methods.put( "method2", new Boolean( false ) );
    methods.put( "method3", new Boolean( true ) );
    
    parameter.setMethods( methods );
    parameter.setOutputWsdlLocation( "/SomeProj/SomeFolder/SomeFile" );
    parameter.setUrlLocation( "SomeURLLocation" );
    parameter.setStyle( JavaWSDLParameter.STYLE_DOCUMENT );
      
    return parameter;
  }
  
  public boolean getCustomizeServiceMappings()
  {
    return true;
  }      
}
