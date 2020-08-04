package tacos.web.api;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tacos.Order;
import tacos.data.OrderRepository;

@RestController
@RequestMapping(path = "/orders", produces = {"application/json", "text/xml"})
@CrossOrigin(origins = "*")
public class OrderRestController {
	private OrderRepository orderRepo;

	@PutMapping("/{orderId}")
	public Order putOrder(@PathVariable("orderId") Long id, @RequestBody Order order) {
		return orderRepo.save(order);
	}

	@PatchMapping(path = "/{orderId}", consumes = "application/json")
	public Order patchOrder(@PathVariable("orderId") Long id, @RequestBody Order patch) {
		Order order = orderRepo.findById(id).get();
		if(patch.getDeliveryName() != null) {
			order.setDeliveryName(patch.getDeliveryName());
		}
		if(patch.getDeliveryCity() != null) {
			order.setDeliveryCity(patch.getDeliveryCity());
		}
		if(patch.getDeliveryDistrict() != null) {
			order.setDeliveryDistrict(patch.getDeliveryDistrict());
		}
		if(patch.getDeliveryStreet() != null) {
			order.setDeliveryStreet(patch.getDeliveryStreet());
		}
		if(patch.getDeliveryDetail() != null) {
			order.setDeliveryDetail(patch.getDeliveryDetail());
		}

		return orderRepo.save(order);
	}

	@DeleteMapping("/{orderId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteOrder(@PathVariable("orderId") Long id) {
		try {
			orderRepo.deleteById(id);
		} catch(EmptyResultDataAccessException e) {}
	}
}
