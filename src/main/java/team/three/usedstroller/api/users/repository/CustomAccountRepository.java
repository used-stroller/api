package team.three.usedstroller.api.users.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import team.three.usedstroller.api.product.domain.Product;
import team.three.usedstroller.api.product.dto.FilterReq;
import team.three.usedstroller.api.product.dto.ProductRes;
import team.three.usedstroller.api.users.dto.res.MyPageDto;

public interface CustomAccountRepository {

  List<Product> getSellingList();
  List<Product> getFavorites(Long accountId);
}
