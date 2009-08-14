/*******************************************************************************
 *
 * Copyright (c)	:	EIG (Environmental Informatics Group)
 * http://www.htw-saarland.de/eig
 * Prof. Dr. Reiner Guettler
 * Prof. Dr. Ralf Denzer
 *
 * HTWdS
 * Hochschule fuer Technik und Wirtschaft des Saarlandes
 * Goebenstr. 40
 * 66117 Saarbruecken
 * Germany
 *
 * Programmers		:	Annen Thomas
 *
 * Project				:	WuNDA 2
 * Filename			:	Resources.java
 * Version				:	1.0
 * Purpose				:
 * Created				:	04.12.2000
 * History				:
 *
 *******************************************************************************/
package Sirius.navigator.multilingual;

import java.io.*;
import java.util.*;

// XXX remove class, migrate to ResourceManager
/**
 * German (de_DE) ResourceBundle
 * @author 	<h2>Thomas Annen</h2>
 * @version <h2>Version 1.0</h2>
 * @deprecated migrate to ResourceManager
 */
public class Resources extends ListResourceBundle implements Serializable
{
    public Object[][] getContents()
    {
        return contents;
    }
    
    private final Object[][] contents =
    {
        { "STL@yesNoOptionARRAY" 				, new String[]
          {	"Ja", "Nein"	}																					},
          { "STL@daysARRAY"								, new String[]
            { "Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"}								},
            { "STL@monthsARRAY"							, new String[]
              {	"Januar",
                "Frebruar",
                "M\u00E4rz",
                "April",
                "Mai",
                "Juni",
                "Juli",
                "August",
                "September",
                "Oktober",
                "November",
                "Dezember"
              }																													},
              { "STL@propertiesValueARRAY" 		, new String[]
                {"Eigenschaft", "Wert"}																		},
                { "STL@yes"											, "Ja"																																		},
                { "STL@ok"											, "OK"																																		},
                { "STL@all"											, "Alle"																																	},
                { "STL@no"											, "Nein"																																	},
                { "STL@navigator"								, "Navigator"																															},
                { "STL@exit"										, "Abmelden"																															},
                { "STL@ignore"									, "Ignorieren"																														},
                { "STL@load"										, "Laden"																																	},
                { "STL@save"										, "Speichern"																															},
                { "STL@svaeAs"									, "Speichern als"																													},
                { "STL@doSave"									, " speichern"																														},
                { "STL@delete"									, "L\u00F6schen"																													},
                { "STL@cancel"									, "Abbrechen"																															},
                { "STL@change"									, "\u00C4ndern"																														},
                { "STL@reset"										, "Zur\u00FCcksetzen"																											},
                { "STL@close"										, "Schliessen"																														},
                { "STL@warning"									,	"Warnung"																																},
                { "STL@takeOn"									, "\u00DCbernehmen"																												},
                { "STL@overwrite"								, "\u00DCberschreiben"																										},
                { "STL@end"											, "Beenden"																																},
                { "STL@function"								, "Funktion"																															},
                { "STL@login"										, "Login"																																	},
                { "STL@search"									, "Suche"																																	},
                { "STL@doSearch"								, "Suchen"																																},
                { "STL@doSearchFor"							, "Suche nach"																														},
                { "STL@details"									, "Details"																																},
                { "STL@out"											, " aus."																																	},
                { "STL@from"										, "von"																																		},
                { "STL@until"										, "bis"																																		},
                { "STL@date"										, "Datum"																																	},
                { "STL@finished"								, "Fertig"																																},
                { "STL@map"											, "Karte"																																	},
                { "STL@append"									, "anh\u00E4ngen"																													},
                { "STL@searchResultProfile"			, "Suchergebnisprofile"																										},
                { "STL@changePwd"								, "Passwort \u00E4ndern"																									},
                { "STL@typeInPwd"								, "Passwort eingeben"																											},
                { "STL@pwdDoesntAgree"					, "Das neue Kennwort stimmt nicht mit dem" +
                  "Best\u00E4tigungskennwort \u00FCberein."																},
                  { "STL@couldntChangePwd"				, "Ihr Kennwort konnte nicht ge\u00E4ndert werden."												},
                  { "STL@changedPwd"							, "Ihr Kennwort wurde ge\u00E4ndert."																			},
                  { "STL@oldPwd"									, "Altes Passwort:"																												},
                  { "STL@newPwd"									, "Neues Passwort:"																												},
                  { "STL@searchError" 						, "<html><p>Die Suche konnte nicht aufgerufen werden.</p></html>"					},
                  { "STL@loginError"							, "<html><p>Fehler w\u00E4hrend des Anmeldevorgangs:</p>" +
                    "<p>Das Programm muss beendet werden.</p></html>"												},
                    { "STL@methodError"							, "<html><p>Die Methode konnte nicht aufgerufen werden.</p></html>"				},
                    { "STL@shouldClose" 						, "<html><center><p>M\u00F6chten Sie den</p>" +
                      "<p>Navigator wirklich schliessen?</p></center></html>"									},
                      { "STL@exitProgram"							, "Programm beenden"																											},
                      { "STL@objectsLoaded"						, "Objekte geladen."																											},
                      { "STL@edit"										, "Bearbeiten"																														},
                      { "STL@description"							, "Beschreibung"																													},
                      { "STL@noEntryYet"							, "Noch kein Eintrag"																											},
                      { "STL@deleteEntries"						, "Eintr\u00e4ge l\u00F6schen"																						},
                      { "STL@adoptInTree"							, "In Ergebnissbaum \u00FCbernehmen"																			},
                      { "STL@backInResultTree"				, "im Ergebnisbaum zur\u00FCck"																						},
                      { "STL@forwardInResultTree"			, "im Ergebnisbaum vor"																										},
                      { "STL@navigatorToMap"					, "Objekte aus Selektion in Navigator zur Karte transferieren"						},
                      { "STL@mapToNavigator"					, "Objekte aus Selektion in Karte zum Navigator transferieren"						},
                      { "STL@transferErrorMapToNav"		, "<html><p>Bei der Uebertragung von Objekten von der Karte</p>" +
                        "<p>zum Navigator ist ein Fehler aufgetreten.</p></html>"								},
                        { "STL@transferErrorNavToMap"		, "<html><p>Bei der Uebertragung von Objekten vom Navigator</p>" +
                          "<p>zur Karte ist ein Fehler aufgetreten.</p></html>"										},
                          { "STL@methodNotAvailable"			, "<html><p>Diese Methode ist nicht verf\u00FCgbar:</p>"+
                            "<p>Objekt unterst\u00FCtzt diese Metohde nicht.</p></html>"						},
                            { "STL@navigatorToMap"					, "Navigator->Karte"																											},
                            { "STL@mapToNavigator"					, "Karte->Navigator"																											},
                            { "STL@noObjectSelected"				, "<html><p>Diese Methode ist nicht verf\u00FCgbar:</p>"+
                              "<p>Keine Objekte ausgew\u00E4hlt.</p></html>"													},
                              { "STL@noCoordsSelected"				, "<html><p>Diese Methode ist nicht verf\u00FCgbar:</p>"+
                                "<p>Es sind keine Objekte oder Koordinaten ausgew\u00E4hlt.</p></html>"	},
                                { "STL@noLayerSelected"					, "<html><p>Diese Methode ist nicht verf\u00FCgbar:</p>"+
                                  "<p>Es sind keine Layer ausgew\u00E4hlt.</p></html>"										},
                                  { "STL@oneRootNodeSelected"			, "<html><p>Bitte stellen Sie sicher, dass sie mindestens einen Knoten "+
                                    "selektiert haben,</p>"+"<p>und dass es sich dabei um einen "+
                                    "Root-Knoten handelt.</p></html>"																				},
                                    { "STL@oneNodeSelected"					, "<html><p>Bitte stellen Sie sicher, dass sie mindestens einen Knoten "+
                                      "selektiert haben.</p></html>"																					},
                                      { "STL@couldNotDeleteNode"			, "Knoten konnten nicht gel\u00F6scht werden"															},
                                      { "STL@coultNotTransferNode"		, "Knoten konnten nicht \u00FCbertragen werden"														},
                                      { "STL@allUncompleteData"				, "<html><p>Alle selektierten Objekte enthielten unvollst\u00E4ndige "+
                                        "Daten:</p><p>Die Suche kann nicht durchgef\u00FChrt werden.</p></html>"},
                                        { "STL@noObjectsFound"					, "<html><p>Es wurden keine Objekte in der Karte gefunden!</p></html>"		},
                                        { "STL@someUncompleteData"			, "<html><p>Einige selektierte Objekte enthielten unvollst\u00E4ndige "+
                                          "Daten:</p><p>Die Suche wird mit Einschr\u00E4nkungen "+
                                          "durchgef\u00FChrt</p></html>"																					},
                                          { "STL@unknownMethodType"				, "<html><p>Die Methode konnte nicht aufgerufen werden:</p>"+
                                            "<p>Unbekannter Typ</p></html>"																					},
                                            { "STL@selectAll"								, "alle selektieren"																											},
                                            { "STL@dateChooserTitle"				, "Date Chooser"																													},
                                            { "STL@widgetNotAvailable"			, "Diese Widget ist z.Z. nicht anzeigbar"																	},
                                            { "STL@demoFrameTitle"					, "DemoFrame"																															},
                                            { "STL@coordsOfInterests"				, "<html><center><p>Bitte geben Sie die Koordinaten des "+
                                              "Interessensbereichs an,</p><p>auf den Sie die Karte beschr\u00E4nken "+
                                              "wollen.</p></center></html>"																						},
                                              { "STL@interests"								,	"Interessensbereich angeben"																						},
                                              { "STL@rightValLeftUnder"				, "Rechtswert linksunten:"																								},
                                              { "STL@highValLeftUnder"				, "Hochwert linksunten:"																									},
                                              { "STL@rightValRightAbove"			,	"Rechtswert rechtsoben:"																								},
                                              { "STL@highValRightAbove"				, "Hochwert rechtsoben:"																									},
                                              { "STL@typeInAllRightVal"				, "Bitte geben Sie alle Rechtswerte an."																	},
                                              { "STL@typeInAllHighVal"				, "Bitte geben Sie alle Hochwerte an."																		},
                                              { "STL@inputError"							, "Fehlerhafte Eingabe"																										},
                                              { "STL@missingInput"						, "Fehlende Eingabe"																											},
                                              { "STL@criticalError"						, "Kritischer Fehler"																											},
                                              { "STL@criticalErororOccured"		,	"Es ist ein kritischer Fehler aufgetreten"															},
                                              { "STL@chooseUserGroup"					, "Benutzergruppe ausw\u00E4hlen"																					},
                                              { "STL@pleaseChooseUserGroup"		, "Bitte w\u00E4hlen Sie Ihre Benutzergruppe:"														},
                                              { "STL@typeInUsernameAndPwd"		,	"Bitte geben Sie Ihren Benutzernamen und Ihr Kennwort an"								},
                                              { "STL@typeInUsername"					, "Bitte geben Sie einen Benutzernamen an "																},
                                              { "STL@usernameFor"							, "Benutzergruppen f\u00FCr "																							},
                                              { "STL@username"								, "Benutzername:"																													},
                                              { "STL@password"								, "Kennwort:"																															},
                                              { "STL@userGroup"								, "Benutzergruppe:"																												},
                                              { "STL@homeServer"							, "Heimatserver:"																													},
                                              { "STL@loginFailed"							, "Login fehlgeschlagen!"																									},
                                              { "STL@wrongUserName"						, "Falscher Benutzername"																									},
                                              { "STL@wrongPassword"						, "Falsches Kennwort"																											},
                                              { "STL@wrongUserGroup"					, "Falsche Benutzergruppe"																								},
                                              { "STL@wrongLocalServer"				, "Falscher LocalServer"																									},
                                              { "STL@passwordAcknowledgement"	, "Kennwortbest\u00E4tigung"																							},
                                              { "STL@moreOptions"							, "Erweiterte Optionen"																										},
                                              { "STL@server"									, "Server"																																},
                                              { "STL@OptionsDialog.tabbedPaneTitle"					, "SICAD IMS"																										},
                                              { "STL@OptionsDialog.plugInPaneTitle"					, "Installierte PlugIns"																										},
                                              { "STL@callServerIP"						, "CallServer IP Adresse:"																								},
                                              { "STL@maxConnections"					, "max. Verbindungen:"																										},
                                              { "STL@maxSearchResults"				, "max. Suchergebnisse:"																									},
                                              { "STL@componentView"						, "Komponenten Ansicht"																										},
                                              { "STL@propSizeChange"					, "Proportionale Gr\u00F6ssen\u00E4nderung"																},
                                              { "STL@showWhileSizeChange"			, "W\u00E4hrend Gr\u00F6ssen\u00E4nderung anzeigen"												},
                                              { "STL@aknowledge" 							, "Best\u00E4tigung:"																											},
                                              { "STL@fillAllFields"						, "Bitte f\u00FCllen Sie alle Felder aus."																},
                                              { "STL@notAvailable"						, "nicht verf\u00FCgbar"																									},
                                              { "STL@administrationFrom"			, "Verwaltung der " 																											},
                                              { "STL@administration"					, "Verwaltung"			 																											},
                                              { "STL@userGroupProfiles"				, "Benutzergruppenprofile"																								},
                                              { "STL@userGroupProfile"				, "Benutzergruppenprofil"																									},
                                              { "STL@userProfiles"						, "Benutzerprofile"																												},
                                              { "STL@userProfile"							, "Benutzerprofil"																												},
                                              { "STL@profiles"								, "Profile"																																},
                                              { "STL@searchProfiles"					, "Suchprofile"																														},
                                              { "STL@searchResults"						, "Suchergebnisse"																												},
                                              { "STL@pleaseInsertUserName"		, "<html><p>Bitte geben Sie einen Namen ein, </p><p>unter dem Sie die "		},
                                              { "STL@wantToSave"							, " speichern wollen."																										},
                                              { "STL@entry"										, "<html><p>Der Eintrag '"																								},
                                              { "STL@alreadyExistsOverwrite"	, "' ist bereits vorhanden.</p><p>Mu00F6chten Sie ihn " +
                                                "\u00FCberschreiben?</p></html>"																				},
                                                { "STL@typeInName"							, "Bitte geben Sie einen Namen ein."																			},
                                                { "STL@delProfileNotPermitted"	, "Sie haben nicht die Berechtigung ein Benutzergruppenprofil " +
                                                  "zu l\u00F6schen"																												},
                                                  { "STL@deleteProfile"						, "Profil l\u00F6schen"																										},
                                                  { "STL@pleaseChooseFirst"				, "Bitte w\u00E4hlen Sie zuerst ein "																			},
                                                  { "STL@noSelection"							, "Keine Selektion"																												},
                                                  { "STL@noSearchResultsAvailable", "Es sind keine Suchergebnisse vorhanden."																},
                                                  { "STL@noSearchValSelected"			, "Es wurde kein Suchkriterium gew\u00E4hlt."															},
                                                  { "STL@noSearchResults"					, "Keine Suchergebnisse"																									},
                                                  { "STL@selectAllTopics"					, "alle Themen selektieren"																								},
                                                  { "STL@notSelectableString"			, "<html><center><p>Dieses Suchkriterium ist aufgrund</p>"+
                                                    "<p>eingeschr\u00E4nkter Themenauswahl bzw.</p><p>Benutzerrechte nicht"+
                                                    " ausw\u00E4hlbar.</p></center></html>"																	},
                                                    { "STL@noGeoString"							, "<html><center><p>Suche ohne Raumbezug</p></center></html>"							},
                                                    { "STL@notExists"								, "nicht vorhanden"																												},
                                                    { "STL@coordsFromCatalogInUse"	, "<html><center><p>Die Koordinaten aus dem Katalog</p>"+
                                                      "<p>werden verwendet</p></center></html>"																},
                                                      { "STL@coordsFromMapInUse"			, "<html><center><p>Die Koordinaten aus der Karte</p>"+
                                                        "<p>werden verwendet</p></center></html>"																},
                                                        { "STL@withBufferFrom"					, "Mit Puffer von "																												},
                                                        { "stl@meter"										, "Metern"																																},
                                                        { "STL@areaConstraints"					, "Raumbezug"																															},
                                                        { "STL@street"									, "Strasse:"																															},
                                                        { "STL@streetName"							, "Strassennamen"																													},
                                                        { "STL@houseNumber"							, "Hausnummer"																														},
                                                        { "STL@houseNumberDoubleDot"		, "Hausnummer:"																														},
                                                        { "STL@gridSquare"							, "Planquadrat:"																												 	},
                                                        { "STL@kilometerSquare"					, "Kilometerquadrat:"																											},
                                                        { "STL@searchValue"							, "Suchbegriff"																														},
                                                        { "STL@caseSensitive"						, "Gross- und Kleinschreibung beachten."																	},
                                                        { "STL@fullTextSearch"					, "Volltextsuche"																													},
                                                        { "STL@lpeaseTypeInStreetName"	, "Bitte geben Sie einen Strassennamen ein."															},
                                                        { "STL@pleaseTypeInGridSquare"	, "Bitte geben Sie ein Planquadrat ein."																	},
                                                        { "STL@pleaseTypeInKMSquare"		, "Bitte geben Sie ein Kilometerquadrat ein."															},
                                                        { "STL@pleaseTypeInRightVals" 	, "Bitte geben Sie alle Rechtswerte an."																	},
                                                        { "STL@pleaseTypeInHighVals" 	  , "Bitte geben Sie alle Hochwerte an."																		},
                                                        { "STL@catalogCoordsNotAvaiable", "Die Koordianten aus dem Katalog sind nicht verf\u00FCgbar."						},
                                                        { "STL@mapCoordsNotAvailable"		, "Die Koordianten aus der Karte sind nicht verf\u00FCgbar."							},
                                                        { "STL@missingStreetName"				, "Strassenname fehlt"																										},
                                                        { "STL@missingGridSquare"				, "Planquadrat fehlt"																											},
                                                        { "STL@missingKMSquare"					, "Kilometerquadrat fehlt"																								},
                                                        { "STL@missingRightVal"					, "Rechtswert fehlt"																											},
                                                        { "STL@missingHightVal"					, "Hochwert fehlt"																												},
                                                        { "STL@missingCoordsFromCatalog", "keine Koordinaten aus Katalog"																					},
                                                        { "STL@missingCoordsFromMap"		, "keine Koordinaten aus Karte"																						},
                                                        { "STL@fullTextSearchRequires"	, "Die Volltextsuche ben\u00F6tigt mindestens drei Zeichen."							},
                                                        { "STL@searchDataWrongInput" 		, "<html><p>Fehler beim Absetzen der Suche</p><p>Ihre Eingabe " +
                                                          "enthielt m\u00F6glicherweise</p><p>fehlerhafte Daten.</p></html>"			},
                                                          { "STL@searchCanceled"					, "Suche abgebrochen"																											},
                                                          { "STL@pleaseInsert3SignsMin"		, "Bitte geben Sie mindestens drei Zeichen ein"													},
                                                          { "STL@noStreetnameFound"				, "Keine passenden Strassennamen gefunden"																},
                                                          { "STL@noHousenumberFound"			, "Keine passenden Hausnummern gefunden"																	},
                                                          { "STL@housenumbers"						, "Hausnummern"																														},
                                                          { "STL@housenumberFrom"					, "Hausnummern von "																											},
                                                          { "STL@objectsWillBeLoaded"			, "<html><p>Die selektierten Objekte aus der Karte werden geladen.</p>"+
                                                            "<p>Bitte haben Sie einen Augenblick Geduld</p></html>"									},
                                                            { "STL@dataTransferMapToNav"		, "Datentransfer Karte->Navigator"																				},
                                                            { "STL@performingSearch"				, "Suche wird durchgef\u00FChrt ..."																			},
                                                            { "STL@noObjectsFound"					, "Keine Objekte gefunden"																								},
                                                            { "STL@objetsFound"							, " Objekte gefunden"																											},
                                                            { "STL@serverError"							, "Server Error"																													},
                                                            { "STL@withoutLocationRelation"	, "Ohne Raumbezug"																												},
                                                            { "STL@streetAndHousenumber"		, "Strasse, Hausnummer"																										},
                                                            { "STL@planSquare"							, "Planquadrat"																														},
                                                            { "STL@kmSquare"								, "Kilometerquadrat"																											},
                                                            { "STL@coordinates"							, "Koordinaten"																														},
                                                            { "STL@useCatalogCoords"				, "Koordinaten aus Katalog verwenden"																			},
                                                            { "STL@useMapCoords"						, "Koordinaten aus Karte verwenden"																				},
                                                            { "STL@sort"										, "Sortierung"																														},
                                                            { "STL@useSort"									, "Sortierung verwenden"																									},
                                                            { "STL@sortAscent"							, "aufsteigend sortieren"																									},
                                                            { "STL@sortDescent"							, "absteigend sortieren"																									},
                                                            { "STL@sortDescent"							, "absteigend sortieren"																									},
                                                            { "STL@sortDescent"							, "absteigend sortieren"																									},
                                                            { "STL@pleaseChoose"						, "Bitte ausw\u00E4hlen!"																									},
                                                            { "STL@streets"									, "Strassen"																															},
                                                            { "STL@closeNavigator"					, "<html><center><p>M\u00F6chten Sie den</p>"+
                                                              "<p>Navigator wirklich schliessen?</p></center></html>"									},
                                                              { "STL@noObjectsFound"					, "Es wurden keine Objekte gefunden."																			},
                                                              { "STL@noMatch"									, "Keine Treffer"																													},
                                                              { "STL@toMuchMatches"						, "Zuviele Treffer"																												},
                                                              { "STL@first100Objects"					, "Es werden nur die ersten 1000 Objekte angezeigt."											},
                                                              { "STL@establishSrvConnection"	, "Serververbindung herstellen"																						},
                                                              { "STL@requestUserData"					, "Abfragen der Benutzerdaten"																						},
                                                              { "STL@cacheClasses"						, "Classes cachen"																												},
                                                              { "STL@setEventQueue"						, "EventQueue setzen"																											},
                                                              { "STL@createWidgets1"					, "Widgets erzeugen ."																										},
                                                              { "STL@createWidgets2"					, "Widgets erzeugen . ."																									},
                                                              { "STL@createWidgets3"					, "Widgets erzeugen . . ."																								},
                                                              { "STL@createWidgets4"					, "Widgets erzeugen . . . ."																							},
                                                              { "STL@createWidgets5"					, "Widgets erzeugen . . . . ."																						},
                                                              { "STL@includeSICADIMSClient"		, "SICAD IMS Client einbinden"																						},
                                                              { "STL@createControls"					, "Controls erzeugen"																											},
                                                              { "STL@registerListener"				, "Listener registrieren"																									},
                                                              { "STL@addWidgetsToLayoutMgr"		, "Widgets zum LayoutManager hinzuf\u00FCgen"															},
                                                              { "STL@topicAndObjectKatalog"		, "Themen- und Objektkatalog"																							},
                                                              { "STL@objectInformation"				, "Informationen zum Objekt"																							},
                                                              { "STL@addLayoutMgrToNavigator"	, "LayoutManager zum Navigator hinzuf\u00FCgen"														},
                                                              { "STL@cantStartNavigator"			, "<html><p>Der Navigator konnte nicht gestartet werden.</p></html>"			},
                                                              { "STL@navigatorStarted"				, "Navigator konnte gestartet werden"																			},
                                                              { "STL@welcomeTo"								, "<html><center>Willkommen beim<h2>"																			},
                                                              { "STL@startNavigator"					, "Navigator starten"																											},
                                                              { "STL@callServerOn"						, "CallServer auf "																												},
                                                              { "STL@restart"									, "Neustart"																															},
                                                              { "STL@unknownError"						, "Es ist ein unbekannter Fehler aufgetreten"															},
                                                              { "STL@navigatorWillBeStarted"	, "Navigator wird gestartet"																							},
                                                              { "STL@startingFinished"				, "Startvorgang erfolgreich abgeschlossen"																},
                                                              { "STL@startingCanceled"				, "Startvorgang abgebrochen"																							},
                                                              { "STL@wunda"										, "Wuppertaler Navigations- und Datenmanagementsystem"										},
                                                              { "STL@classCacheError"					, "<html><p>ClassCache Fehler:</p><p>Die Classes konnten nicht"+
                                                                "vom Server geladen werden.</p></html>"																	},
                                                                { "STL@errorWhileInit"					, "<html><p>Fehler w\u00E4hrend der Initialisierung:</p>"									},
                                                                { "STL@connectionToSrv"					,	"<p>Die Verbindung zum Server"																					},
                                                                { "STL@cantBeEstablished"				, " konnte nicht hergestellt werden!</p></html>"													},
                                                                { "STL@cantLoadUserData"				, "<p>Die Benutzerdaten konnten nicht geladen werden.</p></html>"					},
                                                                { "STL@cantLoadLocalSrvList"		, "<p>Die Liste der LocalServer konnte nicht geladen werden</p></html>"		},
                                                                { "STL@cantLoadUserGroupList"		, "<p>Die Liste der Benutzergruppen konnte nicht geladen "+
                                                                  "werden</p></html>"																											},
                                                                  { "STL@userGroupListForUser"		, "<p>Die Liste der Benutzergruppen fuer den Benutzer '"									},
                                                                  { "STL@cantLoadStdIcons"				, "<p>Die Standard-Icons konnten nicht geladen werden.</p></html>"				},
                                                                  { "STL@fromServer"							, "'</p><p>vom Server "																										},
                                                                  { "STL@cantLoadEntryPoint"			, "<p>Die Einstiegsknoten konnten nicht geladen werden</p></html>"				},
                                                                  { "STL@cantBeLoadedHTML"				, " konnte nicht geladen werden</p></html>"																},
                                                                  { "STL@cantBeLoaded"						, " konnte nicht geladen werden</p>"																			},
                                                                  { "STL@noEntryForTranslationSrv", "<p>Kein Eintrag f\u00FCr den Translation Server vorhanden</p></html>"	},
                                                                  { "STL@cantConnectToNodeSrv"		, "<html><p>Server Fehler: nodeServer</p>"+
                                                                    "<p>Konnte keine Verbindung zum nodeServer herstellen.</p></html>"			},
                                                                    { "STL@cantConnectToUserSrv"		, "<html><p>Server Fehler: ui</p>"+
                                                                      "<p>Konnte keine Verbindung zum userServer herstellen.</p></html>"			},
                                                                      
                                                                      
                                                                      { "STL@cantConnectToCallSrv"		, "<html><p>Server Fehler: cs</p>"+
                                                                        "<p>Konnte keine Verbindung zum callServer herstellen.</p></html>"			},
                                                                        
                                                                        
                                                                        
                                                                        
                                                                        
                                                                        
                                                                        
                                                                        { "STL@cantConnectToSearchSrv"	, "<html><p>Server Fehler: search</p>"+
                                                                          "<p>Konnte keine Verbindung zum searchServer herstellen.</p></html>"		},
                                                                          {	"STL@cantConnectToProfileSrv"	, "<html><p>Server Fehler: profile</p><p>Konnte keine Verbindung zum"+
                                                                                "profileServer herstellen.</p>"+
                                                                                "<p>Sie werden keinen Zugriff auf Ihre Suchprofile haben!</p></html>"		},
                                                                                { "STL@cantConnectToTranslSrv"	, "<html><p>Server Fehler: translationServer</p>"+
                                                                                  "<p>Konnte keine Verbindung zum translationServer herstellen.</p>"+
                                                                                  "</html>"																																},
                                                                                  { "STL@cantInitCMCache"					, "<html><p>Server Fehler:</p><p>Der Class- und Methodencache "+
                                                                                    "konnte nicht initialisiert werden.</p></html>"													},
                                                                                    { "STL@cantLoadClassFromCache"	, "<html><p>ClassAndMethodCache Fehler:</p>"+
                                                                                      "<p>Eine Class konnte nicht aus dem Cache geladen werden.</p></html>"		},
                                                                                      { "STL@cantLoadCMFromCache"			, "<html><p>ClassAndMethodCache Fehler:</p><p>Die Classes oder Methoden"+
                                                                                        " konnten nicht aus dem Cache geladen werden.</p></html>"								},
                                                                                        { "STL@srvErrorChildsOfNodes"		, "<html><p>Server Fehler:</p><p>Die Kinder des Knotens "									},
                                                                                        { "STL@possiblySrvHasBeen"			, "<p>Moeglicherweise wurde der Server "																	},
                                                                                        { "STL@waitOrConntactAdmin"			, " heruntergefahren.</p><p>Warten Sie einige Minuten bis der Server "+
                                                                                          "wieder verf\u00FCgbar ist,</p><p>oder wenden Sie sich an einen "+
                                                                                          "Administrator.</p></html>"																							},
                                                                                          { "STL@cantProceedAutoSearch"		, "<html><p>Fehler bei der Sortierung:</p><p>Die automatische Sortierung"+
                                                                                            " konnte nicht durchgef\u00FChrt werden.</p><p>\u00DCberpr\u00FCfen "+
                                                                                            "Sie die Einstellungen in der Datenbank:</p><p>nodeID: "								},
                                                                                            { "STL@localSrvName"						, " localServerName: "																										},
                                                                                            { "STL@sortsChildsBy"						, " sortChildsBy: "																												},
                                                                                            { "STL@cantStartSearch"					, "<html><p>Server Fehler:</p>"+
                                                                                              "<p>Die Suche konnte nicht abgesetzt werden.</p></html>"								},
                                                                                              { "STL@cantLoadMethodList"			, "<html><p>Server Fehler:</p>"+
                                                                                                "<p>Die Liste der Methoden konnte nicht geladen werden.</p></html>"			},
                                                                                                { "STL@convertingNavToMapError"	, "<html><p>Server Fehler:</p><p>Umsetzung der Objekte f\u00FCr "+
                                                                                                  "Navigator->Karte fehlgeschalgen.</p></html>"														},
                                                                                                  { "STL@cantLoadStreetList"			, "<html><p>Server Fehler:</p><p>Die Liste der verf\u00FCgbaren "+
                                                                                                    "Strassen konnte nicht geladen werden.</p></html>"											},
                                                                                                    { "STL@cantLoadHousenumberList"	, "<html><p>Server Fehler:</p><p>Die Liste der verf\u00Fcgbaren "+
                                                                                                      "Hausnummern konnte nicht geladen werden.</p></html>"										},
                                                                                                      { "STL@cantLoadSearchProfiles"	, "<html><p>Server Fehler:</p><p>Die Suchprofile "+
                                                                                                        "konnten nicht geladen werden</p></html>"																},
                                                                                                        { "STL@searchProfileForUserGr"	, "<html><p>Server Fehler:</p>"+
                                                                                                          "<p>Das Suchprofil f\u00FCr die Benutzergruppe "												},
                                                                                                          { "STL@cantBeOverwritten"				, "</p><p>konnte nicht \u00FCberschrieben werden</p></html>"							},
                                                                                                          { "STL@cantDeleteSearchProfile"	, "<html><p>Server Fehler:</p>"+
                                                                                                            "<p>Das Suchprofil konnte nicht gel\u00F6scht werden</p></html>"				},
                                                                                                            { "STL@cantLoadUserSearProfile"	, "<html><p>Server Fehler:</p><p>Die Liste der Benutzer Suchprofile "+
                                                                                                              "konnte nicht geladen werden</p></html>"																},
                                                                                                              { "STL@searchProfileForUser"		, "<html><p>Server Fehler:</p><p>Das Suchprofil f\u00FCr den Benutzer "		},
                                                                                                              { "STL@cantBeSaved"							, "</p><p>konnte nicht gespeichert werden</p></html>"											},
                                                                                                              { "STL@cantLoadUserGrSearProf"	, "<html><p>Server Fehler:</p><p>Die Liste der Benutzergruppen "+
                                                                                                                "Suchprofile konnte nicht geladen werden</p></html>"										},
                                                                                                                { "STL@cantLoadUserSearResults"	, "<html><p>Server Fehler:</p><p>Die Liste der Benutzer "+
                                                                                                                  "Suchergebnisprofile konnte nicht geladen werden</p></html>"						},
                                                                                                                  { "STL@cantLoadSearchResults"		, "<html><p>Server Fehler:</p><p>Die Suchergebnisse konnten nicht "+
                                                                                                                    "geladen werden</p></html>"																							},
                                                                                                                    { "STL@searchResultProForUser"	, "<html><p>Server Fehler:</p><p>Das Suchergebnisprofil f\u00FCr "+
                                                                                                                      "den Benutzer "																													},
                                                                                                                      { "STL@cantDeleteSearResProfile", "<html><p>Server Fehler:</p><p>Das Suchergebnisprofil konnte "+
                                                                                                                        "nicht gel\u00F6scht werden</p></html>"																			},
                                                                                                                        { "STL@chooseObjForDescr"				, "<html><h1>W\u00E4hlen Sie ein Objekt aus,<br>um eine Beschreibung "+
                                                                                                                          "zu erhalten</h1></html>"																								},
                                                                                                                          { "STL@descrWillBeLoaded"				, "Beschreibungen werden geladen"																					},
                                                                                                                          { "STL@desrcHasBeenLoadedSucc"	, "Beschreibungen wurden erfolgreich geladen"															},
                                                                                                                          { "STLcantOpenURL"							, "<html><p><h1>Folgende URL konnte nicht ge\u00F6ffnet "+
                                                                                                                            "werden: </h1></p><b>"																									},
                                                                                                                            { "STL@cantLoadDescriptionsTAG"	, "<html><p><h1>Die Beschreibungen konnten nicht geladen "+
                                                                                                                              "werden</h1></p></html>"																								},
                                                                                                                              { "STL@cantGetDataSource"				, "<html><p>Die Datasource konnte nicht geladen werden.</p></html>"																								},
                                                                                                                              { "STL@cantLoadDescriptions"		, "Beschreibungen konnten nicht geladen werden"														},
                                                                                                                              { "STL@treeInstanceated"				, "Tree instantiiert"																											},
                                                                                                                              { "STL@knodeSelected"						, " Knoten selektiert"																										},
                                                                                                                              { "STL@loadedDataFromServer"		, "Daten vom Server geladen"																							},
                                                                                                                              { "STL@dataLoadedFromCache"			, "Daten aus Cache geladen"																								},
                                                                                                                              { "STL@loadingData"							, "Daten werden geladen ..."																							},
                                                                                                                              { "STL@willBeLoaded"						, "wird geladen ..."																											},
                                                                                                                              { "STL@wrongNodeType"						, "<TREENODE> Fehler: falscher Node-Typ: "																},
                /*
                 * Mnemonics
                 */
                                                                                                                              { "STL@AMnemonic" , "A"},
                                                                                                                              { "STL@BMnemonic"	, "B"},
                                                                                                                              { "STL@CMnemonic"	, "C"},
                                                                                                                              { "STL@DMnemonic"	, "D"},
                                                                                                                              { "STL@EMnemonic"	, "E"},
                                                                                                                              { "STL@FMnemonic"	, "F"},
                                                                                                                              { "STL@GMnemonic"	, "G"},
                                                                                                                              { "STL@HMnemonic"	, "H"},
                                                                                                                              { "STL@IMnemonic"	, "I"},
                                                                                                                              { "STL@JMnemonic"	, "J"},
                                                                                                                              { "STL@KMnemonic"	, "K"},
                                                                                                                              { "STL@LMnemonic"	, "L"},
                                                                                                                              { "STL@MMnemonic"	, "M"},
                                                                                                                              { "STL@NMnemonic"	, "N"},
                                                                                                                              { "STL@OMnemonic"	, "O"},
                                                                                                                              { "STL@PMnemonic"	, "P"},
                                                                                                                              { "STL@QMnemonic"	, "Q"},
                                                                                                                              { "STL@RMnemonic"	, "R"},
                                                                                                                              { "STL@SMnemonic"	, "S"},
                                                                                                                              { "STL@TMnemonic"	, "T"},
                                                                                                                              { "STL@UMnemonic"	, "U"},
                                                                                                                              { "STL@VMnemonic"	, "V"},
                                                                                                                              { "STL@WMnemonic"	, "W"},
                                                                                                                              { "STL@XMnemonic"	, "X"},
                                                                                                                              { "STL@YMnemonic"	, "Y"},
                                                                                                                              { "STL@ZMnemonic"	, "Z"},
                                                                                                                              
                                                                                                                              { "STL@aMnemonic" , "a"},
                                                                                                                              { "STL@bMnemonic"	, "b"},
                                                                                                                              { "STL@cMnemonic"	, "c"},
                                                                                                                              { "STL@dMnemonic"	, "d"},
                                                                                                                              { "STL@eMnemonic"	, "e"},
                                                                                                                              { "STL@fMnemonic"	, "f"},
                                                                                                                              { "STL@gMnemonic"	, "g"},
                                                                                                                              { "STL@hMnemonic"	, "h"},
                                                                                                                              { "STL@iMnemonic"	, "i"},
                                                                                                                              { "STL@jMnemonic"	, "j"},
                                                                                                                              { "STL@kMnemonic"	, "k"},
                                                                                                                              { "STL@lMnemonic"	, "l"},
                                                                                                                              { "STL@mMnemonic"	, "m"},
                                                                                                                              { "STL@nMnemonic"	, "n"},
                                                                                                                              { "STL@oMnemonic"	, "o"},
                                                                                                                              { "STL@pMnemonic"	, "p"},
                                                                                                                              { "STL@qMnemonic"	, "q"},
                                                                                                                              { "STL@rMnemonic"	, "r"},
                                                                                                                              { "STL@sMnemonic"	, "s"},
                                                                                                                              { "STL@tMnemonic"	, "t"},
                                                                                                                              { "STL@uMnemonic"	, "u"},
                                                                                                                              { "STL@vMnemonic"	, "v"},
                                                                                                                              { "STL@wMnemonic"	, "w"},
                                                                                                                              { "STL@xMnemonic"	, "x"},
                                                                                                                              { "STL@yMnemonic"	, "y"},
                                                                                                                              { "STL@zMnemonic"	, "z"},
    };
}

