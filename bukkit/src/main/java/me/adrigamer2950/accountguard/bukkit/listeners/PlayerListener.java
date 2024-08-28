package me.adrigamer2950.accountguard.bukkit.listeners;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.time.NtpTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import lombok.RequiredArgsConstructor;
import me.adrigamer2950.accountguard.bukkit.AGBukkit;
import me.adrigamer2950.accountguard.bukkit.map.QrMapRenderer;
import me.adrigamer2950.adriapi.api.colors.Colors;
import me.adrigamer2950.adriapi.api.folia.Scheduler;
import me.adrigamer2950.adriapi.api.user.User;
import me.adrigamer2950.adriapi.api.user.UserImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class PlayerListener implements Listener {

    private final AGBukkit plugin;

    private final Map<Player, Map<Scheduler.Task, String>> totpWaiting = new HashMap<>();

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerPreJoin(AsyncPlayerPreLoginEvent e) {
        String ip = e.getAddress().getHostAddress();

        if (!this.plugin.database.getIPs(e.getUniqueId()).isEmpty() && !this.plugin.database.getIPs(e.getUniqueId()).contains(ip)) {
            e.setLoginResult(
                    AsyncPlayerPreLoginEvent.Result.KICK_OTHER
            );
            //noinspection deprecation
            e.setKickMessage(Colors.translateColors("&cYour IP is not allowed to join this server as &a%s\n\n&6Powered by &cAccountGuard".formatted(e.getName())));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!this.plugin.config.totp().enable()) return;

        final ItemStack item = new ItemStack(Material.FILLED_MAP);

        final MapMeta mapMeta = (MapMeta) item.getItemMeta();

        MapView view = Bukkit.createMap(e.getPlayer().getWorld());

        QrMapRenderer mapRenderer = new QrMapRenderer(this.plugin, e.getPlayer().getName());

        view.getRenderers().clear();
        view.addRenderer(mapRenderer);

        mapMeta.setMapView(view);
        item.setItemMeta(mapMeta);

        e.getPlayer().getInventory().setItem(0, item);
        e.getPlayer().updateInventory();

        Location l = e.getPlayer().getLocation();
        l.setPitch(45.0f);
        e.getPlayer().teleport(l);

        Scheduler.Task task = this.plugin.getScheduler().runTimer( () -> {
            User user = new UserImpl(e.getPlayer());

            user.sendMessage(
                    "&m|                                                                   &r|",
                    this.plugin.config.prefix() + "&aPlease scan this QR Code in your TOTP app in order to play in this server",
                    this.plugin.config.prefix() + "&aThen, type your TOTP code in the chat to verify",
                    this.plugin.config.prefix() + "&aYou could also use this secret: &6&l" + mapRenderer.getTotp().getSecret(),
                    "&m|                                                                   &r|"
            );
            user.getPlayerOrNull().playSound(user.getPlayerOrNull(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
        }, 0L, 80L);

        Map<Scheduler.Task, String> map = new HashMap<>();

        map.put(task, mapRenderer.getTotp().getSecret());

        this.totpWaiting.put(e.getPlayer(), map);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (!this.totpWaiting.containsKey(e.getPlayer())) return;

        if (
                e.getFrom().getX() != e.getTo().getX()
                || e.getFrom().getY() != e.getTo().getY()
                || e.getFrom().getZ() != e.getTo().getZ()
        )
            e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) throws UnknownHostException {
        if (!this.totpWaiting.containsKey(e.getPlayer())) return;

        TimeProvider timeProvider = new NtpTimeProvider("pool.ntp.org");
        CodeGenerator codeGenerator = new DefaultCodeGenerator(this.plugin.config.totp().algorithm());
        DefaultCodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

        Map<Scheduler.Task, String> map = this.totpWaiting.get(e.getPlayer());

        Scheduler.Task task = map.keySet().stream().findFirst().get();
        String secret = map.get(task);

        boolean successful = verifier.isValidCode(secret, e.getMessage());

        if (successful) {
            task.cancel();

            new UserImpl(e.getPlayer()).sendMessage("&aYou successfully authenticated with the server.");

            this.totpWaiting.remove(e.getPlayer());
        } else {
            new UserImpl(e.getPlayer()).sendMessage("&cIncorrect code.");
        }

        e.setCancelled(true);
    }
}
