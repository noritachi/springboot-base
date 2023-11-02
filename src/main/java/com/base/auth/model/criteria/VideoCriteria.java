package com.base.auth.model.criteria;

import com.base.auth.model.Video;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Data
public class VideoCriteria {

    private Long id;
    private String title;
    private Integer kind;

    public Specification<Video> getSpecification() {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Video> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }

                if(getKind() != null) {
                    predicates.add(cb.equal(root.get("kind"), getKind()));
                }

                if(getTitle() != null) {
                    predicates.add(cb.like(root.get("title"), "%" + getTitle().toLowerCase() + "%"  ));
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
