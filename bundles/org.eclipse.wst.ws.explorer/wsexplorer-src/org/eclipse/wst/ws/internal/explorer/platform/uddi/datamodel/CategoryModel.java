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
package org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javax.servlet.ServletContext;
import org.eclipse.wst.ws.internal.datamodel.BasicModel;
import org.eclipse.wst.ws.internal.datamodel.Element;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.util.HTMLUtils;
import org.uddi4j.util.KeyedReference;

public class CategoryModel extends BasicModel
{
  private ServletContext application_;
  private String defaultDataFile_;
  private Hashtable categoryElements_;
  private String columnDelimiter_;
  private String stringDelimiter_;
  private String categoryKey_;
  private boolean checked_;
  private String displayName_;
  private String tModelKey_;
  private Throwable errorException_;
  
  // Return codes for loading and saving category data.
  public static final byte OPERATION_SUCCESSFUL = 0x00;
  public static final byte ERROR_FILE = 0x01;
  public static final byte ERROR_SECURITY = 0x02;
  public static final byte ERROR_ENCODING = 0x03;
  public static final byte ERROR_IO = 0x04;
  public static final byte ERROR_CATEGORY_KEY = 0x05;
  
  private final String defaultColumnDelimiter_ = "#";
  private final String defaultStringDelimiter_ = "\"";
  
  public CategoryModel()
  {
    super("categories");
    application_ = null;
	defaultDataFile_ = null;
    columnDelimiter_ = defaultColumnDelimiter_;
    stringDelimiter_ = defaultStringDelimiter_;
    categoryElements_ = null;
    categoryKey_ = null;
    checked_ = true;
    displayName_ = null;
    tModelKey_ = null;
    errorException_ = null;
  }
  
  /**
   * CategoryModels load their data from the file named via the
   * {@link #setDefaultDataFile(String)} method. The data file
   * name is interpretted as either a physical pathname or a
   * context-root relative pathname depending on whether the
   * servlet context or "application" is null (default) or not.
   * @param application The application context, null by default.
   * Value can be the ServletContext of the WSExplorer or null.
   */
  public final void setServletContext(ServletContext application)
  {
	application_ = application;
  }
  
  public final void setDefaultDataFile(String defaultDataFile)
  {
    defaultDataFile_ = defaultDataFile;
  }

  public final void setCategoryKey(String categoryKey)
  {
    categoryKey_ = categoryKey;
  }
  
  public final String getCategoryKey()
  {
    return categoryKey_;
  }
  
  public final void setColumnDelimiter(String columnDelimiter)
  {
    columnDelimiter_ = columnDelimiter;
  }
  
  public final String getColumnDelimiter()
  {
    return columnDelimiter_;
  }
  
  public final void setStringDelimiter(String stringDelimiter)
  {
    stringDelimiter_ = stringDelimiter;
  }
  
  public final String getStringDelimiter()
  {
    return stringDelimiter_;
  }
  
  public final void enableChecked(boolean isChecked)
  {
    checked_ = isChecked;
  }
  
  public final boolean isChecked()
  {
    return checked_;
  }
  
  public final void setDisplayName(String displayName)
  {
    displayName_ = displayName;
  }
  
  public final String getDisplayName()
  {
    return displayName_;
  }
  
  public final void setTModelKey(String tModelKey)
  {
    tModelKey_ = tModelKey;
  }
  
  public final String getTModelKey()
  {
    return tModelKey_;
  }
  
  public final boolean isDataLoaded()
  {
    return (categoryElements_ != null);
  }
  
  public final Throwable getErrorException()
  {
    return errorException_;
  }
  
  private final boolean isEnclosedInQuotes(String string)
  {
    return string.startsWith("\"") && string.endsWith("\"");
  }

  public final byte loadFromDefaultDataFile()
  {
    try
    {
      if (defaultDataFile_ == null)
        throw new FileNotFoundException();
	  BufferedReader br = null;
	  if (application_ == null)
		br = new BufferedReader(new InputStreamReader(new FileInputStream(defaultDataFile_),HTMLUtils.UTF8_ENCODING));
	  else
	    br = new BufferedReader(new InputStreamReader(application_.getResourceAsStream(defaultDataFile_),HTMLUtils.UTF8_ENCODING));
      return loadData(br);
    }
    catch (FileNotFoundException e)
    {
      errorException_ = e;
      return ERROR_FILE;
    }
    catch (SecurityException e)
    {
      errorException_ = e;
      return ERROR_SECURITY;
    }
    catch (UnsupportedEncodingException e)
    {
      errorException_ = e;
      return ERROR_ENCODING;
    }
  }
  
