package com.indix.gocd.models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Revision implements Comparable {
    private String revision;
    private String[] parts;
    private String major;
    private String minor;
    private String patch;

    public Revision(String revision) {
        this.revision = revision;
        this.parts = revision.split("\\.");
        this.major = parts[0];
        this.minor = parts[1];
        if (parts.length == 3) {
            this.patch = parts[2];
        } else {
            this.patch = "0";
        }
    }

    public Revision(String key, String patternString) {
        this.revision = key;
        Pattern pattern = Pattern.compile(patternString);
        Matcher m = pattern.matcher(key);
        if (m.matches()) {
            this.major = m.group("major");
            this.minor = m.group("minor");
            this.patch = m.group("patch");
        }
    }


    public static Revision base() {
        return new Revision("0.0.0");
    }

    @Override
    public int compareTo(Object otherInstance) {
        if(! (otherInstance instanceof Revision))
            throw new RuntimeException("Cannot compare a non-Revision type with Revision type");
        Revision that = (Revision)otherInstance;
        int majorDiff = this.major.compareTo(that.major);
        int minorDiff = this.minor.compareTo(that.minor);
        int patchDiff = this.patch.compareTo(that.patch);

        if(majorDiff != 0)
            return majorDiff;
        else if(minorDiff != 0)
            return minorDiff;
        else if(patchDiff != 0)
            return patchDiff;
        else
            return 0;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }
}
