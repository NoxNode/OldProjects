function InitPlayer(player, newX) {
    player.x = newX;

    player.isDodging = false;
    player.dodgeTime = 0;
    player.facingDir = 1;
    player.moveInputs = [{dir: 0, time: Date.now()}];
    player.xPositions = [newX];
    player.attackInputs = [];
    player.facingInputs = [];
    player.curAttack = 0;
    player.attackTime = 0;
}

function Player(inputDelay, mainColor, dodgeColor) {
    var player = this;
    player.inputDelay = inputDelay;

    player.y = gRenderer.height / 2 - gPlayerSideLen / 2;

    player.mainColor = mainColor;
    player.dodgeColor = dodgeColor;
    player.mainGraphics = CreateRect(gPlayerSideLen, gPlayerSideLen, player.mainColor, player);
    player.dodgeGraphics = CreateRect(gPlayerSideLen, player.dodgeColor, player);
    player.graphics = player.mainGraphics;
    AddRect(player.graphics);

    player.facingIndicator = CreateRect(gPlayerSideLen / 10, gPlayerSideLen, 0);
    player.facingIndicator.y = player.y;
    AddRect(player.facingIndicator);

    player.slashBox = CreateRect(1, 1, dodgeColor);
    player.slashBox.scale.x = 0;
    player.slashBox.scale.y = gPlayerSideLen;
    player.slashBox.y = player.y;
    AddRect(player.slashBox);
    player.mySlash = CreateRect(1, 1, 0);
    player.mySlash.scale.x = 0;
    player.mySlash.y = player.y;
    AddRect(player.mySlash);

    player.stabBox = CreateRect(1, 1, dodgeColor);
    player.stabBox.scale.x = 0;
    player.stabBox.scale.y = gPlayerStabHeight;
    player.stabBox.y = player.y + gPlayerSideLen / 2 - gPlayerStabHeight / 2;
    AddRect(player.stabBox);
    player.myStab = CreateRect(1, 1, 0);
    player.myStab.scale.x = 0;
    AddRect(player.myStab);

    return player;
}
