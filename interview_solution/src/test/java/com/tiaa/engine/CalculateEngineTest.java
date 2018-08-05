package com.tiaa.engine;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//@RunWith(SpringRunner.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = EngineApplication.class)
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootTest(properties="application.properties")
public class CalculateEngineTest {

	private static final Logger log = LoggerFactory.getLogger(CalculateEngineTest.class);
	@Value("${ftpDirLocation}")
	private String ftpDirLocation;
	@InjectMocks
	private CalculateEngine calculateEngine;

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testGetJsonFiles() {
		try {
			List<String> jsonFiles = calculateEngine.getJsonFiles();
			int noOfFiles = jsonFiles.size();
			Assert.assertEquals(noOfFiles, 3);
		} catch (IOException e) {
			log.error("testGetJsonFiles :", e);
		}
	}

	@Test
	public void testGetXmlFiles() {
		try {
			calculateEngine.getXmlFiles();
		} catch (IOException e) {
			log.error("testGetXmlFiles :", e);
		}
	}

	@Test
	public void testReadXml() {
		try {
			calculateEngine.getXmlFiles();
		} catch (IOException e) {
			log.error("testReadXml :", e);
		}
	}

	@Test
	public void testReadJson() {
		try {
			calculateEngine.readJson(null);
		} catch (IOException e) {
			log.error("testReadJson :", e);
		}
	}

	@Test
	public void testIsMatchCheck() {
		calculateEngine.isMatchCheck(null);
	}

	@Test
	public void testWriteJson() {
		try {
			calculateEngine.writeJson(null, null);
		} catch (Exception e) {
			log.error("testWriteJson :", e);
		}
	}

	@Test
	public void testCalculateMatchMismatch() {
		try {
			calculateEngine.calculateMatchMismatch();
		} catch (Exception e) {
			log.error("testCalculateMatchMismatch :", e);
		}

	}

	@Test
	public void testCrateMatchObject() {
		try {
			calculateEngine.crateMatchObject(null);
		} catch (Exception e) {
			log.error("testCrateMatchObject :", e);
		}
	}

}
