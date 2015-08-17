package com.nox.engine.util;

public class Serializer {
	public static int getIntFromByteBuffer(byte[] buffer, int offset, int nBytes) {
		int retVal = 0;
		for(int i = 0; i < nBytes; i++) {
			retVal = retVal | ((buffer[offset + i] & 0xFF) << (nBytes - 1 - i) * 8);
		}
		return retVal;
	}

	public static byte[] addIntToByteBuffer(byte[] buffer, int num, int offset, int nBytes) {
		for(int i = 0; i < nBytes; i++) {
			buffer[offset + i] = (byte) (num >>> ((nBytes - 1 - i) * 8));
		}
		return buffer;
	}

	public static long getLongFromByteBuffer(byte[] buffer, int offset, int nBytes) {
		long retVal = 0;
		if(nBytes > 4) {
			int leftBufferLen = nBytes - 4;
			int leftInt = getIntFromByteBuffer(buffer, 0, leftBufferLen);
			int rightInt = getIntFromByteBuffer(buffer, leftBufferLen, 4);
			retVal = (leftInt * 4294967296L) + unsignedInt(rightInt);
		}
		else {
			retVal = getIntFromByteBuffer(buffer, offset, nBytes);
		}

		return retVal;
	}

	public static byte[] addLongToByteBuffer(byte[] buffer, long num, int offset, int nBytes) {
		if(nBytes > 4) {
			int leftInt = (int) (num / 4294967296L);
			int leftBufferLen = nBytes - 4;
			addIntToByteBuffer(buffer, leftInt, offset, leftBufferLen);
			addIntToByteBuffer(buffer, (int) num, offset + leftBufferLen, 4);
		}
		else {
			addIntToByteBuffer(buffer, (int) num, offset, nBytes);
		}
		return buffer;
	}

	public static long unsignedInt(int num) {
		int rightByte = num & 0xFF;
		int rightShifted = num >>> 8;

		return (rightShifted * 256L + rightByte);
	}
}
