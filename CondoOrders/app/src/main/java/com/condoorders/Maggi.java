package com.condoorders;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.condoorders.R;
import com.condoorders.SendMailTaskOrders;
import com.condoorders.UserDetailsFireStoreItems;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Maggi extends Activity {

    EditText quantityEditText;
    TextView costTextView;
    TextView serviceChargeTextView;
    TextView discountTextView;
    TextView totalTextView;
    TextView totalLabel;
    TextView totalDivider;
    TextView totalNairaSign;
    Button orderBtn;
    Button cancelBtn;
    Button btnRedeemBonus;
    TextView bonusBalanceTextView;
    TextView bonusInfoTextView;
    TextView discountTextViewNairaSign;
    ImageView bonusBalanceIcon;
    LinearLayout bonusBalanceLinearLayout;


    private DatabaseReference mUserDetailsReference;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseUser user;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mUserOrdersReference;
    ;


    double petrolPrice = 10.50;
    String serviceCharge;
    double discount;
    double cost;
    double total;
    Dialog dialog;
    Dialog ratingDialog;
    double bonusBalance;
    String address;
    EditText deliveryAddressEditTextViewForOrderPage;
    String userFullName;
    String userMobileNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_order);

        bonusBalanceLinearLayout = (LinearLayout) findViewById(R.id.bonusBalanceLinearLayout);
        Animation animation = AnimationUtils.loadAnimation(GasOrder.this,R.anim.click_bounce);
        bonusBalanceLinearLayout.startAnimation(animation);

        mFirebaseAuth = FirebaseAuth.getInstance();
        user = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserDetailsReference = mFirebaseDatabase.getReference().child("users");
        mUserOrdersReference = mFirebaseDatabase.getReference().child("usersOrders");


        quantityEditText = (EditText) findViewById(R.id.quantityEditText);
        costTextView = (TextView) findViewById(R.id.costTextView);
        serviceChargeTextView = (TextView) findViewById(R.id.serviceChargeTextView);
        discountTextView = (TextView) findViewById(R.id.discountTextView);
        totalTextView = (TextView) findViewById(R.id.totalTextView);
        totalLabel = (TextView) findViewById(R.id.totalLabel);
        totalDivider = (TextView) findViewById(R.id.totalDivider);
        totalNairaSign = (TextView) findViewById(R.id.totalNairaSign);
        btnRedeemBonus = (Button) findViewById(R.id.reedemBonusBtn);
        bonusBalanceTextView = (TextView) findViewById(R.id.bonusBalaceTextview);
        bonusInfoTextView = (TextView) findViewById(R.id.bonusInfoTextView);
        discountTextViewNairaSign = (TextView) findViewById(R.id.discountTextViewNairaSign);
       // bonusBalanceIcon = (ImageView) findViewById(R.id.bonusBalanceIcon);
        deliveryAddressEditTextViewForOrderPage = (EditText) findViewById(R.id.deliveryAddressEditTextForOrderPage);




        mUserDetailsReference.addValueEventListener(new ValueEventListener() {




            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                //      UserDetailsFireStoreItems detail = postSnapshot.getValue(UserDetailsFireStoreItems.class);

                //    String name = detail.getFullName();



                if (dataSnapshot.child(user.getUid()).child("fullName").exists() && dataSnapshot.child(user.getUid()).child("userBonus").exists()) {



                    bonusBalance = (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userBonus").getValue(Double.class));

                    bonusBalanceTextView.setText(NumberFormat.getNumberInstance(Locale.US).format(bonusBalance));


                    address = (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("address").getValue(String.class));
                    deliveryAddressEditTextViewForOrderPage.setText(address);

                    userFullName = (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("fullName").getValue(String.class));


                    if (bonusBalance != 0.00 && !bonusBalanceTextView.getText().toString().equals("0.00")){

                        bonusInfoTextView.setText("You will get a discount of RM" + NumberFormat.getNumberInstance(Locale.US).format(bonusBalance) + " if you reedem your bonus before Ordering.");

                    }
                    else if (bonusBalance == 0.00 && bonusBalanceTextView.getText().toString().equals("0")){

                        btnRedeemBonus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (bonusBalance == 0) {
                                    Animation animation = AnimationUtils.loadAnimation(GasOrder.this, R.anim.shake);
                                    bonusBalanceLinearLayout.startAnimation(animation);

                                    btnRedeemBonus.setBackgroundColor(getResources().getColor(R.color.divider));
                                    btnRedeemBonus.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            Toast.makeText(GasOrder.this, "Your bonus balance is empty", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            }

                        });
                        btnRedeemBonus.setBackgroundColor(getResources().getColor(R.color.divider));

                        bonusInfoTextView.setText("Your bonus balance is empty");




                    }
                    //******************************************************************************//


                }
                else {

                    bonusBalanceTextView.setText("" + bonusBalance);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());



            }



        });












        cancelBtn = (Button) findViewById(R.id.canceOrderBtn);

        /**

         final Handler handler = new Handler();
         handler.postDelayed(new Runnable() {
        @Override public void run() {

        //  cancelBtn.setText("Order Received");
        // cancelBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        Toast.makeText(GasOrder.this, "Its time", Toast.LENGTH_LONG).show();
        }
        }, 10000);

         */

        orderBtn = (Button) findViewById(R.id.btnOrder);
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {






                String getQuantity = quantityEditText.getText().toString();


                if (TextUtils.isEmpty(getQuantity)) {

                    /** AlertDialog alertDialog = new AlertDialog.Bu0ilder(GasOrder.this).create();
                     alertDialog.setTitle("Ooooops!");
                     alertDialog.setMessage("It seems you're forgetting something, Please enter Quantity");
                     alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                     new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int which) {
                     dialog.dismiss();
                     }
                     });
                     alertDialog.show();

                     */


                    Toast t = Toast.makeText(GasOrder.this, "Enter your Order quantity above", Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();

                    quantityEditText.setError("Enter Liters here.");
                    return;

                }

                else if (TextUtils.isEmpty(deliveryAddressEditTextViewForOrderPage.getText().toString())){
                    Toast t = Toast.makeText(GasOrder.this, "Enter your delivery address below", Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();

                    deliveryAddressEditTextViewForOrderPage.setError("Enter your delivery address here");
                    return;

                }


                double getQuantityDouble = Double.parseDouble(getQuantity);


                cost = getQuantityDouble * petrolPrice;

                total = (cost  - discount);
                DecimalFormat formatter = new DecimalFormat("#,###,###");
                String costFormat = formatter.format(cost);
                String totalFormat = formatter.format(total);


                costTextView.setText("" + costFormat);
                totalTextView.setText("" + totalFormat);

                totalLabel.setTextColor(getResources().getColor(R.color.greenLight));
                totalDivider.setTextColor(getResources().getColor(R.color.greenLight));
                totalNairaSign.setTextColor(getResources().getColor(R.color.greenLight));
                totalTextView.setTextColor(getResources().getColor(R.color.greenLight));
                orderBtn.setBackgroundColor(getResources().getColor(R.color.greenLight));


                orderBtn.setText("Order Now");
                orderBtn.setTextColor(getResources().getColor(R.color.white));


                if (orderBtn.getText() == "Order Now") {

                    orderBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (TextUtils.isEmpty(quantityEditText.getText().toString())) {

                                /** AlertDialog alertDialog = new AlertDialog.Builder(GasOrder.this).create();
                                 alertDialog.setTitle("Ooooops!");
                                 alertDialog.setMessage("It seems you're forgetting something, Please enter Quantity");
                                 alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                 new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int which) {
                                 dialog.dismiss();
                                 }
                                 });
                                 alertDialog.show();

                                 */

                                Toast t = Toast.makeText(GasOrder.this, "Enter your Order quantity above", Toast.LENGTH_SHORT);
                                t.setGravity(Gravity.CENTER, 0, 0);
                                t.show();

                                quantityEditText.setError("Enter Liters here.");
                                return;

                            }

                            else if (TextUtils.isEmpty(deliveryAddressEditTextViewForOrderPage.getText().toString())){
                                Toast t = Toast.makeText(GasOrder.this, "Enter your delivery address below", Toast.LENGTH_SHORT);
                                t.setGravity(Gravity.CENTER, 0, 0);
                                t.show();

                                deliveryAddressEditTextViewForOrderPage.setError("Enter your delivery address here");

                                return;

                            }


                            final MediaPlayer mp = MediaPlayer.create(GasOrder.this, R.raw.youmobiledefaultnotificationsound);
                            mp.start();





                            Log.i("SendMailActivity", "Send Button Clicked.");

                            String quantity = quantityEditText.getText().toString();
                            String cost = costTextView.getText().toString();
                            String serviceCharge = serviceChargeTextView.getText().toString();
                            String discount = discountTextView.getText().toString();
                            String total = totalTextView.getText().toString();

                            String fromEmail = "usniceglobal@gmail.com";
                            String fromPassword = "pass422???";
                            String toEmails = "youmobileorders@gmail.com";
                            List<String> toEmailList = Arrays.asList(toEmails
                                    .split("\\s*,\\s*"));
                            Log.i("SendMailActivity", "To List: " + toEmailList);
                            String emailSubject =  "NEW ORDER ALERT (PETROL)";
                            String emailBody = "Hello Team,\n\n" + "We have a new order request available. Find the details below.\n\n" + "USER PERSONAL DETAILS\n\n" + "FULL NAME: " + userFullName + "\nMOBILE NUMBER: " + user.getPhoneNumber() + "\nDELIVERY ADDRESS: " + address + "\n\nUSER ORDER DETAILS" +"\n\n" + "QUANTITY: " + quantity
                                    +  " Liters"  + "\nCOST: RM" + cost + "\nSERVICE CHARGE: " + serviceCharge + "\nDISCOUNT: - RM" + discount + "\nTOTAL (AMOUNT TO PAY): RM" + total + "\n\nThank you.\n\nBest regards,\nYouMobile Team.";
                            new SendMailTaskOrders(GasOrder.this).execute(fromEmail,
                                    fromPassword, toEmailList, emailSubject, emailBody);




                            OrderItems userOrdersItems = new OrderItems(userFullName, address, user.getPhoneNumber(), quantityEditText.getText().toString(), costTextView.getText().toString(), serviceChargeTextView.getText().toString(), discountTextView.getText().toString(), totalTextView.getText().toString());


                            mUserOrdersReference.push().setValue(userOrdersItems);


                            Toast.makeText(GasOrder.this, "Your order is in progress...", Toast.LENGTH_SHORT).show();
                            dialog = new Dialog(GasOrder.this);

                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.content_order_in_progress);

                            Button returnHomeBtn = (Button) dialog.findViewById(R.id.returnHomeBtn);
                            returnHomeBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent returnHome = new Intent(GasOrder.this, HomePage.class);
                                    startActivity(returnHome);
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                                }
                            });

                            dialog.show();


                            FirebaseUser user;
                            String userId;
                            user = mFirebaseAuth.getCurrentUser();
                            userId = user.getUid();

                            double balanceAfterBonusRedeemed = 0.00;



                            UserDetailsFireStoreItems updateBonusBalanceAfterRedeedmed = new UserDetailsFireStoreItems(userFullName, balanceAfterBonusRedeemed);

                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(updateBonusBalanceAfterRedeedmed).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                    }
                                }
                            });




                        }
                    });
                }


            }
        });


    }

    public void goToHomePage(View v) {


        Intent homePage = new Intent(GasOrder.this, HomePage.class);
      startActivity(homePage);


        dialog.dismiss();


    }

    public void redeemDiscount(View view){

        // btnRedeemBonus.setEnabled(false);
        btnRedeemBonus.setText("REDEEMED");
        btnRedeemBonus.setBackgroundColor(getResources().getColor(R.color.divider));
        Toast.makeText(GasOrder.this, "Successfully redeemed.", Toast.LENGTH_SHORT).show();


        discountTextView.setText(NumberFormat.getNumberInstance(Locale.US).format(bonusBalance));
        discountTextView.setTextColor(getResources().getColor(R.color.red));
        discountTextViewNairaSign.setText("- RM");
        discountTextViewNairaSign.setTextColor(getResources().getColor(R.color.red));

        quantityEditText.getRootView().clearFocus();

        discount = bonusBalance;
        bonusBalanceTextView.setText(NumberFormat.getNumberInstance(Locale.US).format(bonusBalance));

        if (btnRedeemBonus.getText() == "REDEEMED") {

            btnRedeemBonus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animation animation = AnimationUtils.loadAnimation(GasOrder.this,R.anim.shake);
                    discountTextView.startAnimation(animation);


                    Toast.makeText(GasOrder.this, "Your bonus is already redeemed", Toast.LENGTH_SHORT).show();
                }
            });
        }



    }


    public void onCancelOrderBtnClicked(View view) {


        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:

                        dialog.dismiss();
                        Toast toast = Toast.makeText(GasOrder.this, "Your order has been canceled", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        dialog.cancel();
                        Intent i = new Intent(GasOrder.this, HomePage.class);
                        startActivity(i);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to cancel this order now?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();


    }

    public void clearALLFields(View view){

        quantityEditText.setText("");
        costTextView.setText("0.00");
        discountTextView.setText("0.00");
        discountTextView.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
        discountTextViewNairaSign.setText("RM");
        discountTextViewNairaSign.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));

        btnRedeemBonus.setText("REDEEM BONUS");
        totalTextView.setText("0.00");
        totalTextView.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
        totalNairaSign.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
        totalDivider.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
        totalLabel.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
        orderBtn.setText("CALCULATE & REVIEW ORDER");
        orderBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        bonusBalanceTextView.setText(NumberFormat.getNumberInstance(Locale.US).format(bonusBalance));

        if (bonusBalanceTextView.getText().toString().equals("0")){

            btnRedeemBonus.setBackgroundColor(getResources().getColor(R.color.divider));
        } else {

            btnRedeemBonus.setBackgroundColor(getResources().getColor(R.color.darkGreen));
        }


    }

    @Override
    public void onBackPressed() {

        //boolean fromNewActivity=true;

        Intent goBackToHomePage = new Intent(GasOrder.this, HomePage.class);
        startActivity(goBackToHomePage);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

    }
}
