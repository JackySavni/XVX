package com.video.player.videoplayer.xvxvideoplayer.trend;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.trend.edmt.dev.videoplayer.VP_VideoPlayerRecyclerView;
import com.video.player.videoplayer.xvxvideoplayer.trend.edmt.dev.videoplayer.adapter.VP_VideoPlayerRecyclerAdapter;
import com.video.player.videoplayer.xvxvideoplayer.trend.edmt.dev.videoplayer.model.VP_MediaObject;
import com.video.player.videoplayer.xvxvideoplayer.trend.edmt.dev.videoplayer.utils.VP_VerticalSpacingItemDecorator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class VP_TrendFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String VP_ARG_PARAM1 = "title";
    private static final String VP_ARG_PARAM2 = "para2";

    // TODO: Rename and change types of parameters
    private String vp_mParam1 = "", vp_mParam2 = "";
    VP_VideoPlayerRecyclerView VPVideoPlayerRecyclerView;
    private ProgressDialog vp_pDialog;
    private View vp_fragmentView;

    public VP_TrendFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static VP_TrendFragment newInstance(String param1, String param2) {
        VP_TrendFragment vp_fragment = new VP_TrendFragment();
        Bundle vp_args = new Bundle();
        vp_args.putString(VP_ARG_PARAM1, param1);
        vp_args.putString(VP_ARG_PARAM2, param2);
        vp_fragment.setArguments(vp_args);
        return vp_fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            vp_mParam1 = getArguments().getString(VP_ARG_PARAM1);
            vp_mParam2 = getArguments().getString(VP_ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vp_fragmentView = inflater.inflate(R.layout.vp_fragment_ex_trend, container, false);
        init(vp_fragmentView);
        return vp_fragmentView;
    }

    private void init(View fragmentView) {
        VPVideoPlayerRecyclerView = fragmentView.findViewById(R.id.vp_video_player_id);
        LinearLayoutManager vp_layoutManager = new LinearLayoutManager(fragmentView.getContext());
        VPVideoPlayerRecyclerView.setLayoutManager(vp_layoutManager);
        VP_VerticalSpacingItemDecorator VPVerticalSpacingItemDecorator = new VP_VerticalSpacingItemDecorator(10);
        VPVideoPlayerRecyclerView.addItemDecoration(VPVerticalSpacingItemDecorator);
        vp_pDialog = new ProgressDialog(fragmentView.getContext(), R.style.MyProgressDialogStyle);
        vp_pDialog.setMessage("Loading...");
        vp_pDialog.show();
        getData(fragmentView);
    }

    private RequestManager initGlide() {
        RequestOptions vp_requestOptions = new RequestOptions()
                .placeholder(R.drawable.vp_white_ad_border)
                .error(R.drawable.vp_white_ad_border);

        return Glide.with(this).setDefaultRequestOptions(vp_requestOptions);
    }

    private void getData(View fragmentView) {
        String vp_url = vp_mParam2 + vp_mParam1 + ".json";
        RequestQueue vp_queue = Volley.newRequestQueue(fragmentView.getContext());
        JsonObjectRequest vp_jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, vp_url, null, jsonObject -> {
                    if (vp_pDialog != null) {
                        vp_pDialog.dismiss();
                    }
                    try {
                        JSONArray vp_jsonArray = jsonObject.getJSONArray("Sheet1");
                        ArrayList<VP_MediaObject> vp_arrayList = new ArrayList<>();
                        for (int i = 0; i < vp_jsonArray.length(); i++) {
                            JSONObject vp_jsonObject1 = (JSONObject) vp_jsonArray.get(i);
                            vp_arrayList.add(new VP_MediaObject(vp_jsonObject1.getString("title"),
                                    vp_jsonObject1.getString("thumbnail"),
                                    vp_jsonObject1.getString("preview"),
                                    vp_jsonObject1.getString("media_url"), false));
                        }
                        ArrayList<VP_MediaObject> vp_sourceVideos = new ArrayList<>(vp_arrayList);
                        VPVideoPlayerRecyclerView.setMediaObjects(vp_sourceVideos);

                        VP_VideoPlayerRecyclerAdapter vp_videoAdapter = new VP_VideoPlayerRecyclerAdapter(getActivity(), vp_arrayList, initGlide());
                        VPVideoPlayerRecyclerView.setAdapter(vp_videoAdapter);

                    } catch (Exception ignored) {
                    }
                }, error -> {

                });

        vp_queue.add(vp_jsonObjectRequest);
    }
}