function NetPlayerAttack() {
    var player = this.parentObj;
    var now = Date.now();
    AnimateAttacks(player, now, player.inputDelay);
}
