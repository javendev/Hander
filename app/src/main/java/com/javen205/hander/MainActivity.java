package com.javen205.hander;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.javen205.entity.Dog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.id_tv)
    TextView idTv;
    @BindView(R.id.id_btn1)
    Button idBtn1;
    @BindView(R.id.id_btn2)
    Button idBtn2;
    @BindView(R.id.id_btn3)
    Button idBtn3;
    @BindView(R.id.id_btn4)
    Button idBtn4;
    @BindView(R.id.id_btn5)
    Button idBtn5;
    @BindView(R.id.id_btn6)
    Button idBtn6;
    @BindView(R.id.id_btn7)
    Button idBtn7;
    @BindView(R.id.id_btn8)
    Button idBtn8;
    @BindView(R.id.id_btn9)
    Button idBtn9;
    @BindView(R.id.id_img)
    ImageView idImg;

    private  int images[]= {R.drawable.image4,R.drawable.image6,R.drawable.image7};
    private int index=0;
    Handler handler = new Handler();

    Handler customHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            idTv.setText("msg.arg1>"+msg.arg1+ "\nmsg.arg2>" +msg.arg2 +"\nmsg.obj>"+((Dog)msg.obj).toString());
        }
    };

    Handler interceptHander = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(MainActivity.this, "callback handleMessage", Toast.LENGTH_SHORT).show();
            System.out.println("is intercept Handler>"+msg.what);
            // 设置true拦截消息
            return true;
        }
    }){
        @Override
        public void handleMessage(Message msg) {
            System.out.println("is intercept Handler");
        }
    };

    Thread myThread = new Thread(){
        @Override
        public void run() {
            index++;
            index = index%3;
            System.out.println(index);
            idImg.setImageResource(images[index]);
            handler.postDelayed(myThread,1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        new Thread() {
            @Override
            public void run() {
                idTv.setText("Javen205测试非UI线程直接更新UI");
            }
        }.start();
    }

    @OnClick({R.id.id_btn1, R.id.id_btn2, R.id.id_btn3, R.id.id_btn4, R.id.id_btn5, R.id.id_btn6, R.id.id_btn7, R.id.id_btn8, R.id.id_btn9})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn1:
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000 * 5);
                            idTv.setText("Javen205测试非UI线程更新UI会出现什么异常呢？");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                break;
            case R.id.id_btn2:
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000 * 5);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    idTv.setText("Javen205测试非UI线程更新UI会出现什么异常呢？");
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

                break;
            case R.id.id_btn3:
                handler.postDelayed(myThread,1000);
                break;
            case R.id.id_btn4:
                handler.removeCallbacks(myThread);
                break;
            case R.id.id_btn5:
                Dog dog=new Dog("萨摩耶",1);
//                Message message = new Message();
                Message message= customHander.obtainMessage();
                message.arg1 = 1;
                message.arg2 = 2;
                message.obj = dog;
//                customHander.sendMessage(message);
                message.sendToTarget();
                break;
            case R.id.id_btn6:
                interceptHander.sendEmptyMessage(1);
                break;

            case R.id.id_btn7:
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000*5);
//                    idTv.setText("Javen205测试非UI线程更新UI会出现什么异常呢？");
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    idTv.setText("Javen205测试非UI线程更新UI会出现什么异常呢？");
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                break;

            case R.id.id_btn8:
                startActivity(new Intent(this,HandlerActivity.class));
                break;
            case R.id.id_btn9:
                startActivity(new Intent(this,HandlerThreadActivity.class));
                break;
        }
    }
}
