package net;

import java.util.ArrayList;

public class Serializer {
	/**
	 * @param byteBuffer
	 * @return byteBuffer from int if byteBuffer.length <= 4, else returns -1
	 */
	public static int byteBufferToInt(byte[] byteBuffer) {
		int nBytes = byteBuffer.length;
		int i = 0;
		int temp;

		if(nBytes <= 4) {
			for(int j = 0; j < nBytes; j++) {
				temp = 0;
				temp = toUnsigned(byteBuffer[j]) << ((nBytes - 1) - j) * 8;

				i = i | temp;
			}
			return i;
		}
		return -1;
	}

	private static long byteBufferToInt4Long(byte[] byteBuffer) {
		int nBytes = byteBuffer.length;
		long i = 0;
		int temp;
		for(int j = 0; j < nBytes; j++) {
			temp = 0;
			temp = toUnsigned(byteBuffer[j]) << ((nBytes - 1) - j) * 8;

			i = i + toUnsigned(temp);
		}
		return i;
	}

	public static long byteBufferToLong(byte[] byteBuffer) {
		int nBytes = byteBuffer.length;
		if(nBytes > 4) {
			byte[] bBuffer1 = new byte[nBytes - 4];
			byte[] bBuffer2 = new byte[4];
			for(int i = 0; i < bBuffer1.length; i++) {
				bBuffer1[i] = byteBuffer[i];
			}
			for(int i = bBuffer1.length; i < nBytes; i++) {
				bBuffer2[i - bBuffer1.length] = byteBuffer[i];
			}

			long i1 = byteBufferToInt4Long(bBuffer1);
			long i2 = byteBufferToInt4Long(bBuffer2);

			return (i1 * 4294967296L) | i2;
		}
		return byteBufferToInt4Long(byteBuffer);
	}

	public static byte[] intToByteBuffer(int i, int nBytes) {
		byte[] byteBuffer = null;
		if(nBytes > 0 & nBytes <= 4) {
			byteBuffer = new byte[nBytes];
			for(int j = 0; j < nBytes; j++) {
				byteBuffer[j] = (byte) (i >> (nBytes - 1 - j) * 8);
			}
		}
		return byteBuffer;
	}

	public static byte[] longToByteBuffer(long l, int nBytes) {
		byte[] byteBuffer = null;
		if(nBytes > 0) {
			if(nBytes > 4) {
				byteBuffer = new byte[nBytes];
				int i1 = (int) (l >> 32);
				int i2 = (int) l;
				byte[] bBuffer1 = intToByteBuffer(i1, nBytes - 4);
				byte[] bBuffer2 = intToByteBuffer(i2, 4);
				for(int j = 0; j < nBytes - 4; j++) {
					byteBuffer[j] = bBuffer1[j];
				}
				for(int j = 0; j < 4; j++) {
					byteBuffer[j + nBytes - 4] = bBuffer2[j];
				}
			}
			else if(nBytes == 8) {
				byteBuffer = intToByteBuffer((int) l, nBytes);
			}
		}
		return byteBuffer;
	}

	public static int nBytesUsedInInt(int i) {
		int nBytes = 0;
		if(i < 0) {
			nBytes = 4;
		}
		else if(i < 2) {
			nBytes = 1;
		}
		else if(i < 5536) {
			nBytes = 2;
		}
		else if(i < 1677216) {
			nBytes = 3;
		}
		else {
			nBytes = 4;
		}
		return nBytes;
	}

	public static int nBytesUsedInLong(long l) {
		int nBytes = 0;
		if(l < 0) {
			nBytes = 8;
		}
		else if(l < 256) {
			nBytes = 1;
		}
		else if(l < 65536) {
			nBytes = 2;
		}
		else if(l < 16777216) {
			nBytes = 3;
		}
		else if(l < 4294967296L) {
			nBytes = 4;
		}
		else if(l < 1099511627776L) {
			nBytes = 5;
		}
		else if(l < 281474976710656L) {
			nBytes = 6;
		}
		else if(l < 72057594037927936L) {
			nBytes = 7;
		}
		else {
			nBytes = 8;
		}
		return nBytes;
	}

	public static int toUnsigned(byte b) {
		return b & 0xFF;
	}

