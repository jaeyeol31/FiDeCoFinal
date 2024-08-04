package project.boot.fideco.cart.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "cart")
public class CartEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cart_id")
	private Long cartId; // Auto Increment ID

	@Column(name = "member_id", nullable = false, unique = true)
	private String memberId; // Each member has one cart

	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CartItemEntity> cartItems = new ArrayList<>();

	public void addCartItem(CartItemEntity cartItem) {
		cartItems.add(cartItem);
		cartItem.setCart(this);
	}

	public void removeCartItem(CartItemEntity cartItem) {
		cartItems.remove(cartItem);
		cartItem.setCart(null);
	}
}
