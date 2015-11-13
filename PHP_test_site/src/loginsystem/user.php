<?php
class User {

	public $username;

	public $password;

	public $email;

	public $registered;

	public $ips;

	public function isLoggedIn($cookie) {
		if ($cookie) {
			$username = filter_input (INPUT_COOKIE, "username");
			$password = filter_input (INPUT_COOKIE, "password");
			
			if ($username && $password && ctype_alnum ($username) && ctype_alnum ($password)) {
				$rows = Queries::getRowWithValue (Database::$table_users, "username", $username);
				if ($rows) {
					if ($rows ['password'] == $password) {
						$this->username = $username;
						$this->password = $password;
						$this->email = $rows ['email'];
						return true;
					}
				}
			}
			return false;
		}
	}

	public static function login($cookie, $username, $password) {
		if ($cookie) {
			setcookie ('username', $username, time () + (86400 * 30), "/"); // 86400 = 1 day
			setcookie ('password', $password, time () + (86400 * 30), "/"); // 86400 = 1 day
		}
	}

	public static function logout($cookie) {
		if ($cookie) {
			setcookie ('username', '', time () + (1), "/"); // 86400 = 1 day
			setcookie ('password', '', time () + (1), "/"); // 86400 = 1 day
		}
	}
}
?>