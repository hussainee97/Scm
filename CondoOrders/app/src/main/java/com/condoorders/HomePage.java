package com.condoorders;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;


public class HomePage extends AppCompatActivity {

    private static final String TAG = "HomPage";
    // private String fullNameText = "";

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;

    private ListView mMessageListView;
    // private MessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private EditText mEmailEditText;
    private Button mSendButton;
    private EditText addressEditText;
    private Button saveButton;
    private ImageView signOutImageView;
    private  TextView exploreTextView;
    private ImageView exploreImageView;
    //   private TextView userId;
    private TextView userFullName;
    BufferedReader br;

    TextView textView;
    TextView fullNameTextView;
    private String mUsername;
    TextView userMobileNumber;
    EditText input;
    Calendar calendar;
    public static final int RC_SIGN_IN = 1;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    String userFirstName;
    double bonusBalance;
    double bonusValue = 0.00;
    TextView bonusBalanceTextView;
    //  private static final int RC_PHOTO_PICKER = 2;
    //  public static final String FRIENDLY_MSG_LENGTH_KEY = "friendly_msg_length";


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserDetailsReference;
    private FirebaseUser user;
    String userId;
    private DatabaseReference mMessagesDatabaseReference, mOtherDetailsDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    //    private DatabaseReference mFullnameReference;
    private DatabaseReference mUserIdDatabaseReference;
    private TextView mUserId;
    UserDetailsFireStoreItems detail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userMobileNumber = (TextView) findViewById(R.id.userMobileNumberTextView);
        userFullName = (TextView) findViewById(R.id.user_full_name);

        bonusBalanceTextView = (TextView) findViewById(R.id.bonusBalaceTextview);
        //   if (userFullName.getText() != "Click here to set your name") {

        // userFullName.setClickable(false);


        //  }
        //  else {


        editUserFullName();


        //   }


        mUsername = ANONYMOUS;


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserDetailsReference = mFirebaseDatabase.getReference().child("users");
        //  mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");
        // mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages").child("userId");
        //  mOtherDetailsDatabaseReference = mFirebaseDatabase.getReference().child("otherdetails");
        //  mUserIdDatabaseReference = mFirebaseDatabase.getReference().child("userId");
        //mUserId = mFirebaseDatabase.getReference().child("userId");
        mFirebaseAuth = FirebaseAuth.getInstance();
        //   mFirebaseStorage = FirebaseStorage.getInstance();
        //  mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        //  mChatPhotosStorageReference = mFirebaseStorage.getReference().child("chat_photos");


        // LayoutInflater inflater = this.getLayoutInflater();
        // inflate the View


