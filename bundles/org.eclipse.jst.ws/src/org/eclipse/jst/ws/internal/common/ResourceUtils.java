/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.j2ee.applicationclient.componentcore.util.AppClientArtifactEdit;
import org.eclipse.jst.j2ee.componentcore.util.EARArtifactEdit;
import org.eclipse.jst.j2ee.ejb.EnterpriseBean;
import org.eclipse.jst.j2ee.ejb.componentcore.util.EJBArtifactEdit;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.wst.command.internal.env.eclipse.EclipseLog;
import org.eclipse.wst.command.internal.provisional.env.core.common.Log;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.StructureEdit;
import org.eclipse.wst.common.componentcore.internal.WorkbenchComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.componentcore.resources.IVirtualResource;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerUtil;
import org.eclipse.wst.server.core.model.IURLProvider;

/**
 * This class contains useful methods for working with Eclipse resources.
 */
public final class ResourceUtils {

	// Keeps the IWorkspaceRoot hanging around. See getWorkspaceRoot().
	private static IWorkspaceRoot root_ = null;

	// Keeps the IWorkspace hanging around. See getWorkspace().

	private static IWorkspace workspace_ = null;

	/**
	 * As returned by {@link #getProjectType getProjectType()}, indicates that
	 * the given project has no Java or Web nature.
	 */
	public static byte PROJECT_TYPE_NONE = 0;

	/**
	 * As returned by {@link #getProjectType getProjectType()}, indicates that
	 * the given project has a Java project nature.
	 */
	public static byte PROJECT_TYPE_JAVA = 1;

	/**
	 * As returned by {@link #getProjectType getProjectType()}, indicates that
	 * the given project has a Web project nature.
	 */
	public static byte PROJECT_TYPE_WEB = 2;

	/**
	 * As returned by {@link #getProjectType getProjectType()}, indicates that
	 * the given project has an EJB project nature.
	 */
	public static byte PROJECT_TYPE_EJB = 4;
	
	/**
	 * As returned by {@link #getProjectType getProjectType()}, indicates that
	 * the given project has an Application client project nature.
	 */	
	public static byte PROJECT_TYPE_APPCLIENT = 8;

	/**
	 * The SOAP rpcrouter servlet extension to be added to web project URL
	 */
	public static String SERVLET_EXT = "/servlet/rpcrouter";

	private static final String DEFAULT_CLIENT_WEB_PROJECT_EXT = "Client";
    private static final String DEFAULT_CLIENT_EJB_PROJECT_EXT = "EJBClient";
	private static final String DEFAULT_EJB_PROJECT_NAME = "WebServiceEJBProject";
    private static final String DEFAULT_EJB_COMPONENT_NAME = "WebServiceEJB";
	private static final String DEFAULT_WEB_PROJECT_NAME = "WebServiceProject";
    private static final String DEFAULT_WEB_COMPONENT_NAME = "WebServiceWeb";
	private static final String DEFAULT_ROUTER_PROJECT_EXT = "Router";

	private static final String DEFAULT_SERVICE_EAR_PROJECT_NAME = "WebServiceEARProject";
    private static final String DEFAULT_SERVICE_EAR_COMPONENT_NAME = "WebServiceEAR";
	private static final String DEFAULT_CLIENT_EAR_PROJECT_NAME = "WebServiceClientEARProject";
    private static final String DEFAULT_CLIENT_EAR_COMPONENT_NAME = "WebServiceClientEAR";

	/**
	 * Returns the IWorkspaceRoot object.
	 * @return The IWorkspaceRoot object.
	 */
	public static IWorkspaceRoot getWorkspaceRoot() {
		if (root_ == null) {
			root_ = ResourcesPlugin.getWorkspace().getRoot();
		}
		return root_;
	}

	/**
	 * Returns the IWorkspace object.
	 * @return The IWorkspace object.
	 */
	public static IWorkspace getWorkspace() {
		if (workspace_ == null) {
			if (root_ == null) {
				root_ = ResourcesPlugin.getWorkspace().getRoot();
			}
			workspace_ = root_.getWorkspace();
		}
		return workspace_;
	}

	/**
	 * Returns an {@link org.eclipse.core.resources.IResource IResource}of the
	 * given absolute pathname or null if no such resource exists.
	 * 
	 * @param absolutePathname
	 *            The absolute path of the resource.
	 * @return The <code>IResource</code>.
	 */
	public static IResource findResource(String absolutePathname) {
		if (absolutePathname == null) {
			return null;
		}
		return findResource(new Path(absolutePathname));
	}

	/**
	 * Returns an {@link org.eclipse.core.resources.IResource IResource}of the
	 * given absolute path or null if no such resource exists.
	 * 
	 * @param absolutePath
	 *            The absolute <code>IPath</code> of the resource.
	 * @return The <code>IResource</code>.
	 */
	public static IResource findResource(IPath absolutePath) {
		if (absolutePath == null) {
			return null;
		}
		return ResourceUtils.getWorkspaceRoot().findMember(absolutePath);
	}

	/**
	 * Validates the given string as a name for a resource of the given type(s).
	 * This method obeys the contract of
	 * {@link org.eclipse.core.resources.IWorkspace#validateName IWorkspace.validateName()}.
	 * 
	 * @param segment
	 *            The path to validate.
	 * @param typeMask
	 *            The <code>IResource</code> type or types.
	 * @return The status with a value if <code>IStatus.OK</code> if the path
	 *         is valid, or with appropriate severity and message information if
	 *         the path is not valid.
	 *         
	 * @deprecated not used         
	 */
	public static IStatus validateName(String segment, int typeMask) {
		return getWorkspace().validateName(segment, typeMask);
	}

	/**
	 * Validates the given string as the path of a resource of the given
	 * type(s). This method obeys the contract of
	 * {@link org.eclipse.core.resources.IWorkspace#validatePath IWorkspace.validatePath()}.
	 * 
	 * @param path
	 *            The path to validate.
	 * @param typeMask
	 *            The <code>IResource</code> type or types.
	 * @return The status with a value if <code>IStatus.OK</code> if the path
	 *         is valid, or with appropriate severity and message information if
	 *         the path is not valid.
	 * @deprecated not used 
	 */
	public static IStatus validatePath(String path, int typeMask) {
		return getWorkspace().validatePath(path, typeMask);
	}

	/**
	 * Validates the given Java type name.
	 * 
	 * @param typeName
	 *            The Java type name.
	 * @return The status with a value of <code>IStatus.OK</code> if the name
	 *         is valid, or with appropriate severity and message information if
	 *         name is not valid. The primitive types (boolean, char, byte,
	 *         short, int, long, float, double) are valid. Arrays of valid types
	 *         are themselves valid.
	 * @deprecated use JavaConventions in the jdt core 
	 */
	public static IStatus validateJavaTypeName(String typeName) {
		//
		// Strip off the trailing array bits, if any.
		//
		int a = typeName.indexOf('[');
		if (a > 0) {
			typeName = typeName.substring(0, a);
		}
		//
		// Allow primitives.
		//
		if (isPrimitiveJavaType(typeName)) {
			return new Status(IStatus.OK, WebServicePlugin.ID, 0, "", null);
		}
		//
		// Defer to JavaConventions.
		//
		return JavaConventions.validateJavaTypeName(typeName);
	}

	/**
	 * Returns true if the given <code>typeName</code> is a Java primitive
	 * (boolean, char, byte, short, int, long, float, double).
	 * 
	 * @return True if the type name is a Java primitive.
	 * 
	 * @deprecated
	 */
	public static boolean isPrimitiveJavaType(String typeName) {
		return (typeName.equals("boolean") || typeName.equals("char")
				|| typeName.equals("byte") || typeName.equals("short")
				|| typeName.equals("int") || typeName.equals("long")
				|| typeName.equals("float") || typeName.equals("double"));
	}

