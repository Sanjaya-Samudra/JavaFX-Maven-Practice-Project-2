package repository.custom;

import entity.CustomerEntity;
import repository.CrudRepository;
import repository.SuperDao;

public interface CustomerDao extends CrudRepository<CustomerEntity> {
}