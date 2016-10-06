var gInput = {};

function RefreshInput() {
    gInput.keysJustDown = {};
    gInput.keysJustUp = {};
    gInput.leftMouseJustDown = false;
    gInput.rightMouseJustDown = false;
}

function GetScaledMousePos() {
    var xPos = gInput.mousePos.x * gRenderer.canvas.width / gRenderer.styleWidth;
    var yPos = gInput.mousePos.y * gRenderer.canvas.height / gRenderer.styleHeight;
    return {x: xPos, y: yPos};
}

function InitInput() {
    if(!gInput.initialized) {
        gInput.initialized = true;

        ////////// keys //////////
        gInput.keysDown = {};
        gInput.keysJustDown = {};
        gInput.keysJustUp = {};

        document.addEventListener("keydown", function (e) {
            if(!gInput.keysDown[e.keyCode]) {
        	       gInput.keysJustDown[e.keyCode] = true;
            }
        	gInput.keysDown[e.keyCode] = true;
        }, false);

        document.addEventListener("keyup", function (e) {
            gInput.keysJustUp[e.keyCode] = true;
        	delete gInput.keysDown[e.keyCode];
        }, false);

        ////////// mouse //////////
        gInput.mousePos = {};
        gInput.leftMouseDown = false;
        gInput.rightMouseDown = false;
        gInput.leftMouseJustDown = false;
        gInput.rightMouseJustDown = false;
        gInput.mouseWheelDeltaY = 0;

        document.addEventListener("mousemove", function(e) {
            gInput.mousePos.x = e.clientX;
            gInput.mousePos.y = e.clientY;
        }, false);

        document.addEventListener("mousedown", function(e) {
        	if(e.button == 2) {
        		gInput.rightMouseDown = true;
        		gInput.rightMouseJustDown = true;
        	}
        	else if(e.button == 0) {
        		gInput.leftMouseDown = true;
        		gInput.leftMouseJustDown = true;
        	}
        }, false);

        document.addEventListener("mouseup", function(e) {
        	if(e.button == 2) {
        		gInput.rightMouseDown = false;
        	}
        	else {
        		gInput.leftMouseDown = false;
        	}
        }, false);

        document.addEventListener("wheel", function(e) {
        	gInput.mouseWheelDeltaY = e.deltaY;
        }, false);

        ////////// gamepad //////////
        var haveEvents = 'GamepadEvent' in window;
        gInput.controllers = {};

        if (haveEvents) {
            window.addEventListener("gamepadconnected", Input_Connecthandler);
            window.addEventListener("gamepaddisconnected", Input_Disconnecthandler);
        } else {
            setInterval(Input_Scangamepads, 500);
        }
    }
}

function Input_Connecthandler(e) {
    Input_Addgamepad(e.gamepad);
}
function Input_Addgamepad(gamepad) {
    gInput.controllers[gamepad.index] = gamepad;
}

function Input_Disconnecthandler(e) {
    Input_Removegamepad(e.gamepad);
}

function Input_Removegamepad(gamepad) {
    delete gInput.controllers[gamepad.index];
}

function Input_UpdateStatus() {
    Input_Scangamepads();
    // left
    gInput.controllerLeft = gInput.controllers[0] && (gInput.controllers[0].buttons[14].pressed || gInput.controllers[0].axes[0] < -0.2);
    // right
    gInput.controllerRight = gInput.controllers[0] && (gInput.controllers[0].buttons[15].pressed || gInput.controllers[0].axes[0] > 0.2);
    // LB
    gInput.controllerLB = gInput.controllers[0] && gInput.controllers[0].buttons[4].pressed;
    gInput.controllerRB = gInput.controllers[0] && gInput.controllers[0].buttons[5].pressed;
    // 4 main buttons
    gInput.controllerA = gInput.controllers[0] && gInput.controllers[0].buttons[0].pressed;
    gInput.controllerB = gInput.controllers[0] && gInput.controllers[0].buttons[1].pressed;
    gInput.controllerX = gInput.controllers[0] && gInput.controllers[0].buttons[2].pressed;
    gInput.controllerY = gInput.controllers[0] && gInput.controllers[0].buttons[3].pressed;
}

function Input_Scangamepads() {
    var gamepads = navigator.getGamepads ? navigator.getGamepads() : (navigator.webkitGetGamepads ? navigator.webkitGetGamepads() : []);
    for (var i = 0; i < gamepads.length; i++) {
        if (gamepads[i]) {
            if (!(gamepads[i].index in gInput.controllers)) {
                Input_Addgamepad(gamepads[i]);
            } else {
                gInput.controllers[gamepads[i].index] = gamepads[i];
            }
        }
    }
}