  public final byte loadFromDelimiterFile()
  {
    // Get the associated .properties file and set the delimiters. The defaultDataFile_ is set to the delimiter file in this case.
    // The properties file contains the following keys:
    // wsad.dataFile - location of the category data file.
    // wsad.checked - whether or not the category is checked.
    // wsad.name - the display name of the category.
    // column.delimiter - the delimiter character used for separating columns.
    // string.delimiter - the delimiter character used to encapsulate data such that column delimiter characters are treated as data.
    try
    {
      Properties p = new Properties();
      InputStream fin = null;
	  if (application_ == null)
	    fin = new FileInputStream(defaultDataFile_);
	  else
		fin = application_.getResourceAsStream(defaultDataFile_);
      p.load(fin);
      fin.close();
      columnDelimiter_ = p.getProperty("column.delimiter");
      stringDelimiter_ = p.getProperty("string.delimiter");
      defaultDataFile_ = p.getProperty("wsad.dataFile");
      return loadFromDefaultDataFile();
    }
    catch (FileNotFoundException e)
    {
      errorException_ = e;
      return ERROR_FILE;
    }
    catch (SecurityException e)
    {
      errorException_ = e;
      return ERROR_SECURITY;
    }
    catch (IOException e)
    {
      errorException_ = e;
      return ERROR_IO;
    }        
  }
  
  public final byte loadData(BufferedReader br)
  {
    errorException_ = null;
    CategoryElement rootElement = null;
    categoryElements_ = new Hashtable();
    byte returnCode = OPERATION_SUCCESSFUL;
    String line = null;
    int lineNumber = 0;
    try
    {
      char columnDelimiterChar = columnDelimiter_.charAt(0);
      char stringDelimiterChar = stringDelimiter_.charAt(0);
      Vector values = new Vector();
      while((line = br.readLine()) != null)
      {
        lineNumber++;
        // Ignore blank lines.
        if (line.trim().length() == 0)
          continue;
        int index = 0;
        int length = line.length();
        boolean inStringDelimiters = false;
        values.removeAllElements();
        StringBuffer currentToken = new StringBuffer();
        while (index < length)
        {
          char currentChar = line.charAt(index);
          if (inStringDelimiters)
          {
            // In quote so ignoring delimiters
            if (currentChar == stringDelimiterChar)
            {
              if (index < length - 1)
              {
                // currentChar could be a closing string delimiter or escape
                // Need to look ahead to be sure...
                char followingChar = line.charAt(index + 1);
                if (followingChar == stringDelimiterChar)
                {
                  // There is an escaped quote
                  currentToken.append(stringDelimiterChar);
                  // jump forward two characters.
                  index += 2;
                }
                else
                {
                  // There was a closing string delimiter...jump forward to the next delimiter
                  inStringDelimiters = false;
                  //look for the next column delimiter character
                  int nextDelimiter = line.indexOf(columnDelimiterChar, index);
                  if (nextDelimiter == -1)
                  {
                    // There were no more delimiters so break out of the loop
                    break;
                  }
                  else
                  {
                    values.addElement(currentToken.toString());
                    //values[tokenCount++] = currentToken.toString();
                    currentToken.setLength(0);
                    inStringDelimiters = false;
                    index = nextDelimiter + 1;
                  }
                }
              }
              else
              {
                // This is the last character and it's a closing string delimiter.
                index++;
                inStringDelimiters = false;
              }
            }
            else
            {
              currentToken.append(currentChar);
              index++;
            }
          }
          else if (currentChar == columnDelimiterChar)
          {
            // There was a delimiter outside of quotes
            values.addElement(currentToken.toString());
            //values[tokenCount++] = currentToken.toString();
            currentToken.setLength(0);
            index++;
          }
          else if (currentChar == stringDelimiterChar)
          {
            // A quote appearing outside of quotes must be a opening quote
            inStringDelimiters = true;
            index++;
          }
          else
          {
            // There is a normal char outside of quotes
            currentToken.append(currentChar);
            index++;
          }
        }
        // Expect token count to be values.length - 1 at this point if everything is ok
        if (inStringDelimiters)
        {
          br.close();
          throw new ParseException(line,lineNumber);
        }
        
        // Add the final token.
        values.addElement(currentToken.toString());

        // 3 columns format. From left to right, these are:
        // 1) category's key value
        // 2) category's key name
        // 3) category's parent key value
        // Convert to 4 columns format.
        if (values.size() == 3)
        {
          if (categoryKey_ != null)
            values.insertElementAt(categoryKey_, 0);
          else
            values.insertElementAt(tModelKey_, 0);
        }

        // 4 columns format. From left to right, these are:
        // 1) type of category (categoryKey)
        // 2) category's key value
        // 3) category's key name
        // 4) category's parent key value
        if (values.size() == 4)
        {
          String categoryKey = (String)values.elementAt(0);
          if (categoryKey_ == null)
            categoryKey_ = categoryKey;
          else if (!categoryKey_.equals(categoryKey))
            throw new Exception(categoryKey);
          String keyValue = (String)values.elementAt(1);
          String keyName = (String)values.elementAt(2);
          String parentKeyValue = (String)values.elementAt(3);
          if (rootElement == null)
          {
            rootElement = new CategoryElement(displayName_,null,this);
            setRootElement(rootElement);
          }
          if (isEnclosedInQuotes(keyName))
            keyName = keyName.substring(1,keyName.length()-1);
          // Check if the CategoryElement already exists. If it does, refresh the Category.
          CategoryElement categoryElement = (CategoryElement)categoryElements_.get(keyValue);
          if (categoryElement != null)
            categoryElement.updateCategory(keyName,keyValue,tModelKey_);
          else
            categoryElement = new CategoryElement(keyName,new KeyedReference(keyName,keyValue,tModelKey_),this);
          Element parentElement;
          if (parentKeyValue.equals(keyValue))
            parentElement = rootElement;
          else
            parentElement = (Element)categoryElements_.get(parentKeyValue);
          if (parentElement == null)
          {
            parentElement = new CategoryElement("temp",new KeyedReference("",parentKeyValue,tModelKey_),this);
            categoryElements_.put(parentKeyValue,parentElement);
          }
          else
            parentElement.connect(categoryElement,UDDIModelConstants.REL_SUBCATEGORIES,ModelConstants.REL_OWNER);
          categoryElements_.put(keyValue,categoryElement);          
        }
        else
        {
          br.close();
          throw new ParseException(line,lineNumber);
        }
      }
      br.close();
      return returnCode;
    }
    catch (IOException e)
    {
      errorException_ = e;
      returnCode = ERROR_IO;
    }
    catch (ParseException e)
    {
      errorException_ = e;
      returnCode = ERROR_FILE;
    }
    catch (Exception e)
    {
      errorException_ = e;
      returnCode = ERROR_CATEGORY_KEY;
    }
    categoryElements_ = null;
    return returnCode;
  }
  
