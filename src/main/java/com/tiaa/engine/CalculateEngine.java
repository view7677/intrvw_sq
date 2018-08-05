package com.tiaa.engine;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tiaa.domain.json.Branch;
import com.tiaa.domain.json.Cmfoodchain;
import com.tiaa.domain.json.Match;
import com.tiaa.domain.json.Restaurant;

import generated.CmfoodchainType;
import generated.OrderdetailType;

@Component
public class CalculateEngine {

	private static final Logger log = LoggerFactory.getLogger(CalculateEngine.class);

	@Value("${ftpDirLocation}")
	private String ftpDirLocation;
	
	@Value("${outputDirLocation}")
	private String outputDirLocation;

	@Autowired
	private Jaxb2Marshaller marshaller;

	@Autowired
	CronScheduler cronScheduler;

	List<String> getJsonFiles() throws IOException {

		List<String> jsonFileList = new ArrayList<>();
		Files.newDirectoryStream(Paths.get(ftpDirLocation), path -> path.toString().endsWith(".json"))
				.forEach(filePath -> jsonFileList.add(filePath.toString()));
		return jsonFileList;
	}

	/**
	 * Listing xml files at ftpDirLocation.
	 * 
	 * @return
	 * @throws IOException
	 */
	List<String> getXmlFiles() throws IOException {
		List<String> xmlFileList = new ArrayList<>();
		Files.newDirectoryStream(Paths.get(ftpDirLocation), path -> path.toString().endsWith(".xml"))
				.forEach(filePath -> xmlFileList.add(filePath.toString()));
		return xmlFileList;
	}

	/**
	 * unmarshaling XML data into Object.
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	CmfoodchainType readXml(String path) throws IOException {
		Path source = Paths.get(path);
		CmfoodchainType cmfoodchainType2 = null;
		String xmlData = new String(Files.readAllBytes(source));
		JAXBElement<CmfoodchainType> cmfoodchainType1 = (JAXBElement<CmfoodchainType>) marshaller
				.unmarshal(new StreamSource(new StringReader(xmlData)));
		cmfoodchainType2 = cmfoodchainType1.getValue();

		log.info("readXml-->{}", cmfoodchainType2);
		return cmfoodchainType2;
	}

	/**
	 * Parsing Json data.
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	CmfoodchainType readJson(String path) throws IOException {
		Path source = Paths.get(path);
		String jsonInput = new String(Files.readAllBytes(source));
		ObjectMapper jsonMapper = new ObjectMapper();
		Restaurant cmfoodchainType = jsonMapper.readValue(jsonInput, Restaurant.class);
		log.info("readJson-->{}", cmfoodchainType);
		return cmfoodchainType.getCmfoodchain();
	}

	/**
	 * isMatchCheck method checks whether sumOfOrder is matching with
	 * TotalCollection
	 * 
	 * @param cmfoodchainType
	 * @return boolean
	 */
	boolean isMatchCheck(CmfoodchainType cmfoodchainType) {
		boolean isMatch = false;
		float sum = 0;

		for (OrderdetailType o : cmfoodchainType.getOrders().getOrderdetail()) {
			sum = sum + o.getBillamount();
		}
		if (sum == cmfoodchainType.getBranch().getTotalcollection()) {
			log.info("Match");
			isMatch = true;
		} else {
			log.info("Mismatch");
			isMatch = false;
		}
		return isMatch;
	}

	/**
	 * 
	 * @param fileName
	 * @param obj
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	void writeJson(String fileName, Object obj) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		objectMapper.writeValue(new File(outputDirLocation+fileName), obj);
	}

	public void calculateMatchMismatch() throws Exception {

		List<CmfoodchainType> matchList = new ArrayList<>();
		List<CmfoodchainType> misMatchList = new ArrayList<>();
		List<String> xmlFiles = getXmlFiles();

		for (String xmlFile : xmlFiles) {
			CmfoodchainType cmfoodchainType = readXml(xmlFile);

			if (isMatchCheck(cmfoodchainType)) {
				matchList.add(cmfoodchainType);
			} else {
				misMatchList.add(cmfoodchainType);
			}
		}
		List<String> jsonFiles = getJsonFiles();
		for (String jsonFile : jsonFiles) {
			CmfoodchainType cmfoodchainType = readJson(jsonFile);
			if (isMatchCheck(cmfoodchainType)) {
				matchList.add(cmfoodchainType);
			} else {
				misMatchList.add(cmfoodchainType);
			}
		}

		writeJson("Match.json", crateMatchObject(matchList));
		writeJson("Mismatch.json", crateMatchObject(misMatchList));
		log.info("done");

	}

	/**
	 * 
	 * @param matchList
	 * @return
	 */
	Match crateMatchObject(List<CmfoodchainType> matchList) {
		Match match = new Match();
		Cmfoodchain cmfoodchain = new Cmfoodchain();
		Branch[] branches = new Branch[matchList.size()];
		int i = 0;
		for (CmfoodchainType cmfoodchainType : matchList) {
			Branch branch = new Branch();
			branch.setLocation(cmfoodchainType.getBranch().getLocation());
			branch.setTotalcollection("" + cmfoodchainType.getBranch().getTotalcollection());

			float sumoforder = 0;
			if (cmfoodchainType.getOrders() != null) {

				for (OrderdetailType o : cmfoodchainType.getOrders().getOrderdetail()) {
					sumoforder = sumoforder + o.getBillamount();
				}
			}
			branch.setSumoforder("" + sumoforder);
			branch.setLocationid(cmfoodchainType.getBranch().getLocationid());
			branches[i] = branch;
			i++;
		}

		cmfoodchain.setBranch(branches);
		match.setCmfoodchain(cmfoodchain);
		return match;
	}
}
