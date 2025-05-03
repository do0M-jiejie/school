package com.suse.hwj.springboot.mapper;

import com.suse.hwj.springboot.entity.Cart;

import java.util.List;

public interface CartMapper {

    void addToCart(Cart cart);

    List<Cart> getCartList(Long userId);

    void removeFromCart(Long cartId);
}
