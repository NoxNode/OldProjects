<html>
<body>
	<div>
	<?php
	include '../includes.php';
	Tracking::track (basename (filter_input (INPUT_SERVER, 'PHP_SELF')));
	
	$error_message = filter_input (INPUT_GET, 'error_message');
	if ($error_message && Input::isValidInput ($error_message, 1, 100, ctype_alnum ($error_message))) {
		echo $error_message;
	}
	?>
    </div>
	<div id="registerbox">
		<form name="registerform" method="post" action="registernext.php">
			<p>
				Username: <input type="text" name="username" size="15"
					maxlength="100" value="" />
			</p>
			<p>
				Password: <input type="password" name="password" size="15"
					maxlength="100" value="" />
			</p>
			<p>
				Confirm Password: <input type="password" name="password2" size="15"
					maxlength="100" value="" />
			</p>
			<p>
				Email: <input type="text" name="email" size="15" maxlength="100"
					value="" />
			</p>
			<p>
				Confirm Email: <input type="text" name="email2" size="15"
					maxlength="100" value="" />
			</p>
			<p>
				<input type="submit" name="submit" value="register" />
			</p>
		</form>
	</div>
</body>
</html>