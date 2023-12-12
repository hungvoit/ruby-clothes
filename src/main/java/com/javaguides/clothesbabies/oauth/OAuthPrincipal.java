package com.javaguides.clothesbabies.oauth;

import java.math.BigInteger;

public class OAuthPrincipal {
    private final BigInteger id;

    public OAuthPrincipal(BigInteger id) {
        this.id = id;
    }

    public BigInteger getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OAuthPrincipal that = (OAuthPrincipal) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
