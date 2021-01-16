package com.loginiusinfotech.sonapartner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.loginiusinfotech.sonapartner.MainActivity;
import com.loginiusinfotech.sonapartner.R;
import com.loginiusinfotech.sonapartner.activity.HomeActivity;
import com.loginiusinfotech.sonapartner.utils.AppConstants;
import com.loginiusinfotech.sonapartner.utils.Pref;

public class LogoutFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_logout, container, false);
        Pref.setPrefDate(getActivity(), AppConstants.SAVE_USER_ID, "");
        Pref.setPrefDate(getActivity(), AppConstants.SAVE_EMAIL, "");
        Pref.setPrefDate(getActivity(), AppConstants.SAVE_ROLE_ID, "");
        Pref.setPrefDate(getActivity(), AppConstants.SAVE_ROLE, "");
        getActivity().finish();
        return root;
    }
}