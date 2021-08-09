package com.BoMC.Clans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class Clan implements ConfigurationSerializable {

	public enum Side {
		NEPHITES, LAMANITES
	}

	// This enum holds the data for the 7 possible tribes allowed. Multiple clans
	// with the same "subclan/tribe" can exist.
	public enum Subclan implements ConfigurationSerializable {

		NEPHITES("Nephites", Side.NEPHITES), JACOBITES("Jacobites", Side.NEPHITES),
		JOSEPHITES("Josephites", Side.NEPHITES), ZORAMITES("Zoramites", Side.NEPHITES),
		LAMANITES("Lamanites", Side.LAMANITES), LEMUELITES("Lemuelites", Side.LAMANITES),
		ISHMAELITES("Ishmaelites", Side.LAMANITES);

		public final String label;
		public final Side side;

		// Deserialization constructor
		Subclan(Map<String, Object> m) {
			label = (String) m.get("label");
			side = Side.valueOf((String) m.get("side"));
		}

		// Regular constructor (used above for the 7 values)
		Subclan(String l, Side s) {
			label = l;
			side = s;
		}

		@Override
		public Map<String, Object> serialize() {
			Map<String, Object> m = new HashMap<>();
			m.put("label", label);
			m.put("side", side.toString());
			return m;
		}
	}

	// Each clan has a list of members (stored by UUID), a label string, and a
	// subclass enum constant defining which of the 7 tribes they are from/in
	private List<UUID> members;
	private String id;
	private Subclan sub;

	// Deserialization constructor
	public Clan(Map<String, Object> m) {
		members = (List<UUID>) m.get("members");
		id = (String) m.get("id");
		sub = (Subclan) m.get("subclan");
	}

	// Regular constructor (called when a new clan is created, usually by a player,
	// except for the first 7, which are done by the server for the 7 default clans)
	public Clan(String i, Subclan s) {
		members = new ArrayList<UUID>();
		id = i;
		sub = s;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> m = new HashMap<>();
		m.put("members", members);
		m.put("id", id);
		m.put("subclan", sub);
		return m;
	}

	// The only time we don't want to retrive data in the form of OfflinePlayer,
	// it's because we're saving to config, and that is already covered by
	// serialize()
	public List<OfflinePlayer> getMembers() {
		return members.stream().map(Bukkit::getOfflinePlayer).collect(Collectors.toList());
	}

	public String getID() {
		return id;
	}

	public Subclan getSubclan() {
		return sub;
	}

	public Side getSide() {
		return sub.side;
	}

}
