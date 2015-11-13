#include "LinkedList.h"

LinkedList::LinkedList() :
root(0)
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
	while (curNode != 0) {
		curNode->print();
		curNode = curNode->getNext();
	}
}
