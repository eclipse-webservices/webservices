/*******************************************************************************
 * Copyright (c) 2007 WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070213  168766 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  facet to the framework for 168766
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.facet.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jst.ws.axis2.core.plugin.data.ServerModel;
import org.eclipse.jst.ws.axis2.core.plugin.messages.Axis2CoreUIMessages;
import org.eclipse.jst.ws.axis2.core.utils.Axis2CoreUtils;
import org.eclipse.jst.ws.axis2.core.utils.FileUtils;

public class Axis2RuntimeUtils {
	
	public static final int ZIP=0, UNZIP = 1;
	protected static int mode = UNZIP;
	static ZipFile zipFile;
	protected static byte[] b;
	static SortedSet createdDirs;
	static String outputPath;

	public static String  copyAxis2War(IProgressMonitor monitor, String Axis2Home) {
		String tempWarFile = null;
		String tempWarLocation = null;
		String  tempUnzipLocation = null;
		try {
			if (new File (Axis2CoreUtils.tempAxis2Directory()).isDirectory()) {
				tempWarLocation = Axis2CoreUtils.addAnotherNodeToPath(
															Axis2CoreUtils.tempAxis2Directory(),
															Axis2CoreUIMessages.DIR_TEMPWAR);
			File tempWarLocationFile= new File(tempWarLocation);
			if (tempWarLocationFile.exists()) {
				FileUtils.deleteDirectories(tempWarLocationFile);
			}
			tempWarLocationFile.mkdirs();
			tempWarFile = Axis2CoreUtils.addAnotherNodeToPath(
															tempWarLocation,	
															Axis2CoreUIMessages.FILE_AXIS2_WAR);
			new File(tempWarFile).createNewFile();
			Properties properties = new Properties();
			properties.load(new FileInputStream(Axis2CoreUtils.tempAxis2WebappFileLocation()));
				if (properties.containsKey(Axis2CoreUIMessages.PROPERTY_KEY_PATH)){
					String axis2WarLocation = Axis2CoreUtils.addAnotherNodeToPath(
													(ServerModel.getAxis2ServerPath()!=null)?ServerModel.getAxis2ServerPath():		
															properties.getProperty(Axis2CoreUIMessages.PROPERTY_KEY_PATH),
													Axis2CoreUIMessages.DIR_DIST);
					String axis2WarFile = Axis2CoreUtils.addAnotherNodeToPath(
																axis2WarLocation,
																Axis2CoreUIMessages.FILE_AXIS2_WAR);
					
					FileChannel srcChannel = new FileInputStream(axis2WarFile).getChannel();
					FileChannel dstChannel = new FileOutputStream(tempWarFile).getChannel();
					// Copy file contents from source to destination
					dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
					// Close the channels
					srcChannel.close();
					dstChannel.close();
				
				//unzip this into another foulder
				tempUnzipLocation = FileUtils.addAnotherNodeToPath(tempWarLocation, Axis2CoreUIMessages.DIR_UNZIP);
				File tempUnzipLocationFile= new File(tempUnzipLocation);
				if (!tempUnzipLocationFile.exists()) {
					tempUnzipLocationFile.mkdirs();
				}
				unzipAxis2War(tempWarFile,tempUnzipLocation );

				}
				
			} else {
				//Throws an error message
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();	
		} catch (IOException e) {
			e.printStackTrace();
		}

		return tempUnzipLocation;
	}
	
	
	private static void unzipAxis2War(String fileName,String outputPath){
	      b = new byte[8092];
	      setOutputPath(outputPath);
	      if (fileName.endsWith(".war") )
	        unZip(fileName);
	      else
	    	  throw new IllegalStateException("Wrong file type" + fileName);
	}

	/** For a given Zip file, process each entry. */
	public static void unZip(String fileName) {
	
	  createdDirs = new TreeSet();
	  try {
	    zipFile = new ZipFile(fileName);
	    Enumeration all = zipFile.entries();
	    while (all.hasMoreElements()) {
	      unzipFile((ZipEntry) all.nextElement());
	    }
	  } catch (IOException e) {
	    e.printStackTrace();
	  }
	}

	
	protected static void unzipFile(ZipEntry e) throws IOException {

	  boolean warnedMkDir = false;
	    String zipName = e.getName();
	    switch (mode) {
	    case UNZIP:
	      if (zipName.startsWith("/")) {
	        if (!warnedMkDir)
	        warnedMkDir = true;
	        zipName = zipName.substring(1);
	      }
	      if (zipName.endsWith("/")) {
	        return;
	      }
	      int ix = zipName.lastIndexOf('/');
	      if (ix > 0) {
	        String dirName = zipName.substring(0, ix);
	        if (!createdDirs.contains(dirName)) {
	          File d = new File(getOutputPath()+File.separator+dirName);
	          if (!(d.exists() && d.isDirectory())) {
	            if (!d.mkdirs()) {
	              throw new IllegalStateException("Warning: unable to mkdir " + dirName);
	            }
	            createdDirs.add(dirName);
	          }
	        }
	      }
          FileOutputStream os = new FileOutputStream(getOutputPath()+File.separator+zipName);
	      InputStream is = zipFile.getInputStream(e);
	      int n = 0;
	      while ((n = is.read(b)) > 0)
	        os.write(b, 0, n);
	      is.close();
	      os.close();
	      break;
	    default:
	      throw new IllegalStateException("mode value (" + mode + ") bad");
	    }
	  }

	

	public static String getOutputPath() {
		return outputPath;
	}


	public static void setOutputPath(String outputPath) {
		Axis2RuntimeUtils.outputPath = outputPath;
	}

		    
}
