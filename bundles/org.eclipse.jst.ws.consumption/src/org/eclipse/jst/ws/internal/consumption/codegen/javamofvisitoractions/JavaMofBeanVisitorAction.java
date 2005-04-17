/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitoractions;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaParameter;
import org.eclipse.jem.java.Method;
import org.eclipse.jst.ws.internal.command.NullStatusMonitor;
import org.eclipse.jst.ws.internal.command.StatusMonitor;
import org.eclipse.jst.ws.internal.consumption.codegen.Visitor;
import org.eclipse.jst.ws.internal.consumption.codegen.VisitorAction;
import org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitors.JavaMofAttributeVisitor;
import org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitors.JavaMofFieldVisitor;
import org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitors.JavaMofMethodVisitor;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.BeanElement;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.BeanModelElementsFactory;
import org.eclipse.wst.command.internal.provisional.env.core.common.Choice;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.command.internal.provisional.env.core.selection.BooleanSelection;
import org.eclipse.wst.ws.internal.datamodel.Element;
import org.eclipse.wst.ws.internal.datamodel.Model;

/**
* Objects of this class represent a JavaMofBeanVisitorAction.
* This VisitorAction will create a BeanElement using the
* JavaClass and the BeanModelElementsFactory
* It will also automatically walk the methods in the JavaClass
* */
public class JavaMofBeanVisitorAction implements VisitorAction
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  private MessageUtils msgUtils_;
  protected Environment env_;
 // protected BeanElement fBeanElement;

  protected Visitor fVisitor;

  /*
  * The project that defines the context
  **/
  protected String clientProject;

  /*
  * The Status monitor
  **/
  protected StatusMonitor monitor_;

  /*
  * The model that will be created
  **/
  protected Model fModel;

  /*
  * This is the parent element used when creating a
  * new element
  */
  protected Element fParentElement;

  /*
  * Methods processed from proxy
  */
  protected Vector fBeansCreated;

  private BooleanSelection[] fMethodsSelected;

  /*
  * this boolean tells wether the bean is a return parameter or an input parameter
  * usually this should be set around the parametervisitoraction, and passed on from there
  */
  protected boolean fReturnParam=false;

  public JavaMofBeanVisitorAction(String clientProject,BooleanSelection[] methods, Environment env)
  {
    this.clientProject = clientProject;
    fMethodsSelected = methods;
	String pluginId = "org.eclipse.jst.ws.consumption";
	msgUtils_ = new MessageUtils(pluginId + ".plugin", this); 
	env_ = env;
  }



  /*
  *Constructor
  * @param Model model is the Model that this bean will be placed in
  * @param IProject used in nature
  **/
  public JavaMofBeanVisitorAction(Model model, String clientProject, Environment env)
  {
    this.clientProject = clientProject;
    fModel = model;
    String pluginId = "org.eclipse.jst.ws.consumption";
	msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
	env_ = env;
  }

  /*
  *Constructor
  * @param Element the parent element that this element will be added too
  * @param IProject used in nature
  **/
  public JavaMofBeanVisitorAction(Element parentElement, String clientProject, Environment env )
  {
    fParentElement = parentElement;
    this.clientProject = clientProject;
    String pluginId = "org.eclipse.jst.ws.consumption";
	msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
	env_ = env;
  }

  /**
  * The visit will create the bean
  * Walk the methods
  * @param JavaClass the class to be used to create the bean model
  **/
  public Status visit (Object javaclass)
  {
  	Choice OKChoice = new Choice('O', msgUtils_.getMessage("LABEL_OK"), msgUtils_.getMessage("DESCRIPTION_OK"));
  	Choice CancelChoice = new Choice('C', msgUtils_.getMessage("LABEL_CANCEL"), msgUtils_.getMessage("DESCRIPTION_CANCEL"));
  	Status status = new SimpleStatus("");
    JavaClass javaClass = (JavaClass)javaclass;
    
    
    BeanElement beanElement = (BeanElement)BeanModelElementsFactory.getBeanModelElement(javaClass,fParentElement,fModel);
    if(beanElement.isOwnerParameter()) {
        resetBeansCreated();
    }
    fModel = beanElement.getModel();

    //this for the subsequent beans that represent types
    //first do the attributes
    //if we are not on the root
    if (!(beanElement == fModel.getRootElement())){
       Enumeration e = getBeansCreated().elements();
       while(e.hasMoreElements()){
          String name = (String)e.nextElement();
          if(name.equals(javaClass.getName())){
          	status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_JTS_CYCLIC_BEAN"), Status.WARNING);
            //getStatusMonitor().reportStatus(new Status(IStatus.WARNING,WebServiceConsumptionPlugin.ID,0,
            			//WebServiceConsumptionPlugin.getMessage( "%MSG_ERROR_JTS_CYCLIC_BEAN" ),null));
            return status;
          }
       }
       
       JavaMofAttributeVisitorAction attributeVisitorAction = new JavaMofAttributeVisitorAction(beanElement,clientProject, env_);
       //attributeVisitorAction.setStatusMonitor(getStatusMonitor());
       Vector childVector = ((Vector)getBeansCreated().clone());
       childVector.addElement(javaClass.getName());  
       attributeVisitorAction.setBeansCreated(childVector);
       attributeVisitorAction.setReturnParam(getReturnParam());
       JavaMofAttributeVisitor attributeVisitor = new JavaMofAttributeVisitor();
       attributeVisitor.setReturnParameter(getReturnParam());
       attributeVisitor.setProject(getProject());
       attributeVisitor.setEnvironment(env_);
       status = attributeVisitor.run(javaClass,attributeVisitorAction);
       //
       int severity = status.getSeverity(); 
       if (severity==Status.ERROR)
       	return status;
       
       if (severity==Status.WARNING)
       {
         Choice result = env_.getStatusHandler().report(status, new Choice[]{OKChoice, CancelChoice});
         if (result.getLabel().equals(CancelChoice.getLabel()))
         {
         	 //return an error status since the user canceled
         	  return new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_SAMPLE_CREATION_CANCELED"), Status.ERROR);
         }
         	
       }
       //                           

       JavaMofFieldVisitorAction fieldVisitorAction = new JavaMofFieldVisitorAction(beanElement,clientProject, env_);
       //fieldVisitorAction.setStatusMonitor(getStatusMonitor());
       Vector childVector2 = ((Vector)getBeansCreated().clone());
       childVector2.addElement(javaClass.getName());  
       fieldVisitorAction.setBeansCreated(childVector2);
       fieldVisitorAction.setReturnParam(getReturnParam());
       JavaMofFieldVisitor fieldVisitor = new JavaMofFieldVisitor();
       fieldVisitor.setProject(getProject());
       status = fieldVisitor.run(javaClass,fieldVisitorAction);
       //
       severity = status.getSeverity(); 
       if (severity==Status.ERROR)
       	return status;
       
       if (severity==Status.WARNING)
       {
         Choice result = env_.getStatusHandler().report(status, new Choice[]{OKChoice, CancelChoice});
         if (result.getLabel().equals(CancelChoice.getLabel()))
         {
         	 //return an error status since the user canceled
         	  return new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_SAMPLE_CREATION_CANCELED"), Status.ERROR);
         }
         	
       }
       //       
       
   }

    //this is to insure we are on the proxy
    //now do the methods
    if (beanElement == fModel.getRootElement()){
      //first lets make sure the proxy is in good form
      //-it has a default constructor
      //-it has at least one method
      //otherwise return the problem in the form of a status

      if (!proxyCheck(javaClass, status)) return status;
      JavaMofMethodVisitorAction methodVisitorAction = new JavaMofMethodVisitorAction(beanElement,clientProject, env_);
      
      //methodVisitorAction.setStatusMonitor(getStatusMonitor());
      JavaMofMethodVisitor methodVisitor = new JavaMofMethodVisitor();
      methodVisitor.setMethodSelection(fMethodsSelected);
      status = methodVisitor.run(javaClass,methodVisitorAction);

      //
      int severity = status.getSeverity(); 
      if (severity==Status.ERROR)
      	return status;
      
      if (severity==Status.WARNING)
      {
        Choice result = env_.getStatusHandler().report(status, new Choice[]{OKChoice, CancelChoice});
        if (result.getLabel().equals(CancelChoice.getLabel()))
        {
        	 //return an error status since the user canceled
        	  return new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_SAMPLE_CREATION_CANCELED"), Status.ERROR);
        }
        	
      }
      //                      
      //This is where we check for several things:
      //-if no methods were processed because of unsupported types then we send back that result
      //-if there were any methods omitted we should warn them

      //first no methods
      if (!methodVisitorAction.wereMethodsProcessed()){
        //this has to be done to insure the dialog is an error
      	status = new SimpleStatus("",msgUtils_.getMessage("MSG_ERROR_JTS_NO_PROXY_METHODS_PROCESSED"), Status.ERROR);
      	return status;
        //getStatusMonitor().reportStatus( new Status(IStatus.ERROR,WebServiceConsumptionPlugin.ID,0,
        //		WebServiceConsumptionPlugin.getMessage( "%MSG_ERROR_JTS_NO_PROXY_METHODS_PROCESSED" ),null));
      }        	
      //now methods omitted
      else if(methodVisitorAction.wereMethodsOmitted()){
        //The dialog is already a warning so we just need to set the overall message
      	status = new SimpleStatus("",msgUtils_.getMessage("MSG_WARN_JTS_PROXY_METHODS_OMITTED"), Status.WARNING);
      	return status;
        //getStatusMonitor().reportStatus( new Status(IStatus.WARNING,WebServiceConsumptionPlugin.ID,0,
        //		WebServiceConsumptionPlugin.getMessage( "%MSG_WARN_JTS_PROXY_METHODS_OMITTED" ),null));
      }	
    }
    return status;
  }

  public void initialize(String resident)
  {
    //nothing to be done but must be implemented
  }


  /*
  * Return the model that was created
  * @return Model the bean model that was created
  */
  public Model getModel()
  {
    return fModel;
  }

  /*
  * The proxy check insures there is a default constructor and
  * at Least one method
  * @param JavaClass javaClass is used to traverse the methods on the proxy
  */
  public boolean proxyCheck(JavaClass javaClass, Status status)
  {
     // first check for a method
     Iterator m=javaClass.getPublicMethods().iterator();
     if (!m.hasNext()){
     	status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_JTS_PROXY_HAS_NO_METHODS"),Status.ERROR);
       //getStatusMonitor().reportStatus( new Status(IStatus.ERROR,WebServiceConsumptionPlugin.ID,0,
       //			WebServiceConsumptionPlugin.getMessage( "%MSG_ERROR_JTS_PROXY_HAS_NO_METHODS" ),null));
       return false;
     }	
     //now check for a default constructor
     while (m.hasNext()) {
       Method method=(Method)m.next();
       if (javaClass.getName().equals(method.getName())){
          //now the inputs
          JavaParameter javaParameter[] = method.listParametersWithoutReturn();
          if (javaParameter.length > 0){
             //then we have no default constructor
          	 status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_JTS_PROXY_HAS_NO_DEFAULT"),Status.ERROR);
             //getStatusMonitor().reportStatus( new Status(IStatus.ERROR,WebServiceConsumptionPlugin.ID,0,
             		//WebServiceConsumptionPlugin.getMessage( "%MSG_ERROR_JTS_PROXY_HAS_NO_DEFAULT" ),null));
             return false;
          }
       }
    }
    return true;

  }

 /**
  * Sets the status monitor that this action will use to report status that occres while executing the Action.
  */
  public void setStatusMonitor ( StatusMonitor monitor )
  {
	monitor_ = monitor;
  }

  /**
  * Returns the status monitor that this task is using to report status.
  */
  public StatusMonitor getStatusMonitor ()
  {
    if (monitor_ == null)
    {
      monitor_ = new NullStatusMonitor();
    }
    return monitor_;
  }


   /**
  * check if the action has finished sucsessfully 
  */
  /*
  public boolean isSuccessful ()
  {
   	return getStatusMonitor().canContinue();
  }
  */ 
  public void resetBeansCreated()
  {
    fBeansCreated = new Vector();
  }

  public void setEnvironment(Environment env)
  {
  	env_ = env;
  }
  
  /*
  * The command that called this will get the results of the operation in the form
  * of a status.
  */
  public Vector getBeansCreated ()
  {
    if(fBeansCreated == null) fBeansCreated = new Vector();
    return fBeansCreated;
  }

  /*
  * The command that called this will get the results of the operation in the form
  * of a status.
  */
  public void setBeansCreated (Vector beansCreated)
  {
    fBeansCreated = beansCreated;
  }

   /*
  * This boolean tells wether we are dealing with a return param 
  */
  public boolean getReturnParam ()
  {
    return fReturnParam;
  }

  /*
  * This boolean tells wether we are dealing with a return param 
  */
  public void setReturnParam (boolean returnparam)
  {
    fReturnParam = returnparam;
  }

   /*
  * This boolean tells wether we are dealing with a return param 
  */
  public String getProject ()
  {
    return clientProject;
  }

  /*
  * This boolean tells wether we are dealing with a return param 
  */
  public void setProject (String clientProject)
  {
    this.clientProject = clientProject;
  }


  /**
  * sets the visitor that calls the visit
  * @parameter Visitor
  */

  public void setVisitor(Visitor visitor)
  {
      fVisitor = visitor;
  }

  /*
  * Sets the parent element the child then knows where to
  * add itself to the model
  * @param Element parent element
  */
  public void setParentElement(Element element)
  {
    fParentElement = element;
  }
}
