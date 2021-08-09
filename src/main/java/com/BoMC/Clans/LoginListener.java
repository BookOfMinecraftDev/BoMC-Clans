package com.BoMC.Clans;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class LoginListener implements Listener {

	private ClansPlugin plugin;

	public LoginListener(ClansPlugin p) {
		plugin = p;
	}

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		if (!plugin.doesPlayerHaveClan(event.getPlayer())) {
			// Give him a clan, since he doesn't have one
			Clan c = plugin.findSmallestDefaultClan();
			c.getMembers().add(event.getPlayer());
			plugin.getLogger().info(String.format("Assigned player %s to default clan %s", event.getPlayer().getDisplayName(), c.getSubclan().label));
			event.getPlayer().sendMessage(String.format("You have been randomly assigned the default clan of %s!", c.getSubclan().label));
		}
	}
}
