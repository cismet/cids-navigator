/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.reconnector.rmi;

import java.rmi.Remote;

import javax.swing.JFrame;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public abstract class RmiReconnectorProxyFactory {

    //~ Methods ----------------------------------------------------------------

    /**
     * Erzeugt einen Dynamischen ReconnectorProxy ohne graphische Oberfläche (unattended). Das Wiederverbinden bei
     * Verbindungsabbrüchen wird automatisch angestoßen.
     *
     * @param   serviceClass  Remote-Interface des zu erzeugenen Proxy
     * @param   serviceName   Name des RMI-Dienstes
     *
     * @return  Dynamischer ReconnectorProxy des gewählten RMI-Dienstes
     */
    public static Remote createReconnectorProxy(final Class<? extends Remote> serviceClass, final String serviceName) {
        return createReconnectorProxy(serviceClass, serviceName, false, null);
    }

    /**
     * Erzeugt einen Dynamischen ReconnectorProxy mit graphischer Oberfläche in Form eines Dialogs.
     *
     * @param   serviceClass  Remote-Interface des zu erzeugenen Proxy
     * @param   serviceName   Name des RMI-Dienstes
     *
     * @return  Dynamischer ReconnectorProxy des gewählten RMI-Dienstes
     */
    public static Remote createReconnectorProxyWithDialog(final Class<? extends Remote> serviceClass,
            final String serviceName) {
        return createReconnectorProxy(serviceClass, serviceName, true, null);
    }

    /**
     * Erzeugt einen Dynamischen ReconnectorProxy mit graphischer Oberfläche in Form eines Dialogs.
     *
     * @param   serviceClass  Remote-Interface des zu erzeugenen Proxy
     * @param   serviceName   Name des RMI-Dienstes
     * @param   parentFrame   Vater-Frame des RmiReconnector-Dialoges
     *
     * @return  Dynamischer ReconnectorProxy des gewählten RMI-Dienstes
     */
    public static Remote createReconnectorProxyWithDialog(final Class<? extends Remote> serviceClass,
            final String serviceName,
            final JFrame parentFrame) {
        return createReconnectorProxy(serviceClass, serviceName, true, parentFrame);
    }

    /**
     * Erzeugt einen Dynamischen ReconnectorProxy mit oder ohne graphische Oberfläche in Form eines Dialogs.
     *
     * @param   serviceClass  Remote-Interface des zu erzeugenen Proxy
     * @param   serviceName   Name des RMI-Dienstes
     * @param   useDialog     Soll ein ReconnectorDialog verwendet werden
     * @param   parentFrame   Vater-Frame des RmiReconnector-Dialoges
     *
     * @return  Dynamischer ReconnectorProxy des gewählten RMI-Dienstes
     */
    public static Remote createReconnectorProxy(final Class<? extends Remote> serviceClass,
            final String serviceName,
            final boolean useDialog,
            final JFrame parentFrame) {
        // RmiReconnector erzeugen
        final RmiReconnector reconnector = new RmiReconnector(serviceClass, serviceName);
        // mit oder ohne Benutzung eines Dialogs
        reconnector.useDialog(useDialog, parentFrame);
        // ReconnectorProxy zurückliefern.
        return (Remote)reconnector.getCallserver();
    }
}
