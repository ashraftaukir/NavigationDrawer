package info.androidhive.navigationdrawer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import info.androidhive.navigationdrawer.R;


public class BottomFragment extends Fragment {

    View view;
    TextView display_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.bottomfragment, container, false);
        display_text = (TextView) view.findViewById(R.id.textView);
        return view;
    }

    public void displayValue(String value) {
        display_text.setText(value);
    }
}