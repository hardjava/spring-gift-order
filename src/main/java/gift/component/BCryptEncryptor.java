package gift.component;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BCryptEncryptor {
    public String encode(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean isMatch(String enterPassword, String hashedPassword) {
        return BCrypt.checkpw(enterPassword, hashedPassword);
    }
}
