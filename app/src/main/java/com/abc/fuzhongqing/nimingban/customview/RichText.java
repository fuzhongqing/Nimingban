package com.abc.fuzhongqing.nimingban.customview;

import java.util.regex.Pattern;

/**
 * Created by fuzhongqing on 16/6/21.
 *
 *
 */
public class RichText extends com.zzhoujay.richtext.RichText {
    private static Pattern IMAGE_TAG_PATTERN = Pattern.compile("\\>\\>d+");
}
