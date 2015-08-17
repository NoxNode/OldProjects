package Entities 
{
	/**
	 * ...
	 * @author Beast Mode Entertainment
	 */
	import Entities.Items;
	import flash.display.Sprite;
	import flash.geom.Matrix;
	
	public class Teleport extends Items 
	{
		public function Teleport(IRANDX:int,IRANDY:int,srand:int,W_AND_H:int) 
		{
			super(IRANDX,IRANDY,srand,W_AND_H);
			image_sprite = new Sprite();
			
			image_sprite.graphics.lineStyle(1, 0x3F48CC, 1, true);
			image_sprite.graphics.drawRect(0, 0, 20, 20);
			image_sprite.graphics.moveTo(2, 3);
			image_sprite.graphics.lineTo(2, 5);
			image_sprite.graphics.lineTo(3, 5);
			image_sprite.graphics.lineTo(3, 6);
			image_sprite.graphics.lineTo(5, 6);
			image_sprite.graphics.lineTo(5, 5);
			image_sprite.graphics.lineTo(6, 5);
			image_sprite.graphics.lineTo(6, 3);
			image_sprite.graphics.lineTo(2, 3);
			
			image_sprite.graphics.moveTo(16, 10);
			image_sprite.graphics.lineTo(12, 10);
			image_sprite.graphics.lineTo(9, 12);
			image_sprite.graphics.lineTo(9, 15);
			image_sprite.graphics.lineTo(12, 17);
			image_sprite.graphics.lineTo(15, 17);
			image_sprite.graphics.lineTo(16, 15);
			image_sprite.graphics.lineTo(16, 10);
			
			image_sprite.graphics.lineStyle(1, 0xFFF317, 1, true);
			image_sprite.graphics.drawRect(12, 8, 1, 1);
			image_sprite.graphics.drawRect(18, 11, 1, 1);
			image_sprite.graphics.drawRect(6, 15, 1, 1);
			image_sprite.graphics.drawRect(9, 17, 1, 1);
			image_sprite.graphics.drawRect(17, 18, 1, 1);
		}
		public function Render():void
		{
			var matrix:Matrix = new Matrix();
			matrix.translate(x, y);
			Game.Renderer.draw(image_sprite, matrix);
		}
		
	}

}