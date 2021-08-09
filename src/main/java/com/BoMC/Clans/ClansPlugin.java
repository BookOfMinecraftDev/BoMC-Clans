package com.BoMC.Clans;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import com.BoMC.Clans.Clan.Subclan;

public class ClansPlugin extends JavaPlugin {

	private List<Clan> clans;
	private static final String DEFAULT_CLAN_ID = "default";

	@Override
	public void onEnable() {
		saveDefaultConfig();
		clans = (List<Clan>) getConfig().getList("clans", new ArrayList<Clan>());
		createDefaultClans();
		getServer().getPluginManager().registerEvents(new LoginListener(this), this);
		getLogger().info("Enabled!");
	}

	@Override
	public void onDisable() {
		getLogger().info("Disabled!");
	}

	public List<Clan> getClans() {
		return clans;
	}

	public boolean doesPlayerHaveClan(OfflinePlayer op) {
		for (Clan c : clans) {
			if (c.getMembers().contains(op))
				return true;
		}
		return false;
	}

	public Clan getClanForPlayer(OfflinePlayer op) {
		for (Clan c : clans) {
			if (c.getMembers().contains(op))
				return c;
		}
		return null;
	}

	public void createClan(String id, Subclan sub) {
		clans.add(new Clan(id, sub));
		getLogger().info("Created a new clan with id '" + id + "' and tribe '" + sub.label + "'");
	}

	// This will create any default clans, if they don't exist. It will create one
	// clan for each tribe. If any default clans do exist, they will be ignored.
	// Thus, this can be safely called at any time with no repercussions, because it
	// will only create new clans, not remove or overwrite existing ones.
	public void createDefaultClans() {

		// Loop through all subclans (tribes)
		for (Subclan sub : Subclan.values()) {

			// Flag for whether a default clan for that tribe exists
			boolean exists = false;

			// Loop through every clan with that tribe
			for (Clan c : clans.stream().filter(c -> c.getSubclan().equals(sub)).collect(Collectors.toList())) {
				if (c.getID().equals(DEFAULT_CLAN_ID)) {
					exists = true;
					break;
				}
			}

			if (!exists) {
				createClan(DEFAULT_CLAN_ID, sub);
			}
		}
	}

	// This method will look through all the subclans and find the smallest one.
	// This method assumes that the default clans have been created using
	// createDefaultClans()
	public Clan findSmallestDefaultClan() {
		List<Clan> defaultClans = clans.stream().filter(c -> c.getID().equals(DEFAULT_CLAN_ID)).collect(Collectors.toList());

		defaultClans.sort((c1, c2) -> c1.getMembers().size() - c2.getMembers().size());

		return defaultClans.get(0);
	}
}
