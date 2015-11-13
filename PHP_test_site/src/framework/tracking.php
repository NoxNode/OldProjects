<?php
class Tracking {

	public static function track($page) {
		$ip = filter_input (INPUT_SERVER, 'REMOTE_ADDR');
		$time = time ();
		$date = date ("H:i:s - m/d/y", $time);
		if (! Queries::instertValuesIntoTable (Database::$table_track, array ($ip, $page, $date))) {
			echo '<br>error tracking<br>';
		}
	}

	public static function getIPActivity($ip) {
		$rows = Queries::getRowsWithValue (Database::$table_track, "ip", $ip);
		foreach ( $rows as $row ) {
			$pages [] = $row ['page'];
			$dates [] = $row ['date'];
		}
		return array ($pages, $dates);
	}

	public static function getPageActivity($page) {
		$rows = Queries::getRowsWithValue (Database::$table_track, "page", $page);
		foreach ( $rows as $row ) {
			$ips [] = $row ['ip'];
			$dates [] = $row ['date'];
		}
		return array ($ips, $dates);
	}

	public static function getDateActivity($date) {
		$rows = Queries::getAllRows (Database::$table_track);
		foreach ( $rows as $row ) {
			if (strpos ($row ['date'], $date) !== false) {
				$ips [] = $row ['ip'];
				$pages [] = $row ['page'];
			}
		}
		return array ($ips, $pages);
	}

	public static function isBanned($ip) {
		return Queries::getRowWithValue (Database::$table_bans, "ip", $ip);
	}

	public static function ban($ip) {
		Queries::insertBan (Database::$table_bans, $ip);
	}
}
?>
