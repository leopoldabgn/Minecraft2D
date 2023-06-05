package minecraft2d;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class AppTest {

    @Test
    public void exempleTestClassiqueJunit() {
        assertTrue(true);
    }

    //Exemple pour utiliser asserThat
    @Test
    public void exempleTestAsserThat() {
        int a = 2;
        int b = 3;
        assertThat(a + b).isEqualTo(5);
        assertThat(a + b).isNotEqualTo(6);
        assertThat(b).isGreaterThan(a);
    }

}
