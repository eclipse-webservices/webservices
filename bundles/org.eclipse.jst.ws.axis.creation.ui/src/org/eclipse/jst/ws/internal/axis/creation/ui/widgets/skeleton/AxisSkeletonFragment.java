/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.widgets.skeleton;

import org.eclipse.jst.ws.internal.axis.consumption.core.command.AxisDeployCommand;
import org.eclipse.jst.ws.internal.axis.consumption.core.command.WSDL2JavaCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.AddJarsToProjectBuildPathTask;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.CheckAxisDeploymentDescriptorsTask;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.CopyAxisJarCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.RefreshProjectCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.ValidateWSDLCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.widgets.AxisMappingsFragment;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.ComputeAxisSkeletonBeanCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.UpdateWEBXMLCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.MoveDeploymentFilesTask;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.Skeleton2WSDLCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.BuildProjectCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.StartProjectCommand;
import org.eclipse.jst.ws.internal.consumption.ui.command.OpenJavaEditorCommand;
import org.eclipse.jst.ws.internal.consumption.ui.command.data.ProjectName2IProjectTransformer;
import org.eclipse.jst.ws.internal.consumption.ui.command.data.ServerName2IServerTransformer;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ServerExtensionDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ServerExtensionOutputCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.ObjectSelectionOutputCommand;
import org.eclipse.wst.command.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.env.core.fragment.SequenceFragment;
import org.eclipse.wst.command.env.core.fragment.SimpleFragment;
import org.eclipse.wst.command.env.ui.widgets.SelectionCommand;

public class AxisSkeletonFragment extends SequenceFragment
{
  public AxisSkeletonFragment()
  {
    add( new SimpleFragment( new AxisSkeletonDefaultingCommand(), "" ) );
    add( new SimpleFragment( new ValidateWSDLCommand(), ""));
    add( new SimpleFragment( new SkeletonConfigWidgetDefaultingCommand(), ""));
    add( new SimpleFragment( "SkeletonConfig") );
    add( new AxisMappingsFragment() );
    add( new SimpleFragment( new CheckAxisDeploymentDescriptorsTask(), ""));
    add( new SimpleFragment( new AddJarsToProjectBuildPathTask(), ""));
    add( new SimpleFragment( new CopyAxisJarCommand(), ""));
    add( new SimpleFragment( new WSDL2JavaCommand(), ""));
    add( new SimpleFragment( new MoveDeploymentFilesTask(), ""));
    add( new SimpleFragment( new Skeleton2WSDLCommand(), ""));
    add( new SimpleFragment( new UpdateWEBXMLCommand(), ""));
    add( new SimpleFragment( new RefreshProjectCommand(), ""));
    add( new SimpleFragment( new BuildProjectCommand(), ""));
    add( new SimpleFragment( new StartProjectCommand(), ""));
    // the second build project command is needed to synchronize with workspace auto build
    add( new SimpleFragment( new BuildProjectCommand(), ""));
    add( new SimpleFragment( new AxisDeployCommand(), ""));
    add( new SimpleFragment( new RefreshProjectCommand(), ""));
    add (new SimpleFragment( new ComputeAxisSkeletonBeanCommand(), ""));
    add( new SimpleFragment( new OpenJavaEditorCommand(), ""));
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.core.fragment.CommandFragment#registerDataMappings(org.eclipse.wst.command.env.core.data.DataMappingRegistry)
   */
  public void registerDataMappings(DataMappingRegistry dataRegistry)
  {
    // Transformers
    ProjectName2IProjectTransformer projectTransformer = new ProjectName2IProjectTransformer();

    dataRegistry.addMapping(SelectionCommand.class, "InitialSelection", AxisSkeletonDefaultingCommand.class);
    dataRegistry.addMapping(ObjectSelectionOutputCommand.class, "ObjectSelection", AxisSkeletonDefaultingCommand.class);
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "WebServicesParser", AxisSkeletonDefaultingCommand.class);
    
    // ValidateWSDLCommand
    dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "WebServicesParser", ValidateWSDLCommand.class);
    dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "WsdlURI", ValidateWSDLCommand.class);
    
    // SkeletonConfigWidgetDefaultingCommand
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", SkeletonConfigWidgetDefaultingCommand.class);
    dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "WsdlURI", SkeletonConfigWidgetDefaultingCommand.class);
    
    // CheckAxisDeploymentDescriptorsTask
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", CheckAxisDeploymentDescriptorsTask.class, "ServerProject", projectTransformer);

    // AddJarsToProjectBuildPathTask
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", AddJarsToProjectBuildPathTask.class, "Project", projectTransformer);
    
    dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "WsdlURI", WSDL2JavaCommand.class);
    //dataRegistry.addMapping(WSDLSelectionTreeWidget.class, "WsdlURI", WSDL2JavaCommand.class);
    dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "HttpBasicAuthUsername", WSDL2JavaCommand.class);
    dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "HttpBasicAuthPassword", WSDL2JavaCommand.class);

    // MoveDeploymentFilesTask
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", MoveDeploymentFilesTask.class, "ServerProject", projectTransformer);
    dataRegistry.addMapping(WSDL2JavaCommand.class, "JavaWSDLParam", MoveDeploymentFilesTask.class);

    // CopyAxisJarCommand
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", CopyAxisJarCommand.class, "Project", projectTransformer);

    // Skeleton2WSDLCommand
    dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "WebServicesParser", Skeleton2WSDLCommand.class);
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", Skeleton2WSDLCommand.class, "ServerProject", projectTransformer);
    dataRegistry.addMapping(WSDL2JavaCommand.class, "JavaWSDLParam", Skeleton2WSDLCommand.class);

    // UpdateWEBXMLCommand
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", UpdateWEBXMLCommand.class, "ServerProject", projectTransformer);

    // RefreshProjectCommand
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", RefreshProjectCommand.class, "Project", projectTransformer);
    
    // BuildProjectCommand
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", BuildProjectCommand.class, "Project", projectTransformer);

    // StartProjectCommand
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", StartProjectCommand.class, "ServiceProject", projectTransformer);
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerServer", StartProjectCommand.class, "ServiceExistingServer", new ServerName2IServerTransformer());
    dataRegistry.addMapping(CopyAxisJarCommand.class, "ProjectRestartRequired", StartProjectCommand.class, "IsWebProjectStartupRequested", null);

    // AxisDeployCommand
    dataRegistry.addMapping(WSDL2JavaCommand.class, "JavaWSDLParam", AxisDeployCommand.class);
    
    // ServerExtensionOutputCommand
    dataRegistry.addMapping(Skeleton2WSDLCommand.class, "WsdlURI", ServerExtensionOutputCommand.class);
    dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "WebServicesParser", ServerExtensionOutputCommand.class);
    
    // ComputeAxisSkeletonBeanCommand
    dataRegistry.addMapping(WSDL2JavaCommand.class, "JavaWSDLParam", ComputeAxisSkeletonBeanCommand.class);
    //dataRegistry.addMapping(Skeleton2WSDLCommand.class, "WsdlURI", ComputeAxisSkeletonBeanCommand.class);
    //dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "WebServicesParser", ComputeAxisSkeletonBeanCommand.class);
    
    // OpenJavaEditorCommand
    dataRegistry.addMapping(ComputeAxisSkeletonBeanCommand.class, "ClassNames", OpenJavaEditorCommand.class);
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", OpenJavaEditorCommand.class, "Project", projectTransformer);
  }
}
