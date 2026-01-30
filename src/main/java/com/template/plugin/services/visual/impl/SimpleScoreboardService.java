package com.template.plugin.services.visual.impl;

import com.template.plugin.core.PluginCore;
import com.template.plugin.services.core.interfaces.IConfigService;
import com.template.plugin.services.core.interfaces.IPlaceholderService;
import com.template.plugin.services.visual.interfaces.IScoreboardService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleScoreboardService implements IScoreboardService {

    private PluginCore core;
    private IConfigService configService;
    private IPlaceholderService placeholderService;

    private final Map<UUID, Scoreboard> boards = new ConcurrentHashMap<>();
    private int taskId = -1;

    @Override
    public void onEnable(PluginCore core) {
        this.core = core;
        this.configService = core.getService(IConfigService.class);
        this.placeholderService = core.getService(IPlaceholderService.class);

        // Scoreboard para quem já está online (ex: reload)
        for (Player player : Bukkit.getOnlinePlayers()) {
            createScoreboard(player);
        }

        startTask();
    }

    @Override
    public void onDisable() {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            removeScoreboard(player);
        }

        boards.clear();
    }

    private void startTask() {
        int interval = configService.getScoreboardSettings().getUpdateInterval();
        taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(core.getPlugin(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                updateScoreboard(player);
            }
        }, interval, interval).getTaskId();
    }

    @Override
    public void createScoreboard(Player player) {
        if (!configService.getScoreboardSettings().isEnabled())
            return;

        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = sb.registerNewObjective("main", "dummy", "Title");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        player.setScoreboard(sb);
        boards.put(player.getUniqueId(), sb);
        updateScoreboard(player);
    }

    @Override
    public void removeScoreboard(Player player) {
        boards.remove(player.getUniqueId());
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

    @Override
    public void updateScoreboard(Player player) {
        Scoreboard sb = boards.get(player.getUniqueId());
        if (sb == null)
            return;

        com.template.plugin.services.visual.settings.ScoreboardSettings settings = configService
                .getScoreboardSettings();
        List<String> lines = settings.getLines();
        String title = placeholderService.apply(player, settings.getTitle());

        // Process lines async
        List<String> processedLines = new ArrayList<>();
        for (String line : lines) {
            processedLines.add(placeholderService.apply(player, line));
        }

        // Apply sync
        Bukkit.getScheduler().runTask(core.getPlugin(), () -> {
            Objective obj = sb.getObjective("main");
            if (obj != null) {
                obj.setDisplayName(Color(title));
                updateLines(sb, obj, processedLines);
            }
        });
    }

    private void updateLines(Scoreboard sb, Objective obj, List<String> lines) {
        // Simple Anti-Flicker: using Teams to update entries
        // This is a simplified version. For full robust 1.8-1.20 support, specialized
        // libs like FastBoard are recommended.
        // Here we map lines to scores (lines.size() down to 1).

        int score = lines.size();
        for (String line : lines) {
            line = Color(line);

            // Create or get team
            String teamName = "line-" + score;
            Team team = sb.getTeam(teamName);
            if (team == null) {
                team = sb.registerNewTeam(teamName);
                String entry = ChatColor.values()[score].toString(); // Unique invisible entry
                team.addEntry(entry);
                obj.getScore(entry).setScore(score);
            }

            // Split prefix/suffix for 1.12- support if needed, but for 1.13+ we can put
            // mostly in prefix
            // For safety, let's just set the prefix (limit 64 chars in newer versions)
            // Ideally we logic checking length.

            team.setPrefix(line);
            score--;
        }
    }

    private String Color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
