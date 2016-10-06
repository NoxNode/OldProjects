function Shuriken(x, y, throwTime, throwDirection) {
    this.x = x;
    this.y = y;
    this.width = gPlayerShurikenSideLen;
    this.height = gPlayerShurikenSideLen;
    this.color = 0;

    this.startX = x;
    this.throwTime = throwTime;
    this.throwDirection = throwDirection;

    AddRect(this);
    AddUpdateFunc(this, UpdateShuriken);
}
