package me.adrigamer2950.accountguard.api;

import java.util.Set;
import java.util.UUID;

/**
 * AccountGuard API
 * @author Adrigamer2950
 * @since 1.0.0
 */
public interface AccountGuard {

    /**
     * @param uuid The player's UUID
     * @return A Set of IPs this player is able to join from
     */
    Set<String> getIPs(UUID uuid);

    /**
     * @param ip The IP
     * @return True if the IP is a valid IPV4 or IPV6, false otherwise
     */
    boolean isValidIP(String ip);

    /**
     * Adds an IP to the player's IP whitelist
     * @param uuid The player's UUID
     * @param ip The IP that you want to add
     * @return True if the IP was added, false if it was already in
     */
    boolean addIP(UUID uuid, String ip);

    /**
     * Removes an IP from the player's IP whitelist
     * @param uuid The player's UUID
     * @param ip The IP that you want to add
     * @return True if the IP was removed, false if it wasn't already in
     */
    boolean removeIP(UUID uuid, String ip);

    /**
     * Checks if an IP is whitelisted in player's IP whitelist
     * @param uuid The player's UUID
     * @param ip The IP
     * @return True if the IP is in player's IP whitelist, false otherwise
     */
    boolean hasIP(UUID uuid, String ip);
}
