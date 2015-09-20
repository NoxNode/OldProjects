#include <iostream>
#include "NoxMath.h"

using namespace std;

int main() {
	cout << "This program does integer arithmetic operations using only the OR, AND, and NOT operators to demonstrate how a computer does this at a lower level (ctrl + c to stop executing)" << endl << endl;

	int a;
	int b;
	int remainder = 0;
	const int bits = 32;

	while (true) {
		cout << "Enter the first number" << endl;
		cin >> a;
		cout << "Enter the second number" << endl;
		cin >> b;

		cout << a << " + " << b << " = " << noxmath::fullAdd(a, b, bits) << endl;
		cout << a << " - " << b << " = " << noxmath::fullSub(a, b, bits) << endl;
		cout << a << " x " << b << " = " << noxmath::multiply(a, b, bits) << endl;
		cout << a << " / " << b << " = " << noxmath::divide(a, b, &remainder, bits);
		cout << " with remainder " << remainder << endl;
	}
}
