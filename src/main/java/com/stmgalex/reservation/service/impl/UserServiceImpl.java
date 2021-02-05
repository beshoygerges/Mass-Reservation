package com.stmgalex.reservation.service.impl;

import static com.stmgalex.reservation.util.DateUtil.calculateAge;
import static com.stmgalex.reservation.util.DateUtil.getBirthDate;

import com.stmgalex.reservation.dto.UserDto;
import com.stmgalex.reservation.entity.User;
import com.stmgalex.reservation.repository.UserRepository;
import com.stmgalex.reservation.service.UserService;
import com.stmgalex.reservation.util.MapperUtil;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public UserDto register(final UserDto userDto) {
    Optional<User> optionalUser = userRepository.findByNationalId(userDto.getNationalId());

    if (optionalUser.isPresent()) {
      throw new RuntimeException("هذا المستخدم موجود بالفعل");
    }

    userDto.setName(userDto.getName().trim());

    User user = MapperUtil.map(userDto, User.class);

    user.setBirthdate(getBirthDate(user.getNationalId()));

    user.setAge(calculateAge(user.getBirthdate()));

    user = userRepository.save(user);

    return MapperUtil.map(user, UserDto.class);
  }
}
