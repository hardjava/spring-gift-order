package study;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class StationRepositoryTest {
    @Autowired
    private StationRepository stations;

    @Test
    void save() {
        var station = new Station("잠실역");
        assertThat(station.getId()).isNull();
        var actual = stations.save(station);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isNotNull();
    }

    @Test
    void findByName() {
        var actual1 = stations.findByName("잠실역");
        assertThat(actual1).isEmpty();
        stations.save(new Station("잠실역"));
        var actual2 = stations.findByName("잠실역");
        assertThat(actual2).isNotEmpty();
    }

    @Test
    void identity1() {
        var station1 = stations.save(new Station("잠실역"));
        var station2 = stations.findById(station1.getId()).get();
        assertThat(station1).isSameAs(station2);
    }

    @Test
    void identity2() {
        var station1 = stations.save(new Station("잠실역"));
        var station2 = stations.findByName("잠실역").get();
        assertThat(station1).isSameAs(station2);
    }

//    @Test
//    void lazy() {
//        var station = new Station(1L, "잠실역");
//        var actual = stations.save(station);
//        assertThat(actual).isNotNull();
//    }

    @Test
    void update() {
        var station1 = stations.save(new Station("잠실역"));
        station1.changeName("몽촌토성역");
        var station2 = stations.findByName("몽촌토성역");
        assertThat(station2).isNotEmpty();
    }
}
