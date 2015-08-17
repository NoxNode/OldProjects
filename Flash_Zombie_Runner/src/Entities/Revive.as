package Entities 
{
	/**
	 * ...
	 * @author Beast Mode Entertainment
	 */
	import Entities.GameSprite;
	import flash.display.Sprite;
	import flash.geom.Matrix;
	
	public class Revive extends GameSprite 
	{	
		public function Revive(IRANDX:int,IRANDY:int,srand:int,W_AND_H:int)
		{
			update = new Updates;
			super(update.Rand(IRANDX * srand) % (Game.Renderer.width - W_AND_H), update.Rand(IRANDY * srand) % (Game.Renderer.height - W_AND_H));
			image_sprite = new Sprite();
			
			//draw
			image_sprite.graphics.beginFill(0x3F48CC);
			image_sprite.graphics.drawCircle(W_AND_H / 2, W_AND_H / 2, W_AND_H / 2);
			image_sprite.graphics.endFill();
			
			image_sprite.graphics.lineStyle(1, 0x000000, 1, true);
			image_sprite.graphics.moveTo(9, 4);
			image_sprite.graphics.lineTo(9, 15);
			image_sprite.graphics.lineTo(10, 15);
			image_sprite.graphics.lineTo(10, 4);
			image_sprite.graphics.moveTo(4, 9);
			image_sprite.graphics.lineTo(15, 9);
			image_sprite.graphics.lineTo(15, 10);
			image_sprite.graphics.lineTo(4, 10);
		}
		public function Render():void
		{
			var matrix:Matrix = new Matrix();
			matrix.translate(x, y);
			Game.Renderer.draw(image_sprite, matrix);
		}
		
	}

}