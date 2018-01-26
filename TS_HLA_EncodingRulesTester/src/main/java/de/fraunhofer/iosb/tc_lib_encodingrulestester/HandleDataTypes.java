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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.fraunhofer.iosb.tc_lib_encodingrulestester.AlternativeStringPair;
import de.fraunhofer.iosb.tc_lib_encodingrulestester.HlaDataBasicType;
import de.fraunhofer.iosb.tc_lib_encodingrulestester.HlaDataEnumType;
import de.fraunhofer.iosb.tc_lib_encodingrulestester.HlaDataFixedArrayType;
import de.fraunhofer.iosb.tc_lib_encodingrulestester.HlaDataFixedRecordType;
import de.fraunhofer.iosb.tc_lib_encodingrulestester.HlaDataType;
import de.fraunhofer.iosb.tc_lib_encodingrulestester.HlaDataTypes;
import de.fraunhofer.iosb.tc_lib_encodingrulestester.HlaDataVariableArrayType;
import de.fraunhofer.iosb.tc_lib_encodingrulestester.HlaDataVariantRecordType;
import de.fraunhofer.iosb.tc_lib_encodingrulestester.HlaDataSimpleType;

public class HandleDataTypes {
    private static Logger logger = LoggerFactory.getLogger(HandleDataTypes.class);
	HlaDataTypes hlaDataTypes;
	/**
	 * 
	 * @param theSelectedNode the Xerces node at this level
	 * @return true means error
	 */
	private boolean decodeBasicData(Node theSelectedNode) {
		boolean gotName = false;
		boolean gotSize = false;
		boolean gotEndian = false;
		String nameStr = null;
		boolean bigEndian = false;
		int size = 0;

		/*
		 * Loop for all data for one basicDataType
		 */
		for (Node child = theSelectedNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			// ---------------------------------------------------------------------------
			try {
				if (child.getNodeName().equals("name")) {
					if (((Element) child).getFirstChild() != null) {
						nameStr = ((Element) child).getFirstChild().getNodeValue();
						gotName = true;
						logger.trace("HandleDataTypes.decodeBasicData: BasicDataName: " + nameStr);
					}
					continue;
				}
				if (child.getNodeName().equals("size")) {
					if (((Element) child).getFirstChild() != null) {
						String textStr = ((Element) child).getFirstChild().getNodeValue();
						size = Integer.parseInt(textStr);
						gotSize = true;
						logger.trace("HandleDataTypes.decodeBasicData: BasicDataSize: " + size);
					}
					continue;
				}
				if (child.getNodeName().equals("endian")) {
					if (((Element) child).getFirstChild() != null) {
						String textStr = ((Element) child).getFirstChild().getNodeValue();
						if (textStr.equalsIgnoreCase("Big")) {
							bigEndian = true;
						}
						gotEndian = true;
						logger.trace("HandleDataTypes.decodeBasicData: BasicDataEndian: " + bigEndian);
					}
					continue;
				}
			} catch (Exception e) {
				logger.error("HandleDataTypes.decodeBasicData: " + e);
				return true;
			}
		}

		/*
		 * All elements found
		 */
		if (gotName && gotSize && gotEndian) {
			HlaDataBasicType hlaDataTypeBasic0 = new HlaDataBasicType(nameStr, size, bigEndian);
			/*
			 * Check if we already have the dataType
			 */
			HlaDataType tmpHlaDataType = hlaDataTypes.dataTypeMap.get(nameStr);
			if (tmpHlaDataType == null) {
				hlaDataTypes.dataTypeMap.put(nameStr, hlaDataTypeBasic0);
			} else {
				if (tmpHlaDataType instanceof HlaDataBasicType) {
					HlaDataBasicType tmpType = (HlaDataBasicType) tmpHlaDataType;
					if (tmpType.equalTo(hlaDataTypeBasic0)) {
						logger.info("HandleDataTypes.decodeBasicData: EQUAL DATA TYPE: " + nameStr + " IGNORED");
					} else {
						logger.info("HandleDataTypes.decodeBasicData: UNEQUAL DATA TYPE: " + nameStr + " NOT MERGED");
					}
				} else {
					logger.info("HandleDataTypes.decodeBasicData: UNEQUAL DATA TYPES: " + nameStr + " NOT MERGED");
				}
			}
			return false;
		}

		/*
		 * Incomplete data
		 */
		if (gotName == false) {
			logger.error("HandleDataTypes.decodeBasicData: missing name");
		}
		if (gotSize == false) {
			logger.error("HandleDataTypes.decodeBasicData: missing size");
		}
		if (gotEndian == false) {
			logger.error("HandleDataTypes.decodeBasicData: missing endian");
		}

		return true;
	}

