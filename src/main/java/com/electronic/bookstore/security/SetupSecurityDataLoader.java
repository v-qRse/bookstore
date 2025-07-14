package com.electronic.bookstore.security;

import com.electronic.bookstore.security.repositories.PrivilegesRepository;
import com.electronic.bookstore.security.repositories.RolesRepository;
import com.electronic.bookstore.security.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

@Component
public class SetupSecurityDataLoader implements
        ApplicationListener<ContextRefreshedEvent>
{
   boolean alreadySetup = false;

   @Autowired
   private UsersRepository usersRepository;

   @Autowired
   private RolesRepository rolesRepository;

   @Autowired
   private PrivilegesRepository privilegesRepository;

   @Autowired
   private PasswordEncoder passwordEncoder;

   @Override
   @Transactional
   public void onApplicationEvent(ContextRefreshedEvent event) {
      if (alreadySetup) {
         return;
      }
      Privilege createEmpPrivilege = createPrivilegeIfNotFound("CREATE_EMPLOYEE_PRIVILEGE");
      Privilege createBookPrivilege = createPrivilegeIfNotFound("CREATE_BOOK_PRIVILEGE");
      Privilege cartPrivilege = createPrivilegeIfNotFound("CART_PRIVILEGE");
      //Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

      createRoleIfNotFound("ROLE_ADMIN", Arrays.asList(createEmpPrivilege));
      createRoleIfNotFound("ROLE_EMPLOYEE", Arrays.asList(createBookPrivilege));
      createRoleIfNotFound("ROLE_USER", Arrays.asList(cartPrivilege));

      User admin = new User();
      admin.setFirstName("Admin");
      admin.setLastName("Admin");
      admin.setEmail("admin");
      admin.setPassword(passwordEncoder.encode("passwordAdmin"));
      admin.setRoles(Arrays.asList(rolesRepository.findByName("ROLE_ADMIN")));
      usersRepository.save(admin);

      User employee = new User();
      employee.setFirstName("Employee");
      employee.setLastName("Employee");
      employee.setEmail("employee");
      employee.setPassword(passwordEncoder.encode("passwordEmployee"));
      employee.setRoles(Arrays.asList(rolesRepository.findByName("ROLE_EMPLOYEE")));
      usersRepository.save(employee);

      User user = new User();
      user.setFirstName("User");
      user.setLastName("Userovich");
      user.setEmail("user");
      user.setPassword(passwordEncoder.encode("passwordUser"));
      user.setRoles(Arrays.asList(rolesRepository.findByName("ROLE_USER")));
      usersRepository.save(user);

      alreadySetup = true;
   }

   @Transactional
   private Privilege createPrivilegeIfNotFound(String name) {
      Privilege privilege = privilegesRepository.findByName(name);
      if (privilege == null) {
         privilege = new Privilege();
         privilege.setName(name);
         privilegesRepository.save(privilege);
      }
      return privilege;
   }

   @Transactional
   private Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
      Role role = rolesRepository.findByName(name);
      if (role == null) {
         role = new Role();
         role.setName(name);
         role.setPrivileges(privileges);
         rolesRepository.save(role);
      }
      return role;
   }
}
