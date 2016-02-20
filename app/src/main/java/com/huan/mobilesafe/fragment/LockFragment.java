package com.huan.mobilesafe.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huan.mobilesafe.R;

/**
 * LockFragment
 *
 * @author: 欢
 * @time: 2016/2/18 21:19
 */
public class LockFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_lock_list, null);
        return view;
    }
}
