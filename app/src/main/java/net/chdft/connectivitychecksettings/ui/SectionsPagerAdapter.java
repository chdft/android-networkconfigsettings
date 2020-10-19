package net.chdft.connectivitychecksettings.ui;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.jaredrummler.android.shell.Shell;

import net.chdft.connectivitychecksettings.R;

import java.util.Objects;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.section_networkconnectivitycheck};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
       if(Objects.requireNonNull(mContext).checkSelfPermission("android.permission.WRITE_SECURE_SETTINGS") == PackageManager.PERMISSION_GRANTED || Shell.SU.available()){
            switch (position){
                case 0: return NetworkConnectivityCheckFragment.newInstance();
                case 1: return NtpFragment.newInstance();
                default: return null; //should never happen
            }
       }else{
           return NoRootFragment.newInstance();
       }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}