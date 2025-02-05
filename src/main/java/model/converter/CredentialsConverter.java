package model.converter;

import model.bean.CredentialsBean;
import model.domain.Credentials;

public class CredentialsConverter {

    private CredentialsConverter() {
    }

    // Convert CredentialsBean to Credentials
    public static Credentials toDomain(CredentialsBean bean) {
        Credentials credentials = new Credentials();
        credentials.setUsername(bean.getUsername());
        credentials.setPassword(bean.getPassword());
        credentials.setRole(bean.getRole());
        return credentials;
    }

}

