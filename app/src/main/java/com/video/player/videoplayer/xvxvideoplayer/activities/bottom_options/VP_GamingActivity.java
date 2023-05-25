package com.video.player.videoplayer.xvxvideoplayer.activities.bottom_options;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.adapters.VP_GameBoxAdapterInnerFeatured;
import com.video.player.videoplayer.xvxvideoplayer.adapters.VP_GameBoxAdapterInnerRegular;
import com.video.player.videoplayer.xvxvideoplayer.model.VP_GameModel;
import com.video.player.videoplayer.xvxvideoplayer.model.VP_GamesMainModel;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_ChromeLauncher;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class VP_GamingActivity extends AppCompatActivity {
    private ImageView ex_searchBtn;
    private RecyclerView ex_game_recyclerView;
    private Dialog ex_search_dialog;
    private final ArrayList<VP_GameModel> ex_arrayList = new ArrayList<>();
    private final ArrayList<String> ex_arrayListCat = new ArrayList<>();
    private final ArrayList<VP_GamesMainModel> ex_arrayListMain = new ArrayList<>();
    private final ArrayList<VP_GameModel> ex_searchArrayList = new ArrayList<>();
    private final ArrayList<VP_GameModel> ex_arrayListSec = new ArrayList<>();
    private ProgressDialog ex_gameProgressDialog;
    public static boolean vp_ShowAppOpen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vp_ex_activity_gaming);

        ImageView back_gaming = findViewById(R.id.vp_back_gaming);
        back_gaming.setOnClickListener(view -> VP_GamingActivity.this.onBackPressed());

        ex_game_recyclerView = findViewById(R.id.vp_game_recyclerView);
        ex_game_recyclerView.hasFixedSize();
        ex_searchBtn = findViewById(R.id.vp_searchBtn);
        ex_searchBtn.setOnClickListener(view -> {
            ex_search_dialog = new Dialog(VP_GamingActivity.this);
            ex_search_dialog.setContentView(R.layout.vp_search_dialog);
            ex_search_dialog.setCanceledOnTouchOutside(false);
            SearchView searchView = ex_search_dialog.findViewById(R.id.vp_searchView);
            searchView.setIconifiedByDefault(false);
            searchView.setFocusable(true);
            searchView.setIconified(false);
            searchView.requestFocusFromTouch();
            EditText txtSearch =
                    ((EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text));
            txtSearch.setHint("Search Games");
            txtSearch.setHintTextColor(Color.LTGRAY);
            txtSearch.setTextColor(Color.LTGRAY);

            final RecyclerView myRecycler =
                    ex_search_dialog.findViewById(R.id.vp_search_recycler);
            myRecycler.hasFixedSize();
            myRecycler.setLayoutManager(new LinearLayoutManager(VP_GamingActivity.this,
                    LinearLayoutManager.VERTICAL, false));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if (s == null || s.trim().isEmpty()) {
                        myRecycler.setAdapter(new RVAdapterS(VP_GamingActivity.this, ex_arrayListSec));
                        return false;
                    }

                    ArrayList<VP_GameModel> sortedList = new ArrayList<>();

                    for (VP_GameModel value : ex_searchArrayList) {
                        if (value.getVp_gameTitle().toLowerCase().contains(s.toLowerCase())) {
                            sortedList.add(value);
                        }
                    }
                    RVAdapterS rvAdapterS = new RVAdapterS(VP_GamingActivity.this, sortedList);
                    myRecycler.setAdapter(rvAdapterS);
                    return false;
                }
            });
            Window window = ex_search_dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ex_search_dialog.show();
        });
        getData();
    }

    private void getData() {
        ex_gameProgressDialog = new ProgressDialog(VP_GamingActivity.this);
        ex_gameProgressDialog.setMessage("Loading...");
        ex_gameProgressDialog.show();
        String gameUrl = "https://pub.gamezop.com/v3/games?id=" + VP_SharePref.vp_GameZopID(VP_GamingActivity.this);
        StringRequest jsonArrayRequest =
                new StringRequest(gameUrl, this::setGameAdapter, error -> {
                    Log.e("Volley", error.toString());
                    ex_gameProgressDialog.dismiss();
                });
        RequestQueue requestQueue = Volley.newRequestQueue(VP_GamingActivity.this);
        requestQueue.add(jsonArrayRequest);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setGameAdapter(String response) {
        ex_arrayList.clear();
        ex_arrayListCat.clear();
        ex_arrayListMain.clear();
        ex_searchArrayList.clear();
        try {
            JSONObject root = new JSONObject(response);
            JSONArray array = root.getJSONArray("games");
            ex_arrayListCat.add("Featured");
            for (int i = 0; i < array.length(); i++) {
                String code, name, assets;

                JSONObject object = array.getJSONObject(i);
                code = object.getString("code");
                JSONObject nameObject = object.getJSONObject("name");
                name = nameObject.getString("en");
                JSONObject assetsObject = object.getJSONObject("assets");
                assets = assetsObject.getString("brick");

                JSONObject cateObject = object.getJSONObject("categories");
                JSONArray catArray = cateObject.getJSONArray("en");
                final ArrayList<String> cat = new ArrayList<>();
                for (int l = 0; l < catArray.length(); l++) {
                    cat.add(catArray.getString(l));
                    if (ex_arrayListCat.contains(catArray.getString(l)))
                        continue;

                    ex_arrayListCat.add(catArray.getString(l));
                }

                ex_arrayList.add(new VP_GameModel(code, name, assets, cat));
                ex_searchArrayList.add(new VP_GameModel(code, name, assets, cat));
            }

            for (int i = 0; i < ex_arrayListCat.size(); i++) {
                final ArrayList<VP_GameModel> tempArr = new ArrayList<>();
                for (int z = 0; z < ex_arrayList.size(); z++) {
                    VP_GameModel VPGameModel = ex_arrayList.get(z);
                    ArrayList<String> arr = VPGameModel.getVp_gameCat();
                    for (int x = 0; x < arr.size(); x++) {
                        if (arr.get(x).equals(ex_arrayListCat.get(i))) {
                            tempArr.add(VPGameModel);
                        }
                    }
                }
                ex_arrayListMain.add(new VP_GamesMainModel(ex_arrayListCat.get(i), tempArr));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ex_game_recyclerView.setHasFixedSize(true);
        ex_game_recyclerView.setLayoutManager(new LinearLayoutManager(VP_GamingActivity.this,
                LinearLayoutManager.VERTICAL, false));
        RVAdapterMain rvAdapterMain = new RVAdapterMain(VP_GamingActivity.this, ex_arrayListMain);
        ex_game_recyclerView.setAdapter(rvAdapterMain);
        rvAdapterMain.notifyDataSetChanged();
        ex_gameProgressDialog.dismiss();
        ex_searchBtn.setVisibility(View.VISIBLE);
    }

    public static class RVAdapterS extends RecyclerView.Adapter<RVAdapterS.MyViewHolder> {
        private final ArrayList<VP_GameModel> arrayList;
        private final Activity context;

        public RVAdapterS(Activity context, ArrayList<VP_GameModel> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.vp_row_sf_search, viewGroup,
                    false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            final VP_GameModel VPGameModel = arrayList.get(i);
            myViewHolder.title.setText(VPGameModel.getVp_gameTitle());
            myViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vp_ShowAppOpen = false;
                    VP_ChromeLauncher.vp_launchGame(context, "noCash", VPGameModel.getVp_gameCode());
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        static class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView searchImg;
            TextView title;
            View mView;

            private MyViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                searchImg = itemView.findViewById(R.id.vp_search_img);
                title = itemView.findViewById(R.id.searchTitle);
            }
        }
    }

    public class RVAdapterMain extends RecyclerView.Adapter<RVAdapterMain.MyViewHolder> {
        private final ArrayList<VP_GamesMainModel> arrayList;
        private final Activity context;
        private final int FETURED = 0;
        private final int REGULAR = 1;

        public RVAdapterMain(Activity context, ArrayList<VP_GamesMainModel> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return FETURED;
            } else {
                return REGULAR;
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,
                                               int viewType) {
            View view;
            if (viewType == FETURED) {
                view = LayoutInflater.from(context).inflate(R.layout.vp_games_raw_main_featured,
                        viewGroup,
                        false);
                return new MyViewHolder(view);
            } else if (viewType == REGULAR) {
                view = LayoutInflater.from(context).inflate(R.layout.vp_games_raw_main_regular,
                        viewGroup,
                        false);
                return new MyViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            if (myViewHolder.getItemViewType() == FETURED) {
                final VP_GamesMainModel VPGamesMainModel = arrayList.get(i);
                myViewHolder.gameCatName.setText(VPGamesMainModel.getVp_catName());
                ArrayList<VP_GameModel> arrayList1 = VPGamesMainModel.getVp_catArraylist();
                if (arrayList1.isEmpty()) {
                    myViewHolder.gameCatRecyclerView.setVisibility(View.GONE);
                    myViewHolder.gameCatName.setGravity(Gravity.CENTER);
                    myViewHolder.gameCatName.setText(R.string.game_status);
                }
                Collections.shuffle(arrayList1);
                VP_GameBoxAdapterInnerFeatured VPGameBoxAdapterInnerFeatured =
                        new VP_GameBoxAdapterInnerFeatured(context, arrayList1);
                myViewHolder.gameCatRecyclerView.setHasFixedSize(true);
                myViewHolder.gameCatRecyclerView.setLayoutManager(new LinearLayoutManager(context,
                        LinearLayoutManager.HORIZONTAL, false));
                myViewHolder.gameCatRecyclerView.setAdapter(VPGameBoxAdapterInnerFeatured);
                myViewHolder.gameCatRecyclerView.setNestedScrollingEnabled(false);

            } else {
                final VP_GamesMainModel VPGamesMainModel = arrayList.get(i);
                myViewHolder.gameCatName.setText(VPGamesMainModel.getVp_catName());
                ArrayList<VP_GameModel> arrayList1 = VPGamesMainModel.getVp_catArraylist();
                Collections.shuffle(arrayList1);
                VP_GameBoxAdapterInnerRegular VPGameBoxAdapterInnerRegular =
                        new VP_GameBoxAdapterInnerRegular(context,
                                arrayList1);
                myViewHolder.gameCatRecyclerView.setHasFixedSize(true);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(VP_GamingActivity.this, 2,
                        GridLayoutManager.VERTICAL, false);
                myViewHolder.gameCatRecyclerView.setLayoutManager(gridLayoutManager);
                myViewHolder.gameCatRecyclerView.setAdapter(VPGameBoxAdapterInnerRegular);
                myViewHolder.gameCatRecyclerView.setNestedScrollingEnabled(false);

                myViewHolder.gameSeeAllBtn.setOnClickListener(v -> {
                    Intent intent = new Intent(context, VP_GameCateDetail.class);
                    intent.putExtra("catName", VPGamesMainModel.getVp_catName());
                    startActivity(intent);
                });
            }
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private final TextView gameCatName;
            private final TextView gameSeeAllBtn;
            private final RecyclerView gameCatRecyclerView;

            private MyViewHolder(View itemView) {
                super(itemView);
                gameCatName = itemView.findViewById(R.id.vp_game_cat_name);
                gameSeeAllBtn = itemView.findViewById(R.id.vp_game_see_all_btn);
                gameCatRecyclerView = itemView.findViewById(R.id.vp_game_cat_recyclerView);
            }
        }
    }

    protected void onResume() {
        super.onResume();
        VP_GamingActivity.vp_ShowAppOpen = true;
    }
}