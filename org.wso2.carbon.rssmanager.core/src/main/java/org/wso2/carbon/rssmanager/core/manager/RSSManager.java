/*
 *  Copyright (c) 2005-2013, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.rssmanager.core.manager;

import org.wso2.carbon.rssmanager.core.dto.common.DatabasePrivilegeSet;
import org.wso2.carbon.rssmanager.core.dto.common.UserDatabaseEntry;
import org.wso2.carbon.rssmanager.core.dto.restricted.Database;
import org.wso2.carbon.rssmanager.core.dto.restricted.DatabaseUser;
import org.wso2.carbon.rssmanager.core.exception.RSSManagerException;

public interface RSSManager {

    /* Methods to manage databases */

    /**
     * Add database to the system.
     * During the adding of the database, it will perform a distributed transaction which performs adding the
     * database to the meta data repository and the specified rss instance
     * If the database is adding to the user defined rss instance, it wil only add user to the specified rss instance
     * If the database adding to the system rss instance, it will add it to one of the system rss instances in the environment
     * in round robin manner
     * @param database database properties
     * @return Database
     * @throws RSSManagerException
     */
    Database addDatabase(Database database) throws RSSManagerException;

    Database editDatabase(Database database)throws RSSManagerException;

    /**
     * Remove database from the system.
     * During the removal of the database, it will perform a distributed transaction which performs remove the
     * database entry from the meta data repository and the specified rss instance
     * @param rssInstanceName name of the rss instance
     * @param databaseName name of the database
     * @throws RSSManagerException
     */
    void removeDatabase(String rssInstanceName, String databaseName) throws RSSManagerException;

    /**
     * Get databases
     * @return Database[]
     * @throws RSSManagerException
     */
    Database[] getDatabases() throws RSSManagerException;

    /**
     * Check whether database is exist in given rss instance
     * @param rssInstanceName name of the rss instance
     * @param databaseName name of the database
     * @return boolean
     * @throws RSSManagerException
     */
    boolean isDatabaseExist(String rssInstanceName, String databaseName) throws RSSManagerException;

    /**
     * Get database info of a given database
     * @param rssInstanceName name of the rss instance
     * @param databaseName name of the database
     * @return Database
     * @throws RSSManagerException
     */
    Database getDatabase(String rssInstanceName, String databaseName) throws RSSManagerException;

    /**
     * Add database user.
     * During the adding of the database user, it will perform a distributed transaction which performs adding the
     * database user to the meta data repository and the specified rss instances which depend on the system rss instances
     * and user defined rss instance
     * If the user is adding to the user defined rss instance, it wil only add user to the specified rss instance
     * If the user adding to the system rss instance, it will add it to the all the system rss instances in the environment
     * @param user database user info
     * @return DatabaseUser
     * @throws RSSManagerException
     */
    DatabaseUser addDatabaseUser(DatabaseUser user) throws RSSManagerException;

    /**
     * Remove database user
     * During the removal of the database user, it will perform a distributed transaction which performs remove the
     * database user entry from the meta data repository and the specified rss instance
     * @param rssInstanceName
     * @param username
     * @throws RSSManagerException
     */
    void removeDatabaseUser(String rssInstanceName, String username) throws RSSManagerException;

    /**
     * Get database user in a given rss instance and username
     * @param rssInstanceName name of the rss instance
     * @param username name of the database user
     * @return DatabaseUser
     * @throws RSSManagerException
     */
    DatabaseUser getDatabaseUser(String rssInstanceName, String username) throws RSSManagerException;

    /**
     * Get attached database users given rss instance and databas
     * @param rssInstanceName name of the rss instance
     * @param databaseName name of the database
     * @return DatabaseUser[]
     * @throws RSSManagerException
     */
    DatabaseUser[] getAttachedUsers(String rssInstanceName,
                                    String databaseName) throws RSSManagerException;

    /**
     * Get available users to attach to the database
     * @param rssInstanceName name of the rss instance
     * @param databaseName name of the database
     * @return DatabaseUser[]
     * @throws RSSManagerException
     */
    DatabaseUser[] getAvailableUsers(
            String rssInstanceName, String databaseName) throws RSSManagerException;

    /**
     * Attach database user
     * During the attaching of the database user, it will perform a distributed transaction which performs adding the
     * database user entry to the meta data repository and the specified rss instances
     * @param ude user database entry
     * @param privileges database user privilege template to attach to the database user
     * @throws RSSManagerException
     */
    void attachUser(UserDatabaseEntry ude,
                    DatabasePrivilegeSet privileges) throws RSSManagerException;

    /**
     * Deatach user from the database in a provided rss instance
     * During the detaching of the database user, it will perform a distributed transaction which performs remove the
     * database user entry from the meta data repository and the specified rss instance
     * @param ude user database entry
     * @throws RSSManagerException
     */
    void detachUser(UserDatabaseEntry ude) throws RSSManagerException;

    /**
     * Get database users
     * @return DatabaseUser[]
     * @throws RSSManagerException
     */
    DatabaseUser[] getDatabaseUsers() throws RSSManagerException;

    /**
     * Check whether database user exist in the given rss instance
     * @param rssInstanceName name of the rss instance
     * @param username name of the database user
     * @return
     * @throws RSSManagerException
     */
    boolean isDatabaseUserExist(String rssInstanceName, String username) throws RSSManagerException;

    /**
     * Update database user privileges
     * During updating of the database user privileges, it will perform a distributed transaction which perform update on the
     * database user privileges of the the meta data repository and the specified rss instances
     * @param privileges
     * @param user
     * @param databaseName
     * @throws RSSManagerException
     */
    void updateDatabaseUserPrivileges(DatabasePrivilegeSet privileges, DatabaseUser user,
                                      String databaseName) throws RSSManagerException;

    /**
     * Get database user privileges given attached database user
     * @param rssInstanceName name of the rss instance
     * @param databaseName name of the database
     * @param username name of the database user
     * @return
     * @throws RSSManagerException
     */
    DatabasePrivilegeSet getUserDatabasePrivileges(String rssInstanceName, String databaseName,
                                                   String username) throws RSSManagerException;

    /**
     * Edit the database user properties
     * During the updating of the database user properties, it will perform a distributed transaction which update the
     * database user properties of meta data repository and rss instances depend on the system or user defined
     * If user is from the user define rss instance,user in that rss instance will be updated
     * If the user is from the system rss instance, the user which in the all the system rss instances will be updated
     * @param environmentName
     * @param databaseUser
     * @return
     * @throws RSSManagerException
     */
    DatabaseUser editDatabaseUser(String environmentName,DatabaseUser databaseUser) throws RSSManagerException;

}