	/**
	 * Returns the handle of the IProject at the beginning of the given
	 * <code>absolutePath</code>. Neither the given path nor the project need
	 * exist in the workspace. The path must be absolute, and must consist of at
	 * least one segment.
	 * 
	 * @return An IProject handle for the project at the beginning of the given
	 *         <code>absolutePath</code>, or null if the path does not
	 *         specify a project.
	 */
	public static IProject getProjectOf(IPath absolutePath) {
		if (absolutePath.isAbsolute()) {
			String projectName = absolutePath.segment(0);
			if (projectName != null) {
				return getWorkspaceRoot().getProject(projectName);
			}
		}
		return null;
	}

	/**
	 * 
	 * @param absolutePath
	 * @return
	 */
	public static IVirtualComponent getComponentOf(IPath absolutePath){
		if (absolutePath.isAbsolute()) {
			String componentName = absolutePath.segment(1);
			if (componentName != null) {
				String projectName = absolutePath.segment(0);
				IProject project = getWorkspaceRoot().getProject(projectName);
				if (projectName != null) {
					return ComponentCore.createComponent(project, componentName);
				}
			}
		}
		return null;
	}
	
	public static IVirtualComponent getComponentOf(IResource res)
	{
		IVirtualResource[] vresources = ComponentCore.createResources(res);
		IVirtualComponent vcomp = null;
		if (vresources != null && vresources.length>0)
		{
			IVirtualResource vres = vresources[0];
			vcomp = vres.getComponent();
		}
		return vcomp;
	}
	
	
	/**
	 * Returns true if the given <code>project</code> is a Java Project.
	 * 
	 * @param project
	 *            The project.
	 * @return True if the project is a Java Project.
	 */
	public static boolean isJavaProject(IProject project) {
		return (JavaCore.create(project) != null);
	}

	/**
	 * Returns true if the given <code>project</code> is a Java Project.
	 * 
	 * @param project
	 *            The project.
	 * @return True if the project is a Java Project.
	 */
	public static boolean isTrueJavaProject(IProject project) {
		return (!isWebProject(project) && !isAppClientProject(project) && !isEARProject(project)
				&& !isEJBProject(project) && isJavaProject(project));
		/*
		 * try { String[] natures = project.getDescription().getNatureIds();
		 * return (natures.length == 1 &&
		 * natures[0].equals(JavaCore.NATURE_ID)); } catch (CoreException e) { }
		 * return false;
		 */
	}

	/**
	 * Returns true if the given <code>project</code> is a Web Project.
	 * Note: For components; use J2EEUtils.isWebComponent()
	 * @param project
	 *            The project.
	 * @return True if the project is a Web Project.
   * 
   * @deprecated
	 */
	public static boolean isWebProject(IProject project) {
		boolean isWeb = false;
		StructureEdit mc = null;
		try {
		  mc = StructureEdit.getStructureEditForRead(project);
		  WorkbenchComponent[] wbcs = mc.getWorkbenchModules();
		  if (wbcs.length!=0) {
			  //isWeb = WebArtifactEdit.isValidWebModule(wbcs[0]);
		  }
		}
		catch(Exception ex){
			// handle exception
		}
		finally{
			if (mc!=null)
				mc.dispose();
		}

		return isWeb;
	}

	/**
	 * Note: for components; use J2EEUtils.isEARComponent()
	 * @param project
	 * @return
   * 
   * @deprecated
	 */
	public static boolean isEARProject(IProject project){
		boolean isEAR = false;
		StructureEdit mc = null;
		try {
		  mc = StructureEdit.getStructureEditForRead(project);
		  WorkbenchComponent[] wbcs = mc.getWorkbenchModules();
		  if (wbcs.length!=0) {
			EARArtifactEdit earEdit = null;
			try {
			  //earEdit = EARArtifactEdit.getEARArtifactEditForRead(wbcs[0]);
			  if (earEdit!=null){
				isEAR = true;
			  }
			}
			finally{
				if (earEdit!=null)
					earEdit.dispose();
			}
		  }
		}
		catch(Exception ex){}
		finally{
			if (mc!=null)
				mc.dispose();
		}
		
		return isEAR;
	}
	/**
	 * Returns true if the given <code>project</code> is an EJB 1.1 or EJB 2.0
	 * Project.
	 * Note: for components, use J2EEUtils.isEJBComponent
	 * @param project
	 *            The project.
	 * @return True if the project is an EJB 1.1 or an EJB 2.0 Project.
   * 
   * @deprecated
   * 
	 */
	public static boolean isEJBProject(IProject project) {
		boolean isEJB = false;
		StructureEdit mc = null;
		try {
		  mc = StructureEdit.getStructureEditForRead(project);
		  WorkbenchComponent[] wbcs = mc.getWorkbenchModules();
		  if (wbcs.length!=0) {
			EJBArtifactEdit ejbEdit = null;
			try {
			  //ejbEdit = EJBArtifactEdit.getEJBArtifactEditForRead(wbcs[0]);
			  if (ejbEdit!=null){
				isEJB = true;
			  }
			}
			finally{
				if (ejbEdit!=null)
					ejbEdit.dispose();
			}
		  }
		}
		catch(Exception ex){}
		finally{
			if (mc!=null)
				mc.dispose();
		}
		
		return isEJB;	
	}

	/**
	 * Returns true if the given <code>project</code> is an Application Client
	 * Project.
	 * Note: for components, use J2EEUtils.isAppClientComponent()
	 * @param project
	 *            The project.
	 * @return True if the project is an Application Client Project
   * 
   * @deprecated
	 */
	public static boolean isAppClientProject(IProject project) {
		boolean isAppClient = false;
		StructureEdit mc = null;
		try {
		  mc = StructureEdit.getStructureEditForRead(project);
		  WorkbenchComponent[] wbcs = mc.getWorkbenchModules();
		  if (wbcs.length!=0) {
			AppClientArtifactEdit appClientEdit = null;
			try {
				//appClientEdit = AppClientArtifactEdit.getAppClientArtifactEditForRead(wbcs[0]);
			  if (appClientEdit!=null){
				  isAppClient = true;
			  }
			}
			finally{
				if (appClientEdit!=null)
					appClientEdit.dispose();
			}
		  }
		}
		catch(Exception ex){}
		finally{
			if (mc!=null)
				mc.dispose();
		}
		
		return isAppClient;	
	}

	/**
	 * Returns the type of the given <code>project</code> as a bitmask.
	 * Relevant bitmask values are:
	 * <ol>
	 * <li>{@link #PROJECT_TYPE_JAVA PROJECT_TYPE_JAVA}
	 * <li>{@link #PROJECT_TYPE_WEB PROJECT_TYPE_WEB}
	 * <li>{@link #PROJECT_TYPE_EJB PROJECT_TYPE_EJB}
	 * <li>{@link #PROJECT_TYPE_NONE PROJECT_TYPE_NONE}
	 * </ol>
	 * 
	 * @param project
	 *            The project.
	 * @return The type bitmask of the project.
	 * 
	 * @deprecated use getComponentType
	 */ 
	public static byte getProjectType(IProject project) {
		byte projectType = PROJECT_TYPE_NONE;
		if (ResourceUtils.isJavaProject(project)) {
			projectType |= PROJECT_TYPE_JAVA;
		}
		if (ResourceUtils.isWebProject(project)) {
			projectType |= PROJECT_TYPE_WEB;
		}
		if (ResourceUtils.isEJBProject(project)) {
			projectType |= PROJECT_TYPE_EJB;
		}
		return projectType;
	}

