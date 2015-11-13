<?php
include 'includes.php';
Tracking::track (basename (filter_input (INPUT_SERVER, 'PHP_SELF')));
?>

<html>
<body>
	<a href="loginsystem/register.php">register</a>
	<br>
	<a href="loginsystem/login.php">login</a>
</body>
</html>