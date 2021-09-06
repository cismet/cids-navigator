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

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public interface AfterClosingHook {

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public enum Status {

        //~ Enum constants -----------------------------------------------------

        CANCELED, SAVED
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  event  DOCUMENT ME!
     */
    void afterClosing(final Event event);

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

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new Event object.
         *
         * @param  status  DOCUMENT ME!
         */
        public Event(final Status status) {
            this.status = status;
        }
    }
}
