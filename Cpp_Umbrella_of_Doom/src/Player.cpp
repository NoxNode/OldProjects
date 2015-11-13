#include "Player.h"
#include <iostream>
#include "Settings.h"

#define PI 3.14159265f

Player::Player(LinkedList* entities) :
hasUmbrella(false),
health(100),
isAttacking(false),
isBlocking(false),
stamina(100),
staminaCooldown(0)
{
	setEntities(entities);
	setId(PLAYER);

	//playerSprites
	sf::Texture* playerTexture = new sf::Texture();
	playerTexture->loadFromFile("res/textures/player.png");
	sf::Sprite* playerSprite = new sf::Sprite(*playerTexture);
	playerSprite->setOrigin(playerTexture->getSize().x / 2.0f, playerTexture->getSize().y / 2.0f);

	//playerShield1
	sf::Texture* playerShield1Texture = new sf::Texture();
	playerShield1Texture->loadFromFile("res/textures/playerShield1.png");
	sf::Sprite* playerShield1Sprite = new sf::Sprite(*playerShield1Texture);
	playerShield1Sprite->setOrigin(playerShield1Texture->getSize().x / 2.0f, playerShield1Texture->getSize().y / 2.0f);

	//playerShield2
	sf::Texture* playerShield2Texture = new sf::Texture();
	playerShield2Texture->loadFromFile("res/textures/playerShield2.png");
	sf::Sprite* playerShield2Sprite = new sf::Sprite(*playerShield2Texture);
	playerShield2Sprite->setOrigin(playerShield2Texture->getSize().x / 2.0f, playerShield2Texture->getSize().y / 2.0f);

	//playerShield3
	sf::Texture* playerShield3Texture = new sf::Texture();
	playerShield3Texture->loadFromFile("res/textures/playerShield3.png");
	sf::Sprite* playerShield3Sprite = new sf::Sprite(*playerShield3Texture);
	playerShield3Sprite->setOrigin(playerShield3Texture->getSize().x / 2.0f, playerShield3Texture->getSize().y / 2.0f);

	//playerWithUmbrella
	sf::Texture* playerWithUmbrellaTexture = new sf::Texture();
	playerWithUmbrellaTexture->loadFromFile("res/textures/playerWithUmbrella.png");
	sf::Sprite* playerWithUmbrellaSprite = new sf::Sprite(*playerWithUmbrellaTexture);
	playerWithUmbrellaSprite->setOrigin(playerWithUmbrellaTexture->getSize().x / 2.0f, playerWithUmbrellaTexture->getSize().y / 2.0f);

	//playerAttack1
	sf::Texture* playerAttack1Texture = new sf::Texture();
	playerAttack1Texture->loadFromFile("res/textures/playerAttack1.png");
	sf::Sprite* playerAttack1Sprite = new sf::Sprite(*playerAttack1Texture);
	playerAttack1Sprite->setOrigin(playerAttack1Texture->getSize().x / 2.0f, playerAttack1Texture->getSize().y / 2.0f);

	//playerAttack2
	sf::Texture* playerAttack2Texture = new sf::Texture();
	playerAttack2Texture->loadFromFile("res/textures/playerAttack2.png");
	sf::Sprite* playerAttack2Sprite = new sf::Sprite(*playerAttack2Texture);
	playerAttack2Sprite->setOrigin(playerAttack2Texture->getSize().x / 2.0f, playerAttack2Texture->getSize().y / 2.0f);

	//playerAttack3
	sf::Texture* playerAttack3Texture = new sf::Texture();
	playerAttack3Texture->loadFromFile("res/textures/playerAttack3.png");
	sf::Sprite* playerAttack3Sprite = new sf::Sprite(*playerAttack3Texture);
	playerAttack3Sprite->setOrigin(playerAttack3Texture->getSize().x / 2.0f, playerAttack3Texture->getSize().y / 2.0f);

	const int nPlayerSprites = 8;
	sf::Sprite** playerSprites = new sf::Sprite*[nPlayerSprites];
	playerSprites[0] = playerSprite;
	playerSprites[1] = playerShield1Sprite;
	playerSprites[2] = playerShield2Sprite;
	playerSprites[3] = playerShield3Sprite;
	playerSprites[4] = playerWithUmbrellaSprite;
	playerSprites[5] = playerAttack1Sprite;
	playerSprites[6] = playerAttack2Sprite;
	playerSprites[7] = playerAttack3Sprite;

	setSprites(playerSprites);
	setNSprites(nPlayerSprites);
}

