package nus.iss.edu.sg.SSF_Assessment.Assessment;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import nus.iss.edu.sg.SSF_Assessment.Assessment.Model.Quotation;
import nus.iss.edu.sg.SSF_Assessment.Assessment.Service.QuotationService;

@SpringBootTest	
	
	
class AssessmentApplicationTests {

	@Autowired
	private QuotationService quotationSvc;

	@Test
	void contextLoads() throws IOException {

		List<String> fruits = Arrays.asList("durian", "plum", "pear");

		java.util.Optional<Quotation> opt = quotationSvc.getQuotations(fruits);

		Assertions.assertThat(opt.isEmpty());
}}
