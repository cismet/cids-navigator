package Sirius.navigator.method;

//import Sirius.navigator.NavigatorLogger;
import Sirius.navigator.connection.SessionManager;
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
 * Programmers		:	Pascal
 *
 * Project			:	WuNDA 2
 * Filename		:
 * Version			:	1.0
 * Purpose			:
 * Created			:	21.08.2000
 * History			:
 *
 *******************************************************************************/

import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

import org.apache.log4j.Logger;
import Sirius.server.middleware.types.*;
import Sirius.server.newuser.permission.*;
import Sirius.navigator.tools.*;

import Sirius.navigator.types.iterator.*;
import Sirius.navigator.types.treenode.*;
import Sirius.navigator.ui.*;
import Sirius.navigator.exception.*;
import Sirius.navigator.ui.tree.*;
import Sirius.server.localserver.method.Method;
import Sirius.navigator.ui.dialog.*;
import Sirius.server.localserver.attribute.MemberAttributeInfo;




public class MethodManager
{
    // availability
    public final static long NONE = 0;
    public final static long PURE_NODE = 1;
    public final static long CLASS_NODE = 2;
    public final static long OBJECT_NODE = 4;
    public final static long MULTIPLE = 8;
    public final static long CLASS_MULTIPLE = 16;
    // public final static long OBJECT_NODE_MULTIPLE = 32;
    // public final static long DOMAIN_MULTIPLE = 64;

    
    private final static Logger logger = Logger.getLogger(MethodManager.class);
    
    private static MethodManager manager = null;
    
    //protected ControlModel model = null;
    //protected CoordinateChooser coordinateChooser;
    
    protected MetaClass[] classArray = null;
    protected MetaObject[] objectArray = null;
    protected Vector methodVector = new Vector(5,1);
    
    protected boolean wrongParameters = false;
  
    
    
    private MethodManager()
    {
        
    }
    private final static Object blocker=new Object();
    public final static MethodManager getManager()
    {
        synchronized(blocker)
        {
            if(manager == null)
            {
                manager = new MethodManager();
            }
            
            return manager;
        }
    }
    
    public final static void destroy()
    {
        synchronized(blocker)
        {
            logger.warn("destroying singelton MethodManager instance");  // NOI18N
            manager = null;
        }
    }
    
    
    /*public MethodManager(ControlModel model)
    {
        this.model = model;
        initMethodManager();
    }*/
    
    /*protected void initMethodManager()
    {
     
    }*/
    
    /**
     * Liefert die Namen und IDs der verfuegbaren Methoden, in Abhaengigkeit
     * der selektierten Knoten des Baums.
     *
     * @param selectedTree Der ausgewaehlte Baum
     * @return Eine String Matrix: [i][0] = Methodenname, [i][1] = MethodenID
     */
    /*public String[][] getAvailableMethods()
    {
        classArray = ComponentRegistry.getRegistry().getActiveCatalogue().getSelectedClasses(MetaCatalogueTree.OBJECT_NODES);
        objectArray = ComponentRegistry.getRegistry().getActiveCatalogue().getSelectedObjects();
        methodVector.clear();
     
        if(evaluateMethods())
        {
            String[][] methodIDs = new String[methodVector.size()][2];
     
            for(int i = 0; i < methodVector.size(); i++)
            {
                methodIDs[i][0] = ((Method)methodVector.elementAt(i)).getName();
                methodIDs[i][1] = String.valueOf(((Method)methodVector.elementAt(i)).getID());
            }
     
            return methodIDs;
        }
        else
        {
            return null;
        }
    }*/
    
    protected Method getMethod(String methodID)
    {
        Method tmpMethod = null;
        
        for(int i = 0; i < methodVector.size(); i++)
        {
            tmpMethod = (Method)methodVector.elementAt(i);
            if(String.valueOf(tmpMethod.getID()).equals(methodID))
                return tmpMethod;
        }
        
        return null;
    }
    
   /* public void callToSIMS(MetaCatalogueTree ComponentRegistry.getRegistry().getActiveCatalogue()) throws Exception
    {
        classArray = ComponentRegistry.getRegistry().getActiveCatalogue().getSelectedClasses(MetaCatalogueTree.OBJECT_NODES);
        objectArray = ComponentRegistry.getRegistry().getActiveCatalogue().getSelectedObjects();
        methodVector.clear();
        String methodID = null;
    
        if(evaluateMethods())
        {
            for(int i = 0; i < methodVector.size(); i++)
            {
                char type = ((Method)methodVector.elementAt(i)).getType();
    
                if(type == 'O' || type == 'L' || type == 'M')
                    methodID = String.valueOf(((Method)methodVector.elementAt(i)).getID());
            }
    
            if(methodID != null)
            {
                callMethod(methodID);
            }
            else
            {
                JOptionPane.showMessageDialog(ComponentRegistry.getRegistry().getMainWindow(), StringLoader.getString("STL@methodNotAvailable"), StringLoader.getString("STL@navigatorToMap"), JOptionPane.WARNING_MESSAGE);
                //_TA_JOptionPane.showMessageDialog(model.navigator, "<html><p>Diese Methode ist nicht verfuegbar:</p><p>Objekt unterstuetzt diese Metohde nicht.</p></html>", "Navigator->Karte", JOptionPane.WARNING_MESSAGE);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(ComponentRegistry.getRegistry().getMainWindow(), StringLoader.getString("STL@noObjectSelected"), StringLoader.getString("STL@navigatorToMap"), JOptionPane.WARNING_MESSAGE);
            //_TA_JOptionPane.showMessageDialog(model.navigator, "<html><p>Diese Methode ist nicht verfuegbar:</p><p>Keine Objekte ausgewaehlt.</p></html>", "Navigator->Karte", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /*
    public void callFromSIMS(MetaCatalogueTree ComponentRegistry.getRegistry().getActiveCatalogue()) throws Exception
    {
        classArray = ComponentRegistry.getRegistry().getActiveCatalogue().getSelectedClasses(MetaCatalogueTree.OBJECT_NODES);
        objectArray = ComponentRegistry.getRegistry().getActiveCatalogue().getSelectedObjects();
        methodVector.clear();
        String methodID = null;
    
    
        //SICADTheme[] activeThemes = null;
        SICADLayer[] activeLayers = null;
        SICADObject[] activeObjects = null;
        SICADCoordinate activeCoordinate = null;*/
    
                /*try
                {
                activeThemes = model.isFloatingFrame.getActiveThemes();
                }
                catch (SIMSException se)
                {
                activeThemes = null;
                //if(NavigatorLogger.DEV)se.printStackTrace();
                }*/
    
