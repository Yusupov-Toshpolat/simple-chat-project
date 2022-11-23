package uz.nt.simplechatproject.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.nt.simplechatproject.dto.ResponseDTO;
import uz.nt.simplechatproject.dto.UserDTO;
import uz.nt.simplechatproject.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @ApiOperation(value = "Registration of user")
    @PostMapping("/add")
    public ResponseDTO<?> register(@RequestBody @Valid UserDTO userDTO){
        return userService.addUser(userDTO);
    }

    @ApiOperation(value = "Get token with username and password. Token expires in 3 hours")
    @PostMapping("/token")
    public ResponseDTO<String> jwt(@RequestBody UserDTO userDTO, HttpServletRequest request){
        return userService.generateJWT(userDTO, request);
    }

    @ApiOperation(value = "Get user info with chats by user ID")
    @GetMapping("/{userId}")
    public ResponseDTO<UserDTO> getUserById(@PathVariable Integer userId){
        return userService.getById(userId);
    }
}
