package com.ahmetc.islemibul;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class SliderAdapter extends PagerAdapter {

    private String[] Baslik;
    private String[] Aciklama;
    private int[] Images = {R.drawable.tutorial_1, R.drawable.tutorial_2, R.drawable.tutorial_3, R.drawable.tutorial_4, R.drawable.tutorial_5, R.drawable.tutorial_6};
    private  Context context;

    public SliderAdapter(Context context) {
        this.context = context;
        Baslik = context.getResources().getStringArray(R.array.Basliklar);
        Aciklama = context.getResources().getStringArray(R.array.Aciklamalar);
    }
    @Override
    public int getCount() {
        return Baslik.length;
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view==o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slider,container,false);

        TextView slider_baslik = view.findViewById(R.id.slider_baslik);
        TextView slider_aciklama = view.findViewById(R.id.slider_aciklama);
        ImageView slider_resim = view.findViewById(R.id.slider_resim);

        slider_baslik.setText(Baslik[position]);
        slider_aciklama.setText(Aciklama[position]);
        slider_resim.setImageResource(Images[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
