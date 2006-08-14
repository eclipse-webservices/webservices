/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060811   153482 mahutch@ca.ibm.com - Mark Hutchinson
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.codegen;

import java.util.Vector;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;



/**
* Objects of this class represent a generator.
* */
public class Generator implements VisitorAction
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";


  /**
  * The StringBuffer that holds the codegen 
  * */
  protected StringBuffer fbuffer; 

  /**
  * The Visitor that calls the visit method 
  * */
  protected Visitor fVisitor; 

  /**
  * Vector that holds state data for this  generator 
  * */
  protected Vector fResidentVector;
 
  /*
  * This int holds the current number 
  */
  protected int fUniqueNumber =0;
 

  protected String fClientFolderPath;

  public static final String FACTORY = "Factory"; 

  public static final String TAB = "    ";
  public static final String DOUBLE_TAB = "        ";
  public static final String SPACE = " ";
  
  /**
  * Constructor.
  * 
  */
  public Generator ()
  {
      fbuffer = new StringBuffer();
      fResidentVector = new Vector(); 
    
  }

  /**
  * Constructor.
  * @param StringBuffer Takes the buffer it will write code to
  */
  public Generator (StringBuffer buffer)
  {
      fbuffer = buffer;
      fResidentVector = new Vector(); 
  }
  
  public void initialize(String resident)
  {
    //nothing to be done but must be implemented
  }
  
  public String getClientFolderPath()
  {
    return fClientFolderPath;
  }

  public void setClientFolderPath(String fClientFolderPath_)
  {
    fClientFolderPath = fClientFolderPath_;
  }

  protected String serviceName = "";
  protected String portName = "";
  protected String packageName = "";
  protected String proxyBaseName = "";

  public void setInfo(String service, String port, String packageName, String proxyBase)
  {
    serviceName = service;
    portName = port;
    this.packageName = packageName; 
    proxyBaseName = proxyBase;  
  }

  private static final char UNDERSCORE = '_';
  public String getSessionBeanId()
  {
    String name = getClientFolderPath();
    int index = name.lastIndexOf("/");
    index++;
    StringBuffer newName = new StringBuffer(name.substring(index));
    
    //We need to make this a valid java identifier
    //To do this we will replace any character that is not valid with an underscore
    for (int i = 0; i < newName.length(); i++)
    {
    	char c = newName.charAt(i);
    	if (i == 0 && !Character.isJavaIdentifierStart(c) || !Character.isJavaIdentifierPart(c))
    	{
    		newName.setCharAt(i,UNDERSCORE);
    	}
    }
    return newName + "id";
  }

  /*
  * The Number Factory is here for naming purposes. In 
  * this way we insure no names we create in a piece of
  * code gen are the same. We keep track of the numbers
  * used and we hand back the next one. Like take a number 
  * buddy
  * @param int number we are at as the state data has to be settable
  */
  public void setNumberFactory(int number)
  {
     fUniqueNumber = number;
  }


  /*
  * This is a unique number for state purposes
  * @return int 
  */
  public int getNumberFactory()
  {
     return fUniqueNumber; 
  }
  /*
  * This is a unique number for naming purposes
  * @return int 
  */
  public int getUniqueNumber()
  {
     return fUniqueNumber++;
  }


  /**
  * returns the current StringBuffer
  * @return StringBuffer Takes the buffer it will write code too
  */
  public StringBuffer getStringBuffer ()
  {
      return fbuffer;
  }

  
  /**
  * returns the Visitor that called this generator
  * @return Visitor 
  */
  public Visitor getVisitor ()
  {
      return fVisitor;
  }
  



  /**
  * vector used to hold state data 
  * @parameter Visitor 
  */
  public void putResidentVector (Object object)
  {
      fResidentVector.addElement(object);
  }
  

  /**
  * vector used to hold state data 
  * @parameter Visitor 
  */
  public void setResidentVector (Vector vector)
  {
      fResidentVector = vector;
  }
  

  /**
  * returns the resident vector
  * @return Visitor 
  */
  public Vector getResidentVector ()
  {
      return fResidentVector;
  }
  

  /**
  * This function is used to initialize any state a generator may have
  *
  **/

  public void initialize()
  {
     //to be implemented in sublasses
  }


   /**
  * sets the visitor that calls the visit
  * @parameter Visitor 
  */

  public void setVisitor(Visitor visitor)
  {
      fVisitor = visitor;
  }


  public IStatus visit (Object object)
  {
   //implemented by subclasses
  	return Status.OK_STATUS;
  }
  

}
