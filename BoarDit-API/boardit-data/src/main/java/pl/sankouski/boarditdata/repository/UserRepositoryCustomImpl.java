package pl.sankouski.boarditdata.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import pl.sankouski.boarditdata.model.user.Role;
import pl.sankouski.boarditdata.model.user.User;

import java.util.List;

@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> filterUser(String filterStr) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> userRoot = query.from(User.class);
        Join<User, Role> rolesJoin = userRoot.join("roles", JoinType.LEFT);


        Predicate firstNamePredicate = cb.like(cb.lower(userRoot.get("firstName")), "%" + filterStr.toLowerCase() + "%");
        Predicate lastNamePredicate = cb.like(cb.lower(userRoot.get("lastName")), "%" + filterStr.toLowerCase() + "%");
        Predicate middleNamePredicate = cb.like(cb.lower(userRoot.get("middleName")), "%" + filterStr.toLowerCase() + "%");
        Predicate loginPredicate = cb.like(cb.lower(userRoot.get("login")), "%" + filterStr.toLowerCase() + "%");
        Predicate rolePredicate = cb.like(cb.lower(rolesJoin.get("pid")), "%" + filterStr.toLowerCase() + "%");


        Predicate combinedPredicate = cb.or(firstNamePredicate, lastNamePredicate, middleNamePredicate, loginPredicate, rolePredicate);


        query.select(userRoot).where(combinedPredicate).distinct(true);

        return em.createQuery(query).getResultList();
    }
}