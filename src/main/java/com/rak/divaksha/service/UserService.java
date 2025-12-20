package com.rak.divaksha.service;

import com.rak.divaksha.dto.ReferralTreeNode;
import com.rak.divaksha.entity.CommissionLedger;
import com.rak.divaksha.entity.User;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;

public interface UserService {

	
	public User registerUser(String username, Long parentId);

	public List<User> getAllUsers() ;

	public Optional<User> getUserById(Long id) ;

	public Page<CommissionLedger> getLedgerCommissionByUser(Long userId, int page, int size);

	public ReferralTreeNode buildTree(Long userId, int level) ;

	public void markUserInactive(Long userId) ;

	public void reactivateUser(Long userId) ;

	public void updateLastSaleAt(Long userId) ;
}

