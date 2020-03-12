package com.anju.yyk.client.http;

import com.anju.yyk.client.BuildConfig;
import com.anju.yyk.client.data.ElderInfoRsp;
import com.anju.yyk.client.data.LoginRsp;
import com.anju.yyk.client.data.NoticeRsp;
import com.anju.yyk.client.data.TipsRsp;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    private static final String BASE_URL = "http://haohua.zhihuidangjian.com/";
    private static final String ACTION_LOGIN = "denglu";
    private static final String ACTION_INFO = "laoren_list";
    private static final String ACTION_NOTICE = "tongzhi";
    private static final String ACTION_TIPS = "tixing";

    private static RetrofitHelper self;
    private ApiService apiService;

    public static RetrofitHelper getInstance() {
        if (self == null) {
            synchronized (RetrofitHelper.class) {
                if (self == null) {
                    self = new RetrofitHelper();
                }
            }
        }
        return self;
    }

    private RetrofitHelper() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            //Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);//这里可以选择拦截级别
            builder.addInterceptor(loggingInterceptor);
        }

        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public void login(Observer<LoginRsp> observer, String username, String password) {
        apiService.login(ACTION_LOGIN, username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getElderInfo(Observer<ElderInfoRsp> observer, String id) {
        apiService.getElderInfo(ACTION_INFO, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getNotice(Observer<NoticeRsp> observer) {
        apiService.getNotice(ACTION_NOTICE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getTips(Observer<TipsRsp> observer, String id) {
        apiService.getTips(ACTION_TIPS, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
