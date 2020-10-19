package net.chdft.connectivitychecksettings.ui;

import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.chdft.connectivitychecksettings.R;
import net.chdft.connectivitychecksettings.settings.ApiSettingsProvider;
import net.chdft.connectivitychecksettings.settings.SettingsProvider;
import net.chdft.connectivitychecksettings.settings.ShellSettingsProvider;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NtpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NtpFragment extends Fragment {
    EditText server;
    RecyclerView knownServersList;
    RecyclerView.LayoutManager knownServersListLayoutManager;
    Button saveSettings;
    Button loadSettings;
    SettingsProvider settingsProvider;
    TextView reliabilityHint;

    public NtpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NetworkConnectivityCheckFragment.
     */
    public static NtpFragment newInstance() {
        NtpFragment fragment = new NtpFragment();
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

        server =view.findViewById(R.id.editTextTextServerUrlHttp);


        knownServersList=view.findViewById(R.id.recycleViewServersList);
        knownServersListLayoutManager=new LinearLayoutManager(this.getContext());
        knownServersList.setLayoutManager(knownServersListLayoutManager);
        knownServersList.setAdapter(new KnownServerAdapter());

        saveSettings=view.findViewById(R.id.buttonSaveSettings);
        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setServer(server.getText().toString());
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

    protected SettingsProvider getSettingsProvider(){
        if(settingsProvider==null){
            if(Objects.requireNonNull(getContext()).checkSelfPermission("android.permission.WRITE_SECURE_SETTINGS") == PackageManager.PERMISSION_GRANTED){
                settingsProvider = new ApiSettingsProvider(getContext().getContentResolver());
            }else {
                settingsProvider = new ShellSettingsProvider();
            }
        }
        return settingsProvider;
    }

    private void updateUiFromSettings(){
        {
            String server = getServer();
            if (server == null) {
                server = "";
            }
            this.server.setText(server);
        }
    }

    private void setServer(String serverUrl){
        SettingsProvider settingsProvider = getSettingsProvider();
        for(String settingsKey : getResources().getStringArray(R.array.ntp_server_settings)){
            settingsProvider.put(SettingsProvider.Namespace.Global, settingsKey, serverUrl);
        }
    }

    private String getServer(){
        SettingsProvider settingsProvider = getSettingsProvider();
        String value = null;
        for(String settingsKey : getResources().getStringArray(R.array.ntp_server_settings)){
            value = settingsProvider.get(SettingsProvider.Namespace.Global, settingsKey);
            if(value != null){
                break;
            }
        }
        return value;
    }

    class KnownServerAdapter extends RecyclerView.Adapter<KnownServerAdapter.KnownServerViewHolder> {
        private String[] titles;
        private String[] urlsHttp;
        private String[] descriptions;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class KnownServerViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView title;
            public TextView url;
            public TextView description;
            public KnownServerViewHolder(View v) {
                super(v);
                title = v.findViewById(R.id.textViewTitle);
                url=v.findViewById(R.id.textViewServerUrl);
                description=v.findViewById(R.id.textViewDescription);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        server.setText(url.getText());
                    }
                });
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public KnownServerAdapter() {
            updateData();
        }

        public void updateData(){
            titles = getResources().getStringArray(R.array.known_ntp_servers_title);
            urlsHttp=getResources().getStringArray(R.array.known_ntp_servers_url);
            descriptions=getResources().getStringArray(R.array.known_ntp_servers_description);
        }

        // Create new views (invoked by the layout manager)
        @NonNull
        @Override
        public KnownServerAdapter.KnownServerViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                        int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_ntp_server, parent, false);

            return new KnownServerAdapter.KnownServerViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(KnownServerAdapter.KnownServerViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.url.setText(urlsHttp[position]);
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