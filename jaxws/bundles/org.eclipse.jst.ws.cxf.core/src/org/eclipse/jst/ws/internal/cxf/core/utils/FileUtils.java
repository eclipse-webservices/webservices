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
package org.eclipse.jst.ws.internal.cxf.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel;
import org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel;
import org.eclipse.wst.sse.core.internal.format.IStructuredFormatProcessor;
import org.eclipse.wst.sse.ui.internal.FormatProcessorsExtensionReader;

/**
 * @author sclarke
 */
@SuppressWarnings("restriction")
public final class FileUtils {

    private static final String TMP_FOLDER_NAME = ".cxftmp"; //$NON-NLS-1$

    private FileUtils() {
    }

    public static IProject getProject(String projectName) {
        return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
    }
    
    public static void copyFolder(String source, String destination) {
        File sourceFolder = new File(source);

        String[] files = sourceFolder.list();
        for (int i = 0; i < files.length; i++) {
            File file = new File(sourceFolder + File.separator + files[i]);
            if (file.isDirectory()) {
                copyFolder(source, destination, files[i]);
            } else {
                try {
                    copyFile(source, destination, files[i]);
                } catch (IOException ioe) {
                    CXFCorePlugin.log(ioe);
                }
            }
        }
    }

    private static void copyFolder(String sourceFolder, String targetFolder, String name) {
        File target = new File(targetFolder + File.separator + name);
        if (!target.exists()) {
            target.mkdir();
        }
        copyFolder(sourceFolder + File.separator + name, targetFolder + File.separator + name);
    }

    public static void copyFile(String sourceFolder, String targetFolder, String fileName) 
            throws IOException {
        File sourceFile = new File(sourceFolder + File.separator + fileName);
        File targetFile = new File(targetFolder + File.separator + fileName);
        if (!targetFile.exists()) {
            InputStream inputStream = new FileInputStream(sourceFile);
            OutputStream outputStream = new FileOutputStream(targetFile);
            byte[] buffer = new byte[102400];
            while (true) {
                int numberOfBytes = inputStream.read(buffer);
                if (numberOfBytes < 0) {
                    break;
                }
                outputStream.write(buffer, 0, numberOfBytes);
            }
            inputStream.close();
            outputStream.close();
        } else {
            if (sourceFile.getName().indexOf(".java") != -1) { //$NON-NLS-1$
                MergeUtils.merge(sourceFile, targetFile);
            }
        }
    }

    public static IStatus copyFiles(IPath fromDiretoryPath, IPath toDirectPath, String fileExtension) 
            throws IOException {
        IStatus status = Status.OK_STATUS;

        File fromDirectory = new File(fromDiretoryPath.toOSString());
        if (fromDirectory.exists() && fromDirectory.isDirectory()) {
            String[] files = fromDirectory.list();
            for (int i = 0; i < files.length; i++) {
                File file = new File(fromDirectory.getPath() + File.separator + files[i]);
                String fileName = file.getName();
                InputStream fileInputStream = new FileInputStream(file);

                if (fileName.indexOf(".") != -1
                        && fileName.substring(fileName.lastIndexOf("."), fileName.length()).equals(
                                fileExtension)) {
                    try {
                        IFolder toFolder = ResourcesPlugin.getWorkspace().getRoot().getFolder(toDirectPath);
                        IFile fileCopy = toFolder.getFile(fileName);
                        if (!fileCopy.exists()) {
                            fileCopy.create(fileInputStream, true, new NullProgressMonitor());
                        }
                    } catch (CoreException ce) {
                        status = ce.getStatus();
                        CXFCorePlugin.log(status);
                    } finally {
                        fileInputStream.close();
                    }
                }
            }
        }
        return status;
    }

    public static String getTmpFolderName() {
        return TMP_FOLDER_NAME;
    }
    
    public static String getTmpFolder(String projectName) {
        return FileUtils.getTmpFolder(FileUtils.getProject(projectName));
    }

    public static String getTmpFolder(IProject project) {
        IFolder tmpFolder = project.getFolder(TMP_FOLDER_NAME);
        if (!tmpFolder.exists()) {
            try {
                tmpFolder.create(true, true, new NullProgressMonitor());
            } catch (CoreException ce) {
                CXFCorePlugin.log(ce.getStatus());
            }
        }
        
        IFolder tmpSrcFolder = tmpFolder.getFolder("src"); //$NON-NLS-1$
        if (!tmpSrcFolder.exists()) {
            try {
                tmpSrcFolder.create(true, true, new NullProgressMonitor());
            } catch (CoreException ce) {
                CXFCorePlugin.log(ce.getStatus());
            }
        }
        
        IFolder tmpWSDLFolder = tmpFolder.getFolder("wsdl"); //$NON-NLS-1$
        if (!tmpWSDLFolder.exists()) {
            try {
                tmpWSDLFolder.create(true, true, new NullProgressMonitor());
            } catch (CoreException ce) {
                CXFCorePlugin.log(ce.getStatus());
            }
        }
        return tmpFolder.getLocation().toOSString();
    }

