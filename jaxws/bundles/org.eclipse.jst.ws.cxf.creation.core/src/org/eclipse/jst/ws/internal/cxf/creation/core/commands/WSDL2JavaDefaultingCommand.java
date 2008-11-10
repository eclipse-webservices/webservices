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
package org.eclipse.jst.ws.internal.cxf.creation.core.commands;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.xml.namespace.QName;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.context.WSDL2JavaPersistentContext;
import org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel;
import org.eclipse.jst.ws.internal.cxf.core.resources.WebContentChangeListener;
import org.eclipse.jst.ws.internal.cxf.core.utils.FileUtils;
import org.eclipse.jst.ws.internal.cxf.core.utils.JDTUtils;
import org.eclipse.jst.ws.internal.cxf.core.utils.WSDLUtils;
import org.eclipse.jst.ws.internal.cxf.creation.core.CXFCreationCorePlugin;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.util.WSDLCopier;

/**
 * @author sclarke
 */
@SuppressWarnings("restriction")
public class WSDL2JavaDefaultingCommand extends AbstractDataModelOperation {
    private WSDL2JavaDataModel model;
    private String projectName;
    private String inputURL;
    
    private WebContentChangeListener webContentChangeListener;
    
    public WSDL2JavaDefaultingCommand(WSDL2JavaDataModel model, String projectName, 
    		String inputURL) {
        this.model = model;
        this.projectName = projectName;
        this.inputURL = inputURL;
    }

    @Override
    @SuppressWarnings({ "unchecked", "deprecation" })
    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
    	IStatus status = Status.OK_STATUS;
    	
    	webContentChangeListener = new WebContentChangeListener(projectName);
    	ResourcesPlugin.getWorkspace().addResourceChangeListener(webContentChangeListener,
    	        IResourceChangeEvent.POST_CHANGE);
    	
        WSDL2JavaPersistentContext context = CXFCorePlugin.getDefault().getWSDL2JavaContext();
        model.setCxfRuntimeVersion(context.getCxfRuntimeVersion());
        //
        model.setGenerateImplementation(context.isGenerateImplementation());
        model.setGenerateServer(context.isGenerateServer());
        model.setDatabinding(context.getDatabinding());
        model.setFrontend(context.getFrontend());
        model.setWsdlVersion(context.getWsdlVersion());
        model.setGenerateAntBuildFile(context.isGenerateAntBuildFile());
        model.setIncludedNamespaces(new HashMap<String, String>());
        model.setExcludedNamespaces(new HashMap<String, String>());

        // XJC
        model.setXjcUseDefaultValues(context.isXjcUseDefaultValues());
        model.setXjcToString(context.isXjcToString());
        model.setXjcToStringMultiLine(context.isXjcToStringMultiLine());
        model.setXjcToStringSimple(context.isXjcToStringSimple());
        model.setXjcLocator(context.isXjcLocator());
        model.setXjcSyncMethods(context.isXjcSyncMethods());
        model.setXjcMarkGenerated(context.isXjcMarkGenerated());

        model.setValidate(context.isValidate());
        model.setProcessSOAPHeaders(context.isProcessSOAPHeaders());
        model.setLoadDefaultExcludesNamepsaceMapping(context.isLoadDefaultExcludesNamepsaceMapping());
        model.setLoadDefaultNamespacePackageNameMapping(context.isLoadDefaultNamespacePackageNameMapping());
        model.setUseDefaultValues(context.isUseDefaultValues());
        model.setNoAddressBinding(context.isNoAddressBinding());
        model.setUseSpringApplicationContext(context.isUseSpringApplicationContext());
        model.setJavaSourceFolder(JDTUtils.getJavaProjectSourceDirectoryPath(model.getProjectName()));

