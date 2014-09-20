/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package net.texsoftware.lib;

import java.io.ByteArrayInputStream;

/**
 * Abstract class for network operations.
 */
public abstract class NetworkOperation {

    public static final int METHOD_GET = 1;
    public static final int METHOD_POST = 2;

    /**
     * Starts the operation in asynchronous manner.
     */
    public abstract void start();

    /**
     * HTTP method the operation uses.
     * 
     * @return METHOD_GET or METHOD_POST
     */
    public int getMethod() {
        return METHOD_GET;
    }

    /**
     * URL for the operation.
     * 
     * @return URL
     */
    public abstract String getUrl();

    /**
     * Request data.
     * 
     * @return data
     */
    public String getData() {
        return "";
    }

    /**
     * Content type for the request data.
     * 
     * @return content type
     */
    public String getContentType() {
        return "application/x-www-form-urlencoded";
    }

    /**
     * Default implementation for interface callback.
     * 
     * @param response
     */
    public void networkHttpPostResponse(String response) {
        System.out.println("Post response IGNORED");
    }

    /**
     * Default implementation for interface callback.
     * 
     * @param data
     */
    public void networkHttpGetResponse(byte[] data) {
        System.out.println("Get response IGNORED");
    }
    
    public void onNetworkFailure() {
        System.out.println("Network failure");
    }
}
