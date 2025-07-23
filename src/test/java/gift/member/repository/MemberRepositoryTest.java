package gift.member.repository;

import gift.member.entity.Member;
import gift.member.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository members;

    @Test
    void save() {
        Member expected = new Member("솨야", "psh@test.com", "1234", Role.USER);
        Member actual = members.save(expected);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo("솨야"),
                () -> assertThat(actual.getEmail()).isEqualTo("psh@test.com"),
                () -> assertThat(actual.getPassword()).isEqualTo("1234")
        );
    }

    @Test
    void findByEmail() {
        String email = "psh@test.com";
        members.save(new Member("솨야", email, "1234", Role.ADMIN));

        Member result = members.findByEmail(email).orElseThrow();
        assertThat(result.getEmail()).isEqualTo(email);
    }

    @Test
    void update() {
        Member member = members.save(new Member("솨야", "psh@test.com", "1234", Role.USER));
        member.update("박수화", "psh_update@test.com", "5678");

        Member updated = members.findById(member.getId()).orElseThrow();
        assertAll(
                () -> assertThat(updated.getName()).isEqualTo("박수화"),
                () -> assertThat(updated.getEmail()).isEqualTo("psh_update@test.com"),
                () -> assertThat(updated.getPassword()).isEqualTo("5678")
        );
    }

    @Test
    void delete() {
        Member member = members.save(new Member("솨야", "psh@test.com", "1234", Role.USER));
        members.delete(member);

        assertThat(members.findById(member.getId())).isEmpty();
    }
}
