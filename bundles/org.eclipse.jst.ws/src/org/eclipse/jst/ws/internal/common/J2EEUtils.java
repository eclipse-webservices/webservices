/*******************************************************************************
 * Copyright (c) 2000, 2008, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060204  124143   rsinha@ca.ibm.com - Rupam Kuehner     
 * 20060329   128069 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060503   126819 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060524   131132 mahutch@ca.ibm.com - Mark Hutchinson
 * 20070723   194434 kathy@ca.ibm.com - Kathy Chan, Check for non-existing EAR with content not deleted
 * 20071218	  200193 gilberta@ca.ibm.com - Gilbert Andrews
 * 20071221   213726 kathy@ca.ibm.com - Kathy Chan
 * 20070123   216345 gilberta@ca.ibm.com - Gilbert Andrews
 * 20080212   208795 ericdp@ca.ibm.com - Eric Peters, WS wizard framework should support EJB 3.0
 * 20080229   218696 ericdp@ca.ibm.com - Eric D. Peters, APIs using EJBArtifactEdit not able to deal with some EJB 3.0 beans properly
 * 20081001   243869 ericdp@ca.ibm.com - Eric D. Peters, Web Service tools allowing mixed J2EE levels
 * 20090114   261087 ericdp@ca.ibm.com - Eric D. Peters, No way to get meta-inf path for a project
 *******************************************************************************/

