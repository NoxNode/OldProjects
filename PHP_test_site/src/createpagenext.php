<?php
    include 'includes.php';
    Tracking::track(basename(filter_input(INPUT_SERVER, 'PHP_SELF')));
    
    $account = new User();

    if($account->isLoggedIn()) {
        $title = filter_input(INPUT_POST, 'title');
        $description = filter_input(INPUT_POST, 'description');
        $goal = filter_input(INPUT_POST, 'goal');
        
        $error_message = "";
        if(!ctype_alnum($title) && strlen($title) < 3 || strlen($title) > 100) {
            $error_message = "Title is not alphanumeric, too short (less than 3 characters), or too long (over 100 characters)";
        }
        if(strpos($description, "'") !== false || strpos($description, "\"") !== false) {
            $error_message = "Description cannot contain a ' or a \"";
        }
        if(!ctype_digit($goal)) {
            $error_message = "Goal is not numeric";
        }
        if($error_message != "") {
            echo $error_message;
        }
        else if(!Queries::valueOccurances(Database::$table_pages, "title", $title)) {
            if(!Queries::instertValuesIntoTable(Database::$table_pages, array($account->username, $title, $description, $goal, 0))) {
                echo "Error inserting page info into database";
            }
            else {
                echo "Successfully created your page!";
            }
        }
        else {
            echo "There's already a page with that title";
        }
    }
    else {
        echo "You need to be logged in to access this page";
    }
?>
