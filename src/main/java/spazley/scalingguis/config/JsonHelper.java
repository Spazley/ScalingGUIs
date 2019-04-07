package spazley.scalingguis.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import spazley.scalingguis.ScalingGUIs;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class JsonHelper
{
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static CustomScales scalesFromJsonFile(File file)
    {
        if(file == null || !file.exists()) {
            return new CustomScales();
        }

        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            CustomScales scales = GSON.fromJson(isr, CustomScales.class);
            isr.close();

            return scales;
        } catch(Exception e) {
            ScalingGUIs.logger.error("Failed to load json assets.scalingguis.file. Backing up the current assets.scalingguis.file and creating a new one.", e);
            backupJson(file);

            return new CustomScales();
        }
    }

    public static void scalesToJsonFile(File file, CustomScales scales)
    {
        try {
            if (!file.exists()) {
                if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
        } catch(Exception e) {
            ScalingGUIs.logger.error("An error occurred while saving scales to json assets.scalingguis.file.", e);
            return;
        }

        try(OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            GSON.toJson(scales, osw);
            osw.flush();
        } catch(Exception e) {
            ScalingGUIs.logger.error("An error occured while saving scales to json assets.scalingguis.file.", e);
        }
    }

    private static JsonObject jsonObjectFromJsonObject(JsonObject jsonObjectIn, String id)
    {
        if(jsonObjectIn == null) {
            return new JsonObject();
        }

        if(jsonObjectIn.has(id) && jsonObjectIn.get(id).isJsonObject()) {
            return jsonObjectIn.get(id).getAsJsonObject();
        }

        return new JsonObject();
    }

    public static List<String> getKeyList(JsonObject jsonObjectIn)
    {
        //List<String> keyList = new ArrayList<>(jsonObjectIn.keySet()); //possible with Gson 2.8.1
        List<String> keyList = new ArrayList<>();

        Map<String, Object> map = new LinkedHashMap<>();
        map = (Map<String, Object>) GSON.fromJson(jsonObjectIn, map.getClass());
        keyList = new ArrayList<>(map.keySet());

        //test whether keys are representative of a known class to prevent error when checking
        //if a gui is an instance of a class
        for (String s : keyList) {
            try {
                Class temp = Class.forName(s);
            } catch(Exception e) {
                ScalingGUIs.logger.error("Unknown class '" + s + "'. Removing from check list. Will be left in json assets.scalingguis.file.", e);
                keyList.remove(s);
            }
        }

        return keyList;
    }

    public static int getScaleAsInt(JsonObject jsonObjectIn, String id)
    {
        if (jsonObjectIn.has(id) && jsonObjectIn.getAsJsonPrimitive(id).isNumber()) {
            return (int)jsonObjectIn.getAsJsonPrimitive(id).getAsNumber();
        }

        return Minecraft.getMinecraft().gameSettings.guiScale;
    }

    public static String instanceWithinGroup(Object o, List<String> keyList)
    {
        for (String s : keyList) {
            try {
                Class c = Class.forName(s);
                if (c.isInstance(o)) {
                    return s;
                }
            } catch (Exception e) {
                ScalingGUIs.logger.error("Unknown class '" + s + "'.", e);
            }
        }
        return "NONE";
    }

    public static Map<String, List<String>> defaultBlacklistsFromJsonFile()
    {
        ResourceLocation loc = new ResourceLocation(ScalingGUIs.MODID, "default_blacklist.json");

        try {
            InputStream in = Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream();
            InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
            Map<String, List<String>> map = GSON.fromJson(isr, Map.class);
            return map;
        } catch(Exception e) {
            ScalingGUIs.logger.error("Failed to load default blacklist.", e);
        }

        return new HashMap<>();
        //return array;
    }

    private static void copyFile(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    private static void backupJson(File source)
    {
        String currentTime = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        File backup = new File(source.getParent(), FilenameUtils.getBaseName(source.getName()) + "_BACKUP_" + currentTime + ".json");
        try {
            copyFile(source, backup);
        } catch(Exception except2) {
            ScalingGUIs.logger.error("Failed to backup json assets.scalingguis.file.", except2);
        }
    }
}
