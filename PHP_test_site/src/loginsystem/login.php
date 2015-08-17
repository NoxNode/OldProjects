<html>
<body>
	<div>
		<?php
		include '../includes.php';
		Tracking::track (basename (filter_input (INPUT_SERVER, 'PHP_SELF')));
		
		$user = new User ();
		
		if ($user->isLoggedIn (true)) {
			header ("Location: loggedin.php");
		} else {
			$error_message = filter_input (INPUT_GET, 'error_message');
			if ($error_message && Input::isValidInput ($error_message, 1, 100, ctype_alnum ($error_message))) {
				echo $error_message;
			}
		}
		?>
            </div>
	<div id="loginbox">
		<form name="loginform" method="post" action="loginnext.php">
			<p>
				Username: <input type="text" name="username" size="15"
					maxlength="100" value="" />
			</p>
			<p>
				Password: <input type="password" name="password" size="15"
					maxlength="100" value="" />
			</p>
			<p>
				<input type="submit" name="submit" value="login" />
			</p>
		</form>
	</div>
</body>
</html>