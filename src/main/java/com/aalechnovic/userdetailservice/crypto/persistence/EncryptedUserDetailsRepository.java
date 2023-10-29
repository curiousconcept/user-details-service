package com.aalechnovic.userdetailservice.crypto.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EncryptedUserDetailsRepository extends CrudRepository<EncryptedUserDetails, Long> {


}
