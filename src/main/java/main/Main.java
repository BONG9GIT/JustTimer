package main;

import main.task.TaskGameTime;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements CommandExecutor {

    private static Main main;
    public static Main getInstance() { return main; }



    public TaskGameTime taskGameTime = null;



    @Override
    public void onEnable() {
        main = this;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String... args) {
        if (cmd.getName().equalsIgnoreCase("timer_start")) {
            if (args.length == 1) {
                try {
                    int sec = Integer.parseInt(args[0]);

                    if (taskGameTime == null) taskGameTime = new TaskGameTime(sec);
                    else sender.sendMessage(ChatColor.RED + "이미 존재하는 task");
                }
                catch (Exception err) { err.printStackTrace(); }
            }
        }
        else if (cmd.getName().equalsIgnoreCase("timer_stop")) {
            if (taskGameTime != null) taskGameTime.cancel();
        }

        return true;
    }
}
