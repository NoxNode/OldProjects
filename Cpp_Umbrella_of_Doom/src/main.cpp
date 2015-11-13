#include <SFML\Graphics.hpp>
#include <iostream>
#include <math.h>
#include "Entity.h"
#include "Player.h"
#include "Enemy.h"
#include "LinkedList.h"
#include "Umbrella.h"
#include "Droplet.h"
#include <random>
#include "Boss.h"

#define PI 3.14159265f

void restart(LinkedList* entities, bool* hasWon, bool* gameOver, sf::RectangleShape* enemyHealthBar, int nEnemies, int* cameraX) {
	//init enemies
	Entity* curEntity = (Entity*)(*entities).getRoot();
	Entity* prevEntity = curEntity;

	bool deleted = false;
	while (curEntity != 0) {
		if (curEntity->getId() == DROPLET) {
			if (prevEntity == curEntity) {
				(*entities).setRoot(curEntity->getNext());
				curEntity->setNext(0);
				delete curEntity;
				deleted = true;
			}
			else {
				prevEntity->setNext(curEntity->getNext());
				curEntity->setNext(0);
				delete curEntity;
				curEntity = prevEntity;
			}
		}
		if (curEntity->getId() == ENEMY) {
			if (prevEntity == curEntity) {
				(*entities).setRoot(curEntity->getNext());
				curEntity->setNext(0);
				delete curEntity;
				deleted = true;
			}
			else {
				prevEntity->setNext(curEntity->getNext());
				curEntity->setNext(0);
				delete curEntity;
				curEntity = prevEntity;
			}
		}
		if (curEntity->getId() == BOSS) {
			if (prevEntity == curEntity) {
				(*entities).setRoot(curEntity->getNext());
				curEntity->setNext(0);
				delete curEntity;
				deleted = true;
			}
			else {
				prevEntity->setNext(curEntity->getNext());
				curEntity->setNext(0);
				delete curEntity;
				curEntity = prevEntity;
			}
		}
		if (curEntity->getId() == PLAYER) {
			//init player
			Player* player = (Player*)curEntity;
			player->setHealth(100);
			player->setStamina(100);
			player->setX(0);
			player->setY(0);
			player->setHasUmbrella(false);
			*cameraX = 0;
		}
		if (deleted) {
			curEntity = (Entity*)entities->getRoot();
			prevEntity = curEntity;
			deleted = false;
		}
		else {
			prevEntity = curEntity;
			curEntity = (Entity*)curEntity->getNext();
		}
	}

	for (int i = 0; i < nEnemies; i++) {
		//health bar
		enemyHealthBar[i].setSize({ 50, 5 });

		//enemy
		Enemy* enemy = new Enemy(entities);

		int left = rand() % 4;
		float randX = (rand() % (800)) + 1600;
		if (left == 1) {
			randX -= 800;
		}
		float randY = rand() % 600;

		enemy->setX(randX);
		enemy->setY(randY);
		(*entities).addNode(enemy);
	}

	//init boss
	Boss* boss = new Boss(entities);
	boss->setX(800 * 4 - 400);
	boss->setY(300);
	entities->addNode(boss);

	//game variables
	*hasWon = false;
	*gameOver = false;
}

