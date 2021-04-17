package com.sakura.common.web.wrapper;


import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @auther YangFan
 * @Date 2021/3/8 15:28
 */
public class SQLInjectionHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] bytes;

    public SQLInjectionHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);

        // 读取输入流里的请求参数，并保存到bytes里
        bytes = IOUtils.toByteArray(request.getInputStream());
    }

    public String getRequestBodyParame() {
        return new String(bytes, Charset.forName("utf8"));
    }

    /**
     *
     * <p>
     * Title: getInputStream
     * </p>
     * <p>
     * Description:处理POST请求参数 RequestBody is missing 问题
     * </p>
     *
     * @return
     * @throws IOException
     * @see javax.servlet.ServletRequestWrapper#getInputStream()
     */
    @Override
    public ServletInputStream getInputStream() {
        String body = new String(this.bytes);
        return new BufferedServletInputStream(body.getBytes());
    }

    class BufferedServletInputStream extends ServletInputStream {
        private ByteArrayInputStream inputStream;

        public BufferedServletInputStream(byte[] buffer) {
            // 此处即赋能，可以详细查看ByteArrayInputStream的该构造函数；
            this.inputStream = new ByteArrayInputStream(buffer);
        }

        @Override
        public int available() throws IOException {
            return inputStream.available();
        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return inputStream.read(b, off, len);
        }

        @Override
        public boolean isFinished() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isReady() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void setReadListener(ReadListener listener) {
            // TODO Auto-generated method stub

        }
    }

    @Override
    public String[] getParameterValues(String parameter) {
        // TODO Auto-generated method stub
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = values[i];
        }
        return encodedValues;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        return value;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return value;
    }
}
