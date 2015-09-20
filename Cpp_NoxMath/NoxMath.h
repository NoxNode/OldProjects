#pragma once

#include <iostream>

using namespace std;

/*
These functions do integer arithmetic operations using only the OR, AND, and NOT operators to demonstrate how a computer does this at a lower level
*/

// TODO: handle out of range errors
namespace noxmath {
	template <typename T>
	T OR(T a, T b) {
		return a | b;
	}

	template <typename T>
	T AND(T a, T b) {
		return a & b;
	}

	template <typename T>
	T NOT(T a) {
		return ~a;
	}

	template <typename T>
	T NAND(T a, T b) {
		return NOT(AND(a, b));
	}

	template <typename T>
	T NOR(T a, T b) {
		return NOT(OR(a, b));
	}

	template <typename T>
	T XOR(T a, T b) {
		T orRet = OR(a, b);
		T nandRet = NAND(a, b);
		return AND(orRet, nandRet);
	}

	// logical NOT
	template <typename T>
	T lNOT(T a) {
		return XOR(a, 1);
	}

	template <typename T>
	void halfAdd(T in1, T in2, T* out1, T* out2) {
		*out1 = XOR(in1, in2);
		*out2 = AND(in1, in2);
	}

	template <typename T>
	T fullAdd(T a, T b, T nBits) {
		T* digits = new T[nBits];
		for (T i = 0; i < nBits; i++) {
			digits[i] = 0;
		}
		T carryDigit = 0;

		for (T i = 0; i < nBits; i++) {
			T out1 = 0;
			T out2 = 0;
			T out3 = 0;
			halfAdd(AND(a, T(1 << i)) >> i, AND(b, T(1 << i)) >> i, &out1, &out2);
			halfAdd(out1, carryDigit, &digits[i], &out3);
			carryDigit = OR(out3, out2);
		}

		T retVal = 0;
		for (T i = 0; i < nBits; i++) {
			retVal = OR(retVal, (digits[i] << i));
		}

		delete[] digits;
		return retVal;
	}

	template <typename T>
	T negate(T a, T nBits) {
		return fullAdd(NOT(a), T(1), nBits);
	}

	template <typename T>
	void halfSub(T in1, T in2, T* out1, T* out2) {
		*out1 = XOR(in1, in2);
		*out2 = AND(NOT(in1), in2);
	}

	template <typename T>
	T fullSub(T a, T b, T nBits) {
		T* digits = new T[nBits];
		for (T i = 0; i < nBits; i++) {
			digits[i] = 0;
		}
		T carryDigit = 0;

		for (T i = 0; i < nBits; i++) {
			T out1 = 0;
			T out2 = 0;
			T out3 = 0;
			halfSub(AND(a, T(1 << i)) >> i, AND(b, T(1 << i)) >> i, &out1, &out2);
			halfSub(out1, carryDigit, &digits[i], &out3);
			carryDigit = OR(out3, out2);
		}

		T retVal = 0;
		for (T i = 0; i < nBits; i++) {
			retVal = OR(retVal, (digits[i] << i));
		}

		delete[] digits;
		return retVal;
	}

	template <typename T>
	T fullSub(T a, T b, T nBits, T* zeroFlag, T* signFlag) {
		T* digits = new T[nBits];
		for (T i = 0; i < nBits; i++) {
			digits[i] = 0;
		}
		T carryDigit = 0;

		for (T i = 0; i < nBits; i++) {
			T out1 = 0;
			T out2 = 0;
			T out3 = 0;
			halfSub(AND(a, T(1 << i)) >> i, AND(b, T(1 << i)) >> i, &out1, &out2);
			halfSub(out1, carryDigit, &digits[i], &out3);
			carryDigit = OR(out3, out2);
		}

		T retVal = 0;
		*zeroFlag = 1;
		for (T i = 0; i < nBits; i++) {
			retVal = OR(retVal, (digits[i] << i));
			*signFlag = digits[i];
			*zeroFlag = NOR(digits[i], lNOT(*zeroFlag));
		}

		delete[] digits;
		return retVal;
	}

	template <typename T>
	T lessThan(T a, T b, T nBits) {
		T zeroFlag = 0;
		T signFlag = 0;
		fullSub(a, b, nBits, &zeroFlag, &signFlag);
		return signFlag;
	}

	template <typename T>
	T equalTo(T a, T b, T nBits) {
		T zeroFlag = 0;
		T signFlag = 0;
		fullSub(a, b, nBits, &zeroFlag, &signFlag);
		return zeroFlag;
	}

	// TODO: change all comparative and logical operators to use my customized operators
	// TODO: find one loop for all sign cases
	template <typename T>
	T multiply(T num, T multiplier, T nBits) {
		T retVal = 0;

		if (num < 0 && multiplier > 0) {
			for (T i = 0; i < multiplier; i++) {
				retVal = fullSub(retVal, num, nBits);
			}

			return negate(retVal, nBits);
		}
		else if (num >= 0 && multiplier < 0) {
			for (T i = 0; i > multiplier; i--) {
				retVal = fullSub(retVal, num, nBits);
			}

			return retVal;
		}
		else if(num < 0) { // both negative
			for (T i = 0; i > multiplier; i--) {
				retVal = fullAdd(retVal, num, nBits);
			}

			return negate(retVal, nBits);
		}
		else {
			for (T i = 0; lessThan(i, multiplier, nBits); i++) {
				retVal = fullAdd(retVal, num, nBits);
			}

			return retVal;
		}
	}

	// TODO: find one loop for all sign cases
	template <typename T>
	T divide(T num, T divisor, T* remainder, T nBits) {
		T retVal = 0;

		if (lNOT(equalTo(divisor, 0, nBits))) {
			if (num < 0 && divisor > 0) {
				while (num <= negate(divisor, nBits)) {
					num = fullAdd(num, divisor, nBits);
					retVal++;
				}
				*remainder = num;

				return negate(retVal, nBits);
			}
			else if (num >= 0 && divisor < 0) {
				while (negate(num, nBits) <= divisor) {
					num = fullAdd(num, divisor, nBits);
					retVal++;
				}
				*remainder = num;

				return negate(retVal, nBits);
			}
			else if(num < 0) { // both negative
				while (num <= divisor) {
					num = fullSub(num, divisor, nBits);
					retVal++;
				}
				*remainder = num;

				return retVal;
			}
			else {
				while (num >= divisor) {
					num = fullSub(num, divisor, nBits);
					retVal++;
				}
				*remainder = num;

				return retVal;
			}
		}
		else {
			cout << "Error: cannot divide by 0" << endl;
			*remainder = num;
			return 0;
		}
	}
}
