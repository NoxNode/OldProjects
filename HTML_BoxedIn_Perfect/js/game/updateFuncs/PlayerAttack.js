function PlayerAttack() {
    var player = this.parentObj;
    var now = Date.now();
    HandleAttackInputs(player, now);
    AnimateAttacks(player, now, player.inputDelay);
}

function AnimateAttacks(player, now, delay) {
    var time = now - delay;
    for(var i = player.attackInputs.length - 1; i >= 0; i--) {
        var curInput = player.attackInputs[i];
        if(curInput.time < time) {
            if(curInput.atk == 1) {
                if(player.facingDir == -1) {
                    player.slashBox.scale.x = gPlayerSideLen;
                    player.slashBox.x = player.x - gPlayerSideLen;
                    player.mySlash.x = player.x;
                    var deltaTime = time - curInput.time;
                    var deltaScaleCoef = deltaTime / gPlayerAttackDuration;
                    if(deltaScaleCoef > 1) {
                        deltaScaleCoef = 1;
                    }
                    player.mySlash.scale.x = -gPlayerSideLen * deltaScaleCoef;
                    player.mySlash.scale.y = gPlayerSideLen * deltaScaleCoef;
                }
                else {
                    player.slashBox.scale.x = gPlayerSideLen;
                    player.slashBox.x = player.x + gPlayerSideLen;
                    player.mySlash.x = player.x + gPlayerSideLen;
                    var deltaTime = time - curInput.time;
                    var deltaScaleCoef = deltaTime / gPlayerAttackDuration;
                    if(deltaScaleCoef > 1) {
                        deltaScaleCoef = 1;
                    }
                    player.mySlash.scale.x = gPlayerSideLen * deltaScaleCoef;
                    player.mySlash.scale.y = gPlayerSideLen * deltaScaleCoef;
                }
            }
            else if(curInput.atk == 2) {
                if(player.facingDir == -1) {
                    player.stabBox.scale.x = gPlayerSideLen - gPlayerStabOffsetX;
                    player.stabBox.x = player.x - gPlayerSideLen;
                    player.myStab.x = player.x - gPlayerStabOffsetX;
                    var deltaTime = time - curInput.time;
                    var deltaScaleCoef = deltaTime / gPlayerAttackDuration;
                    if(deltaScaleCoef > 1) {
                        deltaScaleCoef = 1;
                    }
                    player.myStab.y = player.y + gPlayerSideLen / 2 - (gPlayerStabHeight * deltaScaleCoef) / 2;
                    player.myStab.scale.x = -(gPlayerSideLen - gPlayerStabOffsetX) * deltaScaleCoef;
                    player.myStab.scale.y = gPlayerStabHeight * deltaScaleCoef;
                }
                else {
                    player.stabBox.scale.x = gPlayerSideLen - gPlayerStabOffsetX;
                    player.stabBox.x = player.x + gPlayerSideLen + gPlayerStabOffsetX;
                    player.myStab.x = player.x + gPlayerSideLen + gPlayerStabOffsetX;
                    var deltaTime = time - curInput.time;
                    var deltaScaleCoef = deltaTime / gPlayerAttackDuration;
                    if(deltaScaleCoef > 1) {
                        deltaScaleCoef = 1;
                    }
                    player.myStab.y = player.y + gPlayerSideLen / 2 - (gPlayerStabHeight * deltaScaleCoef) / 2;
                    player.myStab.scale.x = (gPlayerSideLen - gPlayerStabOffsetX) * deltaScaleCoef;
                    player.myStab.scale.y = gPlayerStabHeight * deltaScaleCoef;
                }
            }
            else if(curInput.atk == 3) {

            }
            else if(curInput.atk == 0) {
                player.mySlash.scale.x = 0;
                player.slashBox.scale.x = 0;
                player.myStab.scale.x = 0;
                player.stabBox.scale.x = 0;
            }
            break;
        }
    }
}

function HandleAttackInputs(player, now) {
    var attackDeltaTime = now - player.attackTime;
    if(player.curAttack != 0 && attackDeltaTime > gPlayerAttackDuration) {
        if(player.curAttack == 1 && !gInput.keysDown[74]) {
            player.attackInputs.push({atk: 0, time: now});
            gClient.sendUpdate(gClient, {id: 2, atk: 0, time: now});
            player.curAttack = 0;
        }
        else if(player.curAttack == 2 && !gInput.keysDown[75]) {
            player.attackInputs.push({atk: 0, time: now});
            gClient.sendUpdate(gClient, {id: 2, atk: 0, time: now});
            player.curAttack = 0;
        }
    }
    if(player.curAttack == 0 && attackDeltaTime > gPlayerAttackDuration + gPlayerAttackCooldown) {
        if(gInput.keysDown[74]) {
            player.attackInputs.push({atk: 1, time: now});
            gClient.sendUpdate(gClient, {id: 2, atk: 1, time: now});
            player.curAttack = 1;
            player.attackTime = now;
        }
        if(gInput.keysDown[75]) {
            player.attackInputs.push({atk: 2, time: now});
            gClient.sendUpdate(gClient, {id: 2, atk: 2, time: now});
            player.curAttack = 2;
            player.attackTime = now;
        }
        // if(gInput.keysDown[76]) {
        //     player.attackInputs.push({atk: 3, time: now});
        //     player.curAttack = 3;
        //     player.attackTime = now;
        // }
    }
}