    public static void copyW2JFilesFromTmp(WSDL2JavaDataModel model) {
        IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();

        IProject project = workspaceRoot.getProject(model.getProjectName());
        IFolder srcFolder = workspaceRoot.getFolder(new Path(model.getJavaSourceFolder()));
        FileUtils.copyFolder(getTmpFolder(project) + "/src", srcFolder.getLocation().toOSString()); //$NON-NLS-1$

        try {
            srcFolder.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
        } catch (CoreException ce) {
            CXFCorePlugin.log(ce.getStatus());
        }
        deleteTmpFolder(project);
    }

    public static void copyJ2WFilesFromTmp(Java2WSDataModel model) {
        String projectName = model.getProjectName();
        IProject project = FileUtils.getProject(projectName);

        IType type = JDTUtils.getType(JDTUtils.getJavaProject(projectName), model.getJavaStartingPoint());
        IJavaElement javaElement = type.getPackageFragment().getParent();
        IResource javaElementResource = javaElement.getResource();
        if (javaElementResource instanceof IFolder) {
            IFolder srcDirectory = (IFolder) javaElementResource;
            FileUtils.copyFolder(getTmpFolder(project) + "/src", srcDirectory.getLocation().toOSString()); //$NON-NLS-1$
            if (model.isGenerateWSDL()) {
                IFolder wsdlFolder = WSDLUtils.getWSDLFolder(projectName);
                FileUtils.copyFolder(getTmpFolder(project) + "/wsdl", wsdlFolder.getLocation() //$NON-NLS-1$
                        .toOSString());
                model.setConfigWsdlLocation(wsdlFolder.getName() + "/"  //$NON-NLS-1$
                        + model.getWsdlFileName());
                try {
                    wsdlFolder.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
                } catch (CoreException ce) {
                    CXFCorePlugin.log(ce.getStatus());
                }
            }
        }
        deleteTmpFolder(project);
    }

    private static IStatus deleteTmpFolder(IProject project) {
        IStatus status = Status.OK_STATUS;

        IFolder tmpFolder = project.getFolder(TMP_FOLDER_NAME);
        if (tmpFolder.exists()) {
            try {
                tmpFolder.delete(true, false, new NullProgressMonitor());
            } catch (CoreException ce) {
                CXFCorePlugin.log(ce.getStatus());
            }
        }
        return status;
    }

    public static boolean isFileInWebContentFolder(IProject project, IPath filePath) {
    	IPath webContentPath = FileUtils.getWebContentFolder(project).getLocation();
    	if (webContentPath.isPrefixOf(filePath)) {
       		return true;
    	}
    	return false;
    }
    
    public static boolean isFileInWorkspace(URL fileURL) {
        IPath filePath = new Path(fileURL.getPath());

        IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        IPath workspacePath = workspaceRoot.getLocation();
        if (filePath.isValidPath(workspacePath.toOSString())) {
        	return true;
        }
        return false;
    }

    public static IPath getWebContentPath(IProject project) {
        IPath webContentPath = J2EEUtils.getWebContentPath(project);
        if (!webContentPath.hasTrailingSeparator()) {
            webContentPath = webContentPath.addTrailingSeparator();
        }
        return webContentPath;
    }
    
    public static IFolder getWebContentFolder(String projectName) {
    	return FileUtils.getWebContentFolder(FileUtils.getProject(projectName));
    }
    
    public static IFolder getWebContentFolder(IProject project) {
    	IPath webContentPath = FileUtils.getWebContentPath(project);
    	return ResourcesPlugin.getWorkspace().getRoot().getFolder(webContentPath);
    }
    
    public static void formatXMLFile(IFile file) {
        if (file != null) {
            try {
                IContentDescription contentDescription = file.getContentDescription();
                if (contentDescription == null) {
                    return;
                }
                IContentType contentType = contentDescription.getContentType();
                IStructuredFormatProcessor formatProcessor = FormatProcessorsExtensionReader.getInstance()
                        .getFormatProcessor(contentType.getId());
                if (formatProcessor != null) {
                    formatProcessor.formatFile(file);
                }
            } catch (CoreException ce) {
                CXFCorePlugin.log(ce.getStatus());
            } catch (IOException ioe) {
                CXFCorePlugin.log(ioe);
            }
        }
    }
    
    public static void refreshProject(String projectName, IProgressMonitor monitor) throws CoreException {
        IProject project = FileUtils.getProject(projectName);
        FileUtils.refreshProject(project, monitor);
    }

    public static void refreshProject(IProject project, IProgressMonitor monitor) throws CoreException {
        project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
    }
}
