package me.copdead.realmscraft.marshalling;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class MarshalTeam {
    private static Team marshals = null;

    public MarshalTeam() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        assert manager != null;
        Scoreboard board = manager.getMainScoreboard();
        if(board.getTeam("Marshals") == null)
            createMarshalTeam();
        else marshals = board.getTeam("Marshals");
    }

    private void createMarshalTeam() {
        if(marshals != null) return; //team already exists

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();//we get the main scoreboard, so that users can modify the team.
        Team m = board.registerNewTeam("Marshals");

        m.setColor(ChatColor.GOLD);
        m.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        m.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        marshals = m;
    }

    public static Team getMarshals() {
        return marshals;
    }
}
