/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.client.tools;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
@Getter
public class RemoteLog4JConfig {

    //~ Instance fields --------------------------------------------------------

    @JsonProperty private final String remoteHost;
    @JsonProperty private final int remotePort;
    @JsonProperty private final String logLevel;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new RemoteLog4JConfig object.
     *
     * @param  remoteHost  DOCUMENT ME!
     * @param  remotePort  DOCUMENT ME!
     * @param  logLevel    DOCUMENT ME!
     */
    public RemoteLog4JConfig(@JsonProperty("remoteHost") final String remoteHost,
            @JsonProperty("remotePort") final int remotePort,
            @JsonProperty("logLevel") final String logLevel) {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
        this.logLevel = logLevel;
    }
}
