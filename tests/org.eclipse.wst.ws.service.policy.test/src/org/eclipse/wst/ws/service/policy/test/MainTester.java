/***************************************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************/
package org.eclipse.wst.ws.service.policy.test;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.wst.ws.service.policy.Descriptor;
import org.eclipse.wst.ws.service.policy.IPolicyEnumerationList;
import org.eclipse.wst.ws.service.policy.IPolicyRelationship;
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
     
     System.out.println( "" );
     System.out.println("=========== Enumerations:" );
     
     List<IStateEnumerationItem> errorWarnEnum = platform.getStateEnumeration( "org.eclipse.wst.service.policy.errorWarnEnum" );
     
     System.out.println( "Enum id: " + "org.eclipse.wst.service.policy.errorWarnEnum" );
     
     for( IStateEnumerationItem item : errorWarnEnum )
     {
       System.out.println( "  id: " + item.getId() );
       System.out.println( "  shortname: " + item.getShortName() );
       System.out.println( "  longname: " + item.getLongName() );
       System.out.println( "  isDefault: " + item.isDefault() );
       System.out.println( "" );
     }
   }
   
   private void displayPolicyNode( IServicePolicy policy )
   {
     IServicePolicy parentPolicy = policy.getParentPolicy();
     Descriptor     descriptor   = policy.getDescriptor();
     
     System.out.println( "Found policy: " + policy.getId() + " parentid:" + (parentPolicy == null ? "null" : parentPolicy.getId()) );
     
     for( IServicePolicy child : policy.getChildren() )
     {
       System.out.println( "  child id: " + child.getId() );
     }
     
     List<IPolicyRelationship> relationships = policy.getRelationships();
     
     System.out.println(  "  relationships:" );
     
     for( IPolicyRelationship relationship : relationships )
     {
       IPolicyEnumerationList       sourceList     = relationship.getPolicyEnumerationList();
       List<IPolicyEnumerationList> targetList     = relationship.getRelatedPolicies();
       List<IStateEnumerationItem>  sourceEnumList = sourceList.getEnumerationList();
       
       for( IStateEnumerationItem item : sourceEnumList )
       {
         System.out.println( "    " + item.getId() );
       }
       
       for( IPolicyEnumerationList policyEnumList : targetList )
       {
         String                      policyId       = policyEnumList.getPolicy().getId();
         List<IStateEnumerationItem> targetEnumList = policyEnumList.getEnumerationList();
         
         System.out.println( "    policy id: " + policyId );
         
         for( IStateEnumerationItem targetItem : targetEnumList )
         {
           System.out.println( "      target enum id: " + targetItem.getId() );
         }
       }
     }
         
     System.out.println( "Descriptor:" );
     System.out.println( "  short name: " + descriptor.getShortName() );
     System.out.println( "  long name: " + descriptor.getLongName() );
     System.out.println( "  description: " + descriptor.getDescription() );
     System.out.println( "  context help: " + descriptor.getContextHelpId() );
     System.out.println( "  icon path: " + descriptor.getIconPath() );
     System.out.println( "  icon path bundle id: " + descriptor.getIconBundleId() );
     System.out.println( "===" );
     System.out.println( "" );
     
     for( IServicePolicy child : policy.getChildren() )
     {
       displayPolicyNode( child );
     }   
   }
}
