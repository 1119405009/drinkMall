package com.shopmodule.shoppingcart.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.shopmodule.R;
import com.shopmodule.app.MainActivity;
import com.shopmodule.base.BaseFragment;
import com.shopmodule.home.bean.GoodsBean;
import com.shopmodule.shoppingcart.adapter.ShopCartAdapter;

import com.shopmodule.shoppingcart.pay.PayResult;
import com.shopmodule.shoppingcart.pay.SignUtils;
import com.shopmodule.shoppingcart.utils.CartProvider;
import com.shopmodule.shoppingcart.utils.PayKeys;
import com.shopmodule.utils.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class ShoppingCartFragment extends BaseFragment {

    private TextView tvShopcartEdit;
    private RecyclerView recyclerview;
    private LinearLayout llCheckAll;
    private CheckBox checkboxAll;
    private TextView tvShopcartTotal;
    private Button btnCheckOut;
    private LinearLayout llDelete;
    private CheckBox cbAll;
    private Button btnDelete;
    private Button btnCollection;
    private ImageView ivEmpty;
    private TextView tvEmptyCartTobuy;
    private LinearLayout ll_empty_shopcart;
    /**
     * 编辑状态
     */
    private static final int ACTION_EDIT = 0;
    /**
     * 完成状态
     */
    private static final int ACTION_COMPLETE = 1;

    private ShopCartAdapter adapter;


    private void findViews(View view) {
        tvShopcartEdit = (TextView) view.findViewById(R.id.tv_shopcart_edit);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        llCheckAll = (LinearLayout) view.findViewById(R.id.ll_check_all);
        checkboxAll = (CheckBox) view.findViewById(R.id.checkbox_all);
        tvShopcartTotal = (TextView) view.findViewById(R.id.tv_shopcart_total);
        btnCheckOut = (Button) view.findViewById(R.id.btn_check_out);
        llDelete = (LinearLayout) view.findViewById(R.id.ll_delete);
        cbAll = (CheckBox) view.findViewById(R.id.cb_all);
        btnDelete = (Button) view.findViewById(R.id.btn_delete);
        btnCollection = (Button) view.findViewById(R.id.btn_collection);
        ivEmpty = (ImageView) view.findViewById(R.id.iv_empty);
        tvEmptyCartTobuy = (TextView) view.findViewById(R.id.tv_empty_cart_tobuy);
        ll_empty_shopcart = (LinearLayout) view.findViewById(R.id.ll_empty_shopcart);
        tvEmptyCartTobuy.setClickable(true);
    }


    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_shoppingcart, null);
        findViews(view);
        return view;
    }

    @Override
    public void initData() {
        initListener();
        tvShopcartEdit.setTag(ACTION_EDIT);
        tvShopcartEdit.setText("编辑");
        llCheckAll.setVisibility(View.VISIBLE);
        showData();

    }

    @Override
    public void onResume() {
        super.onResume();
        showData();
    }

    private void initListener() {
        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay(v);
            }
        });
        tvShopcartEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置编辑的点击事件
                int tag = (int) tvShopcartEdit.getTag();
                if (tag == ACTION_EDIT) {
                    //变成完成状态
                    showDelete();
                } else {
                    //变成编辑状态
                    hideDelete();
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.deleteData();
                adapter.showTotalPrice();
                //显示空空如也
                checkData();
                adapter.checkAll();
            }
        });
        tvEmptyCartTobuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                Constants.isBackHome = true;
            }
        });

    }

    private void hideDelete() {
        tvShopcartEdit.setText("编辑");
        tvShopcartEdit.setTag(ACTION_EDIT);

        adapter.checkAll_none(true);
        llDelete.setVisibility(View.GONE);
        llCheckAll.setVisibility(View.VISIBLE);

        adapter.showTotalPrice();
    }

    private void showDelete() {
        tvShopcartEdit.setText("完成");
        tvShopcartEdit.setTag(ACTION_COMPLETE);

        adapter.checkAll_none(false);
        cbAll.setChecked(false);
        checkboxAll.setChecked(false);

        llDelete.setVisibility(View.VISIBLE);
        llCheckAll.setVisibility(View.GONE);

        adapter.showTotalPrice();
    }

    private void checkData() {
        if (adapter != null && adapter.getItemCount() > 0) {
            tvShopcartEdit.setVisibility(View.VISIBLE);
            ll_empty_shopcart.setVisibility(View.GONE);

        } else {
            ll_empty_shopcart.setVisibility(View.VISIBLE);
            tvShopcartEdit.setVisibility(View.GONE);

        }
    }

    private void showData() {
        CartProvider cartProvider = CartProvider.getInstance();

        List<GoodsBean> datas = cartProvider.getDataFromLocal();
        if (datas != null && datas.size() > 0) {
            tvShopcartEdit.setVisibility(View.VISIBLE);

            adapter = new ShopCartAdapter(mContext, datas, tvShopcartTotal, cartProvider, checkboxAll, cbAll);
            recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerview.setAdapter(adapter);
            ll_empty_shopcart.setVisibility(View.GONE);
        } else {
            //显示空的
            tvShopcartEdit.setVisibility(View.GONE);
            ll_empty_shopcart.setVisibility(View.VISIBLE);


        }

    }

    //-------------支付------

    // 商户PID
    public static final String PARTNER = PayKeys.DEFAULT_PARTNER;   //这几个用了PayKey中的方法；
    // 商户收款账号
    public static final String SELLER = PayKeys.DEFAULT_SELLER;
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = PayKeys.PRIVATE;
    // 支付宝公钥
    public static final String RSA_PUBLIC = PayKeys.PUBLIC;
    private static final int SDK_PAY_FLAG = 1;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(mContext, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(View v) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX); //开启沙箱

        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(mContext).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