package org.eclipse.jst.ws.internal.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jem.util.logger.proxy.Logger;
import org.eclipse.jst.j2ee.application.internal.operations.AddComponentToEnterpriseApplicationDataModelProvider;
import org.eclipse.jst.j2ee.commonarchivecore.internal.helpers.ArchiveManifest;
import org.eclipse.jst.j2ee.ejb.EJBJar;
import org.eclipse.jst.j2ee.ejb.EJBResource;
import org.eclipse.jst.j2ee.ejb.EnterpriseBean;
import org.eclipse.jst.j2ee.ejb.Session;
import org.eclipse.jst.j2ee.ejb.SessionType;
import org.eclipse.jst.j2ee.ejb.componentcore.util.EJBArtifactEdit;
import org.eclipse.jst.j2ee.internal.J2EEVersionConstants;
import org.eclipse.jst.j2ee.internal.common.J2EEVersionUtil;
import org.eclipse.jst.j2ee.internal.common.classpath.J2EEComponentClasspathUpdater;
import org.eclipse.jst.j2ee.internal.plugin.IJ2EEModuleConstants;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
import org.eclipse.jst.j2ee.project.EarUtilities;
import org.eclipse.jst.j2ee.project.facet.IJavaProjectMigrationDataModelProperties;
import org.eclipse.jst.j2ee.project.facet.JavaProjectMigrationDataModelProvider;
import org.eclipse.jst.javaee.ejb.EnterpriseBeans;
import org.eclipse.jst.javaee.ejb.SessionBean;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.ModuleCoreNature;
import org.eclipse.wst.common.componentcore.datamodel.properties.ICreateReferenceComponentsDataModelProperties;
import org.eclipse.wst.common.componentcore.internal.operation.CreateReferenceComponentsDataModelProvider;
import org.eclipse.wst.common.componentcore.internal.util.ComponentUtilities;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.componentcore.resources.IVirtualReference;
import org.eclipse.wst.common.environment.EnvironmentService;
import org.eclipse.wst.common.environment.ILog;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.internal.operations.ProjectCreationDataModelProviderNew;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
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
	 * @return
	 */
	public static IVirtualComponent getVirtualComponent(IProject project){
		return ComponentCore.createComponent(project);
	}
	
	/**
	 * 
	 * @param projectName
	 * @return
	 */
	public static IVirtualComponent getVirtualComponent(String projectName){
		IProject project = null;
		if (projectName!=null && projectName.length() > 0 )
      project = ProjectUtilities.getProject(projectName);
		
		return ComponentCore.createComponent(project);
	}
	

	/**
	 * Returns the J2EE version
	 * @param p project
	 * @return int
	 */
	public static int getJ2EEVersion(IProject p){
		
		String ver = J2EEProjectUtilities.getJ2EEProjectVersion(p);
		return J2EEVersionUtil.convertVersionStringToInt(ver);

		/*
		int j2eeVer = -1;
		try {
          IVirtualComponent vc = ComponentCore.createComponent(p);
          if (vc!=null){
            j2eeVer = getJ2EEVersion(vc);
          }
		}
		catch (Exception e){
			//handle exception
		}
		return j2eeVer;
		*/
	}
	
	public static int getJ2EEVersion(IVirtualComponent ch){
      int j2eeVer = -1;
      //check type
      if (ch!=null){
    	  IProject project = ch.getProject();
    	  j2eeVer = getJ2EEVersion(project);
      }
      
      /*  Use new API provided by J2EE 
      if (ch!=null) {
        if (isWebComponent(ch))
          j2eeVer = getWebComponentJ2EEVersion(ch);
        else if (isAppClientComponent(ch))
          j2eeVer = getAppClientComponentJ2EEVersion(ch);
        else if (isEJBComponent(ch))
          j2eeVer = getEJBComponentJ2EEVersion(ch);
        else if (isEARComponent(ch))
          j2eeVer = getEARComponentJ2EEVersion(ch);
      }
      */
      
      return j2eeVer; 
	}
	

	/**
	 * Returns the J2EEVersion
	 * @param p IProject
	 * @return String
	 */
	public static String getJ2EEVersionAsString(IProject p){
		
		String ver = J2EEProjectUtilities.getJ2EEProjectVersion(p);
		return  ver != null ? ver : null;
		 
		/*
		int j2eeVer = getJ2EEVersion(p);
		if (j2eeVer!=-1){
			return J2EEVersionUtil.getJ2EETextVersion(j2eeVer);
		}
		else 
			return null;
		
		*/
	}
	
	public static IVirtualComponent[] getAllComponents(){
		List v = new ArrayList();
		IProject[] projects = ResourceUtils.getWorkspaceRoot().getProjects();
		for (int i = 0; i < projects.length; i++) {
          IVirtualComponent vc = ComponentCore.createComponent(projects[i]);
          if(vc != null)
        	  v.add(vc);
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
    
    public static IVirtualComponent[] getAllEJBComponents(){
        List v = new ArrayList();
        IProject[] projects = ResourceUtils.getWorkspaceRoot().getProjects();
        for (int i = 0; i < projects.length; i++) {
            try {
                IVirtualComponent[] components = getEJBComponents(projects[i]);
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
			IVirtualComponent component = ComponentCore.createComponent(project);
			if (component != null && isEARComponent(component)){
				v.add(component);
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
			IVirtualComponent component = ComponentCore.createComponent(project);
			
				if (component != null && isWebComponent(component)){
					v.add(component);
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
	 * Returns all components of type, componentType in the project.
	 * @param project
	 * @param componentTypeId as defined in IModuleConstants.
	 * @return
	 */
	public static IVirtualComponent[] getComponentsByType(IProject project, String componentTypeId){
		
		//get all components in the project of type componentTypeId
		List v = new ArrayList();
		try {

			IVirtualComponent vc = ComponentCore.createComponent(project);	
			if ( vc != null && J2EEProjectUtilities.isProjectOfType(project,componentTypeId))
			{
				v.add(vc);
			}
			//if (isWebComponent(project, components[i].getName())){
				//v.add(components[i]);
			//}		

		}
		catch (Exception e){
			//handle exception
		}
		
		return (IVirtualComponent[])v.toArray(new IVirtualComponent[0]);
	}
	
	public static String[] getProjectsContainingComponentOfType(String componentTypeId)
	{
	    Vector v = new Vector(); 
	    IVirtualComponent[] comps = getAllComponents();
	    for (int i = 0; i < comps.length; i++) {
			if (J2EEProjectUtilities.isProjectOfType(comps[i].getProject(),componentTypeId))
			{
				//Add the project name to the vector if it has not been added already
				String name = comps[i].getProject().getName();
				if (!v.contains(name))
				{
					v.add(name);	
				}		        			
			}

        }

        return (String[])v.toArray(new String[0]);
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
		if (projectName!=null && projectName.length() > 0 ) {
		  project = ProjectUtilities.getProject(projectName);
        }
        else
          return false;
		
		return exists(project);
	}
	
	/**
	 * True if there exists a underlying resource backing up the component and project
	 * @param project
	 * @return
	 */
	public static boolean exists(IProject project){
		if (project!=null) {
            if (!project.exists())
              return false;
            
			IVirtualComponent vc = ComponentCore.createComponent(project);
			return vc.exists();
		}
		else 
			return false;
		
	}	

	/**
	 * Returns an array of EAR components which are referenced by the component
	 * @param project
	 * @return
	 */
	public static IVirtualComponent[] getReferencingEARComponents(IProject project){
		List ears = new ArrayList();
		try{
			IVirtualComponent targetVC = ComponentCore.createComponent(project);
			
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
	 * Returns the EJB Components referenced by the ear
	 * @param project
	 * @return
	 */
	public static IVirtualComponent[] getReferencingEJBComponentsFromEAR(IProject project){

		List ejbComps = new ArrayList();
		try{
			IVirtualComponent vc = ComponentCore.createComponent(project);
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
	 * 
	 * @param jar
	 * @return  Vector of bean String names.
	 * @deprecated use getBeanNames(IProject project)
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
	 * 
	 * @param project- the project to find stateless session beans in
	 * @return  Vector of bean String names.
	 */
	public static Vector getBeanNames(IProject project) {
		// We currently only support Stateless session beans.
		Vector names = new Vector();
    	IModelProvider provider = ModelProviderManager.getModelProvider(project);
    	Object modelObject = provider.getModelObject();

    	List sessionBeans = new Vector();
		boolean isJ2EE5 = J2EEProjectUtilities.isJEEProject(project);
		if (isJ2EE5) {
    		//a JEE5 project
    		EnterpriseBeans eBeans = ((org.eclipse.jst.javaee.ejb.EJBJar)modelObject).getEnterpriseBeans();
    		if (eBeans !=null)
    			sessionBeans = eBeans.getSessionBeans();
    	} else {
    		sessionBeans = ((EJBJar)modelObject).getSessionBeans();
    	}

		Iterator iterator = sessionBeans.iterator();

		while (iterator.hasNext()) {
			Object next = (iterator.next());
			if (next instanceof EnterpriseBean) {
				EnterpriseBean bean = (EnterpriseBean) next;

				if (bean.isSession()) {
					Session sessionBean = (Session) bean;

					if (sessionBean.getSessionType().getValue() == SessionType.STATELESS) {
						names.add(bean.getName());
					}
				}
			} else {
				SessionBean bean = (SessionBean) next;
				if (bean.getSessionType().getValue() == SessionType.STATELESS) {
					names.add(bean.getEjbName());
				}
			}
		}
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
		if (aVersion.equals(J2EEVersionConstants.VERSION_1_4_TEXT))
			return new Integer(J2EEVersionConstants.J2EE_1_4_ID).toString();
		if (aVersion.equals(J2EEVersionConstants.VERSION_1_3_TEXT))
			return new Integer(J2EEVersionConstants.J2EE_1_3_ID).toString();
		if (aVersion.equals(J2EEVersionConstants.VERSION_1_2_TEXT))
			return new Integer(J2EEVersionConstants.J2EE_1_2_ID).toString();		
		// default
		return new Integer(J2EEVersionConstants.J2EE_1_4_ID).toString();
	}	

	// ----------------------------------------------------------------------



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
				ILog log = EnvironmentService.getEclipseLog();
				log.log(ILog.ERROR, 5039, J2EEUtils.class, "getEARProjects", ce);

			}
		}
		return earProjects[0];
	}

	/**
	 * Gets the EJB Components in the project
	 * @param project
	 * @return
	 */
	public static IVirtualComponent[] getEJBComponents(IProject project){
		
		//get all components in the project
		List v = new ArrayList();
		IVirtualComponent component = ComponentCore.createComponent(project);
		try {
			if (component != null && isEJBComponent(component)){
				v.add(component);
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
			IVirtualComponent component = ComponentCore.createComponent(project);
			
				if (component != null && isAppClientComponent(component)){
					v.add(component);
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
	 * @return
	 */
	public static IVirtualComponent[] getReferencingEJB20ComponentsFromEAR(IProject project){
		 
		List ejbComps = new ArrayList();
		try{
			IVirtualComponent vc = ComponentCore.createComponent(project);
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
	 * 
	 * @param project
	 * @return
	 */
	public static IVirtualComponent[] getReferencingWebComponentsFromEAR(IProject project){
		List webComps = new ArrayList();
		try{
			IVirtualComponent vc = ComponentCore.createComponent(project);
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
	 * @param ejbComponent
	 * @return
	 */
	public static boolean isEJB20Component(IVirtualComponent ejbComponent){
		return isEJB20Component(ejbComponent.getProject());
	}
	
	/**
	 * 
	 * @param project
	 * @return
	 */
	public static boolean isEJB20Component(IProject project){
		boolean isEJB = false;
		try {
      IVirtualComponent vc = ComponentCore.createComponent(project);
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
	 * Checks if the component at compName is referenced by the ear at earCompName
	 * @param earProject
	 * @param project
	 * @return
	 */
	public static boolean isComponentAssociated(IProject earProject, IProject project) {
		IVirtualComponent vc1 = ComponentCore.createComponent(earProject);
		IVirtualComponent vc2 = ComponentCore.createComponent(project);
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
	 * @param earProject
	 */
	public static void associateComponentToEAR(IProject project, IProject earProject) {
		
        IDataModel addComponentToEARDataModel = DataModelFactory.createDataModel(new AddComponentToEnterpriseApplicationDataModelProvider());
		IVirtualComponent earComp = ComponentCore.createComponent(earProject);
        addComponentToEARDataModel.setProperty(ICreateReferenceComponentsDataModelProperties.SOURCE_COMPONENT, earComp);
        List modList = (List) addComponentToEARDataModel.getProperty(ICreateReferenceComponentsDataModelProperties.TARGET_COMPONENT_LIST);
        IVirtualComponent targetComp = ComponentCore.createComponent(project);
        modList.add(targetComp);
		addComponentToEARDataModel.setProperty(ICreateReferenceComponentsDataModelProperties.TARGET_COMPONENT_LIST, modList);
        
		try {
			addComponentToEARDataModel.getDefaultOperation().execute(null, null);
		} catch (ExecutionException e) {
			Logger.getLogger().log(e);
		}
            
	}
	 /**
     * Determines whether project can be associated with earProject safely.
     * @returns true if the project is a Web project, EJB project, or AppClient
     * project that can safely be associated with the EAR project. 
     * Returns false if the project is a Web project, 
     * EJB project, or AppClient project that cannot be associated safely with the EAR project, and
     * true if project is not recognized as a Web, EJB, or
     * AppClient project, and ture if earProject is not recognized as an EAR project.
     */
	public static boolean canAssociateProjectToEARWithoutWarning(IProject project, IProject earProject) {
		  boolean toReturn = true;
	      boolean isWeb = isWebComponent(project);
	      boolean isEJB = isEJBComponent(project);
	      boolean isAppClient = isAppClientComponent(project);
	      boolean isEAR = isEARComponent(earProject);
	      if ((isWeb || isEJB || isAppClient) && isEAR)
	      {        
	        try
	        {
	          IFacetedProject fProject = ProjectFacetsManager.create(project);
	          IFacetedProject fEarProject = ProjectFacetsManager.create(earProject);
	          
	          IProjectFacet earPf = ProjectFacetsManager.getProjectFacet(IJ2EEModuleConstants.JST_EAR_MODULE);
	          IProjectFacetVersion earPfv = fEarProject.getInstalledVersion(earPf);
	          String compareVersion;
	          List<IProjectFacetVersion> supportedFacetVersionList = new ArrayList<IProjectFacetVersion>();
	          if (isWeb)
	          {
	            IProjectFacet webPf = ProjectFacetsManager.getProjectFacet(IJ2EEModuleConstants.JST_WEB_MODULE);
	            IProjectFacetVersion webPfv = fProject.getInstalledVersion(webPf);
	            supportedFacetVersionList = EarUtilities.getSupportedFacets(earPfv, webPf);
	            toReturn = false;
	            for (IProjectFacetVersion supportedFacetVersion : supportedFacetVersionList ) {
	            	IProjectFacetVersion next = supportedFacetVersion;
	            	compareVersion = webPfv.getVersionString();
	            	if (next!= null && compareVersion!=null && compareVersion.compareTo(next.getVersionString()) == 0) { 
	            		toReturn = true;
	            		break;
	            	}
	            }
	            
	          }
	          else if (isEJB)            
	          {
	            IProjectFacet ejbPf = ProjectFacetsManager.getProjectFacet(IJ2EEModuleConstants.JST_EJB_MODULE);
	            IProjectFacetVersion ejbPfv = fProject.getInstalledVersion(ejbPf);
	            supportedFacetVersionList = EarUtilities.getSupportedFacets(earPfv, ejbPf);
	            toReturn = false;
	            for (IProjectFacetVersion supportedFacetVersion : supportedFacetVersionList ) {
	            	IProjectFacetVersion next = supportedFacetVersion;
	            	compareVersion = ejbPfv.getVersionString();
	            	if (next!= null && compareVersion!=null && compareVersion.compareTo(next.getVersionString()) == 0) { 
	            		toReturn = true;
	            		break;
	            	}
	            }
	            
	          }
	          else if (isAppClient)
	          {
	            IProjectFacet acPf = ProjectFacetsManager.getProjectFacet(IJ2EEModuleConstants.JST_APPCLIENT_MODULE);
	            IProjectFacetVersion acPfv = fProject.getInstalledVersion(acPf);
	            supportedFacetVersionList = EarUtilities.getSupportedFacets(earPfv, acPf);
	            toReturn = false;
	            for (IProjectFacetVersion supportedFacetVersion : supportedFacetVersionList ) {
	            	IProjectFacetVersion next = supportedFacetVersion;
	            	compareVersion = acPfv.getVersionString();
	            	if (next!= null && compareVersion!=null && compareVersion.compareTo(next.getVersionString()) == 0) { 
	            		toReturn = true;
	            		break;
	            	}
	            }

	          } 
	          
	        } catch (Exception e)
	        {
	          //not much we can do here, return what we've calculated thus far
	        }         
	      }

		
		return toReturn;
		
	}
    /**
     * Determines whether project can be associated with earProject.
     * @returns IStatus of OK if the project is a Web project, EJB project, or AppClient
     * project that can be associated with the EAR project. 
     * Returns IStatus with an ERROR severity if the project is a Web project, 
     * EJB project, or AppClient project that cannot be associated with the EAR project. 
     * When an IStatus of severity ERROR is returned, the IStatus' message will contain the 
     * lowest J2EE level that this project requires on an EAR.
     * Returns an IStatus with severity OK if project is not recognized as a Web, EJB, or
     * AppClient project or if earProject is not recognized as an EAR project.
     * @deprecated use canAssociateProjectToEARWithoutWarning(IProject project, IProject earProject)
     */
    public static IStatus canAssociateProjectToEAR(IProject project, IProject earProject)
    {
      //If project contains the web, ejb, or appclient facet, and earProject contains the 
      //ear facet, return whether or not the facet versions are such that the project
      //can be added to the ear.      

      IStatus status = Status.OK_STATUS;
      boolean isWeb = isWebComponent(project);
      boolean isEJB = isEJBComponent(project);
      boolean isAppClient = isAppClientComponent(project);
      boolean isEAR = isEARComponent(earProject);
      if ((isWeb || isEJB || isAppClient) && isEAR)
      {        
        try
        {
          IFacetedProject fProject = ProjectFacetsManager.create(project);
          IFacetedProject fEarProject = ProjectFacetsManager.create(earProject);
          
          IProjectFacet earPf = ProjectFacetsManager.getProjectFacet(IJ2EEModuleConstants.JST_EAR_MODULE);
          IProjectFacetVersion earPfv = fEarProject.getInstalledVersion(earPf);
          String earVersion = earPfv.getVersionString();
          
          String webVersion = null;
          String ejbVersion = null;
          String acVersion = null;
            
          if (isWeb)
          {
            IProjectFacet webPf = ProjectFacetsManager.getProjectFacet(IJ2EEModuleConstants.JST_WEB_MODULE);
            IProjectFacetVersion webPfv = fProject.getInstalledVersion(webPf);
            webVersion = webPfv.getVersionString();
          }
          else if (isEJB)            
          {
            IProjectFacet ejbPf = ProjectFacetsManager.getProjectFacet(IJ2EEModuleConstants.JST_EJB_MODULE);
            IProjectFacetVersion ejbPfv = fProject.getInstalledVersion(ejbPf);
            ejbVersion = ejbPfv.getVersionString();
          }
          else if (isAppClient)
          {
            IProjectFacet acPf = ProjectFacetsManager.getProjectFacet(IJ2EEModuleConstants.JST_APPCLIENT_MODULE);
            IProjectFacetVersion acPfv = fProject.getInstalledVersion(acPf);
            acVersion = acPfv.getVersionString();
          } 
          
          if ((isWeb && webVersion.equals(J2EEVersionConstants.VERSION_2_2_TEXT)) ||
              (isEJB && ejbVersion.equals(J2EEVersionConstants.VERSION_1_1_TEXT)) ||
              (isAppClient && acVersion.equals(J2EEVersionConstants.VERSION_1_2_TEXT))
              )
          {
            if (!greaterThanOrEqualTo(earVersion, J2EEVersionConstants.VERSION_1_2_TEXT))
            {
              status = StatusUtils.errorStatus(J2EEVersionConstants.VERSION_1_2_TEXT);
            }            
          }
          else if ((isWeb && webVersion.equals(J2EEVersionConstants.VERSION_2_3_TEXT)) ||
              (isEJB && ejbVersion.equals(J2EEVersionConstants.VERSION_2_0_TEXT)) ||
              (isAppClient && acVersion.equals(J2EEVersionConstants.VERSION_1_3_TEXT))
                )
            {
              if (!greaterThanOrEqualTo(earVersion, J2EEVersionConstants.VERSION_1_3_TEXT))
              {
                status = StatusUtils.errorStatus(J2EEVersionConstants.VERSION_1_3_TEXT);
              }            
            }          
          else if ((isWeb && webVersion.equals(J2EEVersionConstants.VERSION_2_4_TEXT)) ||
              (isEJB && ejbVersion.equals(J2EEVersionConstants.VERSION_2_1_TEXT)) ||
              (isAppClient && acVersion.equals(J2EEVersionConstants.VERSION_1_4_TEXT))
                )
            {
              if (!greaterThanOrEqualTo(earVersion, J2EEVersionConstants.VERSION_1_4_TEXT))
              {
                status = StatusUtils.errorStatus(J2EEVersionConstants.VERSION_1_4_TEXT);
              }            
            }                    
        } catch (CoreException ce)
        {
          //If facet API throws a CoreException when trying to create an IFacetedProject from 
          //an IProject, an OK Status will be returned and clients of this utility method
          //won't raise a validation error.
        }         
      }

      return status;
    }

	
    /*
     * @param versionA version number of the form 1.2.3
     * @param versionA version number of the form 1.2.3
     * @return boolean returns whether versionA is greater than or equal to versionB
     */
    private static boolean greaterThanOrEqualTo(String versionA, String versionB)
    {
      if (versionA.equals(versionB))
      {
        return true;
      }
      else
      {
        StringTokenizer stA = new StringTokenizer(versionA, ".");
        StringTokenizer stB = new StringTokenizer(versionB, ".");

        int sizeA = stA.countTokens();
        int sizeB = stB.countTokens();

        int size;
        if (sizeA < sizeB)
        {
          size = sizeA;
        } 
        else
          size = sizeB;

        for (int i = 0; i < size; i++)
        {
          int a = Integer.parseInt(stA.nextToken());
          int b = Integer.parseInt(stB.nextToken());
          if (a != b)
          {
            return a > b;
          }
        }

        return sizeA > sizeB;
      }
    }
	
	/**
   * @param project
   * @return
   */
	public static IPath getWebInfPath(IProject project){
		
		IVirtualComponent component = ComponentCore.createComponent(project);
		
		//should WEB-INF location be pulled in from the .component file rather than hardcoded here?
		IVirtualFolder webInfDir = component.getRootFolder().getFolder(new Path("/WEB-INF"));
		IPath modulePath = webInfDir.getWorkspaceRelativePath();
		
		if (!webInfDir.exists())
		{	
			try 
			{	
				webInfDir.create(0,null);				
			}
			catch (CoreException e)
			{}
		}		
		return modulePath;
	}

	/**
	   * @param project
	   * @return
	   */
		public static IPath getMetaInfPath(IProject project){
			
			IVirtualComponent component = ComponentCore.createComponent(project);
			
			//should META-INF location be pulled in from the .component file rather than hardcoded here?
			IVirtualFolder metaInfDir = component.getRootFolder().getFolder(new Path("/META-INF"));
			IPath modulePath = metaInfDir.getWorkspaceRelativePath();
			
			if (!metaInfDir.exists())
			{	
				try 
				{	
					metaInfDir.create(0,null);				
				}
				catch (CoreException e)
				{}
			}		
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
          IVirtualComponent vc = ComponentCore.createComponent(project);
		  if (ModuleCoreNature.isFlexibleProject(project)) {
            modulePath = vc.getRootFolder().getWorkspaceRelativePath();
		  }
		}
		catch(Exception ex){}

		return modulePath;			
	}
	
	/**
	 * @param project
	 * @return
	 */
	public static IPath getWebContentPath(IProject project){
		IVirtualComponent component = ComponentCore.createComponent(project);
		IPath modulePath = component.getRootFolder().getWorkspaceRelativePath();
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
	 * @return
	 */
	public static IContainer getWebContentContainer(IProject project){
		IContainer container = null;
		IPath modulePath = getWebContentPath(project);
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
        IVirtualComponent vc = ComponentCore.createComponent(project);
        moduleName = vc.getName();
      }
      catch(Exception ex){}
  
      return moduleName;  			
	}
	
	
	public static boolean isWebComponent(IVirtualComponent comp){
	    return J2EEProjectUtilities.isDynamicWebProject(comp.getProject());
  }
	
	public static boolean isEARComponent(IVirtualComponent comp){
	    return J2EEProjectUtilities.isEARProject(comp.getProject());
	}

	/**
	 * True if the component is a valid EJB component
	 * @param project
	 * @return
	 */
	public static boolean isEJBComponent(IProject project) {
      IVirtualComponent vc = ComponentCore.createComponent(project);
      if (vc != null)
      {
        return isEJBComponent(vc);
      }
      else
      { 
        return false;
      }
	}

	public static boolean isEJBComponent(IVirtualComponent comp){
		return J2EEProjectUtilities.isEJBProject(comp.getProject());
	}	

	public static boolean isAppClientComponent(IVirtualComponent comp){
		return J2EEProjectUtilities.isApplicationClientProject(comp.getProject());
	}
	
	public static boolean isJavaComponent(IVirtualComponent comp){
		return J2EEProjectUtilities.isUtilityProject(comp.getProject());
	}	
	
	public static String getComponentTypeId(IProject project) {
		return J2EEProjectUtilities.getJ2EEProjectType(project);
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
	
	/**
	 * Given a J2EE level, this method returns the highest supported servlet level
	 * @param j2eeLevel
	 * @return web app version
	 */
	public static Integer getServletVersionForJ2EEVersion(String j2eeLevel){
		int aVersion = Integer.valueOf(j2eeLevel).intValue();
		switch (aVersion) {
		case J2EEVersionConstants.J2EE_1_2_ID:
			return new Integer(J2EEVersionConstants.WEB_2_2_ID);

		case J2EEVersionConstants.J2EE_1_3_ID:
			return new Integer(J2EEVersionConstants.WEB_2_3_ID);

		case J2EEVersionConstants.J2EE_1_4_ID:
			return new Integer(J2EEVersionConstants.WEB_2_4_ID);
			
		default:
			return new Integer(J2EEVersionConstants.WEB_2_3_ID);
		}		
	}
	
	/**
	 * Given a J2EE level, this method returns the highest supported EJB version
	 * @param j2eeLevel
	 * @return ejb spec version
	 */
	public static Integer getEJBVersionForJ2EEVersion(String j2eeLevel){
		int aVersion = Integer.valueOf(j2eeLevel).intValue();
		switch (aVersion) {
		case J2EEVersionConstants.J2EE_1_2_ID:
			return new Integer(J2EEVersionConstants.EJB_1_1_ID);

		case J2EEVersionConstants.J2EE_1_3_ID:
			return new Integer(J2EEVersionConstants.EJB_2_0_ID);

		case J2EEVersionConstants.J2EE_1_4_ID:
			return new Integer(J2EEVersionConstants.EJB_2_1_ID);
			
		default:
			return new Integer(J2EEVersionConstants.EJB_2_0_ID);
		}			
	}

	/**
	 * True if the component is a valid Java component
	 * @param project
	 * @return
	 */
	public static boolean isJavaComponent(IProject project) {
	  IVirtualComponent vc = ComponentCore.createComponent(project);
      if (vc != null)
      {
	    return isJavaComponent(vc);
      }
      else
      {
        //check if it's a faceted project with only the Java facet.
        try
        {
          if (project.exists())
          {
            IFacetedProject fProject = ProjectFacetsManager.create(project);
            if (fProject != null)
            {
              Set facets = fProject.getProjectFacets();
              if (facets.size()==1)
              {
                IProjectFacetVersion pfv = (IProjectFacetVersion)facets.iterator().next();
                if (pfv.getProjectFacet().getId().equals(IModuleConstants.JST_JAVA))
                {
                  return true;
                }
              }                            
            }
            else
            {
              //This is not a faceted project. Check if this is a simple Java project.    
              if (ResourceUtils.isJavaProject(project))
              {
                return true;
              }        
            }
          }
        } catch (CoreException ce)
        {
          
        }                        
      }
      
      return false;
	}

	/**
	 * True if the component is a valid Web component
	 * @param project
	 * @return
	 */
	public static boolean isWebComponent(IProject project) {
	  IVirtualComponent vc = ComponentCore.createComponent(project);
      if (vc != null)
      {
        return isWebComponent(vc);  
      }
      else
      {
        return false;
      }	
	}

	/**
	 * True if the component is a true Application client component
	 * @param project
	 * @return
	 */
	public static boolean isAppClientComponent(IProject project) {
	  IVirtualComponent vc = ComponentCore.createComponent(project);
      if (vc != null)
      {
	  return isAppClientComponent(vc);
      }
      else
      {
        return false;
      }
	}

	/**
	 * True is the component is a valid EAR component
	 * @param project
	 * @return
	 */
	public static boolean isEARComponent(IProject project){
	  IVirtualComponent vc = ComponentCore.createComponent(project);
      if (vc != null)
      {
	  return isEARComponent(vc);
      }
      else
      {
        return false;
      }
	}
	
	public static IFolder getOutputContainerRoot (IVirtualComponent component) {
		return (IFolder)J2EEProjectUtilities.getOutputContainers(component.getProject())[0];
	}
	
	// 194434 - Check for non-existing EAR with contents that's not deleted previously
	public static IStatus canCreateEAR(IProject earProject)
    {
		IStatus status = Status.OK_STATUS;
		if( !earProject.exists() ){
			IPath path = ResourcesPlugin.getWorkspace().getRoot().getLocation();
			path = path.append(earProject.getName());
			status = ProjectCreationDataModelProviderNew.validateExisting(earProject.getName(), path.toOSString());
		}
		return status;
    }
	

	 public static void addJAROrModuleDependency(IProject project, String uri) throws IOException, CoreException
	  {
	    if (J2EEUtils.isWebComponent(project))
	    {
	      ArchiveManifest manifest = J2EEProjectUtilities.readManifest(project);
	      manifest.mergeClassPath(new String[]{uri});
	      J2EEProjectUtilities.writeManifest(project, manifest);
	      forceClasspathUpdate(project);
	    }
	  }
	  
	  public static void addJavaProjectAsUtilityJar(IProject javaProject, IProject earProject,IProgressMonitor monitor)
	  {
		  try {
			  IDataModel migrationdm = DataModelFactory.createDataModel(new JavaProjectMigrationDataModelProvider());
			  migrationdm.setProperty(IJavaProjectMigrationDataModelProperties.PROJECT_NAME, javaProject.getName());
			  migrationdm.setProperty(IJavaProjectMigrationDataModelProperties.ADD_TO_EAR, Boolean.FALSE);
			  migrationdm.getDefaultOperation().execute(monitor, null);
	 
	 
			  IDataModel refdm = DataModelFactory.createDataModel(new CreateReferenceComponentsDataModelProvider());
			  List targetCompList = (List) refdm.getProperty(ICreateReferenceComponentsDataModelProperties.TARGET_COMPONENT_LIST);
	 
			  IVirtualComponent targetcomponent = ComponentCore.createComponent(javaProject);
			  IVirtualComponent sourcecomponent = ComponentUtilities.getComponent(earProject.getName());
			  targetCompList.add(targetcomponent);
	 
			  refdm.setProperty(ICreateReferenceComponentsDataModelProperties.SOURCE_COMPONENT,sourcecomponent );
			  refdm.setProperty(ICreateReferenceComponentsDataModelProperties.TARGET_COMPONENT_LIST, targetCompList);
			  refdm.setProperty(ICreateReferenceComponentsDataModelProperties.TARGET_COMPONENTS_DEPLOY_PATH,  "/WEB-INF/lib");
			  refdm.getDefaultOperation().execute(monitor, null);
					  
		  }catch (Exception e) {
			  
		  }
	  }
	  
	//Forcing classpath update
	  public static void forceClasspathUpdate (IProject project) {
		  J2EEComponentClasspathUpdater classpathUpdater = J2EEComponentClasspathUpdater.getInstance();
		  Collection projCollection = Collections.singleton(project);
		  classpathUpdater.forceUpdate(projCollection, true);
		  IJobManager jm = Job.getJobManager();
		  try{
			  jm.join(J2EEComponentClasspathUpdater.MODULE_UPDATE_JOB_NAME, null);
		  }catch(Exception exc){}
	  }
	 /**
     * Determines the IJ2EEModuleConstants.JST_WEB_MODULE facet version that
     * matches the IJ2EEModuleConstants.JST_EAR_MODULE facet version
     * @returns the version String for IJ2EEModuleConstants.JST_WEB_MODULE facet, if there is problem
     * trying to determine the version, either the default version or the latest supported version
     * will be returned
     */
	public static String getWebJ2EEVersionFromEARJ2EEVersion(IProject earProject) {
		String returnVersion = EarUtilities.DYNAMIC_WEB_FACET.getDefaultVersion().getVersionString();
		try {
		returnVersion = EarUtilities.DYNAMIC_WEB_FACET.getLatestVersion().getVersionString();
	    IFacetedProject fEarProject = ProjectFacetsManager.create(earProject);
	    IProjectFacet earPf = ProjectFacetsManager.getProjectFacet(IJ2EEModuleConstants.JST_EAR_MODULE);
	    IProjectFacetVersion earPfv = fEarProject.getInstalledVersion(earPf);
	    IProjectFacet webPf = ProjectFacetsManager.getProjectFacet(IJ2EEModuleConstants.JST_WEB_MODULE);
	    returnVersion= EarUtilities.getSupportedFacets(earPfv, webPf).get(0).getVersionString();
		} catch (Exception e) {
			//nothing we can do here, return what we have calculated thus far
		}
	    return returnVersion;
	}
	 /**
     * Determines the IJ2EEModuleConstants.JST_APPCLIENT_MODULE facet version that
     * matches the IJ2EEModuleConstants.JST_EAR_MODULE facet version
     * @returns the version String for IJ2EEModuleConstants.JST_APPCLIENT_MODULE facet, if there is problem
     * trying to determine the version, either the default version or the latest supported version
     * will be returned
     */
	public static String getAppClientJ2EEVersionFromEARJ2EEVersion(IProject earProject) {
		String returnVersion = EarUtilities.APPLICATION_CLIENT_FACET.getDefaultVersion().getVersionString();
		try {
		returnVersion = EarUtilities.APPLICATION_CLIENT_FACET.getLatestVersion().getVersionString();
	    IFacetedProject fEarProject = ProjectFacetsManager.create(earProject);
	    IProjectFacet earPf = ProjectFacetsManager.getProjectFacet(IJ2EEModuleConstants.JST_EAR_MODULE);
	    IProjectFacetVersion earPfv = fEarProject.getInstalledVersion(earPf);
	    IProjectFacet acPf = ProjectFacetsManager.getProjectFacet(IJ2EEModuleConstants.JST_APPCLIENT_MODULE);
	    returnVersion= EarUtilities.getSupportedFacets(earPfv, acPf).get(0).getVersionString();
		} catch (Exception e) {
			//nothing we can do here, return what we have calculated thus far
		}
	    return returnVersion;
	}
	 /**
     * Determines the IJ2EEModuleConstants.JST_EJB_MODULE facet version that
     * matches the IJ2EEModuleConstants.JST_EAR_MODULE facet version
     * @returns the version String for IJ2EEModuleConstants.JST_EJB_MODULE facet, if there is problem
     * trying to determine the version, either the default version or the latest supported version
     * will be returned
     */
	public static String getEJBJ2EEVersionFromEARJ2EEVersion(IProject earProject) {
		String returnVersion = EarUtilities.EJB_FACET.getDefaultVersion().getVersionString();
		try {
		returnVersion = EarUtilities.EJB_FACET.getLatestVersion().getVersionString();
	    IFacetedProject fEarProject = ProjectFacetsManager.create(earProject);
	    IProjectFacet earPf = ProjectFacetsManager.getProjectFacet(IJ2EEModuleConstants.JST_EAR_MODULE);
	    IProjectFacetVersion earPfv = fEarProject.getInstalledVersion(earPf);
	    IProjectFacet ejbPf = ProjectFacetsManager.getProjectFacet(IJ2EEModuleConstants.JST_EJB_MODULE);
	    returnVersion= EarUtilities.getSupportedFacets(earPfv, ejbPf).get(0).getVersionString();
		} catch (Exception e) {
			//nothing we can do here, return what we have calculated thus far
		}
	    return returnVersion;
	}
}
