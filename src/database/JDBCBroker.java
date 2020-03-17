// tabs=4
//************************************************************
//	COPYRIGHT 2007 ArchSynergy, Ltd. - ALL RIGHTS RESERVED
//
// This file is the product of ArchSynergy, Ltd. and cannot be 
// reproduced, copied, or used in any shape or form without 
// the express written consent of ArchSynergy, Ltd.
//************************************************************
//
//	$URL: svn://archsynergy.net/Volumes/Storage/SEEMTool/Implementation/database/JDBCBroker.java $
//
//	$Date: 2007-01-08 19:49:24 -0500 (Mon, 08 Jan 2007) $
//
//	Reason: Manage the connection to the database. This is a singleton!
//
//*************************************************************

/**
 * @author $Author: tomb $  @version	$Revision: 168 $  @version	$Revision: timmullins,2008-02-20 $
 * @version $Revision: 168 $  @version $Revision: timmullins,2008-02-20 $
 * @version $Revision: 168 $  @version $Revision: timmullins,2008-02-20 $
 */
/** @version $Revision: 168 $ */
/** @version $Revision: timmullins,2008-02-20 $ */

// specify the package
package database;

/// system imports

import Utilities.Debug;
import common.PropertyFile;
import event.Event;

import javax.swing.*;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;

// project imports

//==============================================================
public class JDBCBroker {
    public static Driver theDriver = null;
    // Single broker to be shared by all other Servlets
    private static JDBCBroker myInstance = null;
    private static Connection theDBConnection = null;
    private PropertyFile props;

    // DB Access data
    private String dbName;
    private String username;
    private String password;
    private String server;

    // private constructor for singleton
    //----------------------------------------------------------
    protected JDBCBroker() {
        // DEBUG: System.out.println("JDBCBroker.JDBCBroker()");
        props = new PropertyFile("dbConfig.ini");
        dbName = props.getProperty("dbName");
        username = props.getProperty("username");
        password = props.getProperty("password");
        server = props.getProperty("server");
        if (server == null)
            server = "localhost";
        String driverClassName = "com.mysql.jdbc.Driver";
        try {
            // load and register the JDBC driver classes
            theDriver = (Driver) Class.forName(driverClassName).newInstance();
        } catch (ClassNotFoundException exc) {
            System.err.println("JDBCBroker.JDBCBroker - Could not load driver class: ClassNotFoundException");
            new Event(Event.getLeafLevelClassName(this), "JDBCBroker", "Could not load driver class[" + driverClassName + "]", Event.ERROR);
        } catch (InstantiationException exc) {
            System.err.println("JDBCBroker.JDBCBroker - Could not load driver class: InstantiationException");
            new Event(Event.getLeafLevelClassName(this), "JDBCBroker", "Could not load driver class[" + driverClassName + "]", Event.ERROR);
        } catch (IllegalAccessException exc) {
            System.err.println("JDBCBroker.JDBCBroker - Could not load driver class: IllegalAccessException");
            new Event(Event.getLeafLevelClassName(this), "JDBCBroker", "Could not load driver class[" + driverClassName + "]", Event.ERROR);
        }
    }

    // singleton constructor
    //----------------------------------------------------------
    static public JDBCBroker getInstance() {
        // DEBUG: System.out.println("JDBCBroker.getInstance()");

        if (myInstance == null) {
            myInstance = new JDBCBroker();
        }

        return myInstance;
    }

    /** Create a connection to the database */
    //--------------------------------------------------------
    public Connection getConnection() {
        if (myInstance != null
                && theDBConnection == null
                && (dbName != null) && (username != null) && (password != null)) {
            try {
                //theDBConnection = DriverManager.getConnection("jdbc:mysql://dingusdong.us:3306/csc429_group_john","john","reallygoodpassword");
                System.out.println("Connecting to database...");
                theDBConnection = theDriver.connect("jdbc:mysql://192.168.1.97:3306/csc429_group_john?user=john&password=reallygoodpassword", null);
                if (theDBConnection == null) Debug.logErr("No database connection");
            } catch (SQLException exc) {
                System.err.println("JDBCBroker.getConnection - Could not connect to database!" + "\n" + exc.getMessage());
            }
        }
        //System.out.println("JDBCBroker.getConnection() with connection " + theDBConnection);
        return theDBConnection;
    }


    /** Release a previously allocated connection */
    //--------------------------------------------------------
    public void releaseConnection(Connection connection) {
        // don't release the connection, hang on till we're destructed
    }

    //--------------------------------------------------------
    protected void finalize() {
        if (theDBConnection != null) {
            try {
                theDBConnection.close();
                theDBConnection = null;
            } catch (SQLException exc) {
                new Event(Event.getLeafLevelClassName(this), "releaseConnection", "Could not release connection", Event.WARNING);
            }
        }
    }
}
