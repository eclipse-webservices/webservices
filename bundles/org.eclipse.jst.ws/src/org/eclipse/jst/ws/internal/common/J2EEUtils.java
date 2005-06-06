/*******************************************************************************
 * Copyright (c) 2000, 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.j2ee.application.internal.operations.AddComponentToEnterpriseApplicationDataModel;
import org.eclipse.jst.j2ee.application.internal.operations.AddComponentToEnterpriseApplicationOperation;
import org.eclipse.jst.j2ee.applicationclient.componentcore.util.AppClientArtifactEdit;
import org.eclipse.jst.j2ee.componentcore.util.EARArtifactEdit;
import org.eclipse.jst.j2ee.ejb.EJBJar;
import org.eclipse.jst.j2ee.ejb.EJBResource;
import org.eclipse.jst.j2ee.ejb.EnterpriseBean;
import org.eclipse.jst.j2ee.ejb.Session;
import org.eclipse.jst.j2ee.ejb.SessionType;
import org.eclipse.jst.j2ee.ejb.componentcore.util.EJBArtifactEdit;
import org.eclipse.jst.j2ee.internal.J2EEVersionConstants;
import org.eclipse.jst.j2ee.internal.J2EEVersionUtil;
import org.eclipse.jst.j2ee.internal.earcreation.AddModuleToEARProjectCommand;
import org.eclipse.jst.j2ee.internal.earcreation.EARNatureRuntime;
import org.eclipse.jst.j2ee.internal.earcreation.IEARNatureConstants;
import org.eclipse.jst.j2ee.internal.ejb.project.EJBNatureRuntime;
import org.eclipse.jst.j2ee.internal.project.IEJBNatureConstants;
import org.eclipse.jst.j2ee.internal.project.J2EENature;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.jst.j2ee.internal.web.operations.J2EEWebNatureRuntime;
import org.eclipse.jst.j2ee.web.componentcore.util.WebArtifactEdit;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.command.internal.env.eclipse.EclipseLog;
import org.eclipse.wst.command.internal.provisional.env.core.common.Log;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.ModuleCoreNature;
import org.eclipse.wst.common.componentcore.internal.StructureEdit;
import org.eclipse.wst.common.componentcore.internal.WorkbenchComponent;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IFlexibleProject;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.componentcore.resources.IVirtualReference;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerUtil;
/**
 * This class contains some useful J2EE utilities for Web services internal use.
 */
public final class J2EEUtils {
	
	public final static int WEB = 1;
	public final static int EJB = 2;
	public final static int APPCLIENT = 4;
	public final static int EAR = 8;	
	
	/**
	 * Returns an IVirtualComponent
	 * @param project
	 * @param componentName
	 * @return
	 */
	public static IVirtualComponent getVirtualComponent(IProject project, String componentName){
		return ComponentCore.createComponent(project, componentName);
	}
	
	/**
	 * 
	 * @param projectName
	 * @param componentName
	 * @return
	 */
	public static IVirtualComponent getVirtualComponent(String projectName, String componentName){
		IProject project = null;
		if (projectName!=null && projectName.length() > 0 )
      project = ProjectUtilities.getProject(projectName);
		
		return ComponentCore.createComponent(project, componentName);
	}
	
	/**
	 * Returns the J2EE version id (defined in J2EEVersionConstants) of the
	 * project. If the project does not have a J2EENature, -1 is returned.
	 * 
	 * @param p
	 * @return the J2EE version id (defined in J2EEVersionConstants), -1 if p
	 *         does not have a J2EENature.
	 * 
	 * @deprecated use getJ2EEVersion(IProject, String)
	 */
	public static int getJ2EEVersion(IProject p) {
	  	J2EENature nature = J2EENature.getRegisteredRuntime(p);
	  	if (nature != null)
	  	{
	  		return nature.getJ2EEVersion();
	  	}
	  	return -1;
	}

	/**
	 * Returns the J2EE version
	 * @param p project
	 * @param component name
	 * @return int
	 */
	public static int getJ2EEVersion(IProject p, String componentName){
		int j2eeVer = -1;
		try {
          IVirtualComponent vc = ComponentCore.createComponent(p, componentName);
          if (vc!=null){
            j2eeVer = getJ2EEVersion(vc);
          }
		}
		catch (Exception e){
			//handle exception
		}

		return j2eeVer;	
	}
	
	public static int getJ2EEVersion(IVirtualComponent ch){
      int j2eeVer = -1;
      //check type
      if (ch!=null) {
        if (isWebComponent(ch))
          j2eeVer = getWebComponentJ2EEVersion(ch);
        if (isAppClientComponent(ch))
          j2eeVer = getAppClientComponentJ2EEVersion(ch);
        if (isEJBComponent(ch))
          j2eeVer = getEJBComponentJ2EEVersion(ch);
        if (isEARComponent(ch))
          j2eeVer = getEARComponentJ2EEVersion(ch);
        
      }
      return j2eeVer; 
	}
	
