package  
{
	import flash.geom.Point;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	/**
	 * ...
	 * @author Beast Mode Entertainment
	 */
	public class Input 
	{
		private var keys_down:Array;
		private var last_key_down:int;
		private var keys_up:Array;
		
		public const LEFT:int = 37;
		public const UP:int = 38;
		public const RIGHT:int = 39;
		public const DOWN:int = 40;
		
		public const SPACE:int = 32;
		public const ESC:int = 27;
		
		public var mouse_down:Boolean;
		public var mouse_click:Boolean;
		public var mouse_pos:Point;
		
		public function Input() 
		{
			keys_down = new Array();
			last_key_down = -1;
			keys_up = new Array();
			
			mouse_click = false;
			mouse_down = false;
			mouse_pos = new Point(0, 0);
		}
		public function Refresh():void
		{
			mouse_click = false;
			//clear out the "keys_up" array for next update
			keys_up = new Array();
		}
		public function KeyUp(e:KeyboardEvent):void
		{
			//position of key in the array
			var key_pos:int = -1;
			for (var i:int = 0; i < keys_down.length; i++)
				if (e.keyCode == keys_down[i])
				{
					//the key is found/was pressed before, so store the position
					key_pos = i;
					break;
				}
			//remove the keycode from keys_down if found 
			if(key_pos!=-1)
				keys_down.splice(key_pos, 1);		
				
			keys_up.push(e.keyCode);
		}
		
		public function KeyDown(e:KeyboardEvent):void
		{
			//check to see if the key that is being pressed is already in the array of pressed keys
			var key_down:Boolean = false;
			for (var i:int = 0; i < keys_down.length; i++)
				if (keys_down[i] == e.keyCode)
					key_down = true;
			
			//add the key to the array of pressed keys if it wasn't already in there
			if (!key_down)
				keys_down.push(e.keyCode);			
		}
		
		public function CheckKeyDown(keycode:int):Boolean
		{
			for (var i:int = 0; i < keys_down.length; i++)
				if (keys_down[i] == keycode)
				{
					return true;
					break;
				}
			return false;
		}
		public function CheckKeyUp(keycode:int):Boolean
		{
			for (var i:int = 0; i < keys_up.length; i++)
				if (keys_up[i] == keycode)
				{
					return true;
					break;
				}
			return false;
		}
		public function MoveMouse(e:MouseEvent):void 
		{
			mouse_pos.x = e.stageX;
			mouse_pos.y = e.stageY;
		}
		public function MouseDown(e:MouseEvent):void
		{
			mouse_down = true;
		}
		public function MouseUp(e:MouseEvent):void
		{
			mouse_down = false;
			mouse_click = true;
		}
		public function GetNewKeyDown():int
		{
			if ( last_key_down == keys_down[0] )
			{
				return -1;
			}
			else
			{
				last_key_down = keys_down[0];
				return keys_down[0];
			}
		}
	}

}