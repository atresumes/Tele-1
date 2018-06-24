package com.payu.custombrowser.util;

import android.os.AsyncTask;
import com.payu.custombrowser.bean.CustomBrowserAsyncTaskData;
import com.payu.custombrowser.cbinterface.CustomBrowserAsyncTaskInterface;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CustomBrowserAsyncTask extends AsyncTask<CustomBrowserAsyncTaskData, String, String> {
    private CustomBrowserAsyncTaskInterface mCustomBrowserAsyncTaskInterface;

    private CustomBrowserAsyncTask() {
    }

    public CustomBrowserAsyncTask(CustomBrowserAsyncTaskInterface customBrowserAsyncTaskInterface) {
        this.mCustomBrowserAsyncTaskInterface = customBrowserAsyncTaskInterface;
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(CustomBrowserAsyncTaskData... customBrowserAsyncTaskData) {
        int i = 0;
        CustomBrowserAsyncTaskData mCustomBrowserAsyncTaskData = customBrowserAsyncTaskData[0];
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(mCustomBrowserAsyncTaskData.getUrl()).openConnection();
            conn.setRequestMethod(mCustomBrowserAsyncTaskData.getHttpMethod());
            conn.setRequestProperty("Content-Type", mCustomBrowserAsyncTaskData.getContentType());
            String str = "Content-Length";
            StringBuilder append = new StringBuilder().append("");
            if (mCustomBrowserAsyncTaskData.getPostData() != null) {
                i = mCustomBrowserAsyncTaskData.getPostData().length();
            }
            conn.setRequestProperty(str, append.append(i).toString());
            conn.getOutputStream().write(mCustomBrowserAsyncTaskData.getPostData().getBytes());
            InputStream responseInputStream = conn.getInputStream();
            StringBuffer responseStringBuffer = new StringBuffer();
            byte[] byteContainer = new byte[1024];
            while (true) {
                int i2 = responseInputStream.read(byteContainer);
                if (i2 == -1) {
                    return responseStringBuffer.toString();
                }
                responseStringBuffer.append(new String(byteContainer, 0, i2));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage().toString();
        }
    }

    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        this.mCustomBrowserAsyncTaskInterface.onCustomBrowserAsyncTaskResponse(s);
    }
}
