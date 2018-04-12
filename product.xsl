<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0">
 
	<xsl:template match="/">
		<html>
 
			<head>
				<style type="text/css">
					table.tfmt {
					border: 2px ;
					}
 
					td.colfmt {
					border: 2px ;
					background-color: white;
					color: black;
					text-align:right;
					}
 
					th {
					background-color: #2E9AFE;
					color: white;
					}
 
				</style>
			</head>
 
			<body>
				<table class="tfmt">
					<tr>
						<th style="width:350px">Title:</th>
						<th style="width:350px">Venue:</th>
						<th style="width:50px">Year:</th>
						<th style="width:100px">Type:</th>
						<th style="width:50px">key:</th>
 						<th style="width:250px">clickable links about authors:</th>
 
					</tr>
 
					<xsl:for-each select="result/hits/hit/info">
 
						<tr>
							<td class="colfmt">
								<xsl:value-of select="title" />
							</td>
							<td class="colfmt">
								<xsl:value-of select="venue" />
							</td>
 
							<td class="colfmt">
								<xsl:value-of select="year" />
							</td>
							<td class="colfmt">
								<xsl:value-of select="type" />
							</td>
						<td class="colfmt">
								<xsl:value-of select="key" />
							</td>
						<td class="colfmt">
                                                               
								<xsl:value-of select="url" />
							</td>
						</tr>
 
					</xsl:for-each>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>