    	try {
    		Definition definition = null;
        	IWorkspace workspace = ResourcesPlugin.getWorkspace();
        	IProject project = workspace.getRoot().getProject(projectName);
        	URL wsdlUrl = new URL(inputURL);
        	model.setWsdlURL(wsdlUrl);
        	if (wsdlUrl.getProtocol().equals("file")) { //$NON-NLS-1$
        		if (!FileUtils.isFileInWebContentFolder(project, new Path(wsdlUrl.getPath()))) {
        		    IFolder wsdlFolder = WSDLUtils.getWSDLFolder(project);
        			IPath wsdlFolderPath = wsdlFolder.getLocation();
                    File fileToCopy = new File(wsdlUrl.toURI());
        			WSDLCopier copier = new WSDLCopier();
        			//for Eclipse 3.3 compatibility
        			copier.setSourceURI(wsdlUrl.toExternalForm());
        			copier.setTargetFolderURI(wsdlFolder.getLocationURI().toString());
                    //copier.setTargetFolderURI(wsdlFolderPath.toFile().toString());
        			workspace.run(copier, monitor);
        			wsdlFolder.refreshLocal(IResource.DEPTH_ONE, monitor);
        			File wsdlFile = wsdlFolderPath.addTrailingSeparator().append(fileToCopy.getName()).toFile();
        			model.setWsdlURL(wsdlFile.toURL());
        		}
            	definition = WSDLUtils.readWSDL(model.getWsdlURL());
            	if (definition != null) {
            		String targetNamespace = definition.getTargetNamespace();
            		String packageName = WSDLUtils.getPackageNameFromNamespace(targetNamespace);
            		model.setTargetNamespace(targetNamespace);
            		model.getIncludedNamespaces().put(targetNamespace, packageName);
            		
                    SOAPAddress soapAddress = WSDLUtils.getEndpointAddress(definition);
                    if (soapAddress != null) {
                        String address = soapAddress.getLocationURI();
                        URL endpointURL = new URL(address);
                        if (endpointURL.getQuery() == null) {
                            address += "?wsdl"; //$NON-NLS-1$
                        }
                        model.setWsdlLocation(address);
                    }
            	}
        	} else {
               	String filename = ""; //$NON-NLS-1$
               	definition = WSDLUtils.readWSDL(wsdlUrl);
        		if (definition != null) {
                    Map servicesMap = definition.getServices();
                    Set<Map.Entry> servicesSet = servicesMap.entrySet();
                    for (Map.Entry serviceEntry : servicesSet) {
                        Service service = (Service) serviceEntry.getValue();
                        Map portsMap = service.getPorts();
                        Set<Map.Entry> portsSet = portsMap.entrySet();                     
                        for (Map.Entry portEntry : portsSet) {
                            Port port = (Port) portEntry.getValue();
                            PortType portType = port.getBinding().getPortType();
                            QName qName = portType.getQName();
                            filename = qName.getLocalPart() + WSDLUtils.WSDL_FILE_EXTENSION;
                            break;
                        }
                    }
        		}
        		IPath wsdlFolderPath = WSDLUtils.getWSDLFolder(project).getLocation();
            	WSDLCopier copier = new WSDLCopier();
            	copier.setSourceURI(wsdlUrl.toExternalForm());
            	copier.setTargetFolderURI(wsdlFolderPath.toFile().toString());
            	copier.setTargetFilename(filename);
            	workspace.run(copier, monitor);
            	
            	File wsdlFile = wsdlFolderPath.addTrailingSeparator().append(filename).toFile();
    			model.setWsdlURL(wsdlFile.toURL());
        	}
        	model.setWsdlFileName(WSDLUtils.getWSDLFileNameFromURL(model.getWsdlURL()));
        	
        	IPath wsdlLocationPath = new Path(model.getWsdlURL().getPath());
        	wsdlLocationPath = wsdlLocationPath.removeFirstSegments(FileUtils.getWebContentFolder(project)
        			.getLocation().matchingFirstSegments(wsdlLocationPath));
        	
        	if (wsdlLocationPath.getDevice() != null) {
        	    wsdlLocationPath = wsdlLocationPath.setDevice(null);
        	}
        	model.setWsdlDefinition(definition);
        	model.setConfigWsdlLocation(wsdlLocationPath.toString());
        	
 		} catch (CoreException ce) {
 			status = ce.getStatus();
			CXFCreationCorePlugin.log(status);
		} catch (URISyntaxException urise) {
			status = new Status(IStatus.ERROR, CXFCorePlugin.PLUGIN_ID, urise.getLocalizedMessage());
			CXFCreationCorePlugin.log(status);
		} catch (MalformedURLException murle) {
			status = new Status(IStatus.ERROR, CXFCorePlugin.PLUGIN_ID, murle.getLocalizedMessage());
			CXFCreationCorePlugin.log(murle);
		}
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(webContentChangeListener);
        return status;
    }

    public WSDL2JavaDataModel getWSDL2JavaDataModel() {
        return model;
    }
    
    @Override
    public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        IStatus status = Status.OK_STATUS;
        List<IResource> changedResources = new ArrayList<IResource>();
        changedResources.addAll(webContentChangeListener.getChangedResources());
        if (changedResources.size() > 0) {
            for (IResource resource : changedResources) {
                try {
                    resource.delete(true, monitor);
                } catch (CoreException ce) {
                    status = ce.getStatus();
                    CXFCreationCorePlugin.log(status);
                }
            }
        }
        return status;
    }
}
