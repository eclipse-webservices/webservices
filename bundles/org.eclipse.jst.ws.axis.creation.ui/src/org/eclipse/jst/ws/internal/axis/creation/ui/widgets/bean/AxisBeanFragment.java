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
package org.eclipse.jst.ws.internal.axis.creation.ui.widgets.bean;

import org.eclipse.jst.ws.internal.axis.consumption.core.command.AxisDeployCommand;
import org.eclipse.jst.ws.internal.axis.consumption.core.command.Java2WSDLCommand;
import org.eclipse.jst.ws.internal.axis.consumption.core.command.WSDL2JavaCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.AddJarsToProjectBuildPathTask;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.CheckAxisDeploymentDescriptorsTask;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.CopyAxisJarCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.task.RefreshProjectCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.JavaToWSDLMethodCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.UpdateWEBXMLCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.DefaultsForServerJavaWSDLCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.LiteralSupportMessageTask;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.MoveJavaFilesTask;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.UpdateAxisWSDDFileTask;
import org.eclipse.jst.ws.internal.common.StringToIProjectTransformer;
import org.eclipse.jst.ws.internal.consumption.command.common.BuildProjectCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.StartProjectCommand;
import org.eclipse.jst.ws.internal.consumption.ui.command.WSINonCompliantRuntimeCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ServerExtensionDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ServerExtensionOutputCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.ObjectSelectionOutputCommand;
import org.eclipse.wst.command.env.common.WaitForAutoBuildCommand;
import org.eclipse.wst.command.env.core.common.Condition;
import org.eclipse.wst.command.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.env.core.fragment.BooleanFragment;
import org.eclipse.wst.command.env.core.fragment.SequenceFragment;
import org.eclipse.wst.command.env.core.fragment.SimpleFragment;
import org.eclipse.wst.command.env.ui.widgets.SelectionCommand;

  
public class AxisBeanFragment extends SequenceFragment
{
 
  public AxisBeanFragment()
  {
    add( new SimpleFragment( new ValidateObjectSelectionCommand(), ""));
    add( new SimpleFragment( new BUAxisDefaultingCommand(), "" ) );
    add( new BUAxisCommandsFragment1());
    add( new BUAxisCommandsFragment2());
    add( new SimpleFragment( "BeanConfig"    ) );
    add( new MappingFragment() );
    add( new BUAxisCommandsFragment3());
  }
  
  public void registerDataMappings(DataMappingRegistry registry) 
  {
    //ValidateObjectSelectionCommand
    registry.addMapping(ObjectSelectionOutputCommand.class, "ObjectSelection", ValidateObjectSelectionCommand.class);
    registry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", ValidateObjectSelectionCommand.class,"ServiceProjectName", null );
    
    //BUAxisDefaultingCommand
    registry.addMapping(SelectionCommand.class, "InitialSelection", BUAxisDefaultingCommand.class );
    registry.addMapping(ServerExtensionDefaultingCommand.class, "ServiceTypeRuntimeServer", BUAxisDefaultingCommand.class );
    
    //WSINonCompliantRuntimeCommand
    registry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", WSINonCompliantRuntimeCommand.class, "ServiceProject", new StringToIProjectTransformer());
    
    //BUAxisCommands2 - these run after BeanClassWidget
    //DefaultsForServerJavaWSDLCommand
    registry.addMapping(BUAxisDefaultingCommand.class, "JavaWSDLParam", DefaultsForServerJavaWSDLCommand.class);
    registry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", DefaultsForServerJavaWSDLCommand.class, "ServiceProject", new StringToIProjectTransformer());
    registry.addMapping(BUAxisDefaultingCommand.class, "JavaBeanName", DefaultsForServerJavaWSDLCommand.class);
    registry.addMapping(BUAxisDefaultingCommand.class, "Parser", DefaultsForServerJavaWSDLCommand.class);
    registry.addMapping(BUAxisDefaultingCommand.class, "CustomizeServiceMappings", AxisBeanFragment.MappingFragment.class);
    
    //JavaWSDLMethodCommand
    registry.addMapping(DefaultsForServerJavaWSDLCommand.class, "JavaWSDLParam", JavaToWSDLMethodCommand.class);
    registry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", JavaToWSDLMethodCommand.class, "ServiceProject", new StringToIProjectTransformer());

    // BUAxisCommands3 - these run after BeanConfigWidget
    //LiteralSupportMessageTask
    registry.addMapping(JavaToWSDLMethodCommand.class, "JavaWSDLParam", LiteralSupportMessageTask.class);
    
    //CheckAxisDeploymentDescriptorsTask
    //registry.addMapping(JavaToWSDLMethodCommand.class, "JavaWSDLParam", CheckAxisDeploymentDescriptorsTask.class);
    registry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", CheckAxisDeploymentDescriptorsTask.class, "ServerProject", new StringToIProjectTransformer());
    
    //CopyAxisJarCommand
    registry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", CopyAxisJarCommand.class, "Project", new StringToIProjectTransformer());
    
    //AddJarsToProjectBuildPathTask
    registry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", AddJarsToProjectBuildPathTask.class, "Project", new StringToIProjectTransformer());
    
    //Java2WSDLCommand
    registry.addMapping(JavaToWSDLMethodCommand.class, "JavaWSDLParam", Java2WSDLCommand.class);
    
    //RefreshProjectCommand
    registry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", RefreshProjectCommand.class, "Project", new StringToIProjectTransformer());
    
    //WSDL2JavaCommand
    registry.addMapping(JavaToWSDLMethodCommand.class, "JavaWSDLParam", WSDL2JavaCommand.class);
    

    //MoveJavaFilesTask
    registry.addMapping(WSDL2JavaCommand.class, "JavaWSDLParam", MoveJavaFilesTask.class);
    registry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", MoveJavaFilesTask.class, "ServiceProject", new StringToIProjectTransformer());
    
    //UpdateAxisWSDDFileTask
    registry.addMapping(MoveJavaFilesTask.class, "JavaWSDLParam", UpdateAxisWSDDFileTask.class);
    registry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", UpdateAxisWSDDFileTask.class, "ServiceProject", new StringToIProjectTransformer());
    
    //UpdateWEBXMLCommand
    registry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", UpdateWEBXMLCommand.class, "ServerProject", new StringToIProjectTransformer());
    
    //BuildProjectCommand
    registry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", BuildProjectCommand.class, "Project", new StringToIProjectTransformer());
    registry.addMapping(BUAxisDefaultingCommand.class, "ForceBuild", BuildProjectCommand.class);
    registry.addMapping(BUAxisDefaultingCommand.class, "ValidationManager", BuildProjectCommand.class);
    
    //StartProjectCommand
    registry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", StartProjectCommand.class, "ServiceProject", new StringToIProjectTransformer());    
    registry.addMapping(BUAxisDefaultingCommand.class, "SampleProject", StartProjectCommand.class);
    registry.addMapping(BUAxisDefaultingCommand.class, "ServiceServerTypeID", StartProjectCommand.class);
    registry.addMapping(BUAxisDefaultingCommand.class, "SampleServerTypeID", StartProjectCommand.class);
    registry.addMapping(BUAxisDefaultingCommand.class, "ServiceExistingServer", StartProjectCommand.class);
    registry.addMapping(BUAxisDefaultingCommand.class, "SampleExistingServer", StartProjectCommand.class);
    registry.addMapping(BUAxisDefaultingCommand.class, "IsWebProjectStartupRequested",StartProjectCommand.class);
        
    //AxisDeployCommand
    registry.addMapping(UpdateAxisWSDDFileTask.class, "JavaWSDLParam", AxisDeployCommand.class);
    
    //CopyAxisServerConfigTask
    /*
    registry.addMapping(UpdateAxisWSDDFileTask.class, "JavaWSDLParam", CopyAxisServerConfigTask.class);
    registry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", CopyAxisServerConfigTask.class, "ServiceProject", new StringToIProjectTransformer());
    registry.addMapping(BUAxisDefaultingCommand.class, "ServiceExistingServer", CopyAxisServerConfigTask.class);
    registry.addMapping(BUAxisDefaultingCommand.class, "ServiceServerTypeID", CopyAxisServerConfigTask.class);
    */
    
    // ServerExtensionOutputCommand
    registry.addMapping(Java2WSDLCommand.class, "WsdlURI", ServerExtensionOutputCommand.class);    
  }
  
