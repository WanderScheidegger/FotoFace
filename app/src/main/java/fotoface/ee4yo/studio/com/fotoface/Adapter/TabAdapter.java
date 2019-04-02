package fotoface.ee4yo.studio.com.fotoface.Adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import fotoface.ee4yo.studio.com.fotoface.fragment.CameraFragment;
import fotoface.ee4yo.studio.com.fotoface.fragment.VideoFragment;

public class TabAdapter extends FragmentStatePagerAdapter{

    private String[] tituloAbas = {"FOTOS","VÍDEO"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new CameraFragment();
                break;
            case 1:
                fragment = new VideoFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return tituloAbas.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tituloAbas[position];
    }
}
