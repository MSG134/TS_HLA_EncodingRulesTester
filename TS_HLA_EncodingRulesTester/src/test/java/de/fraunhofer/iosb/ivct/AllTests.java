package de.fraunhofer.iosb.ivct;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import de.fraunhofer.iosb.tc_lib.TcInconclusive;
import de.fraunhofer.iosb.tc_lib_encodingrulestester.DataTreeBuilder;
import de.fraunhofer.iosb.tc_lib_encodingrulestester.EncodingRulesException;
import de.fraunhofer.iosb.tc_lib_encodingrulestester.HlaDataType;
import de.fraunhofer.iosb.tc_lib_encodingrulestester.HlaDataTypes;
import de.fraunhofer.iosb.tc_lib_encodingrulestester.HlaDataVariableArrayType;
import de.fraunhofer.iosb.tc_lib_encodingrulestester.ObjectClassData;
import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ParameterHandle;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.InvalidInteractionClassHandle;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;

/*
 * This Java source file was auto generated by running 'gradle init --type java-library'
 * by 'mul' at '07.04.17 10:59' with Gradle 2.9
 *
 * @author mul, @date 07.04.17 10:59
 */
public class AllTests {
	static HlaDataTypes hlaDataTypes = new HlaDataTypes();
	static Map<AttributeHandle, String> attributeHandleDataTypeMap = new HashMap<AttributeHandle, String>();
	static Map<ParameterHandle, String> parameterHandleDataTypeMap = new HashMap<ParameterHandle, String>();
	static RtiJunitImpl dummyRtiImpl = new RtiJunitImpl();

    public byte hexToByte(String hexString) {
        int firstDigit = toDigit(hexString.charAt(0));
        int secondDigit = toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }

