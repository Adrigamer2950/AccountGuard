package me.adrigamer2950.accountguard.bukkit.config;

import me.adrigamer2950.adriapi.api.config.yaml.YamlConfig;

public class Messages {

    public final String NO_PERMISSION;
    public final String KICK_REASON;
    public final String PLAYER_NOT_SPECIFIED;
    public final String PLAYER_DOESNT_EXISTS;
    public final String IP_NOT_SPECIFIED;
    public final String INVALID_IP;
    public final String IP_ADDED_TO_WHITELIST;
    public final String IP_ALREADY_IN_WHITELIST;
    public final String IP_REMOVED_FROM_WHITELIST;
    public final String IP_NOT_IN_WHITELIST;
    public final String RELOAD_MESSAGE;
    public final String LIST_IPS_MESSAGE;

    public Messages(YamlConfig yaml) {
        this.NO_PERMISSION = yaml.getYaml().getString("no-permission");
        this.KICK_REASON = yaml.getYaml().getString("kick-reason");
        this.PLAYER_NOT_SPECIFIED = yaml.getYaml().getString("player-not-specified");
        this.PLAYER_DOESNT_EXISTS = yaml.getYaml().getString("player-doesnt-exists");
        this.IP_NOT_SPECIFIED = yaml.getYaml().getString("ip-not-specified");
        this.INVALID_IP = yaml.getYaml().getString("invalid-ip");
        this.IP_ADDED_TO_WHITELIST = yaml.getYaml().getString("ip-added-to-whitelist");
        this.IP_ALREADY_IN_WHITELIST = yaml.getYaml().getString("ip-already-in-whitelist");
        this.IP_REMOVED_FROM_WHITELIST = yaml.getYaml().getString("ip-removed-from-whitelist");
        this.IP_NOT_IN_WHITELIST = yaml.getYaml().getString("ip-not-in-whitelist");
        this.RELOAD_MESSAGE = yaml.getYaml().getString("reload-message");
        this.LIST_IPS_MESSAGE = yaml.getYaml().getString("list-ips-message");
    }
}
