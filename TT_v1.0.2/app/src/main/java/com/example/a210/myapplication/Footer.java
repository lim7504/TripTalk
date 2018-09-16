package com.example.a210.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Footer.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Footer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Footer extends Fragment implements  View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Footer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Footer.
     */
    // TODO: Rename and change types and number of parameters
    public static Footer newInstance(String param1, String param2) {
        Footer fragment = new Footer();
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
        ImageButton home, finger, map, home_dap, finger_dap, map_dap, search, answer,blog, bigdata;
        LinearLayout lin1, lin2;

        View view = inflater.inflate(R.layout.fragment_footer,container,false);

        finger      = (ImageButton)view.findViewById(R.id.finger);
        home        = (ImageButton)view.findViewById(R.id.home);
        map        = (ImageButton)view.findViewById(R.id.giopence);
        finger_dap      = (ImageButton)view.findViewById(R.id.finger_dap);
        home_dap        = (ImageButton)view.findViewById(R.id.home_dap);
        map_dap        = (ImageButton)view.findViewById(R.id.map_dap);
        search      = (ImageButton)view.findViewById(R.id.search);
        answer      = (ImageButton)view.findViewById(R.id.answer);
        blog        = (ImageButton)view.findViewById(R.id.blog);
        bigdata     = (ImageButton)view.findViewById(R.id.bigdata) ;

        String sep = getArguments().getString("sep");

        lin1        = (LinearLayout)view.findViewById(R.id.lin1);
        lin2        = (LinearLayout)view.findViewById(R.id.lin2);

        if(sep.equals("quest")) {
            lin1.setVisibility(View.VISIBLE);
            lin2.setVisibility(View.GONE);
        } else if(sep.equals("dap")) {
            lin1.setVisibility(View.GONE);
            lin2.setVisibility(View.VISIBLE);
        }
        // Inflate the layout for this fragment

        finger.setOnClickListener(this);
        map.setOnClickListener(this);
        home.setOnClickListener(this);
        search.setOnClickListener(this);
        answer.setOnClickListener(this);
        finger_dap.setOnClickListener(this);
        home_dap.setOnClickListener(this);
        map_dap.setOnClickListener(this);
        blog.setOnClickListener(this);
        bigdata.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.finger :
                it = new Intent(getActivity(),FingerActivity.class);
                it.putExtra("sep","quest");
                startActivity(it);
                getActivity().finish();
                break;
            case R.id.giopence :
                it = new Intent(getActivity(),MapActivity.class);
                it.putExtra("sep","quest");
                startActivity(it);
                getActivity().finish();
                break;
            case R.id.home :
                it = new Intent(getActivity(),MainActivity.class);
                it.putExtra("sep","quest");
                startActivity(it);
                getActivity().finish();
                break;
            case R.id.finger_dap :
                it = new Intent(getActivity(),FingerActivity.class);
                it.putExtra("sep","dap");
                startActivity(it);
                getActivity().finish();
                break;
            case R.id.map_dap :
                it = new Intent(getActivity(),MapActivity.class);
                it.putExtra("sep","dap");
                startActivity(it);
                getActivity().finish();
                break;
            case R.id.home_dap :
                it = new Intent(getActivity(),MainActivity.class);
                it.putExtra("sep","dap");
                startActivity(it);
                getActivity().finish();
                break;
            case R.id.blog :
                it = new Intent(getActivity(),BlogActivity.class);
                it.putExtra("sep","dap");
                startActivity(it);
                getActivity().finish();
                break;
            case R.id.search :
                getActivity().findViewById(R.id.lin1).setVisibility(View.GONE);
                getActivity().findViewById(R.id.lin2).setVisibility(View.VISIBLE);
                if(getActivity().getClass().getSimpleName().trim().toString().equals("FingerActivity")) {
                    FingerActivity.itGetTextView.setText("dap");
                    Log.i("뭐야", FingerActivity.itGetTextView.getText().toString());
                } else if(getActivity().getClass().getSimpleName().trim().toString().equals("MapActivity")) {
                    MapActivity.itGetTextView.setText("dap");
                    Log.i("뭐야", FingerActivity.itGetTextView.getText().toString());
                }
                break;
            case R.id.answer :
                getActivity().findViewById(R.id.lin1).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.lin2).setVisibility(View.GONE);
                if(getActivity().getClass().getSimpleName().trim().toString().equals("FingerActivity")) {
                    FingerActivity.itGetTextView.setText("quest");
                    Log.i("뭐야", FingerActivity.itGetTextView.getText().toString());
                } else if(getActivity().getClass().getSimpleName().trim().toString().equals("MapActivity")) {
                    MapActivity.itGetTextView.setText("quest");
                    Log.i("뭐야", FingerActivity.itGetTextView.getText().toString());
                }
                break;
            case R.id.bigdata :
                it = new Intent(getActivity(),BigdataActivity.class);
                it.putExtra("sep","quest");
                startActivity(it);
                getActivity().finish();
                break;
        }
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
