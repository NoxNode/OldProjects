<?php
    include 'includes.php';
    Tracking::track(basename(filter_input(INPUT_SERVER, 'PHP_SELF')));
    
    $user = new User();

    if(!$user->isLoggedIn()) {
        header('login.php?error_message=You need to sign in to create a page');
    }
?>

<html>
    <body>
        <div id="createpagebox">
            <form name="createpageform" method="post" action="createpagenext.php">
                <p>
                    Title: <input type="text" name="title" size="15" maxlength="100" value="" />
                </p>
                <p>
                    Description: <textarea name="description" rows="5" cols="50" ></textarea>
                </p>
                <p>
                    Funds goal: <input type="number" name="goal" size="15" maxlength="100" value="" />
                </p>
                <p>
                    <input type="submit" name="submit" value="createpage" />
                </p>
            </form>
        </div>
    </body>
</html>
