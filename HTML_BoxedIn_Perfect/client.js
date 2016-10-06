var gClient;

(function () {
    var instance;
    Client = function Client() {
        if (instance) {
            return instance;
        }

        instance = this;

        this.connect();
        console.log("Client started");
    };
}());

Client.prototype.connect = function() {
    // var connString = config.protocol + config.domain + ':' + config.clientport;
    var connString = "ws://nodejs-noxide.rhcloud.com:8000";

    console.log("Websocket connection string:", connString, wsclientopts);
    // console.log("Websocket connection string:", connString, config.wsclientopts);

    var self = this;

    self.times = [];
    self.nMessages = 0;
    self.matchIndex = 0;
    self.otherIndex = -1;
    self.other = {x: 500, y: 368};
    self.justFoundMatch = false;
    self.isMatched = false;

    var wsclientopts = {
        reconnection: true,
        reconnectionDelay: 2000,
        reconnectionAttempts: 100,
        secure: false
    };
    this.socket = io.connect(connString, wsclientopts);
    // this.socket = io.connect(connString, config.wsclientopts);

    this.socket.on('error', function (err) {
        console.log("Websocket 'error' event:", err);
    });

    this.socket.on('connect', function () {
        console.log("Websocket 'connected' event with params:", self.socket);
        gClient.isConnected = true;
    });

    this.socket.on('disconnect', function () {
        console.log("Websocket 'disconnect' event");
        gClient.isConnected = false;
    });

    // this.socket.on('pos', function (data) {
    //     self.other.x = data.x;
    //     self.other.y = data.y;
    // });

    this.socket.on("textmessage", function (data) {
        console.log(data.msg);
    });

    this.socket.on('hello', function (data) {
        console.log("Server says:", data.greeting);

        // document.addEventListener("keydown", function (e) {
        //     if(e.keyCode == 13) { //13 == ASCII for enter key
        //         self.send(self, "ping", {msg:"hi"});
        //     }
        // }, false);
    });

    this.socket.on('pong', function (data) {
        console.log("" + data.msg);
    });

    this.socket.on('update', function (data) {
        if(data.id == 0) {
            gGame.netPlayer.moveInputs.push({dodge: data.dodge, dir: data.dir, time: data.time});
            PushNewX(gGame.netPlayer);
        }
        else if(data.id == 1) {
            gGame.netPlayer.facingInputs.push({dir: data.dir, time: data.time});
        }
        else if(data.id == 2) {
            gGame.netPlayer.attackInputs.push({atk: data.atk, time: data.time});
        }
    });

    this.socket.on('match', function (data) {
        if(data.otherIndex != -1) {
            self.matchIndex = data.matchIndex;
            self.otherIndex = data.otherIndex;
            console.log("found match");
            console.log("matchIndex: " + data.matchIndex);
            console.log("otherIndex: " + data.otherIndex);
            gClient.justFoundMatch = true;
            gClient.isMatched = true;
        }
        else {
            console.log("waiting for match...");
            data.otherIndex = -1;
            gClient.isMatched = false;
        }
    });

    Client.prototype.send = function (self, id, msgObj) {
        msgObj.matchIndex = gClient.matchIndex;
        msgObj.otherIndex = gClient.otherIndex;
        if(self.isMatched) {
            self.socket.emit(id, msgObj);
        }
    };

    Client.prototype.sendUpdate = function (self, msgObj) {
        msgObj.matchIndex = gClient.matchIndex;
        msgObj.otherIndex = gClient.otherIndex;
        if(self.isMatched) {
            self.socket.emit("update", msgObj);
        }
    };

    // Client.prototype.sendPos = function(self, xPos, yPos) {
    //     var msg = {
    //         matchIndex: self.matchIndex,
    //         otherIndex: self.otherIndex,
    //         x: xPos,
    //         y: yPos
    //     };
    //     self.socket.emit("pos", msg);
    // }

    Client.prototype.sendTextMessage = function(self, message) {
        var msgObj = {
            matchIndex: self.matchIndex,
            otherIndex: self.otherIndex,
            msg: message
        };
        self.socket.emit("textmessage", msgObj);
    }
}
