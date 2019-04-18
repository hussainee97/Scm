package com.condoorders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.condoorders.HomePage;
import com.condoorders.SendMailTask;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.condoorders.R;

import java.util.Arrays;
import java.util.List;

public class BecomeAPartner extends AppCompatActivity {

    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText phoneNumberEditText;
    EditText productEditText;
    EditText businessLocationEditText;
    Button sendPartnershipRequstBtn;



    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mPartnershipRequestReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_a_partner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mPartnershipRequestReference = mFirebaseDatabase.getReference().child("partnershipRequests");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        businessLocationEditText = (EditText) findViewById(R.id.businessLocationEditText);
        sendPartnershipRequstBtn = (Button) findViewById(R.id.sendPartnershipRequestBtn);
        productEditText = (EditText) findViewById(R.id.productEditText);
        productEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(BecomeAPartner.this);
              //  builderSingle.setIcon(R.drawable.ic_launcher);
                builderSingle.setTitle("What do you sell?");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(BecomeAPartner.this, android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add("Petrol");
                arrayAdapter.add("Gas");
                arrayAdapter.add("Diseal");
                arrayAdapter.add("Kerosene");

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(BecomeAPartner.this);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Your Selected Item is");
                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.dismiss();
                            }
                        });
                        builderInner.show();
                    }
                });
                builderSingle.show();
            }
        });


        sendPartnershipRequstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 *
                 *   EDIT TEXTVIEW VALIDATION
                 */

                if (TextUtils.isEmpty(firstNameEditText.getText().toString())) {

                    /** AlertDialog alertDialog = new AlertDialog.Builder(PetrolOrders.this).create();
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

                    firstNameEditText.setError("Enter First Name");
                    return;

                }

                if (TextUtils.isEmpty(lastNameEditText.getText().toString())) {

                    /** AlertDialog alertDialog = new AlertDialog.Builder(PetrolOrders.this).create();
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

                    lastNameEditText.setError("Enter Last Name");
                    return;

                }

                if (TextUtils.isEmpty(phoneNumberEditText.getText().toString())) {

                    /** AlertDialog alertDialog = new AlertDialog.Builder(PetrolOrders.this).create();
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

                    phoneNumberEditText.setError("Enter Mobile Number");
                    return;

                }

                if (TextUtils.isEmpty(businessLocationEditText.getText().toString())) {

                    /** AlertDialog alertDialog = new AlertDialog.Builder(PetrolOrders.this).create();
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

                    businessLocationEditText.setError("Enter your business location");
                    return;

                }

                /**
                 *
                 * EDIT TEXTVIEW VALIDATION
                 */


                Log.i("SendMailActivity", "Send Button Clicked.");

                String firsName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String phoneNumber = phoneNumberEditText.getText().toString();
                String product = productEditText.getText().toString();
                String businessLocation = businessLocationEditText.getText().toString();

                String fromEmail = "usniceglobal@gmail.com";
                String fromPassword = "pass422???";
                String toEmails = "youmobileorders@gmail.com";
                List<String> toEmailList = Arrays.asList(toEmails
                        .split("\\s*,\\s*"));
                Log.i("SendMailActivity", "To List: " + toEmailList);
                String emailSubject =  "NEW PARTNERSHIP REQUEST";
                String emailBody = "Hello Team,\n\n" + "We have a new Partnership request available. Find the details below.\n\n" + "FULL NAME: " + firsName
                         +  " " + lastName + "\nPHONE NUMBER: " + phoneNumber + "\nBUSINESS LOCATION: " + businessLocation + "\n\nThank you.\n\nBest regards,\nYouMobile Team.";
                new SendMailTask(BecomeAPartner.this).execute(fromEmail,
                        fromPassword, toEmailList, emailSubject, emailBody);




                com.condoorders.PartnerShipRequestFireStoreItems partnerShipRequestFireStoreItems = new com.condoorders.PartnerShipRequestFireStoreItems(firstNameEditText.getText().toString(), lastNameEditText.getText().toString(), phoneNumberEditText.getText().toString(), businessLocationEditText.getText().toString());


                mPartnershipRequestReference.push().setValue(partnerShipRequestFireStoreItems);


            }
        });


    }

    @Override
    public void onBackPressed() {

        //boolean fromNewActivity=true;

        Intent goBackToHomePage = new Intent(BecomeAPartner.this, HomePage.class);
        startActivity(goBackToHomePage);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

    }
}
