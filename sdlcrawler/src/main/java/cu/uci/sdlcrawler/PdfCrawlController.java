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

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yasser Ganjisaffar [lastname at gmail dot com]
 * @author Yusniel Hidalgo Delgado [yhidalgo86 at gmail dot com]
 */
public class PdfCrawlController {

    private static Logger logger = LoggerFactory.getLogger(PdfCrawlController.class);

    public static void main(String[] args) throws Exception {
        Properties cm = PdfCrawlerConfigManager.getInstance().loadConfigFile();
        long startTime = System.currentTimeMillis();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        int numberOfCrawlers = Integer.parseInt(cm.getProperty("sdlcrawler.NumberOfCrawlers"));
        String pdfFolder = cm.getProperty("sdlcrawler.CrawlPdfFolder");

        CrawlConfig config = new CrawlConfig();

        config.setCrawlStorageFolder(cm.getProperty("sdlcrawler.CrawlStorageFolder"));
        config.setProxyHost(cm.getProperty("sdlcrawler.ProxyHost"));
        config.setProxyPort(Integer.parseInt(cm.getProperty("sdlcrawler.ProxyPort")));
        config.setProxyUsername(cm.getProperty("sdlcrawler.ProxyUser"));
        config.setProxyPassword(cm.getProperty("sdlcrawler.ProxyPass"));
        config.setMaxDownloadSize(Integer.parseInt(cm.getProperty("sdlcrawler.MaxDownloadSize")));
        config.setIncludeBinaryContentInCrawling(Boolean.parseBoolean(cm.getProperty("sdlcrawler.IncludeBinaryContent")));
        config.setFollowRedirects(Boolean.parseBoolean(cm.getProperty("sdlcrawler.Redirects")));
        config.setUserAgentString(cm.getProperty("sdlcrawler.UserAgent"));
        config.setMaxDepthOfCrawling(Integer.parseInt(cm.getProperty("sdlcrawler.MaxDepthCrawl")));
        config.setMaxConnectionsPerHost(Integer.parseInt(cm.getProperty("sdlcrawler.MaxConnectionsPerHost")));
        config.setSocketTimeout(Integer.parseInt(cm.getProperty("sdlcrawler.SocketTimeout")));
        config.setMaxOutgoingLinksToFollow(Integer.parseInt(cm.getProperty("sdlcrawler.MaxOutgoingLinks")));
        config.setResumableCrawling(Boolean.parseBoolean(cm.getProperty("sdlcrawler.ResumableCrawling")));
        config.setIncludeHttpsPages(Boolean.parseBoolean(cm.getProperty("sdlcrawler.IncludeHttpsPages")));
        config.setMaxTotalConnections(Integer.parseInt(cm.getProperty("sdlcrawler.MaxTotalConnections")));
        config.setMaxPagesToFetch(Integer.parseInt(cm.getProperty("sdlcrawler.MaxPagesToFetch")));
        config.setPolitenessDelay(Integer.parseInt(cm.getProperty("sdlcrawler.PolitenessDelay")));
        config.setConnectionTimeout(Integer.parseInt(cm.getProperty("sdlcrawler.ConnectionTimeout")));

        System.out.println(config.toString());
        Collection<BasicHeader> defaultHeaders=new HashSet<>();
        defaultHeaders.add(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
        defaultHeaders.add(new BasicHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3"));
        defaultHeaders.add(new BasicHeader("Accept-Language", "en-US,en,es-ES,es;q=0.8"));
        defaultHeaders.add(new BasicHeader("Connection", "keep-alive"));
        config.setDefaultHeaders(defaultHeaders);
        
        List<String> list = Files.readAllLines(Paths.get("config/" + cm.getProperty("sdlcrawler.SeedFile")), StandardCharsets.UTF_8);
        String[] crawlDomains = list.toArray(new String[list.size()]);

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        for (String domain : crawlDomains) {
            controller.addSeed(domain);
        }

        PdfCrawler.configure(crawlDomains, pdfFolder);
        controller.start(PdfCrawler.class, numberOfCrawlers);
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date1 = new Date();
        System.out.println(dateFormat1.format(date1));
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total time:" + totalTime);
    }
}
