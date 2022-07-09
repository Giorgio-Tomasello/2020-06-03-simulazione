package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Coppia;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public HashMap<Integer, Player> listPlayersMediaGol(double media){
		String sql = "SELECT p.PlayerID, p.Name, AVG(a.Goals) as mediaGol "
				+ "FROM Actions a, Players p "
				+ "WHERE a.PlayerID = p.PlayerID "
				+ "GROUP BY p.PlayerID, p.Name "
				+ "HAVING mediaGol>? "
				+ "ORDER BY mediaGol DESC";
		//ArrayList<Player> result = new ArrayList<Player>();
		HashMap<Integer, Player> idMap = new HashMap<Integer, Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, media);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				idMap.put(res.getInt("PlayerID"), player);
				
				//result.add(player);
			}
			conn.close();
			return idMap;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	

	public ArrayList<Coppia> listArchi(double media, HashMap<Integer,Player> idMap){
		String sql = "SELECT a1.PlayerID, a2.PlayerID, SUM(a1.TimePlayed-a2.TimePlayed) as peso "
				+ "	FROM Actions a1, Actions a2 "
				+ "	WHERE a1.MatchID = a2.MatchID "
				+ "	AND a1.TeamID <> a2.TeamID "
				+ "	AND a1.PlayerID > a2.PlayerID "
				+ "	AND a1.Starts = 1 AND a2.Starts = 1 "
				+ "	AND a1.PlayerID IN (SELECT p.PlayerID "
				+ "					FROM Actions a, Players p  "
				+ "					WHERE a.PlayerID = p.PlayerID  "
				+ "					GROUP BY p.PlayerID "
				+ "					HAVING AVG(a.Goals)>?) "
				+ "	AND a2.PlayerID IN (SELECT p.PlayerID "
				+ "					FROM Actions a, Players p  "
				+ "					WHERE a.PlayerID = p.PlayerID  "
				+ "					GROUP BY p.PlayerID  "
				+ "					HAVING AVG(a.Goals)>?) "
				+ "	GROUP BY a1.PlayerID, a2.PlayerID "
				+ "	HAVING peso <> 0 ";
		
		ArrayList<Coppia> result = new ArrayList<Coppia>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, media);
			st.setDouble(2, media);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				Coppia c = new Coppia(idMap.get(res.getInt("a1.PlayerID")), idMap.get(res.getInt("a2.PlayerID")), res.getInt("peso"));
				
				result.add(c);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
}
