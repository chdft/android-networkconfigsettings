package net.chdft.connectivitychecksettings.settings;

import android.util.Pair;

import java.util.Set;

public abstract class SettingsProvider {
    public abstract String get(Namespace namespace, String key);

    public abstract void put(Namespace namespace, String key, String value);

    protected String getNamespaceName(Namespace namespace){
        switch (namespace){
            case Global: return "Global";
            case Secure: return "Secure";
            case System: return "System";
            default:
                throw new IllegalArgumentException();
        }
    }

    public enum Namespace{
        Global,
        System,
        Secure
    }
}