	/**
	 * 
	 * @param theSelectedNode the Xerces node at this level
	 * @return true means error
	 */
	private boolean decodeBasicDataTypes(Node theSelectedNode) {
		String textPointer = null;

		/*
		 * Loop for all basicDataTypes
		 */
		for (Node child = theSelectedNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			// ---------------------------------------------------------------------------
			try {
				if (child.getNodeName().equals("name")) {
					if (((Element) child).getFirstChild() != null) {
						textPointer = ((Element) child).getFirstChild().getNodeValue();
						logger.trace("basicDataName: " + textPointer);
					}
					continue;
				}
				if (child.getNodeName().equals("basicData")) {
					if (decodeBasicData(child)) {
						return true;
					}
					continue;
				}
			} catch (Exception e) {
				logger.error("HandleDataTypes.decodeBasicDataTypes: " + e);
				return true;
			}
		}
		return false;
	}
	
	private HlaDataBasicType checkAddBasicDefault(final String basicTypeName) {
		HlaDataBasicType hlaDataTypeBasic0 = null;
		HlaDataType tmpHlaDataType = hlaDataTypes.dataTypeMap.get(basicTypeName);

		if (tmpHlaDataType == null) {
			int size = HlaDataType.getBasicDataSizeBits(basicTypeName);
			if (size != 0) {
				boolean bigEndian = HlaDataType.getBigEndian(basicTypeName);
				hlaDataTypeBasic0 = new HlaDataBasicType(basicTypeName, size, bigEndian);
				hlaDataTypes.dataTypeMap.put(basicTypeName, hlaDataTypeBasic0);
			} else {
				logger.trace("decodeSimpleData: unknown basicDataType " + basicTypeName + " NOT MERGED");
				return null;
			}
		} else {
			if (tmpHlaDataType instanceof HlaDataBasicType) {
				hlaDataTypeBasic0 = (HlaDataBasicType) tmpHlaDataType;
			}
		}

		/*
		 * If the representation basicDataType not found, do not create the simpleDataType
		 */
		if (hlaDataTypeBasic0 == null) {
			logger.info("decodeSimpleData: cannot get " + basicTypeName + " NOT MERGED");
			return null;
		}

		return hlaDataTypeBasic0;
	}

	/**
	 * 
	 * @param theSelectedNode the Xerces node at this level
	 * @return true means error
	 */
	private boolean decodeSimpleData(Node theSelectedNode) {
		boolean gotName = false;
		boolean gotRepresentation = false;
		String nameStr = null;
		String representationStr = null;
		HlaDataBasicType hlaDataTypeBasic0 = null;
		HlaDataSimpleType hlaDataSimpleType0 = null;

		/*
		 * Loop for all data for one simpleDataType
		 */
		for (Node child = theSelectedNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			// ---------------------------------------------------------------------------
			try {
				if (child.getNodeName().equals("name")) {
					if (((Element) child).getFirstChild() != null) {
						nameStr = ((Element) child).getFirstChild().getNodeValue();
						gotName = true;
						logger.trace("SimpleDataName: " + nameStr);
					}
					continue;
				}
				if (child.getNodeName().equals("representation")) {
					if (((Element) child).getFirstChild() != null) {
						representationStr = ((Element) child).getFirstChild().getNodeValue();
						gotRepresentation = true;
						logger.trace("SimpleDataRepresentation: " + representationStr);
					}
					continue;
				}
			} catch (Exception e) {
				logger.trace("HandleDataTypes.decodeSimpleData: " + e);
				return true;
			}
		}

		/*
		 * All elements found
		 */
		if (gotName && gotRepresentation) {
			/*
			 * Check if we already have the basicDataType, if not add a standard basicDataType
			 */
			hlaDataTypeBasic0 = checkAddBasicDefault(representationStr);
			if (hlaDataTypeBasic0 == null) {
				return true;
			}

			/*
			 * Create the simpleDataType
			 */
			hlaDataSimpleType0 = new HlaDataSimpleType(nameStr, hlaDataTypeBasic0);

			/*
			 * Check if we already have the dataType
			 */
			HlaDataType tmpHlaDataType = hlaDataTypes.dataTypeMap.get(nameStr);
			if (tmpHlaDataType == null) {
				hlaDataTypes.dataTypeMap.put(nameStr, hlaDataSimpleType0);
			} else {
				if (tmpHlaDataType instanceof HlaDataSimpleType) {
					HlaDataSimpleType tmpType = (HlaDataSimpleType) tmpHlaDataType;
					if (tmpType.equalTo(hlaDataSimpleType0)) {
						logger.info("EQUAL DATA TYPE: " + nameStr + " IGNORED");
					} else {
						logger.info("UNEQUAL DATA TYPE: " + nameStr + " NOT MERGED");
					}
				}
			}
			return false;
		}

		/*
		 * Incomplete data
		 */
		if (gotName == false) {
			logger.error("HandleDataTypes.decodeSimpleData: missing name");
		}
		if (gotRepresentation == false) {
			logger.error("HandleDataTypes.decodeSimpleData: missing representation");
		}

		return true;
	}

