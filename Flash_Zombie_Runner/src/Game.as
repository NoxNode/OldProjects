package 
{
	/**
	 * ...
	 * @author Beast Mode Entertainment
	 */
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	import flash.geom.Matrix;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.net.SharedObject;
	/****************	Start Game Specific Import Code	****************/
	
	import Entities.Revive;
	import Entities.Player;
	import Entities.Enemy;
	import Entities.Enemy_Update;
	import Entities.Enemy_Image;
	import Entities.Coin;
	import Entities.Life;
	import Entities.Teleport;
	import Entities.Star;
	import Updates;
	import Text;
	import Settings;
	
	/****************	End Game Specific Import Code	****************/
	public class Game
	{
		//Variable Declarations
		public var bitmap:Bitmap;
		public static var Renderer:BitmapData;
		private var update:Updates;
		
		public var STAGEWIDTH:int;
		public var STAGEHEIGHT:int;
		/****************	Start Game Specific Variable Declarations Code	****************/
		
		//General
		private const W_AND_H:int = 20;
		
		private var e_highscore:int;
		private var n_highscore:int;
		private var h_highscore:int;
		private var x_highscore:int;
		private var c_highscore:int;
		private var save:SharedObject;
		
		private var state:int;
		
		private const START:int = 0;
		private const MENU:int = 1;
		private const GAME:int = 2;
		private const PAUSED:int = 3;
		private const SETTINGS:int = 4;
		private const SETTINGS_ENTER:int = 5;
		private const GAMEOVER:int = 6;
		private var TwoP:Boolean;
		private var AI:Boolean;
		private var Online:Boolean;
		
		private var s_num:int;
		private var s_digit:int;
		private var setting_state:int;
		
		private var settings:Settings;
		
		private var frames:int;
		
		private var text:Text;
		
		//Player
		public var player:Player;
		private var player2:Player;
		//Enemies
		private var nEnemies:int;
		private var enemies:Array;
		private var enemy:Enemy;
		private var enemy_image:Enemy_Image;
		private var enemy_update:Enemy_Update;
		//Coin
		private var coin:Coin;
		//Revive
		private var rev:Revive;
		//Lives
		private var lives:Life;
		private var i_hit:int;
		//Tele
		private var tele:Teleport;
		//Star
		private var star:Star;
		
		//Randomization - these are multiplied by frames to make different random numbers
		private const RCOINX:int = 1;
		private const RCOINY:int = 2;
		private const RENEMYSX:int = 3;
		private const RENEMYSY:int = 4;
		private const LOTTO:int = 5;
		private const RLIVESX:int = 6;
		private const RLIVESY:int = 7;
		private const RTELEX:int = 8;
		private const RTELEY:int = 9;
		private const RSTARX:int = 10;
		private const RSTARY:int = 11;
		private const RREVX:int = 12;
		private const RREVY:int = 13;
		
		//A global random # seed
		private var srand:int;
		
		/****************	End Game Specific Variable Declarations Code	****************/
		public function Game(stageWidth:int, stageHeight:int)
		{
			//Variable Initialization
			STAGEWIDTH = stageWidth;
			STAGEHEIGHT = stageHeight;
			Renderer = new BitmapData(STAGEWIDTH, STAGEHEIGHT, false, 0x000000);
			bitmap = new Bitmap(Renderer);
			
			update = new Updates;
			
			Initialize(true);
		}
		
		/**********************************************************************	Initialize	**********************************************************************/
		
		private function Initialize(firstrun:Boolean):void
		{
			//General
			frames = 1;
			nEnemies = 0;
			srand = 1;
			
			i_hit = -1;
			
			if ( firstrun )
			{
				text = new Text();
				state = MENU;
				//player
				TwoP = false;
				player = new Player(STAGEWIDTH / 2 - W_AND_H / 2, STAGEHEIGHT / 2 - W_AND_H / 2, W_AND_H);
				player2 = new Player(STAGEWIDTH / 2 - W_AND_H / 2, STAGEHEIGHT / 2 - W_AND_H / 2, W_AND_H);
				//Coin
				coin = new Coin(RCOINX, RCOINY, srand, W_AND_H);
				//Revive
				rev = new Revive(RREVX, RREVY, srand, W_AND_H);
				//Lives
				lives = new Life(RLIVESX, RLIVESY, srand, W_AND_H);
				//Tele
				tele = new Teleport(RTELEX, RTELEY, srand, W_AND_H);
				//Star
				star = new Star(RSTARX, RSTARY, srand, W_AND_H);
				
				AI = false;
				setting_state = -1;
				s_num = 0;
				s_digit = 100;
				
				save = SharedObject.getLocal("ZR_Save");
				e_highscore = save.data.e_highscore;
				n_highscore = save.data.n_highscore;
				h_highscore = save.data.h_highscore;
				x_highscore = save.data.x_highscore;
				c_highscore = save.data.c_highscore;
				settings = new Settings();
				if ( save.data.SPEED != null )
				{
					settings.SPEED = save.data.SPEED;
					settings.LIVES_LEVEL = save.data.LIVES_LEVEL;
					settings.LIVES_TIME = save.data.LIVES_TIME;
					settings.TELE_LEVEL = save.data.TELE_LEVEL;
					settings.TELE_TIME = save.data.TELE_TIME;
					settings.STAR_LEVEL = save.data.STAR_LEVEL;
					settings.STAR_TIME = save.data.STAR_TIME;
					settings.STAR_ITIME = save.data.STAR_ITIME;
					settings.BACK_COLOR = save.data.BACK_COLOR;
				}
				if ( save.data.e_highscore != null )
				{
					e_highscore = save.data.e_highscore;
					n_highscore = save.data.n_highscore;
					h_highscore = save.data.h_highscore;
					x_highscore = save.data.x_highscore;
					c_highscore = save.data.c_highscore;
				}
				
				text.settings.text = "Press a to change speed of game  " + settings.SPEED + "\nPress c to change how often lives appear  " + settings.LIVES_LEVEL + "\nPress d to change how long lives appear  " + settings.LIVES_TIME + "\nPress e to change how often teleports appear  " + settings.TELE_LEVEL + "\nPress f to change how long teleports appear  " + settings.TELE_TIME + "\nPress g to change how often stars appear  " + settings.STAR_LEVEL + "\nPress h to change how long stars appear  " + settings.STAR_TIME + "\nPress i to change how long invulnerability lasts  " + settings.STAR_ITIME + "\n\nPress 1,2,3 or 4 for easy, normal, hard, or extreme difficulty\nPress 6,7, 8, or 9 for red, green, blue or black background\nTime is in 60ths of a second\nPress R to reset all saved data\nPress B to return to menu";
			}
			
			if ( settings.SPEED == 4 && settings.LIVES_LEVEL == 4 && settings.LIVES_TIME == ((STAGEWIDTH/settings.SPEED)*3)/4 && settings.TELE_LEVEL == 8 && settings.TELE_TIME == STAGEWIDTH/settings.SPEED/2 && settings.STAR_LEVEL == 12 && settings.STAR_TIME == ((STAGEWIDTH/settings.SPEED)*3)/8 && settings.STAR_ITIME == 375 )
				text.score_txt.text = "Difficulty: Easy\nHighscore: " + e_highscore + "\nScore: " + nEnemies;
			else if ( settings.SPEED == 5 && settings.LIVES_LEVEL == 5 && settings.LIVES_TIME == ((STAGEWIDTH/settings.SPEED)*3)/4 && settings.TELE_LEVEL == 10 && settings.TELE_TIME == STAGEWIDTH/settings.SPEED/2 && settings.STAR_LEVEL == 15 && settings.STAR_TIME == ((STAGEWIDTH/settings.SPEED)*3)/8 && settings.STAR_ITIME == 300 )
				text.score_txt.text = "Difficulty: Normal\nHighscore: " + n_highscore + "\nScore: " + nEnemies;
			else if ( settings.SPEED == 6 && settings.LIVES_LEVEL == 6 && settings.LIVES_TIME == ((STAGEWIDTH/settings.SPEED)*3)/4 && settings.TELE_LEVEL == 12 && settings.TELE_TIME == STAGEWIDTH/settings.SPEED/2 && settings.STAR_LEVEL == 18 && settings.STAR_TIME == ((STAGEWIDTH/settings.SPEED)*3)/8 && settings.STAR_ITIME == 250 )
				text.score_txt.text = "Difficulty: Hard\nHighscore: " + h_highscore + "\nScore: " + nEnemies;
			else if ( settings.SPEED == 7 && settings.LIVES_LEVEL == 7 && settings.LIVES_TIME == ((STAGEWIDTH/settings.SPEED)*3)/4 && settings.TELE_LEVEL == 14 && settings.TELE_TIME == STAGEWIDTH/settings.SPEED/2 && settings.STAR_LEVEL == 21 && settings.STAR_TIME == ((STAGEWIDTH/settings.SPEED)*3)/8 && settings.STAR_ITIME == 214 )
				text.score_txt.text = "Difficulty: Extreme\nHighscore: " + x_highscore + "\nScore: " + nEnemies;
			else
				text.score_txt.text = "Difficulty: Custom\nHighscore: " + c_highscore + "\nScore: " + nEnemies;
			
			//Enemies
			enemy = new Enemy(0, 0, settings.SPEED, settings.SPEED);
			enemies = new Array();
			enemy_image = new Enemy_Image(W_AND_H);
			enemy_update = new Enemy_Update();
			//Player
			player.x = STAGEWIDTH / 2 - W_AND_H / 2;
			player.y = STAGEHEIGHT / 2 - W_AND_H / 2, W_AND_H;
			player.speed = settings.SPEED;
			player.lives = 0;
			player.hit = false;
			player.teles = 0;
			player.star = false;
			player.dead = false;
			player.rev = false;
			player.rev_time = 0;
			//Player2
			player2.x = STAGEWIDTH / 2 - W_AND_H / 2;
			player2.y = STAGEHEIGHT / 2 - W_AND_H / 2, W_AND_H;
			player2.speed = settings.SPEED;
			player2.lives = 0;
			player2.hit = false;
			player2.teles = 0;
			player2.star = false;
			player2.dead = false;
			player2.rev = false;
			player2.rev_time = 0;
			
			text.lives_txt.text = "Lives: " + player.lives;
			text.tele_txt.text = "Teleports: " + player.teles;
			text.star_txt.text = "Invulnerability: Off";
		}
		
		/**********************************************************************	Render	**********************************************************************/
		
		public function Render():void
		{
			
			Renderer.lock();
			Renderer.fillRect(new Rectangle(0, 0, Renderer.width, Renderer.height), settings.BACK_COLOR);
			/****************	Start Game Specific Render Code	****************/
			
			if ( state == GAME || state == PAUSED || state == GAMEOVER )
			{
				//Player
				if(!(player.rev && player.rev_time % 10 < 5) && !(player.star && star.time >= (settings.STAR_ITIME - 60) && star.time % 10 > 5))
					player.Render();
				if( TwoP && !(player2.rev && player2.rev_time % 10 < 5) && !(player.star && star.time >= (settings.STAR_ITIME - 60) && star.time % 10 > 5) )
					player2.Render();
				
				//Enemies
				for ( var i:int = 0; i < nEnemies; i++ )
				{
					enemy_image.Render(enemies[i].x, enemies[i].y);
				}
				
				//Coin
				coin.Render();
				
				//Revive
				if ( TwoP && (player.dead || player2.dead) )
				{
					rev.Render();
				}
				
				//Lives
				if ( nEnemies % settings.LIVES_LEVEL == 0 && nEnemies != 0 && !lives.collected )
					lives.Render();
				Renderer.draw(text.lives_txt, new Matrix(1, 0, 0, 1, text.lives_txt.x , text.lives_txt.y ));
				
				//Tele
				if ( (nEnemies % settings.TELE_LEVEL) == 0 && nEnemies != 0 && !tele.collected )
					tele.Render();
				Renderer.draw(text.tele_txt, new Matrix(1, 0, 0, 1, text.tele_txt.x , text.tele_txt.y ));
				
				//Star
				if ( nEnemies % settings.STAR_LEVEL == 0 && nEnemies != 0 && !star.collected )
					star.Render();
				Renderer.draw(text.star_txt, new Matrix(1, 0, 0, 1, text.star_txt.x , text.star_txt.y ));				
				
				//Score
				Renderer.draw(text.score_txt, new Matrix(1, 0, 0, 1, text.score_txt.x , text.score_txt.y ));
				//GameOver
				if ( state == GAMEOVER )
					Renderer.draw(text.game_over_txt, new Matrix(1, 0, 0, 1, text.game_over_txt.x , text.game_over_txt.y ));
			}
			if ( state == PAUSED )
				Renderer.draw(text.pause, new Matrix(1, 0, 0, 1, text.pause.x , text.pause.y ));
			if ( state == MENU && !(Online || AI))
				Renderer.draw(text.menu, new Matrix(1, 0, 0, 1, text.menu.x , text.menu.y ));
			if ( Online || AI )
				Renderer.draw(text.coming_soon, new Matrix(1, 0, 0, 1, text.coming_soon.x, text.coming_soon.y));
			if ( state == SETTINGS )
				Renderer.draw(text.settings, new Matrix(1, 0, 0, 1, text.settings.x, text.settings.y));
			if ( state == SETTINGS_ENTER )
				Renderer.draw(text.settings_enter, new Matrix(1, 0, 0, 1, text.settings_enter.x, text.settings_enter.y));
			
			/****************	End Game Specific Render Code	****************/
			Renderer.unlock();
		}
		
		/**********************************************************************	Update	**********************************************************************/
		
		public function Update():void
		{
			/****************	Start Game Specific Update Code	****************/
			
			if ( state == GAME )
			{
				var p:Point = new Point;
				//Player
				if ( !TwoP )
				{
					p = player.Update(true, true, W_AND_H,player.x,player.y);
					player.x = p.x;
					player.y = p.y;
				}
				if ( TwoP )
				{
					if ( !player.dead )
					{
						p = player.Update(true, false, W_AND_H,player.x,player.y);
						player.x = p.x;
						player.y = p.y;
					}
					if ( TwoP && !player2.dead )
					{
						p = player.Update(false, true, W_AND_H,player2.x,player2.y);
						player2.x = p.x;
						player2.y = p.y;
					}
				}
				
				//Revive
				if ( TwoP )
				{
					if ( player.dead && update.CircCollision(rev.x, rev.y, W_AND_H / 2, player2.x, player2.y, W_AND_H / 2) )
					{
						rev.x = update.Rand(RREVX * frames * srand) % (STAGEHEIGHT - W_AND_H);
						rev.y = update.Rand(RREVY * frames * srand) % (STAGEHEIGHT - W_AND_H);
						player.dead = false;
						player.rev = true;
					}
					if ( player2.dead && update.CircCollision(rev.x, rev.y, W_AND_H / 2, player.x, player.y, W_AND_H / 2) )
					{
						rev.x = update.Rand(RREVX * frames * srand) % (STAGEHEIGHT - W_AND_H);
						rev.y = update.Rand(RREVY * frames * srand) % (STAGEHEIGHT - W_AND_H);
						player2.dead = false;
						player2.rev = true;
					}
					
					if ( player.rev_time >= 60 )
					{
						player.rev_time = 0;
						player.rev = false;
					}
					if ( player2.rev_time >= 60 )
					{
						player2.rev_time = 0;
						player2.rev = false;
					}
						
					if ( player.rev == true )
						player.rev_time ++;
					if ( player2.rev == true )
						player2.rev_time ++;
				}
				//Lives
				if ((nEnemies % settings.LIVES_LEVEL) == 0 && nEnemies != 0 && !lives.collected && lives.time < settings.LIVES_TIME)
				{
					if ( update.CircCollision(lives.x, lives.y, W_AND_H / 2, player.x, player.y, W_AND_H / 2) || (TwoP && update.CircCollision(lives.x,lives.y,W_AND_H/2,player2.x,player2.y,W_AND_H/2)) )
					{
						player.lives++;
						lives.x = update.Rand(RLIVESX * frames * srand) % (STAGEWIDTH - W_AND_H);
						lives.y = update.Rand(RLIVESY * frames * srand) % (STAGEHEIGHT - W_AND_H);
						lives.collected = true;
						text.lives_txt.text = "Lives: " + player.lives;
					}
					else
						lives.time++;
				}
				if ( lives.time >= settings.LIVES_TIME )
				{
					lives.x = update.Rand(RLIVESX * frames * srand) % (STAGEWIDTH - W_AND_H);
					lives.y = update.Rand(RLIVESY * frames * srand) % (STAGEHEIGHT - W_AND_H);
					lives.collected = true;
				}
				//Tele
				if (nEnemies % settings.TELE_LEVEL == 0 && nEnemies != 0 && !tele.collected && tele.time < settings.TELE_TIME)
				{
					if ( update.CircCollision(tele.x, tele.y, W_AND_H / 2, player.x, player.y, W_AND_H / 2) || (TwoP && update.CircCollision(tele.x,tele.y,W_AND_H/2,player2.x,player2.y,W_AND_H/2)) )
					{
						player.teles++;
						tele.x = update.Rand(RTELEX * frames * srand) % (STAGEWIDTH - W_AND_H);
						tele.y = update.Rand(RTELEY * frames * srand) % (STAGEHEIGHT - W_AND_H);
						tele.collected = true;
						text.tele_txt.text = "Teleports: " + player.teles;
					}
					else
						tele.time++;
				}
				if ( tele.time >= settings.TELE_TIME )
				{
					tele.x = update.Rand(RTELEX * frames * srand) % (STAGEWIDTH - W_AND_H);
					tele.y = update.Rand(RTELEY * frames * srand) % (STAGEHEIGHT - W_AND_H);
					tele.collected = true;
				}
				if ( player.input.mouse_click && player.teles > 0 )
				{
					player.teles = player.teles - 1;
					text.tele_txt.text = "Teleports: " + player.teles;
					player.x = player.input.mouse_pos.x - W_AND_H / 2;
					player.y = player.input.mouse_pos.y - W_AND_H / 2;
					if ( TwoP )
					{
						player2.x = player.input.mouse_pos.x - W_AND_H / 2;
						player2.y = player.input.mouse_pos.y - W_AND_H / 2;
					}
					
					if (player.x < 0)
						player.x = 0;
					else if(player.x + W_AND_H > Renderer.width)
						player.x = Renderer.width - W_AND_H;
					
					if (player.y < 0)
						player.y = 0;
					else if(player.y + W_AND_H > Renderer.height)
						player.y = Renderer.height - W_AND_H;
				}
				//Star
				if (nEnemies % settings.STAR_LEVEL == 0 && nEnemies != 0 && !star.collected && star.time < settings.STAR_TIME)
				{
					if ( update.CircCollision(star.x, star.y, W_AND_H / 2, player.x, player.y, W_AND_H / 2) || (TwoP && update.CircCollision(star.x,star.y,W_AND_H/2,player2.x,player2.y,W_AND_H/2)) )
					{
						player.star = true;
						star.time = 0;
						star.x = update.Rand(RSTARX * frames * srand) % (STAGEWIDTH - W_AND_H);
						star.y = update.Rand(RSTARY * frames * srand) % (STAGEHEIGHT - W_AND_H);
						star.collected = true;
						text.star_txt.text = "Invulnerability: On";
					}
					else
						star.time++;
				}
				if ( !player.star && star.time >= settings.STAR_TIME )
				{
					star.x = update.Rand(RSTARX * frames * srand) % (STAGEWIDTH - W_AND_H);
					star.y = update.Rand(RSTARY * frames * srand) % (STAGEHEIGHT - W_AND_H);
					star.collected = true;
				}
				if (player.star && star.time < settings.STAR_ITIME)
				{
					star.time++;
					if (star.time > (settings.STAR_ITIME * 3) / 4)
						text.star_txt.text = "Invulnerability: Almost off";
				}
				if (player.star && star.time >= settings.STAR_ITIME)
				{
					text.star_txt.text = "Invulnerability: Off";
					player.star = false;
					star.time = 0;
				}
				//Coin
				if ( update.CircCollision(coin.x,coin.y,W_AND_H/2,player.x,player.y,W_AND_H/2) || (TwoP && update.CircCollision(coin.x,coin.y,W_AND_H/2,player2.x,player2.y,W_AND_H/2)) )
				{
					//Add an enemy
					nEnemies++;
					
					//Update score
					if ( settings.SPEED == 4 && settings.LIVES_LEVEL == 4 && settings.LIVES_TIME == ((STAGEWIDTH/settings.SPEED)*3)/4 && settings.TELE_LEVEL == 8 && settings.TELE_TIME == STAGEWIDTH/settings.SPEED/2 && settings.STAR_LEVEL == 12 && settings.STAR_TIME == ((STAGEWIDTH/settings.SPEED)*3)/8 && settings.STAR_ITIME == 375 )
					{
						if ( nEnemies > e_highscore )
							e_highscore = nEnemies;
						text.score_txt.text = "Difficulty: Easy\nHighscore: " + e_highscore + "\nScore: " + nEnemies;
					}
					else if ( settings.SPEED == 5 && settings.LIVES_LEVEL == 5 && settings.LIVES_TIME == ((STAGEWIDTH/settings.SPEED)*3)/4 && settings.TELE_LEVEL == 10 && settings.TELE_TIME == STAGEWIDTH/settings.SPEED/2 && settings.STAR_LEVEL == 15 && settings.STAR_TIME == ((STAGEWIDTH/settings.SPEED)*3)/8 && settings.STAR_ITIME == 300 )
					{
						if ( nEnemies > n_highscore )
							n_highscore = nEnemies;
						text.score_txt.text = "Difficulty: Normal\nHighscore: " + n_highscore + "\nScore: " + nEnemies;
					}
					else if ( settings.SPEED == 6 && settings.LIVES_LEVEL == 6 && settings.LIVES_TIME == ((STAGEWIDTH/settings.SPEED)*3)/4 && settings.TELE_LEVEL == 12 && settings.TELE_TIME == STAGEWIDTH/settings.SPEED/2 && settings.STAR_LEVEL == 18 && settings.STAR_TIME == ((STAGEWIDTH/settings.SPEED)*3)/8 && settings.STAR_ITIME == 250 )
					{
						if ( nEnemies > h_highscore )
							h_highscore = nEnemies;
						text.score_txt.text = "Difficulty: Hard\nHighscore: " + h_highscore + "\nScore: " + nEnemies;
					}
					else if ( settings.SPEED == 7 && settings.LIVES_LEVEL == 7 && settings.LIVES_TIME == ((STAGEWIDTH/settings.SPEED)*3)/4 && settings.TELE_LEVEL == 14 && settings.TELE_TIME == STAGEWIDTH/settings.SPEED/2 && settings.STAR_LEVEL == 21 && settings.STAR_TIME == ((STAGEWIDTH/settings.SPEED)*3)/8 && settings.STAR_ITIME == 214 )
					{
						if ( nEnemies > x_highscore )
							x_highscore = nEnemies;
						text.score_txt.text = "Difficulty: Extreme\nHighscore: " + x_highscore + "\nScore: " + nEnemies;
					}
					else
					{
						if ( nEnemies > c_highscore )
							c_highscore = nEnemies;
						text.score_txt.text = "Difficulty: Custom\nHighscore: " + c_highscore + "\nScore: " + nEnemies;
					}
					
					//Initialize new enemy's position and speed
					enemy = new Enemy(0, 0, 0, 0);
					
					//Spawn new enemy away from player(s)
					
					if ( !TwoP )
					{
						if ( player.x < STAGEWIDTH / 2 && player.y < STAGEHEIGHT / 2 )
						{
							enemy.x = STAGEWIDTH - W_AND_H;
							enemy.y = 0;
						}
						else
						{
							enemy.x = 0;
							enemy.y = 0;
						}
					}
					if ( TwoP )
					{
						if( !(player.x < STAGEWIDTH / 2 && player.y < STAGEHEIGHT / 2) && !(player2.x < STAGEWIDTH / 2 && player2.y < STAGEHEIGHT / 2) )
						{
							enemy.x = 0;
							enemy.y = 0;
						}
						else if ( player.x < STAGEWIDTH / 2 && player.y < STAGEHEIGHT / 2 )
						{
							if ( (player2.x > STAGEWIDTH / 2 && player2.y < STAGEHEIGHT / 2) || (player2.x < STAGEWIDTH / 2 && player2.y > STAGEHEIGHT / 2) )
							{
								enemy.x = STAGEWIDTH - W_AND_H;
								enemy.y = STAGEHEIGHT - W_AND_H;
							}
							else
							{
								enemy.x = STAGEWIDTH - W_AND_H;
								enemy.y = 0;
							}
						}
						else if ( player2.x < STAGEWIDTH / 2 && player2.y < STAGEHEIGHT / 2 )
						{
							if ( (player.x > STAGEWIDTH / 2 && player.y < STAGEHEIGHT / 2) || (player.x < STAGEWIDTH / 2 && player.y > STAGEHEIGHT / 2) )
							{
								enemy.x = STAGEWIDTH - W_AND_H;
								enemy.y = STAGEHEIGHT - W_AND_H;
							}
							else
							{
								enemy.x = STAGEWIDTH - W_AND_H;
								enemy.y = 0;
							}
						}
					}
					
					enemy.xspeed = (update.Rand(RENEMYSX * (nEnemies+5) * frames * srand) % ((settings.SPEED * 2) + 1)) - settings.SPEED;
					enemy.yspeed = (update.Rand(RENEMYSY * (nEnemies+5) * frames * srand) % ((settings.SPEED * 2) + 1)) - settings.SPEED;
					
					//Add new enemy to enemies array
					enemies.push(enemy);
					
					//Change position of coin
					coin.x = update.Rand(RCOINX * frames * srand) % (STAGEWIDTH - W_AND_H);
					coin.y = update.Rand(RCOINY * frames * srand) % (STAGEHEIGHT - W_AND_H);
					
					//Reset Power ups
					lives.collected = false;
					lives.time = 0;
					lives.x = update.Rand(RLIVESX * frames * srand) % (STAGEWIDTH - W_AND_H);
					lives.y = update.Rand(RLIVESY * frames * srand) % (STAGEHEIGHT - W_AND_H);
					
					tele.collected = false;
					tele.time = 0;
					tele.x = update.Rand(RTELEX * frames * srand) % (STAGEWIDTH - W_AND_H);
					tele.y = update.Rand(RTELEY * frames * srand) % (STAGEHEIGHT - W_AND_H);
					
					star.collected = false;
					if ( !player.star )
						star.time = 0;
					star.x = update.Rand(RSTARX * frames * srand) % (STAGEWIDTH - W_AND_H);
					star.y = update.Rand(RSTARY * frames * srand) % (STAGEHEIGHT - W_AND_H);
					
					rev.x = update.Rand(RREVX * frames * srand) % (STAGEHEIGHT - W_AND_H);
					rev.y = update.Rand(RREVY * frames * srand) % (STAGEHEIGHT - W_AND_H);
				}
				
				//Enemies
				for ( var i:int = 0; i < nEnemies; i++ )
				{
					if ((i % 60) == (update.Rand(LOTTO * frames * srand) % 60))
					{
						enemies[i].xspeed = (update.Rand(RENEMYSX * (i+1) * frames * srand) % ((settings.SPEED * 2) + 1)) - settings.SPEED;
						enemies[i].yspeed = (update.Rand(RENEMYSY * (i+1) * frames * srand) % ((settings.SPEED * 2) + 1)) - settings.SPEED;
					}
					enemies[i] = enemy_update.Update(new Enemy(enemies[i].x,enemies[i].y,enemies[i].xspeed,enemies[i].yspeed),player.x,player.y,W_AND_H);
					if ( !TwoP && update.CircCollision(enemies[i].x,enemies[i].y,W_AND_H/2,player.x,player.y,W_AND_H/2) && !player.hit && !player.star && !player.rev )
					{
						if ( player.lives == 0 )
						{
							//Update score
							if ( settings.SPEED == 4 && settings.LIVES_LEVEL == 4 && settings.LIVES_TIME == ((STAGEWIDTH/settings.SPEED)*3)/4 && settings.TELE_LEVEL == 8 && settings.TELE_TIME == STAGEWIDTH/settings.SPEED/2 && settings.STAR_LEVEL == 12 && settings.STAR_TIME == ((STAGEWIDTH/settings.SPEED)*3)/8 && settings.STAR_ITIME == 375 )
							{
								if ( nEnemies >= e_highscore )
									save.data.e_highscore = e_highscore;
								text.score_txt.text = "Difficulty: Easy\nHighscore: " + e_highscore + "\nScore: " + nEnemies;
							}
							else if ( settings.SPEED == 5 && settings.LIVES_LEVEL == 5 && settings.LIVES_TIME == ((STAGEWIDTH/settings.SPEED)*3)/4 && settings.TELE_LEVEL == 10 && settings.TELE_TIME == STAGEWIDTH/settings.SPEED/2 && settings.STAR_LEVEL == 15 && settings.STAR_TIME == ((STAGEWIDTH/settings.SPEED)*3)/8 && settings.STAR_ITIME == 300 )
							{
								if ( nEnemies >= n_highscore )
									save.data.n_highscore = n_highscore;
								text.score_txt.text = "Difficulty: Normal\nHighscore: " + n_highscore + "\nScore: " + nEnemies;
							}
							else if ( settings.SPEED == 6 && settings.LIVES_LEVEL == 6 && settings.LIVES_TIME == ((STAGEWIDTH/settings.SPEED)*3)/4 && settings.TELE_LEVEL == 12 && settings.TELE_TIME == STAGEWIDTH/settings.SPEED/2 && settings.STAR_LEVEL == 18 && settings.STAR_TIME == ((STAGEWIDTH/settings.SPEED)*3)/8 && settings.STAR_ITIME == 250 )
							{
								if ( nEnemies >= h_highscore )
									save.data.h_highscore = h_highscore;
								text.score_txt.text = "Difficulty: Hard\nHighscore: " + h_highscore + "\nScore: " + nEnemies;
							}
							else if ( settings.SPEED == 7 && settings.LIVES_LEVEL == 7 && settings.LIVES_TIME == ((STAGEWIDTH/settings.SPEED)*3)/4 && settings.TELE_LEVEL == 14 && settings.TELE_TIME == STAGEWIDTH/settings.SPEED/2 && settings.STAR_LEVEL == 21 && settings.STAR_TIME == ((STAGEWIDTH/settings.SPEED)*3)/8 && settings.STAR_ITIME == 214 )
							{
								if ( nEnemies >= x_highscore )
									save.data.x_highscore = x_highscore;
								text.score_txt.text = "Difficulty: Extreme\nHighscore: " + x_highscore + "\nScore: " + nEnemies;
							}
							else
							{
								if ( nEnemies >= c_highscore )
									save.data.c_highscore = c_highscore;
								text.score_txt.text = "Difficulty: Custom\nHighscore: " + c_highscore + "\nScore: " + nEnemies;
							}
							state = GAMEOVER;
							save.flush();
						}
						else
						{
							player.lives--;
							text.lives_txt.text = "Lives: " + player.lives;
						}
						player.hit = true;
						i_hit = i;
					}
					if ( TwoP )
					{
						if ( update.CircCollision(enemies[i].x,enemies[i].y,W_AND_H/2,player.x,player.y,W_AND_H/2) && !player.hit && !player.dead && !player.star && !player.rev )
						{
							if ( player.lives == 0 )
								player.dead = true;
							else
							{
								player.lives--;
								text.lives_txt.text = "Lives: " + player.lives;
							}
							player.hit = true;
							i_hit = i;
						}
						if ( update.CircCollision(enemies[i].x,enemies[i].y,W_AND_H/2,player2.x,player2.y,W_AND_H/2) && !player.hit && !player2.dead && !player.star && !player2.rev )
						{
							if ( player.lives == 0 )
								player2.dead = true;
							else
							{
								player.lives--;
								text.lives_txt.text = "Lives: " + player.lives;
							}
							player.hit = true;
							i_hit = i;
						}
					}
					if( i == i_hit && !(update.CircCollision(enemies[i].x,enemies[i].y,W_AND_H/2,player2.x,player2.y,W_AND_H/2) || update.CircCollision(enemies[i].x,enemies[i].y,W_AND_H/2,player.x,player.y,W_AND_H/2)) )
					{
						i_hit = -1;
						player.hit = false;
					}
				}
				if ( TwoP && player.dead && player2.dead )
				{
					//Update score
					if ( settings.SPEED == 4 && settings.LIVES_LEVEL == 4 && settings.LIVES_TIME == ((STAGEWIDTH/settings.SPEED)*3)/4 && settings.TELE_LEVEL == 8 && settings.TELE_TIME == STAGEWIDTH/settings.SPEED/2 && settings.STAR_LEVEL == 12 && settings.STAR_TIME == ((STAGEWIDTH/settings.SPEED)*3)/8 && settings.STAR_ITIME == 375 )
					{
						if ( nEnemies >= e_highscore )
							save.data.e_highscore = e_highscore;
						text.score_txt.text = "Difficulty: Easy\nHighscore: " + e_highscore + "\nScore: " + nEnemies;
					}
					else if ( settings.SPEED == 5 && settings.LIVES_LEVEL == 5 && settings.LIVES_TIME == ((STAGEWIDTH/settings.SPEED)*3)/4 && settings.TELE_LEVEL == 10 && settings.TELE_TIME == STAGEWIDTH/settings.SPEED/2 && settings.STAR_LEVEL == 15 && settings.STAR_TIME == ((STAGEWIDTH/settings.SPEED)*3)/8 && settings.STAR_ITIME == 300 )
					{
						if ( nEnemies >= n_highscore )
							save.data.n_highscore = n_highscore;
						text.score_txt.text = "Difficulty: Normal\nHighscore: " + n_highscore + "\nScore: " + nEnemies;
					}
					else if ( settings.SPEED == 6 && settings.LIVES_LEVEL == 6 && settings.LIVES_TIME == ((STAGEWIDTH/settings.SPEED)*3)/4 && settings.TELE_LEVEL == 12 && settings.TELE_TIME == STAGEWIDTH/settings.SPEED/2 && settings.STAR_LEVEL == 18 && settings.STAR_TIME == ((STAGEWIDTH/settings.SPEED)*3)/8 && settings.STAR_ITIME == 250 )
					{
						if ( nEnemies >= h_highscore )
							save.data.h_highscore = h_highscore;
						text.score_txt.text = "Difficulty: Hard\nHighscore: " + h_highscore + "\nScore: " + nEnemies;
					}
					else if ( settings.SPEED == 7 && settings.LIVES_LEVEL == 7 && settings.LIVES_TIME == ((STAGEWIDTH/settings.SPEED)*3)/4 && settings.TELE_LEVEL == 14 && settings.TELE_TIME == STAGEWIDTH/settings.SPEED/2 && settings.STAR_LEVEL == 21 && settings.STAR_TIME == ((STAGEWIDTH/settings.SPEED)*3)/8 && settings.STAR_ITIME == 214 )
					{
						if ( nEnemies >= x_highscore )
							save.data.x_highscore = x_highscore;
						text.score_txt.text = "Difficulty: Extreme\nHighscore: " + x_highscore + "\nScore: " + nEnemies;
					}
					else
					{
						if ( nEnemies >= c_highscore )
							save.data.c_highscore = c_highscore;
						text.score_txt.text = "Difficulty: Custom\nHighscore: " + c_highscore + "\nScore: " + nEnemies;
					}
					state = GAMEOVER;
					save.flush();
				}
				
				//End Enemies
				
				//Pause
				if ( player.input.CheckKeyDown('P'.charCodeAt(0)) )
					state = PAUSED;
					
				//Cheats for testing
				if ( player.input.CheckKeyDown('C'.charCodeAt(0)) && player.input.CheckKeyDown('L'.charCodeAt(0)) )
				{
					player.lives++;
					lives.x = update.Rand(RLIVESX * frames * srand) % (STAGEWIDTH - W_AND_H);
					lives.y = update.Rand(RLIVESY * frames * srand) % (STAGEHEIGHT - W_AND_H);
					lives.collected = true;
					text.lives_txt.text = "Lives: " + player.lives;
				}
				if ( player.input.CheckKeyDown('C'.charCodeAt(0)) && player.input.CheckKeyDown('T'.charCodeAt(0)) )
				{
					player.teles++;
					tele.x = update.Rand(RTELEX * frames * srand) % (STAGEWIDTH - W_AND_H);
					tele.y = update.Rand(RTELEY * frames * srand) % (STAGEHEIGHT - W_AND_H);
					tele.collected = true;
					text.tele_txt.text = "Teleports: " + player.teles;
				}
				if ( player.input.CheckKeyDown('C'.charCodeAt(0)) && player.input.CheckKeyDown('S'.charCodeAt(0)) )
				{
					player.star = true;
					star.time = 0;
					star.x = update.Rand(RSTARX * frames * srand) % (STAGEWIDTH - W_AND_H);
					star.y = update.Rand(RSTARY * frames * srand) % (STAGEHEIGHT - W_AND_H);
					star.collected = true;
					text.star_txt.text = "Invulnerability: On";
				}
				//End cheats
				
				frames++;
				if ( frames == 200000000 )
					frames = 1;
			}
			//Update menu/paused/gameOver states
			if ( state == PAUSED )
			{
				if(  player.input.CheckKeyDown(' '.charCodeAt(0)) )
					state = GAME;
				if ( player.input.CheckKeyDown('R'.charCodeAt(0)) )
				{
					state = GAME;
					Initialize(false);
				}
				if ( player.input.CheckKeyDown('B'.charCodeAt(0)) )
				{
					//Update score
					if ( settings.SPEED == 4 && settings.LIVES_LEVEL == 4 && settings.LIVES_TIME == ((STAGEWIDTH/settings.SPEED)*3)/4 && settings.TELE_LEVEL == 8 && settings.TELE_TIME == STAGEWIDTH/settings.SPEED/2 && settings.STAR_LEVEL == 12 && settings.STAR_TIME == ((STAGEWIDTH/settings.SPEED)*3)/8 && settings.STAR_ITIME == 375 )
					{
						if ( nEnemies >= e_highscore )
							save.data.e_highscore = e_highscore;
						text.score_txt.text = "Difficulty: Easy\nHighscore: " + e_highscore + "\nScore: " + nEnemies;
					}
					else if ( settings.SPEED == 5 && settings.LIVES_LEVEL == 5 && settings.LIVES_TIME == ((STAGEWIDTH/settings.SPEED)*3)/4 && settings.TELE_LEVEL == 10 && settings.TELE_TIME == STAGEWIDTH/settings.SPEED/2 && settings.STAR_LEVEL == 15 && settings.STAR_TIME == ((STAGEWIDTH/settings.SPEED)*3)/8 && settings.STAR_ITIME == 300 )
					{
						if ( nEnemies >= n_highscore )
							save.data.n_highscore = n_highscore;
						text.score_txt.text = "Difficulty: Normal\nHighscore: " + n_highscore + "\nScore: " + nEnemies;
					}
					else if ( settings.SPEED == 6 && settings.LIVES_LEVEL == 6 && settings.LIVES_TIME == ((STAGEWIDTH/settings.SPEED)*3)/4 && settings.TELE_LEVEL == 12 && settings.TELE_TIME == STAGEWIDTH/settings.SPEED/2 && settings.STAR_LEVEL == 18 && settings.STAR_TIME == ((STAGEWIDTH/settings.SPEED)*3)/8 && settings.STAR_ITIME == 250 )
					{
						if ( nEnemies >= h_highscore )
							save.data.h_highscore = h_highscore;
						text.score_txt.text = "Difficulty: Hard\nHighscore: " + h_highscore + "\nScore: " + nEnemies;
					}
					else if ( settings.SPEED == 7 && settings.LIVES_LEVEL == 7 && settings.LIVES_TIME == ((STAGEWIDTH/settings.SPEED)*3)/4 && settings.TELE_LEVEL == 14 && settings.TELE_TIME == STAGEWIDTH/settings.SPEED/2 && settings.STAR_LEVEL == 21 && settings.STAR_TIME == ((STAGEWIDTH/settings.SPEED)*3)/8 && settings.STAR_ITIME == 214 )
					{
						if ( nEnemies >= x_highscore )
							save.data.x_highscore = x_highscore;
						text.score_txt.text = "Difficulty: Extreme\nHighscore: " + x_highscore + "\nScore: " + nEnemies;
					}
					else
					{
						if ( nEnemies >= c_highscore )
							save.data.c_highscore = c_highscore;
						text.score_txt.text = "Difficulty: Custom\nHighscore: " + c_highscore + "\nScore: " + nEnemies;
					}
					save.flush();
					state = MENU;
				}
			}
			if ( state == MENU && !(Online || AI) )
			{
				if ( player.input.CheckKeyDown('1'.charCodeAt(0)) )
				{
					Initialize(false);
					state = GAME;
					TwoP = false;
				}
				if ( player.input.CheckKeyDown('2'.charCodeAt(0)) )
				{
					Initialize(false);
					state = GAME;
					TwoP = true;
				}
				if ( player.input.CheckKeyDown('3'.charCodeAt(0)) )
				{
					//Initialize(false);
					//state = GAME;
					//TwoP = false;
					Online = true;
				}
				if ( player.input.CheckKeyDown('4'.charCodeAt(0)) )
				{
					//Initialize(false);
					//state = GAME;
					//TwoP = false;
					AI = true;
				}
				if ( player.input.CheckKeyDown('5'.charCodeAt(0)) )
					state = SETTINGS;
			}
			if ( state == GAMEOVER )
			{
				if (  player.input.CheckKeyDown(' '.charCodeAt(0)) )
				{
					state = GAME;
					Initialize(false);
				}
				if ( player.input.CheckKeyDown('B'.charCodeAt(0)) )
					state = MENU;
			}
			if ( Online )
			{
				if ( player.input.CheckKeyDown('B'.charCodeAt(0)) )
				{
					Online = false;
					state = MENU;
				}
			}
			if ( AI )
			{
				if ( player.input.CheckKeyDown('B'.charCodeAt(0)) )
				{
					AI = false;
					state = MENU;
				}
			}
			if ( state == SETTINGS )
			{
				if ( player.input.CheckKeyDown('A'.charCodeAt(0)) || player.input.CheckKeyDown('C'.charCodeAt(0)) || player.input.CheckKeyDown('D'.charCodeAt(0)) || player.input.CheckKeyDown('E'.charCodeAt(0)) || player.input.CheckKeyDown('F'.charCodeAt(0)) || player.input.CheckKeyDown('G'.charCodeAt(0)) || player.input.CheckKeyDown('H'.charCodeAt(0)) || player.input.CheckKeyDown('I'.charCodeAt(0)) )
					state = SETTINGS_ENTER;
				if ( player.input.CheckKeyDown('A'.charCodeAt(0)) )
					setting_state = 0;
				if ( player.input.CheckKeyDown('C'.charCodeAt(0)) )
					setting_state = 1;
				if ( player.input.CheckKeyDown('D'.charCodeAt(0)) )
					setting_state = 2;
				if ( player.input.CheckKeyDown('E'.charCodeAt(0)) )
					setting_state = 3;
				if ( player.input.CheckKeyDown('F'.charCodeAt(0)) )
					setting_state = 4;
				if ( player.input.CheckKeyDown('G'.charCodeAt(0)) )
					setting_state = 5;
				if ( player.input.CheckKeyDown('H'.charCodeAt(0)) )
					setting_state = 6;
				if ( player.input.CheckKeyDown('I'.charCodeAt(0)) )
					setting_state = 7;
				if ( player.input.CheckKeyDown('B'.charCodeAt(0)) )
				{
					player.speed = settings.SPEED;
					player2.speed = settings.SPEED;
					state = MENU;
					
					save.data.SPEED = settings.SPEED;
					save.data.LIVES_LEVEL = settings.LIVES_LEVEL;
					save.data.LIVES_TIME = settings.LIVES_TIME;
					save.data.TELE_LEVEL = settings.TELE_LEVEL;
					save.data.TELE_TIME = settings.TELE_TIME;
					save.data.STAR_LEVEL = settings.STAR_LEVEL;
					save.data.STAR_TIME = settings.STAR_TIME;
					save.data.STAR_ITIME = settings.STAR_ITIME;
					save.data.BACK_COLOR = settings.BACK_COLOR;
					
					save.flush();
					
					if ( settings.SPEED == 4 && settings.LIVES_LEVEL == 4 && settings.LIVES_TIME == ((STAGEWIDTH/settings.SPEED)*3)/4 && settings.TELE_LEVEL == 8 && settings.TELE_TIME == STAGEWIDTH/settings.SPEED/2 && settings.STAR_LEVEL == 12 && settings.STAR_TIME == ((STAGEWIDTH/settings.SPEED)*3)/8 && settings.STAR_ITIME == 375 )
						text.score_txt.text = "Difficulty: Easy\nHighscore: " + e_highscore + "\nScore: " + nEnemies;
					else if ( settings.SPEED == 5 && settings.LIVES_LEVEL == 5 && settings.LIVES_TIME == ((STAGEWIDTH/settings.SPEED)*3)/4 && settings.TELE_LEVEL == 10 && settings.TELE_TIME == STAGEWIDTH/settings.SPEED/2 && settings.STAR_LEVEL == 15 && settings.STAR_TIME == ((STAGEWIDTH/settings.SPEED)*3)/8 && settings.STAR_ITIME == 300 )
						text.score_txt.text = "Difficulty: Normal\nHighscore: " + n_highscore + "\nScore: " + nEnemies;
					else if ( settings.SPEED == 6 && settings.LIVES_LEVEL == 6 && settings.LIVES_TIME == ((STAGEWIDTH/settings.SPEED)*3)/4 && settings.TELE_LEVEL == 12 && settings.TELE_TIME == STAGEWIDTH/settings.SPEED/2 && settings.STAR_LEVEL == 18 && settings.STAR_TIME == ((STAGEWIDTH/settings.SPEED)*3)/8 && settings.STAR_ITIME == 250 )
						text.score_txt.text = "Difficulty: Hard\nHighscore: " + h_highscore + "\nScore: " + nEnemies;
					else if ( settings.SPEED == 7 && settings.LIVES_LEVEL == 7 && settings.LIVES_TIME == ((STAGEWIDTH/settings.SPEED)*3)/4 && settings.TELE_LEVEL == 14 && settings.TELE_TIME == STAGEWIDTH/settings.SPEED/2 && settings.STAR_LEVEL == 21 && settings.STAR_TIME == ((STAGEWIDTH/settings.SPEED)*3)/8 && settings.STAR_ITIME == 214 )
						text.score_txt.text = "Difficulty: Extreme\nHighscore: " + x_highscore + "\nScore: " + nEnemies;
					else
						text.score_txt.text = "Difficulty: Custom\nHighscore: " + c_highscore + "\nScore: " + nEnemies;
				}
				if ( player.input.CheckKeyDown('R'.charCodeAt(0)) )
				{
					settings.SPEED = 5;
					settings.LIVES_LEVEL = 5;
					settings.LIVES_TIME = ((STAGEWIDTH/settings.SPEED)*3)/4;
					settings.TELE_LEVEL = 10;
					settings.TELE_TIME = STAGEWIDTH/settings.SPEED/2;
					settings.STAR_LEVEL = 15;
					settings.STAR_TIME = ((STAGEWIDTH/settings.SPEED)*3)/8;
					settings.STAR_ITIME = 300;
				}
				if ( player.input.CheckKeyDown('1'.charCodeAt(0)) )
				{
					settings.SPEED = 4;
					settings.LIVES_LEVEL = 4;
					settings.LIVES_TIME = ((STAGEWIDTH/settings.SPEED)*3)/4;
					settings.TELE_LEVEL = 8;
					settings.TELE_TIME = STAGEWIDTH/settings.SPEED/2;
					settings.STAR_LEVEL = 12;
					settings.STAR_TIME = ((STAGEWIDTH/settings.SPEED)*3)/8;
					settings.STAR_ITIME = 375;
				}
				if ( player.input.CheckKeyDown('2'.charCodeAt(0)) )
				{
					settings.SPEED = 5;
					settings.LIVES_LEVEL = 5;
					settings.LIVES_TIME = ((STAGEWIDTH/settings.SPEED)*3)/4;
					settings.TELE_LEVEL = 10;
					settings.TELE_TIME = STAGEWIDTH/settings.SPEED/2;
					settings.STAR_LEVEL = 15;
					settings.STAR_TIME = ((STAGEWIDTH/settings.SPEED)*3)/8;
					settings.STAR_ITIME = 300;
				}
				if ( player.input.CheckKeyDown('3'.charCodeAt(0)) )
				{
					settings.SPEED = 6;
					settings.LIVES_LEVEL = 6;
					settings.LIVES_TIME = ((STAGEWIDTH/settings.SPEED)*3)/4;
					settings.TELE_LEVEL = 12;
					settings.TELE_TIME = STAGEWIDTH/settings.SPEED/2;
					settings.STAR_LEVEL = 18;
					settings.STAR_TIME = ((STAGEWIDTH/settings.SPEED)*3)/8;
					settings.STAR_ITIME = 250;
				}
				if ( player.input.CheckKeyDown('4'.charCodeAt(0)) )
				{
					settings.SPEED = 7;
					settings.LIVES_LEVEL = 7;
					settings.LIVES_TIME = ((STAGEWIDTH/settings.SPEED)*3)/4;
					settings.TELE_LEVEL = 14;
					settings.TELE_TIME = STAGEWIDTH/settings.SPEED/2;
					settings.STAR_LEVEL = 21;
					settings.STAR_TIME = ((STAGEWIDTH/settings.SPEED)*3)/8;
					settings.STAR_ITIME = 214;
				}
				if ( player.input.CheckKeyDown('6'.charCodeAt(0)) )
					settings.BACK_COLOR = 0x7D1919;
				if ( player.input.CheckKeyDown('7'.charCodeAt(0)) )
					settings.BACK_COLOR = 0x197D19;
				if ( player.input.CheckKeyDown('8'.charCodeAt(0)) )
					settings.BACK_COLOR = 0x19197D;
				if ( player.input.CheckKeyDown('9'.charCodeAt(0)) )
					settings.BACK_COLOR = 0x000000;
					
				if ( player.input.CheckKeyDown('R'.charCodeAt(0)) )
				{
					save.clear();
					settings.SPEED = 5;
					settings.LIVES_LEVEL = 5;
					settings.LIVES_TIME = ((STAGEWIDTH/settings.SPEED)*3)/4;
					settings.TELE_LEVEL = 10;
					settings.TELE_TIME = STAGEWIDTH/settings.SPEED/2;
					settings.STAR_LEVEL = 15;
					settings.STAR_TIME = ((STAGEWIDTH/settings.SPEED)*3)/8;
					settings.STAR_ITIME = 300;
					settings.BACK_COLOR = 0x000000;
					e_highscore = 0;
					n_highscore = 0;
					h_highscore = 0;
					x_highscore = 0;
					c_highscore = 0;
				}
				
				if ( player.input.CheckKeyDown('1'.charCodeAt(0)) || player.input.CheckKeyDown('2'.charCodeAt(0)) || player.input.CheckKeyDown('3'.charCodeAt(0)) || player.input.CheckKeyDown('4'.charCodeAt(0)) || player.input.CheckKeyDown('R'.charCodeAt(0)) )
					text.settings.text = "Press a to change speed of game  " + settings.SPEED + "\nPress c to change how often lives appear  " + settings.LIVES_LEVEL + "\nPress d to change how long lives appear  " + settings.LIVES_TIME + "\nPress e to change how often teleports appear  " + settings.TELE_LEVEL + "\nPress f to change how long teleports appear  " + settings.TELE_TIME + "\nPress g to change how often stars appear  " + settings.STAR_LEVEL + "\nPress h to change how long stars appear  " + settings.STAR_TIME + "\nPress i to change how long invulnerability lasts  " + settings.STAR_ITIME + "\n\nPress 1,2,3 or 4 for easy, normal, hard, or extreme difficulty\nPress 6,7, 8, or 9 for red, green, blue or black background\nTime is in 60ths of a second\nPress R to reset all saved data\nPress B to return to menu";
			}
			if ( state == SETTINGS_ENTER )
			{
				var temp:int = player.input.GetNewKeyDown();
				if ( temp > 47 && temp < 58 )
				{
					s_num += (temp-48) * s_digit;
					if ( s_digit > 1 )
						s_digit /= 10;
				}
				if( s_digit <= 1 && player.input.CheckKeyDown(' '.charCodeAt(0)) )
				{
					if ( setting_state == 0 )
						settings.SPEED = s_num;
					if ( setting_state == 1 )
						settings.LIVES_LEVEL = s_num;
					if ( setting_state == 2 )
						settings.LIVES_TIME = s_num;
					if ( setting_state == 3 )
						settings.TELE_LEVEL = s_num;
					if ( setting_state == 4 )
						settings.TELE_TIME = s_num;
					if ( setting_state == 5 )
						settings.STAR_LEVEL = s_num;
					if ( setting_state == 6 )
						settings.STAR_TIME = s_num;
					if ( setting_state == 7 )
						settings.STAR_ITIME = s_num;
					
					s_digit = 100;
					s_num = 0;
					state = SETTINGS;
					text.settings.text = "Press a to change speed of game  " + settings.SPEED + "\nPress c to change how often lives appear  " + settings.LIVES_LEVEL + "\nPress d to change how long lives appear  " + settings.LIVES_TIME + "\nPress e to change how often teleports appear  " + settings.TELE_LEVEL + "\nPress f to change how long teleports appear  " + settings.TELE_TIME + "\nPress g to change how often stars appear  " + settings.STAR_LEVEL + "\nPress h to change how long stars appear  " + settings.STAR_TIME + "\nPress i to change how long invulnerability lasts  " + settings.STAR_ITIME + "\n\nPress 1,2,3 or 4 for easy, normal, hard, or extreme difficulty\nPress 6,7, 8, or 9 for red, green, blue or black background\nTime is in 60ths of a second\nPress R to reset all saved data\nPress B to return to menu";
				}
			}
			
			/****************	End Game Specific Update Code	****************/
		}
	}
}