package me.ikeirnez.permissiondump;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PermissionDump extends JavaPlugin {

    Path path;
    
    public void onEnable(){
        getCommand("permdump").setExecutor(new PermDumpCommand(this));
        
        if (!getDataFolder().exists()){
            getDataFolder().mkdir();
        }
        
        File permDump = new File(getDataFolder(), "PermissionDump.txt");
        if (!permDump.exists()){
            try {
                permDump.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }
        
        try {
            path = Paths.get(permDump.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    public void writeToDump(List<String> lines) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)){
            for (String line : lines){
                writer.write(line);
                writer.newLine();
            }
        }
    }

}
