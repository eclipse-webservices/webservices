/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
import org.eclipse.emf.common.util.EList;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.j2ee.ejb.EJBJar;
import org.eclipse.jst.j2ee.ejb.EnterpriseBean;
import org.eclipse.jst.j2ee.ejb.Session;
import org.eclipse.jst.j2ee.ejb.SessionType;
import org.eclipse.jst.j2ee.internal.J2EEVersionConstants;
import org.eclipse.jst.j2ee.internal.earcreation.AddModuleToEARProjectCommand;
import org.eclipse.jst.j2ee.internal.earcreation.EARNatureRuntime;
import org.eclipse.jst.j2ee.internal.earcreation.IEARNatureConstants;
import org.eclipse.jst.j2ee.internal.ejb.project.EJBNatureRuntime;
import org.eclipse.jst.j2ee.internal.project.IEJBNatureConstants;
import org.eclipse.jst.j2ee.internal.project.J2EENature;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.jst.j2ee.internal.web.operations.J2EEWebNatureRuntime;
import org.eclipse.jst.j2ee.web.modulecore.util.WebArtifactEdit;
import org.eclipse.wst.command.env.core.common.Log;
import org.eclipse.wst.command.env.eclipse.EclipseLog;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.StructureEdit;
import org.eclipse.wst.common.componentcore.internal.WorkbenchComponent;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerUtil;
/**
 * This class contains some useful J2EE utilities.
 * 
 * @author Peter Moogk
 * @date August 23, 2001
 */
public final class J2EEUtils {
	private static final String webProjectNature = IModuleConstants.JST_WEB_MODULE; // IWebNatureConstants.J2EE_NATURE_ID;
	private static final String ejbProjectNature = IModuleConstants.JST_EJB_MODULE; // IEJBNatureConstants.NATURE_ID;
	private static final String earProjectNature = IModuleConstants.JST_EAR_MODULE; // IEARNatureConstants.NATURE_ID;

	/**
	 * Returns the J2EE version id (defined in J2EEVersionConstants) of the
	 * project. If the project does not have a J2EENature, -1 is returned.
	 * 
	 * @param p
	 * @return the J2EE version id (defined in J2EEVersionConstants), -1 if p
	 *         does not have a J2EENature.
	 * 
	 * @deprecated this method only returns the first module's j2ee version
	 */
	public static int getJ2EEVersion(IProject p) {
		int j2eeVer = -1;
		StructureEdit mc = null;
		try {
			mc = StructureEdit.getStructureEditForRead(p);
			WorkbenchComponent[] wbcs = mc.getWorkbenchModules();
			if (wbcs.length!=0) {
				j2eeVer = getWebModuleJ2EEVersion(wbcs[0]);
			}
		}
		catch (Exception e){
			//handle exception
		}
		finally{
			if (mc!=null)
				mc.dispose();
		}
		return j2eeVer;
	}

	public static String getJ2EEVersionAsString(IProject p){
		int j2eeVer = getJ2EEVersion(p);
		if (j2eeVer!=-1){
			return String.valueOf(j2eeVer);
		}
		else 
			return null;
	}
	