	/**
	 * Returns the component type id as defined in IModuleConstants
	 * i.e. IModuleConstants.JST_WEB_MODULE = "jst.web"
	 * @param project
	 * @param componentName
	 * @return
	 */
	public static String getComponentType(IProject project, String componentName){
		IVirtualComponent comp = ComponentCore.createComponent(project, componentName);
		return getComponentType(comp);
	}
	
	/**
	 * Returns the component type
	 * @param component
	 * @return
	 */
	public static String getComponentType(IVirtualComponent component){
		return component.getComponentTypeId();
	}
	
	/**
	 * 
	 * @param project
	 * @return
	 * 
	 * @deprecated not used
	 */
	public static String getProjectTypeAsString(IProject project) {
		if (ResourceUtils.isEJBProject(project))
			return WebServicePlugin.getMessage("%LABEL_EJB");
		else if (ResourceUtils.isWebProject(project))
			return WebServicePlugin.getMessage("%LABEL_WEB");
		else if (ResourceUtils.isJavaProject(project))
			return WebServicePlugin.getMessage("%LABEL_JAVA");
		else
			return "";
	}
	

	/**
	 * Returns WebModule Deployable of the <code>project</code> as an
	 * <code>IDeployable</code>, or null if the project has no Web nature.
	 * 
	 * @param project
	 *            The project.
	 * @return WebModule Deployable of the <code>project</code> or null if the
	 *         project has no Web nature.
	 * @deprecated  see ServerUtils.getModule(IProject, String)
	 */
	public static IModule getModule(IProject project) {
		IModule[] modules = ServerUtil.getModules(project);
		if (modules!=null && modules.length!=0) {
			return modules[0];
		}
		return null;
	}

	/**
	 * Returns the build output location of the <code>project</code> as an
	 * <code>IPath</code>, or null if the project has no Java nature.
	 * i.e. WP\.deployables\webModule\WEB-INF\classes
	 * @param project
	 *            The project.
	 * @return The build output location of the <code>project</code> or null
	 *         if the project has no Java nature.
	 * @deprecated not used 
	 */
	public static IPath getJavaOutputLocation(IProject project) {
		IPath outputLocation = null;
		try {
			IJavaProject javaProject = JavaCore.create(project);
			if (javaProject != null) {
				outputLocation = javaProject.getOutputLocation();
			}
		} catch (JavaModelException e) {
		}
		Log log = new EclipseLog();
		log.log(Log.INFO, 5032, ResourceUtils.class, "getJavaOutputLocation",
				"project=" + project + ",outputLocation=" + outputLocation);

		return outputLocation;
	}
	
	/**
	 * Returns a build source location of the <code>project</code> as an
	 * <code>IPath</code>, or null if the project either has no Java nature
	 * or if the project's build classpath contains no folders local to the
	 * project. If the project has more than one of its own folders on the build
	 * classpath, then one of them is chosen arbitrarily as the build source
	 * location. To work with all entries on the build classpath, use
	 * {@link #getJavaPackageFragmentRoots getJavaPackageFragmentRoots()}or
	 * {@link #getJavaClasspath getJavaClasspath()}.
	 * 
	 * @param project
	 *            The project.
	 * @return A build source location of the <code>project</code> or null if
	 *         the project has no Java nature or if the project's build
	 *         classpath contains no folders local to the project.
	 * @deprecated use getJavaSourceLocation(IProject project, String compName)
	 */
	public static IPath getJavaSourceLocation(IProject project) {
		IPath sourceLocation = null;
		IPackageFragmentRoot[] fragmentRoots = getJavaPackageFragmentRoots(project);
		for (int i = 0; i < fragmentRoots.length && sourceLocation == null; i++) {
			try {
				IResource fragmentRoot = fragmentRoots[i]
						.getCorrespondingResource();
				if (fragmentRoot != null
						&& (fragmentRoot.getProject().equals(project))
						&& (fragmentRoot.getType() != IResource.FILE)) {
					sourceLocation = fragmentRoot.getFullPath();
				}
			} catch (JavaModelException e) {
			}
		}

		Log log = new EclipseLog();
		log.log(Log.INFO, 5030, ResourceUtils.class, "getJavaSourceLocation",
				"project=" + project + ",sourceLocation=" + sourceLocation);
		
		return sourceLocation;
	}

	/**
	 * Returns the "JavaSource" folder
	 * @param project
	 * @param compName
	 * @return
	 */
	public static IPath getJavaSourceLocation(IProject project, String compName){
		IVirtualComponent component = ComponentCore.createComponent(project, compName);
		return getJavaSourceLocation(component);			
	}
	
	/**
	 * Returns the JavaSource location folder
	 * @param comp
	 * @return
	 */
	public static IPath getJavaSourceLocation(IVirtualComponent comp){
		if (comp!=null){
		   IVirtualFolder folder = comp.getRootFolder().getFolder(new Path("/WEB-INF/classes"));
		   return folder.getWorkspaceRelativePath();
		}
		return null;
	}
	
	/**
	 * Returns the JavaSource locations in each project
	 * @param project
	 * @return
	 */
	public static IPath[] getAllJavaSourceLocations(IProject project) {
		Vector pathVector = new Vector();
		IPackageFragmentRoot[] fragmentRoots = getJavaPackageFragmentRoots(project);

		for (int i = 0; i < fragmentRoots.length; i++) {
			try {
				IResource fragmentRoot = fragmentRoots[i].getCorrespondingResource();
				if (fragmentRoot != null && (fragmentRoot.getProject().equals(project))
						&& (fragmentRoot.getType() != IResource.FILE)) {
					pathVector.add(fragmentRoot.getFullPath());
				}
			} catch (JavaModelException e) {
			}
		}
		return (IPath[]) pathVector.toArray(new Path[pathVector.size()]);
	}

	public static IPath[] getAllJavaSourceLocations(IVirtualComponent[] components) {

		if (components!=null){
			List javaSourcePaths = new ArrayList();
			for (int i=0;i<components.length;i++){
				IPath path = getJavaSourceLocation(components[i]);
				javaSourcePaths.add(path);
			}
			return (IPath[])javaSourcePaths.toArray(new IPath[0]);
		}
		return null;
	}
	
	/**
	 * Returns a build source package fragment root of the <code>project</code>
	 * as an <code>IPackageFragmentRoot</code>, or null if the project either
	 * has no Java nature or if the project's build classpath contains no
	 * folders local to the project. If the project has more than one of its own
	 * folders on the build classpath, then one of them is chosen arbitrarily as
	 * the build source location.
	 * 
	 * @param project
	 *            The project.
	 * @return A build source package fragment root of the <code>project</code>
	 *         or null if the project has no Java nature or if the project's
	 *         build classpath contains no folders local to the project.
	 *         
	 * @deprecated not used
	 */
	public static IPackageFragmentRoot getJavaSourcePackageFragmentRoot(IProject project) {
		IPackageFragmentRoot packageFragmentRoot = null;
		IPath javaSourceLocation = getJavaSourceLocation(project);
		try {
			IJavaProject javaProject = JavaCore.create(project);
			if (javaProject != null) {
				packageFragmentRoot = javaProject.findPackageFragmentRoot(javaSourceLocation);
			}
		} catch (JavaModelException e) {
		}
		return packageFragmentRoot;
	}

