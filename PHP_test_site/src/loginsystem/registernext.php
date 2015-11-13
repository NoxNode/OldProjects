<?php
	include '../includes.php';
	Tracking::track (basename (filter_input (INPUT_SERVER, 'PHP_SELF')));
	
	$account = new User ();
	
	$account->username = filter_input (INPUT_POST, 'username');
	$account->password = filter_input (INPUT_POST, 'password');
	$account->email = filter_input (INPUT_POST, 'email');
	
	$confirm_password = filter_input (INPUT_POST, 'password2');
	$confirm_email = filter_input (INPUT_POST, 'email2');
	
	$error_message = "";
	
	if (!Input::isValidInput ($account->username, 3, 100, ctype_alnum ($account->username))) {
		$error_message = "invalid username";
	} else if (!Input::isValidInput ($account->password, 3, 100, ctype_alnum ($account->password))) {
		$error_message = "invalid password";
	} else if (!Input::isValidInput ($account->email, 7, 100, filter_var ($account->email, FILTER_VALIDATE_EMAIL))) {
		$error_message = "invalid email";
	} 
	
	else if (Queries::valueOccurances (Database::$table_users, 'username', $account->username)) {
		$error_message = "Username already exists";
		header ("Location: register.php?error_message=$error_message");
	} else if (Queries::valueOccurances (Database::$table_temp, 'username', $account->username)) {
		$error_message = "The username you entered is already registered, but has not confirmed their email. The activation email has been resent.";
		// Resend email
		$message = "Your confirmation link: \r\n";
		$message .= "Click on this link to activate your account\n";
		$message .= "http://bmetest.site40.net/confirm.php?passkey=$confirm_code";
		
		mail ($account->email, 'Registration Confirmation', "$message");
		
		header ("Location: register.php?error_message=$error_message");
	} 
	
	else if ($confirm_password != $account->password) {
		$error_message = "Passwords do not match";
		header ("Location: register.php?error_message=$error_message");
	} else if ($confirm_email != $account->email) {
		$error_message = "Emails do not match";
		header ("Location: register.php?error_message=$error_message");
	} else if ($error_message == "") {
		$confirm_code = rand ();
		
		while ( Queries::valueOccurances (Database::$table_temp, 'code', $confirm_code) ) {
			$confirm_code = rand ();
		}
		
		if (!Queries::instertValuesIntoTable (Database::$table_temp, array ($confirm_code, $account->username, $account->password, $account->email))) {
			$error_message = "Error putting user into temporary database";
			header ("Location: register.php?error_message=$error_message");
		}
		
		$message = "Your confirmation link: \r\n";
		$message .= "Click on this link to activate your account\n";
		$message .= "http://bmetest.site40.net/confirm.php?passkey=$confirm_code";
		
		mail ($account->email, 'Registration Confirmation', "$message");
		header ("Location: thankyou.html");
	}
?>