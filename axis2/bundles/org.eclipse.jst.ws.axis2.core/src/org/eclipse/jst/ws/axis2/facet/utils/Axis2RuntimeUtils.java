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
 * 20070426   183046 sandakith@wso2.com - Lahiru Sandakith
 * 20070501   180284 sandakith@wso2.com - Lahiru Sandakith
 * 20070516   183147 sandakith@wso2.com - Lahiru Sandakith Fix for the persisting DBCS paths
 * 20070523   174876 sandakith@wso2.com - Lahiru Sandakith, Persist Preferences inside Framework
 * 20070824   200515 sandakith@wso2.com - Lahiru Sandakith, NON-NLS move to seperate file
 * 20100308	  282466 samindaw@wso2.com - Saminda Wijeratne, support for axis2 1.5
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
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.Project;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.ws.axis2.core.constant.Axis2Constants;
import org.eclipse.jst.ws.axis2.core.context.Axis2EmitterContext;
import org.eclipse.jst.ws.axis2.core.plugin.WebServiceAxis2CorePlugin;
import org.eclipse.jst.ws.axis2.core.utils.Axis2CoreUtils;
import org.eclipse.jst.ws.axis2.core.utils.FacetContainerUtils;
import org.eclipse.jst.ws.axis2.core.utils.FileUtils;

public class Axis2RuntimeUtils {
	
	public static final int ZIP=0, UNZIP = 1;
	protected static int mode = UNZIP;
	static ZipFile zipFile;
	protected static byte[] b;
	static SortedSet createdDirs;
	static String outputPath;
	static Axis2EmitterContext context;

	public static String  copyAxis2War(IProgressMonitor monitor, String Axis2Home)
							throws FileNotFoundException, IOException{
		context = WebServiceAxis2CorePlugin.getDefault().getAxisEmitterContext();
		String tempWarFile = null;
		String tempWarLocation = null;
		String  tempUnzipLocation = null;
		try {
			if (new File (Axis2CoreUtils.tempAxis2Directory()).isDirectory()) {
				tempWarLocation = Axis2CoreUtils.addAnotherNodeToPath(
															Axis2CoreUtils.tempAxis2Directory(),
															Axis2Constants.DIR_TEMPWAR);
			File tempWarLocationFile= new File(tempWarLocation);
			if (tempWarLocationFile.exists()) {
				FileUtils.deleteDirectories(tempWarLocationFile);
			}
			tempWarLocationFile.mkdirs();
			tempWarFile = Axis2CoreUtils.addAnotherNodeToPath(
															tempWarLocation,	
															Axis2Constants.FILE_AXIS2_WAR);
			new File(tempWarFile).createNewFile();
			String axis2RuntimrLocation = null;
			if(context.getAxis2RuntimeLocation()!=null){
				axis2RuntimrLocation = context.getAxis2RuntimeLocation();
			}else{
			    Axis2EmitterContext context = WebServiceAxis2CorePlugin
												.getDefault().getAxisEmitterContext();
			    axis2RuntimrLocation =  context.getAxis2RuntimeLocation();

			}
					String axis2WarFile = Axis2CoreUtils.addAnotherNodeToPath(
										axis2RuntimrLocation,
										Axis2Constants.FILE_AXIS2_WAR);
					FileChannel srcChannel = new FileInputStream(axis2WarFile).getChannel();
					FileChannel dstChannel = new FileOutputStream(tempWarFile).getChannel();
					// Copy file contents from source to destination
					dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
					// Close the channels
					srcChannel.close();
					dstChannel.close();
				
				//unzip this into another foulder
				tempUnzipLocation = FileUtils.addAnotherNodeToPath(tempWarLocation, 
						Axis2Constants.DIR_UNZIP);
				File tempUnzipLocationFile= new File(tempUnzipLocation);
				if (!tempUnzipLocationFile.exists()) {
					tempUnzipLocationFile.mkdirs();
				}
				unzipAxis2War(tempWarFile,tempUnzipLocation );

				}
			
			IPath tempWebXMLLocationPath = new Path(tempUnzipLocation)
											   .append(Axis2Constants.DIR_WEB_INF)
											   .append(Axis2Constants.FILE_WEB_XML);
			//delete the axis2 web.xml File(DWP already have)
			new File(tempWebXMLLocationPath.toOSString()).delete();
				
			//} else {
				//Throws an error message
			//}
		} catch (FileNotFoundException e) {
			throw e;	
		} catch (IOException e) {
			throw e;
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
	
	public static String getAxis2ServletAdminClass(IProject project){
		String[] classNames=new String[]{"org.apache.axis2.transport.http.AxisAdminServlet" ,
				"org.apache.axis2.webapp.AxisAdminServlet"};//$NON-NLS-1$
		String defaultClassName=classNames[0];
		String selectedClassName=null;
		String axis2WCLibPath = FacetContainerUtils.getAxis2WebContainerLibPath(project);
		File libFolder = new File(axis2WCLibPath);
		File[] libs = libFolder.listFiles();
		AntClassLoader antClassLoader = new AntClassLoader();
		antClassLoader.setParentFirst(false);
		org.apache.tools.ant.types.Path path = new org.apache.tools.ant.types.Path(new Project());
		path.setPath(FacetContainerUtils.getAxis2WebContainerwebinfClassPath(project));
		for (File lib : libs) {
			if (lib.getName().toLowerCase().endsWith(".jar")){
				path.setPath(lib.getAbsolutePath());
				antClassLoader.setClassPath(path);
				for(String className:classNames){
					try{
						Class.forName(className, false, antClassLoader);
						selectedClassName=className;
					}catch (NoClassDefFoundError e1){
						if (!e1.getMessage().contains(className) && !e1.getCause().getMessage().contains(className)) //dependent class is not found
							selectedClassName=className;
						//class not found
					}catch(Exception e){
						//the class is not found
					}
					if (selectedClassName!=null) break;
				}
			}
			if (selectedClassName!=null) break;
		}
		antClassLoader.cleanup();
		if (selectedClassName==null) selectedClassName=defaultClassName;
		return selectedClassName;
	}
		    
}
