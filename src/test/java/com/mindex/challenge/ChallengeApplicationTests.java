package com.mindex.challenge;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChallengeApplicationTests {
	
	@LocalServerPort
    private int port;

	@Autowired
    private TestRestTemplate restTemplate;

	private String reportsUrl;	
	private String compensationUrl;
	private String compensationIdUrl;
	private Employee lennon;

	@Before
    public void setup() {
        reportsUrl = "http://localhost:" + port + "/reporting/{id}";
		compensationUrl = "http://localhost:" + port + "/compensation";
		compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";         

		lennon = new Employee();
		lennon.setEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
		lennon.setFirstName("John");
		lennon.setLastName("Lennon");
		lennon.setPosition("Development Manager");
		lennon.setDepartment("Engineering");
    }

	@Test
	public void testReadReportingStructure() {

		ReportingStructure structure = restTemplate
				.getForEntity(reportsUrl, ReportingStructure.class, lennon.getEmployeeId()).getBody();

		assertNotNull(structure.getEmployee());
		assertEmployeeEquivalence(lennon, structure.getEmployee());
		assertEquals(4, structure.getNumberofReports());
	}

	@Test 
	public void testCompensation() {
		Employee employee = new Employee();
		Compensation testCompensation = new Compensation();
		employee.setEmployeeId(lennon.getEmployeeId());
		testCompensation.setEmployee(employee);
		testCompensation.setSalary("120000.00");
		testCompensation.setEffectiveDate(LocalDate.of(2022, 01, 01));

		Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, testCompensation, Compensation.class).getBody();

		assertNotNull(createdCompensation.getEmployee());
		assertEmployeeEquivalence(lennon, createdCompensation.getEmployee());
		assertEquals(testCompensation.getSalary(), createdCompensation.getSalary());
		assertEquals(testCompensation.getEffectiveDate(), createdCompensation.getEffectiveDate());

		Compensation readCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation.class, createdCompensation.getEmployee().getEmployeeId()).getBody();
		assertEmployeeEquivalence(createdCompensation.getEmployee(), readCompensation.getEmployee());
		assertEquals(createdCompensation.getSalary(), readCompensation.getSalary());
		assertEquals(createdCompensation.getEffectiveDate(), readCompensation.getEffectiveDate());


	}

	private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
