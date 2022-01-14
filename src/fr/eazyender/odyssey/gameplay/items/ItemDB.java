package fr.eazyender.odyssey.gameplay.items;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.utils.NBTEditor;
import fr.eazyender.odyssey.utils.NBTEditor.NBTCompound;

public class ItemDB {
	
	public static void addItem(String id, ItemStack is) {
		addItem(id, (NBTCompound) NBTEditor.getCompound(is));
	}

	public static void addItem(String id, NBTCompound is) {
		try {
			String sql = "INSERT INTO items (id, itemNBT) VALUES (?, ?);";
			PreparedStatement stmt = OdysseyPl.getOdysseyPlugin().getSqlManager().connection.prepareStatement(sql);
			stmt.setString(1, id);
			stmt.setString(2, is.toJson());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	

	public static void updateItem(String id, NBTCompound is) {
		try {
			String sql = "UPDATE items SET itemNBT=? WHERE id=?;";
			PreparedStatement stmt = OdysseyPl.getOdysseyPlugin().getSqlManager().connection.prepareStatement(sql);
			stmt.setString(1, is.toJson());
			stmt.setString(2, id);
			
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public static void removeItem(String id) {
		try {
			String sql = "DELETE FROM items WHERE id=?;";
			PreparedStatement stmt = OdysseyPl.getOdysseyPlugin().getSqlManager().connection.prepareStatement(sql);
			stmt.setString(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	
	public static ItemStack getItem(String id) {
		
		try {
			String sql = "SELECT * FROM items WHERE id=?;";
			PreparedStatement stmt = OdysseyPl.getOdysseyPlugin().getSqlManager().connection.prepareStatement(sql);
			stmt.setString(1, id);
			ResultSet set = stmt.executeQuery();
			while (set.next())
				return NBTEditor.getItemFromTag(NBTEditor.NBTCompound.fromJson(set.getString("itemNBT")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	
	public static void giveItem(Player p, String id) {
		ItemStack item = getItem(id);
		HashMap<Integer, ItemStack> map = p.getInventory().addItem(item);
		if (!map.isEmpty()) {
			p.getWorld().dropItem(p.getLocation(), item);
		}
		
	}
	
}
