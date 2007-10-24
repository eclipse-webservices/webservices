/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20071024   196997 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy.test;

import java.util.List;
import java.util.Vector;

import junit.framework.TestCase;

import org.eclipse.wst.ws.service.policy.Descriptor;
import org.eclipse.wst.ws.service.policy.IServicePolicy;
import org.eclipse.wst.ws.service.policy.ServicePolicyPlatform;
import org.eclipse.wst.ws.service.policy.ui.IPolicyOperation;
import org.eclipse.wst.ws.service.policy.ui.ServicePolicyPlatformUI;

public class MainUITester extends TestCase
{  
   
   public void testExtensions()
   {
     ServicePolicyPlatformUI platformUI   = ServicePolicyPlatformUI.getInstance();
     List<IPolicyOperation>  operations   = platformUI.getAllOperations();
     
     for( IPolicyOperation operation : operations )
     {
       displayOperation( operation );
     }        
   }
   
   private void displayOperation( IPolicyOperation operation )
   {
     List<IServicePolicy> zeroPolicies = new Vector<IServicePolicy>();
     List<IServicePolicy> manyPolicies = ServicePolicyPlatform.getInstance().getRootServicePolicies( null );
     Descriptor           descriptor   = operation.getDescriptor();
     String               shortName    = descriptor.getShortName();
     String               longName     = descriptor.getLongName();
     
     System.out.println( "Operation id: " + operation.getId() );
     System.out.println( "          short name: " + shortName );
     System.out.println( "          long name: " + longName );
     System.out.println( "          pattern: " + operation.getPolicyIdPattern() );
     System.out.println( "          isEnabled zero: " + operation.isEnabled( zeroPolicies ) );
     System.out.println( "          isEnabled many: " + operation.isEnabled( manyPolicies ) );
     
     switch( operation.getOperationKind() )
     {
       case selection:
       {
         System.out.println( "          selection: true" );
         break;  
       }
       
       case iconSelection:
       {
         System.out.println( "          selection icon: true" );
         break;  
       }
       
       case enumeration:
       {      
         System.out.println( "          enumeration id: " + operation.getEnumerationId() );
         break;  
       }
       
       case complex:
       {
         System.out.println( "          complex: true" ); 
         operation.launchOperation( zeroPolicies );
         break;  
       }
     }
   }
}
