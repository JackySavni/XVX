package com.video.player.videoplayer.xvxvideoplayer.extra;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class VP_DataSource {
    public static final String VP_URL_KEY_DEFAULT = "URL_KEY_DEFAULT";
    public int vp_currentUrlIndex;
    public HashMap<String, String> vp_headerMap = new HashMap<>();
    public boolean vp_looping = false;
    public String vp_title = "";
    public LinkedHashMap vp_urlsMap;

    public VP_DataSource(String str, String str2) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        this.vp_urlsMap = linkedHashMap;
        linkedHashMap.put(VP_URL_KEY_DEFAULT, str);
        this.vp_title = str2;
        this.vp_currentUrlIndex = 0;
    }

    public VP_DataSource(LinkedHashMap linkedHashMap, String str) {
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        this.vp_urlsMap = linkedHashMap2;
        linkedHashMap2.clear();
        this.vp_urlsMap.putAll(linkedHashMap);
        this.vp_title = str;
        this.vp_currentUrlIndex = 0;
    }

    public Object getCurrentUrl() {
        return vp_getValueFromLinkedMap(this.vp_currentUrlIndex);
    }

    public Object getCurrentKey() {
        return vp_getKeyFromDataSource(this.vp_currentUrlIndex);
    }

    public String vp_getKeyFromDataSource(int i) {
        int i2 = 0;
        for (Object obj : this.vp_urlsMap.keySet()) {
            if (i2 == i) {
                return obj.toString();
            }
            i2++;
        }
        return null;
    }

    public Object vp_getValueFromLinkedMap(int i) {
        int vp_i2 = 0;
        for (Object vp_obj : this.vp_urlsMap.keySet()) {
            if (vp_i2 == i) {
                return this.vp_urlsMap.get(vp_obj);
            }
            vp_i2++;
        }
        return null;
    }

    public VP_DataSource vp_cloneMe() {
        LinkedHashMap linkedHashMap = new LinkedHashMap(this.vp_urlsMap);
        return new VP_DataSource(linkedHashMap, this.vp_title);
    }
}
