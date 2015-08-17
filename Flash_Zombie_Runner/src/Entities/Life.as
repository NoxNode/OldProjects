package Entities 
{
	/**
	 * ...
	 * @author Beast Mode Entertainment
	 */
	import Entities.Items;
	import flash.display.Sprite;
	import flash.geom.Matrix;
	
	public class Life extends Items 
	{
		public function Life(IRANDX:int,IRANDY:int,srand:int,W_AND_H:int) 
		{
			super(IRANDX,IRANDY,srand,W_AND_H);
			image_sprite = new Sprite();
			
			image_sprite.graphics.lineStyle(1, 0x1CDD00, 1, true);
			image_sprite.graphics.beginFill(0x1CDD00);
			image_sprite.graphics.moveTo(9, 0);
			image_sprite.graphics.lineTo(0, 9);
			image_sprite.graphics.lineTo(4, 9);
			image_sprite.graphics.lineTo(4, 19);
			image_sprite.graphics.lineTo(15, 19);
			image_sprite.graphics.lineTo(15, 9);
			image_sprite.graphics.lineTo(19, 9);
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