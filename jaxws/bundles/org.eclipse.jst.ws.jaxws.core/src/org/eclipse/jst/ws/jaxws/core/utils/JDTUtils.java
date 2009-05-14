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

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCorePlugin;

/**
 * JDT Utility class.
 * <p>
 * <strong>Provisional API:</strong> This class/interface is part of an interim API that is still under 
 * development and expected to change significantly before reaching stability. It is being made available at 
 * this early stage to solicit feedback from pioneering adopters on the understanding that any code that uses 
 * this API will almost certainly be broken (repeatedly) as the API evolves.
 * </p>
 * @author sclarke
 */
public final class JDTUtils {

    private JDTUtils() {
    }

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
    
    public static IStatus checkTypeExists(IType type, String compilationUnitName) {
        compilationUnitName = compilationUnitName.trim();

        IPackageFragment packageFragment = type.getPackageFragment();
        ICompilationUnit compilationUnit = packageFragment.getCompilationUnit(compilationUnitName);
        IResource resource = compilationUnit.getResource();

        if (resource.exists()) {
            return new Status(IStatus.ERROR, JAXWSCorePlugin.PLUGIN_ID, JAXWSCoreMessages
                    .bind(JAXWSCoreMessages.TYPE_WITH_NAME_ALREADY_EXISTS, new Object[] {
                            compilationUnitName, packageFragment.getElementName() }));
        }
        URI location = resource.getLocationURI();
        if (location != null) {
            try {
                IFileStore fileStore = EFS.getStore(location);
                if (fileStore.fetchInfo().exists()) {
                    return new Status(IStatus.ERROR, JAXWSCorePlugin.PLUGIN_ID,
                            JAXWSCoreMessages.TYPE_NAME_DIFFERENT_CASE_EXISTS);
                }
            } catch (CoreException ce) {
                JAXWSCorePlugin.log(ce.getStatus());
            }
        }
        return Status.OK_STATUS;
    }
    
    public static String getClassName(String projectName, String fullyQualifiedClassName) {
        return JDTUtils.getType(JDTUtils.getJavaProject(projectName), fullyQualifiedClassName)
                .getElementName();
    }

    public static IJavaProject getJavaProject(IProject project) {
        IJavaProject javaProject = JavaCore.create(project);
        return javaProject;
    }

    public static IJavaProject getJavaProject(String projectName) {
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
        return JDTUtils.getJavaProject(project);
    }

    public static String getJavaProjectOutputDirectoryPath(IJavaProject javaProject) {
        IPath outputPath;
        String fullPath = ""; //$NON-NLS-1$
        try {
            outputPath = javaProject.getOutputLocation();
            fullPath = ResourcesPlugin.getWorkspace().getRoot().findMember(outputPath).getLocation()
                    .toOSString();
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }
        return fullPath;
    }

    public static String getJavaProjectOutputDirectoryPath(String projectName) {
        return JDTUtils.getJavaProjectOutputDirectoryPath(JDTUtils.getJavaProject(projectName));
    }

    public static String getJavaProjectSourceDirectoryPath(IJavaProject javaProject,
            String fullyQualifiedClassName) {
        
        IType type = JDTUtils.getType(javaProject, fullyQualifiedClassName);
        IPackageFragment packageFragment = type.getPackageFragment();
        IPackageFragmentRoot packageFragmentRoot = (IPackageFragmentRoot)packageFragment.getParent();
        IResource srcDirectoryResource = packageFragmentRoot.getResource();
        return srcDirectoryResource.getFullPath().toOSString();
    }

    public static String getJavaProjectSourceDirectoryPath(String projectName, 
            String fullyQualifiedClassName) {
        return JDTUtils.getJavaProjectSourceDirectoryPath(JDTUtils.getJavaProject(projectName),
                fullyQualifiedClassName);
    }

    public static String getJavaProjectSourceDirectoryPath(String projectName) {
        return JDTUtils.getJavaProjectSourceDirectoryPath(JDTUtils.getJavaProject(projectName));
    }

    public static String getJavaProjectSourceDirectoryPath(IProject project) {
        return JDTUtils.getJavaProjectSourceDirectoryPath(JDTUtils.getJavaProject(project));
    }
    
    public static String getJavaProjectSourceDirectoryPath(IJavaProject javaProject) {
        try {
            IPackageFragmentRoot[] packageFragmentRoots = javaProject.getAllPackageFragmentRoots();
            IPackageFragmentRoot packageFragmentRoot = packageFragmentRoots[0];
            IResource srcDirectoryResource = packageFragmentRoot.getResource();
            return srcDirectoryResource.getFullPath().toOSString();
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }
        return ""; //$NON-NLS-1$
    }

