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

package org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel;

import java.util.Hashtable;

import org.eclipse.jem.java.JavaHelpers;
/**
 * TypeFactory
 * Creation date: (4/10/2001 12:41:48 PM)
 * @author: Gilbert Andrews
 */
public class TypeFactory 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";


  public static final String URL_NAME                   = "java.net.URL";
  public static final String STRING_NAME               = "java.lang.String";
  public static final String BIG_DECIMAL_NAME          = "java.math.BigDecimal";
  public static final String BIG_INTEGER_NAME          = "java.math.BigInteger";
  public static final String DATE_NAME                 = "java.util.Date";
  public static final String GREGORIAN_CALENDAR_NAME = "java.util.GregorianCalendar";
  public static final String CALENDAR_NAME             = "java.util.Calendar";
  public static final String BOOLEAN_NAME              = "java.lang.Boolean";
  public static final String BYTE_NAME                  = "java.lang.Byte";
  public static final String DOUBLE_NAME               = "java.lang.Double";
  public static final String FLOAT_NAME                 = "java.lang.Float";
  public static final String INTEGER_NAME               = "java.lang.Integer";
  public static final String LONG_NAME                  = "java.lang.Long";
  public static final String SHORT_NAME                 = "java.lang.Short";
  public static final String CHARACTER_NAME            = "java.lang.Character";
  public static final String PRIM_BOOLEAN_NAME         = "boolean";
  public static final String PRIM_BYTE_NAME            = "byte";
  public static final String PRIM_DOUBLE_NAME          = "double";
  public static final String PRIM_FLOAT_NAME           = "float";
  public static final String PRIM_INTEGER_NAME         = "int";
  public static final String PRIM_LONG_NAME            = "long";
  public static final String PRIM_SHORT_NAME           = "short";
  public static final String PRIM_CHAR_NAME            = "char";
  public static final String VOID_NAME                  = "void";

  //some special complex
  public static final String DOM_ELEMENT_NAME   = "org.w3c.dom.Element";
  public static final String SOAP_ELEMENT_NAME = "javax.xml.soap.SOAPElement";
  public static final String STATELESS_BEAN = BeanElement.STATELESS_BEAN;



  public static Hashtable javaTypes = new Hashtable();
  
  public TypeFactory(){}

  static{
    javaTypes.put(BOOLEAN_NAME,new PrimitiveJavaTypes(BOOLEAN_NAME));
    javaTypes.put(BYTE_NAME,   new PrimitiveJavaTypes(BYTE_NAME));
    javaTypes.put(DOUBLE_NAME, new PrimitiveJavaTypes(DOUBLE_NAME));
    javaTypes.put(FLOAT_NAME,  new PrimitiveJavaTypes(FLOAT_NAME));
    javaTypes.put(INTEGER_NAME,new PrimitiveJavaTypes(INTEGER_NAME));
    javaTypes.put(LONG_NAME,   new PrimitiveJavaTypes(LONG_NAME));
    javaTypes.put(SHORT_NAME,  new PrimitiveJavaTypes(SHORT_NAME));
    javaTypes.put(CHARACTER_NAME,  new PrimitiveJavaTypes(CHARACTER_NAME));
  }


  /*
  * This function allows us to handle recognized beans 
  * in displaying and generating code for them instead of 
  * treating them as complex types
  */ 
  public static boolean recognizedBean(String type)
  {

    //     
    //current recognized beans (mostly java.lang)
    if (type.equals(BOOLEAN_NAME)             ||
        type.equals(BYTE_NAME)               ||
        type.equals(DOUBLE_NAME)             ||
        type.equals(FLOAT_NAME)               ||
        type.equals(INTEGER_NAME)             ||
        type.equals(LONG_NAME)               ||
        type.equals(SHORT_NAME)              ||      
        type.equals(CHARACTER_NAME)          ||      
        type.equals(DOM_ELEMENT_NAME)        ||      
	    type.equals(SOAP_ELEMENT_NAME)        ||
        type.equals(BIG_DECIMAL_NAME)         ||
        type.equals(BIG_INTEGER_NAME)         ||
        type.equals(DATE_NAME)                ||
        type.equals(GREGORIAN_CALENDAR_NAME) ||
        type.equals(CALENDAR_NAME)           || 
        type.equals(URL_NAME)                 ||    
        type.equals(STRING_NAME))  return true;      
     return false; 
  
  }

  /*
  * This function allows us to handle unsupported types 
  * if we dont want to support a type and we want to omitt this  
  * method from the samples
  * @param javaHelpers this is the type to be evaluated
  * @return boolean true if the type is not supported
  */ 
  public static boolean isUnSupportedType(JavaHelpers javaHelpers)
  {
    //we also dont support arrays
    if (javaHelpers.isArray()||
    		javaHelpers.getJavaName().equals(MAP_NAME)||
    		javaHelpers.getJavaName().equals(VECTOR_NAME)||
    		javaHelpers.getJavaName().equals("java.lang.class")	) return true;
    
    return false;
  }

  public static String HASHTABLE_NAME = "java.util.Hashtable";
  public static String MAP_NAME = "java.util.Map";
  public static String VECTOR_NAME = "java.util.Vector";
  public static String ARRAY_NAME = ArrayElement.ARRAY_NAME;
  public static String PRIMITIVE_ARRAY_NAME = "primitivearray";

  public static boolean isRecognizedReturnType(JavaHelpers javaHelpers)
  {
    //arrays 
    //Hashtable
    //Vectors
    if (javaHelpers.isArray()) return true;
    else if (javaHelpers.getJavaName().equals(HASHTABLE_NAME))return true;
    else if (javaHelpers.getJavaName().equals(VECTOR_NAME))return true;
    else if (javaHelpers.getJavaName().equals(MAP_NAME))return true;
    else return false;
  }

  public static boolean isRecognizedReturnType(String type)
  {
    //arrays 
    //Hashtable
    //Vectors
    if (type.startsWith(ARRAY_NAME)) return true;
    else if (type.equals(HASHTABLE_NAME))return true;
    else if (type.equals(VECTOR_NAME))return true;
    else if (type.equals(MAP_NAME))return true;
    else return false;
  }

  public static boolean isStateLessBean(String type)
  {
    if(type.startsWith(STATELESS_BEAN)) return true;
    return false;
  }


  /**
  * Creates a type based on the JavaHelper then sets this type in the element
  * @params JavaHelpers this holds the type
  * @param TypeElement The element which will hold the type created
  * @param String Uniquename to be used when naming temp variables
  */
 
  public static DataType createType(String type, String UniqueName)
  {
    //this guy might be both stateless and one of the below elements
    //we would like to be as specific as we can, stateless is the worst case 
    boolean stateless = false;  
    if(type.startsWith(STATELESS_BEAN)) {
      stateless = true;
      type = type.substring(29); 
    }
   
    DataType datatype = null;
    if      (type.equals(PRIM_BOOLEAN_NAME)) datatype = new PrimitiveBooleanType();
    else if (type.equals(PRIM_BYTE_NAME))     datatype = new PrimitiveByteType();
    else if (type.equals(PRIM_DOUBLE_NAME))  datatype = new PrimitiveDoubleType();
    else if (type.equals(PRIM_FLOAT_NAME))   datatype = new PrimitiveFloatType();
    else if (type.equals(PRIM_LONG_NAME))    datatype = new PrimitiveLongType();
    else if (type.equals(PRIM_INTEGER_NAME)) datatype = new PrimitiveIntType();
    else if (type.equals(PRIM_CHAR_NAME))    datatype = new PrimitiveCharType();
    else if (type.equals(PRIM_SHORT_NAME))   datatype = new PrimitiveShortType();
    

    //see if this critter is a javatype
    else if (type.equals(BOOLEAN_NAME)     ||
            type.equals(BYTE_NAME)        ||
            type.equals(DOUBLE_NAME)      ||
            type.equals(FLOAT_NAME)       ||
            type.equals(INTEGER_NAME)     ||
            type.equals(LONG_NAME)        ||
            type.equals(CHARACTER_NAME)  ||
            type.equals(SHORT_NAME))        datatype = (DataType)javaTypes.get(type);   

    //see if its a case we handle
    else if (type.equals(STRING_NAME))               datatype = new StringType(); 
    else if (type.equals(VOID_NAME))                 datatype = new VoidType();
    else if (type.equals(DOM_ELEMENT_NAME))          datatype = new DomElementType();
    else if (type.equals(SOAP_ELEMENT_NAME))         datatype = new SOAPElementType();  
    else if (type.equals(BIG_DECIMAL_NAME))          datatype = new BigDecimalType();
    else if (type.equals(BIG_INTEGER_NAME))          datatype = new BigIntegerType();
    else if (type.equals(DATE_NAME))                  datatype = new DateType();
    else if (type.equals(URL_NAME))                   datatype = new URLType(); 
    else if (type.equals(GREGORIAN_CALENDAR_NAME))  datatype = new GregorianCalendarType();
    else if (type.equals(CALENDAR_NAME))  datatype = new GregorianCalendarType();

    //it could be a return type we support
    else if (type.equals(HASHTABLE_NAME))            datatype = new HashtableType();
    else if (type.equals(VECTOR_NAME))               datatype = new VectorType();
    else if (type.equals(MAP_NAME))                  datatype = new MapType();

    // need to see if it is an object array
    //or a primitive
    else if (type.startsWith(ARRAY_NAME)){              
    	String name = type.substring(21); 
        if(name.startsWith(PRIM_BOOLEAN_NAME) ||
          name.startsWith(PRIM_BYTE_NAME)     ||
          name.startsWith(PRIM_DOUBLE_NAME)  ||
          name.startsWith(PRIM_FLOAT_NAME)    ||
          name.startsWith(PRIM_LONG_NAME)    ||
          name.startsWith(PRIM_INTEGER_NAME)  ||
          name.startsWith(PRIM_CHAR_NAME)    ||
          name.startsWith(PRIM_SHORT_NAME))
            datatype = new PrimitiveArrayType(name);  
        else
        	datatype = new ArrayType(name);

    }
    else if (stateless){
        datatype = new StateLessBeanType(type);

    }


    datatype.setUniqueName(UniqueName);
    return datatype;
     
  }
  
  


}
