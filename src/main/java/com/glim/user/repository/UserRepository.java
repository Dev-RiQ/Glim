package com.glim.user.repository;

import com.glim.user.domain.PlatForm;
import com.glim.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    User findUserById(Long id);

    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);
    List<User> findTop20ByNicknameContainingIgnoreCase(String keyword);

    List<User> findAllByPhone(String phone);

    Optional<User> findByPhone(String phone);

    List<User> findAllByPhoneAndPlatForm(String phone, PlatForm platForm);

}
