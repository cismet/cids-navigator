/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * SearchCategory.java
 *
 * Created on 25. September 2003, 17:11
 */
package Sirius.navigator.search.dynamic;

import java.awt.event.ItemListener;

import java.util.Collection;
import java.util.Locale;

import javax.swing.JComponent;

/**
 * Interface of a generic container that is able to display and manage SearchForms.
 *
 * @author   Pascal
 * @version  $Revision$, $Date$
 */
public interface SearchFormContainer {

    //~ Methods ----------------------------------------------------------------

    /**
     * Select a SearchForm by name.
     *
     * <p>This method should show the selected SearchForm and invoke the method <code>setSelected</code> of the
     * SearchForm.<br>
     * A value of null means, that no Form should be selected</p>
     *
     * @param   formId  tht query id of the SearchForm that should become selected or null.
     *
     * @return  true if the search form has become selected
     */
    boolean setSelectedSearchForm(String formId);

    /**
     * Returns the selected SearchForm object or null if none is selected.
     *
     * @return  the selected SearchForm object or null.
     */
    SearchForm getSelectedSearchForm();

    /**
     * Returns the index of the selected SearchForm or 0 if none is selected.
     *
     * <p>Warning: The index of SearchForms starts at 1 not at 0.</p>
     *
     * @return  the index of the selected SearchForm or -1
     */
    int getSelectedSearchFormIndex();

    /**
     * Select a SearchForm by index.
     *
     * <p>This method should show the selected SearchForm and invoke the method <code>setSelected</code> of the
     * SearchForm.<br>
     * Warning: The index of SearchForms starts at 1 not at 0. A value of 0 means, that no Form should be selected</p>
     *
     * @param   index  index of the Searchform that should become selected or -1
     *
     * @return  true if the search form has become selected
     */
    boolean setSelectedSearchFormIndex(int index);

    /**
     * Returns a collection of all SearchForms (execept the global SearchForm) in this container.
     *
     * @return  a collection of all SearchForms in this container
     */
    Collection getSearchForms();

    /**
     * Returns a map of all SearchForms (execept the global SearchForm) in this container, where key = formId und value
     * = searchForm.
     *
     * @return  a map of all SearchForms in this container
     */
    java.util.Map getSearchFormsMap();

    /**
     * Returns the SearchForm associated with the specific query id.
     *
     * @param   formId  the query id of the form
     *
     * @return  a SearchForms object or null
     */
    SearchForm getSearchForm(String formId);

    /**
     * Removes all previous SearchForms from this container and and adds the new SearchForms in the collection to this
     * container.
     *
     * @param  searchForms  a collection of SearchForms
     */
    void setSearchForms(Collection searchForms);

    /**
     * Returns the global SearchForm.
     *
     * @return  the global SearchForm
     */
    SearchForm getGlobalSearchForm();

    /**
     * Removes the previous global SearchForm and sets the new global SearchForm.
     *
     * @param  globalSearchForm  the new global SearchForm
     */
    void setGlobalSearchForm(SearchForm globalSearchForm);

    /**
     * Getter for property locale.
     *
     * <p>Returns the active Locale of this SearchFormContainer.</p>
     *
     * @return  Value of property locale.
     */
    // public Locale getLocale();

    /**
     * Setter for property locale.
     *
     * <p>Sets the active Locale of this SearchFormContainer and all SearchForms in this container. Each
     * SearchFormContainer and SearchForm is responsible for proper internationalization.</p>
     *
     * @return  DOCUMENT ME!
     */
    // public void setLocale(Locale locale);

    /**
     * Getter for property enabled.
     *
     * <p>Returns true if the container is enabled an the forms are selectable.</p>
     *
     * @return  Value of property enabled.
     */
    boolean isEnabled();

    /**
     * Setter for property enabled.
     *
     * <p>If false, the selected and the global SearchForm should be disabled and no SearchForm must be selectable.</p>
     *
     * @param  enabled  New value of property enabled.
     */
    void setEnabled(boolean enabled);

    /**
     * Getter for property component.
     *
     * <p>Returns this form's user interface component, that will be displayed in the form container.</p>
     *
     * @return  Value of property component.
     */

    /**
     * Returns true if the selected form index is > 0.
     *
     * @return  true if a SearchForm isSelected
     */
    boolean isSearchFormSelected();

    /**
     * Getter for property component.
     *
     * <p># Returns this container's user interface component that allows the selection of the different SearchForms.
     * </p>
     *
     * @return  Value of property component.
     */
    JComponent getFormContainer();

    /**
     * Returns the (internationalized) name of this container.
     *
     * @return  the name of this container
     */
    String getName();

    // .........................................................................

    /**
     * Adds an item listener to the search form container.
     *
     * @param  searchFormSelectionListener  an item listener
     */
    void addSearchFormSelectionListener(ItemListener searchFormSelectionListener);

    // .........................................................................

    /**
     * This method should always return the name of this container.
     *
     * @return  same value as <code>getName()</code>
     */
    @Override
    String toString();

    /**
     * Setter for property name.
     *
     * @param  name  New value of property name.
     */
    void setName(String name);

    /**
     * DOCUMENT ME!
     *
     * @throws  FormInitializationException  DOCUMENT ME!
     */
    void intFormContainer() throws FormInitializationException;

    /**
     * Getter for property resourceBundle.
     *
     * @return  Value of property resourceBundle.
     */
    java.util.ResourceBundle getResourceBundle();

    /**
     * Setter for property resourceBundle.
     *
     * @param  resourceBundle  New value of property resourceBundle.
     */
    void setResourceBundle(java.util.ResourceBundle resourceBundle);

    /**
     * Getter for property visible.
     *
     * @return  Value of property visible.
     */
    boolean isVisible();

    /**
     * Setter for property visible.
     *
     * @param  visible  New value of property visible.
     */
    void setVisible(boolean visible);
}
