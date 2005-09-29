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
package org.eclipse.jst.ws.internal.consumption.sampleapp.codegen;

import java.util.Vector;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.common.StringUtils;
import org.eclipse.jst.ws.internal.consumption.codegen.bean.AttributeVisitor;
import org.eclipse.jst.ws.internal.consumption.codegen.bean.FieldVisitor;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.AttributeElementType;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.BeanElement;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.DataType;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.SimpleElement;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.TypeElement;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.TypeFactory;


/**
* Objects of this class represent a InputFileTypeGenerator.
* */
public class InputFileTypeGenerator extends InputFileHelp2Generator 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

   
  protected String fParentGetters;
  protected int fLevelsDeep;
  protected int fColspan;
  protected int fCurrentLevel;
  private boolean fIsSimple;
  private boolean fStateLessBean;
  private Vector fGetters;
  private Vector fTypes;

  /**
  * Constructor.
  * 
  */
  public InputFileTypeGenerator (StringBuffer buffer, int currentLevel)
  {
     super(buffer);
     fCurrentLevel = currentLevel;
     fLevelsDeep = -1;
     fParentGetters = "";
  }

  /**
  * Constructor.
  * 
  */
  public InputFileTypeGenerator (StringBuffer buffer, int currentLevel,int levelsDeep)
  {
     super(buffer);
     fCurrentLevel = currentLevel;
     fLevelsDeep = levelsDeep;
     fParentGetters = "";
  }

  public void setParentGetters(Vector getter,Vector type)
  {
     if(fGetters != null){
       fGetters.clear();
       fTypes.clear();
     }
     addParentGetter(getter,type);
  }


  /**
  * This function holds some state data that is important in getting the 
  * Strings for display of resultant beans
  * @param String getter this is the getter of an attribute
  * The idea is that by the time we get to the simple type
  * we have getAddress().getStreet()....
  */
  public void addParentGetter(String getter, String type)
  {
    if(fGetters == null) fGetters = new Vector();
    if(fTypes == null) fTypes = new Vector();
    fGetters.addElement(getter);
    fTypes.addElement(type);
  }

  /*
  * this will be used when passing between two visitors
  *
  */
  public void addParentGetter(Vector getters, Vector types)
  {
    if(fGetters == null) fGetters = new Vector();
    if(fTypes == null) fTypes = new Vector();
    int size = getters.size();    
    for(int i = 0; i<size; i++){
      fGetters.addElement(getters.get(i));
      fTypes.addElement(types.get(i));
    }
  }

  protected String returnParentGetter(int count)
  {
    if (fGetters == null || count > fGetters.size()) return null;
    return (String)fGetters.get(count);  
  }

  protected String returnParentGetterType(int count)
  {
    if (fTypes == null || count > fTypes.size()) return null;
    return (String)fTypes.get(count);  
  }

  protected Vector returnParentGetter()
  {
    if (fGetters == null ) return new Vector();
    return fGetters;  
  }

  protected Vector returnParentGetterType()
  {
    if (fTypes == null ) return new Vector();
    return fTypes;  
  }

  protected int getterCount()
  {
    if(fGetters == null) return 0;
    return fGetters.size(); 
  }


  /**
  * This function returns some state data that is important in getting the 
  * Strings for display of resultant beans
  * @return String the string of getters to this point
  * The idea is that by the time we get to the simple type
  * we have getAddress().getStreet()....
  */
  public String getParentGetters()
  {
    return fParentGetters;
  }
  
  /*
  * Takes in an object to be acted upon by this visitor action
  * @param Object The object to be acted upon
  */
  public IStatus visit (Object object)
  {
     TypeElement element = (TypeElement)object;
          
     if (element instanceof SimpleElement) fIsSimple = true;
     else fIsSimple = false;

     //this could be a statelessbean need to know
     if (element instanceof BeanElement ){
        BeanElement bean = (BeanElement)element;
        if (bean.isStateLess()) fStateLessBean = true;
     }



     //Complex type support
     //first I need to find out how many levels deep
     if(fLevelsDeep == -1){
        AttributeVisitor attributeVisitor = new AttributeVisitor();
        LevelsDeepVisitorAction ldva = new LevelsDeepVisitorAction();
        attributeVisitor.run(element,ldva);
        fLevelsDeep = ldva.getLevelsDeep();
        //check out the fields 
        FieldVisitor fieldVisitor = new FieldVisitor();
        LevelsDeepVisitorAction ldva2 = new LevelsDeepVisitorAction();
        fieldVisitor.run(element,ldva2);

        if(fLevelsDeep < ldva2.getLevelsDeep())fLevelsDeep = ldva2.getLevelsDeep(); 
        fLevelsDeep++;
     } 
     fColspan = fLevelsDeep - fCurrentLevel;

     //if we have a return param with no getter make sure we are on the 
     //attribute and not the parameter then return
     if(getReturnParam() &&  element.getOwningElement() instanceof AttributeElementType &&(((AttributeElementType)element.getOwningElement()).getGetterMethod() == null) ) return Status.OK_STATUS;

     //Code gen for all elements
     fbuffer.append("<TR>" + StringUtils.NEWLINE);
     for (int i = 0; i < fCurrentLevel;i++){
        fbuffer.append("<TD WIDTH=\"5%\"></TD>" + StringUtils.NEWLINE);
     }
     fbuffer.append("<TD COLSPAN=\"" + fColspan + "\" ALIGN=\"LEFT\">" + element.getOwningElement().getName() + ":</TD>" + StringUtils.NEWLINE);   
     if(fIsSimple || TypeFactory.recognizedBean(element.getName()) 
                || (getReturnParam() && fStateLessBean) 
                || (getReturnParam() && TypeFactory.isRecognizedReturnType(element.getTypeName()))){
        if(getInstanceName().equals("")){
           DataType dataType = TypeFactory.createType(element.getName(),element.getOwningElement().getMUID());
           fbuffer.append(dataType.inputForm(element.getOwningElement().getMUID()));
        }
        else{
           if(getReturnParam() && (TypeFactory.isRecognizedReturnType(element.getTypeName()) || fStateLessBean)){
             DataType dataType = TypeFactory.createType(element.getTypeName(),element.getOwningElement().getMUID());  
             String uniqueName = "type" + dataType.getUniqueName();
             fbuffer.append("<TD>" + StringUtils.NEWLINE);
             fbuffer.append("<%" + StringUtils.NEWLINE);
             fbuffer.append("if(" +  getInstanceName() + " != null){" + StringUtils.NEWLINE);
             String afterString = getInstanceName(); 
             String tmpString = "tebece";
             String prevString = getInstanceName();
             String newTmpString = "";
             for(int i=0;i<getterCount();i++){
               newTmpString = tmpString + i;
               fbuffer.append(returnParentGetterType(i) + " " + newTmpString + "=" + prevString + "." + returnParentGetter(i) + ";" + StringUtils.NEWLINE);
               prevString = newTmpString; 
               fbuffer.append("if(" + newTmpString + " != null){" + StringUtils.NEWLINE);
               afterString = newTmpString;
             }
             fbuffer.append(element.getName() + " " + uniqueName + " = " + afterString + "." + ((AttributeElementType)element.getOwningElement()).getGetterMethod()+ ";" + StringUtils.NEWLINE);             
             fbuffer.append(dataType.TypeConversion(uniqueName));
             for(int i=0;i<getterCount();i++){
               fbuffer.append("}");
             }
             fbuffer.append("}%>" + StringUtils.NEWLINE);
             fbuffer.append("</TD>" + StringUtils.NEWLINE);
           }

           else{ 
             fbuffer.append("<TD>" + StringUtils.NEWLINE);
             fbuffer.append("<%" + StringUtils.NEWLINE);
             fbuffer.append("if(" +  getInstanceName() + " != null){" + StringUtils.NEWLINE);
             String afterString = getInstanceName();
             String tmpString = "tebece";
             String prevString = getInstanceName();
             String newTmpString = "";
             for(int i=0;i<getterCount();i++){
               newTmpString = tmpString + i;
               fbuffer.append(returnParentGetterType(i) + " " + newTmpString + "=" + prevString + "." + returnParentGetter(i) + ";" + StringUtils.NEWLINE);
               prevString = newTmpString; 
               fbuffer.append("if(" + newTmpString + " != null){" + StringUtils.NEWLINE);
               afterString = newTmpString;
             }
             fbuffer.append("%>" + StringUtils.NEWLINE);
             fbuffer.append("<%=");
             fbuffer.append(afterString + "." + ((AttributeElementType)element.getOwningElement()).getGetterMethod()+ StringUtils.NEWLINE);
             fbuffer.append("%>");
             fbuffer.append("<%");
             for(int i=0;i<getterCount();i++){
               fbuffer.append("}");
             }
             fbuffer.append("}%>" + StringUtils.NEWLINE);
             fbuffer.append("</TD>" + StringUtils.NEWLINE);
           }
        }
       
     }
         
      //Now carry on down the rest of the bean
     else {
        AttributeVisitor attributeVisitor = new AttributeVisitor();
        attributeVisitor.setResidentVector1(returnParentGetter());
        attributeVisitor.setResidentVector2(returnParentGetterType());
        InputFileAttributeGenerator inputFileAttributeGenerator = new InputFileAttributeGenerator(fbuffer,(fCurrentLevel + 1),fLevelsDeep);
        inputFileAttributeGenerator.setReturnParam(getReturnParam());
        inputFileAttributeGenerator.setInstanceName(fInstanceName);
        attributeVisitor.run(element,inputFileAttributeGenerator);
        fbuffer = inputFileAttributeGenerator.getStringBuffer();
       
        //now do fields very similar
        FieldVisitor fieldVisitor = new FieldVisitor();
        fieldVisitor.setResidentVector1(returnParentGetter());
        fieldVisitor.setResidentVector2(returnParentGetterType());
        InputFileAttributeGenerator inputFileAttributeGenerator2 = new InputFileAttributeGenerator(fbuffer,(fCurrentLevel + 1),fLevelsDeep);
        inputFileAttributeGenerator2.setReturnParam(getReturnParam());
        inputFileAttributeGenerator2.setInstanceName(fInstanceName);
        fieldVisitor.run(element,inputFileAttributeGenerator2);
        fbuffer = inputFileAttributeGenerator2.getStringBuffer();
       
        

     }


      return Status.OK_STATUS;
  }
}
