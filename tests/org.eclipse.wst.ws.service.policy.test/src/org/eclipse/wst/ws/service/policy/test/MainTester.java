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

import junit.framework.TestCase;

import org.eclipse.wst.ws.service.policy.Descriptor;
import org.eclipse.wst.ws.service.policy.IPolicyEnumerationList;
import org.eclipse.wst.ws.service.policy.IPolicyRelationship;
import org.eclipse.wst.ws.service.policy.IPolicyStateEnum;
import org.eclipse.wst.ws.service.policy.IServicePolicy;
import org.eclipse.wst.ws.service.policy.IStateEnumerationItem;
import org.eclipse.wst.ws.service.policy.ServicePolicyPlatform;

public class MainTester extends TestCase
{  
   
   public void testExtensions()
   {
     ServicePolicyPlatform platform   = ServicePolicyPlatform.getInstance();
     List<IServicePolicy>  policyList = platform.getRootServicePolicies( null );
     
     for( IServicePolicy policy : policyList )
     {
       displayPolicyNode( policy );
     }   
     
     System.out.println( "" ); //$NON-NLS-1$
     System.out.println("=========== Enumerations:" ); //$NON-NLS-1$
     
     List<IStateEnumerationItem> errorWarnEnum = platform.getStateEnumeration( "org.eclipse.wst.service.policy.errorWarnEnum" ); //$NON-NLS-1$
     
     System.out.println( "Enum id: " + "org.eclipse.wst.service.policy.errorWarnEnum" ); //$NON-NLS-1$ //$NON-NLS-2$
     
     for( IStateEnumerationItem item : errorWarnEnum )
     {
       System.out.println( "  id: " + item.getId() ); //$NON-NLS-1$
       System.out.println( "  shortname: " + item.getShortName() ); //$NON-NLS-1$
       System.out.println( "  longname: " + item.getLongName() ); //$NON-NLS-1$
       System.out.println( "  isDefault: " + item.isDefault() ); //$NON-NLS-1$
       System.out.println( "" ); //$NON-NLS-1$
     }
   }
   
   private void displayPolicyNode( IServicePolicy policy )
   {
     IServicePolicy parentPolicy = policy.getParentPolicy();
     Descriptor     descriptor   = policy.getDescriptor();
     
     System.out.println( "Found policy: " + policy.getId() + " parentid:" + (parentPolicy == null ? "null" : parentPolicy.getId()) ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
     
     for( IServicePolicy child : policy.getChildren() )
     {
       System.out.println( "  child id: " + child.getId() ); //$NON-NLS-1$
     }
     
     List<IPolicyRelationship> relationships = policy.getRelationships();
     
     System.out.println(  "  relationships:" ); //$NON-NLS-1$
     
     for( IPolicyRelationship relationship : relationships )
     {
       IPolicyEnumerationList       sourceList     = relationship.getPolicyEnumerationList();
       List<IPolicyEnumerationList> targetList     = relationship.getRelatedPolicies();
       List<IStateEnumerationItem>  sourceEnumList = sourceList.getEnumerationList();
       
       for( IStateEnumerationItem item : sourceEnumList )
       {
         System.out.println( "    " + item.getId() ); //$NON-NLS-1$
       }
       
       for( IPolicyEnumerationList policyEnumList : targetList )
       {
         String                      policyId       = policyEnumList.getPolicy().getId();
         List<IStateEnumerationItem> targetEnumList = policyEnumList.getEnumerationList();
         
         System.out.println( "    policy id: " + policyId ); //$NON-NLS-1$
         
         for( IStateEnumerationItem targetItem : targetEnumList )
         {
           System.out.println( "      target enum id: " + targetItem.getId() ); //$NON-NLS-1$
         }
       }
     }
         
     System.out.println( "Descriptor:" ); //$NON-NLS-1$
     System.out.println( "  short name: " + descriptor.getShortName() ); //$NON-NLS-1$
     System.out.println( "  long name: " + descriptor.getLongName() ); //$NON-NLS-1$
     System.out.println( "  description: " + descriptor.getDescription() ); //$NON-NLS-1$
     System.out.println( "  context help: " + descriptor.getContextHelpId() ); //$NON-NLS-1$
     System.out.println( "  icon path: " + descriptor.getIconPath() ); //$NON-NLS-1$
     System.out.println( "  icon path bundle id: " + descriptor.getIconBundleId() ); //$NON-NLS-1$
     System.out.println( "===" ); //$NON-NLS-1$
     System.out.println( "" ); //$NON-NLS-1$
     
     for( IServicePolicy child : policy.getChildren() )
     {
       displayPolicyNode( child );
     }   
   }
   
   public void testEnumerations()
   {
     ServicePolicyPlatform platform  = ServicePolicyPlatform.getInstance();
     IServicePolicy        id1       = platform.getServicePolicy( "id1" );
     IPolicyStateEnum      state     = id1.getPolicyStateEnum();
     IStateEnumerationItem item      = state.getCurrentItem();
     
     System.out.println( "id2 value:" + item.getShortName() );
   }
}
