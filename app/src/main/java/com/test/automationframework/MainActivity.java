package com.test.automationframework;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.test.Accessibility.utils.AccessUtils;
import com.test.automationframework.utils.Jurisdiction;
import com.test.automationframework.utils.Tool;

import static com.test.Accessibility.utils.AdbUtils.hasRootPerssion;
import static com.test.automationframework.utils.Common.ROOT_SWITCH;
import static com.test.automationframework.utils.Common.WX_FILE_Acess;
import static com.test.automationframework.utils.Common.WX_FILE_command;
import static com.test.automationframework.utils.LogUtils.Acesslog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Jurisdiction.init(this);


        this.findViewById(R.id.rimei).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessUtils.jumpToSetting(getApplicationContext());
            }
        });

        this.findViewById(R.id.startWX).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT>=24){
                    Tool.writeFileToSDCard("true",
                            WX_FILE_Acess, WX_FILE_command, false, false);
                }else {
                    Toast.makeText(MainActivity.this, "开启失败SDK版本过低", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        ROOT_SWITCH = hasRootPerssion();
        Acesslog("手机是否有ROOT权限 " + ROOT_SWITCH);
    }


}