	/**
	 * Returns the package fragment roots of the <code>project</code> as an
	 * array of <code>IPackageFragmentRoot</code> objects. If the project has
	 * no Java nature then the returned array will be of length zero.
	 * 
	 * @param project
	 *            The project.
	 * @return The package fragment roots of the <code>project</code>.
	 */
	public static IPackageFragmentRoot[] getJavaPackageFragmentRoots(IProject project) {
		try {
			IJavaProject javaProject = JavaCore.create(project);
			if (javaProject != null) {
				IPackageFragmentRoot[] packageFragmentRoots = javaProject
						.getPackageFragmentRoots();
				return packageFragmentRoots;
			}
		} catch (JavaModelException e) {
		}
		return new IPackageFragmentRoot[0];
	}

	/**
	 * Returns the build classpath entries of the <code>project</code> as an
	 * array of <code>IClasspathEntry</code> objects. If the project has no
	 * Java nature then the returned array will be of length zero.
	 * 
	 * @param project
	 *            The project.
	 * @return The classpath entries of the <code>project</code>.
	 * 
	 * @deprecated not used
	 */
	public static IClasspathEntry[] getJavaClasspath(IProject project) {
		try {
			IJavaProject javaProject = JavaCore.create(project);
			if (javaProject != null) {
				IClasspathEntry[] classpathEntries = javaProject.getRawClasspath();
				return classpathEntries;
			}
		} catch (JavaModelException e) {
		}
		return new IClasspathEntry[0];
	}

	/**
	 * 
	 * @param project
	 * @param componentName
	 * @return
	 */
	public static IFolder getWebComponentServerRoot(IProject project, String componentName){
		
      IFolder webModuleServerRoot = null;
      IVirtualComponent vc = ComponentCore.createComponent(project, componentName);
      if (vc.exists())
        webModuleServerRoot = StructureEdit.getOutputContainerRoot(vc);

	  return webModuleServerRoot;
	}

	/**
	 * Returns the URL string corresponding to the web server module root of the
	 * project in a server instance or null if the project has no Web nature or
	 * has no association to a server instance.
	 * 
	 * @param project
	 *            The project.
	 * @return The web server module root URL or null if the project has no Web
	 *         nature or has no association to a server instance.
	 * @deprecated use getWebComponentURL(..) which belongs in ServerUtils
	 */
	public static String getWebProjectURL(IProject project,
			String serverFactoryId, IServer server) {

		String webProjectURL = null;
		IModule module = getModule(project);
		if (module != null) {
			IServer serverInstance = ServerUtils.getServerForModule(module,
					serverFactoryId, server, true, new NullProgressMonitor());
			if (serverInstance != null) {
//				URL url = ((IURLProvider) serverInstance.getDelegate()).getModuleRootURL(module);
				URL url = ((IURLProvider)serverInstance.getAdapter(IURLProvider.class)).getModuleRootURL(module);
				if (url != null) {
					String s = url.toString();
					webProjectURL = (s.endsWith("/") ? s.substring(0, s
							.length() - 1) : s);
				}
			}
		}
		
		Log log = new EclipseLog();
		log.log(Log.INFO, 5036, ResourceUtils.class, "getWebProjectURL",
				"project=" + project + ",webProjectURL=" + webProjectURL);

		return webProjectURL;
	}
	
	/**
	 * Returns the forged URL string corresponding to the web server module root
	 * of the project in a server instance or null if the project has no Web
	 * nature or has no association to a server instance.
	 * 
	 * @param project
	 * @return The web server module root URL or null if the project has no Web
	 *         nature or has no association to a server instance.
	 * @deprecated  not used
	 */
//	public static String getForgedWebProjectURL(IProject project, String serverFactoryId, IServer server){
//  	
//  	String webProjectURL = null;
//  	IModule module = getModule(project);
//  	if (module != null)
//  	{
//  		//IServer serverInstance = ServerUtils.getServerForModule(module,
//		  // serverFactoryId, server, true, new NullProgressMonitor());
//  		if (server != null)
//  		{
//  			String hostname = server.getHost();
//
//  			// get ServerPort
//  			int portNumber = 0;
//
//  	          ServerPort[] ports = server.getServerPorts(null);
//  	          ServerPort port = null;
//  	          for (int it = 0; it<ports.length; it++)
//  	          {
//  	            ServerPort p = ports[it];
//  	            String protocol = p.getProtocol();
//  	            if (protocol != null && protocol.trim().toLowerCase().equals("http"))
//  	            {
//  	              port = p;
//  	              portNumber = p.getPort();
//  	              break;
//  	            }
//  	          }
//  	          
//  	        
//  	        
//  	        URL url = null;
//  	        try {
//  	        	url = new URL("http", hostname, portNumber, "");
//  	        }
//  	        catch(Exception e){
//  	        	e.printStackTrace();
//  	        }
//
//  	        
//  			//URL url = ((IURLProvider) serverInstance.getDelegate()).getModuleRootURL(module);
//  			
//  			if (url != null)
//  			{
//  				String s = url.toString();
//  				webProjectURL = s + "/"+project.getName();
//  				//webProjectURL = (s.endsWith("/") ? s.substring(0,s.length()-1) : s);
//  			}
//  		}
//  	}
// 	Log log = new EclipseLog();
//    log.log(Log.INFO, 5036, ResourceUtils.class, "getWebProjectURL", "project="+project+",webProjectURL="+webProjectURL);
//  	return webProjectURL;  	
//  }
	/**
	 * Returns the URL string corresponding to the web server module root of the
	 * project in a server instance or null if the project has no Web nature or
	 * has no association to a server instance.
	 * 
	 * @param project
	 *            The project.
	 * @return The web server module root URL or null if the project has no Web
	 *         nature or has no association to a server instance.
	 * @deprecated belongs in ServerUtils
	 *
	 */
	public static String getWebProjectURL(IProject project) {
		String webProjectURL = null;
		IModule module = getModule(project);
		if (module != null) {
			IServer serverInstance = ServerUtils.getServerForModule(module);
			if (serverInstance != null) {
				URL url = ((IURLProvider)serverInstance.getAdapter(IURLProvider.class)).getModuleRootURL(module);
				if (url != null) {
					String s = url.toString();
					webProjectURL = (s.endsWith("/") ? s.substring(0, s
							.length() - 1) : s);
				}
			}
		}
		Log log = new EclipseLog();
		log.log(Log.INFO, 5037, ResourceUtils.class, "getWebProjectURL",
				"project=" + project + ",webProjectURL=" + webProjectURL);

		return webProjectURL;
	}

	/**
	 * 
	 * @param project
	 * @return
	 * 
	 * @deprecated should be in ServerUtils
	 */
	public static String getEncodedWebProjectURL(IProject project) {
		String url = getWebProjectURL(project);
		if (url != null) {
			int index = url.lastIndexOf('/');
			if (index != -1) {
				StringBuffer encodedURL = new StringBuffer();
				encodedURL.append(url.substring(0, index + 1));
				try {
					String ctxtRoot = URLEncoder.encode(url.substring(index + 1, url.length()), "UTF-8");
					int plusIndex = ctxtRoot.indexOf('+');
					while (plusIndex != -1) {
						StringBuffer sb = new StringBuffer();
						sb.append(ctxtRoot.substring(0, plusIndex));
						sb.append("%20");
						sb.append(ctxtRoot.substring(plusIndex + 1, ctxtRoot
							.length()));
						ctxtRoot = sb.toString();
						plusIndex = ctxtRoot.indexOf('+');
					}
					encodedURL.append(ctxtRoot);
				}catch (IOException io){
					//handler exception
				}				
				url = encodedURL.toString();
			}
		}
		return url;
	}

