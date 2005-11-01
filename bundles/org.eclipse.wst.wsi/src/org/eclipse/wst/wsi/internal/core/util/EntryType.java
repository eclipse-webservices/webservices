/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.util;

import java.util.TreeMap;
import java.util.Vector;

/**
 * This class ...
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public class EntryType
{
  /**
   * ReportArtifact type.
   */
  private ArtifactType artifactType;

  /**
   * This type.
   */
  private String typeName;

  /**
   * Entry types accessible by type name.
   */
  protected static final TreeMap entryTypeMap = new TreeMap();

  /**
   * Entry type names accessible by artifact.
   */
  protected static final TreeMap entryTypeNameMap = new TreeMap();
  static {
    entryTypeNameMap.put(ArtifactType.TYPE_ENVELOPE, new Vector());
    entryTypeNameMap.put(ArtifactType.TYPE_MESSAGE, new Vector());
    entryTypeNameMap.put(ArtifactType.TYPE_DESCRIPTION, new Vector());
    entryTypeNameMap.put(ArtifactType.TYPE_DISCOVERY, new Vector());
  }

  /**
   * Envelope entry types.
   */
  public static final String TYPE_ENVELOPE_REQUEST = "requestEnvelope";
  public static final String TYPE_ENVELOPE_RESPONSE = "responseEnvelope";
  public static final String TYPE_ENVELOPE_ANY = "anyEnvelope";

  /**
   * Message entry types.
   */
  public static final String TYPE_MESSAGE_REQUEST = "requestMessage";
  public static final String TYPE_MESSAGE_RESPONSE = "responseMessage";
  public static final String TYPE_MESSAGE_ANY = "anyMessage";

  /**
   * MIME entry types.
   */
  public static final String TYPE_MIME_PART = "part";
  public static final String TYPE_MIME_ROOT_PART = "root-part";

  /**
   * Description entry types.
   */
  public static final String TYPE_DESCRIPTION_DEFINITIONS = "definitions";
  public static final String TYPE_DESCRIPTION_IMPORT = "import";
  public static final String TYPE_DESCRIPTION_TYPES = "types";
  public static final String TYPE_DESCRIPTION_MESSAGE = "message";
  public static final String TYPE_DESCRIPTION_OPERATION = "operation";
  public static final String TYPE_DESCRIPTION_PORTTYPE = "portType";
  public static final String TYPE_DESCRIPTION_BINDING = "binding";
  public static final String TYPE_DESCRIPTION_PORT = "port";

  /**
   * Discovery entry types.
   */
  public static final String TYPE_DISCOVERY_BINDINGTEMPLATE = "bindingTemplate";
  public static final String TYPE_DISCOVERY_TMODEL = "tModel";

  /**
   * Envelope entry types.
   */
  public static final EntryType ENTRY_TYPE_REQUESTENV =
    newEntryType(ArtifactType.ARTIFACT_TYPE_ENVELOPE, TYPE_ENVELOPE_REQUEST);
  public static final EntryType ENTRY_TYPE_RESPONSEENV =
    newEntryType(ArtifactType.ARTIFACT_TYPE_ENVELOPE, TYPE_ENVELOPE_RESPONSE);

  /**
   * Message entry types.
   */
  public static final EntryType ENTRY_TYPE_REQUEST =
    newEntryType(ArtifactType.ARTIFACT_TYPE_MESSAGE, TYPE_MESSAGE_REQUEST);
  public static final EntryType ENTRY_TYPE_RESPONSE =
    newEntryType(ArtifactType.ARTIFACT_TYPE_MESSAGE, TYPE_MESSAGE_RESPONSE);
  //  public static final EntryType ENTRY_TYPE_ANYENTRY = 
  //        new EntryType(ArtifactType.ARTIFACT_TYPE_MESSAGES, TYPE_MESSAGE_ANYENTRY);

  /**
   * Description entry types.
   */
  public static final EntryType ENTRY_TYPE_DEFINITIONS =
    newEntryType(
      ArtifactType.ARTIFACT_TYPE_DESCRIPTION,
      TYPE_DESCRIPTION_DEFINITIONS);
  public static final EntryType ENTRY_TYPE_IMPORT =
    newEntryType(
      ArtifactType.ARTIFACT_TYPE_DESCRIPTION,
      TYPE_DESCRIPTION_IMPORT);
  public static final EntryType ENTRY_TYPE_TYPES =
    newEntryType(
      ArtifactType.ARTIFACT_TYPE_DESCRIPTION,
      TYPE_DESCRIPTION_TYPES);
  public static final EntryType ENTRY_TYPE_MESSAGE =
    newEntryType(
      ArtifactType.ARTIFACT_TYPE_DESCRIPTION,
      TYPE_DESCRIPTION_MESSAGE);
  public static final EntryType ENTRY_TYPE_OPERATION =
    newEntryType(
      ArtifactType.ARTIFACT_TYPE_DESCRIPTION,
      TYPE_DESCRIPTION_OPERATION);
  public static final EntryType ENTRY_TYPE_PORTTYPE =
    newEntryType(
      ArtifactType.ARTIFACT_TYPE_DESCRIPTION,
      TYPE_DESCRIPTION_PORTTYPE);
  public static final EntryType ENTRY_TYPE_BINDING =
    newEntryType(
      ArtifactType.ARTIFACT_TYPE_DESCRIPTION,
      TYPE_DESCRIPTION_BINDING);
  public static final EntryType ENTRY_TYPE_PORT =
    newEntryType(ArtifactType.ARTIFACT_TYPE_DESCRIPTION, TYPE_DESCRIPTION_PORT);

  /**
   * Discovery entry types.
   */
  public static final EntryType ENTRY_TYPE_BINDINGTEMPLATE =
    newEntryType(
      ArtifactType.ARTIFACT_TYPE_DISCOVERY,
      TYPE_DISCOVERY_BINDINGTEMPLATE);
  public static final EntryType ENTRY_TYPE_TMODEL =
    newEntryType(ArtifactType.ARTIFACT_TYPE_DISCOVERY, TYPE_DISCOVERY_TMODEL);

  /**
   * Create entry type.
   */
  private EntryType(ArtifactType artifactType, String typeName)
  {
    this.artifactType = artifactType;
    this.typeName = typeName;
  }

  /**
   * Is entry type equal to specified type.
   * @param typeName entry type name.
   * @return true if entry type equal to specified type.
   */
  public boolean isType(String typeName)
  {
    return this.typeName.equals(typeName);
  }

  /**
   * Is entry type equal to specified type.
   * @param entryType entry type.
   * @return true if entry type equal to specified type.
   */
  public boolean isType(EntryType entryType)
  {
    return typeName.equals(entryType.getTypeName());
  }

  /**
   * Get artifact type.
   * @return artifact type.
   */
  public ArtifactType getArtifactType()
  {
    return artifactType;
  }

  /**
   * Get entry type name.
   * @return entry type name.
   */
  public String getTypeName()
  {
    return typeName;
  }

  /**
   * Is valid entry type.
   * @param typeName entry type name.
   * @return true if entry type name is valid.
   */
  public static final boolean isValidEntryType(String typeName)
  {
    return (entryTypeMap.get(typeName) == null ? false : true);
  }

  /**
   * Create entry type.
   * @param typeName entry type name.
   * @return entry type.
   * @throws RuntimeException if entry type name is invalid or inappropriate.
   */
  public static final EntryType getEntryType(String typeName)
    throws RuntimeException
  {
    // Get the entry type by type name
    EntryType entryType = (EntryType) entryTypeMap.get(typeName);

    if (entryType == null)
    {
      throw new RuntimeException(
        "Could not get entry type because type name is invalid: "
          + typeName
          + ".");
    }

    return entryType;
  }

  /**
   * Get list of entry type names for a specified artifact type name.
   * @param artifactTypeName artifact type name.
   * @return list of entry type names for a specified artifact type name.
   * @throws RuntimeException if entry type name is invalid or inappropriate.
   */
  public static final Vector getEntryTypeNameList(String artifactTypeName)
    throws RuntimeException
  {
    // Get list
    Vector entryTypeNameList = (Vector) entryTypeNameMap.get(artifactTypeName);

    // If the list was not found, then throw an exception
    if (entryTypeNameList == null)
    {
      throw new RuntimeException(
        "Could not get entry type name list because artifact type name is invalid: "
          + artifactTypeName
          + ".");
    }

    return entryTypeNameList;
  }

  /**
   * Get list of type names for a specified artifact type name.
   */
  private static final EntryType newEntryType(
    ArtifactType artifactType,
    String typeName)
  {
    EntryType entryType = null;

    // Create entry type
    entryType = new EntryType(artifactType, typeName);

    // Add to entry type map
    entryTypeMap.put(typeName, entryType);

    // Get the entry type name vector for the artifact type
    Vector entryTypeNameList =
      (Vector) entryTypeNameMap.get(artifactType.getTypeName());

    // Add the type name to the list
    entryTypeNameList.add(typeName);

    return entryType;
  }
}
