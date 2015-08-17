package Entities
{
	import flash.display.Sprite;
	import Updates;

	public class GameSprite
	{
		public var x:int;
		public var y:int;
		
		public var update:Updates;

		public var image_sprite:Sprite;

		public function GameSprite(x:int, y:int)
		{
			this.x = x;
			this.y = y;
		}
	}
}