/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui;

import javafx.application.Application;
import javafx.application.Platform;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.embed.swing.JFXPanel;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import netscape.javascript.JSException;

import javax.swing.SwingUtilities;

/**
 * ToDo adapt comment An implementation of DescriptionPane which uses JavaFX WebKit Component to render XHTML content.
 * The retrieval of XHTML documents is done by WebAccessManager. Therefore FLying Saucer is configured to use
 * WebAccessUserAgent which acts as an adapter for WebAccessManager. In order to read invalid XHTML documents or HTML
 * documents, Tagsoup is used as XMLReader.
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
public class DescriptionPaneFX extends DescriptionPane {

    //~ Static fields/initializers ---------------------------------------------

    private static JFXPanel browserFxPanel;

    //~ Instance fields --------------------------------------------------------

    WebEngine eng = null;
    WebView view;
    private Pane browserPane;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DescriptionPaneFX object.
     */
    public DescriptionPaneFX() {
        Platform.setImplicitExit(false);
        browserFxPanel = new JFXPanel();
        Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    browserPane = createBrowser();
                    final Scene scene = new Scene(browserPane);
//                browserFxPanel.set
                    browserFxPanel.setScene(scene);
                }
            });
        add(browserFxPanel, "html");
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * can only be called on FX application Thread !
     *
     * @return  DOCUMENT ME!
     */
    private Pane createBrowser() {
        final Double widthDouble = new Integer(200).doubleValue();
        final Double heightDouble = new Integer(200).doubleValue();
        view = new WebView();
//         disabling the context menue
        view.setContextMenuEnabled(false);

        // disabling scoll bars
// view.getChildrenUnmodifiable().addListener(new ListChangeListener<Node>() {
// public void onChanged(ListChangeListener.Change<? extends Node> change) {
// Set<Node> deadSeaScrolls = view.lookupAll(".scroll-bar");
// for (Node scroll : deadSeaScrolls) {
// scroll.setVisible(false);
// }
// }
// });

        // custom context menue are not possible atm see https://javafx-jira.kenai.com/browse/RT-20306
        view.setMinSize(widthDouble, heightDouble);
        view.setPrefSize(widthDouble, heightDouble);
        eng = view.getEngine();
        final Label warningLabel = new Label("Do you need to specify web proxy information?");
//        eng.load("http://test262.ecmascript.org/#");
//        eng.load("acid3.acidtests.org");
//        eng.load("http://html5test.com/");
//        eng.load("http://google.de");
//        final File index = new File(NavigatorDummy.class.getResource("../../../../public_html/index.html").toString());
//        try {
//            eng.load(index.toString());
//        } catch (Exception ex) {
//           eng.load("http://google.de");
//        }

        final ChangeListener handler = new ChangeListener<Number>() {

                @Override
                public void changed(final ObservableValue<? extends Number> observable,
                        final Number oldValue,
                        final Number newValue) {
                    if (warningLabel.isVisible()) {
                        warningLabel.setVisible(false);
                    }
                }
            };
        eng.getLoadWorker().progressProperty().addListener(handler);

        final TextField locationField = new TextField("http://www.oracle.com/us/index.html");
        locationField.setMaxHeight(Double.MAX_VALUE);
        final Button goButton = new Button("Go");
        goButton.setDefaultButton(true);
        final EventHandler<ActionEvent> goAction = new EventHandler<ActionEvent>() {

                @Override
                public void handle(final ActionEvent event) {
                    eng.load(locationField.getText().startsWith("http://") ? locationField.getText()
                                                                           : ("http://" + locationField.getText()));
                }
            };
        goButton.setOnAction(goAction);
        locationField.setOnAction(goAction);
        eng.locationProperty().addListener(new ChangeListener<String>() {

                @Override
                public void changed(final ObservableValue<? extends String> observable,
                        final String oldValue,
                        final String newValue) {
                    locationField.setText(newValue);
                }
            });

        final GridPane grid = new GridPane();
        grid.setPadding(new Insets(5));
        grid.setVgap(5);
        grid.setHgap(5);
        GridPane.setConstraints(
            locationField,
            0,
            0,
            1,
            1,
            HPos.CENTER,
            VPos.CENTER,
            Priority.ALWAYS,
            Priority.SOMETIMES);
        GridPane.setConstraints(goButton, 1, 0);
        GridPane.setConstraints(view, 0, 1, 2, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
        GridPane.setConstraints(
            warningLabel,
            0,
            2,
            2,
            1,
            HPos.CENTER,
            VPos.CENTER,
            Priority.ALWAYS,
            Priority.SOMETIMES);
        grid.getColumnConstraints()
                .addAll(
                    new ColumnConstraints(
                        widthDouble
                        - 200,
                        widthDouble
                        - 200,
                        Double.MAX_VALUE,
                        Priority.ALWAYS,
                        HPos.CENTER,
                        true),
                    new ColumnConstraints(40, 40, 40, Priority.NEVER, HPos.CENTER, true));
        grid.getChildren().addAll(view);
        return grid;
    }

    @Override
    public void setPageFromURI(final String page) {
        Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    eng.load(page);
                    try {
                        eng.executeScript("foo");
                    } catch (JSException ex) {
                        System.out.println("konnte skript nicht ausführen weil gibt nicht");
                    }
                    SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                DescriptionPaneFX.this.invalidate();
                                DescriptionPaneFX.this.repaint();
                            }
                        });
                }
            });
//        eng.load(page);
    }

    @Override
    public void setPageFromContent(final String page) {
        Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    eng.loadContent(page);
                }
            });
    }

    @Override
    public void setPageFromContent(final String page, final String baseURL) {
        Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    eng.load(page);
                }
            });
    }
}
