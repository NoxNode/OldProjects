#include "Droplet.h"
#include <math.h>
#include "Player.h"
#include "Enemy.h"
#include <iostream>
#include "Settings.h"
#include "Boss.h"

#define PI 3.14159265f

Droplet::Droplet(LinkedList* entities) : 
ownerId(0),
hasHit(false)
{
	setEntities(entities);
	setId(DROPLET);
	
	sf::Texture* dropletTexture = new sf::Texture();
	dropletTexture->loadFromFile("res/textures/droplet.png");
	sf::Sprite* dropletSprite = new sf::Sprite(*dropletTexture);
	dropletSprite->setOrigin(dropletTexture->getSize().x / 2.0f, dropletTexture->getSize().y / 2.0f);

	const int nDropletSprites = 1;
	sf::Sprite** dropletSprites = new sf::Sprite*[nDropletSprites];
	dropletSprites[0] = dropletSprite;

	setSprites(dropletSprites);
	setNSprites(nDropletSprites);
}

void Droplet::update(int* cameraX, int mouseX, int mouseY, int difficulty) {
	LinkedList* entities = getEntities();
	Entity* curEntity = (Entity*)entities->getRoot();
	// go forward
	move(velx, vely);

	while (curEntity != 0) {
		if (curEntity->getId() == PLAYER) {
			float pdx = curEntity->getX() - this->getX();
			float pdy = curEntity->getY() - this->getY();

			if (sqrt(pdx * pdx + pdy * pdy) < 21) {
				if (!hasHit && ownerId == ENEMY) {
					Player* player = (Player*)curEntity;
					const int damage = 10;
					if (player->getHealth() > 0) {
						player->setHealth(player->getHealth() - damage);
					}
					else {
						player->setHealth(0);
					}
					hasHit = true;
				}
				if (ownerId == PLAYER) {
					hasHit = true;
				}
			}

			//check if attacking, if so, and it's in the hit box, go in the direction the player is facing
			Player* player = (Player*)curEntity;
			if (player->getIsAttacking()) {
				const float collisionCenterOffsetX = 28;
				const float collisionCenterOffsetY = 28;

				// look at mouse
				float dx = player->getX() - *cameraX - mouseX;
				float dy = player->getY() - mouseY;
				float length = sqrt(dx * dx + dy * dy);

				float cx = player->getX() - dx / length * collisionCenterOffsetX;
				float cy = player->getY() - dy / length * collisionCenterOffsetY;
				float radius = 13;

				float cdx = getX() - cx;
				float cdy = getY() - cy;

				if (sqrt(cdx * cdx + cdy * cdy) < radius + 5) { //5 == droplet radius?
					this->setOwnerId(PLAYER);
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

					this->setVelx(-velx);
					this->setVely(-vely);

					this->getDrawingSprite()->setRotation(curEntity->getDrawingSprite()->getRotation() + 180);
				}
			}
			if (player->getIsBlocking()) {
				const float collisionCenterOffsetX = 28;
				const float collisionCenterOffsetY = 28;

				// look at mouse
				float dx = player->getX() - *cameraX - mouseX;
				float dy = player->getY() - mouseY;
				float length = sqrt(dx * dx + dy * dy);

				float cx = player->getX() - dx / length * collisionCenterOffsetX;
				float cy = player->getY() - dy / length * collisionCenterOffsetY;
				float radius = 13;

				float cdx = getX() - cx;
				float cdy = getY() - cy;

				if (sqrt(cdx * cdx + cdy * cdy) < radius + 5) { //5 == droplet radius?
					this->setOwnerId(PLAYER);
				}
			}
		}
		if (curEntity->getId() == ENEMY) {
			// detect collision with enemy
			float edx = curEntity->getX() - this->getX();
			float edy = curEntity->getY() - this->getY();

			if (!hasHit && ownerId == PLAYER && sqrt(edx * edx + edy * edy) < 21) {
				Enemy* enemy = (Enemy*)curEntity;
				const int enemyDamage = 34;
				if (enemy->getHealth() > 0) {
					enemy->setHealth(enemy->getHealth() - enemyDamage);
				}
				else {
					enemy->setHealth(0);
				}
				hasHit = true;
			}
		}
		if (curEntity->getId() == BOSS) {
			// detect collision with enemy
			float edx = curEntity->getX() - this->getX();
			float edy = curEntity->getY() - this->getY();

			if (!hasHit && ownerId == PLAYER && sqrt(edx * edx + edy * edy) < 21) {
				Boss* boss = (Boss*)curEntity;
				const int enemyDamage = 34;
				if (boss->getHealth() > 0) {
					boss->setHealth(boss->getHealth() - enemyDamage);
				}
				else {
					boss->setHealth(0);
				}
				hasHit = true;
			}
		}
		curEntity = (Entity*)curEntity->getNext();
	}
}

float Droplet::getVelx() {
	return velx;
}

void Droplet::setVelx(float velx) {
	this->velx = velx;
}

float Droplet::getVely() {
	return vely;
}

void Droplet::setVely(float vely) {
	this->vely = vely;
}

int Droplet::getOwnerId() {
	return ownerId;
}

void Droplet::setOwnerId(int ownerId) {
	this->ownerId = ownerId;
}

bool Droplet::getHastHit() {
	return hasHit;
}

void Droplet::print() {
	using namespace std;
	cout << "I'm a droplet" << endl;
}
