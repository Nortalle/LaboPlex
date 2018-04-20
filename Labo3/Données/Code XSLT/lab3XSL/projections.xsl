<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:variable name="langages" select="document('langages.xml')"/>
<xsl:variable name="mots_cles" select="document('mots_cles.xml')"/>

<!-- <xsl:apply-templates select="$document_1/element_racine" /> -->

<xsl:template match="/">

    <html class="no-js" lang="en" dir="ltr">
        <head>
            <meta charset="utf-8" />
            <meta http-equiv="x-ua-compatible" content="ie=edge" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <title>Plex - Projections</title>
            <link rel="stylesheet" href="css/foundation.min.css" />
            <link rel="stylesheet" href="css/dataTables.foundation.min.css" />
            <link rel="stylesheet" href="css/app.css" />
            <script src="js/jquery.js" type="text/javascript"></script>
            <script src="js/foundation.min.js" type="text/javascript"></script>
            <script src="js/jquery.dataTables.min.js" type="text/javascript"></script>
            <script src="js/dataTables.foundation.min.js" type="text/javascript"></script>
            <script src="js/date-euro.js" type="text/javascript"></script>
            <script src="js/app.js" type="text/javascript"></script>
        </head>
        <body>
            <xsl:apply-templates select="plex/projections" />
            <xsl:apply-templates select="plex/films" />
            <xsl:apply-templates select="plex/acteurs" />
        </body>
    </html>

</xsl:template>

<xsl:template match="projections">

    <div class="wrapper">
        <div class="row">
            <div class="large-12 columns">
                <h1>Plex - Projections</h1>
            </div>
        </div>
        <div class="row">
            <div class="large-12 columns">
                <br/><br/>
                <table id="projections">
                    <thead>
                        <tr>
                            <th>Film</th>
                            <th>Projection</th>
                            <th>Salle</th>
                            <th>Note</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:for-each select="//projection">
                            <xsl:variable name="idfilm" select="@film_id" />
                            <tr>
                                
                                <td><a data-open="{$idfilm}"><xsl:value-of select="//film[@no = $idfilm]/titre" /></a></td>
                                <!-- data-open : Ouvre une fenetre dont le contenu est le div identifié (id) par la valeur de la variable idfilm -->
                                
                                
                                <td><xsl:value-of select="date_heure" /></td>
                                <td><xsl:value-of select="salle" /></td>
                                <td><xsl:value-of select="format-number(sum(//film[@no = $idfilm]/critiques/critique/@note) div count(//film[@no = $idfilm]/critiques/critique/@note),'##.0')" />/5.0</td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</xsl:template>

<xsl:template match="films">

    <xsl:for-each select="film">
        <div id="{@no}" class="reveal large" data-reveal="true">
            <div class="row">
                <div class="large-4 columns">
                    <img src="{photo/@url}" />
                </div>
                <div class="large-8 columns">
                    <h4><xsl:value-of select="titre" /></h4>
                    <p>
                        <strong>Durée : </strong> <xsl:value-of select="duree" /><xsl:text> </xsl:text><xsl:value-of select="duree/@format" /><br />
                        <strong>Genres : </strong> 
                        <xsl:call-template name="split_genres">
                            <xsl:with-param name="genres" select="genres/@liste"/>
                        </xsl:call-template>
                        <br/>
                        <strong>Langues : </strong> 
                        <xsl:call-template name="split_langues">
                            <xsl:with-param name="langues" select="langages/@liste"/>
                        </xsl:call-template>
                    </p>
                    <h4><xsl:text>Synopsys</xsl:text></h4>
                    <p><xsl:value-of select="synopsys" /></p>
                </div>
            </div>
                <div class="row">
                <div class="large-12 columns">
                    <h4><xsl:text>Critiques</xsl:text></h4>
                    <xsl:for-each select="critiques/critique">
                        <hr />
                        <strong><xsl:value-of select="@note" />/5 : </strong> 
                        <xsl:value-of select="text()" />
                    </xsl:for-each>
                    <hr />
                </div>
            </div>
            <br/>
            <div class="row">
                <div class="large-12 columns">
                    <h4><xsl:text>Rôles</xsl:text></h4>
                    <table>
                        <thead>
                            <tr>
                                <th>Place</th>
                                <th>Personnage</th>
                                <th>Acteur</th>
                            </tr>
                        </thead>
                        <tbody>
                            <xsl:for-each select="roles/role">
                                <xsl:sort select="@place" data-type= "number"/>
                                <xsl:variable name="idacteur" select="@acteur_id" />
                                <tr>
                                    <td><xsl:value-of select="@place" /></td>
                                    <td><xsl:value-of select="@personnage" /></td>
                                    <td><a data-open="{$idacteur}"><xsl:value-of select="//acteur[@no = $idacteur]/nom" /></a></td>
                                </tr>
                            </xsl:for-each>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="row">
                <div class="large-12 columns">
                    <h4><xsl:text>Mots-cles</xsl:text></h4>
                    <xsl:call-template name="split_motscles">
                            <xsl:with-param name="motscles" select="mots_cles/@liste"/>
                    </xsl:call-template>
                </div>
            </div>
            
            <button class="close-button" data-close="{@no}" aria-label="Fermer" type="button">
                <span aria-hidden="true">×</span>
            </button>
        </div>
    </xsl:for-each>

