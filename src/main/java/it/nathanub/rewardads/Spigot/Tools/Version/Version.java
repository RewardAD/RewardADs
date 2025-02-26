package it.nathanub.rewardads.Spigot.Tools.Version;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.nathanub.rewardads.Spigot.Tools.Api.Api;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import it.nathanub.rewardads.Spigot.Tools.Logs.Error;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Version {
    private final Plugin plugin;
    private final String code;

    public Version(Plugin plugin) {
        this.plugin = plugin;
        this.code = plugin.getConfig().getString("code");
    }

    public void checkForUpdate() {
        CompletableFuture.supplyAsync(() -> {
            try {
                String response = Api.handle("https://api.spigotmc.org/simple/0.2/index.php?action=getResource&id=121867").get();
                JsonElement jsonElement = new JsonParser().parse(response);
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String currentVersion = jsonObject.get("current_version").getAsString();
                return Objects.equals(currentVersion, getPlugin());
            } catch(Exception e) {
                Error.send(this.code, e);
                return false;
            }
        }).thenAccept(upToDate -> {
            if(!upToDate)
                plugin.getLogger().warning("Keep me updated! Download the latest version from https://spi.rewardads.it.");
        });
    }

    public boolean isServerHigher() {
        String version = Bukkit.getBukkitVersion().split("-")[0];
        String[] parts = version.split("\\.");

        int ver = Integer.parseInt(parts[1]);
        return ver >= 13;
    }

    public String getPlugin() {
        return this.plugin.getDescription().getVersion();
    }
}
