package com.unloged.overlay.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class NetworkUtils
{
    public static String newConnection(String link) {
        String result = "";
        HttpURLConnection con = null;

        try {
            URL url = new URL(link);
            con = (HttpURLConnection)url.openConnection();
            result = getContents(con);
        } catch (IOException var8) {
            var8.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }

        }

        return result;
    }

    public static String newConnectionWithHeader(String link, List<String[]> headerList) {
        String result = "";
        HttpURLConnection con = null;
        try {
            URL url = new URL(link);
            con = (HttpURLConnection)url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
            if (headerList != null && !headerList.isEmpty()) {
                for (String[] header : headerList) {
                    con.setRequestProperty(header[0], header[1]);
                }
            }
            if (con.getResponseCode() != 200) {
                result = String.valueOf(con.getResponseCode());
                System.out.println("Attempted connection (" + (link.length() > 30 ? link.substring(0, 30) + "..." : link) + ") resulted in response code " + result);
            } else {
                result = NetworkUtils.getContents(con);
            }
        }
        catch (IOException iOException) {
        }
        finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return result;
    }

    private static String getContents(HttpURLConnection con) {
        if (con != null) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                Throwable var2 = null;

                String var5;
                try {
                    StringBuilder sb = new StringBuilder();

                    String input;
                    while((input = br.readLine()) != null) {
                        sb.append(input);
                    }

                    var5 = sb.toString();
                } catch (Throwable var15) {
                    var2 = var15;
                    throw var15;
                } finally {
                    if (br != null) {
                        if (var2 != null) {
                            try {
                                br.close();
                            } catch (Throwable var14) {
                                var2.addSuppressed(var14);
                            }
                        } else {
                            br.close();
                        }
                    }

                }

                return var5;
            } catch (IOException var17) {
            }
        }

        return "";
    }
}
