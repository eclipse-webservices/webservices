//
// Copyright (c) 2002, 2004 IBM Corporation and others.
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v1.0
// which accompanies this distribution, and is available at
// http://www.eclipse.org/legal/epl-v10.html
// 
// Contributors:
//     IBM Corporation - initial API and implementation
//

// Requires browserdetect.js

var selectedAnchorName = "";

function setSelectedAnchorName(anchorName)
{
  selectedAnchorName = anchorName;
}

function selectNode(anchorName,openImagePath)
{
  var isIE = isMicrosoftInternetExplorer();
  if (selectedAnchorName.length > 0)
  {
    var selectedAnchor;
    if (isIE)
      selectedAnchor = document.anchors(selectedAnchorName);
    else
      selectedAnchor = document.anchors[selectedAnchorName];
    if (selectedAnchor)
      selectedAnchor.className = "unselectedTextAnchor";
  }
  if (anchorName.indexOf("-1") == -1)
  {
    if (isIE)
      document.anchors(anchorName).className = "selectedTextAnchor";
    else
      document.anchors[anchorName].className = "selectedTextAnchor";
    setSelectedAnchorName(anchorName);
    alterImage(anchorName,openImagePath);
  }
  else
    setSelectedAnchorName("");
}

function alterImage(imageName,imagePath)
{
  document.images[imageName].src = imagePath;
}
