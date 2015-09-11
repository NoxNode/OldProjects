#include "Umbrella.h"
#include "Player.h"
#include <iostream>

Umbrella::Umbrella(LinkedList* entities) {
	setEntities(entities);
	setId(UMBRELLA);

	sf::Texture* umbrellaTexutre = new sf::Texture();
	umbrellaTexutre->loadFromFile("res/textures/umbrella.png");
	sf::Sprite* umbrellaSprite = new sf::Sprite(*umbrellaTexutre);
	umbrellaSprite->setOrigin(umbrellaTexutre->getSize().x / 2.0f, umbrellaTexutre->getSize().y / 2.0f);

	const int nUmbrellaSprites = 1;
	sf::Sprite** umbrellaSprites = new sf::Sprite*[nUmbrellaSprites];
	umbrellaSprites[0] = umbrellaSprite;

	setSprites(umbrellaSprites);
	setNSprites(nUmbrellaSprites);
}

void Umbrella::update() {
	LinkedList* entities = getEntities();
	Entity* curEntity = (Entity*)entities->getRoot();
	while (curEntity != 0) {
		if (curEntity->getId() == PLAYER) {
			float playerX = curEntity->getX() - curEntity->getDrawingSprite()->getTexture()->getSize().x / 2;
			float playerY = curEntity->getY() - curEntity->getDrawingSprite()->getTexture()->getSize().y / 2;
			float playerWidth = curEntity->getDrawingSprite()->getTexture()->getSize().x;
			float playerHeight = curEntity->getDrawingSprite()->getTexture()->getSize().y;

			float x = this->getX() - this->getDrawingSprite()->getTexture()->getSize().x / 2;
			float y = this->getY() - this->getDrawingSprite()->getTexture()->getSize().y / 2;
			float width = this->getDrawingSprite()->getTexture()->getSize().x;
			float height = this->getDrawingSprite()->getTexture()->getSize().y;

			if (playerX + playerWidth > x && playerX < x + width && playerY + playerHeight > y && playerY < y + height) {
				((Player*)(curEntity))->setHasUmbrella(true);
			}
		}
		curEntity = (Entity*)curEntity->getNext();
	}
}

void Umbrella::print() {
	using namespace std;
	cout << "I'm an umbrella" << endl;
}
