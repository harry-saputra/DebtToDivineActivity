package com.umda.debttodivine;
import com.umda.debttodivine.util.Purchase;
import com.umda.debttodivine.util.Inventory;
import com.umda.debttodivine.util.IabHelper;
import com.umda.debttodivine.util.IabResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class PurchaseActivity extends Activity  {
	private static final String TAG = "ProUnlock";
	
	private IabHelper mHelper;
	private boolean mIsUnlocked = false;
	
	private Button mBuyButton;
    private TextView mMessageText;
    private String mItemName = "Unlock Pro Version";
    private String mSku = "com.umda.debttodivine.unlockpro";
    
    private static final int DIALOG_CANNOT_CONNECT_ID = 1;
    private static final int DIALOG_BILLING_NOT_SUPPORTED_ID = 2;
    // (arbitrary) request code for the purchase flow
    private static final int RC_REQUEST = 10001;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.purchase);
		updateUi();
		
		String base64EncodedPublicKey = Settings.getKey();
		
		mHelper = new IabHelper(this, base64EncodedPublicKey);
		
		//Turn off for production
		mHelper.enableDebugLogging(false);
		
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
		   public void onIabSetupFinished(IabResult result) {
		      if (!result.isSuccess()) {
		         // Oh noes, there was a problem.
		         Log.d(TAG, "Problem setting up In-app Billing: " + result);
		         disablePurchase(DIALOG_CANNOT_CONNECT_ID);
		         complain("Failed to start up IabHelper");
		         return;
		      }else{
		    	  enablePurchase();
		      }
		      	// Hooray, IAB is fully set up!  
		      	mHelper.queryInventoryAsync(mGotInventoryListener);
		   }
		});
	}
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            // Do we have the premium upgrade?
            mIsUnlocked = inventory.hasPurchase(mSku);
            Log.d(TAG, "User is " + (mIsUnlocked ? "PREMIUM" : "NOT PREMIUM"));
            
            if (mIsUnlocked){
            	// Update the shared preferences so that we don't perform
                // a RestoreTransactions again.
                Settings.setPro(PurchaseActivity.this, true);
                mBuyButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_tick));
            	mMessageText.setText("Pro Unlocked");
            	mBuyButton.setEnabled(false);
            }else{
            	Settings.setPro(PurchaseActivity.this, false);
            	mBuyButton.setEnabled(true);
        		mBuyButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_dollar));
        		mMessageText.setText(R.string.unlock_pro);
            }
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };
 // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
            if (result.isFailure()) {
                // Oh noes!
                complain("Error purchasing: " + result);
                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(mSku)) {
                // Unlock Pro Version
                Log.d(TAG, "Unlocked Pro Version.");
                Settings.setPro(PurchaseActivity.this, true);
                mBuyButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_tick));
            	mMessageText.setText("Pro Unlocked");
            	mBuyButton.setEnabled(false);
            }
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

	private void enablePurchase(){
		mBuyButton.setEnabled(true);
		mBuyButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_dollar));
		mMessageText.setText(R.string.unlock_pro);
	}
	private void disablePurchase(int message){
		if  (message == DIALOG_CANNOT_CONNECT_ID){
			mMessageText.setText(R.string.cannot_connect);
		}else if(message == DIALOG_BILLING_NOT_SUPPORTED_ID){
			mMessageText.setText(R.string.billing_unsupported);
		}
		mBuyButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.nosign));
		mBuyButton.setEnabled(false);
	}
	/**
     * Called when this activity becomes visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
    }
    
    /**
     * Called when this activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }
    
	@Override
	public void onResume(){
		super.onResume();
		
	}
	@Override
	public void onPause(){
		super.onPause();
	}
	
	/**
     * Sets up the UI.
     */
    private void updateUi() {
        mBuyButton = (Button) findViewById(R.id.but_purchase);
        mMessageText = (TextView)findViewById(R.id.txt_purchase);
    }

	public void onPurchaseButtonClicked(View v) {
	    if (Consts.DEBUG) {
            Log.d(TAG, "buying: " + mItemName + " sku: " + mSku);
            Log.d(TAG, "Launching purchase flow for gas.");
        }
	    mHelper.launchPurchaseFlow(this, mSku, RC_REQUEST, mPurchaseFinishedListener);
	}
	
	void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }
}