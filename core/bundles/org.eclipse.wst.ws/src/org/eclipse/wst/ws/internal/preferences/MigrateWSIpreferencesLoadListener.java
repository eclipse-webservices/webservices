/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20071105   196997 pmoogk@ca.ibm.com - Peter Moogk
 * 20071218   209858 epeters@ca.ibm.com - Eric Peters, Enhancing service policy framework and UI
 * 20080318   223118 ericdp@ca.ibm.com - Eric D. Peters, metadata required in WS-I profiles to support editors & validators
 * 20080318   221578 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.wst.ws.internal.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;
import org.eclipse.wst.ws.service.policy.IPolicyState;
import org.eclipse.wst.ws.service.policy.IServicePolicy;
import org.eclipse.wst.ws.service.policy.ServicePolicyActivator;
import org.eclipse.wst.ws.service.policy.ServicePolicyPlatform;
import org.eclipse.wst.ws.service.policy.listeners.IPolicyPlatformLoadListener;

public class MigrateWSIpreferencesLoadListener implements IPolicyPlatformLoadListener
{
  private static final String[] ENUM_ID_VALUES = { "org.eclipse.wst.sug.require", "org.eclipse.wst.sug.suggest", "org.eclipse.wst.sug.ignore"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  private static final String[] CONTEXT_IDS    = { PersistentWSIContext.STOP_NON_WSI, PersistentWSIContext.WARN_NON_WSI, PersistentWSIContext.IGNORE_NON_WSI };
  
  public void load()
  {
    // We need to check to see if the user has set any WSI preferences.
    // If not we want to default WSI preferences to any old WSI preferences that
    // have been set.
    
    IEclipsePreferences   preferences  = new InstanceScope().getNode( ServicePolicyActivator.PLUGIN_ID );    
    String                apValue      = preferences.get( WSIServicePoliciesConstants.ServicePolicyID_AP10 + "." + IPolicyState.DefaultValueKey, null ); //$NON-NLS-1$
    String                ssbpValue    = preferences.get( WSIServicePoliciesConstants.ServicePolicyID_SSBP10 + "." + IPolicyState.DefaultValueKey, null ); //$NON-NLS-1$
    Preferences           oldPrefs     = WSPlugin.getInstance().getPluginPreferences();
    String[]              oldPropNames = oldPrefs.propertyNames();
    
    if( apValue == null )
    {
      ServicePolicyPlatform platform     = ServicePolicyPlatform.getInstance();
      IServicePolicy        apPolicy     = platform.getServicePolicy( WSIServicePoliciesConstants.ServicePolicyID_AP10 );
      IPolicyState          apState      = apPolicy.getPolicyState();
      
      if( indexOfString( "nonWSIAPCompliance", oldPropNames ) != -1 ) //$NON-NLS-1$
      {
        // We have an old AP value so we will use it's value as the default for settings.
        String oldApValue = oldPrefs.getString( "nonWSIAPCompliance" ); //$NON-NLS-1$
        int    oldApIndex = indexOfString( oldApValue, CONTEXT_IDS );
        String newApValue = null;
        
        if( oldApIndex < ENUM_ID_VALUES.length )
        {
          newApValue = ENUM_ID_VALUES[oldApIndex];
        }
        
        if( newApValue != null )
        {
          apState.putValue( IPolicyState.DefaultValueKey, newApValue );
        }
      }
      		// also need to check if any project specific preferences were set
			QualifiedName name = new QualifiedName(WSPlugin.ID,
					"nonWSIAPCompliance"); //$NON-NLS-1$
			IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
					.getProjects();
			for (int i = 0; i < projects.length; i++) {
				try {
					IProject project = projects[i];
					String oldProperty = null;
						oldProperty = project.getPersistentProperty(name);
					if (oldProperty != null && !oldProperty.equals("")) { //$NON-NLS-1$
						int oldApIndex = indexOfString(oldProperty, CONTEXT_IDS);
						String newApValue = null;
						if (oldApIndex != -1 && oldApIndex < ENUM_ID_VALUES.length) {
							newApValue = ENUM_ID_VALUES[oldApIndex];
						}

						if (newApValue != null) {
							apState = apPolicy.getPolicyState(project);
							platform.setProjectPreferencesEnabled(project, true);
					        apState.putValue( IPolicyState.DefaultValueKey, newApValue );
						}
					}
				} catch (CoreException e) {
					//ignore this project
				}
			}
    }


    
    if( ssbpValue == null )
    {
      ServicePolicyPlatform platform     = ServicePolicyPlatform.getInstance();
      IServicePolicy        ssbpPolicy   = platform.getServicePolicy( WSIServicePoliciesConstants.ServicePolicyID_SSBP10 );
      IPolicyState          ssbpState    = ssbpPolicy.getPolicyState();
      
      if( indexOfString( "nonWSISSBPCompliance", oldPropNames ) != -1 ) //$NON-NLS-1$
      {
        // We have an old SSBP value so we will use it's value as the default for settings.
        String oldSSBPValue = oldPrefs.getString( "nonWSISSBPCompliance" ); //$NON-NLS-1$
        int    oldSSBPIndex = indexOfString( oldSSBPValue, CONTEXT_IDS );
        String newSSBPValue = null;
        
        if( oldSSBPIndex < ENUM_ID_VALUES.length )
        {
          newSSBPValue = ENUM_ID_VALUES[oldSSBPIndex];
        }
        
        if( newSSBPValue != null )
        {
        	ssbpState.putValue( IPolicyState.DefaultValueKey, newSSBPValue );
        }
      }
		// also need to check if any project specific preferences were set
		QualifiedName name = new QualifiedName(WSPlugin.ID,
				"nonWSISSBPCompliance"); //$NON-NLS-1$
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();
		for (int i = 0; i < projects.length; i++) {
			try {
				IProject project = projects[i];
				String oldProperty = null;
					oldProperty = project.getPersistentProperty(name);
				if (oldProperty != null && !oldProperty.equals("")) { //$NON-NLS-1$
					int oldSSBPIndex = indexOfString(oldProperty, CONTEXT_IDS);
					String newSSBPValue = null;
					if (oldSSBPIndex != -1 && oldSSBPIndex < ENUM_ID_VALUES.length) {
						newSSBPValue = ENUM_ID_VALUES[oldSSBPIndex];
					}

					if (newSSBPValue != null) {
						ssbpState = ssbpPolicy.getPolicyState(project);
						platform.setProjectPreferencesEnabled(project, true);
						ssbpState.putValue( IPolicyState.DefaultValueKey, newSSBPValue );
					}
				}
			} catch (CoreException e) {
				//ignore this project
			}
		}

    }
  }
  
  private int indexOfString( String target, String[] values )
  {
    int result = -1;
    
    for( int index = 0; index< values.length; index++ )
    {
      String value = values[index];
      
      if( value.equals( target ) )
      {
        result = index;
        break;
      }
    }
    
    return result;
  }
}
