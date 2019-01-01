package com.allure;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Issue;
import io.qameta.allure.Step;
import io.qameta.allure.TmsLink;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.fail;

/**
 * AllureTestngTest to test various features of allure report in testng maven
 * project
 * 
 * @author Sunil kumar G
 *
 */
public class AllureTestngTest {
	public static final String SAMPLE_TXT_FILE = "Sample1.txt";
	public static final String SAMPLE_CSV_FILE = "Sample2.csv";
	public static final String SAMPLE_SVG_FILE = "Sample3.svg";

	/**
	 * Method to assert an integer
	 */
	@Test
	public void simpleTest() {
		assertThat(2, is(2));
	}

	/**
	 * Method to check that 2 is 2
	 */
	@Step
	public void checkThat2is2() {
		assertThat(2, is(2));
	}

	/**
	 * Method to validate test with steps
	 */
	@Test
	public void simpleTestWithSteps() {
		checkThat2is2();
	}

	/**
	 * Method which returns string to be attached
	 * 
	 * @return
	 */
	@Attachment
	public String makeAttach() {
		return "yeah, 2 is 2";
	}

	/**
	 * Method to attach a string to allure report
	 */
	@Test
	public void simpleTestWithAttachments() {
		assertThat(2, is(2));
		makeAttach();
	}

	/**
	 * Method to purposefully fail the test\
	 */
	@Test
	public void failedTest() {
		fail("This test should be failed");
	}

	/**
	 * Skipped by dependency method
	 */
	@Test(dependsOnMethods = "failedTest")
	public void skippedByDependencyTest() {
	}

	/**
	 * Skipped test
	 */
	@Test(enabled = false)
	public void skippedTest() {
	}

	/**
	 * Data provider
	 * 
	 * @return
	 */
	@DataProvider
	public Object[][] dataProvider() {
		return new Object[][] { { 1 }, { 2 }, { 3 } };
	}

	/**
	 * Parameterized test
	 * 
	 * @param parameter
	 */
	@Test(dataProvider = "dataProvider")
	@Issue("ALR-123 , ALR-456 , ALR-789")
	@TmsLink("TMS-123")
	public void parametrizedTest(int parameter) {
		assertThat(parameter, is(2));
	}

	/**
	 * Another data provider
	 * 
	 * @return
	 */
	@DataProvider
	public Object[][] anotherDataProvider() {
		return new Object[][] { { "Long-long parameter value", 1, 2 },
				{ "Even longer parameter value than long-long parameter value", 2, 3 }, { "", 3, 4 } };
	}

	/**
	 * Text attachment test
	 */
	@Test(description = "Sample TXT attachment")
	public void txtAttachmentTest() {
		saveTxtAttachment();
	}

	/**
	 * Parameterized test
	 * 
	 * @param parameter1
	 * @param parameter2
	 * @param parameters3
	 */
	@Test(dataProvider = "anotherDataProvider")
	@Issue("ALR-123 , ALR-456 , ALR-789")
	@TmsLink("TMS-123")
	public void parametrizedTest(String parameter1, int parameter2, int parameters3) {
		assertThat(parameter2, is(2));
	}

	/**
	 * CSV attachment test
	 */
	@Test(description = "Sample CSV attachment")
	public void csvAttachmentTest() {
		saveCsvAttachment();
	}

	/**
	 * SVG attachment test
	 */
	@Test(description = "Sample SVG attachment")
	public void svgAttachmentTest() {
		saveSvgAttachment();
	}

	/**
	 * Method to add txt attachment
	 */
	@Attachment(value = "Sample txt attachment", type = "text/csv")
	public void saveTxtAttachment() {
		Allure.addAttachment("Sample txt attachment", getSampleFile(SAMPLE_TXT_FILE));
	}

	/**
	 * Method to add csv attachment
	 */
	@Attachment(value = "Sample csv attachment", type = "text/csv")
	public void saveCsvAttachment() {
		Allure.addAttachment("Sample csv attachment", getSampleFile(SAMPLE_CSV_FILE));
	}

	/**
	 * Method to add svg attachment
	 */
	@Attachment(value = "Sample svg attachment", type = "image/svg+xml")
	public void saveSvgAttachment() {
		Allure.addAttachment("Sample svg attachment", getSampleFile(SAMPLE_SVG_FILE));
	}

	/**
	 * Method to get sample file
	 * 
	 * @param fileName
	 * @return
	 */
	private FileInputStream getSampleFile(String fileName) {
		String filePath = "";
		try {
			filePath = System.getProperty("user.dir") + "\\Files\\" + fileName;
			System.out.println("filePath " + filePath);
			Reporter.log("File path - " + filePath);
		} catch (NullPointerException | IllegalArgumentException e) {
			Reporter.log("Unable to get screenshot folderpath" + "----" + e);
		}
		File fileObj = new File(filePath);
		FileInputStream is = null;
		try {
			is = new FileInputStream(fileObj);
		} catch (IOException e) {
			Reporter.log("Attachment not Created" + e, 1);
		}
		return is;
	}

	/**
	 * Method to capture screenshot
	 * 
	 * @param driver
	 * @param methodName
	 */
	public void captureScreenshot(WebDriver driver, String methodName) {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		InputStream is = null;
		try {
			is = new FileInputStream(screenshot);
		} catch (IOException e) {
			Reporter.log("Attachment not Created" + e, 1);
		}
		Allure.addAttachment("<SCREENSHOT> - " + methodName, is);
	}
}
