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
 * 20070110   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 * 20070824   200515 sandakith@wso2.com - Lahiru Sandakith, NON-NLS move to seperate file
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.creation.core.utils;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Jar;
import org.eclipse.jst.ws.axis2.core.constant.Axis2Constants;

public class AARFileWriter extends Jar{

    public AARFileWriter() {
        this.setProject(new Project());
        this.getProject().init();
        this.setTaskType(Axis2Constants.JAR_TASK);
        this.setTaskName(Axis2Constants.JAR_TASK);
        this.setOwningTarget(new org.apache.tools.ant.Target());
    }

    public void writeAARFile(File outputFolder,
    						 String outputFileName,
    						 File inputFileFolder) throws IOException,Exception {

        if (!outputFolder.exists()){
            outputFolder.mkdir(); //create the output path
        }else{
            if (!outputFolder.isDirectory())
                return;
        }

        File targetFile = new File(outputFolder,outputFileName);
        this.setBasedir(inputFileFolder);
        this.setDestFile(targetFile);

        //run the task
        this.perform();
    }

}