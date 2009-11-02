/*******************************************************************************
 * Copyright (c) 2008 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IONA Technologies PLC - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCorePlugin;

/**
 * JDT Utility class.
 * <p>
 * <strong>Provisional API:</strong> This class/interface is part of an interim API that is still under
 * development and expected to change significantly before reaching stability. It is being made available at
 * this early stage to solicit feedback from pioneering adopters on the understanding that any code that uses
 * this API will almost certainly be broken (repeatedly) as the API evolves.
 * </p>
 */
public final class JDTUtils {

    private JDTUtils() {
    }

    /**
     * Add a <code>IClasspathEntry</code> to a <code>IJavaProject</code>
     * @param javaProject the <code>IJavaProject</code> to add the classpath entry to
     * @param classpathEntry the <code>IClasspathEntry</code> to add
     */
    public static void addToClasspath(IJavaProject javaProject, IClasspathEntry classpathEntry) {
        try {
            List<IClasspathEntry> currentEntries = Arrays.asList(javaProject.getRawClasspath());
            List<IClasspathEntry> newEntries = new ArrayList<IClasspathEntry>();
            newEntries.addAll(currentEntries);
            if (classpathEntry != null && !newEntries.contains(classpathEntry)) {
                newEntries.add(classpathEntry);
            }
            javaProject.setRawClasspath(newEntries.toArray(new IClasspathEntry[newEntries.size()]),
                    new NullProgressMonitor());
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }
    }

    /**
     * Remove a <code>IClasspathEntry</code> from a <code>IJavaProject</code>
     * @param javaProject the <code>IJavaProject</code> to remove the classpath entry from
     * @param classpathEntry the <code>IClasspathEntry</code> to remove
     */
    public static void removeFromClasspath(IJavaProject javaProject, IClasspathEntry classpathEntry) {
        try {
            List<IClasspathEntry> currentEntries = Arrays.asList(javaProject.getRawClasspath());
            List<IClasspathEntry> newEntries = new ArrayList<IClasspathEntry>();
            newEntries.addAll(currentEntries);
            if (classpathEntry != null && newEntries.contains(classpathEntry)) {
                newEntries.remove(classpathEntry);
            }
            javaProject.setRawClasspath(newEntries.toArray(new IClasspathEntry[newEntries.size()]),
                    new NullProgressMonitor());
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }
    }

    /**
     * Returns the Java project corresponding to the given project name.
     * @param projectName the project name
     * @return the Java project corresponding to the given project name
     */
    public static IJavaProject getJavaProject(String projectName) {
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
        return JavaCore.create(project);
    }

    /**
     * Returns the absolute path in the local file system of the default output location for the given java project.
     * @param javaProject the java project
     * @return the absolute path of the default output folder for the given java project
     */
    public static IPath getJavaProjectOutputDirectoryPath(IJavaProject javaProject) {
        try {
            return ResourcesPlugin.getWorkspace().getRoot().findMember(javaProject.getOutputLocation()).getLocation();
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }
        return ResourcesPlugin.getWorkspace().getRoot().findMember(javaProject.getPath()).getLocation();
    }

    /**
     * Returns the absolute path in the local file system of the default output location for the given java project name.
     * @param projectName the name of the java project
     * @return  the absolute path of the default output folder for the given java project name
     */
    public static IPath getJavaProjectOutputDirectoryPath(String projectName) {
        return JDTUtils.getJavaProjectOutputDirectoryPath(JDTUtils.getJavaProject(projectName));
    }

    /**
     * Returns the full, absolute path relative to the workspace of the source folder that contains the given type.
     * @param type the <code>IType</code>
     * @return the absolute path of the given <code>IType</code> source folder
     */
    public static IPath getJavaProjectSourceDirectoryPath(IType type) {
        IPackageFragment packageFragment = type.getPackageFragment();
        IPackageFragmentRoot packageFragmentRoot = (IPackageFragmentRoot) packageFragment.getParent();
        IResource srcDirectoryResource = packageFragmentRoot.getResource();
        return srcDirectoryResource.getFullPath();
    }

