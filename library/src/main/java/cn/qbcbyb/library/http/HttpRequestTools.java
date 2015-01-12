package cn.qbcbyb.library.http;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EncodingUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.qbcbyb.library.util.StringUtils;
import cn.qbcbyb.library.util.URLUtil;

public class HttpRequestTools {
    public static interface HttpStatusCallback {
        public boolean updateProgressAndCheckNeedGoing(Integer progress, int token);
    }

    public static class FileToPost {
        private String fileName;
        private byte[] fileData;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public byte[] getFileData() {
            return fileData;
        }

        public void setFileData(byte[] fileData) {
            this.fileData = fileData;
        }
    }

    private static final Pattern NAMES_PATTERN = Pattern.compile("\\{([^/]+?)\\}");
    private static String encodingType = HTTP.UTF_8;

    public static String getEncodingType() {
        return encodingType;
    }

    public static void setEncodingType(String encodingType) {
        HttpRequestTools.encodingType = encodingType;
    }

    /**
     * 对“/path/{value1}/{value2}?key1={value3}”形式的Path字符串按照给定的值进行格式化
     *
     * @param path
     * @param values
     * @return
     */
    public static String formatPath(String path, Object... values) {
        StringBuilder sb = new StringBuilder();
        Matcher m = NAMES_PATTERN.matcher(path);
        int end = 0;
        int arrIndex = 0;
        while (m.find()) {
            sb.append(path.substring(end, m.start()));
            if (arrIndex < values.length) {
                Object value = values[arrIndex];
                if (value != null) {
                    sb.append(URLUtil.formURLEncode(value.toString()));
                }
            }
            end = m.end();
            arrIndex++;
        }
        if (end < path.length()) {
            sb.append(path.substring(end));
        }
        return sb.toString();
    }

    public static String formatParam(Map<String, String> textMap) {
        return formatParam(textMap, true);
    }

    /**
     * 将键值对组合成K=V&K=V&K=V...的形式
     *
     * @param textMap       键值对
     * @param encodingValue 是否需要编码
     * @return
     */
    public static String formatParam(Map<String, String> textMap, boolean encodingValue) {

        StringBuilder sb = new StringBuilder();

        if (textMap != null) {
            Iterator<Entry<String, String>> iter = textMap.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, String> entry = iter.next();
                String inputName = entry.getKey();
                String inputValue = entry.getValue();
                if (inputValue == null) {
                    continue;
                }
                if (sb.length() != 0) {
                    sb.append("&");
                }
                sb.append(inputName);
                sb.append("=");
                if (encodingValue) {
                    try {
                        sb.append(URLEncoder.encode(inputValue, encodingType));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    sb.append(inputValue);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 通过GET方法获取类型为String的返回值
     *
     * @param url
     * @param httpStatusCallback 进度控制管理器
     * @param token              进度控制标识
     * @param proxy
     * @return 获取到的字符串
     * @throws java.io.IOException
     */
    public static String getReturnString(String url, HttpStatusCallback httpStatusCallback, int token, Proxy proxy)
            throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        get(url, outputStream, httpStatusCallback, token, proxy);

        outputStream.close();

        return EncodingUtils.getString(outputStream.toByteArray(), encodingType);
    }

    /**
     * 通过PUT方法获取类型为String的返回值
     *
     * @param url
     * @param httpStatusCallback
     * @param token
     * @param proxy
     * @return
     * @throws IOException
     */
    public static String putReturnString(String url, HttpStatusCallback httpStatusCallback, int token, Proxy proxy)
            throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        put(url, outputStream, httpStatusCallback, token, proxy);

        outputStream.close();

        return EncodingUtils.getString(outputStream.toByteArray(), encodingType);
    }

    /**
     * 通过POST方法获取类型为String的返回值
     *
     * @param url
     * @param textMap
     * @param httpStatusCallback 进度控制管理器
     * @param token              进度控制标识
     * @param proxy
     * @return 获取到的字符串
     * @throws java.io.IOException
     */
    public static String postReturnString(String url, Map<String, String> textMap,
                                          HttpStatusCallback httpStatusCallback, int token, Proxy proxy) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        post(url, textMap, outputStream, httpStatusCallback, token, proxy);

        outputStream.close();

        return EncodingUtils.getString(outputStream.toByteArray(), encodingType);
    }

    /**
     * 通过POST方法上传文件等并获取服务器返回的字符串
     *
     * @param url
     * @param textMap
     * @param fileMap
     * @param httpStatusCallback 进度控制管理器
     * @param token              进度控制标识
     * @param proxy
     * @return
     * @throws java.io.IOException
     */
    public static String postBytesReturnString(String url, Map<String, String> textMap, Map<String, FileToPost> fileMap,
                                               HttpStatusCallback httpStatusCallback, int token, Proxy proxy) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        postBytes(url, textMap, fileMap, outputStream, httpStatusCallback, token, proxy);

        outputStream.close();

        return EncodingUtils.getString(outputStream.toByteArray(), encodingType);
    }

