package com.loginiusinfotech.sonapartner.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.balysv.materialripple.MaterialRippleLayout;
import com.loginiusinfotech.sonapartner.R;
import com.loginiusinfotech.sonapartner.modal.banner.Image;
import com.loginiusinfotech.sonapartner.modal.product.productView.ProductView;
import com.loginiusinfotech.sonapartner.modal.product.productView.ProductViewBody;
import com.loginiusinfotech.sonapartner.modal.product.productView.ProductViewData;
import com.loginiusinfotech.sonapartner.remote.ApiClient;
import com.loginiusinfotech.sonapartner.remote.ApiInterface;
import com.loginiusinfotech.sonapartner.utils.AppConstants;
import com.loginiusinfotech.sonapartner.utils.Pref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewProductActivity extends AppCompatActivity {

    ApiInterface mApiInterface;
    ProgressDialog pd;
    private ViewPager viewPager;
    private LinearLayout layout_dots;
    private AdapterImageSlider adapterImageSlider;
    private Runnable runnable = null;
    private Handler handler = new Handler();
    ArrayList<String> data;
    TextView title, tv_product_name, tv_offer_type,tv_whatsapp,tv_telephone,tv_description,tv_quntity;
    TextView tv_hl1,tv_hl2,tv_hl3,tv_hl4,tv_hl5,tv_hl6,tv_hl7;
    CardView cv_offer_type,cv_hl1,cv_hl2,cv_hl3,cv_hl4,cv_hl5,cv_hl6,cv_hl7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        mApiInterface = ApiClient.getClient(ApiInterface.class);
        pd = new ProgressDialog(ViewProductActivity.this);
        pd.setMessage("please wait ...");
        pd.setCancelable(false);
        title = findViewById(R.id.title);
        tv_product_name = findViewById(R.id.tv_product_name);
        cv_offer_type = findViewById(R.id.cv_offer_type);
        tv_quntity = findViewById(R.id.tv_quntity);
        tv_whatsapp = findViewById(R.id.tv_whatsapp);
        tv_offer_type = findViewById(R.id.tv_offer_type);
        tv_telephone = findViewById(R.id.tv_telephone);
        tv_description = findViewById(R.id.tv_description);

        tv_hl1 = findViewById(R.id.tv_hl1);
        cv_hl1 = findViewById(R.id.cv_hl1);
        tv_hl2 = findViewById(R.id.tv_hl2);
        cv_hl2 = findViewById(R.id.cv_hl2);
        tv_hl3 = findViewById(R.id.tv_hl3);
        cv_hl3 = findViewById(R.id.cv_hl3);
        tv_hl4 = findViewById(R.id.tv_hl4);
        cv_hl4 = findViewById(R.id.cv_hl4);
        tv_hl5 = findViewById(R.id.tv_hl5);
        cv_hl5 = findViewById(R.id.cv_hl5);
        tv_hl6 = findViewById(R.id.tv_hl6);
        cv_hl6 = findViewById(R.id.cv_hl6);
        tv_hl7 = findViewById(R.id.tv_hl7);
        cv_hl7 = findViewById(R.id.cv_hl7);

        title.setText(getIntent().getExtras().getString("product_name", ""));
    }

    public void drawerBack(View view) {
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (runnable != null) {
                handler.removeCallbacks(runnable);
            }
            pd.show();
            productView(getIntent().getExtras().getString("product_id", ""));
        } catch (Exception e) {
            Log.e("App",""+e);
        }
    }



    private void productView(String product_id) {
        Call<ProductView> request = mApiInterface.productView(new ProductViewBody(product_id));
        request.enqueue(new Callback<ProductView>() {
            @Override
            public void onResponse(Call<ProductView> call, Response<ProductView> response) {
                if (response.body() != null) {
                    if (response.body().getResponsecode() == 200) {
                        data=response.body().getData().get(0).getImages();
                        tv_product_name.setText(response.body().getData().get(0).getProductName());
                        tv_quntity.setText(response.body().getData().get(0).getQuantity());
                        Log.e("App","data: "+data);
                        if (response.body().getData().get(0).getOffer().equals("yes")){
                            cv_offer_type.setVisibility(View.VISIBLE);
                        }else {
                            cv_offer_type.setVisibility(View.GONE);
                        }

                        if (response.body().getData().get(0).getHL1().equals("0")){
                            cv_hl1.setVisibility(View.GONE);
                        }else {
                            cv_hl1.setVisibility(View.VISIBLE);
                        }
                        if (response.body().getData().get(0).getHL2().equals("0")){
                            cv_hl2.setVisibility(View.GONE);
                        }else {
                            cv_hl2.setVisibility(View.VISIBLE);
                        }
                        if (response.body().getData().get(0).getHL3().equals("0")){
                            cv_hl3.setVisibility(View.GONE);
                        }else {
                            cv_hl3.setVisibility(View.VISIBLE);
                        }
                        if (response.body().getData().get(0).getHL4().equals("0")){
                            cv_hl4.setVisibility(View.GONE);
                        }else {
                            cv_hl4.setVisibility(View.VISIBLE);
                        }
                        if (response.body().getData().get(0).getHL5().equals("0")){
                            cv_hl5.setVisibility(View.GONE);
                        }else {
                            cv_hl5.setVisibility(View.VISIBLE);
                        }
                        if (response.body().getData().get(0).getHL6().equals("0")){
                            cv_hl6.setVisibility(View.GONE);
                        }else {
                            cv_hl6.setVisibility(View.VISIBLE);
                        }
                        if (response.body().getData().get(0).getHL7().equals("0")){
                            cv_hl7.setVisibility(View.GONE);
                        }else {
                            cv_hl7.setVisibility(View.VISIBLE);
                        }
                        tv_hl1.setText(response.body().getData().get(0).getHL1());
                        tv_hl2.setText(response.body().getData().get(0).getHL2());
                        tv_hl3.setText(response.body().getData().get(0).getHL3());
                        tv_hl4.setText(response.body().getData().get(0).getHL4());
                        tv_hl5.setText(response.body().getData().get(0).getHL5());
                        tv_hl6.setText(response.body().getData().get(0).getHL6());
                        tv_hl7.setText(response.body().getData().get(0).getHL7());
                        tv_offer_type.setText(response.body().getData().get(0).getOffertype());
                        tv_telephone.setText(Pref.getPrefDate(ViewProductActivity.this,AppConstants.SAVE_MOBILE_NUMBER,""));
                        tv_whatsapp.setText(Pref.getPrefDate(ViewProductActivity.this,AppConstants.SAVE_WHATSAPP_NUMBER,""));
                        tv_description.setText(response.body().getData().get(0).getDescription());
                        initSliderComponent();
                    } else {
                        Toast.makeText(ViewProductActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                pd.cancel();
            }

            @Override
            public void onFailure(Call<ProductView> call, Throwable t) {
                pd.cancel();
            }
        });
    }

    private void initSliderComponent() {

        try {
            layout_dots = (LinearLayout) findViewById(R.id.layout_dots);
            viewPager = (ViewPager) findViewById(R.id.pager);
            adapterImageSlider = new AdapterImageSlider(ViewProductActivity.this, new ArrayList<Image>());

            final List<Image> items = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                Image obj = new Image();
                obj.image = data.get(i);
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
                dots[i] = new ImageView(ViewProductActivity.this);
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
    protected void onDestroy() {
        if (runnable != null) handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    public void whatsappOpen(View view) {
        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {

            try {
                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators("91" + Pref.getPrefDate(ViewProductActivity.this,AppConstants.SAVE_WHATSAPP_NUMBER,"")) + "@s.whatsapp.net");
                startActivity(sendIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(ViewProductActivity.this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
            startActivity(goToMarket);
        }
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public void telephoneOpen(View view) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+91" + Pref.getPrefDate(ViewProductActivity.this,AppConstants.SAVE_MOBILE_NUMBER,""), null));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class AdapterImageSlider extends PagerAdapter {

        private Activity act;
        private List<Image> items;

        private OnItemClickListener onItemClickListener;

        // constructor
        private AdapterImageSlider(Activity activity, List<Image> items) {
            this.act = activity;
            this.items = items;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
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
}