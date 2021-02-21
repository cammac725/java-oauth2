package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplication;
import com.lambdaschool.usermodel.exceptions.ResourceNotFoundException;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceImplTest {


    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void b_findUserById() {
        assertEquals("admin", userService.findUserById(4).getUsername());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void ba_findUserByIdNotFound() {
        assertEquals("admin", userService.findUserById(10).getUsername());
    }

    @Test
    public void c_findAll() {
        assertEquals(5, userService.findAll().size());
    }

    @Test
    public void d_delete() {
        userService.delete(13);
        assertEquals(4, userService.findAll().size());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void da_notFoundDelete() {
        userService.delete(100);
        assertEquals(4, userService.findAll().size());
    }

    @Test
    public void a_findByName() {
        assertEquals("admin", userService.findByName("admin").getUsername());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void aa_findUserByNameNotFound() {
        assertEquals("admin", userService.findByName("Jojo").getUsername());
    }

    @Test
    public void ab_findByNameContaining() {
        assertEquals(4, userService.findByNameContaining("a").size());
    }

    @Test
    public void f_save() {
        Role r2 = new Role("user");
        r2.setRoleid(2);

        User u2 = new User("tiger", "ILuvM4th!", "tiger@school.lambda");
        u2.getRoles().add(new UserRoles(u2, r2));
        u2.getUseremails().add(new Useremail(u2, "tiger@tiger.local"));

        User saveu2 = userService.save(u2);

        System.out.println("*** DATA ***");
        System.out.println(saveu2);
        System.out.println("*** DATA ***");

        assertEquals("tiger@tiger.local", saveu2.getUseremails()
                .get(0)
                .getUseremail());
    }

    @Transactional
    // only useful when we have users in SeedData
    @WithUserDetails("cinnamon")
    @Test
    public void g_update() {
        Role r2 = new Role("user");
        r2.setRoleid(2);

        User u2 = new User("cinnamon", "password", "cinnamon@school.lambda");
        u2.getRoles().add(new UserRoles(u2, r2));
        u2.getUseremails().add(new Useremail(u2, "cinnamon@mymail.thump"));
        u2.getUseremails().add(new Useremail(u2, "hops@mymail.thump"));
        u2.getUseremails().add(new Useremail(u2, "bunny@email.thump"));

        User updatedu2 = userService.update(u2, 7);
        System.out.println("*** DATA ***");
        System.out.println(updatedu2);
        System.out.println("*** DATA ***");

        int checking = updatedu2.getUseremails().size() - 1;
        assertEquals("bunny@email.thump", updatedu2.getUseremails()
                .get(checking)
                .getUseremail());
    }

    @Transactional
    // only useful when we have users in SeedData
    @WithUserDetails("cinnamon")
    @Test
    public void ga_updateNotCurrentUserNorAdmin() {
        Role r2 = new Role("user");
        r2.setRoleid(2);

        User u2 = new User("cinnamon", "password", "cinnamon@school.lambda");
        u2.getRoles().add(new UserRoles(u2, r2));
        u2.getUseremails().add(new Useremail(u2, "cinnamon@mymail.thump"));
        u2.getUseremails().add(new Useremail(u2, "hops@mymail.thump"));
        u2.getUseremails().add(new Useremail(u2, "bunny@email.thump"));

        User updatedu2 = userService.update(u2, 8);
        System.out.println("*** DATA ***");
        System.out.println(updatedu2);
        System.out.println("*** DATA ***");

        int checking = updatedu2.getUseremails().size() - 1;
        assertEquals("bunny@email.thump", updatedu2.getUseremails()
                .get(checking)
                .getUseremail());
    }

    @Test
    public void deleteAll() {
    }
}