    /**
     * 通过GET方法获取服务器返回stream，并存入outputStream
     *
     * @param url
     * @param outputStream
     * @param httpStatusCallback 进度控制管理器
     * @param token              进度控制标识
     * @param proxy
     * @throws java.io.IOException
     */
    public static void get(String url, OutputStream outputStream, HttpStatusCallback httpStatusCallback, int token,
                           Proxy proxy) throws IOException {
        request(url, HttpGet.METHOD_NAME, null, null, outputStream, httpStatusCallback, token, proxy);
    }


    /**
     * 通过PUT方法获取服务器返回stream，并存入outputStream
     *
     * @param url
     * @param outputStream
     * @param httpStatusCallback 进度控制管理器
     * @param token              进度控制标识
     * @param proxy
     * @throws java.io.IOException
     */
    public static void put(String url, OutputStream outputStream, HttpStatusCallback httpStatusCallback, int token,
                           Proxy proxy) throws IOException {
        request(url, HttpPut.METHOD_NAME, null, null, outputStream, httpStatusCallback, token, proxy);
    }

    /**
     * 通过POST方法获取服务器返回stream，并存入outputStream
     *
     * @param url
     * @param textMap
     * @param outputStream
     * @param httpStatusCallback 进度控制管理器
     * @param token              进度控制标识
     * @param proxy
     * @throws java.io.IOException
     */
    public static void post(String url, Map<String, String> textMap, OutputStream outputStream,
                            HttpStatusCallback httpStatusCallback, int token, Proxy proxy) throws IOException {
        String postString = formatParam(textMap, false);
        byte[] requestBytes = null;
        if (!StringUtils.isEmpty(postString)) {
            requestBytes = postString.getBytes(encodingType);
        }
        request(url, HttpPost.METHOD_NAME, requestBytes, null, outputStream, httpStatusCallback, token, proxy);
    }

