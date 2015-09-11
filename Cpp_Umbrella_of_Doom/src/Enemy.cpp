#include "Enemy.h"
#include <math.h>
#include <iostream>
#include "Droplet.h"
#include "Settings.h"

#define PI 3.14159265f

Enemy::Enemy(LinkedList* entities) :
health(100),
cooldown(0),
aggroed(false)
{
	setEntities(entities);
	setId(ENEMY);

	sf::Texture* enemyTexture = new sf::Texture();
	enemyTexture->loadFromFile("res/textures/enemy.png");
	sf::Sprite* enemySprite = new sf::Sprite(*enemyTexture);
	enemySprite->setOrigin(enemyTexture->getSize().x / 2.0f, enemyTexture->getSize().y / 2.0f);

	const int nEnemySprites = 1;
	sf::Sprite** enemySprites = new sf::Sprite*[nEnemySprites];
	enemySprites[0] = enemySprite;

	setSprites(enemySprites);
	setNSprites(nEnemySprites);
}

void Enemy::update(int* cameraX, int difficulty) {
	LinkedList* entities = getEntities();
	Entity* curEntity = (Entity*)entities->getRoot();
	cooldown++;
	while (curEntity != 0) {
		if (curEntity->getId() == PLAYER) {
			// look at player
			float dx = getDrawingSprite()->getPosition().x - *cameraX - curEntity->getDrawingSprite()->getPosition().x + *cameraX;
			float dy = getDrawingSprite()->getPosition().y - curEntity->getDrawingSprite()->getPosition().y;

			float length = sqrt(dx * dx + dy * dy);
			if ((difficulty == 1 && length < EASYENEMYRANGE) || (difficulty == 2 && length < MEDIUMENEMYRANGE) || (difficulty == 3 && length < HARDENEMYRANGE)) {
				aggroed = true;
				if ((difficulty == 1 && cooldown > EASYENEMYCOOLDOWN) || (difficulty == 2 && cooldown > MEDIUMENEMYCOOLDOWN) || (difficulty == 3 && cooldown > HARDENEMYCOOLDOWN)) {
					cooldown = 0;
					//shooot hiiiiiim
					float velx;
					float vely;
					if (difficulty == 1) {
						velx = dx / length * EASYDROPLETSPEED;
						vely = dy / length * EASYDROPLETSPEED;
					}
					else if (difficulty == 2) {
						velx = dx / length * MEDIUMDROPLETSPEED;
						vely = dy / length * MEDIUMDROPLETSPEED;
					}
					else {
						velx = dx / length * HARDDROPLETSPEED;
						vely = dy / length * HARDDROPLETSPEED;
					}

					Droplet* droplet = new Droplet(entities);
					droplet->setOwnerId(ENEMY);
					droplet->setX(getX());
					droplet->setY(getY());
					droplet->setVelx(-velx);
					droplet->setVely(-vely);

					droplet->getDrawingSprite()->setRotation(this->getDrawingSprite()->getRotation() + 180);

					entities->addNode(droplet);
				}
			}

			float radians = atan(dy / dx);
			float angle = radians * 180 / PI + 90.0f;

			if (curEntity->getDrawingSprite()->getPosition().x - *cameraX <= getX() - *cameraX) {
				angle += 180.0f;
			}

			getDrawingSprite()->setRotation(angle);

			if (aggroed) {
				float velx;
				float vely;
				if (difficulty == 1) {
					velx = dx / length * EASYENEMYSPEED;
					vely = dy / length * EASYENEMYSPEED;
				}
				else if (difficulty == 2) {
					velx = dx / length * MEDIUMENEMYSPEED;
					vely = dy / length * MEDIUMENEMYSPEED;
				}
				else {
					velx = dx / length * HARDENEMYSPEED;
					vely = dy / length * HARDENEMYSPEED;
				}

				move(-velx, -vely);
			}
		}
		curEntity = (Entity*)curEntity->getNext();
	}
}

int Enemy::getHealth() {
	return health;
}

void Enemy::setHealth(int health) {
	this->health = health;
}

void Enemy::print() {
	using namespace std;
	cout << "I'm an enemy" << endl;
}
