package com.techelevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.time.LocalDate;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import com.techelevator.campground.jdbc.JDBCParkDAO;
import com.techelevator.model.Park;

public class JDBCParkDAOIntegrationTest {

	private static SingleConnectionDataSource dataSource;
	private JDBCParkDAO jdbcParkDao;
	LocalDate date = LocalDate.of(2000, 11, 11);
	JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

	@BeforeClass
	public static void setupDataSource() throws Exception {
		System.out.println("Starting test suite");
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}


	@AfterClass
	public static void closeDataSource() throws Exception {
		System.out.println("All tests are done");
		dataSource.destroy();
	}

	@Before
	public void setUp() throws Exception {
		System.out.println("Starting test");
		jdbcParkDao = new JDBCParkDAO(dataSource);
	}

	@After
	public void rollback() throws Exception {
		System.out.println("Ending test");
		dataSource.getConnection().rollback();
	}

	@Test
	public void get_all_parks_test() {
		List<Park> allParks = jdbcParkDao.getAllParks();
		int parkCount = 0;
		for (int i = 0; i < allParks.size(); i++) {
			parkCount++;
		}
		assertNotNull(allParks);
		assertEquals(parkCount, allParks.size());

	}

	@Test
	public void park_names_and_quit_test() {
		List<Park> allParks = jdbcParkDao.getAllParks();
		String[] parkNames = jdbcParkDao.parkNamesAndQuit();
		int park = 0;
		for (Park thisPark : allParks) {
			parkNames[park] = thisPark.getParkName();
			park++;
		}
		assertNotNull(allParks);
		assertEquals(park, allParks.size());
	}

	@Test
	public void get_park_id_by_name_test() {
		List<Park> allParks = jdbcParkDao.getAllParks();

		int expectedParkId = 1;
		int actualParkId = jdbcParkDao.getParkIdByName("Acadia");
		assertEquals(expectedParkId, actualParkId);
		
		int expectedParkId2 = 2;
		int actualParkId2 = jdbcParkDao.getParkIdByName("Arches");
		assertEquals(expectedParkId2, actualParkId2);
		
		int expectedParkId3 = 2;
		int actualParkId3 = jdbcParkDao.getParkIdByName("Cuyahoga Valley");
		assertEquals(expectedParkId3, actualParkId3);
		
	}
	

}
