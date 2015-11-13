#include "Boss.h"
#include <math.h>
#include <iostream>
#include "Droplet.h"
#include "Settings.h"

#define PI 3.14159265f

Boss::Boss(LinkedList* entities) :
health(1000),
cooldown(0),
aggroed(false)
{
	setEntities(entities);
	setId(BOSS);

	sf::Texture* enemyTexture = new sf::Texture();
	enemyTexture->loadFromFile("res/textures/boss.png");
	sf::Sprite* enemySprite = new sf::Sprite(*enemyTexture);
	enemySprite->setOrigin(enemyTexture->getSize().x / 2.0f, enemyTexture->getSize().y / 2.0f);

	const int nEnemySprites = 1;
	sf::Sprite** enemySprites = new sf::Sprite*[nEnemySprites];
	enemySprites[0] = enemySprite;

	setSprites(enemySprites);
	setNSprites(nEnemySprites);
}

#define COOLDOWN 15

void Boss::update(int* cameraX, int difficulty) {
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
				if (cooldown > COOLDOWN) {
					cooldown = 0;

					float dx1 = getDrawingSprite()->getPosition().x - *cameraX - (curEntity->getDrawingSprite()->getPosition().x - *cameraX) + 20;
					float dy1 = getDrawingSprite()->getPosition().y - curEntity->getDrawingSprite()->getPosition().y;
					float length1 = sqrt(dx * dx + dy * dy);

					float dx2 = getDrawingSprite()->getPosition().x - *cameraX - (curEntity->getDrawingSprite()->getPosition().x - *cameraX) - 20;
					float dy2 = getDrawingSprite()->getPosition().y - curEntity->getDrawingSprite()->getPosition().y;
					float length2 = sqrt(dx * dx + dy * dy);

					float dx3 = getDrawingSprite()->getPosition().x - *cameraX - (curEntity->getDrawingSprite()->getPosition().x - *cameraX);
					float dy3 = getDrawingSprite()->getPosition().y - curEntity->getDrawingSprite()->getPosition().y + 20;
					float length3 = sqrt(dx * dx + dy * dy);

					float dx4 = getDrawingSprite()->getPosition().x - *cameraX - (curEntity->getDrawingSprite()->getPosition().x - *cameraX);
					float dy4 = getDrawingSprite()->getPosition().y - curEntity->getDrawingSprite()->getPosition().y - 20;
					float length4 = sqrt(dx * dx + dy * dy);

					//shooot hiiiiiim
					float velx;
					float vely;

					float velx1;
					float vely1;

					float velx2;
					float vely2;

					float velx3;
					float vely3;

					float velx4;
					float vely4;
					if (difficulty == 1) {
						velx = dx / length * EASYDROPLETSPEED;
						vely = dy / length * EASYDROPLETSPEED;

						velx1 = dx1 / length * EASYDROPLETSPEED;
						vely1 = dy1 / length * EASYDROPLETSPEED;

						velx2 = dx2 / length * EASYDROPLETSPEED;
						vely2 = dy2 / length * EASYDROPLETSPEED;

						velx3 = dx3 / length * EASYDROPLETSPEED;
						vely3 = dy3 / length * EASYDROPLETSPEED;

						velx4 = dx4 / length * EASYDROPLETSPEED;
						vely4 = dy4 / length * EASYDROPLETSPEED;
					}
					else if (difficulty == 2) {
						velx = dx / length * MEDIUMDROPLETSPEED;
						vely = dy / length * MEDIUMDROPLETSPEED;

						velx1 = dx1 / length * MEDIUMDROPLETSPEED;
						vely1 = dy1 / length * MEDIUMDROPLETSPEED;

						velx2 = dx2 / length * MEDIUMDROPLETSPEED;
						vely2 = dy2 / length * MEDIUMDROPLETSPEED;

						velx3 = dx3 / length * MEDIUMDROPLETSPEED;
						vely3 = dy3 / length * MEDIUMDROPLETSPEED;

						velx4 = dx4 / length * MEDIUMDROPLETSPEED;
						vely4 = dy4 / length * MEDIUMDROPLETSPEED;
					}
					else {
						velx = dx / length * HARDDROPLETSPEED;
						vely = dy / length * HARDDROPLETSPEED;

						velx1 = dx1 / length * HARDDROPLETSPEED;
						vely1 = dy1 / length * HARDDROPLETSPEED;

						velx2 = dx2 / length * HARDDROPLETSPEED;
						vely2 = dy2 / length * HARDDROPLETSPEED;

						velx3 = dx3 / length * HARDDROPLETSPEED;
						vely3 = dy3 / length * HARDDROPLETSPEED;

						velx4 = dx4 / length * HARDDROPLETSPEED;
						vely4 = dy4 / length * HARDDROPLETSPEED;
					}

					Droplet* droplet = new Droplet(entities);
					droplet->setOwnerId(ENEMY);
					droplet->setX(getX());
					droplet->setY(getY());
					droplet->setVelx(-velx);
					droplet->setVely(-vely);

					droplet->getDrawingSprite()->setRotation(this->getDrawingSprite()->getRotation() + 180);

					entities->addNode(droplet);

					//1
					Droplet* droplet1 = new Droplet(entities);
					droplet1->setOwnerId(ENEMY);
					droplet1->setX(getX());
					droplet1->setY(getY());
					droplet1->setVelx(-velx1);
					droplet1->setVely(-vely1);

					droplet1->getDrawingSprite()->setRotation(this->getDrawingSprite()->getRotation() + 180);

					entities->addNode(droplet1);

					//2
					Droplet* droplet2 = new Droplet(entities);
					droplet2->setOwnerId(ENEMY);
					droplet2->setX(getX());
					droplet2->setY(getY());
					droplet2->setVelx(-velx2);
					droplet2->setVely(-vely2);

					droplet2->getDrawingSprite()->setRotation(this->getDrawingSprite()->getRotation() + 180);

					entities->addNode(droplet2);

					//3
					Droplet* droplet3 = new Droplet(entities);
					droplet3->setOwnerId(ENEMY);
					droplet3->setX(getX());
					droplet3->setY(getY());
					droplet3->setVelx(-velx3);
					droplet3->setVely(-vely3);

					droplet3->getDrawingSprite()->setRotation(this->getDrawingSprite()->getRotation() + 180);

					entities->addNode(droplet3);

					//4
					Droplet* droplet4 = new Droplet(entities);
					droplet4->setOwnerId(ENEMY);
					droplet4->setX(getX());
					droplet4->setY(getY());
					droplet4->setVelx(-velx4);
					droplet4->setVely(-vely4);

					droplet4->getDrawingSprite()->setRotation(this->getDrawingSprite()->getRotation() + 180);

					entities->addNode(droplet4);
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

int Boss::getHealth() {
	return health;
}

void Boss::setHealth(int health) {
	this->health = health;
}
