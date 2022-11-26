package fr.eazyender.odyssey.player.jobs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import fr.eazyender.odyssey.OdysseyPl;

public class JobsDB {
	
	public static void initPlayer(String uuid) {
		try {
			String sql = "INSERT INTO jobs (uuid, forgeron) VALUES (?, ?);";
			PreparedStatement stmt = OdysseyPl.getOdysseyPlugin().getSqlManager().connection.prepareStatement(sql);
			stmt.setString(1, uuid);
			//init every job
			for(int i = 2; i < 3; i++)
				stmt.setInt(i, 0);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean hasJobsInit(String uuid) {

		try {
			String sql = "SELECT * from jobs WHERE uuid=?;";
			PreparedStatement stmt = OdysseyPl.getOdysseyPlugin().getSqlManager().connection.prepareStatement(sql);
			stmt.setString(1, uuid);
			ResultSet set = stmt.executeQuery();
			if (!set.next()) 
				return false;
			else return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void setXp(String uuid, Job job, int xp) {
		
		if (!hasJobsInit(uuid)) initPlayer(uuid);
		try {
			String sql = "UPDATE masteries SET " + job.name().toLowerCase() + "=? WHERE uuid=?;";
			PreparedStatement stmt = OdysseyPl.getOdysseyPlugin().getSqlManager().connection.prepareStatement(sql);
			stmt.setInt(1, xp);
			stmt.setString(2, uuid);
			
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public static int getXp(Player p, Job job) {
		
		try {
			String sql = "SELECT * FROM jobs WHERE uuid=?;";
			PreparedStatement stmt = OdysseyPl.getOdysseyPlugin().getSqlManager().connection.prepareStatement(sql);
			stmt.setString(1, p.getUniqueId().toString());
			ResultSet set = stmt.executeQuery();
			while (set.next())
				return set.getInt(job.name().toLowerCase());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initPlayer(p.getUniqueId().toString());
		return 0;
		
	}
}
