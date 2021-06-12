/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doat.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.TOCReference;
import org.apache.commons.text.StringEscapeUtils;


/**
 *
 * @author Administrator
 */
public class Test {

    private static Book book;
    private static ArrayList<TOCReference> listSec = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        // read epub file
//        EpubReader epubReader = new EpubReader();
//        book = epubReader.readEpub(new FileInputStream("MaPhápCôngNghiệpĐếQuốc_1278C.epub"));
////        book = epubReader.readEpub(new FileInputStream("CựLongVươngTọa_1253C.epub"));
////        readBook(book.getTableOfContents().getTocReferences(), 0);
////        System.out.println(listSec.size());
////        System.out.println(new String(listSec.get(0).getChildren().get(0).getResource().getData()));
//        String html = "<html><head><title>chương 1: đoạt nguyễn</title></head>"
//                + "<body><p>Parsed HTML into a doc.</p></body></html>";
//        Document doc = Jsoup.parse(html);
//        System.out.println(doc.title());
//        String t = "Ch&#432;&#417;ng 4&#160;:&#160;Ti&#7871;t th&#225;o c&#225;i g&#236; &#273;&#237;ch kh&#244;ng mu&#7889;n cu&#771;ng &#273;&#432;&#417;&#803;c<br>Ch&#432;&#417;ng \n" +
//"    4: Ti&#7871;t th&#225;o c&#225;i g&#236; &#273;&#237;ch kh&#244;ng mu&#7889;n cu&#771;ng &#273;&#432;&#417;&#803;c<br><br>&#272;&#7893;i \n" +
//"    m&#7899;i th&#7901;i gian 2013-5-14 22:40:18 s&#7889; l&#432;&#7907;ng t&#7915;: 2429<br><br>&quot;Tri&#7879;u ";
//        String escapedHTML = StringEscapeUtils.escapeHtml4(t);
//
//        System.out.println(escapedHTML);
        for(int i=0;i<0;i++){
            System.out.println("ok");
        }
    }

    private static void readBook(List<TOCReference> toc, int detph) {
        if (toc == null) {
            return;
        }
        for (TOCReference tocReference : toc) {
            StringBuilder tocString = new StringBuilder();
            StringBuilder tocHref = new StringBuilder();
            for (int i = 0; i < detph; i++) {
                tocString.append("\t");
                tocHref.append("\t");
            }
            tocString.append(tocReference.getTitle());
            tocHref.append(tocReference.getCompleteHref());
//            System.out.println("Sub Titles" + tocString.toString());
//            System.out.println("Complete href" + tocHref.toString());
            if (tocReference.getChildren().size() > 1) {
                listSec.add(tocReference);
            }
            readBook(tocReference.getChildren(), detph + 1);
        }
    }
}