  private class BUAxisCommandsFragment1 extends SequenceFragment
  {    
    public BUAxisCommandsFragment1()
    {
      add(new SimpleFragment(new WSINonCompliantRuntimeCommand(), ""));
    }
  }
  
  private class BUAxisCommandsFragment2 extends SequenceFragment
  {
    public BUAxisCommandsFragment2()
    {
      add(new SimpleFragment(new DefaultsForServerJavaWSDLCommand(), ""));
      add(new SimpleFragment(new JavaToWSDLMethodCommand(), ""));
    }    
  }
  
  private class BUAxisCommandsFragment3 extends SequenceFragment
  {
    public BUAxisCommandsFragment3()
    {
      add(new SimpleFragment(new LiteralSupportMessageTask(), ""));
      add(new SimpleFragment(new CheckAxisDeploymentDescriptorsTask(), ""));
      add(new SimpleFragment(new CopyAxisJarCommand(), ""));
      add(new SimpleFragment(new AddJarsToProjectBuildPathTask(), ""));
      add(new SimpleFragment(new WaitForAutoBuildCommand(), "" ));
      add(new SimpleFragment(new Java2WSDLCommand(), ""));
      add(new SimpleFragment(new RefreshProjectCommand(), ""));
      add(new SimpleFragment(new WSDL2JavaCommand(), ""));
      add(new SimpleFragment(new MoveJavaFilesTask(), ""));
      add(new SimpleFragment(new UpdateAxisWSDDFileTask(), ""));
      add(new SimpleFragment(new UpdateWEBXMLCommand(), ""));
      add(new SimpleFragment(new RefreshProjectCommand(), ""));
      add(new SimpleFragment(new BuildProjectCommand(), ""));
      add(new SimpleFragment(new StartProjectCommand(), ""));
      add(new SimpleFragment(new AxisDeployCommand(), ""));
      //add(new SimpleFragment(new CopyAxisServerConfigTask(), ""));
      add(new SimpleFragment(new RefreshProjectCommand(), ""));
    }       
  }
  
  public class MappingFragment extends BooleanFragment
  {
    private boolean showMappings_;
    
    public MappingFragment()
    {
      super();
      
      setTrueFragment( new SimpleFragment( "AxisServiceBeanMapping" ));
      setCondition( new Condition()
                    {
                      public boolean evaluate()
                      {
                        return showMappings_;
                      }
                    } );
    }
    
    public void setCustomizeServiceMappings( boolean showMappings )
    {
      showMappings_ = showMappings;
    }      
  }  
}