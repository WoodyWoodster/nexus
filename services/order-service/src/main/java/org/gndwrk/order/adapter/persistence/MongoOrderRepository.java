package org.gndwrk.order.adapter.persistence;

import org.gndwrk.order.domain.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoOrderRepository extends MongoRepository<Order, String> {}
