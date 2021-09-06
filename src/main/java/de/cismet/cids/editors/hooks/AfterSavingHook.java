/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.editors.hooks;

import lombok.Getter;

import de.cismet.cids.dynamics.CidsBean;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public interface AfterSavingHook {

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public enum Status {

        //~ Enum constants -----------------------------------------------------

        CANCELED, SAVE_SUCCESS, SAVE_ERROR
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  event  DOCUMENT ME!
     */
    void afterSaving(final Event event);

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    @Getter
    public static class Event {

        //~ Instance fields ----------------------------------------------------

        final Status status;
        final CidsBean persistedBean;
        final Exception exception;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new Event object.
         *
         * @param  status         DOCUMENT ME!
         * @param  persistedBean  DOCUMENT ME!
         */
        public Event(final Status status, final CidsBean persistedBean) {
            this.status = status;
            this.persistedBean = persistedBean;
            this.exception = null;
        }
        /**
         * Creates a new Event object.
         *
         * @param  status     DOCUMENT ME!
         * @param  exception  DOCUMENT ME!
         */
        public Event(final Status status, final Exception exception) {
            this.status = status;
            this.persistedBean = null;
            this.exception = exception;
        }
    }
}
