package com.dire.guard;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;


public class UserTemplate implements MultiAuthenticationUserDetails, CredentialsContainer {

    private final String username;
    private transient String password;
    private final Set<GrantedAuthority> authorities;
    private final boolean methodNonExpired;
    private final boolean credentialsNonExpired;
    private final boolean enabled;
    private final boolean currentMethodNonLocked;
    private final String currentMethod;

    public UserTemplate(String username, String password) {
        this(username, password, new HashSet<>());
    }

    public UserTemplate(String username, String password, Set<GrantedAuthority> authorities) {
        this(username, password, authorities, true, true,
                true, true, "");
    }

    public UserTemplate(String username, String password, Set<GrantedAuthority> authorities, boolean methodNonExpired,
                        boolean credentialsNonExpired, boolean enabled, boolean currentMethodNonLocked, String currentMethod) {
        this.username = username;
        this.password = password;
        this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
        this.methodNonExpired = methodNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.currentMethodNonLocked = currentMethodNonLocked;
        this.currentMethod = currentMethod;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public String getCurrentMethod() {
        return this.currentMethod;
    }

    @Override
    public boolean isCurrentMethodNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isCurrentMethodNonLocked() {
        return this.currentMethodNonLocked;
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    /**
     * 复制的spring security 的{@link org.springframework.security.core.userdetails.User}中的sortAuthorities方法
     */
    private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        if (authorities == null) {
            authorities = new ArrayList<>();
        }
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(new UserTemplate.AuthorityComparator());
        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }
        return sortedAuthorities;
    }

    public static UserTemplateBuilder builder() {
        return new UserTemplateBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof UserTemplate) {
            return this.username.equals(((UserTemplate) o).getUsername());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.username.hashCode();
    }

    @Override
    public String toString() {
        return "UserTemplate{" +
                "username='" + username + '\'' +
                ", password='[PROTECT]" + '\'' +
                ", authorities=" + authorities +
                ", methodNonExpired=" + methodNonExpired +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", enabled=" + enabled +
                ", currentMethodNonLocked=" + currentMethodNonLocked +
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

        private String username;
        private String password;
        private Set<GrantedAuthority> authorities;
        private boolean methodExpired;
        private boolean credentialsExpired;
        private boolean disabled;
        private boolean currentMethodLocked;
        private String currentMethod;
        private Function<String, String> passwordEncoder = (password) -> password;

        public UserTemplateBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserTemplateBuilder password(String password) {
            this.password = password;
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

        public UserTemplateBuilder disabled(boolean disabled) {
            this.disabled = disabled;
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

        public UserTemplateBuilder authorities(GrantedAuthority...grantedAuthorities) {
            return authorities(Arrays.asList(grantedAuthorities));
        }

        public UserTemplateBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
            Assert.notNull(authorities, "");
            if (CollectionUtils.isEmpty(this.authorities)) {
                this.authorities = new HashSet<>();
            }
            this.authorities.addAll(authorities);
            return this;
        }

        public UserTemplateBuilder passwordEncoder(Function<String, String> encoder) {
            Assert.notNull(encoder, "encoder cannot be null");
            this.passwordEncoder = encoder;
            return this;
        }

        public UserTemplate build() {
            if (this.authorities == null) {
                this.authorities = new HashSet<>();
            }
            String encodePassword = passwordEncoder.apply(this.password);
            return new UserTemplate(this.username, encodePassword, this.authorities, !this.methodExpired,
                    !this.credentialsExpired, !this.disabled, !this.currentMethodLocked, this.currentMethod);
        }
    }
}