        /*try
        {
            activeLayers = model.isFloatingFrame.getActiveLayers();
        }
        catch (SIMSException se)
        {
            activeLayers = null;
            //if(NavigatorLogger.DEV)se.printStackTrace();
        }
        try
        {
            if(NavigatorLogger.SIMS_VERBOSE)NavigatorLogger.printMessage("<SIMS> ObjectsSelected: " + model.isFloatingFrame.isObjectsSelected());
         
            if(model.isFloatingFrame.isObjectsSelected())
            {
                activeObjects = model.isFloatingFrame.getActiveObjects();
            }
        }
        catch (SIMSException se)
        {
            activeObjects = null;
            //if(NavigatorLogger.DEV)se.printStackTrace();
        }
        try
        {
            activeCoordinate = model.isFloatingFrame.getActiveCoordinate();
        }
        catch (SIMSException se)
        {
            activeCoordinate = null;
            //if(NavigatorLogger.DEV)se.printStackTrace();
        }
         
                /*
                try
                {
                activeLayers = model.isFloatingFrame.getActiveLayers();
                }
                catch (SIMSException se)
                {
                activeLayers = null;
                //if(NavigatorLogger.DEV)se.printStackTrace();
                }*/
    
       /* if(activeLayers != null)
        {
            if(NavigatorLogger.SIMS_VERBOSE)NavigatorLogger.printMessage("<SIMS> Paramateruebergabe Karte->Navigator");
        
            SiriusParseInfo siriusParseInfo = null;
            Hashtable themeHashtable = new Hashtable(2);
            LsClassSelection tmpSelection;
            String lsName;
        
            for(int i = 0; i < activeLayers.length; i++)
            {
                if(activeLayers[i].getLayerID() != null && activeLayers[i].getIMSName() != null)
                {
                    siriusParseInfo = ConnectionHandler.translate(activeLayers[i].getLayerID(), activeLayers[i].getIMSName());
        
                    // Wenn siriusParseInfo == null: LayerID unbekannt (keine Class).
                    if(siriusParseInfo != null)
                    {
                        lsName = siriusParseInfo.getLocalServerName();
        
                        if(NavigatorLogger.SIMS_VERBOSE)NavigatorLogger.printMessage("<SIMS> SICAD Theme ID: " + activeLayers[i].getLayerID() + " = Sirius Class ID: " + siriusParseInfo.getClassID());
        
                        // neues Thema (Class) selektieren
                        if(!themeHashtable.containsKey(lsName))
                        {
                            tmpSelection = new LsClassSelection(lsName);
                            tmpSelection.addClassID(siriusParseInfo.getClassID());
                            themeHashtable.put(lsName, tmpSelection);
                        }
                        else
                        {
                            ((LsClassSelection)themeHashtable.get(lsName)).addClassID(siriusParseInfo.getClassID());
                        }
                    }
                }
            }
        
            if(activeObjects != null)
            {
                Vector searchTypes = new Vector(activeObjects.length, 1);
        
                for(int i = 0; i < activeObjects.length; i++)
                {
                    if(activeObjects[i] != null && activeObjects[i].getObjectID() != null)
                    {
                        if(NavigatorLogger.SIMS_VERBOSE)NavigatorLogger.printMessage("<SIMS> SICAD Object ID: " + activeObjects[i].getObjectID());
                        searchTypes.add(new TextSearchType(activeObjects[i].getObjectID(), "SICAD_OBJECT", "SICAD_OBJECT", Long.MAX_VALUE, model.searchDialog.getModel().getMaxSearchResults()));
                    }
                }
        
        
                model.searchDialog.performSIMSSearch(model.navigator, searchTypes, new Vector(themeHashtable.values()));
        
                                /*
                                Node[] resultNodes = ConnectionHandler.search(searchTypes, new Vector(themeHashtable.values()));
        
                                if(resultNodes == null || resultNodes.length < 1)
                                {
                                JOptionPane.showMessageDialog(null, "Es wurden keine Objekte gefunden.", "Keine Treffer", JOptionPane.WARNING_MESSAGE);
                                }
                                else if(resultNodes.length >= 1000)
                                {
                                JOptionPane.showMessageDialog(null, "Es werden nur die ersten 1000 Objekte angezeigt.", "Zuviele Treffer", JOptionPane.INFORMATION_MESSAGE);
                                model.SearchResultsTree.setResultNodes(resultNodes, false);
                                model.SearchResultsTree.bringToFront();
                                }
                                else
                                {
                                model.SearchResultsTree.setResultNodes(resultNodes, false);
                                model.SearchResultsTree.bringToFront();
                                }
        */
          /*  }
            else if(activeCoordinate != null)
            {
                String coordinate[] = activeCoordinate.toStringArray();
                //String siriusCoordinate[] = new String[coordinate.length];
           
                if(NavigatorLogger.SIMS_VERBOSE)NavigatorLogger.printMessage("<SIMS> SICAD Coordinate: "+coordinate[0]+","+coordinate[1]+","+coordinate[2]+","+coordinate[3]);
                for(int i = 0; i < coordinate.length; i++)
                    coordinate[i] = coordinate[i].substring(0, coordinate[i].indexOf('.'));
           
                //siriusCoordinate[0] = coordinate[0];
                //siriusCoordinate[1] = coordinate[2];
                //siriusCoordinate[2] = coordinate[1];
                //siriusCoordinate[3] = coordinate[3];
           
                //if(NavigatorLogger.SIMS_VERBOSE)NavigatorLogger.printMessage("<SIMS> Sirius Coordinate: "+coordinate[0]+","+coordinate[1]+","+coordinate[2]+","+coordinate[3]);
           
                Vector searchTypes = new Vector(1,1);
                searchTypes.add(new BoundingBoxSearchType(new BoundingBox(new Coordinate(Integer.parseInt(coordinate[0]), Integer.parseInt(coordinate[1])), new Coordinate(Integer.parseInt(coordinate[2]), Integer.parseInt(coordinate[3]))), "SICAD_COORDINATE", "SICAD_COORDINATE", Long.MAX_VALUE, model.searchDialog.getModel().getMaxSearchResults()));
           
                if(siriusParseInfo != null)
                    {
                        lsName = siriusParseInfo.getLocalServerName();
           
                        if(NavigatorLogger.SIMS_VERBOSE)NavigatorLogger.printMessage("<SIMS> SICAD Theme ID: " + activeLayers[i].getLayerID() + " = Sirius Class ID: " + siriusParseInfo.getClassID());
           
                        // neues Thema (Class) selektieren
                        if(!themeHashtable.containsKey(lsName))
                        {
                            tmpSelection = new LsClassSelection(lsName);
                            tmpSelection.addClassID(siriusParseInfo.getClassID());
                            themeHashtable.put(lsName, tmpSelection);
                        }
                        else
                        {
                            ((LsClassSelection)themeHashtable.get(lsName)).addClassID(siriusParseInfo.getClassID());
                        }
                    }
           
           
                model.searchDialog.performSIMSSearch(model.navigator, searchTypes, new Vector(themeHashtable.values()));
           
                                /*
                                Node[] resultNodes = ConnectionHandler.search(searchTypes, new Vector(themeHashtable.values()));
           
                                if(resultNodes == null || resultNodes.length < 1)
                                {
                                JOptionPane.showMessageDialog(null, "Es wurden keine Objekte gefunden.", "Keine Treffer", JOptionPane.WARNING_MESSAGE);
                                }
                                else if(resultNodes.length >= 1000)
                                {
                                JOptionPane.showMessageDialog(null, "Es werden nur die ersten 1000 Objekte angezeigt.", "Zuviele Treffer", JOptionPane.INFORMATION_MESSAGE);
                                model.SearchResultsTree.setResultNodes(resultNodes, false);
                                model.SearchResultsTree.bringToFront();
                                }
                                else
                                {
                                model.SearchResultsTree.setResultNodes(resultNodes, false);
                                model.SearchResultsTree.bringToFront();
                                }
           */
       /*     }
                        /*else if(activeLayers != null)
                        {
        
                        }*/
    /*        else
            {
                JOptionPane.showMessageDialog(model.navigator, StringLoader.getString("STL@noCoordsSelected"), StringLoader.getString("STL@mapToNavigator"), JOptionPane.WARNING_MESSAGE);
                //_TA_JOptionPane.showMessageDialog(model.navigator, "<html><p>Diese Methode ist nicht verfuegbar:</p><p>Es sind keine Objekte oder Koordinaten ausgewaehlt.</p></html>", "Karte->Navigator", JOptionPane.WARNING_MESSAGE);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(model.navigator, StringLoader.getString("STL@noLayerSelected"), StringLoader.getString("STL@mapToNavigator"),JOptionPane.WARNING_MESSAGE);
            //_TA_JOptionPane.showMessageDialog(model.navigator, "<html><p>Diese Methode ist nicht verfuegbar:</p><p>Es sind keine Layer ausgewaehlt.</p></html>", "Karte->Navigator", JOptionPane.WARNING_MESSAGE);
        }
     
     
        //Node[] search(Vector searchTypes, themeHashtable.values())
     
     
    }*/
    
