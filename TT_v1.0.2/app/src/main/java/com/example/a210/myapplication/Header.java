package com.example.a210.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Header.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Header#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Header extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Header() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Header.
     */
    // TODO: Rename and change types and number of parameters
    public static Header newInstance(String param1, String param2) {
        Header fragment = new Header();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ImageButton setting;
        final Spinner menuSpin;
        ArrayAdapter menuAdapter;

        View view = inflater.inflate(R.layout.fragment_header,container,false);

        setting = (ImageButton)view.findViewById(R.id.setting);
        menuSpin = (Spinner)view.findViewById(R.id.menuSpin);

        menuAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.menu,R.layout.support_simple_spinner_dropdown_item);
        menuSpin.setAdapter(menuAdapter);

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuSpin.performClick();
            }
        });

        menuSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            SharedPreferences prefs = getActivity().getSharedPreferences("USER", MODE_PRIVATE);
            SharedPreferences.Editor edit;
            Intent it = new Intent(getActivity(),LonginActivity.class);

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if(position == 1){
                    edit =  prefs.edit();
                    edit.putBoolean("auto", false);
                    edit.putString("id", "");
                    edit.putString("pass", "");
                    edit.commit();
                    startActivity(it);
                    getActivity().finish();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
