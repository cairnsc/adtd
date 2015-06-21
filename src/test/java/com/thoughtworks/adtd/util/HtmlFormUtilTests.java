package com.thoughtworks.adtd.util;

import com.thoughtworks.adtd.testutil.FileUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.thoughtworks.adtd.util.HtmlFormUtil.getFormElements;
import static com.thoughtworks.adtd.util.SelectorStringBuilder.elementSelector;
import static org.assertj.core.api.Assertions.assertThat;

public class HtmlFormUtilTests {

    @Test
    public void shouldGetFormElements() throws Exception {
        String body = FileUtil.consumeFile("HtmlFormUtilTests/allFormElements.html");
        Document doc = Jsoup.parse(body);
        Element form = doc.select(elementSelector("form")).get(0);

        Elements formElements = getFormElements(form);

        assertThat(formElements.size()).isEqualTo(HtmlFormUtil.formElements.length);
        List<String> elementNames = getElementNames(formElements);
        assertThat(elementNames).containsAll(Arrays.asList(HtmlFormUtil.formElements));
    }

    private List<String> getElementNames(Elements elements) {
        List<String> list = new ArrayList<String>();
        for (Element element : elements) {
            list.add(element.tagName());
        }
        return list;
    }

}
