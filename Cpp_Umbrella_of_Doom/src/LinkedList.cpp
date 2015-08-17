#include "LinkedList.h"

LinkedList::LinkedList() :
root(nullptr)
{}

LinkedList::~LinkedList() {
	delete root;
}

void LinkedList::addNode(Node* node) {
	node->setNext(root);
	root = node;
}

Node* LinkedList::getRoot() {
	return root;
}

void LinkedList::setRoot(Node* node) {
	this->root = node;
}

void LinkedList::print() {
	Node* curNode = root;
	while (curNode != nullptr) {
		curNode->print();
		curNode = curNode->getNext();
	}
}
