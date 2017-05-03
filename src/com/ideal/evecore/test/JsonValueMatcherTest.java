import com.ideal.evecore.common.Mapping;
import com.ideal.evecore.universe.matcher.ObjectValueMatcher;
import com.ideal.evecore.universe.matcher.ValueMatcher;
import com.ideal.evecore.universe.matcher.ValueMatcherUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by Christophe on 03/05/2017.
 */
public class JsonValueMatcherTest {
    @Test
    public void testJsonValueMatcherParse() throws IOException {
        File f = new File("receiver.json");
        Mapping<ValueMatcher> mapping = ValueMatcherUtils.parseJson(f);
        System.out.println(mapping);
    }
}
