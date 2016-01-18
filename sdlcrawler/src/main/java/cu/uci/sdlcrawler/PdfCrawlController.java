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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yasser Ganjisaffar [lastname at gmail dot com]
 * @author Yusniel Hidalgo Delgado [yhdelgado at uci dot cu]
 */
public class PdfCrawlController {

    private static Logger logger = LoggerFactory.getLogger(PdfCrawlController.class);

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        String rootFolder = "root";
        int numberOfCrawlers = Integer.parseInt("5");
        String storageFolder = "pdf";

        CrawlConfig config = new CrawlConfig();

        config.setCrawlStorageFolder(rootFolder);
        config.setMaxDownloadSize(5242880);
        config.setIncludeBinaryContentInCrawling(true);
        config.setFollowRedirects(true);
        config.setUserAgentString("sdlcrawler");
        config.setProxyHost("localhost");
        config.setProxyPort(3128);
        config.setMaxDepthOfCrawling(4);
        //config.setMaxConnectionsPerHost(5);
        config.setSocketTimeout(0);
        System.out.println(config.toString());
        //config.setMaxOutgoingLinksToFollow(50000);
        //System.out.println(config.getMaxOutgoingLinksToFollow());
        //System.exit(0);

        //String[] crawlDomains = new String[]{"http://cinfo.idict.cu/"};
        //String[] crawlDomains = new String[]{"http://scielo.sld.cu/"};
        String[] crawlDomains = new String[]{
            //"http://cvi.mes.edu.cu/peduniv/index.php/peduniv/", 
        "http://rci.cujae.edu.cu/index.php/rci",
        //"http://censa.mes.edu.cu/index.php/RPV/",
        //"http://publicaciones.uci.cu/index.php/SC",
        //"http://rii.cujae.edu.cu/index.php/revistaind/"
        };
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        for (String domain : crawlDomains) {
            controller.addSeed(domain);
        }

        PdfCrawler.configure(crawlDomains, storageFolder);

        controller.start(PdfCrawler.class, numberOfCrawlers);
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date1 = new Date();
        System.out.println(dateFormat1.format(date1));
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total time:" + totalTime);
    }
}
