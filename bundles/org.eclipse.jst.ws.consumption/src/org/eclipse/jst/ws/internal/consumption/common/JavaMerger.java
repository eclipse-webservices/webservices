/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070509   182274 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.codegen.merge.java.JControlModel;
import org.eclipse.emf.codegen.merge.java.JMerger;
import org.eclipse.emf.codegen.merge.java.facade.FacadeHelper;
import org.eclipse.emf.codegen.util.CodeGenUtil;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.common.environment.IStatusHandler;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;
import org.eclipse.wst.ws.internal.preferences.PersistentMergeContext;
import org.eclipse.wst.ws.internal.wsrt.IMerger;
import org.eclipse.wst.ws.internal.wsrt.Merger;
import org.osgi.framework.Bundle;

/*
 * JavaMerger handles the merging of workspace files using JMerger.  If the URIs specified is not a file
 * in the workspace, it will be ignored.
 */
public class JavaMerger extends Merger implements IMerger {

	private static final String WST_WS = "org.eclipse.wst.ws";
	private static final String MERGE_XML = "merge.xml";
	private static final String JMERGER = "jmerger";
	
	private int size = 0; 
	private IFile[] workspaceFiles;
	private JMerger[] mergeModels;
	private JControlModel jMergeControlModel = null;
	private IStatus loadStatus;
	
	
	/*
	 * Convert the URI strings in the uris array to Eclipse IFiles and store in the workspaceFiles array. 
	 * Load the workspaceFiles into JMerger and store in the mergerModels array.  
	 * If the merger model cannot be obtained, null is stored in the mergerModels. 
	 * @see org.eclipse.wst.ws.internal.wsrt.IMerger#load(java.lang.String[])
	 */
	
	public IStatus load(IFile[] files) {
		
		loadStatus = Status.OK_STATUS;
		if (files != null) {

			PersistentMergeContext mergeContext = WSPlugin.getInstance().getMergeContext();
			if (mergeContext.isSkeletonMergeEnabled()) {
				// initialize JMerger
				initialize();
				
				if (jMergeControlModel != null) {
					// JMerger initialized properly
					size = files.length;
					workspaceFiles = new IFile[size];
					mergeModels = new JMerger[size];
					
					for (int i = 0; i < size; i++) {
						JMerger jMerger = null;
						IFile file = null;
						InputStream inStream = null;
						
						file = files[i];
						if (file != null && file.exists()) {														
							try {
								inStream = file.getContents();
								if (inStream != null) {
									jMerger = new JMerger(jMergeControlModel);
									jMerger.setTargetCompilationUnit(jMerger.createCompilationUnitForInputStream(inStream));
								}
							} catch (CoreException e) {
								loadStatus = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_FILE_MERGE_LOAD, file), e);
							} finally {
								if (inStream != null) {
									try {
										inStream.close();
									} catch (IOException e) {
										// ignore exceptions in closing file
									}
								}
							}				
						}
						workspaceFiles[i] = file;
						mergeModels[i] = jMerger;
					}
				}
			}
		}
		return loadStatus;
	}

	/**
	 * Initializes the JMerge model
	 * File merge.xml in WebServicePlugin contains the rules for the merge.
	 * If the merge.xml file is not found, jMergeControlModel is leave as null.
	 */
	private void initialize() {
		if (jMergeControlModel == null) {
			FacadeHelper facadeHelper = CodeGenUtil.instantiateFacadeHelper(JMerger.DEFAULT_FACADE_HELPER_CLASS);
			jMergeControlModel = new JControlModel();

			Bundle        wsBundle = Platform.getBundle(WST_WS);
			IPath         mergePath      = new Path( JMERGER ).append( MERGE_XML );
			URL           fileURL        = FileLocator.find( wsBundle, mergePath, null);
			jMergeControlModel.initialize(facadeHelper, fileURL.toString());
		}
		return;
	}
	
	/**
	 * Merge the current contents of the workspaceFiles with the mergeModels that had been stored earlier.   
	 * Then write the merged contents back to the file.
	 * @param monitor
	 * @param statusHandler
	 * @return IStatus
	 */
	public IStatus merge(IProgressMonitor monitor, IStatusHandler statusHandler) {
		
		IStatus status = Status.OK_STATUS;
		
		if (loadStatus.getSeverity() == IStatus.ERROR) {
			return loadStatus;
		}
		
		PersistentMergeContext mergeContext = WSPlugin.getInstance().getMergeContext();
		if (mergeContext.isSkeletonMergeEnabled()) {
			ResourceContext resourceContext = WebServicePlugin.getInstance().getResourceContext();
			for (int i = 0; i < size; i++) {
				String mergedContent = null;
				JMerger jMerger = null;
				IFile file = null;
				InputStream inStream = null;
				byte[] buf = null;
				
				jMerger = mergeModels[i];
				file = workspaceFiles[i];
				if (file != null && jMerger != null && file.exists()) {  
					// a JMerger model of the file exists, merge the contents
					try {
						inStream = file.getContents();
						jMerger.setSourceCompilationUnit(jMerger.createCompilationUnitForInputStream(inStream));
						jMerger.merge();
						mergedContent = jMerger.getTargetCompilationUnitContents();	
						if (mergedContent != null) {
							try {
								buf = mergedContent.getBytes(file.getCharset());
							} catch (UnsupportedEncodingException e) {
								buf = mergedContent.getBytes();
							}
							inStream = new ByteArrayInputStream(buf);
							IPath targetPath = file.getLocation();
							if (targetPath != null) {
								FileResourceUtils.createFileAtLocation(resourceContext, targetPath.makeAbsolute(), inStream,
									  monitor, statusHandler);
							}
						}
					} catch (CoreException e) {
						status = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_FILE_MERGE, file), e);
					} finally {
						try {
							if (inStream != null) {
								inStream.close();
							}
						} catch (IOException e) {
							// ignore exceptions in closing file
						}
					}
				}
			}
			
		}
		return status;
	}
	
}
