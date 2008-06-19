/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060216   115144 pmoogk@ca.ibm.com - Peter Moogk
 * 20060216   127138 pmoogk@ca.ibm.com - Peter Moogk
 * 20060517   141481 pmoogk@ca.ibm.com - Peter Moogk
 * 20070313   176580 makandre@ca.ibm.com - Andrew Mak, Generate a Client WS Proxy accepting URL
 * 20080613   236523 makandre@ca.ibm.com - Andrew Mak, Overwrite setting on Web service wizard is coupled with preference
 *******************************************************************************/

package org.eclipse.jst.ws.internal.axis.consumption.ui.task;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaParameter;
import org.eclipse.jem.java.JavaVisibilityKind;
import org.eclipse.jem.java.Method;
import org.eclipse.jst.ws.internal.common.JavaMOFUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.command.internal.env.eclipse.BaseEclipseEnvironment;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.IStatusHandler;
import com.ibm.icu.util.StringTokenizer;

public class Stub2BeanInfo
{
  private final String NEW_LINE = System.getProperty("line.separator");

  private int indentCount;

  private String package_;
  private StringBuffer imports_;
  private StringBuffer seis_;
  private StringBuffer services_;
  private StringBuffer jndiNames_;
  private StringBuffer ports_;
  private String class_;
  private Vector usedNames;
  
  private IProject clientProject_;
  private String   outputFolder_;
  
  public Stub2BeanInfo()
  {
    indentCount = 0;
    package_ = null;
    imports_ = new StringBuffer();
    seis_ = new StringBuffer();
    services_ = new StringBuffer();
    jndiNames_ = new StringBuffer();
    ports_ = new StringBuffer();
    class_ = "SEIBean";
    usedNames = new Vector();
  }

  public void setClientProject(IProject clientProject) {
  	this.clientProject_ =  clientProject;
  }
  
  public void setOutputFolder( String outputFolder )
  {
  	outputFolder_ = outputFolder;
  }
  
  public void setPackage(String pkgName)
  {
	  package_ = pkgName;
  }

  public void addImports(String pkgName, String className)
  {
    imports_.append(toFullyQualifiedClassName(pkgName, className));
    imports_.append(";");
  }

  public void addSEI(String seiPkgName, String seiClassName, String servicePkgName, String serviceClassName, String portName)
  {
    addSEI(seiPkgName, seiClassName, servicePkgName, serviceClassName, serviceClassName, portName);
  }

  public void addSEI(String seiPkgName, String seiClassName, String servicePkgName, String serviceClassName, String jndiName, String portName)
  {
    seis_.append(toFullyQualifiedClassName(seiPkgName, seiClassName));
    seis_.append(";");
    services_.append(toFullyQualifiedClassName(servicePkgName, serviceClassName));
    services_.append(";");
    jndiNames_.append(jndiName);
    jndiNames_.append(";");
    ports_.append(portName);
    ports_.append(";");
  }

  public void setClass(String className)
  {
	  class_ = className;
  }

  private String toFullyQualifiedClassName(String pkgName, String className)
  {
    StringBuffer sb = new StringBuffer();
    sb.append(pkgName);
    sb.append(".");
    sb.append(className);
    return sb.toString();
  }

  private String getPackageName(String qname)
  {
    int index = qname.lastIndexOf(".");
    if (index != -1)
      return qname.substring(0, index);
    else
      return qname;
  }

  private String getClassName(String qname)
  {
    int index = qname.lastIndexOf(".");
    if (index != -1)
      return qname.substring(index+1, qname.length());
    else
      return qname;
  }

  private String firstCharToLowerCase(String s)
  {
    StringBuffer sb = new StringBuffer();
    sb.append(s.substring(0, 1).toLowerCase());
    sb.append(s.substring(1, s.length()));
    return sb.toString();
  }

  private String getFullyQualifiedName(JavaHelpers javaHelpers)
  {
    if (javaHelpers.isPrimitive())
      return javaHelpers.getJavaName();
    else
      return javaHelpers.getQualifiedName();
  }

