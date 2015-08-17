package Entities 
{
	/**
	 * ...
	 * @author Beast Mode Entertainment
	 */
	import Entities.GameSprite;
	
	public class Enemy
	{
		public var xspeed:int, yspeed:int, x:int, y:int;
		
		public function Enemy(x:int,y:int,xspeed:int,yspeed:int )
		{
			this.x = x;
			this.y = y;
			this.xspeed = xspeed;
			this.yspeed = yspeed;
		}
		
	}

}