function NetPlayerMove() {
    var player = this.parentObj;
    var now = Date.now();
    MovePlayer(player, now, player.inputDelay);
}
