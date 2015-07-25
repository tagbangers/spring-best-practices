package practice.repository;

import org.springframework.data.repository.CrudRepository;
import practice.entity.Item;

public interface ItemRepository extends CrudRepository<Item, Long>, ItemRepositoryCustom {
}
