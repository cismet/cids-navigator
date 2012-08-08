/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.editors;

import java.beans.*;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class DefaultBindableReferenceComboBeanInfo extends SimpleBeanInfo {

    //~ Static fields/initializers ---------------------------------------------

    // Property identifiers//GEN-FIRST:Properties
    private static final int PROPERTY_accessibleContext = 0;
    private static final int PROPERTY_action = 1;
    private static final int PROPERTY_actionCommand = 2;
    private static final int PROPERTY_actionListeners = 3;
    private static final int PROPERTY_actionMap = 4;
    private static final int PROPERTY_alignmentX = 5;
    private static final int PROPERTY_alignmentY = 6;
    private static final int PROPERTY_ancestorListeners = 7;
    private static final int PROPERTY_autoscrolls = 8;
    private static final int PROPERTY_background = 9;
    private static final int PROPERTY_backgroundSet = 10;
    private static final int PROPERTY_baselineResizeBehavior = 11;
    private static final int PROPERTY_bindingProperty = 12;
    private static final int PROPERTY_border = 13;
    private static final int PROPERTY_bounds = 14;
    private static final int PROPERTY_colorModel = 15;
    private static final int PROPERTY_component = 16;
    private static final int PROPERTY_componentCount = 17;
    private static final int PROPERTY_componentListeners = 18;
    private static final int PROPERTY_componentOrientation = 19;
    private static final int PROPERTY_componentPopupMenu = 20;
    private static final int PROPERTY_components = 21;
    private static final int PROPERTY_containerListeners = 22;
    private static final int PROPERTY_converter = 23;
    private static final int PROPERTY_cursor = 24;
    private static final int PROPERTY_cursorSet = 25;
    private static final int PROPERTY_debugGraphicsOptions = 26;
    private static final int PROPERTY_displayable = 27;
    private static final int PROPERTY_doubleBuffered = 28;
    private static final int PROPERTY_dropTarget = 29;
    private static final int PROPERTY_editable = 30;
    private static final int PROPERTY_editor = 31;
    private static final int PROPERTY_enabled = 32;
    private static final int PROPERTY_focusable = 33;
    private static final int PROPERTY_focusCycleRoot = 34;
    private static final int PROPERTY_focusCycleRootAncestor = 35;
    private static final int PROPERTY_focusListeners = 36;
    private static final int PROPERTY_focusOwner = 37;
    private static final int PROPERTY_focusTraversable = 38;
    private static final int PROPERTY_focusTraversalKeys = 39;
    private static final int PROPERTY_focusTraversalKeysEnabled = 40;
    private static final int PROPERTY_focusTraversalPolicy = 41;
    private static final int PROPERTY_focusTraversalPolicyProvider = 42;
    private static final int PROPERTY_focusTraversalPolicySet = 43;
    private static final int PROPERTY_font = 44;
    private static final int PROPERTY_fontSet = 45;
    private static final int PROPERTY_foreground = 46;
    private static final int PROPERTY_foregroundSet = 47;
    private static final int PROPERTY_graphics = 48;
    private static final int PROPERTY_graphicsConfiguration = 49;
    private static final int PROPERTY_height = 50;
    private static final int PROPERTY_hierarchyBoundsListeners = 51;
    private static final int PROPERTY_hierarchyListeners = 52;
    private static final int PROPERTY_ignoreRepaint = 53;
    private static final int PROPERTY_inheritsPopupMenu = 54;
    private static final int PROPERTY_inputContext = 55;
    private static final int PROPERTY_inputMap = 56;
    private static final int PROPERTY_inputMethodListeners = 57;
    private static final int PROPERTY_inputMethodRequests = 58;
    private static final int PROPERTY_inputVerifier = 59;
    private static final int PROPERTY_insets = 60;
    private static final int PROPERTY_itemAt = 61;
    private static final int PROPERTY_itemCount = 62;
    private static final int PROPERTY_itemListeners = 63;
    private static final int PROPERTY_keyListeners = 64;
    private static final int PROPERTY_keySelectionManager = 65;
    private static final int PROPERTY_layout = 66;
    private static final int PROPERTY_lightweight = 67;
    private static final int PROPERTY_lightWeightPopupEnabled = 68;
    private static final int PROPERTY_locale = 69;
    private static final int PROPERTY_location = 70;
    private static final int PROPERTY_locationOnScreen = 71;
    private static final int PROPERTY_managingFocus = 72;
    private static final int PROPERTY_maximumRowCount = 73;
    private static final int PROPERTY_maximumSize = 74;
    private static final int PROPERTY_maximumSizeSet = 75;
    private static final int PROPERTY_metaClass = 76;
    private static final int PROPERTY_minimumSize = 77;
    private static final int PROPERTY_minimumSizeSet = 78;
    private static final int PROPERTY_model = 79;
    private static final int PROPERTY_mouseListeners = 80;
    private static final int PROPERTY_mouseMotionListeners = 81;
    private static final int PROPERTY_mousePosition = 82;
    private static final int PROPERTY_mouseWheelListeners = 83;
    private static final int PROPERTY_name = 84;
    private static final int PROPERTY_nextFocusableComponent = 85;
    private static final int PROPERTY_opaque = 86;
    private static final int PROPERTY_optimizedDrawingEnabled = 87;
    private static final int PROPERTY_paintingForPrint = 88;
    private static final int PROPERTY_paintingTile = 89;
    private static final int PROPERTY_parent = 90;
    private static final int PROPERTY_peer = 91;
    private static final int PROPERTY_popupMenuListeners = 92;
    private static final int PROPERTY_popupVisible = 93;
    private static final int PROPERTY_preferredSize = 94;
    private static final int PROPERTY_preferredSizeSet = 95;
    private static final int PROPERTY_propertyChangeListeners = 96;
    private static final int PROPERTY_prototypeDisplayValue = 97;
    private static final int PROPERTY_registeredKeyStrokes = 98;
    private static final int PROPERTY_renderer = 99;
    private static final int PROPERTY_requestFocusEnabled = 100;
    private static final int PROPERTY_rootPane = 101;
    private static final int PROPERTY_selectedIndex = 102;
    private static final int PROPERTY_selectedItem = 103;
    private static final int PROPERTY_selectedObjects = 104;
    private static final int PROPERTY_showing = 105;
    private static final int PROPERTY_size = 106;
    private static final int PROPERTY_toolkit = 107;
    private static final int PROPERTY_toolTipText = 108;
    private static final int PROPERTY_topLevelAncestor = 109;
    private static final int PROPERTY_transferHandler = 110;
    private static final int PROPERTY_treeLock = 111;
    private static final int PROPERTY_UI = 112;
    private static final int PROPERTY_UIClassID = 113;
    private static final int PROPERTY_valid = 114;
    private static final int PROPERTY_validateRoot = 115;
    private static final int PROPERTY_validator = 116;
    private static final int PROPERTY_verifyInputWhenFocusTarget = 117;
    private static final int PROPERTY_vetoableChangeListeners = 118;
    private static final int PROPERTY_visible = 119;
    private static final int PROPERTY_visibleRect = 120;
    private static final int PROPERTY_width = 121;
    private static final int PROPERTY_x = 122;
    private static final int PROPERTY_y = 123;

    /**
     * Property array lazy PropertyDescriptor.
     *
     * @return  DOCUMENT ME!
     */
    private static PropertyDescriptor[] getPdescriptor() {
        final PropertyDescriptor[] properties = new PropertyDescriptor[124];

        try {
            properties[PROPERTY_accessibleContext] = new PropertyDescriptor(
                    "accessibleContext",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getAccessibleContext",
                    null);                              // NOI18N
            properties[PROPERTY_action] = new PropertyDescriptor(
                    "action",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getAction",
                    "setAction");                       // NOI18N
            properties[PROPERTY_actionCommand] = new PropertyDescriptor(
                    "actionCommand",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getActionCommand",
                    "setActionCommand");                // NOI18N
            properties[PROPERTY_actionListeners] = new PropertyDescriptor(
                    "actionListeners",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getActionListeners",
                    null);                              // NOI18N
            properties[PROPERTY_actionMap] = new PropertyDescriptor(
                    "actionMap",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getActionMap",
                    "setActionMap");                    // NOI18N
            properties[PROPERTY_alignmentX] = new PropertyDescriptor(
                    "alignmentX",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getAlignmentX",
                    "setAlignmentX");                   // NOI18N
            properties[PROPERTY_alignmentY] = new PropertyDescriptor(
                    "alignmentY",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getAlignmentY",
                    "setAlignmentY");                   // NOI18N
            properties[PROPERTY_ancestorListeners] = new PropertyDescriptor(
                    "ancestorListeners",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getAncestorListeners",
                    null);                              // NOI18N
            properties[PROPERTY_autoscrolls] = new PropertyDescriptor(
                    "autoscrolls",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getAutoscrolls",
                    "setAutoscrolls");                  // NOI18N
            properties[PROPERTY_background] = new PropertyDescriptor(
                    "background",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getBackground",
                    "setBackground");                   // NOI18N
            properties[PROPERTY_backgroundSet] = new PropertyDescriptor(
                    "backgroundSet",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isBackgroundSet",
                    null);                              // NOI18N
            properties[PROPERTY_baselineResizeBehavior] = new PropertyDescriptor(
                    "baselineResizeBehavior",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getBaselineResizeBehavior",
                    null);                              // NOI18N
            properties[PROPERTY_bindingProperty] = new PropertyDescriptor(
                    "bindingProperty",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getBindingProperty",
                    null);                              // NOI18N
            properties[PROPERTY_border] = new PropertyDescriptor(
                    "border",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getBorder",
                    "setBorder");                       // NOI18N
            properties[PROPERTY_bounds] = new PropertyDescriptor(
                    "bounds",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getBounds",
                    "setBounds");                       // NOI18N
            properties[PROPERTY_colorModel] = new PropertyDescriptor(
                    "colorModel",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getColorModel",
                    null);                              // NOI18N
            properties[PROPERTY_component] = new IndexedPropertyDescriptor(
                    "component",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    null,
                    null,
                    "getComponent",
                    null);                              // NOI18N
            properties[PROPERTY_componentCount] = new PropertyDescriptor(
                    "componentCount",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getComponentCount",
                    null);                              // NOI18N
            properties[PROPERTY_componentListeners] = new PropertyDescriptor(
                    "componentListeners",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getComponentListeners",
                    null);                              // NOI18N
            properties[PROPERTY_componentOrientation] = new PropertyDescriptor(
                    "componentOrientation",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getComponentOrientation",
                    "setComponentOrientation");         // NOI18N
            properties[PROPERTY_componentPopupMenu] = new PropertyDescriptor(
                    "componentPopupMenu",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getComponentPopupMenu",
                    "setComponentPopupMenu");           // NOI18N
            properties[PROPERTY_components] = new PropertyDescriptor(
                    "components",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getComponents",
                    null);                              // NOI18N
            properties[PROPERTY_containerListeners] = new PropertyDescriptor(
                    "containerListeners",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getContainerListeners",
                    null);                              // NOI18N
            properties[PROPERTY_converter] = new PropertyDescriptor(
                    "converter",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getConverter",
                    null);                              // NOI18N
            properties[PROPERTY_cursor] = new PropertyDescriptor(
                    "cursor",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getCursor",
                    "setCursor");                       // NOI18N
            properties[PROPERTY_cursorSet] = new PropertyDescriptor(
                    "cursorSet",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isCursorSet",
                    null);                              // NOI18N
            properties[PROPERTY_debugGraphicsOptions] = new PropertyDescriptor(
                    "debugGraphicsOptions",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getDebugGraphicsOptions",
                    "setDebugGraphicsOptions");         // NOI18N
            properties[PROPERTY_displayable] = new PropertyDescriptor(
                    "displayable",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isDisplayable",
                    null);                              // NOI18N
            properties[PROPERTY_doubleBuffered] = new PropertyDescriptor(
                    "doubleBuffered",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isDoubleBuffered",
                    "setDoubleBuffered");               // NOI18N
            properties[PROPERTY_dropTarget] = new PropertyDescriptor(
                    "dropTarget",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getDropTarget",
                    "setDropTarget");                   // NOI18N
            properties[PROPERTY_editable] = new PropertyDescriptor(
                    "editable",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isEditable",
                    "setEditable");                     // NOI18N
            properties[PROPERTY_editor] = new PropertyDescriptor(
                    "editor",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getEditor",
                    "setEditor");                       // NOI18N
            properties[PROPERTY_enabled] = new PropertyDescriptor(
                    "enabled",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isEnabled",
                    "setEnabled");                      // NOI18N
            properties[PROPERTY_focusable] = new PropertyDescriptor(
                    "focusable",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isFocusable",
                    "setFocusable");                    // NOI18N
            properties[PROPERTY_focusCycleRoot] = new PropertyDescriptor(
                    "focusCycleRoot",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isFocusCycleRoot",
                    "setFocusCycleRoot");               // NOI18N
            properties[PROPERTY_focusCycleRootAncestor] = new PropertyDescriptor(
                    "focusCycleRootAncestor",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getFocusCycleRootAncestor",
                    null);                              // NOI18N
            properties[PROPERTY_focusListeners] = new PropertyDescriptor(
                    "focusListeners",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getFocusListeners",
                    null);                              // NOI18N
            properties[PROPERTY_focusOwner] = new PropertyDescriptor(
                    "focusOwner",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isFocusOwner",
                    null);                              // NOI18N
            properties[PROPERTY_focusTraversable] = new PropertyDescriptor(
                    "focusTraversable",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isFocusTraversable",
                    null);                              // NOI18N
            properties[PROPERTY_focusTraversalKeys] = new IndexedPropertyDescriptor(
                    "focusTraversalKeys",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    null,
                    null,
                    null,
                    "setFocusTraversalKeys");           // NOI18N
            properties[PROPERTY_focusTraversalKeysEnabled] = new PropertyDescriptor(
                    "focusTraversalKeysEnabled",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getFocusTraversalKeysEnabled",
                    "setFocusTraversalKeysEnabled");    // NOI18N
            properties[PROPERTY_focusTraversalPolicy] = new PropertyDescriptor(
                    "focusTraversalPolicy",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getFocusTraversalPolicy",
                    "setFocusTraversalPolicy");         // NOI18N
            properties[PROPERTY_focusTraversalPolicyProvider] = new PropertyDescriptor(
                    "focusTraversalPolicyProvider",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isFocusTraversalPolicyProvider",
                    "setFocusTraversalPolicyProvider"); // NOI18N
            properties[PROPERTY_focusTraversalPolicySet] = new PropertyDescriptor(
                    "focusTraversalPolicySet",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isFocusTraversalPolicySet",
                    null);                              // NOI18N
            properties[PROPERTY_font] = new PropertyDescriptor(
                    "font",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getFont",
                    "setFont");                         // NOI18N
            properties[PROPERTY_fontSet] = new PropertyDescriptor(
                    "fontSet",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isFontSet",
                    null);                              // NOI18N
            properties[PROPERTY_foreground] = new PropertyDescriptor(
                    "foreground",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getForeground",
                    "setForeground");                   // NOI18N
            properties[PROPERTY_foregroundSet] = new PropertyDescriptor(
                    "foregroundSet",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isForegroundSet",
                    null);                              // NOI18N
            properties[PROPERTY_graphics] = new PropertyDescriptor(
                    "graphics",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getGraphics",
                    null);                              // NOI18N
            properties[PROPERTY_graphicsConfiguration] = new PropertyDescriptor(
                    "graphicsConfiguration",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getGraphicsConfiguration",
                    null);                              // NOI18N
            properties[PROPERTY_height] = new PropertyDescriptor(
                    "height",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getHeight",
                    null);                              // NOI18N
            properties[PROPERTY_hierarchyBoundsListeners] = new PropertyDescriptor(
                    "hierarchyBoundsListeners",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getHierarchyBoundsListeners",
                    null);                              // NOI18N
            properties[PROPERTY_hierarchyListeners] = new PropertyDescriptor(
                    "hierarchyListeners",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getHierarchyListeners",
                    null);                              // NOI18N
            properties[PROPERTY_ignoreRepaint] = new PropertyDescriptor(
                    "ignoreRepaint",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getIgnoreRepaint",
                    "setIgnoreRepaint");                // NOI18N
            properties[PROPERTY_inheritsPopupMenu] = new PropertyDescriptor(
                    "inheritsPopupMenu",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getInheritsPopupMenu",
                    "setInheritsPopupMenu");            // NOI18N
            properties[PROPERTY_inputContext] = new PropertyDescriptor(
                    "inputContext",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getInputContext",
                    null);                              // NOI18N
            properties[PROPERTY_inputMap] = new PropertyDescriptor(
                    "inputMap",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getInputMap",
                    null);                              // NOI18N
            properties[PROPERTY_inputMethodListeners] = new PropertyDescriptor(
                    "inputMethodListeners",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getInputMethodListeners",
                    null);                              // NOI18N
            properties[PROPERTY_inputMethodRequests] = new PropertyDescriptor(
                    "inputMethodRequests",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getInputMethodRequests",
                    null);                              // NOI18N
            properties[PROPERTY_inputVerifier] = new PropertyDescriptor(
                    "inputVerifier",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getInputVerifier",
                    "setInputVerifier");                // NOI18N
            properties[PROPERTY_insets] = new PropertyDescriptor(
                    "insets",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getInsets",
                    null);                              // NOI18N
            properties[PROPERTY_itemAt] = new IndexedPropertyDescriptor(
                    "itemAt",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    null,
                    null,
                    "getItemAt",
                    null);                              // NOI18N
            properties[PROPERTY_itemCount] = new PropertyDescriptor(
                    "itemCount",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getItemCount",
                    null);                              // NOI18N
            properties[PROPERTY_itemListeners] = new PropertyDescriptor(
                    "itemListeners",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getItemListeners",
                    null);                              // NOI18N
            properties[PROPERTY_keyListeners] = new PropertyDescriptor(
                    "keyListeners",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getKeyListeners",
                    null);                              // NOI18N
            properties[PROPERTY_keySelectionManager] = new PropertyDescriptor(
                    "keySelectionManager",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getKeySelectionManager",
                    "setKeySelectionManager");          // NOI18N
            properties[PROPERTY_layout] = new PropertyDescriptor(
                    "layout",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getLayout",
                    "setLayout");                       // NOI18N
            properties[PROPERTY_lightweight] = new PropertyDescriptor(
                    "lightweight",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isLightweight",
                    null);                              // NOI18N
            properties[PROPERTY_lightWeightPopupEnabled] = new PropertyDescriptor(
                    "lightWeightPopupEnabled",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isLightWeightPopupEnabled",
                    "setLightWeightPopupEnabled");      // NOI18N
            properties[PROPERTY_locale] = new PropertyDescriptor(
                    "locale",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getLocale",
                    "setLocale");                       // NOI18N
            properties[PROPERTY_location] = new PropertyDescriptor(
                    "location",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getLocation",
                    "setLocation");                     // NOI18N
            properties[PROPERTY_locationOnScreen] = new PropertyDescriptor(
                    "locationOnScreen",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getLocationOnScreen",
                    null);                              // NOI18N
            properties[PROPERTY_managingFocus] = new PropertyDescriptor(
                    "managingFocus",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isManagingFocus",
                    null);                              // NOI18N
            properties[PROPERTY_maximumRowCount] = new PropertyDescriptor(
                    "maximumRowCount",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getMaximumRowCount",
                    "setMaximumRowCount");              // NOI18N
            properties[PROPERTY_maximumSize] = new PropertyDescriptor(
                    "maximumSize",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getMaximumSize",
                    "setMaximumSize");                  // NOI18N
            properties[PROPERTY_maximumSizeSet] = new PropertyDescriptor(
                    "maximumSizeSet",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isMaximumSizeSet",
                    null);                              // NOI18N
            properties[PROPERTY_metaClass] = new PropertyDescriptor(
                    "metaClass",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getMetaClass",
                    "setMetaClass");                    // NOI18N
            properties[PROPERTY_metaClass].setPreferred(true);
            properties[PROPERTY_metaClass].setBound(true);
            properties[PROPERTY_minimumSize] = new PropertyDescriptor(
                    "minimumSize",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getMinimumSize",
                    "setMinimumSize");                  // NOI18N
            properties[PROPERTY_minimumSizeSet] = new PropertyDescriptor(
                    "minimumSizeSet",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isMinimumSizeSet",
                    null);                              // NOI18N
            properties[PROPERTY_model] = new PropertyDescriptor(
                    "model",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getModel",
                    "setModel");                        // NOI18N
            properties[PROPERTY_mouseListeners] = new PropertyDescriptor(
                    "mouseListeners",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getMouseListeners",
                    null);                              // NOI18N
            properties[PROPERTY_mouseMotionListeners] = new PropertyDescriptor(
                    "mouseMotionListeners",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getMouseMotionListeners",
                    null);                              // NOI18N
            properties[PROPERTY_mousePosition] = new PropertyDescriptor(
                    "mousePosition",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getMousePosition",
                    null);                              // NOI18N
            properties[PROPERTY_mouseWheelListeners] = new PropertyDescriptor(
                    "mouseWheelListeners",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getMouseWheelListeners",
                    null);                              // NOI18N
            properties[PROPERTY_name] = new PropertyDescriptor(
                    "name",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getName",
                    "setName");                         // NOI18N
            properties[PROPERTY_nextFocusableComponent] = new PropertyDescriptor(
                    "nextFocusableComponent",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getNextFocusableComponent",
                    "setNextFocusableComponent");       // NOI18N
            properties[PROPERTY_opaque] = new PropertyDescriptor(
                    "opaque",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isOpaque",
                    "setOpaque");                       // NOI18N
            properties[PROPERTY_optimizedDrawingEnabled] = new PropertyDescriptor(
                    "optimizedDrawingEnabled",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isOptimizedDrawingEnabled",
                    null);                              // NOI18N
            properties[PROPERTY_paintingForPrint] = new PropertyDescriptor(
                    "paintingForPrint",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isPaintingForPrint",
                    null);                              // NOI18N
            properties[PROPERTY_paintingTile] = new PropertyDescriptor(
                    "paintingTile",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isPaintingTile",
                    null);                              // NOI18N
            properties[PROPERTY_parent] = new PropertyDescriptor(
                    "parent",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getParent",
                    null);                              // NOI18N
            properties[PROPERTY_peer] = new PropertyDescriptor(
                    "peer",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getPeer",
                    null);                              // NOI18N
            properties[PROPERTY_popupMenuListeners] = new PropertyDescriptor(
                    "popupMenuListeners",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getPopupMenuListeners",
                    null);                              // NOI18N
            properties[PROPERTY_popupVisible] = new PropertyDescriptor(
                    "popupVisible",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isPopupVisible",
                    "setPopupVisible");                 // NOI18N
            properties[PROPERTY_preferredSize] = new PropertyDescriptor(
                    "preferredSize",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getPreferredSize",
                    "setPreferredSize");                // NOI18N
            properties[PROPERTY_preferredSizeSet] = new PropertyDescriptor(
                    "preferredSizeSet",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isPreferredSizeSet",
                    null);                              // NOI18N
            properties[PROPERTY_propertyChangeListeners] = new PropertyDescriptor(
                    "propertyChangeListeners",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getPropertyChangeListeners",
                    null);                              // NOI18N
            properties[PROPERTY_prototypeDisplayValue] = new PropertyDescriptor(
                    "prototypeDisplayValue",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getPrototypeDisplayValue",
                    "setPrototypeDisplayValue");        // NOI18N
            properties[PROPERTY_registeredKeyStrokes] = new PropertyDescriptor(
                    "registeredKeyStrokes",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getRegisteredKeyStrokes",
                    null);                              // NOI18N
            properties[PROPERTY_renderer] = new PropertyDescriptor(
                    "renderer",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getRenderer",
                    "setRenderer");                     // NOI18N
            properties[PROPERTY_requestFocusEnabled] = new PropertyDescriptor(
                    "requestFocusEnabled",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isRequestFocusEnabled",
                    "setRequestFocusEnabled");          // NOI18N
            properties[PROPERTY_rootPane] = new PropertyDescriptor(
                    "rootPane",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getRootPane",
                    null);                              // NOI18N
            properties[PROPERTY_selectedIndex] = new PropertyDescriptor(
                    "selectedIndex",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getSelectedIndex",
                    "setSelectedIndex");                // NOI18N
            properties[PROPERTY_selectedItem] = new PropertyDescriptor(
                    "selectedItem",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getSelectedItem",
                    "setSelectedItem");                 // NOI18N
            properties[PROPERTY_selectedObjects] = new PropertyDescriptor(
                    "selectedObjects",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getSelectedObjects",
                    null);                              // NOI18N
            properties[PROPERTY_showing] = new PropertyDescriptor(
                    "showing",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isShowing",
                    null);                              // NOI18N
            properties[PROPERTY_size] = new PropertyDescriptor(
                    "size",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getSize",
                    "setSize");                         // NOI18N
            properties[PROPERTY_toolkit] = new PropertyDescriptor(
                    "toolkit",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getToolkit",
                    null);                              // NOI18N
            properties[PROPERTY_toolTipText] = new PropertyDescriptor(
                    "toolTipText",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getToolTipText",
                    "setToolTipText");                  // NOI18N
            properties[PROPERTY_topLevelAncestor] = new PropertyDescriptor(
                    "topLevelAncestor",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getTopLevelAncestor",
                    null);                              // NOI18N
            properties[PROPERTY_transferHandler] = new PropertyDescriptor(
                    "transferHandler",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getTransferHandler",
                    "setTransferHandler");              // NOI18N
            properties[PROPERTY_treeLock] = new PropertyDescriptor(
                    "treeLock",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getTreeLock",
                    null);                              // NOI18N
            properties[PROPERTY_UI] = new PropertyDescriptor(
                    "UI",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getUI",
                    "setUI");                           // NOI18N
            properties[PROPERTY_UIClassID] = new PropertyDescriptor(
                    "UIClassID",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getUIClassID",
                    null);                              // NOI18N
            properties[PROPERTY_valid] = new PropertyDescriptor(
                    "valid",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isValid",
                    null);                              // NOI18N
            properties[PROPERTY_validateRoot] = new PropertyDescriptor(
                    "validateRoot",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isValidateRoot",
                    null);                              // NOI18N
            properties[PROPERTY_validator] = new PropertyDescriptor(
                    "validator",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getValidator",
                    null);                              // NOI18N
            properties[PROPERTY_verifyInputWhenFocusTarget] = new PropertyDescriptor(
                    "verifyInputWhenFocusTarget",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getVerifyInputWhenFocusTarget",
                    "setVerifyInputWhenFocusTarget");   // NOI18N
            properties[PROPERTY_vetoableChangeListeners] = new PropertyDescriptor(
                    "vetoableChangeListeners",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getVetoableChangeListeners",
                    null);                              // NOI18N
            properties[PROPERTY_visible] = new PropertyDescriptor(
                    "visible",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "isVisible",
                    "setVisible");                      // NOI18N
            properties[PROPERTY_visibleRect] = new PropertyDescriptor(
                    "visibleRect",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getVisibleRect",
                    null);                              // NOI18N
            properties[PROPERTY_width] = new PropertyDescriptor(
                    "width",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getWidth",
                    null);                              // NOI18N
            properties[PROPERTY_x] = new PropertyDescriptor(
                    "x",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getX",
                    null);                              // NOI18N
            properties[PROPERTY_y] = new PropertyDescriptor(
                    "y",
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "getY",
                    null);                              // NOI18N
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }                                               //GEN-HEADEREND:Properties

        // Here you can add code for customizing the properties array.

        return properties;
    } //GEN-LAST:Properties

    // EventSet identifiers//GEN-FIRST:Events
    private static final int EVENT_actionListener = 0;
    private static final int EVENT_ancestorListener = 1;
    private static final int EVENT_componentListener = 2;
    private static final int EVENT_containerListener = 3;
    private static final int EVENT_focusListener = 4;
    private static final int EVENT_hierarchyBoundsListener = 5;
    private static final int EVENT_hierarchyListener = 6;
    private static final int EVENT_inputMethodListener = 7;
    private static final int EVENT_itemListener = 8;
    private static final int EVENT_keyListener = 9;
    private static final int EVENT_mouseListener = 10;
    private static final int EVENT_mouseMotionListener = 11;
    private static final int EVENT_mouseWheelListener = 12;
    private static final int EVENT_popupMenuListener = 13;
    private static final int EVENT_propertyChangeListener = 14;
    private static final int EVENT_vetoableChangeListener = 15;

    /**
     * EventSet array lazy EventSetDescriptor.
     *
     * @return  DOCUMENT ME!
     */
    private static EventSetDescriptor[] getEdescriptor() {
        final EventSetDescriptor[] eventSets = new EventSetDescriptor[16];

        try {
            eventSets[EVENT_actionListener] = new EventSetDescriptor(
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "actionListener",
                    java.awt.event.ActionListener.class,
                    new String[] { "actionPerformed" },
                    "addActionListener",
                    "removeActionListener");          // NOI18N
            eventSets[EVENT_ancestorListener] = new EventSetDescriptor(
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "ancestorListener",
                    javax.swing.event.AncestorListener.class,
                    new String[] { "ancestorAdded", "ancestorRemoved", "ancestorMoved" },
                    "addAncestorListener",
                    "removeAncestorListener");        // NOI18N
            eventSets[EVENT_componentListener] = new EventSetDescriptor(
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "componentListener",
                    java.awt.event.ComponentListener.class,
                    new String[] { "componentResized", "componentMoved", "componentShown", "componentHidden" },
                    "addComponentListener",
                    "removeComponentListener");       // NOI18N
            eventSets[EVENT_containerListener] = new EventSetDescriptor(
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "containerListener",
                    java.awt.event.ContainerListener.class,
                    new String[] { "componentAdded", "componentRemoved" },
                    "addContainerListener",
                    "removeContainerListener");       // NOI18N
            eventSets[EVENT_focusListener] = new EventSetDescriptor(
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "focusListener",
                    java.awt.event.FocusListener.class,
                    new String[] { "focusGained", "focusLost" },
                    "addFocusListener",
                    "removeFocusListener");           // NOI18N
            eventSets[EVENT_hierarchyBoundsListener] = new EventSetDescriptor(
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "hierarchyBoundsListener",
                    java.awt.event.HierarchyBoundsListener.class,
                    new String[] { "ancestorMoved", "ancestorResized" },
                    "addHierarchyBoundsListener",
                    "removeHierarchyBoundsListener"); // NOI18N
            eventSets[EVENT_hierarchyListener] = new EventSetDescriptor(
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "hierarchyListener",
                    java.awt.event.HierarchyListener.class,
                    new String[] { "hierarchyChanged" },
                    "addHierarchyListener",
                    "removeHierarchyListener");       // NOI18N
            eventSets[EVENT_inputMethodListener] = new EventSetDescriptor(
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "inputMethodListener",
                    java.awt.event.InputMethodListener.class,
                    new String[] { "inputMethodTextChanged", "caretPositionChanged" },
                    "addInputMethodListener",
                    "removeInputMethodListener");     // NOI18N
            eventSets[EVENT_itemListener] = new EventSetDescriptor(
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "itemListener",
                    java.awt.event.ItemListener.class,
                    new String[] { "itemStateChanged" },
                    "addItemListener",
                    "removeItemListener");            // NOI18N
            eventSets[EVENT_keyListener] = new EventSetDescriptor(
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "keyListener",
                    java.awt.event.KeyListener.class,
                    new String[] { "keyTyped", "keyPressed", "keyReleased" },
                    "addKeyListener",
                    "removeKeyListener");             // NOI18N
            eventSets[EVENT_mouseListener] = new EventSetDescriptor(
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "mouseListener",
                    java.awt.event.MouseListener.class,
                    new String[] { "mouseClicked", "mousePressed", "mouseReleased", "mouseEntered", "mouseExited" },
                    "addMouseListener",
                    "removeMouseListener");           // NOI18N
            eventSets[EVENT_mouseMotionListener] = new EventSetDescriptor(
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "mouseMotionListener",
                    java.awt.event.MouseMotionListener.class,
                    new String[] { "mouseDragged", "mouseMoved" },
                    "addMouseMotionListener",
                    "removeMouseMotionListener");     // NOI18N
            eventSets[EVENT_mouseWheelListener] = new EventSetDescriptor(
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "mouseWheelListener",
                    java.awt.event.MouseWheelListener.class,
                    new String[] { "mouseWheelMoved" },
                    "addMouseWheelListener",
                    "removeMouseWheelListener");      // NOI18N
            eventSets[EVENT_popupMenuListener] = new EventSetDescriptor(
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "popupMenuListener",
                    javax.swing.event.PopupMenuListener.class,
                    new String[] { "popupMenuWillBecomeVisible", "popupMenuWillBecomeInvisible", "popupMenuCanceled" },
                    "addPopupMenuListener",
                    "removePopupMenuListener");       // NOI18N
            eventSets[EVENT_propertyChangeListener] = new EventSetDescriptor(
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "propertyChangeListener",
                    java.beans.PropertyChangeListener.class,
                    new String[] { "propertyChange" },
                    "addPropertyChangeListener",
                    "removePropertyChangeListener");  // NOI18N
            eventSets[EVENT_vetoableChangeListener] = new EventSetDescriptor(
                    de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                    "vetoableChangeListener",
                    java.beans.VetoableChangeListener.class,
                    new String[] { "vetoableChange" },
                    "addVetoableChangeListener",
                    "removeVetoableChangeListener");  // NOI18N
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }                                             //GEN-HEADEREND:Events

        // Here you can add code for customizing the event sets array.

        return eventSets;
    } //GEN-LAST:Events

    // Method identifiers//GEN-FIRST:Methods
    private static final int METHOD_action0 = 0;
    private static final int METHOD_actionPerformed1 = 1;
    private static final int METHOD_add2 = 2;
    private static final int METHOD_add3 = 3;
    private static final int METHOD_add4 = 4;
    private static final int METHOD_add5 = 5;
    private static final int METHOD_add6 = 6;
    private static final int METHOD_add7 = 7;
    private static final int METHOD_addItem8 = 8;
    private static final int METHOD_addNotify9 = 9;
    private static final int METHOD_addPropertyChangeListener10 = 10;
    private static final int METHOD_applyComponentOrientation11 = 11;
    private static final int METHOD_areFocusTraversalKeysSet12 = 12;
    private static final int METHOD_bounds13 = 13;
    private static final int METHOD_checkImage14 = 14;
    private static final int METHOD_checkImage15 = 15;
    private static final int METHOD_computeVisibleRect16 = 16;
    private static final int METHOD_configureEditor17 = 17;
    private static final int METHOD_contains18 = 18;
    private static final int METHOD_contains19 = 19;
    private static final int METHOD_contentsChanged20 = 20;
    private static final int METHOD_countComponents21 = 21;
    private static final int METHOD_createImage22 = 22;
    private static final int METHOD_createImage23 = 23;
    private static final int METHOD_createToolTip24 = 24;
    private static final int METHOD_createVolatileImage25 = 25;
    private static final int METHOD_createVolatileImage26 = 26;
    private static final int METHOD_deliverEvent27 = 27;
    private static final int METHOD_disable28 = 28;
    private static final int METHOD_dispatchEvent29 = 29;
    private static final int METHOD_doLayout30 = 30;
    private static final int METHOD_enable31 = 31;
    private static final int METHOD_enable32 = 32;
    private static final int METHOD_enableInputMethods33 = 33;
    private static final int METHOD_findComponentAt34 = 34;
    private static final int METHOD_findComponentAt35 = 35;
    private static final int METHOD_firePopupMenuCanceled36 = 36;
    private static final int METHOD_firePopupMenuWillBecomeInvisible37 = 37;
    private static final int METHOD_firePopupMenuWillBecomeVisible38 = 38;
    private static final int METHOD_firePropertyChange39 = 39;
    private static final int METHOD_firePropertyChange40 = 40;
    private static final int METHOD_firePropertyChange41 = 41;
    private static final int METHOD_firePropertyChange42 = 42;
    private static final int METHOD_firePropertyChange43 = 43;
    private static final int METHOD_firePropertyChange44 = 44;
    private static final int METHOD_firePropertyChange45 = 45;
    private static final int METHOD_firePropertyChange46 = 46;
    private static final int METHOD_getActionForKeyStroke47 = 47;
    private static final int METHOD_getBaseline48 = 48;
    private static final int METHOD_getBounds49 = 49;
    private static final int METHOD_getClientProperty50 = 50;
    private static final int METHOD_getComponentAt51 = 51;
    private static final int METHOD_getComponentAt52 = 52;
    private static final int METHOD_getComponentZOrder53 = 53;
    private static final int METHOD_getConditionForKeyStroke54 = 54;
    private static final int METHOD_getDefaultLocale55 = 55;
    private static final int METHOD_getFocusTraversalKeys56 = 56;
    private static final int METHOD_getFontMetrics57 = 57;
    private static final int METHOD_getInsets58 = 58;
    private static final int METHOD_getListeners59 = 59;
    private static final int METHOD_getLocation60 = 60;
    private static final int METHOD_getMousePosition61 = 61;
    private static final int METHOD_getPopupLocation62 = 62;
    private static final int METHOD_getPropertyChangeListeners63 = 63;
    private static final int METHOD_getSize64 = 64;
    private static final int METHOD_getToolTipLocation65 = 65;
    private static final int METHOD_getToolTipText66 = 66;
    private static final int METHOD_gotFocus67 = 67;
    private static final int METHOD_grabFocus68 = 68;
    private static final int METHOD_handleEvent69 = 69;
    private static final int METHOD_hasFocus70 = 70;
    private static final int METHOD_hide71 = 71;
    private static final int METHOD_hidePopup72 = 72;
    private static final int METHOD_imageUpdate73 = 73;
    private static final int METHOD_insertItemAt74 = 74;
    private static final int METHOD_insets75 = 75;
    private static final int METHOD_inside76 = 76;
    private static final int METHOD_intervalAdded77 = 77;
    private static final int METHOD_intervalRemoved78 = 78;
    private static final int METHOD_invalidate79 = 79;
    private static final int METHOD_isAncestorOf80 = 80;
    private static final int METHOD_isFocusCycleRoot81 = 81;
    private static final int METHOD_isLightweightComponent82 = 82;
    private static final int METHOD_keyDown83 = 83;
    private static final int METHOD_keyUp84 = 84;
    private static final int METHOD_layout85 = 85;
    private static final int METHOD_list86 = 86;
    private static final int METHOD_list87 = 87;
    private static final int METHOD_list88 = 88;
    private static final int METHOD_list89 = 89;
    private static final int METHOD_list90 = 90;
    private static final int METHOD_locate91 = 91;
    private static final int METHOD_location92 = 92;
    private static final int METHOD_lostFocus93 = 93;
    private static final int METHOD_minimumSize94 = 94;
    private static final int METHOD_mouseDown95 = 95;
    private static final int METHOD_mouseDrag96 = 96;
    private static final int METHOD_mouseEnter97 = 97;
    private static final int METHOD_mouseExit98 = 98;
    private static final int METHOD_mouseMove99 = 99;
    private static final int METHOD_mouseUp100 = 100;
    private static final int METHOD_move101 = 101;
    private static final int METHOD_nextFocus102 = 102;
    private static final int METHOD_paint103 = 103;
    private static final int METHOD_paintAll104 = 104;
    private static final int METHOD_paintComponents105 = 105;
    private static final int METHOD_paintImmediately106 = 106;
    private static final int METHOD_paintImmediately107 = 107;
    private static final int METHOD_postEvent108 = 108;
    private static final int METHOD_preferredSize109 = 109;
    private static final int METHOD_prepareImage110 = 110;
    private static final int METHOD_prepareImage111 = 111;
    private static final int METHOD_print112 = 112;
    private static final int METHOD_printAll113 = 113;
    private static final int METHOD_printComponents114 = 114;
    private static final int METHOD_processKeyEvent115 = 115;
    private static final int METHOD_putClientProperty116 = 116;
    private static final int METHOD_registerKeyboardAction117 = 117;
    private static final int METHOD_registerKeyboardAction118 = 118;
    private static final int METHOD_remove119 = 119;
    private static final int METHOD_remove120 = 120;
    private static final int METHOD_remove121 = 121;
    private static final int METHOD_removeAll122 = 122;
    private static final int METHOD_removeAllItems123 = 123;
    private static final int METHOD_removeItem124 = 124;
    private static final int METHOD_removeItemAt125 = 125;
    private static final int METHOD_removeNotify126 = 126;
    private static final int METHOD_removePropertyChangeListener127 = 127;
    private static final int METHOD_repaint128 = 128;
    private static final int METHOD_repaint129 = 129;
    private static final int METHOD_repaint130 = 130;
    private static final int METHOD_repaint131 = 131;
    private static final int METHOD_repaint132 = 132;
    private static final int METHOD_requestDefaultFocus133 = 133;
    private static final int METHOD_requestFocus134 = 134;
    private static final int METHOD_requestFocus135 = 135;
    private static final int METHOD_requestFocusInWindow136 = 136;
    private static final int METHOD_resetKeyboardActions137 = 137;
    private static final int METHOD_reshape138 = 138;
    private static final int METHOD_resize139 = 139;
    private static final int METHOD_resize140 = 140;
    private static final int METHOD_revalidate141 = 141;
    private static final int METHOD_scrollRectToVisible142 = 142;
    private static final int METHOD_selectWithKeyChar143 = 143;
    private static final int METHOD_setBounds144 = 144;
    private static final int METHOD_setComponentZOrder145 = 145;
    private static final int METHOD_setDefaultLocale146 = 146;
    private static final int METHOD_show147 = 147;
    private static final int METHOD_show148 = 148;
    private static final int METHOD_showPopup149 = 149;
    private static final int METHOD_size150 = 150;
    private static final int METHOD_toString151 = 151;
    private static final int METHOD_transferFocus152 = 152;
    private static final int METHOD_transferFocusBackward153 = 153;
    private static final int METHOD_transferFocusDownCycle154 = 154;
    private static final int METHOD_transferFocusUpCycle155 = 155;
    private static final int METHOD_unregisterKeyboardAction156 = 156;
    private static final int METHOD_update157 = 157;
    private static final int METHOD_updateUI158 = 158;
    private static final int METHOD_validate159 = 159;

    /**
     * Method array lazy MethodDescriptor.
     *
     * @return  DOCUMENT ME!
     */
    private static MethodDescriptor[] getMdescriptor() {
        final MethodDescriptor[] methods = new MethodDescriptor[160];

        try {
            methods[METHOD_action0] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "action",
                        new Class[] { java.awt.Event.class, java.lang.Object.class }));                                // NOI18N
            methods[METHOD_action0].setDisplayName("");
            methods[METHOD_actionPerformed1] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod(
                        "actionPerformed",
                        new Class[] { java.awt.event.ActionEvent.class }));                                            // NOI18N
            methods[METHOD_actionPerformed1].setDisplayName("");
            methods[METHOD_add2] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "add",
                        new Class[] { java.awt.PopupMenu.class }));                                                    // NOI18N
            methods[METHOD_add2].setDisplayName("");
            methods[METHOD_add3] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "add",
                        new Class[] { java.awt.Component.class }));                                                    // NOI18N
            methods[METHOD_add3].setDisplayName("");
            methods[METHOD_add4] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "add",
                        new Class[] { java.lang.String.class, java.awt.Component.class }));                            // NOI18N
            methods[METHOD_add4].setDisplayName("");
            methods[METHOD_add5] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "add",
                        new Class[] { java.awt.Component.class, int.class }));                                         // NOI18N
            methods[METHOD_add5].setDisplayName("");
            methods[METHOD_add6] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "add",
                        new Class[] { java.awt.Component.class, java.lang.Object.class }));                            // NOI18N
            methods[METHOD_add6].setDisplayName("");
            methods[METHOD_add7] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "add",
                        new Class[] { java.awt.Component.class, java.lang.Object.class, int.class }));                 // NOI18N
            methods[METHOD_add7].setDisplayName("");
            methods[METHOD_addItem8] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod(
                        "addItem",
                        new Class[] { java.lang.Object.class }));                                                      // NOI18N
            methods[METHOD_addItem8].setDisplayName("");
            methods[METHOD_addNotify9] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "addNotify",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_addNotify9].setDisplayName("");
            methods[METHOD_addPropertyChangeListener10] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "addPropertyChangeListener",
                        new Class[] { java.lang.String.class, java.beans.PropertyChangeListener.class }));             // NOI18N
            methods[METHOD_addPropertyChangeListener10].setDisplayName("");
            methods[METHOD_applyComponentOrientation11] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "applyComponentOrientation",
                        new Class[] { java.awt.ComponentOrientation.class }));                                         // NOI18N
            methods[METHOD_applyComponentOrientation11].setDisplayName("");
            methods[METHOD_areFocusTraversalKeysSet12] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "areFocusTraversalKeysSet",
                        new Class[] { int.class }));                                                                   // NOI18N
            methods[METHOD_areFocusTraversalKeysSet12].setDisplayName("");
            methods[METHOD_bounds13] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "bounds",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_bounds13].setDisplayName("");
            methods[METHOD_checkImage14] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "checkImage",
                        new Class[] { java.awt.Image.class, java.awt.image.ImageObserver.class }));                    // NOI18N
            methods[METHOD_checkImage14].setDisplayName("");
            methods[METHOD_checkImage15] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "checkImage",
                        new Class[] {
                            java.awt.Image.class,
                            int.class,
                            int.class,
                            java.awt.image.ImageObserver.class
                        }));                                                                                           // NOI18N
            methods[METHOD_checkImage15].setDisplayName("");
            methods[METHOD_computeVisibleRect16] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "computeVisibleRect",
                        new Class[] { java.awt.Rectangle.class }));                                                    // NOI18N
            methods[METHOD_computeVisibleRect16].setDisplayName("");
            methods[METHOD_configureEditor17] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod(
                        "configureEditor",
                        new Class[] { javax.swing.ComboBoxEditor.class, java.lang.Object.class }));                    // NOI18N
            methods[METHOD_configureEditor17].setDisplayName("");
            methods[METHOD_contains18] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "contains",
                        new Class[] { java.awt.Point.class }));                                                        // NOI18N
            methods[METHOD_contains18].setDisplayName("");
            methods[METHOD_contains19] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "contains",
                        new Class[] { int.class, int.class }));                                                        // NOI18N
            methods[METHOD_contains19].setDisplayName("");
            methods[METHOD_contentsChanged20] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod(
                        "contentsChanged",
                        new Class[] { javax.swing.event.ListDataEvent.class }));                                       // NOI18N
            methods[METHOD_contentsChanged20].setDisplayName("");
            methods[METHOD_countComponents21] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "countComponents",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_countComponents21].setDisplayName("");
            methods[METHOD_createImage22] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "createImage",
                        new Class[] { java.awt.image.ImageProducer.class }));                                          // NOI18N
            methods[METHOD_createImage22].setDisplayName("");
            methods[METHOD_createImage23] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "createImage",
                        new Class[] { int.class, int.class }));                                                        // NOI18N
            methods[METHOD_createImage23].setDisplayName("");
            methods[METHOD_createToolTip24] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "createToolTip",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_createToolTip24].setDisplayName("");
            methods[METHOD_createVolatileImage25] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "createVolatileImage",
                        new Class[] { int.class, int.class }));                                                        // NOI18N
            methods[METHOD_createVolatileImage25].setDisplayName("");
            methods[METHOD_createVolatileImage26] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "createVolatileImage",
                        new Class[] { int.class, int.class, java.awt.ImageCapabilities.class }));                      // NOI18N
            methods[METHOD_createVolatileImage26].setDisplayName("");
            methods[METHOD_deliverEvent27] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "deliverEvent",
                        new Class[] { java.awt.Event.class }));                                                        // NOI18N
            methods[METHOD_deliverEvent27].setDisplayName("");
            methods[METHOD_disable28] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "disable",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_disable28].setDisplayName("");
            methods[METHOD_dispatchEvent29] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "dispatchEvent",
                        new Class[] { java.awt.AWTEvent.class }));                                                     // NOI18N
            methods[METHOD_dispatchEvent29].setDisplayName("");
            methods[METHOD_doLayout30] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "doLayout",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_doLayout30].setDisplayName("");
            methods[METHOD_enable31] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "enable",
                        new Class[] { boolean.class }));                                                               // NOI18N
            methods[METHOD_enable31].setDisplayName("");
            methods[METHOD_enable32] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "enable",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_enable32].setDisplayName("");
            methods[METHOD_enableInputMethods33] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "enableInputMethods",
                        new Class[] { boolean.class }));                                                               // NOI18N
            methods[METHOD_enableInputMethods33].setDisplayName("");
            methods[METHOD_findComponentAt34] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "findComponentAt",
                        new Class[] { int.class, int.class }));                                                        // NOI18N
            methods[METHOD_findComponentAt34].setDisplayName("");
            methods[METHOD_findComponentAt35] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "findComponentAt",
                        new Class[] { java.awt.Point.class }));                                                        // NOI18N
            methods[METHOD_findComponentAt35].setDisplayName("");
            methods[METHOD_firePopupMenuCanceled36] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod(
                        "firePopupMenuCanceled",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_firePopupMenuCanceled36].setDisplayName("");
            methods[METHOD_firePopupMenuWillBecomeInvisible37] = new MethodDescriptor(javax.swing.JComboBox.class
                            .getMethod("firePopupMenuWillBecomeInvisible", new Class[] {}));                           // NOI18N
            methods[METHOD_firePopupMenuWillBecomeInvisible37].setDisplayName("");
            methods[METHOD_firePopupMenuWillBecomeVisible38] = new MethodDescriptor(javax.swing.JComboBox.class
                            .getMethod("firePopupMenuWillBecomeVisible", new Class[] {}));                             // NOI18N
            methods[METHOD_firePopupMenuWillBecomeVisible38].setDisplayName("");
            methods[METHOD_firePropertyChange39] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "firePropertyChange",
                        new Class[] { java.lang.String.class, byte.class, byte.class }));                              // NOI18N
            methods[METHOD_firePropertyChange39].setDisplayName("");
            methods[METHOD_firePropertyChange40] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "firePropertyChange",
                        new Class[] { java.lang.String.class, short.class, short.class }));                            // NOI18N
            methods[METHOD_firePropertyChange40].setDisplayName("");
            methods[METHOD_firePropertyChange41] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "firePropertyChange",
                        new Class[] { java.lang.String.class, long.class, long.class }));                              // NOI18N
            methods[METHOD_firePropertyChange41].setDisplayName("");
            methods[METHOD_firePropertyChange42] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "firePropertyChange",
                        new Class[] { java.lang.String.class, float.class, float.class }));                            // NOI18N
            methods[METHOD_firePropertyChange42].setDisplayName("");
            methods[METHOD_firePropertyChange43] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "firePropertyChange",
                        new Class[] { java.lang.String.class, double.class, double.class }));                          // NOI18N
            methods[METHOD_firePropertyChange43].setDisplayName("");
            methods[METHOD_firePropertyChange44] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "firePropertyChange",
                        new Class[] { java.lang.String.class, boolean.class, boolean.class }));                        // NOI18N
            methods[METHOD_firePropertyChange44].setDisplayName("");
            methods[METHOD_firePropertyChange45] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "firePropertyChange",
                        new Class[] { java.lang.String.class, int.class, int.class }));                                // NOI18N
            methods[METHOD_firePropertyChange45].setDisplayName("");
            methods[METHOD_firePropertyChange46] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "firePropertyChange",
                        new Class[] { java.lang.String.class, char.class, char.class }));                              // NOI18N
            methods[METHOD_firePropertyChange46].setDisplayName("");
            methods[METHOD_getActionForKeyStroke47] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "getActionForKeyStroke",
                        new Class[] { javax.swing.KeyStroke.class }));                                                 // NOI18N
            methods[METHOD_getActionForKeyStroke47].setDisplayName("");
            methods[METHOD_getBaseline48] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "getBaseline",
                        new Class[] { int.class, int.class }));                                                        // NOI18N
            methods[METHOD_getBaseline48].setDisplayName("");
            methods[METHOD_getBounds49] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "getBounds",
                        new Class[] { java.awt.Rectangle.class }));                                                    // NOI18N
            methods[METHOD_getBounds49].setDisplayName("");
            methods[METHOD_getClientProperty50] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "getClientProperty",
                        new Class[] { java.lang.Object.class }));                                                      // NOI18N
            methods[METHOD_getClientProperty50].setDisplayName("");
            methods[METHOD_getComponentAt51] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "getComponentAt",
                        new Class[] { int.class, int.class }));                                                        // NOI18N
            methods[METHOD_getComponentAt51].setDisplayName("");
            methods[METHOD_getComponentAt52] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "getComponentAt",
                        new Class[] { java.awt.Point.class }));                                                        // NOI18N
            methods[METHOD_getComponentAt52].setDisplayName("");
            methods[METHOD_getComponentZOrder53] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "getComponentZOrder",
                        new Class[] { java.awt.Component.class }));                                                    // NOI18N
            methods[METHOD_getComponentZOrder53].setDisplayName("");
            methods[METHOD_getConditionForKeyStroke54] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "getConditionForKeyStroke",
                        new Class[] { javax.swing.KeyStroke.class }));                                                 // NOI18N
            methods[METHOD_getConditionForKeyStroke54].setDisplayName("");
            methods[METHOD_getDefaultLocale55] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "getDefaultLocale",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_getDefaultLocale55].setDisplayName("");
            methods[METHOD_getFocusTraversalKeys56] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "getFocusTraversalKeys",
                        new Class[] { int.class }));                                                                   // NOI18N
            methods[METHOD_getFocusTraversalKeys56].setDisplayName("");
            methods[METHOD_getFontMetrics57] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "getFontMetrics",
                        new Class[] { java.awt.Font.class }));                                                         // NOI18N
            methods[METHOD_getFontMetrics57].setDisplayName("");
            methods[METHOD_getInsets58] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "getInsets",
                        new Class[] { java.awt.Insets.class }));                                                       // NOI18N
            methods[METHOD_getInsets58].setDisplayName("");
            methods[METHOD_getListeners59] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "getListeners",
                        new Class[] { java.lang.Class.class }));                                                       // NOI18N
            methods[METHOD_getListeners59].setDisplayName("");
            methods[METHOD_getLocation60] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "getLocation",
                        new Class[] { java.awt.Point.class }));                                                        // NOI18N
            methods[METHOD_getLocation60].setDisplayName("");
            methods[METHOD_getMousePosition61] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "getMousePosition",
                        new Class[] { boolean.class }));                                                               // NOI18N
            methods[METHOD_getMousePosition61].setDisplayName("");
            methods[METHOD_getPopupLocation62] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "getPopupLocation",
                        new Class[] { java.awt.event.MouseEvent.class }));                                             // NOI18N
            methods[METHOD_getPopupLocation62].setDisplayName("");
            methods[METHOD_getPropertyChangeListeners63] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "getPropertyChangeListeners",
                        new Class[] { java.lang.String.class }));                                                      // NOI18N
            methods[METHOD_getPropertyChangeListeners63].setDisplayName("");
            methods[METHOD_getSize64] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "getSize",
                        new Class[] { java.awt.Dimension.class }));                                                    // NOI18N
            methods[METHOD_getSize64].setDisplayName("");
            methods[METHOD_getToolTipLocation65] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "getToolTipLocation",
                        new Class[] { java.awt.event.MouseEvent.class }));                                             // NOI18N
            methods[METHOD_getToolTipLocation65].setDisplayName("");
            methods[METHOD_getToolTipText66] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "getToolTipText",
                        new Class[] { java.awt.event.MouseEvent.class }));                                             // NOI18N
            methods[METHOD_getToolTipText66].setDisplayName("");
            methods[METHOD_gotFocus67] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "gotFocus",
                        new Class[] { java.awt.Event.class, java.lang.Object.class }));                                // NOI18N
            methods[METHOD_gotFocus67].setDisplayName("");
            methods[METHOD_grabFocus68] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "grabFocus",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_grabFocus68].setDisplayName("");
            methods[METHOD_handleEvent69] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "handleEvent",
                        new Class[] { java.awt.Event.class }));                                                        // NOI18N
            methods[METHOD_handleEvent69].setDisplayName("");
            methods[METHOD_hasFocus70] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "hasFocus",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_hasFocus70].setDisplayName("");
            methods[METHOD_hide71] = new MethodDescriptor(java.awt.Component.class.getMethod("hide", new Class[] {})); // NOI18N
            methods[METHOD_hide71].setDisplayName("");
            methods[METHOD_hidePopup72] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod(
                        "hidePopup",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_hidePopup72].setDisplayName("");
            methods[METHOD_imageUpdate73] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "imageUpdate",
                        new Class[] { java.awt.Image.class, int.class, int.class, int.class, int.class, int.class })); // NOI18N
            methods[METHOD_imageUpdate73].setDisplayName("");
            methods[METHOD_insertItemAt74] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod(
                        "insertItemAt",
                        new Class[] { java.lang.Object.class, int.class }));                                           // NOI18N
            methods[METHOD_insertItemAt74].setDisplayName("");
            methods[METHOD_insets75] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "insets",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_insets75].setDisplayName("");
            methods[METHOD_inside76] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "inside",
                        new Class[] { int.class, int.class }));                                                        // NOI18N
            methods[METHOD_inside76].setDisplayName("");
            methods[METHOD_intervalAdded77] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod(
                        "intervalAdded",
                        new Class[] { javax.swing.event.ListDataEvent.class }));                                       // NOI18N
            methods[METHOD_intervalAdded77].setDisplayName("");
            methods[METHOD_intervalRemoved78] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod(
                        "intervalRemoved",
                        new Class[] { javax.swing.event.ListDataEvent.class }));                                       // NOI18N
            methods[METHOD_intervalRemoved78].setDisplayName("");
            methods[METHOD_invalidate79] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "invalidate",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_invalidate79].setDisplayName("");
            methods[METHOD_isAncestorOf80] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "isAncestorOf",
                        new Class[] { java.awt.Component.class }));                                                    // NOI18N
            methods[METHOD_isAncestorOf80].setDisplayName("");
            methods[METHOD_isFocusCycleRoot81] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "isFocusCycleRoot",
                        new Class[] { java.awt.Container.class }));                                                    // NOI18N
            methods[METHOD_isFocusCycleRoot81].setDisplayName("");
            methods[METHOD_isLightweightComponent82] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "isLightweightComponent",
                        new Class[] { java.awt.Component.class }));                                                    // NOI18N
            methods[METHOD_isLightweightComponent82].setDisplayName("");
            methods[METHOD_keyDown83] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "keyDown",
                        new Class[] { java.awt.Event.class, int.class }));                                             // NOI18N
            methods[METHOD_keyDown83].setDisplayName("");
            methods[METHOD_keyUp84] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "keyUp",
                        new Class[] { java.awt.Event.class, int.class }));                                             // NOI18N
            methods[METHOD_keyUp84].setDisplayName("");
            methods[METHOD_layout85] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "layout",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_layout85].setDisplayName("");
            methods[METHOD_list86] = new MethodDescriptor(java.awt.Component.class.getMethod("list", new Class[] {})); // NOI18N
            methods[METHOD_list86].setDisplayName("");
            methods[METHOD_list87] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "list",
                        new Class[] { java.io.PrintStream.class }));                                                   // NOI18N
            methods[METHOD_list87].setDisplayName("");
            methods[METHOD_list88] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "list",
                        new Class[] { java.io.PrintWriter.class }));                                                   // NOI18N
            methods[METHOD_list88].setDisplayName("");
            methods[METHOD_list89] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "list",
                        new Class[] { java.io.PrintStream.class, int.class }));                                        // NOI18N
            methods[METHOD_list89].setDisplayName("");
            methods[METHOD_list90] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "list",
                        new Class[] { java.io.PrintWriter.class, int.class }));                                        // NOI18N
            methods[METHOD_list90].setDisplayName("");
            methods[METHOD_locate91] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "locate",
                        new Class[] { int.class, int.class }));                                                        // NOI18N
            methods[METHOD_locate91].setDisplayName("");
            methods[METHOD_location92] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "location",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_location92].setDisplayName("");
            methods[METHOD_lostFocus93] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "lostFocus",
                        new Class[] { java.awt.Event.class, java.lang.Object.class }));                                // NOI18N
            methods[METHOD_lostFocus93].setDisplayName("");
            methods[METHOD_minimumSize94] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "minimumSize",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_minimumSize94].setDisplayName("");
            methods[METHOD_mouseDown95] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "mouseDown",
                        new Class[] { java.awt.Event.class, int.class, int.class }));                                  // NOI18N
            methods[METHOD_mouseDown95].setDisplayName("");
            methods[METHOD_mouseDrag96] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "mouseDrag",
                        new Class[] { java.awt.Event.class, int.class, int.class }));                                  // NOI18N
            methods[METHOD_mouseDrag96].setDisplayName("");
            methods[METHOD_mouseEnter97] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "mouseEnter",
                        new Class[] { java.awt.Event.class, int.class, int.class }));                                  // NOI18N
            methods[METHOD_mouseEnter97].setDisplayName("");
            methods[METHOD_mouseExit98] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "mouseExit",
                        new Class[] { java.awt.Event.class, int.class, int.class }));                                  // NOI18N
            methods[METHOD_mouseExit98].setDisplayName("");
            methods[METHOD_mouseMove99] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "mouseMove",
                        new Class[] { java.awt.Event.class, int.class, int.class }));                                  // NOI18N
            methods[METHOD_mouseMove99].setDisplayName("");
            methods[METHOD_mouseUp100] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "mouseUp",
                        new Class[] { java.awt.Event.class, int.class, int.class }));                                  // NOI18N
            methods[METHOD_mouseUp100].setDisplayName("");
            methods[METHOD_move101] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "move",
                        new Class[] { int.class, int.class }));                                                        // NOI18N
            methods[METHOD_move101].setDisplayName("");
            methods[METHOD_nextFocus102] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "nextFocus",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_nextFocus102].setDisplayName("");
            methods[METHOD_paint103] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "paint",
                        new Class[] { java.awt.Graphics.class }));                                                     // NOI18N
            methods[METHOD_paint103].setDisplayName("");
            methods[METHOD_paintAll104] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "paintAll",
                        new Class[] { java.awt.Graphics.class }));                                                     // NOI18N
            methods[METHOD_paintAll104].setDisplayName("");
            methods[METHOD_paintComponents105] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "paintComponents",
                        new Class[] { java.awt.Graphics.class }));                                                     // NOI18N
            methods[METHOD_paintComponents105].setDisplayName("");
            methods[METHOD_paintImmediately106] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "paintImmediately",
                        new Class[] { int.class, int.class, int.class, int.class }));                                  // NOI18N
            methods[METHOD_paintImmediately106].setDisplayName("");
            methods[METHOD_paintImmediately107] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "paintImmediately",
                        new Class[] { java.awt.Rectangle.class }));                                                    // NOI18N
            methods[METHOD_paintImmediately107].setDisplayName("");
            methods[METHOD_postEvent108] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "postEvent",
                        new Class[] { java.awt.Event.class }));                                                        // NOI18N
            methods[METHOD_postEvent108].setDisplayName("");
            methods[METHOD_preferredSize109] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "preferredSize",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_preferredSize109].setDisplayName("");
            methods[METHOD_prepareImage110] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "prepareImage",
                        new Class[] { java.awt.Image.class, java.awt.image.ImageObserver.class }));                    // NOI18N
            methods[METHOD_prepareImage110].setDisplayName("");
            methods[METHOD_prepareImage111] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "prepareImage",
                        new Class[] {
                            java.awt.Image.class,
                            int.class,
                            int.class,
                            java.awt.image.ImageObserver.class
                        }));                                                                                           // NOI18N
            methods[METHOD_prepareImage111].setDisplayName("");
            methods[METHOD_print112] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "print",
                        new Class[] { java.awt.Graphics.class }));                                                     // NOI18N
            methods[METHOD_print112].setDisplayName("");
            methods[METHOD_printAll113] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "printAll",
                        new Class[] { java.awt.Graphics.class }));                                                     // NOI18N
            methods[METHOD_printAll113].setDisplayName("");
            methods[METHOD_printComponents114] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "printComponents",
                        new Class[] { java.awt.Graphics.class }));                                                     // NOI18N
            methods[METHOD_printComponents114].setDisplayName("");
            methods[METHOD_processKeyEvent115] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod(
                        "processKeyEvent",
                        new Class[] { java.awt.event.KeyEvent.class }));                                               // NOI18N
            methods[METHOD_processKeyEvent115].setDisplayName("");
            methods[METHOD_putClientProperty116] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "putClientProperty",
                        new Class[] { java.lang.Object.class, java.lang.Object.class }));                              // NOI18N
            methods[METHOD_putClientProperty116].setDisplayName("");
            methods[METHOD_registerKeyboardAction117] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "registerKeyboardAction",
                        new Class[] {
                            java.awt.event.ActionListener.class,
                            java.lang.String.class,
                            javax.swing.KeyStroke.class,
                            int.class
                        }));                                                                                           // NOI18N
            methods[METHOD_registerKeyboardAction117].setDisplayName("");
            methods[METHOD_registerKeyboardAction118] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "registerKeyboardAction",
                        new Class[] { java.awt.event.ActionListener.class, javax.swing.KeyStroke.class, int.class })); // NOI18N
            methods[METHOD_registerKeyboardAction118].setDisplayName("");
            methods[METHOD_remove119] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "remove",
                        new Class[] { java.awt.MenuComponent.class }));                                                // NOI18N
            methods[METHOD_remove119].setDisplayName("");
            methods[METHOD_remove120] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "remove",
                        new Class[] { int.class }));                                                                   // NOI18N
            methods[METHOD_remove120].setDisplayName("");
            methods[METHOD_remove121] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "remove",
                        new Class[] { java.awt.Component.class }));                                                    // NOI18N
            methods[METHOD_remove121].setDisplayName("");
            methods[METHOD_removeAll122] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "removeAll",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_removeAll122].setDisplayName("");
            methods[METHOD_removeAllItems123] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod(
                        "removeAllItems",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_removeAllItems123].setDisplayName("");
            methods[METHOD_removeItem124] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod(
                        "removeItem",
                        new Class[] { java.lang.Object.class }));                                                      // NOI18N
            methods[METHOD_removeItem124].setDisplayName("");
            methods[METHOD_removeItemAt125] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod(
                        "removeItemAt",
                        new Class[] { int.class }));                                                                   // NOI18N
            methods[METHOD_removeItemAt125].setDisplayName("");
            methods[METHOD_removeNotify126] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "removeNotify",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_removeNotify126].setDisplayName("");
            methods[METHOD_removePropertyChangeListener127] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "removePropertyChangeListener",
                        new Class[] { java.lang.String.class, java.beans.PropertyChangeListener.class }));             // NOI18N
            methods[METHOD_removePropertyChangeListener127].setDisplayName("");
            methods[METHOD_repaint128] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "repaint",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_repaint128].setDisplayName("");
            methods[METHOD_repaint129] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "repaint",
                        new Class[] { long.class }));                                                                  // NOI18N
            methods[METHOD_repaint129].setDisplayName("");
            methods[METHOD_repaint130] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "repaint",
                        new Class[] { int.class, int.class, int.class, int.class }));                                  // NOI18N
            methods[METHOD_repaint130].setDisplayName("");
            methods[METHOD_repaint131] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "repaint",
                        new Class[] { long.class, int.class, int.class, int.class, int.class }));                      // NOI18N
            methods[METHOD_repaint131].setDisplayName("");
            methods[METHOD_repaint132] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "repaint",
                        new Class[] { java.awt.Rectangle.class }));                                                    // NOI18N
            methods[METHOD_repaint132].setDisplayName("");
            methods[METHOD_requestDefaultFocus133] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "requestDefaultFocus",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_requestDefaultFocus133].setDisplayName("");
            methods[METHOD_requestFocus134] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "requestFocus",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_requestFocus134].setDisplayName("");
            methods[METHOD_requestFocus135] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "requestFocus",
                        new Class[] { boolean.class }));                                                               // NOI18N
            methods[METHOD_requestFocus135].setDisplayName("");
            methods[METHOD_requestFocusInWindow136] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "requestFocusInWindow",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_requestFocusInWindow136].setDisplayName("");
            methods[METHOD_resetKeyboardActions137] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "resetKeyboardActions",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_resetKeyboardActions137].setDisplayName("");
            methods[METHOD_reshape138] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "reshape",
                        new Class[] { int.class, int.class, int.class, int.class }));                                  // NOI18N
            methods[METHOD_reshape138].setDisplayName("");
            methods[METHOD_resize139] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "resize",
                        new Class[] { int.class, int.class }));                                                        // NOI18N
            methods[METHOD_resize139].setDisplayName("");
            methods[METHOD_resize140] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "resize",
                        new Class[] { java.awt.Dimension.class }));                                                    // NOI18N
            methods[METHOD_resize140].setDisplayName("");
            methods[METHOD_revalidate141] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "revalidate",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_revalidate141].setDisplayName("");
            methods[METHOD_scrollRectToVisible142] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "scrollRectToVisible",
                        new Class[] { java.awt.Rectangle.class }));                                                    // NOI18N
            methods[METHOD_scrollRectToVisible142].setDisplayName("");
            methods[METHOD_selectWithKeyChar143] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod(
                        "selectWithKeyChar",
                        new Class[] { char.class }));                                                                  // NOI18N
            methods[METHOD_selectWithKeyChar143].setDisplayName("");
            methods[METHOD_setBounds144] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "setBounds",
                        new Class[] { int.class, int.class, int.class, int.class }));                                  // NOI18N
            methods[METHOD_setBounds144].setDisplayName("");
            methods[METHOD_setComponentZOrder145] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "setComponentZOrder",
                        new Class[] { java.awt.Component.class, int.class }));                                         // NOI18N
            methods[METHOD_setComponentZOrder145].setDisplayName("");
            methods[METHOD_setDefaultLocale146] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "setDefaultLocale",
                        new Class[] { java.util.Locale.class }));                                                      // NOI18N
            methods[METHOD_setDefaultLocale146].setDisplayName("");
            methods[METHOD_show147] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "show",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_show147].setDisplayName("");
            methods[METHOD_show148] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "show",
                        new Class[] { boolean.class }));                                                               // NOI18N
            methods[METHOD_show148].setDisplayName("");
            methods[METHOD_showPopup149] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod(
                        "showPopup",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_showPopup149].setDisplayName("");
            methods[METHOD_size150] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "size",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_size150].setDisplayName("");
            methods[METHOD_toString151] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "toString",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_toString151].setDisplayName("");
            methods[METHOD_transferFocus152] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "transferFocus",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_transferFocus152].setDisplayName("");
            methods[METHOD_transferFocusBackward153] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "transferFocusBackward",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_transferFocusBackward153].setDisplayName("");
            methods[METHOD_transferFocusDownCycle154] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "transferFocusDownCycle",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_transferFocusDownCycle154].setDisplayName("");
            methods[METHOD_transferFocusUpCycle155] = new MethodDescriptor(java.awt.Component.class.getMethod(
                        "transferFocusUpCycle",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_transferFocusUpCycle155].setDisplayName("");
            methods[METHOD_unregisterKeyboardAction156] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "unregisterKeyboardAction",
                        new Class[] { javax.swing.KeyStroke.class }));                                                 // NOI18N
            methods[METHOD_unregisterKeyboardAction156].setDisplayName("");
            methods[METHOD_update157] = new MethodDescriptor(javax.swing.JComponent.class.getMethod(
                        "update",
                        new Class[] { java.awt.Graphics.class }));                                                     // NOI18N
            methods[METHOD_update157].setDisplayName("");
            methods[METHOD_updateUI158] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod(
                        "updateUI",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_updateUI158].setDisplayName("");
            methods[METHOD_validate159] = new MethodDescriptor(java.awt.Container.class.getMethod(
                        "validate",
                        new Class[] {}));                                                                              // NOI18N
            methods[METHOD_validate159].setDisplayName("");
        } catch (Exception e) {
        }                                                                                                              //GEN-HEADEREND:Methods

        // Here you can add code for customizing the methods array.

        return methods;
    } //GEN-LAST:Methods

    private static java.awt.Image iconColor16 = null;                           //GEN-BEGIN:IconsDef
    private static java.awt.Image iconColor32 = null;
    private static java.awt.Image iconMono16 = null;
    private static java.awt.Image iconMono32 = null;                            //GEN-END:IconsDef
    private static String iconNameC16 = "/de/cismet/cids/editors/cids16.png";   //GEN-BEGIN:Icons
    private static String iconNameC32 = "/de/cismet/cids/editors/cids32.png";
    private static String iconNameM16 = "/de/cismet/cids/editors/cids16bw.png";
    private static String iconNameM32 = "/de/cismet/cids/editors/cids32bw.png"; //GEN-END:Icons

    private static final int defaultPropertyIndex = -1; //GEN-BEGIN:Idx
    private static final int defaultEventIndex = -1;    //GEN-END:Idx

    //~ Methods ----------------------------------------------------------------

    /**
     * Bean descriptor//GEN-FIRST:BeanDescriptor lazy BeanDescriptor.
     *
     * @return  DOCUMENT ME!
     */
    private static BeanDescriptor getBdescriptor() {
        final BeanDescriptor beanDescriptor = new BeanDescriptor(
                de.cismet.cids.editors.DefaultBindableReferenceCombo.class,
                null);                                       // NOI18N
        beanDescriptor.setDisplayName("CidsReferenceCombo"); //GEN-HEADEREND:BeanDescriptor

        // Here you can add code for customizing the BeanDescriptor.

        return beanDescriptor;
    } //GEN-LAST:BeanDescriptor

