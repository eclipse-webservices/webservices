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
 * 20071105   196997 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.wst.ws.internal.preferences;

import org.eclipse.core.runtime.Preferences;
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
  private static final String[] ENUM_ID_VALUES = { "org.eclipse.wst.sug.require", "org.eclipse.wst.sug.suggest", "org.eclipse.wst.sug.ignore"};
  private static final String[] CONTEXT_IDS    = { PersistentWSIContext.STOP_NON_WSI, PersistentWSIContext.WARN_NON_WSI, PersistentWSIContext.IGNORE_NON_WSI };
  
  public void load()
  {
    // We need to check to see if the user has set any WSI preferences.
    // If not we want to default WSI preferences to any old WSI preferences that
    // have been set.
    
    IEclipsePreferences   preferences  = new InstanceScope().getNode( ServicePolicyActivator.PLUGIN_ID );    
    String                apValue      = preferences.get( "org.eclipse.wst.ws.service.policy.ui.servicepols.wsiprofilecomp.wsiap.value", null );
    String                ssbpValue    = preferences.get( "org.eclipse.wst.ws.service.policy.ui.servicepols.wsiprofilecomp.wsissbp.value", null );
    Preferences           oldPrefs     = WSPlugin.getInstance().getPluginPreferences();
    String[]              oldPropNames = oldPrefs.propertyNames();
    
    if( apValue == null )
    {
      ServicePolicyPlatform platform     = ServicePolicyPlatform.getInstance();
      IServicePolicy        apPolicy     = platform.getServicePolicy( "org.eclipse.wst.ws.service.policy.ui.servicepols.wsiprofilecomp.wsiap" );
      IPolicyState          apState      = apPolicy.getPolicyState();
      
      if( indexOfString( "nonWSIAPCompliance", oldPropNames ) != -1 )
      {
        // We have an old AP value so we will use it's value as the default for settings.
        String oldApValue = oldPrefs.getString( "nonWSIAPCompliance" );
        int    oldApIndex = indexOfString( oldApValue, CONTEXT_IDS );
        String newApValue = null;
        
        if( oldApIndex < ENUM_ID_VALUES.length )
        {
          newApValue = ENUM_ID_VALUES[oldApIndex];
        }
        
        if( newApValue != null )
        {
          apState.putValue( "value", newApValue );
        }
      }
    }
    
    if( ssbpValue == null )
    {
      ServicePolicyPlatform platform     = ServicePolicyPlatform.getInstance();
      IServicePolicy        ssbpPolicy   = platform.getServicePolicy( "org.eclipse.wst.ws.service.policy.ui.servicepols.wsiprofilecomp.wsissbp" );
      IPolicyState          ssbpState    = ssbpPolicy.getPolicyState();
      
      if( indexOfString( "nonWSISSBPCompliance", oldPropNames ) != -1 )
      {
        // We have an old SSBP value so we will use it's value as the default for settings.
        String oldSSBPValue = oldPrefs.getString( "nonWSISSBPCompliance" );
        int    oldSSBPIndex = indexOfString( oldSSBPValue, CONTEXT_IDS );
        String newSSBPValue = null;
        
        if( oldSSBPIndex < ENUM_ID_VALUES.length )
        {
          newSSBPValue = ENUM_ID_VALUES[oldSSBPIndex];
        }
        
        if( newSSBPValue != null )
        {
          ssbpState.putValue( "value", newSSBPValue );
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
