#pragma once

#include "Entity.h"

class Player : public Entity {
public:
	Player(LinkedList* entities);
	
	void update(bool w, bool a, bool s, bool d, bool rightMouseButton, bool leftMouseButton, int mouseX, int mouseY, int* cameraX);

	bool getHasUmbrella();
	void setHasUmbrella(bool value);

	int getHealth();
	void setHealth(int health);
	int getStamina();
	void setStamina(int stamina);

	bool getIsAttacking();
	bool getIsBlocking();

	virtual void print() override;

private:
	int staminaCooldown;
	bool isAttacking, isBlocking;
	bool hasUmbrella;
	int health, stamina;
};

