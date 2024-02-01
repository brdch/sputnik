package pl.touk.sputnik.review;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import pl.touk.sputnik.configuration.Configuration;
import pl.touk.sputnik.configuration.GeneralOption;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReviewFormatterTest {

    @Mock
    private ReviewFile file1;

    @Mock
    private ReviewFile file2;

    @Test
    void shouldStringifyToEmptyStringWhenObjectIsNull() {
        Configuration configMock = mock(Configuration.class);
        when(configMock.getProperty(GeneralOption.MESSAGE_COMMENT_FORMAT)).thenReturn("{0}{1}{2}");
        when(configMock.getProperty(GeneralOption.MESSAGE_PROBLEM_FORMAT)).thenReturn("{0}{1}");

        ReviewFormatter formatter = new ReviewFormatter(configMock);
        String result = formatter.stringify(null);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldStringifyObjectToStringRepresentation() {
        Configuration configMock = mock(Configuration.class);
        when(configMock.getProperty(GeneralOption.MESSAGE_COMMENT_FORMAT)).thenReturn("{0}{1}");
        when(configMock.getProperty(GeneralOption.MESSAGE_PROBLEM_FORMAT)).thenReturn("{0}{1}");

        ReviewFormatter formatter = new ReviewFormatter(configMock);
        Review review = new Review(asList(file1, file2), formatter);
        String result = formatter.stringify(review);

        assertThat(result).isEqualTo(review.toString());
    }
    
    @Test
    void shouldFormatProblemsAndComments() {
        Configuration configMock = mock(Configuration.class);
        when(configMock.getProperty(GeneralOption.MESSAGE_COMMENT_FORMAT)).thenReturn("{0}{1}{2}");
        when(configMock.getProperty(GeneralOption.MESSAGE_PROBLEM_FORMAT)).thenReturn("{0}{1}");

        ReviewFormatter formatter = new ReviewFormatter(configMock);

        assertThat(formatter.formatComment("source/", Severity.ERROR, "/message")).isEqualTo("source/ERROR/message");
        assertThat(formatter.formatProblem("source/", "message")).isEqualTo("source/message");
    }

    @Test
    void shouldPartiallyFormat() {
        Configuration configMock = mock(Configuration.class);
        when(configMock.getProperty(GeneralOption.MESSAGE_COMMENT_FORMAT)).thenReturn("{0}{2}");
        when(configMock.getProperty(GeneralOption.MESSAGE_PROBLEM_FORMAT)).thenReturn("{0}{1}");

        ReviewFormatter formatter = new ReviewFormatter(configMock);

        assertThat(formatter.formatComment("source/", Severity.ERROR, "message")).isEqualTo("source/message");
        assertThat(formatter.formatProblem("source/", null)).isEqualTo("source/");
    }
}
