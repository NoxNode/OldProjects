function PlayerDodge() {
    var player = this.parentObj;
    var now = Date.now();
    var deltaDodgeTime = now - player.dodgeTime;
    if(player.isDodging) {
        player.color = 0x64B5F6;
        var dodgeDeltaTimeCoef = deltaDodgeTime / gPlayerDodgeDuration;
        if(dodgeDeltaTimeCoef < 1) {
            var deltaX = gPlayerDodgeDist * dodgeDeltaTimeCoef;
            if(player.dodgeTargetX < player.dodgeStartX) {
                player.x = player.dodgeStartX - deltaX;
            }
            else if(player.dodgeTargetX > player.dodgeStartX) {
                player.x = player.dodgeStartX + deltaX;
            }
            if(player.isAtLeftWall && player.dodgeTargetX < player.dodgeStartX) {
                var leftWall = gGame.background.leftWall;
                player.x = leftWall.x + leftWall.width;
            }
            else if(player.isAtRightWall && player.dodgeTargetX > player.dodgeStartX) {
                var rightWall = gGame.background.rightWall;
                player.x = rightWall.x - player.width;
            }
        }
        else {
            player.x = player.dodgeTargetX;
            if(player.isAtLeftWall) {
                var leftWall = gGame.background.leftWall;
                player.x = leftWall.x + leftWall.width;
                gClient.sendUpdate(gClient, {id: 6, time: now + 1});
            }
            else if(player.isAtRightWall) {
                var rightWall = gGame.background.rightWall;
                player.x = rightWall.x - player.width;
                gClient.sendUpdate(gClient, {id: 7, time: now + 1});
            }
            player.isDodging = false;
            gClient.sendUpdate(gClient, {id: 2, dir: 2, time: now});
        }
    }
    else {
        player.color = 0x2196F3;
    }
    if(!player.isDodging && deltaDodgeTime > gPlayerDodgeDuration + gPlayerDodgeCooldown && (gInput.keysDown[32] || gInput.controllerRB)) { // 32 = keycode for space bar
        var dodgeDir = 0;
        if((gInput.keysDown[65] && !gInput.keysDown[68]) || gInput.controllerLeft) {
            if(player.isAtLeftWall)
                dodgeDir = 0;
            else
                dodgeDir = -1;
        }
        else if((gInput.keysDown[68] && !gInput.keysDown[65]) || gInput.controllerRight) {
            if(player.isAtRightWall)
                dodgeDir = 0;
            else
                dodgeDir = 1;
        }
        else {
            dodgeDir = 0;
        }
        player.dodgeStartX = player.x;
        player.dodgeTime = now;
        player.isDodging = true;
        player.dodgeTargetX = player.x + dodgeDir * gPlayerDodgeDist;
        if(player.wasMoving) {
            player.wasMoving = false;
            // send stop packet
            gClient.sendUpdate(gClient, {id: 0, dir: 0, time: now});
        }
        // send dodge packet
        gClient.sendUpdate(gClient, {id: 2, dir: dodgeDir, time: now + 1});
    }
}
