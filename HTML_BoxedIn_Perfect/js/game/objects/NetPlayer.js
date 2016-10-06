function InitNetPlayer(netPlayer, x) {
    netPlayer.x = x;
    netPlayer.netX = x;

    // movement
    netPlayer.isMoving = false;
    netPlayer.moveStartX = 0;
    netPlayer.moveTime = 0;
    netPlayer.facingDirection = 1; // -1 == left, 1 == right
    netPlayer.movingDirection = 1; // -1 == left, 1 == right

    // dodging
    netPlayer.isDodging = false;
    netPlayer.dodgeTime = 0;
    netPlayer.dodgeStartX = 0;
    netPlayer.dodgeTargetX = 0;
    netPlayer.dodgeDirection = 0; // 0 == in place, -1 == left, 1 == right

    // attacks
    netPlayer.attackTime = 0;
    netPlayer.isSlashing = false;
    netPlayer.isCounteringSlash = false;
    netPlayer.isStabbing = false;
    netPlayer.isCounteringStab = false;
    netPlayer.isThrowingShuriken = false;
    netPlayer.isCounteringShuriken = false;
}

function NetPlayer() {
    var netPlayer = this;
    // rect grapix
    netPlayer.width = gPlayerSideLen
    netPlayer.height = gPlayerSideLen
    netPlayer.y = gRenderer.height / 2 - netPlayer.height / 2;
    netPlayer.color = 0xC62828;

    // innit m8
    InitNetPlayer(netPlayer, gRenderer.width * 2 / 3 - netPlayer.width / 2);

    // slash
    netPlayer.slashIndicator = {};
    netPlayer.slashIndicator.x = netPlayer.x;
    netPlayer.slashIndicator.y = netPlayer.y;
    netPlayer.slashIndicator.width = 1;
    netPlayer.slashIndicator.height = 1;
    netPlayer.slashIndicator.color = 0x90CAF9;

    netPlayer.mySlash = {};
    netPlayer.mySlash.x = netPlayer.x;
    netPlayer.mySlash.y = netPlayer.y;
    netPlayer.mySlash.width = 1;
    netPlayer.mySlash.height = 1;
    netPlayer.mySlash.color = 0;

    // stab
    netPlayer.stabIndicator = {};
    netPlayer.stabIndicator.x = netPlayer.x;
    netPlayer.stabIndicator.y = netPlayer.y + netPlayer.height / 2 - gPlayerStabHeight / 2;
    netPlayer.stabIndicator.width = 1;
    netPlayer.stabIndicator.height = 1;
    netPlayer.stabIndicator.color = 0x90CAF9;

    netPlayer.myStab = {};
    netPlayer.myStab.x = netPlayer.x;
    netPlayer.myStab.y = netPlayer.y;
    netPlayer.myStab.width = 1;
    netPlayer.myStab.height = 1;
    netPlayer.myStab.color = 0;

    // shuriken
    netPlayer.shurikenIndicator = {};
    netPlayer.shurikenIndicator.x = netPlayer.x;
    netPlayer.shurikenIndicator.y = netPlayer.y + netPlayer.height / 2 - gPlayerShurikenSideLen / 2;
    netPlayer.shurikenIndicator.width = 1;
    netPlayer.shurikenIndicator.height = 1;
    netPlayer.shurikenIndicator.color = 0x90CAF9;

    netPlayer.myShuriken = {};
    netPlayer.myShuriken.x = netPlayer.x;
    netPlayer.myShuriken.y = netPlayer.y + netPlayer.height / 2 - gPlayerShurikenSideLen / 2;
    netPlayer.myShuriken.width = 1;
    netPlayer.myShuriken.height = 1;
    netPlayer.myShuriken.color = 0;

    // facing indicator
    netPlayer.facingIndicator = {};
    netPlayer.facingIndicator.myPlayer = netPlayer;
    netPlayer.facingIndicator.x = netPlayer.x;
    netPlayer.facingIndicator.y = netPlayer.y;
    netPlayer.facingIndicator.width = netPlayer.width / 10;
    netPlayer.facingIndicator.height = netPlayer.height;
    netPlayer.facingIndicator.color = 0;

    // add netPlayer grapix
    AddRect(this);
    AddRect(netPlayer.facingIndicator);

    // add attack grapix
    AddRect(netPlayer.slashIndicator);
    netPlayer.slashIndicator.graphics.scale.x = 0;
    netPlayer.slashIndicator.graphics.scale.y = netPlayer.height;
    AddRect(netPlayer.mySlash);
    netPlayer.mySlash.graphics.scale.x = 0;
    netPlayer.mySlash.graphics.scale.y = netPlayer.height;
    AddRect(netPlayer.stabIndicator);
    netPlayer.stabIndicator.graphics.scale.x = 0;
    netPlayer.stabIndicator.graphics.scale.y = gPlayerStabHeight;
    AddRect(netPlayer.myStab);
    netPlayer.myStab.graphics.scale.x = 0;
    netPlayer.myStab.graphics.scale.y = netPlayer.height;
    AddRect(netPlayer.shurikenIndicator);
    netPlayer.shurikenIndicator.graphics.scale.x = 0;
    netPlayer.shurikenIndicator.graphics.scale.y = gPlayerShurikenSideLen;
    AddRect(netPlayer.myShuriken);
    netPlayer.myShuriken.graphics.scale.x = 0;
    netPlayer.myShuriken.graphics.scale.y = 0;

    return netPlayer;
}
