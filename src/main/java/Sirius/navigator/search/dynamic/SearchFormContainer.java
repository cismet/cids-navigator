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
 * Interface of a generic container that is able to display and manage 
 * SearchForms.
 *
 * @author  Pascal
 */
public interface SearchFormContainer
{
    /**
     * Select a SearchForm by name.<p>
     * This method should show the selected SearchForm and invoke the method 
     * <code>setSelected</code> of the SearchForm.<br>
     * A value of null means, that no Form should be selected
     *
     * @param formId tht query id of the SearchForm that should become selected or null.
     * @return true if the search form has become selected
     */
    public boolean setSelectedSearchForm(String formId);
    
    /**
     * Returns the selected SearchForm object or null if none is selected.
     *
     * @return the selected SearchForm object or null.
     */
    public SearchForm getSelectedSearchForm();
    
    /**
     * Returns the index of the selected SearchForm or 0 if none is selected.<p>
     * Warning: The index of SearchForms starts at 1 not at 0.
     *
     * @return the index of the selected SearchForm or -1
     */
    public int getSelectedSearchFormIndex();
    
    /**
     * Select a SearchForm by index.<p>
     * This method should show the selected SearchForm and invoke the method 
     * <code>setSelected</code> of the SearchForm.<br>
     * Warning: The index of SearchForms starts at 1 not at 0.
     * A value of 0 means, that no Form should be selected
     *
     * @param index index of the Searchform that should become selected or -1
     * @return true if the search form has become selected
     */
    public boolean setSelectedSearchFormIndex(int index);
    
    /**
     * Returns a collection of all SearchForms (execept the global SearchForm)
     * in this container.<p>
     *
     * @return a collection of all SearchForms in this container
     */
    public Collection getSearchForms();
    
    /**
     * Returns a map of all SearchForms (execept the global SearchForm)
     * in this container, where key = formId und value = searchForm.
     *
     * @return a map of all SearchForms in this container
     */
    public java.util.Map getSearchFormsMap();
    
    /**
     * Returns the SearchForm associated with the specific query id
     *
     * @param formId the query id of the form
     * @return a SearchForms object or null
     */
    public SearchForm getSearchForm(String formId);
    
    /**
     * Removes all previous SearchForms from this container and and adds the new
     * SearchForms in the collection to this container.
     *
     * @param searchForms a collection of SearchForms
     */
    public void setSearchForms(Collection searchForms);
    
    /**
     * Returns the global SearchForm.
     *
     * @return the global SearchForm
     */
    public SearchForm getGlobalSearchForm();
    
    /**
     * Removes the previous global SearchForm and sets the new global SearchForm.
     *
     * @param globalSearchForm the new global SearchForm
     */
    public void setGlobalSearchForm(SearchForm globalSearchForm);
    
    /** 
     * Getter for property locale.<p>
     * Returns the active Locale of this SearchFormContainer.
     *
     * @return Value of property locale.
     */
    //public Locale getLocale();
    
    /** 
     * Setter for property locale.<p>
     * Sets the active Locale of this SearchFormContainer and all SearchForms in
     * this container. Each SearchFormContainer and  SearchForm is responsible
     * for proper internationalization.
     *
     * @param locale New value of property locale.
     */
    //public void setLocale(Locale locale);

    /** 
     * Getter for property enabled.<p>
     * Returns true if the container is enabled an the forms are selectable.
     *
     * @return Value of property enabled.
     */
    public boolean isEnabled();
    
    /** 
     * Setter for property enabled.<p>
     * If false, the selected and the global SearchForm should be disabled and  
     * no SearchForm must be selectable.
     *
     * @param enabled New value of property enabled.
     */
    public void setEnabled(boolean enabled);
    
    /** 
     * Getter for property component.<p>
     * Returns this form's user interface component, that will be displayed in the
     * form container.
     *
     * @return Value of property component.
     */
    
    /**
     * Returns true if the selected form index is > 0.<p>
     *
     * @return true if a SearchForm isSelected
     */
    public boolean isSearchFormSelected();

    /** 
     * Getter for property component.<p>#
     * Returns this container's user interface component that allows the selection
     * of the different SearchForms.
     *
     * @return Value of property component.
     */
    public JComponent getFormContainer();
    
    /**
     * Returns the (internationalized) name of this container.
     * 
     * @return the name of this container
     */
    public String getName();
    
    // .........................................................................
    
    /**
     * Adds an item listener to the search form container.
     *
     * @param searchFormSelectionListener an item listener
     */
    public void addSearchFormSelectionListener(ItemListener searchFormSelectionListener);
    
    // .........................................................................
    
    /**
     * This method should always return the name of this container
     *
     * @return same value as <code>getName()</code>
     */
    public String toString();     
    
    /** Setter for property name.
     * @param name New value of property name.
     *
     */
    public void setName(String name);
    
    public void intFormContainer() throws FormInitializationException;
    
    /** Getter for property resourceBundle.
     * @return Value of property resourceBundle.
     *
     */
    public java.util.ResourceBundle getResourceBundle();
    
    /** Setter for property resourceBundle.
     * @param resourceBundle New value of property resourceBundle.
     *
     */
    public void setResourceBundle(java.util.ResourceBundle resourceBundle);
    
    /**
     * Getter for property visible.
     * @return Value of property visible.
     */
    public boolean isVisible();
    
    /**
     * Setter for property visible.
     * @param visible New value of property visible.
     */
    public void setVisible(boolean visible);
    
}