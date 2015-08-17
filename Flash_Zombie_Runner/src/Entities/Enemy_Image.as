package Entities 
{
	/**
	 * ...
	 * @author Beast Mode Entertainment
	 */
	import flash.geom.Matrix;
	import flash.display.Sprite;
	
	public class Enemy_Image
	{
		private var image_sprite:Sprite;
		
		public function Enemy_Image(W_AND_H:int) 
		{
			image_sprite = new Sprite();
			
			//face
			image_sprite.graphics.beginFill(0x6BB51A);
			image_sprite.graphics.drawCircle(W_AND_H / 2, W_AND_H / 2, W_AND_H / 2);
			image_sprite.graphics.endFill();
			
			//left eye
			image_sprite.graphics.beginFill(0xE8F080);
			image_sprite.graphics.lineStyle(1, 0x000000,1,true);
			image_sprite.graphics.moveTo(5, 4);
			image_sprite.graphics.lineTo(8, 4);
			image_sprite.graphics.lineTo(8, 8);
			image_sprite.graphics.lineTo(4, 8);
			image_sprite.graphics.lineTo(4, 5);
			image_sprite.graphics.endFill();
			image_sprite.graphics.lineStyle(1, 0xED1C24,1,true);
			image_sprite.graphics.moveTo(7, 5);
			image_sprite.graphics.lineTo(7, 6);
			
			//right eye
			image_sprite.graphics.beginFill(0xE8F080);
			image_sprite.graphics.lineStyle(1, 0x000000,1,true);
			image_sprite.graphics.moveTo(7+5, 4);
			image_sprite.graphics.lineTo(7+8, 4);
			image_sprite.graphics.lineTo(7+8, 8);
			image_sprite.graphics.lineTo(7+4, 8);
			image_sprite.graphics.lineTo(7+4, 5);
			image_sprite.graphics.endFill();
			image_sprite.graphics.lineStyle(1, 0xED1C24,1,true);
			image_sprite.graphics.moveTo(7+7, 5);
			image_sprite.graphics.lineTo(7+7, 6);
			
			//mouth
			
			image_sprite.graphics.beginFill(0xA349A4);
			
			image_sprite.graphics.lineStyle(1, 0x000000,1,true);
			image_sprite.graphics.moveTo(5, 14);
			image_sprite.graphics.lineTo(5, 17);
			image_sprite.graphics.lineTo(13, 17);
			image_sprite.graphics.lineTo(15, 15);
			image_sprite.graphics.lineTo(16, 15);
			image_sprite.graphics.lineTo(16, 13);
			image_sprite.graphics.lineTo(15, 12);
			image_sprite.graphics.lineTo(14, 12);
			image_sprite.graphics.lineTo(13, 11);
			image_sprite.graphics.lineTo(9, 11);
			image_sprite.graphics.lineTo(7, 13);
			image_sprite.graphics.lineTo(6, 13);
			
			image_sprite.graphics.endFill();
			
			image_sprite.graphics.beginFill(0xFFF200);
			image_sprite.graphics.drawRect(9, 13, 4, 3);
			image_sprite.graphics.endFill();
			
			image_sprite.graphics.lineStyle(1, 0x000000,1,true);
			image_sprite.graphics.moveTo(11, 14);
			image_sprite.graphics.lineTo(11, 15);
			
			//blood
			image_sprite.graphics.lineStyle(1, 0xD70000,1,true);
			image_sprite.graphics.moveTo(5, 17);
			image_sprite.graphics.lineTo(5, 18);
			
			image_sprite.graphics.moveTo(8, 18);
			image_sprite.graphics.lineTo(8, 19);
			
			image_sprite.graphics.moveTo(11, 18);
			image_sprite.graphics.lineTo(11, 19);
			
			image_sprite.graphics.moveTo(14, 17);
			image_sprite.graphics.lineTo(14, 18);
			
			//end drawing
		}
		public function Render(x:int,y:int):void
		{
			var matrix:Matrix = new Matrix();
			matrix.translate(x, y);
			Game.Renderer.draw(image_sprite, matrix);
		}
		
	}

}