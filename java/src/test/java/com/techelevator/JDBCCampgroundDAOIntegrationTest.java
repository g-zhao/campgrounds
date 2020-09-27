package com.techelevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import com.techelevator.campground.jdbc.JDBCCampgroundDAO;
import com.techelevator.model.Campground;

public class JDBCCampgroundDAOIntegrationTest {

	private static SingleConnectionDataSource dataSource;
	private static JDBCCampgroundDAO jdbcCampgroundDao;


	@BeforeClass
	public static void setupDataSource() {
		System.out.println("Starting test suite");
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);

	}

	@AfterClass
	public static void closeDataSource() {
		System.out.println("All tests are done");
		dataSource.destroy();
	}

	@Before
	public void setUp() {
		System.out.println("Starting test");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcCampgroundDao = new JDBCCampgroundDAO(dataSource);


	}
	

	@After
	public void rollback () throws SQLException {
		System.out.println("Ending test");
			dataSource.getConnection().rollback();
	}

	@Test
	public void get_campground_by_park_id_test() {
		List<Campground> campgroundList = jdbcCampgroundDao.getCampgroundByParkId(1L);

		Long expectedCampgroundId1 = 3L;
		assertNotNull(campgroundList);
		assertEquals(expectedCampgroundId1, campgroundList.get(campgroundList.size() - 1).getCampgroundId());
		
		Long expectedCampgroundId2 = 1L;
		assertEquals(expectedCampgroundId2, campgroundList.get(0).getCampgroundId());
		
		Long expectedCampgroundId3 = 2L;
		assertEquals(expectedCampgroundId3, campgroundList.get(1).getCampgroundId());
		
		
	}

}