  public void write(IEnvironment env, IProgressMonitor progressMonitor) throws CoreException, IOException
  {
	IStatusHandler statusMonitor = env.getStatusHandler();
	
    IPath        outputPath = new Path( outputFolder_ );
    IProject     project    = ResourceUtils.getProjectOf( outputPath );
    StringWriter sw         = new StringWriter(2048);
    
    writePackage(sw);
    writeImports(sw);
    writeClass(sw);
    sw.close();
    byte[] bytes = sw.getBuffer().toString().getBytes( project.getDefaultCharset() );
    StringBuffer sb = new StringBuffer();
    if (package_ != null && package_.length() > 0)
    {
      sb.append(package_);
      sb.append(".");
    }
    sb.append(class_);
    sb = new StringBuffer(sb.toString().replace('.', '/'));
    sb.append(".java");

    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
    
    IPath newFilePath = new Path(outputFolder_).append( sb.toString() );
    
    FileResourceUtils.createFile(((BaseEclipseEnvironment) env).getResourceContext(), newFilePath, bais, progressMonitor, statusMonitor);
  }

  private void writePackage(Writer w) throws IOException
  {
    if (package_ != null && package_.length() > 0)
    {
      w.write("package ");
      w.write(package_);
      w.write(";");
      newLine(w);
    }
  }

  private void writeImports(Writer w) throws IOException
  {
    StringTokenizer st = new StringTokenizer(imports_.toString(), ";");
    while (st.hasMoreTokens())
    {
      w.write("import ");
      w.write(st.nextToken());
      w.write(";");
      newLine(w);
    }
  }

  private void writeClass(Writer w) throws IOException, CoreException
  {
    newLine(w);
    w.write("public class ");
    w.write(class_);
    w.write(" ");
    StringTokenizer st = new StringTokenizer(seis_.toString(), ";");
    if (st.hasMoreTokens())
      w.write("implements ");
    while (st.hasMoreTokens())
    {
      w.write(st.nextToken());
      if (st.hasMoreTokens())
        w.write(", ");
    }
    w.write(" {");
    incrementIndent();
    newLine(w);
    writeFieldsDeclarations(w);
    writeConstructor(w);
    writeInit(w);
    /*
    * Cannot use JNDI lookup for AXIS
    *
    writeUseJNDI(w);
    */
    writeGetSetEndpoint(w);
    writeSEIGetters(w);
    writeSEIMethods(w);
    decrementIndent();
    newLine(w);
    w.write("}");
  }

  private void writeFieldsDeclarations(Writer w) throws IOException
  {
    /*
    * Cannot use JNDI lookup for AXIS
    *
    w.write("private boolean _useJNDI = true;");
    newLine(w);
    */
    w.write("private String _endpoint = null;");
    usedNames.add("_endpoint");
    newLine(w);
    StringTokenizer st = new StringTokenizer(seis_.toString(), ";");
    while (st.hasMoreTokens())
    {
      w.write("private ");
      String sei = st.nextToken();
      w.write(sei);
      w.write(" ");
      String stubName = firstCharToLowerCase(getClassName(sei));
      w.write(stubName);
      usedNames.add(stubName);
      w.write(" = null;");
      newLine(w);
    }
  }

  private void writeConstructor(Writer w) throws IOException
  {
    newLine(w);
    w.write("public ");
    w.write(class_);
    w.write("() {");
    incrementIndent();
    newLine(w);
    w.write("_init");
    w.write(class_);
    w.write("();");
    decrementIndent();
    newLine(w);
    w.write("}");
    newLine(w);
    
    newLine(w);
    w.write("public ");
    w.write(class_);
    w.write("(String endpoint) {");
    incrementIndent();
    newLine(w);
    w.write("_endpoint = endpoint;");
    newLine(w);
    w.write("_init");
    w.write(class_);
    w.write("();");
    decrementIndent();
    newLine(w);
    w.write("}");
    newLine(w);
  }

