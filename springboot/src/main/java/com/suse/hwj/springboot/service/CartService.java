package com.suse.hwj.springboot.service;

import com.suse.hwj.springboot.entity.Cart;
import com.suse.hwj.springboot.mapper.CartMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CartService {

    @Resource
    private CartMapper cartMapper;

    public void addToCart(Cart cart){
        cartMapper.addToCart(cart);
    }

    public List<Cart> getCartList(Long userId){
        return cartMapper.getCartList(userId);
    }

    public void removeFromCart(Long cartId){
        cartMapper.removeFromCart(cartId);
    }
}