  private final void saveData(PrintWriter pw,CategoryElement categoryElement,CategoryElement parentElement,boolean isFirst)
  {
    // File format:
    // <categoryKey>#<keyValue>#<keyName>#<parentKeyValue>
    KeyedReference kr = categoryElement.getCategory();
    // Check if this is not the root element.
    if (kr != null)
    {
      String keyName = kr.getKeyName();
      String keyValue = kr.getKeyValue();
      String parentKeyValue;
      if (parentElement == null)
        parentKeyValue = keyValue;
      else
      {
        KeyedReference parentKr = parentElement.getCategory();
        // Check if the parent is the root element.
        if (parentKr == null)
          parentKeyValue = keyValue;
        else
          parentKeyValue = parentKr.getKeyValue();
      }
      if (!isFirst)
        pw.println();
      else
        isFirst = false;
      pw.print(mangle(categoryKey_));
      pw.print(columnDelimiter_);
      pw.print(mangle(keyValue));
      pw.print(columnDelimiter_);
      pw.print(mangle(keyName));
      pw.print(columnDelimiter_);
      pw.print(mangle(parentKeyValue));
    }
    Enumeration e = categoryElement.getElements(UDDIModelConstants.REL_SUBCATEGORIES);
    if (e != null)
    {
      while (e.hasMoreElements())
        saveData(pw,(CategoryElement)e.nextElement(),categoryElement,isFirst);
    }
  }
  
  // Mangle an input string if it contains default column delimiter characters by surrounding it with the string delimiters.
  private final String mangle(String input)
  {
    if (input != null && input.indexOf(columnDelimiter_) != -1)
    {
      StringBuffer s = new StringBuffer(input);
      s.insert(0,stringDelimiter_).append(stringDelimiter_);
      return s.toString();
    }
    return input;
  }
  
  public final byte saveData(String fileName)
  {
    // The fileName should already be URLEncoded.
    byte returnCode = OPERATION_SUCCESSFUL;
    errorException_ = null;
    if (isDataLoaded())
    {
      try
      {
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName),HTMLUtils.UTF8_ENCODING),true);
        CategoryElement rootElement = (CategoryElement)getRootElement();
        saveData(pw,rootElement,null,true);
        pw.flush();
        pw.close();
        // Save the properties file.
        Properties p = new Properties();
        p.setProperty("wsad.dataFile",fileName);
        p.setProperty("wsad.checked",String.valueOf(checked_));
        p.setProperty("wsad.name",displayName_);
        p.setProperty("column.delimiter",columnDelimiter_);
        p.setProperty("string.delimiter",stringDelimiter_);
        StringBuffer propertiesFileName = new StringBuffer(fileName.substring(0,fileName.lastIndexOf('.')));
        propertiesFileName.append(".properties");
        FileOutputStream fout = new FileOutputStream(propertiesFileName.toString());
        p.store(fout,null);
        fout.close();
      }
      catch (FileNotFoundException e)
      {
        errorException_ = e;
        returnCode = ERROR_FILE;
      }
      catch (SecurityException e)
      {
        errorException_ = e;
        returnCode = ERROR_SECURITY;
      }
      catch (UnsupportedEncodingException e)
      {
        errorException_ = e;
        returnCode = ERROR_ENCODING;
      }
      catch (IOException e)
      {
        errorException_ = e;
        returnCode = ERROR_IO;
      }
    }
    return returnCode;
  }
  
  public String toString()
  {
    return displayName_;
  }
}