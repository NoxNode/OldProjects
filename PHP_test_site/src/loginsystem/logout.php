<?php
        include '../includes.php';
        Tracking::track(basename(filter_input(INPUT_SERVER, 'PHP_SELF')));
        
        User::logout();
	
	header("Location: ../default.php");
?>