        /**

         Dialog dialog = new Dialog(HomePage.this);

         dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
         dialog.setCancelable(true);
         dialog.setContentView(R.layout.content_order);

         dialog.show();

         */


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.callFloatBtnHomePage);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent scanQRCode = new Intent(HomePage.this, ScanBarCodeActivity.class);
                startActivity(scanQRCode);

                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                /*

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:


                                Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "08064799390"));
                                startActivity(call);
                                if (ContextCompat.checkSelfPermission(HomePage.this,
                                        Manifest.permission.CALL_PHONE)
                                        != PackageManager.PERMISSION_GRANTED) {

                                    ActivityCompat.requestPermissions(HomePage.this,
                                            new String[]{Manifest.permission.CALL_PHONE},
                                            MY_PERMISSIONS_REQUEST_CALL_PHONE);

                                    // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                                    // app-defined int constant. The callback method gets the
                                    // result of the request.
                                } else {
                                    //You already have permission
                                    try {
                                        startActivity(call);
                                    } catch(SecurityException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.cancel();
                                break;
                        }
                    }


                };




                String name = detail.getFullName();



                int spaceIndex = name.indexOf(" ");
                String[] arr = name.split(" ", 2);
                String firstName = arr[0];
                String theRest = arr[1];



                AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
                builder.setMessage("Hi " + firstName + "," + " you're about to call us").setPositiveButton("CALL NOW", dialogClickListener)
                        .setNegativeButton("CANCEL", dialogClickListener).show();


                */

            }




        });


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                 user = mFirebaseAuth.getCurrentUser();
                if (user != null) {
                    onSignedInInitialize(user.getDisplayName());

                    //////////////////////////////////////////////////////////





                    mUserDetailsReference.addValueEventListener(new ValueEventListener() {


                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            //      UserDetailsFireStoreItems detail = postSnapshot.getValue(UserDetailsFireStoreItems.class);

                            //    String name = detail.getFullName();


                            if (dataSnapshot.child(user.getUid()).child("fullName").exists() && dataSnapshot.child(user.getUid()).child("userBonus").exists()) {


                                bonusBalance = (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userBonus").getValue(Double.class));

                                bonusBalanceTextView.setText(NumberFormat.getNumberInstance(Locale.US).format(bonusBalance));


                                //    address = (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("address").getValue(String.class));
                                //  deliveryAddressEditTextViewForOrderPage.setText(address);

                                String name = (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("fullName").getValue(String.class));


                                //  userFullName.setText(name);


                                //*********************************************************************************//


                                int spaceIndex = name.indexOf(" ");
                                String[] arr = name.split(" ", 2);
                                String firstName = arr[0];
                                String theRest = arr[1];


                                calendar = Calendar.getInstance();
                                int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

                                if (timeOfDay >= 0 && timeOfDay < 12) {
                                    userFullName.setText("Good Morning, " + firstName);


                                } else if (timeOfDay >= 12 && timeOfDay < 16) {

                                    userFullName.setText("Good Afternoon, " + firstName);


                                } else if (timeOfDay >= 16 && timeOfDay < 21) {

                                    userFullName.setText("Good Evening, " + firstName);

                                } else if (timeOfDay >= 21 && timeOfDay < 24) {

                                    userFullName.setText("Good Night, " + firstName);

                                }

                            }

                            else {

                                userFullName.setText("Click here to set your name");
                                bonusBalanceTextView.setText(String.valueOf(bonusBalance));
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            System.out.println("The read failed: " + databaseError.getMessage());

                        }

                    });



                    //////////////////////////////////////////////////////////////////


                            userMobileNumber.setText(user.getPhoneNumber());
                            userMobileNumber.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast fullNameSetToast = Toast.makeText(HomePage.this, "Your Mobile number is " + user.getPhoneNumber(), Toast.LENGTH_SHORT);
                                    fullNameSetToast.setGravity(Gravity.CENTER, 0, 0);
                                    fullNameSetToast.show();


                                }
                            });


                        }




                        else {

                    onSignedOutCleanup();


                    // startActivityForResult(
                    //           AuthUI.getInstance().createSignInIntentBuilder().build(),
                    //           RC_SIGN_IN);

                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    startActivityForResult(

                    AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                          new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            //    new AuthUI.IdpConfig.FacebookBuilder().build(),
                                            //    new AuthUI.IdpConfig.TwitterBuilder().build(),
                                            //    new AuthUI.IdpConfig.GitHubBuilder().build(),
                                            //   new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.PhoneBuilder().build()
                                            //  new AuthUI.IdpConfig.AnonymousBuilder().build()
                                    ))
                                    .build(),
                            RC_SIGN_IN);


                    mFirebaseAuth.setLanguageCode("en");
// To apply the default app language instead of explicitly setting it.
// auth.useAppLanguage();

                }
            }

            ;

        };


        signOutImageView = (ImageView) findViewById(R.id.signOut);
        signOutImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                AuthUI.getInstance().signOut(HomePage.this);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.cancel();
                                break;
                        }
                    }


                };

                AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
                builder.setMessage("Are you sure you want to logout?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();


            }
        });


    }

    /*
    public void selectQuantity(View view){

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("How many liters do you want to buy?");
        final String[] types = {"1 Liter", "2 Liters", "4 Liters", "5 Liters", "6 Liters", "7 Liters", "8 Liters", "9 Liters", "10 Liters", "11 Liters", "12 Liters", "13 Liters", "14 Liters", "15 Liters", "16 Liters", "17 Liters",
                "18 Liters", "19 Liters", "20 Liters", "21 Liters", "22 Liters", "23 Liters", "24 Liters", "25 Liters", "26 Liters", "27 Liters", "28 Liters", "29 Liters", "30 Liters"};
        b.setItems(types, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                switch(which){
                    case 0:

                  //      oneLiter();
                        Toast.makeText(HomePage.this, "1 Liter selected", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                    //    twoLiters();
                        break;
                }
            }

        });

        b.show();
    }

    */


    /**
     * public void calculate(View view){
     * <p>
     * double petrolPrice = 195.00;
     * double serviceCharge = 985.00;
     * double discount = 0.00;
     * <p>
     * String getQuantity =  quantityEditText.getText().toString();
     * <p>
     * double getQuantityDouble = Double.parseDouble(getQuantity);
     * <p>
     * double cost = getQuantityDouble * petrolPrice;
     * double total = ((cost + serviceCharge) - discount);
     * <p>
     * <p>
     * costTextView.setText("" + cost);
     * serviceChargeTextView.setText("" + serviceCharge);
     * totalTextView.setText("" + total);
     * <p>
     * <p>
     * }
     */

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the phone call

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    public void goToOrderPage(View v) {


        calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 8 && timeOfDay <= 18) {

            Intent orderPage = new Intent(HomePage.this, PetrolOrders.class);
            startActivity(orderPage);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);



        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Sorry! We are currently closed. Our operating hours is from 8am - 6pm DAILY. Kindly check back later.\nThank you!")
                    .setCancelable(false)
                    .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }



    }


    public void goToGasOrders(View v) {


        calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 8 && timeOfDay <= 18) {

            Intent orderPage = new Intent(HomePage.this, GasOrder.class);
            startActivity(orderPage);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);



        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Sorry! We are currently closed. Our operating hours is from 8am - 6pm DAILY. Kindly check back later.\nThank you!")
                    .setCancelable(false)
                    .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }







    }




    public void goToPartnerPage(View v) {


        Intent partnerPage = new Intent(HomePage.this, BecomeAPartner.class);
        startActivity(partnerPage);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);




    }

    public  void testAd(View v) {


        Intent testAd = new Intent(HomePage.this, TestAdMob.class);
        startActivity(testAd);


    }

    public void goToExplorePage(View v) {

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.click_bounce);
        exploreImageView.startAnimation(animation);


        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);




    }


    public void testRatingDialog(View view) {

        Dialog ratingDialog;
        ratingDialog = new Dialog(HomePage.this);

        ratingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ratingDialog.setCancelable(true);
        ratingDialog.setContentView(R.layout.content_rating_dialog);

        ratingDialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();


            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }


        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);


        //userFullName.setText("Loading...");


        }







    @Override
    public void onPause() {
        super.onPause();

        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }

        //    detachDatabaseReadListener();
        //    mMessageAdapter.clear();

    }


    public void onSignedInInitialize(String username) {

        mUsername = username;

        //  attachDatabaseReadListener();


    }

    public void onSignedOutCleanup() {

        mUsername = ANONYMOUS;
        //     mMessageAdapter.clear();
        //     detachDatabaseReadListener();

    }


    public void editUserFullName(){

        userFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userFullName.getText().toString().equals("Click here to set your name")) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
                    builder.setTitle("This cannot be changed later");
                    builder.setCancelable(false);


