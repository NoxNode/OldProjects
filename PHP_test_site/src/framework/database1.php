<?php
class Database {

	public static $host = "mysql3.000webhost.com";

	public static $username = "a5879393_bmetest";

	public static $password = "password";

	public static $db_name = "a5879393_test";

	public static $table_users = "users";

	public static $table_temp = "temp";

	public static $table_track = "tracking";

	public static $table_bans = "bans";

	public static $table_pages = "userpages";

	public static $isConnected = false;

	public static function connect() {
		if (! self::$isConnected) {
			mysql_connect (self::$host, self::$username, self::$password) or die ("Cannot connect to server");
			
			mysql_select_db (self::$db_name) or die ("Cannot select DB");
			
			self::$isConnected = true;
		}
	}
}
?>