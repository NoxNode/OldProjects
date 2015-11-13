#pragma once

#include "Entity.h"

class Droplet : public Entity {
public:
	Droplet(LinkedList* entities);
	void update(int* cameraX, int mouseX, int mouseY, int difficulty);

	float getVelx();
	void setVelx(float velx);
	float getVely();
	void setVely(float vely);

	int getOwnerId();
	void setOwnerId(int ownerId);

	bool getHastHit();

	virtual void print() override;

private:
	bool hasHit;
	float velx, vely;
	int ownerId;
};