	/**
	 * Returns the Web Module's J2EE version
	 * @param wbModule
	 * @return the J2EE version id
	 */
	public static int getWebModuleJ2EEVersion(WorkbenchComponent wbModule) {
		WebArtifactEdit webEdit = null;
		int nVersion = 12;
		try {
			webEdit = WebArtifactEdit.getWebArtifactEditForRead(wbModule);
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
	 * This method returns all of the EAR projects that reference the specified
	 * project.
	 * 
	 * @deprecated
	 */
	public static EARNatureRuntime[] getEARProjects(IProject project) {
		
		EARNatureRuntime[] ears = J2EEProjectUtilities.getReferencingEARProjects(project);
		return ears;
	}
		
	/**
	 * 
	 * @param project
	 * @return
	 */
	public static String[] getEARProjectNamesForWebProject(IProject project) {
		Vector EARNames = new Vector();
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
	 * This method returns all of the EAR projects that reference the specified
	 * ejb project.
	 */
	public static EARNatureRuntime[] getEJBEARProjects(IProject project) {
		EARNatureRuntime[] ears = J2EEProjectUtilities.getReferencingEARProjects(project);
		return ears;
	}

	public static String[] getEARProjectNamesForEJBProject(IProject project) {
		Vector EARNames = new Vector();
		if (project != null) {
			EARNatureRuntime[] ears = getEJBEARProjects(project);
			for (int i = 0; i < ears.length; i++) {
				EARNames.add(ears[i].getProject().getName());
			}
		}
		return EARNames.isEmpty() ? null : (String[]) EARNames
				.toArray(new String[0]);
	}

	public static EARNatureRuntime[] getAppClientEARProjects(IProject project) {
		EARNatureRuntime[] ears = J2EEProjectUtilities
				.getReferencingEARProjects(project);
		return ears;
	}

	/**
	 * Returns the EAR nature runtime from an EAR project
	 * 
	 * @param IProject
	 *            the EAR project
	 * @return EARNatureRuntime of the project
	 */
	public static EARNatureRuntime getEARNatureRuntimeFromProject(
			IProject project) {
		return EARNatureRuntime.getRuntime(project);
	}

	/**
	 * This method returns a list of EAR names that are referenced by the
	 * specified web project.
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
	 * @return a vector of EJBNatureRuntimes.
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
	 * Find all Web projects for a particular EAR Nature.
	 * 
	 * @return a vector of J2EEWebNatureRuntimes.
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
	 * @return returns a Vector of bean String names.
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
	 * Get the WebProject, given the EJB
	 */
	public static IProject getProjectFromEJB(EnterpriseBean ejb) {
		return ProjectUtilities.getProject(ejb);
	}

	/**
	 * Get an array of IProject, given a Vector of J2EENature's
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
		IProject[] earProjects = new IProject[v.size()];
		v.copyInto(earProjects);
		return earProjects;
	}


	/**
	 * Get a J2EE 1.2 EAR Project. Returns null if no J2EE 1.2 EAR Projects
	 * exist
	 * 
	 * @deprecated
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

	public static IProject get12EARProject(){
		try{
			IProject[] allEARs = getEARProjects();
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
	 * @deprecated
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
	 * Get a J2EE 1.3 EAR Project. Returns null if no J2EE 1.3 EAR Projects exists
	 * @return
	 */
	public static IProject get13EARProject(){
		try{
			IProject[] allEARs = getEARProjects();
			for (int i=0;i<allEARs.length;i++){
				// return the first 1.3 EAR project
				IProject ear = allEARs[i];
				if (getJ2EEVersionAsString(ear).equals(IModuleConstants.J2EE_VERSION_1_3)){
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
	 * 
	 * @param versionId
	 * @return
	 * 
	 * @deprecated
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
	 * Returns the first EAR project of a given version id
	 * @param versionId
	 * @return
	 */
	public static IProject getEARProjectOfVersion(int versionId){
		try {
			IProject[] allEARs = getEARProjects();
			for (int i = 0; i < allEARs.length; i++) {
				IProject ear = allEARs[i];
				if (getJ2EEVersion(ear) == versionId) {
					return ear;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}

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
				System.out.println("This is not a J2EE version!!");
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

				ears = J2EEProjectUtilities
						.getReferencingEARProjects(serviceProject);

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
	 * Returns the first EAR project associated with the project and server
	 * @param serviceProject
	 * @param server
	 * @return
	 * 
	 * @deprecated
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
	 * Returns true if the given <code>project</code> is an EAR 1.2 or EAR 1.3
	 * Project.
	 * 
	 * @param project
	 *            The project.
	 * @return True if the project is an EAR 1.2 or an EAR 1.3 Project.
	 * 
	 * @deprecated
	 */
	public static boolean isEARProject(IProject project) {
		try {
			if (project.hasNature(IEARNatureConstants.NATURE_ID))
				return true;
		} catch (CoreException e) {
		}
		return false;
	}

	public static boolean isEARAssociated(IProject module, IProject EAR) {

		EARNatureRuntime[] ears = getEARProjects(module, null);
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

	public static void associateWebProject(IProject module, IProject EARProject) {
		try {

			String uri = module.getName() + ".war";
			String contextRoot = module.getName();
			AddModuleToEARProjectCommand amiec = new AddModuleToEARProjectCommand(
					module, EARProject, uri, contextRoot, null);
			if (amiec.canExecute())
				amiec.execute();
			/*
			 * EARNatureRuntime EARNature =
			 * (EARNatureRuntime)getEARNatureRuntimeFromProject(EARProject);
			 * 
			 * Application application = EARNature.getApplication(); Module mod =
			 * (Module)application.getFirstModule(module.getName());
			 * EAREditModel edm = EARNature.getEarEditModelForRead();
			 * edm.addModuleMapping(mod, EARProject); edm.releaseAccess();
			 * 
			 */
		} catch (Exception e) {

		}
	}

	public static void associateEJBProject(IProject ejbProject,
			IProject EARProject) {
		try {
			String uri = ejbProject.getName() + ".jar";
			String contextRoot = ejbProject.getName();
			AddModuleToEARProjectCommand amiec = new AddModuleToEARProjectCommand(
					ejbProject, EARProject, uri, contextRoot, null);
			if (amiec.canExecute())
				amiec.execute();

		} catch (Exception e) {

		}

	}
	
	/**
	 * Returns the first Module's WEB-INF directory
	 * @param project
	 * @return
	 */
	public static IPath getFirstWebInfPath(IProject project){
		IPath modulePath = null;
		StructureEdit mc = null;
		try {
		  mc = StructureEdit.getStructureEditForRead(project);
		  WorkbenchComponent[] wbcs = mc.getWorkbenchModules();
		  
		  if (wbcs.length!=0) {
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
			IVirtualComponent component = ComponentCore.createComponent(project, wbcs[0].getName());
			IVirtualFolder webInfDir = component.getFolder(new Path("/WEB-INF"));
			modulePath = webInfDir.getWorkspaceRelativePath();
			System.out.println("FirstWebInfPath = " +modulePath);
		  }
		}
		catch(Exception ex){}
		finally{
			if (mc!=null)
				mc.dispose();
		}

		return modulePath;		
	}
	
	public static IPath getFirstWebContentPath(IProject project){
		
		IPath modulePath = null;
		StructureEdit mc = null;
		try {
		  mc = StructureEdit.getStructureEditForRead(project);
		  WorkbenchComponent[] wbcs = mc.getWorkbenchModules();
		  if (wbcs.length!=0) {
//			WebArtifactEdit webEdit = null;
//			try {
//			  webEdit = WebArtifactEdit.getWebArtifactEditForRead(wbcs[0]);
//			  if (webEdit!=null){
//				  IPath webXMLPath = webEdit.getDeploymentDescriptorPath();
//				  modulePath = webXMLPath.removeLastSegments(2);
//				  System.out.println("WebContent Path = "+modulePath);
//			  }
//			}
//			finally{
//				if (webEdit!=null)
//					webEdit.dispose();
//			}
			IVirtualComponent component = ComponentCore.createComponent(project, wbcs[0].getName());
			modulePath = component.getWorkspaceRelativePath();
			System.out.println("FirstWebContentPath = " +modulePath);
		  }
		}
		catch(Exception ex){}
		finally{
			if (mc!=null)
				mc.dispose();
		}

		return modulePath;			
	}
	
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
	 * Returns the first Module name 
	 * @param project
	 * @return
	 */
	public static String getFirstWebModuleName(IProject project){
		String moduleName = null;
		StructureEdit mc = null;
		try {
		  mc = StructureEdit.getStructureEditForRead(project);
		  WorkbenchComponent[] wbcs = mc.getWorkbenchModules();
		  if (wbcs.length!=0) {
			  moduleName = wbcs[0].getName();
			  System.out.println("First Module name = "+moduleName);
		  }
		}
		catch(Exception ex){}
		finally{
			if (mc!=null)
				mc.dispose();
		}

		return moduleName;				
	}
	
}
