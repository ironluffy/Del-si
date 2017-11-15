package com.example.user.kinveyprac;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.android.callback.KinveyPingCallback;
import com.kinvey.android.store.DataStore;
import com.kinvey.java.KinveyException;
import com.kinvey.java.Query;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.query.AbstractQuery;
import com.kinvey.java.store.StoreType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dongmin on 2017-11-09.
 */

public class menus extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "menus";
    private Client Kinvey_CLIENT;

    final ArrayList<List<Bicycle>> thisbic = new ArrayList<List<Bicycle>>();

    public String Bic_num = "0";
    public String Bic_passwd = "23145";
    Button Qr_button;
    Button bt_brr;
    Button bt_rtn;
    TextView qr_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.menus);

        Kinvey_CLIENT=((App)getApplication()).getSharedClient();

        Kinvey_CLIENT.ping(new KinveyPingCallback() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                Log.d(TAG, "Kinvey Ping Success3");
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e(TAG, "Kinvey Ping Failed3", throwable);
            }
        });

        Log.i(TAG, "menus\n");

        if (Kinvey_CLIENT.isUserLoggedIn()) {
            Log.i(TAG, "Logged in: ");// + log_act.getSharedClient().getActiveUser().getUsername());
            //Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
        }

        findViewById(R.id.Borrow).setOnClickListener(this);
        findViewById(R.id.Return).setOnClickListener(this);
        findViewById(R.id.Qr_scan).setOnClickListener(this);
        bt_brr=(Button) findViewById(R.id.Borrow);
        bt_rtn=(Button) findViewById(R.id.Return);
        bt_brr.setEnabled(false);
        bt_rtn.setEnabled(false);
        Qr_button = (Button) findViewById(R.id.Qr_scan);
        Qr_button.setEnabled(true);
    }


    public int returnbic(String Key) {
        if (Bic_num == "0") {
            return 0;
        }
        DataStore<Bicycle> bicStore = DataStore.collection("Bicycles", Bicycle.class, StoreType.SYNC, Kinvey_CLIENT);
        Query query = Kinvey_CLIENT.query().equals("key", Key);
        KinveyListCallback<Bicycle> kinveycallback = new KinveyListCallback<Bicycle>() {
            @Override
            public void onSuccess(List<Bicycle> bicycle) {

                thisbic.add(bicycle);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        };
        bicStore.find(query, kinveycallback);
        if (thisbic.get(0).get(0).getStatus() != Kinvey_CLIENT.getActiveUser().getId()) {
            return -1;
        }
        Bic_passwd = thisbic.get(0).get(0).getPasswd();
        qr_txt = (TextView) findViewById(R.id.Qr_txt);
        qr_txt.setText(Bic_passwd);
        return 1;
    }


    public int borrowbic(String key) {
        Log.d(TAG, key+" keykeykey\n");
        if (Bic_num == "0") {
            return 0;
        }
        DataStore<Bicycle> bicStore = DataStore.collection("delsibike", Bicycle.class, StoreType.SYNC, Kinvey_CLIENT);
        Query query = Kinvey_CLIENT.query().addSort("key", AbstractQuery.SortOrder.ASC);

        /*
        bicStore.find("5a0b19946236672a6af2c546", new KinveyClientCallback<Bicycle>() {
            @Override
            public void onSuccess(Bicycle bicycle) {
                Log.d(TAG, "a;selkjf;alkwejf;lkawe;f");
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(TAG, ";lkjwefoiewoijfwe");
            }
        });
        */

        KinveyListCallback<Bicycle> kinveycallback = new KinveyListCallback<Bicycle>() {
            @Override
            public void onSuccess(List<Bicycle> bicycle) {
                Log.d(TAG, "borrow_bic success\n");
                thisbic.add(bicycle);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(TAG, "borrow_bic failure\n");
            }
        };
        bicStore.find(query, kinveycallback);

        if(thisbic.isEmpty())
        {
            Log.d(TAG, "sibalsibal\n");
        }else{
            if(thisbic.get(0).isEmpty())
            {
                Log.d(TAG, "subalsubal\n");
            }
        }

        if (thisbic.get(0).get(0).getStatus() != "Possible") {
            return -1;
        }
        Bic_passwd = thisbic.get(0).get(0).getPasswd();
        qr_txt = (TextView) findViewById(R.id.Qr_txt);
        qr_txt.setText(Bic_passwd);
        return 1;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Qr_scan:
                Log.d(TAG, "why me?\n");
                Intent intent=new Intent(menus.this, demodemo.class);
                startActivityForResult(intent,1);
                break;
            case R.id.Borrow:
                int donewell = this.borrowbic(Bic_num);
                if (donewell == 1) {
                    thisbic.get(0).get(0).setStatus(Kinvey_CLIENT.getActiveUser().getId());
                    DataStore<Bicycle> bicStore = DataStore.collection("Bicycles", Bicycle.class, StoreType.SYNC, Kinvey_CLIENT);
                    try {
                        bicStore.save(thisbic.get(0).get(0), new KinveyClientCallback<Bicycle>() {
                            @Override
                            public void onSuccess(Bicycle bicycle) {
                                Toast.makeText(menus.this, "Borrow success", Toast.LENGTH_SHORT).show();
                                bt_brr.setEnabled(false);
                            }

                            @Override
                            public void onFailure(Throwable throwable) {

                            }
                        });
                    } catch (KinveyException ke) {
                    }
                } else if (donewell == 0) {
                    Toast.makeText(menus.this, "QR scan error please scan again", Toast.LENGTH_SHORT).show();
                } else if (donewell == -1) {
                    Toast.makeText(menus.this, "Borrow failed: this bicycle can not be borrowed", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.Return:
                int thisstat = this.borrowbic(Bic_num);
                if (thisstat == 1) {
                    thisbic.get(0).get(0).setStatus("Possible");
                    DataStore<Bicycle> bicStore = DataStore.collection("Bicycles", Bicycle.class, StoreType.SYNC, Kinvey_CLIENT);
                    try {
                        bicStore.save(thisbic.get(0).get(0), new KinveyClientCallback<Bicycle>() {
                            @Override
                            public void onSuccess(Bicycle bicycle) {
                                Toast.makeText(menus.this, "return success", Toast.LENGTH_SHORT).show();
                                bt_rtn.setEnabled(false);
                            }

                            @Override
                            public void onFailure(Throwable throwable) {

                            }
                        });
                    } catch (KinveyException ke) {
                    }
                } else if (thisstat == 0) {
                    Toast.makeText(menus.this, "QR scan error please scan again", Toast.LENGTH_SHORT).show();
                } else if (thisstat == -1) {
                    Toast.makeText(menus.this, "return failed: this bicycle can not be returned", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String str = data.getStringExtra("QR_text");
                if (!str.equals("")) {
                    Bic_num = str;
            //        qr_txt = (TextView) findViewById(R.id.Qr_txt);
            //        qr_txt.setText(Bic_num);
                }
                Bic_num="test";
                bt_brr.setEnabled(true);
                bt_rtn.setEnabled(true);
            }
        }
    }

}