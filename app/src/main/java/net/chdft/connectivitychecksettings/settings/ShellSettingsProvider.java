package net.chdft.connectivitychecksettings.settings;

import android.util.Pair;

import com.jaredrummler.android.shell.CommandResult;
import com.jaredrummler.android.shell.Shell;

import java.util.HashSet;
import java.util.Set;

public class ShellSettingsProvider extends SettingsProvider {

    public Set<String> getAllKeys(Namespace namespace) {
        CommandResult result = Shell.SU.run("settings list " + getNamespaceName(namespace));
        String rawDump = result.getStdout();
        HashSet<String> all = new HashSet<>();
        for (String line:rawDump.split("\n")) {
            if(!"".equals(line)) {
                String[] parts = line.split("=");
                all.add(parts[0]);
            }
        }

        return all;
    }


    public Set<Pair<String, String>> getAll(Namespace namespace) {
        CommandResult result = Shell.SU.run("settings list " + getNamespaceName(namespace));
        String rawDump = result.getStdout();
        HashSet<Pair<String, String>> all = new HashSet<>();
        for (String line:rawDump.split("\n")) {
            if(!"".equals(line)) {
                String[] parts = line.split("=");
                all.add(new Pair<>(parts[0], parts[1]));
            }
        }

        return all;
    }

    @Override
    public String get(Namespace namespace, String key) {

        CommandResult result = Shell.SU.run("settings get " + getNamespaceName(namespace) + " \"" + key + "\"");
        String resultValue = result.getStdout();
        if(resultValue.equals("null")){
            //check if setting is even defined
            if(!this.getAllKeys(namespace).contains(key)){
                resultValue = null;
            }
        }
        return resultValue;
    }

    @Override
    public void put(Namespace namespace, String key, String value) {
        //TODO: escape the value properly
        //for now not escaping is acceptable, since the user is trusted
        CommandResult result = Shell.SU.run("settings put " + getNamespaceName(namespace) + " \"" + key + "\" \"" + value + "\"");
    }
}
