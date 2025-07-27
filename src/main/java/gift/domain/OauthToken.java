package gift.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "oauth_token")
public class OauthToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(unique = true, nullable = false)
    private String accessToken;

    @Column(unique = true, nullable = false)
    private String refreshToken;

    protected OauthToken() {

    }

    public OauthToken(Member member, String accessToken, String refreshToken) {
        this.member = member;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
