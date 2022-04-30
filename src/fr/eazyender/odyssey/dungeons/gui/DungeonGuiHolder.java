package fr.eazyender.odyssey.dungeons.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import fr.eazyender.odyssey.dungeons.Dungeon;

public class DungeonGuiHolder implements InventoryHolder {

	int page;
	DungeonGuiState state;
	Dungeon dungeon;
	
	
	
	public DungeonGuiHolder(int page, DungeonGuiState state) {
		this.page = page;
		this.state = state;
	}


	

	public Dungeon getDungeon() {
		return dungeon;
	}




	public DungeonGuiHolder setDungeon(Dungeon dungeon) {
		this.dungeon = dungeon;
		return this;
	}




	public int getPage() {
		return page;
	}



	public void setPage(int page) {
		this.page = page;
	}



	public DungeonGuiState getState() {
		return state;
	}



	public void setState(DungeonGuiState state) {
		this.state = state;
	}



	@Override
	public Inventory getInventory() {
		
		return null;
	}

}
