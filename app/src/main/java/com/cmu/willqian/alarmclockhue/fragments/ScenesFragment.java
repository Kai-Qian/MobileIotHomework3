package com.cmu.willqian.alarmclockhue.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.cmu.willqian.alarmclockhue.R;
import com.cmu.willqian.alarmclockhue.data.AccessPointListAdapter;
import com.cmu.willqian.alarmclockhue.data.LightListAdapter;
import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.List;
import java.util.Map;

public class ScenesFragment extends Fragment {

    private List<PHLight> phLights;
    private PHHueSDK phHueSDK;
    private PHBridge bridge;
    private LightListAdapter adapter;
    private OnFragmentInteractionListener mListener;
    private ImageButton btnBright;
    private ImageButton btnDim;
    private ImageButton btnNight;
    private ImageButton btnSOS;
    public static final String TAG = "HUE";
    private int flag = 1;

    public ScenesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scenes, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        phHueSDK = PHHueSDK.create();
        bridge = phHueSDK.getSelectedBridge();
        phLights = bridge.getResourceCache().getAllLights();
        adapter = new LightListAdapter(getActivity(), phLights);
        ListView lightList = (ListView) getActivity().findViewById(R.id.light_list);
        lightList.setAdapter(adapter);

        btnBright = (ImageButton) getActivity().findViewById(R.id.btnBright);
        btnDim = (ImageButton) getActivity().findViewById(R.id.btnDim);
        btnNight = (ImageButton) getActivity().findViewById(R.id.btnNight);
        btnSOS = (ImageButton) getActivity().findViewById(R.id.btnSOS);

        btnBright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLightBrightness(254);
                updateSeekBar();
            }
        });

        btnDim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLightBrightness(125);
                updateSeekBar();
            }
        });

        btnNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLightBrightness(10);
                updateSeekBar();
            }
        });

        btnSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag == 1) {
                    setLightAlert(PHLight.PHLightAlertMode.ALERT_LSELECT);
                    flag = 2;
                } else {
                    setLightAlert(PHLight.PHLightAlertMode.ALERT_NONE);
                    flag = 1;
                }
            }
        });

    }

    public void updateSeekBar() {
        Handler handler=new Handler();
        Runnable runnable=new Runnable(){
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.updateData(bridge.getResourceCache().getAllLights());
                    }
                });
            }
        };
        handler.postDelayed(runnable, 500);
    }

    public void setLightBrightness(int brightness) {
        for (PHLight light : phLights) {
            PHLightState lightState = new PHLightState();
            lightState.setBrightness(brightness);
            bridge.updateLightState(light, lightState, listener);
        }
    }

    public void setLightAlert(PHLight.PHLightAlertMode mode) {
        for (PHLight light : phLights) {
            PHLightState lightState = new PHLightState();
            lightState.setAlertMode(mode);
            bridge.updateLightState(light, lightState, listener);
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

    PHLightListener listener = new PHLightListener() {

        @Override
        public void onSuccess() {
        }

        @Override
        public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {
            Log.w(TAG, "Light has updated");
        }

        @Override
        public void onError(int arg0, String arg1) {}

        @Override
        public void onReceivingLightDetails(PHLight arg0) {}

        @Override
        public void onReceivingLights(List<PHBridgeResource> arg0) {}

        @Override
        public void onSearchComplete() {}
    };
}
