package it.polito.tdp.crimes.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Event;



public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List <Integer> getYear() {
		String sql= "SELECT DISTINCT Year(reported_date) as a FROM events";
		try {
			List <Integer> result= new ArrayList <>();
		
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				int a= res.getInt("a");
				result.add(a);
				
			}
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List <Integer> getDistrict() {
		String sql= "SELECT DISTINCT district_id FROM events";
		try {
			List <Integer> result= new ArrayList <>();
		
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				int a= res.getInt("district_id");
				result.add(a);
				
			}
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public Double getLatMedia(Integer anno, Integer v1) {
String sql= "SELECT AVG(geo_lat) AS lat FROM EVENTS WHERE district_id=? AND YEAR(reported_date)= ?";
		
try {
	

	Connection conn = DBConnect.getConnection() ;

	PreparedStatement st = conn.prepareStatement(sql) ;
	st.setInt(1, v1);
	st.setInt(2,anno);
	
	
	
	ResultSet res = st.executeQuery() ;
	
	if(res.next()) {
	conn.close();
	return res.getDouble("lat") ;

}
	} catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
	}
	return null ;

	}

	public Double getLongMedia(Integer anno, Integer v) {
		String sql= "SELECT AVG(geo_lon) AS lon FROM EVENTS WHERE district_id=? AND YEAR(reported_date)= ?";
		
		try {
			

			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, v);
			st.setInt(2,anno);
			
			
			
			ResultSet res = st.executeQuery() ;
			
			if(res.next()) {
			conn.close();
			return res.getDouble("lon") ;

		}
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		return null;
	}

	public Integer getDistrettoMin(Integer anno) {
	String sql="SELECT district_id FROM `events` WHERE YEAR(reported_date)= ? GROUP by district_id order BY COUNT(*) ASC LIMIT 1";
	
	
	try {
		

		Connection conn = DBConnect.getConnection() ;

		PreparedStatement st = conn.prepareStatement(sql) ;
		st.setInt(1, anno);
		
		ResultSet res = st.executeQuery() ;
		
		if(res.next()) {
		conn.close();
		return res.getInt("district_id");

	}
		} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		
	}
	return null;

	}

	
	public List<Event> listEventsBYDate(Integer anno , Integer mese, Integer giorno){
		String sql = "SELECT * FROM events WHERE year(reported_date)=?"
				+ "AND Month(reported_date)=? And Day(reported_date)=?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1,anno);
			st.setInt(2,mese);
			st.setInt(3,giorno);
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	

	
	

}