    /**
     * Returns the full, absolute path relative to the workspace of the first source folder found in the java project with the given name.
     * @param projectName the name of the java project
     * @return the absolute path of the first source folder found in the java project with the given name.
     */
    public static IPath getJavaProjectSourceDirectoryPath(String projectName) {
        return JDTUtils.getJavaProjectSourceDirectoryPath(JDTUtils.getJavaProject(projectName));
    }

    /**
     * Returns the full, absolute path relative to the workspace of the first source folder found in the given java project.
     * @param javaProject the <code>IJavaProject</code>
     * @return the absolute path of the first source folder found in the given java project.
     */
    public static IPath getJavaProjectSourceDirectoryPath(IJavaProject javaProject) {
        try {
            IPackageFragmentRoot[] packageFragmentRoots = javaProject.getAllPackageFragmentRoots();
            IPackageFragmentRoot packageFragmentRoot = packageFragmentRoots[0];
            IResource srcDirectoryResource = packageFragmentRoot.getResource();
            return srcDirectoryResource.getFullPath();
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }
        return new Path("");
    }

    /**
     * If the given <code>IType</code> is an interface all methods declared in that interface are returned.
     * <p>
     * Alternatively if the given given <code>IType</code> is a class, only methods that are explicitly marked
     * public are returned.
     *
     * @param type the <code>IType</code>
     * @return the public methods declared in this type
     */
    public static IMethod[] getPublicMethods(IType type) {
        List<IMethod> publicMethods = new ArrayList<IMethod>();
        try {
            IMethod[] allMethods = type.getMethods();
            if (type.isInterface()) {
                return allMethods;
            }
            for (int i = 0; i < allMethods.length; i++) {
                IMethod method = allMethods[i];
                if (Flags.isPublic(method.getFlags()) && !method.isConstructor() && !method.isMainMethod()) {
                    publicMethods.add(method);
                }
            }
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }
        return publicMethods.toArray(new IMethod[publicMethods.size()]);
    }

    /**
     * Constructs a target namespace string from the given package name by splitting the dot '.' separated
     * package name, reversing the order of the package name segments followed by prefixing the string with
     * 'http://' and appending a forward slash '/' to the end.
     * <p>E.g., the Java package “com.example.ws” would return the target namespace “http://ws.example.com/”.</p>
     * <p>If the package name is null or is of zero length  "http://default_package/" is returned.</p>
     * @param packageName the package name
     * @return the derived target namespace
     */
    public static String getTargetNamespaceFromPackageName(String packageName) {
        if (packageName == null || packageName.length() == 0) {
            return "http://default_package/"; //$NON-NLS-1$
        }
        List<String> namespaceElements = Arrays.asList(packageName.split("\\.")); //$NON-NLS-1$
        Collections.reverse(namespaceElements);
        String targetNamespace = "http://"; //$NON-NLS-1$

        Iterator<String> namespaceIterator = namespaceElements.iterator();
        while (namespaceIterator.hasNext()) {
            String element = namespaceIterator.next();
            if (element.trim().length() > 0) {
                targetNamespace += element;
                if (namespaceIterator.hasNext()) {
                    targetNamespace += "."; //$NON-NLS-1$
                }
            }
        }
        targetNamespace += "/"; //$NON-NLS-1$
        return targetNamespace;
    }

    /**
     * Returns the first type found following the given java project's classpath with the given fully qualified name or null if none is found.
     * @param javaProject the given <code>IJavaProject</code>
     * @param fullyQualifiedClassName the given fully qualified name
     * @return the first type found following the java project's classpath with the given fully qualified name or null if none is found
     */
    public static IType getType(IJavaProject javaProject, String fullyQualifiedClassName) {
        try {
            return javaProject.findType(fullyQualifiedClassName);
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }
        return null;
    }