void Player::update(bool w, bool a, bool s, bool d, bool rightMouseButton, bool leftMouseButton, int mouseX, int mouseY, int* cameraX) {
	//move
	if (w) {
		move(0, -PLAYERSPEED);
	}
	if (a) {
		move(-PLAYERSPEED, 0);
	}
	if (s) {
		move(0, PLAYERSPEED);
	}
	if (d) {
		move(PLAYERSPEED, 0);
	}

	//boundries
	if (getX() < 16) {
		setX(16);
	}
	if (getY() < 16) {
		setY(16);
	}

	const int worldWidth = 800 * 4;
	const int worldHeight = 600;

	if (getX() > worldWidth - 16) {
		setX(worldWidth - 16);
	}
	if (getY() > worldHeight - 16) {
		setY(worldHeight - 16);
	}

	//camera
	const int rightCameraBuffer = 500, leftCameraBuffer = 300;

	if (getX() - *cameraX > rightCameraBuffer) {
		*cameraX = getX() - rightCameraBuffer;
	}
	if (getX() - *cameraX < leftCameraBuffer) {
		*cameraX = getX() - leftCameraBuffer;
	}
	if (getX() < leftCameraBuffer) {
		*cameraX = 0;
	}
	if (getX() > worldWidth - leftCameraBuffer) {
		*cameraX = worldWidth - 800;
	}

	// do attack "animation"
	const int framesPerAttackSprite = 3;
	const int framesPerBlockSprite = 3;
	if (hasUmbrella) {
		if (staminaCooldown <= 0) {
			const int attackStaminaCost = 25;
			const int blockStaminaCost = 2;
			const int staminaGain = 2;

			static bool justBlocking = false;
			static bool justAttacking = false;

			bool tookStamina = false;
			if (stamina > 0 && leftMouseButton) {
				stamina -= attackStaminaCost;
				tookStamina = true;
			}
			else {
				tookStamina = false;
			}
			if ((tookStamina || (leftMouseButton || isAttacking))) {
				if (!justAttacking) {
					setAnimationCounter(0);
					justAttacking = true;
				}
				isAttacking = true;
				if (getAnimationCounter() < framesPerBlockSprite) {
					setDrawingSpriteIndex(5);
				}
				else if (getAnimationCounter() < framesPerBlockSprite * 2) {
					setDrawingSpriteIndex(6);
				}
				else if (getAnimationCounter() < framesPerBlockSprite * 3) {
					setDrawingSpriteIndex(7);
				}
				else if (getAnimationCounter() >= framesPerBlockSprite * 4) {
					isAttacking = false;
					setDrawingSpriteIndex(4);
				}
				setAnimationCounter(getAnimationCounter() + 1);
			}
			// do shield "animation"
			else if (rightMouseButton && !isAttacking) {
				if (stamina > 0) {
					if (!justBlocking) {
						setAnimationCounter(0);
						justBlocking = true;
					}
					isBlocking = true;
					stamina -= blockStaminaCost;
					if (getAnimationCounter() < framesPerBlockSprite) {
						setDrawingSpriteIndex(1);
					}
					else if (getAnimationCounter() < framesPerBlockSprite * 2) {
						setDrawingSpriteIndex(2);
					}
					else if (getAnimationCounter() < framesPerBlockSprite * 3) {
						setDrawingSpriteIndex(3);
					}
					setAnimationCounter(getAnimationCounter() + 1);
				}
				else {
					stamina = 0;
					isBlocking = false;
					setDrawingSpriteIndex(4);
				}
			}
			else {
				if (stamina < 100) {
					stamina += staminaGain;
				}
				else {
					stamina = 100;
				}
				setDrawingSpriteIndex(4);
				setAnimationCounter(0);
				isAttacking = false;
				isBlocking = false;
				justBlocking = false;
				justAttacking = false;
			}
		}
		else {
			setDrawingSpriteIndex(4);
			setAnimationCounter(0);
			isAttacking = false;
			isBlocking = false;
		}
	}
	else {
		setDrawingSpriteIndex(0);
		setAnimationCounter(0);
	}
	if (stamina <= 0 && staminaCooldown <= 0) {
		const int StaminaCooldown = 50;
		staminaCooldown = StaminaCooldown;
		stamina = 0;
	}
	if (staminaCooldown > 0) {
		staminaCooldown--;
	}

	// look at mouse
	float dx = getDrawingSprite()->getPosition().x - *cameraX - mouseX;
	float dy = getDrawingSprite()->getPosition().y - mouseY;
	float radians = atan(dy / dx);
	float angle = radians * 180 / PI + 90.0f;

	if (mouseX <= getX() - *cameraX) {
		angle += 180.0f;
	}

	getDrawingSprite()->setRotation(angle);
}

bool Player::getHasUmbrella() {
	return hasUmbrella;
}

void Player::setHasUmbrella(bool value) {
	this->hasUmbrella = value;
}

int Player::getHealth() {
	return health;
}

void Player::setHealth(int health) {
	this->health = health;
}

int Player::getStamina() {
	return stamina;
}

void Player::setStamina(int stamina) {
	this->stamina = stamina;
}

bool Player::getIsAttacking() {
	return isAttacking;
}

bool Player::getIsBlocking() {
	return isBlocking;
}

void Player::print() {
	using namespace std;
	cout << "I'm a player" << endl;
}
