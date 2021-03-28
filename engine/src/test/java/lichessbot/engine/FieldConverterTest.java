package lichessbot.engine;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import lichessbot.engine.impl.common.FieldConverter;

public class FieldConverterTest {

  @ParameterizedTest
  @MethodSource("toIndexSource")
  public void testToIndex(String field, int index) {
    assertThat(FieldConverter.toIndex(false,field)).isEqualTo(index);
  }

  @ParameterizedTest
  @MethodSource("toFieldSource")
  public void testToField(int index, String field) {
    assertThat(FieldConverter.toField(index)).isEqualTo(field);
  }

  static Stream<Arguments> toIndexSource() {
    return Stream.of(Arguments.of("a1", 0), //
        Arguments.of("h8", 63), //
        Arguments.of("b2", 9));
  }

  static Stream<Arguments> toFieldSource() {
    return Stream.of(Arguments.of(0, "a1"), //
        Arguments.of(63, "h8"), //
        Arguments.of(9, "b2"));
  }

}
