package Entities 
{
	/**
	 * ...
	 * @author Beast Mode Entertainment
	 */
	import Entities.GameSprite;
	
	public class Items extends GameSprite 
	{
		public var collected:Boolean;
		public var time:int;
		
		public function Items(IRANDX:int,IRANDY:int,srand:int,W_AND_H:int) 
		{
			update = new Updates;
			super(update.Rand(IRANDX*srand)%(Game.Renderer.width-W_AND_H), update.Rand(IRANDY*srand)%(Game.Renderer.height-W_AND_H));
			collected = false;
			time = 0;
		}
		
	}

}