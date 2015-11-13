<?php
    include '../includes.php';
    Tracking::track(basename(filter_input(INPUT_SERVER, 'PHP_SELF')));

	$passkey = filter_input(INPUT_GET, 'passkey');
        
        if(!$passkey) {
            echo "Error receiving passkey";
        }
        else if(!ctype_digit($passkey)) {
            echo "Invalid passkey";
        }
        else if(Queries::valueOccurances(Database::$table_temp, "code", $passkey) == 1) {
            $rows = Queries::getRowWithValue(Database::$table_temp, "code", $passkey);
            
            if(Queries::instertValuesIntoTable(Database::$table_users, array($rows['username'], $rows['password'], $rows['email']))) {
                echo "Email confirmation successful!";
                if(Queries::deleteValueFromTable(Database::$table_temp, "code", $passkey)) {
                    echo "<br>User info moved from temporary to permanent database";
                }
            }
        }
?>