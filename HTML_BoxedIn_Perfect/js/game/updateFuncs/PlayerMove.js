function PlayerMove() {
    var player = this.parentObj;
    var now = Date.now();
    HandleMoveInputs(player, now);
    MovePlayer(player, now, player.inputDelay);
}

function MovePlayer(player, now, delay) {
    player.x = GetXAtTime(player, now - delay);
    player.facingDir = GetFacingDirAtTime(player, now - delay);
    if(player.facingDir == -1) {
        player.facingIndicator.x = player.x;
    }
    else {
        player.facingIndicator.x = player.x + gPlayerSideLen * 9 / 10;
    }
}

function GetXAtTime(player, time) {
    for(var i = player.moveInputs.length - 1; i >= 0; i--) {
        var curInput = player.moveInputs[i];
        var curPos = player.xPositions[i];
        if(curInput.time < time) {
            if(curInput.dir == 0) {
                return curPos;
            }
            else {
                var deltaTime = time - curInput.time;
                return curPos + curInput.dir * deltaTime * gPlayerMoveSpeed;
            }
        }
    }
    return player.x;
}

function GetFacingDirAtTime(player, time) {
    for(var i = player.facingInputs.length - 1; i >= 0; i--) {
        var curInput = player.facingInputs[i];
        if(curInput.time < time) {
            return curInput.dir;
        }
    }
}

function PushNewX(player) {
    var moveStartX = player.xPositions[player.xPositions.length - 1];
    var moveStart = player.moveInputs[player.moveInputs.length - 2];
    var moveEnd = player.moveInputs[player.moveInputs.length - 1];
    var deltaTime = moveEnd.time - moveStart.time;
    var newX = moveStartX + moveStart.dir * deltaTime * gPlayerMoveSpeed;
    player.xPositions.push(newX);
}

function HandleMoveInputs(player, now) {
    var dodgeDeltaTime = now - player.dodgeTime;
    var justDoneDodging = false;
    if(player.isDodging && dodgeDeltaTime > gPlayerDodgeDuration) {
        player.moveInputs.push({dir: 0, time: now});
        gClient.sendUpdate(gClient, {id: 0, dir: 0, time: now});
        PushNewX(player);
        player.isDodging = false;
        justDoneDodging = true;
    }
    if(!player.isDodging && dodgeDeltaTime > gPlayerDodgeDuration + gPlayerDodgeCooldown && gInput.keysDown[32]) {
        if(gInput.keysDown[65] && !gInput.keysDown[68]) {
            player.moveInputs.push({dir: -1 * gPlayerDodgeSpeedCoef, time: now});
            gClient.sendUpdate(gClient, {id: 0, dir: -1 * gPlayerDodgeSpeedCoef, time: now});
        }
        else if(!gInput.keysDown[65] && gInput.keysDown[68]) {
            player.moveInputs.push({dir: 1 * gPlayerDodgeSpeedCoef, time: now});
            gClient.sendUpdate(gClient, {id: 0, dir: 1 * gPlayerDodgeSpeedCoef, time: now});
        }
        else {
            player.moveInputs.push({dir: 0, time: now});
            gClient.sendUpdate(gClient, {id: 0, dir: 0, time: now});
        }
        PushNewX(player);
        player.isDodging = true;
        player.dodgeTime = now;
    }
    if(!player.isDodging) {
        if(gInput.keysJustDown[65] || (justDoneDodging && gInput.keysDown[65])) {
            if(!gInput.keysDown[68]) {
                player.moveInputs.push({dir: -1, time: now});
                gClient.sendUpdate(gClient, {id: 0, dir: -1, time: now});
                PushNewX(player);
                if(!gInput.keysDown[16]) {
                    player.facingInputs.push({dir: -1, time:now});
                    gClient.sendUpdate(gClient, {id: 1, dir: -1, time: now});
                }
            }
            else {
                player.moveInputs.push({dir: 0, time: now});
                gClient.sendUpdate(gClient, {id: 0, dir: 0, time: now});
                PushNewX(player);
            }
        }
        if(gInput.keysJustUp[65]) {
            if(gInput.keysDown[68]) {
                player.moveInputs.push({dir: 1, time: now});
                gClient.sendUpdate(gClient, {id: 0, dir: 1, time: now});
                PushNewX(player);
                if(!gInput.keysDown[16]) {
                    player.facingInputs.push({dir: 1, time:now});
                    gClient.sendUpdate(gClient, {id: 1, dir: 1, time: now});
                }
            }
            else {
                player.moveInputs.push({dir: 0, time: now});
                gClient.sendUpdate(gClient, {id: 0, dir: 0, time: now});
                PushNewX(player);
            }
        }
        if(gInput.keysJustDown[68] || (justDoneDodging && gInput.keysDown[68])) {
            if(!gInput.keysDown[65]) {
                player.moveInputs.push({dir: 1, time: now});
                gClient.sendUpdate(gClient, {id: 0, dir: 1, time: now});
                PushNewX(player);
                if(!gInput.keysDown[16]) {
                    player.facingInputs.push({dir: 1, time:now});
                    gClient.sendUpdate(gClient, {id: 1, dir: 1, time: now});
                }
            }
            else {
                player.moveInputs.push({dir: 0, time: now});
                gClient.sendUpdate(gClient, {id: 0, dir: 0, time: now});
                PushNewX(player);
            }
        }
        if(gInput.keysJustUp[68]) {
            if(gInput.keysDown[65]) {
                player.moveInputs.push({dir: -1, time: now});
                gClient.sendUpdate(gClient, {id: 0, dir: -1, time: now});
                PushNewX(player);
                if(!gInput.keysDown[16]) {
                    player.facingInputs.push({dir: -1, time:now});
                    gClient.sendUpdate(gClient, {id: 1, dir: -1, time: now});
                }
            }
            else {
                player.moveInputs.push({dir: 0, time: now});
                gClient.sendUpdate(gClient, {id: 0, dir: 0, time: now});
                PushNewX(player);
            }
        }
    }
}
