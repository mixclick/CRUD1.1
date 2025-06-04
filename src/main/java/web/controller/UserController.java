package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.service.UserService;
import java.util.Comparator;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users")
    public String getAllUsers(ModelMap model){
        List<User> users = userService.getUsers();
        users = users.stream().sorted(Comparator.comparing(User::getId)).toList();
        model.addAttribute("users", users);
        return "users";
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/users";
    }

    @PostMapping("/updateUser/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") User updatedUser) {
        User existingUser = userService.getUserById(id);
        if (existingUser != null) {
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setAge(updatedUser.getAge());
            userService.updateUser(id, existingUser);
        }
        return "redirect:/users";
    }

    @PostMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.removeUserById(id);
        return "redirect:/users";
    }
}