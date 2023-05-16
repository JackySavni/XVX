package com.video.player.videoplayer.xvxvideoplayer.trend;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class VP_TrendVidActivity2 extends AppCompatActivity {
    private final ArrayList<String> vp_arrayList = new ArrayList<>();
    private String para2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vp_activity_ex_trend_vid_cats);
        getData();
    }

//    private void Init() {
//        TabLayout tabLayout = findViewById(R.id.tabLayout);
//        ViewPager2 viewPager = findViewById(R.id.viewPager);
//        PagerAdapter pagerAdapter = new PagerAdapter(this);
//        viewPager.setAdapter(pagerAdapter);
//        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
//                tab.setText(arrayList.get(position))).attach();
//    }

    private void getData() {
        vp_arrayList.clear();
        new VP_Constant().vp_trendRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loadCats(snapshot.child("trendURL").getValue(String.class),
                        snapshot.child("trendKEY").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadCats(String trendURL, String trendKEY) {
        RequestQueue vp_queue = Volley.newRequestQueue(this);
        JsonObjectRequest vp_jsonObjectRequest = null;
        try {
            String vp_url = VP_VCrypt.vp_decrypt(trendURL, trendKEY, trendKEY);
            para2 = new File(vp_url).getPath().replace(new File(vp_url).getName(), "");
            vp_jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, vp_url, null, jsonObject -> {
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("Sheet1");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                                vp_arrayList.add(jsonObject1.getString("title"));
                            }
//                            Init();
                        } catch (Exception ignored) {
                        }
                    }, error -> {

                    });
        } catch (Exception ignored) {
        }
        vp_queue.add(vp_jsonObjectRequest);
    }

    private class PagerAdapter extends FragmentStateAdapter {
        public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return VP_TrendFragment.newInstance(vp_arrayList.get(position), para2);
        }

        @Override
        public int getItemCount() {
            return vp_arrayList.size();
        }
    }
}