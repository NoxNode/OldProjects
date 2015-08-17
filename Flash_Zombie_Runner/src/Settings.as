package  
{
	/**
	 * ...
	 * @author Beast Mode Entertainment
	 */
	public class Settings 
	{
		public var SPEED:int;
		public var LIVES_LEVEL:int;
		public var LIVES_TIME:int;
		public var TELE_LEVEL:int;
		public var TELE_TIME:int;
		public var STAR_LEVEL:int;
		public var STAR_TIME:int;
		public var STAR_ITIME:int;
		public var BACK_COLOR:uint;
		
		public function Settings()
		{
			SPEED = 5;
			LIVES_LEVEL = 5;
			LIVES_TIME = 90;
			TELE_LEVEL = 10;
			TELE_TIME = 90;
			STAR_LEVEL = 15;
			STAR_TIME = 60;
			STAR_ITIME = 300;
			BACK_COLOR = 0x000000;
		}
	
	}

}