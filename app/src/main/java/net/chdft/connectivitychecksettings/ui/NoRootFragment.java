package net.chdft.connectivitychecksettings.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.chdft.connectivitychecksettings.R;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoRootFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoRootFragment extends Fragment {

    public NoRootFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NoRootFragment.
     */
    public static NoRootFragment newInstance() {
        NoRootFragment fragment = new NoRootFragment();
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
        View view = inflater.inflate(R.layout.fragment_no_root, container, false);
        ((Button)view.findViewById(R.id.buttonRestart)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mStartActivity = new Intent(getContext(), SwitcherActivity.class);
                int mPendingIntentId = 123456;
                PendingIntent mPendingIntent = PendingIntent.getActivity(getContext(), mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager mgr = (AlarmManager) Objects.requireNonNull(getContext()).getSystemService(Context.ALARM_SERVICE);
                if(mgr != null) {
                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                }
                System.exit(0);
            }
        });
        return view;
    }
}