package com.suse.hwj.springboot.controller;

import com.suse.hwj.springboot.common.Result;
import com.suse.hwj.springboot.entity.Cart;
import com.suse.hwj.springboot.service.CartService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Resource
    private CartService cartService;

    @PostMapping("/add")
    public Result addToCart(@RequestBody Cart cart) {
        cartService.addToCart(cart);
        return Result.success();
    }

    @GetMapping("/list/{userId}")
    public Result getCartList(@PathVariable Long userId) {
        List<Cart> cartList = cartService.getCartList(userId);
        return Result.success(cartList);
    }

    @DeleteMapping("/remove/{cartId}")
    public Result removeFromCart(@PathVariable Long cartId) {
        cartService.removeFromCart(cartId);
        return Result.success();
    }
}
