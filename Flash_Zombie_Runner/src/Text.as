package  
{
	/**
	 * ...
	 * @author Beast Mode Entertainment
	 */
	import flash.text.TextField;
	import flash.text.TextFormat;
	
	public class Text 
	{
		public var game_over_txt:TextField;
		public var score_txt:TextField;
		public var lives_txt:TextField;
		public var menu:TextField;
		public var pause:TextField;
		public var tele_txt:TextField;
		public var star_txt:TextField;
		public var coming_soon:TextField;
		public var settings:TextField;
		public var settings_enter:TextField;
		
		public function Text() 
		{
			var format:TextFormat = new TextFormat("Arial", 20, 0xFFFFFF, true);
			format.align = "center";
			
			var format2:TextFormat = new TextFormat("Arial", 20, 0xFFFFFF, true);
			format2.align = "left";
			
			var format3:TextFormat = new TextFormat("Arial", 20, 0xFFFFFF, true);
			format3.align = "right";
			
			score_txt = new TextField();
			score_txt.width = Game.Renderer.width;
			score_txt.x = 20;
			score_txt.y = 10;
			score_txt.height = Game.Renderer.height-11;
			score_txt.defaultTextFormat = format2;
			
			lives_txt = new TextField();
			lives_txt.width = Game.Renderer.width;
			lives_txt.x = 20;
			lives_txt.y = Game.Renderer.height-40;
			lives_txt.defaultTextFormat = format2;
			
			tele_txt = new TextField();
			tele_txt.width = Game.Renderer.width-20;
			tele_txt.x = 0;
			tele_txt.y = Game.Renderer.height-40;
			tele_txt.defaultTextFormat = format3;
			
			star_txt = new TextField();
			star_txt.width = Game.Renderer.width-20;
			star_txt.x = 0;
			star_txt.y = 10;
			star_txt.defaultTextFormat = format3;
			
			game_over_txt = new TextField();
			game_over_txt.width = Game.Renderer.width;
			game_over_txt.x = 0;
			game_over_txt.y = Game.Renderer.height / 2 - 20;
			game_over_txt.defaultTextFormat = format;
			game_over_txt.text = "Game Over Man!\nPress space to restart\nPress B to go back to the menu";
			
			menu = new TextField();
			menu.width = Game.Renderer.width;
			menu.height = 200
			menu.x = 0;
			menu.y = 100;
			menu.defaultTextFormat = format;
			menu.text = "Zombie Runner\n\nPress 1 for 1 player\nPress 2 for 2 player\nPress 3 for online 2 player\nPress 4 for 2 player with AI\nPress 5 for settings";
			
			pause = new TextField();
			pause.width = Game.Renderer.width;
			pause.x = 0;
			pause.y = 100;
			pause.defaultTextFormat = format;
			pause.text = "Paused\nPress space to resume\nPress B to go back to the menu";
			
			coming_soon = new TextField();
			coming_soon.width = Game.Renderer.width;
			coming_soon.x = 0;
			coming_soon.y = 100;
			coming_soon.defaultTextFormat = format;
			coming_soon.text = "Coming soon\nPress B to return to the menu";
			
			settings = new TextField();
			settings.width = Game.Renderer.width;
			settings.x = 0;
			settings.y = 75;
			settings.height = Game.Renderer.height-76;
			settings.defaultTextFormat = format;
			
			settings_enter = new TextField();
			settings_enter.width = Game.Renderer.width;
			settings_enter.x = 0;
			settings_enter.y = 100;
			settings_enter.defaultTextFormat = format;
			settings_enter.text = "Enter a 3 digit number\nThen press space";
		}
		
	}

}