/*******************************************************************************
 * Copyright (c) 2009, 2023 Shane Clarke and Others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.core.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
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
import org.osgi.framework.Bundle;

public class TestJavaProject extends TestProject {
	private static final String[] ADDITIONAL_REQUIRED_BUNDLES = new String[]{"javax.xml.ws", "javax.jws"};
	private IJavaProject javaProject;
    
    public TestJavaProject(String projectName) throws CoreException, IOException {
        super(projectName);
        javaProject = JavaCore.create(getProject());
        addProjectNature(getProject(), JavaCore.NATURE_ID);

        javaProject.setRawClasspath(new IClasspathEntry[0], null);

        createSourceFolder();

		for (String bundleName : ADDITIONAL_REQUIRED_BUNDLES) {
			Bundle bundle = Platform.getBundle(bundleName);
			assertNotNull("The " + bundleName + " bundle was not found", bundle);
			IPath absoluteLocation = null;
			String location = bundle.getLocation();
			if (location.indexOf("reference:file:") >= 0) {
				location = location.substring(location.indexOf("reference:file:") + "reference:file:".length());
			}
			absoluteLocation = Path.fromOSString(location).makeAbsolute();
			if (!absoluteLocation.toFile().exists()) {
				absoluteLocation = Path.fromOSString(System.getProperty("user.dir")).addTrailingSeparator().append(location);
				if (!absoluteLocation.toFile().exists()) {
					absoluteLocation = Path.fromOSString(System.getProperty("user.home")).addTrailingSeparator().append(location);
					if (!absoluteLocation.toFile().exists()) {
						absoluteLocation = Path.fromOSString(Platform.getInstallLocation().getURL().getFile().toString()).addTrailingSeparator().append(location);
					}
				}
			}

			assertTrue("Expected jar at " + absoluteLocation + " does not exist based on bundle location " + bundle.getLocation() + " from " + System.getProperty("user.dir"), absoluteLocation.toFile().exists());
			addToClasspath(javaProject, JavaCore.newLibraryEntry(absoluteLocation, null, null));
		}

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
