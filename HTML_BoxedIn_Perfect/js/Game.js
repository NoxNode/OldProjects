// NOTE: development started February 17, 2016 - github.com/noxnode
// NOTE: development on stream started March 22, 2016 - twitch.tv/noxnode
// NOTE: school ended May 27, 2016
// NOTE: remade with a new simpler engine on June 11, 2016
// NOTE: remade again with a refined version of the simpler engine on June 13, 2016

var gPlayerMoveSpeed = 0.5; // pixels per millisecond
var gPlayerDodgeDuration = 150; // milliseconds
var gPlayerDodgeCooldown = 350; // milliseconds
var gPlayerDodgeSpeedCoef = 2; // coefficient to move speed
var gPlayerAttackDuration = 200; // milliseconds
var gPlayerAttackCooldown = 200; // milliseconds
var gPlayerStabOffsetX = 5;
var gPlayerStabHeight = 12;
var gPlayerShurikenOffsetX = 10;
var gPlayerShurikenSideLen = 10;
var gPlayerSideLen = 26;
var gShurikenSpeed = 1;
var gShurikenDuration = 1000;
var gPlayerInputDelay = 50;
var gNetPlayerInputDelay = 100;
var gPlayerMainColor = 0x2196F3;
var gPlayerDodgeColor = 0x64B5F6;
var gNetPlayerMainColor = 0xC62828;
var gNetPlayerDodgeColor = 0x90CAF9;

function local_InitGame() {
    // instructions text
    var instructionsText = CreateAndAddText("A/D to move left/right, space to dodge, J/K/L to slash/stab/throw shuriken");
    instructionsText.y = gRenderer.height - instructionsText.height;
    // finding match text
    var matchStatusText = gGame.matchStatusText = CreateAndAddText("Finding match...");

    // objects
    gGame.background = new Background();
    var netPlayer = gGame.netPlayer = new Player(gNetPlayerInputDelay, gNetPlayerMainColor, gNetPlayerDodgeColor);
    InitPlayer(netPlayer, gRenderer.width * 2 / 3 - gPlayerSideLen / 2);
    var player = gGame.player = new Player(gPlayerInputDelay, gPlayerMainColor, gPlayerDodgeColor);
    InitPlayer(player, gRenderer.width / 3 - gPlayerSideLen / 2);

    // updateFuncs
    AddUpdateFunc(player, PlayerMove);
    AddUpdateFunc(player, PlayerAttack);
    AddUpdateFunc(player, UpdateGraphicsPos);

    AddUpdateFunc(netPlayer, NetPlayerMove);
    AddUpdateFunc(netPlayer, NetPlayerAttack);
    AddUpdateFunc(netPlayer, UpdateGraphicsPos);
}

function InitGame() {
    gGame.stage = new PIXI.Container();
    gGame.updateFuncs = [];

    // connecting text
    var connectingText = gGame.connectingText = CreateAndAddText("Connecting to server...\nIf this takes awhile, refresh or check your internet connection");

    // connect to server
	gClient = new Client();
}

function onMatchFound() {
    if(gClient.otherIndex == 1) {
        InitPlayer(gGame.player, gRenderer.width / 3 - gPlayerSideLen / 2);
        InitPlayer(gGame.netPlayer, gRenderer.width * 2 / 3 - gPlayerSideLen / 2);
    }
    else {
        InitPlayer(gGame.player, gRenderer.width * 2 / 3 - gPlayerSideLen / 2);
        InitPlayer(gGame.netPlayer, gRenderer.width / 3 - gPlayerSideLen / 2);
    }
}

function RunGame() {
    if(gClient.isConnected) {
        if(!gGame.init) {
            gGame.init = true;
            local_InitGame();
            gGame.connectingText.scale.x = 0;
        }
        if(gClient.justFoundMatch) {
            gGame.matchStatusText.scale.x = 0;
            onMatchFound();
            gClient.justFoundMatch = false;
        }
        if(!gClient.isMatched) {
            gGame.matchStatusText.scale.x = 1;
        }
        Update();
    }
    else {
        gGame.connectingText.scale.x = 1;
    }
    Render();

    requestAnimationFrame(RunGame);
}

function Update() {
    // for controller
    Input_UpdateStatus();
    for(var i = 0; i < gGame.updateFuncs.length; i++) {
        var updateFuncContainer = gGame.updateFuncs[i];
        updateFuncContainer.run();
        // remove if removeMe is true
        if(updateFuncContainer.parentObj.removeMe) {
            var graphicsIndex = updateFuncContainer.parentObj.graphicsIndex;
            gGame.stage.removeChildAt(graphicsIndex);
            for(var j = graphicsIndex; j < gGame.stage.children.length; j++) {
                var child = gGame.stage.getChildAt(j);
                if(child.parentObj) {
                    child.parentObj.graphicsIndex--;
                }
            }
            gGame.updateFuncs.splice(i, 1);
            i--;
        }
    }
    // for keyboard and mouse
    RefreshInput();
}

function Render() {
    gRenderer.renderer.render(gGame.stage);
}

function CreateRect(width, height, color, parentObj) {
    var rect = new PIXI.Graphics();
    rect.beginFill(color);
    rect.drawRect(0, 0, width, height);
    rect.endFill();
    rect.parentObj = parentObj;
    return rect;
}

function CreateAndAddText(string) {
    var text = new PIXI.Text(string);
    gGame.stage.addChild(text);
    return text;
}

function AddRect(rect) {
    if(rect.parentObj != null) {
        rect.parentObj.graphicsIndex = gGame.stage.children.length;
    }
    gGame.stage.addChild(rect);
}

function ReplaceRectAt(index, rect) {
    gGame.stage.removeChildAt(index);
    gGame.stage.addChildAt(index, rect);
}

function AddUpdateFunc(object, func) {
    var funcContainer = {};
    funcContainer.run = func;
    funcContainer.parentObj = object;
    gGame.updateFuncs.push(funcContainer);
}