    /**
     * 通过POST方法上传文件等并获取服务器返回stream，存入outputStream
     *
     * @param url
     * @param textMap
     * @param fileMap
     * @param outputStream
     * @param httpStatusCallback
     * @param token
     * @param proxy
     * @throws java.io.IOException
     */
    public static void postBytes(String url, Map<String, String> textMap, Map<String, FileToPost> fileMap,
                                 OutputStream outputStream, HttpStatusCallback httpStatusCallback, int token, Proxy proxy)
            throws IOException {

        String boundary = "----------" + Long.toHexString(new Date().getTime());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (textMap != null) {
            StringBuilder sb = new StringBuilder();
            Iterator<Entry<String, String>> iter = textMap.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, String> entry = iter.next();
                String inputName = entry.getKey();
                String inputValue = entry.getValue();
                if (inputValue == null) {
                    continue;
                }
                sb.append("\r\n--").append(boundary).append("\r\n");
                sb.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
                sb.append(inputValue);
            }
            byteArrayOutputStream.write(sb.toString().getBytes(encodingType));
        }
        if (fileMap != null) {
            Iterator<Entry<String, FileToPost>> iter = fileMap.entrySet().iterator();
            while (iter.hasNext()) {
                StringBuilder sb = new StringBuilder();
                Entry<String, FileToPost> entry = iter.next();
                String inputName = (String) entry.getKey();
                FileToPost inputValue = entry.getValue();
                if (inputValue == null || inputValue.getFileData() == null || inputValue.getFileData().length == 0) {
                    continue;
                }

                sb.append("\r\n--").append(boundary).append("\r\n");
                sb.append("Content-Disposition: form-data; name=\"").append(inputName).append("\"; filename=\"")
                        .append(inputValue.getFileName()).append("\"");
                sb.append("\r\n");
                sb.append("Content-Type:application/octet-stream");
                sb.append("\r\n");
                sb.append("\r\n");

                byteArrayOutputStream.write(sb.toString().getBytes(encodingType));
                byteArrayOutputStream.write(inputValue.getFileData());
            }
        }
        byte[] boundaryBytes = ("\r\n--" + boundary + "--\r\n").getBytes(encodingType);
        byteArrayOutputStream.write(boundaryBytes);

        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        request(url, HttpPost.METHOD_NAME, byteArrayOutputStream.toByteArray(), boundary, outputStream,
                httpStatusCallback, token, proxy);

    }

    /**
     * http请求的基础方法，一般不直接调用该方法
     *
     * @param url
     * @param requestBytes
     * @param requestMethod
     * @param boundary
     * @param outputStream
     * @param httpStatusCallback
     * @param token
     * @param proxy
     * @throws java.io.IOException
     */
    public static void request(String url, String requestMethod, byte[] requestBytes, String boundary,
                               OutputStream outputStream, HttpStatusCallback httpStatusCallback, int token, Proxy proxy) throws IOException {

        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try {
            URL requestUrl = new URL(url);
            if (proxy != null) {
                conn = (HttpURLConnection) requestUrl.openConnection(proxy);
            } else {
                conn = (HttpURLConnection) requestUrl.openConnection();
            }
            if (requestMethod.equals(HttpPost.METHOD_NAME)) {
                conn.setDoOutput(true);
            }
            conn.setRequestMethod(requestMethod);
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(300000);
            if (!StringUtils.isEmpty(boundary)) {
                conn.setRequestProperty("Connection", "keep-alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            } else {
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            }
            if (requestBytes != null && requestBytes.length > 0) {
                conn.setRequestProperty("Content-Length", "" + requestBytes.length);
            }

            conn.connect();

            if (requestBytes != null && requestBytes.length > 0) {
                OutputStream requestOutputStream = conn.getOutputStream();
                requestOutputStream.write(requestBytes);
                requestOutputStream.flush();
                requestOutputStream.close();
            }

            if (conn.getResponseCode() == HttpStatus.SC_OK || conn.getResponseCode() == HttpStatus.SC_CREATED) {
                inputStream = conn.getInputStream();
                byte[] b = new byte[1024];
                int len = inputStream.read(b);
                int readedCount = len;
                while (len != -1) {
                    outputStream.write(b, 0, len);
                    if (httpStatusCallback != null) {
                        int totalCount = conn.getContentLength();
                        if (totalCount <= 0) {
                            int unReadedCount = -1;
                            try {
                                Field bytesRemainingField = inputStream.getClass().getDeclaredField("bytesRemaining");
                                bytesRemainingField.setAccessible(true);
                                unReadedCount = bytesRemainingField.getInt(inputStream);
                            } catch (SecurityException e) {
                                e.printStackTrace();
                            } catch (NoSuchFieldException e) {
                                e.printStackTrace();
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            if (unReadedCount < inputStream.available()) {
                                unReadedCount = inputStream.available();
                            }
                            totalCount = readedCount + unReadedCount;
                        }
                        if (!httpStatusCallback.updateProgressAndCheckNeedGoing(
                                (int) (readedCount * 100d / totalCount), token)) {
                            throw new IOException("取消请求");
                        }
                    }
                    len = inputStream.read(b);
                    readedCount += len;
                }
                inputStream.close();
            } else {
                throw new IOException(Integer.toString(conn.getResponseCode()));
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            outputStream.flush();
        }
    }

}