// Set up the input
                    input = new EditText(HomePage.this);
                    input.setBackground(getResources().getDrawable(R.drawable.edit_text_bg));
                    input.setHint("Enter your Full Name here");
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setHeight(110);



// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    //  input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    builder.setView(input);

// Set up the buttons
                    builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            userFirstName = input.getText().toString();



                            bonusBalance = bonusValue;






                            UserDetailsFireStoreItems userDetailsFireStoreItems = new UserDetailsFireStoreItems(userFirstName, bonusBalance);


                          //  mUserDetailsReference.push().setValue(otherDetails);

                            user = mFirebaseAuth.getCurrentUser();
                            userId = user.getUid();


                            //  fullNameTextView.setText(userId);


                            /////////////////////////////////////////////////////////////////////////////////////////////

                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userDetailsFireStoreItems).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                    }
                                }
                            });



                            mUserDetailsReference.addValueEventListener(new ValueEventListener() {


                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                 //   for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                     //   UserDetailsFireStoreItems detail = postSnapshot.getValue(UserDetailsFireStoreItems.class);

                                        String name =  (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("fullName").getValue(String.class));


                                        //  userFullName.setText(name);


                                        //*********************************************************************************//



                                        int spaceIndex = name.indexOf(" ");
                                        String[] arr = name.split(" ", 2);
                                        String firstName = arr[0];
                                        String theRest = arr[1];





                                        calendar = Calendar.getInstance();
                                        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

                                        if (timeOfDay >= 0 && timeOfDay < 12) {
                                            userFullName.setText("Good Morning, " + firstName);


                                        } else if (timeOfDay >= 12 && timeOfDay < 16) {

                                            userFullName.setText("Good Afternoon, " + firstName);


                                        } else if (timeOfDay >= 16 && timeOfDay < 21) {

                                            userFullName.setText("Good Evening, " + firstName);

                                        } else if (timeOfDay >= 21 && timeOfDay < 24) {

                                            userFullName.setText("Good Night, " + firstName);

                                        }


                                        bonusBalance = (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userBonus").getValue(Double.class));

                                        bonusBalanceTextView.setText(NumberFormat.getNumberInstance(Locale.US).format(bonusBalance));




                                        //*******************************************************************************//


                                    }
                              //  }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                    // [START_EXCLUDE]
                                    System.out.println("The read failed: " + databaseError.getMessage());
                                }
                            });





                            /////////////////////////////////////////////////////////////////////////////////////////////////



                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                } else {

                    //userFullName.setClickable(true);
                    userFullName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Toast fullNameSetToast = Toast.makeText(HomePage.this, "Your name is already set. This cannot be changed", Toast.LENGTH_SHORT);
                            fullNameSetToast.setGravity(Gravity.CENTER, 0, 0);
                            fullNameSetToast.show();

                        }
                    });


                }

            }
        }); //End of userFullNameText OnClick Listener


    }



    public void deliveryAddressDialog(View view){

        Dialog partnershipDialog;
        partnershipDialog = new Dialog(HomePage.this, R.style.dialogFadeInAnimStyle);

        partnershipDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        partnershipDialog.setCancelable(true);
        partnershipDialog.setContentView(R.layout.content_delivery_address);
        partnershipDialog.show();




    }

    @Override
    public void onBackPressed() {

        //boolean fromNewActivity=true;

        this.finish();
      System.exit(0);

    }




}
