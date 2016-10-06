function Background() {
    var background = {};

    background.bottomWall = CreateRect(gRenderer.width * 4 / 5, gRenderer.height / 10, 0);
    background.bottomWall.x = gRenderer.width / 10;
    background.bottomWall.y = gRenderer.height / 2 + gPlayerSideLen / 2;
    AddRect(background.bottomWall);

    background.topWall = CreateRect(gRenderer.width * 4 / 5, gRenderer.height / 10, 0);
    background.topWall.x = gRenderer.width / 10;
    background.topWall.y = gRenderer.height / 4;
    AddRect(background.topWall);

    background.leftWall = CreateRect(gRenderer.width / 20, background.bottomWall.y - background.topWall.y + background.bottomWall.height, 0);
    background.leftWall.x = background.topWall.x - background.leftWall.width;
    background.leftWall.y = gRenderer.height / 4;
    AddRect(background.leftWall);

    background.rightWall = CreateRect(gRenderer.width / 20, background.bottomWall.y - background.topWall.y + background.bottomWall.height, 0);
    background.rightWall.x = background.topWall.width + background.topWall.x;
    background.rightWall.y = gRenderer.height / 4;
    AddRect(background.rightWall);

    return background;
}
