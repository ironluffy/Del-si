package com.example.user.kinveyprac;

import android.app.Activity;
import android.app.LoaderManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.google.api.client.repackaged.com.google.common.base.Throwables;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyPingCallback;
import com.kinvey.android.callback.KinveyUserManagementCallback;
import com.kinvey.android.model.User;
import com.kinvey.android.store.UserStore;
import com.kinvey.android.store.DataStore;
import com.kinvey.java.core.KinveyClientCallback;

import java.io.IOException;

/**
 * Created by user on 2017-10-14.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static  final String TAG = "LoginActivity";

    public String Bic_num="test";

    private Client Kinvey_CLIENT;
    private EditText username;
    private EditText password;

    Button Qr_button;
    Button logout_bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntent();
        setContentView(R.layout.login);
        Kinvey_CLIENT = new Client.Builder("kid_HkEnHOtp-", "c536a8bb3efa4a0d9ffbf90081da6920", this).setBaseUrl("https://baas.kinvey.com").build();

        Kinvey_CLIENT.ping(new KinveyPingCallback() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                Log.d(TAG, "Kinvey Ping Success");
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e(TAG, "Kinvey Ping Failed", throwable);
            }
        });

        if(Kinvey_CLIENT.isUserLoggedIn()) {
            Log.i(TAG, "Logged in: " + getSharedClient().getActiveUser().getUsername());
            //Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
        }

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        findViewById(R.id.LoginButton).setOnClickListener(this);
        findViewById(R.id.signupButton).setOnClickListener(this);
        logout_bt=(Button)findViewById(R.id.logoutButton);
        logout_bt.setEnabled(false);
        findViewById(R.id.emailVerificationButton).setOnClickListener(this);
        Qr_button=(Button)findViewById(R.id.Qr_scan);
        Qr_button.setEnabled(false);
//        findViewById(R.id.getuserdetailButton).setOnClickListener(this);
    }

    public Client getSharedClient() {
        return Kinvey_CLIENT;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LoginButton:
                login(username.getText().toString(), password.getText().toString());
                break;
            case R.id.signupButton:
                userSignUp(username.getText().toString(), password.getText().toString());
                break;
            case R.id.logoutButton:
                userlogout();
                break;
            case R.id.emailVerificationButton:
                emailVerification();
                break;
            case R.id.Qr_scan:
                Intent intent=null;
                intent=new Intent(LoginActivity.this, demodemo.class);
                startActivityForResult(intent, 1);
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==1) {
            if (resultCode == RESULT_OK) {
                String str=data.getStringExtra("QR_text");
                if(!str.equals("")) {
                    Bic_num=str;
                    TextView qr_txt=(TextView) findViewById(R.id.Qr_txt);
                    qr_txt.setText(Bic_num);
                }
            }
        }
    }

    private void getuserdetail() {
        UserStore.get(Kinvey_CLIENT.getActiveUser().getId(), Kinvey_CLIENT, new KinveyUserManagementCallback() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(LoginActivity.this, "Successfully accessed the user detail", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(LoginActivity.this, "Failed to access the user detail", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void emailVerification() {
        UserStore.sendEmailConfirmation(Kinvey_CLIENT, new KinveyUserManagementCallback() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(LoginActivity.this, "email confirm complete",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(LoginActivity.this, "Email confirm failed",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void userlogout() {
        Kinvey_CLIENT.getActiveUser().put("logged_in", "Out");
        Kinvey_CLIENT.getActiveUser().update(new KinveyClientCallback<User>() {
            @Override
            public void onSuccess(User u) {
                Toast.makeText(LoginActivity.this, "update complete", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Throwable e) {
                Toast.makeText(LoginActivity.this, "Logout failed", Toast.LENGTH_LONG).show();
            }
        });
        UserStore.logout(Kinvey_CLIENT, new KinveyClientCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(LoginActivity.this, "log out successed",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(LoginActivity.this, "log out failed",Toast.LENGTH_LONG).show();
            }
        });
        Qr_button.setEnabled(false);
        logout_bt.setEnabled(false);
    }


    private  void userSignUp(final String uname, final String pwd) {
        UserStore.signUp(uname, pwd, Kinvey_CLIENT, new KinveyClientCallback<User>() {
            @Override
            public void onSuccess(User user) {
                Toast.makeText(LoginActivity.this, "sign up complete",Toast.LENGTH_LONG).show();
//                Kinvey_CLIENT.getActiveUser().put("email", uname);
            }


            @Override
            public void  onFailure(Throwable throwable) {
                Toast.makeText(LoginActivity.this, "sign up failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void login(final String uname, final String pwd) {
        if (Kinvey_CLIENT.isUserLoggedIn()) {
            userlogout();
        }
        try {
            UserStore.login(uname, pwd, Kinvey_CLIENT, new KinveyClientCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    Kinvey_CLIENT.getActiveUser().put("logged_in", "In");
                    Kinvey_CLIENT.getActiveUser().update(new KinveyClientCallback<User>() {
                        @Override
                        public void onSuccess(User u) {
                            Toast.makeText(LoginActivity.this, "Login complete", Toast.LENGTH_LONG).show();
                            //Qr_button.setEnabled(true);
                        }

                        @Override
                        public void onFailure(Throwable e) {
                            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Toast.makeText(LoginActivity.this, "login failed", Toast.LENGTH_LONG).show();

                }
            });
            logout_bt.setEnabled(true);
            Qr_button.setEnabled(true);
            findViewById(R.id.logoutButton).setOnClickListener(this);
            findViewById(R.id.Qr_scan).setOnClickListener(this);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, "login exception", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onDestroy()
    {
        if(Kinvey_CLIENT.isUserLoggedIn()) {
            userlogout();
        }
        super.onDestroy();
    }
}
