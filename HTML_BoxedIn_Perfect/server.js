// Check the configuration file for more details
var config = require('./config');

// Express.js stuff
var express = require('express');
var app = require('express')();
var server = require('http').Server(app);

// Websockets with socket.io
var io = require('socket.io')(server);

console.log("Trying to start server with config:", config.serverip + ":" + config.serverport);

// Both port and ip are needed for the OpenShift, otherwise it tries
// to bind server on IP 0.0.0.0 (or something) and fails
server.listen(config.serverport, config.serverip, function() {
  console.log("Server running @ http://" + config.serverip + ":" + config.serverport);
});

// Allow some files to be sent to the server over HTTP
app.use(express.static(__dirname + '/'));

// Serve GET on http://domain/
app.get('/', function (req, res) {
  res.sendFile(__dirname + '/index.html');
});

// Server GET on http://domain/api/config
// A hack to provide client the system config
app.get('/api/config', function(req, res) {
  res.send('var config = ' + JSON.stringify(config));
});

var clients = [];
var matches = [];
var oldPackets = [];
var packets = [];
var players = [];

io.on('connection', function (socket) {
    clients.push(socket);

    socket.emit('hello', { greeting: 'Your socket id is: ' + socket.id });

    // Match players with the next person connected
    var hitMatch = false;
    for(var i = 0; i < matches.length; i++) {
        if(matches[i][1] == -1) {
            hitMatch = true;
            matches[i][1] = clients.length - 1;
            // send the id of the opponent to each client
            socket.emit("match", {matchIndex: i, otherIndex: 0});
            clients[matches[i][0]].emit("match", {matchIndex: i, otherIndex: 1});
            var netPlayer0 = {};
            // InitNetPlayer(netPlayer0, 1366 / 3 -gPlayerSideLen 2);
            var netPlayer1 = {};
            // InitNetPlayer(netPlayer1, 1366 * 2 / 3 -gPlayerSideLen 2);
            players[i][0] = netPlayer0;
            players[i][1] = netPlayer1;
        }
    }
    if(!hitMatch) {
        var newEntry = [
            clients.length - 1,
            -1
        ];
        matches.push(newEntry);
        var newPacketArray = [[], []];
        packets.push(newPacketArray);
        var newOldPacketArray = [[], []];
        oldPackets.push(newOldPacketArray);
        var newPlayersArray = [[], []];
        players.push(newPlayersArray);
        // send -1 to let them know they're waiting for a match
        socket.emit("match", {otherIndex: -1});
    }

    // socket.on("pos", function (data) {
    //     if(data.matchIndex >= 0 && data.matchIndex < matches.length && (data.otherIndex == 0 || data.otherIndex == 1) && matches[data.matchIndex][data.otherIndex] >= 0 && matches[data.matchIndex][data.otherIndex] < clients.length) {
    //         clients[matches[data.matchIndex][data.otherIndex]].emit("pos", {x: data.x, y: data.y});
    //     }
    // });

    socket.on("update", function (data) {
        if(data.matchIndex >= 0 && data.matchIndex < matches.length && (data.otherIndex == 0 || data.otherIndex == 1) && matches[data.matchIndex][data.otherIndex] >= 0 && matches[data.matchIndex][data.otherIndex] < clients.length) {
            // packets[data.matchIndex][data.otherIndex].push(data);
            clients[matches[data.matchIndex][data.otherIndex]].emit("update", data);
        }
    });

    socket.on("textmessage", function (data) {
        if(data.matchIndex >= 0 && data.matchIndex < matches.length && (data.otherIndex == 0 || data.otherIndex == 1) && matches[data.matchIndex][data.otherIndex] >= 0 && matches[data.matchIndex][data.otherIndex] < clients.length) {
            clients[matches[data.matchIndex][data.otherIndex]].emit("textmessage", {msg: data.msg});
        }
    });

    socket.on("disconnect", function () {
        // TODO: remove clients
        // TODO: if now waiting, match with other waiting ppl
        // free up match, put waiting player in position 0 of match array
        for(var i = 0; i < matches.length; i++) {
            if(clients[matches[i][0]].id == socket.id) {
                // if a waiting player disconnects, remove that element from matches
                if(matches[i][1] == -1) {
                    matches.splice(i, 1);
                    packets.splice(i, 1);
                    oldPackets.splice(i, 1);
                    players.splice(i, 1);
                    break;
                }
                else {
                    matches[i][0] = matches[i][1];
                    clients[matches[i][0]].emit("match", {otherIndex: -1});
                    matches[i][1] = -1;
                    packets[i] = [[], []];
                    oldPackets[i] = [[], []];
                    players[i] = [[], []];
                    break;
                }
            }
            else if(clients[matches[i][1]].id == socket.id) {
                clients[matches[i][0]].emit("match", {otherIndex: -1});
                matches[i][1] = -1;
                packets[i] = [[], []];
                oldPackets[i] = [[], []];
                players[i] = [[], []];
                break;
            }
        }
    });

    socket.on('ping', function (data) {
        console.log("received ping from client: ", data);
        if(data.msg) {
            socket.emit('pong', { msg: data.msg });
        }
    });
});

