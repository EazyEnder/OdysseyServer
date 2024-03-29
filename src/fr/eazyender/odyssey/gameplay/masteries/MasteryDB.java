package fr.eazyender.odyssey.gameplay.masteries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.gameplay.stats.Classe;
public class MasteryDB {

	public static void initPlayer(String uuid) {
		try {
			String sql = "INSERT INTO masteries (uuid, guerrier, archer, tank, fire, water, earth, wind, light, shadow, classe, guerrierXP, archerXP, tankXP) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement stmt = OdysseyPl.getOdysseyPlugin().getSqlManager().connection.prepareStatement(sql);
			stmt.setString(1, uuid);
			for(int i = 2; i < 11; i++)
				stmt.setInt(i, 1);
			stmt.setString(11, "null");
			for(int i = 12; i < 15; i++)
				stmt.setFloat(i, 0);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	public static boolean hasMasteryInit(String uuid) {

		try {
			String sql = "SELECT * from masteries WHERE uuid=?;";
			PreparedStatement stmt = OdysseyPl.getOdysseyPlugin().getSqlManager().connection.prepareStatement(sql);
			stmt.setString(1, uuid);
			ResultSet set = stmt.executeQuery();
			
			if (!set.next()) {
				stmt.close();
				return false;
			} else {
				stmt.close();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	

	public static void setMastery(String uuid, Mastery mastery, int amount) {
		
		if (!hasMasteryInit(uuid)) initPlayer(uuid);
		try {
			String sql = "UPDATE masteries SET " + mastery.name().toLowerCase() + "=? WHERE uuid=?;";
			PreparedStatement stmt = OdysseyPl.getOdysseyPlugin().getSqlManager().connection.prepareStatement(sql);
			stmt.setInt(1, amount);
			stmt.setString(2, uuid);
			
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	
	public static int getMastery(String uuid, Mastery mastery) {
		
		try {
			String sql = "SELECT * FROM masteries WHERE uuid=?;";
			PreparedStatement stmt = OdysseyPl.getOdysseyPlugin().getSqlManager().connection.prepareStatement(sql);
			stmt.setString(1, uuid);
			ResultSet set = stmt.executeQuery();
			while (set.next()) {
				int masteryLvl = set.getInt(mastery.name().toLowerCase());
				stmt.close();
				return masteryLvl;
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initPlayer(uuid);
		return 0;
		
	}
	
	public static Classe getClass(String uuid) {
		
		try {
			String sql = "SELECT * FROM masteries WHERE uuid=?;";
			if(OdysseyPl.getOdysseyPlugin().getSqlManager() == null || OdysseyPl.getOdysseyPlugin().getSqlManager().connection == null) return null;
			PreparedStatement stmt = OdysseyPl.getOdysseyPlugin().getSqlManager().connection.prepareStatement(sql);
			stmt.setString(1, uuid);
			ResultSet set = stmt.executeQuery();
			
			while (set.next()) {
				String classe = set.getString("classe");
				stmt.close();
				if (classe == null) return null;
				return Classe.valueOf(classe);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initPlayer(uuid);
		return null;
		
	}
	
	
	public static void setClass(String uuid, Classe classe) {
		
		if (!hasMasteryInit(uuid)) initPlayer(uuid);
		try {
			String sql = "UPDATE masteries SET classe=? WHERE uuid=?;";
			PreparedStatement stmt = OdysseyPl.getOdysseyPlugin().getSqlManager().connection.prepareStatement(sql);
			stmt.setString(1, classe.name());
			stmt.setString(2, uuid);
			
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	

	public static void setXp(Player p, Mastery classe, float amount) {
		
		if (!hasMasteryInit(p.getUniqueId().toString())) initPlayer(p.getUniqueId().toString());
		try {
			String sql = "UPDATE masteries SET " + classe.name().toLowerCase() + "XP=? WHERE uuid=?;";
			PreparedStatement stmt = OdysseyPl.getOdysseyPlugin().getSqlManager().connection.prepareStatement(sql);
			stmt.setFloat(1, amount);
			stmt.setString(2, p.getUniqueId().toString());
			
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	
	public static float getXp(Player p, Mastery classe) {
		
		try {
			String sql = "SELECT * FROM masteries WHERE uuid=?;";
			PreparedStatement stmt = OdysseyPl.getOdysseyPlugin().getSqlManager().connection.prepareStatement(sql);
			stmt.setString(1, p.getUniqueId().toString());
			ResultSet set = stmt.executeQuery();
		
			while (set.next()) {
				
				float xp = set.getFloat(classe.name().toLowerCase() + "XP");
				stmt.close();
				return xp;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initPlayer(p.getUniqueId().toString());
		return 0;
		
	}
	
	
}
