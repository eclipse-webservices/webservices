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

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import junit.framework.TestCase;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.ws.service.policy.IDescriptor;
import org.eclipse.wst.ws.service.policy.IFilter;
import org.eclipse.wst.ws.service.policy.IServicePolicy;
import org.eclipse.wst.ws.service.policy.ServicePolicyPlatform;
import org.eclipse.wst.ws.service.policy.ui.IPolicyOperation;
import org.eclipse.wst.ws.service.policy.ui.IQuickFixAction;
import org.eclipse.wst.ws.service.policy.ui.IQuickFixActionInfo;
import org.eclipse.wst.ws.service.policy.ui.ServicePolicyPlatformUI;
import org.eclipse.wst.ws.service.policy.ui.utils.ActivityUtils;

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
     IServicePolicy          id1        = platform.getServicePolicy( "id1" ); //$NON-NLS-1$
     IServicePolicy          id2        = platform.getServicePolicy( "id2" ); //$NON-NLS-1$
     IServicePolicy          id3        = platform.getServicePolicy( "id3" ); //$NON-NLS-1$
     IServicePolicy          id4        = platform.getServicePolicy( "id4" ); //$NON-NLS-1$
     IPolicyOperation        op1_id2    = platformUI.getOperation( id2, "service.ui.operation1" ); //$NON-NLS-1$
     IPolicyOperation        op1_id3    = platformUI.getOperation( id3, "service.ui.operation1" ); //$NON-NLS-1$
     IPolicyOperation        op2_id1    = platformUI.getOperation( id1, "service.ui.operation2" ); //$NON-NLS-1$
     IPolicyOperation        op2_id2    = platformUI.getOperation( id2, "service.ui.operation2" ); //$NON-NLS-1$
     IPolicyOperation        op2_id3    = platformUI.getOperation( id3, "service.ui.operation2" ); //$NON-NLS-1$
     IPolicyOperation        op2_id4    = platformUI.getOperation( id4, "service.ui.operation2" ); //$NON-NLS-1$
     IPolicyOperation        op3_id4    = platformUI.getOperation( id4, "service.ui.operation3" ); //$NON-NLS-1$
     IPolicyOperation        op4_id1    = platformUI.getOperation( id1, "service.ui.operation4" ); //$NON-NLS-1$
     IPolicyOperation        op4_id2    = platformUI.getOperation( id2, "service.ui.operation4" ); //$NON-NLS-1$
     IPolicyOperation        op4_id3    = platformUI.getOperation( id3, "service.ui.operation4" ); //$NON-NLS-1$
     
     List<IPolicyOperation> list1 = platformUI.getOperations( id1, true );
     List<IPolicyOperation> list2 = platformUI.getOperations( id2, true );
     List<IPolicyOperation> list3 = platformUI.getOperations( id3, true );
     List<IPolicyOperation> list4 = platformUI.getOperations( id4, true );
     
     System.out.println( "\nDisplay lists" ); //$NON-NLS-1$
     displayList( "List1", list1 ); //$NON-NLS-1$
     displayList( "List2", list2 ); //$NON-NLS-1$
     displayList( "List3", list3 ); //$NON-NLS-1$
     displayList( "List4", list4 ); //$NON-NLS-1$
     
     checkContents( "List1", list1, new IPolicyOperation[]{ op2_id1, op4_id1 } ); //$NON-NLS-1$
     checkContents( "List2", list2, new IPolicyOperation[]{ op1_id2, op2_id2, op4_id2 } ); //$NON-NLS-1$
     checkContents( "List3", list3, new IPolicyOperation[]{ op1_id3, op2_id3, op4_id3 } ); //$NON-NLS-1$
     checkContents( "List4", list4, new IPolicyOperation[]{ op2_id4, op3_id4 } ); //$NON-NLS-1$
   }
   
   public void testEnabledOperations()
   {
     ServicePolicyPlatform   platform    = ServicePolicyPlatform.getInstance();
     ServicePolicyPlatformUI platformUI  = ServicePolicyPlatformUI.getInstance();
     IServicePolicy          id1         = platform.getServicePolicy( "id1" ); //$NON-NLS-1$
     IServicePolicy          id2         = platform.getServicePolicy( "id2" ); //$NON-NLS-1$
     IServicePolicy          id3         = platform.getServicePolicy( "id3" ); //$NON-NLS-1$
     IServicePolicy          id4         = platform.getServicePolicy( "id4" ); //$NON-NLS-1$
     List<IServicePolicy>    policyList1 = new Vector<IServicePolicy>();
     List<IServicePolicy>    policyList2 = new Vector<IServicePolicy>();
     IPolicyOperation        op1         = platformUI.getOperation( id4, "service.ui.operation3" ); //$NON-NLS-1$
     
     policyList1.add( id1 );
     policyList1.add( id2 );
     policyList1.add( id3 );
     policyList1.add( id4 );
     policyList2.add( id4 );
     
     assertTrue( "Expected operation to not be enabled", !op1.isEnabled( policyList1 ) ); //$NON-NLS-1$
     assertTrue( "Expected operation to be enabled", op1.isEnabled( policyList2 ) ); //$NON-NLS-1$
   }
   
   public void testQuickFixActions()
   {
     ServicePolicyPlatform      platform    = ServicePolicyPlatform.getInstance();
     ServicePolicyPlatformUI    platformUI  = ServicePolicyPlatformUI.getInstance();
     IServicePolicy             id1         = platform.getServicePolicy( "id1" ); //$NON-NLS-1$
     IStatus                    status1     = new Status( IStatus.ERROR, "org.eclipse.wst.ws.service.policy.test", 1, "some error", null ); //$NON-NLS-1$ //$NON-NLS-2$
     IStatus                    status2     = new Status( IStatus.ERROR, "org.eclipse.wst.ws.service.policy.test", 2, "some error", null ); //$NON-NLS-1$ //$NON-NLS-2$
     List<IQuickFixActionInfo>  fixList1    = platformUI.getQuickFixes( status1 );
     List<IQuickFixActionInfo>  fixList2    = platformUI.getQuickFixes( status2 );
     
     assertTrue( "Expecting two quick fixes", fixList1.size() == 2 ); //$NON-NLS-1$
     assertTrue( "Expection one quick fix", fixList2.size() == 1 ); //$NON-NLS-1$
     
     for( IQuickFixActionInfo actionInfo : fixList1 )
     {
       IQuickFixAction action = actionInfo.getAction();
       id1.setStatus( status1 );
       
       assertTrue( "Status is not OK", !id1.getStatus().isOK() ); //$NON-NLS-1$
       action.action( id1 );
       assertTrue( "Status is Ok ", id1.getStatus().isOK() ); //$NON-NLS-1$
     }
     
     for( IQuickFixActionInfo actionInfo : fixList2 )
     {
       IQuickFixAction action = actionInfo.getAction();
       id1.setStatus( status2 );
       
       assertTrue( "Status is not OK", !id1.getStatus().isOK() ); //$NON-NLS-1$
       action.action( id1 );
       assertTrue( "Status is Ok ", id1.getStatus().isOK() ); //$NON-NLS-1$
     }
   }
   
   public void testActivitiesFilter()
   {
     IFilter                    activitiesFilter = ActivityUtils.getCurrentActivitiesFilter();
     ServicePolicyPlatform      platform         = ServicePolicyPlatform.getInstance();
     List<IServicePolicy>       policyList       = platform.getRootServicePolicies( activitiesFilter );
         
     displayPolicies( policyList );
   }
   
   @SuppressWarnings("unchecked")
  public void testGetOperationsList()
   {
     ServicePolicyPlatform   platform   = ServicePolicyPlatform.getInstance();
     ServicePolicyPlatformUI platformUI = ServicePolicyPlatformUI.getInstance();
     IServicePolicy          id1        = platform.getServicePolicy( "id1" ); //$NON-NLS-1$
     IServicePolicy          id2        = platform.getServicePolicy( "id2" ); //$NON-NLS-1$
     IServicePolicy          id3        = platform.getServicePolicy( "id3" ); //$NON-NLS-1$
     IServicePolicy          id4        = platform.getServicePolicy( "id4" ); //$NON-NLS-1$
     IPolicyOperation        op1_id2    = platformUI.getOperation( id2, "service.ui.operation1" ); //$NON-NLS-1$
     IPolicyOperation        op1_id3    = platformUI.getOperation( id3, "service.ui.operation1" ); //$NON-NLS-1$
     IPolicyOperation        op2_id1    = platformUI.getOperation( id1, "service.ui.operation2" ); //$NON-NLS-1$
     IPolicyOperation        op2_id2    = platformUI.getOperation( id2, "service.ui.operation2" ); //$NON-NLS-1$
     IPolicyOperation        op2_id3    = platformUI.getOperation( id3, "service.ui.operation2" ); //$NON-NLS-1$
     IPolicyOperation        op2_id4    = platformUI.getOperation( id4, "service.ui.operation2" ); //$NON-NLS-1$
     IPolicyOperation        op3_id4    = platformUI.getOperation( id4, "service.ui.operation3" ); //$NON-NLS-1$
     IPolicyOperation        op4_id1    = platformUI.getOperation( id1, "service.ui.operation4" ); //$NON-NLS-1$
     IPolicyOperation        op4_id2    = platformUI.getOperation( id2, "service.ui.operation4" ); //$NON-NLS-1$
     IPolicyOperation        op4_id3    = platformUI.getOperation( id3, "service.ui.operation4" ); //$NON-NLS-1$
     List<IServicePolicy>    idList1    = null;
     List<IServicePolicy>    idList2    = null;
     List<IServicePolicy>    idList3    = null;
     List<IServicePolicy>    idList4    = null;
     
     List<List<IPolicyOperation>> checkList1 = null;
     List<List<IPolicyOperation>> checkList2 = null;
     List<List<IPolicyOperation>> checkList3 = null;
     List<List<IPolicyOperation>> checkList4 = null;
     
     checkList1 = ll( l(op2_id1), l(op4_id1) );
     checkList2 = ll( l(op1_id2), l(op2_id1, op2_id2), l(op4_id1, op4_id2) );
     checkList3 = ll( l(op1_id2, op1_id3), l(op2_id1, op2_id2, op2_id3), l(op4_id1, op4_id2, op4_id3) );
     checkList4 = ll( l(op1_id2, op1_id3), l(op2_id1, op2_id2, op2_id3, op2_id4), l(op3_id4), l(op4_id1, op4_id2, op4_id3) );
     
     idList1 = Arrays.asList( new IServicePolicy[]{ id1 } );
     idList2 = Arrays.asList( new IServicePolicy[]{ id1, id2 } );
     idList3 = Arrays.asList( new IServicePolicy[]{ id1, id2, id3 } );
     idList4 = Arrays.asList( new IServicePolicy[]{ id1, id2, id3, id4 } );
     
     List<List<IPolicyOperation>> opList1 = platformUI.getOperationsList( idList1, true );
     List<List<IPolicyOperation>> opList2 = platformUI.getOperationsList( idList2, true );
     List<List<IPolicyOperation>> opList3 = platformUI.getOperationsList( idList3, true );
     List<List<IPolicyOperation>> opList4 = platformUI.getOperationsList( idList4, true );
     
     verifyLists( "List1", opList1, checkList1 );
     verifyLists( "List2", opList2, checkList2 );
     verifyLists( "List3", opList3, checkList3 );
     verifyLists( "List4", opList4, checkList4 );
   }
   
   private List<List<IPolicyOperation>> ll( List<IPolicyOperation>... opList )
   {
     return Arrays.asList( opList );
   }
   
   private List<IPolicyOperation> l( IPolicyOperation... opList )
   {
     return Arrays.asList( opList );
   }
   
   private void verifyLists( String listName, List<List<IPolicyOperation>> list1, List<List<IPolicyOperation>> list2 )
   {
     assertTrue( listName + " does not have the same size.", list1.size() == list2.size() );
             
     for( int listIndex = 0 ; listIndex < list1.size(); listIndex++ )
     {
       List<IPolicyOperation> policyList1 = list1.get( listIndex );
       List<IPolicyOperation> policyList2 = findList( policyList1.get(0).getId(), list2 );
       
       assertTrue( listName + " nested list at index " + listIndex + " is not the same size.", policyList1.size() == policyList2.size() );
       
       for( int index = 0; index < policyList1.size(); index++ )
       {
         IPolicyOperation op1 = policyList1.get( index );
         
         assertTrue( listName + " operation not found", findOp( op1, policyList2 ) );
       }
     }  
   }
  
   private boolean findOp( IPolicyOperation op, List<IPolicyOperation> list )
   {
     boolean result = false;
     
     for( IPolicyOperation foundOp : list )
     {
       if( op == foundOp )
       {
         result = true;
         break;
       }
     }
     
     return result;
   }
   
   private List<IPolicyOperation> findList( String id, List<List<IPolicyOperation>> searchList )
   {
     for( List<IPolicyOperation> policyList : searchList )
     {
       if( policyList.get(0).getId().equals( id ) )
       {
         return policyList;
       }
     }
     
     assertTrue( "List not found for id" + id, false );
     return null;
   }
   
   private void displayPolicies( List<IServicePolicy> policyList )
   {
     System.out.println( "Listing service policies:" );
     for( IServicePolicy policy : policyList )
     {
       System.out.println( "  " + policy.getId() );
     }
   }
   
   private void displayList( String setName, List<IPolicyOperation> operationList )
   {
     System.out.print( setName + ": " ); //$NON-NLS-1$
     
     for( IPolicyOperation operation : operationList )
     {
       System.out.print( operation.getId() + "," ); //$NON-NLS-1$
     }
     
     System.out.println( "" ); //$NON-NLS-1$
   }
   
   private void checkContents( String setName, List<IPolicyOperation> operationSet, IPolicyOperation[] operationArray )
   {
     assertTrue( "Set and array sizes don't match for " + setName + ".", operationSet.size() == operationArray.length ); //$NON-NLS-1$ //$NON-NLS-2$

     for( IPolicyOperation operation : operationArray )
     {
       assertTrue( setName + " does not contain operation " + operation.getId() + ".", operationSet.contains( operation ) ); //$NON-NLS-1$ //$NON-NLS-2$
     }
   }
   
   private void displayOperation( IPolicyOperation operation )
   {
     List<IServicePolicy> zeroPolicies = new Vector<IServicePolicy>();
     List<IServicePolicy> manyPolicies = ServicePolicyPlatform.getInstance().getRootServicePolicies( null );
     IDescriptor           descriptor   = operation.getDescriptor();
     String               shortName    = descriptor.getShortName();
     String               longName     = descriptor.getLongName();
     
     System.out.println( "Operation id: " + operation.getId() ); //$NON-NLS-1$
     System.out.println( "          short name: " + shortName ); //$NON-NLS-1$
     System.out.println( "          long name: " + longName ); //$NON-NLS-1$
     System.out.println( "          pattern: " + operation.getPolicyIdPattern() ); //$NON-NLS-1$
     System.out.println( "          isEnabled zero: " + operation.isEnabled( zeroPolicies ) ); //$NON-NLS-1$
     System.out.println( "          isEnabled many: " + operation.isEnabled( manyPolicies ) ); //$NON-NLS-1$
     
     switch( operation.getOperationKind() )
     {
       case selection:
       {
         System.out.println( "          selection: true" ); //$NON-NLS-1$
         break;  
       }
       
       case iconSelection:
       {
         System.out.println( "          selection icon: true" ); //$NON-NLS-1$
         break;  
       }
       
       case enumeration:
       {      
         System.out.println( "          enumeration id: " + operation.getEnumerationId() ); //$NON-NLS-1$
         break;  
       }
       
       case complex:
       {
         System.out.println( "          complex: true" );  //$NON-NLS-1$
         operation.launchOperation( zeroPolicies );
         break;  
       }
     }
   }
}
