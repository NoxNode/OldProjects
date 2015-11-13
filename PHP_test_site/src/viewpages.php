<html>
    <body>
        <ul>
            <?php
                include 'includes.php';
                Tracking::track(basename(filter_input(INPUT_SERVER, 'PHP_SELF')));

                
                
                $rows = Queries::getAllRows(Database::$table_pages);

                if($rows) {
                    foreach($rows as $row) {
                        $owners[] = $row['owner'];
                        $titles[] = $row['title'];
                        $descriptions[] = $row['description'];
                        $goals[] = $row['goal'];
                        $amounts[] = $row['amount'];
                    }
                    for($i = 0; $i < count($titles); $i++) {
                        $title = $titles[$i];
                        echo "<li><a href='userpage.php?title=$title'>$titles[$i] by $owners[$i] - $amounts[$i]/$goals[$i]</a></li>";
                    }
                }
                else {
                    echo "Error receiving data from database";
                }
            ?>
        </ul>
    </body>
</html>

