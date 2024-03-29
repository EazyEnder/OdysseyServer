package fr.eazyender.odyssey.listener;


import org.bukkit.Particle;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.dungeons.gui.DungeonGuiListener;
import fr.eazyender.odyssey.gameplay.aura.AuraCastListener;
import fr.eazyender.odyssey.gameplay.aura.gui.SkillsGuiListener;
import fr.eazyender.odyssey.gameplay.city.building.HammerHandler;
import fr.eazyender.odyssey.gameplay.city.building.objects.BuildObjectListener;
import fr.eazyender.odyssey.gameplay.items.ItemGuiListener;
import fr.eazyender.odyssey.gameplay.items.armorevent.ArmorListener;
import fr.eazyender.odyssey.gameplay.masteries.ClasseCommand;
import fr.eazyender.odyssey.gameplay.masteries.MasteryLeveling;
import fr.eazyender.odyssey.player.CompassUtils;
import fr.eazyender.odyssey.player.InteractionsManager;
import fr.eazyender.odyssey.player.StatsListener;
import fr.eazyender.odyssey.player.TchatListener;
import fr.eazyender.odyssey.player.group.GroupListener;
import fr.eazyender.odyssey.player.harvest.HarvestHandler;
import fr.eazyender.odyssey.sounds.SoundsManager;
import fr.eazyender.odyssey.utils.block.BlockUtils;
import fr.eazyender.odyssey.utils.zone.ZoneUtils;

public class ListenerManager {

	OdysseyPl pl;

	TchatListener chatListener;
	ZoneUtils zUtils;
	CompassUtils cUtils;
	StatsListener sListener;
	BlockUtils bUtils;
	AuraCastListener auraListener;
	ItemGuiListener itemGuiListener;
	HammerHandler hammerHandler;
	GroupListener playerGroupHandler;
	DungeonGuiListener dungeonGuiListener;
	ClasseCommand classeCommand;
	HarvestHandler harvestListener;
	BuildObjectListener bolistener;
	SoundsManager soManager;
	InteractionsManager imanager;
	MasteryLeveling mLevel;
	ArmorListener armorListener;
	SkillsGuiListener skillsGuiListener;
	

	public ListenerManager(OdysseyPl pl) {
		this.pl = pl;
		pl.getServer().getPluginManager().registerEvents(chatListener = new TchatListener(), pl);
		pl.getServer().getPluginManager().registerEvents(zUtils = new ZoneUtils(), pl);
		pl.getServer().getPluginManager().registerEvents(cUtils = new CompassUtils(), pl);
		pl.getServer().getPluginManager().registerEvents(sListener = new StatsListener(), pl);
		pl.getServer().getPluginManager().registerEvents(bUtils = new BlockUtils(), pl);
		pl.getServer().getPluginManager().registerEvents(auraListener = new AuraCastListener(), pl);
		pl.getServer().getPluginManager().registerEvents(itemGuiListener = new ItemGuiListener(), pl);
		pl.getServer().getPluginManager().registerEvents(hammerHandler = new HammerHandler(), pl);
		pl.getServer().getPluginManager().registerEvents(playerGroupHandler = new GroupListener(), pl);
		pl.getServer().getPluginManager().registerEvents(dungeonGuiListener = new DungeonGuiListener(), pl);
		pl.getServer().getPluginManager().registerEvents(harvestListener = new HarvestHandler(), pl);
		pl.getServer().getPluginManager().registerEvents(classeCommand = new ClasseCommand(), pl);
		pl.getServer().getPluginManager().registerEvents(bolistener = new BuildObjectListener(), pl);
		pl.getServer().getPluginManager().registerEvents(soManager = new SoundsManager(), pl);
		pl.getServer().getPluginManager().registerEvents(imanager = new InteractionsManager(), pl);
		pl.getServer().getPluginManager().registerEvents(mLevel = new MasteryLeveling(), pl);
		pl.getServer().getPluginManager().registerEvents(armorListener = new ArmorListener(), pl);
		pl.getServer().getPluginManager().registerEvents(skillsGuiListener = new SkillsGuiListener(), pl);
		
		
		// Cancel heart particles when damaging enemies
		ProtocolManager manager = ProtocolLibrary.getProtocolManager();
		manager.addPacketListener(new PacketAdapter(OdysseyPl.getOdysseyPlugin(), ListenerPriority.HIGH, PacketType.Play.Server.WORLD_PARTICLES) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Particle type = packet.getNewParticles().read(0).getParticle();
                if (type == Particle.DAMAGE_INDICATOR) event.setCancelled(true);
            }
        });
		
	}

	public OdysseyPl getPl() {
		return pl;
	}


	
	
}
