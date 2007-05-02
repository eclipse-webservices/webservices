/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation, WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * IBM Corporation, WSO2 Inc. - Initial API and implementation
 * 20070110   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the 
 * 										  Axis2 runtime to the framework for 168762
 * 20070426   183046 sandakith@wso2.com - Lahiru Sandakith
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils
{
	public FileUtils(){
		super();
	}

	public static void copyFile(String src, String dest) {
		InputStream is = null;
		FileOutputStream fos = null;

		try
		{
			is = new FileInputStream(src);
			fos = new FileOutputStream(dest);
			int c = 0;
			byte[] array = new byte[1024];
			while ((c = is.read(array)) >= 0){
				fos.write(array, 0, c);
			}
		}
		catch (Exception e)	{
			e.printStackTrace();
		}
		finally	{
			try	{
				fos.close();
				is.close();
			}
			catch (Exception e)	{
				e.printStackTrace();
			}
		}
	}

	public static File createFileAndParentDirectories(String fileName) throws Exception {
		File file = new File(fileName);
		File parent = file.getParentFile();
		if (!parent.exists()){
			parent.mkdirs();
		}
		file.createNewFile();
		return file;
	}
	
	public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

	public static void deleteDirectories(File dir) {
		File[] children = dir.listFiles();
		for (int i = 0; i < children.length; i++){
			if (children[i].list() != null && children[i].list().length > 0){
				deleteDirectories(children[i]);
			}
			else{
				children[i].delete();
			}
		}
		dir.delete();
	}

	public static void deleteDirectories(String dir) {
		File directory = new File(dir);
		deleteDirectories(directory);
	}

	public static void createTargetFile(String sourceFileName, String targetFileName) 
							throws Exception {
		createTargetFile(sourceFileName, targetFileName, false);
	}

	public static void createTargetFile(String sourceFileName, String targetFileName, 
						boolean overwrite) throws Exception{
		File idealResultFile = new File(targetFileName);
		if (overwrite || !idealResultFile.exists())
		{
			FileUtils.createFileAndParentDirectories(targetFileName);
			FileUtils.copyFile(sourceFileName, targetFileName);
		}
	}

	public static boolean createDirectory(String directory){
		// Create a directory; all ancestor directories must exist
		boolean success = (new File(directory)).mkdir();
		if (!success) {
			// Directory creation failed
		}
		return success;  
	}

	public static boolean createDirectorys(String directory){
		// Create a directory; all ancestor directories must exist
		boolean success = (new File(directory)).mkdirs();
		if (!success) {
			// Directory creation failed
		}
		return success;  
	}

	//Copies all files under srcDir to dstDir.
	// If dstDir does not exist, it will be created.
	public static void copyDirectory(File srcDir, File dstDir) throws IOException {
		if (srcDir.isDirectory()) {
			if (!dstDir.exists()) {
				dstDir.mkdir();
			}

			String[] children = srcDir.list();
			for (int i=0; i<children.length; i++) {
				copyDirectory(new File(srcDir, children[i]),
						new File(dstDir, children[i]));
			}
		} else {
			copy(srcDir, dstDir);
		}
	}

	//Copies src file to dst file.
	// If the dst file does not exist, it is created
	public static void copy(File src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);

		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	public static String addAnotherNodeToPath(String currentPath, String newNode) {
		return currentPath + File.separator + newNode;
	}
	
	public static String addNodesToPath(String currentPath, String[] newNode) {
		String returnPath=currentPath;
		for (int i = 0; i < newNode.length; i++) {
			returnPath = returnPath + File.separator + newNode[i];
		}
		return returnPath;
	}
	
	public static String addNodesToPath(StringBuffer currentPath, String[] pathNodes) {
		for (int i = 0; i < pathNodes.length; i++){
			currentPath.append(File.separator);
			currentPath.append(pathNodes[i]);
		}
		return currentPath.toString();
	}
	
	public static String addNodesToURL(String currentPath, String[] newNode) {
		String returnPath=currentPath;
		for (int i = 0; i < newNode.length; i++) {
			returnPath = returnPath + "/" + newNode[i];
		}
		return returnPath;
	}

}
