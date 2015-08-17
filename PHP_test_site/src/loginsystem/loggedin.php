<html>
	<body>
		<?php
                    include '../includes.php';
                    Tracking::track(basename(filter_input(INPUT_SERVER, 'PHP_SELF')));
                    
                    $button_value = "logout";
                    
                    $account = new User();
                    
                    if($account->isLoggedIn()) {
                        echo "You logged in successfully!";
                    }
                    else {
                        echo "Login failed. Either your cookies are disabled or you didn't get to this page from the login.php page";
                        $button_value = "back to homepage";
                    }
		?>
		<form name="logoutform" method="post" action="logout.php">
			<p>
				<input type="submit" name="submit" value="<?php echo $button_value ?>" />
			</p>
		</form>
            <p>
                <a href="../createpage.php">create page</a>
                <br>
                <a href="../viewpages.php">view pages</a>
            </p>
	</body>
</html>