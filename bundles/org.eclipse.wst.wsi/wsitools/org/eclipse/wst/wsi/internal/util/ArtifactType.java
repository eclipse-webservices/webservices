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
package org.eclipse.wst.wsi.internal.util;

/**
 * This class ...
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public class ArtifactType
{
  /**
   * This type.
   */
  private String type;

  /**
   * ReportArtifact types.
   */
  public static final String TYPE_ENVELOPE = "envelope";
  public static final String TYPE_MESSAGE = "message";
  public static final String TYPE_DESCRIPTION = "description";
  public static final String TYPE_DISCOVERY = "discovery";

  /**
   * ReportArtifact types.
   */
  public static final ArtifactType ARTIFACT_TYPE_ENVELOPE =
    new ArtifactType(TYPE_ENVELOPE);
  public static final ArtifactType ARTIFACT_TYPE_MESSAGE =
    new ArtifactType(TYPE_MESSAGE);
  public static final ArtifactType ARTIFACT_TYPE_DESCRIPTION =
    new ArtifactType(TYPE_DESCRIPTION);
  public static final ArtifactType ARTIFACT_TYPE_DISCOVERY =
    new ArtifactType(TYPE_DISCOVERY);

  /**
   * Create artifact type.
   */
  private ArtifactType(String type)
  {
    this.type = type;
  }

  /**
   * Is artifact type envelope.
   * @return true if artifact type envelope.
   */
  public boolean isEnvelope()
  {
    return type.equals(TYPE_ENVELOPE);
  }

  /**
   * Is artifact type messages.
   * @return true if artifact type messages.
   */
  public boolean isMessages()
  {
    return type.equals(TYPE_MESSAGE);
  }

  /**
   * Is artifact type description.
   * @return true if artifact type description.
   */
  public boolean isDescription()
  {
    return type.equals(TYPE_DESCRIPTION);
  }

  /**
   * Is artifact type discovery.
   * @return true if artifact type discovery.
   */
  public boolean isDiscovery()
  {
    return type.equals(TYPE_DISCOVERY);
  }

  /**
   * Get artifact type.
   * @return artifact type.
   */
  public String getTypeName()
  {
    return type;
  }

  /**
   * Create artifact type.
   * @param typeName artifact type name.
   * @return newly created artifact type.
   * @throws RuntimeException if artifact type name is invalid or inappropriate.
   */
  public static final ArtifactType newArtifactType(String typeName)
    throws RuntimeException
  {
    ArtifactType artifactType = null;

    if (typeName.equals(TYPE_DESCRIPTION))
    {
      artifactType = ARTIFACT_TYPE_DESCRIPTION;
    }

    else if (typeName.equals(TYPE_MESSAGE))
    {
      artifactType = ARTIFACT_TYPE_MESSAGE;
    }

    else if (typeName.equals(TYPE_ENVELOPE))
    {
      artifactType = ARTIFACT_TYPE_ENVELOPE;
    }

    else if (typeName.equals(TYPE_DISCOVERY))
    {
      artifactType = ARTIFACT_TYPE_DISCOVERY;
    }

    else
    {
      throw new RuntimeException(
        "Could not create new artifact type using invalid type name: "
          + typeName
          + ".");
    }

    return artifactType;
  }
}