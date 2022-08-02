package fr.eazyender.odyssey.gameplay.stats;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;

import fr.eazyender.odyssey.OdysseyPl;
import net.md_5.bungee.api.ChatColor;

public class DamageHelper {
	
	// Def
	public static int getDamageDealt(double damage, double defense) {
		// 100 attack 100 defense = 50 damage
		// 50 attack 100 defense = 16 damage
		// 200 attack 50 defense = 160 damage
		return (int) (damage*(damage/(damage+defense)));
		
	}
	
	// Aura
	public static int getAuraDamage(Player p, double power, double percentage, boolean crit) {
		// Power = puissance du skill (peut changer avec combo)
		// Attaque du joueur
		// Pourcentage demandé par le skill
		// Coup critiqueo u non
		
		// power 100 attack 10 percentage 100% = 20 damage
		
		double attack = CombatStats.getStats(p).getStat(Stat.DAMAGE);
		double damage =  percentage * (attack * (power / 50));
		
		if (crit) 
			damage = damage * ((double)CombatStats.getStats(p).getStat(Stat.CRIT_DAMAGE) / 100);
		

		return applyVariation(damage);
		
	}
	
	public static int applyVariation(double damage) {
		// +5% -5%
		Random r = new Random();
		double var = -5 + r.nextInt(10);
		double damageWithVar = damage += (var / 100) * damage;
		return (int) Math.round(damageWithVar);
	}
	
	public static boolean isCrit(Player p) {
		Random r = new Random();
		if (r.nextInt(100) < CombatStats.getStats(p).getStat(Stat.CRIT_CHANCE)) 
			return true;
		 return false;
	}
	
	
	public static void animateDamage(Player p, LivingEntity e, int damage, boolean crit) {
		Random r = new Random();
		Location loc = e.getLocation().add((-50.0 + (double) r.nextInt(100)) / 100, 1.5 + ((double)r.nextInt(100)) / 100, (-50.0 + (double) r.nextInt(100)) / 100);
		ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
		PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
		int entityID = (int)(Math.random() * Integer.MAX_VALUE);
		// Location, basic values
		packet.getModifier().writeDefaults();
		packet.getIntegers().write(0, entityID);
		packet.getEntityTypeModifier().write(0, EntityType.ARMOR_STAND);
        packet.getDoubles().write(0, loc.getX());
        packet.getDoubles().write(1, loc.getY());
        packet.getDoubles().write(2, loc.getZ());
        packet.getUUIDs().write(0, UUID.randomUUID());
        try {
			protocolManager.sendServerPacket(p, packet);
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		}
        PacketContainer packet2 = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        packet2.getModifier().writeDefaults();
        packet2.getIntegers().write(0, entityID);

        WrappedDataWatcher metadata = new WrappedDataWatcher();
        ChatColor color = ChatColor.of("#f5e17f");
        if (crit) color = ChatColor.of("#c73b0c");
        Optional<?> opt = Optional
                .of(WrappedChatComponent
                        .fromChatMessage(color + "" +damage)[0].getHandle());
        // Invisible
        metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x20);
        // Custom name
        metadata.setObject(new WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), opt);
        metadata.setObject(new WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), true);
        metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(15, WrappedDataWatcher.Registry.get(Byte.class)), (byte) (0x01 | 0x08 | 0x10)); //isSmall, noBasePlate, set Marker
        packet2.getWatchableCollectionModifier().write(0, metadata.getWatchableObjects());
        
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(p, packet2);
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
        }
        new BukkitRunnable() {
        	public void run() {
        		 PacketContainer packet3 = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        		 packet3.getIntLists().write(0, Arrays.asList(entityID));
        	        try {
        	            ProtocolLibrary.getProtocolManager().sendServerPacket(p, packet3);
        	        } catch (InvocationTargetException e2) {
        	            e2.printStackTrace();
        	        }
        	}
        }.runTaskLater(OdysseyPl.getOdysseyPlugin(), 20);
        
        
        
	}

	public static void dealDamage(LivingEntity to, Player from, int damage) {
		if (to instanceof Player)
			to.damage(getDamageDealt(damage, (double)CombatStats.getStats((Player)to).getStat(Stat.DEFENSE)));
		else
			to.damage(damage);
		
	}
	

	
}
