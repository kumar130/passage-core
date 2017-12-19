import com.nm.logging.util.Util;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

/**
 * Created by kre5335 on 5/24/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class UtilTest {

    private static final String json = "{ \"fid\" : \"xyz\" }";

    @Test
    public void whenStringFindAttributeExists_thanReturnValue() throws IOException {
        MatcherAssert.assertThat(Util.findValue(json, "fid"), CoreMatchers.is("xyz"));
    }

    @Test
    public void whenStringFindAttributeNotExist_thanReturnNull() throws IOException {
        MatcherAssert.assertThat(Util.findValue(json, "blah"), CoreMatchers.is(CoreMatchers.nullValue()));
    }

    @Test
    public void whenByteFindAttributeExists_thanReturnValue() throws IOException {
        MatcherAssert.assertThat(Util.findValue(json.getBytes(), "fid"), CoreMatchers.is("xyz"));
    }
}