	/**
	 * Given the <code>absolutePath</code> of a Java resource, returns the
	 * package name of the resource or null if the resource is not properly
	 * located in a project or folder on the build classpath or that is the
	 * build output path.
	 * 
	 * @param absolutePath
	 *            The absolute path of the Java resource.
	 * @return the package name of the Java resource.
	 */
	public static String getJavaResourcePackageName(IPath absolutePath) {
		try {
			IPath javaFolderPath = absolutePath.removeLastSegments(1);
			IProject project = getProjectOf(absolutePath);
			IJavaProject javaProject = JavaCore.create(project);
			if (javaProject != null) {
				IPackageFragment fragment = javaProject
						.findPackageFragment(javaFolderPath);
				if (fragment != null) {
					return fragment.getElementName();
				}
				IPath outputPath = getJavaOutputLocation(project);
				if (outputPath.isPrefixOf(javaFolderPath)) {
					IPath javaPackagePath = javaFolderPath
							.removeFirstSegments(outputPath.segmentCount());
					return javaPackagePath.isEmpty() ? null : javaPackagePath
							.toString().replace(IPath.SEPARATOR, '.');
				}
			}
		} catch (JavaModelException e) {
		}
		return null;
	}

	/**
	 * Given the <code>absolutePath</code> of a Java resource, returns the
	 * package name of the resource as a relative pathname or null if the
	 * resource is not properly located in a project or folder on the build
	 * classpath.
	 * 
	 * @param absolutePath
	 *            The absolute path of the Java resource.
	 * @return the package name of the Java resource as a relative path.
	 */
	public static IPath getJavaResourcePackagePath(IPath absolutePath) {
		String packageName = ResourceUtils
				.getJavaResourcePackageName(absolutePath);
		return (packageName == null ? null : new Path(packageName.replace('.',
				IPath.SEPARATOR)));
	}

	/**
	 * Given the <code>absolutePath</code> of a Java resource, returns the
	 * absolute path of the project or folder that is on the build classpath or
	 * is the build output path and that contains the fully qualified Java
	 * resource, or null if no such project or folder exists.
	 * 
	 * @param absolutePath
	 *            The absolute path of the Java resource.
	 * @return The absolute path of the project or folder containing the fully
	 *         qualified Java resource.
	 * @deprecated not used
	 */
	public static IPath getJavaResourceRootPath(IPath absolutePath) {
		try {
			IProject project = getProjectOf(absolutePath);
			IJavaProject javaProject = JavaCore.create(project);
			if (javaProject != null) {
				IPackageFragmentRoot[] pfrs = javaProject
						.getPackageFragmentRoots();
				for (int i = 0; i < pfrs.length; i++) {
					IResource fragmentRoot = pfrs[i].getCorrespondingResource();
					if (fragmentRoot != null) {
						IPath fragmentPath = fragmentRoot.getFullPath();
						if (fragmentPath.isPrefixOf(absolutePath)) {
							return fragmentPath;
						}
					}
				}
				IPath outputPath = getJavaOutputLocation(project);
				if (outputPath.isPrefixOf(absolutePath)) {
					return outputPath;
				}
			}
		} catch (JavaModelException e) {
		}
		return null;
	}

	/**
	 * Determines an URL (HTTP or FILE) to an Eclipse resource, returning the
	 * URL string if successful and false otherwise. A value of null will
	 * definitely be returned if any of the following conditions are not met:
	 * <ol>
	 * <li>The absolute path begins with a Web Project.
	 * <li>The leading portion of the absolute path matches the path of the
	 * container returned by {#link #getWebModuleServerRoot
	 * getWebModuleServerRoot} for the project.
	 * <li>The path does not lead to the WEB-INF folder or any descendent
	 * thereof.
	 * <li>The Web Project is associated with an explicit or default server
	 * instance.
	 * </ol>
	 * 
	 * @param absolutePath
	 *            The absolute path of the resource.
	 * @return The URL of the file, or null if no URL can be determined.
	 * @deprecated not used
	 */
//	public static String getURLFromPath(IPath absolutePath,
//			String serverFactoryId, IServer server) {
//		return getURLFromPath(absolutePath, getWebProjectURL(
//				getProjectOf(absolutePath), serverFactoryId, server));
//	}

	/**
	 * 
	 * @param absolutePath
	 * @param webProjectURL
	 * @return
	 * @deprecated not used
	 */
//	public static String getURLFromPath(IPath absolutePath, String webProjectURL) {
//		StringBuffer url = new StringBuffer();
//		IProject project = getProjectOf(absolutePath);
//		IContainer webModuleServerRoot = getWebModuleServerRoot(project);
//		if (webModuleServerRoot != null) {
//			IPath webModuleServerRootPath = webModuleServerRoot.getFullPath();
//			if (webModuleServerRootPath.isPrefixOf(absolutePath)) {
//				int numSegment = webModuleServerRootPath.segmentCount();
//				int numSegmentFromPath = absolutePath.segmentCount();
//				if (numSegmentFromPath > numSegment) {
//					String nextSegment = absolutePath.segment(numSegment);
//					// check if the segment after the WebModuleServerRoot is
//					// WEB-INF (ignoring case)
//					if (nextSegment != null	&& !nextSegment.equalsIgnoreCase("WEB-INF")) {
//						IPath relativePath = absolutePath.removeFirstSegments(numSegment);
//						if (webProjectURL != null)
//							url.append(webProjectURL).append(IPath.SEPARATOR).append(relativePath.toString());
//					}
//				} else if (numSegmentFromPath == numSegment)
//					url.append(webProjectURL);
//			}
//		}
//		if (url.length() < 1) {
//			IWorkspaceRoot workspace = getWorkspaceRoot();
//			url.append(getResourceURI(workspace.getFile(absolutePath)));
//		}
//		Log log = new EclipseLog();
//		log.log(Log.INFO, 5038, ResourceUtils.class, "getURLFromPath",
//				"absolutePath=" + absolutePath + ",url=" + url);
//
//		return url.toString();
//	}

	/**
	 * Copies a set of files from a plugin's installation location to a native
	 * directory. The files are named in an index file located relative to the
	 * plugin's installation location.
	 * 
	 * @param plugin
	 *            The plugin containing the files to copy. Must not be null.
	 * @param sourcePath
	 *            The path, relative to the <code>plugin</code> install
	 *            location, containing the files to copy. If null, then the
	 *            plugin install location is the source path (ie. null is
	 *            equivalent to ".").
	 * @param indexPathname
	 *            A file containing a whitespace-delimitted list of pathnames of
	 *            the files to copy. The pathnames are relative to the
	 *            <code>plugin sourcePath</code>. Must not be null.
	 * @param targetPath
	 *            The absolute path of the native directory to which the files
	 *            will be copied. The relative pathnames of the files named in
	 *            the <code>indexPathname</code> file are preserved. Must not
	 *            be null.
	 * @param progressMonitor
	 *            The progress monitor for the operation, or null.
	 * @throws IOException
	 *             An exception indicating an IO error has occured.
	 */
	static public void copyIndexedFilesToOS(Plugin plugin, IPath sourcePath,
			IPath indexPathname, IPath targetPath,
			IProgressMonitor progressMonitor) throws IOException {
		InputStream input = plugin.openStream(indexPathname);
		Enumeration filenames = StringUtils.parseFilenamesFromStream(input);
		copyEnumeratedFilesToOS(plugin, sourcePath, filenames, targetPath,
				progressMonitor);
	}