int main() {
	sf::RenderWindow window(sf::VideoMode(800, 600), "UMBRELLA OF DOOM!");
	window.setVerticalSyncEnabled(true);
	// TODO: maybe restrict fps to 60? people with monitors that have a higher refresh rate may run the game faster

	sf::RectangleShape damageBar;
	damageBar.setFillColor(sf::Color::Red);
	damageBar.setSize({ 50, 4 });
	damageBar.setOrigin(25, 2);

	sf::RectangleShape healthBar;
	healthBar.setFillColor(sf::Color::Green);
	healthBar.setSize({ 50, 4 });
	healthBar.setOrigin(25, 2);

	sf::RectangleShape staminaBar;
	staminaBar.setFillColor(sf::Color::Blue);
	staminaBar.setSize({ 50, 4 });
	staminaBar.setOrigin(25, 2);

	bool gameOver = false;
	bool hasWon = false;

	//1 == easy, 2 == medium, 3 == hard
	int difficulty = 1;

	LinkedList entities;

	//Player
	Player* player = new Player(&entities);
	entities.addNode(player);

	srand(time(NULL));

	//Boss
	Boss* boss = new Boss(&entities);
	entities.addNode(boss);
	boss->setX(800 * 4 - 400);
	boss->setY(300);

	sf::RectangleShape bossDamageBar;
	bossDamageBar.setFillColor(sf::Color::Red);
	bossDamageBar.setSize({ 50, 4 });
	bossDamageBar.setOrigin(25, 2);

	sf::RectangleShape bossHealthBar;
	bossHealthBar.setFillColor(sf::Color::Green);
	bossHealthBar.setSize({ 50, 4 });
	bossHealthBar.setOrigin(25, 2);

	//Enemy
	const int nEnemies = 25;
	const int worldWidth = 800 * 4;
	const int worldHeight = 600;

	sf::RectangleShape enemyDamageBar[nEnemies];
	sf::RectangleShape enemyHealthBar[nEnemies];

	for (int i = 0; i < nEnemies; i++) {
		//health bar
		enemyDamageBar[i].setFillColor(sf::Color::Red);
		enemyDamageBar[i].setSize({ 50, 5 });
		enemyDamageBar[i].setOrigin(25, 3);

		enemyHealthBar[i].setFillColor(sf::Color::Green);
		enemyHealthBar[i].setSize({ 50, 5 });
		enemyHealthBar[i].setOrigin(25, 3);

		//enemy
		Enemy* enemy = new Enemy(&entities);

		int left = rand() % 4;
		float randX = (rand() % (800)) + 1600;
		if (left == 1) {
			randX -= 800;
		}
		float randY = rand() % worldHeight;

		enemy->setX(randX);
		enemy->setY(randY);
		entities.addNode(enemy);
	}

	//Umbrella
	Umbrella* umbrella = new Umbrella(&entities);
	entities.addNode(umbrella);
	umbrella->setX(400);
	umbrella->setY(300);

	//Text
	sf::Font font;
	font.loadFromFile("res/fonts/consola.ttf");
	sf::Text text;
	text.setFont(font);
	text.setString("Pick up the UMBRELLA OF DOOOOOOOM!");
	text.setPosition(150, 200);

	//GameOver text
	sf::Text gameOverText;
	gameOverText.setFont(font);
	gameOverText.setString("Game Ober Man! Press R to restart!");

	//HasWon text
	sf::Text hasWonText;
	hasWonText.setFont(font);
	hasWonText.setString("Noice! You Won! Press R to restart!");

	//Difficulty help text
	sf::Text difficultyHelpText;
	difficultyHelpText.setFont(font);
	difficultyHelpText.setString("Press 1 for easy, 2 for medium, 3 for hard");

	//Ground
	sf::Texture groundTexture;
	groundTexture.loadFromFile("res/textures/ground.png");
	sf::Sprite groundSprite(groundTexture);

	//Event variables
	bool w = false, a = false, s = false, d = false;
	int mouseX = 0, mouseY = 0;
	int cameraX = 0;
	bool leftMouseButton = false, rightMouseButton = false;
	bool leftMouseButtonUsed = false, rightMouseButtonUsed = false;

	while (window.isOpen()) {
		leftMouseButton = false;
		//Events
		sf::Event e;
		window.pollEvent(e);
		switch (e.type) {
		case sf::Event::KeyPressed:
			if (e.key.code == sf::Keyboard::W) {
				w = true;
			}
			if (e.key.code == sf::Keyboard::A) {
				a = true;
			}
			if (e.key.code == sf::Keyboard::S) {
				s = true;
			}
			if (e.key.code == sf::Keyboard::D) {
				d = true;
			}
			if (e.key.code == sf::Keyboard::Num1) {
				difficulty = 1;
			}
			if (e.key.code == sf::Keyboard::Num2) {
				difficulty = 2;
			}
			if (e.key.code == sf::Keyboard::Num3) {
				difficulty = 3;
			}
			if (gameOver && e.key.code == sf::Keyboard::R) {
				restart(&entities, &hasWon, &gameOver, enemyHealthBar, nEnemies, &cameraX);
			}
			break;
		case sf::Event::KeyReleased:
			if (e.key.code == sf::Keyboard::W) {
				w = false;
			}
			if (e.key.code == sf::Keyboard::A) {
				a = false;
			}
			if (e.key.code == sf::Keyboard::S) {
				s = false;
			}
			if (e.key.code == sf::Keyboard::D) {
				d = false;
			}
			if (e.key.code == sf::Keyboard::Num1) {
				difficulty = 1;
			}
			if (e.key.code == sf::Keyboard::Num2) {
				difficulty = 2;
			}
			if (e.key.code == sf::Keyboard::Num3) {
				difficulty = 3;
			}
			break;
		case sf::Event::MouseMoved:
			mouseX = e.mouseMove.x;
			mouseY = e.mouseMove.y;
			break;
		case sf::Event::MouseButtonPressed:
			if (!leftMouseButtonUsed && e.mouseButton.button == sf::Mouse::Left) {
				leftMouseButton = true;
				leftMouseButtonUsed = true;
			}
			if (e.mouseButton.button == sf::Mouse::Right) {
				rightMouseButton = true;
			}
			break;
		case sf::Event::MouseButtonReleased:
			if (e.mouseButton.button == sf::Mouse::Left) {
				leftMouseButton = false;
				leftMouseButtonUsed = false;
			}
			if (e.mouseButton.button == sf::Mouse::Right) {
				rightMouseButton = false;
			}
			break;
		case sf::Event::Closed:
			window.close();
			break;
		}

		//Update
		if (!gameOver) { //update if game is not over yet
			player->update(w, a, s, d, rightMouseButton, leftMouseButton, mouseX, mouseY, &cameraX);

			Entity* curEntity = (Entity*)entities.getRoot();
			Entity* prevEntity = curEntity;

			bool enemiesLeft = false;

			bool deleted = false;
			while (curEntity != 0) {
				if (curEntity->getId() == DROPLET) {
					Droplet* droplet = (Droplet*)curEntity;
					if (droplet->getHastHit() || droplet->getX() < 0 || droplet->getX() > worldWidth || droplet->getY() < 0 || droplet->getY() > worldHeight) {
						//remove droplet cuz its out of bounds
						if (prevEntity == curEntity) {
							entities.setRoot(curEntity->getNext());
							curEntity->setNext(0);
							delete curEntity;
							deleted = true;
						}
						else {
							prevEntity->setNext(curEntity->getNext());
							curEntity->setNext(0);
							delete curEntity;
							curEntity = prevEntity;
						}
					}
					else {
						droplet->update(&cameraX, mouseX, mouseY, difficulty);
					}
				}
				if (curEntity->getId() == BOSS) {
					enemiesLeft = true;
					Boss* boss = (Boss*)curEntity;
					boss->update(&cameraX, difficulty);
					if (boss->getHealth() <= 0) {
						//remove boss cuz its dead
						if (prevEntity == curEntity) {
							entities.setRoot(curEntity->getNext());
							curEntity->setNext(0);
							delete curEntity;
							deleted = true;
						}
						else {
							prevEntity->setNext(curEntity->getNext());
							curEntity->setNext(0);
							delete curEntity;
							curEntity = prevEntity;
						}
					}
				}
				if (curEntity->getId() == ENEMY) {
					enemiesLeft = true;
					Enemy* enemy = (Enemy*)curEntity;
					enemy->update(&cameraX, difficulty);
					if (enemy->getHealth() <= 0) {
						//remove enemy cuz its dead
						if (prevEntity == curEntity) {
							entities.setRoot(curEntity->getNext());
							curEntity->setNext(0);
							delete curEntity;
							deleted = true;
						}
						else {
							prevEntity->setNext(curEntity->getNext());
							curEntity->setNext(0);
							delete curEntity;
							curEntity = prevEntity;
						}
					}
				}
				if (deleted) {
					curEntity = (Entity*)entities.getRoot();
					prevEntity = curEntity;
					deleted = false;
				}
				else {
					prevEntity = curEntity;
					curEntity = (Entity*)curEntity->getNext();
				}
			}
			if (!player->getHasUmbrella()) {
				umbrella->update();
			}
			if (player->getHealth() <= 0) {
				gameOver = true;
			}
			if (!enemiesLeft) {
				hasWon = true;
				gameOver = true;
			}
		}
		else {
			// have some way of resetting the game (use init method)
		}

		//Draw
		window.clear();

		//ground
		const int groundWidth = 800;
		const int groundHeight = 600;

		float groundCameraX = cameraX % 32;
		for (int y = 0; y < groundHeight / groundTexture.getSize().y + groundTexture.getSize().y; y++) {
			for (int x = 0; x < groundWidth / groundTexture.getSize().x + groundTexture.getSize().x; x++) {
				groundSprite.setPosition(x * groundTexture.getSize().x - groundCameraX, y * groundTexture.getSize().y);
				window.draw(groundSprite);
			}
		}

		//umbrella
		if (!player->getHasUmbrella()) {
			window.draw(text);
			sf::Sprite cameraUmbrella = *umbrella->getDrawingSprite();
			cameraUmbrella.move(-cameraX, 0);
			window.draw(cameraUmbrella);
			window.draw(difficultyHelpText);
		}
		//enemy
		Entity* curEntity = (Entity*)entities.getRoot();
		int index = nEnemies;
		while (curEntity != 0) {
			if (curEntity->getId() == ENEMY) {
				index--;
				enemyDamageBar[index].setPosition(curEntity->getX() - cameraX, curEntity->getY() - 24);
				enemyHealthBar[index].setPosition(curEntity->getX() - cameraX, curEntity->getY() - 24);
				enemyHealthBar[index].setSize({ ((Enemy*)curEntity)->getHealth() / 2.0f, 5.0f });
				window.draw(enemyDamageBar[index]);
				window.draw(enemyHealthBar[index]);
				sf::Sprite cameraEnemy = *curEntity->getDrawingSprite();
				cameraEnemy.move(-cameraX, 0);
				window.draw(cameraEnemy);
			}
			if (curEntity->getId() == BOSS) {
				bossDamageBar.setPosition(curEntity->getX() - cameraX, curEntity->getY() - 24);
				bossHealthBar.setPosition(curEntity->getX() - cameraX, curEntity->getY() - 24);
				bossHealthBar.setSize({ ((Enemy*)curEntity)->getHealth() / 2.0f, 5.0f });
				window.draw(bossDamageBar);
				window.draw(bossHealthBar);
				sf::Sprite cameraBoss = *curEntity->getDrawingSprite();
				cameraBoss.move(-cameraX, 0);
				window.draw(cameraBoss);
			}
			if (curEntity->getId() == DROPLET) {
				sf::Sprite cameraDroplet = *curEntity->getDrawingSprite();
				cameraDroplet.move(-cameraX, 0);
				window.draw(cameraDroplet);
			}
			curEntity = (Entity*)curEntity->getNext();
		}
		//player
		damageBar.setPosition(player->getX() - cameraX, player->getY() - 32);
		healthBar.setPosition(player->getX() - cameraX, player->getY() - 32);
		healthBar.setSize({ player->getHealth() / 2.0f, 5.0f });
		window.draw(damageBar);
		window.draw(healthBar);
		damageBar.setPosition(player->getX() - cameraX, player->getY() - 24);
		staminaBar.setPosition(player->getX() - cameraX, player->getY() - 24);
		staminaBar.setSize({ player->getStamina() / 2.0f, 5.0f });
		window.draw(damageBar);
		window.draw(staminaBar);
		sf::Sprite cameraPlayer = *player->getDrawingSprite();
		cameraPlayer.move(-cameraX, 0);
		window.draw(cameraPlayer);

		//display gameOver text
		if (gameOver) {
			if (hasWon) {
				window.draw(hasWonText);
			}
			else {
				window.draw(gameOverText);
			}
		}

		window.display();
	}

	return 0;
}
