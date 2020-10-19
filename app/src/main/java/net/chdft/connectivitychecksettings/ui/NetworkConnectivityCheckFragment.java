package net.chdft.connectivitychecksettings.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.chdft.connectivitychecksettings.R;
import net.chdft.connectivitychecksettings.settings.ApiSettingsProvider;
import net.chdft.connectivitychecksettings.settings.SettingsProvider;
import net.chdft.connectivitychecksettings.settings.ShellSettingsProvider;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NetworkConnectivityCheckFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NetworkConnectivityCheckFragment extends Fragment {
    EditText serverUrlHttp;
    EditText serverUrlHttps;
    RecyclerView knownServersList;
    RecyclerView.LayoutManager knownServersListLayoutManager;
    Button saveSettings;
    Button loadSettings;
    SettingsProvider settingsProvider;
    TextView reliabilityHint;

    public NetworkConnectivityCheckFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NetworkConnectivityCheckFragment.
     */
    public static NetworkConnectivityCheckFragment newInstance() {
        NetworkConnectivityCheckFragment fragment = new NetworkConnectivityCheckFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_network_connectivity_check, container, false);

        serverUrlHttp=view.findViewById(R.id.editTextTextServerUrlHttp);
        serverUrlHttps=view.findViewById(R.id.editTextTextServerUrlHttps);


        knownServersList=view.findViewById(R.id.recycleViewServersList);
        knownServersListLayoutManager=new LinearLayoutManager(this.getContext());
        knownServersList.setLayoutManager(knownServersListLayoutManager);
        knownServersList.setAdapter(new KnownServerAdapter());

        saveSettings=view.findViewById(R.id.buttonSaveSettings);
        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setServer(serverUrlHttp.getText().toString(), false);
                setServer(serverUrlHttps.getText().toString(), true);
            }
        });
        loadSettings=view.findViewById(R.id.buttonLoad);
        loadSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUiFromSettings();
            }
        });

        reliabilityHint = view.findViewById(R.id.textViewReliabilityStatus);
        reliabilityHint.setText(getResources().getBoolean(R.bool.is_settingmap_known) ? R.string.settingsmap_reliable : R.string.settingsmap_unreliable);
        if(!getResources().getBoolean(R.bool.is_settingmap_known)){
            reliabilityHint.setTypeface(reliabilityHint.getTypeface(), Typeface.BOLD_ITALIC);
        }

        if(getResources().getStringArray(R.array.connecivity_check_server_settings_http).length == 0){
            view.findViewById(R.id.editTextTextServerUrlHttp).setVisibility(View.GONE);
        }
        if(getResources().getStringArray(R.array.connecivity_check_server_settings_https).length == 0){
            view.findViewById(R.id.editTextTextServerUrlHttps).setVisibility(View.GONE);
        }

        updateUiFromSettings();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.top_level, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.about:
                Intent aboutIntent = new Intent(getContext(), LicensesActivity.class);
                startActivity(aboutIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected SettingsProvider getSettingsProvider(){
        if(settingsProvider==null){
            if(requireContext().checkSelfPermission("android.permission.WRITE_SECURE_SETTINGS") == PackageManager.PERMISSION_GRANTED){
                settingsProvider = new ApiSettingsProvider(requireContext().getContentResolver());
            }else {
                settingsProvider = new ShellSettingsProvider();
            }
        }
        return settingsProvider;
    }

    private void updateUiFromSettings(){
        {
            String server = getServer(false);
            if (server == null) {
                server = "";
            }
            serverUrlHttp.setText(server);
        }
        {
            String server = getServer(true);
            if (server == null) {
                server = "";
            }
            serverUrlHttps.setText(server);
        }
    }

    private void setServer(String serverUrl, boolean https){
        SettingsProvider settingsProvider = getSettingsProvider();
        for(String settingsKey : getResources().getStringArray(https ? R.array.connecivity_check_server_settings_https : R.array.connecivity_check_server_settings_http)){
            settingsProvider.put(SettingsProvider.Namespace.Global, settingsKey, serverUrl);
        }
    }

    private String getServer(boolean https){
        SettingsProvider settingsProvider = getSettingsProvider();
        String value = null;
        for(String settingsKey : getResources().getStringArray(https ? R.array.connecivity_check_server_settings_https : R.array.connecivity_check_server_settings_http)){
            value = settingsProvider.get(SettingsProvider.Namespace.Global, settingsKey);
            if(value != null){
                break;
            }
        }
        return value;
    }

    class KnownServerAdapter extends RecyclerView.Adapter<KnownServerAdapter.KnownServerViewHolder> {
        private String[] titles;
        private String[] urlsHttps;
        private String[] urlsHttp;
        private String[] descriptions;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class KnownServerViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView title;
            public TextView urlHttp;
            public TextView urlHttps;
            public TextView description;
            public KnownServerViewHolder(View v) {
                super(v);
                title = v.findViewById(R.id.textViewTitle);
                urlHttp=v.findViewById(R.id.textViewServerUrlHttp);
                urlHttps=v.findViewById(R.id.textViewServerUrlHttps);
                description=v.findViewById(R.id.textViewDescription);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        serverUrlHttp.setText(urlHttp.getText());
                        serverUrlHttps.setText(urlHttps.getText());
                    }
                });
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public KnownServerAdapter() {
            updateData();
        }

        public void updateData(){
            titles = getResources().getStringArray(R.array.known_ncc_servers_title);
            urlsHttp=getResources().getStringArray(R.array.known_ncc_servers_url_http);
            urlsHttps=getResources().getStringArray(R.array.known_ncc_servers_url_https);
            descriptions=getResources().getStringArray(R.array.known_ncc_servers_description);
        }

        // Create new views (invoked by the layout manager)
        @NonNull
        @Override
        public KnownServerAdapter.KnownServerViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                        int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_ncc_server, parent, false);

            return new KnownServerAdapter.KnownServerViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(KnownServerAdapter.KnownServerViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.urlHttp.setText(urlsHttp[position]);
            holder.urlHttps.setText(urlsHttps[position]);
            holder.title.setText(titles.length > position ? titles[position] : "Unknown");
            holder.description.setText(descriptions.length > position ? descriptions[position] : "Unknown");
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return urlsHttp.length;
        }
    }
}