package com.example.user.kinveyprac;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Created by Dongmin on 2017-11-14.
 */

public class Bicycle extends GenericJson {
    @Key("_id")
    private String id;

    @Key("key")
    private String key;

    @Key("status")
    private String status;

    @Key("passwd")
    private String passwd;

    public Bicycle() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
