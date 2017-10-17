package org.openmrs.module.muzimabiometrics;

import jlibfprint.JlibFprint;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScanFingerprint{
    private String baseUrl;
    private String url;
    public static void main(String[] args) throws IOException {
        JlibFprint jlibfprint = new JlibFprint();
        JlibFprint.fp_print_data pd1;
        ScanFingerprint scanFingerprint=new ScanFingerprint();
        scanFingerprint.baseUrl="http://localhost:8080/openmrs/module/muzimabiometrics";
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            pd1 = jlibfprint.enroll_finger();
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bo);
            oos.writeObject(pd1);
            oos.flush();
            byte[] encodedByteArray = Base64.encodeBase64(bo.toByteArray());
            System.out.println("encodedByteArray +++++++++++++++++++"+encodedByteArray);
            scanFingerprint.url=scanFingerprint.baseUrl+"/setFingerprint.form";
            HttpPost request=new HttpPost(scanFingerprint.url);
            request.setEntity(new ByteArrayEntity(encodedByteArray));
            request.setHeader("Content-type", "application/octet-stream");
            HttpResponse response = client.execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }
        }
        catch(IOException ex){
        }
        catch (JlibFprint.EnrollException e)
        {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            scanFingerprint.url=scanFingerprint.baseUrl+"/scannerException.form";
            HttpPost httpPost=new HttpPost(scanFingerprint.url);
            ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            StringEntity entity=new StringEntity(String.valueOf(e.enroll_exception));
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-type", "application/text");
            HttpResponse response = client.execute(httpPost);
            System.err.format("Enroll Exception [%d]\n", e.enroll_exception);
            e.printStackTrace();
        }
    }

}
