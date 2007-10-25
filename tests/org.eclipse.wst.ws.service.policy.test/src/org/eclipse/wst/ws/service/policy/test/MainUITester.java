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
import java.util.Set;
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
   
   public void testOperationCache()
   {
     ServicePolicyPlatform   platform   = ServicePolicyPlatform.getInstance();
     ServicePolicyPlatformUI platformUI = ServicePolicyPlatformUI.getInstance();
     IServicePolicy          id1        = platform.getServicePolicy( "id1" );
     IServicePolicy          id2        = platform.getServicePolicy( "id2" );
     IServicePolicy          id3        = platform.getServicePolicy( "id3" );
     IServicePolicy          id4        = platform.getServicePolicy( "id4" );
     List<IServicePolicy>    list1      = new Vector<IServicePolicy>();
     List<IServicePolicy>    list2      = new Vector<IServicePolicy>();
     List<IServicePolicy>    list3      = new Vector<IServicePolicy>();
     List<IServicePolicy>    list4      = new Vector<IServicePolicy>();
     List<IServicePolicy>    list5      = new Vector<IServicePolicy>();
     List<IServicePolicy>    list6      = new Vector<IServicePolicy>();
     List<IServicePolicy>    list7      = new Vector<IServicePolicy>();
     IPolicyOperation        op1        = platformUI.getOperation( "service.ui.operation1" );
     IPolicyOperation        op2        = platformUI.getOperation( "service.ui.operation2" );
     IPolicyOperation        op3        = platformUI.getOperation( "service.ui.operation3" );
     IPolicyOperation        op4        = platformUI.getOperation( "service.ui.operation4" );
  
     list1.add( id1 );
     list2.add( id2 );
     list3.add( id3 );
     list4.add( id4 );
     list5.add( id4 );
     list5.add( id1 );
     list6.add( id2 );
     list6.add( id3 );
     list7.add( id1 );
     list7.add( id2 );
     list7.add( id3 );
     list7.add( id4 );
    
     Set<IPolicyOperation> set1 = platformUI.getSelectedOperations( list1 );
     Set<IPolicyOperation> set2 = platformUI.getSelectedOperations( list2 );
     Set<IPolicyOperation> set3 = platformUI.getSelectedOperations( list3 );
     Set<IPolicyOperation> set4 = platformUI.getSelectedOperations( list4 );
     Set<IPolicyOperation> set5 = platformUI.getSelectedOperations( list5 );
     Set<IPolicyOperation> set6 = platformUI.getSelectedOperations( list6 );
     Set<IPolicyOperation> set7 = platformUI.getSelectedOperations( list7 );
     
     System.out.println( "\nDisplay sets" );
     displaySet( "Set1", set1 );
     displaySet( "Set2", set2 );
     displaySet( "Set3", set3 );
     displaySet( "Set4", set4 );
     displaySet( "Set5", set5 );
     displaySet( "Set6", set6 );
     displaySet( "Set7", set7 );
     
     checkContents( "Set1", set1, new IPolicyOperation[]{ op2, op4 } );
     checkContents( "Set2", set2, new IPolicyOperation[]{ op1, op2, op4 } );
     checkContents( "Set3", set3, new IPolicyOperation[]{ op1, op2, op4 } );
     checkContents( "Set4", set4, new IPolicyOperation[]{ op2, op3 } );
     checkContents( "Set5", set5, new IPolicyOperation[]{ op2, op3, op4 } );
     checkContents( "Set6", set6, new IPolicyOperation[]{ op1, op2, op4 } );
     checkContents( "Set7", set7, new IPolicyOperation[]{ op1, op2, op3, op4 } );
   }
   
   private void displaySet( String setName, Set<IPolicyOperation> operationSet )
   {
     System.out.print( setName + ": " );
     
     for( IPolicyOperation operation : operationSet )
     {
       System.out.print( operation.getId() + "," );
     }
     
     System.out.println( "" );
   }
   
   private void checkContents( String setName, Set<IPolicyOperation> operationSet, IPolicyOperation[] operationArray )
   {
     assertTrue( "Set and array sizes don't match for " + setName + ".", operationSet.size() == operationArray.length );

     for( IPolicyOperation operation : operationArray )
     {
       assertTrue( setName + " does not contain operation " + operation.getId() + ".", operationSet.contains( operation ) );
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
