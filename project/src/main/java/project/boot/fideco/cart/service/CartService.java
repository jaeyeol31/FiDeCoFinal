package project.boot.fideco.cart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.boot.fideco.cart.entity.CartEntity;
import project.boot.fideco.cart.entity.CartItemEntity;
import project.boot.fideco.cart.repository.CartItemRepository;
import project.boot.fideco.cart.repository.CartRepository;
import project.boot.fideco.product.entity.ProductEntity;
import project.boot.fideco.product.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;

	@Autowired
	public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
			ProductRepository productRepository) {
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
		this.productRepository = productRepository;
	}

	@Transactional(readOnly = true)
	public Optional<CartEntity> getCartByMemberId(String memberId) {
		return cartRepository.findByMemberId(memberId);
	}

	@Transactional
	public void addToCart(String memberId, String productId, int quantity) {
		CartEntity cart = cartRepository.findByMemberId(memberId).orElseGet(() -> {
			CartEntity newCart = new CartEntity();
			newCart.setMemberId(memberId);
			return cartRepository.save(newCart);
		});

		Optional<CartItemEntity> existingItem = cart.getCartItems().stream()
				.filter(item -> item.getProduct().getProductId().equals(productId)).findFirst();

		if (existingItem.isPresent()) {
			CartItemEntity cartItem = existingItem.get();
			cartItem.setQuantity(cartItem.getQuantity() + quantity);
		} else {
			CartItemEntity newItem = new CartItemEntity();
			ProductEntity product = productRepository.findById(productId)
					.orElseThrow(() -> new IllegalArgumentException("Product not found"));
			newItem.setProduct(product);
			newItem.setQuantity(quantity);
			cart.addCartItem(newItem);
		}

		cartRepository.save(cart);
	}

	@Transactional
	public void updateCartItemQuantity(Long itemId, Integer quantity) {
		cartItemRepository.findById(itemId).ifPresent(cartItem -> {
			cartItem.setQuantity(quantity);
			cartItemRepository.save(cartItem);
		});
	}

	@Transactional(readOnly = true)
	public List<CartEntity> getAllCarts() {
		return cartRepository.findAll();
	}

	@Transactional
	public void deleteCartItem(Long cartItemId) {
		cartItemRepository.deleteById(cartItemId);
	}

	@Transactional
	public void clearCart(String memberId) {
		Optional<CartEntity> cart = cartRepository.findByMemberId(memberId);
		if (cart.isPresent()) {
			CartEntity cartEntity = cart.get();
			List<CartItemEntity> cartItems = cartEntity.getCartItems();
			if (!cartItems.isEmpty()) {
				cartItems.forEach(cartItem -> {
					cartItem.setCart(null);
					cartItemRepository.delete(cartItem);
				});
			}
		}
	}
}