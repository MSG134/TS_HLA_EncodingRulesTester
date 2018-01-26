/*
Copyright 2017, Johannes Mulder (Fraunhofer IOSB)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package de.fraunhofer.iosb.tc_lib_encodingrulestester;

public abstract class HlaDataType {
	/**
	 *  Always needed
	 */
	protected String dataTypeName;

	/**
	 *  Always needed
	 */
	protected int alignment;

	/**
	 *  Always needed
	 */
	protected boolean dataSizeFixed;

	/**
	 * 
	 */
	protected static int calcPaddingBytes(final int currentPosition, final int alignment) {
		if (currentPosition % alignment != 0) {
			return alignment - currentPosition % alignment;
		}
		return 0;
	}

	/**
	 * 
	 */
	protected static int calcPaddingBytes(final int currentPosition, final String dataTypeName, final HlaDataTypes dataTypes) {
		HlaDataType dataType = dataTypes.dataTypeMap.get(dataTypeName);
		return calcPaddingBytes(currentPosition, dataType.alignment);
	}

	/**
	 * 
	 */
	protected static int calcAlignment(final int dataSize) {
		int alignment;
		alignment = 1;
		if (dataSize > 4) {
			alignment = 8;
		} else {
			if (dataSize > 2) {
				alignment = 4;
			} else {
				if (dataSize > 1) {
					alignment = 2;
				}
			}
		}
		return alignment;
	}

	/**
	 * 
	 */
	protected long decodeInteger(final byte[] buffer, final int currentPosition, final String elementType) {
		long testVal = 0;

		switch(elementType) {
		case "HLAinteger16BE":
			testVal =+ buffer[currentPosition + 0];
			testVal <<= 8;
			testVal =+ buffer[currentPosition + 1];
			break;
		case "HLAinteger32BE":
			testVal =+ buffer[currentPosition + 0];
			testVal <<= 8;
			testVal =+ buffer[currentPosition + 1];
			testVal <<= 8;
			testVal =+ buffer[currentPosition + 2];
			testVal <<= 8;
			testVal =+ buffer[currentPosition + 3];
			break;
		case "HLAinteger64BE":
			testVal =+ buffer[currentPosition + 0];
			testVal <<= 8;
			testVal =+ buffer[currentPosition + 1];
			testVal <<= 8;
			testVal =+ buffer[currentPosition + 2];
			testVal <<= 8;
			testVal =+ buffer[currentPosition + 3];
			testVal <<= 8;
			testVal =+ buffer[currentPosition + 4];
			testVal <<= 8;
			testVal =+ buffer[currentPosition + 5];
			testVal <<= 8;
			testVal =+ buffer[currentPosition + 6];
			testVal <<= 8;
			testVal =+ buffer[currentPosition + 7];
			break;
		case "HLAinteger16LE":
			testVal =+ buffer[currentPosition + 1];
			testVal <<= 8;
			testVal =+ buffer[currentPosition + 0];
			break;
		case "HLAinteger32LE":
			testVal =+ buffer[currentPosition + 3];
			testVal <<= 8;
			testVal =+ buffer[currentPosition + 2];
			testVal <<= 8;
			testVal =+ buffer[currentPosition + 1];
			testVal <<= 8;
			testVal =+ buffer[currentPosition + 0];
			break;
		case "HLAinteger64LE":
			testVal =+ buffer[currentPosition + 7];
			testVal <<= 8;
			testVal =+ buffer[currentPosition + 6];
			testVal <<= 8;
			testVal =+ buffer[currentPosition + 5];
			testVal <<= 8;
			testVal =+ buffer[currentPosition + 4];
			testVal <<= 8;
			testVal =+ buffer[currentPosition + 3];
			testVal <<= 8;
			testVal =+ buffer[currentPosition + 2];
			testVal <<= 8;
			testVal =+ buffer[currentPosition + 1];
			testVal <<= 8;
			testVal =+ buffer[currentPosition + 0];
			break;
		case "HLAoctet":
			testVal =+ buffer[currentPosition + 0];
			break;
		case "UnsignedShort":
			testVal =+ buffer[currentPosition + 0];
			testVal <<= 8;
			testVal =+ buffer[currentPosition + 1];
			break;
		}
		
		return testVal;
	}

	/**
	 * 
	 * @param dataTypeName
	 * @return
	 */
	public static int getBasicDataSizeBits(String dataTypeName) {
		int basicDataSize = 0;
		switch(dataTypeName) {
		case "HLAinteger16BE":
			basicDataSize = 16;
			break;
		case "HLAinteger32BE":
			basicDataSize = 32;
			break;
		case "HLAinteger64BE":
			basicDataSize = 64;
			break;
		case "HLAfloat32BE":
			basicDataSize = 32;
			break;
		case "HLAfloat64BE":
			basicDataSize = 64;
			break;
		case "HLAoctetPairBE":
			basicDataSize = 16;
			break;
		case "HLAinteger16LE":
			basicDataSize = 16;
			break;
		case "HLAinteger32LE":
			basicDataSize = 32;
			break;
		case "HLAinteger64LE":
			basicDataSize = 64;
			break;
		case "HLAfloat32LE":
			basicDataSize = 32;
			break;
		case "HLAfloat64LE":
			basicDataSize = 64;
			break;
		case "HLAoctetPairLE":
			basicDataSize = 16;
			break;
		case "HLAoctet":
			basicDataSize = 8;
			break;
		case "UnsignedShort":
			basicDataSize = 16;
			break;
		}
		return basicDataSize;
	}

	/**
	 * 
	 * @param dataTypeName
	 * @return
	 */
	public static boolean getBigEndian(String dataTypeName) {
		boolean bigEndian = false;
		switch(dataTypeName) {
		case "HLAinteger16BE":
			bigEndian = true;
			break;
		case "HLAinteger32BE":
			bigEndian = true;
			break;
		case "HLAinteger64BE":
			bigEndian = true;
			break;
		case "HLAfloat32BE":
			bigEndian = true;
			break;
		case "HLAfloat64BE":
			bigEndian = true;
			break;
		case "HLAoctetPairBE":
			bigEndian = true;
			break;
		case "HLAinteger16LE":
			bigEndian = false;
			break;
		case "HLAinteger32LE":
			bigEndian = false;
			break;
		case "HLAinteger64LE":
			bigEndian = false;
			break;
		case "HLAfloat32LE":
			bigEndian = false;
			break;
		case "HLAfloat64LE":
			bigEndian = false;
			break;
		case "HLAoctetPairLE":
			bigEndian = false;
			break;
		case "HLAoctet":
			bigEndian = true;
			break;
		case "UnsignedShort":
			bigEndian = true;
			break;
		}
		return bigEndian;
	}
	/**
	 * @return the alignment of the dataype
	 */
	public abstract int getAlignment(final HlaDataTypes dataTypes);

	/**
	 * 
	 * @return whether the datatype size is fixed
	 */
	public abstract boolean getDataSizeFixed();
	
	/**
	 * 
	 * @return the datatype size
	 */
	public abstract int getDataSize();
	
	/**
	 * 
	 * @return the element datatype name
	 */
	public abstract String getElementTypeName();

	/**
	 * @param buffer the buffer from HLA reflectAttributeValue of receiveInteraction parameter
	 * @param currentPosition position of data to process
	 * @param dataTypes the map of datatype information from FOM/SOM
	 * @return the current position of next unprocessed data
	 * @throws EncodingRulesException
	 */
	public abstract int testBuffer(final byte[] buffer, final int currentPosition, final HlaDataTypes dataTypes) throws EncodingRulesException;
}