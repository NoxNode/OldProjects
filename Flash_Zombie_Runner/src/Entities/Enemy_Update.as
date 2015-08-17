package Entities 
{
	/**
	 * ...
	 * @author Beast Mode Entertainment
	 */
	
	import Entities.Enemy;
	
	public class Enemy_Update 
	{
		private var update:Updates;
		private var enemy:Enemy;
		
		public function Update(enemy:Enemy,playerx:int, playery:int,W_AND_H:int):Enemy 
		{
			//enemy = new Enemy(new_enemy.x, new_enemy.y, new_enemy.xspeed, new_enemy.yspeed);
			
			enemy.x += enemy.xspeed;
			enemy.y += enemy.yspeed;
			
			if (enemy.x < 0)
			{
				enemy.x = 0;
				enemy.xspeed = -enemy.xspeed;
			}
			if (enemy.x + W_AND_H > Game.Renderer.width)
			{
				enemy.x = Game.Renderer.width - W_AND_H;
				enemy.xspeed = -enemy.xspeed;
			}
			
			if (enemy.y < 0)
			{
				enemy.y = 0;
				enemy.yspeed = -enemy.yspeed;
			}
			if (enemy.y + W_AND_H > Game.Renderer.height)
			{
				enemy.y = Game.Renderer.height - W_AND_H;
				enemy.yspeed = -enemy.yspeed;
			}
			return enemy;
		}
		
	}

}