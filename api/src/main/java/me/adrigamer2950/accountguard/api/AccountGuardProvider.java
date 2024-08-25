package me.adrigamer2950.accountguard.api;

import org.jetbrains.annotations.ApiStatus;

public class AccountGuardProvider {

    private static AccountGuard instance;

    public static AccountGuard get() {
        AccountGuard instance = AccountGuardProvider.instance;
        if (instance == null) {
            throw new IllegalStateException("API is not loaded yet");
        }
        return instance;
    }

    @ApiStatus.Internal
    public static void register(AccountGuard instance) {
        AccountGuardProvider.instance = instance;
    }

    @ApiStatus.Internal
    public static void unRegister() {
        AccountGuardProvider.instance = null;
    }

    @ApiStatus.Internal
    private AccountGuardProvider() {
        throw new UnsupportedOperationException("This class is unconstructable");
    }
}
