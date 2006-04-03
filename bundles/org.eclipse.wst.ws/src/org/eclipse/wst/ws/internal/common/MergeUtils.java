/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060330 128827   kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/

package org.eclipse.wst.ws.internal.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.codegen.merge.java.JControlModel;
import org.eclipse.emf.codegen.merge.java.JMerger;
import org.eclipse.emf.codegen.merge.java.facade.FacadeHelper;
import org.eclipse.emf.codegen.util.CodeGenUtil;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;

public class MergeUtils {
	private static Hashtable MergeModel;
	private static final String MERGE_XML = "/merge.xml";
	private static JControlModel jMergeControlModel = null;

	
	/**
	 * Stores the JMerge model of the file represented by the URL strings
	 * @param urlStrings String representation of the URL for the file
	 */
	static public void storeMergeModels (String [] urlStrings) {
		if (urlStrings != null) {
			initialize();
			if (jMergeControlModel != null) {
				MergeModel = new Hashtable();
				String filename;
				for (int i = 0; i < urlStrings.length; i++) {
					filename = getFileFromURL(urlStrings[i]);
					if (filename != null) {
						storeModel(filename);
					}
				}
			}
		}
	}

	/**
	 * Stores the JMerge model of the file using the filename as key
	 * @param filename String representation of the file
	 */
	static public void storeModel (String filename) {
		InputStream inStream = null;
		JMerger jMerger;
		File aFile;
		try {
			if (jMergeControlModel != null) {
				aFile = new File (filename);
				if (aFile.exists()) {
					inStream = new FileInputStream(aFile);
					jMerger = new JMerger(jMergeControlModel);
					jMerger.setTargetCompilationUnit(jMerger.createCompilationUnitForInputStream(inStream));
					MergeModel.put(filename, jMerger);
				}
			}
		} catch (FileNotFoundException e) {
			// File not found, just ignore.  Nothing to merge.
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
				}
			}
		}
		
	}

	/**
	 * Initializes the JMerge model
	 * File merge.xml in WebServicePlugin contains the rules for the merge.
	 * If the merge.xml file is not found, jMergeControlModel is leave as null.
	 */
	static private void initialize() {
		if (jMergeControlModel == null) {
			FacadeHelper facadeHelper = CodeGenUtil.instantiateFacadeHelper(JMerger.DEFAULT_FACADE_HELPER_CLASS);

			jMergeControlModel = new JControlModel();
			File mergeXML = null;
			try {
				mergeXML = new File(FileLocator.toFileURL(WSPlugin.getInstance().getBundle().getEntry(MERGE_XML)).getFile()).getAbsoluteFile();
			} catch (IOException e) {
				// This should never happen since merge.xml is in the org.eclipse.jst.ws.consumption plugin directory
			}

			if (mergeXML != null) {
				jMergeControlModel.initialize(facadeHelper, mergeXML.getAbsolutePath());
			}
		}
		return;
	}
	
	/**
	 * Look up the model of this file stored earlier using the filename as key.
	 * Merge the content of this file with the earlier model.
	 * Returns the string representation of the merged content. 
	 * Returns null if an earlier model for this file does not exist or if the new file does not exist.
	 * @param filename
	 * @return merge content
	 */
	static public String mergeFile(String filename) {
		JMerger jMerger = (JMerger) MergeModel.get(filename);
		String mergedContent = null;
		if (jMerger != null) {  
			// a JMerger model of the file exists, merge the contents
			InputStream inStream = null;
			try {
				inStream = new FileInputStream(new File (filename));
				jMerger.setSourceCompilationUnit(jMerger.createCompilationUnitForInputStream(inStream));
				jMerger.merge();
				mergedContent = jMerger.getTargetCompilationUnitContents();			
			} catch (FileNotFoundException e) {
				// File not found, just ignore.  Nothing to merge.
			} finally {
				try {
					if (inStream != null) {
						inStream.close();
					}
				} catch (IOException e) {
				}
			}
		}
		return mergedContent;
	}
	
	/**
	 * Gets the file name of this URL
	 * @param urlString string representation of the URL
	 * @return file name string
	 */
	static public String getFileFromURL(String urlString) {
		String fileString = null;
		try {
			URL url = new URL (urlString);
			fileString = url.getFile();
		} catch (MalformedURLException e) {
		}
		return fileString;
	}

}