	/**
	 * Copies a set of files from a plugin's installation location to a native
	 * directory. The files are named in an enumeration.
	 * 
	 * @param plugin
	 *            The plugin containing the files to copy. Must not be null.
	 * @param sourcePath
	 *            The path, relative to the <code>plugin</code> install
	 *            location, containing the files to copy. If null, then the
	 *            plugin install location is the source path (ie. null is
	 *            equivalent to ".").
	 * @param pathnames
	 *            An enumeration of pathnames of the files to copy. The
	 *            pathnames are relative to the <code>plugin sourcePath</code>.
	 *            Must not be null.
	 * @param targetPath
	 *            The absolute path of the native directory to which the files
	 *            will be copied. The relative pathnames of the files named in
	 *            the <code>pathnames</code> enumeration are preserved. Must
	 *            not be null.
	 * @param progressMonitor
	 *            The progress monitor for the operation, or null.
	 * @throws IOException
	 *             An exception indicating an IO error has occured.
	 */
	static public void copyEnumeratedFilesToOS(Plugin plugin, IPath sourcePath,
			Enumeration pathnames, IPath targetPath,
			IProgressMonitor progressMonitor) throws IOException {
		while (pathnames.hasMoreElements()) {
			String filename = (String) pathnames.nextElement();
			copyFileToOS(plugin, sourcePath, new Path(filename), targetPath,
					progressMonitor);
		}
	}

	/**
	 * Copies a file from a plugin's installation location to a native
	 * directory.
	 * 
	 * @param plugin
	 *            The plugin containing the files to copy. Must not be null.
	 * @param sourcePath
	 *            The path, relative to the <code>plugin</code> install
	 *            location, containing the files to copy. If null, then the
	 *            plugin install location is the source path (ie. null is
	 *            equivalent to ".").
	 * @param pathname
	 *            The pathname of the file to copy. The pathname is relative to
	 *            the <code>plugin sourcePath</code>. Must not be null.
	 * @param targetPath
	 *            The absolute path of the native directory to which the file
	 *            will be copied. The relative pathname of the file is
	 *            preserved. Must not be null.
	 * @param progressMonitor
	 *            The progress monitor for the operation, or null.
	 * @throws IOException
	 *             An exception indicating an IO error has occured.
	 */
	static public void copyFileToOS(Plugin plugin, IPath sourcePath,
			IPath pathname, IPath targetPath, IProgressMonitor progressMonitor)
			throws IOException {
		IPath target = targetPath.append(pathname);
		IPath source = sourcePath == null ? pathname : sourcePath
				.append(pathname);
		InputStream input = plugin.openStream(source);
		OutputStream output = new FileOutputStream(target.toOSString());
		copyStream(input, output);
		input.close();
		output.close();
	}

	/**
	 * Copies data from one stream to another.
	 * 
	 * @param input
	 *            The input stream.
	 * @param output
	 *            The output stream.
	 * @return The number of bytes copied.
	 * @throws IOException
	 *             An exception if an error occurs while processing either of
	 *             the streams.
	 */
	static public int copyStream(InputStream input, OutputStream output)
			throws IOException {
		int t = 0;
		byte[] buffer = new byte[1024];
		int n = input.read(buffer);
		while (n >= 0) {
			output.write(buffer, 0, n);
			t += n;
			n = input.read(buffer);
		}
		return t;
	}

	/**
	 * Creates a native directory path equal to the parent portion of the given
	 * <code>pathname</code>. If the directory already exists, then no action
	 * is taken.
	 * 
	 * @param pathname
	 *            The pathname to the file whose parent directory should be
	 *            created.
	 * @throws IOException
	 *             An exception if an IO error occurs.
	 */
	static public void createParentDirectoryFor(String pathname)
			throws IOException {
		File file = new File(pathname);
		File parent = file.getParentFile();
		if (parent != null) {
			parent.mkdirs();
		}
	}

	/**
	 * Returns the IResource represented by the given selection.
	 * 
	 * @param object
	 *            The selection
	 * @return IResource if the selection represents an IResource. Returns null
	 *         otherwise.
	 * @throws CoreException
	 */
	static public IResource getResourceFromSelection(Object obj)
			throws CoreException {
		IResource res = null;
		if (obj != null) {
			if (obj instanceof IResource) {
				res = (IResource) obj;
			} else if (obj instanceof ICompilationUnit) {
				ICompilationUnit compUnit = (ICompilationUnit) obj;
				res = compUnit.getCorrespondingResource();
			} else if (obj instanceof EnterpriseBean) {
				EnterpriseBean ejbBean = (EnterpriseBean) obj;
				IProject ejbProject = ProjectUtilities.getProject(ejbBean);
				if (ejbProject != null)
					res = ejbProject;
			}

		}

		return res;
	}

	//----------------------------------------------------------------------
	// Naughty bits...
	//----------------------------------------------------------------------

	//
	// Creates a path of folders.
	// Do not call with an absolutePath of less than one segment.
	//
	//  private static IContainer makeFolderPath (
	//        ResourceContext resourceContext,
	//    IPath absolutePath,
	//    IProgressMonitor progressMonitor,
	//    StatusMonitor statusMonitor
	//  )
	//  throws CoreException
	//  {
	//    if (absolutePath.segmentCount() <= 1)
	//    {
	//      return getWorkspaceRoot().getProject(absolutePath.segment(0));
	//    }
	//    else
	//    {
	//      IContainer parent = makeFolderPath(resourceContext,
	// absolutePath.removeLastSegments(1), progressMonitor, statusMonitor);
	//      String folderName = absolutePath.lastSegment();
	//      return makeFolder(resourceContext, parent,folderName, progressMonitor ,
	// statusMonitor);
	//    }
	//  }
	//
	// Creates a folder under a container.
	// The container must already exist.
	//
	// private static IFolder makeFolder (
	//        ResourceContext resourceContext,
	//    IContainer parent,
	//    String folderName,
	//    IProgressMonitor progressMonitor,
	//    StatusMonitor statusMonitor
	//  )
	//  throws CoreException
	//  {
	//    IResource child = parent.findMember(folderName);
	//    if (child == null)
	//    {
	//      if (!resourceContext.isCreateFoldersEnabled()) {
	//        int result = statusMonitor.reportStatus(new
	// Status(IStatus.WARNING,WebServicePlugin.ID,0,
	//                                                         WebServicePlugin.getMessage("%MSG_ERROR_FOLDER_CREATION_DISABLED",
	//                                                         new Object[] {parent.getFullPath().toString(),folderName}),null),
	// getThreeStateFileOptions());
	//        if (result == IStatusDialogConstants.CANCEL_ID)
	//                        return null;
	//
	//          if ( result == IStatusDialogConstants.YES_TO_ALL_ID)
	//                        resourceContext.setCreateFoldersEnabled(true);
	//
	//      }
	//      IFolder folder = parent.getFolder(new Path(folderName));
	//      folder.create(true,true,progressMonitor);
	//      return folder;
	//    }
	//    else if (child.getType() == IResource.FOLDER)
	//    {
	//      return (IFolder)child;
	//    }
	//    else
	//    {
	//      throw new CoreException(new
	// Status(IStatus.ERROR,WebServicePlugin.ID,0,WebServicePlugin.getMessage("%MSG_ERROR_RESOURCE_NOT_FOLDER",new
	// Object[] {parent.getFullPath().append(folderName).toString()}),null));
	//    }
	//  }