  private void writeInit(Writer w) throws IOException
  {
    newLine(w);
    w.write("private void _init");
    w.write(class_);
    w.write("() {");
    incrementIndent();
    newLine(w);
    w.write("try {");
    incrementIndent();
    newLine(w);
    /*
    * Cannot use JNDI lookup for AXIS
    *
    w.write("if (_useJNDI) {");
    incrementIndent();
    newLine(w);
    */
    StringTokenizer st = new StringTokenizer(seis_.toString(), ";");
    StringTokenizer serviceTokens = new StringTokenizer(services_.toString(), ";");
    //StringTokenizer jndiNameTokens = new StringTokenizer(jndiNames_.toString(), ";");
    StringTokenizer portTokens = new StringTokenizer(ports_.toString(), ";");
    /*
    if (st.hasMoreTokens())
    {
      w.write("javax.naming.InitialContext ctx = new javax.naming.InitialContext();");
      newLine(w);
    }
    while (st.hasMoreTokens())
    {
      w.write(firstCharToLowerCase(getClassName(st.nextToken())));
      w.write(" = ((");
      w.write(serviceTokens.nextToken());
      w.write(")ctx.lookup(\"java:comp/env/service/");
      w.write(jndiNameTokens.nextToken());
      w.write("\")).get");
      w.write(mangleClassName(portTokens.nextToken()));
      w.write("();");
      newLine(w);
    }
    decrementIndent();
    newLine(w);
    w.write("}");
    newLine(w);
    w.write("else {");
    incrementIndent();
    newLine(w);
    st = new StringTokenizer(seis_.toString(), ";");
    serviceTokens = new StringTokenizer(services_.toString(), ";");
    portTokens = new StringTokenizer(ports_.toString(), ";");
    */
    while (st.hasMoreTokens())
    {
      String seiName = firstCharToLowerCase(getClassName(st.nextToken()));
      w.write(seiName);
      w.write(" = (new ");
      w.write(serviceTokens.nextToken());
      w.write("Locator()).get");
      w.write(portTokens.nextToken());
      w.write("();");
      newLine(w);
      w.write("if (");
      w.write(seiName);
      w.write(" != null) {");
      incrementIndent();
      newLine(w);
      w.write("if (_endpoint != null)");
      incrementIndent();
      newLine(w);
      w.write("((javax.xml.rpc.Stub)");
      w.write(seiName);
      w.write(")._setProperty(\"javax.xml.rpc.service.endpoint.address\", _endpoint);");
      decrementIndent();
      newLine(w);
      w.write("else");
      incrementIndent();
      newLine(w);
      w.write("_endpoint = (String)((javax.xml.rpc.Stub)");
      w.write(seiName);
      w.write(")._getProperty(\"javax.xml.rpc.service.endpoint.address\");");
      decrementIndent();
      decrementIndent();
      newLine(w);
      w.write("}");
      newLine(w);
    }
    /*
    decrementIndent();
    newLine(w);
    w.write("}");
    */
    decrementIndent();
    newLine(w);
    w.write("}");
    newLine(w);
    /*
    w.write("catch (javax.naming.NamingException namingException) {}");
    newLine(w);
    */
    w.write("catch (javax.xml.rpc.ServiceException serviceException) {}");
    decrementIndent();
    newLine(w);
    w.write("}");
    newLine(w);
  }

  private void writeSetPropertyEndpoint(Writer w) throws IOException
  {
    StringTokenizer st = new StringTokenizer(seis_.toString(), ";");
    while (st.hasMoreTokens())
    {
      String seiName = firstCharToLowerCase(getClassName(st.nextToken()));
      w.write("if (");
      w.write(seiName);
      w.write(" != null)");
      incrementIndent();
      newLine(w);
      w.write("((javax.xml.rpc.Stub)");
      w.write(seiName);
      w.write(")._setProperty(\"javax.xml.rpc.service.endpoint.address\", _endpoint);");
      decrementIndent();
      newLine(w);
    }
  }

 
  private void writeSEIGetters(Writer w) throws IOException
  {
    StringTokenizer st = new StringTokenizer(seis_.toString(), ";");
    while (st.hasMoreTokens())
    {
      String sei = st.nextToken();
      String seiClassName = getClassName(sei);
      String seiFieldName = firstCharToLowerCase(seiClassName);
      newLine(w);
      w.write("public ");
      w.write(sei);
      w.write(" get");
      w.write(seiClassName);
      w.write("() {");
      incrementIndent();
      newLine(w);
      w.write("if (");
      w.write(seiFieldName);
      w.write(" == null)");
      incrementIndent();
      newLine(w);
      w.write("_init");
      w.write(class_);
      w.write("();");
      decrementIndent();
      newLine(w);
      w.write("return ");
      w.write(seiFieldName);
      w.write(";");
      decrementIndent();
      newLine(w);
      w.write("}");
      newLine(w);
    }
  }

