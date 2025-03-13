package kma.ktlt.post.domain.notification.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TokenDeviceFirebaseRepository extends JpaRepository<TokenDeviceFirebase,  Long> {

    @Modifying
    @Query("""
    UPDATE TokenDeviceFirebase tk
    SET tk.tokenDevice = :tokenDevice
    WHERE tk.userId = :userId
""")
    void setTokenDeviceFirebase(@Param("tokenDevice") String tokenDevice, @Param("userId") String userId);


    @Query("""
    SELECT t FROM TokenDeviceFirebase t WHERE t.userId = :userId
""")
    TokenDeviceFirebase findTokenOfUser(@Param("userId") String userId);

    @Query("""
    SELECT t FROM TokenDeviceFirebase t
    WHERE t.userId IN (:userId)
""")
    List<TokenDeviceFirebase> findByListUserId(@Param("userId") List<String> userId);

}
