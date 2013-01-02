package me.ikeirnez.permissiondump;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class PermDumpCommand implements CommandExecutor {

    private PermissionDump plugin;

    public PermDumpCommand(PermissionDump plugin){
        this.plugin = plugin;
    }

    public boolean onCommand(final CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        if (sender.hasPermission("permissiondump.do")){
            sender.sendMessage(ChatColor.GREEN + "Dumping data...");
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
                public void run() {
                    List<String> lines = new ArrayList<String>();

                    for (Permission perm : Bukkit.getPluginManager().getPermissions()){
                        StringBuilder sb = new StringBuilder();
                        sb.append(perm.getName());
                        sb.append(" Default: ");
                        sb.append(perm.getDefault().name());
                        sb.append(" Desc: ");
                        sb.append(perm.getDescription());

                        List<String> inherits = new ArrayList<String>();
                        List<String> notInherits = new ArrayList<String>();

                        for (String child : perm.getChildren().keySet()){
                            boolean value = perm.getChildren().get(child);

                            if (value){
                                inherits.add(child);
                            } else {
                                notInherits.add(child);
                            }
                        }

                        if (inherits.size() > 0){
                            sb.append(" Inherits: ");
                            sb.append(StringUtils.join(inherits.toArray()));
                        }

                        if (notInherits.size() > 0){
                            sb.append(" Will Not Inherit: ");
                            sb.append(StringUtils.join(notInherits.toArray()));
                        }

                        lines.add(sb.toString());
                    }

                    try {
                        plugin.writeToDump(lines);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    sender.sendMessage(ChatColor.GREEN + "All permissions successfully written to dump");
                }
            }, 0L);
        } else {
            sender.sendMessage(ChatColor.RED + "You don't have permission");
        }

        return true;
    }

}
