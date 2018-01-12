package org.openmrs.module.muzimabiometrics;

import jlibfprint.JlibFprint;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScanFingerprint{
    public static void main(String[] args){
        JlibFprint jlibfprint = new JlibFprint();
        JlibFprint.fp_print_data pd1;
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            pd1 = jlibfprint.enroll_finger();
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bo);
            oos.writeObject(pd1);
            oos.flush();
            byte[] encodedByteArray = Base64.encodeBase64(bo.toByteArray());
            System.out.println("new encodedByteArray +++++++++++++++++++"+encodedByteArray);
            String url="http://localhost:8080/openmrs/module/muzimabiometrics/setFingerprint.form";
            //List<NameValuePair> params = new ArrayList<NameValuePair>(2);
            //params.add(new BasicNameValuePair("fingerprint", encodedByteArray.toString()));
            HttpPost request=new HttpPost(url);
            request.setEntity(new ByteArrayEntity(encodedByteArray));
            request.setHeader("Content-type", "application/octet-stream");
            HttpResponse response = client.execute(request);

            BufferedReader rd = new BufferedReader
                    (new InputStreamReader(
                            response.getEntity().getContent()));

            String line = "";
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }
        }
        catch(IOException ex){
        }
        catch (JlibFprint.EnrollException e)
        {
            System.err.format("Enroll Exception [%d]\n", e.enroll_exception);
            e.printStackTrace();
        }
    }

}
