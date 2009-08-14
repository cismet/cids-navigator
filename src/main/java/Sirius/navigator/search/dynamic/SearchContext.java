/*
 * SearchContext.java
 *
 * Created on 17. November 2003, 15:24
 */

package Sirius.navigator.search.dynamic;

import javax.swing.*;

/**
 *
 * @author  pascal
 */
public class SearchContext
{
    private final SearchDialog seachDialog;
    
    // TODO remove
    protected SearchContext(SearchDialog searchDialog)
    {
        this.seachDialog = searchDialog;
    }
    
    public SearchDialog getSearchDialog()
    {
        return this.seachDialog;
    }
    
}
