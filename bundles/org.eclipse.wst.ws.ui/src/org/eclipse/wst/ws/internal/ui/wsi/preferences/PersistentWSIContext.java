/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.ui.wsi.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.wst.ws.internal.ui.plugin.WSUIPlugin;
import org.eclipse.wst.command.internal.env.context.PersistentContext;
import org.eclipse.wst.command.internal.env.eclipse.EclipseLog;
import org.eclipse.wst.command.internal.provisional.env.core.common.Log;



public class PersistentWSIContext extends PersistentContext
{
	public static final String STOP_NON_WSI = "0";
	public static final String WARN_NON_WSI = "1";
	public static final String IGNORE_NON_WSI = "2";
	public static final String FOLLOW_WSI_PREFERENCE = "3";

	protected QualifiedName name = null; // set in subclass
	protected String non_wsi_compliance;
	private static final String NON_WSI_SSBP_COMPLIANCE = "nonWSISSBPCompliance";
	protected String wsiWarning_; // set in subclass
	protected String wsiError_; // set in subclass

public PersistentWSIContext () 
{
	super(  WSUIPlugin.getInstance());
	
//  NOTE:  name, wsiWarning_ and wsiError_ should be set in the subclasses.  This is providing defaulting only.
	non_wsi_compliance = NON_WSI_SSBP_COMPLIANCE;
	name = new QualifiedName(WSUIPlugin.ID , non_wsi_compliance);
	wsiWarning_ = "WSI_SSBP_WARNING";
	wsiError_ = "WSI_SSBP_ERROR";
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
//	 default to Ignore if no init has been done from ini file
	if (property == null || property.equals("")) {
		setValue( non_wsi_compliance, IGNORE_NON_WSI);
		return IGNORE_NON_WSI;
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
    	Log log = new EclipseLog();
    	log.log(Log.INFO, 5084, this, "updateProjectWSICompliances", "No such Project "+project);
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
  try {
     	String property = project.getPersistentProperty(name);
     	if (property == null || property.equals(""))
     	{
     		property = FOLLOW_WSI_PREFERENCE;
     	}
     	return property;
     }
     catch (CoreException e)     {
    	System.out.println("No such Project");
    	return getPersistentWSICompliance();
     }     
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
