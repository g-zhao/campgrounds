package com.techelevator.campground.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.techelevator.model.Site;


public class JDBCSiteDAO {

	private NamedParameterJdbcTemplate jdbcSpecial;

	public JDBCSiteDAO(DataSource dataSource) {
		this.jdbcSpecial = new NamedParameterJdbcTemplate(dataSource);
	}

	// method which sends script to sql to retreieve available sites against
	// reserved sites and the overlapping dates returns the available sites to
	// schedule based on popularity
	public List<Site> searchSiteAvail(String campgroundId, LocalDate arrivalDate, LocalDate departureDate) {
		Set<LocalDate> dates = new HashSet<LocalDate>();

		dates.add(arrivalDate);
		dates.add(departureDate);

		Set<Long> anId = new HashSet<Long>();
		anId.add(Long.parseLong(campgroundId));

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("dates", dates);
		parameters.addValue("id", anId);

		String sqlSelect = "SELECT * " + "FROM site " + "JOIN reservation ON reservation.site_id = site.site_id "
				+ "WHERE campground_id = :id AND reservation.site_id NOT IN " + "(SELECT reservation.site_id "
				+ "FROM reservation WHERE (from_date, to_date) OVERLAPS ( :dates )) "
				+ "GROUP BY site.site_id, reservation.reservation_id " + "ORDER BY COUNT(reservation.site_id) DESC "
				+ "LIMIT 5";
		// siteList is an arrayList which will contain the site objects passed back from
		// the sql query
		List<Site> siteList = new ArrayList<Site>();

		SqlRowSet rowset = jdbcSpecial.queryForRowSet(sqlSelect, parameters);

		while (rowset.next()) {
			Site thisSite = mapRowToSite(rowset);
			siteList.add(thisSite);
		}

		if (siteList.isEmpty()) {
			sqlSelect = "\"SELECT * \" + \"FROM site \" + \"JOIN reservation ON reservation.site_id = site.site_id " 
					+ "WHERE campground_id = :id AND reservation.site_id ORDER BY site_id LIMIT 5";
			rowset = jdbcSpecial.queryForRowSet(sqlSelect, parameters);
			while (rowset.next()) {
				Site thisSite = mapRowToSite(rowset);
				siteList.add(thisSite);
			}
		}
		return siteList;
	}

	private Site mapRowToSite(SqlRowSet result) {
		Site site = new Site();
		site.setAccessible(result.getBoolean("accessible"));
		site.setCampgroundId(result.getLong("campground_id"));
		site.setMaxOccupancy(result.getInt("max_occupancy"));
		site.setMaxRvLength(result.getInt("max_rv_length"));
		site.setSiteId(result.getLong("site_id"));
		site.setSiteNumber(result.getInt("site_number"));
		site.setUtilities(result.getBoolean("utilities"));

		return site;
	}

}
