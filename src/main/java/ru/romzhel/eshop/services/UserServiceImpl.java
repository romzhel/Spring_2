package ru.romzhel.eshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.romzhel.eshop.entities.Role;
import ru.romzhel.eshop.entities.SystemUser;
import ru.romzhel.eshop.entities.User;
import ru.romzhel.eshop.repositories.RoleRepository;
import ru.romzhel.eshop.repositories.UserRepository;
import ru.romzhel.eshop.validation.PhoneValidator;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    private PhoneValidator phoneValidator;

    @Override
    @Transactional
    public User findByUserName(String userName) {
        return userRepository.findOneByUserName(userName);
    }

    @Override
    @Transactional
    public boolean save(SystemUser systemUser) {
        User user = new User();
        user.setUserName(systemUser.getUserName());
        user.setPassword(passwordEncoder.encode(systemUser.getPassword()));
        user.setFirstName(systemUser.getFirstName());
        user.setLastName(systemUser.getLastName());
        user.setEmail(systemUser.getEmail());
        user.setPhone(phoneValidator.normalize(systemUser.getPhone()));

        user.setRoles(Arrays.asList(roleRepository.findOneByName("ROLE_EMPLOYEE")));

        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional
    public boolean save(User user) {
        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteByUserName(String userName) {
        userRepository.deleteByUserName(userName);
        return true;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findOneByUserName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Transactional
    public List<User> getAll() {
        return userRepository.getAllBy();
    }
}
