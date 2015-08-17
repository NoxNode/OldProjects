package Entities 
{
	/**
	 * ...
	 * @author Beast Mode Entertainment
	 */
	import Entities.GameSprite;
	import flash.display.Sprite;
	import flash.geom.Matrix;
	import flash.geom.Point;
	import Input;
	
	public class Player extends GameSprite 
	{
		public var speed:int;
		public var input:Input;
		public var lives:int;
		public var hit:Boolean;
		public var teles:int;
		public var star:Boolean;
		public var dead:Boolean;
		public var rev:Boolean;
		public var rev_time:int;
		
		public function Player(x:int, y:int,W_AND_H:int)
		{
			super(x, y);
			input = new Input();
			image_sprite = new Sprite();
			
			//face
			image_sprite.graphics.beginFill(0xFED034);
			image_sprite.graphics.drawCircle(W_AND_H / 2, W_AND_H / 2, W_AND_H / 2);
			image_sprite.graphics.endFill();
			
			//shades
			image_sprite.graphics.lineStyle(1, 0x000000, 1, true);
			image_sprite.graphics.moveTo(0, 6);
			image_sprite.graphics.lineTo(19, 6);
			
			image_sprite.graphics.beginFill(0x000000);
			image_sprite.graphics.drawRect(4, 4, 4, 4);
			image_sprite.graphics.endFill();
			
			image_sprite.graphics.beginFill(0x000000);
			image_sprite.graphics.drawRect(7+4, 4, 4, 4);
			image_sprite.graphics.endFill();
			
			//mouth
			image_sprite.graphics.lineStyle(1, 0x000000, 1, true);
			image_sprite.graphics.moveTo(6, 13);
			image_sprite.graphics.lineTo(13, 13);
		}
		public function Render():void
		{
			var matrix:Matrix = new Matrix();
			matrix.translate(x, y);
			Game.Renderer.draw(image_sprite, matrix);
		}
		public function Update(arrow_keys:Boolean,WASD:Boolean,W_AND_H:int,x:int,y:int):Point 
		{
			var moved:Boolean = false;
			if ( arrow_keys )
			{
				if (input.CheckKeyDown(input.LEFT))
				{
					moved = true;
					x -= speed;
				}
				
				if (input.CheckKeyDown(input.RIGHT))
				{
					moved = true;
					x += speed;
				}
				
				if (input.CheckKeyDown(input.UP))
				{
					moved = true;
					y -= speed;
				}
				
				if (input.CheckKeyDown(input.DOWN))
				{
					moved = true;
					y += speed;
				}
			}
			if ( WASD && !(arrow_keys && moved) )
			{
				if (input.CheckKeyDown('A'.charCodeAt(0)))
					x -= speed;
				
				if (input.CheckKeyDown('D'.charCodeAt(0)))
					x += speed;
				
				if (input.CheckKeyDown('W'.charCodeAt(0)))
					y -= speed;
				
				if (input.CheckKeyDown('S'.charCodeAt(0)))
					y += speed;
			}
			
			if (x < 0)
				x = 0;
			else if(x + W_AND_H > Game.Renderer.width)
				x = Game.Renderer.width - W_AND_H;
			
			if (y < 0)
				y = 0;
			else if(y + W_AND_H > Game.Renderer.height)
				y = Game.Renderer.height - W_AND_H;
			return new Point(x, y);
		}
	}

}