//                            finish();
                        }
                    }).show();
            return;
        }
   //     String orderInfo = getOrderInfo("淘宝购物", "爆发了...", tvShopcartTotal.getText().toString().replace("￥", ""));//总价格

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
      /*  String sign = sign(orderInfo);
        try {
            *//**
             * 仅需对sign 做URL编码
             *//*
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
      //  final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();  //这里的参数必须由服务端生成
        final String payInfo="app_id=2016082000294682&biz_content=%7B%22body%22%3A%22%E6%91%98%E8%A6%81%22%2C%22out_trade_no%22%3A%22a0fcde54495a4dbd9a92ec222272b805%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22seller_id%22%3A%222088102172218970%22%2C%22subject%22%3A%22%E8%AE%A2%E5%8D%95title%22%2C%22total_amount%22%3A%221100.00%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fpay.egzosn.com%2FpayBack.json&return_url=http%3A%2F%2Fpay.egzosn.com%2FpayBack.html&sign=SmLdOW9WyhyfIAd2s64oBqk%2FRIUDYSaTs0AoEZq1UnLXaGmL9zhbQsiV9Xc2qbhtNfKIgyrRWudzSTgh0qIKVTff%2FEqfEZt7%2FKf4Q28GxjUAqMhIZmlH3qfNsKyda%2FTUt%2Bk3d7WdAM1WFhtfsz2jGqONzBjQwFJbIhwOGi6vsFcVeoEfbC%2Bwr%2BBVLXFb2RitKqnC5QiEU57ClLwA21FhRpc5hZxWoVpfK3KJlStij%2BHyjTQ1iPo1dQh6w7O%2FybDAFZYLaMTqA0YuIKkA6ZXNOnWje6A7OmKnTF1ITyTlqhJ60quVGGR5evJuPN3g%2ByAPal4gG8wuADdP5B%2FniOs1EA%3D%3D&sign_type=RSA2&timestamp=2019-01-15+11%3A46%3A21&version=1.0";
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask((Activity) mContext);
                // 调用支付接口，获取支付结果
                Map<String, String> result = alipay.payV2(payInfo,true);
                Log.v("ShoppingCartFragment","resultStatus============>>>>>>>"+result.get("resultStatus"));
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }



}
