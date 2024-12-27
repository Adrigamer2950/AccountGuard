package me.adrigamer2950.accountguard.common.messages;

public record Messages(
        String NO_PERMISSION,
        String NO_CHANGE_OTHER_WHITELIST_PERMISSION,
        String NO_VIEW_OTHER_WHITELIST_PERMISSION,
        String IP_NOT_SPECIFIED,
        String INVALID_IP,
        String PLAYER_NAME_NOT_SPECIFIED_FROM_CONSOLE,
        String PLAYER_NOT_FOUND,
        String IP_ADDED_IN_WHITELIST,
        String IP_ALREADY_IN_WHITELIST,
        String IP_REMOVED_FROM_WHITELIST,
        String IP_NOT_IN_WHITELIST,
        String WHITELIST_IP_LIST,
        String RELOAD_MESSAGE,
        String KICK_MESSAGE
) {
}