    public static String getPackageNameFromClass(IJavaProject javaProject, String fullyQualifiedClassName) {
        return JDTUtils.getType(javaProject, fullyQualifiedClassName).getPackageFragment().getElementName();
    }

    public static String getPackageNameFromClass(String projectName, String fullyQualifiedClassName) {
        return JDTUtils
                .getPackageNameFromClass(JDTUtils.getJavaProject(projectName), fullyQualifiedClassName);
    }

    
    /**
     * If the given <code>IType</code> is an interface all methods declared in that interface are returned.
     * <p>
     * Alternatively if the given given <code>IType</code> is a class only methods that are explicitly marked
     * public are returned.
     * 
     * @param type the type
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
    
    public static String getSourceFromType(IType type) {
        try {
            return type.getCompilationUnit().getBuffer().getContents();
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }
        return ""; //$NON-NLS-1$
    }

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

    public static IType getType(IJavaProject javaProject, String fullyQualifiedClassName) {
        try {
            return javaProject.findType(fullyQualifiedClassName);
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }
        return null;
    }

    public static IType getType(IProject project, String fullyQualifiedClassName) {
        return JDTUtils.getType(JDTUtils.getJavaProject(project), fullyQualifiedClassName);
    }

    public static IType getType(String projectName, String fullyQualifiedClassName) {
        return JDTUtils.getType(JDTUtils.getJavaProject(projectName), fullyQualifiedClassName);
    }

    public static boolean isJavaProject(IProject project) {
        try {
            return project.hasNature(JavaCore.NATURE_ID);
        } catch (CoreException ce) {
            JAXWSCorePlugin.log(ce.getStatus());
        }
        return false;
    }
    
    public static boolean isPublicMethod(IMethod method) {
        try {
            return Flags.isPublic(method.getFlags()) && !method.isConstructor() && !method.isMainMethod();
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }
        return false;
    }

    public static IStatus validateJavaTypeName(String compilationUnitName) {
        String sourceLevel = JavaCore.getOption(JavaCore.COMPILER_SOURCE);
        String complianceLevel = JavaCore.getOption(JavaCore.COMPILER_COMPLIANCE);
        return JavaConventions.validateJavaTypeName(compilationUnitName, sourceLevel, complianceLevel);
    }

    public static IStatus validateJavaTypeName(String projectName, String compilationUnitName) {
        IJavaProject javaProject = JDTUtils.getJavaProject(projectName);
        String sourceLevel = javaProject.getOption(JavaCore.COMPILER_SOURCE, true);
        String complianceLevel = javaProject.getOption(JavaCore.COMPILER_COMPLIANCE, true);
        return JavaConventions.validateJavaTypeName(compilationUnitName, sourceLevel, complianceLevel);
    }

    public static IStatus validatePackageName(String projectName, String packageName) {
        IJavaProject javaProject = JDTUtils.getJavaProject(projectName);
        String sourceLevel = javaProject.getOption(JavaCore.COMPILER_SOURCE, true);
        String complianceLevel = javaProject.getOption(JavaCore.COMPILER_COMPLIANCE, true);
        return JavaConventions.validatePackageName(packageName, sourceLevel, complianceLevel);
    }

	public static ICompilationUnit getCompilationUnitFromFile(IFile file) {
		IProject project = file.getProject();
		try {
			if (project.hasNature(JavaCore.NATURE_ID)) {
				IJavaProject javaProject = JavaCore.create(project);
				IPackageFragmentRoot[] packageFragmentRoots = javaProject.getPackageFragmentRoots();
				for (IPackageFragmentRoot packageFragmentRoot : packageFragmentRoots) {
					if (packageFragmentRoot.getKind() == IPackageFragmentRoot.K_SOURCE) {
						IJavaElement[] packageFragments = packageFragmentRoot.getChildren();
						for (IJavaElement javaElement : packageFragments) {
							if (javaElement.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
								IPackageFragment packageFragment = (IPackageFragment)javaElement;
								ICompilationUnit[] compilationUnits = packageFragment.getCompilationUnits();
								for (ICompilationUnit compilationUnit : compilationUnits) {
									if (compilationUnit.getPath().equals(file.getFullPath())) {
										return compilationUnit;
									}
								}
							}
						}
					}
				}
			}
		} catch (JavaModelException jme) {
			JAXWSCorePlugin.log(jme.getStatus());
		} catch (CoreException ce) {
			JAXWSCorePlugin.log(ce.getStatus());
		}
		return null;
	}
	
    public static CompilationUnit getCompilationUnit(ICompilationUnit source) {
        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setSource(source);
        CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(new NullProgressMonitor());
        compilationUnit.recordModifications();
        
        return compilationUnit;
    }
}
