package se.magmag.oauth.service;

import se.magmag.oauth.persistence.DbFactory;
import se.magmag.oauth.persistence.entity.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by magnus.eriksson on 12/07/16.
 */

@Stateless
public class UserService {

    @Inject @DbFactory.Database
    private EntityManager em;

    /**
     * Creates and returns a new user that "managed", i.e. currently associated with a transaction
     */
    public User createUser() {
        User user = new User();
        em.persist(user);
        return user;
    }

    public User getUserBySessionId(String sessionId) {
        List userList = em.createQuery("SELECT u FROM User u WHERE u.sessionId =:sid")
                .setParameter("sid", sessionId)
                .getResultList();
        return getUserFromResultList(userList);
    }


    public User getUserByGoogleId(String googleId) {
        List userList = em.createQuery("SELECT u FROM User u WHERE u.googleId =:gid")
                .setParameter("gid", googleId)
                .getResultList();
        return getUserFromResultList(userList);
    }

    private User getUserFromResultList(List userList) {
        return userList.isEmpty() ? null : (User) userList.get(0);
    }
}
