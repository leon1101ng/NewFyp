package net.leon.myfypproject2.Purchase_in_app_credit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import net.leon.myfypproject2.Common.Config;
import net.leon.myfypproject2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class Payment extends AppCompatActivity {
    //Paypal
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);
    private Button Test1;
    private static final int PAYPAL_REQUEST_CODE = 9999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Test1 = (Button)findViewById(R.id.Test2);

        //Init Paypal
        Intent i = new Intent(this, PayPalService.class);
        i.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(i);

        Test1.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                processPayment();

            }
        });
    }

    private void processPayment() {
        int money = 999;
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(money),
                "USD",
                "tEST",
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent i = new Intent(getApplication(), PaymentActivity.class);
        i.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        i.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(i,PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PAYPAL_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation != null){
                    try{
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        JSONObject jsonObject = new JSONObject(paymentDetails);
                        finish();
                    }catch (JSONException E){
                        E.printStackTrace();
                    }
                }
            }
        }
    }
}
