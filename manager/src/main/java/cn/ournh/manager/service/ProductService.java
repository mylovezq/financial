package cn.ournh.manager.service;

import cn.ournh.entity.Product;
import cn.ournh.entity.enums.ProductStatus;
import cn.ournh.manager.erroer.ErrorEnum;
import cn.ournh.manager.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//产品服务类
@Service
public class ProductService {
    private static Logger LOG = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(Product product) {
        LOG.debug("创建产品，参数{}", product);
        //数据校验
        checkProduct(product);
        //设置默认值

        setDefault(product);
        Product result = productRepository.save(product);
        LOG.debug("创建产品,结果{}", result);
        return result;
    }


    //设置默认值 创建、更新时间，投资步长
    private void setDefault(Product product) {
        if (product.getCreateAt() == null) {
            product.setCreateAt(new Date());
        }
        if (product.getUpdateAt() == null) {
            product.setUpdateAt(new Date());
        }
        if (product.getStepAmount() == null) {
            product.setStepAmount(BigDecimal.ZERO);
        }
        if (product.getLockTerm() == null) {
            product.setLockTerm(0);
        }
        if (product.getStatus() == null) {
            product.setStatus(ProductStatus.AUDITING.name());
        }
    }

    //产品数据检验
    private void checkProduct(Product product) {
        Assert.notNull(product.getId(), ErrorEnum.ID_NOT_NULL.getMessage());
        Assert.isTrue(BigDecimal.ZERO.compareTo(product.getRewardRate()) < 0
                && BigDecimal.valueOf(30).compareTo(product.getRewardRate()) >= 0, "收益率范围错误");

        Assert.isTrue(BigDecimal.valueOf(product.getStepAmount().longValue()).compareTo(product.getStepAmount()) == 0, "投资步长要为整数");
    }

    //根据ID查找
    public Product findOne(String id) {
        Assert.notNull(id, ErrorEnum.ID_NOT_NULL.getCode());
        LOG.debug("查询单个产品，id{}", id);

        Product product = productRepository.getOne(id);

        LOG.debug("查询单个产品，产品为{}", product);
        return product;
    }


    //分页查询
    public Page<Product> query(List<String> idList,
                               BigDecimal minRewardRate,
                               BigDecimal maxRewardRate,
                               List<String> statusList,
                               Pageable pageable) {

        LOG.debug("查询产品，idList={},minRewardRate={},maxRewardRate={},statusList={},pageable={}",
                idList, minRewardRate, maxRewardRate, statusList, pageable);
        Specification<Product> specification = new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Expression<String> idCol = root.get("id");
                Expression<BigDecimal> rewardRateCol = root.get("rewardRate");
                Expression<String> statusCol = root.get("status");

                List<Predicate> predicates = new ArrayList<>();

                if (idList != null && idList.size() > 0) {
                    predicates.add(idCol.in(idList));
                }
                if (BigDecimal.ZERO.compareTo(minRewardRate) < 0) {
                    predicates.add(criteriaBuilder.ge(rewardRateCol, minRewardRate));
                }

                if (BigDecimal.ZERO.compareTo(maxRewardRate) < 0) {
                    predicates.add(criteriaBuilder.le(rewardRateCol, maxRewardRate));
                }
                if (statusList != null && statusList.size() > 0) {
                    predicates.add(statusCol.in(statusCol));
                }
                query.where(predicates.toArray(new Predicate[0]));
                return null;
            }
        };

        Page<Product> page = productRepository.findAll(specification, pageable);
        LOG.debug("查询产品，结果={}",page);
        return page;
    }
}
