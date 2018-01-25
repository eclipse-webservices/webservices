/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.wst.ws.internal.preferences.WSIServicePoliciesConstants;
import org.eclipse.wst.ws.service.policy.IPolicyState;
import org.eclipse.wst.ws.service.policy.IPolicyStateEnum;
import org.eclipse.wst.ws.service.policy.IServicePolicy;
import org.eclipse.wst.ws.service.policy.ServicePolicyPlatform;

/**
 * Contains helper methods dealing with service policies
 */
public class ServicePolicyHelper
{
  /**
   * Returns an active policy with an associated default protocol
   * 
   * @param project
   * @return IServicePolicy if there is one, otherwise null
   */
  public static IServicePolicy getActivePolicyWithProtocol(IProject project)
  {
    IServicePolicy thePolicy = null;

    IServicePolicy[] policies = getActiveServicePolicies(project);
    for (int i = 0; i < policies.length; ++i)
    {
      IServicePolicy policy = policies[i];
      // get the default protocol from the policy
      IPolicyState policyState = policy.getPolicyState(project);
      if (policyState != null)
      {
        String defaultBinding = policyState.getValue(WSIServicePoliciesConstants.stateKeyDefaultProtocol);
        if ((defaultBinding != null) && (defaultBinding.length() > 0))
        {
          // policy will be last one active
          thePolicy = policy;
          IPolicyStateEnum policyStateEnum = policy.getPolicyStateEnum(project);
          String enumId = policyStateEnum.getCurrentItem().getId();
          // if found a required policy, stop and return it
          if (WSIServicePoliciesConstants.stateEnumRequire.equals(enumId))
            break;
        }
      }
    }
    return thePolicy;
  }

  /**
   * Get the default binding based on the project
   * 
   * @param project
   * @return String binding id (namespace)
   */
  public static String getDefaultBinding(IProject project)
  {
    String defaultBinding = null;
    IServicePolicy policy = getActivePolicyWithProtocol(project);
    if (policy != null)
    {
      defaultBinding = policy.getPolicyState(project).getValue(WSIServicePoliciesConstants.stateKeyDefaultProtocol);
    }

    // still unable to get default binding so just hardcode a value
    if (defaultBinding == null)
      defaultBinding = getDefaultDefaultBinding();

    return defaultBinding;
  }

  /**
   * Get the default binding based on project and service policy
   * 
   * @param project
   * @param policy
   * @return String binding id (namespace)
   */
  public static String getDefaultBinding(IProject project, IServicePolicy policy)
  {
    String defaultBinding = null;
    if (policy != null)
    {
      defaultBinding = policy.getPolicyState(project).getValue(WSIServicePoliciesConstants.stateKeyDefaultProtocol);
    }

    // still unable to get default binding so just hardcode a value
    if (defaultBinding == null)
      defaultBinding = getDefaultDefaultBinding();

    return defaultBinding;
  }

  /**
   * Get the severity to be used when there are problems with the current policy
   * 
   * @param project
   * @param policy
   * @return int (IMessageProvider.NONE, IMessageProvider.ERROR,
   *         IMessageProvider.WARNING)
   */
  public static int getMessageSeverity(IProject project, IServicePolicy policy)
  {
    int messageSeverity = IMessageProvider.NONE;

    if (policy != null)
    {
      IPolicyStateEnum policyStateEnum = policy.getPolicyStateEnum(project);
      String enumId = policyStateEnum.getCurrentItem().getId();

      if (WSIServicePoliciesConstants.stateEnumRequire.equals(enumId))
        messageSeverity = IMessageProvider.ERROR;
      else if (WSIServicePoliciesConstants.stateEnumSuggest.equals(enumId))
        messageSeverity = IMessageProvider.WARNING;
    }

    return messageSeverity;
  }

  /**
   * Get the default binding
   * 
   * @return String binding id (namespace)
   */
  private static String getDefaultDefaultBinding()
  {
    return WSIServicePoliciesConstants.stateKeyDefaultProtocol_VALUE_SOAP;
  }

  /**
   * Get active service policies based on project
   * 
   * @param project
   * @return IServicePolicy[]
   */
  private static IServicePolicy[] getActiveServicePolicies(IProject project)
  {
    List servicePolicies = new ArrayList();
    // Determine default binding to use based on project's wsi compliance
    // preference
    IServicePolicy rootPolicy = ServicePolicyPlatform.getInstance().getServicePolicy(WSIServicePoliciesConstants.ServicePolicyID_WSIProfileRoot);
    if (rootPolicy != null)
    {
      List wsiPolicies = rootPolicy.getChildren();
      // iterate through wsi policies and determine which ones are applicable
      for (int i = 0; i < wsiPolicies.size(); ++i)
      {
        IServicePolicy policy = (IServicePolicy) wsiPolicies.get(i);
        IPolicyStateEnum policyStateEnum = policy.getPolicyStateEnum(project);
        String enumId = policyStateEnum.getCurrentItem().getId();
        // found an enabled policy
        if (WSIServicePoliciesConstants.stateEnumRequire.equals(enumId) || WSIServicePoliciesConstants.stateEnumSuggest.equals(enumId))
        {
          // special check for AP 1.0 policy (it doesnt really count, so dont
          // add it)
          if (!WSIServicePoliciesConstants.ServicePolicyID_AP10.equals(policy.getId()))
          {
            servicePolicies.add(policy);
          }
        }
      }
    }
    return (IServicePolicy[]) servicePolicies.toArray(new IServicePolicy[servicePolicies.size()]);
  }
}