function packetSortFunc(a, b) {
    return a.time - b.time;
}

var gPlayerMoveSpeed = 0.5; // pixels per millisecond
var gPlayerDodgeDuration = 150; // milliseconds
var gPlayerDodgeCooldown = 350; // milliseconds
var gPlayerDodgeDist = 150; // pixels per millisecond
var gPlayerAttackDuration = 200; // milliseconds
var gPlayerAttackCooldown = 200; // milliseconds
var gPlayerSideLen = 26;

// function Update() {
//     for(var i = 0; i < packets.length; i++) {
//         var packets0 = packets[i][0];
//         packets0.sort(packetSortFunc);
//         for(var j = 0; j < packets0.length; j++) {
//             var packet = packets0[j];
//             var player = players[i][0];
//
//             if(packet.id == 0) {
//                 if(packet.dir != 0) {
//                     player.wasMoving = true;
//                     player.moveTime = packet.time;
//                     player.moveStartX = player.x;
//                     player.movingDirection = packet.dir;
//                 }
//                 else {
//                     if(player.wasMoving) {
//                         var deltaTime = packet.time - player.moveTime;
//                         player.x = player.moveStartX + gPlayerMoveSpeed * deltaTime;
//                         player.wasMoving = false;
//                     }
//                 }
//             }
//             else if(packet.id == 1) {
//                 if(packet.dir == -1) {
//                     player.isDodging = true;
//                     player.dodgeTime = packet.time;
//                     player.dodgeStartX = player.x;
//                     player.dodgeTargetX = player.x - gPlayerDodgeDist;
//                 }
//                 else if(packet.dir == 1) {
//                     player.isDodging = true;
//                     player.dodgeTime = packet.time;
//                     player.dodgeStartX = player.x;
//                     player.dodgeTargetX = player.x + gPlayerDodgeDist;
//                 }
//                 else if(packet.dir == 0) {
//                     player.isDodging = true;
//                     player.dodgeTime = packet.time;
//                     player.dodgeStartX = player.x;
//                     player.dodgeTargetX = player.x;
//                 }
//                 else {
//                     player.isDodging = false;
//                     player.x = player.dodgeTargetX;
//                 }
//             }
//             else if(packet.id == 2) {
//
//             }
//             else if(packet.id == 3) {
//
//             }
//             else if(packet.id == 4) {
//
//             }
//             else if(packet.id == 5) {
//
//             }
//
//             oldPackets[i][0].push(packet);
//             packets[i][0].splice(i, 1);
//         }
//         var packets1 = packets[i][1];
//         packets1.sort(packetSortFunc);
//         for(var j = 0; j < packets1.length; j++) {
//             var packet = packets1[j];
//             var player = players[i][1];
//
//             oldPackets[i][1].push(packet);
//             packets[i][0].splice(i, 1);
//         }
//     }
// }
//
// setInterval(Update, 120);
//
// function InitNetPlayer(netPlayer, x) {
//     netPlayer.x = x;
//
//     // movement
//     netPlayer.wasMoving = false;
//     netPlayer.moveStartX = 0;
//     netPlayer.moveTime = 0;
//     netPlayer.facingDirection = 1; // -1 == left, 1 == right
//     netPlayer.movingDirection = 1; // -1 == left, 1 == right
//
//     // dodging
//     netPlayer.isDodging = false;
//     netPlayer.dodgeTime = 0;
//     netPlayer.dodgeStartX = 0;
//     netPlayer.dodgeTargetX = 0;
//     netPlayer.dodgeDirection = 0; // 0 == in place, -1 == left, 1 == right
//
//     // attacks
//     netPlayer.attackTime = 0;
//     netPlayer.isSlashing = false;
//     netPlayer.isCounteringSlash = false;
//     netPlayer.isStabbing = false;
//     netPlayer.isCounteringStab = false;
//     netPlayer.isThrowingShuriken = false;
//     netPlayer.isCounteringShuriken = false;
// }
