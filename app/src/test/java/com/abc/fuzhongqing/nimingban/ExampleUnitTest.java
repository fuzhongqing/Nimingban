package com.abc.fuzhongqing.nimingban;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void pattern_test() throws Exception {
        Pattern IMAGE_SRC_PATTERN = Pattern.compile("\\>\\>\\d+");
        String Source = ">>123 fhjasdkfh >>345324 fdjkhask fakhsdfkj jhkfda > dfhkj >> ";
        Matcher mMahter = IMAGE_SRC_PATTERN.matcher(Source);
        while (mMahter.find()) {
            assertEquals(mMahter.group().trim(),">>123");
        }
    }
}