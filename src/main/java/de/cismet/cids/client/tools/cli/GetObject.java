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
package de.cismet.cids.client.tools.cli;

import de.cismet.cids.dynamics.CidsBean;

import static de.cismet.cids.client.tools.DevelopmentTools.createCidsBeanFromPureRestfulConnection;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class GetObject {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   args  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static void main(final String[] args) throws Exception {
        String hostWithPortAndProtocol = null;
        String domain = null;
        String table = null;
        int id = -1;
        String user = null;
        String pass = null;
        String group = null;

        try {
            hostWithPortAndProtocol = args[0];
            domain = args[1];
            table = args[1];
            id = Integer.parseInt(args[2]);
            user = args[3];
            pass = args[4];
            group = args[5];
        } catch (Exception skip) {
        }
        final CidsBean result = createCidsBeanFromPureRestfulConnection(
                domain,
                group,
                user,
                pass,
                table,
                id,
                true,
                hostWithPortAndProtocol);
        System.out.println(id + "@" + table + "@" + domain + "->" + result.toJSONString(true));
    }
}
