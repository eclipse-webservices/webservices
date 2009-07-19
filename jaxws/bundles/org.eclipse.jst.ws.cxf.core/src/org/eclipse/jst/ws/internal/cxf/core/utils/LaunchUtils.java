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

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstall2;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFContext;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.console.MessageConsole;

public final class LaunchUtils {
    
    private LaunchUtils() {
    }

    public static IStatus launch(String[] runtimeClasspath, String className, String[] programArgs) {
        IStatus status = Status.OK_STATUS;
        try {
            IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
            IVMRunner vmRunner = vmInstall.getVMRunner(ILaunchManager.RUN_MODE);
            VMRunnerConfiguration vmRunnerConfiguration = new VMRunnerConfiguration(className,
                    runtimeClasspath);
            vmRunnerConfiguration.setProgramArguments(programArgs);

            ILaunch launch = new Launch(null, ILaunchManager.RUN_MODE, null);
            vmRunner.run(vmRunnerConfiguration, launch, null);

            while (!launch.isTerminated()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    CXFCorePlugin.log(ie);
                }
            }

            IProcess[] processes = launch.getProcesses();
            String outputStream = processes[0].getStreamsProxy().getOutputStreamMonitor().getContents();
            if (outputStream != null && outputStream.length() > 0) {
                status = new Status(IStatus.INFO, CXFCorePlugin.PLUGIN_ID, outputStream);
            }

        } catch (CoreException ce) {
            CXFCorePlugin.log(ce);
        }
        return status;
    }

    public static void launch(IJavaProject javaProject, String className, String[] programArgs)
            throws CoreException {
        IVMInstall vmInstall = JavaRuntime.getVMInstall(javaProject);
        if (vmInstall == null) {
            vmInstall = JavaRuntime.getDefaultVMInstall();
        }

        IVMRunner vmRunner = vmInstall.getVMRunner(ILaunchManager.RUN_MODE);
        String[] runtimeClasspath = JavaRuntime.computeDefaultRuntimeClassPath(javaProject);
        VMRunnerConfiguration vmRunnerConfiguration = new VMRunnerConfiguration(className, runtimeClasspath);

        vmRunnerConfiguration.setProgramArguments(programArgs);

        if (vmInstall instanceof IVMInstall2) {
            IVMInstall2 install2 = (IVMInstall2) vmInstall;
            if (install2.getJavaVersion().compareTo(JavaCore.VERSION_1_6) > 0) {
                vmRunnerConfiguration.setVMArguments(new String[] { "-Djava.endorsed.dirs="
                        + CXFCorePlugin.getDefault().getJava2WSContext().getCxfRuntimeLocation() });
            }
        }

        ILaunch launch = new Launch(null, ILaunchManager.RUN_MODE, null);
        vmRunner.run(vmRunnerConfiguration, launch, null);

        while (!launch.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                CXFCorePlugin.log(ie);
            }
        }

        IProcess[] processes = launch.getProcesses();

        String outputStream = processes[0].getStreamsProxy().getOutputStreamMonitor().getContents();
        logStream(outputStream);
        String errorStream = processes[0].getStreamsProxy().getErrorStreamMonitor().getContents();
        logStream(errorStream);
      
        logErrorStreamContents(errorStream, className);
    }

    private static void logStream(String outputStream) {
        try {
            MessageConsole cxfConsole = getCXFConsole();
            IWorkbench workbench = PlatformUI.getWorkbench();
            IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
            if (workbenchWindow != null) {
                IWorkbenchPage workbenchPage = workbenchWindow.getActivePage();
                String console_view_id = IConsoleConstants.ID_CONSOLE_VIEW;
                IConsoleView consoleView = (IConsoleView) workbenchPage.showView(console_view_id);
                consoleView.display(cxfConsole);
                IOConsoleOutputStream consoleOutputStream = cxfConsole.newOutputStream();
                consoleOutputStream.write(outputStream);
                consoleOutputStream.close();
            }
        } catch (PartInitException pie) {
            CXFCorePlugin.log(pie);
        } catch (IOException ioe) {
            CXFCorePlugin.log(ioe);
        }
    }

    private static MessageConsole getCXFConsole() {
        ConsolePlugin consolePlugin = ConsolePlugin.getDefault();
        IConsoleManager consoleManager = consolePlugin.getConsoleManager();
        IConsole[] existingConsoles = consoleManager.getConsoles();
        CXFContext context = CXFCorePlugin.getDefault().getJava2WSContext();
        for (int i = 0; i < existingConsoles.length; i++) {
           if (existingConsoles[i].getName().equals(context.getCxfRuntimeEdition() + " " 
                   + context.getCxfRuntimeVersion())) {
               return (MessageConsole) existingConsoles[i]; 
           }
        }
        MessageConsole cxfConsole = new MessageConsole(context.getCxfRuntimeEdition() + " " 
                + context.getCxfRuntimeVersion(),
                CXFCorePlugin.imageDescriptorFromPlugin(CXFCorePlugin.PLUGIN_ID, 
                        "icons/view16/console_view.gif")); //$NON-NLS-1$
        consoleManager.addConsoles(new IConsole[]{cxfConsole});
        return cxfConsole;
     }

    private static void logErrorStreamContents(String message, String className) {
    	String toolName = className.substring(className.lastIndexOf(".") + 1,  //$NON-NLS-1$
                className.length());

        if (message != null && message.indexOf(toolName + " Error") != -1) { //$NON-NLS-1$
          Status toolStatus = new Status(IStatus.ERROR, CXFCorePlugin.PLUGIN_ID, message);
          CXFCorePlugin.log(toolStatus);
        }
    }
}
