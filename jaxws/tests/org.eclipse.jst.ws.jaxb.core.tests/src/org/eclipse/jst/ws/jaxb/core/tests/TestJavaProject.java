/*******************************************************************************
 * Copyright (c) 2009 Progress Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.jst.ws.jaxb.core.tests;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.apt.core.util.AptConfig;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstall2;
import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.JavaRuntime;

public class TestJavaProject extends TestProject {
    private IJavaProject javaProject;
    
    public TestJavaProject(String projectName) throws CoreException {
        super(projectName);
        javaProject = JavaCore.create(getProject());
        addProjectNature(getProject(), JavaCore.NATURE_ID);

        javaProject.setRawClasspath(new IClasspathEntry[0], null);

        createSourceFolder();
        addToClasspath(javaProject, getJREContainerEntry());
        createOutputFolder();
    }
    
    public void setAutoBuilding(boolean autoBuild) throws CoreException {
        IWorkspaceDescription description = ResourcesPlugin.getWorkspace().getDescription();
        description.setAutoBuilding(autoBuild);
        ResourcesPlugin.getWorkspace().setDescription(description);
    }
    
    public void enableAnnotationProcessing(boolean enable) {
        AptConfig.setEnabled(javaProject, enable);
    }
    
    private IClasspathEntry getJREContainerEntry() {
        IVMInstall defaultVMInstall = JavaRuntime.getDefaultVMInstall();
        if (defaultVMInstall != null && isJava6OrGreaterJRE(defaultVMInstall)) {
            return JavaRuntime.getDefaultJREContainerEntry();
        }
        
        IVMInstallType[] installTypes = JavaRuntime.getVMInstallTypes();
        for (IVMInstallType installType : installTypes) {
            IVMInstall[] vmInstalls = installType.getVMInstalls();
            for (IVMInstall vmInstall : vmInstalls) {
                if (isJava6OrGreaterJRE(vmInstall)) {
                    return JavaCore.newContainerEntry(JavaRuntime.newJREContainerPath(installType.getId(),
                            vmInstall.getName()));
                }
            }
        }
        
        return JavaRuntime.getDefaultJREContainerEntry();
    }
    
    private boolean isJava6OrGreaterJRE(IVMInstall vmInstall) {
        if (vmInstall instanceof IVMInstall2) {
            IVMInstall2 vmInstall2 = (IVMInstall2) vmInstall;
            if (vmInstall2.getJavaVersion().compareTo(JavaCore.VERSION_1_6) > 0) {
                return true;
            }
        }
        return false;
    }

    public ICompilationUnit createCompilationUnit(String packageName, String name, String contents) throws JavaModelException {
        return getPackageFragment(packageName).createCompilationUnit(name, contents, false, monitor);
    }
    
    private IPackageFragment getPackageFragment(String packageName) throws JavaModelException {
        return getPackageFragmentRoot().createPackageFragment(packageName, true, monitor);
    }
    
    private IPackageFragmentRoot getPackageFragmentRoot() {
        return getJavaProject().getPackageFragmentRoot(getProject().getFolder("src"));     
    }
    
    private void createSourceFolder() throws CoreException {
        IFolder srcDir = getProject().getFolder("src");
        mkdirs(srcDir);    
        addToClasspath(javaProject, JavaCore.newSourceEntry(srcDir.getFullPath()));
    }

    private void createOutputFolder() throws CoreException {
        IFolder outputDir = getProject().getFolder("bin");
        mkdirs(outputDir);
        getJavaProject().setOutputLocation(outputDir.getFullPath(), monitor);
    }

    public IJavaProject getJavaProject() {
        return javaProject;
    }
    
    public void addToClasspath(IJavaProject javaProject, IClasspathEntry classpathEntry) {
        try {
            IClasspathEntry[] currentClasspathEntries = javaProject.getRawClasspath();
            IClasspathEntry[] newClasspathEntries = new IClasspathEntry[currentClasspathEntries.length + 1];
            System.arraycopy(currentClasspathEntries, 0, newClasspathEntries, 0, 
                    currentClasspathEntries.length);
            newClasspathEntries[currentClasspathEntries.length] = classpathEntry;
            javaProject.setRawClasspath(newClasspathEntries, new NullProgressMonitor());
        } catch (JavaModelException jme) {
            jme.printStackTrace();
        }
    }

}