	//
	// Creates a file under a container.
	// The container must already exist.
	//
	// private static IFile makeFile (
	//    ResourceContext resourceContext,
	//    IContainer parent,
	//    String fileName,
	//    InputStream inputStream,
	//    IProgressMonitor progressMonitor,
	//    StatusMonitor statusMonitor
	//  )
	//  throws CoreException
	//  {
	//    IResource child = parent.findMember(fileName);
	//    if (child != null)
	//    {
	//      if (child.getType() == IResource.FILE)
	//      {
	//        if (!resourceContext.isOverwriteFilesEnabled()) {
	//          int result = statusMonitor.reportStatus( new
	// Status(IStatus.WARNING,WebServicePlugin.ID,0,
	//                                                         WebServicePlugin.getMessage("%MSG_ERROR_FILE_OVERWRITE_DISABLED",
	//                                                         new Object[] {parent.getFullPath().toString(),fileName}),null),
	// getThreeStateFileOptions());
	//
	//          if (result == IStatusDialogConstants.CANCEL_ID)
	//                        return null;
	//
	//          if ( result == IStatusDialogConstants.YES_TO_ALL_ID)
	//                        resourceContext.setOverwriteFilesEnabled(true);
	//        }
	//        //We have permission to overwrite so check if file is read-only
	//        if (child.isReadOnly())
	//        {
	//          if (!resourceContext.isCheckoutFilesEnabled()) {
	//
	//            int result = statusMonitor.reportStatus( new
	// Status(IStatus.WARNING,WebServicePlugin.ID,0,
	//                                                         WebServicePlugin.getMessage("%MSG_ERROR_FILE_CHECKOUT_DISABLED",
	//                                                         new Object[] {parent.getFullPath().toString(),fileName}),null),
	// getThreeStateFileOptions());
	//
	//            if (result == IStatusDialogConstants.CANCEL_ID)
	//                        return null;
	//
	//            if ( result == IStatusDialogConstants.YES_TO_ALL_ID)
	//                        resourceContext.setCheckoutFilesEnabled(true);
	//          }
	//
	//          IFile[] files = new IFile[1];
	//          files[0] = (IFile)child;
	//
	//          if (
	// !statusMonitor.reportStatus(getWorkspace().validateEdit(files,null)))
	//                                return null;
	//        }
	//
	//        //Change the contents of the existing file.
	//            IFile file = parent.getFile(new Path(fileName));
	//        file.setContents(inputStream,true,true,progressMonitor);
	//        return file;
	//
	//      }
	//      else
	//      {
	//        throw new CoreException(new
	// Status(IStatus.ERROR,WebServicePlugin.ID,0,WebServicePlugin.getMessage("%MSG_ERROR_RESOURCE_NOT_FILE",new
	// Object[] {parent.getFullPath().append(fileName)}),null));
	//      }
	//    }
	//    else
	//    {
	//        //Create a new file.
	//        IFile file = parent.getFile(new Path(fileName));
	//        file.create(inputStream,true,progressMonitor);
	//        return file;
	//    }
	//  }

	/**
	 * Deletes a file under a container. The container must already exist.
	 * 
	 * @param file -
	 *            the IFile to be deleted
	 * @param progressMonitor
	 * @param statusMonitor
	 * @return True if the file does not exist or if it exists and is
	 *         successfully deleted. False otherwise.
	 */
	//  public static boolean deleteFile (
	//        ResourceContext resourceContext,
	//    IFile file,
	//    IProgressMonitor progressMonitor,
	//    StatusMonitor statusMonitor )
	//    throws CoreException
	//    {
	//        if (file.exists())
	//        {
	//                if (!resourceContext.isOverwriteFilesEnabled()) {
	//                        int result = statusMonitor.reportStatus( new
	// Status(IStatus.WARNING,WebServicePlugin.ID,0,
	//                                                                 WebServicePlugin.getMessage("%MSG_ERROR_FILE_OVERWRITE_DISABLED",
	//                                                                 new Object[]
	// {file.getParent().getFullPath().toString(),file.getName()}),null),
	// getThreeStateFileOptions());
	//
	//                            if (result == IStatusDialogConstants.CANCEL_ID)
	//                        return false;
	//
	//                if ( result == IStatusDialogConstants.YES_TO_ALL_ID)
	//                                resourceContext.setOverwriteFilesEnabled(true);
	//                        }
	//                //We have permission to overwrite so check if file is read-only
	//                if (file.isReadOnly())
	//                {
	//                        if (!resourceContext.isCheckoutFilesEnabled()) {
	//                                        int result = statusMonitor.reportStatus( new
	// Status(IStatus.WARNING,WebServicePlugin.ID,0,
	//                                                                 WebServicePlugin.getMessage("%MSG_ERROR_FILE_CHECKOUT_DISABLED",
	//                                                                 new Object[]
	// {file.getParent().getFullPath().toString(),file.getName()}),null),
	// getThreeStateFileOptions());
	//                        if (result == IStatusDialogConstants.CANCEL_ID)
	//                        return false;
	//
	//                        if ( result == IStatusDialogConstants.YES_TO_ALL_ID)
	//                                resourceContext.setCheckoutFilesEnabled(true);
	//
	//                                }
	//                        IFile[] files = new IFile[1];
	//                        files[0] = file;
	//
	//                        if (
	// !statusMonitor.reportStatus(getWorkspace().validateEdit(files,null)))
	//                                            return false;
	//                }
	//                file.delete(true,progressMonitor);
	//                }
	//        //At this point, either the file did not exist or we successfully deleted
	// it. Return success.
	//        return true;
	//  }
	/**
	 * Deletes a folder under a container.
	 * 
	 * @param folder -
	 *            the IFolder to be deleted
	 * @param progressMonitor
	 * @param statusMonitor
	 * @return True if the folder does not exist or if it exists and is
	 *         successfully deleted along with its members. False otherwise.
	 */
	//  public static boolean deleteFolder (
	//    ResourceContext resourceContext,
	//    IFolder folder,
	//    IProgressMonitor progressMonitor,
	//    StatusMonitor statusMonitor
	//  )
	//  throws CoreException
	//  {
	//    if (!folder.exists()) return true;
	//
	//    boolean deleted = true;
	//    IResource[] resources = folder.members();
	//    for (int i=0; i<resources.length; i++)
	//    {
	//      IResource resource = resources[i];
	//      if (resource instanceof IFile)
	//      {
	//        deleted = deleteFile(resourceContext, (IFile)resource, progressMonitor,
	// statusMonitor);
	//      }
	//      if (resource instanceof IFolder)
	//      {
	//        deleted = deleteFolder( resourceContext, (IFolder)resource,
	// progressMonitor, statusMonitor);
	//      }
	//
	//      if (!deleted) break;
	//    }
	//    if (deleted) {
	//      folder.delete(true, true, progressMonitor);
	//      return true;
	//    }
	//    else
	//      return false;
	//
	//  }
	/**
	 * Deletes a set of files that are named in an index file located relative
	 * to the plugin's installation location.
	 * 
	 * @param plugin
	 *            The plugin containing the indexed file Must not be null.
	 * @param sourcePath -
	 *            relative path of the indexed file passing a null in has the
	 *            same effect as passing a "." in
	 * @param indexFilePath -
	 *            the indexed filename Must not be null
	 * @param targetPath -
	 *            path containing the files named in the indexed file Must not
	 *            be null, this path must end with a trailing separator
	 * @param progressMonitor
	 */
	//  public static void deleteIndexFilesFromOS (
	//    Plugin plugin,
	//    IPath indexFilePath,
	//    IPath targetPath
	//  )
	//  throws IOException {
	//    InputStream input = plugin.openStream(indexFilePath);
	//    Enumeration filenames = StringUtils.parseFilenamesFromStream(input);
	//    while (filenames.hasMoreElements()) {
	//      File targetFile =
	// targetPath.append((String)filenames.nextElement()).toFile();
	//      if (targetFile.exists())
	//        targetFile.delete();
	//    }
	//  }
	/**
	 * Returns a URI reference to the given Eclipse resource, or null if no such
	 * reference can be determined (for example, if the resource does not exist
	 * in the underlying filesystem).
	 * 
	 * @param resource
	 *            The resource.
	 * @return The URI as a string, or null if there is no URI to the resource.
	 */
	public static String getResourceURI(IResource resource) {
		String uri = null;
		IPath location = resource.getLocation();
		if (location != null) {
			uri = "file:" + location.toString();
		}
		return uri;
	}

