package com.loginiusinfotech.sonapartner.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.balysv.materialripple.MaterialRippleLayout;
import com.loginiusinfotech.sonapartner.R;
import com.loginiusinfotech.sonapartner.activity.ViewProductActivity;
import com.loginiusinfotech.sonapartner.modal.banner.Image;
import com.loginiusinfotech.sonapartner.modal.banner.OfferBanner;
import com.loginiusinfotech.sonapartner.modal.banner.OfferBannerData;
import com.loginiusinfotech.sonapartner.modal.category.categoryList.CategoryList;
import com.loginiusinfotech.sonapartner.modal.product.productList.ProductList;
import com.loginiusinfotech.sonapartner.modal.product.productList.ProductListBody;
import com.loginiusinfotech.sonapartner.modal.product.productList.ProductListData;
import com.loginiusinfotech.sonapartner.modal.setting.numberlist.NumberList;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryList.SubCategoryList;
import com.loginiusinfotech.sonapartner.modal.subcategory.subCategoryList.SubCategoryListBody;
import com.loginiusinfotech.sonapartner.remote.ApiClient;
import com.loginiusinfotech.sonapartner.remote.ApiInterface;
import com.loginiusinfotech.sonapartner.utils.AppConstants;
import com.loginiusinfotech.sonapartner.utils.Pref;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    View root;
    Recycle_Adpter recycle_adpter;
    RecyclerView recyclerView;
    ApiInterface mApiInterface;
    ProgressDialog pd;
    SearchView searchView;
    Spinner spinner_cat, spinner_sub_cat;
    String str_cat_id_spinner="", str_sub_cat_id_spinner="";

    private ViewPager viewPager;
    private LinearLayout layout_dots;
    private AdapterImageSlider adapterImageSlider;
    private Runnable runnable = null;
    private Handler handler = new Handler();
    List<OfferBannerData> data;
    CardView cv_slider;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        searchView = root.findViewById(R.id.searchView);
        recyclerView.setNestedScrollingEnabled(false);
        spinner_cat = root.findViewById(R.id.spinner_cat);
        spinner_sub_cat = root.findViewById(R.id.spinner_sub_cat);

        cv_slider = root.findViewById(R.id.cv_slider);
        mApiInterface = ApiClient.getClient(ApiInterface.class);
        pd = new ProgressDialog(getActivity());
        pd.setMessage("please wait ...");
        pd.setCancelable(false);
        categoryList();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (!Pref.getPrefDate(getActivity(), AppConstants.SAVE_ROLE_ID, "").equals("2")) {
                bannerList();
            }
            if (runnable != null) {
                handler.removeCallbacks(runnable);
            }
            if (Pref.getPrefDate(getActivity(),AppConstants.SAVE_WHATSAPP_NUMBER,"").equals("")){
                numbersList();
            }else if (Pref.getPrefDate(getActivity(),AppConstants.SAVE_MOBILE_NUMBER,"").equals("")){
                numbersList();
            }
            pd.show();
            spinner_cat.getItemAtPosition(0);
            spinner_sub_cat.getItemAtPosition(0);
