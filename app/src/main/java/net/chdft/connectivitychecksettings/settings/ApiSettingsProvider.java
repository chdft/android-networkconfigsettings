package net.chdft.connectivitychecksettings.settings;

import android.content.ContentResolver;
import android.provider.Settings;
import android.util.Pair;

import java.util.Set;

public class ApiSettingsProvider extends SettingsProvider {
    private ContentResolver resolver;

    public ApiSettingsProvider(ContentResolver resolver){
        this.resolver = resolver;
    }

    @Override
    public String get(Namespace namespace, String key) {
        switch (namespace){
            case Global: return Settings.Global.getString(resolver, key);
            case System: return Settings.System.getString(resolver, key);
            case Secure: return Settings.Secure.getString(resolver, key);
        }
        return null;
    }

    @Override
    public void put(Namespace namespace, String key, String value) {
        switch (namespace){
            case Global: Settings.Global.putString(resolver, key, value); break;
            case System: Settings.System.putString(resolver, key, value); break;
            case Secure: Settings.Secure.putString(resolver, key, value); break;
        }
    }
}
