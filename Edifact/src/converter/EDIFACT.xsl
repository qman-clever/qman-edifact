<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:output method="text"></xsl:output>
	
    <!-- TODO: figure out how to enable validation during transformation             -->
    <!-- TODO: figure out how to selectively add a newline to the segment terminator -->


	<!-- define EDIFACT syntactical elements -->
	<xsl:variable name="data_element_separator">
		<xsl:call-template name="set-variable">
			<xsl:with-param name="pathname" select="//data_element_separator" />
			<xsl:with-param name="default_value" select="'+'" />
		</xsl:call-template>
	</xsl:variable>

	<xsl:variable name="component_data_element_separator">
		<xsl:call-template name="set-variable">
			<xsl:with-param name="pathname"
				select="//component_data_element_separator" />
			<xsl:with-param name="default_value" select="':'" />
		</xsl:call-template>
	</xsl:variable>

	<xsl:variable name="escape_character">
		<xsl:call-template name="set-variable">
			<xsl:with-param name="pathname" select="//escape_character" />
			<xsl:with-param name="default_value" select="'?'" />
		</xsl:call-template>
	</xsl:variable>

	<xsl:variable name="decimal_mark">
		<xsl:call-template name="set-variable">
			<xsl:with-param name="pathname" select="//decimal_mark" />
			<xsl:with-param name="default_value" select="','" />
		</xsl:call-template>
	</xsl:variable>

	<xsl:variable name="segment_terminator">
		<xsl:call-template name="set-variable">
			<xsl:with-param name="pathname" select="//segment_terminator" />
			<!-- Apostrophe and a newline end each segment, 
			     unless overridden by service_string_advice 
			 -->
            <!-- NOTE: there are typically no line breaks in EDI data
                 You may have to modify the terminator element, below
            -->
			<xsl:with-param name="default_value">&apos;&#xA;</xsl:with-param>
		</xsl:call-template>
	</xsl:variable>

    <xsl:template name="set-variable">
        <xsl:param name="pathname" />
        <xsl:param name="default_value" />
        <xsl:message>set-variable called for <xsl:value-of select="$pathname" /></xsl:message>
        <xsl:choose>
            <xsl:when test="/edifact/service_string_advice">
                <xsl:apply-templates select="$pathname" /><!-- MUST be a node-set!! -->
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$default_value" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>



	<!-- Individual element processing -->
	<xsl:template match="segment">
		<xsl:message>Segment template called.</xsl:message>
		<xsl:value-of select="@code"></xsl:value-of>
		<!-- Emit data element separator character, then start processing the elements -->
		<xsl:value-of select="$data_element_separator"></xsl:value-of>
		<xsl:apply-templates select="data_element"></xsl:apply-templates>
		<xsl:value-of select="$segment_terminator"></xsl:value-of>
	</xsl:template>

	<xsl:template match="data_element">
		<xsl:message>Template called for <xsl:value-of select="name()" /></xsl:message>
		<xsl:apply-templates select="text()" />
		<xsl:apply-templates select="component_data_element"></xsl:apply-templates>
		<xsl:if test="not(position()=last())">
			<xsl:value-of select="$data_element_separator"></xsl:value-of>
		</xsl:if>
	</xsl:template>

	<xsl:template match="component_data_element">
		<xsl:message>Template called for <xsl:value-of select="name()" /></xsl:message>
		<xsl:apply-templates select="text()" />
		<xsl:if test="not(position()=last())">
			<xsl:value-of select="$component_data_element_separator"></xsl:value-of>
		</xsl:if>
	</xsl:template>

	<xsl:template match="text()">
		<xsl:message>Template called for text</xsl:message>
		<!-- Eat up all the whitespace. Print out whatever remains. -->
		<xsl:value-of select="normalize-space()"></xsl:value-of>
	</xsl:template>

	<!-- Main program which traverses the entire context tree -->
	<xsl:template match="/edifact">
		<xsl:message>Main program.</xsl:message>
		<xsl:apply-templates select="service_string_advice"></xsl:apply-templates>
		<xsl:apply-templates select="interchange"></xsl:apply-templates>
	</xsl:template>

	<xsl:template match="service_string_advice">
		<xsl:message>Got into service_string_advice.</xsl:message>
		<!-- Print out this "special" segment -->
		<xsl:text>UNA</xsl:text>
		<xsl:value-of select="$component_data_element_separator"></xsl:value-of>
		<xsl:value-of select="$data_element_separator"></xsl:value-of>
		<xsl:value-of select="$decimal_mark"></xsl:value-of>
		<xsl:value-of select="$escape_character"></xsl:value-of>
		<xsl:text> </xsl:text><!-- Mandatory space between these two syntactical 
			elements -->
		<xsl:value-of select="$segment_terminator"></xsl:value-of>
	</xsl:template>

	<xsl:template match="interchange">
		<xsl:message>Got into interchange.</xsl:message>
		<xsl:apply-templates select="interchange_header" />
		<xsl:apply-templates select="//functional_group_header" />
		<xsl:apply-templates select="//message/message_header" />
		<xsl:apply-templates
			select="//message/message_data_segments/segment" />
		<xsl:apply-templates select="//message/message_trailer" />
		<xsl:apply-templates select="//functional_group_trailer" />
		<xsl:apply-templates select="interchange_trailer" />
	</xsl:template>

	<xsl:template
		match="component_data_element_separator|data_element_separator|
                         escape_character|decimal_mark|
                         segment_terminator">
		<xsl:message>Template called for <xsl:value-of select="name()" /></xsl:message>
		<!-- Return the text in the element -->
		<xsl:value-of select="text()" />
	</xsl:template>

	<xsl:template
		match="interchange_header|interchange_trailer|
                         functional_group_header|functional_group_trailer|
                         message_header|message_trailer">
		<xsl:message>Template called for <xsl:value-of select="name()" /></xsl:message>
		<xsl:apply-templates select="segment"></xsl:apply-templates>
	</xsl:template>
</xsl:stylesheet>