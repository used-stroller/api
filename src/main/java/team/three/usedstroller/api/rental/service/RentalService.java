package team.three.usedstroller.api.rental.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import team.three.usedstroller.api.common.jwt.JwtTokenProvider;
import team.three.usedstroller.api.common.utils.SecurityUtil;
import team.three.usedstroller.api.error.ApiErrorCode;
import team.three.usedstroller.api.error.ApiException;
import team.three.usedstroller.api.product.domain.FavoriteEntity;
import team.three.usedstroller.api.product.domain.Product;
import team.three.usedstroller.api.product.dto.res.ProductDto;
import team.three.usedstroller.api.product.repository.ProductRepository;
import team.three.usedstroller.api.rental.dto.RentalDto;
import team.three.usedstroller.api.rental.repository.RentalRepositoryImpl;
import team.three.usedstroller.api.users.dto.AccountDto;
import team.three.usedstroller.api.users.dto.LoginWrapperDto;
import team.three.usedstroller.api.users.dto.ResponseLoginDto;
import team.three.usedstroller.api.users.dto.ResponseLoginTokenDto;
import team.three.usedstroller.api.users.dto.ResultDto;
import team.three.usedstroller.api.users.dto.res.MyPageDto;
import team.three.usedstroller.api.users.entity.Account;
import team.three.usedstroller.api.users.repository.AccountRepository;
import team.three.usedstroller.api.users.repository.FavoriteRepository;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RentalService {

  private final RentalRepositoryImpl rentalRepositoryImpl;

  public List<RentalDto> getRentalList(Pageable pageable) {
    rentalRepositoryImpl.getRentalList(pageable);
  }
}
