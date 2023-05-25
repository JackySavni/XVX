package com.video.player.videoplayer.xvxvideoplayer.activities.bottom_options;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.model.VP_GameModel;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_ChromeLauncher;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VP_GameCateDetail extends AppCompatActivity {
    private ProgressDialog vp_progressDialog;
    private final ArrayList<VP_GameModel> vp_arrayList = new ArrayList<>();
    private final ArrayList<VP_GameModel> vp_arrayListFinal = new ArrayList<>();
    private final ArrayList<VP_GameModel> vp_searchArrayList = new ArrayList<>();
    private final ArrayList<VP_GameModel> vp_arrayListSec = new ArrayList<>();
    private String vp_catName;
    private RecyclerView vp_game_recyclerView;
    private FloatingActionButton vp_searchBtn;
    private Dialog vp_search_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vp_ex_activity_game_cat_detail);

        vp_catName = getIntent().getStringExtra("catName");
        TextView vp_toolText = findViewById(R.id.toolText);
        vp_toolText.setText(vp_catName);
        ImageView back_about = findViewById(R.id.back_gamecate);
        back_about.setOnClickListener(view -> VP_GameCateDetail.this.onBackPressed());

        vp_game_recyclerView = findViewById(R.id.vp_game_recyclerView);
        vp_game_recyclerView.hasFixedSize();
        vp_searchBtn = findViewById(R.id.vp_searchBtn);

        vp_searchBtn.setOnClickListener(view -> {
            vp_search_dialog = new Dialog(VP_GameCateDetail.this);
            vp_search_dialog.setContentView(R.layout.vp_search_dialog);
            vp_search_dialog.setCanceledOnTouchOutside(false);
            SearchView vp_searchView = vp_search_dialog.findViewById(R.id.vp_searchView);
            vp_searchView.setIconifiedByDefault(false);
            vp_searchView.setFocusable(true);
            vp_searchView.setIconified(false);
            vp_searchView.requestFocusFromTouch();
            EditText vp_txtSearch =
                    vp_searchView.findViewById(androidx.appcompat.R.id.search_src_text);
            vp_txtSearch.setHint("Search" + " " + vp_catName + " " + "Games");
            vp_txtSearch.setHintTextColor(Color.LTGRAY);
            vp_txtSearch.setTextColor(Color.LTGRAY);

            final RecyclerView vp_myRecycler =
                    vp_search_dialog.findViewById(R.id.vp_search_recycler);
            vp_myRecycler.hasFixedSize();
            vp_myRecycler.setLayoutManager(new LinearLayoutManager(VP_GameCateDetail.this,
                    LinearLayoutManager.VERTICAL, false));
            vp_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if (s == null || s.trim().isEmpty()) {
                        vp_myRecycler.setAdapter(new RVAdapterS(VP_GameCateDetail.this, vp_arrayListSec));
                        return false;
                    }

                    ArrayList<VP_GameModel> vp_sortedList = new ArrayList<>();

                    for (VP_GameModel value : vp_searchArrayList) {
                        if (value.getVp_gameTitle().toLowerCase().contains(s.toLowerCase())) {
                            vp_sortedList.add(value);
                        }
                    }
                    RVAdapterS rvAdapterS = new RVAdapterS(VP_GameCateDetail.this, vp_sortedList);
                    vp_myRecycler.setAdapter(rvAdapterS);
                    return false;
                }
            });
            Window vp_window = vp_search_dialog.getWindow();
            vp_window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            vp_search_dialog.show();
        });

        getData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setGameAdapter(String response) {
        vp_arrayList.clear();
        try {
            JSONObject vp_root = new JSONObject(response);
            JSONArray array = vp_root.getJSONArray("games");
            for (int i = 0; i < array.length(); i++) {
                String code, name, assets;

                JSONObject vp_object = array.getJSONObject(i);
                code = vp_object.getString("code");
                JSONObject vp_nameObject = vp_object.getJSONObject("name");
                name = vp_nameObject.getString("en");
                JSONObject vp_assetsObject = vp_object.getJSONObject("assets");
                assets = vp_assetsObject.getString("brick");

                JSONObject vp_cateObject = vp_object.getJSONObject("categories");
                JSONArray vp_catArray = vp_cateObject.getJSONArray("en");
                for (int l = 0; l < vp_catArray.length(); l++) {
                    if (vp_catName.equals(vp_catArray.getString(l))) {
                        vp_arrayListFinal.add(new VP_GameModel(code, name, assets));
                        vp_searchArrayList.add(new VP_GameModel(code, name, assets));
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        vp_game_recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(VP_GameCateDetail.this, 2,
                GridLayoutManager.VERTICAL, false);
        vp_game_recyclerView.setLayoutManager(gridLayoutManager);
        GameRVAdapterInner vp_gameRVAdapterInner = new GameRVAdapterInner(VP_GameCateDetail.this,
                vp_arrayListFinal);
        vp_game_recyclerView.setAdapter(vp_gameRVAdapterInner);
        vp_gameRVAdapterInner.notifyDataSetChanged();
        vp_progressDialog.dismiss();
        vp_searchBtn.setVisibility(View.VISIBLE);
    }


    private void getData() {
        vp_progressDialog = new ProgressDialog(VP_GameCateDetail.this);
        vp_progressDialog.setMessage("Loading...");
        vp_progressDialog.show();
        String gameUrl = "https://pub.gamezop.com/v3/games?id="
                + VP_SharePref.vp_GameZopID(VP_GameCateDetail.this);

        StringRequest vp_jsonArrayRequest =
                new StringRequest(gameUrl, this::setGameAdapter, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                        vp_progressDialog.dismiss();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(vp_jsonArrayRequest);
    }

    public static class GameRVAdapterInner extends RecyclerView.Adapter<GameRVAdapterInner.MyViewHolder> {
        Activity context;
        private final ArrayList<VP_GameModel> arrayList;

        public GameRVAdapterInner(Activity context, ArrayList<VP_GameModel> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.vp_games_raw_layout_regular,
                    parent,
                    false);
            return new ItemViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder,
                                     int position) {
            final VP_GameModel model = arrayList.get(position);
            ItemViewHolder itemViewHolder =
                    (ItemViewHolder) holder;
            itemViewHolder.vp_gameTitle.setText(model.getVp_gameTitle());
            itemViewHolder.vp_gameTitle.setSelected(true);
            itemViewHolder.setThumb(model.getVp_gameThumb());

            itemViewHolder.vp_mView.setOnClickListener(v -> {
                VP_GamingActivity.vp_ShowAppOpen = false;
                VP_ChromeLauncher.vp_launchGame(context, "noCash", model.getVp_gameCode());
            });

        }

        static class MyViewHolder extends RecyclerView.ViewHolder {
            public MyViewHolder(View itemView) {
                super(itemView);
            }
        }

        static class ItemViewHolder extends MyViewHolder {
            ImageView vp_gameThumb;
            TextView vp_gameTitle;
            View vp_mView;

            public ItemViewHolder(View itemView) {
                super(itemView);
                vp_mView = itemView;
                vp_gameThumb = vp_mView.findViewById(R.id.vp_game_thumb);
                vp_gameTitle = vp_mView.findViewById(R.id.vp_game_title);
            }

            public void setThumb(String gameThumb) {
                ImageView thumb_image = vp_mView.findViewById(R.id.vp_game_thumb);
                Picasso.get().load(gameThumb).into(thumb_image);
            }
        }
    }

    public static class RVAdapterS extends RecyclerView.Adapter<RVAdapterS.MyViewHolder> {
        private final ArrayList<VP_GameModel> vp_arrayList;
        private final Activity vp_context;

        public RVAdapterS(Activity vp_context, ArrayList<VP_GameModel> vp_arrayList) {
            this.vp_context = vp_context;
            this.vp_arrayList = vp_arrayList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(vp_context).inflate(R.layout.vp_row_sf_search, viewGroup,
                    false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            final VP_GameModel VPGameModel = vp_arrayList.get(i);
            myViewHolder.vp_title.setText(VPGameModel.getVp_gameTitle());
            myViewHolder.vp_mView.setOnClickListener(v -> {
                VP_GamingActivity.vp_ShowAppOpen = false;
                VP_ChromeLauncher.vp_launchGame(vp_context, "noCash", VPGameModel.getVp_gameCode());
            });
        }

        @Override
        public int getItemCount() {
            return vp_arrayList.size();
        }

        static class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView vp_searchImg;
            TextView vp_title;
            View vp_mView;

            private MyViewHolder(View itemView) {
                super(itemView);
                vp_mView = itemView;
                vp_searchImg = itemView.findViewById(R.id.vp_search_img);
                vp_title = itemView.findViewById(R.id.searchTitle);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        VP_GamingActivity.vp_ShowAppOpen = true;
    }
}