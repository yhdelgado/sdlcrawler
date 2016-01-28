/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cu.uci.sdlcrawler;

import com.google.common.io.Files;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author Yasser Ganjisaffar [lastname at gmail dot com]
 * @author Yusniel Hidalgo Delgado [yhidalgo86 at gmail dot com]
 */

/*
 * This class shows how you can crawl images on the web and store them in a
 * folder. This is just for demonstration purposes and doesn't scale for large
 * number of images. For crawling millions of images you would need to store
 * downloaded images in a hierarchy of folders
 */
public class PdfCrawler extends WebCrawler {

    private static final Pattern filters = Pattern.compile(".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v"
            + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
    private static final Pattern excludes = Pattern.compile("[?*!@=]");

    private static File storageFolder;
    private static String[] crawlDomains;

    public static void configure(String[] domain, String storageFolderName) {
        PdfCrawler.crawlDomains = domain;

        storageFolder = new File(storageFolderName);
        if (!storageFolder.exists()) {
            storageFolder.mkdirs();
        }
    }

    @Override
    public boolean shouldVisit(Page page, WebURL url) {
        String href = url.getURL().toLowerCase();
        
        if (filters.matcher(href).matches()) {
            return false;
        }
//        if (excludes.matcher(href).matches()) {
//            return false;
//        }

        if (page.getContentType().equals("application/pdf")) {
            return true;
        }

        for (String domain : crawlDomains) {
            if (href.startsWith(domain)) {
                System.out.println("Este es el href "+href);
                return true;
            }
        }
        return false;
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        if (page.getStatusCode() == 200) {
            System.out.println(page.getContentType() + " -- " + page.getStatusCode() + " -- " + url);
        }

        if (!(page.getContentType().equals("application/pdf")) || !(page.getParseData() instanceof BinaryParseData)) {
            return;
        }

        String extension = ".pdf";
        String hashedName = UUID.randomUUID().toString() + extension;

        String filename = storageFolder.getAbsolutePath() + "/" + hashedName;
        try {
            Files.write(page.getContentData(), new File(filename));
        } catch (IOException iox) {
            System.out.println(iox.getMessage());
        }
    }
}
