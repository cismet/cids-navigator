/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.cids.tools.metaobjectrenderer;

import Sirius.server.middleware.types.MetaObject;
import de.cismet.cids.dynamics.CidsBean;
import java.util.Collection;
import javax.swing.JComponent;

/**
 *
 * @author thorsten
 */
public interface CidsBeanRendererInfo {
    public abstract String getSingleRendererClassName();
    public abstract String getAggregationRenderer();
    @Deprecated
    public abstract double getWidthRatio();
    @Deprecated
    public static final String WIDTH_RATIO="WIDTH_RATIO";//NOI18N

}
