package com.nawbio.api.domain.analytics;

public class TrackingRequest {
    private String pagePath;
    private String referrer;

    // Constructors
    public TrackingRequest() {}

    public TrackingRequest(String pagePath, String referrer) {
        this.pagePath = pagePath;
        this.referrer = referrer;
    }

    // Getters and Setters
    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }
}
