package fr.eazyender.odyssey.gameplay.aura.skills;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.eazyender.odyssey.OdysseyPl;
public class SkillsDB {

	public static void initPlayer(String uuid) {
		try {
			String sql = "INSERT INTO skills (uuid, skill1, skill2, skill3, skill4, skill5, skill6, skill7, skill8, skill9, skill10) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement stmt = OdysseyPl.getOdysseyPlugin().getSqlManager().connection.prepareStatement(sql);
			stmt.setString(1, uuid);
			for(int i = 2; i < 12; i++)
				stmt.setString(i, "null");
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	public static boolean hasSkillsInit(String uuid) {

		try {
			String sql = "SELECT * from skills WHERE uuid=?;";
			PreparedStatement stmt = OdysseyPl.getOdysseyPlugin().getSqlManager().connection.prepareStatement(sql);
			stmt.setString(1, uuid);
			ResultSet set = stmt.executeQuery();
			if (!set.next()) {
				stmt.close();

				return false;
			}
			else {
				stmt.close();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	

	public static void setSkill(String uuid, int slot, String skill) {
		
		if (!hasSkillsInit(uuid)) initPlayer(uuid);
		try {
			String sql = "UPDATE skills SET skill" + slot + "=? WHERE uuid=?;";
			PreparedStatement stmt = OdysseyPl.getOdysseyPlugin().getSqlManager().connection.prepareStatement(sql);
			stmt.setString(1, skill);
			stmt.setString(2, uuid);
			
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	
	public static String getSkill(String uuid, int slot) {
		if (!hasSkillsInit(uuid)) initPlayer(uuid);
		try {
			String sql = "SELECT * FROM skills WHERE uuid=?;";
			PreparedStatement stmt = OdysseyPl.getOdysseyPlugin().getSqlManager().connection.prepareStatement(sql);
			stmt.setString(1, uuid);
			ResultSet set = stmt.executeQuery();
			
			while (set.next()) {
				String skill = set.getString("skill" + slot);
				stmt.close();
				return skill;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "null";
		
	}
	
	public static HashMap<Integer, String> getSkills(String uuid) {
		if (!hasSkillsInit(uuid)) initPlayer(uuid);
		try {
			HashMap<Integer, String> skills = new HashMap<>();
			String sql = "SELECT * FROM skills WHERE uuid=?;";
			PreparedStatement stmt = OdysseyPl.getOdysseyPlugin().getSqlManager().connection.prepareStatement(sql);
			stmt.setString(1, uuid);
			ResultSet set = stmt.executeQuery();
			
			if (set.next())
				for(int i = 1; i < 11; i++)
					skills.put(i, set.getString("skill" + i));
			stmt.close();
			return skills;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	
}
