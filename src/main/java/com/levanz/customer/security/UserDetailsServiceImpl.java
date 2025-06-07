package com.levanz.customer.security;

import com.levanz.customer.repository.AdminRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AdminRepository repo;

    public UserDetailsServiceImpl(AdminRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        return repo.findByUsername(username)
                   .map(UserDetailsImpl::new)
                   .orElseThrow(() ->
                       new UsernameNotFoundException("Admin '" + username + "' not found"));
    }
}
