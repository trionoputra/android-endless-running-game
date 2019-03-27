package com.yondev.gatot.run.android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.yondev.gatot.run.AdsController;
import com.yondev.gatot.run.GatotRun;

public class AndroidLauncher extends AndroidApplication  implements AdsController{
	private static final String BANNER_AD_UNIT_ID = "ca-app-pub-2804081175309116/7101392181";
	private static final String INTERSTITIAL_UNIT_ID = "ca-app-pub-2804081175309116/7679814987";
	
	AdView bannerAd;
	InterstitialAd interstitialAd;  
	
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		View gameView = initializeForView(new GatotRun(this), config);
		//      View gameView = initializeForView(new MyGdxGame(), config);

		Display display = getWindowManager().getDefaultDisplay();
	    DisplayMetrics outMetrics = new DisplayMetrics ();
	    display.getMetrics(outMetrics);
	    
		float density  = getResources().getDisplayMetrics().density;
		int dpHeight = outMetrics.heightPixels;
		int dpWidth  = outMetrics.widthPixels;
		    
		setupAds(dpWidth,dpHeight,density);
		
	        // Define the layout
        RelativeLayout layout = new RelativeLayout(this);
        layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dpHeight < dpWidth ? dpHeight : dpWidth ,ViewGroup.LayoutParams.WRAP_CONTENT);
      //  RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        
     //   params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      //  params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
     //   params.bottomMargin = 70;
        layout.addView(bannerAd, params);
        
        setContentView(layout);
	}
	
	public void setupAds(int dpWidth,int dpHeight,float density) {
	    bannerAd = new AdView(this);
	    bannerAd.setVisibility(View.INVISIBLE);
	    bannerAd.setBackgroundColor(0xff000000);
	    bannerAd.setAdUnitId(BANNER_AD_UNIT_ID);
	  //  AdSize d = new AdSize(AdSize.SMART_BANNER);
	    bannerAd.setAdSize(AdSize.BANNER);
	    
	    interstitialAd = new InterstitialAd(this);
	    interstitialAd.setAdUnitId(INTERSTITIAL_UNIT_ID);
	     
	    AdRequest.Builder builder = new AdRequest.Builder();
	    AdRequest ad = builder.build();
	    interstitialAd.loadAd(ad);
	    
	}

	@Override
	public void showBannerAd() {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	            bannerAd.setVisibility(View.VISIBLE);
	            AdRequest.Builder builder = new AdRequest.Builder();
	            AdRequest ad = builder.build();
	            bannerAd.loadAd(ad);
	        }
	    });
	}

	@Override
	public void hideBannerAd() {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	            bannerAd.setVisibility(View.INVISIBLE);
	        }
	    });
	}
	
	@Override
	   public void showInterstitialAd() {
	       runOnUiThread(new Runnable() {
	           @Override
	           public void run() {

	        	   interstitialAd.setAdListener(new AdListener() {
                       @Override
                       public void onAdClosed() {
                           AdRequest.Builder builder = new AdRequest.Builder();
                           AdRequest ad = builder.build();
                           interstitialAd.loadAd(ad);
                       }
                   });
	        	   
	        	   
	        	   if (interstitialAd.isLoaded()) 
	        		   interstitialAd.show();
	           }
	           
	           
	       });
	   }

}