    /**
     * Returns the first type found with the given fully qualified name following the classpath of the java project with
     * the give project name or null if none is found.
     * @param projectName the name of the java project
     * @param fullyQualifiedClassName the given fully qualified name
     * @return the first type found following the java project's classpath with the given fully qualified name or null if none is found
     */
    public static IType getType(String projectName, String fullyQualifiedClassName) {
        return JDTUtils.getType(JDTUtils.getJavaProject(projectName), fullyQualifiedClassName);
    }

    /**
     * Returns whether the given project has the java nature.
     * @param project the given project
     * @return <code>true</code> if the project has the java nature
     */
    public static boolean isJavaProject(IProject project) {
        try {
            return project.hasNature(JavaCore.NATURE_ID);
        } catch (CoreException ce) {
            JAXWSCorePlugin.log(ce.getStatus());
        }
        return false;
    }

    /**
     * Returns true if the given method isn't a main method or constructor and if it has the public modifier.
     * @param method the given method
     * @return <code>true</code> if the given method is public
     */
    public static boolean isPublicMethod(IMethod method) {
        try {
            return Flags.isPublic(method.getFlags()) && !method.isConstructor() && !method.isMainMethod();
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }
        return false;
    }

    /**
     * Validates the given Java type name, either simple or qualified, using the workspace source and compliance levels.
     * @param name the name of a type
     * @return a status object with code IStatus.OK if the given name is valid as a Java type name, a status with
     * code IStatus.WARNING indicating why the given name is discouraged, otherwise a status object indicating what is wrong with the name
     */
    public static IStatus validateJavaTypeName(String name) {
        String sourceLevel = JavaCore.getOption(JavaCore.COMPILER_SOURCE);
        String complianceLevel = JavaCore.getOption(JavaCore.COMPILER_COMPLIANCE);
        return JavaConventions.validateJavaTypeName(name, sourceLevel, complianceLevel);
    }

    /**
     * Validates the given Java type name, either simple or qualified, using the given projects source and compliance levels.
     * @param projectName the name of the java project
     * @param name the name of a type
     * @return a status object with code IStatus.OK if the given name is valid as a Java type name, a status with
     * code IStatus.WARNING indicating why the given name is discouraged, otherwise a status object indicating what is wrong with the name
     */
    public static IStatus validateJavaTypeName(String projectName, String name) {
        IJavaProject javaProject = JDTUtils.getJavaProject(projectName);
        String sourceLevel = javaProject.getOption(JavaCore.COMPILER_SOURCE, true);
        String complianceLevel = javaProject.getOption(JavaCore.COMPILER_COMPLIANCE, true);
        return JavaConventions.validateJavaTypeName(name, sourceLevel, complianceLevel);
    }

    /**
     * Validate the given package name using the given projects source and compliance levels.
     * @param projectName the name of the java project
     * @param packageName the name of a package
     * @return a status object with code IStatus.OK if the given name is valid as a package name, otherwise a status
     * object indicating what is wrong with the name
     */
    public static IStatus validatePackageName(String projectName, String packageName) {
        IJavaProject javaProject = JDTUtils.getJavaProject(projectName);
        String sourceLevel = javaProject.getOption(JavaCore.COMPILER_SOURCE, true);
        String complianceLevel = javaProject.getOption(JavaCore.COMPILER_COMPLIANCE, true);
        return JavaConventions.validatePackageName(packageName, sourceLevel, complianceLevel);
    }

    /**
     * Validates the given Java identifier with the workspace source and compliance levels.
     * @param id the Java identifier
     * @return a status object with code IStatus.OK if the given identifier is a valid Java identifier, otherwise
     * a status object indicating what is wrong with the identifier
     */
    public static IStatus validateIdentifier(String id) {
        String sourceLevel = JavaCore.getOption(JavaCore.COMPILER_SOURCE);
        String complianceLevel = JavaCore.getOption(JavaCore.COMPILER_COMPLIANCE);
        return JavaConventions.validateIdentifier(id, sourceLevel, complianceLevel);
    }
}
