//package com.clg.wallet.wallet.Omni;
//
//import com.clg.wallet.utils.Base64Coder;
//import com.clg.wallet.utils.JSON;
//import com.clg.wallet.wallet.bitcoin.BitcoinException;
//import com.clg.wallet.wallet.bitcoin.BitcoinRPCException;
//import lombok.extern.slf4j.Slf4j;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.net.URL;
//import java.nio.charset.Charset;
//import java.util.Arrays;
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.Properties;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.SSLSocketFactory;
//
//@Slf4j
//public class JSONRPCClient1 {
//    private static final Logger logger = Logger.getLogger(JSONRPCClient.class.getCanonicalName());
//    public final URL rpcURL;
//    private URL noAuthURL;
//    private String authStr;
//    public static final URL DEFAULT_JSONRPC_URL;
//    public static final URL DEFAULT_JSONRPC_TESTNET_URL;
//    private HostnameVerifier hostnameVerifier;
//    private SSLSocketFactory sslSocketFactory;
//    private int connectTimeout;
//    public static final Charset QUERY_CHARSET;
//
//    public JSONRPCClient(String rpcUrl) throws MalformedURLException {
//        this(new URL(rpcUrl));
//    }
//
//    public JSONRPCClient(URL rpc) {
//        this.hostnameVerifier = null;
//        this.sslSocketFactory = null;
//        this.connectTimeout = 0;
//        this.rpcURL = rpc;
//
//        try {
//            this.noAuthURL = (new URI(rpc.getProtocol(), (String) null, rpc.getHost(), rpc.getPort(), rpc.getPath(), rpc.getQuery(), (String) null)).toURL();
//        } catch (MalformedURLException var3) {
//            throw new IllegalArgumentException(rpc.toString(), var3);
//        } catch (URISyntaxException var4) {
//            throw new IllegalArgumentException(rpc.toString(), var4);
//        }
//
//        this.authStr = rpc.getUserInfo() == null ? null : String.valueOf(Base64Coder.encode(rpc.getUserInfo().getBytes(Charset.forName("ISO8859-1"))));
//    }
//
//    public JSONRPCClient(boolean testNet) {
//        this(testNet ? DEFAULT_JSONRPC_TESTNET_URL : DEFAULT_JSONRPC_URL);
//    }
//
//    public JSONRPCClient() {
//        this(DEFAULT_JSONRPC_TESTNET_URL);
//    }
//
//    public HostnameVerifier getHostnameVerifier() {
//        return this.hostnameVerifier;
//    }
//
//    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
//        this.hostnameVerifier = hostnameVerifier;
//    }
//
//    public SSLSocketFactory getSslSocketFactory() {
//        return this.sslSocketFactory;
//    }
//
//    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
//        this.sslSocketFactory = sslSocketFactory;
//    }
//
//    public void setConnectTimeout(int timeout) {
//        if (timeout < 0) {
//            throw new IllegalArgumentException("timeout can not be negative");
//        } else {
//            this.connectTimeout = timeout;
//        }
//    }
//
//    public int getConnectTimeout() {
//        return this.connectTimeout;
//    }
//
//    public byte[] prepareRequest(final String method, final Object... params) {
//        return JSON.stringify(new LinkedHashMap() {
//            {
//                this.put("method", method);
//                this.put("params", params);
//                this.put("id", "1");
//            }
//        }).getBytes(QUERY_CHARSET);
//    }
//
//    private static byte[] loadStream(InputStream in, boolean close) throws IOException {
//        ByteArrayOutputStream o = new ByteArrayOutputStream();
//        byte[] buffer = new byte[1024];
//
//        while (true) {
//            int nr = in.read(buffer);
//            if (nr == -1) {
//                return o.toByteArray();
//            }
//
//            if (nr == 0) {
//                throw new IOException("Read timed out");
//            }
//
//            o.write(buffer, 0, nr);
//        }
//    }
//
//    public Object loadResponse(InputStream in, Object expectedID, boolean close) throws IOException, BitcoinException {
//        Object var6;
//        try {
//            String r = new String(loadStream(in, close), QUERY_CHARSET);
//            logger.log(Level.FINE, "Bitcoin JSON-RPC response:\n{0}", r);
//
//            try {
//                Map response = (Map) JSON.parse(r);
//                if (!expectedID.equals(response.get("id"))) {
//                    throw new BitcoinRPCException("Wrong response ID (expected: " + String.valueOf(expectedID) + ", response: " + response.get("id") + ")");
//                }
//
//                if (response.get("error") != null) {
//                    throw new BitcoinException(JSON.stringify(response.get("error")));
//                }
//
//                var6 = response.get("result");
//            } catch (ClassCastException var10) {
//                throw new BitcoinRPCException("Invalid server response format (data: \"" + r + "\")");
//            }
//        } finally {
//            if (close) {
//                in.close();
//            }
//
//        }
//
//        return var6;
//    }
//
//    public Object query(String method, Object... o) throws BitcoinException {
//        try {
//            HttpURLConnection conn = (HttpURLConnection) this.noAuthURL.openConnection();
//            if (this.connectTimeout != 0) {
//                conn.setConnectTimeout(this.connectTimeout);
//            }
//
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            if (conn instanceof HttpsURLConnection) {
//                if (this.hostnameVerifier != null) {
//                    ((HttpsURLConnection) conn).setHostnameVerifier(this.hostnameVerifier);
//                }
//
//                if (this.sslSocketFactory != null) {
//                    ((HttpsURLConnection) conn).setSSLSocketFactory(this.sslSocketFactory);
//                }
//            }
//            conn.setRequestProperty("Authorization", "Basic " + this.authStr);
//            byte[] r = this.prepareRequest(method, o);
//            logger.log(Level.FINE, "Bitcoin JSON-RPC request:\n{0}", new String(r, QUERY_CHARSET));
//            conn.getOutputStream().write(r);
//            conn.getOutputStream().close();
//            int responseCode = conn.getResponseCode();
//            if (responseCode != 200) {
//                String response = new String(loadStream(conn.getErrorStream(), true));
//                    BitcoinRPCException bitcoinRPCException = new BitcoinRPCException("RPC Query Failed (method: " + method + ", params: " + Arrays.deepToString(o) + ", response header: " + responseCode + " " + conn.getResponseMessage() + ", response: " + new String(loadStream(conn.getErrorStream(), true)));
//                    bitcoinRPCException.setErrorResponse(response);
//                    throw bitcoinRPCException;
//            } else {
//                return this.loadResponse(conn.getInputStream(), "1", true);
//            }
//        } catch (IOException var8) {
//            throw new BitcoinRPCException("RPC Query Failed (method: " + method + ", params: " + Arrays.deepToString(o) + ")", var8);
//        }
//    }
//
//    static {
//        String user = "user";
//        String password = "pass";
//        String host = "localhost";
//        String port = null;
//
//        try {
//            File home = new File(System.getProperty("user.home"));
//            File f;
//            if (!(f = new File(home, ".bitcoin" + File.separatorChar + "bitcoin.conf")).exists() && !(f = new File(home, "AppData" + File.separatorChar + "Roaming" + File.separatorChar + "Bitcoin" + File.separatorChar + "bitcoin.conf")).exists()) {
//                f = null;
//            }
//
//            if (f != null) {
//                logger.fine("Bitcoin configuration file found");
//                Properties p = new Properties();
//                FileInputStream i = new FileInputStream(f);
//
//                try {
//                    p.load(i);
//                } finally {
//                    i.close();
//                }
//
//                user = p.getProperty("rpcuser", user);
//                password = p.getProperty("rpcpassword", password);
//                host = p.getProperty("rpcconnect", host);
//                port = p.getProperty("rpcport", port);
//            }
//        } catch (Exception var14) {
//            logger.log(Level.SEVERE, (String) null, var14);
//        }
//
//        try {
//            DEFAULT_JSONRPC_URL = new URL("http://" + user + ':' + password + "@" + host + ":" + (port == null ? "8332" : port) + "/");
//            DEFAULT_JSONRPC_TESTNET_URL = new URL("http://" + user + ':' + password + "@" + host + ":" + (port == null ? "18332" : port) + "/");
//        } catch (MalformedURLException var12) {
//            throw new RuntimeException(var12);
//        }
//
//        QUERY_CHARSET = Charset.forName("ISO8859-1");
//    }
//}
