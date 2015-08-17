package  
{
	/**
	 * ...
	 * @author Beast Mode Entertainment
	 */
	public class Updates 
	{
		public function Rand(srand:int):int
		{
			return (uint)((srand * 1103515245 +12345) / 536) % 32768;
		}
		public function CircCollision(x1:int, y1:int, rad1:int, x2:int, y2:int, rad2:int):Boolean
		{
			if( Math.sqrt( (Number)(x1 - x2)*(x1 - x2)
					+ (Number)(y1 - y2)*(y1 - y2) ) < rad1 + rad2 )
			{
				return true;
			}
			return false;
		}
	}
}