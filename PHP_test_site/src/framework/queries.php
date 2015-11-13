<?php
class Queries {

	public static function valueOccurances($table, $column, $value) {
		$query = "SELECT * FROM $table WHERE $column = '$value'";
		$result = mysql_query ($query);
		if (! $result) {
			return $result;
		}
		$num = mysql_num_rows ($result);
		return $num;
	}

	public static function deleteValueFromTable($table, $column, $value) {
		$query = "DELETE FROM $table WHERE $column = '$value' LIMIT 1";
		$result = mysql_query ($query);
		return $result;
	}

	public static function setValue($table, $column, $old_value, $new_value) {
		$query = "UPDATE $table SET $column = '$new_value' WHERE $column = '$old_value'";
		$result = mysql_query ($query);
		return $result;
	}

	public static function instertValuesIntoTable($table, $values) {
		$query = "SHOW COLUMNS FROM $table";
		$result = mysql_query ($query);
		if (! $result) {
			return $result;
		}
		$query = "INSERT INTO $table SET";
		
		if (mysql_num_rows ($result) > 0) {
			$row = mysql_fetch_assoc ($result);
			$field = $row ['Field'];
			$query = $query . " $field = '$values[0]'";
			for($i = 1; $row = mysql_fetch_assoc ($result); $i++) {
				$field = $row ['Field'];
				$query = $query . ", $field = '$values[$i]'";
			}
		}
		$result = mysql_query ($query);
		return $result;
	}

	public static function getRowWithValue($table, $column, $value) {
		$query = "SELECT * FROM $table WHERE $column = '$value'";
		$result = mysql_query ($query);
		if (! $result) {
			return $result;
		}
		$count = mysql_num_rows ($result);
		if ($count == 1) {
			return mysql_fetch_array ($result, MYSQL_BOTH);
		}
		return false;
	}

	public static function getRowsWithValue($table, $column, $value) {
		$query = "SELECT * FROM $table WHERE $column = '$value'";
		$result = mysql_query ($query);
		if (! $result) {
			return $result;
		}
		$rows = array ();
		while ( $row = mysql_fetch_array ($result, MYSQL_BOTH) ) {
			$rows [] = $row;
		}
		return $rows;
	}

	public static function getAllRows($table) {
		$query = "SELECT * FROM $table WHERE 1";
		$result = mysql_query ($query);
		if (! $result) {
			return $result;
		}
		$rows = array ();
		while ( $row = mysql_fetch_array ($result, MYSQL_BOTH) ) {
			$rows [] = $row;
		}
		return $rows;
	}
}
?>