	/**
	 * Return's the EAR module's J2EEVersion
	 * @param wbc
	 * @return
	 */
	private static int getEARComponentJ2EEVersion(IVirtualComponent ch){
		EARArtifactEdit edit = null;
		int nVersion = 12;
		try {
			edit = EARArtifactEdit.getEARArtifactEditForRead(ch);
			if (edit != null) {
				nVersion = edit.getJ2EEVersion();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (edit != null)
				edit.dispose();
		}
		return nVersion;		
	}
	
	
	/**
	 * 
	 * @param p
	 * @return
	 * @deprecated
	 */
	public static String getJ2EEVersionAsString(IProject p){
		int j2eeVer = getJ2EEVersion(p);
		if (j2eeVer!=-1){
			return String.valueOf(j2eeVer);
		}
		else 
			return null;
	}
	
	/**
	 * Returns the J2EEVersion
	 * @param p IProject
	 * @param compName
	 * @return String
	 */
	public static String getJ2EEVersionAsString(IProject p, String compName){
		int j2eeVer = getJ2EEVersion(p, compName);
		if (j2eeVer!=-1){
			return J2EEVersionUtil.getJ2EETextVersion(j2eeVer);
		}
		else 
			return null;
	}
	
	/**
	 * Returns the Web Module's J2EE version
	 * @param wbModule
	 * @return the J2EE version id
	 */
	private static int getWebComponentJ2EEVersion(IVirtualComponent ch) {
		WebArtifactEdit webEdit = null;
		int nVersion = 12;
		try {
          webEdit = WebArtifactEdit.getWebArtifactEditForRead(ch);
          if (webEdit != null) {
			nVersion = webEdit.getJ2EEVersion();
          }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (webEdit != null)
				webEdit.dispose();
		}
		return nVersion;
	}

	/**
	 * Returns Application client's J2EE version
	 * @param wbc
	 * @return
	 */
	private static int getAppClientComponentJ2EEVersion(IVirtualComponent ch){
		AppClientArtifactEdit edit = null;
		int nVersion = 12;
		try {
      edit = AppClientArtifactEdit.getAppClientArtifactEditForRead(ch);
			if (edit != null) {
				nVersion = edit.getJ2EEVersion();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (edit != null)
				edit.dispose();
		}
		return nVersion;		
	}
	
	/**
	 * Returns EJB component's J2EE version
	 * @param wbc
	 * @return
	 */
	private static int getEJBComponentJ2EEVersion(IVirtualComponent ch){
		EJBArtifactEdit edit = null;
		int nVersion = 12;
		try {
      edit = EJBArtifactEdit.getEJBArtifactEditForRead(ch);
			if (edit != null) {
				nVersion = edit.getJ2EEVersion();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (edit != null)
				edit.dispose();
		}
		return nVersion;			
	}
	
	/**
	 * This method returns all of the EAR projects that reference the specified
	 * project.
	 * 
	 * @deprecated
	 */
	public static EARNatureRuntime[] getEARProjects(IProject project) {
		
		EARNatureRuntime[] ears = J2EEProjectUtilities.getReferencingEARProjects(project);
		return ears;
	}
	
	public static IVirtualComponent[] getAllComponents(){
		List v = new ArrayList();
		IProject[] projects = ResourceUtils.getWorkspaceRoot().getProjects();
		for (int i = 0; i < projects.length; i++) {
		  IFlexibleProject fp = ComponentCore.createFlexibleProject(projects[i]);
          IVirtualComponent[] vcs = fp.getComponents();
          for (int j=0;j<vcs.length;j++){
             v.add(vcs[j]);
          }
		}

		return (IVirtualComponent[])v.toArray(new IVirtualComponent[0]);
	}
	
	/**
	 * Returns all the EAR components in the workspace
	 * @return IVirtualComponent[]; empty if none
	 */
	public static IVirtualComponent[] getAllEARComponents() {
		List v = new ArrayList();
		IProject[] projects = ResourceUtils.getWorkspaceRoot().getProjects();
		for (int i = 0; i < projects.length; i++) {
			try {
				IVirtualComponent[] components = getEARComponentsFromProject(projects[i]);
				for (int j=0; j<components.length; j++) {
					if (components[j]!=null)
						v.add(components[j]);
				}
			} catch (Exception e) {
				e.printStackTrace();
				//handle exception
			}

		}
		return (IVirtualComponent[])v.toArray(new IVirtualComponent[0]);
	}
	
	public static IVirtualComponent[] getAllWebComponents(){
		List v = new ArrayList();
		IProject[] projects = ResourceUtils.getWorkspaceRoot().getProjects();
		for (int i = 0; i < projects.length; i++) {
			try {
				IVirtualComponent[] components = getWebComponents(projects[i]);
				for (int j=0; j<components.length; j++) {
					if (components[j]!=null)
						v.add(components[j]);
				}
			} catch (Exception e) {
				e.printStackTrace();
				//handle exception
			}

		}
		return (IVirtualComponent[])v.toArray(new IVirtualComponent[0]);		
	}
	
	/**
	 * Returns the EAR components in a given IProject
	 * @param project
	 * @return empty if no EAR components; must not return null
	 */
	public static IVirtualComponent[] getEARComponentsFromProject(IProject project){
		//get all components in the project
		List v = new ArrayList();		
		try {
			IFlexibleProject flex = ComponentCore.createFlexibleProject(project);
			IVirtualComponent[] components = flex.getComponents();
			for (int i=0;i<components.length;i++){
				if (isEARComponent(project, components[i].getName())){
					v.add(components[i]);
				}
			}
		}
		catch (Exception e){
			//handle exception
		}
		
		return (IVirtualComponent[])v.toArray(new IVirtualComponent[0]);
	}
	
	/**
	 * Returns Web components in a project
	 * @param project
	 * @return empty array if no web components; must not return null
	 */
	public static IVirtualComponent[] getWebComponents(IProject project){
		
		//get all components in the project
		List v = new ArrayList();
		try {
			IFlexibleProject flex = ComponentCore.createFlexibleProject(project);
			IVirtualComponent[] components = flex.getComponents();
			for (int i=0;i<components.length;i++){
				if (isWebComponent(project, components[i].getName())){
					v.add(components[i]);
				}
			}			

		}
		catch (Exception e){
			//handle exception
		}
		
		return (IVirtualComponent[])v.toArray(new IVirtualComponent[0]);
	} 
	
	public static String[] getWebComponentNames(IProject project){
		IVirtualComponent[] vcs = getWebComponents(project);
		return toComponentNamesArray(vcs);
	}
	
	public static String[] getEARComponentNames(IProject project){
		IVirtualComponent[] vcs = getEARComponentsFromProject(project);
		return toComponentNamesArray(vcs);
	}
	
	/**
	 * Returns all components of type, componentType in the project.
	 * @param project
	 * @param componentType i.e. J2EEUtils.WEB
	 * @return
	 */
	public static IVirtualComponent[] getComponentsByType(IProject project, int componentType){
		
		List v = new ArrayList();
		if ( (WEB & componentType)==WEB ){
			IVirtualComponent[] webVC = getWebComponents(project);
			addToArrayListHelper(webVC, v);
		}
		if ( (EJB & componentType)==EJB ){
			IVirtualComponent[] ejbVC = getEJBComponents(project);
			addToArrayListHelper(ejbVC, v);
		}
		if ( (APPCLIENT & componentType)==APPCLIENT ) {
			IVirtualComponent[] appClientVC = getAppClientComponents(project);
			addToArrayListHelper(appClientVC, v);
		}
		if ( (EAR & componentType)==EAR ) {
			IVirtualComponent[] earVC = getEARComponentsFromProject(project);
			addToArrayListHelper(earVC, v);
		}
		
		return (IVirtualComponent[])v.toArray(new IVirtualComponent[0]);		
	}
	
	/**
	 * Helper method for getComponentsByType
	 * @param vcs
	 * @param vect
	 */
	private static void addToArrayListHelper(IVirtualComponent[] vcs, List al){
		for (int i=0;i<vcs.length;i++){
			al.add(vcs[i]);
		}		
	}

	
	/**
	 * Essentially, returns all projects in the workspace
	 * @return
	 */
	public static IProject[] getAllFlexibleProjects(){

		List v = new ArrayList();
		try {
			IProject[] projects = ProjectUtilities.getAllProjects();
			for(int i=0; i<projects.length;i++) {
				if (ModuleCoreNature.getModuleCoreNature(projects[i])!=null){
					v.add(projects[i]);
				}
			}
		}
		catch(Exception e){
		}

		return (IProject[])v.toArray(new IProject[0]);
	}
	
	
	/**
	 * True if there exists a underlying resource backing up the component and project 
	 * @param projectName
	 * @param componentName
	 * @return
	 */
	public static boolean exists(String projectName, String componentName){
		IProject project = null;
		if (projectName!=null && projectName.length() > 0 )
		  project = FileResourceUtils.getWorkspaceRoot().getProject(projectName);
		else 
			return false;
		
		return exists(project, componentName);
	}
	
	/**
	 * True if there exists a underlying resource backing up the component and project
	 * @param project
	 * @param componentName
	 * @return
	 */
	public static boolean exists(IProject project, String componentName){
		if (project!=null && 
				componentName!=null && 
				componentName.length() > 0) {
			IVirtualComponent vc = ComponentCore.createComponent(project, componentName);
			return vc.exists();
		}
		else 
			return false;
		
	}
	
	/**
	 * 
	 * @param project
	 * @return
	 * 
	 * @deprecated  --  use getEARComponentNames(IProject)
	 */
	public static String[] getEARProjectNamesForWebProject(IProject project) {
		List EARNames = new ArrayList();
		if (project != null) {
			EARNatureRuntime[] ears = getEARProjects(project);
			for (int i = 0; i < ears.length; i++) {
				EARNames.add(ears[i].getProject().getName());
			}
		}
		return EARNames.isEmpty() ? null : (String[]) EARNames
				.toArray(new String[0]);
	}

	/**
	 * Returns an array of EAR components which are referenced by the component
	 * @param project
	 * @param componentName
	 * @return
	 */
	public static IVirtualComponent[] getReferencingEARComponents(IProject project, String componentName){
		List ears = new ArrayList();
		try{
			IVirtualComponent targetVC = ComponentCore.createComponent(project, componentName);
			
			IVirtualComponent[] earVC = getAllEARComponents();
			for (int i=0; i<earVC.length;i++) {
				IVirtualReference[] refs = earVC[i].getReferences();
				for (int k=0;k<refs.length;k++) {
					if (targetVC.equals(refs[k].getReferencedComponent())){
						ears.add(refs[k].getEnclosingComponent());
					}
				}
			}

		}
		catch (Exception e){
		}

		return (IVirtualComponent[])ears.toArray(new IVirtualComponent[0]);
	}
	
	
	/**
	 * This method returns all of the EAR projects that reference the specified
	 * ejb project.
	 * 
	 * @deprecated  - use getEARProjects(IProject)
	 */
	public static EARNatureRuntime[] getEJBEARProjects(IProject project) {
		EARNatureRuntime[] ears = J2EEProjectUtilities.getReferencingEARProjects(project);
		return ears;
	}

	/**
	 * 
	 * @param project
	 * @return
	 * @deprecated use getEARProjectNames (not used; to be deleted)
	 */
	public static String[] getEARProjectNamesForEJBProject(IProject project) {
		List EARNames = new ArrayList();
		if (project != null) {
			EARNatureRuntime[] ears = getEJBEARProjects(project);
			for (int i = 0; i < ears.length; i++) {
				EARNames.add(ears[i].getProject().getName());
			}
		}
		return EARNames.isEmpty() ? null : (String[]) EARNames
				.toArray(new String[0]);
	}

	/**
	 * 
	 * @param project
	 * @return
	 * @deprecated not used; to be deleted
	 */
	public static EARNatureRuntime[] getAppClientEARProjects(IProject project) {
		EARNatureRuntime[] ears = J2EEProjectUtilities.getReferencingEARProjects(project);
		return ears;
	}

	/**
	 * Returns the EAR nature runtime from an EAR project
	 * 
	 * @param IProject
	 *            the EAR project
	 * @return EARNatureRuntime of the project
	 * 
	 * @deprecated not used; to be deleted
	 */
	public static EARNatureRuntime getEARNatureRuntimeFromProject(
			IProject project) {
		return EARNatureRuntime.getRuntime(project);
	}

	/**
	 * This method returns a list of EAR names that are referenced by the
	 * specified web project.
	 * 
	 * @deprecated not used; to be deleted
	 */
	public static String[] getEARNames(IProject project) {
		EARNatureRuntime[] ears = getEARProjects(project);
		String[] earNames = new String[ears == null ? 0 : ears.length];

		for (int index = 0; index < earNames.length; index++) {
			earNames[index] = ears[index].getProject().getName();
		}

		return earNames;
	}

	/**
	 * Find all EJB projects for a particular EAR Nature.
	 * 
	 * @return a Vector of EJBNatureRuntimes.
	 * @deprecated use getReferencingEJBComponents(IProject, String)
	 */
	public static Vector getEJBProjects(EARNatureRuntime ear) {
		Vector ejbs = new Vector();
		Iterator earProjects = ear.getModuleProjects().values().iterator();

		while (earProjects.hasNext()) {
			Object object = earProjects.next();

			if (object != null) {
				J2EENature j2eeNature = (J2EENature) object;

				if (j2eeNature instanceof EJBNatureRuntime) {
					ejbs.add(j2eeNature);
				}
			}
		}

		return ejbs;
	}
	
	/**
	 * Returns the EJB Components referenced by the ear
	 * @param project
	 * @param componentName
	 * @return
	 */
	public static IVirtualComponent[] getReferencingEJBComponentsFromEAR(IProject project, String earComponentName){

		List ejbComps = new ArrayList();
		try{
			IVirtualComponent vc = ComponentCore.createComponent(project, earComponentName);
			IVirtualReference[] refs = vc.getReferences();
			for (int i=0;i<refs.length;i++){
				if (isEJBComponent(refs[i].getReferencedComponent())){
					ejbComps.add(refs[i].getReferencedComponent());
				}
			}
		}
		catch(Exception e){
		}
		
		return (IVirtualComponent[])ejbComps.toArray(new IVirtualComponent[0]);		
	}

	/**
	 * Find all Web projects for a particular EAR Nature.
	 * 
	 * @return a vector of J2EEWebNatureRuntimes.
	 * 
	 * @deprecated use getWebProjectsFromEAR(IProject, String)
	 * 			!! not used; to be deleted
	 */
	public static Vector getWebProjects(EARNatureRuntime ear) {
		Vector webProjects = new Vector();
		Iterator earProjects = ear.getModuleProjects().values().iterator();

		while (earProjects.hasNext()) {
			Object object = earProjects.next();

			if (object != null) {
				J2EENature j2eeNature = (J2EENature) object;

				if (j2eeNature instanceof J2EEWebNatureRuntime) {
					webProjects.add(j2eeNature);
				}
			}
		}

		return webProjects;
	}


	/**
	 * @return returns a list of projects names for a given ear.
	 * @deprecated use getEJBProjectFromEAR(IProject, String)
	 * 	!! not used; to be deleted
	 */
	public static String[] getEJBProjectNames(EARNatureRuntime ear) {
		Vector ejbNatures = getEJBProjects(ear);
		String[] ejbProjectNames = new String[ejbNatures.size()];

		for (int index = 0; index < ejbProjectNames.length; index++) {
			ejbProjectNames[index] = ((EJBNatureRuntime) (ejbNatures
					.elementAt(index))).getProject().getName();
		}

		return ejbProjectNames;
	}


	
	/**
	 * 
	 * @param jar
	 * @return  Vector of bean String names.
	 */
	public static Vector getBeanNames(EJBJar jar) {
		// We currently only support Stateless session beans.
		// List cmpBeans = jar.getBeanManagedBeans();
		// List bmpBeans = jar.getContainerManagedBeans();
		List sessionBeans = jar.getSessionBeans();

		Vector names = new Vector();

		// getBeanNames( names, cmpBeans );
		// getBeanNames( names, bmpBeans );
		getBeanNames(names, sessionBeans);

		return names;
	}

	/**
	 * @param names
	 *            specifies that vector of strings that will be used to add bean
	 *            names to.
	 * @param beans
	 *            specifies a list of beans.
	 */
	private static void getBeanNames(Vector names, List beans) {
		Iterator iterator = beans.iterator();

		while (iterator.hasNext()) {
			EnterpriseBean bean = (EnterpriseBean) (iterator.next());

			if (bean.isSession()) {
				Session sessionBean = (Session) bean;

				if (sessionBean.getSessionType().getValue() == SessionType.STATELESS) {
					names.add(bean.getName());
				}
			}
		}
	}

	/**
	 * Uses jem ProjectUtilities to get the project
	 * @param ejb eObject
	 * @return IProject
	 */
	public static IProject getProjectFromEJB(EnterpriseBean ejb) {
		return ProjectUtilities.getProject(ejb);
	}

	/**
	 * Get an array of IProject, given a Vector of J2EENature's
	 * 
	 * @deprecated pls request a new method if necessary
	 */
	public static IProject[] getIProjectsFromJ2EENatures(Vector j2eenatureVector) {
		IProject[] projects = new IProject[j2eenatureVector == null
				? 0
				: j2eenatureVector.size()];
		Enumeration e = j2eenatureVector.elements();
		int i = 0;
		while (e.hasMoreElements()) {
			J2EENature nature = (J2EENature) e.nextElement();
			IProject project = nature.getProject();
			projects[i] = project;
			i++;
		}

		return projects;
	}
	
	/**
	 * Get a J2EE 1.2 EAR Project. Returns null if no J2EE 1.2 EAR Projects
	 * exist
	 * 
	 * @deprecated  use getDefault12EARProject()
	 */
	public static EARNatureRuntime get12EAR() {
		try {
			IProject[] allEARs = getEARProjects();
			for (int i = 0; i < allEARs.length; i++) {
				// return the first 1.2 EAR encountered
				EARNatureRuntime thisEAR = (EARNatureRuntime) (allEARs[i]
						.getNature(IEARNatureConstants.NATURE_ID));
				if (thisEAR.getJ2EEVersion() == J2EEVersionConstants.J2EE_1_2_ID) {
					return thisEAR;
				}
			}
		} catch (CoreException ce) {
			// handle exception
		}
		return null;

	}
	
	/**
	 *  Returns the first j2ee 1.2 EAR project in the workspace
	 * @return null if no 1.2 EAR projects
	 * 
	 * @deprecated -  if not used; to be deleted
	 */
	public static IProject getDefault12EARProject(){
		try{
			IProject[] allEARs = ResourceUtils.getWorkspaceRoot().getProjects(); // getEARProjects();
			for (int i=0;i<allEARs.length;i++){
				// return the first 1.2 EAR project
				IProject ear = allEARs[i];
				if (getJ2EEVersionAsString(ear).equals(IModuleConstants.J2EE_VERSION_1_2)){
					return ear;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			//handle exception
		}
		return null;
	}
	
		
	/**
	 * Get a J2EE 1.3 EAR Project. Returns null if no J2EE 1.3 EAR Projects
	 * exist
	 * 
	 * @deprecated use getDefault13EARComponent()
	 * 
	 */
	public static EARNatureRuntime get13EAR() {
		try {
			IProject[] allEARs = getEARProjects();
			for (int i = 0; i < allEARs.length; i++) {
				// return the first 1.3 EAR encountered
				EARNatureRuntime thisEAR = (EARNatureRuntime) (allEARs[i]
						.getNature(IEARNatureConstants.NATURE_ID));
				if (thisEAR.getJ2EEVersion() == J2EEVersionConstants.J2EE_1_3_ID) {
					return thisEAR;
				}
			}
		} catch (CoreException ce) {
			return null;
		}
		return null;

	}

	/**
	 * Returns a default J2EE 1.3 EAR component in the workspace
	 * @return EAR component name
	 */
	public static IVirtualComponent getDefault13EARComponent(){
		IVirtualComponent[] allEARComponents = getAllEARComponents();
		for (int j=0;j<allEARComponents.length;j++){
			int j2eeVersion = getJ2EEVersion(allEARComponents[j]);
			if (j2eeVersion == J2EEVersionConstants.J2EE_1_3_ID)
				 return allEARComponents[j];
		 }	
		return null;
	}
	
	/**
	 * Returns a default J2EE 1.4 EAR component in the workspace
	 * @return EAR component name
	 */
	public static IVirtualComponent getDefault14EARComponent(){
		IVirtualComponent[] allEARComponents = getAllEARComponents();
		for (int j=0;j<allEARComponents.length;j++){
			int j2eeVersion = getJ2EEVersion(allEARComponents[j]);
			if (j2eeVersion == J2EEVersionConstants.J2EE_1_4_ID)
				 return allEARComponents[j];
		 }	
		return null;
	}
		
	/**
	 * 
	 * @param versionId
	 * @return
	 * 
	 * @deprecated use getEARProjectOfVersion(int)
	 */
	public static EARNatureRuntime getEAR(int versionId) {
		try {
			IProject[] allEARs = getEARProjects();
			for (int i = 0; i < allEARs.length; i++) {
				EARNatureRuntime thisEAR = (EARNatureRuntime) (allEARs[i]
						.getNature(IEARNatureConstants.NATURE_ID));
				if (thisEAR.getJ2EEVersion() == versionId) {
					return thisEAR;
				}
			}
		} catch (CoreException ce) {
			// handle exception
		}
		return null;
	}
	
	/**
	 * Returns the first EAR component of the J2EE version
	 * @param versionId
	 * @return
	 */
	public static IVirtualComponent getEARComponentofJ2EEVersion(int versionId){
		IVirtualComponent[] components = getAllEARComponents();
		for (int i=0; i<components.length; i++){
			if (getJ2EEVersion(components[i]) == versionId)
				return components[i];
		}
		return null;
	}
	
	/**
	 * Returns the first EAR project of a given version id
	 * @param versionId
	 * @return
	 * @deprecated // use getEARComponentofJ2EEVersion
	 */
//	public static IProject getEARProjectOfVersion(int versionId){
//		try {
//			IProject[] allEARs = getEARProjects();
//			for (int i = 0; i < allEARs.length; i++) {
//				IProject ear = allEARs[i];
//				if (getJ2EEVersion(ear) == versionId) {
//					return ear;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;		
//	}


	/**
	 * 
	 * @param j2eeVersionInt
	 * @return
	 */
	public static String getLabelFromJ2EEVersion(String j2eeVersionInt) {
		if (j2eeVersionInt == null || j2eeVersionInt.length() == 0)
			return "";

		int j2eeVersion = Integer.parseInt(j2eeVersionInt);
		switch (j2eeVersion) {
			case J2EEVersionConstants.J2EE_1_2_ID :
				return J2EEVersionConstants.VERSION_1_2_TEXT;
			case J2EEVersionConstants.J2EE_1_3_ID :
				return J2EEVersionConstants.VERSION_1_3_TEXT;
			case J2EEVersionConstants.J2EE_1_4_ID :
				return J2EEVersionConstants.VERSION_1_4_TEXT;
			default :
				return "";
		}
	}

	public static String getJ2EEVersionFromLabel(String j2eeLabel) {
		String j2ee12String = String.valueOf(J2EEVersionConstants.J2EE_1_2_ID);
		String j2ee13String = String.valueOf(J2EEVersionConstants.J2EE_1_3_ID);
		String j2ee14String = String.valueOf(J2EEVersionConstants.J2EE_1_4_ID);
		if (j2eeLabel.equals(J2EEVersionConstants.VERSION_1_2_TEXT))
			return j2ee12String;

		if (j2eeLabel.equals(J2EEVersionConstants.VERSION_1_3_TEXT))
			return j2ee13String;

		if (j2eeLabel.equals(J2EEVersionConstants.VERSION_1_4_TEXT))
			return j2ee14String;

		return "";

	}
	
	/**
	 * Returns String representations of J2EE versions
	 * @param aVersion
	 * @return
	 */
	public static String getJ2EEIntVersionAsString(String aVersion) {
		if (aVersion.equals(J2EEVersionConstants.VERSION_1_2_TEXT))
			return new Integer(J2EEVersionConstants.J2EE_1_2_ID).toString();
		if (aVersion.equals(J2EEVersionConstants.VERSION_1_3_TEXT))
			return new Integer(J2EEVersionConstants.J2EE_1_3_ID).toString();
		if (aVersion.equals(J2EEVersionConstants.VERSION_1_4_TEXT))
			return new Integer(J2EEVersionConstants.J2EE_1_4_ID).toString();
		// default
		return new Integer(J2EEVersionConstants.J2EE_1_4_ID).toString();
	}	

	// ----------------------------------------------------------------------

	/**
	 * Return all the ear projects in which this project is a nested module;
	 * 
	 * @param project
	 *            The project
	 * @return EARs EAR projects, possibly null
	 * 
	 * @deprecated
	 *   This method has too much complexity; to be simplified
	 */
	public static EARNatureRuntime[] getEARProjects(IProject serviceProject,
			IServer server) {

	
		EARNatureRuntime[] earProjects = null;
		EARNatureRuntime ear = null;
		IProject earProject = null;

		if (serviceProject != null && serviceProject.exists()) {
			try {

				EARNatureRuntime[] ears = null;
				boolean isWebEJBOrAppClient = ResourceUtils.isWebProject(serviceProject) //serviceProject.hasNature(IWebNatureConstants.J2EE_NATURE_ID)
						||  ResourceUtils.isEJBProject(serviceProject) //serviceProject.hasNature(IEJBNatureConstants.NATURE_ID)
						||  ResourceUtils.isAppClientProject(serviceProject);//serviceProject.hasNature(IApplicationClientNatureConstants.NATURE_ID);
				if (!isWebEJBOrAppClient) {
					return null;
				}

				ears = J2EEProjectUtilities.getReferencingEARProjects(serviceProject);

				// separate EARs which are already deployed to the existing
				// server
				if (ears != null && ears.length >= 1) {
					ArrayList preferredEARList = new ArrayList();
					ArrayList secondaryEARList = new ArrayList();
					for (int i = 0; i < ears.length; i++) {
						ear = ears[i];
						earProject = ear.getProject();
						IModule module = ResourceUtils.getModule(earProject);
						if (module != null) {
							if (server != null
									|| ServerUtil.containsModule(server,
											module, new NullProgressMonitor())) {
								preferredEARList.add(ear);
							} else {
								secondaryEARList.add(ear);
							}
						}
					}
					// add secondaryEARList items to end of primary list
					for (int j = 0; j < secondaryEARList.size(); j++) {
						preferredEARList.add(secondaryEARList.get(j));
					}
					// toArray
					if (preferredEARList != null) {
						earProjects = (EARNatureRuntime[]) preferredEARList
								.toArray(new EARNatureRuntime[0]);
					}
				}
			} catch (Exception ce) {
				Log log = new EclipseLog();
				log.log(Log.ERROR, 5039, J2EEUtils.class, "getEARProjects", ce);

			}
		}
		return earProjects;
	}

	/**
	 * 
	 * @return
	 * 
	 * @deprecated  // use getALLEARComponents
	 */
	public static IProject[] getEARProjects() {
		Vector v = new Vector();
		IProject[] projects = ResourceUtils.getWorkspaceRoot().getProjects();
		for (int i = 0; i < projects.length; i++) {
			try {
//				if (projects[i].hasNature(IEARNatureConstants.NATURE_ID)) {
				if (ResourceUtils.isEARProject(projects[i])) {
					v.add(projects[i]);
				}
			} catch (Exception e) {
				e.printStackTrace();
				//handle exception
			}

		}

		return (IProject[])v.toArray(new IProject[0]);
	}
	
	
	/**
	 * Returns the first EAR project associated with the project and server
	 * @param serviceProject
	 * @param server
	 * @return
	 * 
	 * @deprecated  // to be simplified
	 */
	public static IProject getDefaultEARProject(IProject serviceProject, IServer server) {

		IProject[] earProjects = null;
		IProject ear = null;

		if (serviceProject != null && serviceProject.exists()) {
			try {

				boolean isWebEJBOrAppClient = ResourceUtils.isWebProject(serviceProject) //serviceProject.hasNature(IWebNatureConstants.J2EE_NATURE_ID)
						||  ResourceUtils.isEJBProject(serviceProject) //serviceProject.hasNature(IEJBNatureConstants.NATURE_ID)
						||  ResourceUtils.isAppClientProject(serviceProject);//serviceProject.hasNature(IApplicationClientNatureConstants.NATURE_ID);
				if (!isWebEJBOrAppClient) {
					return null;
				}

				IProject[] ears = getEARProjects();

				// separate EARs which are already deployed to the existing
				// server
				if (ears != null && ears.length >= 1) {
					ArrayList preferredEARList = new ArrayList();
					ArrayList secondaryEARList = new ArrayList();
					for (int i = 0; i < ears.length; i++) {
						ear = ears[i];
						IModule module = ResourceUtils.getModule(ear);
						if (module != null) {
							if (server != null
									|| ServerUtil.containsModule(server,
											module, new NullProgressMonitor())) {
								preferredEARList.add(ear);
							} else {
								secondaryEARList.add(ear);
							}
						}
					}
					// add secondaryEARList items to end of primary list
					for (int j = 0; j < secondaryEARList.size(); j++) {
						preferredEARList.add(secondaryEARList.get(j));
					}
					// toArray
					if (preferredEARList != null) {
						earProjects = (IProject[]) preferredEARList.toArray(new IProject[0]);
					}
				}
			} catch (Exception ce) {
				Log log = new EclipseLog();
				log.log(Log.ERROR, 5039, J2EEUtils.class, "getEARProjects", ce);

			}
		}
		return earProjects[0];
	}
	
	/**
	 * Returns EJB projects in the ears
	 * 
	 * @param earProjects
	 * @return projects EJB projects
	 * 
	 * @deprecated use getEJB20ComponentsFromEars
	 */
	public static IProject[] getEJB2_0ProjectsFromEARS(EARNatureRuntime[] earProjects) {
		if (earProjects == null)
			return null;

		ArrayList ejbProjects = new ArrayList();
		for (int i = 0; i < earProjects.length; i++) {
			if (earProjects[i] instanceof EARNatureRuntime) {
				EARNatureRuntime ear = (EARNatureRuntime) earProjects[i];
				Map projectsInEAR = ear.getModuleProjects();
				if (projectsInEAR != null && !projectsInEAR.isEmpty()) {
					Iterator iter = projectsInEAR.values().iterator();
					while (iter.hasNext()) {
						Object MOFObject = iter.next();
						if (MOFObject instanceof EJBNatureRuntime) {
							if (((EJBNatureRuntime) MOFObject)
									.getModuleVersion() >= J2EEVersionConstants.EJB_2_0_ID) {
								IProject project = ((EJBNatureRuntime) MOFObject)
										.getProject();
								if (project != null) {
									ejbProjects.add(project);
								}
							}
						}
					}
				}
			}
		} // end for earProjects loop

		return (IProject[]) ejbProjects.toArray(new IProject[0]);
	}
	
	/**
	 * Returns EJB projects in the ears
	 * 
	 * @param earProjects
	 * @return projects EJB projects
	 * 
	 * @deprecated
	 */
	public static IProject[] getEJBProjectsFromEARS(
			EARNatureRuntime[] earProjects) {
		if (earProjects == null)
			return null;

		ArrayList ejbProjects = new ArrayList();
		for (int i = 0; i < earProjects.length; i++) {
			if (earProjects[i] instanceof EARNatureRuntime) {
				EARNatureRuntime ear = (EARNatureRuntime) earProjects[i];
				Map projectsInEAR = ear.getModuleProjects();
				if (projectsInEAR != null && !projectsInEAR.isEmpty()) {
					Iterator iter = projectsInEAR.values().iterator();
					while (iter.hasNext()) {
						Object MOFObject = iter.next();
						if (MOFObject instanceof EJBNatureRuntime) {

							IProject project = ((EJBNatureRuntime) MOFObject)
									.getProject();
							if (project != null) {
								ejbProjects.add(project);
							}

						}
					}
				}
			}
		} // end for earProjects loop

		return (IProject[]) ejbProjects.toArray(new IProject[0]);
	}

	/**
	 * Gets the EJB Components in the project
	 * @param project
	 * @return
	 */
	public static IVirtualComponent[] getEJBComponents(IProject project){
		
		//get all components in the project
		List v = new ArrayList();
		try {
			IFlexibleProject flex = ComponentCore.createFlexibleProject(project);
			IVirtualComponent[] comps = flex.getComponents();
			for (int i=0;i<comps.length;i++){
				if (isEJBComponent(comps[i]))
					v.add(comps[i]);
			}
		}
		catch (Exception e){
			//handle exception
		}
		
		return (IVirtualComponent[])v.toArray(new IVirtualComponent[0]);
	} 
	
	/**
	 * Gets the Application Client components in a project.
	 * @param project
	 * @return
	 */
	public static IVirtualComponent[] getAppClientComponents(IProject project){
		
		//get all components in the project
		List v = new ArrayList();
		try {
			IFlexibleProject flex = ComponentCore.createFlexibleProject(project);
			IVirtualComponent[] comps = flex.getComponents();
			for (int i=0;i<comps.length;i++){
				if (isAppClientComponent(comps[i]))
					v.add(comps[i]);
			}
		}
		catch (Exception e){
			//handle exception
		}
		
		return (IVirtualComponent[])v.toArray(new IVirtualComponent[0]);
	} 	
	
	/**
	 * 
	 * @param project
	 * @param earComponentName
	 * @return
	 */
	public static IVirtualComponent[] getReferencingEJB20ComponentsFromEAR(IProject project, String earComponentName){
		 
		List ejbComps = new ArrayList();
		try{
			IVirtualComponent vc = ComponentCore.createComponent(project, earComponentName);
			IVirtualReference[] refs = vc.getReferences();
			for (int i=0; i<refs.length;i++) {
				if (isEJB20Component(refs[i].getReferencedComponent()))
					ejbComps.add(refs[i].getReferencedComponent());
			}
		}
		catch (Exception e){
		}
		
		return (IVirtualComponent[])ejbComps.toArray(new IVirtualComponent[0]);		
	}
	
	
	/**
	 * Utility method to combine two IProject[]
	 * @param projectArray1
	 * @param projectArray2
	 * @return
	 */
	public static IProject[] combineProjectArrays(IProject[] projectArray1,
			IProject[] projectArray2) {

		// check if either or both arrays are null.
		if (projectArray1 == null && projectArray2 == null)
			return null;
		else if (projectArray1 != null && projectArray2 == null)
			return projectArray1;
		else if (projectArray1 == null && projectArray2 != null)
			return projectArray2;

		IProject[] combinedProjects = new IProject[projectArray1.length
				+ projectArray2.length];

		System.arraycopy(projectArray1, 0, combinedProjects, 0,
				projectArray1.length);
		if (projectArray2.length > 0) {
			System.arraycopy(projectArray2, 0, combinedProjects,
					projectArray1.length, projectArray2.length);
		}

		return combinedProjects;
	}

	/**
	 * Returns all Web projects in the ear(s)
	 * 
	 * @param earProjects
	 * @return projects Web projects
	 * @deprecated  use getReferencingWebComponentsForEar
	 */
	public static IProject[] getWebProjectsFromEARS(
			EARNatureRuntime[] earProjects) {
		if (earProjects == null)
			return null;

		ArrayList webProjects = new ArrayList();
		for (int i = 0; i < earProjects.length; i++) {
			if (earProjects[i] instanceof EARNatureRuntime) {
				EARNatureRuntime ear = (EARNatureRuntime) earProjects[i];
				Map projectsInEAR = ear.getModuleProjects();
				if (projectsInEAR != null && !projectsInEAR.isEmpty()) {
					Iterator iter = projectsInEAR.values().iterator();
					while (iter.hasNext()) {
						// IProjectNature nature =
						// iter.next().getNature(IWebNatureConstants.J2EE_NATURE_ID);
						Object MOFObject = iter.next();
						if (MOFObject instanceof J2EEWebNatureRuntime) {
							IProject project = ((J2EEWebNatureRuntime) MOFObject)
									.getProject();
							if (project != null) {
								webProjects.add(project);
							}

						}
					}
				}
			}
		} // end for earProjects loop

		return (IProject[]) webProjects.toArray(new IProject[0]);
	}

	/**
	 * 
	 * @param earComponents
	 * @return
	 */
	public static IVirtualComponent[] getReferencingWebComponentsFromEAR(IProject project, String earComponentName){
		List webComps = new ArrayList();
		try{
			IVirtualComponent vc = ComponentCore.createComponent(project, earComponentName);
			IVirtualReference[] refs = vc.getReferences();
			for (int i=0; i<refs.length;i++) {
				if (isWebComponent(refs[i].getReferencedComponent()))
					webComps.add(refs[i].getReferencedComponent());
			}			
		}
		catch(Exception e){
		}
		
		return (IVirtualComponent[])webComps.toArray(new IVirtualComponent[0]);	
	}
	
	/**
	 * 
	 * @param project
	 * @return
	 * 
	 * @deprecated // use isEJB20Component
	 */
	public static boolean isEJB2_0Project(IProject project) {

		if (ResourceUtils.isEJBProject(project)) {
			try {

				if (project.hasNature(IEJBNatureConstants.NATURE_ID)
						&& EJBNatureRuntime.getRuntime(project)
								.getModuleVersion() >= J2EEVersionConstants.EJB_2_0_ID) {
					return true;
				}
			} catch (CoreException e) {
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param ejbComponent
	 * @return
	 */
	public static boolean isEJB20Component(IVirtualComponent ejbComponent){
		return isEJB20Component(ejbComponent.getProject(), ejbComponent.getName());
	}
	
	/**
	 * 
	 * @param project
	 * @param componentName
	 * @return
	 */
	public static boolean isEJB20Component(IProject project, String componentName){
		boolean isEJB = false;
		try {
      IVirtualComponent vc = ComponentCore.createComponent(project, componentName);
		  if (EJBArtifactEdit.isValidEJBModule(vc)) {
			  EJBArtifactEdit  ejbEdit = null;
			  try {
				  ejbEdit = EJBArtifactEdit.getEJBArtifactEditForRead(vc);
				  EJBResource ejbRes = ejbEdit.getEJBJarXmiResource();
				  if (ejbRes.getModuleVersionID()== J2EEVersionConstants.EJB_2_0_ID) {
					  return true;
				  }
			  }
			  finally {
				  if (ejbEdit!=null)
					  ejbEdit.dispose();
			  }
		  }
		}
		catch(Exception ex){}
		
		return isEJB;			
	}

	/**
	 * Returns true if the given <code>project</code> is an EAR 1.2 or EAR 1.3
	 * Project.
	 * 
	 * @param project
	 *            The project.
	 * @return True if the project is an EAR 1.2 or an EAR 1.3 Project.
	 * 
	 * @deprecated // use isEARComponent
	 */
	public static boolean isEARProject(IProject project) {
		try {
			if (project.hasNature(IEARNatureConstants.NATURE_ID))
				return true;
		} catch (CoreException e) {
		}
		return false;
	}

	/**
	 * 
	 * @param module
	 * @param EAR
	 * @return
	 * 
	 * @deprecated   use isComponentAssociated
	 */
	public static boolean isEARAssociated(IProject module, IProject EAR) {

		EARNatureRuntime[] ears = getEARProjects(module);
		if (ears != null && ears.length != 0) {
			Vector EARNames = new Vector();
			for (int i = 0; i < ears.length; i++) {
				EARNames.add(ears[i].getProject().getName());
			}
			String[] earNames = (String[]) EARNames.toArray(new String[0]);
			if (Arrays.binarySearch(earNames, EAR.getName()) >= 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the component at compName is referenced by the ear at earCompName
	 * @param earProject
	 * @param earCompName
	 * @param project
	 * @param compName
	 * @return
	 */
	public static boolean isComponentAssociated(IProject earProject, String earCompName,
								IProject project, String compName) {
		IVirtualComponent vc1 = ComponentCore.createComponent(earProject, earCompName);
		IVirtualComponent vc2 = ComponentCore.createComponent(project, compName);
		return isComponentAssociated(vc1, vc2);
	}
	
	/**
	 * Checks if the component is referenced by the ear
	 * @param ear 
	 * @param component
	 * @return
	 */
	private static boolean isComponentAssociated(IVirtualComponent ear, IVirtualComponent component){
		boolean isAssociated = false;
		
		// Note: not implemented by J2EE yet.. to be used later.
		IVirtualReference[] compRefs = ear.getReferences();
		if (compRefs!= null) {
			for (int i=0;i<compRefs.length;i++){
				IVirtualReference vref = compRefs[i];
				if (component.equals(vref.getReferencedComponent()))
						isAssociated = true;
			}
		}
		return isAssociated;
	}

	/**
	 * 
	 * @param project
	 * @param componentName
	 * @param earProject
	 * @param earComponentName
	 */
	public static void associateComponentToEAR(IProject project, String componentName,
							IProject earProject, String earComponentName) {
		
        StructureEdit core = null;
        try {
            core = StructureEdit.getStructureEditForRead(project);
			if (core!=null){
	            WorkbenchComponent wc = core.findComponentByName(componentName);
	            
                //IDataModel addComponentToEARDataModel = DataModelFactory.createDataModel(new AddComponentToEnterpriseApplicationDataModelProvider());
	            AddComponentToEnterpriseApplicationDataModel addComponentToEARDataModel = new AddComponentToEnterpriseApplicationDataModel();;
	            
	            addComponentToEARDataModel.setProperty(AddComponentToEnterpriseApplicationDataModel.EAR_PROJECT_NAME, earProject.getName());
	            addComponentToEARDataModel.setProperty(AddComponentToEnterpriseApplicationDataModel.PROJECT_NAME, project.getName());               
	            addComponentToEARDataModel.setProperty(AddComponentToEnterpriseApplicationDataModel.MODULE_NAME, componentName);
	            addComponentToEARDataModel.setProperty(AddComponentToEnterpriseApplicationDataModel.EAR_MODULE_NAME, earComponentName);
	            
	            List modulesList = new ArrayList();
	            modulesList.add(wc);
	            //String ejbComponentDeployName = model.getStringProperty(EJB_COMPONENT_DEPLOY_NAME);
	            
	            addComponentToEARDataModel.setProperty(AddComponentToEnterpriseApplicationDataModel.MODULE_LIST,modulesList);
	            
	            AddComponentToEnterpriseApplicationOperation addModuleOp = new AddComponentToEnterpriseApplicationOperation(addComponentToEARDataModel);
	            addModuleOp.run(null);
            }
        } catch (Exception e) {
			  //handle InvocationTargetException
        }finally {
            if(core != null)
                core.dispose();
        }		
	}
	
	
	
	/**
	 * 
	 * @param module
	 * @param EARProject
	 * 
	 * @deprecated 
	 */
	public static void associateWebProject(IProject module, IProject EARProject) {
			String uri = module.getName() + ".war";
			String contextRoot = module.getName();
			AddModuleToEARProjectCommand amiec = new AddModuleToEARProjectCommand(
					module, EARProject, uri, contextRoot, null);
			if (amiec.canExecute())
				amiec.execute();
	}

	/**
	 * 
	 * @param ejbProject
	 * @param EARProject
	 * 
	 * @deprecated to be determined
	 */
//	public static void associateEJBProject(IProject ejbProject,
//			IProject EARProject) {
//		try {
//			String uri = ejbProject.getName() + ".jar";
//			String contextRoot = ejbProject.getName();
//			AddModuleToEARProjectCommand amiec = new AddModuleToEARProjectCommand(
//					ejbProject, EARProject, uri, contextRoot, null);
//			if (amiec.canExecute())
//				amiec.execute();
//
//		} catch (Exception e) {
//
//		}
//
//	}
	
	/**
	 * Returns the first Module's WEB-INF directory
	 * @param project
	 * @return
	 * 
	 * @deprecated  use getWebInfPath(project, compName) instead
	 */
//	public static IPath getFirstWebInfPath(IProject project){
//		IPath modulePath = null;
//		StructureEdit mc = null;
//		try {
//		  mc = StructureEdit.getStructureEditForRead(project);
//		  WorkbenchComponent[] wbcs = mc.getWorkbenchModules();
//		  
//		  if (wbcs.length!=0) {
//			WebArtifactEdit webEdit = null;
//			try {
//			  webEdit = WebArtifactEdit.getWebArtifactEditForRead(wbcs[0]);
//			  if (webEdit!=null){
//				  IPath webXMLPath = webEdit.getDeploymentDescriptorPath();
//				  modulePath = webXMLPath.removeLastSegments(1);
//				  System.out.println("WebModulePath/DDPath = "+modulePath);
//			  }
//			}
//			finally{
//				if (webEdit!=null)
//					webEdit.dispose();
//			}
//			IVirtualComponent component = ComponentCore.createComponent(project, wbcs[0].getName());
//			IVirtualFolder webInfDir = component.getFolder(new Path("/WEB-INF"));
//			modulePath = webInfDir.getWorkspaceRelativePath();
//			System.out.println("FirstWebInfPath = " +modulePath);
//		  }
//		}
//		catch(Exception ex){}
//		finally{
//			if (mc!=null)
//				mc.dispose();
//		}
//
//		return modulePath;		
//	}
	
	/**
	 * @param project
	 * @param componentName
	 * @return
	 */
	public static IPath getWebInfPath(IProject project, String componentName){
		
		IVirtualComponent component = ComponentCore.createComponent(project, componentName);
		IVirtualFolder webInfDir = component.getFolder(new Path("/WEB-INF"));
		IPath modulePath = webInfDir.getWorkspaceRelativePath();
	
		return modulePath;
	}
	
	
	/**
	 * 
	 * @param project
	 * @return
	 * 
	 */
	public static IPath getFirstWebContentPath(IProject project){
		
		IPath modulePath = null;
		try {
          IFlexibleProject fp = ComponentCore.createFlexibleProject(project);
          IVirtualComponent[] vcs = fp.getComponents();
		  if (vcs.length!=0) {
            modulePath = vcs[0].getWorkspaceRelativePath();
		  }
		}
		catch(Exception ex){}

		return modulePath;			
	}
	
	/**
	 * @param project
	 * @param componentName
	 * @return
	 */
	public static IPath getWebContentPath(IProject project, String componentName){
		IVirtualComponent component = ComponentCore.createComponent(project, componentName);
		IPath modulePath = component.getWorkspaceRelativePath();
		return modulePath;
	}
	
	/**
	 * 
	 * @param project
	 * @return
	 * 
	 */
	public static IContainer getFirstWebContentContainer(IProject project){
		IContainer container = null;
		IPath modulePath = getFirstWebContentPath(project);
		IResource res = ResourceUtils.findResource(modulePath);
		if (res!=null){
		  container = res.getParent();
		}		
		  
		return container;
	}
	
	/**
	 * @param project
	 * @param componentName
	 * @return
	 */
	public static IContainer getWebContentContainer(IProject project, String componentName){
		IContainer container = null;
		IPath modulePath = getWebContentPath(project, componentName);
		IResource res = ResourceUtils.findResource(modulePath);
		if (res instanceof IContainer){
		  container = (IContainer)res;
		}		
		return container;
	}
	
	
	/**
	 * Returns the first Module name 
	 * @param project
	 * @return
	 * 
	 */
	public static String getFirstWebModuleName(IProject project){
      String moduleName = null;
      try {
        IFlexibleProject fp = ComponentCore.createFlexibleProject(project);
        IVirtualComponent[] vcs = fp.getComponents();
        if (vcs.length!=0)
          moduleName = vcs[0].getName();
      }
      catch(Exception ex){}
  
      return moduleName;  			
	}
	

	/**
	 * True if the component is a valid Web component
	 * @param project
	 * @param componentName
	 * @return
	 */
	public static boolean isWebComponent(IProject project, String componentName) {
    IVirtualComponent vc = ComponentCore.createComponent(project, componentName);
    return isWebComponent(vc);
	}
	
	public static boolean isWebComponent(IVirtualComponent comp){
    if ( comp.getComponentTypeId().equals(IModuleConstants.JST_WEB_MODULE)){
      return true;
    }
    return false;
  }

	/**
	 * True is the component is a valid EAR component
	 * @param project
	 * @param componentName
	 * @return
	 */
	public static boolean isEARComponent(IProject project, String componentName){
    IVirtualComponent vc = ComponentCore.createComponent(project, componentName);
    return isEARComponent(vc);
	}
	
	public static boolean isEARComponent(IVirtualComponent comp){
    if (comp.getComponentTypeId().equals(IModuleConstants.JST_EAR_MODULE)){
      return true;
    }
    return false;
	}

	/**
	 * True if the component is a valid EJB component
	 * @param project
	 * @param componentName
	 * @return
	 */
	public static boolean isEJBComponent(IProject project, String componentName) {
    IVirtualComponent vc = ComponentCore.createComponent(project, componentName);
    return isEJBComponent(vc);
	}

	public static boolean isEJBComponent(IVirtualComponent comp){
    if (comp.getComponentTypeId().equals(IModuleConstants.JST_EJB_MODULE)){
      return true;
    }
    return false;
	}
	
	/**
	 * True if the component is a true Application client component
	 * @param project
	 * @param componentName
	 * @return
	 */
	public static boolean isAppClientComponent(IProject project, String componentName) {
    IVirtualComponent vc = ComponentCore.createComponent(project, componentName);
    return isAppClientComponent(vc);
	}	

	public static boolean isAppClientComponent(IVirtualComponent comp){
    if (comp.getComponentTypeId().equals(IModuleConstants.JST_APPCLIENT_MODULE)){
      return true;
    }
    return false;
	}
	
	/**
	 * True if the component is a valid Java component
	 * @param project
	 * @param componentName
	 * @return
	 */
	public static boolean isJavaComponent(IProject project, String componentName) {
    IVirtualComponent vc = ComponentCore.createComponent(project, componentName);
    return isJavaComponent(vc);
	}
	
	public static boolean isJavaComponent(IVirtualComponent comp){
    if (comp.getComponentTypeId().equals(IModuleConstants.JST_UTILITY_MODULE)){
      return true;
    }
    return false;
	}	
	
	public static String[] toComponentNamesArray(IVirtualComponent[] components){
		String[] ecNames = new String[components.length];
		for(int i=0; i<components.length; i++){
			ecNames[i] = components[i].getName();
		}
		return ecNames;		
	}
	
	public static IProject[] toProjectArray(IVirtualComponent[] components){
		IProject[] projects = new IProject[components.length];
		for (int i=0; i<components.length; i++){
			projects[i] = components[i].getProject();
		}
		return projects;
	}
}
