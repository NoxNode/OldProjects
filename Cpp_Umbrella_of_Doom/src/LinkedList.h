#pragma once

#include "Node.h"

class LinkedList {
public:
	LinkedList();
	~LinkedList();

	void addNode(Node* node);
	Node* getRoot();
	void setRoot(Node* node);

	void print();

private:
	Node* root;
};
