package me.adrigamer2950.accountguard.velocity.objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class OfflinePlayer {

    private final UUID uuid;
    private final String name;
}
