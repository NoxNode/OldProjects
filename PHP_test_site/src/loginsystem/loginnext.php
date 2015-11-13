<?php
    include '../includes.php';
    Tracking::track(basename(filter_input(INPUT_SERVER, 'PHP_SELF')));
    
    $account = new User();
    
    $account->username = filter_input(INPUT_POST, 'username');
    $account->password = filter_input(INPUT_POST, 'password');
    
    $error_message = "";
    
    if(!$account->username) {
        $error_message = "Error receiving username";
    }
    else if(!$account->password) {
        $error_message = "Error receiving password";
    }
    else if(!ctype_alnum($account->username)) {
        $error_message = "Username must be alphanumeric";
    }
    else if(!ctype_alnum($account->password)) {
        $error_message = "Password must be alphanumeric";
    }
    
    if($error_message != "") {
        echo $error_message;
    }
    else {
        if(Queries::valueOccurances(Database::$table_users, "username", $account->username) == 1) {
            if(Queries::valueOccurances(Database::$table_users, "password", $account->password) == 1) {
                User::login($account->username, $account->password);
                header("Location: loggedin.php");
            }
            else {
                echo "Invalid password";
            }
        }
        else {
            echo "Invalid username";
        }
    }
?>