    public void callSpecialTreeCommand()
    {
        if(ComponentRegistry.getRegistry().getActiveCatalogue() instanceof SearchResultsTree)
        {
            if(!((SearchResultsTree)ComponentRegistry.getRegistry().getActiveCatalogue()).removeResultNodes(ComponentRegistry.getRegistry().getActiveCatalogue().getSelectedNodes()))
            {
                JOptionPane.showMessageDialog(ComponentRegistry.getRegistry().getMainWindow(),
                        org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.callSpecialTreeCommand().JOptionPane_anon1.message"),
                        org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.callSpecialTreeCommand().JOptionPane_anon1.title"),
                        JOptionPane.INFORMATION_MESSAGE);
                //_TA_JOptionPane.showMessageDialog(model.navigator, "<html><p>Bitte stellen Sie sicher, dass sie mindestens einen Knoten selektiert haben,</p><p>und dass es sicht dabei um einen Root-Knoten handelt.</p></html>", "Knoten konnten nicht geloescht werden", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else if(ComponentRegistry.getRegistry().getActiveCatalogue() instanceof MetaCatalogueTree)
        {
            DefaultMetaTreeNode[] selectedTreeNodes = ComponentRegistry.getRegistry().getActiveCatalogue().getSelectedNodesArray();
            
            if(selectedTreeNodes != null && selectedTreeNodes.length > 0)
            {
                Node[] selectedNodes = new Node[selectedTreeNodes.length];
                
                for(int i = 0; i < selectedTreeNodes.length; i++)
                {
                    selectedNodes[i] = selectedTreeNodes[i].getNode();
                }
                
                ComponentRegistry.getRegistry().getSearchResultsTree().setResultNodes(selectedNodes, true);
            }
            else
            {
                JOptionPane.showMessageDialog(ComponentRegistry.getRegistry().getMainWindow(),
                        org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.callSpecialTreeCommand().JOptionPane_anon2.message"),
                        org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.callSpecialTreeCommand().JOptionPane_anon2.title"),
                        JOptionPane.INFORMATION_MESSAGE);
                //_TA_JOptionPane.showMessageDialog(model.navigator, "<html><p>Bitte stellen Sie sicher, dass sie mindestens einen Knoten selektiert haben.</p></html>", "Knoten konnten nicht uebertragen werden", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    public void showSearchResults()
    {
        ComponentRegistry.getRegistry().getGUIContainer().select(ComponentRegistry.SEARCHRESULTS_TREE);
    }
    
    public void showAboutDialog()
    {
        AboutDialog aboutDialog = ComponentRegistry.getRegistry().getAboutDialog();
        aboutDialog.pack();
        aboutDialog.setLocationRelativeTo(ComponentRegistry.getRegistry().getMainWindow());
        aboutDialog.show();
    }
    
    public void showQueryResultProfileManager()
    {
        ComponentRegistry.getRegistry().getQueryResultProfileManager().setLocationRelativeTo(ComponentRegistry.getRegistry().getMainWindow());
        ComponentRegistry.getRegistry().getQueryResultProfileManager().show();
    }
    
    public void showPasswordDialog()
    {
        ComponentRegistry.getRegistry().getPasswordDialog().setLocationRelativeTo(ComponentRegistry.getRegistry().getMainWindow());
        ComponentRegistry.getRegistry().getPasswordDialog().show();
    }
    
    public void showPluginManager()
    {
        ComponentRegistry.getRegistry().getPluginManager().setLocationRelativeTo(ComponentRegistry.getRegistry().getMainWindow());
        ComponentRegistry.getRegistry().getPluginManager().show();
    }
    
    public void showSearchDialog() //throws Exception
    {
        //this.showSearchDialog(false);
        
        ComponentRegistry.getRegistry().getSearchDialog().pack();
        ComponentRegistry.getRegistry().getSearchDialog().setLocationRelativeTo(ComponentRegistry.getRegistry().getMainWindow());
        ComponentRegistry.getRegistry().getSearchDialog().show();
    }
    
    public void showQueryProfilesManager() //throws Exception
    {
        //this.showSearchDialog(false);
        
        ComponentRegistry.getRegistry().getSearchDialog().pack();
        ComponentRegistry.getRegistry().getSearchDialog().setLocationRelativeTo(ComponentRegistry.getRegistry().getMainWindow());
        ComponentRegistry.getRegistry().getSearchDialog().showQueryProfilesManager();
    }
    
    /*public void showSearchDialog(boolean selectThemes) throws Exception
    {
        Sirius.server.localserver.attribute.Attribute[] attrArray = null;
        DefaultMetaTreeNode[] mtnArray = null;
        String[] koordinatenKatalog = null;
        String[] koordinatenGIS = null;
        //SICADCoordinate sicadCoordinate = null;
     
        // Koordinaten Katalog
        if(ComponentRegistry.getRegistry().getActiveCatalogue() != null && ComponentRegistry.getRegistry().getActiveCatalogue().getSelectedNodeCount() == 1)
        {
            // FIXME use attribute iterator
            mtnArray = ComponentRegistry.getRegistry().getActiveCatalogue().getSelectedNodesArray();
            attrArray = mtnArray[0].getAttributes(DefaultMetaTreeNode.ANY_NODES);
     
            if(attrArray != null)
            {
                String[] tmpKoordinaten = new String[attrArray.length];
                int j = 0;
     
                for(int i = 0; i < attrArray.length; i++)
                {
                    //NavigatorLogger.printMessage("attrArray[i].getName():" + attrArray[i].getName());
                    if(attrArray[i].isCoordinate())
                    {
                        //NavigatorLogger.printMessage("attrArray[i].getValue():" + attrArray[i].getValue());
                        tmpKoordinaten[j] = attrArray[i].getValue().toString();
                        j++;
                    }
                }
     
                if(j > 0)
                {
                    koordinatenKatalog = new String[j];
                    System.arraycopy(tmpKoordinaten, 0,  koordinatenKatalog, 0, j);
                    //NavigatorLogger.printMessage("koordinatenKatalog[0].getValue():" + koordinatenKatalog[0]);
                }
            }
        }*/
    
    // Koordinaten GIS =====================================================
        /*try
        {
            if(model.isFloatingFrame != null)
            {
                sicadCoordinate = model.isFloatingFrame.getActiveCoordinate();
            }
         
            if(sicadCoordinate != null)
            {
                koordinatenGIS = sicadCoordinate.toStringArray();
         
         
                if(NavigatorLogger.SIMS_VERBOSE)NavigatorLogger.printMessage("<SIMS> SICAD Coordinate: "+koordinatenGIS[0]+", "+koordinatenGIS[1]+", "+koordinatenGIS[2]+", "+koordinatenGIS[3]);
                for(int i = 0; i < koordinatenGIS.length; i++)
                    koordinatenGIS[i] = koordinatenGIS[i].substring(0, koordinatenGIS[i].indexOf('.'));
         
                //koordinatenGIS = new String[tmpString.length];
                //koordinatenGIS[0] = tmpString[0];
                //koordinatenGIS[1] = tmpString[2];
                //koordinatenGIS[2] = tmpString[1];
                //koordinatenGIS[3] = tmpString[3];
         
                if(NavigatorLogger.SIMS_VERBOSE)NavigatorLogger.printMessage("<SIMS> Sirius Coordinate: "+koordinatenGIS[0]+", "+koordinatenGIS[1]+", "+koordinatenGIS[2]+", "+koordinatenGIS[3]);
            }
        }
        catch(Throwable t)
        {
            if(NavigatorLogger.DEV)t.printStackTrace();
        }*/
    
                /*if(selectThemes)
                        model.searchDialog.show(ComponentRegistry.getRegistry().getActiveCatalogue().getSelectedClasses(MetaCatalogueTree.ALL), koordinatenKatalog, koordinatenGIS);
                else*/
    
        /*ComponentRegistry.getRegistry().getSearchDialog().setLocationRelativeTo(ComponentRegistry.getRegistry().getMainWindow());
        ComponentRegistry.getRegistry().getSearchDialog().show();
        //ComponentRegistry.getRegistry().getSearchDialog().show(koordinatenKatalog, koordinatenGIS);
    }*/
    
    
    
    
    /*protected SICADObject[] generateSICADObjectMaps()
    {
        wrongParameters = false;
        SicadParseInfo pInfo = null;
        SICADObject tmpSICADObject = null;
        Vector sicadObjectsVector = new Vector(objectArray.length);
        SICADCoordinate sicadCoordinate = null;
     
        coordinateChooser.show();
     
        if(coordinateChooser.isCoordinateAccepted())
        {
            int[] tmpCoordinate = coordinateChooser.getCoordinate();
            sicadCoordinate = new SICADCoordinate(tmpCoordinate[0], tmpCoordinate[1], tmpCoordinate[2], tmpCoordinate[3]);
        }
     
        for(int k = 0; k < objectArray.length; k++)
        {
            pInfo = ConnectionHandler.translate(objectArray[k].getClassID(), objectArray[k].getDomain());
     
            if(pInfo != null ) //&& objectArray[k].getClassID() == classArray[j].getID() && objectArray[k].getLocalServerName().equals(classArray[j].getLocalServerName()))
            {
                String tmpString = pInfo.getID();
                //NavigatorLogger.printMessage(tmpString + ":" + tmpString.length());
                //NavigatorLogger.printMessage(tmpString.substring(1, tmpString.length()-1));
                String object_id = URLParameterizer.getObjectValue(objectArray[k], tmpString.substring(1, tmpString.length()-1));
     
                tmpString = pInfo.getCoordinate();
     
                if(tmpString != null && tmpString.length() > 0 && object_id != null)
                {
                    String coordinateString = URLParameterizer.getObjectValue(objectArray[k], tmpString.substring(1, tmpString.length()-1));
     
                    if(coordinateString != null)
                        tmpSICADObject = new SICADObject(object_id, pInfo.getTheme(), pInfo.getServer() , new SICADCoordinate(coordinateString));
                    else
                        tmpSICADObject = new SICADObject(object_id, pInfo.getTheme(), pInfo.getServer(), sicadCoordinate);
                }
                else if(object_id != null)
                {
                    tmpSICADObject = new SICADObject(object_id, pInfo.getTheme(), pInfo.getServer(), sicadCoordinate);
                }
                else
                {
                    tmpSICADObject = null;
                }
     
                if(tmpSICADObject != null)
                {
                    if(NavigatorLogger.SIMS_VERBOSE)NavigatorLogger.printMessage("<SIMS> SICADObject: " + tmpSICADObject.getObjectID() + ", " + tmpSICADObject.getThemeID() + ", " + tmpSICADObject.getIMSName() + ", " + tmpSICADObject.getCoordinate());
                    sicadObjectsVector.add(tmpSICADObject);
                }
            }
        }
        //}
     
        if(sicadObjectsVector.size() > 0)
            return (SICADObject[]) sicadObjectsVector.toArray(new SICADObject[sicadObjectsVector.size()]);
        else
            return null;
    }
     
    protected SICADObject[] generateSICADObjects()
    {
        wrongParameters = false;
        SicadParseInfo pInfo = null;
        SICADObject tmpSICADObject = null;
        Vector sicadObjectsVector = new Vector(objectArray.length);
     
     
        for(int k = 0; k < objectArray.length; k++)
        {
            pInfo = ConnectionHandler.translate(objectArray[k].getClassID(), objectArray[k].getDomain());
     
            if(pInfo != null ) //&& objectArray[k].getClassID() == classArray[j].getID() && objectArray[k].getLocalServerName().equals(classArray[j].getLocalServerName()))
            {
                String tmpString = pInfo.getID();
                //NavigatorLogger.printMessage(tmpString + ":" + tmpString.length());
                //NavigatorLogger.printMessage(tmpString.substring(1, tmpString.length()-1));
                String object_id = URLParameterizer.getObjectValue(objectArray[k], tmpString.substring(1, tmpString.length()-1));
     
                tmpString = pInfo.getCoordinate();
     
                if(tmpString != null && tmpString.length() > 0 && object_id != null)
                {
                    String coordinateString = URLParameterizer.getObjectValue(objectArray[k], tmpString.substring(1, tmpString.length()-1));
     
                    if(coordinateString != null)
                    {
                        tmpSICADObject = new SICADObject(object_id, pInfo.getTheme(), pInfo.getServer() , new SICADCoordinate(coordinateString));
                        if(NavigatorLogger.SIMS_VERBOSE)NavigatorLogger.printMessage("<SIMS> SICADObject: " + tmpString + ", " + tmpSICADObject.getObjectID() + ", " + tmpSICADObject.getThemeID() + ", " + tmpSICADObject.getIMSName() + ", " + tmpSICADObject.getCoordinate());
     
                        sicadObjectsVector.add(tmpSICADObject);
                    }
                    else
                        wrongParameters = true;
                }
            }
        }
        //}
     
        if(sicadObjectsVector.size() > 0)
            return (SICADObject[]) sicadObjectsVector.toArray(new SICADObject[sicadObjectsVector.size()]);
        else
            return null;
    }
     
     
    protected SICADLayer[] generateSICADLayers()
    {
        wrongParameters = false;
        SicadParseInfo pInfo = null;
        SICADLayer tmpSICADLayer = null;
        Vector sicadLayersVector = new Vector(objectArray.length);
        SICADCoordinate sicadCoordinate = null;
     
        coordinateChooser.show();
     
        if(coordinateChooser.isCoordinateAccepted())
        {
            int[] tmpCoordinate = coordinateChooser.getCoordinate();
            sicadCoordinate = new SICADCoordinate(tmpCoordinate[0], tmpCoordinate[1], tmpCoordinate[2], tmpCoordinate[3]);
        }
     
        for(int k = 0; k < objectArray.length; k++)
        {
            pInfo = ConnectionHandler.translate(objectArray[k].getClassID(), objectArray[k].getDomain());
     
            if(pInfo != null && pInfo.getID() != null)
            {
                String tmpString = pInfo.getID();
                String layer_id = URLParameterizer.getObjectValue(objectArray[k], tmpString.substring(1, tmpString.length()-1));
     
                //NavigatorLogger.printMessage(tmpString);
                if(layer_id != null)
                {
                    if(NavigatorLogger.SIMS_VERBOSE)NavigatorLogger.printMessage("<SIMS> SICADLayer: " + tmpString + ", " + layer_id + ", " + pInfo.getTheme() + ", " + pInfo.getServer() + ", Coordinate: " + sicadCoordinate);
                    sicadLayersVector.add(new SICADLayer(layer_id, pInfo.getTheme(), pInfo.getServer(), sicadCoordinate));
                }
            }
        }
     
        if(sicadLayersVector.size() > 0)
            return (SICADLayer[]) sicadLayersVector.toArray(new SICADLayer[sicadLayersVector.size()]);
        else
            return null;
    }*/
    
    
    
    public MethodAvailability getMethodAvailability()
    {
        long availability = NONE;
        Collection nodes = ComponentRegistry.getRegistry().getActiveCatalogue().getSelectedNodes();
        HashSet classKeys = new HashSet();
        int i = 0;
        
        try
        {
            if(nodes != null && nodes.size() > 0)
            {
                TreeNodeIterator iterator = new TreeNodeIterator(nodes, new TreeNodeRestriction());
                while(iterator.hasNext())
                {
                    DefaultMetaTreeNode node = iterator.next();
                    
                    if(node.isPureNode() && (PURE_NODE & availability) == 0)
                    {
                        availability += PURE_NODE;
                    }
                    else if(node.isClassNode() && (CLASS_NODE & availability) == 0)
                    {
                        availability += CLASS_NODE;
                        classKeys.add(((ClassTreeNode)node).getKey());
                    }
                    else if(node.isObjectNode() && (OBJECT_NODE & availability) == 0)
                    {
                        availability += OBJECT_NODE;
                        classKeys.add(((ObjectTreeNode)node).getMetaClass().getKey());
                    }
                }
                
                i++;
            }
        }
        catch(Throwable t)
        {
            logger.error("getAvailability() could not comute availabilty",  t);  // NOI18N
            availability = 0;
        }
        
        if(i > 0)
        {
            availability += MULTIPLE;
        }
        
        if(classKeys.size() > 1)
        {
            availability += CLASS_MULTIPLE;
        }
        
        return new MethodAvailability(classKeys, availability);
    }
    
    /*public static void main(String args[])
    {
        System.out.println((DOMAIN_MULTIPLE + CLASS_NODE + OBJECT_NODE) & (CLASS_NODE));
    }*/
    
    public void showSearchResults(Node[] resultNodes, boolean append)
    {
        if(resultNodes == null || resultNodes.length < 1)
        {
            JOptionPane.showMessageDialog(ComponentRegistry.getRegistry().getSearchDialog(),
                    org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.showSearchResults(Node[],boolean).JOptionPane_anon.message"),
                    org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.showSearchResults(Node[],boolean).JOptionPane_anon.title"),
                    JOptionPane.WARNING_MESSAGE);
        }
        else
        {
            ComponentRegistry.getRegistry().getSearchResultsTree().setResultNodes(resultNodes, append);
            this.showSearchResults();
        }
    }
    
    // Tree Operationen ........................................................
    
    /**
     * destinationNode = parentNode
     */
    public boolean updateNode(MetaCatalogueTree metaTree, DefaultMetaTreeNode destinationNode,  DefaultMetaTreeNode sourceNode)
    {
        try
        {
            if(logger.isInfoEnabled())
                logger.info("updateNode() updating node " + sourceNode);  // NOI18N
            // zuerst l\u00F6schen
            SessionManager.getProxy().deleteNode(sourceNode.getNode());
            
            // dann neu einf\u00FCgen
            this.addNode(metaTree, destinationNode, sourceNode);
            
            return true;
        }
        catch(Exception exp)
        {
            logger.error("deleteNode() could not update node " + sourceNode, exp);  // NOI18N
            // XXX i18n
            ExceptionManager.getManager().showExceptionDialog(ExceptionManager.WARNING,
                    org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.updateNode(MetaCatalogueTree,DefaultMetaTreeNode,DefaultMetaTreeNode).ExceptionManager_anon.title"),  // NOI18N
                    org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.updateNode(MetaCatalogueTree,DefaultMetaTreeNode,DefaultMetaTreeNode).ExceptionManager_anon.message", sourceNode),  // NOI18N
                    exp);
        }
        
        return false;
    }
    
    
    public boolean deleteNode(MetaCatalogueTree metaTree, DefaultMetaTreeNode sourceNode)
    {
        if(JOptionPane.YES_NO_OPTION == JOptionPane.showOptionDialog(ComponentRegistry.getRegistry().getMainWindow(),
                org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.deleteNode(MetaCatalogueTree,DefaultMetaTreeNode).JOptionPane_anon.message", sourceNode),  // NOI18N
                org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.deleteNode(MetaCatalogueTree,DefaultMetaTreeNode).JOptionPane_anon.title"),  // NOI18N
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]
        {org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.deleteNode(MetaCatalogueTree,DefaultMetaTreeNode).JOptionPane_anon.option.commit"),  // NOI18N
         org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.deleteNode(MetaCatalogueTree,DefaultMetaTreeNode).JOptionPane_anon.option.cancel")},  // NOI18N
         org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.deleteNode(MetaCatalogueTree,DefaultMetaTreeNode).JOptionPane_anon.option.commit")))  // NOI18N
        {
            try
            {
                if(logger.isInfoEnabled())
                    logger.info("deleteNode() deleting node " + sourceNode);  // NOI18N
                
                ComponentRegistry.getRegistry().getMainWindow().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
                SessionManager.getProxy().deleteNode(sourceNode.getNode());
                if(sourceNode.isObjectNode())
                {
                    if(logger.isDebugEnabled())logger.debug("deleting object node's meta object");  // NOI18N
                    MetaObject MetaObject = ((ObjectTreeNode)sourceNode).getMetaObject();
                    SessionManager.getProxy().deleteMetaObject(MetaObject, MetaObject.getDomain());
                }
                ComponentRegistry.getRegistry().getMainWindow().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
                
                this.deleteTreeNode(metaTree, sourceNode);
                return true;
            }
            catch(Exception exp)
            {
                logger.error("deleteNode() could not delete node " + sourceNode, exp);  // NOI18N
                ComponentRegistry.getRegistry().getMainWindow().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
                
                ExceptionManager.getManager().showExceptionDialog(ExceptionManager.WARNING,
                        org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.deleteNode(MetaCatalogueTree,DefaultMetaTreeNode).ExceptionManager_anon.title"),  // NOI18N
                        org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.deleteNode(MetaCatalogueTree,DefaultMetaTreeNode).ExceptionManager_anon.message", sourceNode),  // NOI18N
                        exp);
            }
        }
        
        return false;
    }
    
    public boolean addNode(MetaCatalogueTree metaTree, DefaultMetaTreeNode destinationNode,  DefaultMetaTreeNode sourceNode)
    {
        return this.addOrLinkNode(metaTree, destinationNode, sourceNode, false);
    }
    
    public boolean copyNode(MetaCatalogueTree metaTree, DefaultMetaTreeNode destinationNode,  DefaultMetaTreeNode sourceNode)
    {
        if(logger.isInfoEnabled())
            logger.info("copy node " + sourceNode + " -> " + destinationNode);  // NOI18N
        if(JOptionPane.YES_NO_OPTION == JOptionPane.showOptionDialog(
                ComponentRegistry.getRegistry().getMainWindow(),
                org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.copyNode(MetaCatalogueTree,DefaultMetaTreeNode,DefaultMetaTreeNode).JOptionPane_anon.message", sourceNode, destinationNode),  // NOI18N
                org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.copyNode(MetaCatalogueTree,DefaultMetaTreeNode,DefaultMetaTreeNode).JOptionPane_anon.title"),  // NOI18N
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]
        {org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.copyNode(MetaCatalogueTree,DefaultMetaTreeNode,DefaultMetaTreeNode).JOptionPane_anon.option.commit"),  // NOI18N
         org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.copyNode(MetaCatalogueTree,DefaultMetaTreeNode,DefaultMetaTreeNode).JOptionPane_anon.option.cancel")},  // NOI18N
         org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.copyNode(MetaCatalogueTree,DefaultMetaTreeNode,DefaultMetaTreeNode).JOptionPane_anon.option.commit")))  // NOI18N
        {
            try
            {
                // copy node
                DefaultMetaTreeNode sourceNodeCopy = (DefaultMetaTreeNode)CloneHelper.clone(sourceNode);
                
                if(sourceNode instanceof ObjectTreeNode)
                {
                    MetaObject oldMetaObject = ((ObjectTreeNode)sourceNodeCopy).getMetaObject();
                    //oldMetaObject.setPrimaryKey(new Integer(-1));
                    oldMetaObject.setPrimaryKeysNull();

                    if(logger.isInfoEnabled())
                        logger.info("copy node(): copy meta object: " + oldMetaObject.getName());  // NOI18N
                    MetaObject newMetaObject = SessionManager.getProxy().insertMetaObject(oldMetaObject, sourceNodeCopy.getDomain());
                    
                    // neues objekt zuweisen
                    ((ObjectTreeNode)sourceNodeCopy).setMetaObject(newMetaObject);
                }
                
                if (this.addNode(metaTree, destinationNode, sourceNodeCopy))
                {
                    this.addTreeNode(metaTree, destinationNode, sourceNodeCopy);
                    return true;
                }
            }
            catch(Exception exp)
            {
                logger.error("could not create copy of node " + sourceNode, exp);  // NOI18N
                ExceptionManager.getManager().showExceptionDialog(ExceptionManager.WARNING,
                        org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.copyNode(MetaCatalogueTree,DefaultMetaTreeNode,DefaultMetaTreeNode).ExceptionManager_anon.title"),  // NOI18N
                        org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.copyNode(MetaCatalogueTree,DefaultMetaTreeNode,DefaultMetaTreeNode).ExceptionManager_anon.message", sourceNode),  // NOI18N
                        exp);
            }
        }
        
        return false;
    }
    
    public boolean moveNode(MetaCatalogueTree metaTree, DefaultMetaTreeNode destinationNode,  DefaultMetaTreeNode sourceNode)
    {
        if(logger.isInfoEnabled())
            logger.info("move node " + sourceNode + " -> " + destinationNode);  // NOI18N
        if(JOptionPane.YES_NO_OPTION == JOptionPane.showOptionDialog(
                ComponentRegistry.getRegistry().getMainWindow(),
                org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.moveNode(MetaCatalogueTree,DefaultMetaTreeNode,DefaultMetaTreeNode).JOptionPane_anon.message", sourceNode, destinationNode),  // NOI18N
                org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.moveNode(MetaCatalogueTree,DefaultMetaTreeNode,DefaultMetaTreeNode).JOptionPane_anon.title"),  // NOI18N
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]
        {org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.moveNode(MetaCatalogueTree,DefaultMetaTreeNode,DefaultMetaTreeNode).JOptionPane_anon.option.commit"),  // NOI18N
         org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.moveNode(MetaCatalogueTree,DefaultMetaTreeNode,DefaultMetaTreeNode).JOptionPane_anon.option.cancel")},  // NOI18N
         org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.moveNode(MetaCatalogueTree,DefaultMetaTreeNode,DefaultMetaTreeNode).JOptionPane_anon.option.commit")))  // NOI18N
        {
            try
            {
                DefaultMetaTreeNode sourceParentNode = (DefaultMetaTreeNode)sourceNode.getParent();
                
                ComponentRegistry.getRegistry().getMainWindow().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
                SessionManager.getProxy().deleteLink(sourceParentNode.getNode(), sourceNode.getNode());
                this.deleteTreeNode(metaTree, sourceNode);
                
                SessionManager.getProxy().addLink(destinationNode.getNode(), sourceNode.getNode());
                this.addTreeNode(metaTree, destinationNode, sourceNode);
                //destinationNode.explore();
                ComponentRegistry.getRegistry().getMainWindow().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
            catch(Exception exp)
            {
                logger.error("addNode() could not add node");  // NOI18N
                ComponentRegistry.getRegistry().getMainWindow().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
                
                ExceptionManager.getManager().showExceptionDialog(ExceptionManager.WARNING,
                        org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.moveNode(MetaCatalogueTree,DefaultMetaTreeNode,DefaultMetaTreeNode).ExceptionManager_anon.title"),  // NOI18N
                        org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.moveNode(MetaCatalogueTree,DefaultMetaTreeNode,DefaultMetaTreeNode).ExceptionManager_anon.message", sourceNode),  // NOI18N
                        exp);
            }
        }
        
        return false;
    }
    
    // TreeNode Merhoden
    public void addTreeNode(MetaCatalogueTree metaTree, DefaultMetaTreeNode destinationNode,  DefaultMetaTreeNode sourceNode)
    {
        if(destinationNode.isLeaf())
        {
            if(logger.isDebugEnabled())logger.debug("addTreeNode() destinationNode " + destinationNode + " is leaf");  // NOI18N
            destinationNode.setLeaf(false);
        }
        
        // int childCount = destinationNode.getChildCount();
        destinationNode.add(sourceNode);
        
        int pos = destinationNode.getIndex(sourceNode);
        
        ((DefaultTreeModel)metaTree.getModel()).nodesWereInserted(destinationNode, new int[]
        {pos});
        
        // aufklappen ...
        destinationNode.setExplored(true);
        metaTree.setSelectionPath(new TreePath(sourceNode.getPath()));
    }
    
    public void deleteTreeNode(MetaCatalogueTree metaTree, DefaultMetaTreeNode sourceNode)
    {
        DefaultMetaTreeNode sourceParentNode = (DefaultMetaTreeNode)sourceNode.getParent();
        Object[] removedChildren = new Object[]
        {sourceNode};
        
        if(logger.isDebugEnabled())logger.debug("removing child node '" + sourceNode + "' from parent node '" + sourceParentNode + "'");  // NOI18N
        int[] childIndices = this.getChildIndices(sourceParentNode, sourceNode);
        sourceNode.removeFromParent();
        
        ((DefaultTreeModel)metaTree.getModel()).nodesWereRemoved(sourceParentNode, childIndices, removedChildren);
    }
    
    
    // Hilfsmethoden ...........................................................
    
    private boolean addOrLinkNode(MetaCatalogueTree metaTree, DefaultMetaTreeNode destinationNode,  DefaultMetaTreeNode sourceNode, boolean linkOnly)
    {
        try
        {
            if(linkOnly)
            {
                if(logger.isDebugEnabled())
                    logger.debug("addOrLinkNode(): linking  node: " + sourceNode);  // NOI18N
                
                ComponentRegistry.getRegistry().getMainWindow().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
                SessionManager.getProxy().addLink(destinationNode.getNode(), sourceNode.getNode());
                ComponentRegistry.getRegistry().getMainWindow().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
            else
            {
                Link link;
                if(destinationNode != null)
                {
                    link = new Link(destinationNode.getID(), destinationNode.getDomain());
                }
                else
                {
                    logger.warn("addNode(): node '" + sourceNode + "' has no parent node'");  // NOI18N
                    link = new Link(-1, sourceNode.getDomain());
                }

                if(logger.isDebugEnabled())
                    logger.debug("addOrLinkNode(): adding node: " + sourceNode);  // NOI18N
                
                ComponentRegistry.getRegistry().getMainWindow().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
                Node node = SessionManager.getProxy().addNode(sourceNode.getNode(), link);
                node.setPermissions(destinationNode.getNode().getPermissions());
                ComponentRegistry.getRegistry().getMainWindow().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
                sourceNode.setNode(node);
            }
            
            //this.addTreeNode(metaTree, destinationNode, sourceNode);
            return true;
        }
        catch(ConnectionException cexp)
        {
            logger.error("addOrLinkNode() could not add node " + sourceNode, cexp);  // NOI18N
            ComponentRegistry.getRegistry().getMainWindow().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
            
            ExceptionManager.getManager().showExceptionDialog(ExceptionManager.WARNING,
                    org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.addOrLinkNode(MetaCatalogueTree,DefaultMetaTreeNode,DefaultMetaTreeNode,boolean).ExceptionManager_anon.title"),  // NOI18N
                    org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.addOrLinkNode(MetaCatalogueTree,DefaultMetaTreeNode,DefaultMetaTreeNode,boolean).ExceptionManager_anon.message", sourceNode),  // NOI18N
                    cexp);
        }
        
        return false;
    }
    
    public boolean linkNode(MetaCatalogueTree metaTree, DefaultMetaTreeNode destinationNode,  DefaultMetaTreeNode sourceNode)
    {
        if(logger.isInfoEnabled())
            logger.info("link node " + sourceNode + " -> " + destinationNode);  // NOI18N
        if(JOptionPane.YES_NO_OPTION == JOptionPane.showOptionDialog(ComponentRegistry.getRegistry().getMainWindow(),
                org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.linkNode(MetaCatalogueTree,DefaultMetaTreeNode,DefaultMetaTreeNode).JOptionPane_anon.message", sourceNode, destinationNode),  // NOI18N
                org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.linkNode(MetaCatalogueTree,DefaultMetaTreeNode,DefaultMetaTreeNode).JOptionPane_anon.title"),  // NOI18N
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]
        {org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.linkNode(MetaCatalogueTree,DefaultMetaTreeNode,DefaultMetaTreeNode).JOptionPane_anon.option.commit"),  // NOI18N
         org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.linkNode(MetaCatalogueTree,DefaultMetaTreeNode,DefaultMetaTreeNode).JOptionPane_anon.option.cancel")},  // NOI18N
         org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.linkNode(MetaCatalogueTree,DefaultMetaTreeNode,DefaultMetaTreeNode).JOptionPane_anon.option.commit")))  // NOI18N
        {
            try
            {
                // copy node
                DefaultMetaTreeNode sourceNodeCopy = (DefaultMetaTreeNode)CloneHelper.clone(sourceNode);
                if(this.addOrLinkNode(metaTree, destinationNode, sourceNodeCopy, true))
                {
                    this.addTreeNode(metaTree, destinationNode, sourceNodeCopy);
                    return true;
                }
            }
            catch(CloneNotSupportedException cnse)
            {
                logger.error("could not create copy of linked node " + sourceNode, cnse);  // NOI18N
                ExceptionManager.getManager().showExceptionDialog(ExceptionManager.WARNING,
                        org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.linkNode(MetaCatalogueTree,DefaultMetaTreeNode,DefaultMetaTreeNode).ExceptionManager_anon.title"),  // NOI18N
                        org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.linkNode(MetaCatalogueTree,DefaultMetaTreeNode,DefaultMetaTreeNode).ExceptionManager_anon.message", sourceNode),  // NOI18N
                        cnse);
            }
        }
        
        return false;
    }
    
    private int[] getChildIndices(TreeNode sourceParentNode, TreeNode sourceNode)
    {
        for(int i = 0; i < sourceParentNode.getChildCount(); i++)
        {
            if(sourceParentNode.getChildAt(i).equals(sourceNode))
            {
                return new int[]
                {i};
            }
        }
        
        logger.warn("getChildIndices() child index of node " + sourceNode + " not found in parent node " + sourceParentNode);  // NOI18N
        return new int[]
        {-1};
    }
    
    public boolean checkPermission(Node node, Permission permission)
    {
        boolean hasPermission = false;
        
        try
        {
            String key = SessionManager.getSession().getUser().getUserGroup().getKey().toString();
            hasPermission = node.getPermissions().hasPermission(key, permission);

            if(logger.isDebugEnabled())
                logger.debug("Permissions for node"+node+"   "+node.getPermissions()+ "  with key"+key);  // NOI18N
        }
        catch(Exception exp)
        {
            logger.error("checkPermission(): could not check permission '" + permission + "' of node '" + node + "'", exp);  // NOI18N
            hasPermission = false;
        }
        
        if(!hasPermission)
        {
            JOptionPane.showMessageDialog(ComponentRegistry.getRegistry().getMainWindow(),
                    org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.checkPermission(Node,Permission).JOptionPane_anon.message"),  // NOI18N
                    org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.checkPermission(Node,Permission).JOptionPane_anon.title"),  // NOI18N
                    JOptionPane.INFORMATION_MESSAGE);
        }
        
        return hasPermission;
    }
    
    
    public boolean checkPermission(MetaObjectNode node, Permission permission)
    {
        boolean hasPermission = false;
        
        try
        {
            String key = SessionManager.getSession().getUser().getUserGroup().getKey().toString();
            MetaClass c =SessionManager.getProxy().getMetaClass(node.getClassId(), node.getDomain());
            
            // wenn MON dann editieren wenn Rechte am Knoten und and der Klasse
            hasPermission = c.getPermissions().hasPermission(key, permission);
            hasPermission &= node.getPermissions().hasPermission(key, permission);
            
            if(logger.isDebugEnabled())
                logger.debug("Check ClassPermissions for node"+node+"   "+c.getPermissions()+ "  with key"+key);  // NOI18N
        }
        catch(Exception exp)
        {
            logger.error("checkPermission(): could not check permission '" + permission + "' of node '" + node + "'", exp);  // NOI18N
            hasPermission = false;
        }
        
        if(!hasPermission)
        {
            JOptionPane.showMessageDialog(ComponentRegistry.getRegistry().getMainWindow(),
                    org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.checkPermission(MetaObjectNode,Permission).JOptionPane_anon.message"),  // NOI18N
                    org.openide.util.NbBundle.getMessage(MethodManager.class, "MethodManager.checkPermission(MetaObjectNode,Permission).JOptionPane_anon.title"),  // NOI18N
                    JOptionPane.INFORMATION_MESSAGE);
        }
        
        return hasPermission;
    }
    
    /**
     * Durchsucht ein MetaObject nach leeren Attributen
     *
     * @param das MetaObject, dass durchsucht werden soll
     * @return der Name des leeren Attributs oder null falls kein leeres Attribut gefunden wurde
     */
    public String findEmptyAttributes(MetaObject MetaObject)
    {
        return this.findEmptyAttributes(MetaObject.getAttributes().values().iterator());
    }
    
    private String findEmptyAttributes(Iterator attributeIterator)
    {
        String attributeName = null;
        
        while(attributeIterator.hasNext() && attributeName == null)
        {
            Sirius.server.localserver.attribute.Attribute attribute = (Sirius.server.localserver.attribute.Attribute)attributeIterator.next();
            if(!attribute.isPrimaryKey() && attribute.isOptional())
            {
                if(attribute.getValue() != null)
                {
                    if(attribute.getValue() instanceof MetaObject)
                    {
                        attributeName = this.findEmptyAttributes((MetaObject)attribute.getValue());
                        
                    }
                }
                else //if (attribute.referencesObject())/* if attribute.value == null*/
                {
                    try
                    {
                        if(logger.isDebugEnabled())logger.debug("looking for default value for mandantory attribute '" + attribute.getName() + "'");  // NOI18N
                        
                        //String ck =attribute.getClassKey(); //BUG muss attribute.getParentClassKey(); sein
                        
                        //Woraround Anfang
                        if (attribute.isOptional())
                                {
                                    if(logger.isDebugEnabled())
                                        logger.debug(attribute.getName() + "is optional. Set it to null");  // NOI18N
                                    attribute.setValue(null);
                                    attributeName = null;
                                }
                        //Workaround Ende
                        
                        
                        
//                        if(ck!=null)
//                        {
//                            if(logger.isDebugEnabled())logger.debug("retrive class for classKey "+ck);
//                            MetaClass metaClass = SessionManager.getProxy().getMetaClass(ck);
//                            
//                            if(metaClass!=null)
//                            {
//                                Object value = null;
//                                Object memberAttributeInfo = metaClass.getMemberAttributeInfos().get(attribute.getKey());
//                                if(memberAttributeInfo != null)
//                                {
//                                    value = ((MemberAttributeInfo)memberAttributeInfo).getDefaultValue();
//                                }
//                                
//                                if(value != null)
//                                {
//                                    attribute.setValue(value);
//                                    attributeName = null;
//                                }
//                                else if (attribute.isOptional())
//                                {
//                                    logger.debug(attribute.getName() + "is optional. Set it to null");
//                                    attribute.setValue(null);
//                                    attributeName = null;
//                                }
//                                else
//                                {
//                                    attribute.setValue(null);
//                                    attributeName = attribute.getName();
//                                    logger.debug("could net set default value of attribute '" + attribute.getName() + "' did set value to null");
//                                }
//                            }
//                        }
                        
                    }
                    catch(Exception exp)
                    {
                        logger.error("could net set default value of attribute '" + attribute.getName() + "'", exp);  // NOI18N
                        attributeName = attribute.getName();
                    }
                }
            }
        }
        
        return attributeName;
    }
}
