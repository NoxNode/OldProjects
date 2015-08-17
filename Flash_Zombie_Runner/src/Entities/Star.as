package Entities 
{
	/**
	 * ...
	 * @author Beast Mode Entertainment
	 */
	import Entities.Items;
	import flash.display.Sprite;
	import flash.geom.Matrix;
	
	public class Star extends Items 
	{
		public function Star(IRANDX:int,IRANDY:int,srand:int,W_AND_H:int) 
		{
			super(IRANDX,IRANDY,srand,W_AND_H);
			image_sprite = new Sprite();
			
			image_sprite.graphics.lineStyle(1, 0xFFF200, 1, true);
			image_sprite.graphics.beginFill(0xFFF200);
			image_sprite.graphics.moveTo(9, 0);
			image_sprite.graphics.lineTo(7, 6);
			image_sprite.graphics.lineTo(0, 7);
			image_sprite.graphics.lineTo(5, 13);
			image_sprite.graphics.lineTo(2, 19);
			image_sprite.graphics.lineTo(10, 16);
			image_sprite.graphics.lineTo(17, 19);
			image_sprite.graphics.lineTo(15, 12);
			image_sprite.graphics.lineTo(19, 7);
			image_sprite.graphics.lineTo(12, 6);
			image_sprite.graphics.lineTo(10, 0);
			image_sprite.graphics.endFill();
		}
		public function Render():void
		{
			var matrix:Matrix = new Matrix();
			matrix.translate(x, y);
			Game.Renderer.draw(image_sprite, matrix);
		}	
	}

}