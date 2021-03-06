/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */
package com.grosscommerce.ICEcat.controller;

import com.grosscommerce.ICEcat.common.Constants;
import com.grosscommerce.ICEcat.utilities.ResourcesDownloader;
import com.grosscommerce.ICEcat.model.Categories;
import com.grosscommerce.ICEcat.model.Category;
import com.grosscommerce.ICEcat.model.Language;
import com.grosscommerce.ICEcat.model.Languages;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used for getting initialization data for ImportController.
 * @author Anykey Skovorodkin
 */
public class ImportContext
{   
    /**
     * Full languages list.
     */
    private Languages languages;
    private Categories categories;
    /**
     * Language used for import.
     */
    private Language importLanguage;
    /**
     * ICEcat user name;
     */
    private String userName;
    /**
     * ICEcat password.
     */
    private String password;
    private ImportType importType = ImportType.Full;
    private boolean initialized = false;

    public ImportContext()
    {
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Categories getCategories()
    {
        return categories;
    }

    public Language getImportLanguage()
    {
        return importLanguage;
    }

    public ImportType getImportType()
    {
        return importType;
    }

    public Languages getLanguages()
    {
        return languages;
    }

    public String getPassword()
    {
        return password;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setImportLanguage(Language importLanguage)
    {
        this.importLanguage = importLanguage;
    }

    public void setImportType(ImportType importType)
    {
        this.importType = importType;
    }// </editor-fold>

    public void init(String userName, String password) throws Throwable
    {
        Logger.getLogger(ImportContext.class.getName()).log(Level.INFO,
                                                            "Initialization, user: {0} pwd: {1}",
                new Object[]
                {
                    userName, password
                });

        // download languages list
        this.languages = ResourcesDownloader.downloadLanguares(userName,
                                                               password);

        this.importLanguage = this.languages.getLanguageByShortCode(Constants.DEFAULT_LANGUAGE_SHORT_CODE);
        
        // download categories list
        this.categories = ResourcesDownloader.downloadCategories(
                userName, password);

        // init was successfully finished
        this.userName = userName;
        this.password = password;

        Logger.getLogger(ImportContext.class.getName()).log(Level.INFO,
                                                            "Successfully initialized");

        this.initialized = true;
    }

    public boolean isValid()
    {
        return this.initialized;
    }

    public String getCategoryName(Integer id) throws IllegalArgumentException
    {
        Category category = this.categories.getById(id);

        if (category == null)
        {
            throw new IllegalArgumentException("Unknown category with id: " + id);
        }

        return category.getName(this.importLanguage.getId());
    }
}
