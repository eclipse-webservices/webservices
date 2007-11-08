/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20071108   209267 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/

package org.eclipse.wst.ws.internal.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.wst.command.internal.env.context.PersistentContext;
import org.eclipse.wst.common.environment.EnvironmentService;
import org.eclipse.wst.common.environment.ILog;
import org.eclipse.wst.ws.internal.WstWSPluginMessages;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;
import org.eclipse.wst.ws.service.policy.IServicePolicy;
import org.eclipse.wst.ws.service.policy.ServicePolicyPlatform;



public class PersistentWSIContext extends PersistentContext
{
	public static final String STOP_NON_WSI = "0";
	public static final String WARN_NON_WSI = "1";
	public static final String IGNORE_NON_WSI = "2";
	public static final String FOLLOW_WSI_PREFERENCE = "3";
	
	private static final String[] ENUM_ID_VALUES = { "org.eclipse.wst.sug.require", "org.eclipse.wst.sug.suggest", "org.eclipse.wst.sug.ignore"};
  private static final String[] CONTEXT_IDS    = { STOP_NON_WSI, WARN_NON_WSI, IGNORE_NON_WSI };
	
	protected QualifiedName name = null; // set in subclass
	protected String non_wsi_compliance;
	private static final String NON_WSI_SSBP_COMPLIANCE = "nonWSISSBPCompliance";
	protected String wsiWarning_; // set in subclass
	protected String wsiError_; // set in subclass

public PersistentWSIContext () 
{
	super(  WSPlugin.getInstance());
	
//  NOTE:  name, wsiWarning_ and wsiError_ should be set in the subclasses.  This is providing defaulting only.
	non_wsi_compliance = NON_WSI_SSBP_COMPLIANCE;
	name = new QualifiedName(WSPlugin.ID , non_wsi_compliance);
	wsiWarning_ = WstWSPluginMessages.WSI_SSBP_WARNING;
	wsiError_ = WstWSPluginMessages.WSI_SSBP_ERROR;
}

public void load() 
{
	// let the default be read from plugin_costomization.ini file
	// setDefault(non_wsi_compliance, WARN_NON_WSI);
}

// to be used only by the preference page
public void updateWSICompliances ( String value)
{
 		setValue( non_wsi_compliance, value);
}

public String getPersistentWSICompliance ()
{
	String property = getValueAsString(non_wsi_compliance);
//	 default to Warning if no init has been done from ini file
	if (property == null || property.equals("")) {
		setValue( non_wsi_compliance, WARN_NON_WSI);
		return WARN_NON_WSI;
	}
	else
		return property;
}

public boolean StopNonWSICompliances()
{
	 return STOP_NON_WSI.equals(getPersistentWSICompliance());
}

public boolean WarnNonWSICompliances()
{
	 return WARN_NON_WSI.equals(getPersistentWSICompliance());
}

public boolean IgnoreNonWSICompliances()
{
	 return IGNORE_NON_WSI.equals(getPersistentWSICompliance());
}

// to be used only by the property page
public void updateProjectWSICompliances ( IProject project, String value)
{
    try {
		project.setPersistentProperty(name, value);
    }
    catch (CoreException e) { 
    	System.out.println("No such Project");
    	ILog log = EnvironmentService.getEclipseLog();
    	log.log(ILog.INFO, 5084, this, "updateProjectWSICompliances", "No such Project "+project);
    }
}

public boolean projectStopNonWSICompliances(IProject project)
{
 	return STOP_NON_WSI.equals(getProjectPersistentProperty(project));    
}

public boolean projectWarnNonWSICompliances(IProject project)
{
 	return WARN_NON_WSI.equals(getProjectPersistentProperty(project));    
}

public boolean projectIgnoreNonWSICompliances(IProject project)
{
 	return IGNORE_NON_WSI.equals(getProjectPersistentProperty(project));    
}


// to be used only by the property page
public boolean projectFollowWSIPreferance(IProject project)
{
    try {
 	String property = project.getPersistentProperty(name);
    return (property == null || property.equals("") || property.equals(FOLLOW_WSI_PREFERENCE));
    	}
    catch (CoreException e)     {
    	return true;
     }     
}

private String getProjectPersistentProperty(IProject project)
{
	String property = getProjectWSICompliance(project);
	if (property.equals(FOLLOW_WSI_PREFERENCE)) {
		property = getPersistentWSICompliance();
	}
	return property;
 }

  public String getProjectWSICompliance(IProject project)
  {

    ServicePolicyPlatform platform       = ServicePolicyPlatform.getInstance();
    IServicePolicy        servicePolicy  = platform.getServicePolicy( getServicePolicyId() );
    String                currentStateID = servicePolicy.getPolicyStateEnum(project).getCurrentItem().getId();
		String                property       = FOLLOW_WSI_PREFERENCE;
		
		for( int index = 0; index < ENUM_ID_VALUES.length; index++ )
		{
		  if( ENUM_ID_VALUES[index].equals( currentStateID ) )
		  {
		    property = CONTEXT_IDS[index];
		    break;
		  }
		}
		
		return property;
  }

  protected String getServicePolicyId()
  {
    return "org.eclipse.wst.ws.service.policy.ui.servicepols.wsiprofilecomp.wsissbp";
  }
  
  public QualifiedName getName()
  {
    return name;  
  }
  
  public String getWarning()
  {
	  return wsiWarning_;
  }

  public String getError()
  {
	  return wsiError_;
  }
}