//GEN-FIRST:Superclass

    // Here you can add code for customizing the Superclass BeanInfo.

//GEN-LAST:Superclass

    /**
     * Gets the bean's <code>BeanDescriptor</code>s.
     *
     * @return  BeanDescriptor describing the editable properties of this bean. May return null if the information
     *          should be obtained by automatic analysis.
     */
    @Override
    public BeanDescriptor getBeanDescriptor() {
        return getBdescriptor();
    }

    /**
     * Gets the bean's <code>PropertyDescriptor</code>s.
     *
     * @return  An array of PropertyDescriptors describing the editable properties supported by this bean. May return
     *          null if the information should be obtained by automatic analysis.
     *
     *          <p>If a property is indexed, then its entry in the result array will belong to the
     *          IndexedPropertyDescriptor subclass of PropertyDescriptor. A client of getPropertyDescriptors can use
     *          "instanceof" to check if a given PropertyDescriptor is an IndexedPropertyDescriptor.</p>
     */
    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return getPdescriptor();
    }

    /**
     * Gets the bean's <code>EventSetDescriptor</code>s.
     *
     * @return  An array of EventSetDescriptors describing the kinds of events fired by this bean. May return null if
     *          the information should be obtained by automatic analysis.
     */
    @Override
    public EventSetDescriptor[] getEventSetDescriptors() {
        return getEdescriptor();
    }

    /**
     * Gets the bean's <code>MethodDescriptor</code>s.
     *
     * @return  An array of MethodDescriptors describing the methods implemented by this bean. May return null if the
     *          information should be obtained by automatic analysis.
     */
    @Override
    public MethodDescriptor[] getMethodDescriptors() {
        return getMdescriptor();
    }

    /**
     * A bean may have a "default" property that is the property that will mostly commonly be initially chosen for
     * update by human's who are customizing the bean.
     *
     * @return  Index of default property in the PropertyDescriptor array returned by getPropertyDescriptors.
     *
     *          <P>Returns -1 if there is no default property.</P>
     */
    @Override
    public int getDefaultPropertyIndex() {
        return defaultPropertyIndex;
    }

    /**
     * A bean may have a "default" event that is the event that will mostly commonly be used by human's when using the
     * bean.
     *
     * @return  Index of default event in the EventSetDescriptor array returned by getEventSetDescriptors.
     *
     *          <P>Returns -1 if there is no default event.</P>
     */
    @Override
    public int getDefaultEventIndex() {
        return defaultEventIndex;
    }

    /**
     * This method returns an image object that can be used to represent the bean in toolboxes, toolbars, etc. Icon
     * images will typically be GIFs, but may in future include other formats.
     *
     * <p>Beans aren't required to provide icons and may return null from this method.</p>
     *
     * <p>There are four possible flavors of icons (16x16 color, 32x32 color, 16x16 mono, 32x32 mono). If a bean choses
     * to only support a single icon we recommend supporting 16x16 color.</p>
     *
     * <p>We recommend that icons have a "transparent" background so they can be rendered onto an existing background.
     * </p>
     *
     * @param   iconKind  The kind of icon requested. This should be one of the constant values ICON_COLOR_16x16,
     *                    ICON_COLOR_32x32, ICON_MONO_16x16, or ICON_MONO_32x32.
     *
     * @return  An image object representing the requested icon. May return null if no suitable icon is available.
     */
    @Override
    public java.awt.Image getIcon(final int iconKind) {
        switch (iconKind) {
            case ICON_COLOR_16x16: {
                if (iconNameC16 == null) {
                    return null;
                } else {
                    if (iconColor16 == null) {
                        iconColor16 = loadImage(iconNameC16);
                    }
                    return iconColor16;
                }
            }
            case ICON_COLOR_32x32: {
                if (iconNameC32 == null) {
                    return null;
                } else {
                    if (iconColor32 == null) {
                        iconColor32 = loadImage(iconNameC32);
                    }
                    return iconColor32;
                }
            }
            case ICON_MONO_16x16: {
                if (iconNameM16 == null) {
                    return null;
                } else {
                    if (iconMono16 == null) {
                        iconMono16 = loadImage(iconNameM16);
                    }
                    return iconMono16;
                }
            }
            case ICON_MONO_32x32: {
                if (iconNameM32 == null) {
                    return null;
                } else {
                    if (iconMono32 == null) {
                        iconMono32 = loadImage(iconNameM32);
                    }
                    return iconMono32;
                }
            }
            default: {
                return null;
            }
        }
    }
}
