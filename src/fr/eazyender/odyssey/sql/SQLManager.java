package fr.eazyender.odyssey.sql;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import fr.eazyender.odyssey.OdysseyPl;

public class SQLManager {
	public Connection connection;
	FileConfiguration config;
	
	public SQLManager(OdysseyPl pl) {
		this.config = pl.getConfig();
		new BukkitRunnable() {
			public void run() {
				try {
					if (SQLManager.this.connection != null && !SQLManager.this.connection.isClosed())
						SQLManager.this.connection.createStatement().execute("SELECT 1");
				} catch (SQLException e) {
					SQLManager.this.connection = SQLManager.this.getNewConnection(config);
				}
			}
		}.runTaskTimerAsynchronously(OdysseyPl.getOdysseyPlugin(), 1200L, 1200L);
		
		if (!init()) {
			for (int i = 0; i < 3; i++)
				Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "[Odyssey] NO ACCESS TO DATABASE !");
		} else {
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Odyssey] Access to database");
		}
	}


	public Connection getNewConnection(FileConfiguration config) {
		String host = config.getString("mysql.host");
		String port = config.getString("mysql.port");
		String database = config.getString("mysql.database");
		String user = config.getString("mysql.user");
		String password = config.getString("mysql.password");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?characterEncoding=latin1";
			Connection connection = DriverManager.getConnection(url, user, password);
			return connection;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void close() throws SQLException {
		if (this.connection != null)
			this.connection.close();
	}

	public boolean execute(String sql) throws SQLException {
		boolean success = this.connection.createStatement().execute(sql);
		return success;
	}

	public boolean checkConnection() throws SQLException {
		if (this.connection == null || this.connection.isClosed()) {
			this.connection = getNewConnection(this.config);
			if (this.connection == null || this.connection.isClosed())
				return false;
			execute("CREATE TABLE IF NOT EXISTS items (id VARCHAR(20) PRIMARY KEY, itemNBT TEXT)");
			execute("CREATE TABLE IF NOT EXISTS masteries (uuid VARCHAR(36) PRIMARY KEY, warrior INT, archer INT, tank INT, fire INT, water INT, earth INT, wind INT, light INT, shadow INT, classe VARCHAR(20))");
			
		}
		return true;
	}

	public boolean init() {
		try {
			return checkConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