	/**
	 * Returns a URI reference to the given Eclipse resource using the
	 * "platform:" protocol, or null if no such reference can be determined (for
	 * example, if the resource does not exist in the underlying filesystem).
	 * 
	 * @param resource
	 *            The resource.
	 * @return The URI as a string, or null if there is no URI to the resource.
	 */
	public static String getPlatformResourceURI(IResource resource) {
		String uri = null;
		IPath location = resource.getFullPath();
		if (location != null) {
			uri = "platform:/resource" + location.toString();
		}
		return uri;
	}

	/**
	 * Returns the default binding namespace string given a Java bean name
	 * (using the convention used by CTC in GM). e.g. Java bean MyClass result
	 * in binding namespace of
	 * "http://www.myclass.com/definitions/MyClassRemoteInterface"
	 * 
	 * @param beanName
	 *            The java bean.
	 * @return The binding namespace as a string.
	 */
	public static String getBindingNamespace(String beanName) {
		return "http://www." + beanName.toLowerCase() + ".com/definitions/"
				+ beanName + "RemoteInterface";
	}

	/**
	 * Returns the default schema namespace string given a Java bean name (using
	 * the convention used by CTC in GM). e.g. Java bean MyClass result in
	 * schema namespace of
	 * "http://www.myclass.com/schemas/MyClassRemoteInterface"
	 * 
	 * @param beanName
	 *            The java bean.
	 * @return The schemas namespace as a string.
	 */
	public static String getSchemaNamespace(String beanName) {
		return "http://www." + beanName.toLowerCase() + ".com/schemas/"
				+ beanName + "RemoteInterface";
	}

	//  public static Vector getThreeStateFileOptions() {
	//        Vector options = new Vector();
	//    options.add(new
	// StatusOption(IStatusDialogConstants.YES_ID,IStatusDialogConstants.YES_LABEL,"",""));
	//    options.add(new
	// StatusOption(IStatusDialogConstants.YES_TO_ALL_ID,IStatusDialogConstants.YES_TO_ALL_LABEL,"",""));
	//    options.add(new
	// StatusOption(IStatusDialogConstants.CANCEL_ID,IStatusDialogConstants.CANCEL_LABEL,"",""));
	//    return options;
	//  }

	//----------------------------------------------------------------------

	/**
	 * Gets the SOAP rpcrouter servlet URL for the service project
	 * 
	 * @param project
	 *            The project.
	 * @param serverFactoryId
	 *            The server factory id
	 * @param server
	 *            The server
	 * @return The URL, possibly null.
	 */

	public static String getServletURL(IProject project,
			String serverFactoryId, IServer server) {
		return getServletURL(getWebProjectURL(project, serverFactoryId, server));
	}

	public static String getServletURL(String webProjectURL) {
		if (webProjectURL == null)
			return null;
		else
			return webProjectURL + SERVLET_EXT;
	}

	/**
	 * Gets the client Web project name
	 * 
	 * @param projectName
	 *            The project name to base on.
	 * @param typeId the webservice type id.
	 * @return The client Web project name.
	 * 
	 * @deprecated
	 */
	public static String getClientWebProjectName(String projectName, String typeId) 
	{
		String   baseClientWebProjectName = projectName + DEFAULT_CLIENT_WEB_PROJECT_EXT;
		IPath    projectPath;
		IProject project;
		boolean  foundWebProject     = false;
		int      i                   = 1;
		
		if( isSkeletonEJBType( typeId ) )
		{
		  // For the skeleton EJB scenario we need to create a slightly different
		  // base name.  When the EJB project is created another project
		  // is created with "Client" tacked onto the end.  We will
		  // add "WS" to our client so we don't collide with the other
		  // client project.
		  baseClientWebProjectName = projectName + "WS" + DEFAULT_CLIENT_WEB_PROJECT_EXT;    
		}
		
		String  clientWebProjectName = baseClientWebProjectName;
		
		while (!foundWebProject) 
		{
		  projectPath = new Path(clientWebProjectName).makeAbsolute();
		  project = ResourceUtils.getProjectOf(projectPath);
		  
		  if (project.exists() && !ResourceUtils.isWebProject(project)) 
		  {
			clientWebProjectName = baseClientWebProjectName + i;
			i++;
		  }
		  else 
		  {
			foundWebProject = true;
		  }
		}
		
		return clientWebProjectName;
	}

  public static String[] getClientProjectComponentName(String projectName, String componentName, boolean isEJB)
  {
    String clientProjectName = null;
    String clientComponentName = null;
    if (isEJB)
    {
      clientProjectName = projectName + DEFAULT_CLIENT_EJB_PROJECT_EXT;
      clientComponentName = componentName + DEFAULT_CLIENT_EJB_PROJECT_EXT;
    }
    else
    {
      clientProjectName = projectName + DEFAULT_CLIENT_WEB_PROJECT_EXT;
//      String baseName = clientProjectName;
      clientComponentName = componentName + DEFAULT_CLIENT_WEB_PROJECT_EXT;
//      String baseCompName = clientComponentName;
    }
    
//    boolean  foundWebProject     = false;
//    int      i                   = 1;
//    
//    while (!foundWebProject) 
//    {
//      IPath projectPath = new Path(clientProjectName).makeAbsolute();
//      IProject project = ResourceUtils.getProjectOf(projectPath);
//      
//      if (project.exists()) 
//      {
//        clientProjectName = baseName + i;
//        clientComponentName = baseCompName + i;
//        i++;
//      }
//      else 
//      {
//        foundWebProject = true;
//      }
//    }
    
    return new String[]{clientProjectName, clientComponentName};
  }
  
    /**
     * 
     * @param typeID
     * @return
     * 
     * @deprecated
     */
	public static boolean isSkeletonEJBType( String typeID )
	{
	  return typeID.equals( "org.eclipse.jst.ws.type.wsdl.ejb" );    
	}
	
	public static String getDefaultEJBProjectName() {
		return DEFAULT_EJB_PROJECT_NAME;
	}
  
  public static String getDefaultEJBComponentName() {
    return DEFAULT_EJB_COMPONENT_NAME;
  }  

	public static String getDefaultWebProjectName() {
		return DEFAULT_WEB_PROJECT_NAME;
	}
  
  public static String getDefaultWebComponentName() {
    return DEFAULT_WEB_COMPONENT_NAME;
  }

  public static String getDefaultClientExtension() {
    return DEFAULT_CLIENT_WEB_PROJECT_EXT;
  }
  
	/**
	 * 
	 * @param projectName
	 * @return
	 * 
	 */
	public static String getRouterProjectName(String projectName) {
		return projectName + DEFAULT_ROUTER_PROJECT_EXT;
	}

	public static String getDefaultServiceEARProjectName() {
		return DEFAULT_SERVICE_EAR_PROJECT_NAME;
	}
  
  public static String getDefaultServiceEARComponentName() {
    return DEFAULT_SERVICE_EAR_COMPONENT_NAME;
  }

	public static String getDefaultClientEARProjectName() {
		return DEFAULT_CLIENT_EAR_PROJECT_NAME;
	}

  public static String getDefaultClientEARComponentName() {
    return DEFAULT_CLIENT_EAR_COMPONENT_NAME;
  }
}