    private int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if(digit == -1) {
            throw new IllegalArgumentException(
              "Invalid Hexadecimal Character: "+ hexChar);
        }
        return digit;
    }

    @BeforeClass
	public static void initDataTypeTree() {
//		The following calls are not needed, since there is no real RTI involved
//		dummyRtiImpl.connect(federateReference, callbackModel);
//		dummyRtiImpl.createFederationExecution(federationExecutionName, fomModule);
//		dummyRtiImpl.joinFederationExecution(federateType, federationExecutionName);
		Map<InteractionClassHandle, Set<ParameterHandle>> interactionClassHandleMap = new HashMap<InteractionClassHandle, Set<ParameterHandle>>();
		Map<ObjectClassHandle, ObjectClassData> objectClassAttributeHandleMap = new HashMap<ObjectClassHandle, ObjectClassData>();
		File f = new File("src/test/resources/RPR_FOM_v2.0_1516-2010.xml");
		URL[] somUrls = new URL[1];
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
			somUrls[0] = f.toURI().toURL();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			DataTreeBuilder dataTreeBuilder = new DataTreeBuilder(dummyRtiImpl, hlaDataTypes, interactionClassHandleMap, parameterHandleDataTypeMap, objectClassAttributeHandleMap, attributeHandleDataTypeMap);
			for (int i = 0; i < somUrls.length; i++) {
				Document document = builder.parse(somUrls[i].toString());
				Element elem = document.getDocumentElement();
				if (dataTreeBuilder.buildData(elem)) {
		            throw new TcInconclusive("EncodingRulesTesterBaseModel.processSOM: error in dataTreeBuilder.buildData");
				}
			}
	        dummyRtiImpl.setGenerateNewHandles(false);
	        HlaDataVariableArrayType.setRPRv2_0();
		} catch (TcInconclusive e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testEmitterType() {
		try {
			ObjectClassHandle och = dummyRtiImpl.getObjectClassHandle("HLAobjectRoot.EmbeddedSystem.EmitterSystem");
			AttributeHandle ah = dummyRtiImpl.getAttributeHandle(och, "EmitterType");
			String dataType = attributeHandleDataTypeMap.get(ah);
			String dataTypeNotNullStr = "attributeHandleDataTypeMap.get() delivers null for EmitterType";
			assertNotNull(dataTypeNotNullStr, dataType);
			HlaDataType hlaDataType = hlaDataTypes.dataTypeMap.get(dataType);
			String notNullStr = "hlaDataTypes.dataTypeMap.get() delivers null for " + hlaDataType;
			assertNotNull(notNullStr);

			//------------------------------------------------------------------------------------------------------------------------
			// Attribute test data
			byte[] buffer = new byte[2];
			buffer[0] = hexToByte("07");
			buffer[1] = hexToByte("99");
			try {
				assertTrue (hlaDataType.testBuffer(buffer, 0, hlaDataTypes) == buffer.length);
			} catch (EncodingRulesException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NameNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FederateNotExecutionMember e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnected e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RTIinternalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidObjectClassHandle e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Test
	public void testEventIdentifier() {
		try {
			ObjectClassHandle och = dummyRtiImpl.getObjectClassHandle("HLAobjectRoot.EmbeddedSystem.EmitterSystem");
			AttributeHandle ah = dummyRtiImpl.getAttributeHandle(och, "EventIdentifier");
			String dataType = attributeHandleDataTypeMap.get(ah);
			String dataTypeNotNullStr = "attributeHandleDataTypeMap.get() delivers null for EventIdentifier";
			assertNotNull(dataTypeNotNullStr, dataType);
			HlaDataType hlaDataType = hlaDataTypes.dataTypeMap.get(dataType);
			String notNullStr = "hlaDataTypes.dataTypeMap.get() delivers null for " + hlaDataType;
			assertNotNull(notNullStr);

			//------------------------------------------------------------------------------------------------------------------------
			// Attribute test data
			byte[] buffer = new byte[3];
			buffer[0] = hexToByte("00");
			buffer[1] = hexToByte("00");
			buffer[2] = hexToByte("00");
			try {
				assertTrue (hlaDataType.testBuffer(buffer, 0, hlaDataTypes) == buffer.length);
			} catch (EncodingRulesException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NameNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FederateNotExecutionMember e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnected e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RTIinternalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidObjectClassHandle e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Test
	public void testHostObjectIdentifier() {
		try {
			ObjectClassHandle och = dummyRtiImpl.getObjectClassHandle("HLAobjectRoot.EmbeddedSystem.EmitterSystem");
			AttributeHandle ah = dummyRtiImpl.getAttributeHandle(och, "HostObjectIdentifier");
			String dataType = attributeHandleDataTypeMap.get(ah);
			String dataTypeNotNullStr = "attributeHandleDataTypeMap.get() delivers null for HostObjectIdentifier";
			assertNotNull(dataTypeNotNullStr, dataType);
			HlaDataType hlaDataType = hlaDataTypes.dataTypeMap.get(dataType);
			String notNullStr = "hlaDataTypes.dataTypeMap.get() delivers null for " + hlaDataType;
			assertNotNull(notNullStr);

			//------------------------------------------------------------------------------------------------------------------------
			// Attribute test data
			byte[] buffer = new byte[24];
			buffer[0] = hexToByte("56");
			buffer[1] = hexToByte("52");
			buffer[2] = hexToByte("46");
			buffer[3] = hexToByte("46");
			buffer[4] = hexToByte("65");
			buffer[5] = hexToByte("64");
			buffer[6] = hexToByte("65");
			buffer[7] = hexToByte("72");
			buffer[8] = hexToByte("61");
			buffer[9] = hexToByte("74");
			buffer[10] = hexToByte("65");
			buffer[11] = hexToByte("48");
			buffer[12] = hexToByte("61");
			buffer[13] = hexToByte("6e");
			buffer[14] = hexToByte("64");
			buffer[15] = hexToByte("6c");
			buffer[16] = hexToByte("65");
			buffer[17] = hexToByte("3c");
			buffer[18] = hexToByte("32");
			buffer[19] = hexToByte("3e");
			buffer[20] = hexToByte("3a");
			buffer[21] = hexToByte("31");
			buffer[22] = hexToByte("31");
			buffer[23] = hexToByte("00");
			try {
				assertTrue (hlaDataType.testBuffer(buffer, 0, hlaDataTypes) == buffer.length);
			} catch (EncodingRulesException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NameNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FederateNotExecutionMember e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnected e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RTIinternalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidObjectClassHandle e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Test
	public void testEntityType() {
		try {
			ObjectClassHandle och = dummyRtiImpl.getObjectClassHandle("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.SurfaceVessel");
			AttributeHandle ah = dummyRtiImpl.getAttributeHandle(och, "EntityType");
			String dataType = attributeHandleDataTypeMap.get(ah);
			String dataTypeNotNullStr = "attributeHandleDataTypeMap.get() delivers null for EntityType";
			assertNotNull(dataTypeNotNullStr, dataType);
			HlaDataType hlaDataType = hlaDataTypes.dataTypeMap.get(dataType);
			String notNullStr = "hlaDataTypes.dataTypeMap.get() delivers null for " + hlaDataType;
			assertNotNull(notNullStr);

			//------------------------------------------------------------------------------------------------------------------------
			// Attribute test data
			byte[] buffer = new byte[8];
			buffer[0] = hexToByte("01");
			buffer[1] = hexToByte("03");
			buffer[2] = hexToByte("00");
			buffer[3] = hexToByte("0d");
			buffer[4] = hexToByte("06");
			buffer[5] = hexToByte("01");
			buffer[6] = hexToByte("01");
			buffer[7] = hexToByte("00");
			try {
				assertTrue (hlaDataType.testBuffer(buffer, 0, hlaDataTypes) == buffer.length);
			} catch (EncodingRulesException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NameNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FederateNotExecutionMember e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnected e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RTIinternalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidObjectClassHandle e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Test
	public void testEntityIdentifier() {
		try {
			ObjectClassHandle och = dummyRtiImpl.getObjectClassHandle("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.SurfaceVessel");
			AttributeHandle ah = dummyRtiImpl.getAttributeHandle(och, "EntityIdentifier");
			String dataType = attributeHandleDataTypeMap.get(ah);
			String dataTypeNotNullStr = "attributeHandleDataTypeMap.get() delivers null for EntityIdentifier";
			assertNotNull(dataTypeNotNullStr, dataType);
			HlaDataType hlaDataType = hlaDataTypes.dataTypeMap.get(dataType);
			String notNullStr = "hlaDataTypes.dataTypeMap.get() delivers null for " + hlaDataType;
			assertNotNull(notNullStr);

			//------------------------------------------------------------------------------------------------------------------------
			// Attribute test data
			byte[] buffer = new byte[6];
			buffer[0] = hexToByte("00");
			buffer[1] = hexToByte("01");
			buffer[2] = hexToByte("00");
			buffer[3] = hexToByte("02");
			buffer[4] = hexToByte("00");
			buffer[5] = hexToByte("01");
			try {
				assertTrue (hlaDataType.testBuffer(buffer, 0, hlaDataTypes) == buffer.length);
			} catch (EncodingRulesException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NameNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FederateNotExecutionMember e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnected e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RTIinternalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidObjectClassHandle e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Test
	public void testMarking() {
		try {
			ObjectClassHandle och = dummyRtiImpl.getObjectClassHandle("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.SurfaceVessel");
			AttributeHandle ah = dummyRtiImpl.getAttributeHandle(och, "Marking");
			String dataType = attributeHandleDataTypeMap.get(ah);
			String dataTypeNotNullStr = "attributeHandleDataTypeMap.get() delivers null for Marking";
			assertNotNull(dataTypeNotNullStr, dataType);
			HlaDataType hlaDataType = hlaDataTypes.dataTypeMap.get(dataType);
			String notNullStr = "hlaDataTypes.dataTypeMap.get() delivers null for " + hlaDataType;
			assertNotNull(notNullStr);

			//------------------------------------------------------------------------------------------------------------------------
			// Attribute test data
			byte[] buffer = new byte[12];
			buffer[0] = hexToByte("01");
			buffer[1] = hexToByte("53");
			buffer[2] = hexToByte("68");
			buffer[3] = hexToByte("69");
			buffer[4] = hexToByte("70");
			buffer[5] = hexToByte("00");
			buffer[6] = hexToByte("00");
			buffer[7] = hexToByte("00");
			buffer[8] = hexToByte("00");
			buffer[9] = hexToByte("00");
			buffer[10] = hexToByte("00");
			buffer[11] = hexToByte("00");
			try {
				assertTrue (hlaDataType.testBuffer(buffer, 0, hlaDataTypes) == buffer.length);
			} catch (EncodingRulesException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NameNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FederateNotExecutionMember e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnected e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RTIinternalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidObjectClassHandle e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/*
	@Test
	public void testRadarBeam() {
		try {
			ObjectClassHandle och = dummyRtiImpl.getObjectClassHandle("HLAobjectRoot.EmitterBeam.RadarBeam");
			AttributeHandle ah = dummyRtiImpl.getAttributeHandle(och, "TrackObjectIdentifiers");
			String dataType = attributeHandleDataTypeMap.get(ah);
			String dataTypeNotNullStr = "attributeHandleDataTypeMap.get() delivers null for TrackObjectIdentifiers";
			assertNotNull(dataTypeNotNullStr, dataType);
			HlaDataType hlaDataType = hlaDataTypes.dataTypeMap.get(dataType);
			String notNullStr = "hlaDataTypes.dataTypeMap.get() delivers null for " + hlaDataType;
			assertNotNull(notNullStr);

			//------------------------------------------------------------------------------------------------------------------------
			// Attribute test data
			byte[] buffer = new byte[3];
			buffer[0] = hexToByte("00");
			buffer[1] = hexToByte("01");
			buffer[2] = hexToByte("00");
			try {
				assertTrue (hlaDataType.testBuffer(buffer, 0, hlaDataTypes) == buffer.length);
			} catch (EncodingRulesException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NameNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FederateNotExecutionMember e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnected e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RTIinternalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidObjectClassHandle e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	*/

	@Test
	public void testEmitterType5570() {
		try {
			ObjectClassHandle och = dummyRtiImpl.getObjectClassHandle("HLAobjectRoot.EmbeddedSystem.EmitterSystem");
			AttributeHandle ah = dummyRtiImpl.getAttributeHandle(och, "EmitterType");
			String dataType = attributeHandleDataTypeMap.get(ah);
			String dataTypeNotNullStr = "attributeHandleDataTypeMap.get() delivers null for EmitterType";
			assertNotNull(dataTypeNotNullStr, dataType);
			HlaDataType hlaDataType = hlaDataTypes.dataTypeMap.get(dataType);
			String notNullStr = "hlaDataTypes.dataTypeMap.get() delivers null for " + hlaDataType;
			assertNotNull(notNullStr);

			//------------------------------------------------------------------------------------------------------------------------
			// Attribute test data
			byte[] buffer = new byte[2];
			buffer[0] = hexToByte("15");
			buffer[1] = hexToByte("c2");
			try {
				assertTrue (hlaDataType.testBuffer(buffer, 0, hlaDataTypes) == buffer.length);
			} catch (EncodingRulesException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NameNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FederateNotExecutionMember e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnected e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RTIinternalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidObjectClassHandle e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Test
	public void testSpatial() {
		try {
			ObjectClassHandle och = dummyRtiImpl.getObjectClassHandle("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.SurfaceVessel");
			AttributeHandle ah = dummyRtiImpl.getAttributeHandle(och, "Spatial");
			String dataType = attributeHandleDataTypeMap.get(ah);
			String dataTypeNotNullStr = "attributeHandleDataTypeMap.get() delivers null for Spatial";
			assertNotNull(dataTypeNotNullStr, dataType);
			HlaDataType hlaDataType = hlaDataTypes.dataTypeMap.get(dataType);
			String notNullStr = "hlaDataTypes.dataTypeMap.get() delivers null for " + hlaDataType;
			assertNotNull(notNullStr);

			//------------------------------------------------------------------------------------------------------------------------
			// Attribute test data
			byte[] buffer = new byte[60];
			buffer[0] = hexToByte("02");
			buffer[1] = hexToByte("00");
			buffer[2] = hexToByte("00");
			buffer[3] = hexToByte("00");
			buffer[4] = hexToByte("00");
			buffer[5] = hexToByte("00");
			buffer[6] = hexToByte("00");
			buffer[7] = hexToByte("00");
			buffer[8] = hexToByte("41");
			buffer[9] = hexToByte("47");
			buffer[10] = hexToByte("da");
			buffer[11] = hexToByte("51");
			buffer[12] = hexToByte("6c");
			buffer[13] = hexToByte("52");
			buffer[14] = hexToByte("59");
			buffer[15] = hexToByte("8c");
			buffer[16] = hexToByte("41");
			buffer[17] = hexToByte("52");
			buffer[18] = hexToByte("37");
			buffer[19] = hexToByte("f0");
			buffer[20] = hexToByte("d1");
			buffer[21] = hexToByte("94");
			buffer[22] = hexToByte("71");
			buffer[23] = hexToByte("f8");
			buffer[24] = hexToByte("41");
			buffer[25] = hexToByte("45");
			buffer[26] = hexToByte("a3");
			buffer[27] = hexToByte("0b");
			buffer[28] = hexToByte("b6");
			buffer[29] = hexToByte("56");
			buffer[30] = hexToByte("6d");
			buffer[31] = hexToByte("9d");
			buffer[32] = hexToByte("00");
			buffer[33] = hexToByte("00");
			buffer[34] = hexToByte("00");
			buffer[35] = hexToByte("00");
			buffer[36] = hexToByte("bf");
			buffer[37] = hexToByte("14");
			buffer[38] = hexToByte("62");
			buffer[39] = hexToByte("38");
			buffer[40] = hexToByte("24");
			buffer[41] = hexToByte("fc");
			buffer[42] = hexToByte("8d");
			buffer[43] = hexToByte("f1");
			buffer[44] = hexToByte("40");
			buffer[45] = hexToByte("02");
			buffer[46] = hexToByte("36");
			buffer[47] = hexToByte("a2");
			buffer[48] = hexToByte("41");
			buffer[49] = hexToByte("05");
			buffer[50] = hexToByte("dd");
			buffer[51] = hexToByte("f4");
			buffer[52] = hexToByte("c0");
			buffer[53] = hexToByte("af");
			buffer[54] = hexToByte("44");
			buffer[55] = hexToByte("55");
			buffer[56] = hexToByte("a6");
			buffer[57] = hexToByte("ec");
			buffer[58] = hexToByte("c5");
			buffer[59] = hexToByte("12");
			try {
				assertTrue (hlaDataType.testBuffer(buffer, 0, hlaDataTypes) == buffer.length);
			} catch (EncodingRulesException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NameNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FederateNotExecutionMember e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnected e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RTIinternalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidObjectClassHandle e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}