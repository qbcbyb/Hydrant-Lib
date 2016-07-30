package com.github.qbcbyb.versioncheck.firim;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by 秋云 on 2015/5/15.
 */
public class VersionInfo {
    private static final long serialVersionUID = -8020663228596890546L;
    private String name;
    private String version;
    private String changelog;
    private String versionShort;
    private String installUrl;
    private String update_url;
    private String install_url;

    public String getName() {
        return name;
    }

    @JSONField(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    @JSONField(name = "version")
    public void setVersion(String version) {
        this.version = version;
    }

    public String getChangelog() {
        return changelog;
    }

    @JSONField(name = "changelog")
    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public String getVersionShort() {
        return versionShort;
    }

    @JSONField(name = "versionShort")
    public void setVersionShort(String versionShort) {
        this.versionShort = versionShort;
    }

    public String getInstallUrl() {
        return installUrl;
    }

    @JSONField(name = "installUrl")
    public void setInstallUrl(String installUrl) {
        this.installUrl = installUrl;
    }

    public String getUpdate_url() {
        return update_url;
    }

    @JSONField(name = "update_url")
    public void setUpdate_url(String update_url) {
        this.update_url = update_url;
    }

    public String getInstall_url() {
        return install_url;
    }

    @JSONField(name = "install_url")
    public void setInstall_url(String install_url) {
        this.install_url = install_url;
    }
}
