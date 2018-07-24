<?php
 
/**
 * A class file to connect to database
 */
class DB_CONNECT {
 
 static $svr = 'http://192.168.43.192';
    // static $svr = 'http://192.168.1.9';
 // static $svr = 'https://ukm-digital.000webhostapp.com';
    // constructor
    function __construct() {
        // connecting to database
        $this->connect();
    }
 
    // destructor
    function __destruct() {
        // closing db connection
        $this->close();
    }
 
    /**
     * Function to connect with database
     */
    function connect() {
        // import database connection variables, lokasinya sesuaikan lokasi db_config.php punyamu, kalo aku di dalam direktori "simple_crud"
        require_once 'db_config.php';
 
        // Connecting to mysql database
        $con = mysql_connect(DB_SERVER, DB_USER, DB_PASSWORD) or die(mysql_error());
 
        // Selecing database
        $db = mysql_select_db(DB_DATABASE) or die(mysql_error()) or die(mysql_error());
 
        // returing connection cursor
        return $con;
    }
 
    /**
     * Function to close db connection
     */
    function close() {
        // closing db connection
        mysql_close();
    }
 
}
 
?>