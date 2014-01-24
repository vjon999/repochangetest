package com.ju.it.pratik.img.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CorrelationCalculatorTester.class, 
	SubstitutionUtilsTest.class,
		WatermarkEmbedTest.class, 
		WatermarkRecoveryTest.class })
public class AllTests {

}
