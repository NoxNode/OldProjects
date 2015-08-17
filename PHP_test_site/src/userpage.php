<html>
    <body>
        <ul>
            <?php
                include 'includes.php';
                Tracking::track(basename(filter_input(INPUT_SERVER, 'PHP_SELF')));

                $account = new User();
                $get_title = filter_input(INPUT_GET, 'title');
                
                if(!$get_title) {
                    echo "Error getting title";
                }

                if($account->isLoggedIn()) {
                    if(ctype_alnum($get_title)) {
                        $row = Queries::getRowWithValue(Database::$table_pages, 'title', $get_title);

                        if($row) {
                            $owner = $row['owner'];
                            $title = $row['title'];
                            $description = $row['description'];
                            $goal = $row['goal'];
                            $amount = $row['amount'];
                            
                            $pageinfo = array($owner, $title, $description, $goal, $amount);
                            
                            for($i = 0; $i < count($pageinfo); $i++) {
                                echo "<li>$pageinfo[$i]</li>";
                            }
                        }
                        else {
                            echo "Error getting a page with that title";
                        }
                    }
                    else {
                        echo "Title must be alphanumeric";
                    }
                }
                else {
                    echo "You must be logged in to access this page";
                }
            ?>
        </ul>
    </body>
</html>