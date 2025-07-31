package gift.domain;

import gift.enums.OauthProvider;
import gift.enums.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "member",
        uniqueConstraints = {
                @UniqueConstraint(name = "kakaoId_oauthProvider", columnNames = {"oauth_id", "oauth_provider"})
        })
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long oauthId;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.ROLE_USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OauthProvider oauthProvider = OauthProvider.NONE;

    public Member(Long id, String email, String password, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Member(String email, String password, Role role) {
        this(null, email, password, role);
    }

    public Member(Long oauthId, OauthProvider oauthProvider) {
        this.oauthId = oauthId;
        this.oauthProvider = oauthProvider;
    }

    protected Member() {
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Long getOauthId() {
        return oauthId;
    }

    public OauthProvider getOauthProvider() {
        return oauthProvider;
    }

    public Role getRole() {
        return role;
    }
}
