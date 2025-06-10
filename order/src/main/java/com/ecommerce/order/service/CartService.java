package com.ecommerce.order.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.repository.CartItemRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;

    public boolean saveToCart(String userId, CartItemRequest cartItemRequest) {

        // look for product

        // var productOptional =
        // productRepository.findById(cartItemRequest.getProductId());

        // if (productOptional.isEmpty())
        // return false;

        // var product = productOptional.get();

        // if (product.getStockQuantity() < cartItemRequest.getQuantity())
        // return false;

        // var userOptional = userRepository.findById(Long.valueOf(userId));

        // if (userOptional.isEmpty())
        // return false;

        // var user = userOptional.get();

        var existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, cartItemRequest.getProductId());

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
            existingCartItem.setPrice(BigDecimal.valueOf(1000));
            cartItemRepository.save(existingCartItem);
        } else {
            var cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(cartItemRequest.getProductId());
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(1000));
            cartItemRepository.save(cartItem);
        }

        return true;
    }

    public boolean deleteItemFromCart(String userId, String productId) {

        var cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);
        if (cartItem != null) {

            cartItemRepository.delete(cartItem);
            return true;
        }

        return false;

    }

    public List<CartItem> findAllItemsByUser(String userId) {

        return cartItemRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {

        cartItemRepository.deleteByUserId(userId);
    }

}
