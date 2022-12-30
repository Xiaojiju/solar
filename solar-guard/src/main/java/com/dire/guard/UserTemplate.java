package com.dire.guard;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;


public class UserTemplate implements AccountDetails, CredentialsContainer, Permission {

    private final String uniqueId;
    private final String currentReferenceKey;
    private String secretKey;
    private final Set<GrantedAuthority> authorities;
    private final boolean methodExpired;
    private final boolean credentialsExpired;
    private final boolean enabled;
    private final boolean currentMethodLocked;
    private final String currentMethod;

    public UserTemplate(String uniqueId, String currentReferenceKey, String secretKey, Collection<? extends GrantedAuthority> authorities) {
        this(uniqueId, currentReferenceKey, secretKey, authorities,false, false,
                true, false, "default");
    }

    public UserTemplate(String uniqueId, String currentReferenceKey, String secretKey,
                        Collection<? extends GrantedAuthority> authorities, boolean methodExpired, boolean credentialsExpired,
                        boolean enabled, boolean currentMethodLocked,
                        String currentMethod) {
        this.uniqueId = uniqueId;
        this.currentReferenceKey = currentReferenceKey;
        this.secretKey = secretKey;
        this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
        this.methodExpired = methodExpired;
        this.credentialsExpired = credentialsExpired;
        this.enabled = enabled;
        this.currentMethodLocked = currentMethodLocked;
        this.currentMethod = currentMethod;
    }

    @Override
    public String getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public String getCurrentReferenceKey() {
        return this.currentReferenceKey;
    }

    @Override
    public String getSecretKey() {
        return this.secretKey;
    }

    @Override
    public String getCurrentMethod() {
        return this.currentMethod;
    }

    @Override
    public boolean isMethodExpired() {
        return this.methodExpired;
    }

    @Override
    public boolean isCredentialsExpired() {
        return this.credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public boolean isCurrentMethodLocked() {
        return this.currentMethodLocked;
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public void eraseCredentials() {
        this.secretKey = null;
    }

    /**
     * 复制的spring security 的{@link org.springframework.security.core.userdetails.User}中的sortAuthorities方法
     */
    private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(new UserTemplate.AuthorityComparator());
        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }
        return sortedAuthorities;
    }

    public static UserTemplateBuilder withReferenceKey(String uniqueId) {
        return null;
    }

    public static UserTemplateBuilder withReferenceKey(String referenceKey, String currentMethod) {
        return null;
    }

    public static UserTemplateBuilder builder() {
        return new UserTemplateBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof UserTemplate) {
            return this.uniqueId.equals(((UserTemplate) o).getUniqueId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.uniqueId.hashCode();
    }

    @Override
    public String toString() {
        return "UserTemplate{" +
                "uniqueId='" + uniqueId + '\'' +
                ", currentReferenceKey='" + currentReferenceKey + '\'' +
                ", secretKey='[PROTECTED]'" +
                ", methodExpired=" + methodExpired +
                ", credentialsExpired=" + credentialsExpired +
                ", enabled=" + enabled +
                ", currentMethodLocked=" + currentMethodLocked +
                ", currentMethod='" + currentMethod + '\'' +
                '}';
    }

    /**
     * 复制的spring security 的{@link org.springframework.security.core.userdetails.User}中的内部类AuthorityComparator
     */
    private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {

        private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

        @Override
        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            // Neither should ever be null as each entry is checked before adding it to
            // the set. If the authority is null, it is a custom authority and should
            // precede others.
            if (g2.getAuthority() == null) {
                return -1;
            }
            if (g1.getAuthority() == null) {
                return 1;
            }
            return g1.getAuthority().compareTo(g2.getAuthority());
        }

    }

    public static class UserTemplateBuilder {

        private String uniqueId;
        private String currentReferenceKey;
        private String secretKey;
        private List<GrantedAuthority> authorities;
        private boolean methodExpired;
        private boolean credentialsExpired;
        private boolean enabled;
        private boolean currentMethodLocked;
        private String currentMethod;

        private UserTemplateBuilder() {}

        public UserTemplateBuilder uniqueId(String uniqueId) {
            this.uniqueId = uniqueId;
            return this;
        }

        public UserTemplateBuilder currentReferenceKey(String currentReferenceKey) {
            this.currentReferenceKey = currentReferenceKey;
            return this;
        }

        public UserTemplateBuilder secretKey(String secretKey) {
            this.secretKey = secretKey;
            return this;
        }

        public UserTemplateBuilder methodExpired(boolean methodExpired) {
            this.methodExpired = methodExpired;
            return this;
        }

        public UserTemplateBuilder credentialsExpired(boolean credentialsExpired) {
            this.credentialsExpired = credentialsExpired;
            return this;
        }

        public UserTemplateBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public UserTemplateBuilder currentMethodLocked(boolean currentMethodLocked) {
            this.currentMethodLocked = currentMethodLocked;
            return this;
        }

        public UserTemplateBuilder currentMethod(String currentMethod) {
            this.currentMethod = currentMethod;
            return this;
        }

        public UserTemplateBuilder authorities(String...authorities) {
            return this;
        }

        public UserTemplateBuilder authorities(GrantedAuthority...authorities) {
            return this;
        }

        public UserTemplateBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
            return this;
        }

        public UserTemplate build() {
            return new UserTemplate(this.uniqueId, this.currentReferenceKey, this.secretKey, this.authorities, this.methodExpired,
                    this.credentialsExpired, this.enabled, this.currentMethodLocked, this.currentMethod);
        }
    }
}