</xsl:template>

<xsl:template match="acteurs">

    <xsl:for-each select="//acteur">
        <div id="{@no}" class="reveal large" data-reveal="true">
            <div class="row">
                <div class="large-12 columns">
                    <h4><xsl:value-of select="nom" /></h4>
                    <p>
                        <strong>Sexe : </strong> <xsl:value-of select="sexe/@valeur" /><br />
                        <strong>Nom de naissance : </strong> <xsl:value-of select="nom_naissance" /><br />
                        <strong>Date de naissance : </strong> <xsl:value-of select="date_naissance" /><br />
                        <strong>Date de décès : </strong> <xsl:value-of select="date_deces" /><br />
                        <br/>
                        <h5>Biographie</h5> 
                        <xsl:value-of select="biographie" />
                    </p>
                </div>
            </div>
            <button class="close-button" data-close="{@no}" aria-label="Fermer" type="button">
                <span aria-hidden="true">×</span>
            </button>
        </div>
    </xsl:for-each>

</xsl:template>

<xsl:template name="split_genres">
    <xsl:param name="genres" select=""/>
    <xsl:if test="string-length($genres)">  <!-- retourne le nbre de car. de la chaîne -->
        <xsl:value-of select="//genre[@no = substring-before(concat($genres,' '),' ')]/text()" />
        <xsl:if test="string-length(substring-after($genres, ' '))"> 
            <xsl:text> - </xsl:text>
        </xsl:if>
        <xsl:call-template name="split_genres">
            <xsl:with-param name="genres" select="substring-after($genres, ' ')"/>
        </xsl:call-template>
    </xsl:if>
</xsl:template>

<xsl:template name="split_langues">
    <xsl:param name="langues" select=""/>
    <xsl:if test="string-length($langues)">  <!-- retourne le nbre de car. de la chaîne -->
        <xsl:value-of select="//langage[@no = substring-before(concat($langues,' '),' ')]/text()" />
        <xsl:if test="string-length(substring-after($langues, ' '))"> 
            <xsl:text> - </xsl:text>
        </xsl:if>
        <xsl:call-template name="split_langues">
            <xsl:with-param name="langues" select="substring-after($langues, ' ')"/>
        </xsl:call-template>
    </xsl:if>
</xsl:template>

<xsl:template name="split_motscles">
    <xsl:param name="motscles" select=""/>
    <xsl:if test="string-length($motscles)">  <!-- retourne le nbre de car. de la chaîne -->
        <span class="secondary label">
            <xsl:value-of select="//mot_cle[@no = substring-before(concat($motscles,' '),' ')]/text()" />
        </span>
        <xsl:if test="string-length(substring-after($motscles, ' '))"> 
            <xsl:text> </xsl:text>
        </xsl:if>
        <xsl:call-template name="split_motscles">
            <xsl:with-param name="motscles" select="substring-after($motscles, ' ')"/>
        </xsl:call-template>
    </xsl:if>
</xsl:template>
</xsl:stylesheet>