	/**
	 * 
	 * @param theSelectedNode the Xerces node at this level
	 * @return true means error
	 */
	private boolean decodeSimpleDataTypes(Node theSelectedNode) {
		String textPointer = null;

		/*
		 * Loop for all simpleDataTypes
		 */
		for (Node child = theSelectedNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			// ---------------------------------------------------------------------------
			try {
				if (child.getNodeName().equals("name")) {
					if (((Element) child).getFirstChild() != null) {
						textPointer = ((Element) child).getFirstChild().getNodeValue();
						logger.trace("simpleDataName: " + textPointer);
					}
					continue;
				}
				if (child.getNodeName().equals("simpleData")) {
					if (decodeSimpleData(child)) {
						return true;
					}
					continue;
				}
			} catch (Exception e) {
				logger.error("HandleDataTypes.decodeSimpleDataTypes: " + e);
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param theSelectedNode the Xerces node at this level
	 * @return true means error
	 */
	private boolean decodeEnumerator(final Node theSelectedNode, final Map<Long, String> enumValueMap) {
		boolean gotName = false;
		boolean gotValue = false;
		String nameStr = null;
		String valueStr = null;

		for (Node child = theSelectedNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			// ---------------------------------------------------------------------------
			try {
				if (child.getNodeName().equals("name")) {
					if (((Element) child).getFirstChild() != null) {
						nameStr = ((Element) child).getFirstChild().getNodeValue();
						gotName = true;
						logger.trace("EnumeratorName: " + nameStr);
					}
					continue;
				}
				if (child.getNodeName().equals("value")) {
					if (((Element) child).getFirstChild() != null) {
						valueStr = ((Element) child).getFirstChild().getNodeValue();
						gotValue = true;
						logger.trace("EnumeratorValue: " + valueStr);
					}
					continue;
				}
			} catch (Exception e) {
				logger.error("HandleDataTypes.decodeEnumerator: " + e);
				return true;
			}
		}
		
		if (gotName && gotValue) {
			long l = Long.parseLong(valueStr);
			enumValueMap.put(l, nameStr);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param theSelectedNode the Xerces node at this level
	 * @return true means error
	 */
	private boolean decodeEnumeratedData(Node theSelectedNode) {
		boolean gotEnumerator = false;
		boolean gotName = false;
		boolean gotRepresentation = false;
		String nameStr = null;
		String representationStr = null;
		HlaDataBasicType hlaDataTypeBasic0 = null;
		HlaDataEnumType hlaDataTypeEnumerated0 = null;
		Map<Long, String> enumValueMap = new LinkedHashMap<Long, String>();


		for (Node child = theSelectedNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			// ---------------------------------------------------------------------------
			try {
				if (child.getNodeName().equals("name")) {
					if (((Element) child).getFirstChild() != null) {
						nameStr = ((Element) child).getFirstChild().getNodeValue();
						gotName = true;
						logger.trace("HandleDataTypes.decodeEnumeratedData: EnumeratedName: " + nameStr);
					}
					continue;
				}
				if (child.getNodeName().equals("representation")) {
					if (((Element) child).getFirstChild() != null) {
						representationStr = ((Element) child).getFirstChild().getNodeValue();
						gotRepresentation = true;
						logger.trace("HandleDataTypes.decodeEnumeratedData: EnumeratedRepresentation: " + representationStr);
					}
					continue;
				}
				if (child.getNodeName().equals("enumerator")) {
					if (decodeEnumerator(child, enumValueMap)) {
						return true;
					}
					gotEnumerator = true;
					continue;
				}
			} catch (Exception e) {
				logger.error("HandleDataTypes.decodeEnumeratedData: " + e);
				return true;
			}
		}

		/*
		 * All elements found
		 */
		if (gotName && gotRepresentation && gotEnumerator) {
			/*
			 * Check if we already have the basicDataType, if not add a standard basicDataType
			 */
			hlaDataTypeBasic0 = checkAddBasicDefault(representationStr);
			if (hlaDataTypeBasic0 == null) {
				return true;
			}

			/*
			 * Create the enumDataType
			 */
			hlaDataTypeEnumerated0 = new HlaDataEnumType(nameStr, hlaDataTypeBasic0, enumValueMap);

			/*
			 * Check if we already have the dataType
			 */
			HlaDataType tmpHlaDataType = hlaDataTypes.dataTypeMap.get(nameStr);
			if (tmpHlaDataType == null) {
				hlaDataTypes.dataTypeMap.put(nameStr, hlaDataTypeEnumerated0);
			} else {
				if (tmpHlaDataType instanceof HlaDataEnumType) {
					HlaDataEnumType tmpType = (HlaDataEnumType) tmpHlaDataType;
					if (tmpType.equalTo(hlaDataTypeEnumerated0)) {
						logger.info("HandleDataTypes.decodeEnumeratedData: EQUAL DATA TYPE: " + nameStr + " IGNORED");
					} else {
						logger.info("HandleDataTypes.decodeEnumeratedData: UNEQUAL DATA TYPE: " + nameStr + " NOT MERGED");
					}
				}
			}
			return false;
		}

		/*
		 * Incomplete data
		 */
		if (gotName == false) {
			logger.error("HandleDataTypes.decodeEnumeratedData: missing name");
		}
		if (gotRepresentation == false) {
			logger.error("HandleDataTypes.decodeEnumeratedData: missing representation");
		}
		if (gotEnumerator == false) {
			logger.error("HandleDataTypes.decodeEnumeratedData: missing enumerator");
		}

		return true;
	}

	/**
	 * 
	 * @param theSelectedNode the Xerces node at this level
	 * @return true means error
	 */
	private boolean decodeEnumeratedDataTypes(Node theSelectedNode) {
		for (Node child = theSelectedNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			// ---------------------------------------------------------------------------
			try {
				if (child.getNodeName().equals("enumeratedData")) {
					if (decodeEnumeratedData(child)) {
						return true;
					}
					continue;
				}
			} catch (Exception e) {
				logger.error("HandleDataTypes.decodeEnumeratedDataTypes: " + e);
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param theSelectedNode the Xerces node at this level
	 * @return true means error
	 */
	private boolean decodeArrayData(Node theSelectedNode) {
		boolean gotName = false;
		boolean gotDataType = false;
		boolean gotCardinality = false;
		String nameStr = null;
		String dataTypeStr = null;
		String cardinalityStr = null;

		for (Node child = theSelectedNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			// ---------------------------------------------------------------------------
			try {
				if (child.getNodeName().equals("name")) {
					if (((Element) child).getFirstChild() != null) {
						nameStr = ((Element) child).getFirstChild().getNodeValue();
						gotName = true;
						logger.trace("ArrayDataName: " + nameStr);
					}
					continue;
				}
				if (child.getNodeName().equals("dataType")) {
					if (((Element) child).getFirstChild() != null) {
						dataTypeStr = ((Element) child).getFirstChild().getNodeValue();
						gotDataType = true;
						logger.trace("ArrayDataType: " + dataTypeStr);
					}
					continue;
				}
				if (child.getNodeName().equals("cardinality")) {
					if (((Element) child).getFirstChild() != null) {
						cardinalityStr = ((Element) child).getFirstChild().getNodeValue();
						gotCardinality = true;
						logger.trace("ArrayDataCardinality: " + cardinalityStr);
					}
					continue;
				}
			} catch (Exception e) {
				logger.error("HandleDataTypes.decodeArrayData: " + e);
				return true;
			}
		}
		
		/*
		 * Process the dataType
		 */
		if (gotName && gotDataType && gotCardinality) {
			HlaDataType tmpHlaDataType = hlaDataTypes.dataTypeMap.get(nameStr);
			HlaDataType tmpHlaDataTypeElement = hlaDataTypes.dataTypeMap.get(dataTypeStr);

			
			
			// TODO
			if (tmpHlaDataTypeElement == null) {
				return false;
			}

			
			
			
			
			
			if (cardinalityStr.equals("Dynamic")) {
				HlaDataVariableArrayType hlaDataTypeVariableArray = new HlaDataVariableArrayType(nameStr, tmpHlaDataTypeElement);
				if (tmpHlaDataType == null) {
					hlaDataTypes.dataTypeMap.put(nameStr, hlaDataTypeVariableArray);
				} else {
					if (tmpHlaDataType instanceof HlaDataVariableArrayType) {
						HlaDataVariableArrayType hlaDataVariableArrayType = (HlaDataVariableArrayType) tmpHlaDataType;
						if (hlaDataVariableArrayType.equalTo(hlaDataTypeVariableArray)) {
							logger.trace("HandleDataTypes.decodeFixedRecordData: EQUAL DATA TYPE: " + nameStr + " IGNORED");
						} else {
							logger.trace("HandleDataTypes.decodeFixedRecordData: UNEQUAL DATA TYPE: " + nameStr + " NOT MERGED");
						}
					} else {
						logger.trace("HandleDataTypes.decodeFixedRecordData: UNEQUAL DATA TYPES: " + nameStr + " NOT MERGED");
					}
				}
			} else {
				HlaDataFixedArrayType hlaDataTypeFixedArray = new HlaDataFixedArrayType(nameStr, dataTypeStr, 4, true, 3);
				if (tmpHlaDataType == null) {
					hlaDataTypes.dataTypeMap.put(nameStr, hlaDataTypeFixedArray);
				} else {
					if (tmpHlaDataType instanceof HlaDataFixedArrayType) {
						HlaDataFixedArrayType hlaDataVariableArrayType = (HlaDataFixedArrayType) tmpHlaDataType;
						if (hlaDataVariableArrayType.equalTo(hlaDataVariableArrayType)) {
							logger.trace("HandleDataTypes.decodeFixedRecordData: EQUAL DATA TYPE: " + nameStr + " IGNORED");
						} else {
							logger.trace("HandleDataTypes.decodeFixedRecordData: UNEQUAL DATA TYPE: " + nameStr + " NOT MERGED");
						}
					} else {
						logger.trace("HandleDataTypes.decodeFixedRecordData: UNEQUAL DATA TYPES: " + nameStr + " NOT MERGED");
					}
				}
			}
			return false;
		}

		/*
		 * Incomplete data
		 */
		if (gotName) {
			logger.error("HandleDataTypes.decodeArrayData: missing name");
		}
		if (gotDataType) {
			logger.error("HandleDataTypes.decodeArrayData: missing dataType");
		}
		if (gotCardinality) {
			logger.error("HandleDataTypes.decodeArrayData: missing cardinality");
		}

		return true;
	}

	/**
	 * 
	 * @param theSelectedNode the Xerces node at this level
	 * @return true means error
	 */
	private boolean decodeArrayDataTypes(Node theSelectedNode) {
		for (Node child = theSelectedNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			// ---------------------------------------------------------------------------
			try {
				if (child.getNodeName().equals("arrayData")) {
					if (decodeArrayData(child)) {
						return true;
					}
					continue;
				}
			} catch (Exception e) {
				logger.error("HandleDataTypes.decodeArrayDataTypes: " + e);
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param theSelectedNode the Xerces node at this level
	 * @return true means error
	 */
	private boolean decodeField(final Node theSelectedNode, final Map<String, String> fields) {
		boolean gotName = false;
		boolean gotDataType = false;
		String nameStr = null;
		String dataTypeStr = null;

		for (Node child = theSelectedNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			// ---------------------------------------------------------------------------
			try {
				if (child.getNodeName().equals("name")) {
					if (((Element) child).getFirstChild() != null) {
						nameStr = ((Element) child).getFirstChild().getNodeValue();
						gotName = true;
						logger.trace("FieldName: " + nameStr);
					}
					continue;
				}
				if (child.getNodeName().equals("dataType")) {
					if (((Element) child).getFirstChild() != null) {
						dataTypeStr = ((Element) child).getFirstChild().getNodeValue();
						gotDataType = true;
						logger.trace("FieldDataType: " + dataTypeStr);
					}
					continue;
				}
			} catch (Exception e) {
				logger.error("HandleDataTypes.decodeField: " + e);
				return true;
			}
		}
		
		/*
		 * Check if we already have the dataType
		 */
		HlaDataType tmpHlaDataType = hlaDataTypes.dataTypeMap.get(nameStr);
		if (tmpHlaDataType != null) {
			logger.trace("HandleDataTypes.decodeField dataType not found: " + nameStr);
			return true;
		}
		
		if (gotName && gotDataType) {
			String s = fields.get(nameStr);
			if (s == null) {
				fields.put(nameStr, dataTypeStr);
			} else {
				if (s.equals(dataTypeStr) == false) {
					logger.trace("HandleDataTypes.decodeField: duplicate field key " + nameStr);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param theSelectedNode the Xerces node at this level
	 * @return true means error
	 */
	private boolean decodeFixedRecordData(Node theSelectedNode) {
		boolean gotName = false;
		boolean gotEncoding = false;
		boolean gotField = false;
		String nameStr = null;
		String textPointer = null;
		Map<String, String> fields = new HashMap<String, String>();

		for (Node child = theSelectedNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			// ---------------------------------------------------------------------------
			try {
				if (child.getNodeName().equals("name")) {
					if (((Element) child).getFirstChild() != null) {
						nameStr = ((Element) child).getFirstChild().getNodeValue();
						gotName = true;
						logger.trace("FixedRecordDataName: " + nameStr);
					}
					continue;
				}
				if (child.getNodeName().equals("encoding")) {
					if (((Element) child).getFirstChild() != null) {
						textPointer = ((Element) child).getFirstChild().getNodeValue();
						gotEncoding = true;
						logger.trace("FixedRecordDataEncoding: " + textPointer);
					}
					continue;
				}
				if (child.getNodeName().equals("field")) {
					if (decodeField(child, fields)) {
						return true;
					}
					gotField = true;
					continue;
				}
			} catch (Exception e) {
				logger.trace("HandleDataTypes.decodeFixedRecordData: " + e);
				return true;
			}
		}

		if (gotName && gotEncoding && gotField) {
			HlaDataFixedRecordType hlaDataTypeFixedRecord = new HlaDataFixedRecordType(nameStr, fields, false);

			HlaDataFixedRecordType tmpHlaDataType = (HlaDataFixedRecordType) hlaDataTypes.dataTypeMap.get(nameStr);
			if (tmpHlaDataType == null) {
				hlaDataTypes.dataTypeMap.put(nameStr, hlaDataTypeFixedRecord);
			} else {
				if (tmpHlaDataType.equalTo(hlaDataTypeFixedRecord)) {
					logger.trace("HandleDataTypes.decodeFixedRecordData: EQUAL DATA TYPE: " + nameStr + " IGNORED");
				} else {
					logger.trace("HandleDataTypes.decodeFixedRecordData: EQUAL DATA TYPE: " + nameStr + " NOT MERGED");
				}
			}
			return false;
		}

		/*
		 * Incomplete data
		 */
		if (gotName) {
			logger.error("HandleDataTypes.decodeFixedRecordData: missing name");
		}
		if (gotEncoding) {
			logger.error("HandleDataTypes.decodeFixedRecordData: missing encoding");
		}
		if (gotField) {
			logger.error("HandleDataTypes.decodeFixedRecordData: missing field");
		}

		return true;
	}

	/**
	 * 
	 * @param theSelectedNode the Xerces node at this level
	 * @return true means error
	 */
	private boolean decodeFixedRecordDataTypes(Node theSelectedNode) {
		for (Node child = theSelectedNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			// ---------------------------------------------------------------------------
			try {
				if (child.getNodeName().equals("fixedRecordData")) {
					if (decodeFixedRecordData(child)) {
						return true;
					}
					continue;
				}
			} catch (Exception e) {
				logger.error("HandleDataTypes.decodeFixedRecordDataTypes: " + e);
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param theSelectedNode the Xerces node at this level
	 * @return true means error
	 */
	private boolean decodeAlternative(Node theSelectedNode, Map<String, AlternativeStringPair> alternativeMap) {
		boolean gotEnumerator = false;
		boolean gotName = false;
		boolean gotDataType = false;
		String enumeratorStr = null;
		String nameStr = null;
		String dataTypeStr = null;

		for (Node child = theSelectedNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			// ---------------------------------------------------------------------------
			try {
				if (child.getNodeName().equals("enumerator")) {
					if (((Element) child).getFirstChild() != null) {
						enumeratorStr = ((Element) child).getFirstChild().getNodeValue();
						gotEnumerator = true;
						logger.trace("AlternativeEnumerator: " + enumeratorStr);
					}
					continue;
				}
				if (child.getNodeName().equals("name")) {
					if (((Element) child).getFirstChild() != null) {
						nameStr = ((Element) child).getFirstChild().getNodeValue();
						gotName = true;
						logger.trace("AlternativeName: " + nameStr);
					}
					continue;
				}
				if (child.getNodeName().equals("dataType")) {
					if (((Element) child).getFirstChild() != null) {
						dataTypeStr = ((Element) child).getFirstChild().getNodeValue();
						gotDataType = true;
						logger.trace("AlternativeDataType: " + dataTypeStr);
					}
					continue;
				}
			} catch (Exception e) {
				logger.error("HandleDataTypes.decodeAlternative: " + e);
				return true;
			}
		}

		if (gotEnumerator && gotName && gotDataType) {
			AlternativeStringPair alternativeStringPair = new AlternativeStringPair(nameStr, dataTypeStr);
			AlternativeStringPair tmpAlternativeStringPair = alternativeMap.get(enumeratorStr);
			if (tmpAlternativeStringPair != null) {
				if (alternativeStringPair.equalTo(tmpAlternativeStringPair) == false) {
					logger.trace("HandleDataTypes.decodeAlternative: alternativeStringPair enumeratorStr differ IGNORED");
					return true;
				}
			}
			alternativeMap.put(enumeratorStr, alternativeStringPair);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param theSelectedNode the Xerces node at this level
	 * @return true means error
	 */
	private boolean decodeVariantRecordData(Node theSelectedNode) {
		boolean gotName = false;
		boolean gotDiscriminant = false;
		boolean gotDataType = false;
		boolean gotAlternative = false;
		Map<String, AlternativeStringPair> alternativeMap = new HashMap<String, AlternativeStringPair>();
		String nameStr = null;
		String discriminantStr = null;
		String dataTypeStr = null;

		for (Node child = theSelectedNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			// ---------------------------------------------------------------------------
			try {
				if (child.getNodeName().equals("name")) {
					if (((Element) child).getFirstChild() != null) {
						nameStr = ((Element) child).getFirstChild().getNodeValue();
						gotName = true;
						logger.trace("VariantRecordName: " + nameStr);
					}
					continue;
				}
				if (child.getNodeName().equals("discriminant")) {
					if (((Element) child).getFirstChild() != null) {
						discriminantStr = ((Element) child).getFirstChild().getNodeValue();
						gotDiscriminant = true;
						logger.trace("VariantRecordDiscriminant: " + discriminantStr);
					}
					continue;
				}
				if (child.getNodeName().equals("dataType")) {
					if (((Element) child).getFirstChild() != null) {
						dataTypeStr = ((Element) child).getFirstChild().getNodeValue();
						gotDataType = true;
						logger.trace("VariantRecordDataType: " + dataTypeStr);
					}
					continue;
				}
				if (child.getNodeName().equals("alternative")) {
					if (decodeAlternative(child, alternativeMap)) {
						return true;
					}
					gotAlternative = true;
					continue;
				}
			} catch (Exception e) {
				logger.error("HandleDataTypes.decodeVariantRecordData: " + e);
				return true;
			}
		}
		
		if (gotName && gotDiscriminant && gotDataType && gotAlternative) {
			HlaDataVariantRecordType hlaDataTypeVariantRecord = new HlaDataVariantRecordType(nameStr, discriminantStr, dataTypeStr, alternativeMap);
			/*
			 * Check if we already have the dataType
			 */
			HlaDataType tmpHlaDataType = hlaDataTypes.dataTypeMap.get(nameStr);
			if (tmpHlaDataType == null) {
				hlaDataTypes.dataTypeMap.put(nameStr, hlaDataTypeVariantRecord);
			} else {
				if (tmpHlaDataType instanceof HlaDataVariantRecordType) {
					HlaDataVariantRecordType tmpType = (HlaDataVariantRecordType) tmpHlaDataType;
					if (tmpType.equalTo(hlaDataTypeVariantRecord)) {
						logger.trace("HandleDataTypes.decodeVariantRecordData: EQUAL DATA TYPE: " + nameStr + " IGNORED");
					} else {
						logger.trace("HandleDataTypes.decodeVariantRecordData: UNEQUAL DATA TYPE: " + nameStr + " NOT MERGED");
					}
				} else {
					logger.trace("HandleDataTypes.decodeVariantRecordData: UNEQUAL DATA TYPES: " + nameStr + " NOT MERGED");
				}
			}
			return false;
		}

		/*
		 * Incomplete data
		 */
		if (gotName) {
			logger.error("HandleDataTypes.decodeVariantRecordData: missing name");
		}
		if (gotDiscriminant) {
			logger.error("HandleDataTypes.decodeVariantRecordData: missing discriminant");
		}
		if (gotDataType) {
			logger.error("HandleDataTypes.decodeVariantRecordData: missing dataType");
		}
		if (gotAlternative) {
			logger.error("HandleDataTypes.decodeVariantRecordData: missing alternative");
		}

		return true;
	}

	/**
	 * 
	 * @param theSelectedNode the Xerces node at this level
	 * @return true means error
	 */
	private boolean decodeVariantRecordDataTypes(Node theSelectedNode) {
		for (Node child = theSelectedNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			// ---------------------------------------------------------------------------
			try {
				if (child.getNodeName().equals("variantRecordData")) {
					if (decodeVariantRecordData(child)) {
						return true;
					}
					continue;
				}
			} catch (Exception e) {
				logger.error("HandleDataTypes.decodeVariantRecordDataTypes: " + e);
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param theSelectedNode the Xerces node at this level
	 * @return true means error
	 */
	boolean decode(final Node theSelectedNode, final HlaDataTypes hlaDataTypes) {
		String textPointer = null;

		this.hlaDataTypes = hlaDataTypes;
		if (theSelectedNode.getNodeType() == Node.ELEMENT_NODE) {
			textPointer = theSelectedNode.getNodeName();
			logger.trace("decode: " + textPointer);
		}
		// Need basicDataRepresentations first, all others refer to these
		for (Node child = theSelectedNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			textPointer = child.getNodeName();
			if (child.getNodeName().equals("basicDataRepresentations")) {
				logger.trace("Got basicDataRepresentations!");
				if (decodeBasicDataTypes(child)) {
					return true;
				}
				continue;
			}
		}
		// Need simpleDataTypes and enumeratedDataTypes next, refer to basicDataRepresentations only and
		// are building stones for more complex types
		for (Node child = theSelectedNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			if (child.getNodeName().equals("simpleDataTypes")) {
				logger.trace("Got simpleDataTypes!");
				if (decodeSimpleDataTypes(child)) {
					return true;
				}
				continue;
			}
			if (child.getNodeName().equals("enumeratedDataTypes")) {
				logger.trace("Got enumeratedDataTypes!");
				if (decodeEnumeratedDataTypes(child)) {
					return true;
				}
				continue;
			}
		}
		// All others refer to above in some manner
		for (Node child = theSelectedNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			if (child.getNodeName().equals("arrayDataTypes")) {
				logger.trace("Got arrayDataTypes!");
				if (decodeArrayDataTypes(child)) {
					return true;
				}
				continue;
			}
			if (child.getNodeName().equals("fixedRecordDataTypes")) {
				logger.trace("Got fixedRecordDataTypes!");
				if (decodeFixedRecordDataTypes(child)) {
					return true;
				}
				continue;
			}
			if (child.getNodeName().equals("variantRecordDataTypes")) {
				logger.trace("Got variantRecordDataTypes!");
				if (decodeVariantRecordDataTypes(child)) {
					return true;
				}
				continue;
			}
		}
		return false;
	}
}