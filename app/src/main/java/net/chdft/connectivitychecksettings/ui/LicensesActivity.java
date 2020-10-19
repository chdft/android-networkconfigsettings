package net.chdft.connectivitychecksettings.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.chdft.connectivitychecksettings.R;

public class LicensesActivity extends AppCompatActivity {

    RecyclerView licenseList;
    RecyclerView.LayoutManager licenseListLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licenses);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        licenseList=findViewById(R.id.recyclerViewLicenseList);
        licenseListLayoutManager=new LinearLayoutManager(this);
        licenseList.setLayoutManager(licenseListLayoutManager);
        licenseList.setAdapter(new LicensesAdapter());
    }

    class LicensesAdapter extends RecyclerView.Adapter<LicensesAdapter.LicenseViewHolder> {
        private String[] libariesName;
        private String[] librariesUrl;
        private String[] licensesName;
        private String[] licensesUrl;
        private String[] uses;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class LicenseViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView licenseName;
            public TextView use;
            public TextView libraryName;
            public String libraryUrl;
            public String licenseUrl;
            public LicenseViewHolder(View v) {
                super(v);
                licenseName=v.findViewById(R.id.textViewLicenseName);
                use=v.findViewById(R.id.textViewUse);
                libraryName=v.findViewById(R.id.textViewLibraryName);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(libraryUrl)));
                    }
                });
                licenseName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(licenseUrl)));
                    }
                });
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public LicensesAdapter() {
            updateData();
        }

        public void updateData(){
            libariesName = getResources().getStringArray(R.array.licenses_libraryname);
            librariesUrl = getResources().getStringArray(R.array.licenses_librarylink);
            licensesName = getResources().getStringArray(R.array.licenses_licensename);
            licensesUrl = getResources().getStringArray(R.array.licenses_licenselink);
            uses = getResources().getStringArray(R.array.licenses_use);
        }

        // Create new views (invoked by the layout manager)
        @NonNull
        @Override
        public LicenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_license, parent, false);

            return new LicenseViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(LicenseViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.use.setText(uses[position]);
            holder.licenseName.setText(licensesName[position]);
            holder.libraryName.setText(libariesName[position]);
            holder.libraryUrl=librariesUrl[position];
            holder.licenseUrl=licensesUrl[position];
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return libariesName.length;
        }
    }
}