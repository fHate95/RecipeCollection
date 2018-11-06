package com.sanechek.recipecollection.injection.modules;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import com.sanechek.recipecollection.api.interceptors.QueryToBodyConverterInterceptor;
import com.sanechek.recipecollection.api.modules.InterceptorModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FieldProviderModule {

    @Provides
    @Singleton
    @Inject
    public QueryToBodyConverterInterceptor.iFieldProvider instantiateFieldProvider(Context context, QueryToBodyConverterInterceptor.iSubjectProvider subjectProvider,
                                                                                   InterceptorModule.iPublicKeyProvider keyProvider, SimpleDateFormat sdf){
        return new FieldProviderImpl(context,keyProvider,sdf, subjectProvider);
    }

    private static class FieldProviderImpl implements QueryToBodyConverterInterceptor.iFieldProvider{

        private final String ANDROID_ID;
        private final InterceptorModule.iPublicKeyProvider keyProvider;
        private final SimpleDateFormat sdf;
        private final QueryToBodyConverterInterceptor.iSubjectProvider subjectProvider;

        public FieldProviderImpl(Context context,
                                 InterceptorModule.iPublicKeyProvider keyProvider,
                                 SimpleDateFormat sdf,
                                 QueryToBodyConverterInterceptor.iSubjectProvider subjectProvider) {
            ANDROID_ID = Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
            this.keyProvider = keyProvider;
            this.sdf = sdf;
            this.subjectProvider = subjectProvider;
        }

        @Override
        public void addParamsIfNeed(String request, JSONObject object) throws JSONException {

        }

        private static String capitalize(String s) {
            if (s == null || s.length() == 0) {
                return "";
            }
            char first = s.charAt(0);
            if (Character.isUpperCase(first)) {
                return s;
            } else {
                return Character.toUpperCase(first) + s.substring(1);
            }
        }

        private static String getDeviceName() {
            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;
            if (model.startsWith(manufacturer)) {
                return capitalize(model);
            } else {
                return capitalize(manufacturer) + " " + model;
            }
        }
    }
}