  private void writeSEIMethods(Writer w) throws IOException, CoreException
  {
    newLine(w);
    //WebServiceElement wse = WebServiceElement.getWebServiceElement(model_);
    if (clientProject_ == null) return;//wse.getProxyProject();
    StringTokenizer st = new StringTokenizer(seis_.toString(), ";");
    while (st.hasMoreTokens())
    {
      String sei = st.nextToken();
      JavaClass javaClass = JavaMOFUtils.getJavaClass(getPackageName(sei), getClassName(sei), clientProject_);
      if (javaClass != null)
      {
        List methods = javaClass.getMethods();
        for (Iterator it = methods.iterator(); it.hasNext();)
        {
          Method method = (Method)it.next();
          if (!method.isConstructor() && !method.isNative() && method.getJavaVisibility().getValue() == JavaVisibilityKind.PUBLIC)
            writeSEIMethods(w, javaClass, method);
        }
      }
    }
  }

  private void writeSEIMethods(Writer w, JavaClass javaClass, Method method) throws IOException
  {
    w.write("public ");
    // isVoid
    if (method.isVoid())
      w.write("void ");
    else
    {
      w.write(getFullyQualifiedName(method.getReturnType()));
      w.write(" ");
    }
    // method name
    w.write(method.getName());
    w.write("(");
    // input parameters
    JavaParameter[] inputParams = method.listParametersWithoutReturn();
    for (int i = 0; i < inputParams.length; i++)
    {
      if (i > 0)
        w.write(", ");
      JavaHelpers javaHelpers = inputParams[i].getJavaType();
      w.write(getFullyQualifiedName(javaHelpers));
      w.write(" ");
      String paramName = getClassName(inputParams[i].getQualifiedName());
      w.write(getUnusedName(paramName));
    }
    w.write(")");
    // exceptions
    List exceptions = method.getJavaExceptions();
    if (!exceptions.isEmpty())
      w.write(" throws ");
    for (Iterator it = exceptions.iterator(); it.hasNext();)
    {
      JavaClass exception = (JavaClass)it.next();
      w.write(exception.getQualifiedNameForReflection());
      if (it.hasNext())
        w.write(", ");
    }
    // method body
    w.write("{");
    incrementIndent();
    newLine(w);
    String stubName = firstCharToLowerCase(javaClass.getName());
    w.write("if (");
    w.write(stubName);
    w.write(" == null)");
    incrementIndent();
    newLine(w);
    w.write("_init");
    w.write(class_);
    w.write("();");
    decrementIndent();
    newLine(w);
    if (!method.isVoid())
      w.write("return ");
    w.write(stubName);
    w.write(".");
    w.write(method.getName());
    w.write("(");
    for (int i = 0; i < inputParams.length; i++)
    {
      if (i > 0)
        w.write(", ");
      String paramName = getClassName(inputParams[i].getQualifiedName());
      w.write(getUnusedName(paramName));
    }
    w.write(");");
    decrementIndent();
    newLine(w);
    w.write("}");
    newLine(w);
    newLine(w);
  }

  private void writeGetSetEndpoint(Writer w) throws IOException
  {
    newLine(w);
    w.write("public String getEndpoint() {");
    incrementIndent();
    newLine(w);
    w.write("return _endpoint;");
    decrementIndent();
    newLine(w);
    w.write("}");
    newLine(w);
    newLine(w);
    w.write("public void setEndpoint(String endpoint) {");
    incrementIndent();
    newLine(w);
    w.write("_endpoint = endpoint;");
    newLine(w);
    writeSetPropertyEndpoint(w);
    decrementIndent();
    newLine(w);
    w.write("}");
    newLine(w);
  }

  private void incrementIndent()
  {
    indentCount++;
  }

  private void decrementIndent()
  {
    indentCount--;
  }

  private void indent(Writer w) throws IOException
  {
    for (int i = 0; i < indentCount; i++)
      w.write("  ");
  }

  private void newLine(Writer w) throws IOException
  {
    w.write(NEW_LINE);
    indent(w);
  }
  
  private String getUnusedName(String name)
  {
    if (usedNames.contains(name))
    {
      for (int i = 0; i < 100; i++)
      {
        String newName = (new StringBuffer(name)).append(String.valueOf(i)).toString();
        if (!usedNames.contains(newName))
          return newName;
      }
    }
    return name;
  }
}
