import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.nm.logging.util.SafeStringUtils;

@RunWith(MockitoJUnitRunner.class)
public class SafeStringUtilsTest {

    @Test
    public void testSanitize() throws Exception {
        String inputText = "xyz " + "\n";
        String expected = "xyz ";

        String actual = SafeStringUtils.sanitize(inputText);

        assertEquals(expected, actual);
    }

    @Test
    public void testSanitize_validInput() throws Exception {
        String inputText = "xyz ";
        String expected = "xyz ";

        String actual = SafeStringUtils.sanitize(inputText);

        assertEquals(expected, actual);
    }

    @Test
    public void testSanitize_nullInput() throws Exception {
        String inputText = null;
        String expected = null;

        String actual = SafeStringUtils.sanitize(inputText);

        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveNonPrintable() throws Exception {
        String inputText = "xyz " + "\n";
        String expected = "xyz ";

        String actual = SafeStringUtils.removeNonPrintable(inputText);

        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveNonPrintable_validInput() throws Exception {
        String inputText = "xyz ";
        String expected = "xyz ";

        String actual = SafeStringUtils.removeNonPrintable(inputText);

        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveNonPrintable_nullInput() throws Exception {
        String inputText = null;
        String expected = null;

        String actual = SafeStringUtils.removeNonPrintable(inputText);

        assertEquals(expected, actual);
    }

    @Test
    public void testReplaceNonPrintable() throws Exception {
        String inputText = "xyz " + "\n";
        String expected = "xyz  ";

        String actual = SafeStringUtils.replaceNonPrintable(inputText);

        assertEquals(expected, actual);
    }

    @Test
    public void testReplaceNonPrintable_validInput() throws Exception {
        String inputText = "xyz ";
        String expected = "xyz ";

        String actual = SafeStringUtils.replaceNonPrintable(inputText);

        assertEquals(expected, actual);
    }

    @Test
    public void testReplaceNonPrintable_nullInput() throws Exception {
        String inputText = null;
        String expected = null;

        String actual = SafeStringUtils.replaceNonPrintable(inputText);

        assertEquals(expected, actual);
    }
    
    @Test
    public void testReplaceNonPrintable_tabCheck() throws Exception {
        String inputText = "xyz " + "\t" + "\027";
        String expected = "xyz   ";

        String actual = SafeStringUtils.replaceNonPrintable(inputText);

        assertEquals(expected, actual);
    }
    
    @Test
    public void testRemoveNonPrintable_returnCheck() throws Exception {
        String inputText = "xyz " + "\r";
        String expected = "xyz ";

        String actual = SafeStringUtils.removeNonPrintable(inputText);

        assertEquals(expected, actual);
    }
    
    @Test
    public void testRemoveNonPrintable_thenReturn() throws Exception {
        String inputText = "xyz " + "\027";
        String expected = "xyz ";

        String actual = SafeStringUtils.removeNonPrintable(inputText);

        assertEquals(expected, actual);
    }
    
    @Test
    public void testRemoveNonPrintable_formFeedCheck() throws Exception {
        String inputText = "xyz " + "\f";
        String expected = "xyz ";

        String actual = SafeStringUtils.removeNonPrintable(inputText);

        assertEquals(expected, actual);
    }

    @Test
    public void testReplaceNonPrintable_returnBackSpaceCheck() throws Exception {
        String inputText = "xyz " + "\t" + "\b";
        String expected = "xyz   ";

        String actual = SafeStringUtils.replaceNonPrintable(inputText);

        assertEquals(expected, actual);
    }
}
