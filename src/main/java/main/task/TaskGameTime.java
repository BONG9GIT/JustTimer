package main.task;

import main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskGameTime extends BukkitRunnable {

    private final String ID_TIMEBAR = "bar_gametime";
    private final int FULLTIME;

    private int microSec = 0;
    private int gameLeft;

    private BossBar timeBar;

    private boolean noti = false;



    public TaskGameTime(int fulltime){
        this.FULLTIME = fulltime;
        this.gameLeft = fulltime;



        this.timeBar = Bukkit.getBossBar(NamespacedKey.minecraft(ID_TIMEBAR));
        if (this.timeBar == null) this.timeBar = Bukkit.createBossBar(NamespacedKey.minecraft(ID_TIMEBAR), ChatColor.GRAY + " ", BarColor.YELLOW, BarStyle.SOLID, (BarFlag) null);



        this.runTaskTimer(Main.getInstance(), 1, 1);
    }

    @Override
    public void cancel(){
        super.cancel();
        Main.getInstance().taskGameTime = null;

        if (timeBar != null) timeBar.removeAll();
        Bukkit.removeBossBar(NamespacedKey.minecraft(ID_TIMEBAR));
    }

    @Override
    public void run(){
        if (Main.getInstance().taskGameTime == null){
            cancel();
            return;
        }



        //Time Format
        StringBuilder _timeFormat = new StringBuilder();

        int min = gameLeft / 60;
        int sec = gameLeft % 60;

        if (min < 10) _timeFormat.append("0").append(min);
        else _timeFormat.append(min);

        _timeFormat.append(" : ");

        if (sec < 10) _timeFormat.append("0").append(sec);
        else _timeFormat.append(sec);



        //Bossbar Maintenance
        if (timeBar != null){
            if (gameLeft < 60) timeBar.setTitle(ChatColor.RED + _timeFormat.toString());
            else timeBar.setTitle(ChatColor.GRAY + _timeFormat.toString());

            Bukkit.getOnlinePlayers().forEach(p -> timeBar.addPlayer(p));
        }
        else{
            timeBar = Bukkit.getBossBar(NamespacedKey.minecraft(ID_TIMEBAR));
            if (timeBar == null) timeBar = Bukkit.createBossBar(NamespacedKey.minecraft(ID_TIMEBAR), ChatColor.GRAY + _timeFormat.toString(), BarColor.YELLOW, BarStyle.SOLID, (BarFlag) null);
        }



        //Game go on
        if (++microSec >= 20){
            microSec = 0;
            gameLeft--;

            if (gameLeft <= 0) {
                cancel();

                Bukkit.getOnlinePlayers().forEach(p -> p.sendTitle(" ", ChatColor.RED + "시간 종료", 10, 100, 10));
            }
        }
    }
}
