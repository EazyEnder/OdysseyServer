package fr.eazyender.odyssey.gameplay.items;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import fr.eazyender.odyssey.gameplay.stats.Stat;

public class ItemInventoryHolder implements InventoryHolder {

	enum State {
		MAIN, ITEM, STAT, TYPE, RANK
	}
	

	
	State state;
	Stat stat;
	int page;
	String idItem;

	public ItemInventoryHolder(State state, int page) {
		this.state = state;
		this.page = page;
	}

	public ItemInventoryHolder(State state, String idItem) {
		this.state = state;
		this.idItem = idItem;
	}

	@Override
	public Inventory getInventory() {

		return null;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getIdItem() {
		return idItem;
	}

	public ItemInventoryHolder setIdItem(String idItem) {
		this.idItem = idItem;
		return this;
	}

	public State getState() {
		return state;
	}

	public ItemInventoryHolder setState(State state) {
		this.state = state;
		return this;
	}

	public Stat getStat() {
		return stat;
	}

	public ItemInventoryHolder setStat(Stat stat) {
		this.stat = stat;
		return this;
	}
	
	

}