//            categoryList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void categoryList() {
        Call<CategoryList> request = mApiInterface.categoryList();
        request.enqueue(new Callback<CategoryList>() {
            @Override
            public void onResponse(Call<CategoryList> call, Response<CategoryList> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        ArrayList<String> categorArrayList = new ArrayList<>();
                        categorArrayList.add("All");
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            categorArrayList.add(response.body().getData().get(i).getCategory());
                        }
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(),
                                R.layout.item_spinner_list_category,
                                R.id.title, categorArrayList);
                        spinner_cat.setAdapter(adapter);
                        spinner_cat.getItemAtPosition(0);
                        spinner_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                if (position == 0) {
                                    str_cat_id_spinner = "";
                                    str_sub_cat_id_spinner = "";
                                    ArrayList<String> arrayList = new ArrayList<>();
                                    arrayList.add("All");
                                    spinner_sub_cat.setAdapter( new ArrayAdapter(getActivity(),
                                            R.layout.item_spinner_list_category,
                                            R.id.title, arrayList));
                                    productList();
                                    spinner_sub_cat.setSelection(0);
                                } else {
                                    int tempPosition = -1 + position;
                                    str_cat_id_spinner = response.body().getData().get(tempPosition).getId();
//                                    Pref.setPrefDate(getActivity(), AppConstants.SAVE_CAT_POSITION, "" + adapterView.getItemAtPosition(tempPosition).toString());
                                    subCategoryList(str_cat_id_spinner);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        try {
//                            if (!Pref.getPrefDate(getActivity(), AppConstants.SAVE_CAT_POSITION, "").equals("")) {
//                                ArrayAdapter<String> array_spinner = (ArrayAdapter<String>) spinner_cat.getAdapter();
//                                spinner_cat.setSelection(array_spinner.getPosition(Pref.getPrefDate(getActivity(), AppConstants.SAVE_CAT_POSITION, "")));
//                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                pd.cancel();
            }

            @Override
            public void onFailure(Call<CategoryList> call, Throwable t) {
                pd.cancel();
            }
        });
    }

    private void bannerList() {
        Call<OfferBanner> request = mApiInterface.bannerList();
        request.enqueue(new Callback<OfferBanner>() {
            @Override
            public void onResponse(Call<OfferBanner> call, Response<OfferBanner> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        data = response.body().getData();
                        initSliderComponent();
                        cv_slider.setVisibility(View.VISIBLE);
                    } else {
                        cv_slider.setVisibility(View.GONE);
//                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                pd.cancel();
            }

            @Override
            public void onFailure(Call<OfferBanner> call, Throwable t) {
                pd.cancel();
            }
        });
    }


    private void productList() {
        pd.show();
        Call<ProductList> request = mApiInterface.homeScreen(new ProductListBody(str_cat_id_spinner, str_sub_cat_id_spinner));
        request.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                        recycle_adpter = new Recycle_Adpter(response.body().getData(), getActivity());
                        recyclerView.setAdapter(recycle_adpter);
//
                        searchView.setQueryHint(Html.fromHtml("<font color = #5D5F96> Search Product</font>"));
                        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
                        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
                        searchView.setMaxWidth(Integer.MAX_VALUE);
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                // filter recycler view when query submitted
                                recycle_adpter.getFilter().filter(query);
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String query) {
                                recycle_adpter.getFilter().filter(query);
                                return false;
                            }
                        });
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
                pd.dismiss();
            }
        });
    }

    private void productList(String cat_id,String sub_cat_id) {
        pd.show();
        Call<ProductList> request = mApiInterface.productList(new ProductListBody(cat_id,sub_cat_id));
        request.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                        recycle_adpter = new Recycle_Adpter(response.body().getData(), getActivity());
                        recyclerView.setAdapter(recycle_adpter);
//
                        searchView.setQueryHint(Html.fromHtml("<font color = #5D5F96> Search Product</font>"));
                        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
                        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
                        searchView.setMaxWidth(Integer.MAX_VALUE);
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                // filter recycler view when query submitted
                                recycle_adpter.getFilter().filter(query);
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String query) {
                                recycle_adpter.getFilter().filter(query);
                                return false;
                            }
                        });
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
                pd.dismiss();
            }
        });
    }


    public class Recycle_Adpter extends RecyclerView.Adapter {

        List<ProductListData> spacecrafts;
        List<ProductListData> spacecraftsFilter;
        Context mContext;

        public Recycle_Adpter(List<ProductListData> data, Context mContext) {
            this.spacecrafts = data;
            this.spacecraftsFilter = data;
            this.mContext = mContext;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int i) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_list_nav_home_fragment, parent, false);
            // set the view's size, margins, paddings and layout parameters
            Recycle_Adpter.MyViewHolder vh = new Recycle_Adpter.MyViewHolder(v); // pass the view to View Holder
            return vh;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@androidx.annotation.NonNull final RecyclerView.ViewHolder holder, final int position) {
            final Recycle_Adpter.MyViewHolder holder1 = (Recycle_Adpter.MyViewHolder) holder;
            final ProductListData productListData = (ProductListData) spacecraftsFilter.get(position);
            Picasso.get().load("" + productListData.getImages().get(0)).error(R.mipmap.ic_launcher_foreground).into(holder1.image);
            holder1.title.setText(productListData.getProductName());
            if (productListData.getOffer().equals("yes")){
                holder1.iv_offer.setVisibility(View.VISIBLE);
            }else {
                holder1.iv_offer.setVisibility(View.GONE);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder1.btn_design.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ViewProductActivity.class);
                    intent.putExtra("product_id", "" + productListData.getId());
                    intent.putExtra("product_name", "" + productListData.getCategory());
                    mContext.startActivity(intent);
                }
            });
        }


        @Override
        public int getItemCount() {
            return spacecraftsFilter.size();
        }

        @Override
        public long getItemId(int position) {
            return getItemCount();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView image,iv_offer;
            Button btn_design;
            TextView title;

            public MyViewHolder(@androidx.annotation.NonNull View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.images);
                iv_offer = itemView.findViewById(R.id.iv_offer);
                title = itemView.findViewById(R.id.title);
                btn_design = itemView.findViewById(R.id.btn_design);
            }
        }

        public Filter getFilter() {
            return new Filter() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    try {
                        if (charSequence.toString().isEmpty()) {
                            spacecraftsFilter = spacecrafts;
                        } else {
                            List<ProductListData> filteredList = new ArrayList<>();
                            for (ProductListData row : spacecrafts) {
                                if (row.getProductName().trim().toLowerCase().contains(charSequence.toString().trim().toLowerCase())) {
                                    filteredList.add(row);
                                }
                            }
                            spacecraftsFilter = filteredList;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = spacecraftsFilter;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    spacecraftsFilter = (ArrayList<ProductListData>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }
    }

    private void initSliderComponent() {

        try {
            layout_dots = (LinearLayout) root.findViewById(R.id.layout_dots);
            viewPager = (ViewPager) root.findViewById(R.id.pager);
            adapterImageSlider = new AdapterImageSlider(getActivity(), new ArrayList<Image>());
            adapterImageSlider.setOnItemClickListener(new AdapterImageSlider.OnItemClickListener() {
                @Override
                public void onItemClick(View view, Image obj) {
                    Intent intent = new Intent(getActivity(), ViewProductActivity.class);
                    intent.putExtra("product_id", "" + obj.id);
                    intent.putExtra("product_name", "" + "View Offer");
                    startActivity(intent);
                }
            });
            final List<Image> items = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                Image obj = new Image();
                obj.image = data.get(i).getBannerImages().get(0);
                obj.id = data.get(i).getId();
                items.add(obj);
            }

            adapterImageSlider.setItems(items);
            viewPager.setAdapter(adapterImageSlider);

            // displaying selected image first
            viewPager.setCurrentItem(0);
            addBottomDots(layout_dots, adapterImageSlider.getCount(), 0);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int pos) {
                    addBottomDots(layout_dots, adapterImageSlider.getCount(), pos);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

            startAutoSlider(adapterImageSlider.getCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        try {
            ImageView[] dots = new ImageView[size];

            layout_dots.removeAllViews();
            for (int i = 0; i < dots.length; i++) {
                dots[i] = new ImageView(getActivity());
                int width_height = 15;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
                params.setMargins(10, 10, 10, 10);
                dots[i].setLayoutParams(params);
                dots[i].setImageResource(R.drawable.shape_circle_outline);
                layout_dots.addView(dots[i]);
            }

            if (dots.length > 0) {
                dots[current].setImageResource(R.drawable.shape_circle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startAutoSlider(final int count) {
        runnable = new Runnable() {
            @Override
            public void run() {
                int pos = viewPager.getCurrentItem();
                pos = pos + 1;
                if (pos >= count) pos = 0;
                viewPager.setCurrentItem(pos);
                handler.postDelayed(runnable, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
    }

    @Override
    public void onDestroy() {
        if (runnable != null) handler.removeCallbacks(runnable);
        super.onDestroy();

    }

    private static class AdapterImageSlider extends PagerAdapter {

        private Activity act;
        private List<Image> items;

        private AdapterImageSlider.OnItemClickListener onItemClickListener;

        // constructor
        private AdapterImageSlider(Activity activity, List<Image> items) {
            this.act = activity;
            this.items = items;
        }

        public void setOnItemClickListener(AdapterImageSlider.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public int getCount() {
            return this.items.size();
        }

        public Image getItem(int pos) {
            return items.get(pos);
        }

        public void setItems(List<Image> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final Image o = items.get(position);
            LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.item_slider_image, container, false);

            ImageView image = (ImageView) v.findViewById(R.id.images);
            MaterialRippleLayout lyt_parent = (MaterialRippleLayout) v.findViewById(R.id.lyt_parent);
            AppConstants.displayImageOriginal(act, image, o.image);
            lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, o);
                    }
                }
            });
            ((ViewPager) container).addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((RelativeLayout) object);

        }

        private interface OnItemClickListener {
            void onItemClick(View view, Image obj);
        }
    }

    private void subCategoryList(String cat_id) {
        pd.show();
        Call<SubCategoryList> request = mApiInterface.subCategoryList(new SubCategoryListBody(cat_id));
        request.enqueue(new Callback<SubCategoryList>() {
            @Override
            public void onResponse(Call<SubCategoryList> call, Response<SubCategoryList> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        ArrayList<String> categorArrayList = new ArrayList<>();
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            categorArrayList.add(response.body().getData().get(i).getSubcategory_name());
                        }
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(),
                                R.layout.item_spinner_list_category,
                                R.id.title, categorArrayList);
                        spinner_sub_cat.setAdapter(adapter);
                        spinner_sub_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                str_sub_cat_id_spinner = response.body().getData().get(position).getId();
                                //Pref.setPrefDate(getActivity(), AppConstants.SAVE_SUB_CAT_POSITION, "" + adapterView.getItemAtPosition(position).toString());
                                if (!str_cat_id_spinner.equals("") && !str_sub_cat_id_spinner.equals("")){
                                    productList(cat_id,str_sub_cat_id_spinner);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        try {
//                            if (!Pref.getPrefDate(getActivity(), AppConstants.SAVE_SUB_CAT_POSITION, "").equals("")) {
//                                ArrayAdapter<String> array_spinner = (ArrayAdapter<String>) spinner_sub_cat.getAdapter();
//                                spinner_sub_cat.setSelection(array_spinner.getPosition(Pref.getPrefDate(getActivity(), AppConstants.SAVE_SUB_CAT_POSITION, "")));
//                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<SubCategoryList> call, Throwable t) {
                pd.dismiss();
            }
        });
    }

    private void numbersList() {
        pd.show();
        Call<NumberList> request = mApiInterface.numbersList();
        request.enqueue(new Callback<NumberList>() {
            @Override
            public void onResponse(Call<NumberList> call, Response<NumberList> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        Pref.setPrefDate(getActivity(),AppConstants.SAVE_MOBILE_NUMBER,""+response.body().getData().get(0).getValue());
                        Pref.setPrefDate(getActivity(),AppConstants.SAVE_WHATSAPP_NUMBER,""+response.body().getData().get(1).getValue());
                    } else {
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<NumberList> call, Throwable t) {
                pd.dismiss();
            }
        });
    }
}