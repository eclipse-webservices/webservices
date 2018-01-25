<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0"
    xmlns:xalan="http://xml.apache.org/xslt"
    xmlns:ns1="http://xml.apache.org/axis/wsdd/"
    xmlns:java="http://xml.apache.org/axis/wsdd/providers/java">
    <xsl:output method="xml" encoding="UTF-8"/>
  
   <xsl:param name="newClassName" select="ss"/>
   
    <xsl:template match="/ns1:deployment/ns1:service/ns1:parameter[@name='className']">
    <xsl:copy>
       <xsl:attribute name="name">className</xsl:attribute>
       	 <xsl:attribute name="value"> 	
  			<xsl:value-of select="$newClassName"/>  	 	
       	 </xsl:attribute>
       	<xsl:apply-templates/>
       	</xsl:copy>
    </xsl:template>
    
    <xsl:template match="@*|node()">
    	<xsl:copy>
    		<xsl:apply-templates select="@*|node()"/>
    	</xsl:copy>
    </xsl:template>
</xsl:stylesheet>
