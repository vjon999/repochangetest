package com.ju.it.pratik.img.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ju.it.pratik.img.test.watermark.WatermarkEmbedTest;
import com.ju.it.pratik.img.test.watermark.WatermarkRecoveryTest;

@RunWith(Suite.class)
@SuiteClasses({ CorrelationCalculatorTester.class, 
	SubstitutionUtilsTest.class,
		WatermarkEmbedTest.class, 
		WatermarkRecoveryTest.class })
public class AllTests {

}
