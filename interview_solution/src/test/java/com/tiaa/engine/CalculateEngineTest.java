package com.tiaa.engine;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CalculateEngineTest {

	private static final Logger log = LoggerFactory.getLogger(CalculateEngineTest.class);
	@Value("${ftpDirLocation}")
	private String ftpDirLocation;

	@Autowired
	private CalculateEngine calculateEngine;

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testGetJsonFiles() throws IOException {
		List<String> jsonFiles = calculateEngine.getJsonFiles();
		int noOfFiles = jsonFiles.size();
		Assert.assertEquals(noOfFiles, 3);
	}

	@Test
	public void testGetXmlFiles() throws IOException {
		List<String> xmlFiles = calculateEngine.getXmlFiles();
		assertNotNull(xmlFiles);

	}

	@Test
	public void testReadXml() throws IOException {
		calculateEngine.getXmlFiles();
	}

	@Test(expected = NullPointerException.class)
	public void testReadJson() throws IOException {
		calculateEngine.readJson(null);
	}

	@Test(expected = NullPointerException.class)
	public void testIsMatchCheck() {
		calculateEngine.isMatchCheck(null);
	}

	@Test
	public void testWriteJson() throws JsonGenerationException, JsonMappingException, IOException {
		calculateEngine.writeJson(null, null);

	}

	@Test
	public void testCalculateMatchMismatch() throws Exception {
		calculateEngine.calculateMatchMismatch();

	}

	@Test(expected = NullPointerException.class)
	public void testCrateMatchObject() {
		calculateEngine.crateMatchObject(null);
	}

}