	public static long toUnsigned(int i) {
		byte temp = (byte) i;
		i = i >>> 8;
		long retVal = i;
		retVal = retVal << 8;
		retVal = retVal + toUnsigned(temp);
		return retVal;
	}

	public static byte boolToByte(boolean b) {
		return (byte) (b ? 1 : 0);
	}

	/*
	 * public static void main(String[] args) { int mark = 1298231915; byte[]
	 * bytes = intToByteBuffer(mark, 4); System.out.println(new String(bytes));
	 * }
	 */

	/*
	 * public static byte[] serializeArrayList(byte[] id, ArrayList<Object>
	 * objects, String dataTypes, String separator, byte nDigitsAfterDecimal) {
	 * int dataTypeLength = dataTypes.length(); int size = objects.size(); int
	 * idLength = id.length; if (dataTypeLength == size) { int nBytes = 0; for
	 * (int i = 0; i < size; i++) { Object object = objects.get(i); if
	 * (dataTypes.charAt(i) == 'i' || dataTypes.charAt(i) == 'y' ||
	 * dataTypes.charAt(i) == 's') { nBytes += nBytesUsedInInt((int) object); }
	 * else if (dataTypes.charAt(i) == 'f') { nBytes += nBytesUsedInInt((int)
	 * ((float) object * nDigitsAfterDecimal * 10)); } else if
	 * (dataTypes.charAt(i) == 'l') { nBytes += nBytesUsedInLong((long) object);
	 * } else if (dataTypes.charAt(i) == 'd') { nBytes +=
	 * nBytesUsedInLong((long) ((double) object * nDigitsAfterDecimal * 10)); }
	 * else if (dataTypes.charAt(i) == 'b') { nBytes += 1; } } nBytes += size -
	 * 1; //for separators byte[] byteBuffer = new byte[nBytes + idLength + 1];
	 * for(int i = 0; i < idLength; i++) { byteBuffer[i] = id[i]; }
	 * byteBuffer[idLength] = nDigitsAfterDecimal; for(int i = idLength + 1; i <
	 * size; i++) { Object object = objects.get(i); if (dataTypes.charAt(i) ==
	 * 'i') { int nBytesUsed = nBytesUsedInInt((int) object); byte[]
	 * tempByteBuffer = intToByteBuffer((int) object, nBytesUsed); for(int j =
	 * 0; j < nBytesUsed; j++) { byteBuffer[i] = tempByteBuffer[j]; i++; } }
	 * else if (dataTypes.charAt(i) == 'f') { int nBytesUsed =
	 * nBytesUsedInInt((int) ((float) object * nDigitsAfterDecimal * 10));
	 * byte[] tempByteBuffer = intToByteBuffer((int) object, nBytesUsed);
	 * for(int j = 0; j < nBytesUsed; j++) { byteBuffer[i] = tempByteBuffer[j];
	 * i++; } } else if (dataTypes.charAt(i) == 'l') { int nBytesUsed =
	 * nBytesUsedInLong((long) object); byte[] tempByteBuffer =
	 * longToByteBuffer((long) object, nBytesUsed); for(int j = 0; j <
	 * nBytesUsed; j++) { byteBuffer[i] = tempByteBuffer[j]; i++; } } else if
	 * (dataTypes.charAt(i) == 'd') { int nBytesUsed = nBytesUsedInLong((long)
	 * ((double) object * nDigitsAfterDecimal * 10)); byte[] tempByteBuffer =
	 * longToByteBuffer((long) object, nBytesUsed); for(int j = 0; j <
	 * nBytesUsed; j++) { byteBuffer[i] = tempByteBuffer[j]; i++; } } else if
	 * (dataTypes.charAt(i) == 'b') { byteBuffer[i] = boolToByte((boolean)
	 * object); } byte[] temp = separator.getBytes(); for(int j = 0; j <
	 * temp.length; j++) { byteBuffer[i + 1] = temp[j]; i++; } } return
	 * byteBuffer; } return null; }
	 * 
	 * Make this if it ever takes up less bytes than the other
	 * serializeArrayList method
	 * 
	 * @param id
	 * 
	 * @param objects
	 * 
	 * @param dataTypes
	 * 
	 * @param nBytesPerObject
	 * 
	 * @return
	 */
	public static byte[] serializeArrayList(byte[] id,
			ArrayList<Object> objects, String dataTypes, int nBytesPerObject) {

		return null;
	}
}
